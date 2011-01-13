package org.digijava.module.visualization.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}
}
