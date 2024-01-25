let cachedGisSettings = null;

function getGisSettings() {
    return new Promise((resolve, reject) => {
        if (cachedGisSettings !== null) {
            resolve(cachedGisSettings);
            return;
        }

        fetch('/rest/amp/settings/gis')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // Cache the GIS settings
                cachedGisSettings = data;

                // Resolve the promise with the JSON response
                resolve(data);
            })
            .catch(error => {
                // Reject the promise with the error
                reject(error);
            });
    });
}

module.exports = getGisSettings;