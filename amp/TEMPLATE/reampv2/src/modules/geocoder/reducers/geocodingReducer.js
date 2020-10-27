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
    GEOCODING_RUN_SEARCH_ERROR,
    GEOCODING_RUN_SEARCH_PENDING,
    GEOCODING_RUN_SEARCH_SUCCESS, GEOCODING_SAVE_ALL_EDITS_ERROR,
    GEOCODING_SAVE_ALL_EDITS_PENDING,
    GEOCODING_SAVE_ALL_EDITS_SUCCESS
} from '../actions/geocodingAction';

const initialState = {
    pending: false,
    reset_pending: false,
    running: false,
    status : "NOT_STARTED",
    creator: null,
    workspace: null,
    activities : [],
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
                error: null
            };
        case FETCH_GEOCODING_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error,
                geocodeShouldRun: false,
                status : action.status,
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
                error: null
            };
        case GEOCODING_SAVE_ALL_EDITS_SUCCESS:
            return {
                ...state,
                error: null,
                pending: false,
            };
        case GEOCODING_SAVE_ALL_EDITS_ERROR:
            return {
                ...state,
                error: action.error,
            };
        default:
            return state;
    }
}

function updateActivity(activities, action) {
    return activities.map( (item, id) => {
        if(item.activity_id === action.payload.activity_id) {
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