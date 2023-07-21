const { dependencies } = require('./package.json');

module.exports = {
    name: 'reampv2',
    filename: 'remoteEntry.js',
    exposes: {
        './Reampv2': './src/index.js'
    },
    shared: {
        ...dependencies,
        react: {
            singleton: true,
            requiredVersion: dependencies['react'],
        },
        'react-dom': {
            singleton: true,
            requiredVersion: dependencies['react-dom'],
        },
    },
};
