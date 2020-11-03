export const FETCH_TRANSLATIONS_PENDING = 'FETCH_TRANSLATIONS_PENDING';
export const FETCH_TRANSLATIONS_SUCCESS = 'FETCH_TRANSLATIONS_SUCCESS';
export const FETCH_TRANSLATIONS_ERROR = 'FETCH_TRANSLATIONS_ERROR';

export function fetchTranslationsPending(translations) {
    return {
        type: FETCH_TRANSLATIONS_PENDING,
        payload: translations
    }
}

export function fetchTranslationsSuccess(translations) {
    return {
        type: FETCH_TRANSLATIONS_SUCCESS,
        payload: translations
    }
}

export function fetchTranslationsError(error) {
    return {
        type: FETCH_TRANSLATIONS_ERROR,
        error: error
    }
};
