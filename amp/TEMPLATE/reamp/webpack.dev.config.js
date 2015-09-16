var webpack = require('webpack');
var path = require('path');
module.exports = {
  entry: {
    "admin/currency/deflator/script": [
      'webpack-dev-server/client?http://localhost:3000',
      'webpack/hot/only-dev-server',
      "./modules/admin/currency/deflator/script.es6"
    ],
    "admin/currency/deflated-currency-dialog/script": [
      'webpack-dev-server/client?http://localhost:3000',
      'webpack/hot/only-dev-server',
      "./modules/admin/currency/deflated-currency-dialog/script.es6"
    ]
  },
  output: {
    path: __dirname,
    filename: "modules/[name].js",
    publicPath: "http://localhost:3000/"
  },
  module: {
    loaders: [
      { test: /\.jsx$/, loaders:['react-hot', 'babel?plugins=typecheck'], exclude: /node_modules/ },
      { test: /\.es6$/, loaders:['babel?plugins=typecheck'], exclude: /node_modules/ },
      { test: /\.json$/, loader: 'json' },
      { test: /\.css$/, exclude: /\.useable\.css$/, loader: "style!css" },
      { test: /\.less$/, loader: "style!css!less" }
    ]
  },
  devtool: 'source-map',
  resolve: {
    extensions: ['', '.js', '.es6', '.jsx'],
    alias: {
      "amp/tools": __dirname + "/tools",
      "amp/modules": __dirname + "/modules",
      "amp/apps": __dirname + "/apps",
      "amp/architecture": __dirname + "/architecture",
      "amp/config": __dirname + "/config"
    }
  },
  plugins: [
    new webpack.HotModuleReplacementPlugin(),
    new webpack.ProvidePlugin({
      fetch: 'imports?this=>global!exports?global.fetch!whatwg-fetch',
      //Promise: 'es6-promise',
    })
  ]
};