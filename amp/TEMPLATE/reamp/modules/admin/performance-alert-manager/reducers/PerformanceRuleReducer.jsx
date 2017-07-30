import {
  LOAD_PERFORMANCE_RULE_LIST_SUCCESS  
} from '../actions/PerformanceRuleActions';

const defaultState = {
        data: {
            performanceRuleList:[],
            errors: [],
            infoMessages: [],
            cid: 1,
            paging:  {
                recordsPerPage: 5,
                currentPageNumber: 1,
                totalPageCount : 1,
                totalRecords:0
             },  
             currentRule: null
           }
};

export default function performanceRuleReducer(state: Object = defaultState.data, action: Object) {    
    switch (action.type) {
    case LOAD_PERFORMANCE_RULE_LIST_SUCCESS:
        var newState = Object.assign({}, action.data);        
        newState.cid = state.cid;               
        return newState;
    
    default:            
        return state;
    }
}