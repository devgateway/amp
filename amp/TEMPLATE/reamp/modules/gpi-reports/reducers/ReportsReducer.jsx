const defaultState = {
    '9b': {
        mainReport: {}
    },
    '6': {
        mainReport: {}
    },
    '5b': {
        mainReport: {}
    },
    '5a': {
        mainReport: {},
        remarks:[]
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
        case 'FETCH_REPORT_5B_MAIN_REPORT_SUCCESS':
            var newState = Object.assign( {}, state );
            newState['5b'].mainReport = action.data;
            return newState;
        case 'FETCH_REPORT_5A_MAIN_REPORT_SUCCESS':
            var newState = Object.assign( {}, state);
            newState['5a'].mainReport = action.data;
            return newState;
        case 'FETCH_REMARKS_SUCCESS':
            var newState = Object.assign( {}, state);
            newState[action.data.code].remarks = action.data.remarks;
            return newState;             
        case 'CLEAR_REMARKS':
            var newState = Object.assign( {}, state);
            newState[action.data.code].remarks = [];
            return newState;         
        default:
            return state;
    }
}