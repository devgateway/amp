package org.digijava.module.aim.util;

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
			return getResourceManagerSettings().getMaximunFileSize();
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
}
