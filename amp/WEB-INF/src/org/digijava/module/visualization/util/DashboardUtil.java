package org.digijava.module.visualization.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.visualization.form.VisualizationForm;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

public class DashboardUtil {
	
	private static Logger logger = Logger.getLogger(DashboardUtil.class);
	
	private static Collection<AmpOrganisation> getAllOrganizations() {
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

	private static Collection<AmpActivity> getAllActivities() {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			
			String queryString = " SELECT act FROM " + AmpActivity.class.getName() + " act ";
			
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

	private static Collection<AmpFundingDetail> getAllFundingsDetails() {
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

	private static Collection<AmpFunding> getAllFundings() {
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

	private static Collection<AmpCategoryValueLocations> getAllRegions() {
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

	private static int getProjectsAmount(){
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
	
	private static int getSectorsAmount(){
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
	
	private static int getRegionsAmount(){
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
	
	private static Collection<AmpFundingDetail>  getFundingDetailsByRegion(Long id){
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
	
	public static Map<AmpOrganisation, BigDecimal> getRankDonors(boolean top){
		Map<AmpOrganisation, BigDecimal> map = new HashMap<AmpOrganisation, BigDecimal>();
		List<AmpFundingDetail> fundDetailListFiltered = new ArrayList<AmpFundingDetail>();
		Collection<AmpOrganisation> donorList = getAllOrganizations();
		Collection<AmpFunding> fundList = getAllFundings();
		
		for (Iterator iterator = donorList.iterator(); iterator.hasNext();) {
			AmpOrganisation ampOrg = (AmpOrganisation) iterator.next();
			for (Iterator iterator2 = fundList.iterator(); iterator2.hasNext();) {
				AmpFunding ampFunding = (AmpFunding) iterator2.next();
				if (ampFunding.getAmpDonorOrgId().getAmpOrgId().equals(ampOrg.getAmpOrgId())) {
					Collection<AmpFundingDetail> fundDetailList = ampFunding.getFundingDetails();
					for (Iterator iterator3 = fundDetailList.iterator(); iterator3.hasNext();) {
						AmpFundingDetail ampFundingDetail = (AmpFundingDetail) iterator3.next();
						fundDetailListFiltered.add(ampFundingDetail);
					}
				}
			}
			FundingCalculationsHelper cal = new FundingCalculationsHelper();
			cal.doCalculations(fundDetailListFiltered, "USD");
	        BigDecimal total = cal.getTotActualDisb().getValue();
	        map.put(ampOrg, total);
	        fundDetailListFiltered.removeAll(fundDetailListFiltered);
		}
		if (top) {
			return sortByValueUsingTop (map);
		} else {
			return sortByValue (map);
		}	
	}
	
	public static Map<AmpActivity, BigDecimal> getRankActivities (boolean top){
		Map<AmpActivity, BigDecimal> map = new HashMap<AmpActivity, BigDecimal>();
		List<AmpFundingDetail> fundDetailList = new ArrayList<AmpFundingDetail>();
		Collection<AmpActivity> activitiesList = getAllActivities();
		for (Iterator iterator = activitiesList.iterator(); iterator.hasNext();) {
			AmpActivity ampActivity = (AmpActivity) iterator.next();
			Collection<AmpFunding> fundList = ampActivity.getFunding();
			for (Iterator iterator2 = fundList.iterator(); iterator2.hasNext();) {
				AmpFunding ampFunding = (AmpFunding) iterator2.next();
				Collection<AmpFundingDetail> fundDetList = ampFunding.getFundingDetails();
				for (Iterator iterator3 = fundDetList.iterator(); iterator3.hasNext();) {
					AmpFundingDetail ampFundingDetail = (AmpFundingDetail) iterator3.next();
					fundDetailList.add(ampFundingDetail);
				}
			}
			FundingCalculationsHelper cal = new FundingCalculationsHelper();
	        cal.doCalculations(fundDetailList, "USD");
	        BigDecimal total = cal.getTotActualDisb().getValue();
	        map.put(ampActivity, total);
	        fundDetailList.removeAll(fundDetailList);
		}
		if (top) {
			return sortByValueUsingTop (map);
		} else {
			return sortByValue (map);
		}	
	}
	
	public static Map<AmpCategoryValueLocations, BigDecimal> getRankRegions (boolean top){
		Map<AmpCategoryValueLocations, BigDecimal> map = new HashMap<AmpCategoryValueLocations, BigDecimal>();
		Collection<AmpCategoryValueLocations> regionsList = getAllRegions();
		for (Iterator iterator = regionsList.iterator(); iterator.hasNext();) {
			AmpCategoryValueLocations region = (AmpCategoryValueLocations) iterator.next();
			Collection<AmpFundingDetail> fundDetList = getFundingDetailsByRegion(region.getId());
			FundingCalculationsHelper cal = new FundingCalculationsHelper();
	        cal.doCalculations(fundDetList, "USD");
	        BigDecimal total = cal.getTotActualDisb().getValue();
	        map.put(region, total);
		}
		if (top) {
			return sortByValueUsingTop (map);
		} else {
			return sortByValue (map);
		}	
	}
	
	private static Map sortByValue(Map map) {
	     List list = new LinkedList(map.entrySet());
	     Collections.sort(list, new Comparator() {
	          public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	              .compareTo(((Map.Entry) (o1)).getValue());
	          }
	     });

	    Map result = new LinkedHashMap();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	} 
	
	private static Map sortByValueUsingTop(Map map) {
		 int top = Constants.GlobalSettings.TOP_LIMIT;
	     List list = new LinkedList(map.entrySet());
	     Collections.sort(list, new Comparator() {
	          public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	              .compareTo(((Map.Entry) (o1)).getValue());
	          }
	     });

	    Map result = new LinkedHashMap();
	    int counter = 0;
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	        counter++;
	        if (counter>=top) {
				break;
			}
	    }
	    return result;
	} 
	
	public static void getSummaryInformation (VisualizationForm form){
		Collection<AmpFundingDetail> fundDetailList = getAllFundingsDetails();
		FundingCalculationsHelper cal = new FundingCalculationsHelper();
		cal.doCalculations(fundDetailList, "USD");
		form.getSummaryInformation().setTotalCommitments(cal.getTotActualComm().getValue());
		form.getSummaryInformation().setTotalDisbursements(cal.getTotActualDisb().getValue());
		form.getSummaryInformation().setNumberOfProjects(getProjectsAmount());
		form.getSummaryInformation().setNumberOfSectors(getSectorsAmount());
		form.getSummaryInformation().setNumberOfRegions(getRegionsAmount());
		form.getSummaryInformation().setAverageProjectSize(cal.getTotActualComm().getValue().divide(new BigDecimal(getProjectsAmount()), 3, RoundingMode.HALF_UP));
	}
	
}
