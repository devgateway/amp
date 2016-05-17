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
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.translator.TranslatorWorker;

public abstract class ActivityExporter {
	
	protected Map<String, Object> filters;
	protected List<String> columnNames;
	protected List<List<String>> rowValues;
	protected List<Activity> activities = new ArrayList<>();
	protected Set<String> geoCodes = new HashSet<String>();


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
		for (String colName : originalNames)
			res.add(TranslatorWorker.translateText(colName));
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
		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Object> otherFilters = (LinkedHashMap<String, Object>)filters.get("otherFilters");
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Object> colFilters = (LinkedHashMap<String, Object>)filters.get("columnFilters");
		MondrianReportFilters mrf = getFilterRules(otherFilters, colFilters);
		if (mrf != null)
			spec.setFilters(mrf);
		return spec;
	}
	
	
	protected MondrianReportFilters getFilterRules(LinkedHashMap<String, Object> otherFilter, 
			LinkedHashMap <String, Object> columnFilters) {
		List<String> activityIds = otherFilter != null ? FilterUtils.applyKeywordSearch(otherFilter) : null;
 		return FilterUtils.getFilterRules(columnFilters, otherFilter, activityIds);
	}
	
	
	protected ActivityExporter(Map<String, Object> filters){ 
		this.filters = filters;
		this.columnNames = generateColumnNames(getOriginalNames());
		this.activities = generateActivities();
		this.rowValues = generateRowValues();
	}
	
	protected abstract List<List<String>> generateRowValues();
	
	/**
	 * Generates a list of activities, implementation-specific criteria
	 * @return
	 */
	protected abstract List<Activity> generateActivities();
	
	public HSSFWorkbook export(String fileName) {
		return generateExcelExport(fileName);
	}
	
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
