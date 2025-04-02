package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

public class AmpFundingMTEFProjection implements Cloneable, Serializable, Comparable<AmpFundingMTEFProjection>,
        FundingInformationItem {

    private static final long serialVersionUID = -1583797313318079006L;

    @InterchangeableId
    @Interchangeable(fieldTitle = "Transaction ID")
    private Long ampFundingMTEFProjectionId;

    @Interchangeable(fieldTitle = "Projection", pickIdOnly = true, importable = true, 
            discriminatorOption = CategoryConstants.MTEF_PROJECTION_KEY,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private AmpCategoryValue projection;

    @Interchangeable(fieldTitle = "Amount", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private Double amount;

    @Interchangeable(fieldTitle = "Currency", pickIdOnly = true, importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private AmpCurrency ampCurrency;

    @Interchangeable(fieldTitle = "Projection Date", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private Date projectionDate;

    @InterchangeableBackReference
    private AmpFunding ampFunding;

    private Date reportingDate;

    private Date updatedDate;
    
    private AmpOrganisation recipientOrg;
    
    private AmpRole recipientRole;
    
    private Long checkSum;
    
    public static class FundingMTEFProjectionComparator implements Comparator<AmpFundingMTEFProjection>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpFundingMTEFProjection arg0, AmpFundingMTEFProjection arg1) {
            if (arg0.getReportingDate() != null && arg1.getReportingDate() != null) {
                return arg0.getReportingDate().compareTo(arg1.getReportingDate());
            } else if (arg0.getAmpFundingMTEFProjectionId() != null && arg1.getAmpFundingMTEFProjectionId() != null) {
                return arg0.getAmpFundingMTEFProjectionId().compareTo(arg1.getAmpFundingMTEFProjectionId());
            } else if (arg0.getAmpFundingMTEFProjectionId() != null && arg1.getAmpFundingMTEFProjectionId() == null) {
                return -1;
            } else if (arg0.getAmpFundingMTEFProjectionId() == null && arg1.getAmpFundingMTEFProjectionId() != null) {
                return 1;
            }
            
            return arg0.hashCode() - arg1.hashCode();
        }
    }

    public AmpFundingMTEFProjection() {
    }

    public AmpFundingMTEFProjection(Integer transactionType, AmpCategoryValue adjustmentType, Double transactionAmount,
            Date transactionDate, AmpCurrency ampCurrencyId, Double fixedExchangeRate) {
        this.amount = transactionAmount;
        this.projectionDate = transactionDate;
        this.ampCurrency = ampCurrencyId;
        this.projection = adjustmentType;
    }

    public Date getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(Date reportingDate) {
        this.reportingDate = reportingDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getDisplayedAmount() {
        return FeaturesUtil.applyThousandsForVisibility(getAmount());
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDisplayedAmount(Double displayedAmount) {
        setAmount(FeaturesUtil.applyThousandsForEntry(displayedAmount));
    }

    public AmpFunding getAmpFunding() {
        return ampFunding;
    }

    public void setAmpFunding(AmpFunding ampFundingId) {
        this.ampFunding = ampFundingId;
    }

    public Long getAmpFundingMTEFProjectionId() {
        return ampFundingMTEFProjectionId;
    }

    public void setAmpFundingMTEFProjectionId(Long ampFundingMTEFProjectionId) {
        this.ampFundingMTEFProjectionId = ampFundingMTEFProjectionId;
    }

    public AmpCurrency getAmpCurrency() {
        return ampCurrency;
    }

    public void setAmpCurrency(AmpCurrency currency) {
        this.ampCurrency = currency;
    }

    public AmpCategoryValue getProjection() {
        return projection;
    }

    public void setProjection(AmpCategoryValue projection) {
        this.projection = projection;
    }

    public Date getProjectionDate() {
        return projectionDate;
    }

    public void setProjectionDate(Date projectionDate) {
        this.projectionDate = projectionDate;
    }

    @Override
    public Long getDbId() {
        return getAmpFundingMTEFProjectionId();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(AmpFundingMTEFProjection o) {
        return o.getAmpFundingMTEFProjectionId().compareTo(getAmpFundingMTEFProjectionId());
    }

    public Double getTransactionAmount() {
        return this.getAmount();
    }

    public Double getAbsoluteTransactionAmount() {
        return this.getAmount();
    }

    public AmpCurrency getAmpCurrencyId() {
        return this.getAmpCurrency();
    }

    public Date getTransactionDate() {
        return this.getProjectionDate();
    }

    public AmpOrganisation getRecipientOrg() {
        return recipientOrg;
    }

    public AmpRole getRecipientRole() {
        return recipientRole;
    }

    public void setRecipientOrg(AmpOrganisation recipientOrg) {
        this.recipientOrg = recipientOrg;
    }

    public void setRecipientRole(AmpRole recipientRole) {
        this.recipientRole = recipientRole;
    }

    public Integer getTransactionType() {
        return Constants.MTEFPROJECTION;
    }

    public AmpCategoryValue getAdjustmentType() {
        return projection == null ? CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB() : projection;
    }

    public String getDisbOrderId() {
        return null;
    }

    public Double getFixedExchangeRate() {
        return null;
    }

    public IPAContract getContract() {
        return null;
    }

    public String getExpCategory() {
        return null;
    }

    public FundingPledges getPledgeid() {
        return null;
    }

    public Float getCapitalSpendingPercentage() {
        return null;
    }

    @Override
    public String toString() {
        String currency = this.getAmpCurrencyId() == null ? "NOCUR" : this.getAmpCurrencyId().getCurrencyCode();
        String recipient = this.getRecipientOrg() == null ? "NOORG" : this.getRecipientOrg().getName();
        String trTypeName = "NOTRTYPE";
        switch (getTransactionType().intValue()) {
            case 0:
                trTypeName = "Commitment";
                break;

            case 1:
                trTypeName = "Disbursement";
                break;

            case 2:
                trTypeName = "Expenditure";
                break;

            case 3:
                trTypeName = "MTEF Projection";
                break;

            default:
                trTypeName = String.format("trType %d", getTransactionType());
                break;
        }

        String transText = (this.getAdjustmentType() == null ? "NOADJUST" : this.getAdjustmentType().getLabel()) + " "
                + trTypeName;

        return String.format("%s %s %s to %s", transText, this.getAbsoluteTransactionAmount(), currency, recipient);
    }

    @Override
    public void setTransactionAmount(Double transactionAmount) {
        this.setAmount(transactionAmount);

    }

    @Override
    public Boolean getDisasterResponse() {
        return null;
    }

    public String getDisbursementId() {
        return null;
    }

    @Override
    public AmpCategoryValue getExpenditureClass() {
        return null;
    }

    public Long getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Long checkSum) {
        this.checkSum = checkSum;
    }

}
