import React, { Component } from 'react'
import { createRecharge } from '../../util/APIUtils'
import './RechargeNew.css'
import { Form, Button, InputNumber } from 'antd'
import ReactRouterPropTypes from 'react-router-prop-types'
import * as PropTypes from 'prop-types'
import CardSelect from '../components/cardSelect/CardSelect'
const FormItem = Form.Item

class RechargeNew extends Component {
  constructor (props) {
    super(props)
    console.log(this)
    this.state = {
      amount: {
        text: ''
      },
      cardNumber: {
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

  validateUsername = () => {
    return {
      validateStatus: 'success',
      errorMsg: null
    }
  }

  validateAmount = (amount) => {
    if (amount <= 0) {
      return {
        validateStatus: 'error',
        errorMsg: 'Must be greater than 0'
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
      fromCardNumber: this.state.cardNumber.value,
      amount: this.state.amount.value,
      remark: ''
    }

    createRecharge(data)
      .then(response => {
        this.props.history.push('/')
      })
  }

  isFormInvalid = () => {
    const stateKeys = ['amount']

    return stateKeys.filter(p => this.state[p].validateStatus !== 'success').length > 0
  }

  handleAmountOnChange = value => {
    const a = parseFloat(value)
    this.setState({
      amount: {
        value: value,
        ...this.validateAmount(a)
      }
    })
  }

  handleUpdateCard = value => {
    this.setState({
      cardNumber: {
        value: value.cardNumber
      }
    })
  }

  render () {
    const { state } = this
    return (
      <div className='new-poll-container'>
        <h1 className='page-title'>Recharge</h1>
        <div className='new-poll-content'>
          <Form onSubmit={this.handleSubmit} className='create-poll-form'>

            <FormItem
              label='To Card'>
              <CardSelect currentUser={this.props.currentUser} onSelected={this.handleUpdateCard} />
            </FormItem>

            <FormItem
              label='Recharge Amount (CNY)'
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

            <FormItem className='poll-form-row'>
              <Button type='primary'
                htmlType='submit'
                size='large'
                disabled={this.isFormInvalid()}
                className='create-poll-form-button'>Recharge</Button>
            </FormItem>
          </Form>
        </div>
      </div>
    )
  }
}

RechargeNew.propTypes = {
  history: ReactRouterPropTypes.history.isRequired,
  currentUser: PropTypes.object.isRequired
}

export default RechargeNew
