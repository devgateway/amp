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