import aidOnBudgetApi from '../api/AidOnBudgetApi.jsx';
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
                    aidOnBudgetList: response.data,                    
                    errors: [],
                    infoMessages: []                    
            };
            
            results.paging = data.paging;
            results.paging.totalRecords = response.totalRecords;
            results.paging.totalPageCount = Math.ceil(results.paging.totalRecords / results.paging.recordsPerPage);
            results.sorting = data.sorting;            
            dispatch(getAidOnBudgetListSuccess(results));
        }).catch(error => {
            throw(error);
        });
    };
}

export function save(data){    
    return function(dispatch) {
        const errors = validate(data);
        if(errors.length > 0 ){
            const result = {};
            result.aidOnBudget = data;
            result.errors = errors;
            result.infoMessages = [];
            return dispatch(onSave(result));            
        } else {
            return aidOnBudgetApi.save(data).then(response => {
                const result = {errors: []};
                result.aidOnBudget = response.data;
                if (response.result === "SAVED") {                    
                    result.aidOnBudget.isEditing = false;
                    result.infoMessages = [{messageKey: 'amp.gpi-data-aid-on-budget:save-successful'}];
                } else if (response.result === "SAVE_FAILED"){
                    result.aidOnBudget.isEditing = true;
                    if (response.errors) {                       
                        response.errors.forEach(function(error){
                            for (var key in error) {                    
                                let messageKey = 'amp.gpi-data-aid-on-budget:server-errors-' + key;
                                result.errors.push({messageKey: messageKey, id: result.aidOnBudget.id, cid: result.aidOnBudget.cid});
                            }
                        }); 
                    }                    
                }
                
                dispatch(onSave(result));
            }).catch(error => {          
                throw(error);
            });            
        }      
    };
}

export function deleteAidOnBudget(data) {
    return function(dispatch) {
        if (data.id) {
            return aidOnBudgetApi.deleteAidOnBudget(data).then(response => {
                const result = {
                        aidOnBudget: data,
                        infoMessages: [{messageKey: 'amp.gpi-data-aid-on-budget:delete-successful'}]
                };
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

export function saveAllEdits(aidOnBudgetList) {
    return function(dispatch) {
        var allErrors = [];
        for (var aidOnBudget of aidOnBudgetList) {
            const errors = validate(aidOnBudget);
            allErrors = [...allErrors, ...errors];
        }
        
        if(allErrors.length > 0 ){
            const result = {};
            result.aidOnBudgetList = aidOnBudgetList;
            result.errors = allErrors;
            result.infoMessages = [];
            return dispatch(onSaveAllEdits(result));            
        } else {
            return aidOnBudgetApi.save(aidOnBudgetList).then(response => {
                const result = {errors:[], infoMessages: []};
                const list = [];
                for(var item of response){                    
                    if (item.result === "SAVED"){
                        item.data.isEditing = false;
                        list.push(item.data);                       
                    } else {                        
                        allErrors = [...allErrors, ...extractErrors(item, item.data)]
                    }                    
                }               
                
                result.aidOnBudgetList = list;
                result.errors = allErrors;
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
        }      
    };
    
}

function validate(aidOnBudget){
    const errors = [];
    if (!isNumber(aidOnBudget['amount']) || isUndefinedOrBlank(aidOnBudget, 'amount')){
        errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-amount-invalid', id: aidOnBudget.id, cid: aidOnBudget.cid});            
    } 
    
    if(isUndefinedOrBlank(aidOnBudget, 'donorId')){
        errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-donor-agency-required', id: aidOnBudget.id, cid: aidOnBudget.cid});
    }
    
    if(isUndefinedOrBlank(aidOnBudget, 'currencyCode')){
        errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-currency-required', id: aidOnBudget.id, cid: aidOnBudget.cid});
    }
    
    if(isUndefinedOrBlank(aidOnBudget, 'indicatorDate')){
        errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-date-required', id: aidOnBudget.id, cid: aidOnBudget.cid}); 
    }
    
    return errors
}

function extractErrors(response, aidOnBudget) {
    var errorMessages = [];
    if (response.errors) {  
        response.errors.forEach(function(error){
            for (var key in error) {                    
                let messageKey = 'amp.gpi-data-aid-on-budget:server-errors-' + key;
                errorMessages.push({messageKey: messageKey, id: aidOnBudget.id, cid: aidOnBudget.cid});
            }
        }); 
    }  
   
    return errorMessages;
}

function isNumber(input) {
    return typeof(input) != "boolean" && !isNaN(input);
}

function isUndefinedOrBlank(aidOnBudget, field) {
    var result = false;
    if(aidOnBudget[field] === '' || !aidOnBudget[field]){
        result = true;
    }
    
    return result;        
}
