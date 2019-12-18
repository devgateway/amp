import { STATE_TRANSLATIONS_LOADED } from '../actions/StartUpAction';
import TranslationManager from '../utils/TranslationManager';

/**
 *
 */


const defaultState = {
    translations: {},
    translationsLoaded: false
};

export default function startUpReducer(state: Object = defaultState, action: Object) {

    switch (action.type) {
        case STATE_TRANSLATIONS_LOADED:
            return Object.assign({}, state, {
                translations: action.payload.translations,
                translationsLoaded: true
            });
        default:
            return state;
    }
}
