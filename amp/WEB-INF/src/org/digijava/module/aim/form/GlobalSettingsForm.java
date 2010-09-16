package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;
public class GlobalSettingsForm extends ActionForm {
	Collection gsfCol = null;
	Collection countryNameCol = null;
	String countryName = null;
	Long gsfId;
	String gsfName = null;
	String gsfValue = null;
	String indexTab = "0";
	
	Long globalId;
	String globalSettingsName = null;
	
	
	Map possibleValues 						= new HashMap(); // The values are of type org.digijava.module.aim.helper.KeyValue
	Map possibleValuesDictionary 			= new HashMap(); // The values are of also of type HashMap (key-value)
	
	Map<String, String> globalSettingsType	= new HashMap<String, String>();
	
	String newSettingName;
	String newTableName;
	
	private String allValues;
	
	public String getAllValues() {
            return allValues;
        }
	public void setAllValues(String allValues) {
            this.allValues = allValues;
        }
	public String getNewSettingName() {
		return newSettingName;
	}
	public Long getGsfId() {
		return gsfId;
	}
	public void setGsfId(Long gsfId) {
		this.gsfId = gsfId;
	}
	public void setNewSettingName(String newSettingName) {
		this.newSettingName = newSettingName;
	}
	public String getNewTableName() {
		return newTableName;
	}
	public void setNewTableName(String newTableName) {
		this.newTableName = newTableName;
	}
	public Long getGlobalId() {
		return globalId; 
	}
	public void setGlobalId(Long gsfId) {
		this.globalId = gsfId;
	}
	public String getGlobalSettingsName() {
		return globalSettingsName;
	}
	public void setGlobalSettingsName(String gsfName) {
		this.globalSettingsName = gsfName;
	}
	public String getGsfValue() {
		return gsfValue;
	}
	public void setGsfValue(String gsfValue) {
		this.gsfValue = gsfValue;
	}
	public Collection getGsfCol() {
		return gsfCol;
	}
	public void setGsfCol(Collection gsfCol) {
		this.gsfCol = gsfCol;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Collection getCountryNameCol() {
		return countryNameCol;
	}
	public void setCountryNameCol(Collection countryNameCol) {
		this.countryNameCol = countryNameCol;
	}

	public void setPossibleValues(String key, Collection values) {
		possibleValues.put(key, values);
	}
	public Collection getPossibleValues(String key) {
		return (Collection) possibleValues.get(key);
	}
	
	public void setPossibleValuesDictionary(String key, Map dictionary) {
		possibleValuesDictionary.put(key, dictionary);
	}
	public Map getPossibleValuesDictionary(String key) {
		return (Map) possibleValuesDictionary.get(key);
	}
	
	public String getGlobalSettingType(String gsName) {
		return globalSettingsType.get(gsName);
	}
	public void setGlobalSettingType(String gsName, String gsType) {
		
		globalSettingsType.put(gsName, gsType);
	}
	public String getIndexTab() {
		return indexTab;
	}
	public void setIndexTab(String indexTab) {
		this.indexTab = indexTab;
	}
}