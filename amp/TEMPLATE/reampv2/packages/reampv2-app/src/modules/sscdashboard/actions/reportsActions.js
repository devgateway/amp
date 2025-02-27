export const FETCH_ACTIVITIES_DETAIL_PENDING = 'FETCH_ACTIVITIES_DETAIL_PENDING';
export const FETCH_ACTIVITIES_DETAIL_SUCCESS = 'FETCH_ACTIVITIES_DETAIL_SUCCESS';
export const FETCH_ACTIVITIES_DETAIL_ERROR = 'FETCH_ACTIVITIES_DETAIL_ERROR';

export const FETCH_ACTIVITIES_PENDING = 'FETCH_ACTIVITIES_PENDING';
export const FETCH_ACTIVITIES_SUCCESS = 'FETCH_ACTIVITIES_SUCCESS';
export const FETCH_ACTIVITIES_ERROR = 'FETCH_ACTIVITIES_ERROR';

export function fetchActivitiesDetailPending() {
  return {
    type: FETCH_ACTIVITIES_DETAIL_PENDING
  };
}

export function fetchActivitiesDetailSuccess(activitiesDetail) {
  return {
    type: FETCH_ACTIVITIES_DETAIL_SUCCESS,
    payload: activitiesDetail
  };
}

export function fetchActivitiesDetailError(error) {
  return {
    type: FETCH_ACTIVITIES_DETAIL_ERROR,
    error
  };
}

export function fetchActivitiesPending() {
  return {
    type: FETCH_ACTIVITIES_PENDING
  };
}

export function fetchActivitiesSuccess(activities) {
  return {
    type: FETCH_ACTIVITIES_SUCCESS,
    payload: activities
  };
}

export function fetchActivitiesError(error) {
  return {
    type: FETCH_ACTIVITIES_ERROR,
    error
  };
}
