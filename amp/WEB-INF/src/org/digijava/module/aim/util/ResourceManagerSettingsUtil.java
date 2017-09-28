package org.digijava.module.aim.util;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;

/**
 * Wrapper class for global settings related to resource manager
 * 
 * @author jdeanquin
 *
 */
public class ResourceManagerSettingsUtil {

    public static String getSortColumn() {
        return FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_RESOURCES_SORT_COLUMN);
    }

    public static Integer getMaximunFileSize() {
        return FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.CR_MAX_FILE_SIZE);
    }

    public static boolean isLimitFileToUpload() {
        return FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.LIMIT_FILE_TYPE_FOR_UPLOAD);
    }

    public static String getResourceSettingValue(String name) {
        if (StringUtils.equals(name, SettingsConstants.SORT_COLUMN)) {
            return getSortColumn();
        } else if (StringUtils.equals(name, SettingsConstants.MAXIMUM_FILE_SIZE)) {
            return Integer.toString(getMaximunFileSize());
        } else if (StringUtils.equals(name, SettingsConstants.LIMIT_FILE_TO_UPLOAD)) {
            return Boolean.toString(isLimitFileToUpload());
        }

        return null;
    }
}
