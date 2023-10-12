/*
 * AmpComponentFunding.java
 * Created : 30-Aug-2005
 */

package org.digijava.module.aim.dbentity;

import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.validators.activity.ComponentFundingOrgRoleValidator;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.activityversioning.VersionableCollection;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.*;

public class AmpComponentFunding implements Cloneable, Serializable {
    // IATI-check: to be ignored

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampComponentFundingId;

    private Integer transactionType;

    private String justAnId;

    public String getJustAnId() {
        return justAnId;
    }

    public void setJustAnId(String justAnId) {
        this.justAnId = justAnId;
    }

//    private Set<AmpComponentFundingDocuments> fundingDocuments;




    @Interchangeable(fieldTitle = COMPONENT_FUNDING_ADJUSTMENT_TYPE, importable = true, pickIdOnly = true,
            fmPath = FMVisibility.PARENT_FM + "/" + COMPONENT_FUNDING_ADJUSTMENT_TYPE,
            interValidators = @InterchangeableValidator(RequiredValidator.class),
            discriminatorOption = CategoryConstants.ADJUSTMENT_TYPE_KEY)
    private AmpCategoryValue adjustmentType;


    @Interchangeable(fieldTitle = COMPONENT_FUNDING_STATUS, importable = true, pickIdOnly = true,
            fmPath = FMVisibility.PARENT_FM + "/" + COMPONENT_FUNDING_STATUS,
            interValidators = @InterchangeableValidator(RequiredValidator.class),
            discriminatorOption = CategoryConstants.COMPONENT_FUNDING_STATUS_KEY)
    private AmpCategoryValue componentFundingStatus;


    @Interchangeable(fieldTitle = "Component Funding Documents",  fmPath = FMVisibility.PARENT_FM + "/" + COMPONENT_FUNDING_DOCS, importable = true)
    @VersionableCollection(fieldTitle = "Component Funding Documents")
    private Set<AmpComponentFundingDocument> componentFundingDocuments = new HashSet<>();

    public Set<AmpComponentFundingDocument> getComponentFundingDocuments() {
        return componentFundingDocuments;
    }
    @Interchangeable(fieldTitle = COMPONENT_FUNDING_TRANSACTION_DATE, importable = true,
            fmPath = FMVisibility.PARENT_FM + "/" + COMPONENT_FUNDING_TRANSACTION_DATE,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private Date transactionDate;

    // @Interchangeable(fieldTitle="Reporting Date")
    private Date reportingDate;

    @Interchangeable(fieldTitle = COMPONENT_FUNDING_AMOUNT, importable = true,
            fmPath = FMVisibility.PARENT_FM + "/" + COMPONENT_FUNDING_AMOUNT,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private Double transactionAmount;

    @Interchangeable(fieldTitle = COMPONENT_ORGANIZATION, importable = true, pickIdOnly = true,
            fmPath = FMVisibility.PARENT_FM + "/" + COMPONENT_ORGANIZATION,
            dependencies = {ComponentFundingOrgRoleValidator.ORGANIZATION_PRESENT_KEY})
    private AmpOrganisation reportingOrganization;

    //@Interchangeable(fieldTitle = COMPONENT_SECOND_REPORTING_ORGANIZATION, importable = true, pickIdOnly = true)
    private AmpOrganisation componentSecondResponsibleOrganization;

    @Interchangeable(fieldTitle = COMPONENT_FUNDING_CURRENCY, importable = true, pickIdOnly = true,
            fmPath = FMVisibility.PARENT_FM + "/" + COMPONENT_FUNDING_CURRENCY,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private AmpCurrency currency;

    @Interchangeable(fieldTitle = COMPONENT_FUNDING_DESCRIPTION,
            fmPath = FMVisibility.PARENT_FM + "/" + COMPONENT_FUNDING_DESCRIPTION)
    private String description;

    @InterchangeableBackReference
    private AmpComponent component;

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

    public AmpCategoryValue getComponentFundingStatus() {
        return componentFundingStatus;
    }

    public String getComponentFundingStatusFormatted() {
        return componentFundingStatus.getValue().toLowerCase();
    }

    public void setComponentFundingDocuments(Set<AmpComponentFundingDocument> componentFundingDocuments) {
        this.componentFundingDocuments = componentFundingDocuments;
    }


    public void setComponentFundingStatus(AmpCategoryValue componentFundingStatus) {
        this.componentFundingStatus = componentFundingStatus;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
}
