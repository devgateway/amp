/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.config.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.helper.KeyValue;


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
	
	/**
	 * Retrieves JsonBean from AmpGlobalSettings
	 * @param object
	 * @return JsonBean
	 */
	public static JsonBean getGlobalSettingJson(AmpGlobalSettings ampGlobalSetting) {
		JsonBean globalSetting = new JsonBean();
		List<KeyValue> possiblesValues = ConfigHelper.getPossibleValues(ampGlobalSetting.getGlobalSettingsPossibleValues());
		JsonBean pValues = new JsonBean();
		if (possiblesValues!=null) {
			for (KeyValue value : possiblesValues) {
				pValues.set(value.getValue(), value.getKey());
			}
		}
		globalSetting.set("settingsName", ampGlobalSetting.getGlobalSettingsName());
		globalSetting.set("settingsValue", ampGlobalSetting.getGlobalSettingsValue());
		globalSetting.set("possibleValues", ampGlobalSetting.getGlobalSettingsPossibleValues()); 
		globalSetting.set("description", ampGlobalSetting.getGlobalSettingsDescription()); 
		globalSetting.set("section", ampGlobalSetting.getSection()); 
		globalSetting.set("valueTranslatable", ampGlobalSetting.getValueTranslatable());
		globalSetting.set("PossibleValuesIds", pValues);
		
		return globalSetting;
	}
	
	public static String getGlobalSettingName(LinkedHashMap<String, Object> object) {
		String name = String.valueOf(object.get(SETTINGS_NAME));
		return name;
	}
	
	public static List<KeyValue> getPossibleValues(String tableName) {
		List<KeyValue> ret = new ArrayList<>();

		if (tableName == null || tableName.length() == 0 || tableName.startsWith("t_") )
			return ret;

		List<Object[]> ls = null;
		try {
			ls = PersistenceManager.getSession().createSQLQuery("select id, value from " + tableName).list();
		} catch (Exception e) {
			return null;
		}
		for (Object[] obj : ls) {
			KeyValue keyValue = new KeyValue(PersistenceManager.getString(obj[0]), PersistenceManager.getString(obj[1]));
			ret.add(keyValue);
		}
		return ret;
	}
}
