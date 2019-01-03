/*
 * AmpComponentFunding.java
 * Created : 30-Aug-2005
 */

package org.digijava.module.aim.dbentity;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.REQUIRED_ALWAYS;
import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.COMPONENT_FUNDING_ADJUSTMENT_TYPE;
import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.COMPONENT_FUNDING_AMOUNT;
import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.COMPONENT_FUNDING_CURRENCY;
import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.COMPONENT_FUNDING_DESCRIPTION;
import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.COMPONENT_FUNDING_TRANSACTION_DATE;
import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.COMPONENT_FUNDING_TRANSACTION_TYPE;
import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.COMPONENT_ORGANIZATION;

import java.io.Serializable;
import java.util.Date;

import org.digijava.kernel.ampapi.endpoints.activity.InterchangeDependencyResolver;
import org.digijava.kernel.ampapi.endpoints.activity.values.ComponentTransactionTypePossibleValuesProvider;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpComponentFunding implements Cloneable, Serializable {
    // IATI-check: to be ignored

    private Long ampComponentFundingId;

    @Interchangeable(fieldTitle = COMPONENT_FUNDING_TRANSACTION_TYPE, importable = true, required = REQUIRED_ALWAYS)
    @PossibleValues(ComponentTransactionTypePossibleValuesProvider.class)
    private Integer transactionType;

    @Interchangeable(fieldTitle = COMPONENT_FUNDING_ADJUSTMENT_TYPE, importable = true, pickIdOnly = true, required = REQUIRED_ALWAYS,
            discriminatorOption = CategoryConstants.ADJUSTMENT_TYPE_KEY)
    private AmpCategoryValue adjustmentType;

    @Interchangeable(fieldTitle = COMPONENT_FUNDING_TRANSACTION_DATE, importable = true, required = REQUIRED_ALWAYS)
    private Date transactionDate;

    // @Interchangeable(fieldTitle="Reporting Date")
    private Date reportingDate;

    @Interchangeable(fieldTitle = COMPONENT_FUNDING_AMOUNT, importable = true, required = REQUIRED_ALWAYS)
    private Double transactionAmount;

    @Interchangeable(fieldTitle = COMPONENT_ORGANIZATION, importable = true, pickIdOnly = true,
            dependencies = {InterchangeDependencyResolver.ORGANIZATION_PRESENT_KEY})
    private AmpOrganisation reportingOrganization;

    //@Interchangeable(fieldTitle = COMPONENT_SECOND_REPORTING_ORGANIZATION, importable = true, pickIdOnly = true)
    private AmpOrganisation componentSecondResponsibleOrganization;

    @Interchangeable(fieldTitle = COMPONENT_FUNDING_CURRENCY, importable = true, pickIdOnly = true, required = REQUIRED_ALWAYS)
    private AmpCurrency currency;

    @Interchangeable(fieldTitle = COMPONENT_FUNDING_DESCRIPTION)
    private String description;

    // @Interchangeable(fieldTitle="Component")
    private AmpComponent component;
    // @Interchangeable(fieldTitle="Exchange Rate")
    private Float exchangeRate;

    /**
     * @return Returns the adjustmentType.
     */
    public AmpCategoryValue getAdjustmentType() {
        return adjustmentType;
    }

    /**
     * @param adjustmentType
     *            The adjustmentType to set.
     */
    public void setAdjustmentType(AmpCategoryValue adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    /**
     * @return Returns the ampComponentFundingId.
     */
    public Long getAmpComponentFundingId() {
        return ampComponentFundingId;
    }

    /**
     * @param ampComponentFundingId
     *            The ampComponentFundingId to set.
     */
    public void setAmpComponentFundingId(Long ampComponentFundingId) {
        this.ampComponentFundingId = ampComponentFundingId;
    }

    /**
     * @return Returns the component.
     */
    public AmpComponent getComponent() {
        return component;
    }

    /**
     * @param component
     *            The component to set.
     */
    public void setComponent(AmpComponent component) {
        this.component = component;
    }

    /**
     * @return Returns the currency.
     */
    public AmpCurrency getCurrency() {
        return currency;
    }

    /**
     * @param currency
     *            The currency to set.
     */
    public void setCurrency(AmpCurrency currency) {
        this.currency = currency;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the reportingDate.
     */
    public Date getReportingDate() {
        return reportingDate;
    }

    /**
     * @param reportingDate
     *            The reportingDate to set.
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
     * @param reportingOrganization
     *            The reportingOrganization to set.
     */
    public void setReportingOrganization(AmpOrganisation reportingOrganization) {
        this.reportingOrganization = reportingOrganization;
    }

    /**
     * @return Returns the componentSecondResponsibleOrganization.
     */
    public AmpOrganisation getComponentSecondResponsibleOrganization() {
        return componentSecondResponsibleOrganization;
    }
    /**
     * @param componentSecondResponsibleOrganization The componentSecondResponsibleOrganization to set.
     */
    public void setComponentSecondResponsibleOrganization(AmpOrganisation componentSecondResponsibleOrganization) {
        this.componentSecondResponsibleOrganization = componentSecondResponsibleOrganization;
    }
    /**
     * @return Returns the transactionAmount.
     */
    public Double getTransactionAmount() {
        return FeaturesUtil.applyThousandsForVisibility(transactionAmount);
    }

    /**
     * @param transactionAmount
     *            The transactionAmount to set.
     */
    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = FeaturesUtil.applyThousandsForEntry(transactionAmount);
    }

    /**
     * @return Returns the transactionDate.
     */
    public Date getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate
     *            The transactionDate to set.
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
     * @param transactionType
     *            The transactionType to set.
     */
    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * If overriding is really needed please fully comment on the reason!
     *
     * @Override public int hashCode() { if (this.ampComponentFundingId == null)
     *           return 0; return ampComponentFundingId.intValue(); }
     */

    /*
     * public boolean equals(Object obj) { if (obj == null) throw new
     * NullPointerException(); if (!(obj instanceof AmpComponentFunding)) throw
     * new ClassCastException(); if(this.ampComponentFundingId == null) return
     * super.equals(obj);
     *
     * AmpComponentFunding comp = (AmpComponentFunding) obj; return
     * this.ampComponentFundingId.equals(comp.ampComponentFundingId);
     *
     * }
     */
    public void setExchangeRate(Float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Float getExchangeRate() {
        return exchangeRate;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
}
