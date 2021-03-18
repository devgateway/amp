import {callDeleteApiEndpoint, fetchApiDataWithStatus} from "../../../utils/apiOperations";

export const FETCH_GEOCODING_PENDING = 'FETCH_GEOCODING_PENDING';
export const FETCH_GEOCODING_SUCCESS = 'FETCH_GEOCODING_SUCCESS';
export const FETCH_GEOCODING_ERROR = 'FETCH_GEOCODING_ERROR';

export const GEOCODING_LOCATION_PENDING = 'GEOCODING_LOCATION_PENDING';
export const GEOCODING_LOCATION_SUCCESS = 'GEOCODING_LOCATION_SUCCESS';
export const GEOCODING_LOCATION_ERROR = 'GEOCODING_LOCATION_ERROR';

export const GEOCODING_RESET_ALL_PENDING = 'GEOCODING_RESET_ALL_PENDING';
export const GEOCODING_RESET_ALL_SUCCESS = 'GEOCODING_RESET_ALL_SUCCESS';
export const GEOCODING_RESET_ALL_ERROR = 'GEOCODING_RESET_ALL_ERROR';

export const GEOCODING_CANCEL_PENDING = 'GEOCODING_CANCEL_PENDING';
export const GEOCODING_CANCEL_SUCCESS = 'GEOCODING_CANCEL_SUCCESS';
export const GEOCODING_CANCEL_ERROR = 'GEOCODING_CANCEL_ERROR';

export const GEOCODING_RUN_SEARCH_PENDING = 'GEOCODING_RUN_SEARCH_PENDING';
export const GEOCODING_RUN_SEARCH_SUCCESS = 'GEOCODING_RUN_SEARCH_SUCCESS';
export const GEOCODING_RUN_SEARCH_ERROR = 'GEOCODING_RUN_SEARCH_ERROR';

export const GEOCODING_SAVE_ALL_EDITS_PENDING = 'GEOCODING_SAVE_ALL_EDITS_PENDING';
export const GEOCODING_SAVE_ALL_EDITS_SUCCESS = 'GEOCODING_SAVE_ALL_EDITS_SUCCESS';
export const GEOCODING_SAVE_ALL_EDITS_ERROR = 'GEOCODING_SAVE_ALL_EDITS_ERROR';

export const GEOCODING_SAVE_ACTIVITY_PENDING = 'GEOCODING_SAVE_ACTIVITY_PENDING';
export const GEOCODING_SAVE_ACTIVITY_SUCCESS = 'GEOCODING_SAVE_ACTIVITY_SUCCESS';
export const GEOCODING_SAVE_ACTIVITY_ERROR = 'GEOCODING_SAVE_ACTIVITY_ERROR';

export const GEOCODING_RESET_SAVE_RESULTS = 'GEOCODING_RESET_SAVE_RESULTS';

export function fetchGeocodingPending() {
    return {
        type: FETCH_GEOCODING_PENDING
    }
}

export function fetchGeocodingSuccess(geocoding) {
    function geocodeShouldRun(data) {
        return data.activities && data.activities.filter(act => act.status == 'RUNNING').length > 0;
    }

    return {
        type: FETCH_GEOCODING_SUCCESS,
        payload: geocoding,
        pending: false,
        geocodeShouldRun: geocodeShouldRun(geocoding.data),
        status: 'IN_PROGRESS'
    }
}

export function fetchGeocodingNotFound(geocoding) {
    return {
        type: FETCH_GEOCODING_SUCCESS,
        payload: {},
        status: 'NOT_STARTED'
    }
}


export function fetchGeocodingError(error, status) {
    return fetchGeocodingErrorWithCode(error, status, null);
}

