import {fetchNDDSuccess, fetchNDDPending, fetchNDDError} from './startupAction';
import {MAPPING_CONFIG} from '../constants/Constants'

function fetchNDD() {
    return dispatch => {
        dispatch(fetchNDDPending());
        fetch(MAPPING_CONFIG)
            .then(res => res.json())
            .then(res => {
                if (res.error) {
                    throw(res.error);
                }
                dispatch(fetchNDDSuccess(res));
                return res;
            })
            .catch(error => {
                dispatch(fetchNDDError(error));
            });
    }
}

export default fetchNDD;
