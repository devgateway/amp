package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.IdentifiedReportCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.helpers.geojson.objects.ClusteredPoints;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.jdbc.Work;

/**
 * 
 * @author Diego Dimunzio
 *
 */

public class LocationService {
	protected static Logger logger = Logger.getLogger(LocationService.class);

	/**
	 * Get totals (actual commitments/ actual disbursements) by administrative level
	 * @param admlevel
	 * @param config json configuration
	 * @return
	 */
	public JsonBean getTotals(String admlevel, JsonBean config) {
	    EndpointUtils.useNiReports(true);
		JsonBean retlist = new JsonBean();
		HardCodedCategoryValue admLevelCV = null;
		switch (admlevel) {
		case "adm0":
			admlevel = ColumnConstants.COUNTRY; 
			admLevelCV = CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY;
			break;
		case "adm1":
			admlevel = ColumnConstants.REGION;
			admLevelCV = CategoryConstants.IMPLEMENTATION_LOCATION_REGION;
			break;
		case "adm2":
			admlevel = ColumnConstants.ZONE; 
			admLevelCV = CategoryConstants.IMPLEMENTATION_LOCATION_ZONE;
			break;
		case "adm3":
			admlevel = ColumnConstants.DISTRICT; 
			admLevelCV = CategoryConstants.IMPLEMENTATION_LOCATION_DISTRICT;
			break;
		default:
			admlevel = ColumnConstants.REGION; 
			admLevelCV = CategoryConstants.IMPLEMENTATION_LOCATION_REGION;
			break;
		}
		
		String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
		ReportSpecificationImpl spec = new ReportSpecificationImpl("LocationsTotals", ArConstants.DONOR_TYPE);
		spec.addColumn(new ReportColumn(admlevel));
		spec.getHierarchies().addAll(spec.getColumns());
		// also configures the measure(s) from funding type settings request
		SettingsUtils.applyExtendedSettings(spec, config);
		ReportSettingsImpl mrs = (ReportSettingsImpl) spec.getSettings();
		mrs.setUnitsOption(AmountsUnits.AMOUNTS_OPTION_UNITS);
		
		MondrianReportFilters filterRules = new MondrianReportFilters((AmpFiscalCalendar) spec.getSettings().getCalendar());
		
		if(config != null){
			Object columnFilters = config.get("columnFilters");
			if(columnFilters!=null){
				filterRules = FilterUtils.getApiColumnFilter(
						(LinkedHashMap<String, Object>) config.get("columnFilters"), filterRules);	
			}
 		}
		Map<String, String> admLevelToGeoCode;
		if (admlevel.equals(ColumnConstants.COUNTRY)) {
			// If the admin level is country we filter only to show projects at
			// the country of the current installation
			final ValueWrapper<String> countryId = new ValueWrapper<String>("");
			final ValueWrapper<String> countryName = new ValueWrapper<String>("");
			PersistenceManager.getSession().doWork(new Work() {
				public void execute(Connection conn) throws SQLException {
					String countryIdQuery = "select acvl.id,acvl.location_name from amp_category_value_location acvl,amp_global_settings gs "
							+ " where acvl.iso=gs.settingsvalue  " + " and gs.settingsname ='Default Country'";
					RsInfo rsi = SQLUtils.rawRunQuery(conn, countryIdQuery, null);
					if (rsi.rs.next()) {
						countryId.value = rsi.rs.getString(1);
						countryName.value = rsi.rs.getString(2);
					}
					rsi.close();
				}
			});

			filterRules.addFilterRule(new ReportColumn(ColumnConstants.COUNTRY), new FilterRule(countryId.value, true));
			//if country level we only return the current country with 0 has GeoCode
			admLevelToGeoCode = Collections.unmodifiableMap(new HashMap<String, String>() {
				{
					this.put(countryName.value, "0");
				}
			});
		} else {
			//we only get the geocodes if !country level
			admLevelToGeoCode = getAdmLevelGeoCodeMap(admlevel, admLevelCV);

		}
		

		
		spec.setFilters(filterRules);
		
		String currcode = FilterUtils.getSettingbyName(config, SettingsConstants.CURRENCY_ID);
		retlist.set("currency", currcode);
		retlist.set("numberformat", numberformat);


		GeneratedReport report = EndpointUtils.runReport(spec);
		List<JsonBean> values = new ArrayList<JsonBean>();
		
		if (report != null && report.reportContents != null && report.reportContents.getChildren() != null) {
			// find the admID (geocode) for each implementation location name
			
			for (ReportArea reportArea : report.reportContents.getChildren()) {
				JsonBean item = new JsonBean();
				Iterator<ReportCell> iter = reportArea.getContents().values().iterator();
				String displayedValue=iter.next().displayedValue;
				String admid = admLevelToGeoCode.get(displayedValue);
				item.set("admID", admid);
				ReportCell reportcell = (ReportCell) iter.next();
				item.set("amount", reportcell.value);
				if (admid!=null){
					values.add(item);
				}
			}
		}
		retlist.set("values", values);
		return retlist;
	}
	
