// @flow
import {
    STATE_TYPES_LOADED,
    STATE_TYPES_LOADING,
    STATE_ALLOWED_TYPES_ERROR,
    STATE_ALLOWED_TYPES_LOADING,
    STATE_ALLOWED_TYPES_LOADED,
    STATE_ALLOWED_TYPES_SAVE_SAVING,
    STATE_ALLOWED_TYPES_SAVE_SAVED,
    STATE_ALLOWED_TYPES_SAVE_ERROR
} from '../actions/HomeActions.jsx';
import { ALERT_TYPE } from '../utils/constants.jsx';
const defaultState = {
    loadingTypesAvailable: false,
    typesLoaded: false,
    typesAvailable: [],
    loadingTypesAllowed: false,
    allowedLoaded: false,
    typesAllowed: [],
    alert: ALERT_TYPE.NONE,
    alertMsg: '',
    isSavingTypes: false

};

export default function homePage(state: Object = defaultState, action: Object) {
    console.log('HomePage');
    switch (action.type) {
        case STATE_TYPES_LOADING:
            return Object.assign({}, state, {
                loadingTypesAvailable: true
            });
            isSavingTypes
        case STATE_TYPES_LOADED:
            return Object.assign({}, state, {
                    loadingTypesAvailable: false,
                    typesLoaded: true,
                    typesAvailable: action.actionData,
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
                    loadingTypesAllowed: false,
                    allowedLoaded: true,
                    typesAllowed: action.actionData,
                }
            )
            break;
        case STATE_ALLOWED_TYPES_SAVE_SAVING:
            return Object.assign({}, state, {
                isSavingTypes: true
            });
            break;

        case STATE_ALLOWED_TYPES_SAVE_SAVED:

            return Object.assign({}, state, {
                isSavingTypes: false,
                alert: ALERT_TYPE.SUCCESS
            });
            break;

        case STATE_ALLOWED_TYPES_SAVE_ERROR:
        case STATE_ALLOWED_TYPES_ERROR:
            debugger;
            return Object.assign({}, state, {
                isSavingTypes: false,
                alert: ALERT_TYPE.ERROR,
                alertMsg: action.actionData
            });
            break;

        default:
            return state;
    }
}
