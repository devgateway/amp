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
    },
    '1': {
        output1: {},
        output2: {},
        remarks:[],
        supportingEvidence: {}
    }
};

export default function reports( state: Object = defaultState, action: Object ) {
    switch ( action.type ) {        
        case 'FETCH_REPORT_SUCCESS':
            var newState = Object.assign({}, state);
            if(action.data.code == 1){
                if(action.data.requestData.output == 1){
                    newState[action.data.code].output1 = action.data.reportData;
                } else {
                    newState[action.data.code].output2 = action.data.reportData;
                }                
            } else {
                newState[action.data.code].mainReport = action.data.reportData;
            }
            
            return newState;
        case 'FETCH_REMARKS_SUCCESS':
            var newState = Object.assign( {}, state);
            newState[action.data.code].remarks = action.data.remarks;
            return newState;             
        case 'CLEAR_REMARKS':
            var newState = Object.assign( {}, state);
            newState[action.data.code].remarks = [];
            return newState;   
        case 'FETCH_SUPPORTING_EVIDENCE_SUCCESS':
            var newState = Object.assign( {}, state);
            newState[action.data.code].supportingEvidence = action.data.supportingEvidence;
            return newState;
        case 'CLEAR_SUPPORTING_EVIDENCE':
            var newState = Object.assign( {}, state);
            newState[action.data.code].supportingEvidence = {};
            return newState;
        default:
            return state;
    }
}