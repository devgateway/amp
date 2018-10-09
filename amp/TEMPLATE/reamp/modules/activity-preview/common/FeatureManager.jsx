/**
 * This is a helper class for checking FM configuration status,
 * base taken from AMP-offline FeatureManajger.js
 * revision 6567ae13218497a51c028ec90b0f4f281db20341
 * Its simplified just to check if a full path is enabled in amp
 * since so far is the only thing needed in activity preview. For this said simplicity we are just creating the full
 * list of entries
 * @author from offline : Nadejda Mandrescu
 *
 */
//TODO move to FM Constants
export const ACTIVITY_FORM_FM_ENTRY = 'ACTIVITY FORM';
export const PROJECT_MANAGEMENT_FM_ENTRY = 'PROJECT MANAGEMENT';

export default class FeatureManager {

    constructor(fmTree) {
        this._fmTree = fmTree;
    }

    set fmTree(fmTree) {
        this._fmTree = fmTree;
    }

    static setFMTree(fmTree) {
        this.fmTree = fmTree;
    }

    /**
     * Checks if the given FM path is fully enabled or only last segment is enabled
     * @param fmPath the FM path, e.g. '/PROJECT MANAGEMENT/Funding/Funding Information/Delivery rate'
     * @param onlyLastSegment specifies if to check if only the last segment is enabled (the AMP behavior for some cases)
     * @return {boolean}
     */
    static isFMSettingEnabled(fmPath, onlyLastSegment) {
        return this.isFMSettingEnabled(fmPath, onlyLastSegment);
    }

    static hasFMSetting(fmPath) {
        return this.hasFMSetting(fmPath);
    }

    isFMSettingEnabled(fmPath, onlyLastSegment) {
        if (this._fmTree) {
            let lastFMSubTree = this._fmTree;
            const segments = this._getPathSegments(fmPath);
            const foundLastFMSubTree = segments.every(segment => {
                lastFMSubTree = lastFMSubTree[segment];
                return lastFMSubTree !== undefined && (lastFMSubTree.__enabled || onlyLastSegment);
            });
            return foundLastFMSubTree && lastFMSubTree.__enabled;
        }
        return false;
    }

    hasFMSetting(fmPath) {
        const fmSetting = this.findFMSetting(fmPath);
        return fmSetting && fmSetting.__enabled !== undefined;
    }

    findFMSetting(fmPath) {
        const segments = this._getPathSegments(fmPath);
        return segments.reduce((currentFMSetting, segment) => currentFMSetting && currentFMSetting[segment]
            , this._fmTree || {});
    }

    setFMSetting(fmPath, enabled) {
        if (this._fmTree) {
            const segments = this._getPathSegments(fmPath);
            const lastFMSubTree = segments.reduce((currentFMTree, segment) => {
                let segmentFM = currentFMTree[segment];
                if (segmentFM === undefined) {
                    segmentFM = {};
                    currentFMTree[segment] = segmentFM;
                }
                return segmentFM;
            }, this._fmTree);
            lastFMSubTree.__enabled = enabled;
        }
    }

    _getPathSegments(fmPath) {
        // ignore first "/" to exclude empty string from the split
        return fmPath.substring(1).split('/');
    }
}

