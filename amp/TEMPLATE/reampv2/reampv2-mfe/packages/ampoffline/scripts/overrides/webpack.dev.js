const { ModuleFederationPlugin } = require('webpack').container;
const packageJson = require('../../package.json');

const webpackConfigPath = 'react-scripts/config/webpack.config';
// eslint-disable-next-line import/no-dynamic-require
const webpackDev = require(webpackConfigPath);

const override = config => {
  // eslint-disable-next-line global-require
  const moduleFederationPlugin = new ModuleFederationPlugin({
    name: 'ampoffline',
    filename: 'remoteEntry.js',
    exposes: {
      './AmpOfflineApp': './src/bootstrap',
    },
    shared: {
        ...packageJson.dependencies,
        react: {
            singleton: true,
            requiredVersion: packageJson.dependencies.react,
        },
        'react-dom': {
            singleton: true,
            requiredVersion: packageJson.dependencies['react-dom'],
        }
    }
  })

  config.plugins.push(moduleFederationPlugin);

  config.output.publicPath = 'http://localhost:3001/';

  config.module.rules = [
    ...config.module.rules,
    {
      test: [/\.js?$/, /\.ts?$/, /\.jsx?$/, /\.tsx?$/],
      enforce: 'pre',
      exclude: /node_modules/,
      use: ['source-map-loader'],
    }
  ];

  return config;
};

require.cache[require.resolve(webpackConfigPath)].exports = env => override(webpackDev(env));

// eslint-disable-next-line import/no-dynamic-require
module.exports = require(webpackConfigPath);
