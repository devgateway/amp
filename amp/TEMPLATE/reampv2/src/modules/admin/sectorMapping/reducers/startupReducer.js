import {
  FETCH_SECTOR_MAP_SUCCESS,
  FETCH_SECTOR_MAP_PENDING,
  FETCH_SECTOR_MAP_ERROR,
  FETCH_SCHEMES_PENDING,
  FETCH_SCHEMES_SUCCESS,
  FETCH_SCHEMES_ERROR
} from '../actions/startupAction';

const initialState = {
  pendingSectorMapping: false,
  pendingSchemes: false,
  sectorMappings: [],
  error: null,
  schemes: []
};

export default function startupReducer(state = initialState, action) {
  switch (action.type) {
    case FETCH_SECTOR_MAP_PENDING:
      return {
        ...state,
        pendingSectorMapping: true
      };
    case FETCH_SECTOR_MAP_SUCCESS:
      return {
        ...state,
        pendingSectorMapping: false,
        sectorMappings: action.payload
      };
    case FETCH_SECTOR_MAP_ERROR:
      return {
        ...state,
        pendingSectorMapping: false,
        error: action.error
      };

    case FETCH_SCHEMES_PENDING:
      return {
        ...state,
        pendingSchemes: true
      };
    case FETCH_SCHEMES_SUCCESS:
      return {
        ...state,
        pendingSchemes: false,
        schemes: action.payload
      };
    case FETCH_SCHEMES_ERROR:
      return {
        ...state,
        pendingSchemes: false,
        error: action.error
      };
    default:
      return state;
  }
}

export const getSectorMappings = state => state.sectorMappings;
export const getSectorMappingPending = state => state.pendingSectorMapping;
export const getSectorMappingError = state => state.error;
export const getSchemes = state => state.schemes;
export const getSchemesPending = state => state.pendingSchemes
export const getSchemesError = state => state.error;