	/**
	 * Provides admLevel name to geo code map
	 * @param admLevel
	 * @param admLevelCV
	 * @return
	 */
	public Map<String, String> getAdmLevelGeoCodeMap(String admLevel, HardCodedCategoryValue admLevelCV) {
		Set<AmpCategoryValueLocations> acvlData = DynLocationManagerUtil.getLocationsByLayer(admLevelCV);
		Map<String, String> levelToGeoCodeMap = new HashMap<String, String>();
		if (acvlData != null) {
			for (AmpCategoryValueLocations acvl : acvlData) {
				levelToGeoCodeMap.put(acvl.getName(), acvl.getGeoCode());
			}
		}
		return levelToGeoCodeMap;
	}
	
	/**
	 * Build an excel file export by structure
	 * @return
	 */
	public static HSSFWorkbook generateExcelExportByStructure(LinkedHashMap<String, Object> filters){
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(TranslatorWorker.translateText("Time Stamp"));//1
		columnNames.add(TranslatorWorker.translateText("Activity Id"));//2
		columnNames.add(TranslatorWorker.translateText("Project Title"));//3
		columnNames.add(TranslatorWorker.translateText("Project Site Description"));//4
//		columnNames.add(TranslatorWorker.translateText("Approval Date"));
		columnNames.add(TranslatorWorker.translateText("Project Site"));//5
		columnNames.add(TranslatorWorker.translateText("Latitude"));//6
		columnNames.add(TranslatorWorker.translateText("Longitude"));//7
//		columnNames.add(TranslatorWorker.translateText("Project Site"));//8
		columnNames.add(TranslatorWorker.translateText("Sectors"));//9
		columnNames.add(TranslatorWorker.translateText("Donors"));//10
		columnNames.add(TranslatorWorker.translateText("Total Project Commitments"));//11 
		columnNames.add(TranslatorWorker.translateText("Total Project Disbursements"));//12
		
		
		List<Activity> report=getMapExportByStructure(filters);
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
	public static List<Activity> getMapExportByLocation(final Map<String,Activity>geocodeInfo,LinkedHashMap<String, Object> filters) {
		List<Activity> activities = new ArrayList<Activity>();
		final List<String> geoCodesId = new ArrayList<String>();
		ReportSpecificationImpl spec = new ReportSpecificationImpl("MapExport", ArConstants.DONOR_TYPE);
		Set<ReportColumn> hierarchies = new LinkedHashSet<ReportColumn>();
		ReportColumn ampId = new ReportColumn(ColumnConstants.AMP_ID);

		ReportColumn geoid = new ReportColumn(ColumnConstants.GEOCODE);

		ReportColumn impLevel = new ReportColumn(ColumnConstants.IMPLEMENTATION_LEVEL);
		spec.addColumn(geoid);

		spec.addColumn(ampId);
		spec.addColumn(impLevel);
		spec.addColumn(geoid);
		hierarchies.add(ampId);
		hierarchies.add(impLevel);
		hierarchies.add(geoid);

		spec.setHierarchies(hierarchies);

		getCommonSpecForExport(spec);

		ReportExecutor generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		GeneratedReport report = null;
		
		
		applyFilters((LinkedHashMap<String, Object>)filters.get("otherFilters"),(LinkedHashMap<String, Object>)filters.get("columnFilters"), spec);
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
		if (report != null && report.reportContents != null
				&& report.reportContents.getChildren() != null) {
			for (ReportArea reportArea : report.reportContents.getChildren()) {
				getActivitiesById(reportArea, activities, geoCodesId);
			}
			// Go and fetch location specific information
			if(geoCodesId.size()==0){
				return activities;
			}
			PersistenceManager.getSession().doWork(new Work() {
				public void execute(Connection conn) throws SQLException {

					String query = "select geo_code,location_name,gs_lat,gs_long from "
							+ " amp_category_value_location   "
							+ "where  geo_code in ("
							+ org.dgfoundation.amp.Util.toCSString(geoCodesId)
							+ ")";
					try(RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
						ResultSet rs = rsi.rs;
						while (rs.next()) {
							Activity a = new Activity();
							String geoCode = rs.getString("geo_code");
							a.setLocationName(rs.getString("location_name"));
							a.setLatitude(rs.getString("gs_lat"));
							a.setLongitude(rs.getString("gs_long"));
							a.setGeoCode(geoCode);
							geocodeInfo.put(geoCode, a);
						}
					}
				}
			});
			Collections.sort(activities, new Comparator<Activity>() {
				@Override
				public int compare(Activity a, Activity b) {
					return a.getAmpId().compareTo(b.getAmpId());
				}
			});
		}
		return activities;
	}
	private static void applyFilters(LinkedHashMap<String, Object> otherFilter,LinkedHashMap<String, Object>columnFilters,
			ReportSpecificationImpl spec) {
		List<String> activitIds=null;
		if (otherFilter != null) {
			activitIds = FilterUtils.applyKeywordSearch( otherFilter);
		}
		
 		MondrianReportFilters filterRules = FilterUtils.getFilterRules(columnFilters,
				otherFilter, activitIds);
		if(filterRules!=null){
			spec.setFilters(filterRules);
		}
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
					if(columnValue==null || columnValue.equals("Undefined")|| columnValue.equals("") || columnValue.equals("GeoId: Undefined")){
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
	public static List<Activity> getMapExportByStructure(LinkedHashMap<String, Object> filters) {
		final List<Activity> mapExportBean = new ArrayList<Activity>();

		ReportSpecificationImpl spec = new ReportSpecificationImpl("MapExport", ArConstants.DONOR_TYPE);
		//since amp_id will be added as a hiearchy onthe other report
		spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID));
		spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
		getCommonSpecForExport(spec);

		ReportExecutor generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()),false);
		GeneratedReport report = null;
		applyFilters((LinkedHashMap<String, Object>)filters.get("otherFilters"),(LinkedHashMap<String, Object>)filters.get("columnFilters"), spec);
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
		final Map<Long,Activity>activities=new LinkedHashMap<Long,Activity>();

