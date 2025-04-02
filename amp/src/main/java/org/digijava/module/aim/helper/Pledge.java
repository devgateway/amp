package org.digijava.module.aim.helper;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class Pledge {
    private long indexId;
    private long dbId;
    private String date;
    private String amount;
    private String currencyCode;
    private AmpCategoryValue  adjustmentType;
    
    private String program;
    public AmpCategoryValue getAdjustmentType() {
        return adjustmentType;
    }
    public void setAdjustmentType(AmpCategoryValue adjustmentType) {
        this.adjustmentType = adjustmentType;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getCurrencyCode() {
        return currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public long getIndexId() {
        return indexId;
    }
    public void setIndexId(long indexId) {
        this.indexId = indexId;
    }
    public String getProgram() {
        return program;
    }
    public void setProgram(String program) {
        this.program = program;
    }
    public long getDbId() {
        return dbId;
    }
    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

}
