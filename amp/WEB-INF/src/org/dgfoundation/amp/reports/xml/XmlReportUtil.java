package org.dgfoundation.amp.reports.xml;

import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

public class XmlReportUtil {

	/**
	 * Convert a custom xml report into json object
	 * @param customReport
	 * @return
	 */
	public static JsonBean convertXmlCustomReportToJsonObj(CustomReport customReport) {
		JsonBean reportConfig = new JsonBean();

		reportConfig.set(EPConstants.REPORT_NAME, customReport.getName());
		
		if (customReport.getColumns() != null) {
			reportConfig.set(EPConstants.ADD_COLUMNS, customReport.getColumns().getColumn());
		}
		
		if (customReport.getMeasures() != null) {
			reportConfig.set(EPConstants.ADD_MEASURES, customReport.getMeasures().getMeasure());
		}
		
		if (customReport.getHierarchies() != null) {
			reportConfig.set(EPConstants.ADD_HIERARCHIES, customReport.getHierarchies().getHierarchy());
		}

		if (customReport.getSettings() != null) {
			Map<String, Object> settings = new HashMap<>();
			settings.put(SettingsConstants.CURRENCY_ID, customReport.getSettings().getCurrencyCode());
			settings.put(SettingsConstants.CALENDAR_TYPE_ID, customReport.getSettings().getCalendarId());
	
			if (customReport.getSettings().getYearRange() != null) {
				Map<String, Object> yearRange = new HashMap<>();
				yearRange.put(SettingsConstants.YEAR_FROM,
						String.valueOf(customReport.getSettings().getYearRange().getFrom()));
				yearRange.put(SettingsConstants.YEAR_TO, String.valueOf(customReport.getSettings().getYearRange().getTo()));
				settings.put(SettingsConstants.YEAR_RANGE_ID, yearRange);
			}
	
			if (customReport.getSettings().getAmountFormat() != null) {
				Map<String, Object> amountFormat = new HashMap<>();
				amountFormat.put(SettingsConstants.AMOUNT_UNITS,
						customReport.getSettings().getAmountFormat().getNumberDivider());
				amountFormat.put(SettingsConstants.MAX_FRACT_DIGITS,
						customReport.getSettings().getAmountFormat().getMaxFracDigits());
				amountFormat.put(SettingsConstants.DECIMAL_SYMBOL,
						customReport.getSettings().getAmountFormat().getDecimalSymbol());
				amountFormat.put(SettingsConstants.USE_GROUPING,
						customReport.getSettings().getAmountFormat().isUseGrouping());
				amountFormat.put(SettingsConstants.GROUP_SEPARATOR,
						customReport.getSettings().getAmountFormat().getGroupSeparator());
				amountFormat.put(SettingsConstants.GROUP_SIZE, 
						customReport.getSettings().getAmountFormat().getGroupSize());
	
				settings.put(SettingsConstants.AMOUNT_FORMAT_ID, amountFormat);
			}
	
			reportConfig.set(EPConstants.SETTINGS, settings);
		}

		return reportConfig;
	}

}
