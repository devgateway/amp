import {
  getSharedDataPending, getSharedDataError, getSharedDataSuccess
} from './shareLinkActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { GET_SHARED_EP } from '../utils/constants';

export const getSharedData = (id) => dispatch => {
  dispatch(getSharedDataPending());
  return fetchApiData({
    url: GET_SHARED_EP + id
  })
    .then(payload => dispatch(getSharedDataSuccess(payload)))
    .catch(error => dispatch(getSharedDataError(error)));
};

export default getSharedData;
