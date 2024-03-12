import ApiHelper from "../utils/ApiHelper.jsx";

export default class ActivityApi {
    static getContacts(contactsId) {
        const url = '/rest/contact/batch';
        return ApiHelper._postData(url, contactsId);
    }
    static getContactsEnabledFields(){
        const url = '/rest/contact/fields';
        return ApiHelper._fetchData(url);
    }
    static fetchValuesForHydration(contactFieldsWithIds){
        const url = '/rest/contact/field/id-values';
        return ApiHelper._postData(url, contactFieldsWithIds);
    }
}
