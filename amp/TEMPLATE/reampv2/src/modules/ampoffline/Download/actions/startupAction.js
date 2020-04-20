export const FETCH_RELEASES_PENDING = 'FETCH_RELEASES_PENDING';
export const FETCH_RELEASES_SUCCESS = 'FETCH_RELEASES_SUCCESS';
export const FETCH_RELEASES_ERROR = 'FETCH_RELEASES_ERROR';

export function fetchReleasesPending() {
    return {
        type: FETCH_RELEASES_PENDING
    }
}

export function fetchReleasesSuccess(releases) {
    return {
        type: FETCH_RELEASES_SUCCESS,
        payload: releases
    }

}

export function fetchReleasesError(error) {
    return {
        type: FETCH_RELEASES_ERROR,
        error: error
    }
};
