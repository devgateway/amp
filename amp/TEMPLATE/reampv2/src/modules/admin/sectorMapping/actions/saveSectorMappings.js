import {saveSectorMappingPending, saveSectorMappingSuccess, saveSectorMappingError} from './saveAction';
import {LAYOUT_EP} from '../constants/Constants';

function saveSectorMappings(mappings, urlSave) {
  return dispatch => {
    dispatch(saveSectorMappingPending());
    fetch(LAYOUT_EP).then(layoutRes => layoutRes.json()).then(data => {
      if (data.logged) {
        fetch(urlSave, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(mappings)
        }).then(response => {
            processResponse(response);
            dispatch(saveSectorMappingSuccess(response));
            return response;
          })
          .catch(error => {
            dispatch(saveSectorMappingError(error));
          });
      } else {
        window.location.replace('/login.do');
        dispatch(saveSectorMappingError({ error: 'not logged' }));
      }
    }).catch(error => {
      dispatch(saveSectorMappingError(error));
    });
  };
}

function processResponse(response) {
  if (response.status === 500 || response.error) {
    if (response.error) {
      throw (response.error);
    } else {
      throw (response.statusText);
    }
  }
}

export default saveSectorMappings;
