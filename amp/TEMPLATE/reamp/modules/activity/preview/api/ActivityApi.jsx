import {fetchJson, postJson} from 'amp/tools/index';
import {ACTIVITY_API, FIELDS_DEFINITION_API, POSSIBLE_VALUES_API, FM_API} from '../common/Constants.jsx'


export default class ActivityApi {
    static getActivity(activityId) {
        const url = ACTIVITY_API + activityId;
        return this._fetchData(url);
    }

    static getFieldsDefinition() {
        const url = FIELDS_DEFINITION_API;
        return ActivityApi._fetchData(url);
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

    static fetchPossibleValues(body){
        const url = POSSIBLE_VALUES_API;
        return ActivityApi._postData(url, body)
    }
    static fetchFmConfiguration(body){
        const url = FM_API;
        return ActivityApi._postData(url, body);
    }
    static _postData(url, body) {
        return new Promise((resolve, reject) => {
            postJson(url,body).then((result) => {
                return resolve(result.json())
            }).catch((error) => {
                return reject(error);
            });
        });
    }
}
