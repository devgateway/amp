export function extractSettings(gs) {
  const settings = {
    'amount-format': {},
    'calendar-id': null,
    'currency-code': null,
    'year-range': {}
  };

  settings['amount-format']['decimal-symbol'] = gs['number-decimal-separator'] || '.';
  const numberFormat = gs['number-settings'] || '#,#.#';
  settings['amount-format']['group-separator'] = numberFormat.indexOf(',') !== -1
    ? (gs['number-group-separator'] || ',')
    : '';
  settings['amount-format']['group-size'] = 3;
  settings['amount-format']['max-frac-digits'] = 0;
  if (numberFormat.indexOf(',') !== -1) {
    settings['amount-format']['max-frac-digits'] = numberFormat.length - numberFormat.indexOf('.') - 1;
  }
  settings['amount-format']['number-divider'] = gs['number-divider'];
  settings['amount-format']['use-grouping'] = (numberFormat.indexOf(',') !== -1);
  settings['calendar-id'] = `${gs['calendar-id']}`;
  settings['currency-code'] = gs['effective-currency'].code;
  settings['year-range'].rangeFrom = gs['dashboard-default-min-year-range'];
  settings['year-range'].rangeTo = gs['dashboard-default-max-year-range'];
  settings['year-range'].to = gs['dashboard-default-max-year-range'];
  settings['year-range'].type = 'INT_VALUE';
  settings['year-range'].from = `${Math.floor((Number(settings['year-range'].to)
    - Number(settings['year-range'].rangeFrom)) / 2) + Number(settings['year-range'].rangeFrom)}`;
  return settings;
}
