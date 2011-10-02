package org.digijava.module.visualization.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DbUtil {
	private static Logger logger = Logger.getLogger(DbUtil.class);
	
	public static List<AmpOrganisation> getDonorOrganisationByGroupId(
			Long orgGroupId, boolean publicView) {
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
	
	public static Collection<AmpOrganisation> getAllOrganizations() {
		Session session = null;
		Collection<AmpOrganisation> col = new ArrayList<AmpOrganisation>();

		try {
			session = PersistenceManager.getSession();
			
			String queryString = " SELECT aorg FROM " + AmpOrganisation.class.getName() + " aorg ";
			
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get Organizations, " + ex);
		} 
		return col;
	}
	
	public static Collection<AmpSector> getAllSectors(){
		Session session = null;
        Query qry = null;
        Collection<AmpSector> col = new ArrayList<AmpSector>();
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "SELECT DISTINCT s.* FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
					+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id AND s.parent_sector_id is null "
					+ "AND c.name='Primary'";
             qry = session.createSQLQuery(queryString).addEntity(AmpSector.class);
             col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting sectors : " + ex);
		} 
        return col;
	}
	
	public static List<AmpSector> getParentSectorsFromConfig(Long configId) throws DgException{
		  	Session session = null;
		  	List<AmpSector> sectors =null;
	        StringBuilder queryString = new StringBuilder();
	        Query qry = null;
	        try {
	            session = PersistenceManager.getRequestDBSession();
	            queryString.append("select distinct sec from ");
	            queryString.append(AmpActivitySector.class.getName());
	            queryString.append(" actSec inner join actSec.classificationConfig cls inner join actSec.sectorId sec where  cls.id=:configId and sec.ampSecSchemeId=cls.classification and sec.parentSectorId is null order by sec.name");
				qry = session.createQuery(queryString.toString());
				qry.setLong("configId", configId);
	            sectors=qry.list();
	        } catch (Exception ex) {
	            logger.error("Unable to get config from database ",ex);
	            throw new DgException(ex);

	        }
	        return sectors;

	}
	
	public static List<AmpSector> getSubSectors(Long id){
		Session session = null;
        Query qry = null;
        List<AmpSector> col = new ArrayList<AmpSector>();
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "SELECT s.* FROM amp_sector s "
					+ "WHERE s.parent_sector_id =:id " ;
             qry = session.createSQLQuery(queryString).addEntity(AmpSector.class);
             qry.setParameter("id", id, Hibernate.LONG);
             col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting sub-sectors : " + ex);
		} 
        return col;
	}
	
	/**
     * Returns pledge amount in selected currency
     * for selected organization and year
     * @param orgID
     * @param year
     * @param currCode
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    @SuppressWarnings("unchecked")
    public static DecimalWraper getPledgesFunding(Long[] orgIds, Long[] orgGroupIds,
            Date startDate, Date endDate,
            String currCode) throws DgException {
    	DecimalWraper totalPlannedPldges = new DecimalWraper();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Integer startYear = Integer.valueOf(sdf.format(startDate));
        Integer endYear = Integer.valueOf(sdf.format(endDate));
        String years = "";
        for (int i = startYear; i <= endYear; i++) {
			if(!years.equals(""))
				years = years + ", ";
			years = years + "'" + i + "'";
		}
        
        String oql = "select fd ";
        oql += " from ";
        oql += FundingPledgesDetails.class.getName()
                + " fd inner join fd.pledgeid plg ";
        oql += " inner join  plg.organization org  ";
        oql += " where  fd.fundingYear in (" + years + ")";
        if (orgIds == null || orgIds.length==0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
                oql += " and  org.orgGrpId.ampOrgGrpId in (" + DashboardUtil.getInStatement(orgGroupIds) + ") ";
            }
        } else {
            oql += " and org.ampOrgId in (" + DashboardUtil.getInStatement(orgIds) + ") ";
        }
        Session session = PersistenceManager.getRequestDBSession();
        List<FundingPledgesDetails> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            //query.setDate("startDate", startDate);
            //query.setDate("endDate", endDate);
            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
            //    query.setLong("orgGroupId", orgGroupId);
            //}
            fundingDets = query.list();
            Iterator<FundingPledgesDetails> fundDetIter = fundingDets.iterator();
            while (fundDetIter.hasNext()) {
                FundingPledgesDetails pledge = fundDetIter.next();
                //converting amounts
                java.sql.Date dt = new java.sql.Date(pledge.getFunding_date().getTime());
                double frmExRt = Util.getExchange(pledge.getCurrency().getCurrencyCode(), dt);
                double toExRt = Util.getExchange(currCode, dt);
                DecimalWraper amt = CurrencyWorker.convertWrapper(pledge.getAmount(), frmExRt, toExRt, dt);
                totalPlannedPldges.setValue(totalPlannedPldges.getValue().add(amt.getValue()));
            }



        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return totalPlannedPldges;
    }
    @SuppressWarnings("unchecked")
    public static List<AmpFundingDetail> getUnallocatedFunding(DashboardFilter filter)
            throws DgException {
        Long[] orgIds = filter.getOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        int transactionType = filter.getTransactionType();
        TeamMember tm = filter.getTeamMember();
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
        Session session = PersistenceManager.getRequestDBSession();
        String oql = "select fd ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        oql += " left join act.locations actloc  ";
        oql += "   inner join act.sectors actSec ";
        oql += "   inner join actSec.classificationConfig config ";

        oql += "  where fd.adjustmentType = 1 and config.id=:config";
        oql += " inner join act.ampActivityGroup actGroup ";
        //if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            oql += " and fd.transactionType =:transactionType  ";
        //} else {
        //    oql += " and (fd.transactionType=1 or fd.transactionType=0) "; // the option comm&disb is selected
        //}
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<:endDate)   ";
        if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
                oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
            }
        } else {
            oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
        }
     
        if(filter.getFromPublicView() != null && filter.getFromPublicView() == true){
            oql += DashboardUtil.getTeamQueryManagement();
        }
        else
        {
            oql += DashboardUtil.getTeamQuery(tm);
        }
        
        oql += " and actloc is NULL ";
        
        oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

        Query query = session.createQuery(oql);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        query.setLong("config", filter.getSelSectorConfigId());
        //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
        //    query.setLong("orgGroupId", orgGroupId);
        //}
        if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            query.setLong("transactionType", transactionType);
        }
        List<AmpFundingDetail> fundingDets = query.list();
        return fundingDets;

    }
	public static Set<AmpCategoryValueLocations> getSubRegions(Long id){
        List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

        if (id != null && id != -1) {
            AmpCategoryValueLocations region;
			try {
				region = LocationUtil.getAmpCategoryValueLocationById(id);
	            if (region.getChildLocations() != null) {
	                return region.getChildLocations();
	            }
			} catch (DgException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return null;
	}    
    
    public static List<AmpCategoryValueLocations> getRegions(DashboardFilter filter) throws DgException {
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
	        
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
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
	            oql += "   inner join act.sectors actSec ";
	            oql += "   inner join actSec.classificationConfig config ";
	            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
	            oql += " inner join loc.parentCategoryValue parcv ";
	            oql += " inner join act.ampActivityGroup actGroup ";
	            if (sectorCondition) {
	                oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
	            }
	            oql += "  where fd.adjustmentType = 1 and config.id=:config";
	            //if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                oql += " and fd.transactionType =:transactionType  ";
	            //} else {
	            //    oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
	            //}
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
	
	            oql += "  and parcv.value = 'Region'";// get only regions
	            oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

	            oql+=" order by loc.parentCategoryValue";
	            
	            Session session = PersistenceManager.getRequestDBSession();
	            Query query = session.createQuery(oql);
	            query.setDate("startDate", startDate);
	            query.setDate("endDate", endDate);
	            query.setLong("config", filter.getSelSectorConfigId());
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
    
    public static List<AmpSector> getSectors(DashboardFilter filter) throws DgException {
    	List<AmpSector> sectors = new ArrayList<AmpSector>();
        if (filter.getSelSectorIds()!=null && filter.getSelSectorIds().length > 0 && filter.getSelSectorIds()[0] != -1) {
			if (filter.getSelSectorIds().length == 1) {
				List<AmpSector> sector = getSubSectors(filter.getSelSectorIds()[0]);
				sectors.addAll(sector);
				return sectors;
			} else {
				for (int i = 0; i < filter.getSelSectorIds().length; i++) {
					AmpSector sector = SectorUtil.getAmpSector(filter.getSelSectorIds()[i]);
					sectors.add(sector);
				}
				return sectors;
			}
		} else {
			//Get the selected Organization Groups and Organizations
			Long[] orgGroupIds = filter.getSelOrgGroupIds();
	        Long[] orgIds = filter.getOrgIds();
	        
	        //Get the funding transaction type
	        int transactionType = filter.getTransactionType();

	        //Get the user logged in to filter later
	        TeamMember teamMember = filter.getTeamMember();

	        //Get the Fiscal Calendar to determine Start/End Date
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
	        Long[] locationIds = filter.getSelLocationIds();
	        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
	        /*
	         * We are selecting sectors which are funded
	         * In selected year by the selected organization
	         *
	         */
	        try {
	            String oql = "select distinct sec  from ";
	            oql += AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f ";
	            oql += " inner join f.ampActivityId act ";
	            oql += " inner join act.sectors actSec ";
	            oql += " inner join actSec.sectorId sec ";
	            oql += " inner join act.ampActivityGroup actGroup ";
	            if (locationCondition) {
	                oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
	            }
	            oql += "  where fd.adjustmentType = 1";
                oql += " and fd.transactionType =:transactionType  ";
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
	            if (locationCondition) {
	                oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
	            }
	
	            oql += "  and sec.ampSecSchemeId in (select clscfg.classification.id from " 
	            	+ AmpClassificationConfiguration.class.getName() + " clscfg where clscfg.id =:configId) "; 
	            
	            oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

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
	            query.setLong("configId", filter.getSelSectorConfigId());
	
	            sectors = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load sectors from db", e);
	        }
	        return sectors;
    	}
     }
    
    public static List getActivities(DashboardFilter filter) throws DgException {
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        List activities = null;
        Long[] orgIds= filter.getOrgIds();
        
        int transactionType = filter.getTransactionType();
        TeamMember teamMember = filter.getTeamMember();
        // apply calendar filter
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        
        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
        Long[] locationIds = filter.getSelLocationIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        Long[] sectorIds = filter.getSelSectorIds();
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);

        //Sectors should include all the subsectors
        if(sectorCondition){
        	sectorIds = getAllDescendants(sectorIds, filter.getAllSectorList());
        }
        /*
         * We are selecting sectors which are funded
         * In selected year by the selected organization
         *
         */
        try {
            String oql = "select act.ampActivityId, act.ampId, act.name from ";
            oql += AmpFundingDetail.class.getName()
                    + " as fd inner join fd.ampFundingId f ";
            oql += " inner join f.ampActivityId act ";
            oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
            oql += " inner join actsec.classificationConfig config ";
            oql += " inner join act.ampActivityGroup actGroup ";
            if (locationCondition) {
                oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
            }
            if (sectorCondition) {
                oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
            }
            oql += "  where fd.adjustmentType = 1 and config.id=:config";
            //if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
                oql += " and fd.transactionType =:transactionType  ";
            //} else {
            //    oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
            //}
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
            if (locationCondition) {
                oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
            }
            if (sectorCondition) {
                oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
            }

            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
				oql += ActivityUtil.getApprovedActivityQueryString("act");
			}
            
            oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

            Session session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            query.setLong("config", filter.getSelSectorConfigId());
            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
            //    query.setLong("orgGroupId", orgGroupId);
            //}
            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
                query.setLong("transactionType", transactionType);
            }
