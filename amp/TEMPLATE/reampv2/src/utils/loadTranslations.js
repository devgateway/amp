function getRequestOptions(body) {
    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Accept': 'application/json'},
        body: JSON.stringify(body)
    };
    return requestOptions;
}

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
