import { fetchApiData } from '../../../utils/loadTranslations';
import { URL_GET_REPORT, URL_METADATA, URL_SAVE, URL_SAVE_NEW } from '../utils/constants';

export const UPDATE_REPORT_DETAILS_TOTAL_GROUPING = 'UPDATE_REPORT_DETAILS_TOTAL_GROUPING';
export const UPDATE_REPORT_DETAILS_TOTALS_ONLY = 'UPDATE_REPORT_DETAILS_TOTALS_ONLY';
export const UPDATE_REPORT_DETAILS_FUNDING_GROUPING = 'UPDATE_REPORT_DETAILS_FUNDING_GROUPING';
export const UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS = 'UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS';
export const UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING = 'UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING';
export const UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES = 'UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES';
export const UPDATE_REPORT_DETAILS_DESCRIPTION = 'UPDATE_REPORT_DETAILS_DESCRIPTION';
export const UPDATE_REPORT_DETAILS_NAME = 'UPDATE_REPORT_DETAILS_NAME';
export const UPDATE_REPORT_DETAILS_ALSO_SHOW_PLEDGES = 'UPDATE_REPORT_DETAILS_ALSO_SHOW_PLEDGES';
export const UPDATE_REPORT_DETAILS_USE_ABOVE_FILTERS = 'UPDATE_REPORT_DETAILS_USE_ABOVE_FILTERS';
export const UPDATE_REPORT_DETAILS_REPORT_CATEGORY = 'UPDATE_REPORT_DETAILS_REPORT_CATEGORY';
export const UPDATE_COLUMNS_SELECTED_COLUMN = 'UPDATE_COLUMNS_SELECTED_COLUMN';
export const UPDATE_COLUMNS_SORT_COLUMN = 'UPDATE_COLUMNS_SORT_COLUMN';
export const RESET_COLUMNS_SELECTED_COLUMN = 'RESET_COLUMNS_SELECTED_COLUMN';
export const UPDATE_MEASURES_SORT_COLUMN = 'UPDATE_MEASURES_SORT_COLUMN';
export const UPDATE_MEASURES_SELECTED_COLUMN = 'UPDATE_MEASURES_SELECTED_COLUMN';
export const RESET_MEASURES_SELECTED_COLUMN = 'RESET_MEASURES_SELECTED_COLUMN';
export const UPDATE_HIERARCHIES_SORT_COLUMN = 'UPDATE_HIERARCHIES_SORT_COLUMN';
export const UPDATE_HIERARCHIES_SELECTED_COLUMN = 'UPDATE_HIERARCHIES_SELECTED_COLUMN';
export const UPDATE_HIERARCHIES_LIST = 'UPDATE_HIERARCHIES_LIST';
export const UPDATE_APPLIED_FILTERS = 'UPDATE_APPLIED_FILTERS';
export const UPDATE_APPLIED_SETTINGS = 'UPDATE_APPLIED_SETTINGS';
export const UPDATE_PROFILE = 'UPDATE_PROFILE';
export const UPDATE_ID = 'UPDATE_ID';

export const FETCH_METADATA_PENDING = 'FETCH_METADATA_PENDING';
export const FETCH_METADATA_SUCCESS = 'FETCH_METADATA_SUCCESS';
export const FETCH_METADATA_ERROR = 'FETCH_METADATA_ERROR';
export const FETCH_METADATA_IGNORE = 'FETCH_METADATA_IGNORE';

export const FETCH_REPORT_PENDING = 'FETCH_REPORT_PENDING';
export const FETCH_REPORT_SUCCESS = 'FETCH_REPORT_SUCCESS';
export const FETCH_REPORT_ERROR = 'FETCH_REPORT_ERROR';

export const SAVE_NEW_REPORT_PENDING = 'SAVE_NEW_REPORT_PENDING';
export const SAVE_NEW_REPORT_SUCCESS = 'SAVE_NEW_REPORT_SUCCESS';
export const SAVE_NEW_REPORT_ERROR = 'SAVE_NEW_REPORT_ERROR';
export const SAVE_REPORT_PENDING = 'SAVE_REPORT_PENDING';
export const SAVE_REPORT_SUCCESS = 'SAVE_REPORT_SUCCESS';
export const SAVE_REPORT_ERROR = 'SAVE_REPORT_ERROR';

export function updateReportDetailsTotalGrouping(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_TOTAL_GROUPING,
    payload
  };
}

export function updateReportDetailsTotalsOnly(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_TOTALS_ONLY,
    payload
  };
}

export function updateReportDetailsFundingGrouping(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_FUNDING_GROUPING,
    payload
  };
}

export function updateReportDetailsAllowEmptyFundingColumns(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_ALLOW_EMPTY_FUNDING_COLUMNS,
    payload
  };
}

export function updateReportDetailsSplitByFunding(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_SPLIT_BY_FUNDING,
    payload
  };
}

export function updateReportDetailsShowOriginalCurrencies(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_SHOW_ORIGINAL_CURRENCIES,
    payload
  };
}

export function updateReportDetailsAlsoShowPledges(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_ALSO_SHOW_PLEDGES,
    payload
  };
}

export function updateReportDetailsUseAboveFilters(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_USE_ABOVE_FILTERS,
    payload
  };
}

