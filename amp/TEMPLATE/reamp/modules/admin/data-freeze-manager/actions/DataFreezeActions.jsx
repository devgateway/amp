import DataFreezeApi from '../api/DataFreezeApi';
import Utils from '../common/Utils';
import * as Constants from '../common/Constants';
export function getDataFreezeEventsListSuccess(data){
    return {type: 'LOAD_DATA_FREEZE_EVENTS_LIST_SUCCESS', data: data }
}

export function onSave(data){
    return {type: 'DATA_FREEZE_EVENT_ON_SAVE', data: data } 
}

export function loadDataFreezeEventsList(data) {
    return function(dispatch) {
        return DataFreezeApi.getDataFreezeEventsList(data).then(response => {
            
            var results = {
                    dataFreezeEventsList: [],                    
                    errors: [],
                    infoMessages: []                    
            };
            
            results.paging = data.paging;
            results.sorting = data.sorting;             
            if (response.error) {
                results.errors = Utils.extractErrors(response.error);                
            } else {
                results.dataFreezeEventsList = response.data;
                results.paging.totalRecords = response.totalRecords;
                results.paging.totalPageCount = Math.ceil(results.paging.totalRecords / results.paging.recordsPerPage);                
            }  
            
            dispatch(getDataFreezeEventsListSuccess(results));
        }).catch(error => {
            throw(error);
        });
    };
}

export function save(data) {
    return function(dispatch) {
        return DataFreezeApi.save(data).then(response => {
            const result = {errors: []};
            result.dataFreezeEvent = response.data || data;
            if (response.result === Constants.SAVE_SUCCESSFUL) { 
                result.dataFreezeEvent.isEditing = false;
                result.infoMessages = [{messageKey: 'amp.data-freeze-event:save-successful'}];
            } 
            
            if (response.errors || response.error) {
                result.dataFreezeEvent.isEditing = true;
                if(response.errors){
                    result.errors = [...Utils.extractErrors(response.errors , result.dataFreezeEvent)] 
                }
                
                if(response.error){
                    result.errors = [...Utils.extractErrors(response.error , result.dataFreezeEvent)]
                }                
            }
            dispatch(onSave(result));
        })
    }
}


export function deleteDataFreezeEvent(data) {
    return function(dispatch) {
        if (data.id) {            
            return dataFreezeEventApi.deleteDataFreezeEvent(data).then(response => {
                const result = {infoMessages: [], errors: []};
                result.dataFreezeEvent = data;
                if(response.error) {
                    result.errors = [...Utils.extractErrors(response.error , result.dataFreezeEvent)]
                } else{
                    result.infoMessages = [{messageKey: 'amp.data-freeze-event:delete-successful'}]; 
                }
                
                dispatch(deleteSuccess(result));
            }).catch(error => {
                throw(error);
            });   
        } else {
            const result = {
                    dataFreezeEvent: data,
                    infoMessages: [{messageKey: 'amp.data-freeze-event:delete-successful'}]
            };
            dispatch(deleteSuccess(result));
        }        
    }; 
}
