import { fetchSectorsPending, fetchSectorsSuccess, fetchSectorsError } from './filtersActions';
import { fetchApiData } from '../../../utils/loadTranslations';
import { API_FILTERS_SECTORS_URL } from '../utils/constants';

export const loadSectorsFilters = () => {
    return dispatch => {
        dispatch(fetchSectorsPending());
        return fetchApiData({url: API_FILTERS_SECTORS_URL})
            .then(sectors => {
                return dispatch(fetchSectorsSuccess(sectors));
            })
            .catch(error => {
                return dispatch(fetchSectorsError(error))
            });
    }
};

