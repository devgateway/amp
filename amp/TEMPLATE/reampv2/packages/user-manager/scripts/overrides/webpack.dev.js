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
        import: 'react',
        shareKey: 'newReact',
        shareScope: 'default',
        singleton: true,
      },
      'react-dom': {
        import: 'react-dom',
        shareKey: 'newReactDom',
        shareScope: 'default',
        singleton: true, // only a single version of the shared module is allowed
      },
      'react-router-dom': {
        import: 'react-router-dom',
        shareKey: 'newReactRouterDom',
        shareScope: 'default',
        singleton: true
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
