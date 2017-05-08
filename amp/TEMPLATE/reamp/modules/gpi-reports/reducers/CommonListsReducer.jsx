const defaultState = {orgList:[]};

export default function commonListsReducer(state: Object = defaultState, action: Object) { 
    switch (action.type) {               
    case 'LOAD_ORG_LIST_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.orgList = action.orgList
        return  newState;           
    default:            
        return state;
    }
}