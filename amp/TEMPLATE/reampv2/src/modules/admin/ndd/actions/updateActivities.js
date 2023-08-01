import {
    invokeUpdateActivities,
    updateActivitiesError,
    updateActivitiesPending,
    updateActivitiesSuccess
} from './updateActivitiesAction';
import {UPDATE_ACTIVITIES_EP} from '../constants/Constants';

let timer;
const GET = 'GET';

function scheduleTimer(url, dispatch) {
  clearInterval(timer);
  timer = setInterval(() => checkForUpdate(url, dispatch), 1000);
}

function checkForUpdate(url, dispatch) {
  fetch(url).then(layoutRes => {
    if (layoutRes.status === 200) {
      return layoutRes.headers.get('X-Async-Status');
    } else {
      return layoutRes.json();
    }
  }).then((result) => {
    if (result === null) {
      clearInterval(timer);
      return dispatch(updateActivitiesSuccess());
    } else if (!result.error) {
      if (result === 'RUNNING') {
        // its running do nothing
      } else {
        dispatch(updateActivitiesError({ msg: `result ${result}` }));
        clearInterval(timer);
      }
    } else {
      dispatch(updateActivitiesError({ msg: 'result false' }));
      clearInterval(timer);
    }
    return result;
  }).catch(error => {
    dispatch(updateActivitiesError(error));
  });
}

function updateActivities() {
  return dispatch => {
    dispatch(updateActivitiesPending());
    const requestOptions = {
      method: GET,
      headers: { Prefer: 'respond-async' }
    };
    fetch(UPDATE_ACTIVITIES_EP, requestOptions).then(layoutRes => {
      if (layoutRes.status === 200) {
        return layoutRes.headers.get('location');
      } else {
        return layoutRes.json();
      }
    }).then((result) => {
      if (!result.error) {
        dispatch(invokeUpdateActivities());
        scheduleTimer(result, dispatch);
        // TODO better process generic errors coming from the API
      } else if (result.error['0212']) {
        const msg = result.error['0212'][0].PROCESS_ALREADY_RUNNING[0];
        dispatch(updateActivitiesError({ msg }));
      } else {
        dispatch(updateActivitiesError({ msg: 'result false' }));
      }
      return result;
    }).catch(error => {
      dispatch(updateActivitiesError(error));
    });
  };
}

export default updateActivities;
