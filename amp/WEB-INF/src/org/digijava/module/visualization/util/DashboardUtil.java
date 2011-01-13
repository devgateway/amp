package org.digijava.module.visualization.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.visualization.form.VisualizationForm;

public class DashboardUtil {
	
	private static Logger logger = Logger.getLogger(DashboardUtil.class);

	public static Map<AmpOrganisation, BigDecimal> getRankDonors(){
		Map<AmpOrganisation, BigDecimal> map = new HashMap<AmpOrganisation, BigDecimal>();
		List<AmpFundingDetail> fundDetailListFiltered = new ArrayList<AmpFundingDetail>();
		Collection<AmpOrganisation> donorList = DbUtil.getAllOrganizations();
		Collection<AmpFunding> fundList = DbUtil.getAllFundings();
		
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
	        BigDecimal total = cal.getTotActualDisb().getValue().setScale(3, RoundingMode.HALF_UP);
	        map.put(ampOrg, total);
	        fundDetailListFiltered.removeAll(fundDetailListFiltered);
		}
		return sortByValue (map);
	}
	
	public static Map<AmpActivity, BigDecimal> getRankActivities (){
		Map<AmpActivity, BigDecimal> map = new HashMap<AmpActivity, BigDecimal>();
		List<AmpFundingDetail> fundDetailList = new ArrayList<AmpFundingDetail>();
		Collection<AmpActivity> activitiesList = DbUtil.getAllActivities();
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
	        BigDecimal total = cal.getTotActualDisb().getValue().setScale(3, RoundingMode.HALF_UP);
	        map.put(ampActivity, total);
	        fundDetailList.removeAll(fundDetailList);
		}
		return sortByValue (map);
	}
	
	public static Map<AmpCategoryValueLocations, BigDecimal> getRankRegions (){
		Map<AmpCategoryValueLocations, BigDecimal> map = new HashMap<AmpCategoryValueLocations, BigDecimal>();
		Collection<AmpCategoryValueLocations> regionsList = DbUtil.getAllRegions();
		for (Iterator iterator = regionsList.iterator(); iterator.hasNext();) {
			AmpCategoryValueLocations region = (AmpCategoryValueLocations) iterator.next();
			Collection<AmpFundingDetail> fundDetList = DbUtil.getFundingDetailsByRegion(region.getId());
			FundingCalculationsHelper cal = new FundingCalculationsHelper();
	        cal.doCalculations(fundDetList, "USD");
	        BigDecimal total = cal.getTotActualDisb().getValue().setScale(3, RoundingMode.HALF_UP);
	        map.put(region, total);
		}
		return sortByValue (map);
	}
	
	public static Map<AmpSector, BigDecimal> getRankSectors (){
		Map<AmpSector, BigDecimal> map = new HashMap<AmpSector, BigDecimal>();
		Collection<AmpSector> sectorsList = DbUtil.getAllSectors();
		for (Iterator iterator = sectorsList.iterator(); iterator.hasNext();) {
			AmpSector sector = (AmpSector) iterator.next();
			Collection<AmpFundingDetail> fundDetList = DbUtil.getFundingDetailsBySector(sector.getAmpSectorId());
			FundingCalculationsHelper cal = new FundingCalculationsHelper();
	        cal.doCalculations(fundDetList, "USD");
	        BigDecimal total = cal.getTotActualDisb().getValue().setScale(3, RoundingMode.HALF_UP);
	        map.put(sector, total);
		}
		return sortByValue (map);
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
	
	public static void getSummaryInformation (VisualizationForm form){
		Collection<AmpFundingDetail> fundDetailList = DbUtil.getAllFundingsDetails();
		FundingCalculationsHelper cal = new FundingCalculationsHelper();
		cal.doCalculations(fundDetailList, "USD");
		form.getSummaryInformation().setTotalCommitments(cal.getTotActualComm().getValue().setScale(3, RoundingMode.HALF_UP));
		form.getSummaryInformation().setTotalDisbursements(cal.getTotActualDisb().getValue().setScale(3, RoundingMode.HALF_UP));
		form.getSummaryInformation().setNumberOfProjects(DbUtil.getProjectsAmount());
		form.getSummaryInformation().setNumberOfSectors(DbUtil.getSectorsAmount());
		form.getSummaryInformation().setNumberOfRegions(DbUtil.getRegionsAmount());
		form.getSummaryInformation().setAverageProjectSize((cal.getTotActualComm().getValue().divide(new BigDecimal(DbUtil.getProjectsAmount()), 3, RoundingMode.HALF_UP)).setScale(3, RoundingMode.HALF_UP));
	}
	
}
