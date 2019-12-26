import { loadTranslations } from 'amp/modules/translate';
import { initialTranslations } from '../common/initialTranslations';
import TranslationManager from '../utils/TranslationManager';
import StartUpApi from "../api/StartUpApi";

/**
 *
 */
export const STATE_TRANSLATIONS_LOADED = 'STATE_TRANSLATIONS_LOADED';
export const STATE_APP_INITIALIZED = 'STATE_APP_INITIALIZED';

export function startUp(dispatch) {
    return Promise.all([StartUpApi.fetchSettings(),
        loadTranslations(initialTranslations)]).then(([settings, trns]) => {
        TranslationManager.initializeTranslations(trns);
        return dispatch({
            type: STATE_APP_INITIALIZED,
            payload: {
                translations: trns,
                settings
            }
        });
    });
}
