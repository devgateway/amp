// @flow
import { STATE_TRANSLATIONS_LOADED } from '../actions/StartUpAction.jsx';

const defaultState = {
    translations: {}
};

export default function startUp(state: Object = defaultState, action: Object) {
    console.log('HomePage');
    switch (action.type) {
        case STATE_TRANSLATIONS_LOADED:
            return Object.assign({}, state, {
                translations: action.actionData.translations,
            });
        default:
            return state;
    }
}
