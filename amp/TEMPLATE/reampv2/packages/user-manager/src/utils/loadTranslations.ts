const POST = 'POST';
const GET = 'GET';
export const WS_PREFIX = 'AMP_';

interface RequestParams<T, U> {
    method: 'POST' | 'GET'| 'PUT' | 'DELETE' | 'PATCH';
    body?: T
    headers?: Record<string, U>;
}

function getRequestOptions(body : Record<string, any>, headers: Record<string, any> | null) {
  const requestOptions: RequestParams<string, string> = {
    method: body ? POST : GET,
    headers: headers || { 'Content-Type': 'application/json', Accept: 'application/json', 'ws-prefix': WS_PREFIX }
  };
  if (body) {
    requestOptions.body = JSON.stringify(body);
  }
  return requestOptions;
}

export function loadTranslations(trnPack: any) {
  return new Promise((resolve) => {
    fetch('/rest/translations/label-translations', getRequestOptions(trnPack, null))
      .then((response) => {
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.indexOf('application/json') !== -1) {
          return response.json().then((data) => {
            if (data.error) {
              throw new Error(data.error);
            }
            return resolve(data);
          });
        }
      });
  });
}
