package org.digijava.module.aim.helper;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.util.Date;

/**
 * @author Aldo Picca
 */
public class SummaryChange {
    private static final Logger LOGGER = Logger.getLogger(SummaryChange.class);
    private Integer transactionType;
    private AmpCategoryValue adjustmentType;
    private String changeType;
    private Double previousValue;
    private AmpCurrency previousCurrency;
    private Double currentValue;
    private AmpCurrency currentCurrency;
    private Date transactionDate;
    private Quarter quarter;

    public SummaryChange(Integer transactionType, AmpCategoryValue adjustmentType, String changeType, Double
            previousValue, AmpCurrency previousCurrency, Double currentValue, AmpCurrency currentCurrency, Date
            transactionDate) {
        this.transactionType = transactionType;
        this.adjustmentType = adjustmentType;
        this.changeType = changeType;
        this.previousValue = previousValue;
        this.previousCurrency = previousCurrency;
        this.currentValue = currentValue;
        this.currentCurrency = currentCurrency;
        this.transactionDate = transactionDate;
        this.quarter = generateQuarter();

    }

    private Quarter generateQuarter() {
        Long gsCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
        AmpFiscalCalendar fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(gsCalendarId);
        return new Quarter(fiscalCalendar, this.transactionDate);
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public String getFundingDetailType() {
        return this.adjustmentType + " "
                + ArConstants.TRANSACTION_ID_TO_TYPE_NAME.get(this.transactionType);
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public AmpCategoryValue getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(AmpCategoryValue adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Double getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(Double previousValue) {
        this.previousValue = previousValue;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }

    public AmpCurrency getPreviousCurrency() {
        return previousCurrency;
    }

    public void setPreviousCurrency(AmpCurrency previousCurrency) {
        this.previousCurrency = previousCurrency;
    }

    public AmpCurrency getCurrentCurrency() {
        return currentCurrency;
    }

    public void setCurrentCurrency(AmpCurrency currentCurrency) {
        this.currentCurrency = currentCurrency;
    }
}
