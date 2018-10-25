import commonListsApi from '../api/CommonListsApi';
import * as AC from '../utils/ActivityConstants'


/**
 *    
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

export function getFundingInfoLoading(){
    return {type: 'LOADING_FUNDING_INFO'}
}

export function getFundingInfoSuccess(fundingInfo){
    return {type: 'LOAD_FUNDING_INFO_SUCCESS', fundingInfo: fundingInfo}
}

export function getSettingsAndActivity(activityId){
    return function(dispatch) {
        dispatch(getSettingsLoading());
        return commonListsApi.getSettings().then(settings => {
            dispatch(getSettingsSuccess(settings));
            dispatch(getFundingInfoLoading());
            const lang = settings[AC.LANGUAGE]
            commonListsApi.getFundingData(activityId, settings[AC.EFFECTIVE_CURRENCY].id
                ? settings[AC.EFFECTIVE_CURRENCY].id : 21).then(fundingInfo =>{
                dispatch(getFundingInfoSuccess(fundingInfo));
                dispatch(getHydratedActivityLoading());
                let hydratedActivity = {};
                commonListsApi.getActivity(activityId).then(activity => {
                    if (!activity.error) {
                        dispatch(getActivitySuccess(activity));
                        if(fundingInfo) {
                            activity[AC.FUNDINGS].forEach(funding => {
                                let details = fundingInfo[AC.FUNDING_INFORMATION][AC.FUNDINGS].find(f => f[AC.FUNDING_ID] === funding[AC.FUNDING_ID]);
                                funding[AC.FUNDING_DETAILS] = details[AC.FUNDING_DETAILS];
                                funding[AC.UNDISBURSED_BALANCE] = details[AC.UNDISBURSED_BALANCE];
                            });
                            activity[AC.FUNDING_TOTALS] = {};
                            activity[AC.FUNDING_TOTALS][AC.UNDISBURSED_BALANCE] = fundingInfo[AC.FUNDING_INFORMATION][AC.UNDISBURSED_BALANCE];
                            activity[AC.FUNDING_TOTALS][AC.TOTALS] = fundingInfo[AC.FUNDING_INFORMATION][AC.TOTALS];
                            activity[AC.FUNDING_TOTALS][AC.DELIVERY_RATE_PROP] = fundingInfo[AC.FUNDING_INFORMATION][AC.DELIVERY_RATE_PROP];
                            activity[AC.FUNDING_TOTALS][AC.PPC_AMOUNT] = fundingInfo[AC.PPC_AMOUNT];
                            activity[AC.FUNDING_TOTALS][AC.RPC_AMOUNT] = fundingInfo[AC.RPC_AMOUNT];
                            activity[AC.FUNDING_TOTALS][AC.CURRENCY] = fundingInfo[AC.CURRENCY];
                        }
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
                            _addRealValue(hydratedActivity, fields, lang).then(response => {
                                let docs = hydratedActivity[AC.ACTIVITY_DOCUMENTS].value;
                                if (docs && docs.length > 0) {
                                    let res = [];
                                    for(var i in docs) {
                                        res.push(docs[i][AC.DOC_UUID].value);
                                    }
                                    commonListsApi.fetchResources(res).then(resources =>{
                                        hydratedActivity[AC.ACTIVITY_DOCUMENTS].value = resources;
                                        dispatch(getHydratedActivitySuccess(hydratedActivity));
                                    }).catch(error => {
                                        throw(error);
                                    });
                                } else {
                                    dispatch(getHydratedActivitySuccess(hydratedActivity));
                                }
                                
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
            }).catch(error => {
                dispatch(getActivityError(error));
                throw(error);
            });            
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

function _addRealValue(hydratedActivity, fields, lang) {
    return new Promise((resolve, reject) => {
        let requestData = {};
        _createRequestDataHelper(requestData, hydratedActivity, fields);
        commonListsApi.fetchFieldsData(requestData).then(fields => {
            let keys = Object.keys(fields);
            for(var key in keys) {
                let path = keys[key].split('~');
                if(fields[keys[key]]) {
                    _addRealValueHelper(hydratedActivity, path, fields[keys[key]], lang)
                }
            }
            if(hydratedActivity[AC.FUNDING_TOTALS] && hydratedActivity[AC.FUNDING_TOTALS].value && hydratedActivity[AC.FUNDING_TOTALS].value[AC.TOTALS].length > 0) {
                hydratedActivity[AC.FUNDING_TOTALS].value[AC.TOTALS].forEach(t => {
                    t[AC.TRANSACTION_TYPE] = fields[AC.TRX_TYPE_PATH].find(x => x.id === t[AC.TRANSACTION_TYPE]).value;
                    t[AC.ADJUSTMENT_TYPE] = fields[AC.ADJ_TYPE_PATH].find(x => x.id === t[AC.ADJUSTMENT_TYPE]).value;
                });
            }
            resolve(hydratedActivity);
        }).catch(error => {
            throw(error);
        });        
    });
}

function _addRealValueHelper(fieldParam, path, values, lang) {
    let pathName = path.shift();
    if (fieldParam.value && Array.isArray(fieldParam.value)) {
        for(var field in fieldParam.value){
            if (path.length > 0) {
                var newPath = path.slice();
                _addRealValueHelper(fieldParam.value[field][pathName], newPath, values);                
            } else {        
                let valueId = fieldParam.value[field][pathName];
                let valueObj = values.find(c => c.id === valueId.value);
                if (valueObj) {
                    if (valueObj[AC.ANCESTOR_VALUES]) {
                        valueId.value = '';
                        for(var id in valueObj[AC.ANCESTOR_VALUES]) {
                            valueId.value += '[' + valueObj[AC.ANCESTOR_VALUES][id] + ']';
                        }
                    } else {
                        valueId.value = valueObj.value;
                        if (lang !== 'en' && valueObj && valueObj[AC.TRANSLATED_VALUE] && valueObj[AC.TRANSLATED_VALUE][lang]) {
                            valueId.value = valueObj[AC.TRANSLATED_VALUE][lang];
                        }
                    }                    
                }
            }
        }
    } else {
        if (path.length > 0) {
            _addRealValueHelper(fieldParam[pathName], path, values)
        } else {        
            let valueId = fieldParam[pathName];
            let valueObj = values.find(c => c.id === valueId.value);
            fieldParam[pathName].value = valueObj ? valueObj.value : valueId.value;
            if (lang !== 'en' && valueObj && valueObj[AC.TRANSLATED_VALUE] && valueObj[AC.TRANSLATED_VALUE][lang]) {
                fieldParam[pathName].value = valueObj[AC.TRANSLATED_VALUE][lang];
            }
        }
    }
}


function _createRequestDataHelper(requestData, hydratedActivity, fieldsInfo, parentName) {
    let keys = Object.keys(hydratedActivity);
    for(var key in keys) {
        let fieldObj = hydratedActivity[keys[key]];
        if(fieldObj.field_type === 'long') {
            if(fieldObj.value !== undefined && fieldObj.value !== null) {
                let newFieldsInfo = fieldsInfo.find(f => f[AC.FIELD_NAME] === keys[key]);
                if (newFieldsInfo[AC.ID_ONLY]) {
                    let _parentName = parentName ? parentName + '~' + keys[key] : keys[key];
                    if (requestData[_parentName]) {
                        requestData[_parentName].push(fieldObj.value);
                    } else {
                        requestData[_parentName] = [fieldObj.value];
                    }
                }
            }
        } else if (fieldObj.field_type === 'list' && fieldObj.value && fieldObj.value.length > 0) {
            let _parentName = parentName ? parentName + '~' + keys[key] : keys[key];
            for(var pos in fieldObj.value) {
                let newFieldsInfo = fieldsInfo.find(f => f[AC.FIELD_NAME] === keys[key]);
                _createRequestDataHelper(requestData, fieldObj.value[pos], newFieldsInfo[AC.CHILDREN], _parentName);
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
