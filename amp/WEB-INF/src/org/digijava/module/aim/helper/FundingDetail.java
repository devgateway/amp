package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
/**
 * @author jose
 *
 */
public class FundingDetail implements Serializable, Comparable
{
    private Long fundingId;
    private long indexId;
    private boolean checked;
    private int transactionType;
    //private int adjustmentType;
    
    private Date reportingDate;
    private AmpCategoryValue adjustmentTypeName;
    private AmpCategoryValue projectionTypeName;
    private AmpCategoryValue expenditureClass;
    private String transactionDate;
    private String transactionAmount;
    private Long reportingOrganizationId;
    private String reportingOrganizationName;
    private String currencyCode;
    private String currencyName;
    private int index;
    private String classification;
    private boolean useFixedRate;
    private String fixedExchangeRate;
    private Long ampComponentFundingId;
    private AmpOrganisation recipientOrganisation;
    private AmpRole recipientOrganisationRole;
    

    private Long fundDetId;
    private String disbOrderId;
    private IPAContract contract;
    private Boolean disbursementOrderRejected;        
    private Long pledge;
    private AmpCategoryValue pledgename;
    
    private Float capitalPercent;
    
    private AmpOrganisation componentOrganisation;
    private AmpOrganisation componentSecondResponsibleOrganization;
    private String componentTransactionDescription;
       
    private String attachedPledgeName;
    
    //instead of showing a specific date, this funding detail should show 
    //the fiscal year, e.g. 2014/2015
    private String fiscalYear;
    
    public Boolean disasterResponse;

    public FundingDetail() {}

