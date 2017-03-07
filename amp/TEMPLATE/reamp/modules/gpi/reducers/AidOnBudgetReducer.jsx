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
                 orderBy: 'ampGPINiAidOnBudgetId',
                 sortOrder: 'desc'
             }             
           }
};

export default function aidOnBudgetReducer(state: Object = defaultState.data, action: Object) {    
    switch (action.type) {
    case 'LOAD_AID_ON_BUDGET_LIST_SUCCESS':
        const data = Object.assign({}, action.data)
        data.cid = state.cid;               
        return data;
    case 'AID_ON_BUDGET_ON_SAVE':
        var data = {};
        if (action.data.errors && action.data.errors.length > 0) {
            data.aidOnBudgetList = [...state.aidOnBudgetList]
        } else {
            data.aidOnBudgetList = state.aidOnBudgetList.map(function(aidOnBudget) { return ((aidOnBudget.id && aidOnBudget.id === action.data.aidOnBudget.id) || (aidOnBudget.cid && aidOnBudget.cid === action.data.aidOnBudget.cid)) ? Object.assign({}, action.data.aidOnBudget) : aidOnBudget; });
        }   
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid;
        data.paging = state.paging;
        data.sorting = state.sorting;
        return data;
    case 'AID_ON_BUDGET_ON_SAVE_ALL_EDITS':        
        var data = {};
        data.aidOnBudgetList = state.aidOnBudgetList.map(function(aidOnBudget) { 
            var found = action.data.aidOnBudgetList.find(obj =>{ obj
                return ((aidOnBudget.id && aidOnBudget.id === obj.id) || (aidOnBudget.cid && aidOnBudget.cid === obj.cid))     
            })
            
            return found ? Object.assign({}, found) : aidOnBudget;            
        });
        
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid;
        data.paging = state.paging;
        data.sorting = state.sorting;
        return data;        
    case 'AID_ON_BUDGET_DELETE_SUCCESS':
        var data = {};
        data.aidOnBudgetList = [...state.aidOnBudgetList.filter(aidOnBudget => aidOnBudget.id !== action.data.aidOnBudget.id || aidOnBudget.cid !== action.data.aidOnBudget.cid)]
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid;
        data.paging = state.paging;
        data.sorting = state.sorting;
        return data;
    case 'ADD_AID_ON_BUDGET':
        var data = {};
        action.data.aidOnBudget.cid = state.cid;
        data.aidOnBudgetList = [Object.assign({}, action.data.aidOnBudget), ...state.aidOnBudgetList];
        data.errors = [];
        data.infoMessages = [];
        data.cid = ++state.cid;
        data.paging = state.paging;
        data.sorting = state.sorting;
        return data; 
    case 'UPDATE_AID_ON_BUDGET':
        var data = {};
        data.aidOnBudgetList =  state.aidOnBudgetList.map(function(aidOnBudget) { return ((aidOnBudget.id && aidOnBudget.id === action.data.aidOnBudget.id) || (aidOnBudget.cid && aidOnBudget.cid === action.data.aidOnBudget.cid)) ? Object.assign({}, action.data.aidOnBudget) : aidOnBudget; });     
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid; 
        data.paging = state.paging;
        data.sorting = state.sorting;
        return data;   
    default:            
        return state;
    }
}