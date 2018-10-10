import * as AC from './ActivityConstants'
import DateUtils from "./DateUtils";
/**
 *    
 */

export default class ActivityUtils {

    static getTitle(field, settings) {
        let ret = '';
        let lang = settings ? settings[AC.LANGUAGE] : 'en';
        if (field && field.field_label) {
            if (field.field_label[lang.toLowerCase()]) {
                ret = field.field_label[lang.toLowerCase()];
            } else if (field.field_label[lang.toUpperCase()]) {
                ret = field.field_label[lang.toUpperCase()];
            }
        }
        return ret;
    }

    static calculateDurationOfProjects(activityFieldsManager, activity, settings, translations) {
        const showIfNotAvailable = new Set([AC.PROPOSED_PROJECT_LIFE, AC.PROPOSED_APPROVAL_DATE, AC.ACTUAL_APPROVAL_DATE,
            AC.PROPOSED_START_DATE, AC.ACTUAL_START_DATE, AC.PROPOSED_COMPLETION_DATE, AC.ACTUAL_COMPLETION_DATE]);
        const isActualCompletionDateEnabledWithValue = activityFieldsManager.isFieldPathEnabled(AC.ACTUAL_COMPLETION_DATE)
            && activity[AC.ACTUAL_COMPLETION_DATE].value;
        const isProposedCompletionDateEnabledWithValue = activityFieldsManager.isFieldPathEnabled(AC.PROPOSED_COMPLETION_DATE)
            && activity[AC.PROPOSED_COMPLETION_DATE].value;
        const isActualStartDateEnabledWithValue = activityFieldsManager.isFieldPathEnabled(AC.ACTUAL_START_DATE)
            && activity[AC.ACTUAL_START_DATE].value;

        let endDateHelper = isActualCompletionDateEnabledWithValue ? activity[AC.ACTUAL_COMPLETION_DATE].value :
            (isProposedCompletionDateEnabledWithValue ? activity[AC.PROPOSED_COMPLETION_DATE].value : null);

        let startDate = isActualStartDateEnabledWithValue ?
            DateUtils.createFormattedDate(activity[AC.ACTUAL_START_DATE].value, settings) : null;

        let endDate = endDateHelper ? DateUtils.createFormattedDate(endDateHelper, settings) : null;
        let duration = translations['amp.activity-preview:noData'];
        if (startDate && endDate) {
            duration = DateUtils.durationImproved(startDate, endDate, settings) + ' ' + translations['months'];
        }
        return {showIfNotAvailable, duration};
    }
}