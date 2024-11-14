const API_ROOT='';
const NEW_REPORT_API='/rest/new_report';
const OPTIONS_URL='/options';
export const loadReportData = async (size, page, filters) => {
    filters['size'] = size+'';
    filters['page'] = page+'';
    return post(`${NEW_REPORT_API}`,filters,false);
};

export const loadFilterOptions = async (type) => {
    const params = new URLSearchParams({});
    params.append('type', type);
    return get(`${NEW_REPORT_API}${OPTIONS_URL}?${params.toString()}`);
};


 const post = (url, params, isBlob) => {

    return new Promise((resolve, reject) => {
        fetch(url, {

            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/json'
            },
            method: 'POST',
            body: JSON.stringify(params)
        })
            .then(
                function (response) {
                    if (response.status !== 200) {
                        reject(response)
                    }
                    if (isBlob) {
                        resolve(response.blob())
                    }
                    response.json().then(function (data) {
                        resolve(data)
                    }).catch(() => resolve(response.status))
                }
            )
            .catch(function (err) {
                reject(err)
            })
    })
}
 const get = (url, params = {}) => {
    return new Promise((resolve, reject) => {

        fetch(url,{ headers: {
                Accept: 'application/json',
                'Content-Type': 'application/json'
            },
        })
            .then(
                function (response) {
                    if (response.status !== 200) {
                        reject(response)
                    }
                    response.json().then(function (data) {
                        resolve(data)
                    })
                }
            )
            .catch(function (err) {
                reject(err)
            })
    })
}
