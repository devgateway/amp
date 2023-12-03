import {CONTACTS_LOAD_LOADING, CONTACTS_LOAD_LOADED, CONTACTS_LOAD_FAILED} from '../actions/ContactsAction.jsx';

const defaultState = {
    contactsByIds: {},
    contactFieldsManager: undefined,
    isContactLoading: true,
    isContactLoaded: false,
    error: undefined
};

export default function contactReducer(state: Object = defaultState, action: Object) {
    switch (action.type) {

        case CONTACTS_LOAD_LOADING:
            return {...state, isContactLoading: true};
        case CONTACTS_LOAD_LOADED:
            return {
                ...state,
                contactsByIds: action.payload.contactsByIds,
                contactFieldsManager: action.payload.contactFieldsManager,
                isContactLoading: false,
                isContactLoaded: true
            };
        case CONTACTS_LOAD_FAILED:
            return {...state, error: action.payload.error}
        default:
            return state;
    }
}
