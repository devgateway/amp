package org.digijava.module.aim.action.dataimporter.dbentity;

import java.io.Serializable;

public class DataImporterConfigValues implements Serializable {
    private String configKey;
    private String configValue;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
}
