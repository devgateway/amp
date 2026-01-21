const {ModuleFederationPlugin} = require('webpack').container;
const Dotenv = require('dotenv-webpack');

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

const ppackageJson = require("../../package.json");

const DOMAIN_NAME = process.env.AMP_URL;
const PUBLIC_PATH = '/TEMPLATE/reampv2/packages/container/build/';

const override = config => {
    const dotenvPlugin = new Dotenv({
        systemvars: true
    });
    config.plugins.push(dotenvPlugin);
    // eslint-disable-next-line global-require
    const moduleFederationPlugin = new ModuleFederationPlugin({
        name: 'container',
        filename: 'remoteEntry.js',
        remotes: {
            'ampoffline': `ampoffline@/TEMPLATE/reampv2/packages/ampoffline/build/remoteEntry.js`,
            'reampv2App': `reampv2App@/TEMPLATE/reampv2/packages/reampv2-app/build/remoteEntry.js`,
            'userManager': `userManager@/TEMPLATE/reampv2/packages/user-manager/build/remoteEntry.js`
        },
        shared: {
            ...ppackageJson.dependencies,
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
                singleton: true
            }
        },
    });
    config.plugins.push(moduleFederationPlugin);

    config.mode = 'production';

    config.devServer = {
        ...config.devServer,
        historyApiFallback: true,
    }

    config.output = {
        // Make sure to use [name] or [id] in output.filename
        //  when using multiple entry points
        ...config.output,
        publicPath: PUBLIC_PATH,
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

