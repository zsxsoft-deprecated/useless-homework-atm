import React, { Component } from 'react'
import * as PropTypes from 'prop-types'
import { updatePassword } from '../../../util/APIUtils'
import { Form, Icon, Input, Button, Checkbox, Modal } from 'antd'
const FormItem = Form.Item

class UpdatePassword extends Component {
  constructor (props) {
    super(props)
    this.state = {
      waiting: false
    }
  }

  handleSubmit = (e) => {
    e.preventDefault()
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.setState({
          waiting: true
        })
        updatePassword(values).then(p => {
          if (p) {
            this.props.handleLogout()
          }
          this.setState({
            waiting: false
          })
        })
      }
    })
  }

  compareToFirstPassword = (rule, value, callback) => {
    const form = this.props.form
    console.log(value)
    console.log(form.getFieldValue('newPassword'))
    if (value && value !== form.getFieldValue('newPassword')) {
      callback('Two passwords that you enter is inconsistent!')
    } else {
      callback()
    }
  }

  render () {
    const { getFieldDecorator } = this.props.form

    return (
      <Form onSubmit={this.handleSubmit} className='login-form' style={{marginTop: 40}}>
        <FormItem>
          {getFieldDecorator('oldPassword', {
            rules: [
              { required: true, message: 'Please input old password!' },
              { min: 6, message: 'Password must be at least 6 characters'}
            ]
          })(
            <Input prefix={<Icon type='lock' style={{ color: 'rgba(0,0,0,.25)' }} />} type='password' placeholder='Old Password' />
          )}
        </FormItem>
        <FormItem>
          {getFieldDecorator('newPassword', {
            rules: [
              { required: true, message: 'Please input new password!' },
              { min: 6, message: 'Password must be at least 6 characters'}]
          })(
            <Input prefix={<Icon type='lock' style={{ color: 'rgba(0,0,0,.25)' }} />} type='password' placeholder='New Password' />
          )}
        </FormItem>
        <FormItem>
          {getFieldDecorator('confirmPassword', {
            rules: [
              { required: true, message: 'Please confirm new password!' },
              { min: 6, message: 'Password must be at least 6 characters'},
              { validator: this.compareToFirstPassword },
            ]
          })(
            <Input prefix={<Icon type='lock' style={{ color: 'rgba(0,0,0,.25)' }} />} type='password' placeholder='Confirm New Password' />
          )}
        </FormItem>
        <FormItem>
          <Button type='primary' htmlType='submit' className='login-form-button' loading={this.state.waiting}>
            Update
          </Button>
        </FormItem>
      </Form>
    )
  }
}

UpdatePassword.propTypes = {}
const WrappedUpdatePasswordForm = Form.create()(UpdatePassword)
export default WrappedUpdatePasswordForm
