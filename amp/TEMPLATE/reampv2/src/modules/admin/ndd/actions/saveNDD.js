import { saveNDDSuccess, saveNDDPending, saveNDDError } from './saveAction';
import { SAVE_CONFIG, SAVE_MAIN_PROGRAMS } from '../constants/Constants';

function saveNDD(src, dst, mappings) {
  return dispatch => {
    dispatch(saveNDDPending());
    fetch(SAVE_MAIN_PROGRAMS, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ 'src-program': src.id, 'dst-program': dst.id })
    }).then(res => {
      if (res.error) {
        throw (res.error);
      }
      fetch(SAVE_CONFIG, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(mappings)
      }).then(res => {
        if (res.error) {
          throw (res.error);
        }
        dispatch(saveNDDSuccess(res));
        return res;
      }).catch(error => {
        dispatch(saveNDDError(error));
      });
    }).catch(error => {
      dispatch(saveNDDError(error));
    });
  };
}

export default saveNDD;
