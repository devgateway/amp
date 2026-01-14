export const SET_SETTINGS_DATA = 'SET_SETTINGS_DATA';
export const SET_FILTERS_DATA = 'SET_FILTERS_DATA';
export const SET_COLUMNS_DATA = 'SET_COLUMNS_DATA';
export const SET_HIERARCHIES_DATA = 'SET_HIERARCHIES_DATA';
export const SET_MEASURES_DATA = 'SET_MEASURES_DATA';

export function setSettingsData(payload) {
  return {
    type: SET_SETTINGS_DATA,
    payload,
  };
}

export function setFiltersData(payload) {
  return {
    type: SET_FILTERS_DATA,
    payload,
  };
}

export function setColumnsData(payload) {
  return {
    type: SET_COLUMNS_DATA,
    payload,
  };
}

export function setHierarchiesData(payload) {
  return {
    type: SET_HIERARCHIES_DATA,
    payload,
  };
}

export function setMeasuresData(payload) {
  return {
    type: SET_MEASURES_DATA,
    payload,
  };
}
