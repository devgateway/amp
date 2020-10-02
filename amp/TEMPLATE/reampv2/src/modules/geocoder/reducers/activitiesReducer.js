import {
    FETCH_ACTIVITIES_ERROR, FETCH_ACTIVITIES_PENDING, FETCH_ACTIVITIES_SUCCESS, SELECT_ACTIVITY_FOR_GEOCODING
} from '../actions/activitiesAction';
import {
    GEOCODING_RUN_SEARCH_ERROR,
    GEOCODING_RUN_SEARCH_PENDING,
    GEOCODING_RUN_SEARCH_SUCCESS
} from "../actions/geocodingAction";

const initialState = {
    pending: false,
    activities: [],
    selectedActivities: [],
    error: null
};

export default function activitiesReducer(state = initialState, action) {
    switch(action.type) {
        case FETCH_ACTIVITIES_PENDING:
            return {
                ...state,
                pending: true,
            };
        case FETCH_ACTIVITIES_SUCCESS:
            return {
                ...state,
                pending: false,
                activities: action.payload
            };
        case FETCH_ACTIVITIES_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error
            };
        case GEOCODING_RUN_SEARCH_PENDING:
            return {
                ...state,
                selectedActivities: [],
                pending: true,
                error: null
            };
        case GEOCODING_RUN_SEARCH_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error
            };
        case GEOCODING_RUN_SEARCH_SUCCESS:
            return {
                ...state,
                pending: action.pending,
                error: null
            };
        case SELECT_ACTIVITY_FOR_GEOCODING:
            return {
                ...state,
                selectedActivities: action.payload
            };
        default:
            return state;
    }
}
