/*
 * Components.java
 */

package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;


public class Components<T> implements Comparable , Serializable{
    
    private Long componentId;
    private String title;
    private String description;
    private String amount;
    private Collection<T> commitments;
    private Collection<T> disbursements;
    private Collection<T> expenditures;
    private String reportingDate;   
    private String currencyCode;
    private String code;
    private String url;
    private Long type_Id;

    // Used for display purposes only
    private String typeName;
    
    private SortedMap<Integer,Map<String,Double>> financeByYearInfo;
    
    public Components() {}
    
    public Components(Long id) {
        componentId = id;
    }
    
    /**
     * @return Returns the amount.
     */
    public String getAmount() {
        return amount;
    }
    /**
     * @param amount The amount to set.
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }
    /**
     * @return Returns the componentId.
     */
    public Long getComponentId() {
        return componentId;
    }
    /**
     * @param componentId The componentId to set.
     */
    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * @return Returns the reportingDate.
     */
    public String getReportingDate() {
        return reportingDate;
    }
    /**
     * @param reportingDate The reportingDate to set.
     */
    public void setReportingDate(String reportingDate) {
        this.reportingDate = reportingDate;
    }
    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    public int compareTo(Object obj) {
        if (obj == null) throw new NullPointerException();
        if (!(obj instanceof Components)) throw new ClassCastException();
        
        Components comp = (Components) obj;
        return comp.getComponentId().compareTo(this.componentId);
    }
    /**
     * @return Returns the currencyCode.
     */
    public String getCurrencyCode() {
        return currencyCode;
    }
    /**
     * @param currencyCode The currencyCode to set.
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

        /**
     * @return Returns the commitments.
     */
    public Collection<T> getCommitments() {
        return commitments;
    }

    /**
     * @param commitments The commitments to set.
     */
    public void setCommitments(Collection<T> commitments) {
        this.commitments = commitments;
    }

        /**
     * @return Returns the disbursements.
     */
    public Collection<T> getDisbursements() {
        return disbursements;
    }

    /**
     * @param disbursements The disbursements to set.
     */
    public void setDisbursements(Collection<T> disbursements) {
        this.disbursements = disbursements;
    }

        /**
     * @return Returns the expenditures.
     */
    public Collection<T> getExpenditures() {
        return expenditures;
    }

    /**
     * @param expenditures The expenditures to set.
     */
    public void setExpenditures(Collection<T> expenditures) {
        this.expenditures = expenditures;
    }

    public boolean equals(Object obj) {
        if (obj == null) throw new NullPointerException();
        if (!(obj instanceof Components)) throw new ClassCastException();
        
        Components temp = (Components) obj;
        if (this.componentId == null) return false;
        if (temp.componentId == null) return false;
        return this.componentId.equals(temp.componentId);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setFinanceByYearInfo(SortedMap<Integer,Map<String,Double>> financeByYearInfo) {
        this.financeByYearInfo = financeByYearInfo;
    }

    public SortedMap<Integer,Map<String,Double>> getFinanceByYearInfo() {
        return financeByYearInfo;
    }

    public Long getType_Id() {
        return type_Id;
    }

    public void setType_Id(Long type_Id) {
        this.type_Id = type_Id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
