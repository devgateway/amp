/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.FilterParam;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicatorOption;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.amp_togole;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureImg;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.ActivityForm;
import org.digijava.module.aim.helper.ActivityHistory;
import org.digijava.module.aim.helper.ActivityItem;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.IdWithValueShim;
import org.digijava.module.common.util.DateTimeUtil;
import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.joda.time.Period;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils.ACTIVITY_FM_ID;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils.WORKSPACE_PREFIX;
import static org.digijava.kernel.translator.util.TrnUtil.PREFIXES;

public class ActivityUtil {

  private static Logger logger = Logger.getLogger(ActivityUtil.class);

  private static final  Integer MILLISECONDS_IN_A_DAY = 1000 * 60 * 60 * 24;

  public static List<AmpComponent> getComponents(Long actId) {
    Session session = null;
    List<AmpComponent> col = new ArrayList<AmpComponent>();
    logger.info(" this is the other components getting called....");
    try {
      session = PersistenceManager.getRequestDBSession();
      String rewrittenColumns = SQLUtils.rewriteQuery("amp_components", "ac", 
              new HashMap<String, String>(){{
                  put("title", InternationalizedModelDescription.getForProperty(AmpComponent.class, "title").getSQLFunctionCall("ac.amp_component_id"));
                  put("description", InternationalizedModelDescription.getForProperty(AmpComponent.class, "description").getSQLFunctionCall("ac.amp_component_id"));
              }});      
      String queryString = "select " + rewrittenColumns + " from amp_components ac " +
            "where (ac.amp_activity_id=:actId)";
      Query qry = session.createSQLQuery(queryString).addEntity(AmpComponent.class);
      qry.setParameter("actId", actId, LongType.INSTANCE);
      col = qry.list();
    }
    catch (Exception e) {
      logger.error("Unable to get all components");
      logger.error(e.getMessage());
    }
    return col;
  }

