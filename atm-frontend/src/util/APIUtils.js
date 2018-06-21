import { API_BASE_URL, LIST_SIZE, ACCESS_TOKEN } from '../constants'
import { notification } from 'antd/lib/index'

const request = (options) => {
  const headers = new Headers({
    'Content-Type': 'application/json'
  })

  if (localStorage.getItem(ACCESS_TOKEN)) {
    headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
  }

  const defaults = {headers: headers}
  options = Object.assign({}, defaults, options)

  return fetch(API_BASE_URL + options.url, options)
    .then(response =>
      response.json().then(json => {
        if (!response.ok) {
          return Promise.reject(json)
        }
        return json
      })
    ).catch(e => {
      if (options.noGlobalErrorHandler) {
        throw e
      } else {
        console.error(e)
        notification.error({
          message: 'ATM',
          description: e.message || 'Sorry! Something went wrong. Please try again!'
        })
        throw e
      }
    })
}

export function getTransactions (method = '', page = 0, size = LIST_SIZE, paramater = '') {
  return request({
    url: `/transactions${method !== '' ? '/' + method + '/' + paramater : ''}?page=${page}&size=${size}`,
    method: 'GET'
  })
}

export function createTransaction (data) {
  return request({
    url: '/transactions',
    method: 'POST',
    body: JSON.stringify(data)
  })
}

export function createRecharge (data) {
  return request({
    url: '/recharge',
    method: 'POST',
    body: JSON.stringify(data)
  })
}

export function createWithdraw (data) {
  return request({
    url: '/withdraw',
    method: 'POST',
    body: JSON.stringify(data)
  })
}

export function login (loginRequest) {
  return request({
    url: '/auth/signin',
    method: 'POST',
    body: JSON.stringify(loginRequest),
    noGlobalErrorHandler: true
  })
}

export function signup (signupRequest) {
  return request({
    url: '/auth/signup',
    method: 'POST',
    body: JSON.stringify(signupRequest)
  })
}

export function checkUsernameAvailability (username) {
  return request({
    url: '/user/checkUsernameAvailability?username=' + username,
    method: 'GET',
    noGlobalErrorHandler: true
  })
}

export function checkEmailAvailability (email) {
  return request({
    url: '/user/checkEmailAvailability?email=' + email,
    method: 'GET',
    noGlobalErrorHandler: true
  })
}

export function getCurrentUser () {
  if (!localStorage.getItem(ACCESS_TOKEN)) {
    return Promise.reject(new Error('No access token set.'))
  }

  return request({
    url: '/user/me',
    method: 'GET'
  })
}

export function getUserProfile (username) {
  return request({
    url: '/users/username/' + username,
    method: 'GET'
  })
}

export function getCardProfile (username, useGlobalErrorHandler = false) {
  return request({
    url: '/cards/card/' + username,
    method: 'GET',
    noGlobalErrorHandler: !useGlobalErrorHandler
  })
}

export function updatePassword (data) {
  return request({
    url: '/user/password',
    method: 'POST',
    body: JSON.stringify(data)
  })
}

export function newCard (username) {
  return request({
    url: '/cards/new',
    method: 'POST'
  })
}

export function resetPasswordToInitial (id) {
  return request({
    url: '/admin/user/password/reset/' + id,
    method: 'POST'
  })
}

export function updateStatus (id) {
  return request({
    url: '/admin/user/status/update/' + id,
    method: 'POST'
  })
}
