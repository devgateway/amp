import {
  fetchShareLinkPending, fetchShareLinkError, fetchShareLinkSuccess
} from './shareLinkActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { SHARING_EP } from '../utils/constants';

export const getShareLink = (data) => dispatch => {
  dispatch(fetchShareLinkPending());
  return fetchApiData({
    url: SHARING_EP,
    body: { data }
  })
    .then(payload => dispatch(fetchShareLinkSuccess(payload)))
    .catch(error => dispatch(fetchShareLinkError(error)));
};
