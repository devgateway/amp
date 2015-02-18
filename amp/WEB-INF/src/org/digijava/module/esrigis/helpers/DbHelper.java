package org.digijava.module.esrigis.helpers;

/**
 * @author Diego Dimunzio
 */

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.visualization.util.DashboardUtil;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class DbHelper {
	private static Logger logger = Logger.getLogger(DbHelper.class);

	public static void delete(AmpMapConfig map) {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			// beginTransaction();
			sess.delete(map);
			// tx.commit();
		} catch (Exception e) {
			if (e instanceof JDBCException)
				throw (JDBCException) e;
			logger.error("Exception " + e.toString());
			try {
				tx.rollback();
			} catch (HibernateException ex) {
				logger.error("rollback() failed");
				logger.error(ex.toString());
			}
		}
	}

	public static AmpMapConfig getMap(Long id) {
		Session session = null;
		AmpMapConfig map = null;
		try {
			session = PersistenceManager.getRequestDBSession();
            StringBuilder qs = new StringBuilder("from ").append(AmpMapConfig.class.getName()).
                    append(" mc where mc.id=:ID");
            Query q = session.createQuery(qs.toString());
            q.setLong("ID", id);

			//map = (AmpMapConfig) session.load(AmpMapConfig.class, id);
            map = (AmpMapConfig) q.uniqueResult();
		} catch (Exception e) {
			logger.error("Unable to get object of class "
					+ AmpMapConfig.class.getName() + " width id=" + id
					+ ". Error was:" + e);
		}
		return map;
	}

	public static void save(AmpMapConfig map) {
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			session.saveOrUpdate(map);
		} catch (Exception e) {
			logger.error("Unable to save item", e);
		}

	}
	
	
	/**
	 * returns list of all activities which have funding items which match the filter 
	 * @param filter
	 * @param request
	 * @return
	 * @throws DgException
	 */
	public static List<Long> getActivitiesIds(MapFilter filter, Boolean forceUseMtefProjections)
			throws DgException {
		Long[] orgGroupIds = filter.getSelOrgGroupIds();
		List<Long> activities = null;
		Long[] orgIds = filter.getOrgIds();
		Long[] implOrgIds = filter.getImplOrgIds();
		Long[] implOrgGroupIds = filter.getImplOrgGroupIds();

		Long[] orgtypeids = filter.getOrgtypeIds();
		Long [] natPlanObjIds = filter.getSelectedNatPlanObj();
		Long [] primaryProgramsIds = filter.getSelectedPrimaryPrograms();
		Long [] secondaryProgramsIds = filter.getSelectedSecondaryPrograms();
		
		boolean useMtefProjections = forceUseMtefProjections != null ? 
					forceUseMtefProjections : filter.getTransactionType() == Constants.MTEFPROJECTION;
		
		//int transactionType = filter.getTransactionType();
//		TeamMember teamMember = filter.getTeamMember();
		// apply calendar filter
		Long fiscalCalendarId = filter.getFiscalCalendarId();
		Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
		Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
		Long[] locationIds = filter.getSelLocationIds();
		boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
		Long[] zonesids = filter.getZoneIds();
		//boolean zonescondition = zonesids != null && zonesids.length > 1;
		Long[] sectorIds = filter.getSelSectorIds();
		boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
		boolean structureTypeCondition = filter.getSelStructureTypes() != null && !QueryUtil.inArray(-1l,filter.getSelStructureTypes() );
		boolean programCondition = (natPlanObjIds!= null && natPlanObjIds.length>0) || (primaryProgramsIds!= null && primaryProgramsIds.length>0) || (secondaryProgramsIds!= null && secondaryProgramsIds.length>0);
		
		/*
		 * We are selecting sectors which are funded In selected year by the
		 * selected organization
		 */
		try {
			String oql = "select distinct act.ampActivityId from ";
			
			if (useMtefProjections)
			{
				oql += AmpFundingMTEFProjection.class.getName() + 
					" as fd inner join fd.ampFunding f ";
			}
			else
			{
				oql += AmpFundingDetail.class.getName() + 
					" as fd inner join fd.ampFundingId f ";
			}
			oql += " inner join f.ampActivityId act ";
	        oql += " inner join act.ampActivityGroup actGroup ";
			
			if (locationCondition) {
				oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
			}

			if (sectorCondition) {
				oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
			}
			if (programCondition) {
				oql += " inner join act.actPrograms actPrograms ";
				
			}
			
				oql += " inner join act.orgrole role  ";
			
			if (structureTypeCondition) {
				oql += " inner join act.structures str  ";
			}
			// Status
			if ((filter.getSelprojectstatus()) != null || (filter.getSelectedBudget() != null)) {
				oql += " join  act.categories as categories ";
			}

			oql += " WHERE 1=1 ";
			
			if (!useMtefProjections)
			{
				oql += " AND fd.adjustmentType = " + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase();
			
				switch(filter.getTransactionType()) {
					case Constants.TRANSACTION_TYPE_COMMITMENTS_AND_DISBURSEMENTS:
						oql += " and (fd.transactionType = 0 or  fd.transactionType =1) "; // commitmens OR disbursements
						break;
									
					default:
						oql += " and fd.transactionType =:transactionType  ";
				}
			}
			
			if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
				if (orgGroupIds != null && orgGroupIds.length > 0
						&& orgGroupIds[0] != -1) {
					oql += QueryUtil.getOrganizationQuery(true, orgIds,
							orgGroupIds);
				}
			} else {
				oql += QueryUtil.getOrganizationQuery(false, orgIds,
						orgGroupIds);
			}

			if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
				if (orgtypeids != null && orgtypeids.length > 0
						&& orgtypeids[0] != -1) {
					oql += QueryUtil.getOrganizationTypeQuery(true, orgIds,
							orgtypeids);
				}
			} else if (orgtypeids != null) {
				oql += QueryUtil.getOrganizationTypeQuery(true, orgIds,
						orgtypeids);
				;
			}

			if (useMtefProjections)
			{
				oql += " and  (fd.projectionDate>=:startDate and fd.projectionDate<=:endDate)";
			}
			else
			{
				oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)";
			}
			
			List<Long> workSpaceactivityList = filter.buildFilteredActivitiesList(); 
			String inactivities = Util.toCSStringForIN(workSpaceactivityList);
			oql += " and act.ampActivityId in("+ inactivities +")";
			
			//locations filter
			
			if (locationCondition) {
				locationIds = getAllDescendantsLocation(locationIds);
				filter.setSelLocationIds(locationIds);
				if (zonesids!=null && zonesids.length > 0) {
					zonesids = getAllDescendantsLocation(zonesids);
					Long[] both = (Long[]) ArrayUtils.addAll(locationIds,
							zonesids);
					filter.setSelLocationIds(both);
					oql += " and loc.id in ("
							+ QueryUtil.getInStatement(locationIds) + ","
							+ QueryUtil.getInStatement(zonesids) + ") ";
				} else {
					oql += " and loc.id in ("
							+ QueryUtil.getInStatement(locationIds) + ") ";
				}
			}

			if (sectorCondition) {
				oql += " and sec.id in (" + QueryUtil.getInStatement(sectorIds)
						+ ") ";
			}
			if (programCondition) {
				if (natPlanObjIds!=null && natPlanObjIds.length > 0) {
				oql += " and (actPrograms.program.ampThemeId in  (" + QueryUtil.getInStatement(natPlanObjIds)
						+ ") and actPrograms.programSetting.name='National Plan Objective') ";
				}
				if (primaryProgramsIds!=null && primaryProgramsIds.length > 0) {
					oql += " and (actPrograms.program.ampThemeId in  (" + QueryUtil.getInStatement(primaryProgramsIds)
							+ ") and actPrograms.programSetting.name='Primary Program') ";
					}
				if (secondaryProgramsIds!=null && secondaryProgramsIds.length > 0) {
					oql += " and (actPrograms.program.ampThemeId in  (" + QueryUtil.getInStatement(secondaryProgramsIds)
							+ ") and actPrograms.programSetting.name='Secondary Program') ";
					}
			}

			// Organization Type
			if (filter.getSelorganizationsTypes() != null) {
				oql += " and role.organisation.orgGrpId.orgType in ("+ QueryUtil.getInStatement(filter.getSelorganizationsTypes()) + ")";
			}

			// Implementing Agency
			if (implOrgIds == null || implOrgIds.length == 0|| implOrgIds[0] == -1) {
				if (implOrgGroupIds != null && implOrgGroupIds.length > 0 && implOrgGroupIds[0] != -1) {
					oql += QueryUtil.getOrganizationQuery(true, implOrgIds,implOrgGroupIds, Constants.IMPLEMENTING_AGENCY);
				}
			} else {
				oql += QueryUtil.getOrganizationQuery(false, implOrgIds, implOrgGroupIds, Constants.IMPLEMENTING_AGENCY);
			}

			// Project Status
			if (filter.getSelprojectstatus() != null) {
				oql += " and categories.id in ("
						+ QueryUtil
								.getInStatement(filter.getSelprojectstatus())
						+ ") ";
			}
			// On/Off/treasury budget
			if (filter.getSelectedBudget() != null && filter.getSelectedBudget() != 0) {
					oql += " and categories.id in (" + filter.getSelectedBudget() + ") ";
			}
			// Type of assistance
			if (filter.getSeltypeofassistence() != null) {
				oql += " and f.typeOfAssistance in ("+ QueryUtil.getInStatement(filter.getSeltypeofassistence()) + ") ";
			}
			
			// Financing instrument / Aid modality
			if (filter.getSelfinancingInstruments() != null) {
				oql += " and f.financingInstrument in ("+ QueryUtil.getInStatement(filter.getSelfinancingInstruments()) + ") ";
			}
			
			// Structure Types
			if (structureTypeCondition) {
				oql += " and str.type.typeId in ("+ QueryUtil.getInStatement(filter.getSelStructureTypes()) + ") ";
			}

			oql += ActivityUtil.getApprovedActivityQueryString("act");
			// Show only activities that are not draft
			//oql += ActivityUtil.getNonDraftActivityQueryString("act"); - pointless call - getApprovedActivityQueryString filters by non-draft too, anyway
			
			//Additional clause to get the last version
			if (ActivityVersionUtil.isVersioningEnabled()){
				if(filter.getFromPublicView() !=null&& filter.getFromPublicView())
		        	oql += " and act.ampActivityId = (select agc.ampActivityLastVersion from "+AmpActivityGroupCached.class.getName()+" agc where agc.ampActivityGroup=actGroup.ampActivityGroupId) ";
		        else
		        	oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";	
				oql += " and (act.deleted = false or act.deleted is null)";
			}

			if (filter.isModeexport()) {
				ArrayList<Long> filteractivities = getInActivities(filter.getReportfilterquery());
				inactivities = Util.toCSStringForIN(filteractivities);
				oql += " and act.ampActivityId in(" + inactivities + ")";
			}
			
			
			Session session = PersistenceManager.getRequestDBSession();
			Query query = session.createQuery(oql);
			query.setDate("startDate", startDate);
			query.setDate("endDate", endDate);
			if ((!useMtefProjections) && (filter.getTransactionType() != Constants.TRANSACTION_TYPE_COMMITMENTS_AND_DISBURSEMENTS)) {
				query.setLong("transactionType", filter.getTransactionType());
			}
			
			activities = query.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load activities from db", e);
		}
		return activities;

	}
	
