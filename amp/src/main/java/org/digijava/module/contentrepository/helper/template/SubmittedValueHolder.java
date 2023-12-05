package org.digijava.module.contentrepository.helper.template;

public class SubmittedValueHolder {
    private Integer fieldOrdinalValue;
    private String submittedValue;
    private boolean needsNewParagraph; //whether there should be an empty line before this entity
    
    public SubmittedValueHolder(Integer fieldOrdNum, String value){
        this.fieldOrdinalValue=fieldOrdNum;
        this.submittedValue=value;
    }
    
    public SubmittedValueHolder(){
        
    }

    public Integer getFieldOrdinalValue() {
        return fieldOrdinalValue;
    }
    public void setFieldOrdinalValue(Integer fieldOrdinalValue) {
        this.fieldOrdinalValue = fieldOrdinalValue;
    }
    public String getSubmittedValue() {
        return submittedValue;
    }
    public void setSubmittedValue(String submittedValue) {
        this.submittedValue = submittedValue;
    }

    public boolean isNeedsNewParagraph() {
        return needsNewParagraph;
    }

    public void setNeedsNewParagraph(boolean needsNewParagraph) {
        this.needsNewParagraph = needsNewParagraph;
    }
    
}
