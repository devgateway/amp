export const FETCH_SECTOR_MAP_PENDING = 'FETCH_SECTOR_MAP_PENDING';
export const FETCH_SECTOR_MAP_SUCCESS = 'FETCH_SECTOR_MAP_SUCCESS';
export const FETCH_SECTOR_MAP_ERROR = 'FETCH_SECTOR_MAP_ERROR';
export const FETCH_SECTORS_PENDING = 'FETCH_SECTORS_PENDING';
export const FETCH_SECTORS_SUCCESS = 'FETCH_SECTORS_SUCCESS';
export const FETCH_SECTORS_ERROR = 'FETCH_SECTORS_ERROR';
export const FETCH_SCHEMES_PENDING = 'FETCH_SCHEMES_PENDING';
export const FETCH_SCHEMES_SUCCESS = 'FETCH_SCHEMES_SUCCESS';
export const FETCH_SCHEMES_ERROR = 'FETCH_SCHEMES_ERROR';

export function fetchSectorMappingPending() {
  return {
    type: FETCH_SECTOR_MAP_PENDING
  };
}

export function fetchSectorMappingSuccess(sectorMapping) {
  return {
    type: FETCH_SECTOR_MAP_SUCCESS,
    payload: sectorMapping
  };
}
export function fetchSectorMappingError(error) {
  return {
    type: FETCH_SECTOR_MAP_ERROR,
    error
  };
}
export function fetchSectorsPending() {
  return {
    type: FETCH_SECTORS_PENDING
  };
}

export function fetchSectorsSuccess(sectors) {
  return {
    type: FETCH_SECTORS_SUCCESS,
    payload: sectors
  };
}
export function fetchSectorsError(error) {
  return {
    type: FETCH_SECTORS_ERROR,
    error
  };
}


export function fetchSchemesPending() {
  return {
    type: FETCH_SCHEMES_PENDING
  };
}

export function fetchSchemesSuccess(schemes) {
  return {
    type: FETCH_SCHEMES_SUCCESS,
    payload: schemes
  };
}
export function fetchSchemesError(error) {
  return {
    type: FETCH_SCHEMES_ERROR,
    error
  };
}

