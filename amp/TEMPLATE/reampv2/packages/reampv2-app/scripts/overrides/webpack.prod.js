const {ModuleFederationPlugin} = require('webpack').container;

const webpackConfigPath = 'react-scripts/config/webpack.config';

// Check if react-scripts is available before requiring
let webpackConfig;
try {
  // Try to resolve the module first to get a better error if it's missing
  require.resolve(webpackConfigPath);
  // eslint-disable-next-line import/no-dynamic-require
  webpackConfig = require(webpackConfigPath);
} catch (error) {
  if (error.code === 'MODULE_NOT_FOUND') {
    console.error(`Error: Cannot find module '${webpackConfigPath}'`);
    console.error('This usually means react-scripts is not installed properly.');
    console.error('Please ensure npm install has completed successfully.');
    console.error('If this is a cache issue, try clearing the npm cache or rebuilding without cache.');
    process.exit(1);
  }
  throw error;
}

const packageJson = require("../../package.json");

const override = config => {
    // eslint-disable-next-line global-require
    const moduleFederationPlugin = new ModuleFederationPlugin({
        name: 'reampv2App',
        filename: 'remoteEntry.js',
        exposes: {
            './Reampv2App': './src/bootstrap',
        },
        shared: {
            ...packageJson.dependencies,
            react: {
                import: 'react',
                shareKey: 'oldReact',
                shareScope: 'legacy',
                singleton: true
            },
            'react-dom': {
                import: 'react-dom', // the "react" package will be used a provided and fallback module
                shareKey: 'oldReactDom', // under this name the shared module will be placed in the share scope
                shareScope: 'legacy', // share scope with this name will be used
                singleton: true, // only a single version of the shared module is allowed
            },
            'react-router-dom': {
                import: 'react-router-dom',
                shareKey: 'oldReactRouterDom',
                shareScope: 'legacy',
                singleton: true,
            },
            'semantic-ui-react': {
                import: 'semantic-ui-react',
                shareKey: 'oldSemanticUiReact',
                shareScope: 'legacy',
                singleton: true,
            },
            'semantic-ui-css': {
                import: 'semantic-ui-css',
                shareKey: 'oldSemanticUiCss',
                shareScope: 'legacy',
                singleton: true,
            }
        }
    });

    config.plugins.push(moduleFederationPlugin);
    config.mode = 'production';

    config.devServer = {
        ...config.devServer,
        historyApiFallback: true,
    }

    config.resolve.fallback = {
        ...config.resolve.fallback,
        stream: require.resolve('stream-browserify')
    };

    config.output = {
        // Make sure to use [name] or [id] in output.filename
        //  when using multiple entry points
        ...config.output,
        publicPath: '/TEMPLATE/reampv2/packages/reampv2-app/build/',
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

    return config;
};

// Cache the override function
try {
  const resolvedPath = require.resolve(webpackConfigPath);
  require.cache[resolvedPath].exports = env => override(webpackConfig(env));
} catch (error) {
  // If caching fails, we'll still export the override function
  // This shouldn't happen if the require above succeeded, but handle it gracefully
  if (error.code === 'MODULE_NOT_FOUND') {
    console.error(`Error: Cannot resolve module '${webpackConfigPath}' for caching`);
    process.exit(1);
  }
  throw error;
}

// eslint-disable-next-line import/no-dynamic-require
module.exports = require(webpackConfigPath);

