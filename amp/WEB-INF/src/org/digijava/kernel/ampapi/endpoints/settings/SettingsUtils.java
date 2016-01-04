/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.settings;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.reports.mondrian.ReportSettingsImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.CalendarUtil;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.h2.util.StringUtils;

/**
 * Utility class for amp settings handling
 * @author Nadejda Mandrescu
 */
public class SettingsUtils {
	
	protected static final Logger logger = Logger.getLogger(SettingsUtils.class);

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
				defaultId, options, true);
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
				defaultId, options, true);
	}
	
	/**
	 * 
	 * @param measureToDisplayName
	 * @return
	 */
	public static SettingOptions getFundingTypeSettings(Set<String> measures) {
		//identifies the default funding type
		String defaultId = SettingsConstants.DEFAULT_FUNDING_TYPE_ID;				
		//AMP-20157: We need to check if the default funding type (usually Actual Commitments) is in the list of available active options.
		boolean found = false;
				
		//build funding type options
		List<SettingOptions.Option> options = new ArrayList<SettingOptions.Option>();
		for (String measure : measures) {
			SettingOptions.Option fundingTypeOption = new SettingOptions.Option(
					measure, measure, true);
			options.add(fundingTypeOption);
			if (measure.equalsIgnoreCase(defaultId)) {
				found = true;
			}
		}		
		if (!found) {
			if(options.size() > 0) {
				defaultId = options.get(0).name;
			}
		}
		
		return new SettingOptions(SettingsConstants.FUNDING_TYPE_ID, true,
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.FUNDING_TYPE_ID),
				defaultId, options, true);
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
			selectedStartYearId = EndpointUtils.getDefaultReportStartYear();
			selectedEndYearId = EndpointUtils.getDefaultReportEndYear();
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
		
		return new SettingOptions(null, options);
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
				defaults.options, false);
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
		final String selectedAmountUnits = String.valueOf(spec.getSettings().getUnitsOption().multiplier);
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
				new SettingOptions(selectedId, options));
	}
	
	/**
	 * Settings that can be reused by modules that rely upon Gis Originated settings UI panel.
	 *      
	 * @return list of GIS settings
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public static List<SettingOptions> getGisSettings() throws NumberFormatException, Exception {
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

		settings.add(new SettingOptions("use-icons-for-sectors-in-project-list",
				GisConstants.USE_ICONS_FOR_SECTORS_IN_PROJECT_LIST, new SettingOptions.Option(Boolean.toString(FeaturesUtil
						.isVisibleFeature(GisConstants.USE_ICONS_FOR_SECTORS_IN_PROJECT_LIST)))));

		settings.add(new SettingOptions("max-locations-icons", GlobalSettingsConstants.MAX_LOCATIONS_ICONS,
				new SettingOptions.Option(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MAX_LOCATIONS_ICONS))));

		settings.add(new SettingOptions("number-format", GlobalSettingsConstants.NUMBER_FORMAT,
				new SettingOptions.Option("number-format", MondrianReportUtils.getCurrentUserDefaultSettings()
						.getCurrencyFormat().toPattern(), false)));

		settings.add(new SettingOptions("number-group-separator", GlobalSettingsConstants.GROUP_SEPARATOR,
				new SettingOptions.Option(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR))));

		settings.add(new SettingOptions(
				"number-decimal-separator",
				GlobalSettingsConstants.DECIMAL_SEPARATOR,
				new SettingOptions.Option(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR))));

		settings.add(new SettingOptions("number-multiplier", GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS,
				new SettingOptions.Option(String.valueOf(AmountsUnits.getDefaultValue().multiplier))));

		settings.add(new SettingOptions("language", "language", new SettingOptions.Option(TLSUtils
				.getEffectiveLangCode())));

		// Workspace Settings
		if (tm != null) {
			settings.add(new SettingOptions("team-id", "team-id", new SettingOptions.Option(EndpointUtils
					.getAppSettings().getTeam().getAmpTeamId().toString())));
			settings.add(new SettingOptions("team-lead", "team-lead", new SettingOptions.Option(String.valueOf(tm
					.getTeamHead()))));
			settings.add(new SettingOptions("team-validator", "team-validator", new SettingOptions.Option(String
					.valueOf(tm.isApprover()))));
			// Cross Team validation
			settings.add(new SettingOptions("cross_team_validation", "cross_team_validation",
					new SettingOptions.Option(String.valueOf(EndpointUtils.getAppSettings().getTeam()
							.getCrossteamvalidation()))));
			settings.add(new SettingOptions("workspace_type", "workspace_type", new SettingOptions.Option(String
					.valueOf(EndpointUtils.getAppSettings().getTeam().getAccessType()))));
		}

		// Dashboard / GIS specific date range settings

		long defaultCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
		AmpFiscalCalendar gsFiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(defaultCalendarId);
		AmpFiscalCalendar currentCalendar = AmpARFilter.getDefaultCalendar();

		addDateSetting(settings, GlobalSettingsConstants.DASHBOARD_DEFAULT_MAX_YEAR_RANGE,
				"dashboard-default-max-date", "dashboard-default-max-year-range", 
				defaultCalendarId, gsFiscalCalendar, currentCalendar, true);
		addDateSetting(settings, GlobalSettingsConstants.DASHBOARD_DEFAULT_MIN_YEAR_RANGE,
				"dashboard-default-min-date", "dashboard-default-min-year-range", 
				defaultCalendarId, gsFiscalCalendar, currentCalendar, false);
		addDateSetting(settings, GlobalSettingsConstants.GIS_DEFAUL_MAX_YEAR_RANGE, "gis-default-max-date",
				"gis-default-max-year-range", defaultCalendarId, gsFiscalCalendar, currentCalendar, true);
		addDateSetting(settings, GlobalSettingsConstants.GIS_DEFAUL_MIN_YEAR_RANGE, "gis-default-min-date",
				"gis-default-min-year-range", defaultCalendarId, gsFiscalCalendar, currentCalendar, false);

		return settings;
	}
	
	protected static void addDateSetting(List<SettingOptions> settings, String globalSettingsName,
			String dateSettingsName, String yearSettingsName,
			long calendarId, AmpFiscalCalendar gsCalendar, AmpFiscalCalendar currentCalendar,
			boolean yearEnd) throws Exception {
		
		String yearNumber = FeaturesUtil.getGlobalSettingValue(globalSettingsName);
		settings.add(new SettingOptions(yearSettingsName, false, yearNumber, null, null, false));

		if (!yearNumber.equals("-1")) {
			int yearDelta = yearEnd ? 1 : 0;
			int daysDelta = yearEnd ? -1 : 0;
			Date gsDate = FiscalCalendarUtil.toGregorianDate(gsCalendar, Integer.parseInt(yearNumber) + yearDelta, 
					daysDelta); 
			
			/*
			 * uncomment when filter picker support for other calendars will be available
			Date date = FiscalCalendarUtil.convertDate(gsCalendar, gsDate, currentCalendar);
			*/
			Date date = gsDate;
				
			settings.add(new SettingOptions(dateSettingsName, false, DateTimeUtil.parseDateForPicker2(date, Constants.CALENDAR_DATE_PICKER), null, null, true));
		}
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
	 * Applies common settings and other custom settings (e.g. funding type) 
	 * 
	 * @param spec report specification 
	 * @param config request configuration that stores the settings  
	 */
	public static void applyExtendedSettings(ReportSpecificationImpl spec, JsonBean config) {
		// apply first common settings, i.e. calendar and currency
		applySettings(spec, config, true);
		
		// now apply custom settings, i.e. selected measures
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
					measureOptions.addAll((List<String>) fundingTypes);
			}
		}
		if (measureOptions.size() > 0) {
			for (String measure : measureOptions)
				spec.addMeasure(new ReportMeasure(measure));
		} else {
			spec.addMeasure(new ReportMeasure(SettingsConstants.DEFAULT_FUNDING_TYPE_ID));
		}
	}
		
	/**
	 * Configures report specification with settings provided via Json
	 * 
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
	 * 
	 * @param spec report specification
	 * @param config json object that stores the settings
	 * @param setDefaults if true, then not specified settings will be configured with defaults
	 * 
	 * @see SettingsUtils#applySettings(ReportSpecificationImpl, JsonBean)
	 */
	public static void applySettings(ReportSpecificationImpl spec, JsonBean config, boolean setDefaults) {
		if (spec.getSettings() != null && !ReportSettingsImpl.class.isAssignableFrom(spec.getSettings().getClass())) {
			logger.error("Unsupported conversion for: " + spec.getSettings().getClass());
			return;
		}
		
		ReportSettingsImpl reportSettings = (ReportSettingsImpl) spec.getSettings();
		if (reportSettings == null) {
			reportSettings = new ReportSettingsImpl();
			spec.setSettings(reportSettings);
		}
		
		// these are the settings provided via json
		Map<String, Object> settings = (Map<String, Object>) config.get(EPConstants.SETTINGS);
		
		configureCurrencyCode(reportSettings, settings, setDefaults);
		configureNumberFormat(reportSettings, settings, setDefaults);
		configureCalendar(reportSettings, settings, setDefaults);
		configureYearRange(reportSettings, settings, setDefaults);
	}
	
	/**
	 * 
	 * @param reportSettings
	 * @param settings
	 * @param setDefaults
	 */
	private static void configureCurrencyCode(ReportSettingsImpl reportSettings, Map<String, Object> settings, 
			boolean setDefaults) {
		String currency = settings == null ? null : (String) settings.get(SettingsConstants.CURRENCY_ID);
		if (currency != null)
			reportSettings.setCurrencyCode(currency);
		
		if (setDefaults && reportSettings.getCurrencyCode() == null)
			reportSettings.setCurrencyCode(EndpointUtils.getDefaultCurrencyCode());
	}
	
	/**
	 * 
	 * @param reportSettings
	 * @param settings
	 * @param setDefaults
	 */
	private static void configureNumberFormat(ReportSettingsImpl reportSettings, Map<String, Object> settings, 
			boolean setDefaults) {
		// apply numberFormat
		Map<String, Object> amountFormat = settings == null ? null : 
			(Map<String, Object>) settings.get(SettingsConstants.AMOUNT_FORMAT_ID);
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
			
			Double multiplier = (Double) amountFormat.get(SettingsConstants.AMOUNT_UNITS);
			if (multiplier != null)
				reportSettings.setUnitsOption(AmountsUnits.findByMultiplier(multiplier));
		}
		
		if (setDefaults && reportSettings.getCurrencyFormat() == null) {
			reportSettings.setCurrencyFormat(EndpointUtils.getDecimalSymbols());
		}
	}
	
	/**
	 * 
	 * @param reportSettings
	 * @param settings
	 * @param setDefaults
	 */
	private static void configureCalendar(ReportSettingsImpl reportSettings, Map<String, Object> settings, 
			boolean setDefaults) {
		String calendarId = settings == null ? null : String.valueOf(settings.get(SettingsConstants.CALENDAR_TYPE_ID));
		if (settings != null && StringUtils.isNumber(calendarId)) {
			reportSettings.setOldCalendar(reportSettings.getCalendar());
			reportSettings.setCalendar(DbUtil.getAmpFiscalCalendar(Long.valueOf(calendarId)));
		}
		
		if (setDefaults && reportSettings.getCalendar() == null)
			reportSettings.setCalendar(DbUtil.getAmpFiscalCalendar(Long.valueOf(EndpointUtils.getDefaultCalendarId())));
	}
	
	/**
	 * Configures year range setting
	 * @param reportSettings
	 * @param settings
	 * @param setDefaults: if true AND there is no range setting in @reportSettings, then reportSettings will be populated with the workspace/system's default 
	 */
	public static void configureYearRange(ReportSettingsImpl reportSettings, Map<String, Object> settings, boolean setDefaults) {
		ReportElement yearRangeElement = new ReportElement(ElementType.YEAR); 
		boolean preExistingYearRangeSetting = (reportSettings.getFilterRules().get(yearRangeElement) != null) && 
				(!reportSettings.getFilterRules().get(yearRangeElement).isEmpty());
		
		// TODO: once year range settings will be configurable through UI / API, then always re-apply
		if (preExistingYearRangeSetting && !setDefaults && (reportSettings.getOldCalendar() == null ||
				reportSettings.getCalendar().getAmpFiscalCalId().equals(
						reportSettings.getOldCalendar().getAmpFiscalCalId())))
			return;
		
		// apply year range settings
		Integer start = null;
		Integer end = null;
		if (settings!= null && settings.get(SettingsConstants.YEAR_RANGE_ID) != null) {
			Map<String, Object> yearRange = (Map<String, Object>) settings.get(SettingsConstants.YEAR_RANGE_ID);
			start = Integer.valueOf((String) yearRange.get(SettingsConstants.YEAR_FROM));
			end = Integer.valueOf((String) yearRange.get(SettingsConstants.YEAR_TO));
		} else {
			start = AmpARFilter.getDefaultStartYear(reportSettings.getCalendar());
			end = AmpARFilter.getDefaultEndYear(reportSettings.getCalendar());
		}
		
		// clear previous year settings
		reportSettings.getFilterRules().remove(yearRangeElement);
		reportSettings.setOldCalendar(null);
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
