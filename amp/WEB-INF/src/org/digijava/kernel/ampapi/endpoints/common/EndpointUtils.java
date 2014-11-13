/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportSettings;
import org.digijava.kernel.ampapi.endpoints.settings.SettingField;
import org.digijava.kernel.ampapi.endpoints.settings.SettingOptions;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.util.GisUtil;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.esrigis.dbentity.AmpApiState;
import org.h2.util.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

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
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, 
				ReportEnvironment.buildFor(TLSUtils.getRequest()));
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			logger.error("error running report", e);
		}
		return report;
	}
	
	/**
	 * Retrieves the value associated to the specified key if available 
	 * or returns the default
	 * 
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
			SettingOptions.Option currencyOption = new SettingOptions.Option(
					ampCurrency.getCurrencyCode(), ampCurrency.getCurrencyName());
			options.add(currencyOption);
		}
		//identifies the base currency 
		String defaultId = EndpointUtils.getDefaultCurrencyCode();
		
		return new SettingOptions(SettingsConstants.CURRENCY_ID, false,
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.CURRENCY_ID), 
				defaultId, options);
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
		
		return new SettingOptions(SettingsConstants.CALENDAR_TYPE_ID, false,
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.CALENDAR_TYPE_ID),
				defaultId, options);
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
		String defaultId = SettingsConstants.DEFAULT_FUNDING_TYPE_ID;
		
		return new SettingOptions(SettingsConstants.FUNDING_TYPE_ID, true,
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.FUNDING_TYPE_ID),
				defaultId, options);
	}
	
	public static List<SettingField> getReportSettings(ReportSpecification spec) {
		if (spec == null || spec.getSettings() == null)
			return null;
		
		List<SettingField> settings = new ArrayList<SettingField>();
		
		settings.add(getReportNumberFormat(spec));
		settings.add(getReportCurrency(spec));
		settings.add(getReportCalendar(spec));
		//settings.add()
		
		return settings;
	}
	
	private static SettingField getReportCalendar(ReportSpecification spec) {
		String selectedId = null;
		if (spec.getSettings() != null && spec.getSettings().getCalendar() != null)
			selectedId = spec.getSettings().getCalendar().getIdentifier().toString();
		
		return getSelectedOptions(selectedId, getCalendarSettings(), 
				SettingsConstants.CALENDAR_TYPE_ID);
	}
	
	private static SettingField getReportCurrency(ReportSpecification spec) {
		String selectedId = null;
		if (spec.getSettings() != null && spec.getSettings().getCurrencyCode() != null)
			selectedId = spec.getSettings().getCurrencyCode();
		
		return getSelectedOptions(selectedId, getCurrencySettings(), 
				SettingsConstants.CURRENCY_ID);
	}
	
	private static SettingField getSelectedOptions(String selectedId, 
			SettingOptions defaults, String id) {
		/* configuring id & name to null, because they must be removed later on,
		 * when agreed with GIS to switch to a bit different structure provided by 
		 * SettingFilter as a root 
		 */
		SettingOptions actualOptions = new SettingOptions(null, 
				defaults.multi, null,
				(selectedId == null ? defaults.defaultId : selectedId), 
				defaults.options);
		return new SettingField(id, null, SettingsConstants.ID_NAME_MAP.get(id) , actualOptions);
	}
	
	private static SettingField getReportNumberFormat(ReportSpecification spec) {
		DecimalFormat format = null; 
		if (spec.getSettings() != null && spec.getSettings().getCurrencyFormat() != null)
			format = spec.getSettings().getCurrencyFormat();
		else
			format = FormatHelper.getDefaultFormat();
		final List<SettingField> formatFields = new ArrayList<SettingField>();
		
		// decimal separators
		final String selectedDecimalSeparator = String.valueOf(format.getDecimalFormatSymbols().getDecimalSeparator());
		formatFields.add(getOptionValueSetting(SettingsConstants.DECIMAL_SYMBOL, null, selectedDecimalSeparator,
				SettingsConstants.DECIMAL_SEPARATOR_MAP));
				
		// maximum fraction digits
		final String selectedMaxFarctDigits = String.valueOf(format.getMaximumFractionDigits());
		formatFields.add(getOptionValueSetting(SettingsConstants.MAX_FRACT_DIGITS, null, selectedMaxFarctDigits,
				SettingsConstants.MAX_FRACT_DIGITS_MAP));
		
		// is grouping used
		formatFields.add(new SettingField(SettingsConstants.USE_GROUPING, null, 
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.USE_GROUPING), format.isGroupingUsed()));
		
		// grouping separator
		final String selectedGroupSeparator = String.valueOf(format.getDecimalFormatSymbols().getGroupingSeparator());
		formatFields.add(getOptionValueSetting(SettingsConstants.GROUP_SEPARATOR, 
				SettingsConstants.USE_GROUPING, selectedGroupSeparator,
				SettingsConstants.GROUP_SEPARATOR_MAP));
		
		// group size
		formatFields.add(new SettingField(SettingsConstants.GROUP_SIZE, 
				SettingsConstants.USE_GROUPING, 
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.GROUP_SIZE), format.getGroupingSize()));
		
		// amount units
		final String selectedAmountUnits = String.valueOf(spec.getSettings().getUnitsMultiplier());
		formatFields.add(getOptionValueSetting(SettingsConstants.AMOUNT_UNITS, 
				SettingsConstants.USE_GROUPING, selectedAmountUnits,
				SettingsConstants.AMOUNT_UNITS_MAP));
		
		return new SettingField(SettingsConstants.AMOUNT_FORMAT_ID, null, 
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.AMOUNT_FORMAT_ID),
				formatFields);
	}
	
	private static SettingField getOptionValueSetting(final String settingId, final String groupId, 
			final String selectedValue, final Map<String, String> idValueUnmodifiable) {
		
		final List<SettingOptions.Option> options = new ArrayList<SettingOptions.Option>();
		final Map<String, String> idValue = new LinkedHashMap<String, String>(idValueUnmodifiable);
		String selectedId = null;
		
		if (idValue.containsKey(SettingsConstants.CUSTOM) && !idValue.values().contains(selectedValue))
			idValue.put(SettingsConstants.CUSTOM, selectedValue);
		
		for (Entry<String, String> entry : idValue.entrySet()) {
			if (entry.getValue().equals(selectedValue))
				selectedId = entry.getKey();
			final String name = SettingsConstants.ID_NAME_MAP.get(entry.getKey()); 
			options.add(new SettingOptions.Option(
					entry.getKey(), 
					name == null ? entry.getValue() : name, 
					entry.getValue(), 
					name == null ? false : true));
		}
		
		if (selectedId == null)
			selectedId = idValue.entrySet().iterator().next().getKey();
		
		return new SettingField(settingId, groupId, SettingsConstants.ID_NAME_MAP.get(settingId), 
				new SettingOptions(null, false, null, selectedId, options));
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
	 * “settings” :  { // fields selected options or specified values
     * 		"0" : [“Actual Commitments”, “Actual Disbursements”],
     * 		"1" : “USD”,
     * 		"2" : “123”
     *      "3" : {
     *              decimalSymbol : ".", 
     *              maxFracDigits : 2,
     *              useGrouping   : true,
     *              groupSeparator: " ",
     *              groupSize     : 3,
     *              amountUnits   : 0.001
     *             }
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
			
			// apply currency option
			String currency = (String) settings.get(SettingsConstants.CURRENCY_ID);
			if (currency != null)
				reportSettings.setCurrencyCode(currency);
			
			// apply calendar option
			String calendarId = String.valueOf(settings.get(SettingsConstants.CALENDAR_TYPE_ID));
			if (StringUtils.isNumber(calendarId))
				reportSettings.setCalendar(DbUtil.getAmpFiscalCalendar(Long.valueOf(calendarId)));
			
			// apply numberFormat
			Map<String, Object> amountFormat = (Map<String, Object>)settings.get(SettingsConstants.AMOUNT_FORMAT_ID);
			if (amountFormat != null) {
				String decimalSymbol = (String) amountFormat.get(SettingsConstants.DECIMAL_SYMBOL);
				String maxFractDigits = (String) amountFormat.get(SettingsConstants.MAX_FRACT_DIGITS);
				Integer maxFractDigitsNum  = StringUtils.isNumber(maxFractDigits) ? Integer.valueOf(maxFractDigits) : null;
				Boolean useGrouping  = (Boolean) amountFormat.get(SettingsConstants.DECIMAL_SYMBOL);
				String groupingSeparator  = (String) amountFormat.get(SettingsConstants.GROUP_SEPARATOR);
				Integer groupingSize  = (Integer) amountFormat.get(SettingsConstants.GROUP_SIZE);
				
				DecimalFormat format = AmpARFilter.buildCustomFormat(decimalSymbol, groupingSeparator, 
						maxFractDigitsNum, useGrouping, groupingSize);
				reportSettings.setCurrencyFormat(format);
				
				Double multiplier  = (Double) amountFormat.get(SettingsConstants.AMOUNT_UNITS);
				if (multiplier != null)
					reportSettings.setUnitsMultiplier(multiplier);
			}
		}
	}
	public static List<JsonBean> getApiStateList(String type) {
		List<JsonBean> maps = new ArrayList<JsonBean>();

		try {
			List<AmpApiState> l = QueryUtil.getMapList(type);
			for (AmpApiState map : l) {
				maps.add(getJsonBeanFromApiState(map, Boolean.FALSE));
			}
			return maps;
		} catch (DgException e) {
			logger.error("Cannot get maps list", e);
			throw new WebApplicationException(e);
		}
	}
	private static JsonBean getJsonBeanFromApiState(AmpApiState map, Boolean getBlob) {
		JsonBean jMap = new JsonBean();

		jMap.set("id", map.getId());
		jMap.set("title", map.getTitle());
		jMap.set("description", map.getDescription());
		if (getBlob) {
			jMap.set("stateBlob", map.getStateBlob());
		}
		jMap.set("created", GisUtil.formatDate(map.getCreatedDate()));
		if(map.getLastAccesedDate()!=null){
			jMap.set("lastAccess", GisUtil.formatDate(map.getLastAccesedDate()));
		}
		return jMap;
	}	
	
	public static AmpApiState getSavedMap(Long mapId) throws AmpApiException {
		try {
			Session s = PersistenceManager.getRequestDBSession();
			AmpApiState map = (AmpApiState) s.load(AmpApiState.class, mapId);
			map.setLastAccesedDate(new Date());
			s.merge(map);
			return map;
		} catch (DgException ex) {
			throw new AmpApiException(ex);
		}
		
	}

	public static JsonBean getApiState(Long mapId) {
		JsonBean jMap = null;
		try {
			AmpApiState map = getSavedMap(mapId);
			jMap = getJsonBeanFromApiState(map, Boolean.TRUE);


		} catch (ObjectNotFoundException e) {
			jMap = new JsonBean();
		} catch (AmpApiException e) {
			logger.error("cannot get map by id " + mapId, e);
			throw new WebApplicationException(e);
		}
		return jMap;
	}	
	public static JsonBean saveApiState(final JsonBean pMap,String type) {
		Date creationDate = new Date();
		JsonBean mapId = new JsonBean();

		AmpApiState map = new AmpApiState();
		map.setTitle(pMap.getString("title"));
		map.setDescription(pMap.getString("description"));
		map.setStateBlob(pMap.getString("stateBlob"));
		map.setCreatedDate(creationDate);
		map.setUpdatedDate(creationDate);
		map.setLastAccesedDate(creationDate);
		map.setType(type);
		try {
			Session s = PersistenceManager.getRequestDBSession();
			s.save(map);
			s.flush();
			mapId.set("mapId", map.getId());
		} catch (DgException e) {
			logger.error("Cannot Save map", e);
			throw new WebApplicationException(e);
		}
		return mapId;
	}	
}
