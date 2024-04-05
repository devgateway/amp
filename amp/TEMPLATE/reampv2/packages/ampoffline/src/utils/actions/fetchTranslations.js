import { fetchTranslationsSuccess, fetchTranslationsPending, fetchTranslationsError }
  from '../../actions/translationsActions';
import { loadTranslations } from './loadTranslations';

function fetchTranslations(translations) {
  return dispatch => {
    dispatch(fetchTranslationsPending(translations));
    return loadTranslations(translations)
      .then(trnPack => dispatch(fetchTranslationsSuccess(trnPack)))
      .catch(error => dispatch(fetchTranslationsError(error)));
  };
}
export default fetchTranslations;
