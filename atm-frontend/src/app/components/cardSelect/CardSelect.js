import React, { Component } from 'react'
import * as PropTypes from 'prop-types'
import { Select } from 'antd'
const Option = Select.Option

class CardSelect extends Component {
  componentWillMount () {
    if (this.props.currentUser && this.props.currentUser.cards) {
      this.props.onSelected(this.props.currentUser.cards[0])
    }
  }

  selectCard = (index) => {
    this.props.onSelected(this.props.currentUser.cards[index])
  }

  render () {
    const { props } = this
    const { currentUser } = props
    const { cards } = currentUser
    return (
      <div>
        <Select size='large' defaultValue={cards[0].cardNumber} onChange={this.selectCard}>
          {cards.map((card, index) => (
            <Option value={index}>{card.cardNumber}</Option>
          ))}
        </Select>
      </div>
    )
  }
}

CardSelect.propTypes = {
  currentUser: PropTypes.object.isRequired,
  onSelected: PropTypes.func.isRequired
}

export default CardSelect
