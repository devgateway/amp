let cachedGisSettings = null;

function getGisSettings() {
    if (cachedGisSettings !== null) {
        return Promise.resolve(cachedGisSettings);
    }

    // Return the fetch Promise
    return fetch('/rest/amp/settings/gis')
        .then(response => {
            if (!response.ok) {
                throw new Error('Response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Cache the GIS settings
            cachedGisSettings = data;

            // Resolve the promise with the JSON response
            return data;
        })
        .catch(error => {
            // Reject the promise with the error
            console.log(error);
            throw error; // Re-throw the error to continue propagating it
        });
}

module.exports = getGisSettings;
