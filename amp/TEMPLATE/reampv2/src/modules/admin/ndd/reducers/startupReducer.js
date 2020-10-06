import {FETCH_NDD_PENDING, FETCH_NDD_SUCCESS, FETCH_NDD_ERROR} from '../actions/startupAction';

const initialState = {
    pending: false,
    NDDs: [],
    error: null
}

export default function startupReducer(state = initialState, action) {
    switch(action.type) {
        case FETCH_NDD_PENDING:
            return {
                ...state,
                pending: true
            }
        case FETCH_NDD_SUCCESS:
            return {
                ...state,
                pending: false,
                NDDs: action.payload
            }
        case FETCH_NDD_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error
            }
        default:
            return state;
    }
}

export const getNDD = state => state.NDDs;
export const getNDDPending = state => state.pending;
export const getNDDError = state => state.error;