//	public static List<AmpActivityVersion> getActivities(MapFilter filter,HttpServletRequest request)
//			throws DgException {
//
//		List<AmpActivityVersion> activities = null;
//		try {
//			List<Long> ids = getActivitiesIds(filter);
//			if (ids.size()==0){
//				return activities;
//			}
//			String oql = "select distinct act from ";
//			oql += AmpActivityVersion.class.getName() + " act WHERE ampActivityId IN (" + Util.toCSString(ids) + ")";
//			Session session = PersistenceManager.getRequestDBSession();
//			Query query = session.createQuery(oql);
//			
//			activities = query.list();
//		} catch (Exception e) {
//			logger.error(e);
//			throw new DgException("Cannot load activities from db", e);
//		}
//		return activities;
//
//	}

	public static List<AmpActivityVersion> getActivities(MapFilter filter)
			throws DgException {

		List<AmpActivityVersion> activities = null;
		try {
			List<Long> ids = getActivitiesIds(filter, null);
			String activityIdsCSV = Util.toCSStringForIN(ids);
			String oql = "select distinct act from ";
			oql += AmpActivityVersion.class.getName()
					+ " act WHERE ampActivityId IN (" + activityIdsCSV + ")";
			Session session = PersistenceManager.getRequestDBSession();
			Query query = session.createQuery(oql);
			
			activities = query.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load activities from db", e);
		}
		return activities;

	}
	
	public static ArrayList<Long> getInActivities(String query)
			throws Exception {
		Session session = PersistenceManager.getRequestDBSession();
		List<Object> qResult = session.createSQLQuery(query).list();
		ArrayList<Long> result = new ArrayList<Long>(qResult.size());
		for (Object obj : qResult) {
			result.add(PersistenceManager.getLong(obj));
		}
		return result;
	}
	

