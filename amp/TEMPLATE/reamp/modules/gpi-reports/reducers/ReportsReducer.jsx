const defaultState = {'9b': {}};
export default function reports(state: Object = defaultState, action: Object) { 
    switch (action.type) {
    case 'FETCH_REPORT_9B_DATA_SUCCESS': 
        var newState = Object.assign({}, state); 
        newState['9b'] = action.data;
        return  newState;            
    default:            
        return state;
    }
}