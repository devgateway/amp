/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.config.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;


/**
 * Helper methods for config save
 * 
 * @author apicca
 */
public class ConfigHelper {
	
	private static final String SECTION = "section";
	private static final String DESCRIPTION = "description";
	private static final String SETTINGS_VALUE = "settingsValue";
	private static final String POSSIBLE_VALUES = "possibleValues";
	private static final String SETTINGS_NAME = "settingsName";
	private static final String ORG_DIGIJAVA_MODULE_AIM_HELPER_GLOBAL_SETTINGS_CONSTANTS = "org.digijava.module.aim.helper.GlobalSettingsConstants";
	private static final Logger logger = Logger.getLogger(ConfigHelper.class);
	/**
	 * Retrieves the class specified as type for Generics
	 * @param field
	 * @return
	 */
	public static ArrayList<String> getValidSettings() {
		ArrayList<String> list = new ArrayList<String>();
		Class<?> clazz = null;
		try {
			clazz = Class.forName(ORG_DIGIJAVA_MODULE_AIM_HELPER_GLOBAL_SETTINGS_CONSTANTS);

			Field[] fields = clazz.getDeclaredFields();

			for (Field f : fields) {
				if ("string".equalsIgnoreCase(f.getType().getSimpleName())) {
					list.add((String) f.get(null));
					System.err.printf("%s: %s\n", f, (String) f.get(null));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Retrieves AmpGlobalSettings from request
	 * @param object
	 * @return AmpGlobalSettings
	 */
	public static AmpGlobalSettings getGlobalSetting(AmpGlobalSettings set,LinkedHashMap<String, Object> object) {
		set.setGlobalSettingsName(String.valueOf(object.get(SETTINGS_NAME)));
		set.setGlobalSettingsPossibleValues(String.valueOf(object.get(POSSIBLE_VALUES)));
		set.setGlobalSettingsValue(String.valueOf(object.get(SETTINGS_VALUE)));
		set.setGlobalSettingsDescription(String.valueOf(object.get(DESCRIPTION)));
		set.setSection(String.valueOf(object.get(SECTION)));
		return set;
	}
	
	public static String getGlobalSettingName(LinkedHashMap<String, Object> object) {
		String name = String.valueOf(object.get(SETTINGS_NAME));
		return name;
	}
	
}
