package org.digijava.module.aim.helper.donorReport;

import java.util.List;


public class OrgProfileReportHelper {
    private String columnName;
    private List<List<String>> values;
    private List<String> subHeaders;
    private boolean ngoOnlyProperty;
    private boolean allTypeProperty;
    
    public boolean isNgoOnlyProperty() {
        return ngoOnlyProperty;
    }
    public void setNgoOnlyProperty(boolean ngoOnlyProperty) {
        this.ngoOnlyProperty = ngoOnlyProperty;
    }
    public boolean isAllTypeProperty() {
        return allTypeProperty;
    }
    public void setAllTypeProperty(boolean allTypeProperty) {
        this.allTypeProperty = allTypeProperty;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public List<String> getSubHeaders() {
        return subHeaders;
    }
    public void setSubHeaders(List<String> subHeaders) {
        this.subHeaders = subHeaders;
    }
    public List<List<String>> getValues() {
        return values;
    }
    public void setValues(List<List<String>> values) {
        this.values = values;
    }
    
}
