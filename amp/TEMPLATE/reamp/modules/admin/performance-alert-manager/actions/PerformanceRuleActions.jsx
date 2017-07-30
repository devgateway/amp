import performanceRuleApi from '../api/PerformanceRuleApi.jsx';
import Utils from '../common/Utils.jsx';
export const LOAD_PERFORMANCE_RULE_LIST_SUCCESS = 'LOAD_PERFORMANCE_RULE_LIST_SUCCESS';

export function getPerformanceRuleListSuccess(data){
    return {type: LOAD_PERFORMANCE_RULE_LIST_SUCCESS, data: data }
}

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
            dispatch(getPerformanceRuleListSuccess(results));
        }).catch(error => {
            throw(error);
        });
    };
}

    
