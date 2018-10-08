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
export const ACTIVITY_FORM_FM_ENTRY = 'ACTIVITY FORM';

export default class FeatureManager {

    constructor(fmConfig) {
        this._fmConfig = fmConfig[ACTIVITY_FORM_FM_ENTRY];
    }
    isFMSettingEnabled(fmPath) {
        return this._fmConfig.includes(fmPath);
    }
}


