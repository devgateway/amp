package org.dgfoundation.amp.reports.xml;

import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.reports.ReportFormParameters;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;

public class XmlReportUtil {

    /**
     * Convert a custom xml report into json object
     * @param reportParameter
     * @return
     */
    public static ReportFormParameters convertXmlCustomReportToJsonObj(ReportParameter reportParameter) {
        ReportFormParameters reportConfig = new ReportFormParameters();

        reportConfig.setReportName(reportParameter.getName());
        
        if (reportParameter.getColumns() != null) {
            reportConfig.setAdditionalColumns(reportParameter.getColumns().getColumn());
        }
        
        if (reportParameter.getMeasures() != null) {
            reportConfig.setAdditionalMeasures(reportParameter.getMeasures().getMeasure());
        }
        
        if (reportParameter.getHierarchies() != null) {
            reportConfig.setAdditionalHierarchies(reportParameter.getHierarchies().getHierarchy());
        }

        if (reportParameter.getSettings() != null) {
            Map<String, Object> settings = new HashMap<>();
            if (reportParameter.getSettings().getCurrencyCode() != null) {
                settings.put(SettingsConstants.CURRENCY_ID, reportParameter.getSettings().getCurrencyCode());
            }
            
            if (reportParameter.getSettings().getCalendarId() != null) {
                settings.put(SettingsConstants.CALENDAR_TYPE_ID, reportParameter.getSettings().getCalendarId());
            }
    
            if (reportParameter.getSettings().getYearRange() != null) {
                Map<String, Object> yearRange = new HashMap<>();
                if (reportParameter.getSettings().getYearRange().getFrom() != null) {
                    yearRange.put(SettingsConstants.YEAR_FROM,
                            String.valueOf(reportParameter.getSettings().getYearRange().getFrom()));
                }
                
                if (reportParameter.getSettings().getYearRange().getTo() != null) {
                    yearRange.put(SettingsConstants.YEAR_TO, 
                            String.valueOf(reportParameter.getSettings().getYearRange().getTo()));
                }
                
                settings.put(SettingsConstants.YEAR_RANGE_ID, yearRange);
            }
    
            if (reportParameter.getSettings().getAmountFormat() != null) {
                Map<String, Object> amountFormat = new HashMap<>();
                
                if (reportParameter.getSettings().getAmountFormat().getNumberDivider() != null) {
                    amountFormat.put(SettingsConstants.AMOUNT_UNITS,
                            reportParameter.getSettings().getAmountFormat().getNumberDivider());
                }
                
                if (reportParameter.getSettings().getAmountFormat().getMaxFracDigits() != null) {
                    amountFormat.put(SettingsConstants.MAX_FRACT_DIGITS,
                            reportParameter.getSettings().getAmountFormat().getMaxFracDigits());
                }
                
                if (reportParameter.getSettings().getAmountFormat().getDecimalSymbol() != null) {
                    amountFormat.put(SettingsConstants.DECIMAL_SYMBOL,
                            reportParameter.getSettings().getAmountFormat().getDecimalSymbol());
                }
                
                if (reportParameter.getSettings().getAmountFormat().getGroupSeparator() != null) {
                    amountFormat.put(SettingsConstants.GROUP_SEPARATOR,
                            reportParameter.getSettings().getAmountFormat().getGroupSeparator());
                }
                
                if (reportParameter.getSettings().getAmountFormat().getGroupSize() != null) {
                    amountFormat.put(SettingsConstants.GROUP_SIZE, 
                            reportParameter.getSettings().getAmountFormat().getGroupSize());
                }
                
                amountFormat.put(SettingsConstants.USE_GROUPING,
                        reportParameter.getSettings().getAmountFormat().isUseGrouping());
    
                settings.put(SettingsConstants.AMOUNT_FORMAT_ID, amountFormat);
            }
    
            reportConfig.setSettings(settings);
        }
        
        if (reportParameter.isSummary() != null) {
            reportConfig.setSummary(reportParameter.isSummary());
            reportConfig.setGroupingOption("");
            
            if (reportParameter.getGroupingOption() != null) {
                reportConfig.setGroupingOption(reportParameter.getGroupingOption().value());
            }
        }
        
        if (reportParameter.getFilters() != null) {
            Map<String, Object> filters = new HashMap<>();
            
            for (Filter f : reportParameter.getFilters().getFilter()) {
                if (f.getStart() != null || f.getEnd() != null) {
                    Map<String, Object> dateObject = new HashMap<>();
                    dateObject.put("start", f.getStart());
                    dateObject.put("end", f.getEnd());
                    
                    filters.put(f.getName(), dateObject);
                } else if (f.getValues() != null && f.getValues().getValue() != null) {
                    filters.put(f.getName(), f.getValues().getValue());
                }
            }
            
            reportConfig.setFilters(filters);
        }

        return reportConfig;
    }
}
