package org.digijava.module.visualization.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.dbentity.AmpGraph;
import org.hibernate.Query;
import org.hibernate.Session;

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



    public static String getInStatement(ArrayList<?> ids) {
        return Util.toCSStringForIN(ids);
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

	public static AmpTheme getTopLevelProgram(AmpTheme program) {
		if (program.getParentThemeId() != null && program.getIndlevel()>1) {
			program = getTopLevelProgram(program.getParentThemeId());
		}
		return program;
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
	        double unitsMultiplier = 1d / AmountsUnits.getDefaultValue().divider; // this is to get the value in units because in AmpFundingDetail.java is being applied the multiplier
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
	
	public static TreeMap<Long, String> generateIdToNameForDashboards(Collection<AmpDashboard> dashboards) {
		TreeMap<Long, String> dashboardNames	= new TreeMap<Long, String>();
        if ( dashboards != null ) {
        	for (AmpDashboard ampDashboard : dashboards) {
        		dashboardNames.put(ampDashboard.getId(), ampDashboard.getName() );
			}
        }
        return dashboardNames;
	}
}
