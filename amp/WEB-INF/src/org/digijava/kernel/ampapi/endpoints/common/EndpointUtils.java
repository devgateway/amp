/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;

import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

/**
 * Common utility methods for all endpoints
 * @author Nadejda Mandrescu
 */
public class EndpointUtils {

	/**
	 * @return current user application settings 
	 */
	public static AmpApplicationSettings getAppSettings() {
		HttpServletRequest request = TLSUtils.getRequest();
		if (request != null) {
			TeamMember tm = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
			if (tm != null)
				return DbUtil.getTeamAppSettings(tm.getTeamId());
		}
		return null;
	}
	
	/**
	 * @return default currency code for public or logged in user
	 */
	public static String getDefaultCurrencyCode() {
		AmpApplicationSettings appSettings = getAppSettings();
		if(appSettings != null)
			return appSettings.getCurrency().getCurrencyCode();
		return CurrencyUtil.getDefaultCurrency().getCurrencyCode();
	}
	
	/**
	 * @return default calendar id for public or logged in user
	 */
	public static String getDefaultCalendarId() {
		AmpApplicationSettings appSettings = getAppSettings();
		if(appSettings != null)
			return String.valueOf(appSettings.getFiscalCalendar().getIdentifier());
		return String.valueOf(DbUtil.getBaseFiscalCalendar());
	}
}
