import * as AMP from 'amp/architecture';
import {loadTranslations} from 'amp/modules/translate';
import Home from '../components/HomePage/Home.jsx';

export const STATE_TRANSLATIONS_LOADED = 'STATE_TRANSLATIONS_LOADED';

export function resourceManagerStartUp(store) {
    const translations = Home.translations();
    return new Promise((resolve, reject) => {
        let toTranslate = new AMP.Model().toJS();
        loadTranslations(translations).then(trns => {
            toTranslate = trns;
            store.dispatch({
                type: STATE_TRANSLATIONS_LOADED,
                actionData: { translations: toTranslate }
            });
        });
        resolve();
    });
}
