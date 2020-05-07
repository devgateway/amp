import {
    FETCH_FILTERS_SECTORS_SUCCESS,
    FETCH_FILTERS_SECTORS_ERROR,
    FETCH_FILTERS_SECTORS_PENDING
} from '../actions/filtersActions';

const initialState = {
    sectorsLoaded: false,
    sectorsPending: false,
    sectors: [],
    sectorLoadingErrors: null
};
export default (state = initialState, action) => {
    switch (action.type) {
        case FETCH_FILTERS_SECTORS_PENDING:
            return {
                ...state,
                sectorsPending: true
            };
        case FETCH_FILTERS_SECTORS_SUCCESS:
            return {
                ...state,
                sectorsPending: false,
                sectorsLoaded: true,
                sectors: action.payload
            };
        case FETCH_FILTERS_SECTORS_ERROR:
            return {
                ...state,
                sectorsPending: false,
                sectorsLoaded: false,
                error: action.payload.error
            };
        default:
            return state
    }
}
