import {saveNDDSuccess, saveNDDPending, saveNDDError} from './saveAction';
import {MAPPING_CONFIG} from '../constants/Constants'

function saveNDD(payload) {
    debugger
    return dispatch => {
        debugger
        dispatch(saveNDDPending());
        fetch(MAPPING_CONFIG, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: payload
        })
            .then(res => res.json())
            .then(res => {
                debugger
                if (res.error) {
                    throw(res.error);
                }
                dispatch(saveNDDSuccess(res));
                return res;
            })
            .catch(error => {
                dispatch(saveNDDError(error));
            });
    }
}

export default saveNDD;