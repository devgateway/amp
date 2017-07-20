const defaultState = {settings:{}};
export default function commonListsReducer(state: Object = defaultState, action: Object) { 
    switch (action.type) {
    case 'LOAD_SETTINGS_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.settings = action.settings
        return  newState;        
    default:            
        return state;
    }
}