export function fetchGeocodingErrorWithCode(error, status, code) {
    return {
        type: FETCH_GEOCODING_ERROR,
        error: error,
        status: status,
        errorCode: code
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

export function resetAllLocationStatusesSuccess() {
    return {
        type: GEOCODING_RESET_ALL_SUCCESS,
    }
}

export function resetAllLocationStatusesError(error) {
    return {
        type: GEOCODING_RESET_ALL_ERROR,
        error: error,
    }
}

export function cancelGeocodingPending() {
    return {
        type: GEOCODING_CANCEL_PENDING,
    }
}

export function cancelGeocodingSuccess() {
    return {
        type: GEOCODING_CANCEL_SUCCESS,
    }
}

export function cancelGeocodingError(error) {
    return {
        type: GEOCODING_CANCEL_ERROR,
        error: error,
    }
}

export function saveAllEditsPending() {
    return {
        type: GEOCODING_SAVE_ALL_EDITS_PENDING,
    }
}

export function saveAllEditsSuccess() {
    return {
        type: GEOCODING_SAVE_ALL_EDITS_SUCCESS,
    }
}

export function saveAllEditsError(error) {
    return {
        type: GEOCODING_SAVE_ALL_EDITS_ERROR,
    }
}

export function saveActivityPending(activityId) {
    return {
        type: GEOCODING_SAVE_ACTIVITY_PENDING,
        payload: activityId
    }
}

export function saveActivitySuccess(activityId) {
    return {
        type: GEOCODING_SAVE_ACTIVITY_SUCCESS,
        payload: activityId
    }
}

export function saveActivityError(activityId, error) {
    return {
        type: GEOCODING_SAVE_ACTIVITY_ERROR,
        error: error,
        payload: activityId
    }
}

export function resetSaveActivitiesResults() {
    return {
        type: GEOCODING_RESET_SAVE_RESULTS,
    }
}

export function runSearchPending() {
    return {
        type: GEOCODING_RUN_SEARCH_PENDING
    }
}

export function runSearchSuccess(geocoding) {
    return {
        type: GEOCODING_RUN_SEARCH_SUCCESS,
        payload: geocoding,
        pending: false,
        status: 'IN_PROGRESS'
    }
}

export function runSearchError(error) {
    return {
        type: GEOCODING_RUN_SEARCH_ERROR,
        error: error.message,
        errorCode: error.code
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
                return dispatch(fetchGeocodingErrorWithCode(error.message, "NOT_AVAILABLE", error.code))
            });
    }
};

export const runSearch = (activityIds) => {
    return dispatch => {
        dispatch(runSearchPending());
        return fetchApiDataWithStatus({body: activityIds, url: '/rest/geo-coder/process'})
            .then(geocoding => {
                return dispatch(runSearchSuccess(geocoding));
            })
            .catch(error => {
                return dispatch(runSearchError(error))
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

export const resetAllLocationStatuses = () => {
    return dispatch => {
        dispatch(resetAllActivitiesPending());
        return fetchApiDataWithStatus({url: '/rest/geo-coder/reset-location-statuses', body: {}})
            .then(() => {
                return dispatch(resetAllLocationStatusesSuccess());
            })
            .catch(error => {
                return dispatch(resetAllLocationStatusesError(error.message))
            });
    }
};

export const cancelGeocoding = () => {
    return dispatch => {
        dispatch(cancelGeocodingPending());
        return callDeleteApiEndpoint({url: '/rest/geo-coder/process'})
            .then(() => {
                return dispatch(cancelGeocodingSuccess());
            })
            .catch(error => {
                return dispatch(cancelGeocodingError(error))
            });
    }
};

export const saveAllEdits = (activityIds) => {
    return dispatch => {
        dispatch(saveAllEditsPending());
        activityIds.forEach(activityId => dispatch(saveActivity(activityId)));
        return dispatch(saveAllEditsSuccess());
    }
};

export const saveActivity = (activityId) => {
    return dispatch => {
        dispatch(saveActivityPending(activityId));
        return fetchApiDataWithStatus({body: {}, url: '/rest/geo-coder/activity/save/' + activityId})
            .then(result => {
                return dispatch(saveActivitySuccess(activityId));
            })
            .catch(error => {
                return dispatch(saveActivityError(activityId, error.message))
            });
    }
};

export const resetSaveResults = () => {
    return dispatch => dispatch(resetSaveActivitiesResults());
};

function isGeocodingNotFound(errorObject) {
    return errorObject.status == 404;
}
