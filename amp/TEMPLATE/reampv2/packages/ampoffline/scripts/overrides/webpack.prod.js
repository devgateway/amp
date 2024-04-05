const {ModuleFederationPlugin} = require('webpack').container;

const webpackConfigPath = 'react-scripts/config/webpack.config';
// eslint-disable-next-line import/no-dynamic-require
const webpackConfig = require(webpackConfigPath);
const packageJson = require("../../package.json");

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
        publicPath: '/TEMPLATE/reampv2/packages/ampoffline/build/',
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

