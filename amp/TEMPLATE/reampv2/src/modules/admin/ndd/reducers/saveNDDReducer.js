import {SAVE_NDD_PENDING, SAVE_NDD_SUCCESS, SAVE_NDD_ERROR} from '../actions/saveAction';

const initialState = {
    saving: false,
    NDDs: [],
    error: null
}

export default function sendNDDReducer(state = initialState, action) {
    switch(action.type) {
        case SAVE_NDD_PENDING:
            return {
                ...state,
                saving: true
            }
        case SAVE_NDD_SUCCESS:
            return {
                ...state,
                saving: false,
                data: action.payload
            }
        case SAVE_NDD_ERROR:
            return {
                ...state,
                saving: false,
                error: action.error
            }
        default:
            return state;
    }
}

export const sendNDD = state => state.data;
export const sendNDDPending = state => state.saving;
export const sendNDDError = state => state.error;
