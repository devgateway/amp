import {fetchProgramsSuccess, fetchProgramsPending, fetchProgramsError} from './startupAction';
import {AVAILABLE_PROGRAMS} from '../constants/Constants'

function fetchPrograms() {
    return dispatch => {
        dispatch(fetchProgramsPending());
        fetch(AVAILABLE_PROGRAMS)
            .then(res => res.json())
            .then(res => {
                if (res.error) {
                    throw(res.error);
                }
                dispatch(fetchProgramsSuccess(res));
                return res;
            })
            .catch(error => {
                dispatch(fetchProgramsError(error));
            });
    }
}

export default fetchPrograms;