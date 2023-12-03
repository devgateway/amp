import {ActivityConstants, FieldPathConstants, FieldsManager} from 'amp-ui';
import ContactsApi from '../api/ContactApi.jsx';
import Logger from '../utils/LoggerManager';
import HydratorHelper from '../utils/HydratorHelper.jsx';

export const CONTACTS_LOAD_LOADING = 'CONTACTS_LOAD_LOADING';
export const CONTACTS_LOAD_LOADED = 'CONTACTS_LOAD_LOADED';
export const CONTACTS_LOAD_FAILED = 'CONTACTS_LOAD_FAILED';


export const loadHydratedContactsForActivity = (activity) => (dispatch, ownProps) => {
    dispatch({
        type: CONTACTS_LOAD_LOADING
    });
    return loadHydratedContacts(getActivityContactsId(activity))(dispatch, ownProps);
}
export const loadHydratedContacts = (ids) => (dispatch, ownProps) => {
    Promise.all([ContactsApi.getContacts(ids), ContactsApi.getContactsEnabledFields()]).then(([contactsByIdsRaw, contactsFieldsDef]) => {
        const contactsByIds = {};
        const { settings } = ownProps().startUpReducer;
        const contactFieldsManager = new FieldsManager(contactsFieldsDef, [],
            settings.language, Logger);
        // So far there is no need to Hydrate values that doesn't already come from contacts api
        // const requestData = {};
        //contactsByIdsRaw.forEach(c => HydratorHelper.hydrateObject(c, contactFieldsManager, '', requestData));
        //ContactsApi.fetchValuesForHydration(requestData).then(contactsDataForHydration => {
            contactsByIdsRaw.forEach(c => {
                // So far there is no need to Hydrate values that doesn't already come from contacts api
                // HydratorHelper.hydrateObject(c, contactFieldsManager, '', requestData, contactsDataForHydration);
                c['hydrated'] = true;
                contactsByIds[c.id] = c;

            });
            return dispatch({
                type: CONTACTS_LOAD_LOADED,
                payload: {
                    contactsByIds: contactsByIds,
                    contactFieldsManager: contactFieldsManager
                }
            });
        // s})
    });
}
export const getActivityContactsId = (activity, asIds = true) => {
    const contactsIds = new Set();
    FieldPathConstants.ACTIVITY_CONTACT_PATHS.forEach(cType => {
        const cs = activity[cType];
        if (cs && cs.length) {
            cs.forEach(c => contactsIds.add((asIds && c[ActivityConstants.CONTACT].id) || c[ActivityConstants.CONTACT]));
        }
    });
    return Array.from(contactsIds);
}
