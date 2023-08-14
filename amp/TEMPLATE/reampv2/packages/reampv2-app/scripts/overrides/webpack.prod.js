const {ModuleFederationPlugin} = require('webpack').container;

const webpackConfigPath = 'react-scripts/config/webpack.config';
// eslint-disable-next-line import/no-dynamic-require
const webpackConfig = require(webpackConfigPath);
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
                singleton: true,
                requiredVersion: packageJson.dependencies.react,
            },
            'react-dom': {
                import: 'react-dom', // the "react" package will be used a provided and fallback module
                shareKey: 'react-dom', // under this name the shared module will be placed in the share scope
                shareScope: 'legacy', // share scope with this name will be used
                singleton: true, // only a single version of the shared module is allowed
            },
            'react-router-dom': {
                import: 'react-router-dom',
                shareKey: 'react-router-dom-old',
                shareScope: 'default',
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

require.cache[require.resolve(webpackConfigPath)].exports = env => override(webpackConfig(env));

// eslint-disable-next-line import/no-dynamic-require
module.exports = require(webpackConfigPath);

