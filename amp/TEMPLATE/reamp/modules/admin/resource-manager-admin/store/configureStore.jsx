if (process.env.NODE_ENV === 'production') {
  module.exports = require('./configureStore.production.jsx'); // eslint-disable-line global-require
} else {
  module.exports = require('./configureStore.development.jsx'); // eslint-disable-line global-require
}
