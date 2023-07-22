/*
 * AmpRegionalFunding.java
 * Created : 30-Aug-2005 
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.digijava.kernel.ampapi.endpoints.common.CommonFieldsConstants;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.RegionPossibleValuesProvider;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AMP_REGIONAL_FUNDING")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpRegionalFunding implements Versionable, Serializable, Cloneable {
    //IATI-check: to be ignored

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_REGIONAL_FUNDING_seq")
    @SequenceGenerator(name = "AMP_REGIONAL_FUNDING_seq", sequenceName = "AMP_REGIONAL_FUNDING_seq", allocationSize = 1)
    @Column(name = "amp_regional_funding_id")
    @InterchangeableId
    @Interchangeable(fieldTitle = "ID")
    private Long ampRegionalFundingId;

    @Column(name = "transaction_type")
    private Integer transactionType;

    @Column(name = "transaction_date")
    @Interchangeable(fieldTitle = "Transaction Date", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private Date transactionDate;

    @Column(name = "reporting_date")
    private Date reportingDate;

    @Column(name = "transaction_amount")

    @Interchangeable(fieldTitle = "Transaction Amount", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private Double transactionAmount;

    @Column(name = "exp_category")
    private String expenditureCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", referencedColumnName = "activity_id")
    @InterchangeableBackReference

    private AmpActivityVersion activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rep_organization_id", referencedColumnName = "organization_id")
    private AmpOrganisation reportingOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", referencedColumnName = "currency_id")
    @Interchangeable(fieldTitle = "Currency", importable = true, pickIdOnly = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class),
            commonPV = CommonFieldsConstants.COMMON_CURRENCY)
    private AmpCurrency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @PossibleValues(RegionPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = ActivityFieldsConstants.RegionalFunding.LOCATION, pickIdOnly = true,
            commonPV = CommonFieldsConstants.COMMON_REGION, importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @JoinColumn(name = "region_location_id", referencedColumnName = "location_id", nullable = false)
    private AmpCategoryValueLocations regionLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_type", referencedColumnName = "category_value_id")
    @Interchangeable(fieldTitle = "Adjustment Type", importable = true, pickIdOnly = true,
            discriminatorOption = CategoryConstants.ADJUSTMENT_TYPE_KEY,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private AmpCategoryValue adjustmentType;



    /**
     * @return Returns the activity.
     */
    public AmpActivityVersion getActivity() {
        return activity;
    }
    /**
     * @param activity The activity to set.
     */
    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }
    /**
     * @return Returns the adjustmentType.
     */
    public AmpCategoryValue getAdjustmentType() {
        return adjustmentType;
    }
    /**
     * @param adjustmentType The adjustmentType to set.
     */
    public void setAdjustmentType(AmpCategoryValue adjustmentType) {
        this.adjustmentType = adjustmentType;
    }
    /**
     * @return Returns the ampRegionalFundingId.
     */
    public Long getAmpRegionalFundingId() {
        return ampRegionalFundingId;
    }
    /**
     * @param ampRegionalFundingId The ampRegionalFundingId to set.
     */
    public void setAmpRegionalFundingId(Long ampRegionalFundingId) {
        this.ampRegionalFundingId = ampRegionalFundingId;
    }
    /**
     * @return Returns the currency.
     */
    public AmpCurrency getCurrency() {
        return currency;
    }
    /**
     * @param currency The currency to set.
     */
    public void setCurrency(AmpCurrency currency) {
        this.currency = currency;
    }
    /**
     * @return Returns the expenditureCategory.
     */
    public String getExpenditureCategory() {
        return expenditureCategory;
    }
    /**
     * @param expenditureCategory The expenditureCategory to set.
     */
    public void setExpenditureCategory(String expenditureCategory) {
        this.expenditureCategory = expenditureCategory;
    }
    
    /**
     * @return the regionLocation
     */
    public AmpCategoryValueLocations getRegionLocation() {
        return regionLocation;
    }
    /**
     * @param regionLocation the regionLocation to set
     */
    public void setRegionLocation(AmpCategoryValueLocations regionLocation) {
        this.regionLocation = regionLocation;
    }
    /**
     * @return Returns the reportingDate.
     */
    public Date getReportingDate() {
        return reportingDate;
    }
    /**
     * @param reportingDate The reportingDate to set.
     */
    public void setReportingDate(Date reportingDate) {
        this.reportingDate = reportingDate;
    }
    /**
     * @return Returns the reportingOrganization.
     */
    public AmpOrganisation getReportingOrganization() {
        return reportingOrganization;
    }
    /**
     * @param reportingOrganization The reportingOrganization to set.
     */
    public void setReportingOrganization(AmpOrganisation reportingOrganization) {
        this.reportingOrganization = reportingOrganization;
    }

    /**
     * @return Returns the transactionAmount.
     */
    public Double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param transactionAmount The transactionAmount to set.
     */
    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * Return transaction amount. Amount is converted for visibility.
     * @return Returns the transactionAmount.
     */
    public Double getTransactionAmountWithFormatConversion() {
        return FeaturesUtil.applyThousandsForVisibility(transactionAmount);
    }

    /**
     * Used from Wicket.
     * Set transaction amount. Amount is converted from visibility to storage format.
     * @param transactionAmount The transactionAmount to set.
     */
    public void setTransactionAmountWithFormatConversion(Double transactionAmount) {
        this.transactionAmount = FeaturesUtil.applyThousandsForEntry(transactionAmount);
    }

    /**
     * @return Returns the transactionDate.
     */
    public Date getTransactionDate() {
        return transactionDate;
    }
    /**
     * @param transactionDate The transactionDate to set.
     */
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    /**
     * @return Returns the transactionType.
     */
    public Integer getTransactionType() {
        return transactionType;
    }
    /**
     * @param transactionType The transactionType to set.
     */
    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }
    
    public boolean equals(Object arg) {
        if (arg instanceof AmpRegionalFunding) {
            AmpRegionalFunding regFund = (AmpRegionalFunding) arg;
            return ampRegionalFundingId.equals(regFund.getAmpRegionalFundingId());  
        }
        throw new ClassCastException();
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpRegionalFunding aux = (AmpRegionalFunding) obj;
        String original = " " + this.regionLocation + this.currency + this.transactionType
                + this.transactionAmount.longValue() + this.transactionDate + this.adjustmentType;
        String copy = " " + aux.regionLocation + aux.currency + aux.transactionType + aux.transactionAmount.longValue()
                + aux.transactionDate + aux.adjustmentType;
        if (original.equals(copy)) {
            return true;
        }
        return false;
    }
    
    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(
                new Output(null, new String[] { "Region" }, new Object[] { this.regionLocation.getName() }));
        String transactionType = "";
        switch (this.transactionType.intValue()) {
        case 0:
            transactionType = "Commitments";

            break;
        case 1:
            transactionType = "Disbursements";
            break;
        case 2:
            transactionType = "Expenditures";
            break;
        case 3:
            transactionType = "Disbursement Orders";
            break;
        case 4:
            transactionType = "MTEF Projection";
            break;
        }
        out.getOutputs().add(new Output(null, new String[] { "Trn" }, new Object[] { transactionType }));
        out.getOutputs().add(
                new Output(null, new String[] { "Value" }, new Object[] {
                        " " + this.adjustmentType.getValue() + " - ", this.transactionAmount,
                        " ", this.currency, " - ", this.transactionDate }));
        return out;
    }
    
    @Override
    public Object getValue() {
        return "" + this.transactionType + this.transactionDate + this.transactionAmount + this.reportingDate
                + this.currency + this.expenditureCategory + this.adjustmentType + this.reportingOrganization;
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpRegionalFunding aux = (AmpRegionalFunding) clone();
        aux.activity = newActivity;
        aux.ampRegionalFundingId = null;
        return aux;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
    
}
