import { fetchNDDSuccess, fetchNDDPending, fetchNDDError } from './startupAction';

function fetchNDD(url) {
  return dispatch => {
    dispatch(fetchNDDPending());
    fetch(url)
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw (res.error);
        }
        dispatch(fetchNDDSuccess(res));
        return res;
      })
      .catch(error => {
        dispatch(fetchNDDError(error));
      });
  };
}

export default fetchNDD;
