import {
    FETCH_ACTIVITIES_DETAIL_ERROR,
    FETCH_ACTIVITIES_DETAIL_PENDING,
    FETCH_ACTIVITIES_DETAIL_SUCCESS,
    FETCH_ACTIVITIES_ERROR,
    FETCH_ACTIVITIES_PENDING,
    FETCH_ACTIVITIES_SUCCESS
} from '../actions/reportsActions';

const initialState = {
  activitiesDetailsPending: false,
  activitiesDetailsLoaded: false,
  activitiesDetails: [],
  activitiesPending: false,
  activitiesLoaded: false,
  activities: []
};

export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_ACTIVITIES_DETAIL_PENDING:
      return {
        ...state,
        activitiesDetailsPending: false
      };
    case FETCH_ACTIVITIES_DETAIL_SUCCESS:
      return {
        ...state,
        activitiesDetailsPending: false,
        activitiesDetailsLoaded: true,
        activitiesDetails: action.payload
      };
    case FETCH_ACTIVITIES_DETAIL_ERROR:
      return {
        ...state,
        activitiesDetailsPending: false,
        activitiesDetailsLoaded: false,
        error: action.payload.error
      };

    case FETCH_ACTIVITIES_PENDING:
      return {
        ...state,
        activitiesPending: false
      };
    case FETCH_ACTIVITIES_SUCCESS:
      return {
        ...state,
        activitiesPending: false,
        activitiesLoaded: true,
        activities: action.payload
      };
    case FETCH_ACTIVITIES_ERROR:
      return {
        ...state,
        activitiesPending: false,
        activitiesLoaded: false,
        error: action.payload.error
      };
    default:
      return state;
  }
};
