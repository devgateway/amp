import { UPDATE_ACTIVITIES_PENDING, UPDATE_ACTIVITIES_SUCCESS, UPDATE_ACTIVITIES_ERROR }
  from '../actions/updateActivitiesAction';

const initialState = {
  updating: false,
  error: null
};

export default function updateActivitiesReducer(state = initialState, action) {
  switch (action.type) {
    case UPDATE_ACTIVITIES_PENDING:
      return {
        ...state,
        updating: true,
        error: null
      };
    case UPDATE_ACTIVITIES_SUCCESS:
      return {
        ...state,
        updating: false,
        error: null
      };
    case UPDATE_ACTIVITIES_ERROR:
      return {
        ...state,
        updating: false,
        error: action.error.msg
      };
    default:
      return state;
  }
}

export const updateActivities = state => state;
export const updateActivitiesPending = state => state.updating;
export const updateActivitiesError = state => state.error;
