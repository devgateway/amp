import * as AC from './ActivityConstants';
import * as FM from  './FeatureManagerConstants';

export const ACTIVITY_FIELDS_FM_PATH = {};
ACTIVITY_FIELDS_FM_PATH[AC.MODIFIED_BY] = FM.ACTIVITY_LAST_UPDATED_BY;
ACTIVITY_FIELDS_FM_PATH[AC.UPDATE_DATE] = FM.ACTIVITY_LAST_UPDATED_ON;