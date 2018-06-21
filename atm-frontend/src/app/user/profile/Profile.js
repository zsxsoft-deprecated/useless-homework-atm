import React, { Component } from 'react'
import { getUserProfile, newCard } from '../../../util/APIUtils'
import { Avatar, Button, Card, Popconfirm, Table, Tabs } from 'antd'
import { getAvatarColor } from '../../../util/Colors'
import LoadingIndicator from '../../../common/LoadingIndicator'
import './Profile.css'
import NotFound from '../../../common/NotFound'
import ServerError from '../../../common/ServerError'
import RedirectButton from '../../components/RedirectButton'
import ReactRouterPropTypes from 'react-router-prop-types'
import * as PropTypes from 'prop-types'
import ProfileAdmin from './ProfileAdmin'
import { formatDate } from '../../../util/Helpers'
import Cards from '../../components/cards/Cards'
const ButtonGroup = Button.Group
const TabPane = Tabs.TabPane

class Profile extends Component {
  constructor (props) {
    super(props)
    this.state = {
      user: null,
      isLoading: false
    }
  }

  loadUserProfile = (username) => {
    this.setState({
      isLoading: true
    })

    getUserProfile(username)
      .then(response => {
        this.setState({
          user: response,
          isLoading: false
        })
      }).catch(error => {
        if (error.status === 404) {
          this.setState({
            notFound: true,
            isLoading: false
          })
        } else {
          this.setState({
            serverError: true,
            isLoading: false
          })
        }
      })
  }

  requestForNewCard = () => {
    this.setState({
      isLoading: true
    })
    newCard().then(p => {
      const username = this.props.match.params.username
      this.loadUserProfile(username)
    })
  }

  updatePassword = () => {

  }

  empty = () => {}

  componentDidMount () {
    const username = this.props.match.params.username
    this.loadUserProfile(username)
  }

  componentWillReceiveProps (nextProps) {
    if (this.props.match.params.username !== nextProps.match.params.username) {
      this.loadUserProfile(nextProps.match.params.username)
    }
  }



  render () {
    if (this.state.isLoading) {
      return <LoadingIndicator />
    }

    if (this.state.notFound) {
      return <NotFound />
    }

    if (this.state.serverError) {
      return <ServerError />
    }

    const currentUser = this.props.currentUser

    return (
      <div className='profile'>
        {
          this.state.user ? (
            <div className='user-profile'>
              <div className='user-details'>
                <div className='user-avatar'>
                  <Avatar className='user-avatar-circle' style={{
                    backgroundColor: getAvatarColor(this.state.user.name)
                  }}>
                    {this.state.user.name[0].toUpperCase()}
                  </Avatar>
                </div>
                <div className='user-summary'>
                  <div className='full-name'>{this.state.user.name}{this.state.user.status === 'USERSTATUS_DISABLED' ? ' (Deactivated)' : ''}</div>
                  <div className='username'>@{this.state.user.username}</div>
                  <div className='user-joined'>Joined {formatDate(this.state.user.joinedAt)}</div>
                </div>
              </div>
              {(currentUser && this.state.user.id === currentUser.id
                ? <div style={{textAlign: 'center', margin: '0 auto', paddingTop: 20}}>
                  <ButtonGroup>
                    <RedirectButton size='large' to={'/recharge/new'}>Recharge</RedirectButton>
                    <RedirectButton size='large' to={'/withdraw/new'}>Withdraw</RedirectButton>
                    <RedirectButton size='large' to={'/transaction/new'}>Transfer</RedirectButton>
                  </ButtonGroup>
                  <div style={{paddingTop: '1em'}}>
                    <Tabs>
                      <TabPane tab='Cards' key='1'>
                        <Cards user={this.state.user} isAdmin={false} />
                      </TabPane>
                      <TabPane tab='Actions'>
                        <ButtonGroup>
                          <Popconfirm title='Requsest for a new debit card?' onConfirm={this.requestForNewCard} onCancel={this.empty} okText='Yes' cancelText='No'>
                            <Button size='large'>New Card</Button>
                          </Popconfirm>
                          <RedirectButton size='large' to={'/user/password'}>Update Password</RedirectButton>
                        </ButtonGroup>
                      </TabPane>
                    </Tabs>
                  </div>
                </div>
                : <div />
              )}
              {(currentUser && this.state.user.id !== this.props.currentUser.id && this.props.currentUser.isAdmin
                ? <div style={{textAlign: 'center', margin: '0 auto', paddingTop: 20}}>
                  <ProfileAdmin currentUser={currentUser} user={this.state.user} />
                </div>
                : <div />)}
            </div>
          ) : null
        }
      </div>
    )
  }
}

Profile.propTypes = {
  match: ReactRouterPropTypes.match.isRequired,
  currentUser: PropTypes.object.isRequired
}
export default Profile
