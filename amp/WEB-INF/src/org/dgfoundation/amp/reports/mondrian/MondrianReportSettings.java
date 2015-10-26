/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.text.DecimalFormat;

import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

/**
 * Report Settings
 * 
 * @author Nadejda Mandrescu
 */
public class MondrianReportSettings extends MondrianReportFilters implements ReportSettings {
	private String currencyCode = null;
	private DecimalFormat currencyFormat = null;
	private AmountsUnits unitsOption;
	
	public MondrianReportSettings() {
		super();
	}
	
	public MondrianReportSettings(AmpFiscalCalendar calendar) {
		super(calendar);
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
}
