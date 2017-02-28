import { store } from '../script.es6';
import * as AMP from 'amp/architecture';
import { loadTranslations } from 'amp/modules/translate';
import { initialTranslations } from '../utils/translations.jsx';
export const STATE_TRANSLATIONS_LOADED = 'STATE_TRANSLATIONS_LOADED';

export function gpiStartUp() {    
    return new Promise((resolve, reject) => {
        let toTranslate = new AMP.Model().toJS();
        loadTranslations(initialTranslations).then(trns => {
            toTranslate = trns;
                store.dispatch({
                type: STATE_TRANSLATIONS_LOADED,
                actionData: { translations: toTranslate }
            });
        });
        resolve();
    });
}