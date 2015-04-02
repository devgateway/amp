package org.digijava.module.visualization.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.dbentity.AmpGraph;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.helper.EntityRelatedListHelper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GregorianChronology;

public class DashboardUtil {
	
	private static Logger logger = Logger.getLogger(DashboardUtil.class);
	public static final String VISUALIZATION_PROGRESS_SESSION = "visualizationProgressSession";
	
	public static Collection<Long> getNationalActivityList() {
		Collection<Long> ret = new HashSet<Long>();
		try {
			Session session = PersistenceManager.getRequestDBSession();
			Long id = CategoryConstants.IMPLEMENTATION_LEVEL_NATIONAL.getIdInDatabase();
			Query query = session.createSQLQuery("SELECT amp_activity_id FROM amp_activities_categoryvalues WHERE amp_categoryvalue_id = ?");
			query.setLong(0, id);
			ret = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static Map<AmpOrganisation, BigDecimal> getRankAgenciesByKey(Collection<Long> orgList,  DashboardFilter filter) throws DgException{
		Map<AmpOrganisation, BigDecimal> map = new HashMap<AmpOrganisation, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
        String currCode = filter.getCurrencyCode();
        map = DbUtil.getFundingByAgencyList(orgList, currCode, startDate, endDate, filter.getTransactionType(), filter.getAdjustmentType(), filter.getDecimalsToShow(),divideByDenominator, filter);
		return sortByValue (map, null);
	}
	
	public static Map<AmpSector, BigDecimal> getRankSectorsByKey(Collection<AmpSector> secListChildren, Collection<AmpSector> secListParent,  DashboardFilter filter) throws DgException{
		Map<AmpSector, BigDecimal> map = new HashMap<AmpSector, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
        String currCode = filter.getCurrencyCode();
        if (secListChildren!=null && secListChildren.size()!=0)
        	map = DbUtil.getFundingBySectorList(secListChildren, secListParent, currCode, startDate, endDate, filter.getTransactionType(), filter.getAdjustmentType(), filter.getDecimalsToShow(),divideByDenominator, filter, true);
		return sortByValue (map, null);
	}
	
	public static Map<AmpCategoryValueLocations, BigDecimal> getRankRegionsByKey(Collection<AmpCategoryValueLocations> regListChildren, Collection<AmpCategoryValueLocations> regListParent, DashboardFilter filter, boolean donorFundingOnly, HttpServletRequest request) throws DgException{
		Map<AmpCategoryValueLocations, BigDecimal> map = new HashMap<AmpCategoryValueLocations, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
		BigDecimal divideByDenominator;
		if (regListChildren.size()==0) {
			return map;
		}
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
        String currCode = filter.getCurrencyCode();
        
        AmpCategoryValueLocations natLevelLocation = null, nativeNationalLevelLocation = null;
        for (AmpCategoryValueLocations ampCategoryValueLocations : regListChildren) {
			if (ampCategoryValueLocations.getParentLocation()!=null){
				natLevelLocation = getTopLevelLocation(ampCategoryValueLocations).getParentLocation();
			}
			else
				nativeNationalLevelLocation = ampCategoryValueLocations;
		}
                
        if (natLevelLocation == null)
        	natLevelLocation = nativeNationalLevelLocation;
        
        AmpCategoryValueLocations tempLoc = new AmpCategoryValueLocations();
		tempLoc.setName(TranslatorWorker.translateText("National"));
		tempLoc.setId(natLevelLocation.getId());
//		regList.add(natLevelLocation); // add national location to list
        map = DbUtil.getFundingByRegionList(regListChildren, regListParent, tempLoc, currCode, startDate, endDate, filter.getTransactionType(), filter.getAdjustmentType(), filter.getDecimalsToShow(),divideByDenominator, filter, donorFundingOnly, request);
      //Unallocated values   
        AmpCategoryValueLocations tempLoc2 = new AmpCategoryValueLocations();
        tempLoc2.setName(TranslatorWorker.translateText("Unallocated"));
		tempLoc2.setId(0l);
        Long[] ids2 = {0l};
        DashboardFilter newFilter = filter.getCopyFilterForFunding();
		newFilter.setSelLocationIds(ids2);
		DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), filter.getAdjustmentType(), false)[0];
		BigDecimal total = fundingCal.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
		if (total.compareTo(BigDecimal.ZERO) == 1)
			map.put(tempLoc2, total);
        return sortByValue (map, null);
	}
	
	public static Map<AmpTheme, BigDecimal> getRankProgramsByKey(Collection<AmpTheme> progList,  DashboardFilter filter, boolean donorFundingOnly) throws DgException{
		Map<AmpTheme, BigDecimal> map = new HashMap<AmpTheme, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
        String currCode = filter.getCurrencyCode();
        if (progList!=null && progList.size()!=0)
        	map = DbUtil.getFundingByProgramList(progList, currCode, startDate, endDate, filter.getTransactionType(), filter.getAdjustmentType(), filter.getDecimalsToShow(),divideByDenominator, filter, donorFundingOnly);
		return sortByValue (map, null);
	}
	
