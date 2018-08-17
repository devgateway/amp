const defaultState = {activity: {}, fields: [], fieldSublist:[]};

export default function commonListsReducer(state: Object = defaultState, action: Object) { 
    switch (action.type) {               
    case 'LOAD_ACTIVITY_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState.activity = action.activity
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