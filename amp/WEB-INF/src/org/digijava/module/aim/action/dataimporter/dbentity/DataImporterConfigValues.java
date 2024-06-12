package org.digijava.module.aim.action.dataimporter.dbentity;

import java.io.Serializable;

public class DataImporterConfigValues implements Serializable {
    private String configKey;
    private String configValue;
    private DataImporterConfig dataImporterConfig;

    public DataImporterConfig getDataImporterConfig() {
        return dataImporterConfig;
    }

    public void setDataImporterConfig(DataImporterConfig dataImporterConfig) {
        this.dataImporterConfig = dataImporterConfig;
    }

    public DataImporterConfigValues(String configKey, String configValue, DataImporterConfig dataImporterConfig) {
        this.configKey = configKey;
        this.configValue = configValue;
        this.dataImporterConfig = dataImporterConfig;
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
