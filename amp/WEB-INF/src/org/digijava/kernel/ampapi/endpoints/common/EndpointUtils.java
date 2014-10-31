/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportSettings;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.SettingOptions;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.h2.util.StringUtils;

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
	public static <T> T getSingleValue(JsonBean formParams, String key, T defaultValue) {
		if (formParams.get(key) != null)
			return (T) formParams.get(key);
		return defaultValue;
	}
	
	/**
	 * @return general currency settings
	 */
	public static SettingOptions getCurrencySettings() {
		//build currency options
		List<SettingOptions.Option> options = new ArrayList<SettingOptions.Option>();
		for (AmpCurrency ampCurrency : CurrencyUtil.getActiveAmpCurrencyByName()) {
			SettingOptions.Option currencyOption = new SettingOptions.Option(ampCurrency.getCurrencyCode(), ampCurrency.getCurrencyName());
			options.add(currencyOption);
		}
		//identifies the base currency 
		String defaultId = EndpointUtils.getDefaultCurrencyCode();
		
		return new SettingOptions(EPConstants.SETTINGS_CURRENCY_ID, false,
				EPConstants.SETTINGS_CURRENCY_NAME, defaultId, options);
	}
	
	/**
	 * @return general calendar settings
	 */
	public static SettingOptions getCalendarSettings() {
		//build calendar options
		List<SettingOptions.Option> options = new ArrayList<SettingOptions.Option>();
		for (AmpFiscalCalendar ampCalendar : DbUtil.getAllFisCalenders()) {
			SettingOptions.Option calendarOption = new SettingOptions.Option(
					String.valueOf(ampCalendar.getAmpFiscalCalId()),
					ampCalendar.getName(), true);
			options.add(calendarOption);
		}
		//identifies the default calendar 
		String defaultId = EndpointUtils.getDefaultCalendarId();
		
		return new SettingOptions(EPConstants.SETTINGS_CALENDAR_TYPE_ID, false,
				EPConstants.SETTINGS_CALENDAR_TYPE_NAME, defaultId, options);
	}
	
	/**
	 * 
	 * @param measureToDisplayName
	 * @return
	 */
	public static SettingOptions getFundingTypeSettings(Map<String, String> measureToDisplayName) {
		//build funding type options
		List<SettingOptions.Option> options = new ArrayList<SettingOptions.Option>();
		for (Entry<String, String> entry : measureToDisplayName.entrySet()) {
			SettingOptions.Option fundingTypeOption = new SettingOptions.Option(
					entry.getKey(), entry.getValue(), true);
			options.add(fundingTypeOption);
		}
		//identifies the default funding type
		String defaultId = EPConstants.SETTINGS_DEFAULT_FUNDING_TYPE_ID;
		
		return new SettingOptions(EPConstants.SETTINGS_FUNDING_TYPE_ID, true, 
				EPConstants.SETTINGS_FUNDING_TYPE_NAME, defaultId, options);
	}
	
	/**
	 * @return retrieves the default settings for currency and calendar 
	 */
	public static List<SettingOptions> getSettings() {
		List<SettingOptions> settings = new ArrayList<SettingOptions>();
		settings.add(getCurrencySettings());
		settings.add(getCalendarSettings());
		return settings;
	}
	
	/**
	 * Applies request settings, that are expected in the following format:
	 * config = {
	 * ...,
	 * “settings” :  {
   * 		"0" : [“Actual Commitments”, “Actual Disbursements”],
   * 		"1" : “USD”,
   * 		"2" : “123”
   * 	}
   * }
	 * @param spec - report specification over which to apply the settings
	 * @param config - JSON request that includes the settings 
	 */
	public static void applySettings(ReportSpecificationImpl spec, JsonBean config) {
		if (spec.getSettings() != null && !MondrianReportSettings.class.isAssignableFrom(spec.getSettings().getClass())) {
			logger.error("Unsupported conversion for: " + spec.getSettings().getClass());
			return;
		}
		
		Map<Integer, Object> settings = (Map<Integer, Object>) config.get(EPConstants.SETTINGS);
		if (settings != null) {
			MondrianReportSettings reportSettings = (MondrianReportSettings)spec.getSettings();
			if (reportSettings == null) {
				reportSettings = new MondrianReportSettings();
				spec.setSettings(reportSettings);
			}
			
			//apply currency option
			String currency = (String) settings.get(EPConstants.SETTINGS_CURRENCY_ID);
			if (currency != null)
				reportSettings.setCurrencyCode(currency);
			
			//apply calendar option
			String calendarId = String.valueOf(settings.get(EPConstants.SETTINGS_CALENDAR_TYPE_ID));
			if (StringUtils.isNumber(calendarId))
				reportSettings.setCalendar(DbUtil.getAmpFiscalCalendar(Long.valueOf(calendarId)));
		}
	}
}
