import performanceRuleApi from '../api/PerformanceRuleApi.jsx';
import Utils from '../common/Utils.jsx';
export const LOAD_PERFORMANCE_RULE_LIST_SUCCESS = 'LOAD_PERFORMANCE_RULE_LIST_SUCCESS';
export const LOAD_TYPE_LIST_SUCCESS = 'LOAD_TYPE_LIST_SUCCESS';
export const LOAD_LEVEL_LIST_SUCCESS = 'LOAD_LEVEL_LIST_SUCCESS';
export const LOAD_ATTRIBUTE_LIST_SUCCESS = 'LOAD_ATTRIBUTE_LIST_SUCCESS';
export const ADD_PERFORMANCE_RULE = 'ADD_PERFORMANCE_RULE';
export const CLOSE_PERFORMANCE_RULE = 'CLOSE_PERFORMANCE_RULE';
export const EDIT_PERFORMANCE_RULE = 'EDIT_PERFORMANCE_RULE';
export const SAVE_PERFORMANCE_RULE_SUCCESS = 'SAVE_PERFORMANCE_RULE_SUCCESS';
export const DELETE_PERFORMANCE_RULE_SUCCESS = 'DELETE_PERFORMANCE_RULE_SUCCESS';

export function loadPerformanceRuleList(data) {
    return function(dispatch) {
        return performanceRuleApi.getPerformanceRuleList(data).then(response => {
            
            var results = {
                    performanceRuleList: [],                    
                    errors: [],
                    infoMessages: []                    
            };
            
            results.paging = data.paging;                      
            results.performanceRuleList = response.data;
            results.paging.totalRecords = response.totalRecords;
            results.paging.totalPageCount = Math.ceil(results.paging.totalRecords / results.paging.recordsPerPage);           
            dispatch({type: LOAD_PERFORMANCE_RULE_LIST_SUCCESS, data: results });
        }).catch(error => {
            throw(error);
        });
    };
}

export function getTypeList() {
    return function(dispatch) {
        return performanceRuleApi.getTypeList().then(response => {           
            dispatch({type: LOAD_TYPE_LIST_SUCCESS, data: response });
        }).catch(error => {
            throw(error);
        });
    };
}

export function getLevelList() {
    return function(dispatch) {
        return performanceRuleApi.getLevelList().then(response => {           
            dispatch({type: LOAD_LEVEL_LIST_SUCCESS, data: response });
        }).catch(error => {
            throw(error);
        });
    };
}

export function getAttributeList(ruleType) {    
    return function(dispatch) {
        return performanceRuleApi.getAttributeList(ruleType).then(response => {           
            dispatch({type: LOAD_ATTRIBUTE_LIST_SUCCESS, data: response });
        }).catch(error => {
            throw(error);
        });
    };
}

export function addNewPerformanceRule(){
    return function(dispatch) {
        dispatch({type: ADD_PERFORMANCE_RULE, data: {} });
    };    
}

export function closePerformanceRule(){
    return function(dispatch) {
        dispatch({type: CLOSE_PERFORMANCE_RULE, data: null });
    };    
}

export function editPerformanceRule(performanceRule){
    return function(dispatch) {
        dispatch({type: EDIT_PERFORMANCE_RULE, data: performanceRule });
    }; 
}

export function savePerformanceRule(data){
    return function(dispatch) {
        return performanceRuleApi.save(data).then(response => { 
            let result = {};
            if(response.status == 400) {                
                result.errors = [{messageKey: 'amp.performance-rule:save-error'}];
            } else {
                result.infoMessages = [{messageKey: 'amp.performance-rule:save-successful'}];
            }            
            dispatch({type: SAVE_PERFORMANCE_RULE_SUCCESS, data:result });
        }).catch(error => {
            console.log(error);
            throw(error);
        });
    };
}

export function deletePerformanceRule(data) {
    return function(dispatch) {
        return performanceRuleApi.deletePerformanceRule(data).then(response => {            
            dispatch({type: DELETE_PERFORMANCE_RULE_SUCCESS, data: response });
        }).catch(error => {
            console.log(error);
            throw(error);
        });
    };
}


    
