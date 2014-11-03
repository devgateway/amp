package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.FilterRule;
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
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
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
	public JsonBean getTotals(String admlevel, String type, JsonBean filter) {
		String err = null;
		JsonBean retlist = new JsonBean();
		
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
		String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
 		ReportSpecificationImpl spec = new ReportSpecificationImpl("LocationsTotals");
		spec.addColumn(new ReportColumn(admlevel, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.GEOCODE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(type, ReportEntityType.ENTITY_TYPE_ALL));
		spec.getHierarchies().addAll(spec.getColumns());
		MondrianReportFilters filterRules=new MondrianReportFilters(); 
		
		if(filter!=null){
			Object columnFilters=filter.get("columnFilters");
			if(columnFilters!=null){
				filterRules = FilterUtils.getApiColumnFilter((LinkedHashMap<String, Object>)filter.get("columnFilters"));	
			}
 		}
		filterRules.addFilterRule(MondrianReportUtils.getColumn(ColumnConstants.IMPLEMENTATION_LEVEL, ReportEntityType.ENTITY_TYPE_ACTIVITY), 
				new FilterRule(admlevel, true, false));
		spec.setFilters(filterRules);
		
		
		
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()),true);
		GeneratedReport report = null;
		
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			err = e.getMessage();
		}
		
		String currcode = EndpointUtils.getDefaultCurrencyCode();
		retlist.set("currency", currcode);

		retlist.set("numberformat", numberformat);
		List<JsonBean> values = new ArrayList<JsonBean>();
		for (Iterator iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
			JsonBean item = new JsonBean();
			ReportAreaImpl reportArea =  (ReportAreaImpl) iterator.next();
			LinkedHashMap<ReportOutputColumn, ReportCell> content = (LinkedHashMap<ReportOutputColumn, ReportCell>) reportArea.getContents();
			org.dgfoundation.amp.newreports.TextCell reportcolumn = (org.dgfoundation.amp.newreports.TextCell) content.values().toArray()[1];
			item.set("admID",reportcolumn.value);
			ReportCell reportcell = (ReportCell) content.values().toArray()[2];
			item.set("amount",reportcell.value);
			values.add(item);
		}
		retlist.set("values", values);
		return retlist;
	}
	/**
	 * Build an excel file export by structure
	 * @return
	 */
	public static HSSFWorkbook generateExcelExportByStructure(){
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(TranslatorWorker.translateText("Time Stamp"));//1
		columnNames.add(TranslatorWorker.translateText("Activity Id"));//2
		columnNames.add(TranslatorWorker.translateText("Project Title"));//3
		columnNames.add(TranslatorWorker.translateText("Description"));//4
//		columnNames.add(TranslatorWorker.translateText("Approval Date"));
		columnNames.add(TranslatorWorker.translateText("Project Site"));//5
		columnNames.add(TranslatorWorker.translateText("Latitude"));//6
		columnNames.add(TranslatorWorker.translateText("Longitude"));//7
//		columnNames.add(TranslatorWorker.translateText("Project Site"));//8
		columnNames.add(TranslatorWorker.translateText("Sectors"));//9
		columnNames.add(TranslatorWorker.translateText("Donors"));//10
		columnNames.add(TranslatorWorker.translateText("Total Project Commitments"));//11 
		columnNames.add(TranslatorWorker.translateText("Total Project Disbursements"));//12
		
		
		List<Activity> report=getMapExportByStructure();
		java.util.Date date = new java.util.Date();
		
		int i=1;
		List<List>rowValues=new ArrayList<>();

		for (Activity a : report) {
			List<String>coloumnsValues=new ArrayList<String>();
			coloumnsValues.add(date.toString());			//1
			coloumnsValues.add(a.getAmpId());//2
			coloumnsValues.add(a.getName());//3
			coloumnsValues.add(a.getDescription());//4
			coloumnsValues.add(a.getStructureName());//5
			coloumnsValues.add(a.getLatitude());//6
			coloumnsValues.add(a.getLongitude());//7
//			coloumnsValues.add(a.getImplementationLevel());//8
			coloumnsValues.add(a.getPrimarySector());//9
			coloumnsValues.add(a.getDonorAgency());//10
			coloumnsValues.add(a.getTotalCommitments());//11
			coloumnsValues.add(a.getTotalDisbursments());//12
			rowValues.add(coloumnsValues);
		}
		
		
		return generateExcelExport(columnNames,rowValues,"MapExport-structure.xls");
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
	public static List<Activity> getMapExportByLocation(final Map<String,Activity>geocodeInfo) {
		List<Activity> activities = new ArrayList<Activity>();
		final List<String>geoCodesId=new ArrayList<String >();
		ReportSpecificationImpl spec = new ReportSpecificationImpl("MapExport");
		Set<ReportColumn> hierarchies = new LinkedHashSet<ReportColumn>();
		ReportColumn ampId = new ReportColumn(ColumnConstants.AMP_ID,
				ReportEntityType.ENTITY_TYPE_ALL);

		ReportColumn geoid=MondrianReportUtils.getColumn(
				ColumnConstants.GEOCODE,
				ReportEntityType.ENTITY_TYPE_ACTIVITY);
		
		ReportColumn impLevel=		MondrianReportUtils.getColumn(
				ColumnConstants.IMPLEMENTATION_LEVEL,
				ReportEntityType.ENTITY_TYPE_ACTIVITY);
		spec.addColumn(geoid);

		
		spec.addColumn(ampId );
		spec.addColumn(impLevel);
		spec.addColumn(geoid);
		hierarchies.add(ampId );
		hierarchies.add(impLevel);
		hierarchies.add(geoid);
		
		spec.setHierarchies(hierarchies);
		
		getCommonSpecForExport(spec);

		MondrianReportGenerator generator = new MondrianReportGenerator(
				ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils
						.getRequest()), false);
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		} 
		for (ReportArea reportArea : report.reportContents.getChildren()) {
			getActivitiesById(reportArea,activities,geoCodesId);
		}
		//Go and fetch location specific information
		
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {

				
				String query="select geo_code,location_name,gs_lat,gs_long from "+ 
						" amp_category_value_location   "+
						"where  geo_code in ("+ org.dgfoundation.amp.Util.toCSString(geoCodesId) +")";
	    		ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
	    		while(rs.next()){
	    			Activity a=new Activity();
	    			String geoCode=rs.getString("geo_code");
	    			a.setLocationName(rs.getString("location_name"));
	    			a.setLatitude(rs.getString("gs_lat"));
	    			a.setLongitude(rs.getString("gs_long"));
	    			a.setGeoCode(geoCode);
	    			geocodeInfo.put(geoCode, a);
	    		}
			}
		});
		Collections.sort(activities, new Comparator<Activity>() {
			@Override
			public int compare(Activity a, Activity b) {
				return a.getAmpId().compareTo(b.getAmpId());
			}
		});
		return activities;
	}
	
	private static void getActivitiesById(ReportArea reportArea,
			List<Activity> activities,List<String>geoCodesId) {
		if (reportArea.getChildren() == null) {
			Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
			Set<ReportOutputColumn> col = row.keySet();
			Activity a = new Activity();
			for (ReportOutputColumn reportOutputColumn : col) {
				String columnValue = row.get(reportOutputColumn).displayedValue
						.toString();
				if (reportOutputColumn.originalColumnName
						.equals(ColumnConstants.GEOCODE)) {
					if(columnValue==null || columnValue.equals("Undefined")|| columnValue.equals("")){
						return;
					}
					geoCodesId.add(columnValue);
					a.setGeoCode(columnValue);
				}
				else{
					if (reportOutputColumn.originalColumnName
							.equals(ColumnConstants.IMPLEMENTATION_LEVEL)) {
						a.setImplementationLevel(columnValue);
					}else{
						getActivityIdForReports( a, row,reportOutputColumn);
					}
				}
			}
			activities.add(a);
		} else {
			for (ReportArea innerreportArea : reportArea.getChildren()) {
				getActivitiesById(innerreportArea,activities,geoCodesId);
			}
		}
	}
	/**
	 * Returs a list of activities for the mapexport. once we have the chance to as a report row its value
	 * by report column name we can avoid all the iff and directly export the report to excel
	 * also we can remove the query once the amp_structure table is in the mondrian schem
	 * @return
	 */
	public static List<Activity> getMapExportByStructure() {

		ReportSpecificationImpl spec = new ReportSpecificationImpl("MapExport");
		//since amp_id will be added as a hiearchy onthe other report
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.AMP_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		getCommonSpecForExport(spec);

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
					getActivityIdForReports(activity, row,reportOutputColumn);
				}
			}
			activities.put(activityId,activity);
		}
		//once we have all activities we go and fetch structures associated to those activities
		//since its not yet implemented on reports we fetch them separately 
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
	private static void  getActivityIdForReports(
			Activity activity, Map<ReportOutputColumn, ReportCell> row,
			ReportOutputColumn reportOutputColumn) {

			String columnValue=row.get(reportOutputColumn).displayedValue.toString();
			if(reportOutputColumn.originalColumnName.equals(ColumnConstants.AMP_ID)){
				activity.setAmpId(columnValue);
			}else{
				if(reportOutputColumn.originalColumnName.equals(ColumnConstants.PROJECT_TITLE)){
					activity.setName(columnValue);
				}else{
					if(reportOutputColumn.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS)){
						activity.setTotalCommitments(columnValue);
					}
						else{
							if(reportOutputColumn.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)){
								activity.setTotalDisbursments(columnValue);
							}	else{ 
								if(reportOutputColumn.originalColumnName.equals(ColumnConstants.DONOR_AGENCY)){
									activity.setDonorAgency(columnValue);
								}else{
									if(reportOutputColumn.originalColumnName.equals(ColumnConstants.PRIMARY_SECTOR)){
										activity.setPrimarySector(columnValue);
									}
									else{
										if(reportOutputColumn.originalColumnName.equals(ColumnConstants.PROJECT_DESCRIPTION)){
											activity.setDescription(columnValue);
										}
									}
								}
							}
					}
				}
			}

	}
	public static void getCommonSpecForExport(ReportSpecificationImpl spec) {
		boolean doTotals=true;


		//hierarchies
//		Set<ReportColumn> hierarchies=new LinkedHashSet<ReportColumn>();
//		hierarchies.add(c);
//		spec.setHierarchies(hierarchies);
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.DONOR_AGENCY, ReportEntityType.ENTITY_TYPE_ACTIVITY));
//		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_DESCRIPTION, ReportEntityType.ENTITY_TYPE_ACTIVITY));

		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));

		spec.setCalculateColumnTotals(doTotals);
		spec.setCalculateRowTotals(doTotals);
	}
	public static HSSFWorkbook generateExcelExportByLocation() {

		
		
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(TranslatorWorker.translateText("Time Stamp"));//0
		columnNames.add(TranslatorWorker.translateText("Activity Id"));//1
		columnNames.add(TranslatorWorker.translateText("Project Title"));//2
		columnNames.add(TranslatorWorker.translateText("Description"));//3
		columnNames.add(TranslatorWorker.translateText("Type"));//4
		columnNames.add(TranslatorWorker.translateText("Location Name"));//5
		columnNames.add(TranslatorWorker.translateText("Latitude"));//6
		columnNames.add(TranslatorWorker.translateText("Longitude"));//7
		columnNames.add(TranslatorWorker.translateText("GeoId"));//8
		columnNames.add(TranslatorWorker.translateText("Sectors"));//9
		columnNames.add(TranslatorWorker.translateText("Donors"));//10
		columnNames.add(TranslatorWorker.translateText("Total Project Commitments"));//11
		columnNames.add(TranslatorWorker.translateText("Total Project Disbursements"));//12

		final Map<String,Activity>geocodeInfo=new LinkedHashMap<String,Activity>();
		
		List<Activity> report=getMapExportByLocation(geocodeInfo);
		java.util.Date date = new java.util.Date();
		
		int i=1;
		List<List>rowValues=new ArrayList<>();

		for (Activity a : report) {
			List<String>coloumnsValues=new ArrayList<String>();
			coloumnsValues.add(date.toString());//0
			coloumnsValues.add(a.getAmpId());//1
			coloumnsValues.add(a.getName());//2
			coloumnsValues.add(a.getDescription());//3
			coloumnsValues.add(a.getImplementationLevel());//4
			coloumnsValues.add(geocodeInfo.get(a.getGeoCode()).getLocationName());//5
			coloumnsValues.add(geocodeInfo.get(a.getGeoCode()).getLatitude());//6
			coloumnsValues.add(geocodeInfo.get(a.getGeoCode()).getLongitude());//7
			coloumnsValues.add(a.getGeoCode());//8

			coloumnsValues.add(a.getPrimarySector());//9
			coloumnsValues.add(a.getDonorAgency());//10
			coloumnsValues.add(a.getTotalCommitments());//11
			coloumnsValues.add(a.getTotalDisbursments());//12	
			rowValues.add(coloumnsValues);
		}
		
		
		return generateExcelExport(columnNames,rowValues,"map-export-administrative-Locations.xls");
	}
}
