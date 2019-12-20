import * as AMP from 'amp/architecture';
import { loadTranslations } from 'amp/modules/translate';
import { initialTranslations } from '../common/initialTranslations';
import TranslationManager from '../utils/TranslationManager';

/**
 *
 */
export const STATE_TRANSLATIONS_LOADED = 'STATE_TRANSLATIONS_LOADED';

export function startUp(store) {
    return new Promise((resolve, reject) => {
        let toTranslate = new AMP.Model().toJS();
        loadTranslations(initialTranslations).then(trns => {
            TranslationManager.initializeTranslations(trns);
            toTranslate = trns;
            store.dispatch({
                type: STATE_TRANSLATIONS_LOADED,
                payload: {
                    translations: trns
                }
            });
        });
        resolve();
    });
}
