package org.digijava.module.aim.action.dataimporter.dbentity;

import java.io.Serializable;

public class DataImporterConfigValues implements Serializable {
    private String configKey;
    private String configValue;

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
