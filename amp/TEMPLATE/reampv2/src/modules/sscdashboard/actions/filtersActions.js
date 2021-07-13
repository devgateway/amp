import { UNDEFINED_FILTER } from '../utils/constants';
import { EXTRA_INFO } from '../utils/FieldsConstants';
import { toCamelCase } from '../utils/Utils';

export const FETCH_FILTERS_SECTORS_PENDING = 'FETCH_FILTERS_SECTORS_PENDING';
export const FETCH_FILTERS_SECTORS_SUCCESS = 'FETCH_FILTERS_SECTORS_SUCCESS';
export const FETCH_FILTERS_SECTORS_ERROR = 'FETCH_FILTERS_SECTORS_ERROR';
export const FETCH_FILTERS_COUNTRIES_PENDING = 'FETCH_FILTERS_COUNTRIES_PENDING';
export const FETCH_FILTERS_COUNTRIES_SUCCESS = 'FETCH_FILTERS_COUNTRIES_SUCCESS';
export const FETCH_FILTERS_COUNTRIES_ERROR = 'FETCH_FILTERS_COUNTRIES_ERROR';
export const FETCH_FILTERS_MODALITIES_PENDING = 'FETCH_FILTERS_MODALITIES_PENDING';
export const FETCH_FILTERS_MODALITIES_SUCCESS = 'FETCH_FILTERS_MODALITIES_SUCCESS';
export const FETCH_FILTERS_MODALITIES_ERROR = 'FETCH_FILTERS_MODALITIES_ERROR';

export function fetchSectorsPending() {
  return {
    type: FETCH_FILTERS_SECTORS_PENDING
  };
}

export function fetchSectorsSuccess(sectorsFilter) {
  return {
    type: FETCH_FILTERS_SECTORS_SUCCESS,
    payload: makeFilterCamelCase(sectorsFilter.items.primary)
  };
}

export function fetchSectorsError(error) {
  return {
    type: FETCH_FILTERS_SECTORS_ERROR,
    error
  };
}

export function fetchCountriesPending() {
  return {
    type: FETCH_FILTERS_COUNTRIES_PENDING
  };
}

export function fetchCountriesSuccess(countriesFilter) {
  // TODO this filter should check the SSC template as parameter
  const countries = countriesFilter.items.locations
    .filter(c => c[EXTRA_INFO] && c[EXTRA_INFO].template && c.id !== UNDEFINED_FILTER);
  return {
    type: FETCH_FILTERS_COUNTRIES_SUCCESS,
    payload: countries
  };
}

export function fetchCountriesError(error) {
  return {
    type: FETCH_FILTERS_COUNTRIES_ERROR,
    error
  };
}

export function fetchModalitiesPending() {
  return {
    type: FETCH_FILTERS_MODALITIES_PENDING
  };
}

function makeFilterCamelCase(theFilter) {
  return theFilter.map(f => {
    f.name = toCamelCase(f.name);
    return f;
  });
}

export function fetchModalitiesSuccess(modalitiesFilter) {
  return {
    type: FETCH_FILTERS_MODALITIES_SUCCESS,
    payload: modalitiesFilter.items.values
  };
}

export function fetchModalitiesError(error) {
  return {
    type: FETCH_FILTERS_MODALITIES_ERROR,
    error
  };
}
