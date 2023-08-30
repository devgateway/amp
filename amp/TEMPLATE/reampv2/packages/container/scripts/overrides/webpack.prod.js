const {ModuleFederationPlugin} = require('webpack').container;

const webpackConfigPath = 'react-scripts/config/webpack.config';
// eslint-disable-next-line import/no-dynamic-require
const webpackConfig = require(webpackConfigPath);
const ppackageJson = require("../../package.json");

const DOMAIN_NAME = process.env.AMP_URL || 'http://localhost:8080';
const PUBLIC_PATH = '/TEMPLATE/reampv2/packages/container/build/';

const override = config => {
    // eslint-disable-next-line global-require
    const moduleFederationPlugin = new ModuleFederationPlugin({
        name: 'container',
        filename: 'remoteEntry.js',
        remotes: {
            'ampoffline': `ampoffline@${DOMAIN_NAME}/TEMPLATE/reampv2/packages/ampoffline/build/remoteEntry.js`,
            'reampv2App': `reampv2App@${DOMAIN_NAME}/TEMPLATE/reampv2/packages/reampv2-app/build/remoteEntry.js`,
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
                singleton: true,
                requiredVersion: ppackageJson.dependencies['react-dom'],
            },
            'react-router-dom': {
                import: 'react-router-dom',
                singleton: true,
                shareKey: 'react-router-dom-new',
                shareScope: 'default',
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

