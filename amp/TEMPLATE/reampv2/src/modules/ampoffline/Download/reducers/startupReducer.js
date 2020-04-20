import {FETCH_RELEASES_PENDING, FETCH_RELEASES_SUCCESS, FETCH_RELEASES_ERROR} from '../actions/startupAction';

const initialState = {
    pending: false,
    releases: [],
    error: null
}

export default function startupReducer(state = initialState, action) {
    switch(action.type) {
        case FETCH_RELEASES_PENDING:
            return {
                ...state,
                pending: true
            }
        case FETCH_RELEASES_SUCCESS:
            return {
                ...state,
                pending: false,
                releases: action.payload
            }
        case FETCH_RELEASES_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error
            }
        default:
            return state;
    }
}

export const getReleases = state => state.releases;
export const getReleasesPending = state => state.pending;
export const getReleasesError = state => state.error;
