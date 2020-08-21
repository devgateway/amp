const POST = 'POST';
const GET = 'GET';

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
            .then(response => response.json().then(data => ({
                    data: data,
                    ok : response.ok,
                    status: response.status
                })
            ).then(res => {
                if (!res.ok) {
                    return reject({status: res.status, message: extractErrorMessageFromResponse(res)});
                }

                return resolve(res);
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