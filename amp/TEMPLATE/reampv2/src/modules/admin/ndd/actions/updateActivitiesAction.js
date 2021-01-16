export const UPDATE_ACTIVITIES_PENDING = 'UPDATE_ACTIVITIES_PENDING';
export const UPDATE_ACTIVITIES_SUCCESS = 'UPDATE_ACTIVITIES_SUCCESS';
export const UPDATE_ACTIVITIES_ERROR = 'UPDATE_ACTIVITIES_ERROR';

export function updateActivitiesPending() {
  return {
    type: UPDATE_ACTIVITIES_PENDING
  };
}

export function updateActivitiesSuccess() {
  return {
    type: UPDATE_ACTIVITIES_SUCCESS
  };
}

export function updateActivitiesError(error) {
  return {
    type: UPDATE_ACTIVITIES_ERROR,
    error
  };
}
