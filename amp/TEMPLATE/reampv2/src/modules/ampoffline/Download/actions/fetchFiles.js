import {fetchFilesSuccess, fetchFilesPending, fetchFilesError} from '../actions/startupAction';
import {AMP_OFFLINE_INSTALLERS} from '../constants/Constants'

function fetchFiles() {
    return dispatch => {
        dispatch(fetchFilesPending());
        fetch(AMP_OFFLINE_INSTALLERS)
            .then(res => res.json())
            .then(res => {
                if (res.error) {
                    throw(res.error);
                }
                dispatch(fetchFilesSuccess(res));
                return res;
            })
            .catch(error => {
                dispatch(fetchFilesError(error));
            })
    }
}
export default fetchFiles;
