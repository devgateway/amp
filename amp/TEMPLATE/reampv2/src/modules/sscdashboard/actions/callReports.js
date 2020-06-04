import {
    fetchActivitiesDetailError,
    fetchActivitiesDetailPending,
    fetchActivitiesDetailSuccess
} from './reportsActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { API_REPORTS_ACTIVITY_DETAIL_URL } from '../utils/constants';

export const loadActivitiesDetails = (activitiesId) => {
    return dispatch => {
        dispatch(fetchActivitiesDetailPending());
        //TODO TO CHECK IF THE IDS SHOULDN'T GO BY POST
        const url = `${API_REPORTS_ACTIVITY_DETAIL_URL}?${activitiesId.map(p => `id=${p}`).join('&')}`;
        return fetchApiData({url, body: {}})
            .then(activitiesDetail => {
                return dispatch(fetchActivitiesDetailSuccess(activitiesDetail));
            })
            .catch(error => {
                return dispatch(fetchActivitiesDetailError(error))
            });
    }
};
