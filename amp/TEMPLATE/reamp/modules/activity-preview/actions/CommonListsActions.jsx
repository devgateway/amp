import commonListsApi from '../api/CommonListsApi';
import * as AC from '../utils/ActivityConstants'


/**
 * @author Daniel Oliva
 */

export function getSettingsLoading(){
    return {type: 'LOADING_SETTINGS'}
}

export function getSettingsSuccess(settings){
    return {type: 'LOAD_SETTINGS_SUCCESS', settings: settings}
}

export function getActivityInfoLoading(){
    return {type: 'LOADING_ACTIVITY_INFO'}
}

export function getActivityInfoSuccess(activityInfo){
    return {type: 'LOAD_ACTIVITY_INFO_SUCCESS', activityInfo: activityInfo}
}

export function getActivityLoading(){
    return {type: 'LOADING_ACTIVITY'}
}

export function getActivitySuccess(activity){
    return {type: 'LOAD_ACTIVITY_SUCCESS', activity: activity}
}

export function getActivityError(errorMsg){
    return {type: 'LOAD_ACTIVITY_ERROR', errorMsg: errorMsg}
}

export function getHydratedActivityLoading(hydratedActivity){
    return {type: 'LOADING_HYDRATED_ACTIVITY', hydratedActivity: [hydratedActivity]}
}

export function getHydratedActivitySuccess(hydratedActivity){
    return {type: 'LOAD_HYDRATED_ACTIVITY_SUCCESS', hydratedActivity: [hydratedActivity]}
}

export function getFieldsSuccess(fields){
    return {type: 'LOAD_FIELDS_SUCCESS', fields: fields}
}

export function getFieldSubListSuccess(){
    return {type: 'LOAD_FIELD_SUBLIST_SUCCESS'}
}

export function getSettings(){
    return function(dispatch) {
        dispatch(getSettingsLoading());
        return commonListsApi.getSettings().then(settings => {
            dispatch(getSettingsSuccess(settings));
        }).catch(error => {
            throw(error);
        });
    }
}

export function getActivityAndFields(activityId){
    return function(dispatch) {
        dispatch(getHydratedActivityLoading());
        let hydratedActivity = {};
        return commonListsApi.getActivity(activityId).then(activity => {
            if (!activity.error) {
                dispatch(getActivitySuccess(activity));
                dispatch(getActivityInfoLoading());
                commonListsApi.getActivityInfo(activityId).then(activityInfo => {
                    dispatch(getActivityInfoSuccess(activityInfo));
                }).catch(error => {
                    throw(error);
                });
                hydratedActivity = _createHydratedActivity(Object.keys(activity), activity);
                dispatch(getHydratedActivityLoading(hydratedActivity));
                commonListsApi.getFields().then(fields => {
                    dispatch(getFieldsSuccess(fields));
                    _addLabelAndType(hydratedActivity, fields);
                    _addRealValue(hydratedActivity).then(response => {;
                        dispatch(getHydratedActivitySuccess(hydratedActivity));
                    }).catch(error => {
                        throw(error);
                    });
                }).catch(error => {
                    throw(error);
                });
            } else {
                let errorMsg = '';
                let keys = Object.keys(activity.error);
                for(var key in keys) {
                    activity.error[keys[key]].forEach(error => {errorMsg += error + ' '});
                }
                dispatch(getActivityError(errorMsg));
            }
        }).catch(error => {
            dispatch(getActivityError(error));
            throw(error);
        });
    }
}

function _createHydratedActivity(keys, obj) {
    let ret = {}
    for(var key in keys) {
        let value = obj[keys[key]];
        if (Array.isArray(value)) {            
            var childs = [];
            for(var child in value) {
                childs.push( _createHydratedActivity(Object.keys(value[child]), value[child]));
            }
            value = childs;  
        }        
        ret[keys[key]] = {
            value: value
        }
    }
    return ret;
}

