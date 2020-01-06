import { fetchJson, postJson } from 'amp/tools/index';
import { ACTIVITY_API, FIELDS_DEFINITION_API, POSSIBLE_VALUES_API, FM_API, SETTINGS_API , ACTIVITY_INFO_API,
    FUNDING_INFORMATION_API, ACTIVITY_FIELDS_ID_VALUES_API, ACTIVITY_WS_INFO }  from '../common/ReampConstants.jsx'


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

    static fetchPossibleValues(body) {
        const url = POSSIBLE_VALUES_API;
        return ActivityApi._postData(url, body)
    }

    static fetchFmConfiguration(body) {
        const url = FM_API;
        return ActivityApi._postData(url, body);
    }

    static fetchActivityInfo(activityId) {
        const url = ACTIVITY_INFO_API + activityId;
        return ActivityApi._fetchData(url);
    }
    static fetchFundingInformation(activityId, currencyId){
        const url = FUNDING_INFORMATION_API.replace('{ACTIVITY_ID}', activityId).
        replace('{CURRENCY_ID}',currencyId);
        return this._fetchData(url);
    }
    static fetchActivityWsInformation(activityId){
        const url = ACTIVITY_WS_INFO.replace('{ACTIVITY_ID}', activityId);
        return this._fetchData(url);

    }
    static fetchValuesForHydration(activityFieldsWithIds){
        const url = ACTIVITY_FIELDS_ID_VALUES_API;
        return this._postData(url, activityFieldsWithIds);
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
}
