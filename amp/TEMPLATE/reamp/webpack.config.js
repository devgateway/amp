var webpack = require('webpack');
module.exports = {
  entry: {
    "admin/currency/deflator/script": "./modules/admin/currency/deflator/script.es6"
  },
  output: {
    path: './',
    filename: "modules/[name].js"
  },
  module: {
    loaders: [
      { test: /\.(jsx|es6)$/, loaders:['babel?plugins=typecheck'], exclude: /node_modules/ },
      { test: /\.json$/, loader: 'json' },
      { test: /\.css$/, exclude: /\.useable\.css$/, loader: "style!css" },
      { test: /\.less$/, loader: "style!css!less" }
    ]
  },
  devtool: 'source-map',
  resolve: {
    extensions: ['', '.js', '.es6', '.jsx', '.flow'],
    alias: {
      "amp/tools": __dirname + "/tools",
      "amp/modules": __dirname + "/modules",
      "amp/apps": __dirname + "/apps",
      "amp/architecture": __dirname + "/architecture",
      "amp/config": __dirname + "/config"
    }
  },
  plugins: [
    new webpack.ProvidePlugin({
      fetch: 'imports?this=>global!exports?global.fetch!whatwg-fetch',
      //Promise: 'es6-promise',
    })
  ]
};