//	/**
//	 * returns list of AmpCategoryValueLocations.id locations which have activities which are funded according to filter
//	 * @param filter
//	 * @param implementationLevel
//	 * @param request
//	 * @return
//	 * @throws DgException
//	 */
//	public static List<Long> getLocations(MapFilter filter, String implementationLevel) throws DgException
//	{		
//		boolean useMtefProjections = filter.getTransactionType() == Constants.MTEFPROJECTION;
//		
//		List<Long> locations = new ArrayList<Long>();
//		if (filter.getSelLocationIds() != null && filter.getSelLocationIds().length > 0 && filter.getSelLocationIds()[0] != -1) {
//			if (filter.getSelLocationIds().length == 1)
//			{
//				AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[0]);
//				for(AmpCategoryValueLocations childLoc:loc.getChildLocations())
//					locations.add(childLoc.getId());
//				return locations;
//			} else {
//				for (Long locId: filter.getSelLocationIds())
//					if (locId > 0)
//						locations.add(locId);
//				return locations;
//			}
//		} else {
//			
//	        Long[] orgGroupIds = filter.getSelOrgGroupIds();
//	        Long[] orgIds = filter.getOrgIds();
//	        Long[] implOrgIds = filter.getImplOrgIds();
//	        Long[] implOrgGroupIds = filter.getImplOrgGroupIds();
//	        
//	        //int transactionType = filter.getTransactionType();
//	        //TeamMember teamMember = filter.getTeamMember();
//	        // apply calendar filter
//	        Long fiscalCalendarId = filter.getFiscalCalendarId();
//	        
//	        Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
//	        Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
//	        Long[] sectorIds = filter.getSelSectorIds();
//	        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
//	        boolean structureTypeCondition = filter.getSelStructureTypes() != null && !QueryUtil.inArray(-1l,filter.getSelStructureTypes() );
//			
//			AmpCategoryValue budgetOn = null;
//			AmpCategoryValue budgetOff = null;
//			try {
//				budgetOn = CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ACTIVITY_BUDGET_ON);
//				budgetOff = CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ACTIVITY_BUDGET_OFF);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//	        /*
//	         * We are selecting regions which are funded
//	         * In selected year by the selected organization
//	         *
//	         */
//	        try {
//	            String oql = "select distinct loc.id from ";
//	            
//				if (useMtefProjections)
//				{
//					oql += AmpFundingMTEFProjection.class.getName() + 
//						" as fd inner join fd.ampFunding f ";
//				}
//				else
//				{
//					oql += AmpFundingDetail.class.getName() + 
//						" as fd inner join fd.ampFundingId f ";
//				}
//				
//	            oql += " inner join f.ampActivityId act ";
//	            oql += " inner join act.ampActivityGroup actGroup ";
//	            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
//	            oql += " inner join loc.parentCategoryValue parcv ";
//	            oql += " inner join act.orgrole role  ";
//	            
//	            if (sectorCondition) {
//	                oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
//	            }
//	            
//				oql += " WHERE 1=1 ";
//
//				if (!useMtefProjections)
//				{	            
//					oql += " AND fd.adjustmentType ="+CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL).getId();
//				}
//	            
//				switch(filter.getTransactionType())
//				{
//					case Constants.TRANSACTION_TYPE_COMMITMENTS_AND_DISBURSEMENTS:
//						oql += " and (fd.transactionType = 0 or  fd.transactionType =1) "; // commitmens OR disbursements
//						break;
//					
//					case Constants.MTEFPROJECTION:
//						// nothing to filter on
//						break;
//						
//					default:
//						oql += " and fd.transactionType =:transactionType  ";
//				}
//				
//	            if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
//	                if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
//	                    oql += QueryUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
//	                }
//	            } else {
//	                oql += QueryUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
//	            }
//	            
//				if (useMtefProjections)
//				{
//					oql += " and  (fd.projectionDate>=:startDate and fd.projectionDate<=:endDate)";
//				}
//				else
//				{
//					oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)";
//				}
//	            
//	            oql += String.format(" and act.team is not null AND (act.draft=false OR act.draft IS NULL) AND act.approvalStatus IN ('%s','%s') ", Constants.APPROVED_STATUS, Constants.STARTED_APPROVED_STATUS);
//	            
//	            List<Long> workSpaceActivityList = filter.buildFilteredActivitiesList();
//	            String inactivities = Util.toCSStringForIN(workSpaceActivityList);	 
//	    		oql += " and act.ampActivityId in("+ inactivities +")";
//	            
//	            
//	            if (sectorCondition) {
//	                oql += " and sec.id in ("+QueryUtil.getInStatement(sectorIds)+") ";
//	            }
//	            
//	         // Organization Type
//				if (filter.getSelorganizationsTypes() != null) {
//					oql += " and role.organisation.orgGrpId.orgType in ("+ QueryUtil.getInStatement(filter.getSelorganizationsTypes()) + ")";
//				}
//
//				// Implementing Agency
//				if (implOrgIds == null || implOrgIds.length == 0|| implOrgIds[0] == -1) {
//					if (implOrgGroupIds != null && implOrgGroupIds.length > 0 && implOrgGroupIds[0] != -1) {
//						oql += QueryUtil.getOrganizationQuery(true, implOrgIds,implOrgGroupIds, Constants.IMPLEMENTING_AGENCY);
//					}
//				} else {
//					oql += QueryUtil.getOrganizationQuery(false, implOrgIds, implOrgGroupIds, Constants.IMPLEMENTING_AGENCY);
//				}
//
//				// Project Status
//				if (filter.getSelprojectstatus() != null) {
//					oql += " and categories.id in ("+ QueryUtil.getInStatement(filter.getSelprojectstatus())+ ") ";
//				}
//				// On/Off budget
//				if (filter.getOnBudget() != null) {
//					if (filter.getOnBudget() == 1) {
//						oql += " and categories.id in (" + budgetOn.getId() + ") ";
//					} else if (filter.getOnBudget() == 2) {
//						oql += " and categories.id in (" + budgetOff.getId() + ") ";
//					}
//				}
//				// Type of assistance
//				if (filter.getSeltypeofassistence() != null) {
//					oql += " and f.typeOfAssistance in ("+ QueryUtil.getInStatement(filter.getSeltypeofassistence()) + ") ";
//				}
//				
//				// Financing instrument / Aid modality
//				if (filter.getSelfinancingInstruments() != null) {
//					oql += " and f.financingInstrument in ("+ QueryUtil.getInStatement(filter.getSelfinancingInstruments()) + ") ";
//				}
//				
//				// Structure Types
//				if (structureTypeCondition) {
//					oql += " and str.type.typeId in ("+ QueryUtil.getInStatement(filter.getSelStructureTypes()) + ") ";
//				}
//	            
//	            
//				oql += ActivityUtil.getApprovedActivityQueryString("act");
//				oql += ActivityUtil.getNonDraftActivityQueryString("act");
//	            
//	            
//				//Additional clause to get the last version
//				if (ActivityVersionUtil.isVersioningEnabled()){
//					if(filter.getFromPublicView() !=null&& filter.getFromPublicView())
//			        	oql += " and act.ampActivityId = (select agc.ampActivityLastVersion from "+AmpActivityGroupCached.class.getName()+" agc where agc.ampActivityGroup=actGroup.ampActivityGroupId) ";
//			        else
//			        	oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";	
//						oql += " and (act.deleted = false or act.deleted is null)";
//				}
//
//	           
//	            if ("zone".equalsIgnoreCase(implementationLevel)){
//	            	oql += "  and (parcv.value =:implementationLevel or parcv.value =:district or parcv.value =:communal)";
//	            }else{
//	            	oql += "  and (parcv.value =:implementationLevel or parcv.value =:zone or parcv.value =:district or parcv.value =:communal)";
//	            }
//	            //oql+=" order by loc.parentCategoryValue";
//	            Session session = PersistenceManager.getRequestDBSession();
//	            Query query = session.createQuery(oql);
//	            query.setDate("startDate", startDate);
//	            query.setDate("endDate", endDate);
//	            
//	            if ("zone".equalsIgnoreCase(implementationLevel)){
//	            	query.setString("implementationLevel", implementationLevel);
//	            	query.setString("district", "District");
//	            	query.setString("communal", "Communal section");
//	            }else{
//	            	query.setString("implementationLevel", implementationLevel);
//	            	query.setString("zone", "Zone");
//	            	query.setString("district", "District");
//	            	query.setString("communal", "Communal section");
//	            }
//	           
//	            if (filter.isModeexport()){
//					List<Long> filteractivities = getInActivities(filter.getReportfilterquery());
//					inactivities = Util.toCSStringForIN(filteractivities);
//					oql += " and act.ampActivityId in("+ inactivities +")";
//				}
//	            
//				if ((!useMtefProjections) && (filter.getTransactionType() != Constants.TRANSACTION_TYPE_COMMITMENTS_AND_DISBURSEMENTS)) { // the option comm&disb is
//					query.setLong("transactionType", filter.getTransactionType());
//				}
//	            locations = query.list();
//	        }
//	        catch (Exception e) {
//	            logger.error(e);
//	            throw new DgException("Cannot load regions from db", e);
//	        }
//	        return locations;
//		}
//	}
	
	/**
	 * returns a map of all the locations with a certain id
	 * @param ids
	 * @return
	 */
	protected static Map<Long, AmpCategoryValueLocations> getLocationsById(Set<Long> ids)
	{
		Map<Long, AmpCategoryValueLocations> locationsMap = new HashMap<Long, AmpCategoryValueLocations>();
		
		Session session = PersistenceManager.getSession();
		Query createQuery = session.createQuery("from "+AmpCategoryValueLocations.class.getName() + " acvl WHERE acvl.id IN (" + Util.toCSStringForIN(ids) + ")");
		Iterator<AmpCategoryValueLocations> it = createQuery.list().iterator();
		while (it.hasNext()) {
			AmpCategoryValueLocations loc = it.next();
			locationsMap.put(loc.getId(), loc);
		}
		return locationsMap;
	}
	
	/**
	 * constructs a Map<ACVL.id, Region/Zone-ACVL.id>
	 * @param allUsedAcvlIDs
	 * @param impLevel
	 * @return
	 */
	protected static Map<Long, Long> getLocationRegions(Set<Long> allUsedAcvlIDs, String impLevel)
	{
		final String findLocationRegionsQuery = 
				String.format("SELECT acvl.id, getlocationidbyimpllocMap(acvl.id, '%s'::character varying) AS region_id FROM amp_category_value_location acvl WHERE acvl.id IN (" + Util.toCSStringForIN(allUsedAcvlIDs) + ")",
						impLevel);		
		final Map<Long, Long> locationToRegion = new HashMap<Long, Long>();
		PersistenceManager.getSession().doWork(new Work()
		{
			public void execute(java.sql.Connection conn) throws java.sql.SQLException
			{
				try(RsInfo rsi = SQLUtils.rawRunQuery(conn, findLocationRegionsQuery, null)) {
					while (rsi.rs.next())
					{
						Long locId = rsi.rs.getLong(1);
						Long regionLocId = rsi.rs.getLong(2);
						locationToRegion.put(locId, regionLocId);
					}
				}
			}
		});
		return locationToRegion;
	}
	
	/**
	 * generate a list of (Location, Funding) entries based on an unsorted input soup of funding information
	 * @param fundingDets - the input soup of raw funding information:
	 * 	item[0] = FundingInformationItem fd
	 * 	item[1] = [Long id] of region
	 *  item[2] = [String name] of region
	 *  item[3...end] = (optional) [Float locationPercentage, sectorPercentage, xxxPercentage]
	 * @param currCode - the code of the currency to use while doing the calculations
	 * @param adjustmentType - the adjustment type to add the totals to
	 * @param impLevel - the implementation level, used for location levels while walking up the hierarchy
	 * @param regListChildren - Collection<AmpCategoryValueLocations.id>, ignored
	 * @param decimalsToShow: decimals to format the output
	 * @param divideByDenominator: units used for formatting the output
	 * @return
	 */
	protected static ArrayList<SimpleLocation> generateFundingSummaries(List<Object[]> fundingDets, String currCode, HardCodedCategoryValue adjustmentType, String impLevel, int decimalsToShow, BigDecimal divideByDenominator)
	{
		// collect all the location Ids existant in the system
		Set<Long> allUsedAcvlIDs = new HashSet<Long>();
		for(Object[] obj:fundingDets){
			allUsedAcvlIDs.add((Long) obj[1]);
		}

		Map<Long, Long> locationToRegion = getLocationRegions(allUsedAcvlIDs, impLevel); // Map<acvl.id, region.id>
		Map<Long, AmpCategoryValueLocations> regionLocations = getLocationsById(new HashSet<Long>(locationToRegion.values())); //Map<region.id, region>
		        
		// group all funding items by respective region/zone/whatever
        HashMap<Long, ArrayList<FundingInformationItem>> fundingByRegion = new HashMap<Long, ArrayList<FundingInformationItem>>();      
        for(Object[] item:fundingDets)
        {
        	FundingInformationItem fd = (FundingInformationItem) item[0];
        	FundingInformationItem currentFd;
			if(fd.getTransactionType().equals(Constants.MTEFPROJECTION)){
				currentFd=new AmpFundingMTEFProjection(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
			}else{
				currentFd = new AmpFundingDetail(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
			}
        	Double finalAmount = currentFd.getAbsoluteTransactionAmount();
        	for(int i = 3; i < item.length; i++)
        		if (item[i] != null)
        		{
       				Float fl = (Float) item[i];
       				finalAmount *= (fl / 100.0);
        		}
        	currentFd.setTransactionAmount(finalAmount);
        	Long originald = (Long) item[1];
        	Long regionId = locationToRegion.get(originald);
        	if (regionId == null)
        		continue; // this location has no region
        	
        	if (!fundingByRegion.containsKey(regionId))
        		fundingByRegion.put(regionId, new ArrayList<FundingInformationItem>());
        	
        	fundingByRegion.get(regionId).add(currentFd);
        }
        
		ArrayList<SimpleLocation> regionTotals = new ArrayList<SimpleLocation>();
        DecimalWraper totaldisbursement = null;
        DecimalWraper totalexpenditures = null;
        DecimalWraper totalcommitment = null;
        DecimalWraper totalMtef = null;
        Iterator<Long> it2 = fundingByRegion.keySet().iterator();
        while(it2.hasNext())
        {
            Long locId = it2.next();
            //Long regionId = locationToRegion.get(locId);
            Long regionId = null;
            for (Map.Entry<Long, Long> e : locationToRegion.entrySet()) {
            	if (e.getValue()==locId){
            		regionId = e.getKey();
            		break;
            	}
            }
            if (regionId!= 0L && regionId == null)
            	continue;

        	FundingCalculationsHelper cal = new FundingCalculationsHelper();

        	ArrayList<FundingInformationItem> afda = fundingByRegion.get(locId);
            
            cal.doCalculations(afda, currCode, true);
           
            if (CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey().equals(adjustmentType.getValueKey())) {
            	totalexpenditures = cal.getTotActualExp();
            } else {
            	totalexpenditures = cal.getTotPlannedExp();
            }
            
            if (CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey().equals(adjustmentType.getValueKey())) {
            	totaldisbursement = cal.getTotActualDisb();
            } else {
            	totaldisbursement = cal.getTotPlanDisb();
            }
            
            if (CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey().equals(adjustmentType.getValueKey())) {
            	totalcommitment = cal.getTotActualComm();
            } else {
            	totalcommitment = cal.getTotPlannedComm();
            }

            totalMtef = cal.getTotalMtef(); // no adjustment for MTEF
            
            SimpleLocation sl = new SimpleLocation();
            sl.setName(regionLocations.get(locId).getName());
            sl.setGeoId(regionLocations.get(locId).getGeoCode());
            sl.setCommitments(totalcommitment.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP).toString());
            sl.setDisbursements(totaldisbursement.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP).toString());
            sl.setExpenditures(totalexpenditures.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP).toString());
            sl.setAmountsCurrencyCode(currCode); // was filter.getCurrencyCode()
            sl.setMtef(totalMtef.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP).toString());
            regionTotals.add(sl);
        }
	
        return regionTotals;
	}

	/**
	 * fetches all fundings of type (mtef or non-mtef) which match a set of filters
	 * the format of the output Object[] instances is documented in {@link #generateFundingSummaries(List, String, HardCodedCategoryValue, String, Collection, int, BigDecimal)}
	 * @param regListChildren: Collection<AmpCategoryValueLocations.id>
	 * @param mtef: whether to fetch the MTEF or non-mtef part of the fundings
	 * @return funding together with metadata
	 */
	protected static List<Object[]> getFundingsOfType(MapFilter filter, Collection<Long> regListChildren, HardCodedCategoryValue adjustmentType, Date startDate, Date endDate, boolean mtef) throws DgException
	{
		long start = System.currentTimeMillis();
		Long[] orgIds = filter.getOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        
        Long[] sectorIds = filter.getSelSectorIds();
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);

        String oql = "";
       
        oql = "select fd, loc.id, loc.name ";
        //if (filter.getSelProgramIds()!=null && filter.getSelProgramIds().length>0) 
        //	oql += ", actProg.programPercentage ";
        //if (locationCondition)
        	oql += ", actloc.locationPercentage ";
        if (sectorCondition)        	
        	oql += ", actsec.sectorPercentage ";
        
		if(filter.getFromPublicView() !=null&& filter.getFromPublicView())
			oql += " from "+AmpActivityGroupCached.class.getName()+" grpLink inner join grpLink.ampActivityGroup as actGroup, ";
		else 
			oql+= " from ";
		
		if (mtef)
		{
			oql += " " + AmpFundingMTEFProjection.class.getName() + " as fd inner join fd.ampFunding f";
		}
		else
		{
			oql += " " + AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f";
		}
		
        oql += " inner join f.ampActivityId act ";
    	
    	if(!(filter.getFromPublicView() !=null&& filter.getFromPublicView()))
        	oql += " inner join act.ampActivityGroup actGroup ";
    	    oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        if (sectorCondition) {
            oql += "  inner join act.sectors actsec ";
            oql += "  inner join actsec.classificationConfig config  ";
            oql += " inner join actsec.sectorId sec ";
        }
        if (sectorCondition) 
        	oql += " where config.id=:config  ";
        else
        	oql += " where 1=1 ";
        
        if (!mtef)
        {
        	oql += " and  fd.adjustmentType.value =:adjustmentType ";
            oql += " and (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
        }
        else
        {
        	oql += " and (fd.projectionDate>=:startDate and fd.projectionDate<=:endDate)  ";
        }
        if (regListChildren != null)
        	oql += " and loc.id in (" + Util.toCSStringForIN(regListChildren) + ")";

