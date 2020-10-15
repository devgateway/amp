import {saveNDDSuccess, saveNDDPending, saveNDDError} from './saveAction';
import {SAVE_CONFIG} from '../constants/Constants'

function saveNDD(payload) {
    return dispatch => {
        dispatch(saveNDDPending());
        fetch(SAVE_CONFIG, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        })
            .then(res => {
                if (res.error) {
                    throw(res.error);
                }
                dispatch(saveNDDSuccess(res));
                return res;
            })
            .catch(error => {
                debugger
                dispatch(saveNDDError(error));
            });
    }
}

export default saveNDD;