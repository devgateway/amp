import { fetchApiData } from '../../../utils/loadTranslations';
import {
  fetchXlsReportDone,
  fetchXlsReportError,
  fetchXlsReportPending,
  fetchXlsReportSuccess
} from './downloadDataActions';
import { API_XLS_REPORT_URL } from '../utils/constants';

const report = {
  settings: {
    'amount-format': { 'number-divider': '1' },
    'calendar-id': 4
  },
  filters: {},
};
export const dataDownloaded = () => dispatch => {
  dispatch(fetchXlsReportDone());
}
export const downloadData = (selectedFilters) => dispatch => {
  dispatch(fetchXlsReportPending());
  // selectedYears
  if (selectedFilters.selectedSectors.length > 0) {
    report.filters['primary-sector'] = selectedFilters.selectedSectors;
  }
  if (selectedFilters.selectedModalities.length > 0) {
    report.filters.modalities = selectedFilters.selectedModalities;
  }
  if (selectedFilters.selectedCountries.length > 0) {
    report.filters['donor-agency-country'] = selectedFilters.selectedCountries;
  }
  if (selectedFilters.selectedYears) {
    report.years = selectedFilters.selectedYears;
  }
  const url = `${API_XLS_REPORT_URL}`;
  return fetchApiData({ url, body: report })
    .then(dataDownload => dispatch(fetchXlsReportSuccess(dataDownload)))
    .catch(error => dispatch(fetchXlsReportError(error)));
};
