import {
  LOAD_PERFORMANCE_RULE_LIST_SUCCESS,
  LOAD_TYPE_LIST_SUCCESS,
  LOAD_LEVEL_LIST_SUCCESS,
  LOAD_ATTRIBUTE_LIST_SUCCESS,
  ADD_PERFORMANCE_RULE,
  CLOSE_PERFORMANCE_RULE,
  EDIT_PERFORMANCE_RULE,
  SAVE_PERFORMANCE_RULE_SUCCESS
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
             currentPerformanceRule: null,
             typeList:[],
             levelList:[],
             attributeList:[]
           }
};

export default function performanceRuleReducer(state: Object = defaultState.data, action: Object) {    
    switch (action.type) {
    case LOAD_PERFORMANCE_RULE_LIST_SUCCESS:
        var newState = Object.assign({}, state);
        newState.paging = action.data.paging;
        newState.performanceRuleList = action.data.performanceRuleList;                       
        return newState;
    case LOAD_TYPE_LIST_SUCCESS:
        var newState = Object.assign({}, state);
        newState.typeList = action.data;                              
        return newState;
    case LOAD_LEVEL_LIST_SUCCESS:
        var newState = Object.assign({}, state);
        newState.levelList = action.data;                              
        return newState;
    case ADD_PERFORMANCE_RULE:
        var newState = Object.assign({}, state);
        newState.currentPerformanceRule = action.data;                              
        return newState;
    case CLOSE_PERFORMANCE_RULE:
        var newState = Object.assign({}, state);
        newState.currentPerformanceRule = action.data;                              
        return newState;
    case EDIT_PERFORMANCE_RULE:
        var newState = Object.assign({}, state);
        newState.currentPerformanceRule = Object.assign({}, action.data);                              
        return newState;
    case LOAD_ATTRIBUTE_LIST_SUCCESS:
        var newState = Object.assign({}, state);
        newState.attributeList = action.data;                              
        return newState;
    case SAVE_PERFORMANCE_RULE_SUCCESS:
        var newState = Object.assign({}, state);
        newState.errors = action.data.errors; 
        newState.infoMessages = action.data.infoMessages;
        return newState;    
    default:            
        return state;
    }
}