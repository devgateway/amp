package org.digijava.kernel.ampapi.postgis.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.dgfoundation.amp.error.AMPException;
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
import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.helpers.geojson.objects.ClusteredPoints;
import org.digijava.kernel.ampapi.postgis.entity.AmpLocator;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.util.OrganisationUtil;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.esrigis.dbentity.AmpMapState;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class QueryUtil {
	protected static Logger logger = Logger.getLogger(QueryUtil.class);

	public static List<ClusteredPoints> getClusteredPoints(JsonBean config) throws AmpApiException {
		String adminLevel = "";

		if (config != null) {
			Object otherFilter = config.get("otherFilters");
			if (otherFilter != null
					&& ((Map<String, Object>) otherFilter).get("adminLevel") != null) {
				adminLevel = ((Map<String, Object>) otherFilter).get(
						"adminLevel").toString();
			}
		}
		//fetch activities filtered by mondrian
		
		boolean doTotals=false;
 		ReportSpecificationImpl spec = new ReportSpecificationImpl("ActivityIds");

		spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
 		MondrianReportFilters filterRules = null;

		if (config != null) {
			Object filter = config.get("columnFilters");
			if (filter != null) {
				filterRules = FilterUtils
						.getApiColumnFilter((LinkedHashMap<String, Object>) config
								.get("columnFilters"));
			}
		}
		if (filterRules != null) {
			spec.setFilters(filterRules);
		}
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class,ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		GeneratedReport report = null;		
		try {
			report = generator.executeReport(spec);
		} catch (AMPException e) {
			logger.error("Cannot execute report", e);
			throw new AmpApiException(e);
		}
		List<Long>activitiesId=new ArrayList<Long>();
		List<ReportArea> ll=null;
		ll = report.reportContents.getChildren();
		for (ReportArea reportArea : ll) {
			Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
			Set<ReportOutputColumn> col = row.keySet();
			for (ReportOutputColumn reportOutputColumn : col) {
				if(
				reportOutputColumn.originalColumnName.equals(ColumnConstants.ACTIVITY_ID)){
					activitiesId.add(new Long(row.get(reportOutputColumn).value.toString()));
				}
			}
		}
		
		List<ClusteredPoints> l = new ArrayList<ClusteredPoints>();
		
		ClusteredPoints cp = null;
		
		
		String qry = " WITH RECURSIVE rt_amp_category_value_location(id, parent_id, gs_lat, gs_long, acvl_parent_category_value, level, root_location_id,root_location_description) AS ( "
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
				+ " SELECT al.amp_activity_id, acvl.root_location_id, acvl.root_location_description, acvl.gs_lat, acvl.gs_long  "
				+ " FROM amp_activity_location al  "
				+ " join amp_location loc on al.amp_location_id = loc.amp_location_id  "
				+ " join rt_amp_category_value_location acvl on loc.location_id = acvl.id  "
				+ " where al.amp_activity_id in(" + Util.toCSStringForIN(activitiesId) + " ) "
				+ " order by acvl.root_location_id,al.amp_activity_id";
		Connection conn = null;
		try {
			conn = PersistenceManager.getJdbcConnection();
			java.sql.ResultSet rs = SQLUtils.rawRunQuery(PersistenceManager.getJdbcConnection(), qry, null);
			Long rootLocationId = 0L;
			while (rs.next()) {
				if (!rootLocationId.equals(rs.getLong("root_location_id"))) {
					if (cp != null) {
						l.add(cp);
					}
					rootLocationId = rs.getLong("root_location_id");
					cp = new ClusteredPoints();
					cp.setAdmin(rs.getString("root_location_description"));
					cp.setLat(rs.getString("gs_lat"));
					cp.setLon(rs.getString("gs_long"));
				}
				cp.getActivityids().add(rs.getLong("amp_activity_id"));
			}
			if (cp != null) {
				l.add(cp);
			}
			rs.close();

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug("cannot close connection", e);
			}
		}
		return l;
	}

	public static List<String> getAdminLevels() {
		return new ArrayList<String>(Arrays.asList("Country", "Region", "Zone", "District"));
	}

	@SuppressWarnings("unchecked")
	public static List<AmpStructure> getStructures() {
		List<AmpStructure> al = null;
		String queryString = "select s from " + AmpStructure.class.getName() + " s inner join s.activities a where"
					+ " a.ampActivityId in (select aa from " + AmpActivity.class.getName() + " aa )";
			Query q = PersistenceManager.getSession().createQuery(queryString);
			q.setMaxResults(100);
			al = q.list();

		
		return al;

	}

	public static AmpActivity getActivity(Long ampActivityId) {
		try {
			return (AmpActivity) PersistenceManager.getRequestDBSession().load(AmpActivity.class, ampActivityId);
		} catch (DgException e) {
			logger.error("cannot retrieve activity");
			return null;
		}
	}
	/**
	 * return a list of saved maps.
	 * 
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<AmpMapState> getMapList() throws DgException {
		Criteria mapsCriteria = PersistenceManager.getRequestDBSession().createCriteria(AmpMapState.class);
		return mapsCriteria.list();
	}

	public static List<OrganizationSkeleton> getOrganizations(Long roleId) {
		try {
			AmpRole r = (AmpRole) PersistenceManager.getRequestDBSession().load(AmpRole.class, roleId);
			return OrganizationSkeleton.populateOrganisationSkeletonList(r.getRoleCode());
		} catch (DgException e) {
			logger.error("cannot load orgs", e);
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public static List<AmpLocator> getLocationsWithinDistance(Geometry point, long distance, List <Long> ommitedLocations) {
		// true to use spheroid
		String queryString = "select l from " + AmpLocator.class.getName()
				+ " l where dwithin(l.theGeometry, :geometry, :distance,true) = true and l.id not in (:ommitedLocations)";
		Query q = PersistenceManager.getSession().createQuery(queryString);
		q.setParameter("geometry", point);
		q.setParameter("distance", distance);
		q.setParameterList("ommitedLocations", ommitedLocations);
		return q.list();

	}

	public static List<AmpLocator> getLocationsFromKeyword(String keyword) {
		List <AmpLocator> locationList = new ArrayList <AmpLocator> ();
		try {
			int distance = ScoreCalculator.getMaxAllowedDistance(keyword);
			String queryString = "select  id,name,latitude,longitude,levenshtein('"+keyword+"',lower (name),1,1,1) as distance,thegeometry from amp_locator where levenshtein('"+keyword+"',lower (name),1,1,1) <= "+distance+
					 " or lower (name) like '%"+keyword+"%'";
			java.sql.ResultSet rs = SQLUtils.rawRunQuery(PersistenceManager.getJdbcConnection(), queryString, null);
			while (rs.next()) {
				AmpLocator locator = new AmpLocator();
				locator.setId(rs.getLong("id"));
				locator.setName(rs.getString("name"));
				locator.setLatitude(rs.getString("latitude"));
				locator.setLongitude(rs.getString("longitude"));
				locator.setDistance(rs.getInt("distance"));
				locator.setTheGeometry(wktToGeometry ("POINT ("+locator.getLongitude()+" "+locator.getLatitude()+")"));
				locationList.add(locator);
			}
		} catch (SQLException e) {
		logger.error("Exception searching for location "+e);
		}
		return locationList;
	}
	
	private static Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (ParseException e) {
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        return geom;
    }
	
	public static List <Long> getIdsList (List <AmpLocator> locations) {
		List <Long> ids = new ArrayList <Long> ();
		for (AmpLocator locator:locations) {
			ids.add(locator.getId());
		}
		return ids;
		
	}

	/**
	 * Filter organization skelleton by orgGrpId
	 * 
	 * @param orgGrpId
	 * @return
	 */
	public static List<OrganizationSkeleton> getOrganizations(
			List<Long> orgGrpId) {
		final List<String> roleCodes = OrganisationUtil.getVisibleRoles();
		return OrganizationSkeleton
				.populateOrganisationSkeletonListByOrgGrpIp(orgGrpId, roleCodes);
	}
	
	public static List<JsonBean> getOrgTypes(){
		final List<JsonBean> orgTypes=new ArrayList<JsonBean>();
		PersistenceManager.getSession().doWork(new Work(){
				public void execute(Connection conn) throws SQLException {
					
					Map<Long, String> orgTypesName=QueryUtil.getTranslatedName(conn, "amp_org_type", "amp_org_type_id", "org_type");
					
					String query="select  aot.amp_org_type_id orgTypeId,aog.amp_org_grp_id orgGrpId " + 
								" ,aot.org_type orgTypeName, aot.org_type_code orgTypeCode " +
								" from amp_org_group aog,amp_org_type aot " +
								" where aot.amp_org_type_id=aog.org_type" +
								" order by orgTypeId";
					ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
					Long lastOrgTypeId = 0L;
					List<Long> orgsGrpId = null;
					while (rs.next()) {
						if (!lastOrgTypeId.equals(rs.getLong("orgTypeId"))) {
							lastOrgTypeId = rs.getLong("orgTypeId");
							JsonBean orgType = new JsonBean();
							orgsGrpId = new ArrayList<Long>();
							orgType.set("id", rs.getLong("orgTypeId"));
							if (orgTypesName != null){
								orgType.set("name", orgTypesName.get(lastOrgTypeId));
							} else {
								orgType.set("name", rs.getString("orgTypeName"));
							}
							orgType.set("groupIds", orgsGrpId);
							orgTypes.add(orgType);
						}
						orgsGrpId.add(rs.getLong("orgGrpId"));
					}
	
				}
	    });
	    return orgTypes;
	}

