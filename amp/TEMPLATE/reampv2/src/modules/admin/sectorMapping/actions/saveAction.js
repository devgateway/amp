export const SAVE_SECTOR_MAP_PENDING = 'SAVE_SECTOR_MAP_PENDING';
export const SAVE_SECTOR_MAP_SUCCESS = 'SAVE_SECTOR_MAP_SUCCESS';
export const SAVE_SECTOR_MAP_ERROR = 'SAVE_SECTOR_MAP_ERROR';

export function saveSectorMappingPending() {
  return {
    type: SAVE_SECTOR_MAP_PENDING
  };
}

export function saveSectorMappingSuccess(mappings) {
  return {
    type: SAVE_SECTOR_MAP_SUCCESS,
    payload: mappings
  };
}

export function saveSectorMappingError(error) {
  return {
    type: SAVE_SECTOR_MAP_ERROR,
    error
  };
}
