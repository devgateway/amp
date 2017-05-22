const defaultState = {orgList: [], years: [], reportVisibility: {}};

export default function commonListsReducer(state: Object = defaultState, action: Object) { 
    switch (action.type) {               
    case 'LOAD_ORG_LIST_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.orgList = action.orgList
        return  newState; 
    case 'FETCH_YEARS_SUCCESS':
        var newState = Object.assign({}, state); 
        newState.years = action.years;        
        return  newState; 
    case 'FETCH_REPORT_VISIBILITY_CONF_SUCCESS':
        var newState = Object.assign({}, state); 
        newState.reportVisibility = action.reportVisibility;        
        return  newState; 
    default:            
        return state;
    }
}