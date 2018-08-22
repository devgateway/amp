/**
 * @author Daniel Oliva
 */

const defaultState = {
    isActivityLoading: false,
    isActivityLoaded: false,
    isActivityHydrated: false,
    activity: undefined, 
    fields: undefined, 
    fieldSublist: undefined
};


export default function commonListsReducer(state: Object = defaultState, action: Object) { 
    switch (action.type) {
    case 'LOADING_ACTIVITY':
        var newState = Object.assign({}, state); 
        newState.isActivityLoading = true;        
        return  newState;              
    case 'LOAD_ACTIVITY_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.activity = action.activity;
        newState.isActivityLoading = false;
        newState.isActivityLoaded = true;
        return  newState; 
    case 'LOAD_FIELDS_SUCCESS':
        var newState = Object.assign({}, state); 
        newState.fields = action.fields;        
        return  newState; 
    case 'LOAD_FIELD_SUBLIST_SUCCESS':
        var newState = Object.assign({}, state); 
        newState.fieldSublist = action.fieldSublist;        
        return  newState; 
    default:            
        return state;
    }
}