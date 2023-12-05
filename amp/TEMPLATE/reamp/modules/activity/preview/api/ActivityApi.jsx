import ApiHelper from "../utils/ApiHelper.jsx";
import {
    ACTIVITY_API, FIELDS_DEFINITION_API, POSSIBLE_VALUES_API, FM_API, ACTIVITY_INFO_API,
    FUNDING_INFORMATION_API, ACTIVITY_FIELDS_ID_VALUES_API, ACTIVITY_WS_INFO
} from '../common/ReampConstants.jsx';


export default class ActivityApi {
    static getActivity(activityId) {
        const url = ACTIVITY_API + activityId;
        return ApiHelper._fetchData(url);
    }

    static getFieldsDefinition(fmId) {
        let url = FIELDS_DEFINITION_API;
        if (fmId) {
            url += '/' + fmId;
        }
        return ApiHelper._fetchData(url);
    }

    static fetchPossibleValues(body) {
        const url = POSSIBLE_VALUES_API;
        return ApiHelper._postData(url, body);

    }

    static fetchFmConfiguration(body) {
        const url = FM_API;
        return ApiHelper._postData(url, body);
    }

    static fetchActivityInfo(activityId) {
        const url = ACTIVITY_INFO_API + activityId;
        return ApiHelper._fetchData(url);
    }

    static fetchFundingInformation(activityId, currencyId) {
        const url = FUNDING_INFORMATION_API.replace('{ACTIVITY_ID}', activityId).replace('{CURRENCY_ID}', currencyId);
        return ApiHelper._fetchData(url);
    }

    static fetchActivityWsInformation(activityId) {
        const url = ACTIVITY_WS_INFO.replace('{ACTIVITY_ID}', activityId);
        return ApiHelper._fetchData(url);

    }

    static fetchValuesForHydration(activityFieldsWithIds, fmId) {
        let url = ACTIVITY_FIELDS_ID_VALUES_API;
        url = url.replace('/ws_id', fmId ? '/' + fmId : '');
        return ApiHelper._postData(url, activityFieldsWithIds);
    }

}
