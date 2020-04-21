import {
    FETCH_TRANSLATIONS_ERROR,FETCH_TRANSLATIONS_PENDING,FETCH_TRANSLATIONS_SUCCESS
} from '../actions/translationsActions';

const initialState = {
    pending: false,
    translations: {},
    error: null
}

export default function translationsReducer(state = initialState, action) {
    switch(action.type) {
        case FETCH_TRANSLATIONS_PENDING:
            return {
                ...state,
                pending: true,
                translations: action.payload.defaultTrnPack
            }
        case FETCH_TRANSLATIONS_SUCCESS:
            return {
                ...state,
                pending: false,
                translations: action.payload
            }
        case FETCH_TRANSLATIONS_ERROR:
            return {
                ...state,
                pending: false,
                error: action.error
            }
        default:
            return state;
    }
}
