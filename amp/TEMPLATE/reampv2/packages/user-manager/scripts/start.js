process.env.NODE_ENV = process.env.NODE_ENV || 'development';
process.env.GENERATE_SOURCEMAP = process.env.GENERATE_SOURCEMAP || false;
process.env.PORT = process.env.PORT || 3003;
require('./overrides/webpack.dev');
require('react-scripts/scripts/start');
