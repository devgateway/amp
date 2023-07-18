const {ModuleFederationPlugin} = require('webpack').container;

const webpackConfigPath = 'react-scripts/config/webpack.config';
// eslint-disable-next-line import/no-dynamic-require
const webpackConfig = require(webpackConfigPath);
const ppackageJson = require("../../package.json");

const DOMAIN_NAME = 'http://localhost:8080';
const PUBLIC_PATH = '/TEMPLATE/reampv2/packages/container/build/';

const override = config => {
    // eslint-disable-next-line global-require
    const moduleFederationPlugin = new ModuleFederationPlugin({
        name: 'container',
        filename: 'remoteEntry.js',
        remotes: {
            'ampoffline': `ampoffline@${DOMAIN_NAME}/TEMPLATE/reampv2/packages/ampoffline/build/remoteEntry.js`,
        },
        shared: {
            ...ppackageJson.dependencies,
            react: {
                singleton: true,
                requiredVersion: ppackageJson.dependencies.react,
            },
            'react-dom': {
                singleton: true,
                requiredVersion: ppackageJson.dependencies['react-dom'],
            },
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

