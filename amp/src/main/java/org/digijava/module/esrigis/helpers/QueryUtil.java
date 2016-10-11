package org.digijava.module.esrigis.helpers;
/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.digijava.module.aim.helper.Constants.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.HierarchyListableUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.esrigis.action.MainMap;
import org.digijava.module.visualization.helper.EntityRelatedListHelper;
import org.digijava.module.visualization.util.Constants;
import org.joda.time.DateTime;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GregorianChronology;

public class QueryUtil {
	 public static final BigDecimal ONEHUNDERT = new BigDecimal(100);
	 private static Logger logger = Logger.getLogger(QueryUtil.class);

	 
	public static Date getStartDate(Long fiscalCalendarId, int year) {
		Date startDate = null;
		if (fiscalCalendarId != null && fiscalCalendarId != -1) {
			AmpFiscalCalendar calendar = FiscalCalendarUtil
					.getAmpFiscalCalendar(fiscalCalendarId);
			if (calendar.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
				startDate = getStartOfYear(year,
						calendar.getStartMonthNum() - 1,
						calendar.getStartDayNum());
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

	public static Date getStartOfYear(int year, int month, int day) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, month, day, 0, 0, 0);
		return cal.getTime();
	}

	public static Date getGregorianCalendarDate(AmpFiscalCalendar fiscalCalendar, int year, boolean startDate) {
	       return getCalendar(fiscalCalendar, startDate, year);
	    }


	@Deprecated
	/**
	 * 1. this code is buggy, it is part of an old buggy module <br />
	 * 2. DO NOT COPY-PASTE OR USE THIS FUNCTION, it is part of an old buggy module which is scheduled for physical removal <br />
	 * 3. You are probably interested in {@link org.digijava.kernel.ampapi.endpoints.util.CalendarUtil#getDate(AmpFiscalCalendar, boolean, int)} <br />
	 * 
	 * @param fiscalCalendar
	 * @param startDate
	 * @param year
	 * @return
	 */
	public static Date getCalendar(AmpFiscalCalendar fiscalCalendar, boolean startDate, int year) {
	        DateTime dt = null;
	        String calendarType = fiscalCalendar.getBaseCal();
	        if (calendarType.equals("ETH-CAL")) {
	        	DateTime dtEth = new DateTime(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum(),0,0,0,0,EthiopicChronology.getInstance());
	        	dt = dtEth.withChronology(GregorianChronology.getInstance());
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
	
	public static Date getEndDate(Long fiscalCalendarId, int year) {
		Date endDate = null;
		if (fiscalCalendarId != null && fiscalCalendarId != -1) {
			AmpFiscalCalendar calendar = FiscalCalendarUtil
					.getAmpFiscalCalendar(fiscalCalendarId);
			if (calendar.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
				// we need data including the last day of toYear,this is till
				// the first day of toYear+1
				int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
				endDate = new Date(getStartOfYear(year + 1,
						calendar.getStartMonthNum() - 1,
						calendar.getStartDayNum()).getTime()
						- MILLISECONDS_IN_DAY);
			} else {
				endDate = getGregorianCalendarDate(calendar, year, false);
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

	public static String getOrganizationQuery(boolean orgGroupView, Long[] selectedOrganizations, Long[] selectedOrgGroups) {
		String qry = "";
		if (orgGroupView) {
			qry = " and  f.ampDonorOrgId.orgGrpId.ampOrgGrpId in ("
					+ getInStatement(selectedOrgGroups) + ") ";
		} else {
			qry = " and f.ampDonorOrgId in ("
					+ getInStatement(selectedOrganizations) + ") ";
		}
		return qry;
	}
	public static String getOrganizationQuery(boolean orgGroupView, Long[] selectedOrganizations, Long[] selectedOrgGroups, String typeCode) {
		String qry = "";
		if (orgGroupView) {
			qry = " and  role.organisation.ampOrgGrpId in ("
					+ getInStatement(selectedOrgGroups) + ") and role.roleCode = '" + typeCode +"' ";
		} else {
			qry = " and role.organisation.orgGrpId in ("
					+ getInStatement(selectedOrganizations) + ") and role.roleCode = '" + typeCode +"' ";
		}
		return qry;
	}	
	public static String getOrganizationTypeQuery(boolean orgTypeView, Long[] selectedOrganizations, Long[] selectedtypes) {
		String qry = "";
		if (orgTypeView) {
			qry = " and  f.ampDonorOrgId.orgGrpId.orgType in ("+ getInStatement(selectedtypes) + ") ";
		} else {
			qry = " and f.ampDonorOrgId in ("+ getInStatement(selectedOrganizations) + ") ";
		}
		return qry;
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
	 
	public static String getTeamQueryManagement() {
		String qr = "";
		qr += String.format(" and (act.draft=false OR act.draft is null) and act.approvalStatus in ('%s', '%s') ", APPROVED_STATUS, STARTED_APPROVED_STATUS);
		qr += " and act.team is not null and act.team in (select at.ampTeamId from "
				+ AmpTeam.class.getName()
				+ " at where parentTeamId is not null)";

		return qr;
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

	  @Deprecated
	  public static String getTeamQuery(TeamMember teamMember) {
	        String qr = "";
	        if (teamMember != null) {
	            AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
	            List<AmpTeam> teams = new ArrayList<AmpTeam>();
	            getTeams(team, teams);
	            String relatedOrgs = "";
	            String teamIds = "";
	            if (teamMember.getTeamAccessType().equals("Management")) {
	            	qr += String.format(" and (act.draft=false OR act.draft is null) and act.approvalStatus in ('%s', '%s') ", APPROVED_STATUS, STARTED_APPROVED_STATUS);
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
	                qr += " or f.ampDonorOrgId in(" + relatedOrgs + ") or role.organisation in (" + relatedOrgs + ")";
	            }
	            qr += ")";

	        } else {
	            qr += String.format(" and (act.draft=false OR act.draft is null) and act.approvalStatus in ('%s', '%s') and act.team is not null ", APPROVED_STATUS, STARTED_APPROVED_STATUS);
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
    
    public static MapFilter getNewFilter(HttpServletRequest request){
    	MapFilter filter = new MapFilter();
    	//List<AmpOrgGroup> orgGroups = new ArrayList(DbUtil.getAllOrgGroups());
		//filter.setOrgGroups(orgGroups);
    	    	
    	filter.buildOrganizationsByOrgGroup();
		
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
    	
		List<OrganizationSkeleton> orgs = null;

		if (filter.getOrgGroupId() == null || filter.getOrgGroupId() == -1) {
			filter.setOrgGroupId(-1l);
		}
		if (filter.getSelLocationIds() == null) {
			Long[] locationIds={-1l};
			filter.setSelLocationIds(locationIds);
		}
		
		orgs = DbUtil.getDonorOrganisationByGroupId(filter.getOrgGroupId(), false); 
		filter.setOrganizations(orgs);
		
		List<AmpOrgType> orgtypes = new ArrayList<AmpOrgType>(DbUtil.getAllOrgTypes());
		filter.setOrganizationsType(orgtypes);
		
		//List<AmpSector> sectors = new ArrayList(org.digijava.module.visualization.util.DbUtil.getAllSectors());
		//filter.setSectors(sectors);
		
		try {
			if(filter.getSelSectorConfigId()==null){
					filter.setSelSectorConfigId(SectorUtil.getPrimaryConfigClassification().getId());
			}
			filter.setSectorConfigs(SectorUtil.getAllClassificationConfigs());
			filter.setConfigWithSectorAndSubSectors(new ArrayList<EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>>());
			List<AmpSector> sectors = org.digijava.module.visualization.util.DbUtil.getParentSectorsFromConfig(filter.getSelSectorConfigId());
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
		
		if (filter.getStartYear() == null) {
			long year = (long) Util.getCurrentFiscalYear();
			
			filter.setDefaultStartYear(year - 3);
			filter.setStartYear(year - 3);
			filter.setStartYearFilter(year - 3);
			filter.setEndYear(year);
			filter.setDefaultEndYear(year);
		}
		filter.setYears(new TreeMap<Integer, Integer>());
		int yearFrom = Integer
				.parseInt(FeaturesUtil
						.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
		int countYear = Integer
				.parseInt(FeaturesUtil
						.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
		long maxYear = yearFrom + countYear;
		if (maxYear < filter.getStartYear()) {
			maxYear = filter.getStartYear();
		}
		for (int i = yearFrom; i <= maxYear; i++) {
			filter.getYears().put(i, i);
		}
		

		Collection calendars = DbUtil.getAllFisCalenders();
		if (calendars != null) {
			filter.setFiscalCalendars(new ArrayList(calendars));
		}
		String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (value != null) {
			Long fisCalId = Long.parseLong(value);
			filter.setFiscalCalendarId(fisCalId);
			filter.setDefaultFiscalCalendarId(fisCalId);
		}
		
		if (filter.getLargestProjectNumber() == null) {
			filter.setLargestProjectNumber(10);
		}
		if (filter.getDivideThousands() == null) {
			filter.setDivideThousands(false);
		}
		if (filter.getDivideThousandsDecimalPlaces() == null) {
			filter.setDivideThousandsDecimalPlaces(0);
		}
		if (filter.getRegions() == null) {
			try {
				filter.setRegions(new ArrayList<AmpCategoryValueLocations>(DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry()));
			} catch (Exception e) {
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
				e.printStackTrace();
			}

		}
		filter.setZones(zones);
        filter.setCurrencies(CurrencyUtil.getUsableCurrencies());
        filter.setPeacebuildingMarkers(DbHelper.getPeacebuildingMarkers());
        if (filter.getPeacebuildingMarkers() != null &&
                !filter.getPeacebuildingMarkers().isEmpty()) {
            filter.setSelectedPeacebuildingMarkerId(filter.getPeacebuildingMarkers().get(0).getId());
        }
        if (filter.getProgramElements() == null) {
        	try {
				filter.setProgramElements(initializePrograms());
			} catch (DgException e) {
				logger.error ("Exception trying to get a new filter",e);
			}
        }

        filter.setIsinitialized(true);
		return filter;
    	
    }
    
    public static String getPercentage(BigDecimal base, BigDecimal pct){
        return FormatHelper.formatNumber(base.multiply(pct).divide(ONEHUNDERT).doubleValue()); 
    }
    public static boolean inArray(Object comparableObject, Object[] objects){
    	for(Object oC : objects){
    		if(oC.equals(comparableObject)) return true;
    	}
    	return false;
    }
    
    public static Map<String,AmpTheme> initializePrograms () throws DgException {
	    Map<String,AmpTheme> programLevelMap = new HashMap <String,AmpTheme> ();
	    List<AmpTheme> allPrograms	= ProgramUtil.getAllThemes(true);
        HashMap<Long, AmpTheme> progMap		= ProgramUtil.prepareStructure(allPrograms);
        
		AmpActivityProgramSettings primaryPrgSetting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
		AmpTheme primaryProg = null;
		if (primaryPrgSetting!=null && primaryPrgSetting.getDefaultHierarchy() != null) {
			primaryProg = progMap.get(primaryPrgSetting.getDefaultHierarchyId() );
			HierarchyListableUtil.changeTranslateable(primaryProg, false);
			programLevelMap.put("selectedPrimaryPrograms", primaryProg);
		}
		AmpTheme secondaryProg = null;
 	 	AmpActivityProgramSettings secondaryPrg = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM);
		if (secondaryPrg!=null && secondaryPrg.getDefaultHierarchy() != null) {
			secondaryProg	= progMap.get(secondaryPrg.getDefaultHierarchyId() );
			HierarchyListableUtil.changeTranslateable(secondaryProg, false);
			programLevelMap.put("selectedSecondaryPrograms", secondaryProg);
					
		}	 	
		AmpActivityProgramSettings natPlanSetting       = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
 	 	if (natPlanSetting != null && natPlanSetting.getDefaultHierarchy() != null) {
 	 		AmpTheme nationalPlanningProg	= progMap.get(natPlanSetting.getDefaultHierarchyId() );
 	 		HierarchyListableUtil.changeTranslateable(nationalPlanningProg, false);
 	 		programLevelMap.put("selectedNatPlanObj", nationalPlanningProg);
 			
				
		}
 	 	return programLevelMap;
	
}


}
