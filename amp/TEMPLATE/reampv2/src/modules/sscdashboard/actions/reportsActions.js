export const FETCH_ACTIVITIES_DETAIL_PENDING = 'FETCH_ACTIVITIES_DETAIL_PENDING';
export const FETCH_ACTIVITIES_DETAIL_SUCCESS = 'FETCH_ACTIVITIES_DETAIL_SUCCESS';
export const FETCH_ACTIVITIES_DETAIL_ERROR = 'FETCH_ACTIVITIES_DETAIL_ERROR';

export function fetchActivitiesDetailPending() {
    return {
        type: FETCH_ACTIVITIES_DETAIL_PENDING
    }
}

export function fetchActivitiesDetailSuccess(activitiesDetail) {
    return {
        type: FETCH_ACTIVITIES_DETAIL_SUCCESS,
        payload: activitiesDetail
    }
}

export function fetchActivitiesDetailError(error) {
    return {
        type: FETCH_ACTIVITIES_DETAIL_ERROR,
        error: error
    }
};
