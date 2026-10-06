const defaultState = {
        data: {
            aidOnBudgetList:[],
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
                 orderBy: 'indicatorDate',
                 sortOrder: 'desc'
             }             
           }
};

export default function aidOnBudgetReducer(state: Object = defaultState.data, action: Object) {    
    switch (action.type) {
    case 'LOAD_AID_ON_BUDGET_LIST_SUCCESS':
        var newState = Object.assign({}, action.data);        
        newState.cid = state.cid;               
        return newState;
    case 'AID_ON_BUDGET_ON_SAVE':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        if (actionData.errors && actionData.errors.length > 0) {
            newState.aidOnBudgetList = [...newState.aidOnBudgetList]
        } else {
            newState.aidOnBudgetList = newState.aidOnBudgetList.map(function(aidOnBudget) { return ((aidOnBudget.id && aidOnBudget.id === actionData.aidOnBudget.id) || (aidOnBudget.cid && aidOnBudget.cid === actionData.aidOnBudget.cid)) ? Object.assign({}, actionData.aidOnBudget) : aidOnBudget; });
        }   
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || [];         
        return newState;
    case 'AID_ON_BUDGET_ON_SAVE_ALL_EDITS':        
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        newState.aidOnBudgetList = newState.aidOnBudgetList.map(function(aidOnBudget) { 
            var found = actionData.aidOnBudgetList.find(obj =>{ obj
                return ((aidOnBudget.id && aidOnBudget.id === obj.id) || (aidOnBudget.cid && aidOnBudget.cid === obj.cid))     
            });            
            return found ? Object.assign({}, found) : aidOnBudget;            
        });        
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || [];        
        return newState;     
    case 'AID_ON_BUDGET_DELETE_SUCCESS':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        newState.aidOnBudgetList = [...newState.aidOnBudgetList.filter(aidOnBudget => aidOnBudget.id !== actionData.aidOnBudget.id || aidOnBudget.cid !== actionData.aidOnBudget.cid)]
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || []; 
        return newState;
    case 'ADD_AID_ON_BUDGET':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        actionData.aidOnBudget.cid = state.cid;
        newState.aidOnBudgetList = [Object.assign({}, actionData.aidOnBudget), ...newState.aidOnBudgetList];
        newState.errors = [];
        newState.infoMessages = [];
        newState.cid = ++newState.cid;        
        return newState; 
    case 'UPDATE_AID_ON_BUDGET':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);        
        newState.aidOnBudgetList =  newState.aidOnBudgetList.map(function(aidOnBudget) { 
            return ((aidOnBudget.id && aidOnBudget.id === actionData.aidOnBudget.id) || (aidOnBudget.cid && aidOnBudget.cid === actionData.aidOnBudget.cid)) ? Object.assign({}, actionData.aidOnBudget) : aidOnBudget;
            });        
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || [];         
        return newState;   
    default:            
        return state;
    }
}