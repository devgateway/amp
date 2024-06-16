import {
    fetchSectorsSuccess,
    fetchSectorsPending,
    fetchSectorsError
} from './startupAction';

function fetchSectors(url, type) {
  return dispatch => {
    dispatch(fetchSectorsPending());
    fetch(url + '/' + type)
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw (res.error);
        }
        dispatch(fetchSectorsSuccess(res));
        return res;
      })
      .catch(error => {
        dispatch(fetchSectorsError(error));
      });
  };
}

export default fetchSectors;
