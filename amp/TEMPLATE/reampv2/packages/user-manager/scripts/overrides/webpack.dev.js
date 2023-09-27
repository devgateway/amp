const { ModuleFederationPlugin } = require('webpack').container;
const packageJson = require('../../package.json');

const webpackConfigPath = 'react-scripts/config/webpack.config';
// eslint-disable-next-line import/no-dynamic-require
const webpackDev = require(webpackConfigPath);

const override = (config) => {
  // eslint-disable-next-line global-require
  const moduleFederationPlugin = new ModuleFederationPlugin({
    name: 'userManager',
    filename: 'remoteEntry.js',
    exposes: {
      './UserManagerApp': './src/bootstrap'
    },
    shared: {
      ...packageJson.dependencies,
      react: {
        singleton: true,
        requiredVersion: packageJson.dependencies.react,
      },
      'react-dom': {
        requiredVersion: packageJson.dependencies['react-dom'],
        singleton: true, // only a single version of the shared module is allowed
      },
      'react-router-dom': {
        requiredVersion: packageJson.dependencies['react-router-dom'],
        singleton: true,
      },
      'semantic-ui-react': {
        requiredVersion: packageJson.dependencies['semantic-ui-react'],
        singleton: true,
      },
      'semantic-ui-css': {
        requiredVersion: packageJson.dependencies['semantic-ui-css'],
        singleton: true,
      }
    },
  });

  config.plugins.push(moduleFederationPlugin);

  config.output.publicPath = 'http://localhost:3003/';

  config.devServer = {
    ...config.devServer,
    historyApiFallback: true,
  };

  config.module.rules = [
    ...config.module.rules,
    {
      test: [/\.js?$/, /\.ts?$/, /\.jsx?$/, /\.tsx?$/],
      enforce: 'pre',
      exclude: /node_modules/,
      use: ['source-map-loader'],
    },
  ];

  return config;
};

require.cache[require.resolve(webpackConfigPath)].exports = (env) => override(webpackDev(env));

// eslint-disable-next-line import/no-dynamic-require
module.exports = require(webpackConfigPath);
