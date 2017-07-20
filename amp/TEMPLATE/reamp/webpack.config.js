var config = require('./webpack.dev.config.js');
var webpack = require('webpack');
config.entry["admin/currency/deflator/script"] = "./modules/admin/currency/deflator/script.es6";
config.entry["admin/dashboard/script"] = "./modules/admin/dashboard/script.es6";
config.entry["gpi-data/script"] = "./modules/gpi-data/script.es6";
config.entry["gpi-reports/script"] = "./modules/gpi-reports/script.es6";
config.entry["admin/resource-manager-admin/script"] = "./modules/admin/resource-manager-admin/script.es6";
config.entry["admin/data-freeze-manager/script"] = "./modules/admin/data-freeze-manager/script.es6";
config.output.filename = "modules/[name].min.js";
delete config.output.publicPath;
delete config.devtool;
config.plugins.push(new webpack.optimize.UglifyJsPlugin());
config.plugins.push(new webpack.optimize.DedupePlugin());

module.exports = config;