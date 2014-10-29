package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
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
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
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
	/**
	 * Build an excel file export by structure
	 * @return
	 */
	public static HSSFWorkbook generateExcelExportByStructure(){
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(TranslatorWorker.translateText("Time Stamp"));
		columnNames.add(TranslatorWorker.translateText("Activity Id"));
		columnNames.add(TranslatorWorker.translateText("Project Title"));
		columnNames.add(TranslatorWorker.translateText("Description"));
//		columnNames.add(TranslatorWorker.translateText("Approval Date"));
		columnNames.add(TranslatorWorker.translateText("Location Name"));
		columnNames.add(TranslatorWorker.translateText("Latitude"));
		columnNames.add(TranslatorWorker.translateText("Longitude"));
		columnNames.add(TranslatorWorker.translateText("Project Site"));
		columnNames.add(TranslatorWorker.translateText("Sectors"));
		columnNames.add(TranslatorWorker.translateText("Donors"));
		columnNames.add(TranslatorWorker.translateText("Total Project Commitments"));
		columnNames.add(TranslatorWorker.translateText("Total Project Disbursements"));
		
		
		List<Activity> report=getMapExportByStructure();
		java.util.Date date = new java.util.Date();
		
		int i=1;
		List<List>rowValues=new ArrayList<>();

		for (Activity a : report) {
			List<String>coloumnsValues=new ArrayList<String>();
			coloumnsValues.add(date.toString());			
			coloumnsValues.add(a.getAmpId());
			coloumnsValues.add(a.getName());
			coloumnsValues.add(a.getDescription());
			coloumnsValues.add(a.getStructureName());
			coloumnsValues.add(a.getLatitude());
			coloumnsValues.add(a.getLongitude());
			coloumnsValues.add(a.getImplementationLevel());
			coloumnsValues.add(a.getPrimarySector());
			coloumnsValues.add(a.getDonorAgency());
			coloumnsValues.add(a.getTotalCommitments());
			coloumnsValues.add(a.getTotalDisbursments());	
			rowValues.add(coloumnsValues);
		}
		
		
		return generateExcelExport(columnNames,rowValues,"MapExport.xls");
	}
	/**
	 * Return a excel workbook 
	 * @param columnNames arraylist of columnames 
	 * @param rowValues arraylist of row values, should be in the same order as column names
	 * @param fileName file for the excel file
	 * @return
	 */
	
	
	public static HSSFWorkbook generateExcelExport(List<String> columnNames,List<List>rowValues,String fileName){
		HSSFWorkbook wb = new HSSFWorkbook();


		HSSFSheet sheet = wb.createSheet(fileName);

		HSSFRow row = sheet.createRow((short) (0));
		HSSFRichTextString str = null;
		HSSFFont titlefont = wb.createFont();

		titlefont.setFontHeightInPoints((short) 10);
		titlefont.setBoldweight(titlefont.BOLDWEIGHT_BOLD);
		
		HSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);
		font.setBoldweight(font.BOLDWEIGHT_NORMAL);
		HSSFCellStyle style = wb.createCellStyle();
		HSSFCellStyle tstyle = wb.createCellStyle();
		tstyle.setFont(titlefont);
		tstyle.setAlignment(style.ALIGN_CENTER);
		
		int x = 0;
		for (String columnName:columnNames) {
			HSSFCell cell = row.createCell( x);
			str = new HSSFRichTextString(columnName);
			cell.setCellValue(str);
			cell.setCellStyle(tstyle);
			x++;
		}
		int i=1;
		for (List<String> list : rowValues) {
			int j=0;
			row = sheet.createRow((short) (i));
			for (String colValue : list) {
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(new HSSFRichTextString(colValue));
				cell.setCellStyle(style);
				j++;
			}
			i++;
		}
		return wb;
	}
	/**
	 * Returs a list of activities for the mapexport. once we have the chance to as a report row its value
	 * by report column name we can avoid all the iff and directly export the report to excel
	 * also we can remove the query once the amp_structure table is in the mondrian schem
	 * @return
	 */
	public static List<Activity> getMapExportByLocation() {
		List<Activity>activities=new ArrayList<Activity>();
		ReportSpecificationImpl spec = getCommonSpecForExport("MapExport");		
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.DISTRICT, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.REGION, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.REGION, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.ZONE, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		
		return activities;
	}
	/**
	 * Returs a list of activities for the mapexport. once we have the chance to as a report row its value
	 * by report column name we can avoid all the iff and directly export the report to excel
	 * also we can remove the query once the amp_structure table is in the mondrian schem
	 * @return
	 */
	public static List<Activity> getMapExportByStructure() {

		ReportSpecificationImpl spec = getCommonSpecForExport("MapExport");		

		
		
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
				
				activityId = getActivityIdForReports(activityId, activity, row,
						reportOutputColumn);
			}
			activities.put(activityId,activity);
		}
		//once we have all activities we go and fetch structures asosiated to those activities
		
		final List<Activity> mapExportBean = new ArrayList<Activity>();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {

				
				String query="select ast.amp_activity_id,s.title, "+
						" s.latitude, "+
						" s.longitude "+
						"  from amp_activity_structures ast , amp_structure s  " +
						" where ast.amp_structure_id=s.amp_structure_id "+
						" and ast.amp_activity_id in("+ org.dgfoundation.amp.Util.toCSString(activities.keySet()) +")";
	    		ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
	    		while (rs.next()){
	    			Activity a= activities.get(rs.getLong("amp_activity_id"));
	    			Activity newActivity=new Activity();
	    			newActivity.setId(a.getId());
	    			newActivity.setAmpId(a.getAmpId());
	    			newActivity.setName(a.getName());
	    			newActivity.setTotalCommitments(a.getTotalCommitments());
	    			newActivity.setTotalDisbursments(a.getTotalDisbursments());
	    			newActivity.setDonorAgency(a.getDonorAgency());
	    			newActivity.setImplementationLevel(a.getImplementationLevel());
	    			newActivity.setPrimarySector(a.getPrimarySector());

	    			newActivity.setDescription(a.getDescription());
	    			
	    			newActivity.setStructureName(rs.getString("title"));
	    			newActivity.setLatitude(rs.getString("latitude"));
	    			newActivity.setLongitude(rs.getString("longitude"));
	    			
	    			mapExportBean.add(newActivity);
	    			
	    		}
				
			}
			});
		

		return mapExportBean;
	}
	private static Long getActivityIdForReports(Long activityId,
			Activity activity, Map<ReportOutputColumn, ReportCell> row,
			ReportOutputColumn reportOutputColumn) {
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
								if(reportOutputColumn.columnName.equals(ColumnConstants.DONOR_AGENCY)){
									activity.setDonorAgency(columnValue);
								}else{
									if(reportOutputColumn.columnName.equals(ColumnConstants.PRIMARY_SECTOR)){
										activity.setPrimarySector(columnValue);
									}
									else{
										if(reportOutputColumn.columnName.equals(ColumnConstants.PROJECT_DESCRIPTION)){
											activity.setDescription(columnValue);
										}
									}
								}
							}
					}
				}
			}
		}
		return activityId;
	}
	private static ReportSpecificationImpl getCommonSpecForExport(String name) {
		boolean doTotals=true;
		ReportSpecificationImpl spec = new ReportSpecificationImpl(name);
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.AMP_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.IMPLEMENTATION_LEVEL, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.DONOR_AGENCY, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY));

		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_DESCRIPTION, ReportEntityType.ENTITY_TYPE_ACTIVITY));

		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));

		spec.setCalculateColumnTotals(doTotals);
		spec.setCalculateRowTotals(doTotals);
		return spec;
	}
	public static HSSFWorkbook generateExcelExportByLocation() {
		// TODO Auto-generated method stub
		return null;
	}
}