	public static Map<AmpActivityVersion, BigDecimal> getRankActivitiesByKey(Collection<Long> actList,  DashboardFilter filter) throws DgException{
		Map<AmpActivityVersion, BigDecimal> map = new HashMap<AmpActivityVersion, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
		if (actList!=null && actList.size()!=0)
        	map = DbUtil.getFundingByActivityList(actList, filter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), filter.getDecimalsToShow(),divideByDenominator, true);
        return sortByValue (map, null);
	}
	
	public static Map<AmpSector, BigDecimal> getRankSubSectors (Collection<AmpSector> sectorsList, DashboardFilter filter, Integer startYear, Integer endYear) throws DgException{
		Map<AmpSector, BigDecimal> map = new HashMap<AmpSector, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
        if (startYear !=null && endYear != null) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear);
		} 
		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
		for (Iterator<AmpSector> iterator = sectorsList.iterator(); iterator.hasNext();) {
			AmpSector sector = (AmpSector) iterator.next();
			Long[] ids = {sector.getAmpSectorId()};
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelSectorIds(ids);
            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), filter.getAdjustmentType(), false)[0];
	        BigDecimal total = fundingCal.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        map.put(sector, total);
		}
		return sortByValue (map, null);
	}
	public static Map<AmpSector, BigDecimal> getRankSubRegions (Set<AmpCategoryValueLocations> regionList, DashboardFilter filter, Integer startYear, Integer endYear) throws DgException{
		Map<AmpCategoryValueLocations, BigDecimal> map = new HashMap<AmpCategoryValueLocations, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
        if (startYear !=null && endYear != null) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear);
		} 
		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
		for (Iterator<AmpCategoryValueLocations> iterator = regionList.iterator(); iterator.hasNext();) {
			AmpCategoryValueLocations location = (AmpCategoryValueLocations) iterator.next();
			Long[] ids = {location.getId()};
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelLocationIds(ids);
            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), filter.getAdjustmentType(), false)[0];
	        BigDecimal total = fundingCal.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        if(total.doubleValue()!=0d){
	        	map.put(location, total);
	        }
		}
		return sortByValue (map, null);
	}	

	public static Map sortByValue(Map map, Long top) {
	     List list = new LinkedList(map.entrySet());
	     Collections.sort(list, new Comparator() {
	          public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	              .compareTo(((Map.Entry) (o1)).getValue());
	          }
	     });

	    Long cnt = top;
	    Map result = new LinkedHashMap();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	        if (top!=null && cnt--==0){
	        	break;
	        }
	    }
	    return result;
	} 
	
	private static Map getTop (Map map, int top){
		List list = new LinkedList(map.entrySet());
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
	
//	/**
//	 * If amp_activity_group and cached_amp_activity_group doesnt have the same number of rows then recreate the cache table.
//	 */
//	private static void checkAmpActivityGroupCachedIntegrity() {		
//		java.sql.Connection connection = null;
//		boolean autoCommit = false;
//		try {
//			Session session = PersistenceManager.getRequestDBSession();
//			Query query = session.createSQLQuery("SELECT COUNT(*) FROM amp_activity_group WHERE amp_activity_last_version_id IS NOT NULL");
//			List original = query.list();
//			query = session.createSQLQuery("SELECT COUNT(*) FROM cached_amp_activity_group");
//			List cached = query.list();
//			if(!original.get(0).equals(cached.get(0))) {
//				logger.warn("Updating cached_amp_activity_group");				
//				connection = PersistenceManager.getJdbcConnection();
//				autoCommit = connection.getAutoCommit();
//				connection.setAutoCommit(false);
//				connection.setAutoCommit(true);
//				PublicViewColumnsUtil.createCachedAmpActivityGroupTable(connection);
//				connection.setAutoCommit(false);
//			}							
//		} catch (Exception e) {
//			logger.error(
//					"Error updating table cached_amp_activity_group",
//					e);
//		} finally {
//			if(connection != null) {
//				try {
//					connection.setAutoCommit(autoCommit);
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				PersistenceManager.closeQuietly(connection);
//			}
//		}
//	}
	
	public static void getSummaryAndRankInformation (VisualizationForm form, boolean donorFundingOnly, HttpServletRequest request) throws DgException{
		String trnStep1, trnStep2, trnStep3, trnStep4, trnStep5, trnStep6, trnStep7, trnStep8, trnStep9;
		trnStep1 = trnStep2 = trnStep3 = trnStep4 = trnStep5 = trnStep6 = trnStep7 = trnStep8 = trnStep9 = "";
		try{
			trnStep1 = TranslatorWorker.translateText("Step 1/10: Gathering initial information");
			trnStep2 = TranslatorWorker.translateText("Step 2/10: Gathering aggregated information on commitments");
			trnStep3 = TranslatorWorker.translateText("Step 3/10: Gathering aggregated information on disbursements");
			trnStep4 = TranslatorWorker.translateText("Step 4/10: Gathering aggregated sector information");
			trnStep5 = TranslatorWorker.translateText("Step 5/10: Gathering aggregated location information");
			trnStep6 = TranslatorWorker.translateText("Step 6/10: Gathering full list of projects");
			trnStep7 = TranslatorWorker.translateText("Step 7/10: Gathering aggregated organization information");
			trnStep8 = TranslatorWorker.translateText("Step 8/10: Gathering aggregated NPO information");
			trnStep9 = TranslatorWorker.translateText("Step 9/10: Gathering aggregated program information");
		}
		catch(Exception e){
			logger.error("Couldn't retrieve translation for progress steps");
		}
		
		// AMP-16750 and AMP-16835: Sometimes table CACHED_AMP_ACTIVITY_GROUP(AmpActivityGroupCached) has out-of-date data, generating problems mostly with
		// "Management" dashboards. Here we will update the table if needed before the first dashboard query.
		//TODO: CACHED_AMP_ACTIVITY_GROUP is supposed to have out-of-date-data, because it is a snapshot of an older state of the database.
		// TO BE FIXED REALLY SOON (c) (tm) (r)
		//checkAmpActivityGroupCachedIntegrity();
		
		DashboardFilter filter = form.getFilter();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = getEndDate(fiscalCalendarId, filter.getEndYear().intValue());

		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);

        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, trnStep1);

		//long startTime = System.currentTimeMillis();
		Collection activityListReduced = DbUtil.getActivities(filter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType());
		//long endTime = System.currentTimeMillis();
		////System.out.println("Total elapsed time in execution: "+ (endTime-startTime));
       
		HashMap<Long, AmpActivityVersion> activityList = new HashMap<Long, AmpActivityVersion>();
        Iterator iter = activityListReduced.iterator();
        while (iter.hasNext()) {
            Object[] item = (Object[])iter.next();
            Long ampActivityId = (Long) item[0];
            String ampId = (String) item[1];
            String name = (String) item[2];
            AmpActivityVersion activity = new AmpActivityVersion(ampActivityId, name, ampId);
            activityList.put(ampActivityId, activity);
        }
        List<AmpSector> sectorListChildren = DbUtil.getSectors(filter);
        Collection<AmpSector> sectorListParent = DashboardUtil.getTopLevelParentList(sectorListChildren);

        List<AmpCategoryValueLocations> regionListChildren = DbUtil.getRegions(filter);
		Collection<AmpCategoryValueLocations> regionListParent = DashboardUtil.getTopLevelLocationList(regionListChildren);

		Collection<AmpTheme> NPOListReduced = DbUtil.getPrograms(filter, 0);
        Collection<AmpTheme> programListReduced = DbUtil.getPrograms(filter, 1);
        Collection<AmpTheme> secondaryProgramListReduced = DbUtil.getPrograms(filter, 2);
		Collection<AmpOrganisation> agencyListReduced = DbUtil.getAgencies(filter, true);
		
		HashMap<Long, AmpSector> sectorList = new HashMap<Long, AmpSector>();
        iter = sectorListParent.iterator();
        while (iter.hasNext()) {
        	AmpSector sec = (AmpSector)iter.next();
            sectorList.put(sec.getAmpSectorId(), sec);
        }
        
        HashMap<Long, AmpCategoryValueLocations> regionList = new HashMap<Long, AmpCategoryValueLocations>();
        iter = regionListParent.iterator();
        while (iter.hasNext()) {
        	AmpCategoryValueLocations reg = (AmpCategoryValueLocations)iter.next();
            regionList.put(reg.getId(), reg);
        }
        
        HashMap<Long, AmpTheme> NPOList = new HashMap<Long, AmpTheme>();
        iter = NPOListReduced.iterator();
        while (iter.hasNext()) {
        	AmpTheme npo = (AmpTheme)iter.next();
            NPOList.put(npo.getAmpThemeId(), npo);
        }
        
        HashMap<Long, AmpTheme> programList = new HashMap<Long, AmpTheme>();
        iter = programListReduced.iterator();
        while (iter.hasNext()) {
        	AmpTheme prog = (AmpTheme)iter.next();
        	programList.put(prog.getAmpThemeId(), prog);
        }
        
        HashMap<Long, AmpTheme> secondaryProgramList = new HashMap<Long, AmpTheme>();
        iter = secondaryProgramListReduced.iterator();
        while (iter.hasNext()) {
        	AmpTheme prog = (AmpTheme)iter.next();
        	secondaryProgramList.put(prog.getAmpThemeId(), prog);
        }
        
        HashMap<Long, AmpOrganisation> agencyList = new HashMap<Long, AmpOrganisation>();
        iter = agencyListReduced.iterator();
        while (iter.hasNext()) {
        	AmpOrganisation org = (AmpOrganisation)iter.next();
            agencyList.put(org.getAmpOrgId(), org);
        }
        
		if (activityListReduced.size()>0) {
	        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, trnStep2);
	        DashboardFilter newFilter = filter.getCopyFilterForFunding();
	        newFilter.setTransactionType(org.digijava.module.aim.helper.Constants.COMMITMENT);
	        List<AmpFundingDetail> preloadFundingDetailsCommitments = DbUtil.getFundingDetails(newFilter, startDate, endDate, null, null, true);
	        newFilter.setTransactionType(org.digijava.module.aim.helper.Constants.DISBURSEMENT);
	        List<AmpFundingDetail> preloadFundingDetailsDisbursements = DbUtil.getFundingDetails(newFilter, startDate, endDate, null, null, true);
			DecimalWraper fundingCal = null;
			if (filter.getTransactionType()!=org.digijava.module.aim.helper.Constants.MTEFPROJECTION){
				fundingCal = DbUtil.calculateDetails(filter, preloadFundingDetailsCommitments, org.digijava.module.aim.helper.Constants.COMMITMENT, filter.getAdjustmentType());
				form.getSummaryInformation().setTotalCommitments(fundingCal.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
		        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, trnStep3);
				fundingCal = DbUtil.calculateDetails(filter, preloadFundingDetailsDisbursements, org.digijava.module.aim.helper.Constants.DISBURSEMENT, filter.getAdjustmentType());
				form.getSummaryInformation().setTotalDisbursements(fundingCal.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				form.getSummaryInformation().setAverageProjectSize((fundingCal.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP).divide(new BigDecimal(activityList.size()), filter.getDecimalsToShow(), RoundingMode.HALF_UP)).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			} else {
				form.getSummaryInformation().setTotalCommitments(new BigDecimal(0));
				form.getSummaryInformation().setTotalDisbursements(new BigDecimal(0));
				form.getSummaryInformation().setAverageProjectSize(new BigDecimal(0));		
			}			
			form.getSummaryInformation().setNumberOfSectors(sectorList.size());
			form.getSummaryInformation().setNumberOfRegions(regionList.size());
			form.getSummaryInformation().setNumberOfOrganizations(agencyList.size());
			try {
				request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, trnStep4);
				if (filter.getShowSectorsRanking()==null || filter.getShowSectorsRanking() || isInGraphInList(form.getGraphList(),"SectorProfile")) {
		        	if (sectorList==null || sectorList.size()==0) {
		        		form.getRanksInformation().setFullSectors(null);
				        form.getRanksInformation().setTopSectors(null);
					} else {
						form.getRanksInformation().setFullSectors(getRankSectorsByKey(sectorListChildren, sectorListParent, form.getFilter()));
						form.getRanksInformation().setTopSectors(getTop(form.getRanksInformation().getFullSectors(),form.getFilter().getTopLists()));
					}
				} 
		        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, trnStep5);
				if (filter.getShowRegionsRanking()==null || filter.getShowRegionsRanking() || isInGraphInList(form.getGraphList(),"RegionProfile")) {
	        		if (regionList==null || regionList.size()==0) {
	        			form.getRanksInformation().setFullRegions(null);
			        	form.getRanksInformation().setTopRegions(null);
					} else {
						form.getRanksInformation().setFullRegions(getRankRegionsByKey(regionListChildren, regionListParent, form.getFilter(), donorFundingOnly, request));
			        	form.getRanksInformation().setTopRegions(getTop(form.getRanksInformation().getFullRegions(),form.getFilter().getTopLists()));
					}
		        }
		        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, trnStep6);
		        if (filter.getShowProjectsRanking()==null || filter.getShowProjectsRanking()) {
		        	if (activityList==null || activityList.size()==0) {
		        		form.getRanksInformation().setFullProjects(null);
			        	form.getRanksInformation().setTopProjects(null);
			        	form.getSummaryInformation().setNumberOfProjects(0);
					} else {
						form.getRanksInformation().setFullProjects(getRankActivitiesByKey(activityList.keySet(), form.getFilter()));
						// AMP-16528
						form.getSummaryInformation().setNumberOfProjects(form.getRanksInformation().getFullProjects().size());
			        	
						//form.getRanksInformation().setFullProjects(getRankActivities(activityListReduced, form.getFilter()));
			        	form.getRanksInformation().setTopProjects(getTop(form.getRanksInformation().getFullProjects(),form.getFilter().getTopLists()));			        	
					}
		        }
		        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, trnStep7);
				if (filter.getShowOrganizationsRanking()==null || filter.getShowOrganizationsRanking() || isInGraphInList(form.getGraphList(),"OrganizationProfile")) {
		        	if (agencyList==null || agencyList.size()==0) {
		        		form.getRanksInformation().setFullOrganizations(null);
			        	form.getRanksInformation().setTopOrganizations(null);
					} else {
						form.getRanksInformation().setFullOrganizations(getRankAgenciesByKey(agencyList.keySet(), form.getFilter()));
						form.getRanksInformation().setTopOrganizations(getTop(form.getRanksInformation().getFullOrganizations(),form.getFilter().getTopLists()));
					}
		        }
		        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, trnStep8);
				if (filter.getShowNPORanking()==null || filter.getShowNPORanking() || isInGraphInList(form.getGraphList(),"NPOProfile")) {
					if (NPOListReduced==null || NPOListReduced.size()==0) {
		        		form.getRanksInformation().setFullNPOs(null);
			        	form.getRanksInformation().setTopNPOs(null);
					} else {
						form.getRanksInformation().setFullNPOs(getRankProgramsByKey(NPOListReduced, form.getFilter(), donorFundingOnly));
						form.getRanksInformation().setTopNPOs(getTop(form.getRanksInformation().getFullNPOs(),form.getFilter().getTopLists()));
					}
				} 
		        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, trnStep9);
				if (filter.getShowProgramsRanking()==null || filter.getShowProgramsRanking() || isInGraphInList(form.getGraphList(),"ProgramProfile")) {
					if (programListReduced==null || programListReduced.size()==0) {
		        		form.getRanksInformation().setFullPrograms(null);
			        	form.getRanksInformation().setTopPrograms(null);
					} else {
						form.getRanksInformation().setFullPrograms(getRankProgramsByKey(programListReduced, form.getFilter(), donorFundingOnly));
						form.getRanksInformation().setTopPrograms(getTop(form.getRanksInformation().getFullPrograms(),form.getFilter().getTopLists()));
					}
				} 
				if (filter.getShowProgramsRanking()==null || filter.getShowProgramsRanking() || isInGraphInList(form.getGraphList(),"SecondaryProgramProfile")) {
					if (secondaryProgramListReduced==null || secondaryProgramListReduced.size()==0) {
		        		form.getRanksInformation().setFullSecondaryPrograms(null);
			        	form.getRanksInformation().setTopSecondaryPrograms(null);
					} else {
						form.getRanksInformation().setFullSecondaryPrograms(getRankProgramsByKey(secondaryProgramListReduced, form.getFilter(), donorFundingOnly)); 
						form.getRanksInformation().setTopSecondaryPrograms(getTop(form.getRanksInformation().getFullSecondaryPrograms(),form.getFilter().getTopLists()));
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		} else {
			form.getSummaryInformation().setTotalCommitments(new BigDecimal(0));
			form.getSummaryInformation().setTotalDisbursements(new BigDecimal(0));
			form.getSummaryInformation().setNumberOfProjects(0);
			form.getSummaryInformation().setNumberOfSectors(0);
			form.getSummaryInformation().setNumberOfRegions(0);
			form.getSummaryInformation().setNumberOfOrganizations(0);
			form.getSummaryInformation().setAverageProjectSize(new BigDecimal(0));
			form.getRanksInformation().setFullOrganizations(null);
			form.getRanksInformation().setFullSectors(null);
			form.getRanksInformation().setFullRegions(null);
			form.getRanksInformation().setFullProjects(null);
			form.getRanksInformation().setFullNPOs(null);
			form.getRanksInformation().setFullPrograms(null);
			form.getRanksInformation().setFullSecondaryPrograms(null);
			form.getRanksInformation().setTopOrganizations(null);
			form.getRanksInformation().setTopSectors(null);
			form.getRanksInformation().setTopRegions(null);
			form.getRanksInformation().setTopProjects(null);
			form.getRanksInformation().setTopNPOs(null);
			form.getRanksInformation().setTopPrograms(null);
			form.getRanksInformation().setTopSecondaryPrograms(null);
		}
	}
    
	public static Long[] getProgramsDescendentsIds(Long[] ids){
		Collection<AmpTheme> col = new ArrayList<AmpTheme>();
		for (int i = 0; i < ids.length; i++) {
			AmpTheme prog = DbUtil.getProgramById(ids[i]);
			col.addAll(getProgramsDescendents(prog));
		}
		Long[] retIds = new Long[col.size()];
		int i = 0;
		for (Iterator iterator = col.iterator(); iterator.hasNext();) {
			AmpTheme ampTheme = (AmpTheme) iterator.next();
			retIds[i++] = ampTheme.getAmpThemeId();
		}
		return retIds;
	}
	
	public static Collection<AmpTheme> getProgramsDescendents(AmpTheme prog){
		Collection<AmpTheme> col = new ArrayList<AmpTheme>();
		col.add(prog);
		if ( prog.getSiblings() != null ) {
 	 		for ( AmpTheme th: prog.getSiblings() )
 	 			col.addAll(getProgramsDescendents(th));
 	 	}
 	 	return col;
		
	}
	
	public static String getInStatement(Object ids[])
	{
		return getInStatement(Arrays.asList(ids));
    }

