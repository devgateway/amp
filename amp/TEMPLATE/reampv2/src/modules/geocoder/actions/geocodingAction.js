export const FETCH_GEOCODING_PENDING = 'FETCH_GEOCODING_PENDING';
export const FETCH_GEOCODING_SUCCESS = 'FETCH_GEOCODING_SUCCESS';
export const FETCH_GEOCODING_ERROR = 'FETCH_GEOCODING_ERROR';

let geocondings = {
    1 : {
        activityId : 1,
        name: "Haiti"
    },
    2 : {
        activityId : 2,
        name: "Jacmel"
    }
}

export function fetchGeocodingPending() {
    return {
        type: FETCH_GEOCODING_PENDING
    }
}

export function fetchGeocodingSuccess(geocoding) {
    return {
        type: FETCH_GEOCODING_SUCCESS,
        payload: geocoding
    }
}

export function fetchGeocodingError(error) {
    return {
        type: FETCH_GEOCODING_ERROR,
        error: error
    }
}

export const loadGeocoding = (activityId) => {
    return dispatch => {
        dispatch(fetchGeocodingPending());
        let geocoding = geocondings[activityId];
        return dispatch(fetchGeocodingSuccess(geocoding));
        // return fetchApiData({url: '/rest/data/saikureport/8106', body: queryModel})
        //     .then(geocoding => {
        //         return dispatch(fetchGeocodingSuccess(geocoding));
        //     })
        //     .catch(error => {
        //         return dispatch(fetchGeocodingError(error))
        //     });
    }
};