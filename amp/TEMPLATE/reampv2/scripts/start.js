process.env.NODE_ENV = process.env.NODE_ENV || 'development';
process.env.GENRATE_SOURCEMAP = process.env.GENRATE_SOURCEMAP || 'false';
require('./overrides/webpack.config');
require('react-scripts/scripts/start');
