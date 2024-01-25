// Import any necessary libraries, if needed (e.g., in a Node.js environment)
// const fetch = require('node-fetch');

// Define the function to fetch data from the endpoint
function getGisSettings() {
    const endpoint = "/rest/amp/settings/gis";

    // Return a promise
    return fetch(endpoint)
        .then(response => {
            // Check if the response status is OK (200)
            if (response.ok) {
                // Parse the JSON data from the response and return it
                return response.json();
            } else {
                // If the response status is not OK, reject the promise with an error
                throw new Error(`Failed to fetch data. Status: ${response.status}`);
            }
        })
        .catch(error => {
            // Handle any errors that may occur during the fetch
            console.error("Error fetching data:", error.message);
            // You can choose to throw the error again or handle it in a different way
            throw error;
        });
}

// Export the fetchData function
module.exports = getGisSettings;
