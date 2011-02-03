package org.digijava.module.visualization.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

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
		Collection col = new ArrayList();

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

	public static Collection<AmpActivity> getAllActivities() {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			
			String queryString = " SELECT act FROM " + AmpActivityVersion.class.getName() + " act ";
			
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get Activities, " + ex);
		} 
		return col;
	}

	public static Collection<AmpFundingDetail> getAllFundingsDetails() {
		Session session = null;
		Collection col = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			//String queryString = "select * from AMP_FUNDING_DETAIL ";
			String queryString = " SELECT afd FROM " + AmpFundingDetail.class.getName() + " afd ";
			
			//Query qry = session.createSQLQuery(queryString).addEntity(AmpFundingDetail.class);
			Query qry = session.createQuery(queryString);
			col = qry.list();
			
		} catch (Exception ex) {
			logger.error("Unable to get Fundings Details, " + ex);
		} 
		return col;
	}

	public static Collection<AmpFunding> getAllFundings() {
		Session session = null;
		Collection col = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			
			String queryString = " SELECT af FROM " + AmpFunding.class.getName() + " af ";
			
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get Fundings, " + ex);
		} 
		return col;
	}

	public static Collection<AmpCategoryValueLocations> getAllRegions() {
		Session session = null;
		Collection col = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			
			String queryString = "SELECT * FROM amp_category_value_location acvl " +
						"INNER JOIN amp_category_value acv ON (acvl.parent_category_value = acv.id)" +
						"WHERE acv.category_value = 'Region'";
			Query qry = session.createSQLQuery(queryString).addEntity(AmpCategoryValueLocations.class);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get regions, " + ex);
		} 
		return col;
	}

	public static int getProjectsAmount(){
		Session session = null;
        Query qry = null;
        int count=0;
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "select count(*) from " + AmpActivity.class.getName() + " act ";
             qry = session.createQuery(queryString);
             count= ((Integer) qry.iterate().next()).intValue();
		} catch (Exception ex) {
			logger.error("Exception while getting activities amount:" + ex);
		} 
		return count;
		
	}
	
	public static int getSectorsAmount(){
		Session session = null;
        Query qry = null;
        int count=0;
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "select count(*) from " + AmpSector.class.getName() + " sec where sec.parentSectorId is null";
             qry = session.createQuery(queryString);
             count=((Integer)qry.uniqueResult()).intValue();
		} catch (Exception ex) {
			logger.error("Exception while getting sectors amount:" + ex);
		} 
		return count;
	}
	
	public static Collection<AmpSector> getAllSectors(){
		Session session = null;
        Collection col = new ArrayList();
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "select sec from " + AmpSector.class.getName() + " sec where sec.parentSectorId is null";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting sectors :" + ex);
		} 
		return col;
	}
	
	public static int getRegionsAmount(){
		Session session = null;
        Query qry = null;
        int count=0;
        try {	
        	session = PersistenceManager.getSession();
			String queryString = "SELECT COUNT(*) FROM amp_category_value_location acvl " +
					"INNER JOIN amp_category_value acv ON (acvl.parent_category_value = acv.id)" +
					"WHERE acv.category_value = 'Region'";
             qry = session.createSQLQuery(queryString);
             count=((BigInteger)qry.uniqueResult()).intValue();
             
		} catch (Exception ex) {
			logger.error("Exception while getting regions amount:" + ex);
		} 
		return count;
	}
	
	public static Collection<AmpFundingDetail>  getFundingDetailsByRegion(Long id){
		Session session = null;
		Collection col = new ArrayList();
        try {	
        	session = PersistenceManager.getRequestDBSession();
        	String queryString = "SELECT " +
					"afd.`amp_fund_detail_id`,afd.`fiscal_year`,afd.`fiscal_quarter`,afd.`transaction_type`," +
					"afd.`adjustment_type`,afd.`transaction_date`,afd.`transaction_date2`,afd.`reporting_date`," +
					"(afd.`transaction_amount`*aal.`location_percentage`/100) as transaction_amount," +
					"afd.`language`,afd.`version`,afd.`cal_type`,afd.`org_role_code`,afd.`exp_category`," +
					"afd.`fixed_exchange_rate`,afd.`disb_order_id`,afd.`disbursement_order_rejected`,afd.`reporting_org_id`," +
					"afd.`amp_currency_id`,afd.`amp_funding_id`,afd.`ipa_contract_id`,afd.`fixed_base_currency_id`,afd.`pledge_id` " +
					"FROM `amp_funding_detail` afd " +
					"INNER JOIN `amp_funding` af ON (af.`amp_funding_id`=afd.`amp_funding_id`) " +
					"INNER JOIN `amp_activity` act ON (af.`amp_activity_id`=act.`amp_activity_id`) " +
					"INNER JOIN `amp_activity_location` aal ON (act.`amp_activity_id`=aal.`amp_activity_id`) " +
					"INNER JOIN `amp_location` al ON (al.`amp_location_id`= aal.`amp_location_id`) " +
					"WHERE al.`region_location_id` =:id";
					 
					 
        	Query qry = session.createSQLQuery(queryString).addEntity(AmpFundingDetail.class);
        	qry.setParameter("id", id, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting funding details by regions:" + ex);
			ex.printStackTrace();
		} 
		return col;
	}
	
	public static Collection<AmpFundingDetail>  getFundingDetailsBySector(Long id){
		Session session = null;
		Collection col = new ArrayList();
        try {	
        	session = PersistenceManager.getRequestDBSession();
        	String queryString = "SELECT " +
					"afd.`amp_fund_detail_id`,afd.`fiscal_year`,afd.`fiscal_quarter`,afd.`transaction_type`," +
					"afd.`adjustment_type`,afd.`transaction_date`,afd.`transaction_date2`,afd.`reporting_date`," +
					"(afd.`transaction_amount`*aas.`sector_percentage`/100) as transaction_amount," +
					"afd.`language`,afd.`version`,afd.`cal_type`,afd.`org_role_code`,afd.`exp_category`," +
					"afd.`fixed_exchange_rate`,afd.`disb_order_id`,afd.`disbursement_order_rejected`,afd.`reporting_org_id`," +
					"afd.`amp_currency_id`,afd.`amp_funding_id`,afd.`ipa_contract_id`,afd.`fixed_base_currency_id`,afd.`pledge_id` " +
					"FROM `amp_funding_detail` afd " +
					"INNER JOIN `amp_funding` af ON (af.`amp_funding_id`=afd.`amp_funding_id`) " +
					"INNER JOIN `amp_activity` act ON (af.`amp_activity_id`=act.`amp_activity_id`) " +
					"INNER JOIN `amp_activity_sector` aas ON (act.`amp_activity_id`=aas.`amp_activity_id`) " +
					"INNER JOIN `amp_sector` asec ON (asec.`amp_sector_id`= aas.`amp_sector_id`) " +
					"WHERE (asec.`amp_sector_id` =:id " +
					"OR asec.`parent_sector_id` =:id " +
					"OR asec.`parent_sector_id` IN (SELECT asec1.`amp_sector_id` FROM  `amp_sector` asec1 WHERE asec1.`parent_sector_id` =:id)) ";
					 
        	Query qry = session.createSQLQuery(queryString).addEntity(AmpFundingDetail.class);
        	qry.setParameter("id", id, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting funding details by sector:" + ex);
			ex.printStackTrace();
		} 
		return col;
	}
	
	public static Collection<AmpActivity> getActivitiesUsingFilter(VisualizationForm form) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			
			//String queryString = " SELECT act FROM " + AmpActivityVersion.class.getName() + " act ";
			String queryString = "SELECT distinct * FROM amp_activity WHERE 1=1 ";
			
			if (form.getFilter().getOrganizationsSelected()!=null && form.getFilter().getOrganizationsSelected().size() > 0) {
				queryString += "AND amp_activity_id IN (SELECT v.amp_activity_id FROM v_donors v  " +
						"WHERE v.amp_donor_org_id IN ("	+ Util.toCSString(form.getFilter().getOrganizationsSelected()) + "))";
			} else {
				if (form.getFilter().getOrganizationGroupId()!=null && form.getFilter().getOrganizationGroupId()!=-1) {
					queryString += "AND amp_activity_id IN (SELECT v.amp_activity_id FROM v_donor_groups v  " +
					"WHERE v.amp_org_grp_id IN ("	+ form.getFilter().getOrganizationGroupId() + "))";
				}
			}
			
			if (form.getFilter().getSectorsSelected()!=null && form.getFilter().getSectorsSelected().size() > 0) {
				queryString += "AND amp_activity_id IN (SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
					+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
					+ "AND c.name='Primary' AND (aas.amp_sector_id IN (" + Util.toCSString(form.getFilter().getSectorsSelected()) + ") " 
					+ "OR aas.amp_sector_id IN (SELECT s2.amp_sector_id FROM amp_sector s2 WHERE s2.parent_sector_id IN (" + Util.toCSString(form.getFilter().getSectorsSelected()) + ")) " 
					+ "OR aas.amp_sector_id IN (SELECT s3.amp_sector_id FROM amp_sector s3 WHERE s3.parent_sector_id IN (SELECT s4.amp_sector_id FROM amp_sector s4 WHERE s4.parent_sector_id IN (" + Util.toCSString(form.getFilter().getSectorsSelected()) + ")))))";
			}
			
			if (form.getFilter().getLocationsSelected()!=null && form.getFilter().getLocationsSelected().size() > 0) {
				queryString += "AND amp_activity_id IN (SELECT aal.amp_activity_id FROM amp_activity_location aal, amp_location al " +
				"WHERE ( aal.amp_location_id=al.amp_location_id AND " +
				"al.location_id IN ("+ Util.toCSString(form.getFilter().getLocationsSelected()) + ") ))";
			}
			
			Query qry = session.createSQLQuery(queryString).addEntity(AmpActivity.class);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get Activities with filter, " + ex);
		} 
		return col;
	}
	
	public static Collection<AmpSector> getSectorsUsedInActivities(String actList){
		Session session = null;
        Query qry = null;
        Collection col = new ArrayList();
        if (actList!=null && actList.length()>0) {
			try {	
	        	session = PersistenceManager.getSession();
				String queryString = "SELECT DISTINCT s.* FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
						+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id AND s.parent_sector_id is null "
						+ "AND c.name='Primary' AND aas.amp_activity_id in (" + actList + ")";
	             qry = session.createSQLQuery(queryString).addEntity(AmpSector.class);
	             col = qry.list();
			} catch (Exception ex) {
				logger.error("Exception while getting sectors : " + ex);
			} 
        }
		return col;
	}
	
	public static Collection<AmpSector> getSectors(){
		Session session = null;
        Query qry = null;
        Collection col = new ArrayList();
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
	
	public static List<AmpSector> getSubSectors(Long id){
		Session session = null;
        Query qry = null;
        List col = new ArrayList();
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
	
	public static Collection<AmpLocation> getRegionsUsedInActivities(String actList){
		Session session = null;
        Query qry = null;
        Collection col = new ArrayList();
        if (actList!=null && actList.length()>0) {
	        try {	
	        	session = PersistenceManager.getSession();
	        	String queryString = "SELECT DISTINCT loc.* FROM amp_activity_location aal, amp_location loc, amp_category_value_location acvl, amp_category_value acv "
	        		+ " WHERE aal.amp_location_id = loc.amp_location_id AND loc.location_id = acvl.id AND acvl.parent_category_value = acv.id AND acv.category_value = 'Region' "
	        		+ " AND aal.amp_activity_id in (" + actList + ")";
				
	             qry = session.createSQLQuery(queryString).addEntity(AmpLocation.class);
	             col = qry.list();
	             
			} catch (Exception ex) {
				logger.error("Exception while getting regions :" + ex);
			} 
        }
		return col;
	}
	
	public static Collection<AmpLocation> getLocationsUsedInActivities(String actList){
		Session session = null;
        Query qry = null;
        Collection col = new ArrayList();
        if (actList!=null && actList.length()>0) {
	        try {	
	        	session = PersistenceManager.getSession();
	        	String queryString = "SELECT DISTINCT loc.* FROM amp_activity_location aal, amp_location loc, amp_category_value_location acvl, amp_category_value acv "
	        		+ " WHERE aal.amp_location_id = loc.amp_location_id AND loc.location_id = acvl.id AND acvl.parent_category_value = acv.id "
	        		+ " AND aal.amp_activity_id in (" + actList + ")";
				
	             qry = session.createSQLQuery(queryString).addEntity(AmpLocation.class);
	             col = qry.list();
	             
			} catch (Exception ex) {
				logger.error("Exception while getting regions :" + ex);
			} 
        }
		return col;
	}
	//TODO: Move this to Util class.
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
    public static double getPledgesFunding(Long[] orgIds, Long orgGroupId,
            Date startDate, Date endDate,
            String currCode) throws DgException {
        double totalPlannedPldges = 0;
        String oql = "select fd ";
        oql += " from ";
        oql += FundingPledgesDetails.class.getName()
                + " fd inner join fd.pledgeid plg ";
        oql += " inner join  plg.organization org  ";
        oql += " where  fd.funding_date>=:startDate and fd.funding_date<=:endDate   ";
        if (orgIds == null) {
            if (orgGroupId != -1) {
                oql += " and  org.orgGrpId.ampOrgGrpId=:orgGroupId ";
            }
        } else {
            oql += " and org.ampOrgId in (" + DashboardUtil.getInStatement(orgIds) + ") ";
        }
        Session session = PersistenceManager.getRequestDBSession();
        List<FundingPledgesDetails> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (orgIds == null && orgGroupId != -1) {
                query.setLong("orgGroupId", orgGroupId);
            }
            fundingDets = query.list();
            Iterator<FundingPledgesDetails> fundDetIter = fundingDets.iterator();
            while (fundDetIter.hasNext()) {
                FundingPledgesDetails pledge = fundDetIter.next();
                //converting amounts
                java.sql.Date dt = new java.sql.Date(pledge.getFunding_date().getTime());
                double frmExRt = Util.getExchange(pledge.getCurrency().getCurrencyCode(), dt);
                double toExRt = Util.getExchange(currCode, dt);
                DecimalWraper amt = CurrencyWorker.convertWrapper(pledge.getAmount(), frmExRt, toExRt, dt);
                totalPlannedPldges += amt.doubleValue();
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
        List<AmpOrganisation> selectedOrganizations = filter.getOrganizationsSelected();
        Long orgGroupId = filter.getOrganizationGroupId();
        int transactionType = filter.getTransactionType();
        TeamMember tm = filter.getTeamMember();
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue());
        Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
        Session session = PersistenceManager.getRequestDBSession();
        String oql = "select fd ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName()
                + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        oql += " left join act.locations actloc  ";

        oql += "  where  "
                + "   fd.adjustmentType = 1 ";
        if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            oql += " and fd.transactionType =:transactionType  ";
        } else {
            oql += " and (fd.transactionType=1 or fd.transactionType=0) "; // the option comm&disb is selected
        }
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<:endDate)   ";
        if (selectedOrganizations == null || selectedOrganizations.size() == 0) {
            if (orgGroupId != -1) {
                oql += DashboardUtil.getOrganizationQuery(true, selectedOrganizations);
            }
        } else {
            oql += DashboardUtil.getOrganizationQuery(false, selectedOrganizations);
        }
     
        if(filter.getFromPublicView() != null && filter.getFromPublicView() == true){
            oql += DashboardUtil.getTeamQueryManagement();
        }
        else
        {
            oql += DashboardUtil.getTeamQuery(tm);
        }
        
        oql += " and actloc is NULL ";

        if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
			oql += ActivityUtil.getApprovedActivityQueryString("act");
		}
        
        Query query = session.createQuery(oql);
        query.setDate("startDate", startDate);
        query.setDate("endDate", endDate);
        if (selectedOrganizations == null && orgGroupId != -1) {
            query.setLong("orgGroupId", orgGroupId);
        }
        if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            query.setLong("transactionType", transactionType);
        }
        List<AmpFundingDetail> fundingDets = query.list();
        return fundingDets;

    }
    
    
    public static List<AmpFundingDetail> getLocationFunding(DashboardFilter filter,AmpCategoryValueLocations location) throws DgException {
        Long orgGroupId = filter.getOrganizationGroupId();
        List<AmpOrganisation> selectedOrganizations = filter.getOrganizationsSelected();
        int transactionType = filter.getTransactionType();
        TeamMember teamMember = filter.getTeamMember();
        // apply calendar filter
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue());
        Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());

            /* query that creates new  AmpFundingDetail objects
            which amounts are calculated by multiplication
            of the region percent and amount value*/
            String oql = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actloc.locationPercentage,fd.fixedExchangeRate) ";
            oql += " from ";
            oql += AmpFundingDetail.class.getName()
                    + " as fd inner join fd.ampFundingId f ";
            oql += "   inner join f.ampActivityId act ";
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";

            oql += " where  fd.adjustmentType = 1 ";
            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            oql += " and fd.transactionType =:transactionType  ";
            }
            else{
                 oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
            }
            if (selectedOrganizations == null || selectedOrganizations.size() == 0) {
                if (orgGroupId != -1) {
                    oql += DashboardUtil.getOrganizationQuery(true, selectedOrganizations);
                }
            } else {
                oql += DashboardUtil.getOrganizationQuery(false, selectedOrganizations);
            }
            oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  and  loc.id=  " + location.getId();
            if(filter.getFromPublicView() != null && filter.getFromPublicView() == true){
                oql += DashboardUtil.getTeamQueryManagement();
            }
            else
            {
                oql += DashboardUtil.getTeamQuery(teamMember);
            }
            
            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
            	oql += ActivityUtil.getApprovedActivityQueryString("act");
            }
            
            Session session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (selectedOrganizations == null && orgGroupId != -1) {
                query.setLong("orgGroupId", orgGroupId);
            }
             if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
            query.setLong("transactionType", transactionType);
             }

            List<AmpFundingDetail> fundingDets = query.list();
            return fundingDets;
    }

    public static List<AmpCategoryValueLocations> getLocations(DashboardFilter filter) throws DgException {
        Long orgGroupId = filter.getOrganizationGroupId();
        List<AmpCategoryValueLocations> locations=null;
        List<AmpOrganisation> selectedOrganizations = filter.getOrganizationsSelected();
//        Long[] orgIds = filter.getOrgIds();
        
        int transactionType = filter.getTransactionType();
        TeamMember teamMember = filter.getTeamMember();
        // apply calendar filter
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        
        Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue());
        Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
        /*
         * We are selecting regions which are funded
         * In selected year by the selected organization
         *
         */
        try {
            Long regionId = filter.getSelRegionId();
            String oql = "select distinct loc  from ";
            oql += AmpFundingDetail.class.getName()
                    + " as fd inner join fd.ampFundingId f ";
            oql += "   inner join f.ampActivityId act ";
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
            oql += "  where fd.adjustmentType = 1";
            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
                oql += " and fd.transactionType =:transactionType  ";
            } else {
                oql += " and (fd.transactionType =0 or  fd.transactionType =1) "; // the option comm&disb is selected
            }
            if (selectedOrganizations == null || selectedOrganizations.size() == 0) {
                if (orgGroupId != -1) {
                    oql += DashboardUtil.getOrganizationQuery(true, selectedOrganizations);
                }
            } else {
                oql += DashboardUtil.getOrganizationQuery(false, selectedOrganizations);
            }
            oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
            
            if(filter.getFromPublicView() != null && filter.getFromPublicView() == true){
                oql += DashboardUtil.getTeamQueryManagement();
            }
            else
            {
                oql += DashboardUtil.getTeamQuery(teamMember);
            }
            if (regionId != null && regionId != -1) {
                oql += " and loc.id in (:locations) ";
            }
            
            if (filter.getShowOnlyApprovedActivities() != null && filter.getShowOnlyApprovedActivities()) {
				oql += ActivityUtil.getApprovedActivityQueryString("act");
			}
            
            oql+=" order by loc.parentCategoryValue";
            Session session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (selectedOrganizations == null && orgGroupId != -1) {
                query.setLong("orgGroupId", orgGroupId);
            }
            if (filter.getTransactionType() < 2) { // the option comm&disb is not selected
                query.setLong("transactionType", transactionType);
            }

            Collection<Long> locationIds = filter.getLocationIds();
            Iterator<AmpCategoryValueLocations> it = filter.getLocationsSelected().iterator();
            while(it.hasNext()){
            	AmpCategoryValueLocations loc = it.next();
            	locationIds.clear();
            	locationIds.add(loc.getId());
            }
            if (regionId != null && regionId != -1) {
                query.setParameterList("locations", locationIds);
            }
            locations = query.list();
        }
        catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load locations fundings by donors from db", e);
        }
        return locations;

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
        String currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
        
        List<AmpOrganisation> selectedOrganizations = filter.getOrganizationsSelected();
        Long orgGroupId = filter.getOrganizationGroupId();
        
        TeamMember tm = filter.getTeamMember();
        Collection<Long> locationIds = filter.getLocationIds();
        boolean locationCondition = locationIds != null && locationIds.size() > 0;

        if (locationCondition) {
            oql = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actloc.locationPercentage,fd.fixedExchangeRate) ";
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

        oql += " where  fd.transactionType =:transactionType  and  fd.adjustmentType =:adjustmentType ";
        if (selectedOrganizations == null || selectedOrganizations.size() == 0) {
            if (orgGroupId != -1) {
                oql += DashboardUtil.getOrganizationQuery(true, selectedOrganizations);
            }
        } else {
            oql += DashboardUtil.getOrganizationQuery(false, selectedOrganizations);
        }
        if (locationCondition) {
            oql += " and loc.id in (:locations) ";
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
            if ((selectedOrganizations == null || selectedOrganizations.size() == 0) && orgGroupId != -1) {
                query.setLong("orgGroupId", orgGroupId);
            }
            if (assistanceTypeId != null) {
                query.setLong("assistanceTypeId", assistanceTypeId);
            }
            if (financingInstrumentId != null) {
                query.setLong("financingInstrumentId", financingInstrumentId);
            }
            query.setLong("transactionType", transactionType);
            query.setLong("adjustmentType",adjustmentType);
            if (locationCondition) {
                query.setParameterList("locations", locationIds);
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
                    "Cannot load sector fundings by donors from db", e);
        }


        return total;
    }
    public static AmpOrganisation getOrganisation(Long id) {
        Session session = null;
        AmpOrganisation org = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select o from "
                + AmpOrganisation.class.getName() + " o "
                + "where (o.ampOrgId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                org = (AmpOrganisation) itr.next();
            }
            // end

        } catch (Exception ex) {
            logger.error("Unable to get organisation from database", ex);
        }
        logger.debug("Getting organisation successfully ");
        return org;
    }
		
}
