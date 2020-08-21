import {fetchApiData, fetchApiDataWithStatus} from "../../../utils/apiOperations";

export const FETCH_GEOCODING_PENDING = 'FETCH_GEOCODING_PENDING';
export const FETCH_GEOCODING_SUCCESS = 'FETCH_GEOCODING_SUCCESS';
export const FETCH_GEOCODING_ERROR = 'FETCH_GEOCODING_ERROR';

export const GEOCODING_LOCATION_PENDING = 'GEOCODING_LOCATION_PENDING';
export const GEOCODING_LOCATION_SUCCESS = 'GEOCODING_LOCATION_SUCCESS';
export const GEOCODING_LOCATION_ERROR = 'GEOCODING_LOCATION_ERROR';

export const GEOCODING_RESET_ALL_PENDING = 'GEOCODING_RESET_ALL_PENDING';
export const GEOCODING_RESET_ALL_SUCCESS = 'GEOCODING_RESET_ALL_SUCCESS';
export const GEOCODING_RESET_ALL_ERROR = 'GEOCODING_RESET_ALL_ERROR';

export const GEOCODING_RUN_SEARCH_PENDING = 'GEOCODING_RUN_SEARCH_PENDING';
export const GEOCODING_RUN_SEARCH_SUCCESS = 'GEOCODING_RUN_SEARCH_SUCCESS';
export const GEOCODING_RUN_SEARCH_ERROR = 'GEOCODING_RUN_SEARCH_ERROR';

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
                    accepted: true,
                },
                {  "id": 3268,
                    "name": "Jacmel",
                    "administrative_level": "Region",
                    "fields": [
                    {"field_name": "objective", "text": "Project objective Jacmel"}
                ],
                    accepted: false
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
                    accepted: null
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

export function fetchGeocodingNotFound(geocoding) {
    return {
        type: FETCH_GEOCODING_SUCCESS,
        error: null,
        status: 'NOT_STARTED'
    }
}


export function fetchGeocodingError(error, status) {
    return {
        type: FETCH_GEOCODING_ERROR,
        error: error,
        status: status
    }
}

export function geocodeLocationPending(activity_id, location_id, accepted) {
    return {
        type: GEOCODING_LOCATION_PENDING,
        payload: {
            activity_id: activity_id,
            location_id: location_id,
            accepted: accepted
        }
    }
}

export function geocodeLocationSuccess(activity_id, location_id, accepted) {
    return {
        type: GEOCODING_LOCATION_SUCCESS,
        payload: {
            activity_id: activity_id,
            location_id: location_id,
            accepted: accepted
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
    }
}

export function resetAllActivitiesSuccess() {
    return {
        type: GEOCODING_RESET_ALL_SUCCESS,
    }
}

export function resetAllActivitiesError(error) {
    return {
        type: GEOCODING_RESET_ALL_ERROR,
        error: error,
    }
}

export function runSearchPending() {
    return {
        type: GEOCODING_RUN_SEARCH_PENDING,
    }
}

export function runSearchSuccess() {
    return {
        type: GEOCODING_RUN_SEARCH_SUCCESS,
    }
}

export function runSearchError(error) {
    return {
        type: GEOCODING_RUN_SEARCH_ERROR,
        error: error,
    }
}

export const loadGeocoding = () => {
    return dispatch => {
        dispatch(fetchGeocodingPending());
        return fetchApiDataWithStatus({url: '/rest/geo-coder/process'})
            .then(geocoding => {
                return dispatch(fetchGeocodingSuccess(geocoding));
            })
            .catch(error => {
                if(isGeocodingNotFound(error)) {
                    return dispatch(fetchGeocodingNotFound(null));
                }
                return dispatch(fetchGeocodingError(error.message, "NOT_AVAILABLE"))
            });
    }
};

export const runSearch = (activityIds) => {
    return dispatch => {
        dispatch(runSearchPending());
        return fetchApiDataWithStatus({body: activityIds, url: '/rest/geo-coder/process'})
            .then(() => {
                return dispatch(runSearchSuccess());
            })
            .catch(error => {
                return dispatch(runSearchError(error.message))
            });
    }
};

export const geocodeLocation = (activityId, locationId, accepted) => {
    const locationStatusRequest = {
        amp_activity_id: activityId,
        amp_category_value_location_id : locationId,
        accepted: accepted
    }

    return dispatch => {
        dispatch(geocodeLocationPending(activityId, locationId, accepted));
        return fetchApiDataWithStatus({url: '/rest/geo-coder/location-status', body: locationStatusRequest})
            .then(() => {
                return dispatch(geocodeLocationSuccess(activityId, locationId, accepted));
            })
            .catch(error => {
                return dispatch(geocodeLocationError(error.message))
            });
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

function isGeocodingNotFound(errorObject) {
    return errorObject.status == 404;
}
