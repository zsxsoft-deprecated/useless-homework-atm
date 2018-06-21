import React, { Component } from 'react'
import { getTransactions } from '../../../util/APIUtils'
import LoadingIndicator from '../../../common/LoadingIndicator'
import { BackTop, Button, Icon, Table } from 'antd'
import { LIST_SIZE } from '../../../constants/index'
import { withRouter } from 'react-router-dom'
import './TransactionList.css'
import * as PropTypes from 'prop-types'

class TransactionList extends Component {
  constructor (props) {
    super(props)
    this.state = {
      transactions: [],
      isLoading: false,
      pagination: {
        current: 0,
        pageSize: LIST_SIZE,
        total: 0
      }
    }
  }

  loadTransactionList = (page = 0, size = LIST_SIZE) => {
    const { method, username, cardNumber } = this.props
    this.setState({
      isLoading: true
    })

    return getTransactions(method, page, size, method === 'user' ? username : cardNumber)
      .then(response => {
        const currentUser = this.props.currentUser
        const transactions = response.content.slice().map(p => {
          p.displayAmount = currentUser.id === p.fromCard.userId ? -p.actualAmount : p.actualAmount
          p.createdAt = new Date(p.createdAt)
          return p
        }).sort((a, b) => a.createdAt.getTime() - b.createdAt.getTime())
        this.setState({
          transactions,
          last: response.last,
          isLoading: false,
          pagination: {
            current: response.page,
            pageSize: response.size,
            total: response.totalElements
          }
        })
      }).catch(e => {
        console.error(e)
        this.setState({
          isLoading: false
        })
      })
  }

  componentWillMount () {
    this.loadTransactionList()
  }

  componentWillReceiveProps (nextProps) {
    if (this.props.isAuthenticated !== nextProps.isAuthenticated) {
      // Reset State
      this.setState({
        transactions: [],
        isLoading: false,
        pagination: {
          current: 0,
          pageSize: LIST_SIZE,
          total: 0
        }
      })
      this.loadTransactionList()
    }
  }

  handleTableChange = (pagination, filters, sorter) => {
    const pager = { ...this.state.pagination }
    pager.current = pagination.current
    this.setState({
      isLoading: true
    })
    this.refs.backtop.scrollToTop()
    this.loadTransactionList(pager.current - 1)
  }

  columns = [{
    title: 'Date',
    dataIndex: 'createdAt',
    key: 'createdAt',
    render: (text) => new Date(text).toDateString(),
    width: 150
  }, {
    title: 'Remark',
    key: 'remark',
    dataIndex: 'remark',
    render: (text) => text.substr(0, 50)
  }, {
    title: 'Amount',
    key: 'amount',
    dataIndex: 'displayAmount',
    width: 150
  }, {
    title: 'Type',
    key: 'type',
    dataIndex: 'type',
    render: text => text.replace('TRANSACTION_', '').toLowerCase().replace(/^(.)/, (a) => a.toUpperCase()),
    width: 150
  }]

  render () {
    return (
      <div className='polls-container'>
        <Table
          dataSource={this.state.transactions}
          columns={this.columns}
          pagination={this.state.pagination}
          loading={this.state.isLoading}
          onChange={this.handleTableChange}
          expandedRowRender={record => <div>
            <p>Transaction ID: {record.id}</p>
            <p>From: <a href={`/users/${record.fromCard.user.username}`} target='_blank'>{record.fromCard.user.name}</a> / {record.fromCard.cardNumber}</p>
            <p>To: <a href={`/users/${record.toCard.user.username}`} target='_blank'>{record.toCard.user.name}</a> / {record.toCard.cardNumber}</p>
            <p>Create Time: {record.createdAt.toString()}</p>
            <p>Remark: {record.remark}</p>
          </div>}
        />
        <BackTop ref={'backtop'} />
      </div>
    )
  }
}

TransactionList.propTypes = {
  currentUser: PropTypes.object.isRequired,
  isAuthenticated: PropTypes.bool.isRequired,
  username: PropTypes.string,
  cardNumber: PropTypes.string,
  method: PropTypes.string
}

export default withRouter(TransactionList)