//        //Mapping the locations with their parents
//        HashMap<Long, Long> locationMap = new HashMap<Long, Long>();
//        for(AmpCategoryValueLocations currentLocation : regListChildren ){
//        	locationMap.put(currentLocation.getId(), getTopLevelLocation(currentLocation,impLevel).getId());
//        }

       
        if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) 
            	oql += QueryUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
            	//oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds, filter.getAgencyType());
        } else 
        	oql += QueryUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
            //oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds, filter.getAgencyType());
        
        if (sectorCondition) {
        	sectorIds = getAllDescendants(filter.getSectorIds(),(ArrayList<AmpSector>) filter.getSectors());
            oql += " and sec.id in ("+QueryUtil.getInStatement(sectorIds)+") ";
        }

        //AMP-17549
        List<Long> workSpaceActivityList = getActivitiesIds(filter, mtef);        
        /*List<Long> workSpaceActivityList = filter.buildFilteredActivitiesList();*/
        String inactivities= Util.toCSStringForIN(workSpaceActivityList);
		oql += " and act.ampActivityId in("+ inactivities +")";


        if (ActivityVersionUtil.isVersioningEnabled()) {
			oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";
		}

        oql += " and (act.draft = FALSE OR act.draft IS NULL) and act.approvalStatus IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") ";
        oql += " and (act.deleted = FALSE or act.deleted IS NULL)";

        if (filter.isFilterByPeacebuildingMarker() && filter.getSelectedPeacebuildingMarkerId() != null) {
            oql += " and :PB_MARK_ID in elements(act.categories) ";
        }


        Session session = PersistenceManager.getRequestDBSession();
        List<Object[]> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (filter.isFilterByPeacebuildingMarker() && filter.getSelectedPeacebuildingMarkerId() != null) {
                query.setLong("PB_MARK_ID", filter.getSelectedPeacebuildingMarkerId());
            }

            if (!mtef)
            {
            	query.setString("adjustmentType",adjustmentType.getValueKey());
            }
            if (sectorCondition) {
            	query.setLong("config", filter.getSelSectorConfigId());
            }
            
            fundingDets = query.list();
            logger.info("getFundingsOfType(mtef = " + mtef + ") took " + (System.currentTimeMillis() - start) / 1000.0 + " seconds");
            return fundingDets;
            
        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }		
	}
	
