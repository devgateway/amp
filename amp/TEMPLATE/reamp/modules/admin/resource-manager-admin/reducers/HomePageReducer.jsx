// @flow
import { STATE_LOADING_OK } from '../actions/HomePageActions.jsx';

const defaultState = {
    loaded: false,
};

/**
 * This reducer saves info related to the login process only.
 * @param state
 * @param action
 * @returns {*}
 */
export default function homePage(state: Object = defaultState, action: Object) {
    console.log('HomePage');
    switch (action.type) {
        case STATE_LOADING_OK:
            return Object.assign({}, state, {
                loggedIn: true,
                testing:'Hola'
            });
        default:
            return state;
    }
}