  /**
   * Searches activities.
   * Please note that this method is too slow if there are too many activities, because hibernate should load them all. Please use pagination.
   * @param ampThemeId filter by program
   * @param statusCode filter by status if not null
   * @param donorOrgId filter by donor org if not null
   * @param fromDate filter by date if not null
   * @param toDate filter by date if not null
   * @param locationId filter by location if not null
   * @param teamMember filter by team if not null
   * @param pageStart if null then 0 is assumed.
   * @param rowCount number of activities to return
   * @return list of activities.
   * @throws DgException
   */
  public static Collection<AmpActivityVersion> searchActivities(Long ampThemeId,
      String statusCode,
      String donorOrgId,
      Date fromDate,
      Date toDate,
      Long locationId,
      TeamMember teamMember,Integer pageStart,Integer rowCount) throws DgException{
    Collection<AmpActivityVersion> result = null;
    try {
      Session session = PersistenceManager.getRequestDBSession();

      String oql = "select distinct act from " + AmpActivityProgram.class.getName() + " prog ";
      oql+= getSearchActivitiesWhereClause(ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
      oql += " order by act.name";

      Query query = session.createQuery(oql);
      
      setSearchActivitiesQueryParams(query, ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
      
      if (pageStart!=null && rowCount!=null){
          query.setFirstResult(pageStart);
          query.setMaxResults(rowCount);
      }
      
      result = query.list();
    }
    catch (Exception ex) {
      throw new DgException("Cannot search activities for NPD",ex);
    }

    return result;
  }

  /**
   * <p>Searches activities which are assigened to specific program and returns collection of {@link ActivityItem} objects.
   * Each object is created using {@link ActivityItem#ActivityItem(AmpActivity,Long)} constructor.</p>
   * <p>Please note that this method is too slow if there are too many activities, because hibernate should load them all. Please use pagination.</p>
   * @param ampThemeId filter by program
   * @param statusCode filter by status if not null
   * @param donorOrgId filter by donor org if not null
   * @param fromDate filter by date if not null
   * @param toDate filter by date if not null
   * @param locationId filter by location if not null
   * @param teamMember filter by team if not null
   * @param pageStart if null then 0 is assumed.
   * @param rowCount number of activities to return
   * @return list of activities.
   * @see ActivityItem
   * @throws DgException
   */
  public static Collection<ActivityItem> searchActivitieProgPercents(Long ampThemeId,
      String statusCode,
      String donorOrgId,
      Date fromDate,
      Date toDate,
      Long locationId,
      TeamMember teamMember,Integer pageStart,Integer rowCount) throws DgException{
      List<ActivityItem> result = null;
    try {
      Session session = PersistenceManager.getRequestDBSession();


      String oql = "select distinct  new  org.digijava.module.aim.helper.ActivityItem(latestAct,prog.programPercentage) " +
            "   from " + AmpActivityProgram.class.getName() + " prog ";

      oql+= getSearchActivitiesWhereClause(ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
    
      
      Query query = session.createQuery(oql);

      setSearchActivitiesQueryParams(query, ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);

      if (pageStart!=null && rowCount!=null){
          query.setFirstResult(pageStart);
          query.setMaxResults(rowCount);
      }

      result = query.list();
    }
    catch (Exception ex) {
      throw new DgException("Cannot search activities for NPD",ex);
    }

    return result;
  }


  /**
   * Count how man activities will find search without pagination.
   * This method is used together with {@link #searchActivities(Long, String, Long, Date, Date, Long, TeamMember, Integer, Integer)}
   * @param ampThemeId
   * @param statusCode
   * @param donorOrgId
   * @param fromDate
   * @param toDate
   * @param locationId
   * @param teamMember
   * @return
   * @throws DgException
   */
  public static Integer searchActivitiesCount(Long ampThemeId,
          String statusCode,
          String donorOrgId,
          Date fromDate,
          Date toDate,
          Long locationId,
          TeamMember teamMember) throws DgException{
        Integer result = null;
        try {
          Session session = PersistenceManager.getRequestDBSession();
          String oql = "select count(distinct latestAct) from " + AmpActivityProgram.class.getName() + " prog ";
          oql += getSearchActivitiesWhereClause(ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
          Query query = session.createQuery(oql);

          setSearchActivitiesQueryParams(query, ampThemeId, statusCode, donorOrgId, fromDate, toDate, locationId, teamMember);
          
          result = (Integer)query.uniqueResult();
        }
        catch (Exception ex) {
          throw new DgException("Cannot count activities for NPD",ex);
        }

        return result;
      }

  /**
   * Setups query string where clause for search and count methods.
   * @param ampThemeId
   * @param statusCode
   * @param donorOrgId
   * @param fromDate
   * @param toDate
   * @param locationId
   * @param teamMember
   * @return
   * @see #searchActivities(Long, String, Long, Date, Date, Long, TeamMember, Integer, Integer)
   * @see #searchActivitiesCount(Long, String, Long, Date, Date, Long, TeamMember)
   */
  public static String getSearchActivitiesWhereClause(Long ampThemeId,
          String statusCode,
          String donorOrgId,
          Date fromDate,
          Date toDate,
          Long locationId,
          TeamMember teamMember) {
      
      String oql="";
      
      if (ampThemeId!=null){
          oql += " inner join prog.program as theme ";
      }
      oql+=" inner join prog.activity as  act ";
      oql+=" inner join act.ampActivityGroup grp";
      oql+=" inner join grp.ampActivityLastVersion latestAct";
      if (statusCode!=null && !"".equals(statusCode.trim())){
          oql+=" join  latestAct.categories as categories ";
      }
      StringBuilder whereTeamStatement=new StringBuilder();
      boolean relatedOrgsCriteria=false;
      if (teamMember != null) {
          //oql += " and " +getTeamMemberWhereClause(teamMember);
          AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
          if (teamMember.getComputation()!=null&&teamMember.getComputation()) {
              String ids = OrganisationUtil.getComputationOrgsQry(team);
              if(ids.length()>1){
              ids = ids.substring(0, ids.length() - 1);
                whereTeamStatement.append("  and ( latestAct.team.ampTeamId =:teamId or  role.organisation.ampOrgId in(" + ids+"))");
              }
              relatedOrgsCriteria=true;
          }
          else{
                if (team.getAccessType().equals("Management")) {
                    whereTeamStatement.append(String.format(
                            " and (latestAct.draft=false or latestAct.draft is null) "
                                    + "and latestAct.approvalStatus IN ('%s', '%s') ",
                            ApprovalStatus.APPROVED.getDbName(),
                            ApprovalStatus.STARTED_APPROVED.getDbName()));
                    List<AmpTeam> teams = new ArrayList<AmpTeam>();
                    TeamUtil.getTeams(team, teams);
                    String relatedOrgs = "", teamIds = "";
                    for (AmpTeam tm : teams) {
                        if (tm.getComputation() != null && tm.getComputation()) {
                            relatedOrgs += OrganisationUtil.getComputationOrgsQry(tm);
                            relatedOrgsCriteria=true;
                        }
                        teamIds += tm.getAmpTeamId() + ",";
                    }
                    if (relatedOrgs.length() > 1) {
                        relatedOrgs = relatedOrgs.substring(0,
                                relatedOrgs.length() - 1);
                        whereTeamStatement.append("  and ( latestAct.team.ampTeamId ="+team.getAmpTeamId()+" or  role.organisation.ampOrgId in("
                                + relatedOrgs + "))");
                    }
                    if (teamIds.length() > 1) {
                        teamIds = teamIds.substring(0, teamIds.length() - 1);
                        whereTeamStatement.append(" and latestAct.team.ampTeamId in ( " + teamIds
                                + ")");
                    }

                } else {
                    whereTeamStatement.append(" and ( latestAct.team.ampTeamId =:teamId ) ");
          }
          }
        
      }
      if(relatedOrgsCriteria){
          oql+=" inner join latestAct.orgrole role ";
      }
      oql+=" where 1=1 ";
      if (ampThemeId != null) {
          oql += " and ( theme.ampThemeId = :ampThemeId) ";
        }
      if (donorOrgId != null&&!donorOrgId.trim().equals("")) {
        String s = " and latestAct in (select f.ampActivityId from " +
             AmpFunding.class.getName() + " f inner join f.ampDonorOrgId org " +
            " where org.ampOrgId in ("+donorOrgId+")) ";
        oql += s;
      }
      if (statusCode != null&&!"".equals(statusCode.trim())) {
        oql += " and categories.id in ("+statusCode+") ";
      }
      if (fromDate != null) {
        oql += " and (latestAct.actualStartDate >= :FromDate or (latestAct.actualStartDate is null and latestAct.proposedStartDate >= :FromDate) )";
      }
      if (toDate != null) {
        oql += " and (latestAct.actualStartDate <= :ToDate or (latestAct.actualStartDate is null and latestAct.proposedStartDate <= :ToDate) ) ";
      }
      if (locationId != null) {
        throw new RuntimeException("filtering by location not implemented");
      }
      oql+=whereTeamStatement.toString();
      return oql;
  }

  public static void setSearchActivitiesQueryParams(Query query, Long ampThemeId,
          String statusCode,
          String donorOrgId,
          Date fromDate,
          Date toDate,
          Long locationId,
          TeamMember teamMember) {
      
      if (ampThemeId != null) {
          query.setLong("ampThemeId", ampThemeId.longValue());
        }
      
        if (fromDate != null) {
          query.setDate("FromDate", fromDate);
        }
        if (toDate != null) {
          query.setDate("ToDate", toDate);
        }
        if (locationId != null) {
          query.setLong("LocationID", locationId.longValue());
        }
        if (teamMember!=null && teamMember.getTeamId()!=null&&!teamMember.getTeamAccessType().equals("Management")){
          query.setLong("teamId", teamMember.getTeamId());
        }
        
  }

  @SuppressWarnings("unchecked")
public static List<AmpTheme> getActivityPrograms(Long activityId) {
      String relName = AmpActivityProgram.class.getName();
      String queryString = "SELECT prog FROM " + relName + " prog WHERE prog.activity.ampActivityId=:actId";
      return PersistenceManager.getSession().createQuery(queryString).setParameter("actId", activityId, LongType.INSTANCE).list();
  }

  public static List<AmpActivityLocation> getActivityLocations(Long activityId) {
      String queryString = "select locs.* from amp_activity_location locs where (locs.amp_activity_id=:actId) ";
      return PersistenceManager.getSession().createSQLQuery(queryString).addEntity(AmpActivityLocation.class)
              .setParameter("actId", activityId, LongType.INSTANCE).list();
  }
    
    
    /**
     * Load activity from db.
     * Use this one instead of method below this if you realy want to load all data.
     * @author irakli
     * @param id
     * @return
     * @throws DgException
     */
    public static AmpActivityVersion loadActivity(Long id) throws DgException {
        AmpActivityVersion result = null;
        Session session = PersistenceManager.getRequestDBSession();
        try {
//session.flush();
            result = (AmpActivityVersion) session.get(AmpActivityVersion.class, id);
            session.evict(result);
            result = (AmpActivityVersion) session.get(AmpActivityVersion.class, id);
            Hibernate.initialize(result.getInternalIds());
            Hibernate.initialize(result.getLocations());
            Hibernate.initialize(result.getSectors());
            Hibernate.initialize(result.getFunding());
            if (result.getFunding() != null) {
                for(Object obj:result.getFunding()){
                    AmpFunding funding = (AmpFunding) obj;
                    Hibernate.initialize(funding.getFundingDetails());
                    Hibernate.initialize(funding.getMtefProjections());
                }
            }
            Hibernate.initialize(result.getActivityDocuments());
            Hibernate.initialize(result.getComponents());
            Hibernate.initialize(result.getOrgrole());
            //we need to initialize role from org role
            if (result.getOrgrole() != null) {
                for (AmpOrgRole or : result.getOrgrole()) {
                    Hibernate.initialize(or.getRole());
                }
            }
            Hibernate.initialize(result.getIssues());
            Hibernate.initialize(result.getRegionalObservations());
            Hibernate.initialize(result.getStructures());
            for(AmpStructure str:result.getStructures()) {
                Hibernate.initialize(str.getImages());
                Hibernate.initialize(str.getType());
                Hibernate.initialize(str.getCoordinates());
            }

            // AMPOFFLINE-1528
            ActivityUtil.setCurrentWorkspacePrefixIntoRequest(result);

            ActivityUtil.initializeForApi(result);
            
        } catch (ObjectNotFoundException e) {
            logger.debug("AmpActivityVersion with id=" + id + " not found");
        } catch (Exception e) {
            throw new DgException("Cannot load AmpActivityVersion with id " + id, e);
        }
        return result;
    }

    public static void initializeForApi(AmpActivityVersion activity) {
        // initialize the fiscal year list field. Used in Activity API only
        initializeFiscalYears(activity);
    }

    /**
     * Initialize Fiscal Years list object in activity. Used in Activity API only.
     *
     * @param activity
     */
    private static void initializeFiscalYears(AmpActivityVersion activity) {
        if (activity.getFiscalYears() == null || activity.getFiscalYears().size() == 0) {
            List<Long> fiscalYears = new ArrayList<>();
            if (StringUtils.isNotBlank(activity.getFY())) {
                try {
                    List<String> years = Arrays.asList(activity.getFY().split(","));
                    for (String year : years) {
                        fiscalYears.add(Long.parseLong(year));
                    }
                    activity.setFiscalYears(new HashSet<>(fiscalYears));
                } catch (NumberFormatException e) {
                    logger.error("Error in parsing numbers of FY field - " + activity.getFY(), e);
                }
            }
        }
    }

    public static Long findActivityIdByAmpId(String ampId) {
        Session session = PersistenceManager.getRequestDBSession();
        return (Long) session.createQuery("select ampActivityId from AmpActivity where ampId=:ampId")
                .setParameter("ampId", ampId)
                .uniqueResult();
    }


    public static List<AmpActivityVersion> getLastActivitiesVersionByAmpIds(List<String> ampIds) {
        String queryString = "select a from "
                + AmpActivityVersion.class.getName()
                + " a where a.ampId in (:ampIds) "
                + " and a.ampActivityId =( select av.ampActivityId from " + AmpActivity.class.getName() + " av where av"
                + ".ampId = a.ampId)";
        return PersistenceManager.getSession().createQuery(queryString)
                .setParameterList("ampIds", ampIds)
                .list();
    }

    public static List<AmpActivityVersion> getActivitiesByAmpIds(List<String> ampIds) {
        String queryString = "select a from "
                + AmpActivity.class.getName()
                + " a where a.ampId in (:ampIds) ";
        return PersistenceManager.getSession().createQuery(queryString)
                .setParameterList("ampIds", ampIds)
                .list();
    }
  
  public static AmpActivityVersion loadAmpActivity(Long id){
     return (AmpActivityVersion) PersistenceManager.getSession().load(AmpActivityVersion.class, id); 
  }
 
  public static List<AmpActivitySector> getAmpActivitySectors(Long actId) {
      String queryString = "select a.* from amp_activity_sector a " + "where a.amp_activity_id=:actId";
      return PersistenceManager.getSession().createSQLQuery(queryString).addEntity(AmpActivitySector.class)
              .setParameter("actId", actId, LongType.INSTANCE).list();
  }

  public static List<AmpOrgRole> getOrgRole(Long id) {
      String queryString = "select aor from " + AmpOrgRole.class.getName() +
          " aor " + "where (aor.activity=:actId)";
      return PersistenceManager.getSession().createQuery(queryString).setParameter("actId", id, LongType.INSTANCE).list();
  }

  public static amp_togole getAmpRole(Long actId, Long orgRoleId) {
        Session session = null;
        AmpRole role = null;
        try {
          session = PersistenceManager.getSession();
          String queryString = "select ar.* from amp_role ar " +
                "inner join amp_org_role aor on (aor.role = ar.amp_role_id) " +
                "inner join amp_activity aa on (aa.amp_activity_id = aor.activity) " +
                "where (aa.amp_activity_id=:actId) and (aor.amp_org_role_id=:orgRoleId)";
          Query qry = session.createSQLQuery(queryString).addEntity(AmpRole.class);
          qry.setParameter("actId", actId, LongType.INSTANCE);
          qry.setParameter("orgRoleId", orgRoleId, LongType.INSTANCE);
          if ((qry.list() != null) && (qry.list().size()>0)) {
              role = (AmpRole)qry.list().get(0);
          }
        }
        catch (Exception ex) {
          logger.error("Unable to get amprole :" + ex);
        }
        return role;
      }

  public static List<AmpOrgRole> getAmpRolesForActivityAndOrganizationsAndRole(Long actId, List<Long> organizationId,
                                                                               Long roleId) {
      List<AmpOrgRole> ampOrgRoles = new ArrayList<>();
      if (organizationId != null && organizationId.size() > 0) {
          String queryString = "select ar from AmpOrgRole ar "
                  + " where ar.activity.ampActivityId = :actId "
                  + " and ar.organisation.ampOrgId in ( :orgId ) "
                  + " and ar.role.ampRoleId = :roleId ";
          Query qry = PersistenceManager.getSession().createQuery(queryString);
          qry.setParameter("actId", actId);
          qry.setParameterList("orgId", organizationId);
          qry.setParameter("roleId", roleId);
          ampOrgRoles = qry.list();
      }

      return ampOrgRoles;
  }

  public static AmpOrganisation getAmpOrganisation(Long actId, Long orgRoleId) {
        Session session = null;
        AmpOrganisation organisation = null;
        try {
          session = PersistenceManager.getSession();
          String rewrittenColumns = SQLUtils.rewriteQuery("amp_organisation", "ao", 
                  new HashMap<String, String>(){{
                      put("name", InternationalizedModelDescription.getForProperty(AmpOrganisation.class, "name").getSQLFunctionCall("ao.amp_org_id"));
                      put("description", InternationalizedModelDescription.getForProperty(AmpOrganisation.class, "description").getSQLFunctionCall("ao.amp_org_id"));
                    }});
          String queryString = "select " + rewrittenColumns + " from amp_organisation ao " +                "inner join amp_org_role aor on (aor.organisation = ao.amp_org_id) " +
                "inner join amp_activity aa on (aa.amp_activity_id = aor.activity) " +
                "where (aa.amp_activity_id=:actId) and (aor.amp_org_role_id=:orgRoleId)";
          Query qry = session.createSQLQuery(queryString).addEntity(AmpOrganisation.class);
          qry.setParameter("actId", actId, LongType.INSTANCE);
          qry.setParameter("orgRoleId", orgRoleId, LongType.INSTANCE);
          if ((qry.list() != null) && (qry.list().size()>0)) {
              organisation = (AmpOrganisation) qry.list().get(0);
          }
        }
        catch (Exception ex) {
          logger.error("Unable to get AmpOrganisation :" + ex);
        }
        return organisation;
      }

    public static List<String> getAmpIdsByFundingOrg(Long id) {
        String queryString = "select a.ampId "
                + "from " + AmpFunding.class.getName() + " f, " + AmpActivity.class.getName() + " a "
                + "where f.ampActivityId=a.ampActivityId "
                + "and (f.ampDonorOrgId=:orgId)";
        Query qry = PersistenceManager.getSession().createQuery(queryString);
        qry.setParameter("orgId", id, LongType.INSTANCE);
        return (List<String>) qry.list();
    }

  public static Collection<Components> getAllComponents(Long id) {
    Collection<Components> componentsCollection = new ArrayList<Components>();

    Session session = null;

    try {
      session = PersistenceManager.getSession();
      AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
      Set comp = activity.getComponents();
      if (comp != null && comp.size() > 0) {
        Iterator itr1 = comp.iterator();
        while (itr1.hasNext()) {
          AmpComponent ampComp = (AmpComponent) itr1.next();
          Components<FundingDetail> components = new Components<FundingDetail>();
          components.setComponentId(ampComp.getAmpComponentId());
          components.setDescription(ampComp.getDescription());
          components.setType_Id((ampComp.getType()!=null)?ampComp.getType().getType_id():null);
          components.setTitle(ampComp.getTitle());
          components.setCommitments(new ArrayList());
          components.setDisbursements(new ArrayList());
          components.setExpenditures(new ArrayList());

          Collection<AmpComponentFunding> componentsFunding = ampComp.getFundings();
          Iterator compFundIterator = componentsFunding.iterator();
          while (compFundIterator.hasNext()) {
            AmpComponentFunding cf = (AmpComponentFunding) compFundIterator.next();
            FundingDetail fd = new FundingDetail();
            fd.setAdjustmentTypeName(cf.getAdjustmentType());
 
            fd.setCurrencyCode(cf.getCurrency().getCurrencyCode());
            fd.setCurrencyName(cf.getCurrency().getCurrencyName());
            fd.setTransactionAmount(FormatHelper.formatNumber(cf.getTransactionAmount().doubleValue()));
            fd.setTransactionDate(DateConversion.convertDateToLocalizedString(cf.getTransactionDate()));
            fd.setFiscalYear(DateConversion.convertDateToFiscalYearString(cf.getTransactionDate()));
            fd.setTransactionType(cf.getTransactionType().intValue());
            if (fd.getTransactionType() == Constants.COMMITMENT) {
              components.getCommitments().add(fd);
            }
            else if (fd.getTransactionType() == Constants.DISBURSEMENT) {
              components.getDisbursements().add(fd);
            }
            else if (fd.getTransactionType() == Constants.EXPENDITURE) {
              components.getExpenditures().add(fd);
            }
          }
          List list = null;
          if (components.getCommitments() != null) {
            list = new ArrayList(components.getCommitments());
            Collections.sort(list, FundingValidator.dateComp);
          }
          components.setCommitments(list);
          list = null;
          if (components.getDisbursements() != null) {
            list = new ArrayList(components.getDisbursements());
            Collections.sort(list, FundingValidator.dateComp);
          }
          components.setDisbursements(list);
          list = null;
          if (components.getExpenditures() != null) {
            list = new ArrayList(components.getExpenditures());
            Collections.sort(list, FundingValidator.dateComp);
          }
          components.setExpenditures(list);
          componentsCollection.add(components);
        }
      }

    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
    }
    return componentsCollection;
  }

  /*
   * edited by Govind Dalwani
   */
  // this function is to get the fundings for the components along with the activity Id

  public static Collection<AmpComponentFunding> getFundingComponentActivity(Long componentId) {
      logger.debug(" inside getting the funding.....");
      String qryStr = "select a from " + AmpComponentFunding.class.getName() +
          " a " +
          "where amp_component_id = '" + componentId + "'";
      Query qry = PersistenceManager.getSession().createQuery(qryStr);
      return qry.list();
  }
  
  public static Collection<AmpActivityVersion> getOldActivities(Session session,int size,Date date){
      List<AmpActivityVersion> colAv;
        Collection<AmpActivityVersion> colAll = new ArrayList<AmpActivityVersion>();
        logger.info(" inside getting the old activities.....");
        try {

            List result = session.createSQLQuery("Select * from ( select amp_activity_id, amp_activity_group_id, date_updated, rank() over (PARTITION BY amp_activity_group_id order by date_updated desc) as rank from amp_activity_version order by amp_activity_group_id) as SQ where sq.rank > "+size).list();
            Iterator iter = result.iterator();
            List<Long> idActivities = new ArrayList<Long>();
            while(iter.hasNext()){
                Object[] objects = (Object[]) iter.next();
                 BigInteger id = (BigInteger) objects[0];
                 idActivities.add(id.longValue());
            }
            if(idActivities.size()>0){
                String qryGroups = "select av from "
                        + AmpActivityVersion.class.getName()+" av where av.ampActivityId in:list";
                Query qry = session.createQuery(qryGroups);
                qry.setParameterList("list", idActivities);
                ////System.out.println(result.size());
                colAv = qry.list();
                for(AmpActivityVersion act:colAv){
                    if (act.getUpdatedDate().before(date))
                        colAll.add(act);
                }
            }

        } catch (Exception e) {
            logger.debug("Exception in getOldActivities() " + e.getMessage());
        }
        return colAll;
  }

  // function for getting fundings for components and ids ends here

  
  
  /**
   * checks whether the 'name' activity title exists as a translation in any language 
   * in any latest version of an activity
   * excluding the activity group of the current activity
   * @param name title of the activity
   * @param g activity group of the activity in question
   * @return null if no collisions found or IdWithValueShim with amp_activity_id and team name in case of collision
   */
  public static IdWithValueShim getActivityCollisions(final String name, final AmpActivityGroup g) {
      final IdWithValueShim result = new IdWithValueShim(-1l, "");
      PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                List<FilterParam> params = new ArrayList<FilterParam>();
                String groupClause = "";

                if (g != null) {
                    groupClause = "AND amp_activity_id NOT IN "
                            + "(SELECT amp_activity_id FROM amp_activity_version WHERE amp_activity_group_id = ?) ";
                    params.add(new FilterParam(g.getAmpActivityGroupId(), java.sql.Types.BIGINT));
                }

                String query = "SELECT aav.amp_activity_id, team.name FROM amp_activity_version aav "
                        + "LEFT OUTER JOIN amp_team team ON aav.amp_team_id = team.amp_team_id "
                        + "WHERE amp_activity_id IN (SELECT amp_activity_last_version_id FROM amp_activity_group) "
                        + groupClause
                        + "AND (amp_activity_id IN (SELECT object_id FROM amp_content_translation "
                        + "WHERE object_class = 'org.digijava.module.aim.dbentity.AmpActivityVersion' "
                        + "AND field_name='name' AND translation = ?) "
                        + "OR aav.name = ?)";

                params.add(new FilterParam(name, java.sql.Types.VARCHAR));
                params.add(new FilterParam(name, java.sql.Types.VARCHAR));

                try(RsInfo rsi = SQLUtils.rawRunQuery(conn, query, params)) {
                    while (rsi.rs.next()) {
                        Long id = rsi.rs.getLong(1);
                        String teamName = rsi.rs.getString(2);
                        result.setId(id);
                        result.setValue(teamName);
                    }
                }
            }
      });
      if (result.getId() == -1l)
          return null;
      return result;
  
  }
      
    public static List<AmpActivityVersion> getSortedActivitiesByDonors (List<AmpActivityVersion> acts, boolean acs) {
        List<AmpActivityVersion> retVal = new ArrayList<AmpActivityVersion>();

        Map<String, AmpActivityVersion> donorNameActivityMap = new HashMap<String, AmpActivityVersion> ();
        List<AmpActivityVersion> noFundingActivities = null;
        for (AmpActivityVersion actItem : acts) {
            if (actItem.getFunding() != null && !actItem.getFunding().isEmpty()) {                
                StringBuilder donorNames = new StringBuilder();
                
                List<AmpFunding> organizations = new ArrayList<>(actItem.getFunding());
                if (organizations != null && organizations.size() > 1) {
                    Collections.sort(organizations, new Comparator<AmpFunding>() {
                        public int compare(AmpFunding o1, AmpFunding o2) {
                            return o1.getAmpDonorOrgId().getName().compareTo(o2.getAmpDonorOrgId().getName());
                        }
                    });
                }

                for (Object fndObj : organizations) {
                    AmpFunding fnd = (AmpFunding) fndObj;
                    donorNames.append(fnd.getAmpDonorOrgId().getName());
                    donorNames.append(",");
                }
                donorNameActivityMap.put(donorNames.toString(), actItem);

            } else {
                if (noFundingActivities == null) {
                    noFundingActivities = new ArrayList <AmpActivityVersion> ();
                }
                noFundingActivities.add(actItem);
            }
        }
        
        Set <String> keys = donorNameActivityMap.keySet();
        List <String> sortedKeys = new ArrayList <String> (keys);
        Collections.sort(sortedKeys);
        if (!acs) {
            Collections.reverse(sortedKeys);
        }
        
        for (String key : sortedKeys) {
            retVal.add(donorNameActivityMap.get(key));
        }

        if (noFundingActivities != null) {
            retVal.addAll(noFundingActivities);
        }
        
        return retVal;
    }

    /*
   * get the  the Contracts for Activity
   * 
   */
  public static List<IPAContract> getIPAContracts(Long activityId) {
    
      String queryString = "select con from " + IPAContract.class.getName()
          + " con " + "where (con.activity=:activityId)";
      Query qry = PersistenceManager.getSession().createQuery(queryString).setLong("activityId",activityId );
      List<IPAContract> contrcats = qry.list();
      String cc = "";
      for(IPAContract c:contrcats){
          cc = c.getTotalAmountCurrency().getCurrencyCode();
          double td = 0;
          for(IPAContractDisbursement cd:c.getDisbursements())
          {
              if (cd.getAmount() != null)
                  td += cd.getAmount().doubleValue();
          }
          if(c.getDibusrsementsGlobalCurrency()!=null)
               cc=c.getDibusrsementsGlobalCurrency().getCurrencyCode();
          c.setTotalDisbursements(new Double(td));
          c.setExecutionRate(ActivityUtil.computeExecutionRateFromTotalAmount(c, c.getTotalAmountCurrency().getCurrencyCode()));
          c.setFundingTotalDisbursements(ActivityUtil.computeFundingDisbursementIPA(c, cc));
          c.setFundingExecutionRate(ActivityUtil.computeExecutionRateFromContractTotalValue(c, cc));  
      }
      return  contrcats ;
  } 

    public static double computeFundingDisbursementIPA(IPAContract contract, String cc){
        
        ArrayList<AmpFundingDetail> disbs1 = (ArrayList<AmpFundingDetail>) DbUtil.getDisbursementsFundingOfIPAContract(contract);                
        //if there is no disbursement global currency saved in db we'll use the default from edit activity form
        
       if(contract.getTotalAmountCurrency()!=null)
           cc=contract.getTotalAmountCurrency().getCurrencyCode();
        double td=0;
        double usdAmount=0;  
        double finalAmount=0; 

        for(Iterator<AmpFundingDetail> j=disbs1.iterator();j.hasNext();)
        {
            AmpFundingDetail fd=(AmpFundingDetail) j.next();
          // converting the amount to the currency from the top and adding to the final sum.
          if(fd.getTransactionAmount()!=null)
              {
                try {
                    usdAmount = CurrencyWorker.convertToUSD(fd.getTransactionAmount().doubleValue(),fd.getAmpCurrencyId().getCurrencyCode());
                } catch (AimException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    finalAmount = CurrencyWorker.convertFromUSD(usdAmount,cc);
                } catch (AimException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                td+=finalAmount;
              }
         }
//          contract.setFundingTotalDisbursements(td);
//          contract.setFundingExecutionRate(ActivityUtil.computeExecutionRateFromContractTotalValue(contract, cc));
        return td;
    }
  
    public static double computeExecutionRateFromContractTotalValue(IPAContract c, String currCode){
        double usdAmount1=0;  
           double finalAmount1=0; 
        try {
            if(c.getContractTotalValue()!=null && c.getTotalAmountCurrency().getCurrencyCode()!=null)   
                usdAmount1 = CurrencyWorker.convertToUSD(c.getContractTotalValue().doubleValue(),c.getTotalAmountCurrency().getCurrencyCode());
            else usdAmount1 = 0.0;
            } catch (AimException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
                try {
                finalAmount1 = CurrencyWorker.convertFromUSD(usdAmount1,currCode);
            } catch (AimException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }   
          
          double execRate=0;
          if(finalAmount1!=0)
              execRate=c.getFundingTotalDisbursements()/finalAmount1;
          c.setExecutionRate(execRate);
          return execRate;
    }

    public static double computeExecutionRateFromTotalAmount(IPAContract c, String currCode){
        double usdAmount1=0;  
           double finalAmount1=0; 
        try {
            if(c.getTotalAmount()!=null && c.getTotalAmountCurrency()!=null )   
                usdAmount1 = CurrencyWorker.convertToUSD(c.getTotalAmount().doubleValue(),c.getTotalAmountCurrency().getCurrencyCode());
            else usdAmount1=0.0;
            } catch (AimException e) {
                e.printStackTrace();
            }
                try {
                finalAmount1 = CurrencyWorker.convertFromUSD(usdAmount1,currCode);
            } catch (AimException e) {
                e.printStackTrace();
            }   
          
          double execRate=0;
          if(finalAmount1!=0)
              execRate=c.getTotalDisbursements()/finalAmount1;
          c.setExecutionRate(execRate);
          return execRate;
    }

    /**
     * returns a set of all ampActivityIds passed by the workspace filter
     * @param session
     */
    public static Set<Long> fetchLongs(final String usedQuery)
    {
        Set<Long> ampActivityIds = new TreeSet<Long>();
        List<Object> res;
        
        if (usedQuery.contains(":")) {
            // slower but always works
            res = PersistenceManager.getSession().doReturningWork(new ReturningWork<List<Object>>() {
                    @Override public List<Object> execute(Connection connection) throws SQLException {
                        return (List) SQLUtils.fetchLongs(connection, usedQuery);
                    }});
        }
        else {
            res = PersistenceManager.getSession().createSQLQuery(usedQuery).list();
        }
        for(Object aaa:res)
        {
            Long ampActivityId = PersistenceManager.getLong(aaa);
            ampActivityIds.add(ampActivityId);
        }
        return ampActivityIds;
    }
    
    /**
     * returns a set of all ampActivityIds passed by the workspace filter
     * @param session
     */
    public static Set<Long> getAllLegalAmpActivityIds() {
        return fetchLongs(WorkspaceFilter.getWorkspaceFilterQuery(TLSUtils.getRequest().getSession()));
    }
    
    public static List<AmpActivityFake> getLastUpdatedActivities() {
        String workspaceQuery = Util.toCSStringForIN(getAllLegalAmpActivityIds());
        
        List<AmpActivityFake> res = new ArrayList<AmpActivityFake>();
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select ampAct.ampActivityId, ampAct.ampId, " + AmpActivityVersion.hqlStringForName("ampAct") + " from "
                + AmpActivityVersion.class.getName()
                + " ampAct where ampAct.ampActivityId IN (" + workspaceQuery + ")"
                + " AND (ampAct.deleted = false or ampAct.deleted is null) AND (ampAct.team.id IS NOT NULL) "
                + " order by ampAct.ampActivityId desc";
            qry = session.createQuery(queryString).setMaxResults(5);
            List<Object[]> results = qry.list();
            for(Object[] activityInfo:results)
            {
                AmpActivityFake activityDigest = new AmpActivityFake(PersistenceManager.getString(activityInfo[2]), PersistenceManager.getString(activityInfo[1]), PersistenceManager.getLong(activityInfo[0]));
                res.add(activityDigest);
            }
        } catch (Exception e1) {
            logger.error("Could not retrieve the activities list from getLastUpdatedActivities", e1);
        }
        return res;
    }
  
  /*
   * this function is so incredibly slow that you should NEVER use it. Left it here because it is used by one very old (most probably unused) page
   */
    public static List<AmpActivityVersion> getAllActivitiesList() {
      String queryString = "select ampAct from " + AmpActivityVersion.class.getName() + " ampAct";
      return PersistenceManager.getSession().createQuery(queryString).list();
  }
  
  public static List<AmpActivityVersion> getActivitiesWhichMatchDate(String dateField, Date value) {
      Date minDate = new Date(value.getTime() - 24 * 3600l * 1000l);
      Date maxDate = new Date(value.getTime() + 24 * 3600l * 1000l);
      String queryString = String.format(
              "select ampAct from %s ampAct WHERE (ampAct.team IS NOT NULL) AND " + 
            "(ampAct.%s >= :minDate) AND (ampAct.%s <= :maxDate)",
              AmpActivityVersion.class.getName(), dateField, dateField);
      List<AmpActivityVersion> aavs = PersistenceManager.getSession()
              .createQuery(queryString)
              .setDate("minDate", minDate)
              .setDate("maxDate", maxDate)
              .list();
      try {
          List<AmpActivityVersion> res = new ArrayList<>();
          Method m = AmpActivityVersion.class.getMethod("get" + Character.toUpperCase(dateField.charAt(0)) + dateField.substring(1));
          for(AmpActivityVersion aav:aavs) {
              Date date = (Date) m.invoke(aav);
              if (date.getDay() == value.getDay())
                  res.add(aav);
          }
          return res;
      }
      catch(Exception e) {
          throw new RuntimeException(e);
      }
  }

    public static List<AmpActivityVersion> getActivitiesPendingValidation() {

        String daysToValidation = FeaturesUtil
                .getGlobalSettingValue(GlobalSettingsConstants.NUMBER_OF_DAYS_BEFORE_AUTOMATIC_VALIDATION);

        String queryString = String.format(
                "select ampAct from %s ampAct WHERE amp_activity_id in ( select act.ampActivityId from %s act "
                + " where draft = false and not amp_team_id is null and "
                        + " date_updated <= ( current_date - %s ) and approval_status in ( %s ))" ,
                AmpActivityVersion.class.getName(), AmpActivity.class.getName(), daysToValidation, Constants.ACTIVITY_NEEDS_APPROVAL_STATUS);

        return PersistenceManager.getSession()
                .createQuery(queryString)
                .list();

    }

  /*
   * get the list of all the activities
   * to display in the activity manager of Admin
   */
  public static List<AmpActivityVersion> getAllActivitiesByName(String name) {
      String queryString = "select ampAct from " + AmpActivityVersion.class.getName() +
              String.format(" ampAct where upper(%s) like upper(:name)",
                      AmpActivityVersion.hqlStringForName("ampAct"));
      return PersistenceManager.getSession().createQuery(queryString)
              .setParameter("name", "%" + name + "%", StringType.INSTANCE)
              .list();
  }
  
  private static AmpActivityGroup getActivityGroups(Session session, Long actId) {
      String queryString = "select group from " + AmpActivityGroup.class.getName() + " group "
                      + "where group.ampActivityLastVersion.ampActivityId=:actId";
      return (AmpActivityGroup) session.createQuery(queryString)
              .setParameter("actId", actId)
              .uniqueResult();
  }

    public static void deleteActivityContent(AmpActivityVersion ampAct, Session session) throws Exception{

        logger.info("deleting ... Activity # " + ampAct.getAmpActivityId());
        Connection con = ((SessionImplementor)session).connection();
        //   delete surveys
        String deleteActivitySurveyResponse = "DELETE FROM amp_ahsurvey_response WHERE amp_ahsurvey_id in ( SELECT amp_ahsurvey_id FROM amp_ahsurvey WHERE amp_activity_id = " + ampAct.getAmpActivityId() + " ) ";
        SQLUtils.executeQuery(con, deleteActivitySurveyResponse );

        String deleteActivitySurvey = "DELETE FROM amp_ahsurvey WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        SQLUtils.executeQuery(con, deleteActivitySurvey );

        //   delete surveys
        String deleteActivityGPISurveyReponse = "DELETE FROM amp_gpi_survey_response WHERE amp_gpisurvey_id in ( SELECT amp_gpisurvey_id FROM amp_gpi_survey WHERE amp_activity_id = " + ampAct.getAmpActivityId() + " ) " ;
        SQLUtils.executeQuery(con, deleteActivityGPISurveyReponse );

        String deleteActivityGPISurvey = "DELETE FROM amp_gpi_survey WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        SQLUtils.executeQuery(con, deleteActivityGPISurvey );

        //   delete all previous comments
        String deleteActivityComments = "DELETE FROM amp_comments WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        SQLUtils.executeQuery(con, deleteActivityComments );

        //Delete the connection with Team.
        String deleteActivityTeam = "DELETE FROM amp_team_activities WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        SQLUtils.executeQuery(con, deleteActivityTeam );
    }
    
    public static void removeMergeSources(Long ampActivityId,Session session){
        String queryString1 = "select act from " + AmpActivityVersion.class.getName() + " act where (act.mergeSource1=:activityId)";
        String queryString2 = "select act from " + AmpActivityVersion.class.getName() + " act where (act.mergeSource2=:activityId)";
        Query qry1 = session.createQuery(queryString1);
        Query qry2 = session.createQuery(queryString2);
        qry1.setParameter("activityId", ampActivityId, LongType.INSTANCE);
        qry2.setParameter("activityId", ampActivityId, LongType.INSTANCE);
        
        Collection col =qry1.list();
        if (col != null && col.size() > 0) {
            Iterator<AmpActivityVersion> itrAmp = col.iterator();
            while(itrAmp.hasNext()){
                AmpActivityVersion actVersion = itrAmp.next();
                actVersion.setMergeSource1(null);
                session.update(actVersion);
            }
        }
        col =qry2.list();
        if (col != null && col.size() > 0) {
            Iterator<AmpActivityVersion> itrAmp = col.iterator();
            while(itrAmp.hasNext()){
                AmpActivityVersion actVersion = itrAmp.next();
                actVersion.setMergeSource2(null);
                session.update(actVersion);
            }
        }
        
    }
  
  /**
   * @deprecated
   *
   * vlimansky
   * Deprecated. But to refactor it a lot of efforts is needed
   * Also, it is used in one place only
   * In fact I do not see it's slow
   *
   * VERY SLOW. Use {@link #TeamUtil.getAllTeamAmpActivitiesResume(Long, boolean, String, String...)}
   * @param ampActIds
   * @return
   */
  public static List<AmpActivity> getActivities(Set<Long> ampActIds){
      if (ampActIds == null || ampActIds.size() == 0) {
          return new ArrayList<AmpActivity>();
      } else {
          String queryString = "select a from "
                  + AmpActivity.class.getName()
                  + " a where a.ampActivityId in(:ampActIds) ";

          return PersistenceManager.getSession().createQuery(queryString)
                  .setParameterList("ampActIds", ampActIds)
                  .setCacheable(false).list();
      }
  }
  
  public static void deleteActivityIndicatorsSession(Long ampActivityId,Session session) throws Exception{
        Collection col = null;
        Query qry = null;
        String queryString = "select indAct from "
                + IndicatorActivity.class.getName() + " indAct "
                + " where (indAct.activity=:ampActId)";
        qry = session.createQuery(queryString);
        qry.setParameter("ampActId", ampActivityId, LongType.INSTANCE);
        col = qry.list();
        
        Iterator itrIndAct = col.iterator();
        while(itrIndAct.hasNext()){
            IndicatorActivity indAct =(IndicatorActivity)itrIndAct.next();
            session.delete(indAct);
        }
      
  }

  public static void deleteActivityIndicators(Collection activityInd, AmpActivityVersion activity, Session session) throws Exception {
    
            if (activityInd != null && activityInd.size() > 0) {
                for (Object indAct : activityInd) {

                    AmpIndicator ind = (AmpIndicator) session.get(AmpIndicator.class, ((IndicatorActivity) indAct).getIndicator().getIndicatorId());
                    IndicatorActivity indConn = IndicatorUtil.findActivityIndicatorConnection(activity, ind);
                    IndicatorUtil.removeConnection(indConn);
                }
            }
  }

  //TODO change so it calculates days to automatic validation
    public static ValidationStatus getValidationStatus(AmpActivityVersion activity, TeamMember tm) {
        ValidationStatus vs = ValidationStatus.UNKNOWN;
        if (!activity.getDraft()) {
            AmpTeam currentTeam = null;
            if (tm != null) {
                currentTeam = TeamUtil.getAmpTeam(tm.getTeamId());
            }
            boolean hasTeamLeadOrValidator = false;
            if (currentTeam != null) {
                List<AmpTeamMember> valids = TeamMemberUtil.getTeamHeadAndApprovers(currentTeam.getAmpTeamId());
                if (valids != null && valids.size() > 0) {
                    hasTeamLeadOrValidator = true;
                }
            }
            if (Constants.ACTIVITY_NEEDS_APPROVAL_STATUS_SET.contains(activity.getApprovalStatus())) {
                if (hasTeamLeadOrValidator) {
                    if (ActivityUtil.isAutomaticValidationEnabled()) {
                        return ValidationStatus.AUTOMATIC_VALIDATION;
                    } else {
                        return ValidationStatus.AWAITING_VALIDATION;
                    }
                } else {
                    return ValidationStatus.CANNOT_BE_VALIDATED;
                }
            }
        }
        return vs;
    }

    /* functions to DELETE an activity by Admin end here.... */


  public static class ActivityAmounts {
    private Double proposedAmout;
    private Double actualAmount;
    private Double actualDisbAmount;

     public void AddActualDisb(double amount) {
      if (actualDisbAmount != null) {
        actualDisbAmount = new Double(actualDisbAmount + amount);
      }
      else {
        actualDisbAmount = amount;
      }
    }

    public void AddActual(double amount) {
      if (actualAmount != null) {
        actualAmount = new Double(actualAmount.doubleValue() + amount);
      }
      else {
        actualAmount = new Double(amount);
      }
    }

    public String actualAmount() {
      if (actualAmount == null || actualAmount == 0) {
        return "N/A";
      }
      return FormatHelper.formatNumber(actualAmount);
    }

    public String actualDisbAmount() {
      if (actualDisbAmount == null || actualDisbAmount == 0) {
        return "N/A";
      }
      return FormatHelper.formatNumber(actualDisbAmount);
    }

    public String proposedAmout() {
      if (proposedAmout == null) {
        return "N/A";
      }
      return FormatHelper.formatNumber(proposedAmout);
    }

    public void setProposedAmout(double proposedAmout) {
      this.proposedAmout = new Double(proposedAmout);
    }


      public double getActualDisbAmoount() {
          if (actualDisbAmount == null) {
              return 0;
          }
          return actualDisbAmount;
      }

     public void setActualDisbAmount(Double actualDisbAmount) {
          this.actualDisbAmount = actualDisbAmount;
      }

    public double getActualAmount() {
      if (actualAmount == null) {
        return 0;
      }
      return actualAmount.doubleValue();
    }

    public double getProposedAmout() {
      if (proposedAmout == null) {
        return 0;
      }
      return proposedAmout.doubleValue();
    }
  }

  public static ActivityAmounts getActivityAmmountIn(AmpActivityVersion act,
      String tocode,Float percent, boolean donorFundingOnly) throws Exception {
    double tempProposed = 0;
    double tempActual = 0;
    double tempPlanned = 0;
    ActivityAmounts result = new ActivityAmounts();
    percent=(percent==null)?100:percent;

    AmpCategoryValue statusValue = CategoryManagerUtil.
        getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY,act.getCategories());

    if (act != null && statusValue != null) {
        AmpFundingAmount ppc = act.getProjectCostByType(AmpFundingAmount.FundingType.PROPOSED);
      if (CategoryConstants.ACTIVITY_STATUS_PROPOSED.equalsCategoryValue(statusValue) && ppc != null && ppc.getFunAmount() != null) {
        String currencyCode = ppc.getCurrencyCode();
        //AMP-1403 assume USD if no code is specified
        if (currencyCode == null || currencyCode.trim().equals("")) {
          currencyCode = "USD";
        } //end of AMP-1403
        //apply program percent
        tempProposed = CurrencyWorker.convert(ppc.getFunAmount().doubleValue()*percent/100,tocode);
        result.setProposedAmout(tempProposed);
      }
      else {

          Set<AmpFunding> fundings = act.getFunding();
          if (fundings != null) {
              Iterator<AmpFunding> fundItr = act.getFunding().iterator();
              while(fundItr.hasNext()) {
                  AmpFunding ampFunding = fundItr.next();
                  org.digijava.module.aim.logic.FundingCalculationsHelper calculations = new org.digijava.module.aim.logic.FundingCalculationsHelper();
                  calculations.doCalculations(ampFunding, tocode);
                  //apply program percent
                  result.AddActual(calculations.getTotActualComm().doubleValue()*percent/100);
                  result.AddActualDisb(calculations.getTotActualDisb().doubleValue()*percent/100);
              }
          }
        }

    }
    return result;
  }

  public static List<AmpActivityProgram> getActivityProgramsByProgramType(Long actId, String settingName) {
      String queryString = "select ap from " +AmpActivityProgram.class.getName() +
              " ap join ap.programSetting s where (ap.activity=:actId) and (s.name=:settingName)";
      return PersistenceManager.getSession().createQuery(queryString).setLong("actId",actId).setString("settingName",settingName).list();
  }
 
  public static class HelperAmpActivityNameComparator
        implements Comparator {
        public int compare(Object obj1, Object obj2) {
            AmpActivityVersion act1 = (AmpActivityVersion) obj1;
            AmpActivityVersion act2 = (AmpActivityVersion) obj2;
            return (act1.getName()!=null && act2.getName()!=null)?act1.getName().compareTo(act2.getName()):0; 
        }
    }


  /**
   * generates ampId
   * @param user,actId
   * @return ampId
   * @author dare
 * @param session 
   */
  public static String generateAmpId(User user, Long actId, Session session) {
      String globSetting = "numeric";// TODO This should come from global settings
      if (globSetting.equals("numeric")){
          return numericAmpId(user, actId, session);
      }
      else
          return combinedAmpId(actId);
    }
  
/**
 * combines countryId, current member id and last activityId+1 and makes ampId
 * @param user,actId
 * @return 
 * @author dare
 * @param session 
 */
    private static String numericAmpId(User user, Long actId, Session session){
        String countryCode = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY);
        String userId = user.getId().toString();
        Country country = (Country) session.load(Country.class, countryCode);   
        String countryId = "0";
        if (country != null){
            countryId = country.getCountryId().toString();
        }
        
        String lastId = null;
        if (actId != null){
            lastId = actId.toString();  
        }
        return countryId + userId + lastId;
    }

