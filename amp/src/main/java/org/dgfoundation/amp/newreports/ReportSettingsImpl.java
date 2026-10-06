/**
 * 
 */
package org.dgfoundation.amp.newreports;

import org.digijava.kernel.ampapi.endpoints.util.DateFilterUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

import java.text.DecimalFormat;

/**
 * Report Settings mutable implementation
 * 
 * @author Nadejda Mandrescu, Constantin Dolghier
 */
public class ReportSettingsImpl implements ReportSettings {
    protected String currencyCode = null;
    protected DecimalFormat currencyFormat = null;
    protected AmountsUnits unitsOption;
    protected AmpFiscalCalendar calendar;
    
    /** used by frontend API*/
    protected AmpFiscalCalendar oldCalendar;
    
    protected FilterRule yearRangeFilter;
        
    public ReportSettingsImpl() {}
    
    public ReportSettingsImpl(AmpFiscalCalendar calendar) {
        this.calendar = calendar;
    }
    
    @Override
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    /**
     * Configures the currency to be used to display the amounts
     * @param currencyCode 
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public DecimalFormat getCurrencyFormat() {
        return currencyFormat;
    }
    
    /**
     * Configures the currency pattern to be used to display the amount 
     * @param currencyFormat
     */
    public void setCurrencyFormat(DecimalFormat currencyFormat) {
        this.currencyFormat = currencyFormat;
    }
    
    @Override
    /** if the stored value is null, returns the default one (GlobalSettings.AMOUNTS_IN_THOUSANDS) */
    public AmountsUnits getUnitsOption() {
        return unitsOption == null ? AmountsUnits.getDefaultValue() : unitsOption;
    }
    
    public void setUnitsOption(AmountsUnits unitsOption) {
        this.unitsOption = unitsOption;
    }

    @Override
    public AmpFiscalCalendar getCalendar() {
        return this.calendar;
    }
    
    public void setCalendar(AmpFiscalCalendar calendar) {
        this.calendar = calendar;
    }

    public FilterRule getYearRangeFilter() {
        return yearRangeFilter;
    }

    public void setYearRangeFilter(FilterRule yearRangeFilter) {
        this.yearRangeFilter = yearRangeFilter;
    }
    
    public void setYearsRangeFilterRule(Integer from, Integer to) throws Exception {
        this.yearRangeFilter = DateFilterUtils.getYearsRangeFilter(from, to, oldCalendar, calendar);
    }

    public AmpFiscalCalendar getOldCalendar() {
        return oldCalendar;
    }

    public void setOldCalendar(AmpFiscalCalendar oldCalendar) {
        this.oldCalendar = oldCalendar;
    }
}
