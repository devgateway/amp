import {RESOURCES_API, RESOURCES_ENABLED_FIELDS_API, RESOURCES_POSSIBLE_VALUES_API} from '../common/ReampConstants.jsx';
import ApiHelper from "../utils/ApiHelper.jsx";

export default class ResourceApi {
    static getResourcesEnabledFields() {
        const url = RESOURCES_ENABLED_FIELDS_API;
        return ApiHelper._fetchData(url);
    }

    static fetchResources(resourceUUID) {
        const url = RESOURCES_API;
        return ApiHelper._postData(url, resourceUUID);
    }

    static fetchPossibleValues(fields) {
        const url = RESOURCES_POSSIBLE_VALUES_API;
        return ApiHelper._postData(url, fields);
    }
}
