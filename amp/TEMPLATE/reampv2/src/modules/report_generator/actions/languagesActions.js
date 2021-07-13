import { fetchApiData } from '../../../utils/loadTranslations';
import { URL_LANGUAGES } from '../utils/constants';

export const FETCH_LANGUAGES_PENDING = 'FETCH_LANGUAGES_PENDING';
export const FETCH_LANGUAGES_SUCCESS = 'FETCH_LANGUAGES_SUCCESS';
export const FETCH_LANGUAGES_ERROR = 'FETCH_LANGUAGES_ERROR';

export function fetchLanguagesPending() {
  return {
    type: FETCH_LANGUAGES_PENDING
  };
}

export function fetchLanguagesSuccess(payload) {
  return {
    type: FETCH_LANGUAGES_SUCCESS,
    payload,
  };
}

export function fetchLanguagesError(error) {
  return {
    type: FETCH_LANGUAGES_ERROR,
    error
  };
}

export const fetchLanguages = () => dispatch => {
  dispatch(fetchLanguagesPending());
  return fetchApiData({
    url: URL_LANGUAGES
  }).then((data) => dispatch(fetchLanguagesSuccess(data)))
    .catch(error => dispatch(fetchLanguagesError(error)));
};
