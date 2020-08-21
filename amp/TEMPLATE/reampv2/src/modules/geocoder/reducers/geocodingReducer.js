import {
    FETCH_GEOCODING_ERROR,
    FETCH_GEOCODING_PENDING,
    FETCH_GEOCODING_SUCCESS,
    GEOCODING_LOCATION_ERROR,
    GEOCODING_LOCATION_PENDING,
    GEOCODING_LOCATION_SUCCESS,
    GEOCODING_RESET_ALL_ERROR,
    GEOCODING_RESET_ALL_PENDING,
    GEOCODING_RESET_ALL_SUCCESS, GEOCODING_RUN_SEARCH_ERROR,
    GEOCODING_RUN_SEARCH_PENDING, GEOCODING_RUN_SEARCH_SUCCESS
} from '../actions/geocodingAction';

const initialState = {
    pending: true,
    reset_pending: false,
    run_search_pending: false,
    running: false,
    status : "NOT_STARTED",
    creator: null,
    workspace: null,
    activities : [],
    error: null,
    run_search_error: null
};

export default function geocodingReducer(state = initialState, action) {
    switch(action.type) {
        case FETCH_GEOCODING_PENDING:
            return {
                ...state,
                pending: true,
            };
        case FETCH_GEOCODING_SUCCESS:
            return {
                ...state,
                ...action.payload.data,
                pending: false,
                error: null
            };
        case FETCH_GEOCODING_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error,
                status : action.status,
            };
        case GEOCODING_RUN_SEARCH_PENDING:
            return {
                ...state,
                run_search_pending: true,
            };
        case GEOCODING_RUN_SEARCH_SUCCESS:
            return {
                ...state,
                run_search_pending: false,
            };
        case GEOCODING_RUN_SEARCH_ERROR:
            return {
                ...state,
                run_search_pending: false,
                run_search_error: action.error
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
                reset_pending: true,
            };
        case GEOCODING_RESET_ALL_SUCCESS:
            return {
                ...state,
                reset_pending: false,
                activities: resetAll(state.activities)
            };
        case GEOCODING_RESET_ALL_ERROR:
            return {
                ...state,
                reset_pending: false,
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
                status: action.payload.status,
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
            status: null,
            pending: false
        };
    });
}