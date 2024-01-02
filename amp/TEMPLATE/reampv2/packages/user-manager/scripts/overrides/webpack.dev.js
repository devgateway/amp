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
    remotes: {
      'reampv2App': 'reampv2App@http://localhost:3002/remoteEntry.js',
      'ampoffline': 'ampoffline@http://localhost:3001/remoteEntry.js',
    },
    shared: {
      ...packageJson.dependencies,
      react: {
        import: 'react', // the "react" package will be used a provided and fallback module
        shareKey: 'newReact', // under this name the shared module will be placed in the share scope
        shareScope: 'default', // share scope with this name will be used
        singleton: true, // only a single version of the shared module is allowed
      },
      'react-dom': {
        import: 'react-dom', // the "react" package will be used a provided and fallback module
        shareKey: 'newReactDom', // under this name the shared module will be placed in the share scope
        shareScope: 'default', // share scope with this name will be used
        singleton: true, // only a single version of the shared module is allowed
      },
      'react-router-dom': {
        import: 'react-router-dom',
        shareKey: 'newReactRouterDom',
        shareScope: 'default',
        singleton: true,
      },
      'semantic-ui-react': {
        import: 'semantic-ui-react',
        shareKey: 'newSemanticUiReact',
        shareScope: 'default',
        singleton: true,
      },
      'semantic-ui-css': {
        import: 'semantic-ui-css',
        shareKey: 'newSemanticUiCss',
        shareScope: 'default',
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
