export const FETCH_FILES_PENDING = 'FETCH_FILES_PENDING';
export const FETCH_FILES_SUCCESS = 'FETCH_FILES_SUCCESS';
export const FETCH_FILES_ERROR = 'FETCH_FILES_ERROR';

export const startupAction = () => dispatch => {
    debugger;
    dispatch({
        type: 'SIMPLE_ACTION',
        payload: 'result_of_simple_action'
    })
}

export function fetchFilesPending() {
    debugger;
    return {
        type: FETCH_FILES_PENDING
    }
}

export function fetchFilesSuccess(files) {
    return {
        type: FETCH_FILES_SUCCESS,
        payload: files
    }

}

export function fetchFilesError(error) {
    return {
        type: FETCH_FILES_ERROR,
        error: error
    }
};
