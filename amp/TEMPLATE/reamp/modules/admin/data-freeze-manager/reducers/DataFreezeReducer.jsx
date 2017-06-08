const defaultState = {
        data: {
            dataFreezeEventsList:[],
            errors: [],
            infoMessages: [],
            cid: 1,
            paging:  {
                recordsPerPage: 10,
                offset: 0,
                currentPageNumber: 1,
                totalPageCount : 1
             },
             sorting: {
                 orderBy: 'id',
                 sortOrder: 'desc'
             }             
           }
};

export default function dataFreezeReducer(state: Object = defaultState.data, action: Object) {    
    switch (action.type) {
    case 'LOAD_DATA_FREEZE_EVENTS_LIST_SUCCESS':
        var newState = Object.assign({}, action.data);        
        newState.cid = state.cid;               
        return newState;
    case 'DATA_FREEZE_EVENT_ON_SAVE':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        if (actionData.errors && actionData.errors.length > 0) {
            newState.dataFreezeEventsList = [...newState.dataFreezeEventsList]
        } else {
            newState.dataFreezeEventsList = newState.dataFreezeEventsList.map(function(dataFreezeEvent) { return ((dataFreezeEvent.id && dataFreezeEvent.id === actionData.dataFreezeEvent.id) || (dataFreezeEvent.cid && dataFreezeEvent.cid === actionData.dataFreezeEvent.cid)) ? Object.assign({}, actionData.dataFreezeEvent) : dataFreezeEvent; });
        }   
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || [];         
        return newState;    
    case 'DATA_FREEZE_EVENT_DELETE_SUCCESS':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        newState.dataFreezeEventsList = [...newState.dataFreezeEventsList.filter(dataFreezeEvent => dataFreezeEvent.id !== actionData.dataFreezeEvent.id || dataFreezeEvent.cid !== actionData.dataFreezeEvent.cid)]
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || []; 
        return newState;
    case 'ADD_DATA_FREEZE_EVENT':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        actionData.dataFreezeEvent.cid = state.cid;
        newState.dataFreezeEventsList = [Object.assign({}, actionData.dataFreezeEvent), ...newState.dataFreezeEventsList];
        newState.errors = [];
        newState.infoMessages = [];
        newState.cid = ++newState.cid;        
        return newState; 
    case 'UPDATE_DATA_FREEZE_EVENT':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);        
        newState.dataFreezeEventsList =  newState.dataFreezeEventsList.map(function(dataFreezeEvent) { 
            return ((dataFreezeEvent.id && dataFreezeEvent.id === actionData.dataFreezeEvent.id) || (dataFreezeEvent.cid && dataFreezeEvent.cid === actionData.dataFreezeEvent.cid)) ? Object.assign({}, actionData.dataFreezeEvent) : dataFreezeEvent;
            });        
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || [];         
        return newState;   
    default:            
        return state;
    }
}