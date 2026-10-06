package org.digijava.module.aim.form;

public class DynamicDateFilter {
    
    private String currentPeriod;
    private Integer amount;
    private String operator;
    private String xPeriod;
    
    public String getCurrentPeriod() {
        return currentPeriod;
    }
    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getxPeriod() {
        return xPeriod;
    }
    public void setxPeriod(String xPeriod) {
        this.xPeriod = xPeriod;
    }
}
