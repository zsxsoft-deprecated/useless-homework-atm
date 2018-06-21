import React, {PureComponent} from 'react'
import {
  Route,
  Redirect
} from 'react-router-dom'
import { Button } from 'antd'
import { withRouter } from 'react-router'

class RedirectButton extends PureComponent {
  redirect = () => {
    console.log(this.props)
    this.props.history.push(this.props.to)
  }

  render () {
    const { to, ...props } = this.props
    return <Button onClick={this.redirect} {...props} />
  }
}

export default withRouter(RedirectButton)