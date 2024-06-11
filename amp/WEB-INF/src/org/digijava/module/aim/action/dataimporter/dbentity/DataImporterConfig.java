package org.digijava.module.aim.action.dataimporter.dbentity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class DataImporterConfig implements Serializable {
    private Long id;
    private String configName;
    private Set<DataImporterConfigValues> configValues= new HashSet<>();


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

    public Set<DataImporterConfigValues> getConfigValues() {
        return configValues;
    }

    public void setConfigValues(Set<DataImporterConfigValues> configValues)
    {
        this.configValues = configValues;
    }
}
