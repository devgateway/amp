const defaultState = {
    '9b': {
        mainReport: {}
    },
    '6': {
        mainReport: {}
    }
};
export default function reports( state: Object = defaultState, action: Object ) {
    switch ( action.type ) {
        case 'FETCH_REPORT_9B_MAIN_REPORT_SUCCESS':
            var newState = Object.assign( {}, state );
            newState['9b'].mainReport = action.data;
            return newState;
        case 'FETCH_REPORT_6_MAIN_REPORT_SUCCESS':
            var newState = Object.assign( {}, state );
            newState['6'].mainReport = action.data;
            return newState;
        default:
            return state;
    }
}