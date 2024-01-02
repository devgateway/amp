process.env.NODE_ENV = 'production';
process.env.DISABLE_ESLINT_PLUGIN = 'true'
require('./overrides/webpack.prod');
require('react-scripts/scripts/build');