    public FundingDetail(long id) {
        this.indexId = id;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getReportingOrganizationName() {
        return reportingOrganizationName;
    }

    public void setReportingOrganizationName(String reportingOrganizationName) {
        this.reportingOrganizationName = reportingOrganizationName;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public Long getReportingOrganizationId() {
        return reportingOrganizationId;
    }

    public void setReportingOrganizationId(Long reportingOrganizationId) {
        this.reportingOrganizationId = reportingOrganizationId;
    }

    public AmpCategoryValue getAdjustmentTypeName() {
        return adjustmentTypeName;
    }

    public String getAdjustmentTypeNameTrimmed(){
        return adjustmentTypeName.getValue().replaceAll(" ","");
    }

    public void setAdjustmentTypeName(AmpCategoryValue adjustmentTypeName) {
        this.adjustmentTypeName = adjustmentTypeName;

    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * @return Returns the index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index
     *            The index to set.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return Returns the indexId.
     */
    public long getIndexId() {
        return indexId;
    }
    /**
     * @param indexId The indexId to set.
     */
    public void setIndexId(long indexId) {
        this.indexId = indexId;
    }

    public boolean equals(Object obj) {
        if (obj == null) throw new NullPointerException();
        if (!(obj instanceof FundingDetail)) throw new ClassCastException();

        FundingDetail fd = (FundingDetail) obj;
        return (this.indexId == fd.indexId);
    }
    /**
     * @return Returns the classification.
     */
    public String getClassification() {
        return classification;
    }
    /**
     * @param classification The classification to set.
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * @return Returns the fixedExchangeRate.
     */
    public String getFixedExchangeRate() {
        return fixedExchangeRate;
    }

    /**
     * @param fixedExchangeRate The fixedExchangeRate to set.
     */
    public void setFixedExchangeRate(String fixedExchangeRate) {
        this.fixedExchangeRate = fixedExchangeRate;
    }

    /**
     * @return Returns the useFixedRate.
     */
    public boolean isUseFixedRate() {
        return useFixedRate;
    }

    /**
     * @param useFixedRate The useFixedRate to set.
     */
    public void setUseFixedRate(boolean useFixedRate) {
        if(useFixedRate==false){
        setFixedExchangeRate(null);
        }
        this.useFixedRate = useFixedRate;
    }

    public Long getAmpComponentFundingId() {
        return ampComponentFundingId;
    }

    public Long getFundDetId() {
        return fundDetId;
    }

    public String getDisbOrderId() {
        return disbOrderId;
    }

    public void setAmpComponentFundingId(Long ampComponentFundingId) {
        this.ampComponentFundingId = ampComponentFundingId;
    }

    public void setFundDetId(Long fundDetId) {
        this.fundDetId = fundDetId;
    }

    public void setDisbOrderId(String disbOrderId) {
        this.disbOrderId = disbOrderId;
    }

    public String getFormattedRate() {
        String returnValue = null;
        if (getFixedExchangeRate() != null) {
            DecimalFormat decFor = new DecimalFormat();
            BigDecimal fixedExchangeRate = new BigDecimal(
                    getFixedExchangeRate().replace(",", "."));
            returnValue = decFor.format(fixedExchangeRate);
        }
        return returnValue;
    }

    public Date getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(Date reportingDate) {
        this.reportingDate = reportingDate;
    }

    public void setRecipientOrganisation(AmpOrganisation recipientOrganisation) {
        this.recipientOrganisation = recipientOrganisation;
    }

    public void setRecipientOrganisationRole(AmpRole recipientRole) {
        this.recipientOrganisationRole = recipientRole;
    }

    public AmpOrganisation getRecipientOrganisation() {
        return recipientOrganisation;
    }

    public AmpRole getRecipientOrganisationRole() {
        return recipientOrganisationRole;
    }

    public void setAttachedPledgeName(String attachedPledgeName){
        this.attachedPledgeName = attachedPledgeName;
    }
    
    public String getAttachedPledgeName(){
        return this.attachedPledgeName;
    }
    
    public void setComponentOrganisation(AmpOrganisation compOrganisation) {
        this.componentOrganisation = compOrganisation;
    }

    public AmpOrganisation getComponentOrganisation() {
        return this.componentOrganisation;
    }

    public void setComponentSecondResponsibleOrganization(AmpOrganisation componentSecondResponsibleOrganization) {
        this.componentSecondResponsibleOrganization = componentSecondResponsibleOrganization;
    }

    public AmpOrganisation getComponentSecondResponsibleOrganization() {
        return this.componentSecondResponsibleOrganization;
    }

    public void setComponentTransactionDescription(String ctDescription) {
        this.componentTransactionDescription = ctDescription;
    }

    public String getComponentTransactionDescription() {
        return this.componentTransactionDescription;
    }

    public Float getCapitalPercent() {
        return this.capitalPercent;
    }

    public void setCapitalPercent(Float capitalPercent) {
        this.capitalPercent = capitalPercent;
    }

    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        FundingDetail obj = (FundingDetail) o;
        return this.getFundingId().compareTo(obj.getFundingId());
    }

    public Long getFundingId() {
        return fundingId;
    }

    public void setFundingId(Long fundingId) {
        this.fundingId = fundingId;
    }

    public void setDisbursementOrderRejected(Boolean disbursementOrderRejected) {
        this.disbursementOrderRejected = disbursementOrderRejected;
    }

    public Boolean getDisbursementOrderRejected() {
        return disbursementOrderRejected;
    }

    @Override
    public String toString() {
        return String.format("%s %s at %s (type = %d)", this.transactionAmount,
                this.currencyCode, this.transactionDate, this.transactionType);
    }

    public AmpCategoryValue getPledgename() {
        if (this.pledge != null && !this.pledge.equals(0L)) {
            FundingPledges pledge = PledgesEntityHelper
                    .getPledgesById(this.pledge);
            return pledge.getTitle();
        }
        return pledgename;
    }

    public void setPledgename(AmpCategoryValue pledgename) {
        this.pledgename = pledgename;
    }

    public Long getPledge() {
        if (pledge != null) {
            return pledge;
        } else {
            return 0L;
        }
    }

    public void setPledge(Long pledge) {
        this.pledge = pledge;
    }

    public IPAContract getContract() {
        return contract;
    }

    public void setContract(IPAContract contract) {
        this.contract = contract;
    }
    
    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
    
    public AmpCategoryValue getProjectionTypeName() {
        return projectionTypeName;
    }

    public void setProjectionTypeName(AmpCategoryValue projectionTypeName) {
        this.projectionTypeName = projectionTypeName;
    }
    public Boolean getDisasterResponse() {
        return disasterResponse;
    }

    public void setDisasterResponse(Boolean disasterResponse) {
        this.disasterResponse = disasterResponse;
    }

    public AmpCategoryValue getExpenditureClass() {
        return expenditureClass;
    }

    public void setExpenditureClass(AmpCategoryValue expenditureClass) {
        this.expenditureClass = expenditureClass;
    }
 
}
