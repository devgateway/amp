import { saveNDDSuccess, saveNDDPending, saveNDDError } from './saveAction';
import { LAYOUT_EP } from '../constants/Constants';

function saveNDD(src, dst, mappings, urlSavePrograms, urlSaveConfig, level) {
  return dispatch => {
    dispatch(saveNDDPending());
    fetch(LAYOUT_EP).then(layoutRes => layoutRes.json()).then(data => {
      if (data.logged) {
        fetch(urlSavePrograms, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            'src-program': src ? src.id : null,
            'dst-program': dst ? dst.id : null,
            level
          })
        }).then(res => {
          processResponse(res);
          return fetch(urlSaveConfig, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(mappings)
          })
            .then(response => {
              processResponse(response);
              dispatch(saveNDDSuccess(response));
              return response;
            })
            .catch(error => {
              dispatch(saveNDDError(error));
            });
        })
          .catch(error => {
            dispatch(saveNDDError(error));
          });
      } else {
        window.location.replace('/login.do');
        dispatch(saveNDDError({ error: 'not logged' }));
      }
    }).catch(error => {
      dispatch(saveNDDError(error));
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

export default saveNDD;
