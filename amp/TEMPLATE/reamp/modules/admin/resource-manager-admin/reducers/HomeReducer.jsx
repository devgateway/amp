// @flow
import {
    STATE_TYPES_LOADED,
    STATE_TYPES_LOADING,
    STATE_ALLOWED_TYPES_LOADING,
    STATE_ALLOWED_TYPES_LOADED
} from "../actions/HomeActions.jsx";

const defaultState = {
    loadingTypesAvailable: false,
    mimeTypesAvailable: [],
    loadingTypesAllowed: false,
    mimeTypesAllowed: [],
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
        case STATE_TYPES_LOADING:
            return Object.assign({}, state, {
                loadingTypesAvailable: true
            });
        case STATE_TYPES_LOADED:
            return Object.assign({}, state, {
                    loadingTypesAvailable: false,
                    mimeTypesAvailable: action.actionData,
                }
            )
            break;
        case STATE_ALLOWED_TYPES_LOADING:
            return Object.assign({}, state, {
                loadingTypesAllowed: true
            });
            break;
        case STATE_ALLOWED_TYPES_LOADED:

            return Object.assign({}, state, {
                    loadingTypesAvailable: false,
                    mimeTypesAllowed: action.actionData,
                }
            )
            break;
        default:
            return state;
    }
}
