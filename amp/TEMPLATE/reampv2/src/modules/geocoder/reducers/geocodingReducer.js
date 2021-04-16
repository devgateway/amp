import {
    FETCH_GEOCODING_ERROR,
    FETCH_GEOCODING_PENDING,
    FETCH_GEOCODING_SUCCESS,
    GEOCODING_CANCEL_ERROR,
    GEOCODING_CANCEL_PENDING,
    GEOCODING_CANCEL_SUCCESS,
    GEOCODING_LOCATION_ERROR,
    GEOCODING_LOCATION_PENDING,
    GEOCODING_LOCATION_SUCCESS,
    GEOCODING_RESET_ALL_ERROR,
    GEOCODING_RESET_ALL_PENDING,
    GEOCODING_RESET_ALL_SUCCESS,
    GEOCODING_RUN_SEARCH_SUCCESS,
    GEOCODING_SAVE_ALL_EDITS_ERROR,
    GEOCODING_SAVE_ALL_EDITS_PENDING,
    GEOCODING_SAVE_ALL_EDITS_SUCCESS,
    GEOCODING_SAVE_ACTIVITY_ERROR,
    GEOCODING_SAVE_ACTIVITY_PENDING,
    GEOCODING_SAVE_ACTIVITY_SUCCESS,
    GEOCODING_RESET_SAVE_RESULTS,
    GEOCODING_RUN_SEARCH_ERROR,
    GEOCODING_REMOVE_PROJECT_PENDING, GEOCODING_REMOVE_PROJECT_SUCCESS, GEOCODING_REMOVE_PROJECT_ERROR
} from '../actions/geocodingAction';

const initialState = {
    pending: false,
    reset_pending: false,
    running: false,
    status : null,
    creator: null,
    workspace: null,
    activities : [],
    save_activities_result: [],
    save_pending: false,
    error: null,
    reset_error: null,
    geocodeShouldRun: false
};

export default function geocodingReducer(state = initialState, action) {
    switch(action.type) {
        case FETCH_GEOCODING_PENDING:
            return {
                ...state,
                geocodeShouldRun: false,
                pending: true,
            };
        case FETCH_GEOCODING_SUCCESS:
            return {
                ...state,
                ...action.payload.data,
                status: action.status,
                pending: action.pending,
                geocodeShouldRun: action.geocodeShouldRun,
                error: null,
                errorCode: null
            };
        case FETCH_GEOCODING_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error,
                geocodeShouldRun: false,
                status : action.status,
                errorCode: action.errorCode
            };
        case GEOCODING_RUN_SEARCH_SUCCESS:
            return {
                ...state,
                ...action.payload.data,
                status: action.status,
                pending: false,
                geocodeShouldRun: true,
                error: null
            };
        case GEOCODING_LOCATION_PENDING:
            return {
                ...state,
                activities: updateActivity(state.activities, action)
            };
        case GEOCODING_LOCATION_SUCCESS:
            return {
                ...state,
                pending: false,
                activities: updateActivity(state.activities, action)
            };
        case GEOCODING_LOCATION_ERROR:
            return {
                ...state,
                pending: false,
                activities: updateActivity(state.activities, action)
            };
        case GEOCODING_RESET_ALL_PENDING:
            return {
                ...state,
                pending: true,
                error: null
            };
        case GEOCODING_RESET_ALL_SUCCESS:
            return {
                ...state,
                pending: false,
                activities: resetAll(state.activities)
            };
        case GEOCODING_RESET_ALL_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error,
            };
        case GEOCODING_CANCEL_PENDING:
            return {
                ...state,
                pending: true,
                error: null
            };
        case GEOCODING_CANCEL_SUCCESS:
            return {
                ...state,
                status : "NOT_STARTED",
                error: null,
                pending: false,
                activities : []
            };
        case GEOCODING_CANCEL_ERROR:
            return {
                ...state,
                error: action.error,
            };
        case GEOCODING_SAVE_ALL_EDITS_PENDING:
            return {
                ...state,
                pending: true,
                save_pending: true,
                error: null
            };
        case GEOCODING_SAVE_ALL_EDITS_SUCCESS:
            return {
                ...state,
                error: null,
                save_pending: false,
                pending: false,
                geocodeShouldRun: true,
            };
        case GEOCODING_SAVE_ALL_EDITS_ERROR:
            return {
                ...state,
                save_pending: false,
                error: action.error,
                geocodeShouldRun: true,
            };
        case GEOCODING_SAVE_ACTIVITY_PENDING:
            return {
                ...state,
                save_activities_result: updateActivitySaveStatus(state.save_activities_result, action.payload, true, null),
            };
        case GEOCODING_SAVE_ACTIVITY_SUCCESS:
            return {
                ...state,
                save_activities_result: updateActivitySaveStatus(state.save_activities_result, action.payload, false, null),
                activities: removeProject(state.activities, action.payload)
            };
        case GEOCODING_SAVE_ACTIVITY_ERROR:
            return {
                ...state,
                save_activities_result: updateActivitySaveStatus(state.save_activities_result, action.payload, false, action.error),
            };
        case GEOCODING_RESET_SAVE_RESULTS:
            return {
                ...state,
                save_activities_result: [],
            };
        case GEOCODING_RUN_SEARCH_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error,
                errorCode: action.errorCode
            };
        case GEOCODING_REMOVE_PROJECT_PENDING:
            return {
                ...state,
                pending: true,
                error: null
            };
        case GEOCODING_REMOVE_PROJECT_SUCCESS:
            return {
                ...state,
                pending: false,
                activities: removeProject(state.activities, action.payload),
            };
        case GEOCODING_REMOVE_PROJECT_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error,
                errorCode: action.errorCode
            };
        default:
            return state;
    }
}

function updateActivity(activities, action) {
    return activities.map((item, id) => {
        if(item.amp_id === action.payload.amp_id) {
            return {
                ...item,
                locations: updateLocation(item.locations, action)
            };
        }

        return item;
    });
}

function updateLocation(locations, action) {
    return locations.map((item, id) => {
        if(item.id === action.payload.location_id) {
            return {
                ...item,
                accepted: action.payload.accepted,
                pending: false
            };
        }

        return item;
    });
}

function resetAll(activities) {
    return activities.map((item, id) => {
        return {
            ...item,
            locations: resetLocations(item.locations)
        };
    });
}

function resetLocations(locations) {
    return locations.map((item, id) => {
        return {
            ...item,
            accepted: null,
            pending: false
        };
    });
}

function updateActivitySaveStatus(activities, ampId, pending, error) {
    if (!activities.find(item => item.amp_id === ampId)) {
        activities.push({
            amp_id : ampId,
            pending: pending,
            error: error
        });
        return activities;
    }

    return activities.map((item, id) => {
        if(item.amp_id === ampId) {
            return {
                ...item,
                pending: pending,
                error: error
            };
        }
        return item;
    });
}

function removeProject(activities, ampId) {
    return activities.filter(item => item.amp_id !== ampId);
}