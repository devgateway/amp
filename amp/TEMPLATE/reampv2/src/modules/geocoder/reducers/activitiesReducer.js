import {
    FETCH_ACTIVITIES_ERROR, FETCH_ACTIVITIES_PENDING, FETCH_ACTIVITIES_SUCCESS
} from '../actions/activitiesAction';

const initialState = {
    pending: true,
    activities: [],
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
        default:
            return state;
    }
}
