import {
  LOAD_PERFORMANCE_RULE_LIST_SUCCESS,
  LOAD_TYPE_LIST_SUCCESS,
  LOAD_LEVEL_LIST_SUCCESS,
  LOAD_ATTRIBUTE_LIST_SUCCESS,
  ADD_PERFORMANCE_RULE,
  CLOSE_PERFORMANCE_RULE,
  EDIT_PERFORMANCE_RULE,
  SAVE_PERFORMANCE_RULE_SUCCESS,
  DELETE_PERFORMANCE_RULE_SUCCESS,
  VALIDATE_PERFORMANCE_RULE,
  CLEAR_MESSAGES,
  UPDATE_PERFORMANCE_RULE
} from '../actions/PerformanceRuleActions';

const defaultState = {
        data: {
            performanceRuleList:[],
            errors: [],
            infoMessages: [],
            cid: 1,
            paging:  {
                recordsPerPage: 10,
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
    const newState = Object.assign({}, state);    
    switch (action.type) {
    case LOAD_PERFORMANCE_RULE_LIST_SUCCESS:        
        newState.paging = action.data.paging;
        newState.performanceRuleList = action.data.performanceRuleList;                       
        return newState;
    case LOAD_TYPE_LIST_SUCCESS:
        newState.typeList = action.data;                              
        return newState;
    case LOAD_LEVEL_LIST_SUCCESS:        
        newState.levelList = action.data;                              
        return newState;
    case ADD_PERFORMANCE_RULE:        
        newState.currentPerformanceRule = action.data;                              
        return newState;
    case CLOSE_PERFORMANCE_RULE:        
        newState.currentPerformanceRule = action.data;                              
        return newState;
    case EDIT_PERFORMANCE_RULE:        
        newState.currentPerformanceRule = Object.assign({}, action.data);                              
        return newState;
    case LOAD_ATTRIBUTE_LIST_SUCCESS:        
        newState.attributeList = action.data;                              
        return newState;
    case SAVE_PERFORMANCE_RULE_SUCCESS:        
        newState.errors = action.data.errors; 
        newState.infoMessages = action.data.infoMessages;  
        if(action.data.infoMessages.length > 0){
            newState.currentPerformanceRule = null
        }
        return newState;  
    case DELETE_PERFORMANCE_RULE_SUCCESS:
    case CLEAR_MESSAGES: 
    case VALIDATE_PERFORMANCE_RULE:        
        newState.errors = action.data.errors; 
        newState.infoMessages = action.data.infoMessages;    
        return newState; 
    case UPDATE_PERFORMANCE_RULE:
        newState.errors = []; 
        newState.infoMessages = [];  
        newState.currentPerformanceRule = Object.assign({}, action.data);        
        return newState;  
    default:            
        return state;
    }
}