//    public static String getInStatement(Object[] ids) {
//        StringBuffer oql = new StringBuffer();
//        for (int i = 0; i < ids.length; i++) {
//            oql.append(ids[i]);
//            if (i < ids.length - 1) {
//                oql.append(",");
//            }
//        }
//        return oql.toString();
//	}    

    public static String getInStatement(ArrayList<?> ids) {
        return Util.toCSStringForIN(ids);
	}    

    public static String getInStatement(Collection<?> objs) {
    	
    	if (objs.isEmpty())
    		return "-999"; // avoid generating statements like "loc.id in ()", as they trigger "org.hibernate.hql.ast.QuerySyntaxException: unexpected end of subtree"
    	
        StringBuffer oql = new StringBuffer();
        for (Object obj : objs)
        {
            if (oql.length() != 0) {
                oql.append(",");
            }
            
            if (obj instanceof Identifiable){
            	oql.append(((Identifiable) obj).getIdentifier());
            }
            else
            {
            	oql.append(obj.toString());
            }
        }
        return oql.toString();
	}
    
    /**
     * Given a FiscalYearId and a Date, will return the year where the date belongs (reusing existent methods). 
     * @param fiscalCalendarId
     * @param date
     * @return
     */
	public static int getFiscalYearFromDate(AmpFiscalCalendar fiscalCalendar,
			Date date) {
		int year = -1;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// Get the current year (normal calendar) for the date.
		int auxYear = cal.get(Calendar.YEAR);
		if (fiscalCalendar != null) {
			// Get the date when this FiscalYear ends (using existent methods).
			DateTime startDate = new DateTime(getStartDate(fiscalCalendar, auxYear));
			DateTime endDate = new DateTime(getEndDate(fiscalCalendar, auxYear));
			DateTime auxDate = new DateTime(date);
			DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.secondOfMinute());

			if (comparator.compare(auxDate, startDate) < 0) {
				auxYear--;
			} else if (comparator.compare(auxDate, endDate) > 0) {
				auxYear++;
			}
			year = auxYear;
			if (fiscalCalendar.getYearOffset() != null) {
				year += fiscalCalendar.getYearOffset().intValue();
			}
		} else {
			// No special calendar used then return the normal year.
			year = auxYear;
		}
		return year;
	}
	
	/**
	 * This was replaced by new class for new dashboards
	 *
	 * @deprecated use {@link org.digijava.kernel.ampapi.endpoints.util.CalendarUtil} instead.  
	 */
    @Deprecated
    public static Date getStartDate(Long AmpFiscalCalendarId, int year) {
    	AmpFiscalCalendar fiscalCalendar = null;
    	if(AmpFiscalCalendarId != null) {
    		fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(AmpFiscalCalendarId);
    	}
    	return getStartDate(fiscalCalendar, year);
    }
    /**
	 * This was replaced by new class for new dashboards
	 *
	 * @deprecated use {@link org.digijava.kernel.ampapi.endpoints.util.CalendarUtil} instead.  
	 */
    @Deprecated
    public static Date getEndDate(Long AmpFiscalCalendarId, int year) {
    	AmpFiscalCalendar fiscalCalendar = null;
    	if(AmpFiscalCalendarId != null) {
    		fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(AmpFiscalCalendarId);
    	}
    	return getEndDate(fiscalCalendar, year);
    }
    /**
	 * This was replaced by new class for new dashboards
	 *
	 * @deprecated use {@link org.digijava.kernel.ampapi.endpoints.util.CalendarUtil} instead.  
	 */
    @Deprecated
    public static Date getStartDate(AmpFiscalCalendar calendar, int year) {
        Date startDate = null;
        if (calendar != null) {
            if (calendar.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
                startDate = getStartOfYear(year, calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
            } else {      
                 startDate = getGregorianCalendarDate(calendar, year, true);
            }
        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.YEAR, year);
            startDate = cal.getTime();
        }
        return startDate;
    }
    /**
	 * This was replaced by new class for new dashboards
	 *
	 * @deprecated use {@link org.digijava.kernel.ampapi.endpoints.util.CalendarUtil} instead.  
	 */
    @Deprecated
    public static Date getEndDate(AmpFiscalCalendar calendar, int year) {
        Date endDate = null;
        if (calendar != null) {
            if (calendar.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
                //we need data including the last day of toYear,this is till the first day of toYear+1
                int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
                endDate = new Date(getStartOfYear(year + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum()).getTime() - MILLISECONDS_IN_DAY);
            } else {
                endDate=getGregorianCalendarDate(calendar, year, false);
            }

        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.YEAR, year + 1);
            endDate = cal.getTime();
        }
        return endDate;
    }
    /**
	 * This was replaced by new class for new dashboards
	 *
	 * @deprecated use {@link org.digijava.kernel.ampapi.endpoints.util.CalendarUtil} instead.  
	 */
    @Deprecated
    public static Date getStartOfYear(int year, int month, int day) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(year, month, day, 0, 0, 0);
        return cal.getTime();
    }
    /**
	 * This was replaced by new class for new dashboards
	 *
	 * @deprecated use {@link org.digijava.kernel.ampapi.endpoints.util.CalendarUtil} instead.  
	 */
    @Deprecated
    public static Date getGregorianCalendarDate(AmpFiscalCalendar fiscalCalendar, int year, boolean startDate) {
       return getCalendar(fiscalCalendar, startDate, year);
    }
    /**
	 * This was replaced by new class for new dashboards
	 *
	 * @deprecated use {@link org.digijava.kernel.ampapi.endpoints.util.CalendarUtil} instead.  
	 */
    @Deprecated
    public static Date getCalendar(AmpFiscalCalendar fiscalCalendar, boolean startDate, int year) {
        DateTime dt = null;
        String calendarType = fiscalCalendar.getBaseCal();
        if (calendarType.equals("ETH-CAL")) {
        	DateTime dtEth = new DateTime(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum(),0,0,0,0,GregorianChronology.getInstance());
        	DateTime dt1 = dtEth.withChronology(EthiopicChronology.getInstance());
        	dt = new DateTime();
        	dt = dt.withDate(dt1.getYear(), dt1.getMonthOfYear(), dt1.getDayOfMonth());
        } else {
            if (calendarType.equals("NEP-CAL")) {
            	dt = new DateTime(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum(),0,0,0,0,GregorianChronology.getInstance());
            	dt = dt.plusYears(56);
            	dt = dt.plusMonths(8);
            	dt = dt.plusDays(17); //this is to convert gregorian to nepali calendar
            } else
        	dt = new DateTime(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum(),0,0,0,0,GregorianChronology.getInstance());
        }
        if (!startDate) {
        	dt = dt.plusYears(1);
        	dt = dt.minusDays(1);
        }
        return dt.toDate();
    }

    
    public static String getOrganizationQuery(boolean orgGroupView, Long[] selectedOrganizations, Long[] selectedOrgGroups, int agencyType, boolean donorFundingOnly) {
        String qry = "";
        switch (agencyType) {
        case org.digijava.module.visualization.util.Constants.DONOR_AGENCY:
			if (orgGroupView) {
	            qry = " and f.ampDonorOrgId.orgGrpId.ampOrgGrpId in (" + getInStatement(selectedOrgGroups) + ") ";
	        } else {
	            qry = " and f.ampDonorOrgId in (" + getInStatement(selectedOrganizations) + ") ";
	        }
			if (donorFundingOnly)
				qry += " and f.sourceRole.roleCode = '" + org.digijava.module.aim.helper.Constants.ROLE_CODE_DONOR + "' ";
			break;

        case org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY:
			if (orgGroupView) {
	            qry = " and role.roleCode='EA' and orole.organisation.orgGrpId.ampOrgGrpId in (" + getInStatement(selectedOrgGroups) + ") ";
	        } else {
	            qry = " and role.roleCode='EA' and orole.organisation in (" + getInStatement(selectedOrganizations) + ") ";
	        }
			break;

        case org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY:
        	if (orgGroupView) {
	            qry = " and role.roleCode='BA' and orole.organisation.orgGrpId.ampOrgGrpId in (" + getInStatement(selectedOrgGroups) + ") ";
	        } else {
	            qry = " and role.roleCode='BA' and orole.organisation in (" + getInStatement(selectedOrganizations) + ") ";
	        }
			break;
			
        case org.digijava.module.visualization.util.Constants.RESPONSIBLE_ORGANIZATION:
        	if (orgGroupView) {
	            qry = " and role.roleCode='RO' and orole.organisation.orgGrpId.ampOrgGrpId in (" + getInStatement(selectedOrgGroups) + ") ";
	        } else {
	            qry = " and role.roleCode='RO' and orole.organisation in (" + getInStatement(selectedOrganizations) + ") ";
	        }
			break;

		default:
			break;
		}
        
        return qry;
    }

	public static String getTeamQueryManagement() {
        String qr = "";
        List<AmpTeam> teams = DbUtil.getAllChildComputedWorkspaces();
        String relatedOrgs = "";
        for (AmpTeam tm : teams) {
            if (tm.getComputation() != null && tm.getComputation()) {
                relatedOrgs += getComputationOrgsQry(tm);
            }
        }
        //qr += " and act.draft=false and act.approvalStatus ='approved' ";
        qr += " and act.team is not null and (act.team in (select at.ampTeamId from " 
		+ AmpTeam.class.getName() + " at) ";
        if (relatedOrgs.length() > 1) {
            relatedOrgs = relatedOrgs.substring(0, relatedOrgs.length() - 1);
            qr += " or f.ampDonorOrgId in(" + relatedOrgs + ")";
        }
        qr += ")";
        return qr;
    }
	
	public static String getTeamQuery(TeamMember teamMember) {
        String qr = "";
        if (teamMember != null) {
            AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
            List<AmpTeam> teams = new ArrayList<AmpTeam>();
            getTeams(team, teams);
            String relatedOrgs = "";
            String teamIds = "";
            //qr += " and act.draft=false and act.approvalStatus ='approved' ";
            qr += " and (";
            for (AmpTeam tm : teams) {
                if (tm.getComputation() != null && tm.getComputation()) {
                    relatedOrgs += getComputationOrgsQry(tm);
                }
                teamIds += tm.getAmpTeamId() + ",";

            }
            if (teamIds.length() > 1) {
                teamIds = teamIds.substring(0, teamIds.length() - 1);
                qr += " act.team.ampTeamId in ( " + teamIds + ")";

            }
            if (relatedOrgs.length() > 1) {
                relatedOrgs = relatedOrgs.substring(0, relatedOrgs.length() - 1);
                qr += " or f.ampDonorOrgId in(" + relatedOrgs + ")";
            }
            qr += ")";

        } else {
            qr += "  and (act.draft=false OR act.draft is null) and act.team is not null ";
        }
        return qr;
    }

    public static String getComputationOrgsQry(AmpTeam team) {
        String orgIds = "";
        if (team.getComputation() != null && team.getComputation()) {
            Set<AmpOrganisation> orgs = team.getOrganizations();
            Iterator<AmpOrganisation> orgIter = orgs.iterator();
            while (orgIter.hasNext()) {
                AmpOrganisation org = orgIter.next();
                orgIds += org.getAmpOrgId() + ",";
            }

        }
        return orgIds;
    }    
    public static void getTeams(AmpTeam team, List<AmpTeam> teams) {
        teams.add(team);
        Collection<AmpTeam> childrenTeams =  TeamUtil.getAllChildrenWorkspaces(team.getAmpTeamId());
        if (childrenTeams != null) {
            for (AmpTeam tm : childrenTeams) {
                getTeams(tm, teams);
            }
        }
    }
    
    // This recursive method helps the generateLevelHierarchy method.
	public static AmpSector getTopLevelParent(AmpSector topLevelSector) {
		if (topLevelSector.getParentSectorId() != null) {
			topLevelSector = getTopLevelParent(topLevelSector.getParentSectorId());
		}
		return topLevelSector;
	}

	public static AmpTheme getTopLevelProgram(AmpTheme program) {
		if (program.getParentThemeId() != null && program.getIndlevel()>1) {
			program = getTopLevelProgram(program.getParentThemeId());
		}
		return program;
	}

    public static List<AmpSector> getTopLevelParentList(List<AmpSector> list) {
    	List<AmpSector> sectors = new ArrayList<AmpSector>();
    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			AmpSector ampSector = (AmpSector) iterator.next();
			AmpSector ampSectorTopLevel = getTopLevelParent(ampSector);
			if (!sectors.contains(ampSectorTopLevel)) {
				sectors.add(ampSectorTopLevel);
			}
		}
		return sectors;
	}

	public static String getYearName(String headingFY, Long fiscalCalendarId, Date startDate, Date endDate) {
		String result = "";
		String startYear = "";
		String endYear = "";
        AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalendarId);
		SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
		startYear = simpleDateformat.format(startDate);
		endYear = simpleDateformat.format(endDate);
		
		if(startYear.equalsIgnoreCase(endYear)){
			result = startYear;
		}
		else
		{
			if (calendar.getIsFiscal()){
				SimpleDateFormat shortSimpleDateformat=new SimpleDateFormat("yy");
				startYear = shortSimpleDateformat.format(startDate);
				endYear = shortSimpleDateformat.format(endDate);
				result = headingFY + " " + startYear + "-" + endYear;
			} else {
				result = startYear;
			}
		}
		return result;
	}
	
	public static AmpCategoryValueLocations getTopLevelLocation(
			AmpCategoryValueLocations location) {
		if (location.getParentLocation() != null && !location.getParentLocation().getParentCategoryValue().getValue().equals("Country")) {
			location = getTopLevelLocation(location.getParentLocation());
		}
		return location;
	}

	 public static List<AmpCategoryValueLocations> getTopLevelLocationList(List<AmpCategoryValueLocations> list) {
	    	List<AmpCategoryValueLocations> locs = new ArrayList<AmpCategoryValueLocations>();
	    	String Iso = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY);
	    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
	    		AmpCategoryValueLocations ampLoc = (AmpCategoryValueLocations) iterator.next();
	    		if (ampLoc.getParentLocation() == null && ampLoc.getParentCategoryValue().getValue().equals("Country")){
					if (ampLoc.getIso() != null && ampLoc.getIso().equals(Iso)) { // AMP-16629 - This is to skip those locations that are other countries (international), doesn't want to be show on dashboards.
						locs.add(ampLoc);
					}	
	    		} else {
	    			AmpCategoryValueLocations ampLocTopLevel = getTopLevelLocation(ampLoc);
					if (!locs.contains(ampLocTopLevel)) {
						locs.add(ampLocTopLevel);
					}
	    		}
			}
			return locs;
		}

		
	public static BigDecimal getDividingDenominator(Boolean divideThousands, Boolean showInThousands, Boolean isProfile) {
		//All information by default is shown in millions, unless the property filter.showInThousands is activated.
		//If it's activated, then the dividing denominator should be 1000 instead of 1000000
		//This has to take into account that the amounts could already be in thousands
		//In this method, divideThousands holds the individual chart information if it's going to be divided again

		BigDecimal divideByDenominator;
		if (divideThousands == null) divideThousands = false;
		if(isProfile){ //The profile already divide in the preload of data in method getSummaryAndRankInformation(VisualizationForm, HttpServletRequest)
			//return new BigDecimal(1);
			if (divideThousands)
				divideByDenominator = new BigDecimal(1000);
			else
				divideByDenominator = new BigDecimal(1);
		}
		else
		{
	        long divideThousandsMultiplier = divideThousands ? 1000 : 1;
	        long showInThousandsMultiplier = showInThousands ? 1000 : 1000000; //if show in thousands is set then divide by 1000, else, it divides by 1000000 and show in millions
	        //long unitsMultiplier = 1000000 / Math.max(1000000, FeaturesUtil.getAmountMultiplier());
	        double unitsMultiplier = 1d/FeaturesUtil.getAmountMultiplier();// this is to get the value in units because in AmpFundingDetail.java is being applied the multiplier
			divideByDenominator = new BigDecimal(unitsMultiplier * showInThousandsMultiplier * divideThousandsMultiplier);
		}
		return divideByDenominator;
	}

	public static boolean isInGraphInList(List<AmpGraph> list, String containerId){
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			AmpGraph ampGraph = (AmpGraph) iterator.next();
			if (ampGraph.getContainerId().equals(containerId)) {
				return true;
			}
		}
		return false;
	}
	
	public static void initializeFilter(DashboardFilter filter, HttpServletRequest request, AmpDashboard dashboard) {
		
		String publicView = request.getParameter("publicView") != null ? (String) request.getParameter("publicView") : "false";
		if (publicView.equals("true")) {
			filter.setFromPublicView(true);
		} else {
			filter.setFromPublicView(false);
		}
		filter.setDashboardType(Constants.DashboardType.DONOR);
		filter.setCommitmentsVisible(true);
		filter.setDisbursementsVisible(true);
		filter.setExpendituresVisible(true);
		filter.setPledgeVisible(true);
		filter.setShowOrganizationsRanking(false);
		filter.setShowRegionsRanking(false);
		filter.setShowSectorsRanking(false);
		filter.setShowProjectsRanking(false);
		filter.setShowAcronymForOrgNames(false);
		filter.setNationalProjectsToo(false);
		filter.setShowGroupsNotOrgs(false);
		String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
		String locale = RequestUtils.getNavigationLanguage(request).getCode();
		String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (value != null) {
			Long fisCalId = Long.parseLong(value);
			filter.setFiscalCalendarId(fisCalId);
			filter.setDefaultFiscalCalendarId(fisCalId);
			
		}

		List<AmpOrgGroup> orgGroups = new ArrayList<AmpOrgGroup>(org.digijava.module.aim.util.DbUtil.getAllOrgGroups());
		filter.setOrgGroups(orgGroups);
		List<EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>> orgGroupsWithOrgsList = new ArrayList<EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>>();
		for(AmpOrgGroup orgGroup:orgGroups){
			List<AmpOrganisation> organizations=org.digijava.module.aim.util.DbUtil.getOrganisationByGroupId(orgGroup.getAmpOrgGrpId());
			orgGroupsWithOrgsList.add(new EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>(orgGroup,organizations));
		}
		filter.setOrgGroupWithOrgsList(orgGroupsWithOrgsList);
		List<OrganizationSkeleton> orgs = null;

		if (filter.getOrgGroupId() == null
				|| filter.getOrgGroupId() == -1) {

			filter.setOrgGroupId(-1l);// -1 option denotes
												// "All Groups", which is the
												// default choice.
		}
		if(filter.getOrgGroupId()!=-1){

		orgs = org.digijava.module.aim.util.DbUtil.getDonorOrganisationByGroupId(
				filter.getOrgGroupId(), filter.getFromPublicView());
		}
		filter.setOrganizations(orgs);
		try {
		if(filter.getSelSectorConfigId()==null){
				filter.setSelSectorConfigId(SectorUtil.getPrimaryConfigClassification().getId());
		}
		filter.setSectorConfigs(SectorUtil.getAllClassificationConfigs());
		filter.setConfigWithSectorAndSubSectors(new ArrayList<EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>>());
		List<AmpSector> sectors = org.digijava.module.visualization.util.DbUtil
					.getParentSectorsFromConfig(filter.getSelSectorConfigId());
		filter.setSectors(sectors);
		for(AmpClassificationConfiguration config: filter.getSectorConfigs()){
			List<AmpSector> currentConfigSectors = org.digijava.module.visualization.util.DbUtil.getParentSectorsFromConfig(config.getId());
			List<EntityRelatedListHelper<AmpSector,AmpSector>> sectorsWithSubSectors = new ArrayList<EntityRelatedListHelper<AmpSector,AmpSector>>();
			for(AmpSector sector:currentConfigSectors){;
				List<AmpSector> sectorList=new ArrayList<AmpSector>(sector.getSectors());
				sectorsWithSubSectors.add(new EntityRelatedListHelper<AmpSector,AmpSector>(sector,sectorList));
			}
			filter.getConfigWithSectorAndSubSectors().add(new EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>(config,sectorsWithSubSectors));
			}
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (dashboard!=null && filter.getTransactionType() == -1 && filter.getTransactionTypeFilter() == -1 && filter.getTransactionTypeQuickFilter() == -1) {
			if(dashboard.getTransactionTypeFilter() != null && dashboard.getTransactionTypeFilter() > -1) {
				filter.setTransactionType(dashboard.getTransactionTypeFilter());
				filter.setTransactionTypeFilter(dashboard.getTransactionTypeFilter());
				filter.setTransactionTypeQuickFilter(dashboard.getTransactionTypeFilter());
			} else {
				filter.setTransactionType(1);
				filter.setTransactionTypeFilter(1);
				filter.setTransactionTypeQuickFilter(1);
			}
		}

		if (filter.getStartYear() == null) {
			Long year = null;
			Long minYear = null;
			if(dashboard != null && dashboard.getMaxYearFilter() != null && dashboard.getMaxYearFilter() > 0
					&& dashboard.getMinYearFilter() != null && dashboard.getMinYearFilter() > 0){
				filter.setDefaultStartYear(dashboard.getMinYearFilter().longValue());
				filter.setStartYear(dashboard.getMinYearFilter().longValue());
				filter.setStartYearQuickFilter(dashboard.getMinYearFilter().longValue());
				filter.setStartYearFilter(dashboard.getMinYearFilter().longValue());
				filter.setEndYear(dashboard.getMaxYearFilter().longValue());
				filter.setDefaultEndYear(dashboard.getMaxYearFilter().longValue());
				filter.setEndYearQuickFilter(dashboard.getMaxYearFilter().longValue());
				filter.setEndYearFilter(dashboard.getMaxYearFilter().longValue());
				filter.setYearToCompare(dashboard.getMaxYearFilter().longValue() - 1);
			} else {
				try {
					year = Long.parseLong(FeaturesUtil
							.getGlobalSettingValue("Current Fiscal Year"));
					minYear = year - 3;
				} catch (NumberFormatException ex) {
					year = new Long(Calendar.getInstance().get(Calendar.YEAR));
					minYear = year - 3;
				}
				filter.setDefaultStartYear(minYear);
				filter.setStartYear(minYear);
				filter.setStartYearQuickFilter(minYear);
				filter.setStartYearFilter(minYear);
				filter.setEndYear(year);
				filter.setDefaultEndYear(year);
				filter.setEndYearQuickFilter(year);
				filter.setEndYearFilter(year);
				filter.setYearToCompare(year-1);
			}
		}
		filter.setYears(new TreeMap<String, Integer>());
		int yearFrom = Integer.parseInt(FeaturesUtil
						.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
		int countYear = Integer.parseInt(FeaturesUtil
						.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
		long maxYear = yearFrom + countYear;
		if (maxYear < filter.getStartYear()) {
			maxYear = filter.getStartYear();
		}
		for (int i = yearFrom; i <= maxYear; i++) {
			Long fiscalCalendarId = filter.getFiscalCalendarId();
			Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
			Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
            String headingFY = TranslatorWorker.translateText("FY");
			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
			filter.getYears().put(yearName,i);
		}
		String sliderLabels = "";
		for (Long i = filter.getStartYear(); i <= filter.getEndYear(); i++) {
			Long fiscalCalendarId = filter.getFiscalCalendarId();
			Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
			Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
            String headingFY = TranslatorWorker.translateText("FY");
			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
			sliderLabels = sliderLabels + yearName + ",";
		}
		filter.setFlashSliderLabels(sliderLabels);
		
		Collection calendars = org.digijava.module.aim.util.DbUtil.getAllFisCalenders();
		if (calendars != null) {
			filter.setFiscalCalendars(new ArrayList(calendars));
		}
		// if (fromPublicView == false) {
		// if (orgForm.getFiscalCalendarId() == null) {
		// Long fisCalId = tm.getAppSettings().getFisCalId();
		// if (fisCalId == null) {
		// String value = FeaturesUtil
		// .getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		// if (value != null) {
		// fisCalId = Long.parseLong(value);
		// }
		// }
		// orgForm.setFiscalCalendarId(fisCalId);
		// }
		// } else {
		// }
		if (filter.getLargestProjectNumber() == null) {
			filter.setLargestProjectNumber(10);
		}
		if (filter.getDivideThousands() == null) {
			filter.setDivideThousands(false);
		}
		if (filter.getDivideThousandsDecimalPlaces() == null) {
			filter.setDivideThousandsDecimalPlaces(0);
		}
		//Initialize formatting information
		if(filter.getDecimalSeparator() == null || filter.getGroupSeparator() == null ){
			filter.setDecimalSeparator(FormatHelper.getDecimalSymbol());
			filter.setGroupSeparator(FormatHelper.getGroupSymbol());
		}
		
		if (filter.getRegions() == null) {
			try {
				filter.setRegions(new ArrayList<AmpCategoryValueLocations>(
						DynLocationManagerUtil.
						getRegionsOfDefCountryHierarchy()));
				List<EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>> regionWithZones = new ArrayList<EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>>();
				for(AmpCategoryValueLocations region:filter.getRegions()){
					List<AmpCategoryValueLocations> zones=new ArrayList<AmpCategoryValueLocations>(region.getChildLocations());
					regionWithZones.add(new EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>(region,zones));
				}
				filter.setRegionWithZones(regionWithZones);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Long[] regionId = filter.getRegionIds();
		List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

		if (regionId != null && regionId.length!=0 && regionId[0] != -1) {
			AmpCategoryValueLocations region;
			try {
				region = LocationUtil.getAmpCategoryValueLocationById(regionId[0]);
				if (region.getChildLocations() != null) {
					zones.addAll(region.getChildLocations());

				}
			} catch (DgException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		filter.setZones(zones);
        filter.setCurrencies(CurrencyUtil.getUsableCurrencies());
        HttpSession httpSession = request.getSession();
        TeamMember teamMember = (TeamMember) httpSession.getAttribute("currentMember");
		AmpApplicationSettings tempSettings = null;
		if (teamMember != null) {
			tempSettings = org.digijava.module.aim.util.DbUtil.getTeamAppSettings(teamMember.getTeamId());
			if (tempSettings!=null && tempSettings.getCurrency()!=null){
				filter.setCurrencyId(tempSettings.getCurrency().getAmpCurrencyId());
				filter.setCurrencyIdQuickFilter(tempSettings.getCurrency().getAmpCurrencyId());
				filter.setCurrencyIdDefault(filter.getCurrencyId());
			}
		}
		
		if(dashboard != null && teamMember!= null) {
			filter.setWorkspaceOnly(dashboard.getShowOnlyDataFromThisWorkspace());
			filter.setWorkspaceOnlyQuickFilter(dashboard.getShowOnlyDataFromThisWorkspace());
		}
		
		List<CategoryConstants.HardCodedCategoryValue> adjustmentTypeList = new ArrayList<CategoryConstants.HardCodedCategoryValue>();
		adjustmentTypeList.add(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
		adjustmentTypeList.add(CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
        filter.setAdjustmentTypeList(adjustmentTypeList);
        filter.setStatusList(new ArrayList<AmpCategoryValue>(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY)));
        filter.setOrganizations(DbUtil.getOrganisationSkeletons());
		filter.setImplementingAgencyList(DbUtil.getOrganisationSkeletons());
		filter.setBeneficiaryAgencyList(DbUtil.getOrganisationSkeletons());
		filter.setResponsibleOrganizationList(DbUtil.getOrganisationSkeletons());
		filter.setSecondaryProgramsList(DbUtil.getPrograms(2));
		filter.setPeacebuilderMarkerList(new ArrayList<AmpCategoryValue>());
		ArrayList<AmpCategoryValue> catList = new ArrayList<AmpCategoryValue>(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PEACE_MARKERS_KEY));
		for (Iterator iterator = catList.iterator(); iterator.hasNext();) {
			AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
			if (ampCategoryValue.getValue().equals("1")||ampCategoryValue.getValue().equals("2")||ampCategoryValue.getValue().equals("3")){
				ampCategoryValue.setValue(CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue));
				filter.getPeacebuilderMarkerList().add(ampCategoryValue);
			}
		}
		filter.setPeacebuildingList(new ArrayList<AmpCategoryValue>(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PEACEBUILDING_GOALS_KEY)));
        
        filter.setShowAmountsInThousands(Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))==0?1:Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)));
        filter.setShowAmountsInThousandsDefault(filter.getShowAmountsInThousands());
        
        if(dashboard != null && dashboard.getShowAcronymForOrgNames()!= null && dashboard.getShowAcronymForOrgNames().booleanValue() == true) {
        	filter.setShowAcronymForOrgNames(true);
        }
	}
	
	public static TreeMap<Long, String> generateIdToNameForDashboards(Collection<AmpDashboard> dashboards) {
		TreeMap<Long, String> dashboardNames	= new TreeMap<Long, String>();
        if ( dashboards != null ) {
        	for (AmpDashboard ampDashboard : dashboards) {
        		dashboardNames.put(ampDashboard.getId(), ampDashboard.getName() );
			}
        }
        return dashboardNames;
	}
	
	/**
	 * This method will get a map with organizations and translate it into a map of org groups.
	 * @param map
	 * @return
	 */
	public static Map<AmpOrgGroup, BigDecimal> groupOrganizations(
			Map<AmpOrganisation, BigDecimal> map) {
		Map<AmpOrgGroup, BigDecimal> retGroup = new HashMap<AmpOrgGroup, BigDecimal>();
		Iterator<Entry<AmpOrganisation, BigDecimal>> i = map.entrySet()
				.iterator();
		while (i.hasNext()) {
			Entry<AmpOrganisation, BigDecimal> entry = i.next();			
			AmpOrgGroup auxGroup = DbUtil.getOrganisation(entry.getKey().getAmpOrgId()).getOrgGrpId();
			if (retGroup.containsKey(auxGroup)) {
				BigDecimal auxSum = retGroup.get(auxGroup);
				// Replace org group and add the values.
				retGroup.put(auxGroup, auxSum.add(entry.getValue())); 
			} else {
				// New org group.
				retGroup.put(auxGroup, entry.getValue());
			}				
		}
		return sortByValue(retGroup, null);
	}

}
