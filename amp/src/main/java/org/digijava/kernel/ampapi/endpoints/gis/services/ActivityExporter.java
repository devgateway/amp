package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.translator.TranslatorWorker;

public abstract class ActivityExporter {
    
    protected Map<String, Object> filters;
    protected List<String> columnNames;
    protected List<List<String>> rowValues;
    protected List<Activity> activities = new ArrayList<>();
    private Set<String> geoCodes = new HashSet<String>();


    protected Set<String> getGeoCodes() {
        return geoCodes;
    }

    protected void appendToGeoCodes(String geoCode) {
        if (geoCodes == null) {
            geoCodes = new HashSet<String>();
        }
        geoCodes.add(geoCode);
    }
    
    /**
     * Generates untranslated names of Excel column headers
     * @return
     */
    protected abstract List<String> getOriginalNames();
    
    /**
     * Genenerates translated names of Excel column headers
     * @param originalNames
     * @return
     */
    private static List<String> generateColumnNames(List<String> originalNames) {
        List<String> res = new ArrayList<>();
        for (String colName : originalNames) {
            res.add(TranslatorWorker.translateText(colName));
        }
        return res;
    }
    
    protected abstract ReportSpecificationImpl generateCustomSpec();
    
    /**
     * Generates a ReportSpecification common for Location Export and Structure Export
     * @return
     */
    protected ReportSpecificationImpl generateSpec() {
        ReportSpecificationImpl spec = generateCustomSpec();
        spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR)).
             addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE)).
             addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
        AmpReportFilters mrf = getFilterRules();
        if (mrf != null)
            spec.setFilters(mrf);
        return spec;
    }
    
    
    private AmpReportFilters getFilterRules() {
        List<String> activityIds = filters != null ? FilterUtils.applyKeywordSearch(filters) : null;
        return FilterUtils.getFilterRules(filters, activityIds);
    }
    
    
    protected ActivityExporter(Map<String, Object> filters){ 
        this.filters = filters;
        this.columnNames = generateColumnNames(getOriginalNames());
        this.activities = generateActivities();
        this.rowValues = generateRowValues();
    }
    
    /**
     * Generates rows of strings; used in the export (generateExcelExport) afterwards
     * @return
     */
    protected abstract List<List<String>> generateRowValues();
    
    /**
     * Generates a list of dto.activities (rows for the excel export), implementation-specific criteria
     * @return
     */
    protected abstract List<Activity> generateActivities();
    
    public HSSFWorkbook export(String fileName) {
        return generateExcelExport(fileName);
    }
    
    private static boolean isUndefined(String value) {
        return value == null || value.equals("Undefined") || value.equals("GeoId: Undefined");
    }
    
    /**
     * used in ActivityLocationExporter code paths
     */
    private String overridingGeoCode = null;

    
    
    protected String getOverridingGeoCode() {
        return overridingGeoCode;
    }

    protected void setOverridingGeoCode(String overridingGeoCode) {
        this.overridingGeoCode = overridingGeoCode;
    }

    protected Activity setActivityField(String key, String value, Activity a) {
        switch(key) {
            case ColumnConstants.GEOCODE:
                if (!isUndefined(value)) {
                    if ("".equals(value)) {
                        //special case: this is the (i>0)th row of a Mondrian report; 
                        //the actual geocode to be used is the overriding one
                        a.setGeoCode(overridingGeoCode);
                        break;
                    } else {
                        geoCodes.add(value);
                        overridingGeoCode = value;
                        a.setGeoCode(value);
                        break;
                    }
                } else 
                    return null;
            case ColumnConstants.ACTIVITY_ID:
                a.setId(Long.parseLong(value));
            case ColumnConstants.IMPLEMENTATION_LEVEL:
                a.setImplementationLevel(value);
                break;
            case ColumnConstants.AMP_ID:
                a.setAmpId(value);
                break;
            case ColumnConstants.PROJECT_TITLE:
                a.setName(value);
                break;
            case MeasureConstants.ACTUAL_COMMITMENTS:
                a.setTotalCommitments(value);
                break;
            case MeasureConstants.ACTUAL_DISBURSEMENTS:
                a.setTotalDisbursments(value);
                break;
            case ColumnConstants.DONOR_AGENCY:
                a.setDonorAgency(value);
                break;
            case ColumnConstants.PRIMARY_SECTOR:
                a.setPrimarySector(value);
                break;
            case ColumnConstants.PROJECT_DESCRIPTION:
                a.setDescription(value);
                break;
        }
        return a;
    }
    
    /**
     * Generates the Excel export based on the list of value rows
     * @param fileName
     * @return
     */
    public HSSFWorkbook generateExcelExport(String fileName){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(fileName);
        HSSFRow row = sheet.createRow(0);
        HSSFRichTextString str = null;
        HSSFFont titlefont = wb.createFont();

        titlefont.setFontHeightInPoints((short) 10);
        titlefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFFont font = wb.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 8);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCellStyle tstyle = wb.createCellStyle();
        tstyle.setFont(titlefont);
        tstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        for (int i = 0; i < columnNames.size(); i++) {
            HSSFCell cell = row.createCell(i);
            str = new HSSFRichTextString(columnNames.get(i));
            cell.setCellValue(str);
            cell.setCellStyle(tstyle);
        }
        for (int i = 0; i < rowValues.size(); i++) {
            row = sheet.createRow(i + 1);
            List<String> list = rowValues.get(i);
            for (int j = 0; j < list.size(); j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellValue(new HSSFRichTextString(list.get(j)));
                cell.setCellStyle(style);
            }
        }
        return wb;
    }
}
