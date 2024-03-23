process.env.NODE_ENV = 'production';
require('./overrides/webpack.prod');
require('react-scripts/scripts/build');
