import React, { Component } from 'react'
import { getCardProfile, createTransaction } from '../../util/APIUtils'
import './TransactionNew.css'
import { Form, Input, Button, Icon, InputNumber, Col, notification } from 'antd'
import CardSelect from '../components/cardSelect/CardSelect'
const FormItem = Form.Item
const { TextArea } = Input

class TransactionNew extends Component {
  constructor (props) {
    super(props)
    console.log(this)
    this.state = {
      currentCard: {
        availableBalance: 0,
      },
      toCardData: {
        id: 0,
        username: ''
      },
      amount: {
        text: ''
      },
      toCardNumber: {
        text: ''
      },
      remark: {
        text: ''
      },
      fromCardNumber: {
        text: ''
      }
    }
  }

  handleInputChange = (event, validationFun) => {
    const target = event.target
    const inputName = target.name
    const inputValue = target.value

    this.setState({
      [inputName]: {
        value: inputValue,
        ...validationFun(inputValue)
      }
    })
  }

  validateToCardNumber = () => {
    return {
      validateStatus: 'success',
      errorMsg: null
    }
  }

  validateAmount = (amount) => {
    if (amount > this.state.currentCard.availableBalance || amount < 0) {
      return {
        validateStatus: 'error',
        errorMsg: 'You don\'t have enough money'
      }
    }
    return {
      validateStatus: 'success',
      errorMsg: null
    }
  }

  handleSubmit = (event) => {
    event.preventDefault()
    const data = {
      userId: '0',
      fromCardNumber: this.state.fromCardNumber.value,
      toCardNumber: this.state.toCardNumber.value,
      amount: this.state.amount.value,
      remark: this.state.remark.value
    }

    createTransaction(data)
      .then(response => {
        this.props.history.push('/')
      })
  }

    validateToCardNumberAvailability = () => {
      const toCardNumberValue = this.state.toCardNumber.value
      const toCardNumberValidation = this.validateToCardNumber(toCardNumberValue)

      if (toCardNumberValidation.validateStatus === 'error') {
        this.setState({
          toCardNumber: {
            value: toCardNumberValue,
            ...toCardNumberValidation
          }
        })
        return
      }

      this.setState({
        toCardNumber: {
          value: toCardNumberValue,
          validateStatus: 'validating',
          errorMsg: null
        }
      })

      getCardProfile(toCardNumberValue, false)
        .then(response => {
          this.setState({
            toCardNumber: {
              value: toCardNumberValue,
              validateStatus: 'success',
              errorMsg: null
            },
            toCardData: response
          })
        }).catch(() => {
          this.setState({
            toCardNumber: {
              value: toCardNumberValue,
              validateStatus: 'error',
              errorMsg: 'This card is not existence!'
            },
            toCardData: {
              id: 0
            }
          })
        })
    }

    isFormInvalid = () => {
      const stateKeys = ['amount', 'toCardNumber']

      return stateKeys.filter(p => this.state[p].validateStatus !== 'success').length > 0
    }

    handleToCardNumberOnChange = e => this.handleInputChange(e, this.validateToCardNumber)
    handleRemarkOnChange = e => this.handleInputChange(e, () => {})

    handleAmountOnChange = value => {
      const a = parseFloat(value)
      this.setState({
        amount: {
          value: value,
          ...this.validateAmount(a)
        }
      })
    }

    handleUpdateFromCard = value => {
      this.setState({
        fromCardNumber: {
          value: value.cardNumber
        },
        currentCard: value
      })
    }

    render () {
      const { state } = this
      const { toCardData } = state
      console.log(toCardData)
      const accountDisplay = toCardData.id !== 0 ? `The name of card you just typed: ${toCardData.user.name}` : ''
      return (
        <div className='new-poll-container'>
          <h1 className='page-title'>Create Transaction</h1>
          <div className='new-poll-content'>
            <Form onSubmit={this.handleSubmit} className='create-poll-form'>

              <FormItem
                label='From Card'>
                <CardSelect currentUser={this.props.currentUser} onSelected={this.handleUpdateFromCard} />
              </FormItem>

              <FormItem
                label='To Card'
                extra={accountDisplay}
                validateStatus={this.state.toCardNumber.validateStatus}
                help={this.state.toCardNumber.errorMsg}>
                <Input
                  size='large'
                  name='toCardNumber'
                  autoComplete='off'
                  placeholder='Card Number'
                  value={this.state.toCardNumber.value}
                  prefix={<Icon type='user' style={{ color: 'rgba(0,0,0,.25)' }} />}
                  onBlur={this.validateToCardNumberAvailability}
                  onChange={this.handleToCardNumberOnChange} />
              </FormItem>

              <FormItem
                label='Transfer Amount (CNY)'
                validateStatus={this.state.amount.validateStatus}
                help={this.state.amount.errorMsg}>
                <InputNumber
                  size='large'
                  name='amount'
                  autoComplete='off'
                  defaultValue={'0.00'}
                  formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',').replace(/\.(\d{0,2})\d*/, '.$1')}
                  parser={value => value.replace(/\$\s?|(,*)/g, '')}
                  min={0}
                  step={0.1}
                  value={this.state.amount.value}
                  onChange={this.handleAmountOnChange} />
              </FormItem>

              <FormItem
                label='Remark'
              >
                <TextArea
                  name={'remark'}
                  onChange={this.handleRemarkOnChange}
                  rows='6'
                />
              </FormItem>

              <FormItem className='poll-form-row'>
                <Button type='primary'
                  htmlType='submit'
                  size='large'
                  disabled={this.isFormInvalid()}
                  className='create-poll-form-button'>Create Transaction</Button>
              </FormItem>
            </Form>
          </div>
        </div>
      )
    }
}

export default TransactionNew
