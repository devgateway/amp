package org.digijava.module.contentrepository.helper.template;

public class PossibleValueHelper {
    private Long dbId;
    private String tempId;
    private String preDefinedValue;
    
    public PossibleValueHelper(){
        
    }
    
    public PossibleValueHelper(String tempId){
        this.tempId=tempId;
    }
    
    public String getTempId() {
        return tempId;
    }
    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getPreDefinedValue() {
        return preDefinedValue;
    }

    public void setPreDefinedValue(String preDefinedValue) {
        this.preDefinedValue = preDefinedValue;
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }
    
}
