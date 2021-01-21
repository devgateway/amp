import {
  fetchIndirectReportPending,
  fetchIndirectReportError,
  fetchIndirectReportSuccess,
  fetchTopReportPending,
  fetchTopReportSuccess, fetchTopReportError,
  resetTopReport,
  fetchYearDetailSuccess, fetchYearDetailPending, fetchYearDetailError
} from './reportActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import {
  CURRENCY_CODE, DEFAULT_CURRENCY,
  DIRECT_INDIRECT_REPORT, FUNDING_TYPE, INCLUDE_LOCATIONS_WITH_CHILDREN, TOP_DONOR_REPORT,
  ACTIVITY_DETAIL_REPORT
} from '../utils/constants';

export const callReport = (fundingType, filters, programIds, settings) => dispatch => {
  dispatch(fetchIndirectReportPending());
  const newSettings = { [FUNDING_TYPE]: fundingType, programIds, ...settings };
  return Promise.all([fetchApiData({
    url: DIRECT_INDIRECT_REPORT,
    body: {
      settings: newSettings,
      filters: (filters ? filters.filters : null)
    }
  })]).then((data) => dispatch(fetchIndirectReportSuccess(data[0])))
    .catch(error => dispatch(fetchIndirectReportError(error)));
};

export const clearTopReport = () => dispatch => {
  dispatch(resetTopReport());
};

export const callTopReport = (fundingType, settings, filterParam, selectedProgram) => dispatch => {
  const params = { ...filterParam };
  if (!params.filters) {
    params.filters = {};
    params[INCLUDE_LOCATIONS_WITH_CHILDREN] = true;
  }
  if (selectedProgram !== null) {
    if (!params.filters[selectedProgram.filterColumnName]) {
      params.filters[selectedProgram.filterColumnName] = [];
    }
    if (!params.filters[selectedProgram.filterColumnName].find(v => v === selectedProgram.objectId)) {
      params.filters[selectedProgram.filterColumnName].push(selectedProgram.objectId);
    }
  }
  params.settings = { ...settings, [FUNDING_TYPE]: fundingType };
  if (!params.settings[CURRENCY_CODE]) {
    params.settings[CURRENCY_CODE] = DEFAULT_CURRENCY;
  }

  dispatch(fetchTopReportPending());
  return fetchApiData({
    url: TOP_DONOR_REPORT,
    body: params
  })
    .then(payload => dispatch(fetchTopReportSuccess(payload)))
    .catch(error => dispatch(fetchTopReportError(error)));
};

export const callYearDetailReport = (fundingType, filters, programId, year, settings) => dispatch => {
  dispatch(fetchYearDetailPending());
  const newSettings = {
    [FUNDING_TYPE]: fundingType, id: programId, year, ...settings
  };
  return Promise.all([fetchApiData({
    url: ACTIVITY_DETAIL_REPORT,
    body: {
      settings: newSettings,
      filters: (filters ? filters.filters : null)
    }
  })]).then((data) => dispatch(fetchYearDetailSuccess(data[0])))
    .catch(error => dispatch(fetchYearDetailError(error)));
};
