
    const { dependencies } = require('./package.json');

    module.exports = {
        name: 'container',
        filename: 'remoteEntry.js',
        remotes: {
            ampoffline: 'ampoffline@http://localhost:3001/remoteEntry.js',
        },
        shared: {
            ...dependencies,
            react: {
                singleton: true,
                requiredVersion: dependencies.react,
            },
            'react-dom': {
                singleton: true,
                requiredVersion: dependencies['react-dom'],
            },
        },
    };
