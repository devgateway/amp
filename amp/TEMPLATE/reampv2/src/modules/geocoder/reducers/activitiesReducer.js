import {
    FETCH_ACTIVITIES_ERROR, FETCH_ACTIVITIES_PENDING, FETCH_ACTIVITIES_SUCCESS, SELECT_ACTIVITY_FOR_GEOCODING
} from '../actions/activitiesAction';

const initialState = {
    pending: true,
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
        case SELECT_ACTIVITY_FOR_GEOCODING:
            return {
                ...state,
                pending: false,
                selectedActivities: action.payload
            };
        default:
            return state;
    }
}
