import {FETCH_FILES_PENDING, FETCH_FILES_SUCCESS, FETCH_FILES_ERROR} from '../actions/startupAction';

const initialState = {
    pending: false,
    files: [],
    error: null
}

export default function startupReducer(state = initialState, action) {
    switch(action.type) {
        case FETCH_FILES_PENDING:
            return {
                ...state,
                pending: true
            }
        case FETCH_FILES_SUCCESS:
            debugger;
            return {
                ...state,
                pending: false,
                files: action.payload
            }
        case FETCH_FILES_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error
            }
        default:
            return state;
    }
}

export const getFiles = state => state.files;
export const getFilesPending = state => state.pending;
export const getFilesError = state => state.error;
