import {
  UPDATE_ACTIVITIES_PENDING, UPDATE_ACTIVITIES_SUCCESS, UPDATE_ACTIVITIES_ERROR, INVOKE_ACTIVITIES_SUCCESS
}
  from '../actions/updateActivitiesAction';

const initialState = {
  updating: false,
  error: null,
  indirectProgramUpdatePending: false
};

export default function updateActivitiesReducer(state = initialState, action) {
  switch (action.type) {
    case UPDATE_ACTIVITIES_PENDING:
      return {
        ...state,
        updating: true,
        error: null
      };
    case INVOKE_ACTIVITIES_SUCCESS:
      return {
        ...state,
        updating: false,
        error: null,
        indirectProgramUpdatePending: true
      };
    case UPDATE_ACTIVITIES_SUCCESS: {
      return {
        ...state,
        indirectProgramUpdatePending: false
      };
    }
    case UPDATE_ACTIVITIES_ERROR:
      return {
        ...state,
        updating: false,
        error: action.error.msg,
        indirectProgramUpdatePending: false
      };
    default:
      return state;
  }
}

export const updateActivities = state => state;
export const updateActivitiesPending = state => state.updating;
export const updateActivitiesError = state => state.error;
