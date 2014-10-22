/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
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
	protected static final Logger logger = Logger.getLogger(EndpointUtils.class);

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
	
	/**
	 * Generates a report based on a given specification
	 * @param spec - report specification
	 * @return GeneratedReport will info
	 */
	public static GeneratedReport runReport(ReportSpecification spec) {
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()));
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			logger.error(e);
		}
		return report;
	}
	
	/**
	 * Retrieves first
	 * @param formParams
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getSingleValue(MultivaluedMap<String, String> formParams, String key, String defaultValue) {
		if (formParams.containsKey(key))
			return formParams.getFirst(key);
		return defaultValue;
	}
}
