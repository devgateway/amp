const defaultState = {aidOnBudgetList:[]};

export default function aidOnBudgetReducer(state: Object = defaultState.aidOnBudgetList, action: Object) {
 
    switch (action.type) {
        case 'LOAD_AID_ON_BUDGET_LIST_SUCCESS':            
            return action.aidOnBudgetList;
        case 'AID_ON_BUDGET_SAVE_SUCCESS':
            return action.data;
        default:            
            return state;
    }
}