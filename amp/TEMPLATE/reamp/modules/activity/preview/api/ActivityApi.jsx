import {fetchJson, postJson} from 'amp/tools/index';
import {ACTIVITY_API, FIELDS_DEFINITION_API, POSSIBLE_VALUES_API, FM_API} from '../common/Constants.jsx'


export default class ActivityApi {
    static getActivity(activityId) {
        return new Promise((resolve, reject) => {
            fetchJson(ACTIVITY_API + activityId).then((activity) => {
                resolve(activity)
            }).catch((error) => {
                reject(error);
            });
        });
    }

    static getFieldsDefinition() {
        const url = FIELDS_DEFINITION_API;
        return ActivityApi._fetchData(url);
    }

    static _fetchData(url) {
        return new Promise((resolve, reject) => {
            fetchJson(url).then((result) => {
                resolve(result)
            }).catch((error) => {
                reject(error);
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
                resolve(result.json())
            }).catch((error) => {
                reject(error);
            });
        });
    }
}
