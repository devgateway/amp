import { WS_PREFIX } from "../modules/sscdashboard/utils/constants";

const POST = 'POST';
const GET = 'GET';

function getRequestOptions(body) {
  const requestOptions = {
    method: body ? POST : GET,
    headers: { 'Content-Type': 'application/json', Accept: 'application/json', 'ws-prefix': WS_PREFIX }
  };
  if (body) {
    requestOptions.body = JSON.stringify(body);
  }
  return requestOptions;
}

export const fetchApiData = ({ body, url }) => new Promise((resolve, reject) => fetch(url, getRequestOptions(body))
  .then(response => response.json())
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
