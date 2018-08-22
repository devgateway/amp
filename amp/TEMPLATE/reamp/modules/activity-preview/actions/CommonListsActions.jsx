import commonListsApi from '../api/CommonListsApi.jsx';


/**
 * @author Daniel Oliva
 */

export function getActivityLoading(){
    return {type: 'LOADING_ACTIVITY'}
}

export function getActivitySuccess(activity){
    return {type: 'LOAD_ACTIVITY_SUCCESS', activity: activity}
}

export function getFieldsSuccess(fields){
    return {type: 'LOAD_FIELDS_SUCCESS', fields: fields}
}

export function getFieldSubListSuccess(fieldSublist){
    return {type: 'LOAD_FIELD_SUBLIST_SUCCESS', fieldSublist: fieldSublist}
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
            dispatch(getFieldSubListSuccess(response));
        }).catch(error => {
            throw(error);
        });
    }
}
