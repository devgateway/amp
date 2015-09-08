var config = require('./webpack.dev.config.js');
var webpack = require('webpack');
config.entry["admin/currency/deflator/script"] = "./modules/admin/currency/deflator/script.es6";
config.output.filename = "modules/[name].min.js";
delete config.output.publicPath;
delete config.devtool;
config.plugins.push(new webpack.optimize.UglifyJsPlugin());
config.plugins.push(new webpack.optimize.DedupePlugin());

module.exports = config;