		if (report != null && report.reportContents != null
				&& report.reportContents.getChildren() != null) {
			for (ReportArea reportArea : report.reportContents.getChildren()) {
				Long activityId = 0L;
				Activity activity = new Activity();
				Map<ReportOutputColumn, ReportCell> row = reportArea
						.getContents();
				Set<ReportOutputColumn> col = row.keySet();
				for (ReportOutputColumn reportOutputColumn : col) {
					if (reportOutputColumn.originalColumnName
							.equals(ColumnConstants.ACTIVITY_ID)) {
						activityId = Long
								.parseLong(row.get(reportOutputColumn).value
										.toString());
						activity.setId(activityId);
					} else {
						getActivityIdForReports(activity, row,
								reportOutputColumn);
					}
				}
				activities.put(activityId, activity);
			}
			if(activities.size()==0){
				return mapExportBean;
			}
		//once we have all activities we go and fetch structures associated to those activities
		//since its not yet implemented on reports we fetch them separately 
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				
				
				String query="select ast.amp_activity_id,s.title, "+
						" s.latitude, "+
						" s.longitude ,"
						+ " s.description"+
						"  from amp_activity_structures ast , amp_structure s  " +
						" where ast.amp_structure_id=s.amp_structure_id "+
						" and ast.amp_activity_id in("+ org.dgfoundation.amp.Util.toCSString(activities.keySet()) +")";
	    		try(RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
	    			ResultSet rs = rsi.rs;
	    			while (rs.next()) {
	    				Activity a = activities.get(rs.getLong("amp_activity_id"));
	    				Activity newActivity = new Activity();
	    				newActivity.setId(a.getId());
	    				newActivity.setAmpId(a.getAmpId());
	    				newActivity.setName(a.getName());
	    				newActivity.setTotalCommitments(a.getTotalCommitments());
	    				newActivity.setTotalDisbursments(a.getTotalDisbursments());
	    				newActivity.setDonorAgency(a.getDonorAgency());
	    				newActivity.setImplementationLevel(a.getImplementationLevel());
	    				newActivity.setPrimarySector(a.getPrimarySector());

	    				newActivity.setStructureName(rs.getString("title"));
	    				newActivity.setLatitude(rs.getString("latitude"));
	    				newActivity.setLongitude(rs.getString("longitude"));
	    				newActivity.setDescription(rs.getString("description"));
	    			
	    				mapExportBean.add(newActivity);
	    			}	
	    		}	
			}
			});
		}
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
		//hierarchies
