package org.digijava.module.esrigis.helpers;

/**
 * @author Diego Dimunzio
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.esrigis.dbentitiy.AmpMapConfig;
import org.digijava.module.visualization.util.DashboardUtil; //TODO: Check this functions and use a common
import org.digijava.module.visualization.util.DbUtil;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DbHelper {
	private static Logger logger = Logger.getLogger(DbHelper.class);

	public static List<AmpActivityVersion> getActivities(MapFilter filter)throws DgException {
		Long[] orgGroupIds = filter.getSelOrgGroupIds();
		List<AmpActivityVersion> activities = null;
		Long[] orgIds = filter.getOrgIds();
		Long[] implOrgIds = filter.getImplOrgIds();
		Long[] implOrgGroupIds = filter.getImplOrgGroupIds();

		Long[] orgtypeids = filter.getOrgtypeIds();
		int transactionType = filter.getTransactionType();
		TeamMember teamMember = filter.getTeamMember();
		// apply calendar filter
		Long fiscalCalendarId = filter.getFiscalCalendarId();
		Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue() - filter.getYearsInRange());
		Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
		Long[] locationIds = filter.getSelLocationIds();
		boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
		Long[] zonesids = filter.getZoneIds();
		boolean zonescondition = zonesids != null && zonesids.length > 1;
		Long[] sectorIds = filter.getSelSectorIds();
		boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
		boolean organizationTypeCondition = filter.getOrganizationsTypeId() != null && !filter.getOrganizationsTypeId().equals(-1l);
		boolean structureTypeCondition = filter.getSelStructureTypes() != null && !QueryUtil.inArray(-1l,filter.getSelStructureTypes() );
		
		/*
		 * We are selecting sectors which are funded In selected year by the
		 * selected organization
		 */
		try {
			String oql = "select distinct act from ";
			oql += AmpFundingDetail.class.getName()
					+ " as fd inner join fd.ampFundingId f ";
			oql += " inner join f.ampActivityId act ";
			oql += " inner join act.ampActivityGroup actGroup ";
			//oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
			if (locationCondition) {
				oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
			}
			
			if (sectorCondition) {
				oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
			}
			//Organization Type
			if(organizationTypeCondition){
				oql += " inner join act.orgrole role  ";
			}
			if(structureTypeCondition){
				oql += " inner join act.structures str  ";
			}
			//Status
		    if (filter.getProjectStatusId()!=null){
		    	oql+=" join  act.categories as categories ";
		    }
			oql += "  where fd.adjustmentType = "+CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL).getId();
			if (filter.getTransactionType() < 2) {
				oql += " and fd.transactionType =:transactionType  ";
			} else {
				oql += " and (fd.transactionType =0 or  fd.transactionType =1) ";
			}
			if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
				if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
					oql += QueryUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
				}
			} else {
				oql += QueryUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
			}
			
			if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
				if (orgtypeids != null && orgtypeids.length > 0 && orgtypeids[0] != -1) {
					oql += QueryUtil.getOrganizationTypeQuery(true, orgIds, orgtypeids);
				}
			}
			else if(orgtypeids!=null){
				oql += QueryUtil.getOrganizationTypeQuery(true, orgIds, orgtypeids);;
			}
			
			oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)";

			if (filter.getFromPublicView() != null && filter.getFromPublicView() == true) {
				oql += QueryUtil.getTeamQueryManagement();
			} else {
				oql += QueryUtil.getTeamQuery(teamMember);
			}
			if (!zonescondition && locationCondition) {
            	locationIds = getAllDescendantsLocation(locationIds, DbUtil.getAmpLocations());
				oql += " and loc.id in (" + QueryUtil.getInStatement(locationIds,0) + ") ";
			}else if (zonescondition){
				zonesids = getAllDescendantsLocation(zonesids, DbUtil.getAmpLocations());
				oql += " and loc.id in (" + QueryUtil.getInStatement(zonesids,1) + ") ";
			}
			
			if (sectorCondition) {
	        	sectorIds = getAllDescendants(sectorIds, DbUtil.getAmpSectors());
				oql += " and sec.id in (" + QueryUtil.getInStatement(sectorIds,0) + ") ";
			}
			
			//Organization Type
			if(organizationTypeCondition) {
				oql += " and role.organisation.orgGrpId.orgType = " + filter.getOrganizationsTypeId();
			}
			
			//Implementing Agency
			if (implOrgIds == null || implOrgIds.length == 0 || implOrgIds[0] == -1) {
				if (implOrgGroupIds != null && implOrgGroupIds.length > 0 && implOrgGroupIds[0] != -1) {
					oql += QueryUtil.getOrganizationQuery(true, implOrgIds, implOrgGroupIds, Constants.IMPLEMENTING_AGENCY);
				}
			} else {
				oql += QueryUtil.getOrganizationQuery(false, implOrgIds, implOrgGroupIds, Constants.IMPLEMENTING_AGENCY);
			}

			//Project Status
			if (filter.getProjectStatusId() != null && !filter.getProjectStatusId().equals(0l)) {
				oql += " and categories.id in ("+filter.getProjectStatusId()+") ";
	        }
			// On/Off budget
			if (filter.getOnBudget() != null && !filter.getProjectStatusId().equals(0l)) {
				oql += " and act.budget is not null";
	        }
			// Type of assistance
			if (filter.getTypeAssistanceId() != null && !filter.getTypeAssistanceId().equals(0l)) {
				oql += " and categories.id in ("+filter.getTypeAssistanceId()+") ";
	        }
			// Financing instrument
			if (filter.getFinancingInstrumentId() != null && !filter.getFinancingInstrumentId().equals(0l)) {
				oql += " and categories.id in ("+filter.getFinancingInstrumentId()+") ";
	        }
			// Structure Types
			if(structureTypeCondition){
				oql += " and str.type.typeId in ("+QueryUtil.getInStatement(filter.getSelStructureTypes(),0)+") ";
			}

			oql += ActivityUtil.getApprovedActivityQueryString("act");
			//Show only activities that are not draft
			oql += ActivityUtil.getNonDraftActivityQueryString("act");
			
			//Additional clause to get the last version
			if (ActivityVersionUtil.isVersioningEnabled()){
				oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";			
			}
			
			if (filter.isModeexport()){
				String inactivities= "";
				ArrayList<BigInteger> filteractivities = getInActivities(filter.getReportfilterquery());
				for (Iterator iterator = filteractivities.iterator(); iterator.hasNext();) {
					BigInteger id = (BigInteger) iterator.next();
					if (inactivities ==""){
						inactivities += id.toString();
					}else{
						inactivities +="," + id.toString();
					}
				}
				oql += " and act.ampActivityId in("+ inactivities +")";
			}

			Session session = PersistenceManager.getRequestDBSession();
			Query query = session.createQuery(oql);
			query.setDate("startDate", startDate);
			query.setDate("endDate", endDate);
			if (filter.getTransactionType() < 2) { // the option comm&disb is
				query.setLong("transactionType", transactionType);
			}

			activities = query.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load activities from db", e);
		}
		return activities;

	}
	
	public static ArrayList<BigInteger> getInActivities(String query) throws Exception{
		Session session = PersistenceManager.getRequestDBSession();
		ArrayList<BigInteger> result = (ArrayList<BigInteger>) session.createSQLQuery(query).list();
		return result;
	}
	
    public static List<AmpCategoryValueLocations> getLocations(MapFilter filter, String implementationLevel) throws DgException {
    	List<AmpCategoryValueLocations> locations = new ArrayList<AmpCategoryValueLocations>();
    	if (filter.getSelLocationIds()!=null && filter.getSelLocationIds().length > 0 && filter.getSelLocationIds()[0] != -1) {
			if (filter.getSelLocationIds().length == 1) {
				AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[0]);
				locations.addAll(loc.getChildLocations());
				return locations;
			} else {
				for (int i = 0; i < filter.getSelLocationIds().length; i++) {
					AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[i]);
					locations.add(loc);
				}
				return locations;
			}
		} else {
			
	        Long[] orgGroupIds = filter.getSelOrgGroupIds();
	        Long[] orgIds = filter.getOrgIds();
	        
	        int transactionType = filter.getTransactionType();
	        TeamMember teamMember = filter.getTeamMember();
	        // apply calendar filter
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
	        Long[] sectorIds = filter.getSelSectorIds();
	        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
	        /*
	         * We are selecting regions which are funded
	         * In selected year by the selected organization
	         *
	         */
	        try {
	            String oql = "select distinct loc  from ";
	            oql += AmpFundingDetail.class.getName()
	                    + " as fd inner join fd.ampFundingId f ";
	            oql += " inner join f.ampActivityId act ";
	            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
	            oql += " inner join loc.parentCategoryValue parcv ";
	            if (sectorCondition) {
	                oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
	            }
	            oql += "  where fd.adjustmentType = 1";
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                oql += " and fd.transactionType =:transactionType  ";
	            } else {
	                oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
	            }
	            if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
	                if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
	                    oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
	                }
	            } else {
	                oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
	            }
	            oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
	            
	            if(filter.getFromPublicView() != null && filter.getFromPublicView() == true){
	                oql += DashboardUtil.getTeamQueryManagement();
	            }
	            else
	            {
	                oql += DashboardUtil.getTeamQuery(teamMember);
	            }
	            if (sectorCondition) {
	                oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
	            }
	
	            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
					oql += ActivityUtil.getApprovedActivityQueryString("act");
				}
	            oql += "  and parcv.value = :implementationLevel";
	            
	            oql+=" order by loc.parentCategoryValue";
	            Session session = PersistenceManager.getRequestDBSession();
	            Query query = session.createQuery(oql);
	            query.setDate("startDate", startDate);
	            query.setDate("endDate", endDate);
	            query.setDate("endDate", endDate);
	            query.setString("implementationLevel", implementationLevel);
	            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
	            //    query.setLong("orgGroupId", orgGroupId);
	            //}
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                query.setLong("transactionType", transactionType);
	            }
	            locations = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load regions from db", e);
	        }
	        return locations;
		}
     }

    public static List<AmpCategoryValueLocations> getZones(MapFilter filter) throws DgException {
    	List<AmpCategoryValueLocations> locations = new ArrayList<AmpCategoryValueLocations>();
    	if (filter.getSelLocationIds()!=null && filter.getSelLocationIds().length > 0 && filter.getSelLocationIds()[0] != -1) {
			if (filter.getSelLocationIds().length == 1) {
				AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[0]);
				locations.addAll(loc.getChildLocations());
				return locations;
			} else {
				for (int i = 0; i < filter.getSelLocationIds().length; i++) {
					AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[i]);
					locations.add(loc);
				}
				return locations;
			}
		} else {
			
	        Long[] orgGroupIds = filter.getSelOrgGroupIds();
	        Long[] orgIds = filter.getOrgIds();
	        
	        int transactionType = filter.getTransactionType();
	        TeamMember teamMember = filter.getTeamMember();
	        // apply calendar filter
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
	        Long[] sectorIds = filter.getSelSectorIds();
	        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
	        /*
	         * We are selecting regions which are funded
	         * In selected year by the selected organization
	         *
	         */
	        try {
	            String oql = "select distinct loc  from ";
	            oql += AmpFundingDetail.class.getName()
	                    + " as fd inner join fd.ampFundingId f ";
	            oql += "   inner join f.ampActivityId act ";
	            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
	            oql += " inner join loc.parentCategoryValue parcv ";
	            if (sectorCondition) {
	                oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
	            }
	            oql += "  where fd.adjustmentType = 1";
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                oql += " and fd.transactionType =:transactionType  ";
	            } else {
	                oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
	            }
	            if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
	                if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
	                    oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
	                }
	            } else {
	                oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
	            }
	            oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
	            
	            if(filter.getFromPublicView() != null && filter.getFromPublicView() == true){
	                oql += DashboardUtil.getTeamQueryManagement();
	            }
	            else
	            {
	                oql += DashboardUtil.getTeamQuery(teamMember);
	            }
	            if (sectorCondition) {
	                oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
	            }
	
	            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
					oql += ActivityUtil.getApprovedActivityQueryString("act");
				}
	            oql += "  and parcv.value = 'Zone'";// get only regions
	            
	            oql+=" order by loc.parentCategoryValue";
	            Session session = PersistenceManager.getRequestDBSession();
	            Query query = session.createQuery(oql);
	            query.setDate("startDate", startDate);
	            query.setDate("endDate", endDate);
	            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
	            //    query.setLong("orgGroupId", orgGroupId);
	            //}
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                query.setLong("transactionType", transactionType);
	            }
	            locations = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load zones from db", e);
	        }
	        return locations;
		}
     }

    @SuppressWarnings("unchecked")
    //TODO: Remove this and make a common function for Visualization/GIS
    public static DecimalWraper getFunding(MapFilter filter, Date startDate,
            Date endDate, Long assistanceTypeId,
            Long financingInstrumentId,
            int transactionType,int adjustmentType) throws DgException {
        DecimalWraper total = null;
        String oql = "";
        String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} 
        
        Long[] orgIds = filter.getOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        
        TeamMember tm = filter.getTeamMember();
        Long[] locationIds = filter.getSelLocationIds();
        Long[] sectorIds = filter.getSelSectorIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);

        if (locationCondition && sectorCondition) {
        	oql = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actloc.locationPercentage,actsec.sectorPercentage,fd.fixedExchangeRate) ";
        } else if (locationCondition)  {
        	oql = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actloc.locationPercentage,fd.fixedExchangeRate) ";
        } else if (sectorCondition)  {
        	oql = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actsec.sectorPercentage,fd.fixedExchangeRate) ";
        } else {
            oql = "select fd ";
        }
        oql += " from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        if (locationCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }

        if (sectorCondition) {
            oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
        }

        oql += " where  fd.transactionType =:transactionType  and  fd.adjustmentType =:adjustmentType ";
        if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
                oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
            }
        } else {
            oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
        }
        if (locationCondition) {
            oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
        }

        if (sectorCondition) {
            oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
        }

        if (filter.getActivityId()!=null) {
            oql += " and act.ampActivityId =:activityId ";
        }

        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
        if (assistanceTypeId != null) {
            oql += "  and f.typeOfAssistance=:assistanceTypeId ";
        }
        if (financingInstrumentId != null) {
            oql += "   and f.financingInstrument=:financingInstrumentId  ";
        }

        if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
			oql += ActivityUtil.getApprovedActivityQueryString("act");
		}
        if(filter.getFromPublicView() != filter.getFromPublicView()){
            oql += DashboardUtil.getTeamQueryManagement();
        }
        else
        {
            oql += DashboardUtil.getTeamQuery(tm);
        }

        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            //if ((orgIds == null || orgIds.length == 0 || orgIds[0] == -1) && orgGroupId != -1) {
            //    query.setLong("orgGroupId", orgGroupId);
            //}
            if (assistanceTypeId != null) {
                query.setLong("assistanceTypeId", assistanceTypeId);
            }
            if (financingInstrumentId != null) {
                query.setLong("financingInstrumentId", financingInstrumentId);
            }
            query.setLong("transactionType", transactionType);
            query.setLong("adjustmentType",adjustmentType);
            
            if (filter.getActivityId()!=null) {
                query.setLong("activityId", filter.getActivityId());
            }
            fundingDets = query.list();
            /*the objects returned by query  and   selected currency
            are passed doCalculations  method*/
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(fundingDets, currCode);
            /*Depending on what is selected in the filter
            we should return either actual commitments
            or actual Disbursement or  */
            switch (transactionType) {
                case Constants.EXPENDITURE:
                    if (Constants.PLANNED == adjustmentType) {
                        total = cal.getTotPlannedExp();
                    } else {
                        total = cal.getTotActualExp();
                    }
                    break;
                case Constants.DISBURSEMENT:
                    if (Constants.ACTUAL == adjustmentType) {
                        total = cal.getTotActualDisb();
                    } else {
                        total = cal.getTotPlanDisb();
                    }
                    break;
                default:
                    if (Constants.ACTUAL == adjustmentType) {
                        total = cal.getTotActualComm();
                    } else {
                        total = cal.getTotPlannedComm();
                    }
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }


        return total;
	}
    
    public static List<AmpOrganisation> getDonorOrganisationByGroupId(Long orgGroupId, boolean publicView) {
        Session session = null;
        Query q = null;
        List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
        StringBuilder queryString = new StringBuilder("select distinct org from " + AmpOrgRole.class.getName() + " orgRole inner join orgRole.role role inner join orgRole.organisation org ");
        if (publicView) {
            queryString.append(" inner join orgRole.activity act  inner join act.team tm ");
        }
        queryString.append(" where  role.roleCode='DN' ");
         if (orgGroupId != null&&orgGroupId !=-1) {
            queryString.append(" and org.orgGrpId=:orgGroupId ");
        }
        if (publicView) {
            queryString.append(" and act.draft=false and act.approvalStatus ='approved' and tm.parentTeamId is not null ");
        }

        queryString.append("order by org.name asc");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            if (orgGroupId != null&&orgGroupId !=-1) {
                q.setLong("orgGroupId", orgGroupId);
            }
            organizations = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get Amp organization names from database ", ex);
        }
        return organizations;
    }
    
    public static List<AmpOrganisation> getDonorOrganisationByType(Long orgTypeId, boolean publicView) {
        Session session = null;
        Query q = null;
        List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
        StringBuilder queryString = new StringBuilder("select distinct org from " + AmpOrgRole.class.getName() + " orgRole inner join orgRole.role role inner join orgRole.organisation org ");
        if (publicView) {
            queryString.append(" inner join orgRole.activity act  inner join act.team tm ");
        }
        queryString.append(" where  role.roleCode='DN' ");
         if (orgTypeId != null && orgTypeId !=-1) {
            queryString.append(" and org.orgGrpId.orgType=:orgtypeId ");
        }
        if (publicView) {
            queryString.append(" and act.draft=false and act.approvalStatus ='approved' and tm.parentTeamId is not null ");
        }

        queryString.append("order by org.name asc");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            if (orgTypeId != null && orgTypeId !=-1) {
                q.setLong("orgtypeId", orgTypeId);
            }
            organizations = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get Amp organization names from database ", ex);
        }
        return organizations;
    }

	public static Collection<AmpStructureType> getAllStructureTypes() {
        Session session = null;
        Query q = null;
        List<AmpStructureType> sts = new ArrayList<AmpStructureType>();
        StringBuilder queryString = new StringBuilder("select structure_type from " + AmpStructureType.class.getName() + " structure_type ");
        queryString.append("order by structure_type.name asc");
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
        StringBuilder queryString = new StringBuilder("select st from " + AmpStructureType.class.getName() + " st ");
        queryString.append("where st.name=:name");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            q.setString("name", name);
            stt = (AmpStructureType) q.list().get(0);
        } catch (Exception ex) {
            logger.error("Unable to get Amp Structure Type from database ", ex);
        }
        return stt;
	}
	
	public static Set<AmpActivity> getActivityByAmpId(String id) {
        Session session = null;
        Query q = null;
        Set<AmpActivity> activities = null;
        StringBuilder queryString = new StringBuilder("select a from " + AmpActivity.class.getName() + " a ");
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
	
	
	public static List<AmpMapConfig> getMaps() {
		Session session = null;
        Query q = null;
        List<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
        StringBuilder queryString = new StringBuilder("select a from " + AmpMapConfig.class.getName()+ " a");
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
					+ AmpStructureType.class.getName() + " width id=" + structureTypeId
					+ ". Error was:" + e);
		}
		return ampStructureType;
	}
	public static void deleteStructureType(AmpStructureType structureType) {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
//beginTransaction();
			sess.delete(structureType);
			//tx.commit();
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
	
    private static Long[] getAllDescendants(Long[] sectorIds,
			ArrayList<AmpSector> allSectorList) {
    	//Go through the list to determine the children
    	List<Long> tempSectorIds = new ArrayList<Long>();
		for(AmpSector as : allSectorList){
			for(Long i : sectorIds){
		    	if(!tempSectorIds.contains(i)) tempSectorIds.add(i);
    			if(as.getParentSectorId() != null && as.getParentSectorId().getAmpSectorId().equals(i)){
    	    		tempSectorIds.add(as.getAmpSectorId());
    			} else if(as.getParentSectorId() != null && as.getParentSectorId().getParentSectorId() != null && as.getParentSectorId().getParentSectorId().getAmpSectorId().equals(i)){
    	    		tempSectorIds.add(as.getAmpSectorId());
    			}
    		}
    	}
		return (Long[]) tempSectorIds.toArray(new Long[0]);
	}

	private static Long[] getAllDescendantsLocation(Long[] locationIds,
			ArrayList<AmpCategoryValueLocations> allLocationsList) {
    	List<Long> tempLocationsIds = new ArrayList<Long>();
		for(AmpCategoryValueLocations as : allLocationsList){
			for(Long i : locationIds){
		    	if(!tempLocationsIds.contains(i)) tempLocationsIds.add(i);
    			if(as.getParentLocation() != null && as.getParentLocation().getId().equals(i)){
    				tempLocationsIds.add(as.getId());
    			} else if(as.getParentLocation() != null && as.getParentLocation().getParentLocation() != null && as.getParentLocation().getParentLocation().getId().equals(i)){
    				tempLocationsIds.add(as.getId());
    			} else if(as.getParentLocation() != null && as.getParentLocation().getParentLocation() != null && as.getParentLocation().getParentLocation().getParentLocation() != null
    					&& as.getParentLocation().getParentLocation().getParentLocation().getId().equals(i)){
    				tempLocationsIds.add(as.getId());
    			}
    		}
    	}
		return (Long[]) tempLocationsIds.toArray(new Long[0]);
	}

}
