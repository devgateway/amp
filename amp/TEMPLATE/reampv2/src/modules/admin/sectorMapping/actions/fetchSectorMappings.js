import { fetchSectorMappingSuccess, fetchSectorMappingPending, fetchSectorMappingError } from './startupAction';

function fetchSectorMappings(url) {
  return dispatch => {
    dispatch(fetchSectorMappingPending());
    fetch(url)
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw (res.error);
        }
        dispatch(fetchSectorMappingSuccess(res));
        return res;
      })
      .catch(error => {
        dispatch(fetchSectorMappingError(error));
      });
  };
}

export default fetchSectorMappings;
