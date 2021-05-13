const POST = 'POST';
const GET = 'GET';

function getRequestOptions(body, headers) {
  const requestOptions = {
    method: body ? POST : GET,
    headers: headers || { 'Content-Type': 'application/json', Accept: 'application/json' }
  };
  if (body) {
    requestOptions.body = JSON.stringify(body);
  }
  return requestOptions;
}

export const fetchApiData = ({ body, url, headers }) => new Promise((resolve, reject) => fetch(url,
  getRequestOptions(body, headers))
  .then(response => (headers && headers.Accept === 'text/html' ? response.text() : response.json()))
  .then(data => {
    if (data.error) {
      return reject(data.error);
      // throw new Error(data.error);
    }
    return resolve(data);
  }));

// TODO to move api route to a constant.
export function loadTranslations(trnPack) {
  return new Promise((resolve) => fetch('/rest/translations/label-translations', getRequestOptions(trnPack))
    .then(response => response.json())
    .then(data => {
      if (data.error) {
        throw new Error(data.error);
      }
      return resolve(data);
    }));
}