public static List<JsonBean> getOrgGroups() {
	final List<JsonBean> orgGroups=new ArrayList<JsonBean>();
    PersistenceManager.getSession().doWork(new Work(){
			public void execute(Connection conn) throws SQLException {
				
				Map<Long,String>orgGroupsNames=QueryUtil.getTranslatedName(conn, "amp_org_group", "amp_org_id", "org_grp_name");
				String query=" select aog.amp_org_grp_id orgGrpId, "+
							 " aog.org_grp_name grpName, "+
							 " aog.org_grp_code orgCode, "+
							 " aog.org_type  orgType, "+
							 " o.amp_org_id orgId  "+
							 " from amp_org_group aog "+ 
							 " left outer join amp_organisation o on aog.amp_org_grp_id=o.org_grp_id "+
							 " order by orgGrpId";
				ResultSet rs=SQLUtils.rawRunQuery(conn, query, null);
				Long lastOrgGrpId=0L;
				List <Long>orgsId=null;
				while(rs.next()){
				if(!lastOrgGrpId.equals(rs.getLong("orgGrpId"))){
					lastOrgGrpId=rs.getLong("orgGrpId");
					JsonBean orgGrp=new JsonBean();
					orgsId=new ArrayList<Long>();
					orgGrp.set("id", rs.getLong("orgGrpId"));
						if(orgGroupsNames!=null){
							orgGrp.set("name", orgGroupsNames.get(lastOrgGrpId));	
						}else{
							orgGrp.set("name", rs.getString("grpName"));
						}
					orgGrp.set("typeId", rs.getLong("orgType"));
					orgGrp.set("orgIds",orgsId);
					orgGroups.add(orgGrp);
				}
					if(rs.getLong("orgId")!=0){
						orgsId.add(rs.getLong("orgId"));
					}
				}

			}
    });
    return orgGroups;
}

	public static List<JsonBean> getOrgs() {
		final List<JsonBean> orgs = new ArrayList<JsonBean>();
		final List<String> roleCodes = OrganisationUtil.getVisibleRoles();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				
				//go and fetch translated version of organisation name if multilingual is enabled
				
				Map<Long, String> organisationsNames = QueryUtil.getTranslatedName(conn,"amp_organisation","amp_org_id","name");
				String query = " select distinct o.amp_org_id orgId, "+
						" o.name ,  "+
						" aor.role roleId , "+ 
						" o.org_grp_id grpId  "+
						" from amp_org_role aor,amp_organisation o "+
						" where aor.organisation=o.amp_org_id " +
						" and aor.role IN " +
							"(SELECT r.amp_role_id FROM amp_role r WHERE r.role_code IN (" + 
							Util.toCSStringForIN(roleCodes) + "))" +
						" order by o.amp_org_id";
				ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
				Long lastOrgId = 0L;
				List<Long> rolesId = null;
				while (rs.next()) {
					if (!lastOrgId .equals(rs.getLong("orgId"))) {
						lastOrgId  = rs.getLong("orgId");
						JsonBean org = new JsonBean();
						rolesId = new ArrayList<Long>();
						org.set("id", lastOrgId);
						if(ContentTranslationUtil.multilingualIsEnabled()){
							org.set("name", organisationsNames.get(lastOrgId));
						}else{
							org.set("name", rs.getString("name"));
						}
						org.set("groupId", rs.getLong("grpId"));	
						org.set("rolesIds", rolesId);
						orgs.add(org);
					}
					rolesId.add(rs.getLong("roleId"));
				}

			}


		});
		return orgs;
	}
	private static Map<Long, String> getTranslatedName(Connection conn,String tableName,String id,String name)
			throws SQLException {
		Map<Long, String> names = null;
		if (ContentTranslationUtil.multilingualIsEnabled()) {
			names  = new HashMap<Long, String>();
			ViewFetcher v = DatabaseViewFetcher.getFetcherForView(tableName,"",TLSUtils.getEffectiveLangCode(),
					new HashMap<PropertyDescription, ColumnValuesCacher>(),conn, "*");
			ResultSet rs = v.fetch(null);
			while (rs.next()) {
				names .put(rs.getLong(id),rs.getString(name));
			}

		}
		return names;
	}

	public static List<SimpleJsonBean> getOrgRoles() {
		// //yet not translatable but its ready when it is
		final List<SimpleJsonBean> rogRoles = new ArrayList<SimpleJsonBean>();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_role","",TLSUtils.getEffectiveLangCode(),
						new HashMap<PropertyDescription, ColumnValuesCacher>(),conn, "*");
				ResultSet rs = v.fetch(null);
				
				while (rs.next()) {
					rogRoles.add(new SimpleJsonBean(rs.getLong("amp_role_id"),rs.getString("name")));
				}

			}
		});
		return rogRoles;
	}
	
	public static List <AmpIndicatorLayer> getIndicatorLayers () {
		Session dbSession = PersistenceManager.getSession();
		String queryString = "select ind from "
				+ AmpIndicatorLayer.class.getName() + " ind";
		Query qry = dbSession.createQuery(queryString);
		qry.setCacheable(true);
		return qry.list();
 }
	
	 public static List <AmpIndicatorLayer> getIndicatorByCategoryValue (String value) {
			Session dbSession = PersistenceManager.getSession();
			String queryString = "select ind from "
					+ AmpIndicatorLayer.class.getName()
					+ " ind where upper(ind.admLevel.value)=:value)";
			Query qry = dbSession.createQuery(queryString);
			qry.setCacheable(true);
			qry.setString("value", value.toUpperCase());
			return qry.list();
		 
	 }
	 
	 @SuppressWarnings("unchecked")
		public static List<AmpCategoryValue> getClusterLevels() {
			List<AmpCategoryValue> al = null;
			String queryString = "select distinct a.parentCategoryValue from " + AmpCategoryValueLocations.class.getName()+ " a";
				Query q = PersistenceManager.getSession().createQuery(queryString);
				q.setMaxResults(100);
				al = q.list();
			return al;

		}
}

