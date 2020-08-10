export const FETCH_GEOCODING_PENDING = 'FETCH_GEOCODING_PENDING';
export const FETCH_GEOCODING_SUCCESS = 'FETCH_GEOCODING_SUCCESS';
export const FETCH_GEOCODING_ERROR = 'FETCH_GEOCODING_ERROR';

let geocoding_available = {
    status : "AVAILABLE",
    activities : [{
            activityId : 1,
            col1: "23/02/20116",
            col2: "XYZ-65-65",
            col3: "Emergency Spending Allocation - (BID/HA-G1001)",
            col4: "---"
        },
        {
            activityId : 1,
            col1: "23/02/2014",
            col2: "XYZ-65-68",
            col3: "Development of the Industrial Park Model to Improve Trade Opportunities for Jacmel",
            col4: "---"
        }
    ]
}

let geocoding_not_available = {
    status : "NOT_AVAILABLE",
    creator: "John Doe",
    workspace: "Training Workspace",
    activities : []
}

let geocoding_completed = {
    status : "COMPLETED",
    creator: null,
    workspace: null,
    activities : []
}

let geocoding_running = {
    status : "RUNNING",
    creator: "John Doe",
    workspace: "Training Workspace",
    activities : []
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

export const loadGeocoding = () => {
    return dispatch => {
        dispatch(fetchGeocodingPending());
        return dispatch(fetchGeocodingSuccess(geocoding_completed));
        // return fetchApiData({url: '/rest/geocoding/results', body: queryModel})
        //     .then(geocoding => {
        //         return dispatch(fetchGeocodingSuccess(geocoding));
        //     })
        //     .catch(error => {
        //         return dispatch(fetchGeocodingError(error))
        //     });
    }
};