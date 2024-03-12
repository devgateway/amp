const defaultState = {settings:{}, user:{}};
export default function commonListsReducer(state: Object = defaultState, action: Object) { 
    switch (action.type) {
    case 'LOAD_SETTINGS_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.settings = action.settings
        return  newState;   
    case 'LOAD_USER_INFO_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.user = action.user
        return  newState;  
    default:            
        return state;
    }
}