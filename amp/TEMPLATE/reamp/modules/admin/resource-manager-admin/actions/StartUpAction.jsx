import { store } from '../script.es6'
import * as AMP from "amp/architecture";
import { loadTranslations } from "amp/modules/translate";

export const STATE_TRANSLATIONS_LOADED = 'STATE_TRANSLATIONS_LOADED';

export function resourceManagerStartUp() {
    debugger;
    return new Promise((resolve, reject) => {
        let toTranslate = new AMP.Model(translations).toJS();
        loadTranslations(translations).then(trns => {
            toTranslate = trns;
            console.log(trns);
            debugger;
            store.dispatch({
                type: STATE_TRANSLATIONS_LOADED,
                actionData: { translations: toTranslate }
            });
        });
        resolve();
    });
}
let translations = {
    'amp.resource-manager:resource-manager-title': 'Resource manager Admin',
};
