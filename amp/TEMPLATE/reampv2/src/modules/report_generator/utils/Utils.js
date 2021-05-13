export function validateSaveModal(title) {
  if (title === null || title === undefined || title.toString().trim().length === 0) {
    return 'missingTitle';
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
    case 'regional-component':
      return 'R';
    case 'funding-contribution':
      return 'C';
    case 'funding-pledges':
      return 'P';
    default:
      return null;
  }
}