//	protected static List<Long> getFundingIdsForType(MapFilter filter, Collection<Long> regListChildren) throws DgException
//	{
//		Long[] orgIds = filter.getOrgIds();
//        Long[] orgGroupIds = filter.getSelOrgGroupIds();
//        
//        Long[] sectorIds = filter.getSelSectorIds();
//        boolean sectorCondition = filter.hasSectorCondition();
//
//        String oql = "";
//
//       
//        oql = "select distinct(f.id) ";
//        //if (filter.getSelProgramIds()!=null && filter.getSelProgramIds().length>0) 
//        //	oql += ", actProg.programPercentage ";
//        //if (locationCondition)
//        
//		if(filter.getFromPublicView() !=null&& filter.getFromPublicView())
//			oql += " from "+AmpActivityGroupCached.class.getName()+" grpLink inner join grpLink.ampActivityGroup as actGroup, ";
//		else 
//			oql+= " from ";
//
//		oql += AmpFunding.class.getName() + " f ";
////		if (mtef)
////		{
////			oql += " " + AmpFundingMTEFProjection.class.getName() + " as fd inner join fd.ampFunding f";
////		}
////		else
////		{
////			oql += " " + AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f";
////		}
//		
//        oql += " inner join f.ampActivityId act ";
//    	
//    	if(!(filter.getFromPublicView() !=null&& filter.getFromPublicView()))
//        	oql += " inner join act.ampActivityGroup actGroup ";
//    	    oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
//        if (sectorCondition) {
//            oql += "  inner join act.sectors actsec ";
//            oql += "  inner join actsec.classificationConfig config  ";
//            oql += " inner join actsec.sectorId sec ";
//        }
//        if (sectorCondition) 
//        	oql += " where config.id=:config  ";
//        else
//        	oql += " where 1=1 ";
//        
////        if (!mtef)
////        {
////        	oql += " and  fd.adjustmentType.value =:adjustmentType ";
////            oql += " and (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
////        }
////        else
////        {
////        	oql += " and (fd.projectionDate>=:startDate and fd.projectionDate<=:endDate)  ";
////        }
//        if (regListChildren != null)
//        	oql += " and loc.id in (" + Util.toCSStringForIN(regListChildren) + ")";
//       
//        if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
//            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) 
//            	oql += QueryUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
//            	//oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds, filter.getAgencyType());
//        } else 
//        	oql += QueryUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
//            //oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds, filter.getAgencyType());
//        
//        if (sectorCondition) {
//        	sectorIds = getAllDescendants(filter.getSectorIds(),(ArrayList<AmpSector>) filter.getSectors());
//            oql += " and sec.id in ("+QueryUtil.getInStatement(sectorIds)+") ";
//        }
//
//        List<Long> workSpaceActivityList = filter.buildFilteredActivitiesList();
//        String inactivities= Util.toCSStringForIN(workSpaceActivityList);
//		oql += " and act.ampActivityId in("+ inactivities +")";
//
//
//        if (ActivityVersionUtil.isVersioningEnabled()) {
//			oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";
//		}
//
//        oql += " and (act.draft = FALSE OR act.draft IS NULL) and act.approvalStatus IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") ";
//        oql += " and (act.deleted = FALSE or act.deleted IS NULL)";
//
//        if (filter.isFilterByPeacebuildingMarker() && filter.getSelectedPeacebuildingMarkerId() != null) {
//            oql += " and :PB_MARK_ID in elements(act.categories) ";
//        }
//
//
//        Session session = PersistenceManager.getSession();
//        Query query = session.createQuery(oql);
////        query.setDate("startDate", startDate);
////        query.setDate("endDate", endDate);
//        if (filter.isFilterByPeacebuildingMarker() && filter.getSelectedPeacebuildingMarkerId() != null) {
//        	query.setLong("PB_MARK_ID", filter.getSelectedPeacebuildingMarkerId());
//        }
//
////        if (!mtef)
////        {
////        	query.setString("adjustmentType",adjustmentType.getValueKey());
////        }
//        if (sectorCondition) {
//        	query.setLong("config", filter.getSelSectorConfigId());
//        }
//            
//        List<Long> fundingIds = query.list();
//        return fundingIds;            
//	}
	
	
//	/**
//	 * fetches all fundings of type (mtef or non-mtef) which match a set of filters
//	 * the format of the output Object[] instances is documented in {@link #generateFundingSummaries(List, String, HardCodedCategoryValue, String, Collection, int, BigDecimal)}
//	 * @param regListChildren: Collection<AmpCategoryValueLocations.id>
//	 * @param mtef: whether to fetch the MTEF or non-mtef part of the fundings
//	 * @return funding together with metadata
//	 */
//	protected static List<Object[]> getFundingsOfType(List<Long> ampFundingIds, MapFilter filter, HardCodedCategoryValue adjustmentType, Date startDate, Date endDate, boolean mtef) throws DgException
//	{
//        String oql = "";
//
//       
//        oql = "select fd, loc.id, loc.name ";
//        //if (filter.getSelProgramIds()!=null && filter.getSelProgramIds().length>0) 
//        //	oql += ", actProg.programPercentage ";
//        //if (locationCondition)
//        	oql += ", actloc.locationPercentage ";
//        if (filter.hasSectorCondition())        	
//        	oql += ", actsec.sectorPercentage ";
//        
//		oql+= " from ";
//		
//		if (mtef)
//		{
//			oql += " " + AmpFundingMTEFProjection.class.getName() + " as fd inner join fd.ampFunding f";
//		}
//		else
//		{
//			oql += " " + AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f";
//		}
//		
//        oql += " inner join f.ampActivityId act ";
//    	
//    	if(!(filter.getFromPublicView() !=null&& filter.getFromPublicView()))
//        	oql += " inner join act.ampActivityGroup actGroup ";
//    	oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
//       	oql += " where f.ampFundingId IN (" + Util.toCSStringForIN(ampFundingIds) + ") ";
//        
//        if (!mtef)
//        {
//        	oql += " and  fd.adjustmentType.value =:adjustmentType ";
//            oql += " and (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
//        }
//        else
//        {
//        	oql += " and (fd.projectionDate>=:startDate and fd.projectionDate<=:endDate)  ";
//        }       
//
//
//        Session session = PersistenceManager.getSession();
//        List<Object[]> fundingDets = null;
//        Query query = session.createQuery(oql);
//        query.setDate("startDate", startDate);
//        query.setDate("endDate", endDate);
//
//        if (!mtef)
//        {
//        	query.setString("adjustmentType",adjustmentType.getValueKey());
//        }
//            
//        fundingDets = query.list();
//        return fundingDets;            
//	}
	
	/**
	 * 
	 * @param regListChildren - Collection<AmpCategoryValueLocations.id>
	 * @param impLevel
	 * @param currCode
	 * @param startDate
	 * @param endDate
	 * @param adjustmentType
	 * @param decimalsToShow
	 * @param divideByDenominator
	 * @param filter
	 * @param request
	 * @return
	 * @throws DgException
	 */
	public static ArrayList<SimpleLocation> getFundingByRegionList(Collection<Long> regListChildren, String impLevel, String currCode,  Date startDate,
            Date endDate, HardCodedCategoryValue adjustmentType, int decimalsToShow, BigDecimal divideByDenominator, MapFilter filter) throws DgException {
        
		Long aaa = System.currentTimeMillis();
//		List<Long> ampFundingIds = getFundingIdsForType(filter, regListChildren);
//		List<Object[]> data = getFundingsOfType(ampFundingIds, filter, adjustmentType, startDate, endDate, false);
//		List<Object[]> data2 = getFundingsOfType(ampFundingIds, filter, adjustmentType, startDate, endDate, true);
        
        List<Object[]> mtefFunding = getFundingsOfType(filter, regListChildren, adjustmentType, startDate, endDate, true);
        List<Object[]> nonMtefFunding = getFundingsOfType(filter, regListChildren, adjustmentType, startDate, endDate, false);
        
        ArrayList<Object[]> allFunding = new ArrayList<Object[]>();
        allFunding.addAll(mtefFunding);
        allFunding.addAll(nonMtefFunding);

		aaa = System.currentTimeMillis() - aaa;
		//System.out.println("getting fundingIds took " + aaa / 1000.0 + " seconds");

        return generateFundingSummaries(allFunding, currCode, adjustmentType, impLevel, decimalsToShow, divideByDenominator);            
    }
	
	
	public static ArrayList<Long> getInActivitiesLong(String query)
			throws Exception {
		Session session = PersistenceManager.getRequestDBSession();
		ArrayList<Long> result = (ArrayList<Long>) session.createSQLQuery(query).list();
		return result;
	}
	
	public static AmpCategoryValueLocations getTopLevelLocation(AmpCategoryValueLocations location, String level) {
		if (level.equals("Region"))
			if (location.getParentLocation() != null && !location.getParentLocation().getParentCategoryValue().getValue().equals("Country")) {
				location = getTopLevelLocation(location.getParentLocation(), level);
			}
		if (level.equals("Zone"))
			if (location.getParentLocation() != null && !location.getParentLocation().getParentCategoryValue().getValue().equals("Region")) {
				location = getTopLevelLocation(location.getParentLocation(), level);
			}
		return location;
	}
	
	public static List<AmpOrganisation> getDonorOrganisationByGroupId(
			Long orgGroupId, boolean publicView) {
		Session session = null;
		Query q = null;
		List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
		StringBuilder queryString = new StringBuilder(
				"select distinct org from "
						+ AmpOrgRole.class.getName()
						+ " orgRole inner join orgRole.role role inner join orgRole.organisation org ");
		if (publicView) {
			queryString
					.append(" inner join orgRole.activity act  inner join act.team tm ");
		}
		queryString.append(" where  role.roleCode='DN' ");
		if (orgGroupId != null && orgGroupId != -1) {
			queryString.append(" and org.orgGrpId=:orgGroupId ");
		}
		if (publicView) {
			queryString.append(String.format(" and (act.draft=false OR act.draft is null) and act.approvalStatus IN ('%s', '%s') and tm.parentTeamId is not null ", Constants.STARTED_APPROVED_STATUS, Constants.APPROVED_STATUS));
		}

		queryString.append("order by org.name asc");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			if (orgGroupId != null && orgGroupId != -1) {
				q.setLong("orgGroupId", orgGroupId);
			}
			organizations = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Amp organization names from database ",
					ex);
		}
		return organizations;
	}

	public static List<AmpOrganisation> getDonorOrganisationByType(
			Long orgTypeId, boolean publicView) {
		Session session = null;
		Query q = null;
		List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
		StringBuilder queryString = new StringBuilder(
				"select distinct org from "
						+ AmpOrgRole.class.getName()
						+ " orgRole inner join orgRole.role role inner join orgRole.organisation org ");
		if (publicView) {
			queryString
					.append(" inner join orgRole.activity act  inner join act.team tm ");
		}
		queryString.append(" where  role.roleCode='DN' ");
		if (orgTypeId != null && orgTypeId != -1) {
			queryString.append(" and org.orgGrpId.orgType=:orgtypeId ");
		}
		if (publicView) {
			queryString.append(String.format(" and (act.draft=false OR act.draft is null) and act.approvalStatus in ('%s', '%s') and tm.parentTeamId is not null ", Constants.STARTED_APPROVED_STATUS, Constants.APPROVED_STATUS));
		}

		queryString.append("order by org.name asc");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			if (orgTypeId != null && orgTypeId != -1) {
				q.setLong("orgtypeId", orgTypeId);
			}
			organizations = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Amp organization names from database ",
					ex);
		}
		return organizations;
	}

	public static Collection<AmpStructureType> getAllStructureTypes() {
		Session session = null;
		Query q = null;
		List<AmpStructureType> sts = new ArrayList<AmpStructureType>();
		StringBuilder queryString = new StringBuilder(
				"select structure_type from "
						+ AmpStructureType.class.getName() + " structure_type ");
		queryString.append("order by " + AmpStructureType.hqlStringForName("structure_type") + " asc");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			sts = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Amp Structure Types from database ", ex);
		}
		return sts;
	}

	public static AmpStructureType getStructureTypesByName(String name) {
		Session session = null;
		Query q = null;
		AmpStructureType stt = null;
		ArrayList result;
		StringBuilder queryString = new StringBuilder("select st from "
				+ AmpStructureType.class.getName() + " st ");
		queryString.append("where " + AmpStructureType.hqlStringForName("st") + "=:name");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			q.setString("name", name.trim());
			result = (ArrayList) q.list();
			if (result.size() > 0) {
				stt = (AmpStructureType) result.get(0);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp Structure Type from database "
					+ name + " ", ex);
		}
		return stt;
	}

	public static Set<AmpActivity> getActivityByAmpId(String id) {
		Session session = null;
		Query q = null;
		Set<AmpActivity> activities = null;
		StringBuilder queryString = new StringBuilder("select a from "
				+ AmpActivity.class.getName() + " a ");
		queryString.append("where a.ampId=:id");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			q.setString("id", id);
			activities = new HashSet<AmpActivity>(q.list());

		} catch (Exception ex) {
			logger.error("Unable to get Amp Structure Type from database ", ex);
		}
		return activities;
	}

	public static AmpActivityVersion getActivityById(Long id) {
		Session session = null;
		AmpActivityVersion activity = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			activity = (AmpActivityVersion) session.load(
					AmpActivityVersion.class, id);

		} catch (Exception ex) {
			logger.error("Unable to get Amp Structure Type from database ", ex);
		}
		return activity;
	}

	public static List<AmpMapConfig> getMaps() {
		Session session = null;
		Query q = null;
		List<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
		StringBuilder queryString = new StringBuilder("select a from "
				+ AmpMapConfig.class.getName() + " a");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			maps = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get maps from database ", ex);
		}
		return maps;
	}

	public static AmpStructureType getStructureType(Long structureTypeId) {
		Session session = null;
		AmpStructureType ampStructureType = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			ampStructureType = (AmpStructureType) session.load(
					AmpStructureType.class, structureTypeId);
		} catch (Exception e) {
			logger.error("Unable to get object of class "
					+ AmpStructureType.class.getName() + " width id="
					+ structureTypeId + ". Error was:" + e);
		}
		return ampStructureType;
	}
	public static void deleteStructureType(AmpStructureType structureType) throws AdminException {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
            Query q = sess.createQuery("select st from " + AmpStructure.class.getName() + " st where st.type.typeId=:typeId "  );
            q.setLong("typeId", structureType.getTypeId());
            if (!q.list().isEmpty()){
            	throw new AdminException("The Structure Type is being referenced, it can not be deleted.");	
            }	
			
//beginTransaction();
			sess.delete(structureType);
			//tx.commit();
		}
		catch (Exception e) {
			if (e instanceof AdminException){
				throw (AdminException)e;
			}
			if (e instanceof JDBCException)
				throw (JDBCException) e;
			logger.error("Exception " + e.toString());
			try {
				tx.rollback();
			} catch (HibernateException ex) {
				logger.error("rollback() failed");
				logger.error(ex.toString());
			}
		}

	}

	public static void saveStructure(AmpStructure structure) {
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			session.save(structure);

		} catch (Exception e) {
			logger.error("Unable to save structure type", e);
		}

	}

	public static void saveStructureType(AmpStructureType structureType) {
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			session.save(structureType);

		} catch (Exception e) {
			logger.error("Unable to save structure type", e);
		}

	}

	public static void saveMapConfig(AmpMapConfig map) {
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			session.save(map);
		} catch (Exception e) {
			logger.error("Unable to save structure type", e);
		}

	}

	private static Long[] getAllDescendants(Long[] sectorIds,
			ArrayList<AmpSector> allSectorList) {
		// Go through the list to determine the children
		List<Long> tempSectorIds = new ArrayList<Long>();
		for (AmpSector as : allSectorList) {
			if(sectorIds != null) {
				for (Long i : sectorIds) {
					if (!tempSectorIds.contains(i))
						tempSectorIds.add(i);
					if (as.getParentSectorId() != null
							&& as.getParentSectorId().getAmpSectorId().equals(i)) {
						tempSectorIds.add(as.getAmpSectorId());
					} else if (as.getParentSectorId() != null
							&& as.getParentSectorId().getParentSectorId() != null
							&& as.getParentSectorId().getParentSectorId()
									.getAmpSectorId().equals(i)) {
						tempSectorIds.add(as.getAmpSectorId());
					}
				}
			} else {
				tempSectorIds.add(new Long(-1));
			}
		}
		return (Long[]) tempSectorIds.toArray(new Long[0]);
	}

	public static Long[] getAllDescendantsLocation(Long[] locationIds) 
	{
		Set<Long> ids = DynLocationManagerUtil.getRecursiveChildrenOfCategoryValueLocations(Arrays.asList(locationIds));
		return (Long[]) ids.toArray(new Long[0]);
	}

	public static AmpMapConfig getMapByType(Integer mapType) {
		Session session = null;
		Query q = null;
		AmpMapConfig map = new AmpMapConfig();
		StringBuilder queryString = new StringBuilder("select a from "
				+ AmpMapConfig.class.getName() + " a where mapType = :mapType ");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			q.setInteger("mapType", mapType);
			List<AmpMapConfig> maps = q.list();
			if (maps.size() > 0)
				map = maps.get(0);
			else
				map = null;
		} catch (Exception ex) {
			logger.error("Unable to get individual map from database, of type: " + mapType, ex);
		}
		return map;
	}

	public static List<AmpMapConfig> getMapsBySubType(Integer mapSubType) {
		Session session = null;
		Query q = null;
		
		List<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
		StringBuilder queryString = new StringBuilder("select a from "
				+ AmpMapConfig.class.getName() + " a where mapSubType = :mapSubType ");
		try {
			session = PersistenceManager.getRequestDBSession();
			q = session.createQuery(queryString.toString());
			q.setInteger("mapSubType", mapSubType);
			maps = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get individual map from database, of type: " + mapSubType, ex);
		}
		return maps;		
	}

    public static List<AmpCategoryValue> getPeacebuildingMarkers() {
        List<AmpCategoryValue> retVal = null;
        StringBuilder qs = new StringBuilder("from ").
                append(AmpCategoryValue.class.getName()).
                append(" cv where cv.ampCategoryClass.keyName='procurement_system'");
        try {
            Session session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery(qs.toString());
            retVal = q.list();

            if (retVal != null && !retVal.isEmpty()) {

                Collections.sort(retVal, new Comparator <AmpCategoryValue> (){
                    public int compare(AmpCategoryValue o1, AmpCategoryValue o2) {
                        int retVal;
                        if (o1.getIndex()<o2.getIndex()) retVal = -1;
                                else if (o1.getIndex()<o2.getIndex()) retVal = 0;
                                else retVal = 1;
                        return retVal;
                    }
                }
                );
            }

        } catch (Exception ex) {
            logger.error("Unable to get peacebuilding markers from database", ex);
        }

        return retVal;
    }
    
   
    
    

}
