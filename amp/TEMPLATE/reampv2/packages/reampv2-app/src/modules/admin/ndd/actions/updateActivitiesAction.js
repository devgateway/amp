export const UPDATE_ACTIVITIES_PENDING = 'UPDATE_ACTIVITIES_PENDING';
export const UPDATE_ACTIVITIES_SUCCESS = 'UPDATE_ACTIVITIES_SUCCESS';
export const UPDATE_ACTIVITIES_ERROR = 'UPDATE_ACTIVITIES_ERROR';
export const INVOKE_ACTIVITIES_SUCCESS = 'INVOKE_ACTIVITIES_SUCCESS';

export function updateActivitiesPending() {
  return {
    type: UPDATE_ACTIVITIES_PENDING
  };
}

export function invokeUpdateActivities() {
  return {
    type: INVOKE_ACTIVITIES_SUCCESS
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
