package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
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
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.reports.ReportAreaMultiLinked;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.jdbc.Work;

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
		columnNames.add(TranslatorWorker.translateText("Activity Id"));
		columnNames.add(TranslatorWorker.translateText("Project Title"));
//		columnNames.add(TranslatorWorker.translateText("Description"));
//		columnNames.add(TranslatorWorker.translateText("Approval Date"));
//		columnNames.add(TranslatorWorker.translateText("GEOID"));
		columnNames.add(TranslatorWorker.translateText("Name"));
		columnNames.add(TranslatorWorker.translateText("Latitude"));
		columnNames.add(TranslatorWorker.translateText("Longitude"));
		columnNames.add(TranslatorWorker.translateText("Type"));

//		columnNames.add(TranslatorWorker.translateText("Sectors"));
		columnNames.add(TranslatorWorker.translateText("Donors"));
		columnNames.add(TranslatorWorker.translateText("Actual Commitments"));
		columnNames.add(TranslatorWorker.translateText("Actual Disbursement"));
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
		List<Activity> report=getMapExport();
		//we iterate over the report
//		List<ReportArea> ll = report.reportContents.getChildren();
		int i=1;
		//we iterate over each row
		for (Activity a : report) {
			JsonBean activity = new JsonBean();
//			Map<ReportOutputColumn, ReportCell> rRow = reportArea.getContents();
			
//			Set<ReportOutputColumn> col = rRow.keySet();
			row = sheet.createRow((short) (i));

			//we first add the date
			HSSFCell cell = row.createCell(0);
			str = new HSSFRichTextString(date.toString());
			cell.setCellValue(str);
			cell.setCellStyle(style);
			//Activity Id
			cell = row.createCell(1);
			cell.setCellValue(new HSSFRichTextString(a.getAmpId()));
			cell.setCellStyle(style);

			//Project Title
			cell = row.createCell(2);
			cell.setCellValue(new HSSFRichTextString(a.getName()));
			cell.setCellStyle(style);			
			//Structure Name
			cell = row.createCell(3);
			cell.setCellValue(new HSSFRichTextString(a.getStructureName()));
			cell.setCellStyle(style);			

			//Latitude
			cell = row.createCell(4);
			cell.setCellValue(new HSSFRichTextString(a.getLatitude()));
			cell.setCellStyle(style);
			
			//Longitued
			cell = row.createCell(5);
			cell.setCellValue(new HSSFRichTextString(a.getLongitude()));
			cell.setCellStyle(style);			

	
			//Longitued
			cell = row.createCell(6);
			cell.setCellValue(new HSSFRichTextString(a.getStructureType()));
			cell.setCellStyle(style);			
			//DonorAgency
			cell = row.createCell(7);
			cell.setCellValue(new HSSFRichTextString(a.getDonorAgency()));
			cell.setCellStyle(style);
			
			//Actual Commitments
			cell = row.createCell(8);
			cell.setCellValue(new HSSFRichTextString(a.getTotalCommitments()));
			cell.setCellStyle(style);
			
			//Actual Disbursement
			cell = row.createCell(9);
			cell.setCellValue(new HSSFRichTextString(a.getTotalDisbursments()));
			cell.setCellStyle(style);						
			i++;
		}
		return wb;
	}
	public static List<Activity> getMapExport() {

		String name= "MapExport";
		boolean doTotals=true;
		ReportSpecificationImpl spec = new ReportSpecificationImpl(name);
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.AMP_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.DONOR_AGENCY, ReportEntityType.ENTITY_TYPE_ACTIVITY));

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
		final Map<Long,Activity>activities=new LinkedHashMap<Long,Activity>();
		for (ReportArea reportArea : report.reportContents.getChildren()) {
			Long activityId=0L;
			Activity activity = new Activity();
			Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
			Set<ReportOutputColumn> col = row.keySet();
			for (ReportOutputColumn reportOutputColumn : col) {
				if(reportOutputColumn.columnName.equals(ColumnConstants.ACTIVITY_ID)){
					activityId=Long.parseLong(row.get(reportOutputColumn).value.toString());
					activity.setId(activityId);
				}else{ 
					String columnValue=row.get(reportOutputColumn).displayedValue.toString();
					if(reportOutputColumn.columnName.equals(ColumnConstants.AMP_ID)){
						activity.setAmpId(columnValue);
					}else{
						if(reportOutputColumn.columnName.equals(ColumnConstants.PROJECT_TITLE)){
							activity.setName(columnValue);
						}else{
							if(reportOutputColumn.columnName.equals(MeasureConstants.ACTUAL_COMMITMENTS)){
								activity.setTotalCommitments(columnValue);
							}
								else{
									if(reportOutputColumn.columnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)){
										activity.setTotalDisbursments(columnValue);
									}	else{
										activity.setDonorAgency(columnValue);
									}
							}
						}
					}
				}
			}
			activities.put(activityId,activity);
		}
		//once we have all activities we go and fetch structures asosiated to those activities
		
		final List<Activity> mapExportBean = new ArrayList<Activity>();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {

				
				String query="select ast.amp_activity_id,s.title, "+
						" s.latitude, "+
						" s.longitude, "+
						" st.name type_name  from amp_activity_structures ast , amp_structure s , amp_structure_type st " +
						" where ast.amp_structure_id=s.amp_structure_id "+
						" and s.type = st.typeid "+
						" and ast.amp_activity_id in("+ org.dgfoundation.amp.Util.toCSString(activities.keySet()) +")";
	    		ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
	    		System.out.println(query);
	    		while (rs.next()){
	    			Activity a= activities.get(rs.getLong("amp_activity_id"));
	    			Activity newActivity=new Activity();
	    			newActivity.setId(a.getId());
	    			newActivity.setAmpId(a.getAmpId());
	    			newActivity.setName(a.getName());
	    			newActivity.setTotalCommitments(a.getTotalCommitments());
	    			newActivity.setTotalDisbursments(a.getTotalDisbursments());
	    			
	    			newActivity.setStructureName(rs.getString("title"));
	    			newActivity.setLatitude(rs.getString("latitude"));
	    			newActivity.setLongitude(rs.getString("longitude"));
	    			newActivity.setStructureType(rs.getString("type_name"));
	    			
	    			mapExportBean.add(newActivity);
	    			
	    		}
				
			}
			});
		

		return mapExportBean;
	}
}
