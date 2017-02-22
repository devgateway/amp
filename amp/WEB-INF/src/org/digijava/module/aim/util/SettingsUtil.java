package org.digijava.module.aim.util;

import org.digijava.module.aim.dbentity.AmpResourceManagerSettings;

public class SettingsUtil {
	public static String getSortColumn() {
		{
			if (getResourceManagerSettings() == null) {
				return null;
			} else {
				return getResourceManagerSettings().getSortColumn();
			}
		}
	}

	public static Integer getMaximunFileSize() {
		{
			if (getResourceManagerSettings() == null) {
				return null;
			} else {
				return getResourceManagerSettings().getMaximunFileSize();
			}
		}
	}

	public static boolean isLimitFileToUpload() {
		if (getResourceManagerSettings() == null) {
			return false;
		} else {
			return getResourceManagerSettings().isLimitFileToUpload();
		}
	}

	public static AmpResourceManagerSettings getResourceManagerSettings() {
		return org.digijava.module.aim.util.DbUtil.getResourceManagerSettings();
	}

}
