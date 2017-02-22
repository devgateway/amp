const defaultState = {currencyList:[], orgList:[]};

export default function commonListsReducer(state: Object = defaultState, action: Object) { 
    switch (action.type) {
        case 'LOAD_CURRENCY_LIST_SUCCESS':            
            return {
              orgList: state.orgList,
              currencyList: action.currencyList
            };            
        case 'LOAD_ORG_LIST_SUCCESS':             
            return {
            orgList: action.orgList,
            currencyList: state.currencyList
          };  
        default:            
            return state;
    }
}