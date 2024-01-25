// Import any necessary libraries, if needed (e.g., in a Node.js environment)
// const fetch = require('node-fetch');

// Define the function to fetch data from the endpoint
export function getGisSettings() {
    return new Promise((resolve, reject) => {
        fetch('/rest/amp/settings/gis')
            .then(response => response.json())
            .then(data => resolve(data))
            .catch(error => reject(error));
    });
}

// module.exports = getGisSettings;
