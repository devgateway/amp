export const ZERO = 0;
export const ONE = 1;
export const NEGATIVE_ONE = -1;
export const FLAGS_DIRECTORY = '/assets/img/flags/';

export const DASHBOARD_DEFAULT_MIN_YEAR_RANGE = 'dashboard-default-min-year-range';
export const DASHBOARD_DEFAULT_MAX_YEAR_RANGE = 'dashboard-default-max-year-range';

export const API_FILTERS_SECTORS_URL = '/rest/filters/sectors';
export const API_FILTERS_COUNTRIES_URL = '/rest/filters/locations?firstLevelOnly=true&showAllCountries=true';
export const API_AMP_SETTINGS_URL = '/rest/amp/settings';
export const API_FILTERS_MODALITIES_URL = '/rest/filters/modalities?sscWorkspace=true';
export const API_SSC_DASHBOARD_URL = '/rest/gis/sscdashboard';
export const API_XLS_REPORT_URL = '/rest/gis/sscdashboard/xsl';
export const API_REPORTS_ACTIVITY_DETAIL_URL = '/rest/gis/activities';

export const BOOTSTRAP_COLUMNS_COUNT = 12;

export const NON_SELECTED_LINE_COLOR = '#fd8f00';
export const NON_SELECTED_BUBBLE_COLOR = '#fd8f00';

export const SELECTED_LINE_COLOR = '#007236';
export const SELECTED_BUBBLE_COLOR = '#007236';
export const PNG_FORMAT = 'png';
export const PROJECT_LENGTH_HOME_PAGE = 50;
export const UNDEFINED_FILTER = -999999999;
export const FLAGS_TO_SHOW_DEFAULT = 9;
export const FLAGS_TO_SHOW_SMALL = 6;
export const FLAGS_TO_SHOW_SMALL_WIDTH_THRESHOLD = 1766;
export const SECTORS_LIMIT_CHART = 5;
export const SECTORS_OTHERS_ID_CHART = '-1';
export const DEFAULT_SCREEN_SIZE = 1920;
export const DEFAULT_ELLIPSIS = 145;
export const SECTOR_MODALITY_MIN_SIZE = 2;
export const SECTOR_MODALITY_LEADING_LEFT = '0';
export const WS_PREFIX = 'SSC_';

export const HOME_CHART = 'home';
export const SECTORS_CHART = 'sector';
export const MODALITY_CHART = 'modality';
export const DOWNLOAD_CHART = 'download';
export const COUNTRY_COLUMN = 'country-column';
// eslint-disable-next-line max-len
export const FALLBACK_FLAG = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAbCAQAAACkGQXlAAAAI0lEQVR42mP8L8lAVcA4auCogaMGjho4auCogaMGjhpINAAAOBcdpLw/CDsAAAAASUVORK5CYII=';
export const OTHERS_CODE = '000';
export const INVALID_COLUMN_ERROR_CODE = '0602';

export const SECTOR_COLOR_MAP = new Map([
  ['110', '#d46453'], ['120', '#f5a15d'], ['130', '#ffcf8e'], ['140', '#ff7a7d'], ['150', '#ff417d'],
  ['160', '#d61a88'], ['210', '#fff540'], ['220', '#28c074'], ['230', '#429058'], ['240', '#bd4035'],
  ['250', '#b0fff1'], ['311', '#144491'], ['312', '#9b0e3e'], ['313', '#10908e'], ['321', '#8f5765'],
  ['322', '#c7d4e1'], ['323', '#77b02a'], ['331', '#488bd4'], ['332', '#928fb8'], ['400', '#c6d831'],
  ['500', '#d41e3c'], ['600', '#cf968c'], ['700', '#78d7ff'], ['910', '#94007a'], ['920', '#ffb84a'],
  ['930', '#ed7b39'], ['998', '#ed4c40'], ['000', '#8f5765']
]);
export const MODALITIES_COLOR_MAP = new Map([
  [OTHERS_CODE, '#8f5765']]);

export const MODALITIES_COLOR = ['#00429d', '#3761ab', '#5681b9', '#73a2c6', '#93c4d2', '#b9e5dd',
  '#ffffe0', '#ffd3bf', '#ffa59e', '#f4777f', '#dd4c65', '#be214d', '#93003a'];
export const COLOR_MAP_CUSTOM = new Map([[
  MODALITY_CHART, MODALITIES_COLOR
]]);

export const COLOR_MAP = new Map([
  [SECTORS_CHART, SECTOR_COLOR_MAP],
  [MODALITY_CHART, MODALITIES_COLOR_MAP]
]);
