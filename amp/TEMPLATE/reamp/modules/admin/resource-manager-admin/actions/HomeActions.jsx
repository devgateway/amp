export const STATE_LOADING_OK = 'STATE_LOADING_OK';
export const STATE_TYPES_LOADING = 'STATE_TYPES_LOADING';
export const STATE_TYPES_LOADED = 'STATE_TYPES_LOADED';

export const STATE_ALLOWED_TYPES_LOADING = 'STATE_ALLOWED_TYPES_LOADING';
export const STATE_ALLOWED_TYPES_LOADED = 'STATE_ALLOWED_TYPES_LOADED';
export const STATE_ALLOWED_TYPES_ERROR = 'STATE_ALLOWED_TYPES_ERROR';

export const STATE_SETTINGS_SAVE_SAVING = 'STATE_SETTINGS_SAVING';
export const STATE_SETTINGS_SAVE_SAVED = 'STATE_SETTINGS_SAVED';
export const STATE_SETTINGS_SAVE_ERROR = 'STATE_SETTINGS_ERROR';

export const STATE_SETTINGS_LOADING = 'STATE_SETTINGS_LOADING';
export const STATE_SETTINGS_LOADED = 'STATE_SETTINGS_LOADED';
export const STATE_SETTINGS_ERROR = 'STATE_SETTINGS_ERROR';
import {
    ALLOWED_FILE_TYPES_ENDPOINT,
    REST_BASE,
    AVAILABLE_FILE_TYPES_ENDPOINT,
    SAVE_FILE_TYPES_ENDPOINT,
    SETTINGS_ENDPOINT
} from '../utils/constants.jsx';
import { postJson, delay, fetchJson } from 'amp/tools';

export function loadAction() {
    console.log('loadAction');
    return (dispatch) => {
        return {
            type: STATE_LOADING_OK
        };
    };
}
function _loadSettings() {
    return new Promise((resolve, reject) => {
        fetchJson(REST_BASE + SETTINGS_ENDPOINT).then((settings) => {
            resolve(settings)
        }).catch((error) => {
            reject(error);
        });
    });
}
export function loadSettings() {
    return (dispatch, ownProps) => {
        dispatch(_sendingRequest(STATE_SETTINGS_LOADING));
        _loadSettings().then((settings) => {
            dispatch({
                    type: STATE_SETTINGS_LOADED,
                    actionData: settings
                }
            );
        }).catch((error) => {
            dispatch({
                    type: STATE_SETTINGS_ERROR,
                    actionData: error
                }
            );
        });
    }
}
export function loadAvailableTypes() {
    return (dispatch, ownProps) => {
        dispatch(_sendingRequest(STATE_TYPES_LOADING));

        fetchJson(REST_BASE + AVAILABLE_FILE_TYPES_ENDPOINT).then((typesAvailable) => {
            if (typesAvailable.error) {
                //we need to iterate throw errors and show them
                throw 'Internal server error';
            } else {
                dispatch({
                        type: STATE_TYPES_LOADED,
                        actionData: typesAvailable
                    }
                );
            }
        }).catch((error) => {
            dispatch({
                    type: STATE_ALLOWED_TYPES_ERROR,
                    actionData: error
                }
            );
        });
    }
}
export function loadAllowedTypes() {
    return (dispatch, ownProps) => {
        dispatch(_sendingRequest(STATE_ALLOWED_TYPES_LOADING));
        fetchJson(REST_BASE + ALLOWED_FILE_TYPES_ENDPOINT).then((typesAllowed) => {
            if (typesAllowed.error) {
                //we need to iterate throw errors and show them
                throw 'Internal server error';
            } else {
                dispatch({
                        type: STATE_ALLOWED_TYPES_LOADED,
                        actionData: typesAllowed
                    }
                );
            }
        }).catch((error) => {
            dispatch({
                    type: STATE_ALLOWED_TYPES_ERROR,
                    actionData: error
                }
            );
        });
    }
}

export function saveSettings(saveAllowedTypes, settingsValuesSelected) {

    return (dispatch, ownProps) => {
        _sendingRequest(STATE_SETTINGS_SAVE_SAVING);
        let allowedTypesToSave = [];
        saveAllowedTypes.forEach((element) => {
            allowedTypesToSave.push(element.name);
        });
        let valuesToSave = {};
        valuesToSave.allowedFileType = allowedTypesToSave;
        valuesToSave.resourceSettings = {};
        valuesToSave.resourceSettings['maximum-file-size'] = settingsValuesSelected.maximumFileSize;
        valuesToSave.resourceSettings['sort-column'] = settingsValuesSelected.resourceSortOption.toString();
        valuesToSave.resourceSettings['limit-file-to-upload'] = settingsValuesSelected.limitFileToUpload === 1 ? "true" : "false";

        postJson(REST_BASE + SAVE_FILE_TYPES_ENDPOINT, valuesToSave).then((result) => {
            if (result.status === 500) {
                throw result.statusText;
            } else {
                //once settings are saved we send them to the reducer
                //so they are properly saved in redux props
                dispatch({
                        type: STATE_SETTINGS_SAVE_SAVED,
                        actionData: valuesToSave
                    }
                );
            }

        }).catch((error) => {
            dispatch({
                    type: STATE_SETTINGS_SAVE_ERROR,
                    actionData: error
                }
            );
        });

    }
}

function removeDuplicates(myArr, prop) {
    return myArr.filter((obj, pos, arr) => {
        return arr.map(mapObj => mapObj[prop]).indexOf(obj[prop]) === pos && obj.description != '';
    });
}

function _sendingRequest(type) {
    return {
        type: type
    };
}
