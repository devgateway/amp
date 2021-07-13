import {
  IS_MEASURELESS_REPORT, PROFILE_REPORT, PROFILE_TAB, TRN_PREFIX_REPORT, TRN_PREFIX_TAB
} from './constants';

export function validateSaveModal(title, columns, reportDetails, hierarchies, measures) {
  if (title === null || title === undefined || title.toString().trim().length === 0) {
    return 'missingTitle';
  }
  if (!columns || columns.length === 0) {
    return 'missingColumns';
  }
  if (reportDetails.selectedSummaryReport && hierarchies.selected.length === 0 && measures.selected.length === 0) {
    return 'summaryReportWithoutMeasuresAndHierarchies';
  }
  return null;
}

export function javaHashCode(s) {
  let h = 0;
  for (let i = 0; i < s.length; i++) {
    // eslint-disable-next-line no-bitwise
    h = Math.imul(31, h) + s.charCodeAt(i) | 0;
  }
  return h;
}

export function convertTotalGrouping(value) {
  switch (value) {
    case 'annual-report':
      return 'A';
    case 'quarterly-report':
      return 'Q';
    case 'monthly-report':
      return 'M';
    case 'totals-only':
      return 'T';
    default:
      return null;
  }
}

export function revertTotalGrouping(value) {
  switch (value) {
    case 'A':
      return 'annual-report';
    case 'Q':
      return 'quarterly-report';
    case 'M':
      return 'monthly-report';
    case 'T':
      return 'totals-only';
    default:
      return null;
  }
}

export function convertReportType(value) {
  switch (value) {
    case 'funding-donor':
      return 'D';
    case 'funding-regional':
      return 'R';
    case 'funding-component':
      return 'C';
    case 'funding-pledges':
      return 'P';
    default:
      return null;
  }
}

export function revertReportType(value) {
  switch (value) {
    case 'D':
      return 'funding-donor';
    case 'R':
      return 'funding-regional';
    case 'C':
      return 'funding-component';
    case 'P':
      return 'funding-pledges';
    default:
      return null;
  }
}

export function hasFilters(filters) {
  return filters && Object.keys(filters).length > 0 && Object.keys(filters).some(i => filters[i]);
}

/**
 * Note: Always a pure function.
 * @param data
 * @returns {string}
 */
export function getProfileFromReport(data) {
  if (data && data.tab === true) {
    return PROFILE_TAB;
  }
  return PROFILE_REPORT;
}

export function areEnoughDataForPreview(columns, measures, hierarchies, reportDetails, profile) {
  if (profile !== PROFILE_TAB) {
    if (columns && measures && hierarchies && reportDetails && columns.available && columns.available.length > 0) {
      if (columns.selected.length > 0 && (IS_MEASURELESS_REPORT || measures.selected.length > 0)) {
        const selectedSummaryReport = reportDetails && reportDetails.selectedSummaryReport;
        if (!selectedSummaryReport) {
          if (columns.selected.length === hierarchies.selected.length) {
            return false;
          }
        } else if (measures.selected.length === 0 && hierarchies.selected.length === 0) {
          return false;
        }
        return true;
      }
    }
  }
  return false;
}

/**
 * Return translations for tabs and reports.
 * Default to reports if missing key for tabs.
 * @param key
 * @param profile
 * @param translations
 * @returns {*}
 */
export function translate(key, profile, translations) {
  const prefix = profile === PROFILE_TAB ? TRN_PREFIX_TAB : TRN_PREFIX_REPORT;
  let value = translations[prefix + key];
  if (!value) {
    value = translations[TRN_PREFIX_REPORT + key];
  }
  return value;
}
