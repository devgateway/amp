/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
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

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRegionalObservation;
import org.digijava.module.aim.dbentity.AmpRegionalObservationActor;
import org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpStructureImg;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.ActivityItem;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.visualization.util.DashboardUtil;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 * ActivityUtil is the persister class for all activity related
 * entities
 *
 * @author Priyajith
 */
public class ActivityUtil {

  private static Logger logger = Logger.getLogger(ActivityUtil.class);



  public static void updateActivityDocuments(Long activityId, Set documents) {
		Session session = null;
		AmpActivityVersion oldActivity = null;

		try {
			session = PersistenceManager.getRequestDBSession();
//beginTransaction();

			oldActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class,
					activityId);

			if (oldActivity == null) {
				logger.debug("Previous Activity is null");
				return;
			}

			oldActivity.setDocuments(documents);

			session.update(oldActivity);
			//tx.commit();
			logger.debug("Activity saved");
		} catch (Exception ex) {
			logger.error("Exception from saveActivity()  " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}
  
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
      		"inner join amp_activity_components aac on (aac.amp_component_id = ac.amp_component_id) " +
      		"where (aac.amp_activity_id=:actId)";
      Query qry = session.createSQLQuery(queryString).addEntity(AmpComponent.class);
      qry.setParameter("actId", actId, LongType.INSTANCE);
      col = qry.list();
    }
    catch (Exception e) {
      logger.error("Unable to get all components");
      logger.error(e.getMessage());
    }
//    finally {
//      try {
//        PersistenceManager.releaseSession(session);
//      }
//      catch (Exception ex) {
//        logger.error("Release Session failed :" + ex);
//      }
//    }
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
      		"	from " + AmpActivityProgram.class.getName() + " prog ";

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
              String ids = DashboardUtil.getComputationOrgsQry(team);
              if(ids.length()>1){
              ids = ids.substring(0, ids.length() - 1);
              	whereTeamStatement.append("  and ( latestAct.team.ampTeamId =:teamId or  role.organisation.ampOrgId in(" + ids+"))");
              }
              relatedOrgsCriteria=true;
          }
          else{
				if (team.getAccessType().equals("Management")) {
					whereTeamStatement.append(String.format(" and (latestAct.draft=false or latestAct.draft is null) and latestAct.approvalStatus IN ('%s', '%s') ", Constants.APPROVED_STATUS, Constants.STARTED_APPROVED_STATUS));
					List<AmpTeam> teams = new ArrayList<AmpTeam>();
					DashboardUtil.getTeams(team, teams);
					String relatedOrgs = "", teamIds = "";
					for (AmpTeam tm : teams) {
						if (tm.getComputation() != null && tm.getComputation()) {
							relatedOrgs += DashboardUtil
									.getComputationOrgsQry(tm);
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
        oql += " and latestAct.locations in (from " + AmpLocation.class.getName() +" loc where loc.id=:LocationID)";
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

  public static Collection getActivityPrograms(Long activityId) {
	    Session session = null;
	    Collection col = new ArrayList();

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String queryString = "select prog from " +
	          AmpTheme.class.getName() +
	          " prog where (prog.activityId=:actId) ";
	      Query qry = session.createQuery(queryString);
	      qry.setParameter("actId", activityId, LongType.INSTANCE);
	      col = qry.list();
	    }
	    catch (Exception e) {
	      logger.error("Unable to get activity programs");
	      logger.error(e.getMessage());
	    }
	    return col;
	  }


  public static Collection getActivityLocations(Long activityId) {
	    Session session = null;
	    Collection col = new ArrayList();

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String queryString = "select locs.* from amp_activity_location locs where (locs.amp_activity_id=:actId) ";
	      Query qry = session.createSQLQuery(queryString).addEntity(AmpActivityLocation.class);
	      qry.setParameter("actId", activityId, LongType.INSTANCE);
	      col = qry.list();
	    }
	    catch (Exception e) {
	      logger.error("Unable to get activity locations");
	      logger.error(e.getMessage());
	    }
//	    finally {
//	      try {
//	        PersistenceManager.releaseSession(session);
//	      }
//	      catch (Exception ex) {
//	        logger.error("Release Session failed :" + ex);
//	      }
//	    }
	    return col;
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
			Hibernate.initialize(result.getCosts());
			Hibernate.initialize(result.getInternalIds());
			Hibernate.initialize(result.getLocations());
			Hibernate.initialize(result.getSectors());
			Hibernate.initialize(result.getFunding());
			if(result.getFunding()!=null){
				for(Object obj:result.getFunding()){
					AmpFunding funding=(AmpFunding)obj;
					Hibernate.initialize(funding.getFundingDetails());
					Hibernate.initialize(funding.getMtefProjections());
				}
			}
			Hibernate.initialize(result.getActivityDocuments());
			Hibernate.initialize(result.getComponents());
			Hibernate.initialize(result.getOrgrole());
			Hibernate.initialize(result.getIssues());
			Hibernate.initialize(result.getRegionalObservations());
			Hibernate.initialize(result.getStructures());
		} catch (ObjectNotFoundException e) {
			logger.debug("AmpActivityVersion with id=" + id + " not found");
		} catch (Exception e) {
			throw new DgException("Cannot load AmpActivityVersion with id " + id, e);
		}
		return result;
	}
  
  public static AmpActivityVersion loadAmpActivity(Long id){
	  try{
		 return (AmpActivityVersion) PersistenceManager.getRequestDBSession().load(AmpActivityVersion.class.getName(), id); 
	  }
	  catch (Exception e){
		  throw new RuntimeException(e);
	  }
  }
  
  public static AmpActivityVersion getAmpActivityVersion(Long id) {
		// TODO: This is a mess, shouldn't be here. Check where it is used and
		// change it.

		Session session = null;
		AmpActivityVersion activity = null;

		try {
			session = PersistenceManager.getRequestDBSession();

			activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
		} catch (Exception e) {
			logger.error("Unable to getAmpActivity");
			e.printStackTrace(System.out);
		}
		return activity;
	}

  public static Collection getAmpActivitySectors(Long actId) {
    Session session = null;
    Collection sectors = new ArrayList();

    try {
      session = PersistenceManager.getRequestDBSession();
      String queryString = "select a.* from amp_activity_sector a " + "where a.amp_activity_id=:actId";
      Query qry = session.createSQLQuery(queryString).addEntity(AmpActivitySector.class);
      qry.setParameter("actId", actId, LongType.INSTANCE);
      sectors = qry.list();
    }
    catch (Exception ex) {
      logger.error("Unable to get activity sectors :" + ex);
    }
    return sectors;
  }

  public static Collection getOrgRole(Long id) {
    Session session = null;
    Collection orgroles = new ArrayList();
    try {
      session = PersistenceManager.getSession();
      String queryString = "select aor from " + AmpOrgRole.class.getName() +
          " aor " + "where (aor.activity=:actId)";
      Query qry = session.createQuery(queryString);
      qry.setParameter("actId", id, LongType.INSTANCE);
      orgroles = qry.list();
    }
    catch (Exception ex) {
      logger.error("Unable to get activity sectors :" + ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.error("releaseSession() failed ");
      }
    }
    return orgroles;
  }


  public static AmpRole getAmpRole(Long actId, Long orgRoleId) {
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
	      String queryString = "select " + rewrittenColumns + " from amp_organisation ao " +	      		"inner join amp_org_role aor on (aor.organisation = ao.amp_org_id) " +
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
  
  public static int getFundingByOrgCount(Long id) {
	    Session session = null;
	    int orgrolesCount = 0;
	    try {
	      session = PersistenceManager.getSession();
	      String queryString = "select count(*) from " + AmpFunding.class.getName() +" f, "
	    		  + AmpActivity.class.getName()	+ " a "
	    		  + "where f.ampActivityId=a.ampActivityId and (f.ampDonorOrgId=:orgId)";
	      Query qry = session.createQuery(queryString);
	      qry.setParameter("orgId", id, LongType.INSTANCE);
	      orgrolesCount = (Integer)qry.uniqueResult();
	    }
	    catch (Exception ex) {
	      logger.error("Unable to get fundings for organization :" + ex);
	    }
	    return orgrolesCount;
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

          Collection<AmpComponentFunding> componentsFunding = ActivityUtil.getFundingComponentActivity(ampComp.
              getAmpComponentId(), activity.getAmpActivityId());
          Iterator compFundIterator = componentsFunding.iterator();
          while (compFundIterator.hasNext()) {
            AmpComponentFunding cf = (AmpComponentFunding) compFundIterator.next();
            FundingDetail fd = new FundingDetail();
            fd.setAdjustmentTypeName(cf.getAdjustmentType());
 
            fd.setCurrencyCode(cf.getCurrency().getCurrencyCode());
            fd.setCurrencyName(cf.getCurrency().getCurrencyName());
            fd.setTransactionAmount(FormatHelper.formatNumber(cf.getTransactionAmount().doubleValue()));
            fd.setTransactionDate(
                DateConversion.ConvertDateToString(
                    cf.getTransactionDate()));
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

  public static Collection<AmpComponentFunding> getFundingComponentActivity(Long componentId, Long activityId) {
    Collection col = null;
    logger.debug(" inside getting the funding.....");
    Session session = null;

    try {
      session = PersistenceManager.getRequestDBSession();
    	//session = PersistenceManager.getRequestDBSession();
      String qryStr = "select a from " + AmpComponentFunding.class.getName() +
          " a " +
          "where amp_component_id = '" + componentId + "' and activity_id = '" + activityId +
          "'";
      Query qry = session.createQuery(qryStr);
      col = qry.list();
    }
    catch (Exception e) {
      logger.debug("Exception in getAmpComponents() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    //getComponents();
    return col;
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

  //function for physical progress

  public static Collection getAmpIssues(Long actId) {
	  	Collection issues = null;
	    Session session = null;

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String qryStr = "select a from " + AmpIssues.class.getName() +
	          " a where a.activity.ampActivityId=:actId ";
	      Query qry = session.createQuery(qryStr);
	      qry.setParameter("actId", actId, LongType.INSTANCE);
	      issues = qry.list();
	      // get issues measures
	      AmpIssues ampIssues = null;
	      Collection measures = null;
	      for (Iterator it = issues.iterator(); it.hasNext();) {
	    	  ampIssues = (AmpIssues) it.next();
	    	  measures = getAmpMeasures(ampIssues.getAmpIssueId());
	    	  if (ampIssues.getMeasures() == null) {
	    		  ampIssues.setMeasures(Collections.emptySet());
	    	  }
	    	  ampIssues.getMeasures().addAll(measures);
	    	  // get measures actors
	    	  AmpMeasure ampMeasure = null;
	    	  Collection actors = null;
	    	  for (Iterator it2 = measures.iterator(); it2.hasNext();) {
	    		  ampMeasure = (AmpMeasure) it2.next();
	    		  actors = getAmpActors(ampMeasure.getAmpMeasureId());
	    		  if (ampMeasure.getActors() == null) {
	    			  ampMeasure.setActors(Collections.emptySet());
	    		  }
	    		  ampMeasure.getActors().addAll(actors);
	    	  }
	      }
	    }
	    catch (Exception e) {
	      logger.debug("Exception in getAmpIssues() " + e.getMessage());
	    }
	    return issues;
  }

  public static Collection getAmpMeasures(Long issueId) {
	  	Collection col = null;
	    Session session = null;

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String qryStr = "select a from " + AmpMeasure.class.getName() +
	          " a where a.amp_issue_id=:issueId ";
	      Query qry = session.createQuery(qryStr);
	      qry.setParameter("issueId", issueId, LongType.INSTANCE);
	      col = qry.list();
	    }
	    catch (Exception e) {
	      logger.debug("Exception in getAmpMeasures() " + e.getMessage());
	    }
	    return col;
  }
  

  public static Collection getAmpActors(Long measureId) {
	  	Collection col = null;
	    Session session = null;

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String qryStr = "select a from " + AmpActor.class.getName() +
	          " a where a.amp_measure_id=:measureId ";
	      Query qry = session.createQuery(qryStr);
	      qry.setParameter("measureId", measureId, LongType.INSTANCE);
	      col = qry.list();
	    }
	    catch (Exception e) {
	      logger.debug("Exception in getAmpActors() " + e.getMessage());
	    }
	    return col;
  }

//end functino to get components
  
  public static ArrayList getRegionalObservations(Long id) {
		ArrayList<AmpRegionalObservation> list = new ArrayList<AmpRegionalObservation>();
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
			Set regObs = activity.getRegionalObservations();
			Iterator<AmpRegionalObservation> iRegObs = regObs.iterator();
			while (iRegObs.hasNext()) {
				AmpRegionalObservation auxRegOb = iRegObs.next();
				Iterator<AmpRegionalObservationMeasure> iRegMeasures = auxRegOb.getRegionalObservationMeasures()
						.iterator();
				while (iRegMeasures.hasNext()) {
					AmpRegionalObservationMeasure auxRegMeasure = iRegMeasures.next();
					Iterator<AmpRegionalObservationActor> iRegActors = auxRegMeasure.getActors().iterator();
					while (iRegActors.hasNext()) {
						iRegActors.next();
					}
				}

				list.add(auxRegOb);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
  
  public static ArrayList getIssues(Long id) {
    ArrayList list = new ArrayList();

    Session session = null;
    try {
      session = PersistenceManager.getSession();
      AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
      Set issues = activity.getIssues();
      if (issues != null && issues.size() > 0) {
        Iterator iItr = issues.iterator();
        while (iItr.hasNext()) {
          AmpIssues ampIssue = (AmpIssues) iItr.next();
          Issues issue = new Issues();
          issue.setId(ampIssue.getAmpIssueId());
          issue.setName(ampIssue.getName());
          issue.setIssueDate(FormatHelper.formatDate(ampIssue.getIssueDate()));
          ArrayList mList = new ArrayList();
          if (ampIssue.getMeasures() != null &&
              ampIssue.getMeasures().size() > 0) {
            Iterator mItr = ampIssue.getMeasures().iterator();
            while (mItr.hasNext()) {
              AmpMeasure ampMeasure = (AmpMeasure) mItr.next();
              Measures measure = new Measures();
              measure.setId(ampMeasure.getAmpMeasureId());
              measure.setName(ampMeasure.getName());
              measure.setMeasureDate(FormatHelper.formatDate(ampMeasure.getMeasureDate()));
                 
              ArrayList aList = new ArrayList();
              if (ampMeasure.getActors() != null &&
                  ampMeasure.getActors().size() > 0) {
                Iterator aItr = ampMeasure.getActors().iterator();
                while (aItr.hasNext()) {
                  AmpActor actor = (AmpActor) aItr.next();
                  aList.add(actor);
                }
              }
              measure.setActors(aList);
              mList.add(measure);
            }
          }
          issue.setMeasures(mList);
          list.add(issue);
        }
      }
    }
    catch (Exception e) {
      logger.debug("Exception in getIssues() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    return list;
  }

  public static Collection getRegionalFundings(Long id) {
    Collection col = new ArrayList();

    Session session = null;
    try {
      session = PersistenceManager.getRequestDBSession();
      AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
      col = activity.getRegionalFundings();
    }
    catch (Exception e) {
      logger.debug("Exception in getRegionalFundings() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    return col;
  }

  public static Collection getRegionalFundings(Long id, Long regId) {
    Collection col = new ArrayList();

    Session session = null;
    try {
      session = PersistenceManager.getRequestDBSession();
      AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
      col = activity.getRegionalFundings();
      ArrayList temp = new ArrayList(col);
      Iterator itr = temp.iterator();
      AmpRegionalFunding regionFunding = new AmpRegionalFunding();
      regionFunding.setAmpRegionalFundingId(regId);
      while (itr.hasNext()) {
        AmpRegionalFunding regFund = (AmpRegionalFunding) itr.next();
        if (regionFunding.equals(regFund)) {
          col.remove(regFund);
        }
      }
    }
    catch (Exception e) {
      logger.debug("Exception in getRegionalFundings() " + e.getMessage());
      e.printStackTrace(System.out);
    }
    return col;
  }

  public static AmpActivity getActivityByNameExcludingGroup(String name , AmpActivityGroup g) {
	  
		Session session=null;
		try {
			session = PersistenceManager.getRequestDBSession();
		} catch (DgException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
	  Criteria crit = session.createCriteria(AmpActivity.class);
	  
	  Conjunction conjunction = Restrictions.conjunction();
	  String locale = TLSUtils.getLangCode();
	  conjunction.add(SQLUtils.getUnaccentILikeExpression("name", name, locale, MatchMode.EXACT));
	  if(g!=null) conjunction.add(Restrictions.not(Restrictions.eq("ampActivityGroup",g)));
	  crit.add(conjunction);
	  
	  List ret = crit.list();
	  if(ret.size()>0) return (AmpActivity) ret.get(0);
				
	  return null;
  }

    public static StringBuilder getDonorsForActivity(Long activityId) {
        StringBuilder donors = new StringBuilder();
        if (activityId != null) {
            Session session = PersistenceManager.getSession();
            String queryString = "select distinct donor from " + AmpFunding.class.getName() + " f inner join f.ampDonorOrgId donor inner join f.ampActivityId act ";
            queryString += " where act.ampActivityId=:activityId";
            Query qry = session.createQuery(queryString);
            qry.setLong("activityId", activityId);

            List<AmpOrganisation> organizations = qry.list();
            if (organizations != null && organizations.size() > 1) {
                Collections.sort(organizations, new Comparator<AmpOrganisation>() {
                    public int compare(AmpOrganisation o1, AmpOrganisation o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
            }

            if (organizations != null) {
                for (AmpOrganisation donor : organizations) {
                    donors.append(donor.getName());
                    donors.append(", ");
                }

                if (donors.length() > 1) {
                    // remove last coma
                    donors.setLength(donors.length() - 2);
                }
            }

        }
        return donors;
    }

    public static List getSortedActivitiesByDonors (List<AmpActivityVersion> acts, boolean acs) {
        List<AmpActivityVersion> retVal = new ArrayList<AmpActivityVersion>();

        Map <String, AmpActivityVersion> donorNameActivityMap = new HashMap <String, AmpActivityVersion> ();
        List <AmpActivityVersion> noFundingActivities = null;
        for (AmpActivityVersion actItem : acts) {
            if (actItem.getFunding() != null && !actItem.getFunding().isEmpty()) {
                /*
                Set <String> nameSorter = new HashSet<String>();
                for (Object fndObj : actItem.getFunding()) {
                    AmpFunding fnd = (AmpFunding) fndObj;
                    nameSorter.add(fnd.getAmpDonorOrgId().getName());
                }
                List <String> sortedList = new ArrayList<String>(nameSorter);
                Collections.sort(sortedList);*/
                //donorNameActivityMap.put(sortedList.get(0), actItem);
                //donorNameActivityMap.put(((AmpFunding)actItem.getFunding().iterator().next()).getAmpDonorOrgId().getName(), actItem);
                
                StringBuilder donorNames = new StringBuilder();
                
                List organizations = new ArrayList(actItem.getFunding());
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

  public static AmpActivityVersion getProjectChannelOverview(Long id) {
    Session session = null;
    AmpActivityVersion activity = null;

    try {
      logger.debug("Id is " + id);
      session = PersistenceManager.getRequestDBSession();

      // modified by Priyajith
      // Desc: removed the usage of session.load and used the select query
      // start
      String queryString = "select a from " + AmpActivityVersion.class.getName()
          + " a " + "where (a.ampActivityId=:id)";
      Query qry = session.createQuery(queryString);
      qry.setLong("id", id);
      activity =(AmpActivityVersion)qry.uniqueResult();
      // end
    }
    catch (Exception ex) {
      logger
          .error("Unable to get Amp Activity getProjectChannelOverview() :"
                 + ex);
    }
    return activity;
  }
   /*
   * get the  the Contracts for Activity
   * 
   */
  
  public static List getIPAContracts(Long activityId) {
    Session session = null;
    List<IPAContract> contrcats = null;

    try {
      session = PersistenceManager.getRequestDBSession();

     
      String queryString = "select con from " + IPAContract.class.getName()
          + " con " + "where (con.activity=:activityId)";
      Query qry = session.createQuery(queryString);
      qry.setLong("activityId",activityId );
      contrcats = qry.list();
      ArrayList<IPAContract> fullContracts=new ArrayList<IPAContract>();
      String cc="";
      for(Iterator i=contrcats.iterator();i.hasNext();)
      {
    	  IPAContract c=(IPAContract) i.next();
    	  cc=c.getTotalAmountCurrency().getCurrencyCode();
    	  double td=0;
    	  for(Iterator j=c.getDisbursements().iterator();j.hasNext();)
    	  {
    		  IPAContractDisbursement cd=(IPAContractDisbursement) j.next();
    		  if(cd.getAmount()!=null)
    			  td+=cd.getAmount().doubleValue();
    	  }
    	  if(c.getDibusrsementsGlobalCurrency()!=null)
        	   cc=c.getDibusrsementsGlobalCurrency().getCurrencyCode();
    	  c.setTotalDisbursements(new Double(td));
    	  c.setExecutionRate(ActivityUtil.computeExecutionRateFromTotalAmount(c, c.getTotalAmountCurrency().getCurrencyCode()));
		  c.setFundingTotalDisbursements(ActivityUtil.computeFundingDisbursementIPA(c, cc));
		  c.setFundingExecutionRate(ActivityUtil.computeExecutionRateFromContractTotalValue(c, cc));
    	  
      }
    }
     
    catch (Exception ex) {
      logger.error(" [getIPAContracts(Long activityId)] Unable to get IPAContracts :"  + ex);
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
//      	contract.setFundingTotalDisbursements(td);
//      	contract.setFundingExecutionRate(ActivityUtil.computeExecutionRateFromContractTotalValue(contract, cc));
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
			  execRate=c.getTotalDisbursements()/finalAmount1;
		  c.setExecutionRate(execRate);
		  return execRate;
  	}

  	public static List<AmpActivityFake> getLastUpdatedActivities() {
 		String workspaceQuery = Util.toCSStringForIN(org.digijava.module.gis.util.DbUtil.getAllLegalAmpActivityIds());
  		
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
   * get the list of all the activities
   * to display in the activity manager of Admin
   */
  public static List<AmpActivityVersion> getAllActivitiesList() {
    List col = null;
    Session session = null;
    Query qry = null;

    try {
      session = PersistenceManager.getRequestDBSession();
      String queryString = "select ampAct from " + AmpActivityVersion.class.getName() +
          " ampAct";
      qry = session.createQuery(queryString);
      col = qry.list();
      logger.debug("the size of the ampActivity : " + col.size());
    }
    catch (Exception e1) {
      logger.error("Could not retrieve the activities list from getallactivitieslist");
      e1.printStackTrace(System.out);
    }
    return col;
  }
  
  public static List<AmpActivityVersion> getAllAssignedActivitiesList() {
	    List col = null;
	    Session session = null;
	    Query qry = null;

	    try {
	      session = PersistenceManager.getRequestDBSession();
	      String queryString = "select ampAct from " + AmpActivityVersion.class.getName() + " ampAct where ampAct.team is not null";
	      qry = session.createQuery(queryString);
	      col = qry.list();
	      logger.debug("the size of the ampActivity : " + col.size());
	    }
	    catch (Exception e1) {
	      logger.error("Could not retrieve the activities list from getallactivitieslist");
	      e1.printStackTrace(System.out);
	    }
	    return col;
	  }
  

  /*
   * get the list of all the activities
   * to display in the activity manager of Admin
   */
  public static List<AmpActivityVersion> getAllActivitiesByName(String name) {
    List col = null;
    Session session = null;
    Query qry = null;

    try {
      session = PersistenceManager.getSession();
      String queryString = "select ampAct from " + AmpActivityVersion.class.getName() +
    		String.format(" ampAct where upper(%s) like upper(:name)",
    		AmpActivityVersion.hqlStringForName("ampAct"));
      qry = session.createQuery(queryString);
      qry.setParameter("name", "%" + name + "%", StringType.INSTANCE);
      col = qry.list();
      logger.debug("the size of the ampActivity : " + col.size());
    }
    catch (Exception e1) {
      logger.error("Could not retrieve the activities list from getallactivitiesbyname", e1);
      e1.printStackTrace();
    }
    return col;
  }
  
  public static List <AmpActivityGroup> getActivityGroups(Session session , Long actId){
	  List <AmpActivityGroup> retVal=null;
	  //Session session = null;
	  Query qry = null;
	  try {
		  //session = PersistenceManager.getRequestDBSession();	      
	      String queryString ="select group from "+ AmpActivityGroup.class.getName()+" group where group.ampActivityLastVersion.ampActivityId="+actId;
	      qry = session.createQuery(queryString);
	      retVal = qry.list();
	} catch (Exception e) {
		logger.error("Could not retrieve groups list");
	    e.printStackTrace(System.out);
	}
	  return retVal;
  }
   
    public static void deleteActivityContent(AmpActivityVersion ampAct, Session session) throws Exception{

        /* delete AMP activity Survey */
        Set ampSurvey = ampAct.getSurvey();
        if (ampSurvey != null) {
          Iterator surveyItr = ampSurvey.iterator();
          while (surveyItr.hasNext()) {
            AmpAhsurvey ahSurvey = (AmpAhsurvey) surveyItr.next();
            Set ahAmpSurvey = ahSurvey.getResponses();
            if (ahSurvey != null) {
              Iterator ahSurveyItr = ahAmpSurvey.iterator();
              while (ahSurveyItr.hasNext()) {
                AmpAhsurveyResponse surveyResp = (AmpAhsurveyResponse)
                    ahSurveyItr.next();
                ahSurvey.getResponses().remove(surveyResp);
                session.delete(surveyResp);
                session.flush();
                ahSurveyItr = ahSurvey.getResponses().iterator();
              }
            }
            ampAct.getSurvey().remove(ahSurvey);
            session.delete(ahSurvey);
            session.flush();
            surveyItr = ampAct.getSurvey().iterator();
          }
        }

        //	 delete all previous comments
        ArrayList col = org.digijava.module.aim.util.DbUtil.
            getAllCommentsByActivityId(ampAct.getAmpActivityId(), session);
        logger.info("col.size() [Inside deleting]: " + col.size());
        if (col != null) {
          Iterator itr = col.iterator();
          while (itr.hasNext()) {
            AmpComments comObj = (AmpComments) itr.next();
            comObj.setAmpActivityId(null);
            session.delete(comObj);
          }
        }
        String deleteActivityComments = "DELETE FROM amp_comments WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        Connection con = ((SessionImplementor)session).connection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate(deleteActivityComments);
        logger.info("comments deleted");
        
        //Delete the connection with Team.
        String deleteActivityTeam = "DELETE FROM amp_team_activities WHERE amp_activity_id = " + ampAct.getAmpActivityId();
         con = ((SessionImplementor)session).connection();
         stmt = con.createStatement();
        int deletedRows = stmt.executeUpdate(deleteActivityTeam);
        
        //Delete the connection with amp_physical_performance.
        String deletePhysicalPerformance = "DELETE FROM amp_physical_performance WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        con = ((SessionImplementor)session).connection();
        stmt = con.createStatement();
        deletedRows = stmt.executeUpdate(deletePhysicalPerformance);
        
        //Delete the connection with Indicator Project. 
        //String deleteIndicatorProject = "DELETE FROM amp_indicator_project WHERE amp_activity_id = " + ampAct.getAmpActivityId();
        //con = session.connection();
        //stmt = con.createStatement();
        //deletedRows = stmt.executeUpdate(deleteIndicatorProject);
        
//        ArrayList ipacontracts = org.digijava.module.aim.util.DbUtil.getAllIPAContractsByActivityId(ampAct.getAmpActivityId());
//	    logger.debug("contracts number [Inside deleting]: " + ipacontracts.size());
//	    if (ipacontracts != null) {
//	      Iterator itr = ipacontracts.iterator();
//	      while (itr.hasNext()) {
//	        IPAContract contract = (IPAContract) itr.next();
//	        session.delete(contract);
//	      }
//	    }
//	    logger.debug("contracts deleted");

      
      
    //Section moved here from ActivityManager.java because it didn't worked there.
	//ActivityUtil.deleteActivityAmpComments(DbUtil.getActivityAmpComments(ampActId), session);
    
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
   * VERY SLOW. Use {@link #getAllTeamAmpActivitiesResume(Long, boolean, String, String...)}
   * @param ampActIds
   * @param session
   * @return
   */
  public static List<AmpActivity> getActivityById(Set<Long> ampActIds ,Session session){
	  List<AmpActivity> act = null;
      if (ampActIds != null && !ampActIds.isEmpty()) {
	  try {
	      Query qry = null;
	      String queryString = "select a from "
	          + AmpActivity.class.getName()
	          + " a where a.ampActivityId in(:ampActIds) ";
	          //+ " where (phyCompReport.ampActivityId=:ampActId)";
	      qry = session.createQuery(queryString);
	      qry.setParameterList("ampActIds", ampActIds);
	      qry.setCacheable(false);
	      act = qry.list();
	  }
	    catch (Exception e1) {
	      logger.error("Could not retrieve the activities ");
	      e1.printStackTrace(System.out);
	    }
      } else {
          act = new ArrayList<AmpActivity> ();
      }
      return act;
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

  /* functions to DELETE an activity by Admin end here.... */


  public static class ActivityAmounts {
    private Double proposedAmout;
    @Deprecated
    private Double plannedAmount;
    private Double actualAmount;
    private Double actualDisbAmount;

    public void AddPalenned(double amount) {
      if (plannedAmount != null) {
        plannedAmount = new Double(plannedAmount.doubleValue() + amount);
      }
      else {
        plannedAmount = new Double(amount);
      }
    }

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

    public String plannedAmount() {
      if (plannedAmount == null|| plannedAmount == 0) {
        return "N/A";
      }
      return FormatHelper.formatNumber(plannedAmount);
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

    public double getPlannedAmount() {
      if (plannedAmount == null) {
        return 0;
      }
      return plannedAmount.doubleValue();
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
      if (CategoryConstants.ACTIVITY_STATUS_PROPOSED.equalsCategoryValue(statusValue) && act.getFunAmount() != null) {
        String currencyCode = act.getCurrencyCode();
        //AMP-1403 assume USD if no code is specified
        if (currencyCode == null || currencyCode.trim().equals("")) {
          currencyCode = "USD";
        } //end of AMP-1403
        //apply program percent
        tempProposed = CurrencyWorker.convert(act.getFunAmount().doubleValue()*percent/100,tocode);
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
                  result.AddPalenned(calculations.getTotPlannedComm().doubleValue()*percent/100);
                  result.AddActualDisb(calculations.getTotActualDisb().doubleValue()*percent/100);
              }
          }
        }

    }
    return result;
  }

  public static List getActivityProgramsByProgramType(Long actId, String settingName) {
                Session session = null;
                List col = new ArrayList();
                try {
                       session = PersistenceManager.getRequestDBSession();
                       String queryString = "select ap from " +AmpActivityProgram.class.getName() +
                       " ap join ap.programSetting s where (ap.activity=:actId) and (s.name=:settingName)";
                       Query qry = session.createQuery(queryString);
                       qry.setLong("actId",actId);
                       qry.setString("settingName",settingName);
                       col = qry.list();
                } catch (Exception e) {
                       logger.error("Unable to get all components");
                       logger.error(e.getMessage());
                }
                return col;
       }
  public static boolean isImplLocationCountry(Long actId) {
                Session session = null;
                boolean flag = false;
                try {
                       session = PersistenceManager.getRequestDBSession();
                       String queryString = "select apl from " +AmpActivityLocation.class.getName() +
                       " apl join apl.location l where (apl.activity=:actId) and (l.ampRegion is not NULL or l.ampZone is not  NULL or l.ampWoreda is not NULL)";
                       Query qry = session.createQuery(queryString);
                       qry.setLong("actId",actId);
                       if(qry.list()!=null&&qry.list().size()>0){
                           flag=true;
                       }
                } catch (Exception e) {
                       logger.error("Unable to get locations");
                       logger.error(e.getMessage());
                }
                return flag;
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
   * Comparator for AmpActivityVersion class.
   * Compears activities by its ID's.
   * AmpActivityVersion is comparable itself, but it is comparable by names,
   * so this class was created to compeare them with ID's
   * @see AmpActivityVersion#compareTo(AmpActivityVersion)
   */
  public static class ActivityIdComparator
      implements Comparator<AmpActivityVersion> {

    public int compare(AmpActivityVersion act1, AmpActivityVersion act2) {
      return act1.getAmpActivityId().compareTo(act2.getAmpActivityId());
    }
  }

  /**
   * Creates map from {@link AmpActivityReferenceDoc} collection
   * where each elements key is the id of {@link AmpCategoryValue} object which is asigned to the element itself
   *
   */
  public static class CategoryIdRefDocMapBuilder implements AmpCollectionUtils.KeyResolver<Long, AmpActivityReferenceDoc>{

	public Long resolveKey(AmpActivityReferenceDoc element) {
		return element.getCategoryValue().getId();
	}

  }
  
  /**
   * generates ampId
   * @param user,actId
   * @return ampId
   * @author dare
 * @param session 
   */
  public static String generateAmpId(User user,Long actId, Session session) {
		String retValue=null;		
		String globSetting="numeric";// TODO This should come from global settings
		if(globSetting.equals("numeric")){
			retValue=numericAmpId(user,actId,session);
		}else if(globSetting.equals("text")){
			retValue=combinedAmpId(actId);
		}
		return retValue;
	}
/**
 * combines countryId, current member id and last activityId+1 and makes ampId
 * @param user,actId
 * @return 
 * @author dare
 * @param session 
 */
	private static String numericAmpId(User user,Long actId, Session session){
		String retVal=null;
		String countryCode=FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY);
		String userId=user.getId().toString();
	    Country country=(Country) session.load(Country.class, countryCode);   
            //    Country country=DbUtil.getDgCountry(countryCode);
                String countryId="0";
                if(country!=null){
                    countryId=country.getCountryId().toString();
                }
		
		String lastId=null;
		if(actId!=null){
			 lastId = actId.toString();	
		}		
		retVal=countryId+userId+lastId;
		return retVal;
	}

	/**
	 * combines countryId, current member id (for admin is 00) and last activityId+1 and makes ampId
	 * @param user,actId
	 * @return 
	 * @author dan
	 */
		public static String numericAmpId(String user,Long actId){
			String retVal=null;
			String countryCode=FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY);
			String userId=user;
	                Country country=DbUtil.getDgCountry(countryCode);
	                String countryId="0";
	                if(country!=null){
	                    countryId=country.getCountryId().toString();
	                }
			
			String lastId=null;
			if(actId!=null){
				 lastId = actId.toString();	
			}		
			retVal=countryId+userId+lastId;
			return retVal;
		}
	
	/**
	 * combines countryIso and last activityId+1 and makes ampId
	 * @param actId
	 * @return 
	 * @author dare
	 */
	private static String combinedAmpId(Long actId){
		String retVal=null;
		String countryCode=FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBAL_DEFAULT_COUNTRY);
		String lastId=null;
		if(actId!=null){
			 lastId = actId.toString();	
		}	
		retVal=countryCode.toUpperCase()+"/"+lastId;		
		return retVal;
	}
	
	public static Collection getActivitiesRelatedToAmpTeamMember(Session session, Long ampTeamMemberId) {
		  try {
	            String queryStr	= "SELECT a FROM " + AmpActivityVersion.class.getName()  + " a left join a.member m WHERE " +
	            			"(a.activityCreator=:atmId) OR (a.modifiedBy=:atmId) OR (a.approvedBy = :atmId) OR (m.ampTeamMemId = :atmId)  OR (a.modifiedBy = :atmId)";
	            Query qry = session.createQuery(queryStr);
	            qry.setLong("atmId", ampTeamMemberId);
	            
	            return qry.list();
	            
		  }
		  catch (Exception ex) {
	        ex.printStackTrace();
	        logger.error("There was an error getting all activities related to AmpTeamMember " + ampTeamMemberId);
	        return null;
		  }
	}
	
	public static String collectionToCSV(Collection<AmpActivityVersion> activities) {
		if ( activities == null )
			return null;
		String ret	= "";
		Iterator<AmpActivityVersion> iter	= activities.iterator();
		while ( iter.hasNext() ) {
			AmpActivityVersion activity	= iter.next();
			if ( activity.getName() != null ) {
				ret	+= "'" + activity.getName() + "'" + ", ";
			}
			else
				ret +=  "' '" + ", ";
		}
		return ret.substring(0, ret.length()-2);
		
	}
        
        /**
         * @author Dare
         * @param partOfName
         * @return Array of Strings,which have a look like: activity_name(activiti_id) 
         */
        public static String[] loadActivitiesNamesAndIds(TeamMember member) throws DgException{
        	Session session=null;
    		String queryString =null;
    		Query query=null;
    		List activities=null;
    		String [] retValue=null;
    		try {
                    session=PersistenceManager.getRequestDBSession();
                    
                Set<String> activityStatus = new HashSet<String>();
                String teamType=member.getTeamType();
                activityStatus.add(Constants.APPROVED_STATUS);
                activityStatus.add(Constants.EDITED_STATUS);
                Set relatedTeams=TeamUtil.getRelatedTeamsForMember(member);
                Set teamAO = TeamUtil.getComputedOrgs(relatedTeams);
                String activityNameString = AmpActivityVersion.hqlStringForName("a");
                // computed workspace
                if (teamAO != null && !teamAO.isEmpty()) {
                  	queryString = "select " + activityNameString + ", a.ampActivityId from " + AmpActivity.class.getName() + " a left outer join a.orgrole r  left outer join a.funding f " +
                   			" where  a.team in  (" + Util.toCSStringForIN(relatedTeams) + ")    or (r.organisation in  (" + Util.toCSStringForIN(teamAO) + ") or f.ampDonorOrgId in (" + Util.toCSStringForIN(teamAO) + ")) order by " + activityNameString;

                } else 
                {
                	// none computed workspace
                    queryString = "select " + activityNameString + ", a.ampActivityId from " + AmpActivity.class.getName() + " a  where  a.team in  (" + Util.toCSString(relatedTeams) + ")    ";
                    if (teamType!= null && teamType.equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
                    	queryString += "  and approvalStatus in (" + Util.toCSString(activityStatus) + ")  ";
                    }
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

                Set<String> activityStatus = new HashSet<String>();
                String teamType=member.getTeamType();
		activityStatus.add(Constants.APPROVED_STATUS);
		activityStatus.add(Constants.EDITED_STATUS);
                Set relatedTeams=TeamUtil.getRelatedTeamsForMember(member);
                    Set teamAO = TeamUtil.getComputedOrgs(relatedTeams);
                    // computed workspace
//                    if (teamAO != null && !teamAO.isEmpty()) {
//                        queryString = "select a.name, a.ampActivityId from " + AmpActivityVersion.class.getName() + " a left outer join a.orgrole r  left outer join a.funding f " +
//                                " where  a.team in  (" + Util.toCSString(relatedTeams) + ")    or (r.organisation in  (" + Util.toCSString(teamAO) + ") or f.ampDonorOrgId in (" + Util.toCSString(teamAO) + ")) and lower(a.name) like lower(:searchStr) order by a.name";
//
//                    } else {
//                        // none computed workspace
//                        queryString = "select a.name, a.ampActivityId from " + AmpActivityVersion.class.getName() + " a  where  a.team in  (" + Util.toCSString(relatedTeams) + ") and lower(a.name) like lower(:searchStr)";
//                        if (teamType!= null && teamType.equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
//                            queryString += "  and approvalStatus in (" + Util.toCSString(activityStatus) + ")  ";
//                        }
//                        queryString += " order by a.name ";
//                    }
                    
                    String activityName = AmpActivityVersion.hqlStringForName("gr.ampActivityLastVersion");
                    queryString ="select " + activityName + ", gr.ampActivityLastVersion.ampActivityId from "+ AmpActivityGroup.class.getName()+" gr ";                    
                    if (teamAO != null && !teamAO.isEmpty()) {
                    	queryString +=" left outer join gr.ampActivityLastVersion.orgrole r  left outer join gr.ampActivityLastVersion.funding f "+
                    	" where gr.ampActivityLastVersion.team in (" + Util.toCSStringForIN(relatedTeams) + ")  " +
                    			" or (r.organisation in  (" + Util.toCSStringForIN(teamAO) + ") or f.ampDonorOrgId in (" + Util.toCSStringForIN(teamAO) + ")) ";
                    	
                    } else {
                        // none computed workspace
                    	queryString +=" where gr.ampActivityLastVersion.team in  (" + Util.toCSStringForIN(relatedTeams) + ") ";                    	
                        if (teamType!= null && teamType.equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
                            queryString += "  and gr.ampActivityLastVersion.approvalStatus in (" + Util.toCSString(activityStatus) + ")  ";
                        }
                        
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
        	Session session=null;
    		String queryString =null;
    		Query query=null;    		
    		String name=null;
    		try {
    			session=PersistenceManager.getRequestDBSession();                   
    			String activityName = AmpActivityVersion.hqlStringForName("gr.ampActivityLastVersion");
    			queryString ="select " + activityName + " from "+ AmpActivityGroup.class.getName()+" gr where gr.ampActivityLastVersion.ampActivityId = " + actId;                    
    			query=session.createQuery(queryString);    			
    			name=(String)query.uniqueResult();    			
    		}catch(Exception ex) { 
    			logger.error("couldn't load Activity" + ex.getMessage());	
    			ex.printStackTrace(); 
    		} 
    		return name;
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
      
	public static String getApprovedActivityQueryString(String label) {
//		String query = null;
//		query = " AND " + label + ".draft = false AND " + label + ".approvalStatus LIKE 'approved' ";
		String query = String.format(" AND (%s.draft IS NULL OR %s.draft = false) AND (%s.approvalStatus='%s' OR %s.approvalStatus='%s')",
				label, label, 
				label, Constants.APPROVED_STATUS,
				label, Constants.STARTED_APPROVED_STATUS
				);
		return query;
	}

	public static String getNonDraftActivityQueryString(String label) {
		String query = null;
		query = " AND " + label + ".draft = false ";
		return query;
	}
	
	
    public static ArrayList<AmpActivityFake> getAllActivitiesAdmin(String searchTerm) {
        try {
            Session session = PersistenceManager.getSession();
            
            boolean isSearchByName = searchTerm != null && (!searchTerm.trim().isEmpty());
            String activityName = AmpActivityVersion.hqlStringForName("f");
            String nameSearchQuery = isSearchByName ? " (f.ampActivityId IN (SELECT t.objectId FROM " + AmpContentTranslation.class.getName() + " t WHERE t.objectClass = '" + AmpActivityVersion.class.getName() + "' AND upper(t.translation) like upper(:searchTerm))) AND " :
            	"";
            
            String queryString = "select f.ampActivityId, f.ampId, " + activityName + ", ampTeam , ampGroup FROM " + AmpActivity.class.getName() +  
            	" as f left join f.team as ampTeam left join f.ampActivityGroup as ampGroup WHERE " + nameSearchQuery + " ((f.deleted = false) or (f.deleted is null))";
            
            Query qry = session.createQuery(queryString);
            if(isSearchByName) {
            	qry.setString("searchTerm", "%" + searchTerm + "%");
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

    public static void deleteAmpActivityWithVersions(Long ampActId){
    	  Session session = null;
		    Transaction tx = null;

		    try {
		      session = PersistenceManager.getSession();
//beginTransaction();

		      List<AmpActivityGroup> groups=getActivityGroups(session , ampActId);
		      if(groups!=null && groups.size()>0){
		      		for (AmpActivityGroup ampActivityGroup : groups) {
		      			
		      			Query qry = session.createQuery("UPDATE " + AmpActivityVersion.class.getName()+ " SET ampActivityPreviousVersion = NULL WHERE ampActivityGroup = " + ampActivityGroup.getAmpActivityGroupId());
						qry.executeUpdate();
						
		      			Set<AmpActivityVersion> activityversions=ampActivityGroup.getActivities();
		      			if(activityversions!=null && activityversions.size()>0){
		      				for (Iterator<AmpActivityVersion> iterator = activityversions.iterator(); iterator.hasNext();) {
		      					AmpActivityVersion ampActivityVersion=iterator.next();
		      					ampActivityVersion.setAmpActivityGroup(null);
		      					session.update(ampActivityVersion);
		      					deleteFullActivityContent(ampActivityVersion,session);
		      					session.delete(ampActivityVersion);
							}
		      			}
		      			else{
		      				AmpActivityVersion ampAct = (AmpActivityVersion) session.load(AmpActivityVersion.class, ampActId);
		      				deleteFullActivityContent(ampAct,session);
	      					session.delete(ampAct);
		      			}
		  				session.delete(ampActivityGroup);
		  			}
		      	}
		      //tx.commit();
//session.flush();
		      
		    }
		    catch (Exception e1) {
			      logger.error("Could not delete the activity with id : " + ampActId);
			      e1.printStackTrace(System.out);
			    }
			    finally {
			      if (session != null) {
			        try {
			          PersistenceManager.releaseSession(session);
			        }
			        catch (Exception e2) {
			          logger.error("Release session failed");
			        }
			      }
			    }
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
    
	public static void archiveAmpActivityWithVersions(Long ampActId) {
		logger.error("archiving activity and all of its versions: " + ampActId);
		Transaction tx = null;
		try{
			Session session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			List<AmpActivityGroup> groups = getActivityGroups(session, ampActId);
			logger.error("\tactivity groups linked with this ampActId: " + Util.toCSString(groups));
			if(groups != null && groups.size() > 0){
				for (AmpActivityGroup ampActivityGroup : groups) {
					logger.error("\tprocessing AmpActivityGroup with id = " + ampActivityGroup.getAmpActivityGroupId());
					for(AmpActivityVersion ampActivityVersion: ampActivityGroup.getActivities()){
						logger.error("\t\tmarking AmpActivityVersion as deleted, id = " + ampActivityVersion.getAmpActivityId());
						session.createQuery("UPDATE " + AmpActivityVersion.class.getName() + " aav SET aav.deleted = true WHERE aav.ampActivityId = " + ampActivityVersion.getAmpActivityId()).executeUpdate();
					}
				}
			}		      
//			logger.error("\tnow marking the ampActId per se as deleted: " + ampActId);
//			session.createQuery("UPDATE " + AmpActivityVersion.class.getName() + " aav SET aav.deleted = true WHERE aav.ampActivityId = " + ampActId).executeUpdate();	     
			logger.error("\tnow committing transaction");
			tx.commit();
		}
		catch (Exception e) {
			logger.error("error while marking activity as deleted: ", e);
			tx.rollback();
		}
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
		try
		{			
			String query = "SELECT a.amp_activity_id FROM amp_activity_version a WHERE a.amp_activity_id IN (" + TeamUtil.getCommaSeparatedList(activityIds) + ") " +
				"AND (a.approval_status = 'started' OR a.approval_status='edited') AND (a.draft IS NULL OR a.draft IS FALSE)"; // AND (a.amp_team_id = " + tm.getTeamId() + ")";
			
			List<BigInteger> validated_activity_ids = PersistenceManager.getSession().createSQLQuery(query).list();
			Set<Long> result = new HashSet<Long>();
			for(BigInteger bi:validated_activity_ids)
				result.add(bi.longValue());
			
			return result;
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static boolean shouldThisUserBeAbleToEdit(TeamMember tm, Long activityId)
	{
		if (tm == null)
			return false;
		return WorkspaceFilter.isActivityWithinWorkspace(activityId);
	}
	
	public static boolean shouldThisUserValidate (TeamMember tm, Long activityId) {
		if (tm.getTeamHead() )
		//synchronized(lock) // cheaper to synchronize than to get a new connection every time
		{
			try 
			{						
				String query = "SELECT a.amp_activity_id, a.amp_team_id, a.draft, a.approval_status from amp_activity_version a where a.amp_activity_id = " + activityId;
				
				List<Object[]> sqlRes = PersistenceManager.getSession().createSQLQuery(query).list();				
				
				boolean returnValue = false;
				
				int count = sqlRes.size();
				if (count != 1)
					return false;
				
				Object[] rs = sqlRes.get(0);

				long actId = ((BigInteger) rs[0]).longValue();
				long teamId = ((BigInteger) rs[1]).longValue();
				Boolean draft = (Boolean) rs[2];
				String status = (String) rs[3];
					
				if (draft == null)
					draft = false;
					
				if (true || tm.getTeamId().equals(teamId) ) {
					if ( !draft && ("started".equals(status)||"edited".equals(status)) )
					returnValue = true;
				}
				
				return returnValue;
				
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}			
		}		
		return false;
	}
	
	
	 public static void changeActivityArchiveStatus(Collection<Long> activityIds, boolean status) {
			try {
				Session session 			= PersistenceManager.getRequestDBSession();
				String qryString			= "update " + AmpActivityVersion.class.getName()  + 
						" av  set av.archived=:archived where av.ampActivityId in (" + Util.toCSStringForIN(activityIds) + ")";
				Query query					= session.createQuery(qryString);
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
} // End
