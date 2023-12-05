var babelJest = require('babel-jest');
var webpack = require('./webpack.dev.config.js');
var path = require('path');

module.exports = {
  process: function(src, filename) {
    return babelJest.process(Object.keys(webpack.resolve.alias).reduce(function(code, alias){
      return code.replace(new RegExp(alias, 'g'), webpack.resolve.alias[alias].replace(/\\/g, '\\\\'));
    }, src), filename);
  }
};