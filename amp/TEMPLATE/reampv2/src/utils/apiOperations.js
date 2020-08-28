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
            });
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
                    //throw new Error(data.error);
                }
                return resolve(data);
            });
    })
};

export const fetchApiDataWithStatus = ({body, url}) => {
    return new Promise((resolve, reject) => {
        return fetch(url, getRequestOptions(body))
            .then(response => response.text().then(data => ({
                    data: data ? JSON.parse(data) : {},
                    ok : response.ok,
                    status: response.status
                })
            ).then(response => {
                if (!response.ok) {
                    return reject({status: response.status, message: extractErrorMessageFromResponse(response)});
                }

                return resolve(response);
            }));
    })
};

export function extractErrorMessageFromResponse(response) {
    let errorMessage = response.status + ": ";

    for(let k in response.data.error) {
        errorMessage += response.data.error[k][0] + "\n";
    };

    return errorMessage;
}