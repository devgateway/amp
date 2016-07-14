package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.math.BigDecimal;
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
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.IdentifiedReportCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
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
		
		AmpReportFilters filterRules = new AmpReportFilters((AmpFiscalCalendar) spec.getSettings().getCalendar());
		
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
				BigDecimal value=(BigDecimal) iter.next().value;				
				ReportCell reportcell = (ReportCell) iter.next();
				String admid = admLevelToGeoCode.get(reportcell.value);
				item.set("admID", admid);
				item.set("amount", value);
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
		
		AmpReportFilters filterRules = FilterUtils.getFilters(config, new AmpReportFilters((AmpFiscalCalendar) mrs.getCalendar()));
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
