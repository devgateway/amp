import {STATE_TRANSLATIONS_LOADED, STATE_APP_INITIALIZED} from '../actions/StartUpAction';
import TranslationManager from '../utils/TranslationManager';

/**
 *
 */


const defaultState = {
    translations: {},
    isStartupInProgress: true,
    settings: undefined
};

export default function startUpReducer(state: Object = defaultState, action: Object) {

    switch (action.type) {
        case STATE_APP_INITIALIZED:
            return Object.assign({}, state, {
                translations: action.payload.translations,
                isStartupInProgress: false,
                settings: action.payload.settings,
                globalSettings: action.payload.globalSettings,
                calendar: action.payload.calendar,
            });

        default:
            return state;
    }
}
