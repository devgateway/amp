package org.dgfoundation.amp.reports.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.json.JSONObject;
import org.json.XML;

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
	
	public static String convertErrorJsonObjToXmlString(JsonBean obj) {
		JsonBean responseErrorBean = new JsonBean();
		Map<String, Collection<Object>> errorBeans = (Map<String, Collection<Object>>) obj.get("error");
		List<Map<String, Object>> errors = new ArrayList<>();
		
		for(String key : errorBeans.keySet()) {
			Map<String, Object> err = new HashMap<>();
			Collection<Object> error = errorBeans.get(key);
			err.put("code", key);
			err.put("value", error);
			errors.add(err);
		}
		
		Map<String, Object> errorsMap = new HashMap<>();
		errorsMap.put("error", errors);
		responseErrorBean.set("errors", errorsMap);
		
		JSONObject o = new JSONObject(responseErrorBean.asJsonString());
		String xmlString = XML.toString(o);
		
		return xmlString;
	}
}
