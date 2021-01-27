import { getRequestOptions } from './apiOperations';

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
