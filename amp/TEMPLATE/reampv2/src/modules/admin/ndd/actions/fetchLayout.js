import {fetchLayoutError, fetchLayoutPending, fetchLayoutSuccess} from './layoutAction';
import {LAYOUT_EP} from '../constants/Constants';

function fetchLayout() {
  return dispatch => {
    dispatch(fetchLayoutPending());
    return fetch(LAYOUT_EP)
      .then(res => res.json())
      .then(res => {
        if (res.error) {
          throw (res.error);
        }
        dispatch(fetchLayoutSuccess(res));
        return res;
      })
      .catch(error => {
        dispatch(fetchLayoutError(error));
      });
  };
}

export default fetchLayout;
