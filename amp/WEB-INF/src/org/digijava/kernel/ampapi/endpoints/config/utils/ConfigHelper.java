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
	private static final String SETTINGS_VALUE = "settingValue";
	private static final String POSSIBLE_VALUES = "possibleValues";
	private static final String SETTINGS_NAME = "settingName";
	private static final String VALUE_TRANSLATABLE = "valueTranslatable";
	private static final String ORG_DIGIJAVA_MODULE_AIM_HELPER_GLOBAL_SETTINGS_CONSTANTS = "org.digijava.module.aim.helper.GlobalSettingsConstants";
	
	private static final String T_BOOLEAN = "t_Boolean";
	private static final String T_INTEGER = "t_Integer";
	private static final String T_YEAR_DEFAULT_START = "t_year_default_start";
	private static final String T_YEAR_DEFAULT_END = "t_year_default_end";
	private static final String T_STATIC_RANGE = "t_static_range";
	private static final String T_DOUBLE = "t_Double";
	private static final String NULL_VALUE = "null";
	
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
		if ("null".equalsIgnoreCase(set.getGlobalSettingsPossibleValues())) {
			set.setGlobalSettingsPossibleValues("");
		}
		set.setGlobalSettingsDescription(String.valueOf(object.get(DESCRIPTION)));
		set.setSection(String.valueOf(object.get(SECTION)));
		set.setValueTranslatable(Boolean.valueOf(String.valueOf(object.get(VALUE_TRANSLATABLE))));
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
		globalSetting.set("settingName", ampGlobalSetting.getGlobalSettingsName());
		globalSetting.set("settingValue", ampGlobalSetting.getGlobalSettingsValue());
		globalSetting.set("possibleValues", ampGlobalSetting.getGlobalSettingsPossibleValues()); 
		globalSetting.set("description", ampGlobalSetting.getGlobalSettingsDescription()); 
		globalSetting.set("section", ampGlobalSetting.getSection()); 
		globalSetting.set("valueTranslatable", ampGlobalSetting.getValueTranslatable());
		globalSetting.set("possibleValuesIds", pValues);
		
		return globalSetting;
	}
	
	/**
	 * Validate settingValue
	 * @param object
	 * @return boolean
	 */
	public static boolean validateGlobalSetting(AmpGlobalSettings ampGlobalSetting) {
		boolean isValid = false;
		List<KeyValue> possiblesValues = ConfigHelper.getPossibleValues(ampGlobalSetting.getGlobalSettingsPossibleValues());	
		switch(ampGlobalSetting.getGlobalSettingsPossibleValues()) {
        case T_BOOLEAN:
        	isValid = "true".equalsIgnoreCase(ampGlobalSetting.getGlobalSettingsValue()) || "false".equalsIgnoreCase(ampGlobalSetting.getGlobalSettingsValue());
        	break;
        case T_INTEGER: case T_STATIC_RANGE:
        	isValid = isValidNumber(Integer.class,ampGlobalSetting.getGlobalSettingsValue());
        	break;
        case T_DOUBLE:
        	isValid = isValidNumber(Double.class,ampGlobalSetting.getGlobalSettingsValue());
        	break;
        case T_YEAR_DEFAULT_START: case T_YEAR_DEFAULT_END:
        	isValid = isValidNumber(Integer.class,ampGlobalSetting.getGlobalSettingsValue());
        	if (isValid) {
        		int value = Integer.parseInt(ampGlobalSetting.getGlobalSettingsValue()); 
        		isValid = isValid && (value>1900 && value<2099);
        	}
        	break;
        case NULL_VALUE: case "":
        	isValid = true;
        	break;
        default:
    		if (possiblesValues!=null) {
    			for (KeyValue value : possiblesValues) {
    				if (ampGlobalSetting.getGlobalSettingsValue().equals(value.getKey()) ) {
    					isValid = true;
    				}
    			}
    		}
            break;
		}
		return isValid;
	}
	
	/**
	* Does a try catch (Exception) on parsing the given string to the given type
	*
	* @param c the number type. Valid types are (wrappers included): double, int, float, long.
	* @param numString number to check
	* @return false if there is an exception, true otherwise
	*/
	public static boolean isValidNumber(Class c, String numString) {
	  try {
	    if (c == double.class || c == Double.class) {
	      Double.parseDouble(numString);
	    } else if (c == int.class || c == Integer.class) {
	      Integer.parseInt(numString);
	    } else if (c == float.class || c == Float.class) {
	      Float.parseFloat(numString);
	    } else if (c == long.class || c == Long.class) {
	      Long.parseLong(numString);
	    }
	  } catch (Exception ex) {
	    return false;
	  }
	  return true;
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
