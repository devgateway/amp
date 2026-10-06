package org.digijava.module.contentrepository.helper.template;

import java.util.List;

public class TemplateFieldHelper {
    private String fieldTemporaryId;
    private String fieldType;
    //private String value; //in case it's single value field
    private List<PossibleValueHelper> values; //hold pre-defined possible values
    private Boolean hasAddMorePreDefinedValueRights;
    private Integer ordinalNumber;
    private Long dbId;
    
    public String getFieldTemporaryId() {
        return fieldTemporaryId;
    }
    public void setFieldTemporaryId(String fieldTemporaryId) {
        this.fieldTemporaryId = fieldTemporaryId;
    }
    public List<PossibleValueHelper> getValues() {
        return values;
    }
    public void setValues(List<PossibleValueHelper> values) {
        this.values = values;
    }
    public String getFieldType() {
        return fieldType;
    }
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    public Boolean getHasAddMorePreDefinedValueRights() {
        return hasAddMorePreDefinedValueRights;
    }
    public void setHasAddMorePreDefinedValueRights(
            Boolean hasAddMorePreDefinedValueRights) {
        this.hasAddMorePreDefinedValueRights = hasAddMorePreDefinedValueRights;
    }   
    public Long getDbId() {
        return dbId;
    }
    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }
    public Integer getOrdinalNumber() {
        return ordinalNumber;
    }
    public void setOrdinalNumber(Integer ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }   
    
}