//            query.setCacheable(true);
            activities = query.list();
        }
        catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load activities from db", e);
        }
        return activities;

     }
    
    public static Long[] getAllDescendants(Long[] sectorIds) {
        // Make it recursive carefully
    	List<Long> tempSectorIds = new ArrayList<Long>();
        List<AmpSector> sectors = new ArrayList<AmpSector>();
    	for(Long sectorId : sectorIds){
            sectors.add(getAmpSector(sectorId));
            List<AmpSector> childrenSectors = getAmpSubSectors(sectorId);
            if(childrenSectors!=null){
                sectors.addAll(childrenSectors);
                 for (AmpSector sector : childrenSectors) {
                    List<AmpSector> grandChildren = getAmpSubSectors(sector.getAmpSectorId());
                    if( grandChildren!=null){
                         sectors.addAll(grandChildren);
                    }
                }
            }
    	}
        Iterator<AmpSector> it = sectors.iterator();
        while(it.hasNext()){
        	AmpSector currentSector = it.next();
        	tempSectorIds.add(currentSector.getAmpSectorId());
        }
		return (Long[]) tempSectorIds.toArray(new Long[0]);
	}

	public static List<AmpOrganisation> getDonors(DashboardFilter filter) throws DgException {
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        List<AmpOrganisation> donors = new ArrayList<AmpOrganisation>();
        Long[] orgIds= filter.getOrgIds();
        if (orgGroupIds!=null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
			if (orgGroupIds.length == 1) {
				List<AmpOrganisation> donorsByGrp = org.digijava.module.aim.util.DbUtil.getOrganisationByGroupId(orgGroupIds[0]);
				donors.addAll(donorsByGrp);
				return donors;
			} else {
				for (int i = 0; i < filter.getSelLocationIds().length; i++) {
					List<AmpOrganisation> donorsByGrp = org.digijava.module.aim.util.DbUtil.getOrganisationByGroupId(orgGroupIds[i]);
					donors.addAll(donorsByGrp);
				}
				return donors;
			}
		} else {
	        int transactionType = filter.getTransactionType();
	        TeamMember teamMember = filter.getTeamMember();
	        // apply calendar filter
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
	        Long[] locationIds = filter.getSelLocationIds();
	        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
	        Long[] sectorIds = filter.getSelSectorIds();
	        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
	        /*
	         * We are selecting sectors which are funded
	         * In selected year by the selected organization
	         *
	         */
	        try {
	            String oql = "select distinct donor from ";
	            oql += AmpFundingDetail.class.getName()
	                    + " as fd inner join fd.ampFundingId f ";
	            oql += "   inner join f.ampActivityId act ";
	            oql += "   inner join f.ampDonorOrgId donor ";
	            oql += " inner join act.sectors actsec ";
	            oql+=" inner join actsec.classificationConfig config ";
	            oql += " inner join act.ampActivityGroup actGroup ";
	            if (locationCondition) {
	                oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
	            }
	            if (sectorCondition) {
	                oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
	            }
	            oql += "  where fd.adjustmentType = 1 and config.id=:config";
	            //if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                oql += " and fd.transactionType =:transactionType  ";
	            //} else {
	            //    oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
	            //}
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
	            if (locationCondition) {
	                oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
	            }
	            if (sectorCondition) {
	                oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
	            }
	           
	            oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

	            Session session = PersistenceManager.getRequestDBSession();
	            Query query = session.createQuery(oql);
	            query.setDate("startDate", startDate);
	            query.setDate("endDate", endDate);
	            query.setLong("config", filter.getSelSectorConfigId());
	            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
	            //    query.setLong("orgGroupId", orgGroupId);
	            //}
	            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
	                query.setLong("transactionType", transactionType);
	            }
	            
	            donors = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load activities from db", e);
	        }
	        return donors;
		}
     }
    
    /**
     * Returns funding amount
     * @param orgID
     * @param year
     * @param assistanceTypeId
     * @param currCode
     * @param transactionType
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    @SuppressWarnings("unchecked")
    public static DecimalWraper getFunding(DashboardFilter filter, Date startDate,
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
            oql = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,fd.fixedExchangeRate) ";
        }
        oql += " from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        oql += " inner join act.ampActivityGroup actGroup ";
        if (locationCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }

        if (sectorCondition) {
            oql += "  inner join act.sectors actsec ";
            oql += "  inner join actsec.classificationConfig config  ";
            oql += " inner join actsec.sectorId sec ";
        }

        if (sectorCondition) {
        	oql += " where config.id=:config and  fd.transactionType =:transactionType  and  fd.adjustmentType =:adjustmentType ";
        }
        else
        	oql += " where fd.transactionType =:transactionType  and  fd.adjustmentType =:adjustmentType ";
        	

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
        	sectorIds = getAllDescendants(sectorIds, filter.getAllSectorList());
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

        
        if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
            oql += DashboardUtil.getTeamQueryManagement();
        }
        else
        {
            oql += DashboardUtil.getTeamQuery(tm);
        }

        oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (sectorCondition) {
            	query.setLong("config", filter.getSelSectorConfigId());
            }
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

    /**
     * Returns funding amount
     * @param orgID
     * @param year
     * @param assistanceTypeId
     * @param currCode
     * @param transactionType
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    @SuppressWarnings("unchecked")
    public static List<AmpFundingDetail> getFundingDetails(DashboardFilter filter, Date startDate,
            Date endDate, Long assistanceTypeId,
            Long financingInstrumentId) throws DgException {
        
        String oql = "";
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
            oql = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,fd.fixedExchangeRate) ";
        }
        oql += " from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        oql += " inner join act.ampActivityGroup actGroup ";
        if (locationCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }

        if (sectorCondition) {
            oql += "  inner join act.sectors actsec ";
            oql += "  inner join actsec.classificationConfig config  ";
            oql += " inner join actsec.sectorId sec ";
        }

        if (sectorCondition) {
        	oql += " where config.id=:config  ";
        }
        else
        {
        	oql += " where 1=1 ";
        }
        	

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
        	sectorIds = getAllDescendants(sectorIds, filter.getAllSectorList());
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

        
        if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
            oql += DashboardUtil.getTeamQueryManagement();
        }
        else
        {
            oql += DashboardUtil.getTeamQuery(tm);
        }

        oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (sectorCondition) {
            	query.setLong("config", filter.getSelSectorConfigId());
            }
            //if ((orgIds == null || orgIds.length == 0 || orgIds[0] == -1) && orgGroupId != -1) {
            //    query.setLong("orgGroupId", orgGroupId);
            //}
            if (assistanceTypeId != null) {
                query.setLong("assistanceTypeId", assistanceTypeId);
            }
            if (financingInstrumentId != null) {
                query.setLong("financingInstrumentId", financingInstrumentId);
            }
            if (filter.getActivityId()!=null) {
                query.setLong("activityId", filter.getActivityId());
            }
            fundingDets = query.list();

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }


        return fundingDets;
    }

    public static DecimalWraper calculateDetails(DashboardFilter filter, List<AmpFundingDetail> fundingDets,
            int transactionType,int adjustmentType){
        DecimalWraper total = null;
        String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} 
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        cal.doCalculations(fundingDets, currCode, transactionType, adjustmentType);
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
        return total;
    }
	@SuppressWarnings("unchecked")
    public static Map<AmpActivityVersion, BigDecimal> getFundingByActivityList(Collection<Long> actList, String currCode,  Date startDate,
            Date endDate, int transactionType,int adjustmentType, int decimalsToShow) throws DgException {
        
		Map<AmpActivityVersion, BigDecimal> map = new HashMap<AmpActivityVersion, BigDecimal>();
    	
    	DecimalWraper total = null;
        String oql = "";

        oql = "select fd, f.ampActivityId.ampActivityId, f.ampActivityId.name from org.digijava.module.aim.dbentity.AmpFundingDetail as fd inner join fd.ampFundingId f ";
    	oql += "where fd.transactionType =:transactionType  and  fd.adjustmentType =:adjustmentType ";
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
        oql += " and f.ampActivityId in (" + DashboardUtil.getInStatement(actList.toArray()) + ")";

        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            query.setLong("transactionType", transactionType);
            query.setLong("adjustmentType",adjustmentType);
            fundingDets = query.list();
            /*the objects returned by query  and   selected currency
            are passed doCalculations  method*/
            HashMap<Long, ArrayList<AmpFundingDetail>> hm = new HashMap<Long, ArrayList<AmpFundingDetail>>();
            HashMap<Long, String> hmName = new HashMap<Long, String>();
            Iterator it = fundingDets.iterator();
            while(it.hasNext()){
            	Object[] item = (Object[])it.next();
            	
            	AmpFundingDetail currentFd = (AmpFundingDetail) item[0];
            	Long id = (Long) item[1];
            	String name = (String) item[2];
            	if(hm.containsKey(id)){
            		ArrayList<AmpFundingDetail> afda = hm.get(id);
            		afda.add(currentFd);
            	}
            	else
            	{
            		ArrayList<AmpFundingDetail> afda = new ArrayList<AmpFundingDetail>();
            		afda.add(currentFd);
            		hmName.put(id, name);
            		hm.put(id, afda);
            	}
            }

            Iterator<Long> it2 = hm.keySet().iterator();
            while(it2.hasNext()){
            	Long activityId = it2.next();
            	ArrayList<AmpFundingDetail> afda = hm.get(activityId);
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(afda, currCode);
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
                BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
                if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
                    divideByMillionDenominator = new BigDecimal(1000);
                }
                AmpActivityVersion aav = new AmpActivityVersion(activityId, hmName.get(activityId), "");
                map.put(aav, total.getValue().divide(divideByMillionDenominator).setScale(decimalsToShow, RoundingMode.HALF_UP));
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }


        return map;
    }
    public static AmpOrganisation getOrganisation(Long id) {
        Session session = null;
        AmpOrganisation org = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrganisation.class.getName() + " o "
                + "where (o.ampOrgId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                org = (AmpOrganisation) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get organisation from database", ex);
        }
        logger.debug("Getting organisation successfully ");
        return org;
    }
    
    public static Collection<AmpOrganisation> getOrganisationsFromGroup(Long id) {
        Session session = null;
        Collection<AmpOrganisation> orgs = null;
        
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrganisation.class.getName() + " o "
                + "where (o.orgGrpId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            orgs = qry.list();
            
        } catch (Exception ex) {
            logger.error("Unable to get organisations by group from database", ex);
        }
        logger.debug("Getting organisations successfully ");
        return orgs;
    }
    
    public static AmpOrgGroup getOrgGroup(Long id) {
        Session session = null;
        AmpOrgGroup orgGrp = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrgGroup.class.getName() + " o "
                + "where (o.ampOrgGrpId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                orgGrp = (AmpOrgGroup) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get organisation group from database", ex);
        }
        logger.debug("Getting organisation successfully ");
        return orgGrp;
    }
    
    /**
     * Returns Activity List using filter
     */
    @SuppressWarnings("unchecked")
    public static List<AmpActivityVersion> getActivityList(DashboardFilter filter, Date startDate,
            Date endDate, Long assistanceTypeId,
            Long financingInstrumentId,
            int transactionType,int adjustmentType) throws DgException {
        DecimalWraper total = null;
        String oql = "";
        List<AmpActivityVersion> activities = null;
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

        oql = "select distinct act from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        oql += " inner join act.ampActivityGroup actGroup ";
        if (locationCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }
        if (sectorCondition) {
            oql += "  inner join act.sectors actsec ";
            oql += "  inner join actsec.classificationConfig config  ";
            oql += " inner join actsec.sectorId sec ";
        }

        if (sectorCondition) {
        	oql += " where config.id=:config and  fd.transactionType =:transactionType  and  fd.adjustmentType =:adjustmentType ";
        } else {
        	oql += " where fd.transactionType =:transactionType  and  fd.adjustmentType =:adjustmentType ";
        }
        //oql += " where config.id=:config and fd.transactionType =:transactionType  and  fd.adjustmentType =:adjustmentType ";
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
        if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
            oql += DashboardUtil.getTeamQueryManagement();
        }
        else
        {
            oql += DashboardUtil.getTeamQuery(tm);
        }

        oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

        Session session = PersistenceManager.getRequestDBSession();
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (sectorCondition) {
            	query.setLong("config", filter.getSelSectorConfigId());
            }

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
            activities = query.list();
        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }


        return activities;
    }
    public static AmpContact getPrimaryContactForOrganization(Long orgId) throws DgException{
    	AmpContact contact=null;
    	Session session = null;
        StringBuilder queryString = new StringBuilder();
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString.append("select con from ");
            queryString.append(AmpOrganisation.class.getName());
            queryString.append(" org inner join org.organizationContacts orgContact  ");
            queryString.append(" inner join orgContact.contact con ");
            queryString.append(" where org.ampOrgId=:orgId and orgContact.primaryContact=true ");
			qry = session.createQuery(queryString.toString());
			qry.setLong("orgId", orgId);
			contact=(AmpContact)qry.uniqueResult();
        } catch (Exception ex) {
            logger.error("Unable to get contact from database ",ex);
            throw new DgException(ex);

        }
    	return contact;
    }
    public static void saveAdditionalInfo(Long orgId, String orgBackground,String orgDescription) throws DgException{
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
//beginTransaction();
            AmpOrganisation org = (AmpOrganisation) sess.get(AmpOrganisation.class, orgId);
            org.setOrgBackground(orgBackground);
            org.setOrgDescription(orgDescription);
            sess.update(org);
            //tx.commit();
        } catch (Exception e) {
            logger.error("Unable to update", e);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex) {
                    logger.error("rollback() failed", ex);
                }
            }
              throw new DgException(e);
        }

    }
	public static AmpSector getAmpSector(Long id) {

		Session session = null;
		Query qry = null;
		Iterator itr = null;
		AmpSector ampSector = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select s from " + AmpSector.class.getName()
					+ " s where (s.ampSectorId=:ampSectorId)";

			qry = session.createQuery(queryString);
			qry.setParameter("ampSectorId", id, Hibernate.LONG);
			itr = qry.list().iterator();

			if (itr.hasNext()) {
				ampSector = (AmpSector) itr.next();
			}

		} catch (Exception ex) {
			logger.error("Unable to get amp_sector info");
			logger.debug("Exceptiion " + ex);
		}
//session.flush();
		return ampSector;

	}
	public static ArrayList getAmpSubSectors(Long ampSectorId) {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList subsector = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select new AmpSector(ampSectorId, parentSectorId) from "
					+ AmpSector.class.getName()
					+ " Sector where Sector.parentSectorId is not null and Sector.parentSectorId.ampSectorId=:ampSectorId";
			q = session.createQuery(queryString);
			q.setParameter("ampSectorId", ampSectorId, Hibernate.LONG);
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampSector = (AmpSector) iter.next();
				subsector.add(ampSector);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp sub sectors  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return subsector;
	}
	public static ArrayList<AmpSector> getAmpSectors() {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList<AmpSector> sector = new ArrayList<AmpSector>();
		String queryString = null;
		Iterator<AmpSector> iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Sector from "
					+ AmpSector.class.getName()
					+ " Sector where Sector.parentSectorId is not null order by Sector.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampSector = (AmpSector) iter.next();
				sector.add(ampSector);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Sector names  from database "
					+ ex.getMessage());
		}
		return sector;
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

}
