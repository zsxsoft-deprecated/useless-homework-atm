import ReactRouterPropTypes from 'react-router-prop-types'

export default function PropType (propType) {
  return Object.assign({}, propType, {
    history: ReactRouterPropTypes.history.isRequired,
    location: ReactRouterPropTypes.location.isRequired,
    match: ReactRouterPropTypes.match.isRequired,
    route: ReactRouterPropTypes.route.isRequired,
  })
}
