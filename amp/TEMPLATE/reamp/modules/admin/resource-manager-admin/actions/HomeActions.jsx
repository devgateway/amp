export const STATE_LOADING_OK = 'STATE_LOADING_OK';
export const STATE_TYPES_LOADING = 'STATE_TYPES_LOADING';
export const STATE_TYPES_LOADED = 'STATE_TYPES_LOADED';

export const STATE_ALLOWED_TYPES_LOADING = 'STATE_ALLOWED_TYPES_LOADING';
export const STATE_ALLOWED_TYPES_LOADED = 'STATE_ALLOWED_TYPES_LOADED';
export const STATE_ALLOWED_TYPES_ERROR = 'STATE_ALLOWED_TYPES_ERROR';

export const STATE_ALLOWED_TYPES_SAVE_SAVING = 'STATE_ALLOWED_TYPES_SAVE_SAVING';
export const STATE_ALLOWED_TYPES_SAVE_SAVED = 'STATE_ALLOWED_TYPES_SAVE_SAVED';
export const STATE_ALLOWED_TYPES_SAVE_ERROR = 'STATE_ALLOWED_TYPES_SAVE_ERROR';

import {
    ALLOWED_FILE_TYPES_ENDPOINT,
    REST_BASE,
    AVAILABLE_FILE_TYPES_ENDPOINT,
    SAVE_FILE_TYPES_ENDPOINT
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

export function saveAllowedTypes(saveAllowedTypes) {
    return (dispatch, ownProps) => {
        _sendingRequest(STATE_ALLOWED_TYPES_SAVE_SAVING);
        let allowedTypesToSave = [];
        saveAllowedTypes.forEach((element) => {
            allowedTypesToSave.push(element.name);
        });
        postJson(REST_BASE + SAVE_FILE_TYPES_ENDPOINT, allowedTypesToSave).then((result) => {
            if (result.status === 500) {
                throw result.statusText;
            } else {
                dispatch({
                        type: STATE_ALLOWED_TYPES_SAVE_SAVED,
                        actionData: ''
                    }
                );
            }

        }).catch((error) => {
            dispatch({
                    type: STATE_ALLOWED_TYPES_SAVE_ERROR,
                    actionData: error
                }
            );
        });
        console.log(saveAllowedTypes);
        console.log('save allowed types');
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
