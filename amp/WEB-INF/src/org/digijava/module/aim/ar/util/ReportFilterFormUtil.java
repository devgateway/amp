package org.digijava.module.aim.ar.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;

public final class ReportFilterFormUtil {
	
	private static Logger logger 		= Logger.getLogger(ReportWizardAction.class);

	public static Integer getDefaultStartYear() {
		AmpApplicationSettings tempSettings = getAppSetting();
		Integer rStart = -1;
		if (tempSettings != null && tempSettings.getReportStartYear() != null && tempSettings.getReportStartYear().intValue() != 0) {
			rStart = tempSettings.getReportStartYear();
		} else {
			String gvalue = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
			if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
				rStart = Integer.parseInt(gvalue);
			}
		}

		return rStart;
	}
	
	public static Integer getDefaultEndYear() {
		AmpApplicationSettings tempSettings = getAppSetting();
		Integer rEnd = -1;
		if (tempSettings != null && tempSettings.getReportEndYear() != null && tempSettings.getReportEndYear().intValue() != 0) {
			rEnd = tempSettings.getReportEndYear();
		} else {
			String gvalue = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
			if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
				rEnd = Integer.parseInt(gvalue);
			}
		}
		return rEnd;
	}	
	
	public static AmpApplicationSettings getAppSetting() {
		HttpSession httpSession = TLSUtils.getRequest().getSession();
		TeamMember teamMember = (TeamMember) httpSession.getAttribute(Constants.CURRENT_MEMBER);
		AmpApplicationSettings tempSettings = null;
		if (teamMember != null) {
			tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
		}
		return tempSettings;
	}
	
}
