const POST = 'POST';
const GET = 'GET';
const DELETE = 'DELETE';

export function getRequestOptions(body) {
    const requestOptions = {
        method: body ? POST : GET,
        headers: {'Content-Type': 'application/json', 'Accept': 'application/json'}
    };
    if (body) {
        requestOptions.body = JSON.stringify(body);
    }
    return requestOptions;
}

export const callDeleteApiEndpoint = ({url}) => {
    const deleteRequestOptions = {
        method: DELETE,
        headers: {'Content-Type': 'application/json', 'Accept': 'application/json'}
    }

    return new Promise((resolve, reject) => {
        return fetch(url, deleteRequestOptions)
            .then(data => {
                if (data.error) {
                    return reject(data.error);
                }
                return resolve(data);
            }).catch(error => reject(error));
    })
};

export const fetchApiData = ({body, url}) => {
    return new Promise((resolve, reject) => {
        return fetch(url, getRequestOptions(body))
            .then(response => {
                return response.json();
            })
            .then(data => {
                if (data.error) {
                    return reject(data.error);
                }
                return resolve(data);
            }).catch(error => reject(error));
    })
};

export const fetchApiDataWithStatus = ({body, url}) => {
    return new Promise((resolve, reject) => {
        return fetch(url, getRequestOptions(body))
            .then(response => response.text().then(data => {
                let contentType = response.headers.get("content-type");
                let parsedData = data;
                if (data && contentType && contentType.indexOf("application/json") !== -1) {
                    parsedData = JSON.parse(data);
                }
                return ({
                    data: parsedData,
                    ok : response.ok,
                    status: response.status
                })}
            ).then(response => {
                if (!response.ok) {
                    return reject({
                        status: response.status,
                        message: extractErrorMessageFromResponse(response),
                        code: extractErrorCodeFromResponse(response)
                    });
                }
                return resolve(response);
            })).catch(error => reject(error));
    })
};

export function extractErrorMessageFromResponse(response) {
    let errorMessage = "";
    if (response.data && response.data.error) {
        for (let k in response.data.error) {
            errorMessage += JSON.stringify(response.data.error[k]);
        }
    } else {
        errorMessage = response.data
    }

    return errorMessage;
}

export function extractErrorCodeFromResponse(response) {
    if (response.data && response.data.error) {
        for (let k in response.data.error) {
            return k;
        }
    }
    return response.data;
}