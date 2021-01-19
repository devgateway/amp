import { updateActivitiesSuccess, updateActivitiesPending, updateActivitiesError } from './updateActivitiesAction';
import { UPDATE_ACTIVITIES_EP } from '../constants/Constants';

function updateActivities() {
  return dispatch => {
    dispatch(updateActivitiesPending());
    fetch(UPDATE_ACTIVITIES_EP).then(layoutRes => layoutRes.json()).then((result) => {
      if (result) {
        dispatch(updateActivitiesSuccess());
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
