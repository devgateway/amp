package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.admin.helper.FieldInfo;

import java.util.ArrayList;
import java.util.List;

public class DataImporterForm extends ActionForm {
    List<FieldInfo> fieldInfos =new ArrayList<>();

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

    private class DataInfo {
        private String fieldName;
        private String columnName;

        DataInfo(String fieldName, String columnName) {
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
    }
}
