//TODO DELETE THIS ROUTE
export const ROUTES_SECTOR = '/ssc/sectors';
export const ROUTES_HOME = '/ssc/home';
//TODO DELETE THIS ROUTE
export const FLAGS_DIRECTORY = '/assets/img/flags/';
export const FLAG_DEFAULT = '/assets/img/flags/';
export const DEVELOPMENT = 'development';

export const DASHBOARD_DEFAULT_MIN_YEAR_RANGE = 'dashboard-default-min-year-range';
export const DASHBOARD_DEFAULT_MAX_YEAR_RANGE = 'dashboard-default-max-year-range';

export const API_FILTERS_SECTORS_URL = '/rest/filters/sectors';
export const API_FILTERS_COUNTRIES_URL = '/rest/filters/locations?firstLevelOnly=true&showAllCountries=true';
export const API_AMP_SETTINGS_URL = '/rest/amp/settings';
export const API_FILTERS_MODALITIES_URL = '/rest/filters/modalities?sscWorkspace=true';
export const API_SSC_DASHBOARD_URL = '/rest/gis/sscdashboard';

export const API_REPORTS_ACTIVITY_DETAIL_URL = '/rest/gis/activities';

export const BOOTSTRAP_COLUMNS_COUNT = 12;

export const NON_SELECTED_LINE_COLOR = '#fd8f00';
export const NON_SELECTED_BUBBLE_COLOR = '#fd8f00';

export const SELECTED_LINE_COLOR = '#007236';
export const SELECTED_BUBBLE_COLOR = '#007236';
export const PNG_FORMAT = 'png';
export const SVG_FORMAT = 'svg';
export const PROJECT_LENGTH_HOME_PAGE = 50;
export const UNDEFINED_FILTER = -999999999;
export const FLAGS_TO_SHOW_DEFAULT = 9;
export const FLAGS_TO_SHOW_SMALL = 6;
export const SECTORS_LIMIT_CHART = 5;
export const SECTORS_OTHERS_ID_CHART = "-1";
export const DEFAULT_SCREEN_SIZE = 1920;
export const DEFAULT_ELLIPSIS = 145;
export const SECTOR_MIN_SIZE = 2;
export const SECTOR_LEADING_LEFT = '0';

export const HOME_CHART = 'home';
export const SECTORS_CHART = 'sector';
export const MODALITY_CHART = 'modality';
export const DOWNLOAD_CHART = 'download';
export const COUNTRY_COLUMN = 'country-column';
export const FALLBACK_FLAG = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAbCAQAAACkGQXlAAAAI0lEQVR42mP8L8lAVcA4auCogaMGjho4auCogaMGjhpINAAAOBcdpLw/CDsAAAAASUVORK5CYII='
export const OTHERS_CODE = '000';

export const COLOR_MAP = new Map([
    ['11', '#d46453'], ['120', '#f5a15d'], ['130', '#ffcf8e'], ['140', '#ff7a7d'], ['150', '#ff417d'],
    ['160', '#d61a88'], ['210', '#fff540'], ['220', '#28c074'], ['230', '#429058'], ['240', '#bd4035'],
    ['250', '#b0fff1'], ['311', '#144491'], ['312', '#9b0e3e'], ['313', '#10908e'], ['321', '#8f5765'],
    ['322', '#c7d4e1'], ['323', '#77b02a'], ['331', '#488bd4'], ['332', '#928fb8'], ['400', '#c6d831'],
    ['500', '#d41e3c'], ['600', '#cf968c'], ['700', '#78d7ff'], ['910', '#94007a'], ['920', '#ffb84a'],
    ['930', '#ed7b39'], ['998', '#ed4c40'], ['000', '#8f5765']
]);

