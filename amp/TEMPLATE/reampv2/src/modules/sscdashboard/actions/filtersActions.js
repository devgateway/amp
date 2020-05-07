export const FETCH_FILTERS_SECTORS_PENDING = 'FETCH_FILTERS_SECTORS_PENDING';
export const FETCH_FILTERS_SECTORS_SUCCESS = 'FETCH_FILTERS_SECTORS_SUCCESS';
export const FETCH_FILTERS_SECTORS_ERROR = 'FETCH_FILTERS_SECTORS_ERROR';

export function fetchSectorsPending() {
    return {
        type: FETCH_FILTERS_SECTORS_PENDING
    }
}

export function fetchSectorsSuccess(filters) {
    return {
        type: FETCH_FILTERS_SECTORS_SUCCESS,
        payload: filters.items.primary
    }
}

export function fetchSectorsError(error) {
    return {
        type: FETCH_FILTERS_SECTORS_ERROR,
        error: error
    }
};
