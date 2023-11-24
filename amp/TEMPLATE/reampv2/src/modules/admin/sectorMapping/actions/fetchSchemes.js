import {
    fetchSchemesSuccess,
    fetchSchemesPending,
    fetchSchemesError
} from './startupAction';

function fetchSchemes(url) {
  return dispatch => {
    dispatch(fetchSchemesPending());
    fetch(url)
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw (res.error);
        }
        dispatch(fetchSchemesSuccess(res));
        return res;
      })
      .catch(error => {
        dispatch(fetchSchemesError(error));
      });
  };
}

export default fetchSchemes;
