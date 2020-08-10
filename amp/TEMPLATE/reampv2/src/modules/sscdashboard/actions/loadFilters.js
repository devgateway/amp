import {
    fetchSectorsPending,
    fetchSectorsSuccess,
    fetchSectorsError,
    fetchCountriesError,
    fetchCountriesPending,
    fetchCountriesSuccess,
    fetchModalitiesError,
    fetchModalitiesPending,
    fetchModalitiesSuccess
} from './filtersActions';
import { fetchApiData } from '../../../utils/apiOperations';
import { API_FILTERS_SECTORS_URL, API_FILTERS_COUNTRIES_URL, API_FILTERS_MODALITIES_URL } from '../utils/constants';

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

export const loadCountriesFilters = () => {
    return dispatch => {
        dispatch(fetchCountriesPending());
        return fetchApiData({url: API_FILTERS_COUNTRIES_URL})
            .then(countries => {
                return dispatch(fetchCountriesSuccess(countries));
            })
            .catch(error => {
                return dispatch(fetchCountriesError(error))
            });
    }
};

export const loadModalitiesFilters = () => {
    return dispatch => {
        dispatch(fetchModalitiesPending());
        return fetchApiData({url: API_FILTERS_MODALITIES_URL})
            .then(typesOfSupport => {
                return dispatch(fetchModalitiesSuccess(typesOfSupport));
            })
            .catch(error => {
                return dispatch(fetchModalitiesError(error))
            });
    }
};

