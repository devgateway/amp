package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Diego Dimunzio
 *
 */

public class LocationService {
	
	/**
	 * Get totals (actual commitments/ actual disbursements) by administrative level
	 * @param admlevel
	 * @param type
	 * @return
	 */
	public GeneratedReport getTotals(String admlevel, String type) {
		String err = null;
		
		switch (admlevel) {
		case "adm0":
			admlevel = ColumnConstants.COUNTRY; 
			break;
		case "adm1":
			admlevel = ColumnConstants.REGION; 
			break;
		case "adm2":
			admlevel = ColumnConstants.ZONE; 
			break;
		case "adm3":
			admlevel = ColumnConstants.DISTRICT; 
			break;
		default:
			admlevel = ColumnConstants.REGION; 
			break;
		}
		
		switch (type) {
		case "ac":
			type = MeasureConstants.ACTUAL_COMMITMENTS; 
			break;
		case "ad":
			type = MeasureConstants.ACTUAL_DISBURSEMENTS; 
			break;
		default:
			type = MeasureConstants.ACTUAL_COMMITMENTS;
			break;
		}
		ReportSpecificationImpl spec = new ReportSpecificationImpl("LocationsTotals");
		spec.addColumn(new ReportColumn(admlevel, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(type, ReportEntityType.ENTITY_TYPE_ALL));
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()),false);
		GeneratedReport report = null;
		boolean doTotals=true;
		spec.setCalculateColumnTotals(doTotals);
		spec.setCalculateRowTotals(doTotals);
		spec.setDisplayEmptyFundingColumns(true);
		
		spec.setSummaryReport(false);
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			err = e.getMessage();
		}
		
		return report;
	}
	public static List<JsonBean>getExportMap(JsonBean filter){
		List<JsonBean>mapExport=new ArrayList<JsonBean>();
		JsonBean j=new JsonBean();
		j.set("Type", "Region");
		j.set("Latitude", 111);
		j.set("Longitude", 111);
		JsonBean q=new JsonBean();
		q.set("Type", "Typology");
		q.set("Latitude", 111);
		q.set("Longitude", 111);

		mapExport.add(j);
		mapExport.add(q);
		
		
		
		
		getMapExport();
		return mapExport;
	}
	public static HSSFWorkbook generateExcelExport(){
		HSSFWorkbook wb = new HSSFWorkbook();
		ArrayList<String> columnNames = new ArrayList<String>();

		HSSFSheet sheet = wb.createSheet("MapExport.xls");

		HSSFRow row = sheet.createRow((short) (0));
		HSSFRichTextString str = null;
		HSSFFont titlefont = wb.createFont();

		titlefont.setFontHeightInPoints((short) 10);
		titlefont.setBoldweight(titlefont.BOLDWEIGHT_BOLD);
		
		Site site =TLSUtils.getSite(); 

        String langCode =TLSUtils.getEffectiveLangCode();
        
		java.util.Date date = new java.util.Date();

		HSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);
		font.setBoldweight(font.BOLDWEIGHT_NORMAL);
		HSSFCellStyle style = wb.createCellStyle();
		HSSFCellStyle tstyle = wb.createCellStyle();
		tstyle.setFont(titlefont);
		tstyle.setAlignment(style.ALIGN_CENTER);
		columnNames.add(TranslatorWorker.translateText("Time Stamp"));
//		columnNames.add(TranslatorWorker.translateText("Type"));
		columnNames.add(TranslatorWorker.translateText("Activity Id"));
		columnNames.add(TranslatorWorker.translateText("Project Title"));
//		columnNames.add(TranslatorWorker.translateText("Description"));
//		columnNames.add(TranslatorWorker.translateText("Approval Date"));
//		columnNames.add(TranslatorWorker.translateText("GEOID"));
		columnNames.add(TranslatorWorker.translateText("Name"));
//		columnNames.add(TranslatorWorker.translateText("Latitude"));
//		columnNames.add(TranslatorWorker.translateText("Longitude"));
//		columnNames.add(TranslatorWorker.translateText("Sectors"));
//		columnNames.add(TranslatorWorker.translateText("Donors"));
		columnNames.add(TranslatorWorker.translateText("Actual Commitments for this location"));
		columnNames.add(TranslatorWorker.translateText("Actual Disbursement for this location"));
//		columnNames.add(TranslatorWorker.translateText("Total Actual Commitments"));
//		columnNames.add(TranslatorWorker.translateText("Total Actual Disbursement"));
		int x = 0;
		for (String columnName:columnNames) {
			HSSFCell cell = row.createCell( x);
			str = new HSSFRichTextString(columnName);
			cell.setCellValue(str);
			cell.setCellStyle(tstyle);
			x++;
		}
		GeneratedReport report=getMapExport();
		//we iterate over the report
		List<ReportArea> ll = report.reportContents.getChildren();
		int i=1;
		for (ReportArea reportArea : ll) {
			JsonBean activity = new JsonBean();
			Map<ReportOutputColumn, ReportCell> rRow = reportArea.getContents();
			
			Set<ReportOutputColumn> col = rRow.keySet();
			int j=0;
			row = sheet.createRow((short) (i));

			//we first add the date
			HSSFCell cell = row.createCell(j);
			str = new HSSFRichTextString(date.toString());
			cell.setCellValue(str);
			cell.setCellStyle(style);
			j++;
			for (ReportOutputColumn reportOutputColumn : col) {
				
				cell = row.createCell(j);
				str = new HSSFRichTextString(rRow.get(reportOutputColumn).displayedValue.toString());
				cell.setCellValue(str);
				cell.setCellStyle(style);
				j++;	
			}
			i++;
		}
	
		
		getMapExport();
		return wb;
	}
	public static GeneratedReport getMapExport() {
//		ReportSpecificationImpl spec = new ReportSpecificationImpl("MapExport");
//		spec.addColumn(new ReportColumn(ColumnConstants.ZONE, ReportEntityType.ENTITY_TYPE_ACTIVITY));
//		ReportColumn implLevelColumn=
//				MondrianReportUtils.getColumn(ColumnConstants.IMPLEMENTATION_LEVEL, ReportEntityType.ENTITY_TYPE_ACTIVITY);
////		ReportColumn zoneColumn=
////				MondrianReportUtils.getColumn(ColumnConstants.ZONE, ReportEntityType.ENTITY_TYPE_ACTIVITY);		
////		
//		spec.addColumn(implLevelColumn);
////		spec.addColumn(zoneColumn);
//		spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
//		spec.addMeasure(new ReportMeasure( MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
//		
//		
////		Set<ReportColumn> hierarchies=new LinkedHashSet<ReportColumn>();
////		hierarchies.add(implLevelColumn);
////		hierarchies.add(zoneColumn);
////		spec.setHierarchies(hierarchies);
		String name= "MapExport";
		boolean doTotals=false;
		ReportSpecificationImpl spec = new ReportSpecificationImpl(name);
		
		
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.AMP_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ACTIVITY));

		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.IMPLEMENTATION_LEVEL, ReportEntityType.ENTITY_TYPE_ACTIVITY));


		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));

		spec.setCalculateColumnTotals(doTotals);
		spec.setCalculateRowTotals(doTotals);		
		
		
		
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()),false);
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
		return report;
	}
}
