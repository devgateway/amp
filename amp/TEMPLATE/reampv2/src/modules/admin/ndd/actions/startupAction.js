export const FETCH_NDD_PENDING = 'FETCH_NDD_PENDING';
export const FETCH_NDD_SUCCESS = 'FETCH_NDD_SUCCESS';
export const FETCH_NDD_ERROR = 'FETCH_NDD_ERROR';
export const FETCH_PROGRAMS_PENDING = 'FETCH_PROGRAMS_PENDING';
export const FETCH_PROGRAMS_SUCCESS = 'FETCH_PROGRAMS_SUCCESS';
export const FETCH_PROGRAMS_ERROR = 'FETCH_PROGRAMS_ERROR';

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

export function fetchProgramsPending() {
    return {
        type: FETCH_PROGRAMS_PENDING
    }
}

export function fetchProgramsSuccess(programs) {
    return {
        type: FETCH_PROGRAMS_SUCCESS,
        payload: programs
    }
}
export function fetchProgramsError(error) {
    return {
        type: FETCH_PROGRAMS_ERROR,
        error: error
    }
};
