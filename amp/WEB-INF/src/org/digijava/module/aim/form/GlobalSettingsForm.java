package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.KeyValue;
public class GlobalSettingsForm extends ActionForm {
    Collection<AmpGlobalSettings> gsfCol = null;
    Collection countryNameCol = null;
    String countryName = null;
    Long gsfId;
    String gsfName = null;
    String gsfValue = null;
    String indexTab = "0";
    
    Long globalId;
    String globalSettingsName = null;
    
    
    Map<String, Collection<KeyValue>> possibleValues    = new HashMap<String, Collection<KeyValue>>();
    Map<String, Map<String, String>> possibleValuesDictionary = new HashMap<String, Map<String, String>>(); // The values are of also of type HashMap (key-value)
    
    Map<String, String> globalSettingsType  = new HashMap<String, String>();
    
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
    
    public Collection<AmpGlobalSettings> getGsfCol() {
        return Collections.unmodifiableCollection(gsfCol);
    }
    
    public void setGsfCol(Collection<AmpGlobalSettings> gsfCol) {
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

    public void setPossibleValues(String key, Collection<KeyValue> values) {
        possibleValues.put(key, values);
    }
    
    public Collection<KeyValue> getPossibleValues(String key) {
        return possibleValues.get(key);
    }
    
    public void setPossibleValuesDictionary(String key, Map<String, String> dictionary) {
        possibleValuesDictionary.put(key, dictionary);
    } 
    
    public Map<String, String> getPossibleValuesDictionary(String key) {
        return possibleValuesDictionary.get(key);
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
    
    /**
     * returns the value of a GS by its given name
     * @param name
     * @return
     */
    public String getGsValue(String name) {
        for(AmpGlobalSettings ags:this.gsfCol) {
            if (name.equals(ags.getGlobalSettingsName()))
                return ags.getGlobalSettingsValue();
        }
        throw new RuntimeException(String.format("global settings with name '%s' not found", name));
    }
    
    /**
     * called JSP-side
     * @return
     */
    public long getRelevantInflationRateEntries() {
        long res = PersistenceManager.getLong(PersistenceManager.getSession().createQuery("SELECT count(*) from " + AmpInflationRate.class.getName()).uniqueResult());
        //boolean z = res > 0;
        return res;
    }
}
