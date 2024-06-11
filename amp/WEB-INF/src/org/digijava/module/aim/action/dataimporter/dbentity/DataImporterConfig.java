package org.digijava.module.aim.action.dataimporter.dbentity;

import java.io.Serializable;
import java.util.Map;

public class DataImporterConfig implements Serializable {
    private Long id;
    private String configName;
    private Map<String, String> configValues;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Map<String, String> getConfigValues() {
        return configValues;
    }

    public void setConfigValues(Map<String, String> configValues) {
        this.configValues = configValues;
    }
}
