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

export const GEOCODING_REMOVE_PROJECT_PENDING = 'GEOCODING_REMOVE_PROJECT_PENDING';
export const GEOCODING_REMOVE_PROJECT_SUCCESS = 'GEOCODING_REMOVE_PROJECT_SUCCESS';
export const GEOCODING_REMOVE_PROJECT_ERROR = 'GEOCODING_REMOVE_PROJECT_ERROR';

export const GEOCODING_RESET_SAVE_RESULTS = 'GEOCODING_RESET_SAVE_RESULTS';

export function fetchGeocodingPending() {
    return {
        type: FETCH_GEOCODING_PENDING
    }
}

export function fetchGeocodingSuccess(geocoding) {
    function geocodeShouldRun(data) {
        return data.activities && data.activities.filter(act => act.status === 'RUNNING').length > 0;
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

export function geocodeLocationPending(amp_id, location_id, accepted) {
    return {
        type: GEOCODING_LOCATION_PENDING,
        payload: {amp_id, location_id, accepted}
    }
}

export function geocodeLocationSuccess(amp_id, location_id, accepted) {
    return {
        type: GEOCODING_LOCATION_SUCCESS,
        payload: {amp_id, location_id, accepted}
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

export function saveActivityPending(ampId) {
    return {
        type: GEOCODING_SAVE_ACTIVITY_PENDING,
        payload: ampId
    }
}

export function saveActivitySuccess(ampId) {
    return {
        type: GEOCODING_SAVE_ACTIVITY_SUCCESS,
        payload: ampId
    }
}

export function saveActivityError(ampId, error) {
    return {
        type: GEOCODING_SAVE_ACTIVITY_ERROR,
        error: error,
        payload: ampId
    }
}

export function removeProjectPending(ampId) {
    return {
        type: GEOCODING_REMOVE_PROJECT_PENDING,
        payload: ampId
    }
}

export function removeProjectSuccess(ampId) {
    return {
        type: GEOCODING_REMOVE_PROJECT_SUCCESS,
        payload: ampId
    }
}

export function removeProjectError(error) {
    return {
        type: GEOCODING_REMOVE_PROJECT_ERROR,
        error: error
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

export const runSearch = (ampIds) => {
    return dispatch => {
        dispatch(runSearchPending());
        return fetchApiDataWithStatus({body: ampIds, url: '/rest/geo-coder/process'})
            .then(geocoding => {
                return dispatch(runSearchSuccess(geocoding));
            })
            .catch(error => {
                return dispatch(runSearchError(error))
            });
    }
};

export const geocodeLocation = (ampId, locationId, accepted) => {
    const locationStatusRequest = {
        amp_id: ampId,
        amp_category_value_location_id : locationId,
        accepted: accepted
    }

    return dispatch => {
        dispatch(geocodeLocationPending(ampId, locationId, accepted));
        return fetchApiDataWithStatus({url: '/rest/geo-coder/location-status', body: locationStatusRequest})
            .then(() => {
                return dispatch(geocodeLocationSuccess(ampId, locationId, accepted));
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

export const saveAllEdits = (ampIds) => {
    return dispatch => {
        dispatch(saveAllEditsPending());
        ampIds.forEach(ampId => dispatch(saveActivity(ampId)));
        return dispatch(saveAllEditsSuccess());
    }
};

export const saveActivity = (ampId) => {
    return dispatch => {
        dispatch(saveActivityPending(ampId));
        return fetchApiDataWithStatus({body: {}, url: '/rest/geo-coder/activity/save/' + ampId})
            .then(result => {
                return dispatch(saveActivitySuccess(ampId));
            })
            .catch(error => {
                return dispatch(saveActivityError(ampId, error.message))
            });
    }
};

export const removeProject = (ampId) => {
    return dispatch => {
        dispatch(removeProjectPending(ampId));
        return fetchApiDataWithStatus({body: {}, url: '/rest/geo-coder/activity/remove/' + ampId})
            .then(result => {
                return dispatch(removeProjectSuccess(ampId));
            })
            .catch(error => {
                return dispatch(removeProjectError(error.message))
            });
    }
};

export const resetSaveResults = () => {
    return dispatch => dispatch(resetSaveActivitiesResults());
};

function isGeocodingNotFound(errorObject) {
    return errorObject.status === 404;
}
