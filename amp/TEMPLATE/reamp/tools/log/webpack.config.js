module.exports = {
  entry: {
    install: "./index.es6"
  },
  output: {
    path: __dirname,
    filename: "index.js",
    libraryTarget: 'commonjs2'
  },
  module: {
    loaders: [
      {
        test: /\.es6$/,
        loader: 'babel'
      }
    ]
  },
  resolve: {
    extensions: ['', '.js', '.es6']
  }
};