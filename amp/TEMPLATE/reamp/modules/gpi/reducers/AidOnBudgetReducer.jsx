const defaultState = {
        data: {
            aidOnBudgetList:[],
            errors: [],
            infoMessages: [],
            cid: 1
        }
};

export default function aidOnBudgetReducer(state: Object = defaultState.data, action: Object) {    
    switch (action.type) {
    case 'LOAD_AID_ON_BUDGET_LIST_SUCCESS':
        action.data.cid = state.cid;
        return action.data;
    case 'AID_ON_BUDGET_SAVE_SUCCESS':
        var data = {};
        if (action.data.errors && action.data.errors.length > 0) {
            data.aidOnBudgetList = [...state.aidOnBudgetList]
        } else {
            data.aidOnBudgetList = [...state.aidOnBudgetList.filter(aidOnBudget => aidOnBudget.id !== action.data.aidOnBudget.id && aidOnBudget.cid !== action.data.aidOnBudget.cid ),  Object.assign({}, action.data.aidOnBudget)]
        }        
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid;
        return data;
    case 'AID_ON_BUDGET_DELETE_SUCCESS':
        var data = {};
        data.aidOnBudgetList = [...state.aidOnBudgetList.filter(aidOnBudget => aidOnBudget.id !== action.data.aidOnBudget.id || aidOnBudget.cid !== action.data.aidOnBudget.cid)]
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid;
        return data;
    case 'ADD_AID_ON_BUDGET':
        var data = {};
        action.data.aidOnBudget.cid = state.cid;
        data.aidOnBudgetList = [Object.assign({}, action.data.aidOnBudget), ...state.aidOnBudgetList];
        data.errors = [];
        data.infoMessages = [];
        data.cid = ++state.cid;
        return data; 
    case 'UPDATE_AID_ON_BUDGET':
        var data = {};
        data.aidOnBudgetList =  state.aidOnBudgetList.map(function(aidOnBudget) { return (aidOnBudget.id !== action.data.aidOnBudget.id || aidOnBudget.cid !== action.data.aidOnBudget.cid) ? Object.assign({}, action.data.aidOnBudget) : aidOnBudget; });     
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid;
        return data;
    default:            
        return state;
    }
}