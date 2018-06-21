import React, { Component } from 'react'
import TransactionList from '../components/transactions/TransactionList'
import './TransactionCard.css'
import * as PropTypes from 'prop-types'
import ReactRouterPropTypes from 'react-router-prop-types'

class TransactionCard extends Component {
  render () {
    const { props } = this
    return (
      <div className='new-transaction-container'>
        <h1 className='page-title'>Transactions</h1>
        <div className='new-transaction-content'>
          <div><p>Current Card: {this.props.match.params.cardNumber}</p></div>
          <TransactionList isAuthenticated={this.props.isAuthenticated}
            currentUser={this.props.currentUser}
            method='card'
            cardNumber={this.props.match.params.cardNumber}
            {...props} />
        </div>
      </div>
    )
  }
}

TransactionCard.propTypes = {
  currentUser: PropTypes.object.isRequired,
  isAuthenticated: PropTypes.bool.isRequired,
  method: PropTypes.string,
  match: ReactRouterPropTypes.match.isRequired
}

export default TransactionCard
