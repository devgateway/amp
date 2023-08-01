import {fetchShareLinkError, fetchShareLinkPending, fetchShareLinkSuccess} from './shareLinkActions';
import {fetchApiData} from '../../../utils/apiOperations';
import {SHARING_EP} from '../utils/constants';

export const getShareLink = (filters, settings, fundingType, selectedPrograms) => dispatch => {
  dispatch(fetchShareLinkPending());
  return fetchApiData({
    url: SHARING_EP,
    body: {
      title: '',
      description: '',
      stateBlob: JSON.stringify({
        filters, settings, fundingType, selectedPrograms
      })
    }
  })
    .then(payload => dispatch(fetchShareLinkSuccess(payload)))
    .catch(error => dispatch(fetchShareLinkError(error)));
};
