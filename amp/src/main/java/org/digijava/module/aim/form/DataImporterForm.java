package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.form.helpers.FieldInfo;

import java.util.*;

public class DataImporterForm extends ActionForm {
    List<FieldInfo> fieldInfos =new ArrayList<>();
    private FormFile dataFile;


    private boolean internal;
    private boolean skipExisting;
    private boolean skipRecordsWithoutTransactions;
    private boolean createMissingOrgs;
    private boolean createMissingSectors;
    private boolean createMissingOrgGroups;
    private boolean createMissingPrograms;
    private Long orgGroupId;
    private Long defaultActivityStatusId;
    private Long defaultLocationId;
    private String defaultProgramClassification;
    private boolean validateActivities;
    private boolean addDisbursementForCommitment;

    public boolean isSkipExisting() {
        return skipExisting;
    }

    public void setSkipExisting(boolean skipExisting) {
        this.skipExisting = skipExisting;
    }

    public boolean isSkipRecordsWithoutTransactions() {
        return skipRecordsWithoutTransactions;
    }

    public void setSkipRecordsWithoutTransactions(boolean skipRecordsWithoutTransactions) {
        this.skipRecordsWithoutTransactions = skipRecordsWithoutTransactions;
    }

    public boolean isValidateActivities() {
        return validateActivities;
    }

    public void setValidateActivities(boolean validateActivities) {
        this.validateActivities = validateActivities;
    }

    public boolean isAddDisbursementForCommitment() {
        return addDisbursementForCommitment;
    }

    public void setAddDisbursementForCommitment(boolean addDisbursementForCommitment) {
        this.addDisbursementForCommitment = addDisbursementForCommitment;
    }

    public boolean isCreateMissingOrgs() {
        return createMissingOrgs;
    }

    public void setCreateMissingOrgs(boolean createMissingOrgs) {
        this.createMissingOrgs = createMissingOrgs;
    }

    public boolean isCreateMissingSectors() {
        return createMissingSectors;
    }

    public void setCreateMissingSectors(boolean createMissingSectors) {
        this.createMissingSectors = createMissingSectors;
    }

    public boolean isCreateMissingOrgGroups() {
        return createMissingOrgGroups;
    }

    public void setCreateMissingOrgGroups(boolean createMissingOrgGroups) {
        this.createMissingOrgGroups = createMissingOrgGroups;
    }

    public boolean isCreateMissingPrograms() {
        return createMissingPrograms;
    }

    public void setCreateMissingPrograms(boolean createMissingPrograms) {
        this.createMissingPrograms = createMissingPrograms;
    }

    public Long getOrgGroupId() {
        return orgGroupId;
    }

    public void setOrgGroupId(Long orgGroupId) {
        this.orgGroupId = orgGroupId;
    }

    public Long getDefaultActivityStatusId() {
        return defaultActivityStatusId;
    }

    public void setDefaultActivityStatusId(Long defaultActivityStatusId) {
        this.defaultActivityStatusId = defaultActivityStatusId;
    }

    public Long getDefaultLocationId() {
        return defaultLocationId;
    }

    public void setDefaultLocationId(Long defaultLocationId) {
        this.defaultLocationId = defaultLocationId;
    }

    public String getDefaultProgramClassification() {
        return defaultProgramClassification;
    }

    public void setDefaultProgramClassification(String defaultProgramClassification) {
        this.defaultProgramClassification = defaultProgramClassification;
    }

    public Set<String> getFileHeaders() {
        return fileHeaders;
    }

    public void setFileHeaders(Set<String> fileHeaders) {
        this.fileHeaders = fileHeaders;
    }

    public Set<String> fileHeaders= new HashSet<>();

    public FormFile getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(FormFile templateFile) {
        this.templateFile = templateFile;
    }

    private FormFile templateFile;
    private Map<String,String> columnPairs= new HashMap<>();

    public Map<String, String> getColumnPairs() {
        return columnPairs;
    }

    public void setColumnPairs(Map<String, String> columnPairs) {
        this.columnPairs = columnPairs;
    }


    public FormFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(FormFile dataFile) {
        this.dataFile = dataFile;
    }

    public List<DataInfo> getDataInfos() {
        return dataInfos;
    }

    public void setDataInfos(List<DataInfo> dataInfos) {
        this.dataInfos = dataInfos;
    }

    List<DataInfo> dataInfos = new ArrayList<>();

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }
    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }
    @Override
    public String toString() {
        return "DataImporterForm{" +
                "fieldInfos=" + fieldInfos +
                ", dataFile=" + dataFile +
                ", dataInfos=" + dataInfos +
                '}';
    }

    public static class DataInfo {
        private String fieldName;
        private String columnName;

        public DataInfo(String fieldName, String columnName) {
            this.fieldName = fieldName;
            this.columnName = columnName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        @Override
        public String toString() {
            return "DataInfo{" +
                    "fieldName='" + fieldName + '\'' +
                    ", columnName='" + columnName + '\'' +
                    '}';
        }
    }
}
