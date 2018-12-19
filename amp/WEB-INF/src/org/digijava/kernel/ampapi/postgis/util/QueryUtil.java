package org.digijava.kernel.ampapi.postgis.util;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.FilterParam;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorAccessType;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.postgis.entity.AmpLocator;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationSkeleton;
import org.digijava.module.aim.util.OrganisationUtil;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.esrigis.dbentity.AmpApiState;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class QueryUtil {
    protected static Logger logger = Logger.getLogger(QueryUtil.class);



    public static List<String> getAdminLevels() {
        return new ArrayList<String>(Arrays.asList("Country", "Region", "Zone", "District"));
    }



    public static AmpActivity getActivity(Long ampActivityId) {
        return (AmpActivity) PersistenceManager.getSession().load(AmpActivity.class, ampActivityId);
    }
    
    /**
     * return a list of saved maps.
     * 
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static List<AmpApiState> getMapList(String type) throws DgException {
        Criteria mapsCriteria = PersistenceManager.getRequestDBSession().createCriteria(AmpApiState.class);
        mapsCriteria.add(Restrictions.eq("type", type));
        return mapsCriteria.list();
    }

    public static List<OrganizationSkeleton> getOrganizations(Long roleId) {
        AmpRole r = (AmpRole) PersistenceManager.getSession().load(AmpRole.class, roleId);
        return OrganizationSkeleton.populateOrganisationSkeletonList(r.getRoleCode());
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

    public static List<AmpLocator> getLocationsFromKeyword(final String  keyword) {
        final List <AmpLocator> locationList = new ArrayList <AmpLocator> ();
        try {
            int distance = ScoreCalculator.getMaxAllowedDistance(keyword);
            final String queryString = "select id, name, latitude,longitude, levenshtein(lower(SP_ASCII(?)), "
                    + " lower(SP_ASCII(name)), 1, 1, 1) as distance, " 
                    + " thegeometry ,"
                    + " Sp_ascii(name) anglicizedName, "
                    + " Sp_ascii(?) anglicizedKeyword " 
                    + "from amp_locator where levenshtein(lower(SP_ASCII(?)), lower(SP_ASCII(name)),1,1,1) <= " + distance
                    + " or SP_ASCII(name) like SP_ASCII(?)";
            
            PersistenceManager.getSession().doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    
                    ArrayList<FilterParam> params = new ArrayList<FilterParam>();
                    params.add(new FilterParam(keyword,  java.sql.Types.VARCHAR));
                    params.add(new FilterParam(keyword,  java.sql.Types.VARCHAR));
                    params.add(new FilterParam(keyword,  java.sql.Types.VARCHAR));
                    params.add(new FilterParam(keyword,  java.sql.Types.VARCHAR));
                    
                    try(RsInfo rsi = SQLUtils.rawRunQuery(connection, queryString, params)) {
                        ResultSet rs = rsi.rs;
                        while (rs.next()) {
                            AmpLocator locator = new AmpLocator();
                            locator.setId(rs.getLong("id"));
                            locator.setName(rs.getString("name"));
                            locator.setLatitude(rs.getString("latitude"));
                            locator.setLongitude(rs.getString("longitude"));
                            locator.setDistance(rs.getInt("distance"));
                            locator.setAnglicizedKeyword(rs.getString("anglicizedKeyword"));
                            locator.setAnglicizedName(rs.getString("anglicizedName"));
                            locator.setTheGeometry(wktToGeometry ("POINT ("+locator.getLongitude()+" "+locator.getLatitude()+")"));
                            locationList.add(locator);
                        }
                    }
                }});
        }
        catch(Exception e) {
            throw new RuntimeException(e);
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
     * Filter organization skeleton by orgGrpId
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
                    try(RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                        ResultSet rs = rsi.rs;
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
                                orgType.set("filterId", rs.getString("orgTypeName"));
                                orgTypes.add(orgType);
                            }
                            orgsGrpId.add(rs.getLong("orgGrpId"));
                        }
                    }
                }
        });
        return orgTypes;
    }

    public static List<JsonBean> getOrgGroups() {
        final List<JsonBean> orgGroups=new ArrayList<JsonBean>();
        PersistenceManager.getSession().doWork(new Work(){
                public void execute(Connection conn) throws SQLException {
                    Map<Long,String>orgGroupsNames=QueryUtil.getTranslatedName(conn, "amp_org_group", "amp_org_grp_id", "org_grp_name");
                    String query=" select aog.amp_org_grp_id orgGrpId, "+
                                 " aog.org_grp_name grpName, "+
                                 " aog.org_grp_code orgCode, "+
                                 " aog.org_type  orgType, "+
                                 " o.amp_org_id orgId  "+
                                 " from amp_org_group aog "+ 
                                 " left outer join amp_organisation o on aog.amp_org_grp_id=o.org_grp_id "+
                                 " order by orgGrpId,o.name";
                    try(RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                        ResultSet rs = rsi.rs;
                        Long lastOrgGrpId=0L;
                        List <Long>orgsId=null;
                        while (rs.next()){
                            if (!lastOrgGrpId.equals(rs.getLong("orgGrpId"))) {
                                lastOrgGrpId = rs.getLong("orgGrpId");
                                JsonBean orgGrp = new JsonBean();
                                orgsId = new ArrayList<Long>();
                                orgGrp.set("id", rs.getLong("orgGrpId"));
                                if  (orgGroupsNames != null) {
                                    orgGrp.set("name", orgGroupsNames.get(lastOrgGrpId));   
                                } else{
                                    orgGrp.set("name", rs.getString("grpName"));
                                }
                                orgGrp.set("typeId", rs.getLong("orgType"));
                                orgGrp.set("orgIds", orgsId);
                                orgGroups.add(orgGrp);
                            }
                            if (rs.getLong("orgId") != 0){
                                orgsId.add(rs.getLong("orgId"));
                            }
                        }
                    }
                }
        });
        return orgGroups;
    }
    
    /**
     * Get all organizations
     * 
     * @return List<JsonBean> organizations, each JSON contains information about organization:
     * {id : long}, {acronym : String}, {name : String}, {groupId : long}, {rolesIds : list}
     * 
     */
    public static List<JsonBean> getOrgs() {
        final List<String> roleCodes = OrganisationUtil.getVisibleRoles();
        final SortedMap<Long, JsonBean> orgs = new TreeMap<Long, JsonBean>();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                //go and fetch translated version of organisation name if multilingual is enabled
                Map<Long, String> organisationsNames = QueryUtil.getTranslatedName(conn, "amp_organisation",
                        "amp_org_id", "name");
                String query = "SELECT DISTINCT "
                        + " o.amp_org_id orgId, o.name, o.acronym, o.org_grp_id grpId, aor.role roleId "
                        + " FROM amp_org_role aor JOIN amp_organisation o ON aor.organisation=o.amp_org_id"
                        + " WHERE (o.deleted IS NULL OR o.deleted = false) "
                        + " AND aor.role IN (SELECT r.amp_role_id FROM amp_role r WHERE r.role_code IN ("
                        + Util.toCSStringForIN(roleCodes) + "))   "
                        + " UNION "
                        + " SELECT DISTINCT "
                        + " o.amp_org_id orgId, o.name, o.acronym, o.org_grp_id grpId, af.source_role_id roleId "
                        + " FROM amp_funding af JOIN amp_organisation o ON o.amp_org_id = af.amp_donor_org_id "
                        + " WHERE (o.deleted IS NULL OR o.deleted = false)"
                        + " AND af.source_role_id IN (SELECT r.amp_role_id FROM amp_role r WHERE r.role_code IN ("
                        + Util.toCSStringForIN(roleCodes) + ")) ";

                if (roleCodes.contains(Constants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION)) {
                    query += " UNION "
                            + " SELECT DISTINCT org.amp_org_id orgId, org.name, org.acronym, org.org_grp_id "
                            + " grpId, (select amp_role_id from amp_role where role_code = '"
                            + Constants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION + "') roleId "
                            + " FROM amp_components c "
                            + " JOIN amp_component_funding f ON (c.amp_component_id = f.amp_component_id) "
                            + " JOIN amp_organisation org ON org.amp_org_id = f.component_second_rep_org_id ";
                }
                if (roleCodes.contains(Constants.COMPONENT_FUNDING_ORGANIZATION)) {
                    query += " UNION "
                            + " SELECT DISTINCT org.amp_org_id orgId, org.name, org.acronym, org.org_grp_id grpId, "
                            + " (select amp_role_id from amp_role where role_code = '"
                            + Constants.COMPONENT_FUNDING_ORGANIZATION + "') roleId "
                            + " FROM amp_components c "
                            + " JOIN amp_component_funding f ON (f.amp_component_id = c.amp_component_id)"
                            + " JOIN amp_organisation org ON org.amp_org_id = f.rep_organization_id ";
                }
                query += " ORDER BY orgId";

                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                    ResultSet rs = rsi.rs;
                    Long lastOrgId = 0L;
                    JsonBean org = null;
                    Set<Long> roles = null;
                    while (rs.next()) {
                        long currentOrgId = rs.getLong("orgId");
                        if (lastOrgId.equals(currentOrgId)) {
                            roles.add(rs.getLong("roleId"));
                            orgs.get(currentOrgId).set("rolesIds", roles);
                        } else {
                            org = new JsonBean();
                            roles = new HashSet<Long>();
                            roles.add(rs.getLong("roleId"));
                            
                            org.set("id", currentOrgId);
                            org.set("acronym", rs.getString("acronym"));
                            org.set("groupId", rs.getLong("grpId"));    
                            org.set("rolesIds", roles);
                            
                            if (ContentTranslationUtil.multilingualIsEnabled()) {
                                org.set("name", organisationsNames.get(currentOrgId));
                            } else {
                                org.set("name", rs.getString("name"));
                            }
                            
                            orgs.put(currentOrgId, org);
                            lastOrgId = currentOrgId;
                        }
                    }
                }
            }
        });
        
        ArrayList<JsonBean> orgBeans = new ArrayList<JsonBean>();
        orgBeans.addAll(orgs.values());
        
        return orgBeans;
    }
    
    public static Map<Long, String> getTranslatedName(Connection conn,String tableName,String id,String name)
            throws SQLException {
        Map<Long, String> names = null;
        if (ContentTranslationUtil.multilingualIsEnabled()) {
            names  = new HashMap<Long, String>();
            ViewFetcher v = DatabaseViewFetcher.getFetcherForView(tableName,"",TLSUtils.getEffectiveLangCode(),
                    new HashMap<PropertyDescription, ColumnValuesCacher>(),conn, "*");
            try(RsInfo rs = v.fetch(null)) {
                while (rs.rs.next()) {
                    names.put(rs.rs.getLong(id),rs.rs.getString(name));
                }
            }

        }
        return names;
    }

    public static List<SimpleJsonBean> getOrgRoles() {
        // //yet not translatable but its ready when it is
        final List<String> visibleRoles = OrganisationUtil.getVisibleRoles();
        final List<SimpleJsonBean> orgRoles = new ArrayList<SimpleJsonBean>();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {

                try(RsInfo rsi = SQLUtils.rawRunQuery(conn, "select amp_role_id, name, role_code from amp_role", null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        String orgRoleCode = rs.getString("role_code");
                        if (visibleRoles != null && visibleRoles.contains(orgRoleCode)) {
                            Long orgRoleId = rs.getLong("amp_role_id");
                            String orgRoleName = rs.getString("name");
                            String displayName = TranslatorWorker.translateText(rs.getString("name"));
                            
                            SimpleJsonBean orgRole = new SimpleJsonBean(orgRoleId, orgRoleName, null, displayName);
                            orgRole.setFilterId(FiltersConstants.ORG_ROLE_CODE_TO_FILTER_ID.get(orgRoleCode));
                            
                            orgRoles.add(orgRole);
                        }
                    }
                }

            }
        });
        
        return orgRoles;
    }
    
    public static List <AmpIndicatorLayer> getIndicatorLayers () {
        return getIndicatorByCategoryValue(null);
 }

    public static List <AmpIndicatorLayer> getIndicatorByCategoryValue (String admLevel) {
            Session dbSession = PersistenceManager.getSession();
            String queryString = "select ind from "
                    + AmpIndicatorLayer.class.getName() + " ind "
                    + " left join ind.sharedWorkspaces s "
                    + " left join ind.createdBy c "
                    + " where (  "
                    + " ind.accessType = " + IndicatorAccessType.PUBLIC
                    + " or ind.accessType = " + IndicatorAccessType.STANDARD;

            AmpTeamMember current = TeamUtil.getCurrentAmpTeamMember();
            if (current != null && current.getUser() != null){
                queryString += " or (ind.accessType = " + IndicatorAccessType.SHARED + " and s.workspace.ampTeamId = " + current.getAmpTeam().getAmpTeamId() + " ) ";
                queryString += " or (ind.accessType = " + IndicatorAccessType.PRIVATE + " and c.user.id = " + current.getUser().getId() + ") ";
            }

            queryString += " ) ";

            if (admLevel != null)
               queryString += " and upper(ind.admLevel.value)=:admLevel ";

            Query qry = dbSession.createQuery(queryString);
            qry.setCacheable(true);
            if (admLevel != null)
                qry.setString("admLevel", admLevel.toUpperCase());
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
     
    public static List<String>getImplementationLocationsInUse() {
        final List<String>list = new ArrayList<String>();
                 
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String query = "select * from amp_category_value cv,amp_category_class cc "
                        + " where cv.amp_category_class_id =cc.id "
                        + " and keyname ='"+ CategoryConstants.IMPLEMENTATION_LOCATION_KEY +"'  "
                        + " and cv.id in(select distinct parent_category_value from amp_category_value_location )  "
                        + " order by cv.index_column ";     
                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                    while (rsi.rs.next()) {
                        list.add(rsi.rs.getString("category_value"));
                    }
                }
            }});
                
        return list;
    }

    public static JsonBean getLocationsForFilter() {
    
        Map<Long, LocationSkeleton> locations = LocationSkeleton.populateSkeletonLocationsList();
        long parentLocationId = PersistenceManager.getLong(PersistenceManager.getSession()
                .createSQLQuery("SELECT acvl.id FROM amp_category_value_location acvl WHERE acvl.parent_location IS NULL AND location_name=(" + 
                        "select country_name from DG_COUNTRIES WHERE iso='" + FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY) + "')")
                        .list().get(0));
        
        LocationSkeleton rootLocation = locations.get(parentLocationId);
        final JsonBean location = buildLocationsJsonBean(rootLocation, 1);
        return location;
    }
    
    private static final String[] LEVEL_TO_NAME = {"na", FiltersConstants.COUNTRY, FiltersConstants.REGION,
            FiltersConstants.ZONE, FiltersConstants.DISTRICT, FiltersConstants.COMMUNAL_SECTION, "na3", "na4"};
    
    private static JsonBean buildLocationsJsonBean(LocationSkeleton loc, int level) {
        JsonBean res = new JsonBean();
        res.set("id", loc.getId());
        res.set("name", loc.getName());
        res.set("level", level);
        res.set("filterId", LEVEL_TO_NAME[level]);
        ArrayList<JsonBean> children = new ArrayList<JsonBean>();
        for(LocationSkeleton child:loc.getChildLocations())
            children.add(buildLocationsJsonBean(child, level + 1));
        res.set("children", children);
        return res;
    }
    
    
    public static List<JsonBean> getDonors(final boolean toExcludeFilter) {
        final List<JsonBean> donors = new ArrayList<JsonBean>();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                Map<Long, String> organisationsNames = QueryUtil.getTranslatedName(conn,"amp_organisation","amp_org_id","name");
                String query =  "SELECT (o.amp_org_id) orgId, o.name, o.acronym FROM  amp_organisation o WHERE o.amp_org_id IN (" +
                                "SELECT distinct o.amp_org_id FROM  amp_organisation o, amp_org_role aor, amp_role r " +
                                "WHERE (o.amp_org_id = aor.organisation AND aor.role = r.amp_role_id AND r.role_code = 'DN') " +
                                "AND activity NOT IN (select amp_activity_id from amp_activity_version where amp_team_id IN (SELECT amp_team_id from amp_team where isolated = true))" +
                                "UNION " +
                                "SELECT distinct o.amp_org_id FROM  amp_organisation o, amp_funding af, amp_activity_version v, amp_role r   " +                
                                "WHERE  o.amp_org_id = af.amp_donor_org_id  AND v.amp_activity_id = af.amp_activity_id  AND (v.deleted is false) " +
                                "AND ((af.source_role_id IS NULL) OR af.source_role_id = r.amp_role_id and r.role_code = 'DN') "    + 
                                "AND v.amp_team_id NOT IN (SELECT amp_team_id from amp_team where isolated = true)) " +
                                "AND (o.deleted IS NULL OR o.deleted = false) ";
                         
                        if (toExcludeFilter) {
                            query += "AND o.amp_org_id NOT IN (SELECT amp_donor_id FROM amp_scorecard_organisation WHERE to_exclude = true) ";
                        }
                        
                        query +="order by o.amp_org_id";
                
                    try(RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                    ResultSet rs = rsi.rs;
                    Long lastOrgId = 0L;
                    while (rs.next()) {
                        if (!lastOrgId .equals(rs.getLong("orgId"))) {
                            lastOrgId  = rs.getLong("orgId");
                            JsonBean org = new JsonBean();
                            org.set("id", lastOrgId);
                            if (ContentTranslationUtil.multilingualIsEnabled()) {
                                org.set("name", organisationsNames.get(lastOrgId));
                            } else {
                                org.set("name", rs.getString("name"));
                            }
                            org.set("acronym", rs.getString("acronym"));
                            donors.add(org);
                        }
                    }
                }

            }
        });
        return donors;
    }
}
