package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.admin.helper.FieldInfo;

import java.util.*;

public class DataImporterForm extends ActionForm {
    List<FieldInfo> fieldInfos =new ArrayList<>();
    private FormFile dataFile;

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
