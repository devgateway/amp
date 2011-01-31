package org.digijava.module.visualization.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.visualization.form.VisualizationForm;

import fi.joensuu.joyds1.calendar.EthiopicCalendar;
import fi.joensuu.joyds1.calendar.NepaliCalendar;

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
	
	public static Map<AmpActivity, BigDecimal> getRankActivities (Collection<AmpActivity> actList){
		Map<AmpActivity, BigDecimal> map = new HashMap<AmpActivity, BigDecimal>();
		List<AmpFundingDetail> fundDetailList = new ArrayList<AmpFundingDetail>();
		for (Iterator iterator = actList.iterator(); iterator.hasNext();) {
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
	
	public static Map<AmpCategoryValueLocations, BigDecimal> getRankRegions (Collection<AmpLocation> regionsList, Collection<AmpActivity> actList){
		Map<AmpCategoryValueLocations, BigDecimal> map = new HashMap<AmpCategoryValueLocations, BigDecimal>();
		Collection<AmpFundingDetail> fundDetList = new ArrayList();
		for (Iterator iterator = regionsList.iterator(); iterator.hasNext();) {
			AmpLocation location = (AmpLocation) iterator.next();
			for (Iterator iterator2 = actList.iterator(); iterator2.hasNext();) {
				AmpActivity ampActivity = (AmpActivity) iterator2.next();
				Collection<AmpLocation> locs = ampActivity.getLocations();
				for (Iterator iterator3 = locs.iterator(); iterator3.hasNext();) {
					AmpActivityLocation ampActivityLocation = (AmpActivityLocation) iterator3.next();
					AmpLocation ampLocation = ampActivityLocation.getLocation();
					if (ampLocation.getLocation().getId().equals(location.getLocation().getId())
							|| (ampLocation.getLocation().getParentLocation()!=null && ampLocation.getLocation().getParentLocation().getId().equals(location.getLocation().getId()))
							|| (ampLocation.getLocation().getParentLocation()!=null && ampLocation.getLocation().getParentLocation().getParentLocation() !=null && ampLocation.getLocation().getParentLocation().getParentLocation().getId().equals(location.getLocation().getId()))) {
						Float percent = ampActivityLocation.getLocationPercentage();
						Collection<AmpFunding> fund = ampActivity.getFunding();
						for (Iterator iterator4 = fund.iterator(); iterator4.hasNext();) {
							AmpFunding ampFunding = (AmpFunding) iterator4.next();
							Collection<AmpFundingDetail> funDet = ampFunding.getFundingDetails();
							for (Iterator iterator5 = funDet.iterator(); iterator5.hasNext();) {
								AmpFundingDetail ampFundingDetail = (AmpFundingDetail) iterator5.next();
								ampFundingDetail.setTransactionAmount(ampFundingDetail.getTransactionAmount()*percent/100);
								fundDetList.add(ampFundingDetail);
							}
						}
					}
				}
			}
			FundingCalculationsHelper cal = new FundingCalculationsHelper();
	        cal.doCalculations(fundDetList, "USD");
	        BigDecimal total = cal.getTotActualDisb().getValue().setScale(3, RoundingMode.HALF_UP);
	        map.put(location.getLocation(), total);
		}
		return sortByValue (map);
	}
	
	public static Map<AmpSector, BigDecimal> getRankSectors (Collection<AmpSector> sectorsList, Collection<AmpActivity> actList){
		Map<AmpSector, BigDecimal> map = new HashMap<AmpSector, BigDecimal>();
		Collection<AmpFundingDetail> fundDetList = new ArrayList();
		for (Iterator iterator = sectorsList.iterator(); iterator.hasNext();) {
			AmpSector sector = (AmpSector) iterator.next();
			for (Iterator iterator2 = actList.iterator(); iterator2.hasNext();) {
				AmpActivity ampActivity = (AmpActivity) iterator2.next();
				Collection<AmpActivitySector> secs = ampActivity.getSectors();
				for (Iterator iterator3 = secs.iterator(); iterator3.hasNext();) {
					AmpActivitySector ampActSector = (AmpActivitySector) iterator3.next();
					AmpSector ampSector = ampActSector.getSectorId();
					if (ampSector.getAmpSectorId().equals(sector.getAmpSectorId()) 
							|| (ampSector.getParentSectorId()!=null && ampSector.getParentSectorId().getAmpSectorId().equals(sector.getAmpSectorId()))
							|| (ampSector.getParentSectorId()!=null && ampSector.getParentSectorId().getParentSectorId()!=null && ampSector.getParentSectorId().getParentSectorId().getAmpSectorId().equals(sector.getAmpSectorId()))){
						Float percent = ampActSector.getSectorPercentage(); 
						Collection<AmpFunding> fund = ampActivity.getFunding();
						for (Iterator iterator4 = fund.iterator(); iterator4.hasNext();) {
							AmpFunding ampFunding = (AmpFunding) iterator4.next();
							Collection<AmpFundingDetail> funDet = ampFunding.getFundingDetails();
							for (Iterator iterator5 = funDet.iterator(); iterator5.hasNext();) {
								AmpFundingDetail ampFundingDetail = (AmpFundingDetail) iterator5.next();
								ampFundingDetail.setTransactionAmount(ampFundingDetail.getTransactionAmount()*percent/100);
								fundDetList.add(ampFundingDetail);
							}
						}
					}
				}
				
			}
			FundingCalculationsHelper cal = new FundingCalculationsHelper();
	        cal.doCalculations(fundDetList, "USD");
	        BigDecimal total = cal.getTotActualDisb().getValue().setScale(3, RoundingMode.HALF_UP);
	        map.put(sector, total);
	        fundDetList.removeAll(fundDetList);
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
	
	public static void getSummaryAndRankInformation (VisualizationForm form){
		Collection<AmpActivity> activityList = DbUtil.getActivitiesUsingFilter(form);
		Collection<AmpSector> sectorList = DbUtil.getSectorsUsedInActivities(Util.toCSString(activityList));
		Collection<AmpLocation> regionList = DbUtil.getRegionsUsedInActivities(Util.toCSString(activityList));
		
		Collection<AmpFundingDetail> fundDetailList = new ArrayList<AmpFundingDetail>(); //DbUtil.getAllFundingsDetails();
		for (Iterator iterator = activityList.iterator(); iterator.hasNext();) {
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
		}
		FundingCalculationsHelper cal = new FundingCalculationsHelper();
		cal.doCalculations(fundDetailList, "USD");
		if (activityList.size()>0) {
			form.getSummaryInformation().setTotalCommitments(cal.getTotActualComm().getValue().setScale(3, RoundingMode.HALF_UP));
			form.getSummaryInformation().setTotalDisbursements(cal.getTotActualDisb().getValue().setScale(3, RoundingMode.HALF_UP));
			form.getSummaryInformation().setNumberOfProjects(activityList.size());
			form.getSummaryInformation().setNumberOfSectors(sectorList.size());
			form.getSummaryInformation().setNumberOfRegions(regionList.size());
			form.getSummaryInformation().setAverageProjectSize((cal.getTotActualComm().getValue().divide(new BigDecimal(activityList.size()), 3, RoundingMode.HALF_UP)).setScale(3, RoundingMode.HALF_UP));
			form.getRanksInformation().setFullSectors(getRankSectors(sectorList, activityList));
			form.getRanksInformation().setFullRegions(getRankRegions(regionList, activityList));
			form.getRanksInformation().setFullProjects(getRankActivities(activityList));
		} else {
			form.getSummaryInformation().setTotalCommitments(new BigDecimal(0));
			form.getSummaryInformation().setTotalDisbursements(new BigDecimal(0));
			form.getSummaryInformation().setNumberOfProjects(0);
			form.getSummaryInformation().setNumberOfSectors(0);
			form.getSummaryInformation().setNumberOfRegions(0);
			form.getSummaryInformation().setAverageProjectSize(new BigDecimal(0));
			form.getRanksInformation().setFullSectors(null);
			form.getRanksInformation().setFullRegions(null);
			form.getRanksInformation().setFullProjects(null);
		}
		
		
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

    public static String getOrganizationQuery(boolean orgGroupView, List<AmpOrganisation> selectedOrganizations) {
        String qry = "";
        if (orgGroupView) {
            qry = " and  f.ampDonorOrgId.orgGrpId.ampOrgGrpId=:orgGroupId ";
        } else {
            qry = " and f.ampDonorOrgId in (" + getInStatementList(selectedOrganizations) + ") ";
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
    }}
