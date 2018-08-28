import commonListsApi from '../api/CommonListsApi';


/**
 * @author Daniel Oliva
 */

export function getActivityLoading(){
    return {type: 'LOADING_ACTIVITY'}
}

export function getActivitySuccess(activity){
    return {type: 'LOAD_ACTIVITY_SUCCESS', activity: activity}
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

export function getActivityAndFields(activityId){
    return function(dispatch) {
        dispatch(getHydratedActivityLoading());
        let hydratedActivity = {};        
        return commonListsApi.getActivity(activityId).then(activity => {
            dispatch(getActivitySuccess(activity));
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

        }).catch(error => {
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
            hydratedField['field_label'] = fieldObj.field_label;
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

function _addRealValue(hydratedActivity, parentName) {
    return new Promise((resolve, reject) => {
        let actions = [];
        _addRealValueHelper(actions, hydratedActivity, parentName)
        Promise.all(actions).then(data => resolve(hydratedActivity));
        
    });
}

function _addRealValueHelper(actions, hydratedActivity, parentName) {
    let keys = Object.keys(hydratedActivity);
    
    for(var key in keys) {
        let fieldObj = hydratedActivity[keys[key]];
        if(fieldObj.field_type === 'long') {
            let _parentName = parentName ? parentName : keys[key];
            let _childName = parentName ? keys[key] : undefined;
            actions.push(
                commonListsApi.getFieldSubList(_parentName, _childName).then(field => {
                    if (field.length > 0) {
                        let element = field.find(function(element){ if (element.id === fieldObj.value) {
                            return element
                        }});
                        let newValue = element ? element.value : undefined;
                        if (newValue !== undefined) {
                            fieldObj.value = newValue;
                        }
                    }
                }).catch(error => {
                    throw(error);
                })
            );
        } else if (fieldObj.field_type === 'list' && fieldObj.value.length > 0) {
            let _parentName = parentName ? parentName + '~' + keys[key] : keys[key];
            for(var pos in fieldObj.value) {
                _addRealValueHelper(actions, fieldObj.value[pos], _parentName);
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
