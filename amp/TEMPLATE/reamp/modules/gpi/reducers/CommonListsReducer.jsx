const defaultState = {currencyList:[], orgList:[], settings:{}, verifiedOrgList: []};

export default function commonListsReducer(state: Object = defaultState, action: Object) { 
    switch (action.type) {
    case 'LOAD_CURRENCY_LIST_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.currencyList = action.currencyList
        return  newState;           
    case 'LOAD_ORG_LIST_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.orgList = action.orgList
        return  newState;
    case 'LOAD_VERIFIED_ORG_LIST_SUCCESS':
        var newState = Object.assign({}, state); 
        newState.verifiedOrgList = action.verifiedOrgList
        return  newState;
    case 'LOAD_SETTINGS_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.settings = action.settings
        return  newState;  
    default:            
        return state;
    }
}