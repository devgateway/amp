const { ModuleFederationPlugin } = require('webpack').container;

const webpackConfigPath = 'react-scripts/config/webpack.config';
const webpackConfig = require(webpackConfigPath);

const override = config => {
    config.plugins.push(new ModuleFederationPlugin(require('../../module-federation.config')));

    config.output.publicPath = 'http://localhost:3000/';

    config.resolve.fallback = {
        ...config.resolve.fallback,
        stream: require.resolve("stream-browserify")
    }

    return config;
};

require.cache[require.resolve(webpackConfigPath)].exports = env => override(webpackConfig(env));

module.exports = require(webpackConfigPath);
