/**
 *    
 */

const defaultState = {
    isSettingsLoading: false,
    isSettingsLoaded: false,
    isActivityInfoLoading: false,
    isActivityInfoLoaded: false,
    isActivityLoading: false,
    isActivityError: false,
    isActivityLoaded: false,
    isActivityHydratedLoading: false,
    isActivityHydrated: false,    
    isFundingInfoLoading: false,
    isFundingInfoLoaded: false,
    settings: undefined, 
    activity: undefined, 
    activityInfo: undefined, 
    fundingInfo: undefined,
    hydratedActivity: undefined, 
    fields: undefined,
    errorMsg: undefined
};


export default function commonListsReducer(state: Object = defaultState, action: Object) { 
    switch (action.type) {
        case 'LOADING_SETTINGS':
            var newState = Object.assign({}, state); 
            newState.isSettingsLoading = true;  
            newState.isSettingsLoaded = false;        
            return  newState;
        case 'LOAD_SETTINGS_SUCCESS': 
            var newState = Object.assign({}, state); 
            newState.settings = action.settings;
            newState.isSettingsLoading = false;
            newState.isSettingsLoaded = true;
            return  newState; 
        case 'LOADING_ACTIVITY':
            var newState = Object.assign({}, state); 
            newState.isActivityLoading = true;  
            newState.isActivityError = false;        
            return  newState;
        case 'LOADING_ACTIVITY_INFO':
            var newState = Object.assign({}, state); 
            newState.isActivityInfoLoading = true;  
            return  newState;
        case 'LOAD_ACTIVITY_INFO_SUCCESS': 
            var newState = Object.assign({}, state); 
            newState.activityInfo = action.activityInfo;
            newState.isActivityInfoLoading = false;
            newState.isActivityInfoLoaded = true;
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
        case 'LOADING_FUNDING_INFO':
            var newState = Object.assign({}, state); 
            newState.isFundingInfoLoading = true;  
            newState.isFundingInfoLoaded = false;        
            return  newState;
        case 'LOAD_FUNDING_INFO_SUCCESS': 
            var newState = Object.assign({}, state); 
            newState.fundingInfo = action.fundingInfo;
            newState.isFundingInfoLoading = false;
            newState.isFundingInfoLoaded = true;
            return  newState; 
        default:            
            return state;
    }
}