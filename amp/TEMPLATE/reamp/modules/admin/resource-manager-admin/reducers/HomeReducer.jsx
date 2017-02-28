// @flow
import {
    STATE_TYPES_LOADED,
    STATE_TYPES_LOADING,
    STATE_ALLOWED_TYPES_ERROR,
    STATE_ALLOWED_TYPES_LOADING,
    STATE_ALLOWED_TYPES_LOADED,
    STATE_SETTINGS_SAVE_ERROR,
    STATE_SETTINGS_SAVE_SAVED,
    STATE_SETTINGS_SAVE_SAVING,
    STATE_SETTINGS_LOADED,
    STATE_SETTINGS_ERROR,
    STATE_SETTINGS_LOADING
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
    isSavingTypes: false,
    loadingSettings: false,
    settingsLoaded: false,
    settingsList: {}

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
        case STATE_SETTINGS_SAVE_SAVING:
            return Object.assign({}, state, {
                isSavingTypes: true
            });
            break;

        case STATE_SETTINGS_SAVE_SAVED:
            let newState = Object.assign({}, state);
            newState.settingsList[0].value = action.actionData.resourceSettings['maximum-file-size'];
            newState.settingsList[1].value = action.actionData.resourceSettings['limit-file-to-upload'];
            newState.settingsList[2].value.defaultId = action.actionData.resourceSettings['sort-column'];
            newState.isSavingTypes = false;
            newState.alert = ALERT_TYPE.SUCCESS;
            return newState;
            break;
        case STATE_SETTINGS_LOADING:
            return Object.assign({}, state, {
                loadingTypesAllowed: true
            });

            break;
        case STATE_SETTINGS_LOADED:
            return Object.assign({}, state, {
                loadingTypesAllowed: false,
                settingsLoaded: true,
                settingsList: action.actionData,
            });
            break;

        case STATE_SETTINGS_ERROR:
        case STATE_SETTINGS_SAVE_ERROR:
        case STATE_ALLOWED_TYPES_ERROR:
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
