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
     * @param reportParameter
     * @return
     */
    public static JsonBean convertXmlCustomReportToJsonObj(ReportParameter reportParameter) {
        JsonBean reportConfig = new JsonBean();

        reportConfig.set(EPConstants.REPORT_NAME, reportParameter.getName());
        
        if (reportParameter.getColumns() != null) {
            reportConfig.set(EPConstants.ADD_COLUMNS, reportParameter.getColumns().getColumn());
        }
        
        if (reportParameter.getMeasures() != null) {
            reportConfig.set(EPConstants.ADD_MEASURES, reportParameter.getMeasures().getMeasure());
        }
        
        if (reportParameter.getHierarchies() != null) {
            reportConfig.set(EPConstants.ADD_HIERARCHIES, reportParameter.getHierarchies().getHierarchy());
        }

        if (reportParameter.getSettings() != null) {
            Map<String, Object> settings = new HashMap<>();
            settings.put(SettingsConstants.CURRENCY_ID, reportParameter.getSettings().getCurrencyCode());
            settings.put(SettingsConstants.CALENDAR_TYPE_ID, reportParameter.getSettings().getCalendarId());
    
            if (reportParameter.getSettings().getYearRange() != null) {
                Map<String, Object> yearRange = new HashMap<>();
                yearRange.put(SettingsConstants.YEAR_FROM,
                        String.valueOf(reportParameter.getSettings().getYearRange().getFrom()));
                yearRange.put(SettingsConstants.YEAR_TO, String.valueOf(reportParameter.getSettings().getYearRange().getTo()));
                settings.put(SettingsConstants.YEAR_RANGE_ID, yearRange);
            }
    
            if (reportParameter.getSettings().getAmountFormat() != null) {
                Map<String, Object> amountFormat = new HashMap<>();
                amountFormat.put(SettingsConstants.AMOUNT_UNITS,
                        reportParameter.getSettings().getAmountFormat().getNumberDivider());
                amountFormat.put(SettingsConstants.MAX_FRACT_DIGITS,
                        reportParameter.getSettings().getAmountFormat().getMaxFracDigits());
                amountFormat.put(SettingsConstants.DECIMAL_SYMBOL,
                        reportParameter.getSettings().getAmountFormat().getDecimalSymbol());
                amountFormat.put(SettingsConstants.USE_GROUPING,
                        reportParameter.getSettings().getAmountFormat().isUseGrouping());
                amountFormat.put(SettingsConstants.GROUP_SEPARATOR,
                        reportParameter.getSettings().getAmountFormat().getGroupSeparator());
                amountFormat.put(SettingsConstants.GROUP_SIZE, 
                        reportParameter.getSettings().getAmountFormat().getGroupSize());
    
                settings.put(SettingsConstants.AMOUNT_FORMAT_ID, amountFormat);
            }
    
            reportConfig.set(EPConstants.SETTINGS, settings);
        }

        return reportConfig;
    }
}
