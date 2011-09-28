package org.digijava.module.visualization.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;

import fi.joensuu.joyds1.calendar.EthiopicCalendar;
import fi.joensuu.joyds1.calendar.NepaliCalendar;

import org.digijava.module.aim.helper.Constants;

public class DashboardUtil {
	
	private static Logger logger = Logger.getLogger(DashboardUtil.class);
	public static final String VISUALIZATION_PROGRESS_SESSION = "visualizationProgressSession";

	public static Map<AmpOrganisation, BigDecimal> getRankDonors(Collection<AmpOrganisation> donorList, DashboardFilter filter, Integer selectedYear) throws DgException{
		Map<AmpOrganisation, BigDecimal> map = new HashMap<AmpOrganisation, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
        Date endDate = getEndDate(fiscalCalendarId, filter.getYear().intValue());
        if (selectedYear!=null) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, selectedYear);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, selectedYear);
		} 
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
		for (Iterator<AmpOrganisation> iterator = donorList.iterator(); iterator.hasNext();) {
			AmpOrganisation ampOrg = (AmpOrganisation) iterator.next();
			//Long[] oldIds = filter.getOrgIds();
			Long[] ids = {ampOrg.getAmpOrgId()};
            DashboardFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setOrgIds(ids);
            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
            //filter.setOrgIds(oldIds);
            BigDecimal total = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        map.put(ampOrg, total);
		}
		return sortByValue (map);
	}
	
	public static Map<AmpActivityVersion, BigDecimal> getRankActivities (Collection<AmpActivityVersion> actList,  DashboardFilter filter) throws DgException{
		Map<AmpActivityVersion, BigDecimal> map = new HashMap<AmpActivityVersion, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
        Date endDate = getEndDate(fiscalCalendarId, filter.getYear().intValue());
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
        String currCode = filter.getCurrencyCode();
		for (Iterator<AmpActivityVersion> iterator = actList.iterator(); iterator.hasNext();) {
			AmpActivityVersion ampActivity = (AmpActivityVersion) iterator.next();
			//Long oldActivityId = filter.getActivityId();
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setActivityId(ampActivity.getAmpActivityId());
//			DecimalWraper fundingCal = DbUtil.getFundingByActivityId(ampActivity.getAmpActivityId(), currCode, startDate, endDate, filter.getTransactionType(), Constants.ACTUAL);
			DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
            //filter.setActivityId(oldActivityId);
            BigDecimal total = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        map.put(ampActivity, total);
		}
		return sortByValue (map);
	}

	public static Map<AmpActivityVersion, BigDecimal> getRankActivitiesByKey(Collection<Long> actList,  DashboardFilter filter) throws DgException{
		Map<AmpActivityVersion, BigDecimal> map = new HashMap<AmpActivityVersion, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
        Date endDate = getEndDate(fiscalCalendarId, filter.getYear().intValue());
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
        String currCode = filter.getCurrencyCode();
        map = DbUtil.getFundingByActivityList(actList, currCode, startDate, endDate, filter.getTransactionType(), Constants.ACTUAL, filter.getDecimalsToShow());
		return sortByValue (map);
	}
	
	public static Map<AmpCategoryValueLocations, BigDecimal> getRankRegions (Collection<AmpCategoryValueLocations> regionsList, DashboardFilter filter, Integer selectedYear) throws DgException{
		Map<AmpCategoryValueLocations, BigDecimal> map = new HashMap<AmpCategoryValueLocations, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
        Date endDate = getEndDate(fiscalCalendarId, filter.getYear().intValue());
        if (selectedYear!=null) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, selectedYear);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, selectedYear);
		} 
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
		for (Iterator<AmpCategoryValueLocations> iterator = regionsList.iterator(); iterator.hasNext();) {
			AmpCategoryValueLocations location = (AmpCategoryValueLocations) iterator.next();
			//Long[] oldIds = filter.getSelLocationIds();
			Long[] ids = {location.getId()};
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelLocationIds(ids);
            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
            //filter.setSelLocationIds(oldIds);
            BigDecimal total = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        map.put(location, total);
		}
		return sortByValue (map);
	}
	
	public static Map<AmpSector, BigDecimal> getRankSectors (Collection<AmpSector> sectorsList, DashboardFilter filter, Integer selectedYear) throws DgException{
		Map<AmpSector, BigDecimal> map = new HashMap<AmpSector, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
        Date endDate = getEndDate(fiscalCalendarId, filter.getYear().intValue());
        if (selectedYear!=null) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, selectedYear);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, selectedYear);
		} 
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
		for (Iterator<AmpSector> iterator = sectorsList.iterator(); iterator.hasNext();) {
			AmpSector sector = (AmpSector) iterator.next();
			Long[] ids = {sector.getAmpSectorId()};
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelSectorIds(ids);
            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
	        BigDecimal total = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
            AmpSector topLevelSector = getTopLevelParent(sector);
            if(map.containsKey(topLevelSector)){
            	BigDecimal currentTotal = map.get(topLevelSector);
            	map.put(topLevelSector, total.add(currentTotal));
            }
            else
            {
    	        map.put(topLevelSector, total);
            }

		}
		return sortByValue (map);
	}
	
	public static Map<AmpSector, BigDecimal> getRankSubSectors (Collection<AmpSector> sectorsList, DashboardFilter filter, Integer selectedYear) throws DgException{
		Map<AmpSector, BigDecimal> map = new HashMap<AmpSector, BigDecimal>();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
        Date endDate = getEndDate(fiscalCalendarId, filter.getYear().intValue());
        if (selectedYear!=null) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, selectedYear);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, selectedYear);
		} 
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
		for (Iterator<AmpSector> iterator = sectorsList.iterator(); iterator.hasNext();) {
			AmpSector sector = (AmpSector) iterator.next();
			//Long[] oldIds = filter.getSectorIds();
			Long[] ids = {sector.getAmpSectorId()};
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelSectorIds(ids);
            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
            //filter.setSectorIds(oldIds);
	        BigDecimal total = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        map.put(sector, total);
		}
		return sortByValue (map);
	}
	
	public static Map sortByValue(Map map) {
		return sortByValue(map, null);
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
	
	public static void getSummaryAndRankInformation (VisualizationForm form, HttpServletRequest request) throws DgException{
    	Long startTimeTotal, endTimeTotal;
        startTimeTotal = System.currentTimeMillis();
    	Long startTime, endTime;
        startTime = System.currentTimeMillis();
		DashboardFilter filter = form.getFilter();
		Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange());
        Date endDate = getEndDate(fiscalCalendarId, filter.getYear().intValue());
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
        endTime = System.currentTimeMillis();
        logger.info("Gathering information:" + (endTime - startTime));
        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, "Step 1/8: Gathering initial information");
        startTime = System.currentTimeMillis();
        ArrayList<AmpSector> allSectorList = DbUtil.getAmpSectors();
        endTime = System.currentTimeMillis();
        logger.info("Getting Sectors:" + (endTime - startTime));
		filter.setAllSectorList(allSectorList);
        startTime = System.currentTimeMillis();

        Collection activityListReduced = DbUtil.getActivities(filter);
        endTime = System.currentTimeMillis();
        logger.info("Getting Activity List Reduced:" + (endTime - startTime));

        startTime = System.currentTimeMillis();
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
        endTime = System.currentTimeMillis();
        logger.info("Going through activity list preparing the Hash:" + (endTime - startTime));
        
        startTime = System.currentTimeMillis();
		Collection<AmpSector> sectorList = DbUtil.getSectors(filter);
        endTime = System.currentTimeMillis();
        logger.info("Getting Sectors:" + (endTime - startTime));
        startTime = System.currentTimeMillis();
		Collection<AmpCategoryValueLocations> regionList = DbUtil.getRegions(filter);
        endTime = System.currentTimeMillis();
        logger.info("Getting Regions:" + (endTime - startTime));
        startTime = System.currentTimeMillis();
		Collection<AmpOrganisation> donorList = DbUtil.getDonors(filter);
        endTime = System.currentTimeMillis();
        logger.info("Getting Donors:" + (endTime - startTime));
        startTime = System.currentTimeMillis();
		if (activityList.size()>0) {
	        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, "Step 2/8: Gathering aggregated information on commitments");
	        startTime = System.currentTimeMillis();
	        List<AmpFundingDetail> preloadFundingDetails = DbUtil.getFundingDetails(filter, startDate, endDate, null, null);
	        endTime = System.currentTimeMillis();
	        logger.info("Getting Funding Details:" + (endTime - startTime));
			DecimalWraper fundingCal = null;
	        startTime = System.currentTimeMillis();
			fundingCal = DbUtil.calculateDetails(filter, preloadFundingDetails, Constants.COMMITMENT, Constants.ACTUAL);
	        endTime = System.currentTimeMillis();
	        logger.info("First time getting Total Commitments:" + (endTime - startTime));
			form.getSummaryInformation().setTotalCommitments(fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
	        startTime = System.currentTimeMillis();
	        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, "Step 3/8: Gathering aggregated information on disbursements");
			fundingCal = DbUtil.calculateDetails(filter, preloadFundingDetails, Constants.DISBURSEMENT, Constants.ACTUAL);
	        endTime = System.currentTimeMillis();
	        logger.info("First time getting Total Disbursements:" + (endTime - startTime));
			form.getSummaryInformation().setTotalDisbursements(fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			form.getSummaryInformation().setNumberOfProjects(activityList.size());
			form.getSummaryInformation().setNumberOfSectors(sectorList.size());
			form.getSummaryInformation().setNumberOfRegions(regionList.size());
			form.getSummaryInformation().setNumberOfDonors(donorList.size());
			form.getSummaryInformation().setAverageProjectSize((fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP).divide(new BigDecimal(activityList.size()), filter.getDecimalsToShow(), RoundingMode.HALF_UP)).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			try {
		        startTime = System.currentTimeMillis();
		        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, "Step 4/8: Gathering aggregated sector information");
				form.getRanksInformation().setFullSectors(getRankSectors(sectorList, form.getFilter(), null));
		        endTime = System.currentTimeMillis();
		        logger.info("setFullSectors:" + (endTime - startTime));
		        startTime = System.currentTimeMillis();
		        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, "Step 5/8: Gathering aggregated location information");
				form.getRanksInformation().setFullRegions(getRankRegions(regionList, form.getFilter(), null));
		        endTime = System.currentTimeMillis();
		        logger.info("setFullRegions:" + (endTime - startTime));
		        startTime = System.currentTimeMillis();
		        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, "Step 6/8: Gathering full list of projects");
				form.getRanksInformation().setFullProjects(getRankActivitiesByKey(activityList.keySet(), form.getFilter()));
		        endTime = System.currentTimeMillis();
		        logger.info("setFullProjects:" + (endTime - startTime));
		        startTime = System.currentTimeMillis();
		        request.getSession().setAttribute(VISUALIZATION_PROGRESS_SESSION, "Step 7/8: Gathering aggregated donor information");
				form.getRanksInformation().setFullDonors(getRankDonors(donorList, form.getFilter(), null));
		        endTime = System.currentTimeMillis();
		        logger.info("setFullDonors:" + (endTime - startTime));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			form.getSummaryInformation().setTotalCommitments(new BigDecimal(0));
			form.getSummaryInformation().setTotalDisbursements(new BigDecimal(0));
			form.getSummaryInformation().setNumberOfProjects(0);
			form.getSummaryInformation().setNumberOfSectors(0);
			form.getSummaryInformation().setNumberOfRegions(0);
			form.getSummaryInformation().setNumberOfDonors(0);
			form.getSummaryInformation().setAverageProjectSize(new BigDecimal(0));
			form.getRanksInformation().setFullSectors(null);
			form.getRanksInformation().setFullRegions(null);
			form.getRanksInformation().setFullProjects(null);
		}
        endTimeTotal = System.currentTimeMillis();
        logger.info("Total Execution Time:" + (endTimeTotal - startTimeTotal));
		
		
	}
    public static String getInStatement(Long ids[]) {
        String oql = "";
        for (int i = 0; i < ids.length; i++) {
            oql += "" + ids[i];
            if (i < ids.length - 1) {
                oql += ",";
            }
        }
        return oql;
    }

    public static String getInStatement(Object[] ids) {
        String oql = "";
        for (int i = 0; i < ids.length; i++) {
            oql += "" + ids[i];
            if (i < ids.length - 1) {
                oql += ",";
            }
        }
        return oql;
	}    

    public static Date getStartDate(Long fiscalCalendarId, int year) {
        Date startDate = null;
        if (fiscalCalendarId != null && fiscalCalendarId != -1) {
            AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalendarId);
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

    public static Date getEndDate(Long fiscalCalendarId, int year) {
        Date endDate = null;
        if (fiscalCalendarId != null && fiscalCalendarId != -1) {
            AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalendarId);
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

    public static Date getStartOfYear(int year, int month, int day) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(year, month, day, 0, 0, 0);
        return cal.getTime();
    }

    public static Date getGregorianCalendarDate(AmpFiscalCalendar fiscalCalendar, int year, boolean startDate) {
        Date date;
        fi.joensuu.joyds1.calendar.Calendar calendar = getCalendar(fiscalCalendar, startDate, year);
        Calendar gregorianCal = calendar.toJavaUtilGregorianCalendar();
        date = gregorianCal.getTime();
        return date;
    }
    
    public static fi.joensuu.joyds1.calendar.Calendar getCalendar(AmpFiscalCalendar fiscalCalendar, boolean startDate, int year) {
        fi.joensuu.joyds1.calendar.Calendar calendar = null;
        String calendarType = fiscalCalendar.getBaseCal();
        if (calendarType.equals("ETH-CAL")) {
            calendar = new EthiopicCalendar();
        } else {
            if (calendarType.equals("NEP-CAL")) {
                calendar = new NepaliCalendar();
            }
        }
        if (startDate) {
            calendar.set(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum());
        } else {
            calendar.set(year + 1, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum());
            calendar.addDays(-1);
        }
        return calendar;
    }

    public static String getOrganizationQuery(boolean orgGroupView, Long[] selectedOrganizations, Long[] selectedOrgGroups) {
        String qry = "";
        if (orgGroupView) {
            qry = " and  f.ampDonorOrgId.orgGrpId.ampOrgGrpId in (" + getInStatement(selectedOrgGroups) + ") ";
        } else {
            qry = " and f.ampDonorOrgId in (" + getInStatement(selectedOrganizations) + ") ";
        }
        return qry;
    }

    private static String getInStatementList(
			List selectedItems) {
        String oql = "";
        Iterator it = selectedItems.iterator();

        while(it.hasNext()){
        	Object object = it.next();
        	if (object instanceof AmpOrganisation) {
    			AmpOrganisation ampOrganization = (AmpOrganisation)object;
            	oql += ampOrganization.getAmpOrgId();
            	if (it.hasNext()){
            		oql += ",";
            	}
        	}
        	
        }
        return oql;
	}

	public static String getTeamQueryManagement() {
        String qr = "";
        qr += " and act.draft=false and act.approvalStatus ='approved' ";
        qr += " and act.team is not null and act.team in (select at.ampTeamId from " 
		+ AmpTeam.class.getName() + " at where parentTeamId is not null)";
        
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
            if (teamMember.getTeamAccessType().equals("Management")) {
                qr += " and act.draft=false and act.approvalStatus ='approved' ";
            }
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
            qr += "  and act.team is not null ";
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
			SimpleDateFormat shortSimpleDateformat=new SimpleDateFormat("yy");
			startYear = shortSimpleDateformat.format(startDate);
			endYear = shortSimpleDateformat.format(endDate);
			result = headingFY + " " + startYear + "-" + endYear;
		}
		return result;
	}

}
