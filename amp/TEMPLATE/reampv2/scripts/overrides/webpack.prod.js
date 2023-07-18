const { ModuleFederationPlugin } = require('webpack').container;

const webpackConfigPath = 'react-scripts/config/webpack.config';
// eslint-disable-next-line import/no-dynamic-require
const webpackConfig = require(webpackConfigPath);

const override = config => {
  // eslint-disable-next-line global-require
  config.plugins.push(new ModuleFederationPlugin(require('../../module-federation.config')));

  config.output = {
    // Make sure to use [name] or [id] in output.filename
    //  when using multiple entry points
    ...config.output,
    publicPath: '/TEMPLATE/reampv2/build/',
    filename: '[name].bundle.js',
    chunkFilename: '[id].bundle.js'
  };

  config.module.rules = [
    ...config.module.rules,
    {
      test: [/\.js?$/, /\.ts?$/, /\.jsx?$/, /\.tsx?$/],
      enforce: 'pre',
      exclude: /node_modules/,
      use: ['source-map-loader'],
    }
  ];

  config.resolve.fallback = {
    ...config.resolve.fallback,
    stream: require.resolve('stream-browserify')
  };

  return config;
};

require.cache[require.resolve(webpackConfigPath)].exports = env => override(webpackConfig(env));

// eslint-disable-next-line import/no-dynamic-require
module.exports = require(webpackConfigPath);