//		Set<ReportColumn> hierarchies=new LinkedHashSet<ReportColumn>();
//		hierarchies.add(c);
//		spec.setHierarchies(hierarchies);
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
//		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_DESCRIPTION, ReportEntityType.ENTITY_TYPE_ACTIVITY));

		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
	}
	public static HSSFWorkbook generateExcelExportByLocation(LinkedHashMap<String, Object> filters) {

		
		
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(TranslatorWorker.translateText("Time Stamp"));//0
		columnNames.add(TranslatorWorker.translateText("Activity Id"));//1
		columnNames.add(TranslatorWorker.translateText("Project Title"));//2
//		columnNames.add(TranslatorWorker.translateText("Description"));//3
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
		
		List<Activity> report=getMapExportByLocation(geocodeInfo,filters);
		java.util.Date date = new java.util.Date();
		
		int i=1;
		List<List>rowValues=new ArrayList<>();

		for (Activity a : report) {
			List<String>coloumnsValues=new ArrayList<String>();
			coloumnsValues.add(date.toString());//0
			coloumnsValues.add(a.getAmpId());//1
			coloumnsValues.add(a.getName());//2
			//coloumnsValues.add(a.getDescription());//3
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
	
	public static List<ClusteredPoints> getClusteredPoints(JsonBean config) throws AmpApiException {
		String adminLevel = "";
		final List<ClusteredPoints> l = new ArrayList<ClusteredPoints>();

		if (config != null) {
			Object otherFilter = config.get("otherFilters");
			if (otherFilter != null
					&& ((Map<String, Object>) otherFilter).get("adminLevel") != null) {
				adminLevel = ((Map<String, Object>) otherFilter).get(
						"adminLevel").toString();
			}
		}
		
		final String usedAdminLevel = adminLevel;
		Set<Long> activitiesId = getActivitiesForFiltering(config, adminLevel);
		
		final Double countryLatitude=FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.COUNTRY_LATITUDE);
		final Double countryLongitude=FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.COUNTRY_LONGITUDE);
		final ValueWrapper<String> qry = new ValueWrapper<String>(null);
		if(adminLevel.equals("Country")){
					qry.value = " SELECT al.amp_activity_id, acvl.id root_location_id,acvl.location_name root_location_description,acvl.gs_lat, acvl.gs_long "+  
					" FROM amp_activity_location al   "+
					" join amp_location loc on al.amp_location_id = loc.amp_location_id  "+
					" join amp_category_value_location acvl on loc.location_id = acvl.id  "+
					" join amp_category_value amcv on acvl.parent_category_value =amcv.id "+  
					" where amcv.category_value ='Country' "+
					" and al.amp_activity_id in(" + Util.toCSStringForIN(activitiesId) + " ) " +
					" and location_name=(select country_name "
					+ " from DG_COUNTRIES where iso='"+ FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY) +"')";

			
		}else{
		qry.value = " WITH RECURSIVE rt_amp_category_value_location(id, parent_id, gs_lat, gs_long, acvl_parent_category_value, level, root_location_id,root_location_description) AS ( "
				+ " select acvl.id, acvl.parent_location, acvl.gs_lat, acvl.gs_long, acvl.parent_category_value, 1, acvl.id,acvl.location_name  "
				+ " from amp_category_value_location acvl  "
				+ " join amp_category_value amcv on acvl.parent_category_value =amcv.id  "
				+ " where amcv.category_value ='"
				+ adminLevel
				+ "'  "
				+ " and acvl.gs_lat is not null and acvl.gs_long is not null  "
				+ " UNION ALL  "
				+ " SELECT acvl.id, acvl.parent_location, rt.gs_lat, rt.gs_long, acvl.parent_category_value, rt.LEVEL + 1, rt.root_location_id, rt.root_location_description  "
				+ " FROM rt_amp_category_value_location rt, amp_category_value_location acvl  "
				+ " WHERE acvl.parent_location =rt.id  "
				+ " )  "
				+ " SELECT distinct al.amp_activity_id, acvl.root_location_id, acvl.root_location_description, acvl.gs_lat, acvl.gs_long  "
				+ " FROM amp_activity_location al  "
				+ " join amp_location loc on al.amp_location_id = loc.amp_location_id  "
				+ " join rt_amp_category_value_location acvl on loc.location_id = acvl.id  "
				+ " where al.amp_activity_id in(" + Util.toCSStringForIN(activitiesId) + " ) "
				+ " order by acvl.root_location_id,al.amp_activity_id";
		}

		try {
			PersistenceManager.getSession().doWork(new Work() {

				@Override
				public void execute(Connection connection) throws SQLException {
					try(RsInfo rsi = SQLUtils.rawRunQuery(connection, qry.value, null)) {
						ResultSet rs = rsi.rs;
						ClusteredPoints cp = null;
						Long rootLocationId = 0L;
						while (rs.next()) {
							if (!rootLocationId.equals(rs.getLong("root_location_id"))) {
								if (cp != null) {
									l.add(cp);
								}
								rootLocationId = rs.getLong("root_location_id");
								cp = new ClusteredPoints();
								cp.setAdmin(rs.getString("root_location_description"));
								if (usedAdminLevel.equals("Country")){
									cp.setLat(countryLatitude.toString());
									cp.setLon(countryLongitude.toString());							
								}else{
									cp.setLat(rs.getString("gs_lat"));
									cp.setLon(rs.getString("gs_long"));
								}
							}
							cp.getActivityids().add(rs.getLong("amp_activity_id"));
						}
						if (cp != null) {
							l.add(cp);
						}
					}
				}});
		}
		catch(HibernateException e){
			throw new RuntimeException(e);
		}
	
		return l;
	}
	private static Set<Long> getActivitiesForFiltering(JsonBean config, String adminLevel)
			throws AmpApiException {
	    EndpointUtils.useNiReports(true);
		Set<Long> activitiesId = new HashSet<Long>();
		 
		ReportSpecificationImpl spec = new ReportSpecificationImpl("ActivityIdsForCluster", ArConstants.DONOR_TYPE);

		spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
		// AMP-20903 - In order to not have inconsistency with data used in gis map, DONOR_ID was added 
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
		
		OutputSettings outSettings = new OutputSettings(new HashSet<String>() {{add(ColumnConstants.AMP_ID);}});
		
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
		ReportColumn implementationLevelColumn = null;
		if (adminLevel != null) {
			switch (adminLevel) {
				case ColumnConstants.COUNTRY:
					implementationLevelColumn = new ReportColumn(MoConstants.H_COUNTRIES);
					break;
				case ColumnConstants.REGION:
					implementationLevelColumn = new ReportColumn(MoConstants.H_REGIONS);
					break;
				case ColumnConstants.ZONE:
					implementationLevelColumn = new ReportColumn(MoConstants.H_ZONES);
					break;
				case ColumnConstants.DISTRICT:
					implementationLevelColumn = new ReportColumn(MoConstants.H_DISTRICTS);
					break;
			}
		}

		if(implementationLevelColumn != null){
			spec.addColumn(implementationLevelColumn);
			Set<ReportColumn>implementationLevelHierarchy=new HashSet<ReportColumn>();
			implementationLevelHierarchy.add(implementationLevelColumn);
			spec.setHierarchies(implementationLevelHierarchy);
		}
		spec.setDisplayEmptyFundingRows(true);
		
		SettingsUtils.applyExtendedSettings(spec, config);
		ReportSettingsImpl mrs = (ReportSettingsImpl) spec.getSettings();
		mrs.setUnitsOption(AmountsUnits.AMOUNTS_OPTION_UNITS);
		
		MondrianReportFilters filterRules = FilterUtils.getFilters(config, new MondrianReportFilters((AmpFiscalCalendar) mrs.getCalendar()));
		if (filterRules != null) {
			spec.setFilters(filterRules);
		}

		GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, outSettings);
		
		List<ReportArea> ll = null;
		ll = report.reportContents.getChildren();
		if (ll != null) {
			for (ReportArea reportArea : ll) {
				if (implementationLevelColumn != null) {
					List<ReportArea> childrenHierarchy = reportArea.getChildren();

					for (ReportArea reportAreachi : childrenHierarchy) {
						Map<ReportOutputColumn, ReportCell> row = reportAreachi.getContents();
						Set<ReportOutputColumn> col = row.keySet();
						for (ReportOutputColumn reportOutputColumn : col) {
							if (reportOutputColumn.originalColumnName.equals(ColumnConstants.AMP_ID)) {
								activitiesId.add(((IdentifiedReportCell) row.get(reportOutputColumn)).entityId);
							}
						}
					}

				} else {
					// we don't have hierarchy
					Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
					Set<ReportOutputColumn> col = row.keySet();
					for (ReportOutputColumn reportOutputColumn : col) {
						if (reportOutputColumn.originalColumnName.equals(ColumnConstants.AMP_ID)) {
							activitiesId.add(((IdentifiedReportCell) row.get(reportOutputColumn)).entityId);
						}
					}
				}
			}
		}
		return activitiesId;
	}
	@SuppressWarnings("unchecked")
	public static List<AmpStructure> getStructures(JsonBean config) throws AmpApiException{
		List<AmpStructure> al = null;
		Set<Long> activitiesId = getActivitiesForFiltering( config,null);
		String queryString = "select s from " + AmpStructure.class.getName() + " s inner join s.activities a where"
					+ " a.ampActivityId in (" + Util.toCSStringForIN(activitiesId) + " )";
		Query q = PersistenceManager.getSession().createQuery(queryString);
		al = q.list();
		return al;

	}	
}
