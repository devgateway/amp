import {fetchTranslationsSuccess, fetchTranslationsPending, fetchTranslationsError}
    from '../../../../utils/actions/translationsActions';
import {loadTranslations} from '../../../../utils/loadTranslations';
import trnPack from '../config/initialTranslations';

function fetchTranslations(translations) {
    return dispatch => {
        dispatch(fetchTranslationsPending(translations));
        return loadTranslations(trnPack)
            .then(trnPack => dispatch(fetchTranslationsSuccess(trnPack)))
            .catch(error => dispatch(fetchTranslationsError(error)));
    }
}

export default fetchTranslations;
