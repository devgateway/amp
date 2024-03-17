import * as AMP from 'amp/architecture';
import { loadTranslations } from 'amp/modules/translate';
import { initialTranslations } from '../common/Translations.jsx';
export const STATE_TRANSLATIONS_LOADED = 'STATE_TRANSLATIONS_LOADED';

export function gpiStartUp(store) {
    return new Promise((resolve, reject) => {
        let toTranslate = new AMP.Model().toJS();
        loadTranslations(initialTranslations).then(trns => {
            toTranslate = trns;
                store.dispatch({
                type: STATE_TRANSLATIONS_LOADED,
                actionData: { 
                    translations: toTranslate,
                    translate: function(messageKey, params = {}){
                        var message = toTranslate[messageKey];
                        for(var key in params) {
                            message = message.replace('__' + key + '__', params[key]);
                        }
                        return message;
                    }                    
                }
            });
        });
        resolve();
    });
}