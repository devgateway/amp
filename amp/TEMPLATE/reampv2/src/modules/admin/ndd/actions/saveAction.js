export const SAVE_NDD_PENDING = 'SAVE_NDD_PENDING';
export const SAVE_NDD_SUCCESS = 'SAVE_NDD_SUCCESS';
export const SAVE_NDD_ERROR = 'SAVE_NDD_ERROR';

export function saveNDDPending() {
    return {
        type: SAVE_NDD_PENDING
    }
}

export function saveNDDSuccess(ndd) {
    return {
        type: SAVE_NDD_SUCCESS,
        payload: ndd
    }
}

export function saveNDDError(error) {
    return {
        type: SAVE_NDD_ERROR,
        error: error
    }
};
