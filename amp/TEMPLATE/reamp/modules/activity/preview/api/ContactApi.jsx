import { fetchJson, postJson } from 'amp/tools/index';
import {ACTIVITY_FIELDS_ID_VALUES_API} from "../common/ReampConstants";

export default class ActivityApi {
    static getContacts(contactsId) {
        const url = '/rest/contact/batch';
        return this._postData(url, contactsId);
    }
    static getContactsEnabledFields(){
        const url = '/rest/contact/fields';
        return this._fetchData(url);
    }
    static fetchValuesForHydration(contactFieldsWithIds){
        const url = '/rest/contact/field/id-values';
        return this._postData(url, contactFieldsWithIds);
    }
    static _postData(url, body) {
        return new Promise((resolve, reject) => {
            postJson(url, body).then((result) => {
                return resolve(result.json())
            }).catch((error) => {
                return reject(error);
            });
        });
    }
    static _fetchData(url) {
        return new Promise((resolve, reject) => {
            return fetchJson(url).then((result) => {
                resolve(result)
            }).catch((error) => {
                return reject(error);
            });
        });
    }
}
