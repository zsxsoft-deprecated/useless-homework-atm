import React, { Component } from 'react'
import { Avatar, Button, Card, Tabs, Popconfirm } from 'antd'
import TransactionList from '../../components/transactions/TransactionList'
import * as PropTypes from 'prop-types'
import { resetPasswordToInitial, updateStatus } from '../../../util/APIUtils'
import Cards from '../../components/cards/Cards'
const ButtonGroup = Button.Group
const TabPane = Tabs.TabPane

class ProfileAdmin extends Component {
  freezeBalance = () => {}
  unfreezeBalance = () => {}

  resetPassword = () => {
    resetPasswordToInitial(this.props.user.id).then(p => {
      window.location.reload()
    })
  }
  updateStatus = () => {
    updateStatus(this.props.user.id).then(p => {
      window.location.reload()
    })
  }

  render () {
    const { user } = this.props
    return (<div>
      <Tabs
        defaultActiveKey='1'
        tabPosition='left'
      >
        <TabPane tab='Information' key='1'>
          <p>Join time: {user.joinedAt}</p>
          <p>Email: {user.email}</p>
        </TabPane>
        {/*
        <TabPane tab='Balance' key='2'>
          <ButtonGroup>
            <Button size='large' to={'/recharge/new'}>Freeze Balance</Button>
            <Button size='large' to={'/withdraw/new'}>Unfreeze Balance</Button>
          </ButtonGroup>
        </TabPane>
        */}

        <TabPane tab='Cards' key='2'>
          <Cards user={user} isAdmin />
        </TabPane>
        <TabPane tab='Account' key='3'>

          <ButtonGroup>
            <Popconfirm title='Continue?' onConfirm={this.updateStatus} okText='Reset' cancelText='No'>
              <Button size='large' >{user.status === 'USERSTATUS_ENABLED' ? 'Deactivate' : 'Activate'} account</Button>
            </Popconfirm>
            <Popconfirm title='Reset password to 123456?' onConfirm={this.resetPassword} okText='Reset' cancelText='No'>
              <Button size='large' >Reset Password</Button>
            </Popconfirm>
          </ButtonGroup>

        </TabPane>
        <TabPane tab='Transactions' key='4'>
          <TransactionList isAuthenticated
            currentUser={user}
            method={'user'}
            username={user.username} />

        </TabPane>
      </Tabs>

    </div>)
  }
}

ProfileAdmin.propTypes = {
  currentUser: PropTypes.object.isRequired,
  user: PropTypes.object.isRequired
}
export default ProfileAdmin
