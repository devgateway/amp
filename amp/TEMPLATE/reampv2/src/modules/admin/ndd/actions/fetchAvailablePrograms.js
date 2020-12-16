import { fetchProgramsSuccess, fetchProgramsPending, fetchProgramsError } from './startupAction';

function fetchPrograms(url) {
  return dispatch => {
    dispatch(fetchProgramsPending());
    fetch(url)
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw (res.error);
        }
        dispatch(fetchProgramsSuccess(res));
        return res;
      })
      .catch(error => {
        dispatch(fetchProgramsError(error));
      });
  };
}

export default fetchPrograms;
