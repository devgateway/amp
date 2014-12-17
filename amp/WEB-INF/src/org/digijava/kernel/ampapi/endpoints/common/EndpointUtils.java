/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.ColumnsVisibility;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportSettings;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingField;
import org.digijava.kernel.ampapi.endpoints.settings.SettingOptions;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.ampapi.endpoints.util.GisUtil;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
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
	 * @return report default START year selection
	 */
	public static String getDefaultReportStartYear() {
		AmpApplicationSettings appSettings = getAppSettings();
		if(appSettings != null && appSettings.getReportStartYear()!=null && appSettings.getReportStartYear() > 0)
			return String.valueOf(appSettings.getReportStartYear());
		return FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
	} 
	
	/**
	 * @return report default END year selection
	 */
	public static String getDefaultReportEndYear() {
		AmpApplicationSettings appSettings = getAppSettings();
		if(appSettings != null &&appSettings.getReportEndYear()!=null&& appSettings.getReportEndYear() > 0)
			return String.valueOf(appSettings.getReportEndYear());
		return FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
	}
	
	/**
	 * Generates a report based on a given specification
	 * 
	 * @param spec - report specification
	 * @return GeneratedReport that stores all report info and report output
	 */
	public static GeneratedReport runReport(ReportSpecification spec) {
		return runReport(spec, ReportAreaImpl.class);
	}
	
	/**
	 * Generates a report based on a given specification and wraps the output
	 * into the specified class
	 * 
	 * @param spec report specification
	 * @param clazz any class that extends {@link ReportAreaImpl}
	 * @return GeneratedReport that stores all report info and report output
	 */
	public static GeneratedReport runReport(ReportSpecification spec, Class<? extends ReportAreaImpl> clazz) {
		MondrianReportGenerator generator = new MondrianReportGenerator(clazz, 
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
	public static SettingOptions getFundingTypeSettings(Set<String> measures) {
		//build funding type options
		List<SettingOptions.Option> options = new ArrayList<SettingOptions.Option>();
		for (String measure : measures) {
			SettingOptions.Option fundingTypeOption = new SettingOptions.Option(
					measure, measure, true);
			options.add(fundingTypeOption);
		}
		//identifies the default funding type
		String defaultId = SettingsConstants.DEFAULT_FUNDING_TYPE_ID;
		
		return new SettingOptions(SettingsConstants.FUNDING_TYPE_ID, true,
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.FUNDING_TYPE_ID),
				defaultId, options);
	}
	
	/**
	 * Provides current report settings
	 * 
	 * @param spec report specification 
	 * @return settings in a structure to be used in UI, with all options
	 */
	public static List<SettingField> getReportSettings(ReportSpecification spec) {
		if (spec == null || spec.getSettings() == null)
			return null;
		
		List<SettingField> settings = new ArrayList<SettingField>();
		
		settings.add(getReportAmountFormat(spec));
		settings.add(getReportAmountPattern(spec));
		settings.add(getReportCurrency(spec));
		settings.add(getReportCalendar(spec));
		settings.add(getReportYearRange(spec));
		
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
	
	/**
	 * Year range setting
	 * 
	 * @param spec
	 * @return
	 */
	private static SettingField getReportYearRange(ReportSpecification spec) {
		// build the list of years
		SettingOptions yearsOptions = getReportYearsOptions();
		String selectedStartYearId = null;
		String selectedEndYearId = null;
		
		if (spec.getSettings() != null && spec.getSettings().getFilterRules() != null
				&& spec.getSettings().getFilterRules() != null
				&& spec.getSettings().getFilterRules().containsKey(new ReportElement(ElementType.YEAR))) {
			// not sure if the plan to use multiple year range settings is still valid AMP-17715
			for (FilterRule filter : spec.getSettings().getFilterRules().get(new ReportElement(ElementType.YEAR))) {
				selectedStartYearId = getSelectedYearId(filter.min); 
				selectedEndYearId = getSelectedYearId(filter.max);
				// now 1 range
				break;
			}
		} else {
			selectedStartYearId = getDefaultReportStartYear();
			selectedEndYearId = getDefaultReportEndYear();
		}
		
		List<SettingField> range = Arrays.asList(
				getSelectedOptions(selectedStartYearId, yearsOptions, SettingsConstants.YEAR_FROM),
				getSelectedOptions(selectedEndYearId, yearsOptions, SettingsConstants.YEAR_TO));
		
		return new SettingField(SettingsConstants.YEAR_RANGE_ID, null, 
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.YEAR_RANGE_ID), range);
	}
	
	private static String getSelectedYearId(String year) {
		if (year == null || MoConstants.FILTER_UNDEFINED_MAX.equals(year))
			return SettingsConstants.YEAR_ALL;
		return year;
	}
	
	/**
	 * @return report default year list options
	 */
	private static SettingOptions getReportYearsOptions() {
		// build year  options
		List<SettingOptions.Option> options = new ArrayList<SettingOptions.Option>();
		Long optionRangeStart = FeaturesUtil.getGlobalSettingValueLong(Constants.GlobalSettings.YEAR_RANGE_START);
		Long optionRangeEnd = optionRangeStart +  
				FeaturesUtil.getGlobalSettingValueLong(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE);
		
		// add "All" option
		SettingOptions.Option yearOption = new SettingOptions.Option(
				SettingsConstants.YEAR_ALL, 
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.YEAR_ALL),
				SettingsConstants.YEAR_MAP.get(SettingsConstants.YEAR_ALL),
				true);
		options.add(yearOption);
		
		// add actual years list to select from  
		for (long year =  optionRangeStart; year <= optionRangeEnd; year++ ) {
			final String yearStr = String.valueOf(year);
			yearOption = new SettingOptions.Option(yearStr, yearStr);
			options.add(yearOption);
		}
		
		return new SettingOptions(null, false, null, null, options);
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
	
	private static SettingField getReportAmountFormat(ReportSpecification spec) {
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
	
	private static SettingField getReportAmountPattern(ReportSpecification spec) {
		if (spec.getSettings() != null && spec.getSettings().getCurrencyFormat() != null) {
			return getAmountPatternSetting(
					spec.getSettings().getCurrencyFormat().toPattern());
		}
		return getDefaultAmountPattern();
		
	}
	
	private static SettingField getDefaultAmountPattern() {
		return getAmountPatternSetting(
				FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT));
	}
	
	private static SettingField getAmountPatternSetting(String pattern) {
		return new SettingField(SettingsConstants.AMOUNT_PATTERN_ID, null, 
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.AMOUNT_PATTERN_ID),
				pattern);
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
     *      "amountFormat" : {
     *              decimalSymbol : ".", 
     *              maxFracDigits : 2,
     *              useGrouping   : true,
     *              groupSeparator: " ",
     *              groupSize     : 3,
     *              amountUnits   : 0.001
     *             },
     *       "yearRange" : {
     *       		yearFrom : "all",
     *       		yearTo   : "2014"
     *       }
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
		
		Map<String, Object> settings = (Map<String, Object>) config.get(EPConstants.SETTINGS);
		MondrianReportSettings reportSettings = new MondrianReportSettings();
		if (settings != null) {
			reportSettings = (MondrianReportSettings) spec.getSettings();
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
				reportSettings.setCurrencyFormat((DecimalFormat) format.getInstance(new Locale("en", "US")));
				
				Double multiplier  = (Double) amountFormat.get(SettingsConstants.AMOUNT_UNITS);
				if (multiplier != null)
					reportSettings.setUnitsMultiplier(multiplier);
			}else{
				
				reportSettings.setCurrencyFormat(getCurrencySymbols());
				reportSettings.setCurrencyCode(reportSettings.getCurrencyFormat().getCurrency().getCurrencyCode());
				reportSettings.setCalendar(DbUtil.getAmpFiscalCalendar(Long.valueOf(getDefaultCalendarId())));
				ApplyYearRange(reportSettings, settings);
				spec.setSettings(reportSettings);
			}
			
			// apply year range settings
			ApplyYearRange(reportSettings, settings);
			
		} else {
			//Here we should set default setting for request without parameters.
			reportSettings.setCurrencyFormat(getCurrencySymbols());
			reportSettings.setCurrencyCode(reportSettings.getCurrencyFormat().getCurrency().getCurrencyCode());
			ApplyYearRange(reportSettings, settings);
			reportSettings.setCalendar(DbUtil.getAmpFiscalCalendar(Long.valueOf(getDefaultCalendarId())));
			spec.setSettings(reportSettings);
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
		
	/**
	 * Settings that can be reused by modules that rely upon Gis Originated settings UI panel.
	 *      
	 * @return list of GIS settings
	 */
	public static List<SettingOptions> getGisSettings() {
		HttpServletRequest request = TLSUtils.getRequest();
		TeamMember tm = null;
		if (request != null && request.getSession() != null) {
			tm = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
		}
		// retrieve common settings
		List<SettingOptions> settings = getSettings();
		// add GIS specific settings
		Set<String> measures = new LinkedHashSet<String>(GisConstants.FUNDING_TYPES);
		measures.retainAll(MondrianReportUtils.getConfigurableMeasures());
		settings.add(getFundingTypeSettings(measures));
		
		settings.add(new SettingOptions("number-format", false, 
				FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT), null, null));
		int amountOptionId = Integer.valueOf(
				FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));
		
		settings.add(new SettingOptions("number-multiplier", false, 
				String.valueOf(MondrianReportUtils.getAmountMultiplier(amountOptionId))
				, null, null));
		// Workspace Settings
		if (tm != null){ 
			settings.add(new SettingOptions("team-id", false,getAppSettings().getTeam().getAmpTeamId().toString(), null, null));
			settings.add(new SettingOptions("tean-lead", false, String.valueOf(tm.getTeamHead()), null, null));
			settings.add(new SettingOptions("team-validator", false, String.valueOf(tm.isApprover()), null, null));
			// Cross Team validation
			settings.add(new SettingOptions("cross_team_validation", false, String.valueOf(getAppSettings().getTeam().getCrossteamvalidation())
					, null, null));
			settings.add(new SettingOptions("workspace_type", false, String.valueOf(getAppSettings().getTeam().getAccessType())
					, null, null));
		}
		return settings;
	}
		
	/**
	 * Applies specific settings
	 * 
	 * @param spec report specification 
	 * @param config request configuration that stores the settings  
	 */
	public static void applyGeneralSettings(ReportSpecificationImpl spec, JsonBean config) {
		// apply first common settings, i.e. calendar and currency
		EndpointUtils.applySettings(spec, config);
		
		// now apply GIS custom settings, i.e. selected measures
		List<String> measureOptions = new ArrayList<String>();
		if (config.get(EPConstants.SETTINGS) != null) {
			Map<Integer, Object> settings = (Map<Integer, Object>) config.get(EPConstants.SETTINGS);
			Object fundingTypes = settings.get(SettingsConstants.FUNDING_TYPE_ID);
			if (fundingTypes != null) {
				if (fundingTypes instanceof String)
					measureOptions.add((String)fundingTypes);
				// initial requirements was to use multiple funding type options => keeping it just in case it will be needed
				// remove if it will be confirmed over time that is not required
				else if (fundingTypes instanceof List)
					measureOptions.addAll((List<String>)fundingTypes);
			}
		}
		if (measureOptions.size() > 0) {
			for (String measure : measureOptions)
				spec.addMeasure(new ReportMeasure(measure));
		} else {
			// apply adjustment type selection
			Map<String, Object> settings = (Map<String, Object>) config.get(EPConstants.SETTINGS);
			String adjType = (String) settings.get(SettingsConstants.ADJUSTMENT_TYPE_ID);
			if (adjType != null && !adjType.equals("")) {
				switch (adjType.toUpperCase()) {
				case "AC":
					spec.addMeasure(new ReportMeasure(MoConstants.ACTUAL_COMMITMENTS));
					break;
				case "AD":
					spec.addMeasure(new ReportMeasure(MoConstants.ACTUAL_DISBURSEMENTS));
					break;
				case "PC":
					spec.addMeasure(new ReportMeasure(MoConstants.PLANNED_COMMITMENTS));
					break;
				case "PD":
					spec.addMeasure(new ReportMeasure(MoConstants.PLANNED_DISBURSEMENTS));
					break;
				default:
					spec.addMeasure(new ReportMeasure(MoConstants.ACTUAL_COMMITMENTS));
					break;
				}
			} else {
				spec.addMeasure(new ReportMeasure(SettingsConstants.DEFAULT_FUNDING_TYPE_ID));
			}			
		}
	}

	/**
	 * 
	 * @param className
	 * @return
	 */
	public static List<AvailableMethod> getAvailableMethods(String className){
		List<AvailableMethod> availableFilters=new ArrayList<AvailableMethod>(); 
		try {
			Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();
			Class<?> c = Class.forName(className);
			javax.ws.rs.Path p=c.getAnnotation(javax.ws.rs.Path.class);
			String path="/rest/"+p.value();
			Member[] mbrs=c.getMethods();
			for (Member mbr : mbrs) {
				ApiMethod apiAnnotation=
		    			((Method) mbr).getAnnotation(ApiMethod.class);
				if (apiAnnotation != null) {
					final String column = apiAnnotation.column();
					if (EPConstants.NA.equals(column) || visibleColumns.contains(column)) {
						//then we have to add it to the filters list
						javax.ws.rs.Path methodPath = ((Method) mbr).getAnnotation(javax.ws.rs.Path.class);
						AvailableMethod filter = new AvailableMethod();
						//the name should be translatable
						if(apiAnnotation.name()!=null && !apiAnnotation.name().equals("")){
							filter.setName(TranslatorWorker.translateText(apiAnnotation.name()));
						}
						
						String endpoint = "/rest/" + p.value();
						
						if (methodPath != null){
							endpoint += methodPath.value();
						}
						filter.setEndpoint(endpoint);
						filter.setUi(apiAnnotation.ui());
						filter.setId(apiAnnotation.id());
						//we check the method exposed
						if (((Method) mbr).getAnnotation(javax.ws.rs.POST.class) != null){
							filter.setMethod("POST");
						} else {
							if (((Method) mbr).getAnnotation(javax.ws.rs.GET.class) != null){
								filter.setMethod("GET");
							} else {
								if (((Method) mbr).getAnnotation(javax.ws.rs.PUT.class) != null){
									filter.setMethod("PUT");
								} else {
									if (((Method) mbr).getAnnotation(javax.ws.rs.DELETE.class) != null){
										filter.setMethod("DELETE");
									}
								}
							}
						}
						availableFilters.add(filter);
					}
				}
			}
		}
		 catch (ClassNotFoundException e) {
			GisUtil.logger.error("cannot retrieve filters list",e);
			return null;
		}
		return availableFilters;
	}
	
	
	/**
	 * Set locale to US in order to avoid number formating issues
	 * @return
	 */
	
	public static DecimalFormat getCurrencySymbols(){
		Locale locale  = new Locale("en", "US");
		String pattern = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
		FormatHelper.getDecimalFormat();
		DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
		decimalFormat.applyPattern(pattern);
		return decimalFormat;
	}
	
	/**
	 * 
	 * @param reportSettings
	 * @param settings
	 */
	public static void ApplyYearRange(MondrianReportSettings reportSettings, Map<String, Object> settings) {
		// apply year range settings
		Integer start = 0;
		Integer end = 0;
		if (settings!= null && settings.get(SettingsConstants.YEAR_RANGE_ID) != null) {
			Map<String, Object> yearRange = (Map<String, Object>) settings.get(SettingsConstants.YEAR_RANGE_ID);
			start = Integer.valueOf((String) yearRange.get(SettingsConstants.YEAR_FROM));
			end = Integer.valueOf((String) yearRange.get(SettingsConstants.YEAR_TO));
		} else {
			start = new Integer(getDefaultReportStartYear());
			end = new Integer(getDefaultReportEndYear());
		}
			// clear previous year settings
			reportSettings.getFilterRules().remove(new ReportElement(ElementType.YEAR));
			// TODO: update settings to store [ALL, ALL] range just to reflect
			// the previous selection
			if (!(start == -1 && end == -1)) {
				try {
					start = start == -1 ? null : start;
					end = end == -1 ? null : end;
					reportSettings.addYearsRangeFilterRule(start, end);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		
	}
}
