import {
    FETCH_GEOCODING_ERROR, FETCH_GEOCODING_PENDING, FETCH_GEOCODING_SUCCESS
} from '../actions/geocodingAction';

const initialState = {
    pending: true,
    geocoding: [],
    error: null
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
                pending: false,
                geocoding: action.payload,
            };
        case FETCH_GEOCODING_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error,
            };
        default:
            return state;
    }
}
