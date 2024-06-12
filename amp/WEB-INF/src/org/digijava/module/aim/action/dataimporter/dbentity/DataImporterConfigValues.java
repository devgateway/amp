package org.digijava.module.aim.action.dataimporter.dbentity;

import java.io.Serializable;

public class DataImporterConfigValues implements Serializable {
    private String configKey;
    private String configValue;

    public DataImporterConfigValues(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
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

    @Override
    public String toString() {
        return "DataImporterConfigValues{" +
                "configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                '}';
    }
}
