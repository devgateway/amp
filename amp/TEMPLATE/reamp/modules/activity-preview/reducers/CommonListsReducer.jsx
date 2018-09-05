/**
 * @author Daniel Oliva
 */

const defaultState = {
    isActivityLoading: false,
    isActivityError: false,
    isActivityLoaded: false,
    isActivityHydratedLoading: false,
    isActivityHydrated: false,
    activity: undefined, 
    hydratedActivity: undefined, 
    fields: undefined,
    errorMsg: undefined
};


export default function commonListsReducer(state: Object = defaultState, action: Object) { 
    switch (action.type) {
    case 'LOADING_ACTIVITY':
        var newState = Object.assign({}, state); 
        newState.isActivityLoading = true;  
        newState.isActivityError = false;        
        return  newState;
    case 'LOAD_ACTIVITY_ERROR':
        var newState = Object.assign({}, state); 
        newState.isActivityLoading = false;
        newState.isActivityError = true;  
        newState.errorMsg = action.errorMsg;      
        return  newState;
    case 'LOADING_HYDRATED_ACTIVITY':
        var newState = Object.assign({}, state); 
        newState.isActivityHydratedLoading = true;  
        newState.hydratedActivity = action.hydratedActivity;
        return  newState;
    case 'LOAD_ACTIVITY_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.activity = action.activity;
        newState.isActivityLoading = false;
        newState.isActivityLoaded = true;
        return  newState; 
    case 'LOAD_HYDRATED_ACTIVITY_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.hydratedActivity = action.hydratedActivity;
        newState.isActivityHydratedLoading = false;  
        newState.isActivityHydrated = true;
        return  newState; 
    case 'LOAD_FIELDS_SUCCESS':
        var newState = Object.assign({}, state); 
        newState.fields = action.fields;        
        return  newState; 
    case 'LOAD_FIELD_SUBLIST_SUCCESS':
        var newState = Object.assign({}, state); 
        return  newState; 
    default:            
        return state;
    }
}