function _addLabelAndType(hydratedActivity, fields) {
    for(var field in fields) {
        let fieldObj = fields[field];
        if (fieldObj !== undefined) {
            let hydratedField = hydratedActivity[fieldObj.field_name];
            if (hydratedField) {
                hydratedField['field_label'] = fieldObj.field_label ? fieldObj.field_label : '';
                hydratedField['field_type'] = fieldObj.field_type;
                if (fieldObj.children !== undefined && hydratedField.value !== undefined && 
                    Array.isArray(hydratedField.value)) {
                    for(var child in hydratedField.value) {
                        _addLabelAndType(hydratedField.value[child], fieldObj.children);
                    }
                }
            }
        }
    }
}

function _addRealValue(hydratedActivity, parentName) {
    return new Promise((resolve, reject) => {
        let requestData = {};
        _createRequestDataHelper(requestData, hydratedActivity, parentName);
        commonListsApi.fetchFieldsData(requestData).then(fields => {
            let keys = Object.keys(fields);
            for(var key in keys) {
                let path = keys[key].split('~');
                if(fields[keys[key]]) {
                    _addRealValueHelper(hydratedActivity, path, fields[keys[key]])
                }
            }
            resolve(hydratedActivity);
        }).catch(error => {
            throw(error);
        });        
    });
}

function _addRealValueHelper(fieldParam, path, values) {
    let pathName = path.shift();
    if (fieldParam.value && Array.isArray(fieldParam.value)) {
        for(var field in fieldParam.value){
            if (path.length > 0) {
                _addRealValueHelper(fieldParam.value[field][pathName], path, values)
            } else {        
                let valueId = fieldParam.value[field][pathName];
                let valueObj = values.filter(c => c.id === valueId.value);
                if (valueObj[0]) {
                    if (valueObj[0][AC.ANCESTOR_VALUES]) {
                        valueId.value = '';
                        for(var id in valueObj[0][AC.ANCESTOR_VALUES]) {
                            valueId.value += '[' + valueObj[0][AC.ANCESTOR_VALUES][id] + ']';
                        }
                    } else {
                        valueId.value = valueObj[0].value;
                    }                    
                }
            }
        }
    } else {
        if (path.length > 0) {
            _addRealValueHelper(fieldParam[pathName], path, values)
        } else {        
            let valueId = fieldParam[pathName];
            let valueObj = values.filter(c => c.id === valueId.value);
            fieldParam[pathName].value = valueObj[0] ? valueObj[0].value : valueId.value;
        }
    }
}


function _createRequestDataHelper(requestData, hydratedActivity, parentName) {
    let keys = Object.keys(hydratedActivity);
    for(var key in keys) {
        let fieldObj = hydratedActivity[keys[key]];
        if(fieldObj.field_type === 'long') {
            if(fieldObj.value !== undefined && fieldObj.value !== null) {
                let _parentName = parentName ? parentName + '~' + keys[key] : keys[key];
                if (requestData[_parentName]) {
                    requestData[_parentName].push(fieldObj.value);
                } else {
                    requestData[_parentName] = [fieldObj.value];
                }
            }
        } else if (fieldObj.field_type === 'list' && fieldObj.value && fieldObj.value.length > 0) {
            let _parentName = parentName ? parentName + '~' + keys[key] : keys[key];
            for(var pos in fieldObj.value) {
                _createRequestDataHelper(requestData, fieldObj.value[pos], _parentName);
            }
        }
    }
}

export function getActivity(activityId){
    return function(dispatch) {
        dispatch(getActivityLoading());
        return commonListsApi.getActivity(activityId).then(response => {
            dispatch(getActivitySuccess(response));
        }).catch(error => {
            throw(error);
        });
    }
}

export function getFields(){
    return function(dispatch) {
        return commonListsApi.getFields().then(response => {
            dispatch(getFieldsSuccess(response));
        }).catch(error => {
            throw(error);
        });
    }
}

export function getFieldSubList(parentName, childrenName){
    return function(dispatch) {
        return commonListsApi.getFieldSubList(parentName, childrenName).then(response => {
            dispatch(getFieldSubListSuccess());
        }).catch(error => {
            throw(error);
        });
    }
}
