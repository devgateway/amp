const {ModuleFederationPlugin} = require('webpack').container;
const packageJson = require('../../package.json');

const webpackConfigPath = 'react-scripts/config/webpack.config';
// eslint-disable-next-line import/no-dynamic-require
const webpackDev = require(webpackConfigPath);

const override = config => {
    // eslint-disable-next-line global-require
    const moduleFederationPlugin = new ModuleFederationPlugin({
        name: 'reampv2App',
        filename: 'remoteEntry.js',
        exposes: {
            './Reampv2App': './src/bootstrap'
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
    })

    config.plugins.push(moduleFederationPlugin);

    config.output.publicPath = 'http://localhost:3002/';

    config.devServer = {
        ...config.devServer,
        historyApiFallback: true,
    }

    config.resolve.fallback = {
        ...config.resolve.fallback,
        stream: require.resolve('stream-browserify'),
        fs: false,
        os: false,
        path: false
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

require.cache[require.resolve(webpackConfigPath)].exports = env => override(webpackDev(env));

// eslint-disable-next-line import/no-dynamic-require
module.exports = require(webpackConfigPath);
