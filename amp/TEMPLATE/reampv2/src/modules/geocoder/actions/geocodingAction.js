export const FETCH_GEOCODING_PENDING = 'FETCH_GEOCODING_PENDING';
export const FETCH_GEOCODING_SUCCESS = 'FETCH_GEOCODING_SUCCESS';
export const FETCH_GEOCODING_ERROR = 'FETCH_GEOCODING_ERROR';

export const GEOCODING_LOCATION_PENDING = 'GEOCODING_LOCATION_PENDING';
export const GEOCODING_LOCATION_SUCCESS = 'GEOCODING_LOCATION_SUCCESS';
export const GEOCODING_LOCATION_ERROR = 'GEOCODING_LOCATION_ERROR';

export const GEOCODING_RESET_ALL_PENDING = 'GEOCODING_RESET_ALL_PENDING';
export const GEOCODING_RESET_ALL_SUCCESS = 'GEOCODING_RESET_ALL_SUCCESS';
export const GEOCODING_RESET_ALL_ERROR = 'GEOCODING_RESET_ALL_ERROR';

let geocoding_available = {
    status : "AVAILABLE",
    activities : [{
            activity_id : 1,
            project_date: "23/02/20116",
            project_number: "XYZ-65-65",
            project_title: "Emergency Spending Allocation - (BID/HA-G1001)",
            location: "---",
            locations: [
                {"id": 3265,
                "name": "Haiti",
                "administrative_level": "Country",
                "fields": [
                    {"field_name": "project_title", "text": "Project title Haiti"},
                    {"field_name": "description", "text": "Project description Haiti etc."}
                ],
                "status": "ACCEPTED",
                },
                {  "id": 3268,
                    "name": "Jacmel",
                    "administrative_level": "Region",
                    "fields": [
                    {"field_name": "objective", "text": "Project objective Jacmel"}
                ],
                    "status": "REJECTED"
                }
            ]
        },
        {
            activity_id : 2,
            project_date: "23/02/2014",
            project_number: "XYZ-65-68",
            project_title: "Development of the Industrial Park Model to Improve Trade Opportunities for Jacmel",
            location: "---",
            locations: [
                {"id": 3265,
                    "name": "Haiti",
                    "administrative_level": "Country",
                    "fields": [
                        {"field_name": "project_title", "text": "Project title Haiti"},
                        {"field_name": "description", "text": "Project description Haiti etc."}
                    ],
                    "status": null
                }
            ]
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

export function geocodeLocationPending(activity_id, location_id, status) {
    return {
        type: GEOCODING_LOCATION_PENDING,
        payload: {
            activity_id: activity_id,
            location_id: location_id,
            status: status
        }
    }
}

export function geocodeLocationSuccess(activity_id, location_id, status) {
    return {
        type: GEOCODING_LOCATION_SUCCESS,
        payload: {
            activity_id: activity_id,
            location_id: location_id,
            status: status
        }
    }
}

export function geocodeLocationError(error) {
    return {
        type: GEOCODING_LOCATION_ERROR,
        error: error
    }
}

export function resetAllActivitiesPending() {
    return {
        type: GEOCODING_RESET_ALL_PENDING,
        pending: true
    }
}

export function resetAllActivitiesSuccess() {
    return {
        type: GEOCODING_RESET_ALL_SUCCESS,
        pending: false
    }
}

export function resetAllActivitiesError(error) {
    return {
        type: GEOCODING_RESET_ALL_ERROR,
        error: error,
        pending: false
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

export const geocodeLocation = (activityId, locationId, status) => {
    return dispatch => {
        dispatch(geocodeLocationPending(activityId, locationId, status));
        return dispatch(geocodeLocationSuccess(activityId, locationId, status));
        // return fetchApiData({url: '/rest/geocoding/results', body: queryModel})
        //     .then(geocoding => {
        //         return dispatch(fetchGeocodingSuccess(geocoding));
        //     })
        //     .catch(error => {
        //         return dispatch(fetchGeocodingError(error))
        //     });
    }
};

export const resetAllActivities = () => {
    return dispatch => {
        dispatch(resetAllActivitiesPending());
        return dispatch(resetAllActivitiesSuccess());
        // return fetchApiData({url: '/rest/geocoding/results', body: queryModel})
        //     .then(geocoding => {
        //         return dispatch(fetchGeocodingSuccess(geocoding));
        //     })
        //     .catch(error => {
        //         return dispatch(fetchGeocodingError(error))
        //     });
    }
};