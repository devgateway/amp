export const STATE_LOADING_OK = 'STATE_LOADING_OK';
export const STATE_TYPES_LOADING = 'STATE_TYPES_LOADING';
export const STATE_TYPES_LOADED = 'STATE_TYPES_LOADED';

export const STATE_ALLOWED_TYPES_LOADING = 'STATE_ALLOWED_TYPES_LOADING';
export const STATE_ALLOWED_TYPES_LOADED = 'STATE_ALLOWED_TYPES_LOADED';

export const STATE_ALLOWED_TYPES_SAVING = 'STATE_ALLOWED_TYPES_SAVING';
export const STATE_ALLOWED_TYPES_SAVED = 'STATE_ALLOWED_TYPES_SAVED';

import { postJson, delay, fetchJson } from "amp/tools";

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
        fetchJson('/rest/mimetypes').then((mimeTypesAvailable) => {
            //we remove dupliqutes till we fix the API
            const uniqueMimeTypesAvailable = removeDuplicates(mimeTypesAvailable, 'name');
            dispatch({
                    type: STATE_TYPES_LOADED,
                    actionData: uniqueMimeTypesAvailable
                }
            )
            ;
        });
    }
}
export function loadAllowedTypes() {
    return (dispatch, ownProps) => {
        dispatch(_sendingRequest(STATE_ALLOWED_TYPES_LOADING));
        fetchJson('/rest/mimetypes/allowed').then((mimeTypesAllowed) => {
            //we remove dupliqutes till we fix the API
            debugger;
            const uniqueMimeTypesAllowed = removeDuplicates(mimeTypesAllowed, 'name');
            dispatch({
                    type: STATE_ALLOWED_TYPES_LOADED,
                    actionData: uniqueMimeTypesAllowed
                }
            )
            ;
        });
    }
}

export function saveAllowedTypes(saveAllowedTypes) {
    return (dispatch, ownProps) => {
        _sendingRequest(STATE_ALLOWED_TYPES_SAVING);
        let allowedTypesToSave = [];
        saveAllowedTypes.forEach((element) => {
            allowedTypesToSave.push(element.name);
        });

        postJson('/rest/mimetypes/allowed', allowedTypesToSave).then((result) => {
            debugger;
            console.log(result);
        }).catch((error) => {
            debugger;
            console.log(error);
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
