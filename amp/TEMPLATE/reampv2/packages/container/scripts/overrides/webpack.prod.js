const {ModuleFederationPlugin} = require('webpack').container;
const Dotenv = require('dotenv-webpack');

const webpackConfigPath = 'react-scripts/config/webpack.config';
// eslint-disable-next-line import/no-dynamic-require
const webpackConfig = require(webpackConfigPath);
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

require.cache[require.resolve(webpackConfigPath)].exports = env => override(webpackConfig(env));

// eslint-disable-next-line import/no-dynamic-require
module.exports = require(webpackConfigPath);

