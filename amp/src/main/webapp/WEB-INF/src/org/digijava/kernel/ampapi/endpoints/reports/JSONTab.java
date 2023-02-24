package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.translation.util.MultilingualInputFieldValues;

public class JSONTab {
    
    private long id;
    private String name;
    private Boolean visible;
    private Map<String, String> translatedNames = new HashMap<>();
    
    public JSONTab(Long ampReportId, boolean visible) {
        this.id = ampReportId;
        populateNames();
        AmpReports rep = (AmpReports) PersistenceManager.getSession().load(AmpReports.class, id);
        this.name = rep.getName();
        this.visible = visible;
    }
    
    public JSONTab() {}
    
    private void populateNames () {
        translatedNames = new MultilingualInputFieldValues(AmpReports.class, id, "name", null, null).getTranslations();
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Boolean getVisible() {
        return visible;
    }
    
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Map<String, String> getTranslatedNames() {
        return translatedNames;
    }

    public void setTranslatedNames(Map<String, String> translatedNames) {
        this.translatedNames = translatedNames;
    }
}