    /**
     * combines countryIso and last activityId+1 and makes ampId
     * @param actId
     * @return 
     * @author dare
     */
    private static String combinedAmpId(Long actId){
        String retVal = null;
        String countryCode = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY);
        String lastId = null;
        if (actId != null){
             lastId = actId.toString(); 
        }   
        retVal = countryCode.toUpperCase() + "/" + lastId;      
        return retVal;
    }
    
    public static List<AmpActivityVersion> getActivitiesRelatedToAmpTeamMember(Session session, Long ampTeamMemberId) {
        String queryStr = "SELECT a FROM " + AmpActivityVersion.class.getName()  + " a left join a.member m WHERE " +
                "(a.activityCreator=:atmId) OR (a.modifiedBy=:atmId) OR (a.approvedBy = :atmId) OR (m.ampTeamMemId = :atmId)  OR (a.modifiedBy = :atmId)";
        return session.createQuery(queryStr).setLong("atmId", ampTeamMemberId).list();
    }
    
    public static String collectionToCSV(Collection<AmpActivityVersion> activities) {
        if (activities == null)
            return null;
        String ret = "";
        for(AmpActivityVersion activity:activities){
            if (activity.getName() != null ) {
                ret += "'" + activity.getName() + "'" + ", ";
            }
            else
                ret +=  "' '" + ", ";
        }
        return ret.substring(0, ret.length() - 2);      
    }
        
        /**
         * @author Dare
         * @param partOfName
         * @return Array of Strings,which have a look like: activity_name(activity_id) 
         */
        public static String[] loadActivitiesNamesAndIds(TeamMember member) throws DgException{
            Session session=null;
            String queryString =null;
            Query query=null;
            List activities=null;
            String [] retValue=null;
            try {
                    session=PersistenceManager.getRequestDBSession();
                    
                Set relatedTeams=TeamUtil.getRelatedTeamsForMember(member);
                Set teamAO = TeamUtil.getComputedOrgs(relatedTeams);
                String activityNameString = AmpActivityVersion.hqlStringForName("a");
                // computed workspace
                if (teamAO != null && !teamAO.isEmpty()) {
                    queryString = "select " + activityNameString + ", a.ampActivityId from " + AmpActivity.class.getName() + " a left outer join a.orgrole r  left outer join a.funding f " +
                            " where  a.team in  (" + Util.toCSStringForIN(relatedTeams) + ")    or (r.organisation in  (" + Util.toCSStringForIN(teamAO) + ") or f.ampDonorOrgId in (" + Util.toCSStringForIN(teamAO) + ")) order by " + activityNameString;

                } else 
                {
                    // not computed (e.g. team) workspace
                    queryString = "select " + activityNameString + ", a.ampActivityId from " + AmpActivity.class.getName() + " a  where  a.team in  (" + Util.toCSString(relatedTeams) + ")    ";
//                    if (teamType!= null && teamType.equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
//                      queryString += "  and approvalStatus in (" + Util.toCSString(activityStatus) + ")  ";
//                    }
                    queryString += " order by " + activityNameString;
                }   
                            
                query=session.createQuery(queryString);             
                activities=query.list();        
            }catch(Exception ex) { 
                logger.error("couldn't load Activities" + ex.getMessage()); 
                ex.printStackTrace(); 
            } 
            if (activities != null){
                retValue=new String[activities.size()];         
                int i=0;
                for (Object rawRow : activities) {
                    Object[] row = (Object[])rawRow; //:)
                    String nameRow=(String)row[0];          
                    if(nameRow != null){
                    nameRow = nameRow.replace('\n', ' ');
                    nameRow = nameRow.replace('\r', ' ');
                    nameRow = nameRow.replace("\\", "");
                    }
                    ////System.out.println(nameRow);
                    retValue[i]=nameRow+"("+row[1]+")";
                    i++;                    
                }
            }
            return retValue;
        }

    public static String[] searchActivitiesNamesAndIds(TeamMember member, String searchStr) throws DgException{
            Session session=null;
            String queryString =null;
            Query query=null;
            List activities=null;
            String [] retValue=null;
            try {
                    session=PersistenceManager.getRequestDBSession();

                Set relatedTeams=TeamUtil.getRelatedTeamsForMember(member);
                    Set teamAO = TeamUtil.getComputedOrgs(relatedTeams);

                    String activityName = AmpActivityVersion.hqlStringForName("gr.ampActivityLastVersion");
                    queryString ="select " + activityName + ", gr.ampActivityLastVersion.ampActivityId from "+ AmpActivityGroup.class.getName()+" gr ";                    
                    if (teamAO != null && !teamAO.isEmpty()) {
                        queryString +=" left outer join gr.ampActivityLastVersion.orgrole r  left outer join gr.ampActivityLastVersion.funding f "+
                        " where gr.ampActivityLastVersion.team in (" + Util.toCSStringForIN(relatedTeams) + ")  " +
                                " or (r.organisation in  (" + Util.toCSStringForIN(teamAO) + ") or f.ampDonorOrgId in (" + Util.toCSStringForIN(teamAO) + ")) ";
                        
                    } else {
                        // none computed workspace
                        queryString += " where gr.ampActivityLastVersion.team in  ("
                                + Util.toCSStringForIN(relatedTeams) + ") ";
                    }
                queryString += "  and lower(" + activityName + ") like lower(:searchStr) group by gr.ampActivityLastVersion.ampActivityId," + activityName + " order by " + activityName;
                query=session.createQuery(queryString);
                query.setParameter("searchStr", searchStr + "%", StringType.INSTANCE);
                activities=query.list();
            }catch(Exception ex) {
                logger.error("couldn't load Activities" + ex.getMessage());
                ex.printStackTrace();
            }
            if (activities != null){
                retValue=new String[activities.size()];
                int i=0;
                for (Object rawRow : activities) {
                    Object[] row = (Object[])rawRow; //:)
                    String nameRow=(String)row[0];
                    if(nameRow != null){
                    nameRow = nameRow.replace('\n', ' ');
                    nameRow = nameRow.replace('\r', ' ');
                    nameRow = nameRow.replace("\\", "");
                    }
                    ////System.out.println(nameRow);
                    retValue[i]=nameRow+"("+row[1]+")";
                    i++;
                }
            }
            return retValue;
        }
        
        /** 
         * @param actId
         * @return activity name
         * @author dare
         */
        public static String getActivityName(Long actId){
            String activityName = AmpActivityVersion.hqlStringForName("gr.ampActivityLastVersion");
            String queryString = "select " + activityName + " from "+ AmpActivityGroup.class.getName()+" gr where gr.ampActivityLastVersion.ampActivityId = " + actId;                    
            return PersistenceManager.getSession().createQuery(queryString).uniqueResult().toString();              
        }
        
        /**
         * @author Marcelo
         * @param 
         * @return Array of Strings, which have budget_code_project_id's 
         */
        public static String[] getBudgetCodes() throws DgException{
            Session session=null;
            String queryString =null;
            Query query=null;
            List activities=null;
            String [] retValue=null;
            try {
                session=PersistenceManager.getRequestDBSession();
                queryString = "select distinct a.budgetCodeProjectID from " + AmpActivityVersion.class.getName() + " a";                            
                query=session.createQuery(queryString);             
                activities=query.list();        
            }catch(Exception ex) { 
                logger.error("couldn't load Activities" + ex.getMessage()); 
                ex.printStackTrace(); 
            } 
            if (activities != null){
                //filtering null and blank values 
                ArrayList<String> codes = new ArrayList<String>();
                for (Object rawRow : activities) {
                    String val = (String)rawRow; 
                    if(val!=null && val.trim().compareTo("")!=0){
                        codes.add(val);
                    }
                }
                //add filtered values to the array
                int i=0;
                if(codes.size()!=0){
                    retValue=new String[codes.size()];
                    for(String desc : codes){
                        retValue[i]=desc;
                        i++;
                    }
                }
            }
            return retValue;
        }
      
    public static ArrayList<AmpActivityFake> getAllActivitiesAdmin(String searchTerm, Set<Long> frozenActivityIds, ActivityForm.DataFreezeFilter dataFreezeFilter) {
       try {
            Session session = PersistenceManager.getSession();

           if (ActivityForm.DataFreezeFilter.FROZEN.equals(dataFreezeFilter)
                   && (frozenActivityIds == null || frozenActivityIds.isEmpty())) {
                return new ArrayList<>();
           }
            
            boolean isSearchByName = searchTerm != null && (!searchTerm.trim().isEmpty());
            String activityName = AmpActivityVersion.hqlStringForName("f");

            String nameSearchQuery;
            if (isSearchByName) {
                //this query is stupid and should be rewritten!
                nameSearchQuery = " (f.ampActivityId IN (SELECT t.objectId "
                        + "FROM " + AmpContentTranslation.class.getName() + " t "
                        + "WHERE t.objectClass = '" + AmpActivityVersion.class.getName()
                        + "' AND upper(t.translation) like upper(:searchTerm))) "
                        + "OR f.ampActivityId IN (SELECT f2.ampActivityId from " + AmpActivity.class.getName() + " f2 "
                        + "WHERE upper(f2.name) LIKE upper(:searchTerm) OR upper(f2.ampId) LIKE upper(:searchTerm) ) "
                        + " AND ";
            } else {
                nameSearchQuery = "";
            }

           String dataFreezeQuery = "";
           if (frozenActivityIds != null && frozenActivityIds.size() > 0) {
               if (ActivityForm.DataFreezeFilter.FROZEN.equals(dataFreezeFilter)) {
                   dataFreezeQuery = " AND f.ampActivityId IN (:frozenActivityIds) ";
               } else if (ActivityForm.DataFreezeFilter.UNFROZEN.equals(dataFreezeFilter)) {
                   dataFreezeQuery = " AND f.ampActivityId not in (:frozenActivityIds) ";
               }
           }

            String queryString = "select f.ampActivityId, f.ampId, " + activityName + ", ampTeam , ampGroup "
                    + "FROM " + AmpActivity.class.getName()
                    +  " as f left join f.team as ampTeam left join f.ampActivityGroup as ampGroup WHERE "
                    + nameSearchQuery + " ((f.deleted = false) or (f.deleted is null))" + dataFreezeQuery;
            
            Query qry = session.createQuery(queryString);
           if (isSearchByName) {
               qry.setString("searchTerm", "%" + searchTerm + "%");
           }

            if (frozenActivityIds != null && frozenActivityIds.size() > 0
                    && (ActivityForm.DataFreezeFilter.FROZEN.equals(dataFreezeFilter)
                            || ActivityForm.DataFreezeFilter.UNFROZEN.equals(dataFreezeFilter))) {
                qry.setParameterList("frozenActivityIds",
                        frozenActivityIds != null ? frozenActivityIds : new HashSet<>());
            }

            Iterator iter = qry.list().iterator();
            ArrayList<AmpActivityFake> result = new  ArrayList<AmpActivityFake>();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                Long ampActivityId = (Long) item[0];
                String ampId = (String) item[1];
                String name = (String) item[2];
                AmpTeam team = (AmpTeam) item[3];
                AmpActivityGroup ampActGroup = (AmpActivityGroup) item[4];
                AmpActivityFake activity = new AmpActivityFake(name,team,ampId,ampActivityId,ampActGroup);
                result.add(activity);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteAmpActivityWithVersions(Long ampActId) throws Exception {
        Session session = PersistenceManager.getSession();
        AmpActivityGroup ampActivityGroup = getActivityGroups(session, ampActId);
        Set<AmpActivityVersion> activityversions = ampActivityGroup.getActivities();
        if (activityversions != null && activityversions.size() > 0) {
            for (AmpActivityVersion ampActivityVersion : activityversions) {
                deleteFullActivityContent(ampActivityVersion, session);

                session.delete(ampActivityVersion);
            }
        } else {
            AmpActivityVersion ampAct = (AmpActivityVersion) session.load(AmpActivityVersion.class, ampActId);
            deleteFullActivityContent(ampAct, session);
            session.delete(ampAct);
        }
        session.delete(ampActivityGroup);
    }
    
    public static void  deleteFullActivityContent(AmpActivityVersion ampAct, Session session) throws Exception{
        ActivityUtil.deleteActivityContent(ampAct,session);
        Long ampActId = ampAct.getAmpActivityId();
        //This is not deleting AmpMEIndicators, just indicators, ME is deprecated.
        ActivityUtil.deleteActivityIndicators(DbUtil.getActivityMEIndValue(ampActId), ampAct, session);
    }
    
    public static void  deleteAllActivityContent(AmpActivityVersion ampAct, Session session) throws Exception{
        ActivityUtil.deleteActivityContent(ampAct,session);
        Long ampActId = ampAct.getAmpActivityId();
        ActivityUtil.removeMergeSources(ampActId, session);
        ActivityUtil.deleteActivityIndicatorsSession(ampActId, session);
    }
    
    public static Integer activityExists (Long versionId,Session session) throws Exception{
        Integer retVal = null;
        try {
            Query qry= session.createQuery("select count(a) from " +AmpActivityVersion.class.getName() +" a where a.ampActivityId="+versionId);
            retVal = (Integer)qry.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }       
        return retVal;      
    }
    
    
    /**
     * returns a subset of activities which can/should be validated by a team member
     * @param tm
     * @param activityIds
     * @return
     */
    public static Set<Long> getActivitiesWhichShouldBeValidated(TeamMember tm, Collection<Long> activityIds)
    {
        Set<Long> result = new HashSet<Long>();
        boolean crossTeamValidationEnabled = tm != null && tm.getAppSettings() != null && tm.getAppSettings().isCrossTeamValidationEnabled();
        try
        {
            String query = "SELECT a.amp_activity_id FROM amp_activity a WHERE a.amp_activity_id IN (" + TeamUtil.getCommaSeparatedList(activityIds) + ") " +
                "AND (a.approval_status = 'started' OR a.approval_status='edited' OR a.approval_status='rejected') AND (a.draft IS NULL OR a.draft IS FALSE)"; // AND (a.amp_team_id = " + tm.getTeamId() + ")";
            if (!crossTeamValidationEnabled)
                query += "  AND (a.amp_team_id = " + tm.getTeamId() + ")";
            
            List<BigInteger> validated_activity_ids = PersistenceManager.getSession().createSQLQuery(query).list();
            for(BigInteger bi:validated_activity_ids)
                result.add(bi.longValue());
            return result;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    
    public static ArrayList<org.digijava.module.aim.helper.Issues>  getIssues(Long actId) {
        ArrayList<org.digijava.module.aim.helper.Issues> col = new ArrayList<>();
        
        AmpActivityVersion activity = null;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, actId);

            for (AmpIssues issue: activity.getIssues()) {
                col.add(new org.digijava.module.aim.helper.Issues(issue));
            }
        }    catch (Exception e) {
             logger.debug("Exception in getAmpMeasures() " + e.getMessage());
           }
        return col;

    }

    
     public static void changeActivityArchiveStatus(Collection<Long> activityIds, boolean status) {
            try {
                Session session             = PersistenceManager.getRequestDBSession();
                String qryString            = "update " + AmpActivityVersion.class.getName()  + 
                        " av  set av.archived=:archived where av.ampActivityId in (" + Util.toCSStringForIN(activityIds) + ")";
                Query query                 = session.createQuery(qryString);
                query.setBoolean("archived", status);
                query.executeUpdate();
                session.flush();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }  
     
     public static AmpStructureImg getStructureImage(Long structureId, Long imgId) {
         return DbUtil.getStructureImage(structureId, imgId);
     }
     public static AmpStructureImg getMostRecentlyUploadedStructureImage(Long structureId) {
         return DbUtil.getMostRecentlyUploadedStructureImage(structureId);
     }


     public static  java.util.List<String[]> getAidEffectivenesForExport(AmpActivityVersion activity) {
         java.util.List<String[]>aidEffectivenesForExport= new ArrayList<String[]>();
         if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes")
                 && activity.getSelectedEffectivenessIndicatorOptions() != null) {
             AidEffectivenessIndicatorUtil.sortSelectedEffectivenessOptions(activity);
             for (AmpAidEffectivenessIndicatorOption option : activity.getSelectedEffectivenessIndicatorOptions()) {
                 if (FeaturesUtil.isVisibleModule("/Activity Form/Aid Effectivenes/"
                         + option.getIndicator().getAmpIndicatorName())) {
                     String[] aidEffectivenesToAdd = new String[2];
                     aidEffectivenesToAdd[0] = option.getIndicator().getAmpIndicatorName();
                     aidEffectivenesToAdd[1] = option.getAmpIndicatorOptionName();
                     aidEffectivenesForExport.add(aidEffectivenesToAdd);
                 }
             }
         }

         return aidEffectivenesForExport;
     }


        public static String getFmForFundingFlows(Integer transactionType) {
            String fmForFundingFlows = "/Activity Form/Funding/Funding Group/Funding Item/";
            switch (transactionType) {
            case Constants.COMMITMENT:
                fmForFundingFlows += "Commitments/Commitments Table";
                break;
            case Constants.DISBURSEMENT:
                fmForFundingFlows += "Disbursements/Disbursements Table";
                break;

            }
                fmForFundingFlows+="/Funding Flows OrgRole Selector";
            return fmForFundingFlows;
        }

    public static Period getProjectImplementationDelay(AmpActivityVersion activity) {
        Date fromDate = activity.getOriginalCompDate();
        Date toDate;
        if (fromDate == null)
            return null;
        
        if (activity.getActualCompletionDate() != null)
            toDate = activity.getActualCompletionDate();
        else if (activity.getProposedCompletionDate() != null)
            toDate = activity.getProposedCompletionDate();
        else toDate = new Date();
        if (fromDate.before(toDate))
            return DateConversion.getPeriod(fromDate, toDate);
        return null;
    }
    
    /** Get the user first name and last name  who modified (created) the activity.
     * @param actitivity
     * @param auditHistory
     * @return
     */
    public static String getModifiedByUserName(AmpActivityVersion actitivity, ActivityHistory auditHistory) {
        AmpTeamMember modifiedBy = actitivity.getModifiedBy();
        AmpTeamMember createdBy = actitivity.getActivityCreator();
        AmpTeamMember approvedBy = actitivity.getApprovedBy();
        
        if (modifiedBy != null) {
            return String.format("%s %s", modifiedBy.getUser().getFirstNames(), modifiedBy.getUser().getLastName());
        } else if(auditHistory != null) {
            return auditHistory.getModifiedBy();
        } else if (approvedBy != null) {
            return String.format("%s %s", approvedBy.getUser().getFirstNames(), approvedBy.getUser().getLastName());
        } else if (createdBy != null) {
            return String.format("%s %s", createdBy.getUser().getFirstNames(), createdBy.getUser().getLastName());
        }
        
        return "";
    }
    
    /** Get modified date
     * @param activity
     * @param auditHistory
     */
    public static Date getModifiedByDate(AmpActivityVersion activity, ActivityHistory auditHistory) {
        if (activity.getUpdatedDate() != null) {
            return activity.getUpdatedDate();
        } else if (activity.getModifiedDate() != null) {
            return activity.getModifiedDate();
        } else if (auditHistory != null) {
            return DateTimeUtil.parseISO8601Timestamp(auditHistory.getModifiedDate());
        } else if (activity.getApprovalDate() != null) {
            return activity.getApprovalDate();
        } else if (activity.getCreatedDate() != null) {
            return activity.getCreatedDate();
        }
        
        return null;
    }
    
    /**
     * Get audit info about the activity from amp_audit_logger table
     * @param activityId
     * @return
     */
    public static ActivityHistory getModifiedByInfoFromAuditLogger(Long activityId) {
        ActivityHistory logActivityHistory = new ActivityHistory();
        List<AmpAuditLogger> activityLogObjects = AuditLoggerUtil.getActivityLogObjects(activityId.toString());
        
        for(AmpAuditLogger aal : activityLogObjects) {
            if (StringUtils.isNotEmpty(aal.getEditorName())) {
                logActivityHistory.setModifiedBy(aal.getEditorName());
                logActivityHistory.setModifiedDate(DateTimeUtil.formatISO8601Timestamp(aal.getLoggedDate()));
                return logActivityHistory;
            } else if (StringUtils.isNotEmpty(aal.getEditorEmail())) {
                User u = UserUtils.getUserByEmailAddress(aal.getEditorEmail());
                if (u != null) {
                    logActivityHistory.setModifiedBy(String.format("%s %s", u.getFirstNames(), u.getLastName()));
                    logActivityHistory.setModifiedDate(DateTimeUtil.formatISO8601Timestamp(aal.getLoggedDate()));
                    return logActivityHistory;
                }
            }
        }
        
        return null;
    }

    public static Set<String> findExistingAmpIds(Collection<String> candidates) {

        String queryStr = "select activity.ampId from " + AmpActivity.class.getName() + " activity " +
                " where activity.ampId in ( " + Util.toCSString(candidates) + " ) ";

        Session session = PersistenceManager.getRequestDBSession();
        Query qry = session.createQuery(queryStr);

        return new HashSet<String>(((List<String>) qry.list()));
    }

    public static AmpActivityVersion getPreviousVersion(Long activityId) {
        Session session = PersistenceManager.getRequestDBSession();
        AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityId);
        Query qry = session.createQuery(String.format("SELECT act FROM " + AmpActivityVersion.class.getName()
                        + " act WHERE approval_status in ( '%s','%s' ) "
                        + " and act.ampActivityGroup.ampActivityGroupId = ? "
                        + " and act.ampActivityId <> ? "
                        + " ORDER BY act.ampActivityId DESC",
                ApprovalStatus.APPROVED.getDbName(),
                ApprovalStatus.STARTED_APPROVED.getDbName()))
                .setMaxResults(1);
        qry.setParameter(0, activity.getAmpActivityGroup().getAmpActivityGroupId());
        qry.setParameter(1, activityId);
        return (qry.list().size() > 0 ? (AmpActivityVersion) qry.list().get(0) : null);
    }

    public static List<ActivityHistory> getActivityHistories(Long activityId) {
        Session session = PersistenceManager.getRequestDBSession();
        AmpActivityVersion currentActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityId);

        Query qry = session.createQuery("SELECT act FROM " + AmpActivityVersion.class.getName()
                + " act WHERE act.ampActivityGroup.ampActivityGroupId = ? ORDER BY act.ampActivityId DESC")
                .setMaxResults(ActivityVersionUtil.numberOfVersions());
        qry.setParameter(0, currentActivity.getAmpActivityGroup().getAmpActivityGroupId());
        List<AmpActivityVersion> activities = new ArrayList<AmpActivityVersion>(qry.list());

        return getActivitiesHistory(activities);
    }

    private static List<ActivityHistory> getActivitiesHistory(List<AmpActivityVersion> activities) {
        List<ActivityHistory> activitiesHistory = new ArrayList<>();

        for (AmpActivityVersion activity : activities) {
            ActivityHistory auditHistory = null;

            if (activity.getModifiedBy() == null || (activity.getUpdatedDate() == null
                    && activity.getModifiedDate() == null)) {
                auditHistory = ActivityUtil.getModifiedByInfoFromAuditLogger(activity.getAmpActivityId());
            }

            ActivityHistory activityHistory = new ActivityHistory();
            activityHistory.setActivityId(activity.getAmpActivityId());
            activityHistory.setModifiedBy(ActivityUtil.getModifiedByUserName(activity, auditHistory));
            activityHistory.setModifiedDate(DateTimeUtil.formatISO8601Timestamp(
                    ActivityUtil.getModifiedByDate(activity, auditHistory)));

            activitiesHistory.add(activityHistory);
        }

        return activitiesHistory;
    }

    /**
     * @param a
     * @param activityDisbursements
     * @return transactions of specified type
     */
    public static List<AmpFundingDetail> getTransactionsWithType(AmpActivityVersion a, int transactionType) {
        List<AmpFundingDetail> activityTransactions = new ArrayList<>();

        if (a.getFunding() != null) {
            activityTransactions = a.getFunding().stream()
                    .filter(f -> f.getFundingDetails() != null)
                    .flatMap(f -> f.getFundingDetails().stream())
                    .filter(fd -> fd.getTransactionType() == transactionType)
                    .collect(Collectors.toList());
        }

        return activityTransactions;
    }

    public static List<Long> getActivityIdsByApprovalStatus(Set<ApprovalStatus> statuses) {
        Long closedCatValue = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.CLOSED_ACTIVITY_VALUE);

        String filterQuery = "SELECT amp_activity_id FROM amp_activity "
                + "WHERE (draft IS NULL or draft = false) "
                + "AND (amp_team_id IS NOT NULL)"
                + "AND (deleted IS NULL OR deleted = false) "
                + "AND approval_status IN (" + Util.toCSString(statuses) + ") "
                + "AND amp_activity_id IN (SELECT amp_activity_id FROM v_status WHERE amp_status_id != "
                + closedCatValue + ") ";

        Session session = PersistenceManager.getRequestDBSession();

        List<Long> validatedActivityIds = (List<Long>) session.createSQLQuery(filterQuery)
                .addScalar("amp_activity_id", StandardBasicTypes.LONG)
                .list();

        return validatedActivityIds;
    }

    public static List<Long> getValidatedActivityIds() {
        return getActivityIdsByApprovalStatus(AmpARFilter.VALIDATED_ACTIVITY_STATUS);
    }

    /**
     * Get list of editable activity ids for the team member.
     * Useful for cases when you need to check list of editable activities for a team member that is not a principal.
     * I.e. this team member is not authenticated right now.
     * @return List<Long> with the editable activity Ids
     */
    public static List<Long> getEditableActivityIdsNoSession(TeamMember tm) {
        String query = WorkspaceFilter.generateWorkspaceFilterQuery(tm);
        return ActivityUtil.getEditableActivityIds(tm, query);
    }

    /**
     * Get the activities ids for the current workspace
     *
     * @param session HttpSession
     * @return List<Long> with the editable activity Ids
     */
    public static List<Long> getEditableActivityIds(TeamMember tm, String query) {
        // based on AMP-20520 research the only rule found when activities are not editable is when in Mng WS
        if (TeamMemberUtil.isManagementWorkspace(tm)) {
            return Collections.emptyList();
        }

        List<Long> result = PersistenceManager.getSession().createSQLQuery(query)
                .addScalar("amp_activity_id", LongType.INSTANCE).list();

        return result;
    }

    public static boolean canValidateActivity(AmpActivityVersion activity, TeamMember teamMember) {
        boolean canValidate = false;
        
        if (!activity.getDraft()) {
            AmpApplicationSettings appSettings = AmpARFilter.getEffectiveSettings();
            String validationOption = appSettings != null ? appSettings.getValidation() : null;
            
            boolean isTeamMemberValidator = isTeamMemberValidator(teamMember, activity);
            
            if (isTeamMemberValidator) {
                if (Constants.PROJECT_VALIDATION_FOR_ALL_EDITS.equalsIgnoreCase(validationOption)) {
                    if (activity.getTeam() != null
                            && (ApprovalStatus.STARTED.equals(activity.getApprovalStatus())
                            || ApprovalStatus.EDITED.equals(activity.getApprovalStatus()))) {
                        canValidate = true;
                    }
                } else {
                    //it will display the validate label only if it is just started and was not approved not even once
                    if (Constants.PROJECT_VALIDATION_FOR_NEW_ONLY.equalsIgnoreCase(validationOption)
                            && ApprovalStatus.STARTED.equals(activity.getApprovalStatus())) {
                            canValidate = true;
                        }
                    }
            }
        }
        
        return canValidate;
    }
    
    public static boolean isTeamMemberValidator(TeamMember teamMember, AmpActivityVersion activity) {
        
        if (teamMember.getTeamHead()) {
            return true;
        }
    
        AmpApplicationSettings appSettings = AmpARFilter.getEffectiveSettings();
    
        boolean crossTeamValidation = (appSettings != null && appSettings.getTeam() != null)
                ? appSettings.getTeam().getCrossteamvalidation() : false;
    
        //Check if cross team validation is enable
        boolean crossTeamCheck = false;
    
        if (activity.getTeam() != null) {
            if (crossTeamValidation) {
                crossTeamCheck = true;
            } else {
                //check if the activity belongs to the team where the user is logged.
                if (teamMember.getTeamId() != null && activity.getTeam().getAmpTeamId() != null) {
                    crossTeamCheck = teamMember.getTeamId().equals(activity.getTeam().getAmpTeamId());
                }
            }
        
            return teamMember.isApprover() && crossTeamCheck;
        }
        
        return false;
    }

    private static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (MILLISECONDS_IN_A_DAY));
    }

    public static int daysToValidation(AmpActivityVersion activity) {
        int result;
        int daysBetween = daysBetween(activity.getUpdatedDate() != null ? activity.getUpdatedDate(): new Date(), new Date());
        String daysBeforeValidation = FeaturesUtil.getGlobalSettingValue(
                GlobalSettingsConstants.NUMBER_OF_DAYS_BEFORE_AUTOMATIC_VALIDATION);
        result = (Integer.parseInt(daysBeforeValidation) - daysBetween);
        return result <= 0 ? 1 : result;
    }

    public static boolean isAutomaticValidationEnabled() {
        return (QuartzJobUtils.getJobByClassFullname(Constants.AUTOMATIC_VALIDATION_JOB_CLASS_NAME) == null
                ? false : true);
    }

    public static List<String> loadWorkspacePrefixesIntoRequest() {
        List<String> prefixes = TranslatorWorker.getAllPrefixes();
        TLSUtils.getRequest().setAttribute(PREFIXES, prefixes);
        return prefixes;
    }

    public static List<String> getWorkspacePrefixesFromRequest() {
        return (List<String>) TLSUtils.getRequest().getAttribute(PREFIXES);
    }

    /**
     * Set the workspace prefix (if any) and the FM id of the activity into request scope.
     * @param activity
     */
    public static void setCurrentWorkspacePrefixIntoRequest(AmpActivityVersion activity) {
        if (activity.getTeam() != null) {
            if (activity.getTeam().getWorkspacePrefix() != null) {
                TLSUtils.getRequest().setAttribute(WORKSPACE_PREFIX,
                        activity.getTeam().getWorkspacePrefix().getLabel());
            } else {
                TLSUtils.getRequest().setAttribute(WORKSPACE_PREFIX, "");
            }
            if (activity.getTeam().getFmTemplate() != null) {
                TLSUtils.getRequest().setAttribute(ACTIVITY_FM_ID, activity.getTeam().getFmTemplate().getId());
            }
        }
    }

    public static Long getCurrentFMId() {
        if (TLSUtils.getRequest().getAttribute(ACTIVITY_FM_ID) != null) {
            return Long.getLong(TLSUtils.getRequest().getAttribute(ACTIVITY_FM_ID).toString());
        }
        return null;
    }
}
