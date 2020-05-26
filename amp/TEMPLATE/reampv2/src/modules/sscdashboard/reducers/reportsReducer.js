import {
    FETCH_ACTIVITIES_DETAIL_ERROR, FETCH_ACTIVITIES_DETAIL_PENDING, FETCH_ACTIVITIES_DETAIL_SUCCESS
} from '../actions/reportsActions';

const initialState = {
    activitiesDetailsPending: false,
    activitiesDetailLoaded: false,
    activitiesDetails: []
};
export default (state = initialState, action) => {
    switch (action.type) {
        case FETCH_ACTIVITIES_DETAIL_PENDING:
            return {
                ...state,
                activitiesDetailsPending: false
            };
        case FETCH_ACTIVITIES_DETAIL_SUCCESS:
            return {
                ...state,
                activitiesDetailsPending: false,
                activitiesDetailLoaded: true,
                activitiesDetails: action.payload
            };
        case FETCH_ACTIVITIES_DETAIL_ERROR:
            return {
                ...state,
                activitiesDetailsPending: false,
                activitiesDetailLoaded: false,
                error: action.payload.error
            };

        default:
            return state
    }
}
