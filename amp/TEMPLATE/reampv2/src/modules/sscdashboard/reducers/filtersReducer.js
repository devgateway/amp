import {
  FETCH_FILTERS_SECTORS_SUCCESS,
  FETCH_FILTERS_SECTORS_ERROR,
  FETCH_FILTERS_SECTORS_PENDING,
  FETCH_FILTERS_COUNTRIES_ERROR,
  FETCH_FILTERS_COUNTRIES_PENDING,
  FETCH_FILTERS_COUNTRIES_SUCCESS,
  FETCH_FILTERS_MODALITIES_ERROR,
  FETCH_FILTERS_MODALITIES_PENDING,
  FETCH_FILTERS_MODALITIES_SUCCESS
} from '../actions/filtersActions';

const initialState = {
  sectorsLoaded: false,
  sectorsPending: false,
  sectors: [],
  sectorLoadingErrors: null,
  countriesLoaded: false,
  countriesPending: false,
  countries: [],
  countriesLoadingErrors: null,
  modalitiesPending: false,
  modalitiesLoaded: false,
  modalities: []
};
export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_FILTERS_SECTORS_PENDING:
      return {
        ...state,
        sectorsPending: true
      };
    case FETCH_FILTERS_SECTORS_SUCCESS:
      return {
        ...state,
        sectorsPending: false,
        sectorsLoaded: true,
        sectors: action.payload
      };
    case FETCH_FILTERS_SECTORS_ERROR:
      return {
        ...state,
        sectorsPending: false,
        sectorsLoaded: false,
        error: action.payload.error
      };

    case FETCH_FILTERS_COUNTRIES_PENDING:
      return {
        ...state,
        countriesPending: true
      };
    case FETCH_FILTERS_COUNTRIES_SUCCESS:
      return {
        ...state,
        countriesPending: false,
        countriesLoaded: true,
        countries: action.payload
      };
    case FETCH_FILTERS_COUNTRIES_ERROR:
      return {
        ...state,
        countriesPending: false,
        countriesLoaded: false,
        error: action.payload.error
      };
    case FETCH_FILTERS_MODALITIES_PENDING:
      return {
        ...state,
        modalitiesPending: true
      };
    case FETCH_FILTERS_MODALITIES_SUCCESS:
      return {
        ...state,
        modalitiesPending: false,
        modalitiesLoaded: true,
        modalities: action.payload
      };
    case FETCH_FILTERS_MODALITIES_ERROR:
      return {
        ...state,
        modalitiesPending: false,
        modalitiesLoaded: false,
        error: action.payload.error
      };

    default:
      return state;
  }
};
