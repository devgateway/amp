const defaultState = {
        data: {
            aidOnBudgetList:[],
            errors: [],
            infoMessages: []        
        }
};

export default function aidOnBudgetReducer(state: Object = defaultState.data, action: Object) {    
    switch (action.type) {
    case 'LOAD_AID_ON_BUDGET_LIST_SUCCESS':            
        return action.data;
    case 'AID_ON_BUDGET_SAVE_SUCCESS':
        var data = {};
        data.aidOnBudgetList = [...state.aidOnBudgetList.filter(aidOnBudget => aidOnBudget.id !== action.data.aidOnBudget.id),  Object.assign({}, action.data.aidOnBudget)]
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || [];       
        return data;
    case 'AID_ON_BUDGET_DELETE_SUCCESS':
        var data = {};
        data.aidOnBudgetList = [...state.aidOnBudgetList.filter(aidOnBudget => aidOnBudget.id !== action.data.aidOnBudget.id)]
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || [];       
        return data;
    default:            
        return state;
    }
}