import { SAVE_SECTOR_MAP_SUCCESS, SAVE_SECTOR_MAP_PENDING, SAVE_SECTOR_MAP_ERROR } from '../actions/saveAction';

const initialState = {
  saving: false,
  SectorMappings: [],
  error: null
};

export default function sendSectorMappingReducer(state = initialState, action) {
  switch (action.type) {
    case SAVE_SECTOR_MAP_PENDING:
      return {
        ...state,
        saving: true
      };
    case SAVE_SECTOR_MAP_SUCCESS:
      return {
        ...state,
        saving: false,
        data: action.payload
      };
    case SAVE_SECTOR_MAP_ERROR:
      return {
        ...state,
        saving: false,
        error: action.error
      };
    default:
      return state;
  }
}

export const sendSectorMapping = state => state.data;
export const sendSectorMappingPending = state => state.saving;
export const sendSectorMappingError = state => state.error;
export const sendSectorMappingSaving = state => state.saving;

