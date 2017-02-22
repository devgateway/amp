package org.digijava.module.aim.util;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.module.aim.dbentity.AmpResourceManagerSettings;

public class SettingsUtil {

	public static String getSortColumn() {
		if (getResourceManagerSettings() != null) {
			return getResourceManagerSettings().getSortColumn();
		}

		return null;
	}

	public static Integer getMaximunFileSize() {
		if (getResourceManagerSettings() != null) {
			return getResourceManagerSettings().getMaximumFileSize();
		}

		return null;
	}

	public static boolean isLimitFileToUpload() {
		if (getResourceManagerSettings() != null) {
			return getResourceManagerSettings().isLimitFileToUpload();
		}

		return false;
	}

	public static AmpResourceManagerSettings getResourceManagerSettings() {
		return DbUtil.getResourceManagerSettings();
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
