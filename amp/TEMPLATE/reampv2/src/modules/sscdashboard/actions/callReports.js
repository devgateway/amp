import {
  fetchActivitiesDetailError,
  fetchActivitiesDetailPending,
  fetchActivitiesDetailSuccess,
  fetchActivitiesError,
  fetchActivitiesPending,
  fetchActivitiesSuccess
} from './reportsActions';
import { fetchApiData } from '../../../utils/apiOperations.js';
import { API_REPORTS_ACTIVITY_DETAIL_URL, API_SSC_DASHBOARD_URL } from '../utils/constants';

export const loadActivitiesDetails = (activitiesId) => dispatch => {
  dispatch(fetchActivitiesDetailPending());
  // TODO TO CHECK IF THE IDS SHOULDN'T GO BY POST
  const url = `${API_REPORTS_ACTIVITY_DETAIL_URL}?${activitiesId.map(p => `id=${p}`).join('&')}`;
  return fetchApiData({ url, body: {} })
    .then(activitiesDetail => dispatch(fetchActivitiesDetailSuccess(activitiesDetail)))
    .catch(error => dispatch(fetchActivitiesDetailError(error)));
};

export const loadActivities = () => dispatch => {
  dispatch(fetchActivitiesPending());
  return fetchApiData({ url: API_SSC_DASHBOARD_URL })
    .then(activities => {
      dispatch(loadActivitiesDetails(activities.activitiesId));
      return dispatch(fetchActivitiesSuccess(activities));
    })
    .catch(error => dispatch(fetchActivitiesError(error)));
};
