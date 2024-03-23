import { fetchReleasesSuccess, fetchReleasesPending, fetchReleasesError } from './startupAction';
import { AMP_OFFLINE_INSTALLERS } from '../constants/Constants';

function fetchReleases() {
  return dispatch => {
    dispatch(fetchReleasesPending());
    fetch(AMP_OFFLINE_INSTALLERS)
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw (res.error);
        }
        dispatch(fetchReleasesSuccess(res));
        return res;
      })
      .catch(error => {
        dispatch(fetchReleasesError(error));
      });
  };
}
export default fetchReleases;
