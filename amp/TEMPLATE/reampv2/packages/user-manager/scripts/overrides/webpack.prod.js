const { ModuleFederationPlugin } = require('webpack').container;

const webpackConfigPath = 'react-scripts/config/webpack.config';
// eslint-disable-next-line import/no-dynamic-require
const webpackConfig = require(webpackConfigPath);
const packageJson = require('../../package.json');

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
      'semantic-ui-css': {
        requiredVersion: packageJson.dependencies['semantic-ui-css'],
        singleton: true,
      }
    },
  });

  config.plugins.push(moduleFederationPlugin);
  config.mode = 'production';

  config.devServer = {
    ...config.devServer,
    historyApiFallback: true,
  };
  config.output = {
    // Make sure to use [name] or [id] in output.filename
    //  when using multiple entry points
    ...config.output,
    publicPath: '/TEMPLATE/reampv2/packages/user-manager/build/',
    filename: '[name].bundle.js',
    chunkFilename: '[id].bundle.js',
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

require.cache[require.resolve(webpackConfigPath)].exports = (env) => override(webpackConfig(env));

// eslint-disable-next-line import/no-dynamic-require
module.exports = require(webpackConfigPath);
