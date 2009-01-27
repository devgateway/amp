package org.digijava.module.mondrian.query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * 
 * @author Diego Dimunzio
 *
 */
public class MondrianQuery {
	protected static Logger logger = Logger.getLogger(MondrianQuery.class);

public static HttpServletRequest request;
	

	public static void setRequest(HttpServletRequest r){
		request = r;
	}
	
	public static String createQuery(){
		AmpARFilter filter = new AmpARFilter();
		TeamMember tm = (TeamMember) request.getSession().getAttribute(
				Constants.CURRENT_MEMBER);
		filter.setAmpTeams(new TreeSet());
		AmpApplicationSettings tempSettings = null;

		if (tm != null) {
			filter.setAccessType(tm.getTeamAccessType());
			filter.setAmpTeams(TeamUtil.getRelatedTeamsForMember(tm));
			Set teamAO = TeamUtil.getComputedOrgs(filter.getAmpTeams());

			if (teamAO != null && teamAO.size() > 0)
				filter.setTeamAssignedOrgs(teamAO);

			tempSettings = DbUtil.getMemberAppSettings(tm.getMemberId());

			if (tempSettings == null)
				if (tm != null)
					tempSettings = DbUtil.getTeamAppSettings(tm.getTeamId());

			if (filter.getCurrency() == null)
				filter.setCurrency(tempSettings.getCurrency());

		}

		if (filter.getCalendarType() == null) {
			if (tempSettings != null) {
				filter.setCalendarType(tempSettings.getFiscalCalendar());
			}
		}
		String gvalue = FeaturesUtil
				.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (filter.getCalendarType() == null) {
			if (gvalue != null) {
				Long fiscalCalId = Long.parseLong(gvalue);
				filter.setCalendarType(DbUtil.getFiscalCalendar(fiscalCalId));
			}
		}

		Long defaulCalenadarId = null;

		if (tempSettings != null) {
			if (tempSettings.getFiscalCalendar() != null) {
				defaulCalenadarId = tempSettings.getFiscalCalendar()
						.getAmpFiscalCalId();
			} else {
				defaulCalenadarId = Long
						.parseLong(FeaturesUtil
								.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));
			}
		}

		ICalendarWorker worker = null;
		Date checkDate = null;
		if (filter.getRenderStartYear() == null) {
			if (tempSettings != null
					&& tempSettings.getReportStartYear() != null
					&& tempSettings.getReportStartYear().intValue() != 0) {
				filter.setRenderStartYear(tempSettings.getReportStartYear());
			} else {
				gvalue = FeaturesUtil
						.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
				if (gvalue != null && !"".equalsIgnoreCase(gvalue)
						&& Integer.parseInt(gvalue) > 0) {
					filter.setRenderStartYear(Integer.parseInt(gvalue));
				}
			}

			if (filter.getRenderStartYear() != null
					&& filter.getCalendarType() != null
					&& defaulCalenadarId != filter.getCalendarType()
							.getAmpFiscalCalId()) {
				worker = filter.getCalendarType().getworker();
				try {
					checkDate = new SimpleDateFormat("dd/MM/yyyy")
							.parse("01/01/" + filter.getRenderStartYear());
					worker.setTime(checkDate);
					filter.setRenderStartYear(worker.getYear());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		filter.setRenderStartYear((filter.getRenderStartYear() == null) ? -1
				: filter.getRenderStartYear());

		if (filter.getRenderEndYear() == null) {
			if (tempSettings != null && tempSettings.getReportEndYear() != null
					&& tempSettings.getReportEndYear().intValue() != 0) {
				filter.setRenderEndYear(tempSettings.getReportEndYear());
			} else {
				gvalue = null;
				gvalue = FeaturesUtil
						.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
				if (gvalue != null && !"".equalsIgnoreCase(gvalue)
						&& Integer.parseInt(gvalue) > 0) {
					filter.setRenderEndYear(Integer.parseInt(gvalue));
				}
			}

			if (filter.getRenderEndYear() != null
					&& filter.getCalendarType() != null
					&& defaulCalenadarId != filter.getCalendarType()
							.getAmpFiscalCalId()) {
				worker = filter.getCalendarType().getworker();
				try {
					checkDate = new SimpleDateFormat("dd/MM/yyyy")
							.parse("01/01/" + filter.getRenderEndYear());
					worker.setTime(checkDate);
					filter.setRenderEndYear(worker.getYear());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		filter.setRenderEndYear((filter.getRenderEndYear() == null) ? -1
				: filter.getRenderEndYear());

		if (filter.getCurrentFormat() == null) {
			filter.setCurrentFormat(FormatHelper.getDefaultFormat());
			FormatHelper.tlocal.set(null);
		} else {
			FormatHelper.tlocal.set(filter.getCurrentFormat());
		}

		String widget = (String) request.getAttribute("widget");
		if (widget != null)
			filter.setWidget(new Boolean(widget).booleanValue());
		
		filter.generateFilterQuery(request);
		return (filter.getGeneratedFilterQuery());
	}

}
