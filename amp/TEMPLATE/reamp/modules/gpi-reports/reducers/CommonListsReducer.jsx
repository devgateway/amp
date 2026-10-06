const defaultState = {orgList: [], years: [], reportVisibility: {}, settings:{}, calendars:[]};

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
    case 'LOAD_SETTINGS_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.settings = action.settings
        return  newState;
    case 'LOAD_CALENDARS_SUCCESS':
        var newState = Object.assign({}, state); 
        newState.calendars = action.calendars
        return  newState;
    default:            
        return state;
    }
}