/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.CommonFieldsConstants;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldSimple;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.util.Output;
import org.digijava.module.aim.validator.groups.Submit;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Simple Funding Amount
 */
public class AmpFundingAmount implements Comparable<AmpFundingAmount>, Serializable, Versionable, Cloneable {

    public enum FundingType {
        PROPOSED, //0
        REVISED; //1
        
        public final String title;
        private FundingType() {
            title = StringUtils.capitalize(name().toLowerCase());
        }
        
        public static String getTitle(Integer type) {
            if (type < FundingType.values().length) {
                return FundingType.values()[type].title;
            }
            return null;
        }
    };
    
    private Long ampFundingAmountId;

    @InterchangeableBackReference
    private AmpActivityVersion activity;
    
    @Interchangeable(fieldTitle = "Amount", importable = true,
            fmPath = FMVisibility.PARENT_FM + "/" + CategoryConstants.PROJECT_AMOUNT_NAME,
            interValidators = @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                    fmPath = FMVisibility.PARENT_FM + "/Required Validator for Cost Amount"))
    @VersionableFieldSimple(fieldTitle = "Fun Amount")
    protected Double funAmount;
    
    @Interchangeable(fieldTitle = "Currency", importable = true, fmPath = FMVisibility.PARENT_FM + "/Currency",
            pickIdOnly = true, commonPV = CommonFieldsConstants.COMMON_CURRENCY)
    @VersionableFieldSimple(fieldTitle = "Currency")
    private AmpCurrency currency;
    
    @Interchangeable(fieldTitle = "Funding Date", importable = true, fmPath = FMVisibility.PARENT_FM + "/" + CategoryConstants.PROPOSE_PRJC_DATE_NAME)
    @VersionableFieldSimple(fieldTitle = "Fun Date")
    protected Date funDate;
    
    protected FundingType funType;

    /**
     * @return the ampFundingAmountId
     */
    public Long getAmpFundingAmountId() {
        return ampFundingAmountId;
    }

    /**
     * @param ampFundingAmountId the ampFundingAmountId to set
     */
    public void setAmpFundingAmountId(Long ampFundingAmountId) {
        this.ampFundingAmountId = ampFundingAmountId;
    }

    /**
     * @return the activity
     */
    public AmpActivityVersion getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }

    /**
     * @return the funAmount
     */
    public Double getFunAmount() {
        return funAmount;
    }

    /**
     * @param funAmount the funAmount to set
     */
    public void setFunAmount(Double funAmount) {
        this.funAmount = funAmount;
    }

    public AmpCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(AmpCurrency currency) {
        this.currency = currency;
    }

    public String getCurrencyCode() {
        return currency != null ? currency.getCurrencyCode() : null;
    }

    /**
     * @return the funDate
     */
    public Date getFunDate() {
        return funDate;
    }

    /**
     * @param funDate the funDate to set
     */
    public void setFunDate(Date funDate) {
        this.funDate = funDate;
    }

    /**
     * @return the funType
     */
    public FundingType getFunType() {
        return funType;
    }

    /**
     * @param funType the funType to set
     */
    public void setFunType(FundingType funType) {
        this.funType = funType;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpFundingAmount aux = (AmpFundingAmount) obj;
        String original = getVersionableStr();
        String copy = "" + aux.funAmount + "-" + aux.currency.getCurrencyCode() + "-" + aux.funDate;
        if (original.equals(copy)) {
            return true;
        }
        return false;
    }
    
    protected String getVersionableStr() {
        return "" + this.funAmount + "-" + this.currency.getCurrencyCode() + "-" + this.funDate;
    }

    @Override
    public Object getValue() {
        return getVersionableStr();
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        if (funType != null) {
            out.getOutputs().add(new Output(null, new String[]{"Type"}, new Object[]{
                    StringUtils.capitalize(StringUtils.lowerCase(funType.name()))}));
        }
        if (funAmount != null) {
            out.getOutputs().add(new Output(null, new String[]{"Amount"}, new Object[]{funAmount}));
        }
        if (funDate != null) {
            out.getOutputs().add(new Output(null, new String[]{"Date"}, new Object[]{funDate}));
        }
        if (currency != null) {
            out.getOutputs().add(new Output(null, new String[]{"Currency"}, new Object[]{currency}));
        }
        return out;
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws Exception {
        AmpFundingAmount aux = (AmpFundingAmount) clone();
        aux.activity = newActivity;
        aux.ampFundingAmountId = null;
        return aux;
    }

    @Override
    public int compareTo(AmpFundingAmount arg0) {
        if (this.getAmpFundingAmountId() !=null && arg0 != null && arg0.getAmpFundingAmountId() != null) {
            return this.getAmpFundingAmountId().compareTo(arg0.getAmpFundingAmountId());
        } else if (arg0.getAmpFundingAmountId() == null && arg0 != null && arg0.getAmpFundingAmountId() == null) {
            Integer tempId1 = System.identityHashCode(this);
            Integer tempId2 = System.identityHashCode(arg0);
            return tempId1.compareTo(tempId2);
        } else {
            return -1;
        }
    }
    
}
