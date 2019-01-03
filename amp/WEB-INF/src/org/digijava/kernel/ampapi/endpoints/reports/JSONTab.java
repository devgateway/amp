package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.translation.util.MultilingualInputFieldValues;

public class JSONTab {
    private long id;
    private String name;
    private Boolean visible;
    private List<JsonBean> translatedNames = new ArrayList <JsonBean> ();
    
    public JSONTab(Long ampReportId, boolean visible) {
        this.id = ampReportId;
        populateNames();
        AmpReports rep = (AmpReports) PersistenceManager.getSession().load(AmpReports.class, id);
        this.name = rep.getName();
        this.visible = visible;
    }
    
    public JSONTab() {}
    
    private void populateNames () {
        MultilingualInputFieldValues inputValues = new MultilingualInputFieldValues(AmpReports.class, id, "name", null, null);
        for (String language: inputValues.getTranslations().keySet()) {
            JsonBean translatedName = new JsonBean();
            translatedName.set(language, inputValues.getTranslations().get(language));
            translatedNames.add(translatedName);
        }
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

    public List<JsonBean> getTranslatedNames() {
        return translatedNames;
    }

    public void setTranslatedNames(List<JsonBean> translatedNames) {
        this.translatedNames = translatedNames;
    }
}
