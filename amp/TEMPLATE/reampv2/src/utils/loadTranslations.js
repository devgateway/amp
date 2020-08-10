import { fetchApiData, getRequestOptions } from './apiOperations';

export function loadTranslations(trnPack, lang) {
    return new Promise((resolve, reject) => {
        return fetch('/rest/translations/label-translations', getRequestOptions(trnPack))
            .then(response => {
                return response.json();
            })
            .then(data => {
                if (data.error) {
                    throw new Error(data.error);
                }
                return resolve(data);
            });
    })
}
