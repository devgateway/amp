export const FETCH_NDD_PENDING = 'FETCH_NDD_PENDING';
export const FETCH_NDD_SUCCESS = 'FETCH_NDD_SUCCESS';
export const FETCH_NDD_ERROR = 'FETCH_NDD_ERROR';

export function fetchNDDPending() {
    return {
        type: FETCH_NDD_PENDING
    }
}

export function fetchNDDSuccess(ndd) {
    return {
        type: FETCH_NDD_SUCCESS,
        payload: ndd
    }
}
export function fetchNDDError(error) {
    return {
        type: FETCH_NDD_ERROR,
        error: error
    }
};
