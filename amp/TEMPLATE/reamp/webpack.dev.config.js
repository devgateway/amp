var webpack = require('webpack');
var path = require('path');
module.exports = {
  entry: {
    "admin/currency/deflator/script": [
      'webpack-dev-server/client?http://localhost:3000',
      'webpack/hot/only-dev-server',
      "./modules/admin/currency/deflator/script.es6"
    ],
    "admin/dashboard/script": [
       'webpack-dev-server/client?http://localhost:3000',
       'webpack/hot/only-dev-server',
       './modules/admin/dashboard/script.es6'
    ],
    "gpi-data/script": [
        'webpack-dev-server/client?http://localhost:3000',
        'webpack/hot/only-dev-server',
        './modules/gpi-data/script.es6'
     ],
     "activity-preview/script": [
         'webpack-dev-server/client?http://localhost:3000',
         'webpack/hot/only-dev-server',
         './modules/activity-preview/script.es6'
      ],
     "gpi-reports/script": [
         'webpack-dev-server/client?http://localhost:3000',
         'webpack/hot/only-dev-server',
         './modules/gpi-reports/script.es6'
      ],
     "admin/resource-manager-admin/script": [
          'webpack-dev-server/client?http://localhost:3000',
          'webpack/hot/only-dev-server',
          './modules/admin/resource-manager-admin/script.es6'
      ],
      "admin/data-freeze-manager/script": [
          'webpack-dev-server/client?http://localhost:3000',
          'webpack/hot/only-dev-server',
          './modules/admin/data-freeze-manager/script.es6'
      ],
      "admin/performance-alert-manager/script": [
          'webpack-dev-server/client?http://localhost:3000',
          'webpack/hot/only-dev-server',
          './modules/admin/performance-alert-manager/script.es6'
      ],
      "ampoffline/download/script": [
          'webpack-dev-server/client?http://localhost:3000',
          'webpack/hot/only-dev-server',
          './modules/ampoffline/download/script.es6'
       ],
      "activity/preview/script": [
          'webpack-dev-server/client?http://localhost:3000',
          'webpack/hot/only-dev-server',
          './modules/activity/preview/script.es6'
      ]
    //"filters/script": [
    //  'webpack-dev-server/client?http://localhost:3000',
    //  'webpack/hot/only-dev-server',
    //  "./modules/filters/script.es6"
    //],
    //"filters/index": [
    //  'webpack-dev-server/client?http://localhost:3000',
    //  'webpack/hot/only-dev-server',
    //  "./modules/filters/index.jsx"
    //]
  },
  output: {
    path: __dirname,
    filename: "modules/[name].js",
    publicPath: "http://localhost:3000/"
  },
  module: {
    loaders: [

      { test: /\.jsx$/, loader:'babel', exclude: /node_modules/,
          query:{
          presets: ['es2015', 'stage-0','react']
          }
      },
      { test: /\.es6$/, loader:'babel', exclude: /node_modules/,
          query: {
              presets: ['es2015', 'stage-0', 'react']
          }
      },

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
      fetch: 'imports?this=>global!exports?global.fetch!whatwg-fetch'
    })
  ]
};