export function updateReportDetailsDescription(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_DESCRIPTION,
    payload
  };
}

export function updateReportDetailsName(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_NAME,
    payload
  };
}

export function updateReportDetailsNameReportCategory(payload) {
  return {
    type: UPDATE_REPORT_DETAILS_REPORT_CATEGORY,
    payload
  };
}

export function updateColumnsSelected(payload) {
  return {
    type: UPDATE_COLUMNS_SELECTED_COLUMN,
    payload
  };
}

export function updateColumnsSorting(payload) {
  return {
    type: UPDATE_COLUMNS_SORT_COLUMN,
    payload
  };
}

export function resetColumnsSelected() {
  return {
    type: RESET_COLUMNS_SELECTED_COLUMN
  };
}

export function updateHierarchiesAvailable(payload) {
  return {
    type: UPDATE_HIERARCHIES_LIST,
    payload
  };
}

export function updateHierarchiesSelected(payload) {
  return {
    type: UPDATE_HIERARCHIES_SELECTED_COLUMN,
    payload
  };
}

export function updateHierarchiesSorting(payload) {
  return {
    type: UPDATE_HIERARCHIES_SORT_COLUMN,
    payload
  };
}

export function updateMeasuresSelected(payload) {
  return {
    type: UPDATE_MEASURES_SELECTED_COLUMN,
    payload
  };
}

export function updateMeasuresSorting(payload) {
  return {
    type: UPDATE_MEASURES_SORT_COLUMN,
    payload
  };
}

export function resetMeasuresSelected() {
  return {
    type: RESET_MEASURES_SELECTED_COLUMN
  };
}

export function fetchMetaDataPending() {
  return {
    type: FETCH_METADATA_PENDING
  };
}

export function fetchMetaDataSuccess(payload) {
  return {
    type: FETCH_METADATA_SUCCESS,
    payload
  };
}

export function fetchMetaDataError(error) {
  return {
    type: FETCH_METADATA_ERROR,
    error
  };
}

export function fetchMetaDataIgnore() {
  return {
    type: FETCH_METADATA_IGNORE,
  };
}

export function fetchReportPending() {
  return {
    type: FETCH_REPORT_PENDING
  };
}

export function fetchReportSuccess(payload) {
  return {
    type: FETCH_REPORT_SUCCESS,
    payload
  };
}

export function fetchReportError(error) {
  return {
    type: FETCH_REPORT_ERROR,
    error
  };
}

export function updateAppliedFilters(payload, html) {
  return {
    type: UPDATE_APPLIED_FILTERS,
    payload,
    html
  };
}

export function updateAppliedSettings(payload) {
  return {
    type: UPDATE_APPLIED_SETTINGS,
    payload
  };
}

export function updateProfile(payload) {
  return {
    type: UPDATE_PROFILE,
    payload
  };
}

export function updateId(payload) {
  return {
    type: UPDATE_ID,
    payload
  };
}

export function saveNewReportPending() {
  return {
    type: SAVE_NEW_REPORT_PENDING
  };
}

export function saveNewReportSuccess(payload) {
  return {
    type: SAVE_NEW_REPORT_SUCCESS,
    payload
  };
}

export function saveNewReportError(error) {
  return {
    type: SAVE_NEW_REPORT_ERROR,
    error
  };
}

export function saveReportPending() {
  return {
    type: SAVE_REPORT_PENDING
  };
}

export function saveReportSuccess(payload) {
  return {
    type: SAVE_REPORT_SUCCESS,
    payload
  };
}

export function saveReportError(error) {
  return {
    type: SAVE_REPORT_ERROR,
    error
  };
}

let getMetadataPromise = null;
export const getMetadata = (type, profile) => (dispatch, getState) => {
  const state = getState();
  if (state.uiReducer.metaDataPending || state.uiReducer.metaDataLoaded || state.uiReducer.error) {
    dispatch(fetchMetaDataIgnore());
    return getMetadataPromise;
  } else {
    dispatch(fetchMetaDataPending());
    const url = type ? `${URL_METADATA}?type=${type}&profile=${profile}` : URL_METADATA;
    getMetadataPromise = Promise.all([fetchApiData({
      url
    })])
      .then((data) => dispatch(fetchMetaDataSuccess(data[0])))
      .catch(error => dispatch(fetchMetaDataError(error)));
    return getMetadataPromise;
  }
};

export const fetchReport = (id) => dispatch => {
  dispatch(fetchReportPending());
  return fetchApiData({
    url: URL_GET_REPORT.replace('{reportId}', id)
  }).then((data) => dispatch(fetchReportSuccess(data)))
    .catch(error => dispatch(fetchReportError(error)));
};

export const saveNew = (data) => dispatch => {
  dispatch(saveNewReportPending());
  return fetchApiData({
    url: `${URL_SAVE_NEW}?reportId=null`,
    body: data
  }).then((result) => dispatch(saveNewReportSuccess(result)))
    .catch(error => dispatch(saveNewReportError(error)));
};

export const save = (id, data) => dispatch => {
  dispatch(saveReportPending());
  return fetchApiData({
    url: `${URL_SAVE}${id}`,
    body: data
  }).then((result) => dispatch(saveReportSuccess(result)))
    .catch(error => dispatch(saveReportError(error)));
};
