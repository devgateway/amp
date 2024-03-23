import {
  DASHBOARD_DEFAULT_MAX_YEAR_RANGE,
  DASHBOARD_DEFAULT_MIN_YEAR_RANGE,
  FALLBACK_FLAG,
  FLAGS_DIRECTORY
} from './constants';
import { EXTRA_INFO, GROUP_ID } from './FieldsConstants';

export function splitArray(a, n, balanced) {
  if (n < 2) return [a];

  const len = a.length;
  const
    out = [];
  let i = 0;
  let
    size;
  if (len % n === 0) {
    size = Math.floor(len / n);
    while (i < len) {
      out.push(a.slice(i, i += size));
    }
  } else if (balanced) {
    while (i < len) {
      size = Math.ceil((len - i) / n);
      n += 1;
      out.push(a.slice(i, i += size));
    }
  } else {
    n -= 1;
    size = Math.floor(len / n);
    if (len % size === 0) size -= 1;
    while (i < size * n) {
      out.push(a.slice(i, i += size));
    }
    out.push(a.slice(size * n));
  }

  return out;
}

export function compareArrayNumber(a, b) {
  a.sort((c, d) => c - d);
  b.sort((c, d) => c - d);
  const left = [];
  const both = [];
  const
    right = [];
  let i = 0;
  let
    j = 0;
  while (i < a.length && j < b.length) {
    if (a[i] < b[j]) {
      left.push(a[i]);
      i += 1;
    } else if (b[j] < a[i]) {
      right.push(b[j]);
      j += 1;
    } else {
      both.push(a[i]);
      i = 1 + 1;
      j += 1;
    }
  }
  while (i < a.length) {
    left.push(a[i]);
    i += 1;
  }
  while (j < b.length) {
    right.push(b[j]);
    j += 1;
  }
  // left and right is the difference not in use but keeping it if needed
  if (a.length !== b.length) {
    return false;
  }
  return a.length === both.length;
}

export function toCamelCase(str) {
  return str.toLowerCase().split(' ').map(s => s.charAt(0).toUpperCase() + s.slice(1)).join(' ');
}

// TODO move to another utility class.
export function getCountryFlag(name) {
  return [`${process.env.PUBLIC_URL}${FLAGS_DIRECTORY}${name.toLowerCase().replace(/ /g, '_')}.svg`,
    FALLBACK_FLAG];
}

export function calculateUpdatedValuesForDropDowns(ipSelectedFilter, selectedOptions) {
  let updatedSelectedOptions;
  if (selectedOptions.includes(ipSelectedFilter)) {
    updatedSelectedOptions = selectedOptions.filter(sc => sc !== ipSelectedFilter);
  } else {
    updatedSelectedOptions = [...selectedOptions];
    updatedSelectedOptions.push(ipSelectedFilter);
  }
  return updatedSelectedOptions;
}

export function calculateColumnCount(length) {
  let columnCount = 1;
  switch (length) {
    case 1:
      columnCount = 1;
      break;
    case 2:
    case 4:
      columnCount = 2;
      break;
    case 3:
    case 5:
    case 6:
      columnCount = 3;
      break;
    default:
      break;
  }
  return columnCount;
}

export function getCategoryForCountry(country) {
  if (country && country[EXTRA_INFO]) {
    return country[EXTRA_INFO][GROUP_ID];
  } else {
    return null;
  }
}

export function generateYearsFilters(years, settingsParam, maxYearParam) {
  const { settings, settingsLoaded } = settingsParam;
  if (settingsLoaded) {
    const minYear = parseInt(settings[DASHBOARD_DEFAULT_MIN_YEAR_RANGE], 10);
    const maxYear = maxYearParam || parseInt(settings[DASHBOARD_DEFAULT_MAX_YEAR_RANGE], 10);
    for (let i = maxYear; i >= minYear; i--) {
      years.push({ id: i, name: i });
    }
  }
}
