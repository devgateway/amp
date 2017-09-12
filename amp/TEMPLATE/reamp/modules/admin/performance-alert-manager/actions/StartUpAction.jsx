import * as AMP from 'amp/architecture';
import { loadTranslations } from 'amp/modules/translate';
import { initialTranslations } from '../common/Translations.jsx';
export const STATE_TRANSLATIONS_LOADED = 'STATE_TRANSLATIONS_LOADED';

export function startUp( store ) {    
        let toTranslate = new AMP.Model().toJS();
        return loadTranslations( initialTranslations ).then( trns => {
            toTranslate = trns;
            return store.dispatch( {
                type: STATE_TRANSLATIONS_LOADED,
                actionData: {
                    translations: toTranslate,
                    translate: function( messageKey, params = {}) {
                        let message = toTranslate[messageKey];
                        for ( let key in params ) {
                            message = message.replace( '__' + key + '__', params[key] );
                        }
                        return message;
                    }
                }
            });
        });       
}