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
import { fetchApiData } from '../../../utils/loadTranslations';
import { API_FILTERS_SECTORS_URL, API_FILTERS_COUNTRIES_URL, API_FILTERS_MODALITIES_URL } from '../utils/constants';

export const loadSectorsFilters = () => dispatch => {
  dispatch(fetchSectorsPending());
  return fetchApiData({ url: API_FILTERS_SECTORS_URL })
    .then(sectors => dispatch(fetchSectorsSuccess(sectors)))
    .catch(error => dispatch(fetchSectorsError(error)));
};

export const loadCountriesFilters = () => dispatch => {
  dispatch(fetchCountriesPending());
  return fetchApiData({ url: API_FILTERS_COUNTRIES_URL })
    .then(countries => dispatch(fetchCountriesSuccess(countries)))
    .catch(error => dispatch(fetchCountriesError(error)));
};

export const loadModalitiesFilters = () => dispatch => {
  dispatch(fetchModalitiesPending());
  return fetchApiData({ url: API_FILTERS_MODALITIES_URL })
    .then(typesOfSupport => dispatch(fetchModalitiesSuccess(typesOfSupport)))
    .catch(error => dispatch(fetchModalitiesError(error)));
};
