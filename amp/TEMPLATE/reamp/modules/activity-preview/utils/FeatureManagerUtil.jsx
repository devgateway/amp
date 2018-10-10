
//TODO  Move to constants
import * as FMPaths from "./FeatureManagerConstants";
import {ACTIVITY_FORM_FM_ENTRY, PROJECT_MANAGEMENT_FM_ENTRY} from "../common/FeatureManager";
import FeatureManager from "../common/FeatureManager";

export function getFeatureManagerRequestData() {
    const fmPaths = Object.values(FMPaths);
    const featureManagerRequestData = {};
    featureManagerRequestData['reporting-fields'] = false;
    featureManagerRequestData['enabled-modules'] = false;
    featureManagerRequestData['detail-modules'] = [];
    featureManagerRequestData['detail-modules'].push(ACTIVITY_FORM_FM_ENTRY);
    featureManagerRequestData['detail-modules'].push(PROJECT_MANAGEMENT_FM_ENTRY);
    featureManagerRequestData['detail-flat'] = false;
    featureManagerRequestData['full-enabled-paths'] = false;
    featureManagerRequestData['fm-paths'] = fmPaths;
    debugger;
    return featureManagerRequestData;
}

/**
 * Transforms FM tree to Offline format
 * taken from offline to move as a utility, see if we dont have a common lib, at least have this file sin comon
 * to avoid dupe code
 * @param fmTree
 * @return {{}}
 * @private
 */
export function prepareData(fmTree) {
    const dataToStore = {};
    Object.entries(fmTree).forEach(([key, value]) => {
        const excludeItems = new Set([key.toUpperCase(), '__ENABLED']);
        let actualSubKey = key;
        const firstLevelEntries = Object.keys(value).filter(subKey => {
            const subKeyUpper = subKey.toUpperCase();
            if (subKeyUpper === key) {
                actualSubKey = subKey;
            }
            return !excludeItems.has(subKeyUpper);
        });
        if (firstLevelEntries.length === 0) {
            dataToStore[actualSubKey] = value[actualSubKey];
        } else {
            dataToStore[key] = value;
        }
    });
    _setUndetectedFMSettingsAsDisabled(dataToStore);
    return { fmTree: dataToStore };
}

 function _setUndetectedFMSettingsAsDisabled(newFmTree) {
    const fmPaths = Object.values(FMPaths);
    const futureFM = new FeatureManager(newFmTree);
    fmPaths.forEach(fmPath => {
        if (!futureFM.hasFMSetting(fmPath)) {
            futureFM.setFMSetting(fmPath, false);
        }
    });
}