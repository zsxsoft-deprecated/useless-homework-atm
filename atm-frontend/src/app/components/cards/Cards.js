import React, { Component } from 'react'
import * as PropTypes from 'prop-types'
import { Button, Table } from 'antd'
import RedirectButton from '../RedirectButton'
const ButtonGroup = Button.Group

class Cards extends Component {
  render () {
    const { user, isAdmin } = this.props
    const cards = user ? user.cards : []
    const cardColumns = [{
      title: 'Card No.',
      dataIndex: 'cardNumber',
      key: 'cardNumber'
    }, {
      title: 'Type',
      dataIndex: 'type',
      key: 'type',
      render: text => text.replace('BANKCARDTYPE_', '').toLowerCase().replace(/^(.)/, (a) => a.toUpperCase()) + ' Card'
    }, {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: text => text.replace('BANKCARDSTATUS_', '').toLowerCase().replace(/^(.)/, (a) => a.toUpperCase())
    }, {
      title: 'Available Balance',
      dataIndex: 'availableBalance',
      key: 'availableBalance'
    }, {
      title: 'Actions',
      render: (text, item) =>
        <ButtonGroup>
          <RedirectButton type={'primary'} to={`/transactions/card/${item.cardNumber}`}>Transactions</RedirectButton>
          {(false && (isAdmin
            ? <Button type={item.status === 'BANKCARDSTATUS_ENABLED' ? 'danger' : 'default'}>{item.status === 'BANKCARDSTATUS_ENABLED' ? 'Deactivate' : 'Activate'}</Button>
            : <div />))}
        </ButtonGroup>

    }]

    return (
      <div>
        <Table dataSource={cards} columns={isAdmin ? cardColumns : cardColumns} />
      </div>
    )
  }
}

Cards.propTypes = {
  user: PropTypes.object.isRequired,
  isAdmin: PropTypes.bool.isRequired
}

export default Cards
