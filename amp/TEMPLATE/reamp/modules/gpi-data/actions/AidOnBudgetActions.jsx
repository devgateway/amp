import aidOnBudgetApi from '../api/AidOnBudgetApi.jsx';
import Utils from '../common/utils.jsx';

export function getAidOnBudgetListSuccess(data){
    return {type: 'LOAD_AID_ON_BUDGET_LIST_SUCCESS', data: data }
}

export function onSave(data){
    return {type: 'AID_ON_BUDGET_ON_SAVE', data: data } 
}

export function onSaveAllEdits(data){
    return {type: 'AID_ON_BUDGET_ON_SAVE_ALL_EDITS', data: data } 
}

export function deleteSuccess(data){
    return {type: 'AID_ON_BUDGET_DELETE_SUCCESS', data: data } 
}

export function addNewAidOnBudget() {
    return {type: 'ADD_AID_ON_BUDGET', data: {aidOnBudget: {isEditing: true}} } 
}

export function updateAidOnBudget(aidOnBudget) {
    return {type: 'UPDATE_AID_ON_BUDGET', data: {aidOnBudget: aidOnBudget, errors: [], infoMessages:[]} } 
}

export function loadAidOnBudgetList(data) {
    return function(dispatch) {
        return aidOnBudgetApi.getAidOnBudgetList(data).then(response => {
            
            var results = {
                    aidOnBudgetList: [],                    
                    errors: [],
                    infoMessages: []                    
            };
            
            results.paging = data.paging;
            results.sorting = data.sorting;             
            if (response.error) {
                results.errors = Utils.extractErrors(response.error);                
            } else {
                results.aidOnBudgetList = response.data;
                results.paging.totalRecords = response.totalRecords;
                results.paging.totalPageCount = Math.ceil(results.paging.totalRecords / results.paging.recordsPerPage);                
            }  
            
            dispatch(getAidOnBudgetListSuccess(results));
        }).catch(error => {
            throw(error);
        });
    };
}

export function save(data){    
    return function(dispatch) {
        const errors = Utils.validateAidOnBudget(data);
        if(errors.length > 0 ){
            const result = {};
            result.aidOnBudget = data;
            result.errors = errors;
            result.infoMessages = [];
            return dispatch(onSave(result));            
        } 

        let cid = data.cid;
        return aidOnBudgetApi.save(data).then(response => {
            const result = {errors: []};
            result.aidOnBudget = response.data || data;
            if (cid) {
                result.aidOnBudget.cid = cid;
            }
            if (response.result === "SAVED") {                    
                result.aidOnBudget.isEditing = false;
                result.infoMessages = [{messageKey: 'amp.gpi-data-aid-on-budget:save-successful'}];
            } 
            
            if (response.errors || response.error) {
                result.aidOnBudget.isEditing = true;
                if(response.errors){
                    result.errors = [...Utils.extractErrors(response.errors , result.aidOnBudget)] 
                }
                
                if(response.error){
                    result.errors = [...Utils.extractErrors(response.error , result.aidOnBudget)]
                }                
            }
            
            dispatch(onSave(result));
        }).catch(error => {          
            throw(error);
        });            
        
    };
}

export function deleteAidOnBudget(data) {
    return function(dispatch) {
        if (data.id) {            
            return aidOnBudgetApi.deleteAidOnBudget(data).then(response => {
                const result = {infoMessages: [], errors: []};
                result.aidOnBudget = data;
                if(response.error){
                    result.errors = [...Utils.extractErrors(response.error , result.aidOnBudget)]
                } else{
                    result.infoMessages = [{messageKey: 'amp.gpi-data-aid-on-budget:delete-successful'}]; 
                }
                
                dispatch(deleteSuccess(result));
            }).catch(error => {
                throw(error);
            });   
        } else {
            const result = {
                    aidOnBudget: data,
                    infoMessages: [{messageKey: 'amp.gpi-data-aid-on-budget:delete-successful'}]
            };
            dispatch(deleteSuccess(result));
        }        
    }; 
}

export function removeFromState(data) {
    return function(dispatch) {
        const result = {
                aidOnBudget: data,
                infoMessages: []
        };
        dispatch(deleteSuccess(result));  
    }    
}

export function saveAllEdits(aidOnBudgetList) {
    return function(dispatch) {
        var allErrors = [];
        for (var aidOnBudget of aidOnBudgetList) {
            const errors = Utils.validateAidOnBudget(aidOnBudget);
            allErrors = [...allErrors, ...errors];
        }
        
        if(allErrors.length > 0 ){
            const result = {};
            result.aidOnBudgetList = aidOnBudgetList;
            result.errors = allErrors;
            result.infoMessages = [];
            return dispatch(onSaveAllEdits(result));            
        }

        let cids = Utils.getCids(aidOnBudgetList);
        return aidOnBudgetApi.save(aidOnBudgetList).then(response => {
            const result = {errors:[], infoMessages: []};
            
            if (response.error) {
                results.errors = Utils.extractErrors(response.error); 
                return dispatch(onSaveAllEdits(result));
            }
            
            const list = [];
            for (var item of response) {                    
                if (item.result === "SAVED"){
                    item.data.isEditing = false;
                    list.push(item.data);                       
                } else {                        
                    allErrors = [...allErrors, ...Utils.extractErrors(item.errors, item.data)]
                }                    
            }               
            
            result.aidOnBudgetList = Utils.restoreCids(list, cids);
            result.errors = [...result.errors, ...allErrors];
            if (list.length > 0) {
                var params = {};
                params.saved = list.length;
                params.total = aidOnBudgetList.length;
                result.infoMessages = [{messageKey: 'amp.gpi-data-aid-on-budget:save-all', params: params}];  
            }                
            
            dispatch(onSaveAllEdits(result));
        }).catch(error => {          
            throw(error);
        });            
    };
    
}