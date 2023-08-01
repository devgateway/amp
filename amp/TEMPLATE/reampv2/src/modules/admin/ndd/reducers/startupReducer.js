import {
    FETCH_NDD_ERROR,
    FETCH_NDD_PENDING,
    FETCH_NDD_SUCCESS,
    FETCH_PROGRAMS_ERROR,
    FETCH_PROGRAMS_PENDING,
    FETCH_PROGRAMS_SUCCESS
} from '../actions/startupAction';

const initialState = {
  pendingNDD: false,
  pendingPrograms: false,
  NDDs: [],
  error: null,
  programs: []
};

export default function startupReducer(state = initialState, action) {
  switch (action.type) {
    case FETCH_NDD_PENDING:
      return {
        ...state,
        pendingNDD: true
      };
    case FETCH_NDD_SUCCESS:
      return {
        ...state,
        pendingNDD: false,
        NDDs: action.payload
      };
    case FETCH_NDD_ERROR:
      return {
        ...state,
        pendingNDD: false,
        error: action.error
      };
    case FETCH_PROGRAMS_PENDING:
      return {
        ...state,
        pendingPrograms: true
      };
    case FETCH_PROGRAMS_SUCCESS:
      return {
        ...state,
        pendingPrograms: false,
        programs: action.payload
      };
    case FETCH_PROGRAMS_ERROR:
      return {
        ...state,
        pendingPrograms: false,
        error: action.error
      };
    default:
      return state;
  }
}

export const getNDD = state => state.NDDs;
export const getNDDPending = state => state.pendingNDD;
export const getNDDError = state => state.error;

export const getPrograms = state => state.programs;
export const getProgramsPending = state => state.pendingPrograms;
export const getProgramsError = state => state.error;
