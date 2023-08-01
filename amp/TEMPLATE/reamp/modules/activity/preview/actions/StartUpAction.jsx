import {loadTranslations} from 'amp/modules/translate';
import {initialTranslations} from '../common/initialTranslations';
import TranslationManager from '../utils/TranslationManager';
import StartUpApi from "../api/StartUpApi";
import {GS_DEFAULT_CALENDAR} from '../common/ReampConstants';

/**
 *
 */
export const STATE_TRANSLATIONS_LOADED = 'STATE_TRANSLATIONS_LOADED';
export const STATE_APP_INITIALIZED = 'STATE_APP_INITIALIZED';

const fetchGlobalSettingsAndCalendar = () => StartUpApi.fetchGlobalSettings()
  .then(globalSettings => StartUpApi.fetchCalendar(globalSettings[GS_DEFAULT_CALENDAR])
    .then(calendars => ({ globalSettings, calendar: calendars.find(calendar => calendar.id == globalSettings[GS_DEFAULT_CALENDAR]) })));

export function startUp(dispatch) {
    return Promise.all([
        StartUpApi.fetchSettings(),
        fetchGlobalSettingsAndCalendar(),
        loadTranslations(initialTranslations),
    ]).then(([settings, gsAndCal, trns]) => {
        TranslationManager.initializeTranslations(trns);
        return dispatch({
            type: STATE_APP_INITIALIZED,
            payload: {
                translations: trns,
                settings,
                ...gsAndCal,
            }
        });
    });
}
