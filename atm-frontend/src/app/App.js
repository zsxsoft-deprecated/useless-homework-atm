import React, { Component } from 'react'
import './App.css'
import {
  Route,
  withRouter,
  Switch,
  Redirect

} from 'react-router-dom'

import { getCurrentUser } from '../util/APIUtils'
import { ACCESS_TOKEN } from '../constants'
import TransactionList from './components/transactions/TransactionList'
import TransactionNew from './transacations/TransactionNew'
import Login from '../app/user/login/Login'
import Signup from '../app/user/signup/Signup'
import Profile from '../app/user/profile/Profile'
import UpdatePassword from '../app/user/password/UpdatePassword'
import AppHeader from '../common/AppHeader'
import NotFound from '../common/NotFound'
import LoadingIndicator from '../common/LoadingIndicator'
import PrivateRoute from '../common/PrivateRoute'
import ReactRouterPropTypes from 'react-router-prop-types'

import { Layout, notification } from 'antd'
import RechargeNew from './recharge/RechargeNew'
import WithdrawNew from './withdraw/WithdrawNew'
import TransactionCard from './transacations/TransactionCard'

const {Content} = Layout

class App extends Component {
  constructor (props) {
    super(props)
    this.state = {
      currentUser: null,
      isAuthenticated: false,
      isLoading: false
    }

    notification.config({
      placement: 'topRight',
      top: 70,
      duration: 3
    })
  }

  loadCurrentUser = () => {
    this.setState({
      isLoading: true
    })
    getCurrentUser()
      .then(response => {
        const user = Object.assign({}, response, {
          isAdmin: response.roles.filter(p => p.name === 'ROLE_ADMIN').length > 0
        })
        this.setState({
          currentUser: user,
          isAdmin: user.isAdmin,
          isAuthenticated: true,
          isLoading: false
        })
      }).catch(error => {
        console.error(error)
        this.setState({
          isLoading: false
        })
      })
  }

  componentWillMount () {
    this.loadCurrentUser()
  }

  handleLogout = (redirectTo = '/', notificationType = 'success', description = 'You\'re successfully logged out.') => {
    localStorage.removeItem(ACCESS_TOKEN)

    this.setState({
      currentUser: null,
      isAuthenticated: false
    })

    this.props.history.push(redirectTo)

    notification[notificationType]({
      message: 'ATM',
      description: description
    })
  }

  handleLogin = () => {
    notification.success({
      message: 'ATM',
      description: 'You\'re successfully logged in.'
    })
    this.loadCurrentUser()
    this.props.history.push('/')
  }

  render () {
    if (this.state.isLoading) {
      return <LoadingIndicator />
    }
    return (
      <Layout className='app-container'>
        <AppHeader isAuthenticated={this.state.isAuthenticated}
          currentUser={this.state.currentUser}
          onLogout={this.handleLogout} />

        <Content className='app-content'>
          <div className='container'>
            <Switch>
              <PrivateRoute exact path='/' authenticated={this.state.isAuthenticated}
                component={Redirect} to={{
                  pathname: `/users/${this.state.currentUser ? this.state.currentUser.username : ''}`
                }}
                currentUser={this.state.currentUser} />
              <Route exact path='/transactions'
                render={(props) => <TransactionList isAuthenticated={this.state.isAuthenticated}
                  currentUser={this.state.currentUser} {...props} />} />
              <Route exact path='/transactions/card/:cardNumber'
                render={(props) => <TransactionCard isAuthenticated={this.state.isAuthenticated}
                  currentUser={this.state.currentUser} {...props} />} />
              <Route path='/login'
                render={(props) => <Login onLogin={this.handleLogin} {...props} />} />
              <Route path='/signup' component={Signup} />
              <Route path='/users/:username' render={(props) => <Profile isAuthenticated={this.state.isAuthenticated}
                currentUser={this.state.currentUser} {...props} />} />
              <PrivateRoute path='/transaction/new' authenticated={this.state.isAuthenticated}
                component={TransactionNew} handleLogout={this.handleLogout}
                currentUser={this.state.currentUser} />
              <PrivateRoute path='/recharge/new' authenticated={this.state.isAuthenticated}
                component={RechargeNew} handleLogout={this.handleLogout}
                currentUser={this.state.currentUser} />
              <PrivateRoute path='/withdraw/new' authenticated={this.state.isAuthenticated}
                component={WithdrawNew} handleLogout={this.handleLogout}
                currentUser={this.state.currentUser} />
              <PrivateRoute path='/user/password' authenticated={this.state.isAuthenticated} handleLogout={this.handleLogout} component={UpdatePassword} />
              <Route component={NotFound} />
            </Switch>
          </div>
        </Content>
      </Layout>
    )
  }
}

App.propTypes = {
  history: ReactRouterPropTypes.history.isRequired,
  location: ReactRouterPropTypes.location.isRequired,
  match: ReactRouterPropTypes.match.isRequired,
  route: ReactRouterPropTypes.route.isRequired
}
export default withRouter(App)
