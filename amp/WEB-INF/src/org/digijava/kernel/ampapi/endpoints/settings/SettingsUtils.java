package org.digijava.kernel.ampapi.endpoints.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.currency.ConstantCurrency;
import org.dgfoundation.amp.menu.AmpView;
import org.dgfoundation.amp.menu.MenuUtils;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.h2.util.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for amp settings handling
 * 
 * @author Nadejda Mandrescu
 */
public class SettingsUtils {
	
	protected static final Logger logger = Logger.getLogger(SettingsUtils.class);

	/**
	 * @return general currency settings
	 */
	private static SettingOptions getCurrencySettings() {
		//build currency options
		List<SettingOptions.Option> options = new ArrayList<>();
		for (AmpCurrency ampCurrency : CurrencyUtil.getActiveAmpCurrencyByName(true)) {
			String ccValue = ampCurrency.isVirtual() ?
					ConstantCurrency.retrieveCCCurrencyCodeWithoutCalendar(ampCurrency.getCurrencyCode()) :
                    ampCurrency.getCurrencyCode();
			SettingOptions.Option currencyOption = new SettingOptions.Option(
                    ampCurrency.getCurrencyCode(), ampCurrency.getCurrencyName(), ccValue);
			options.add(currencyOption);
		}
		//identifies the base currency 
		String defaultId = EndpointUtils.getDefaultCurrencyCode();
		
		return new SettingOptions(defaultId, options);
	}
	
	/**
	 * @return general calendar settings
	 */
	private static SettingOptions getCalendarSettings() {
		//build calendar options
		List<SettingOptions.Option> options = new ArrayList<>();
		for (AmpFiscalCalendar ampCalendar : DbUtil.getAllFisCalenders()) {
			SettingOptions.Option calendarOption = new SettingOptions.Option(
					String.valueOf(ampCalendar.getAmpFiscalCalId()),
					ampCalendar.getName(), true);
			options.add(calendarOption);
		}
		//identifies the default calendar 
		String defaultId = EndpointUtils.getDefaultCalendarId();
		
		return new SettingOptions(defaultId, options);
	}
	
	/**
	 * @return currency allowed options per calendar
	 */
	private static SettingOptions getCalendarCurrencySettings() {
		List<SettingOptions.Option> options = new ArrayList<>();
		String standardCurrencies = getCurrencyCodes(CurrencyUtil.getActiveAmpCurrencyByName(false));
		for (AmpFiscalCalendar ampCalendar : DbUtil.getAllFisCalenders()) {
			// get applicable currencies
			String calendarCurrencies = standardCurrencies + getCurrencyCodes(ampCalendar.getConstantCurrencies());
			if (calendarCurrencies.length() > 0)
				calendarCurrencies = calendarCurrencies.substring(0, calendarCurrencies.length() - 1);
			SettingOptions.Option calendarOption = new SettingOptions.Option(
					String.valueOf(ampCalendar.getAmpFiscalCalId()),
					ampCalendar.getName(), calendarCurrencies, true);
			options.add(calendarOption);
		}
		//identifies the default calendar 
		String defaultId = EndpointUtils.getDefaultCalendarId();
		
		return new SettingOptions(defaultId, options);
	}
	
	private static String getCurrencyCodes(Collection<AmpCurrency> currencies) {
		StringBuilder sb = new StringBuilder();
		for (AmpCurrency c : currencies) {
			sb.append(c.getCurrencyCode()).append(",");
		}
		return sb.toString();
	}
	
	/**
	 * @return options
	 */
	static SettingOptions getFundingTypeSettings() {
		Set<String> measures = new LinkedHashSet<>(GisConstants.FUNDING_TYPES);
		measures.retainAll(MeasuresVisibility.getConfigurableMeasures());

		//identifies the default funding type
		String defaultId = SettingsConstants.DEFAULT_FUNDING_TYPE_ID;				
		//AMP-20157: We need to check if the default funding type (usually Actual Commitments) is in the list of available active options.
		boolean found = false;
				
		//build funding type options
		List<SettingOptions.Option> options = new ArrayList<>();
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
		
		return new SettingOptions(true, defaultId, options);
	}
	
	/**
	 * Provides current report settings
	 * 
	 * @param spec report specification 
	 * @return settings in a structure to be used in UI, with all options
	 */
	public static Settings getReportSettings(ReportSpecification spec) {
		if (spec == null || spec.getSettings() == null)
			return null;
		
		Settings settings = new Settings();

		settings.setCurrencyCode(getReportCurrencyCode(spec));
		settings.setCalendarId(getReportCalendarId(spec));
		settings.setYearRange(getReportYearRange(spec));
		
		return settings;
	}

	private static String getReportCurrencyCode(ReportSpecification spec) {
		String selectedId = null;
		if (spec.getSettings() != null && spec.getSettings().getCurrencyCode() != null) {
			selectedId = spec.getSettings().getCurrencyCode();
		}
		return selectedId;
	}
	
	private static String getReportCalendarId(ReportSpecification spec) {
		String selectedId = null;
		if (spec.getSettings() != null && spec.getSettings().getCalendar() != null) {
			selectedId = spec.getSettings().getCalendar().getIdentifier().toString();
		}
		return selectedId;
	}
	
	private static Settings.YearRange getReportYearRange(ReportSpecification spec) {
		Settings.YearRange yearRange = null;

		if (spec.getSettings() != null && spec.getSettings().getYearRangeFilter() != null) {
			yearRange = new Settings.YearRange();
			yearRange.setFrom(getReportYear(spec.getSettings().getYearRangeFilter().min));
			yearRange.setTo(getReportYear(spec.getSettings().getYearRangeFilter().max));
		}

		return yearRange;
	}

	static SettingField getCalendarField() {
		return getSettingFieldForOptions(SettingsConstants.CALENDAR_TYPE_ID, getCalendarSettings());
	}

	static SettingField getCurrencyField() {
		return getSettingFieldForOptions(SettingsConstants.CURRENCY_ID, getCurrencySettings());
	}

	static SettingField getFundingTypeField() {
		return getSettingFieldForOptions(SettingsConstants.FUNDING_TYPE_ID, getFundingTypeSettings());
	}

	/**
	 * Return year range field using defaults.
	 *
	 * @return field that defines the year range in reports
	 */
	static SettingField getReportYearRangeField() {
		return getReportYearRangeField(null);
	}

	/**
	 * Return year range field taking in consideration report settings. If report settings are not specified then
	 * defaults are used.
	 *
	 * @param spec report specification used to select default values
	 * @return field that defines the year range in reports
	 */
	static SettingField getReportYearRangeField(ReportSpecification spec) {
		SettingOptions yearsOptions = getReportYearsOptions();

		Settings.YearRange range = getReportYearRange(spec);
		if (range == null) {
			range = new Settings.YearRange();
			range.setFrom(EndpointUtils.getDefaultReportStartYear());
			range.setTo(EndpointUtils.getDefaultReportEndYear());
		}

		List<SettingField> rangeFields = Arrays.asList(
				getSelectedOptions(range.getFrom(), yearsOptions, SettingsConstants.YEAR_FROM),
				getSelectedOptions(range.getTo(), yearsOptions, SettingsConstants.YEAR_TO));

		return new SettingField(SettingsConstants.YEAR_RANGE_ID, null,
				SettingsConstants.ID_NAME_MAP.get(SettingsConstants.YEAR_RANGE_ID), rangeFields);
	}
	
	private static String getReportYear(String year) {
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
	
	private static SettingField getSelectedOptions(String selectedId, SettingOptions defaults, String id) {
		/* configuring id & name to null, because they must be removed later on,
		 * when agreed with GIS to switch to a bit different structure provided by
		 * SettingFilter as a root
		 */
        String defaultId = selectedId == null ? defaults.defaultId : selectedId;
        SettingOptions actualOptions = new SettingOptions(defaults.multi, defaultId, defaults.options);
		return getSettingFieldForOptions(id, actualOptions);
	}

	private static SettingField getSettingFieldForOptions(String id, SettingOptions options) {
		String name = SettingsConstants.ID_NAME_MAP.get(id);
		return new SettingField(id, null, name, options);
	}
	
	/**
	 * Returns general settings.
     *
	 * @return general settings in <i>property: value</i> format
	 */
	public static JsonBean getGeneralSettings() {
		JsonBean settings = new JsonBean();

        settings.set("use-icons-for-sectors-in-project-list",
                FeaturesUtil.isVisibleFeature(GisConstants.USE_ICONS_FOR_SECTORS_IN_PROJECT_LIST));

		settings.set("project-sites", FeaturesUtil.isVisibleFeature(GisConstants.PROJECT_SITES));
		
		settings.set("max-locations-icons", FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.MAX_LOCATIONS_ICONS));

		settings.set("number-format", MondrianReportUtils.getCurrentUserDefaultSettings().getCurrencyFormat().toPattern());

		settings.set("number-group-separator", FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR));

		settings.set("number-decimal-separator", FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR));

		settings.set("number-divider", AmountsUnits.getDefaultValue().divider);

		settings.set("language", TLSUtils.getEffectiveLangCode());

        settings.set("default-language", TLSUtils.getSite().getDefaultLanguage().getCode());

        settings.set("multilingual", ContentTranslationUtil.multilingualIsEnabled());

		settings.set("default-date-format", FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT));

		settings.set("hide-editable-export-formats-public-view", !FeaturesUtil.isVisibleModule("Show Editable Export Formats"));

		settings.set("download-map-selector", FeaturesUtil.isVisibleFeature(GisConstants.DOWNLOAD_MAP_SELECTOR));

		settings.set("gap-analysis-map", FeaturesUtil.isVisibleFeature("Gap Analysis Map"));

		if (MenuUtils.getCurrentView() == AmpView.TEAM) {
            addWorkspaceSettings(settings);
		}

        addDateRangeSettingsForDashboardsAndGis(settings);

		return settings;
	}

    private static void addWorkspaceSettings(JsonBean settings) {
        TeamMember teamMember = getTeamMember();
        AmpTeam ampTeam = EndpointUtils.getAppSettings().getTeam();

        settings.set("team-id", ampTeam.getAmpTeamId().toString());

        settings.set("team-lead", teamMember.getTeamHead());

        settings.set("team-validator", teamMember.isApprover());

        settings.set("cross_team_validation", ampTeam.getCrossteamvalidation());

        settings.set("workspace_type", ampTeam.getAccessType());

        if (ampTeam.getWorkspacePrefix() != null) {
            settings.set("workspace-prefix", ampTeam.getWorkspacePrefix().getValue());
        }
    }

    @Nullable
    private static TeamMember getTeamMember() {
        HttpServletRequest request = TLSUtils.getRequest();
        TeamMember tm = null;
        if (request != null && request.getSession() != null) {
            tm = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
        }
        return tm;
    }

    private static void addDateRangeSettingsForDashboardsAndGis(JsonBean settings) {
        long defaultCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
        AmpFiscalCalendar gsFiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(defaultCalendarId);
        AmpFiscalCalendar currentCalendar = AmpARFilter.getDefaultCalendar();

        addDateSetting(settings, GlobalSettingsConstants.DASHBOARD_DEFAULT_MAX_YEAR_RANGE,
                "dashboard-default-max-date", "dashboard-default-max-year-range",
                gsFiscalCalendar, currentCalendar, true);
        addDateSetting(settings, GlobalSettingsConstants.DASHBOARD_DEFAULT_MIN_YEAR_RANGE,
                "dashboard-default-min-date", "dashboard-default-min-year-range",
                gsFiscalCalendar, currentCalendar, false);
        addDateSetting(settings, GlobalSettingsConstants.GIS_DEFAUL_MAX_YEAR_RANGE, "gis-default-max-date",
                "gis-default-max-year-range", gsFiscalCalendar, currentCalendar, true);
        addDateSetting(settings, GlobalSettingsConstants.GIS_DEFAUL_MIN_YEAR_RANGE, "gis-default-min-date",
                "gis-default-min-year-range", gsFiscalCalendar, currentCalendar, false);
    }

    private static void addDateSetting(JsonBean settings, String globalSettingsName,
			String dateSettingsName, String yearSettingsName, AmpFiscalCalendar gsCalendar,
            AmpFiscalCalendar currentCalendar, boolean yearEnd) {
		
		String yearNumber = FeaturesUtil.getGlobalSettingValue(globalSettingsName);
		settings.set(yearSettingsName, yearNumber);

		if (!StringUtils.equals(yearNumber, "-1")) {
			int yearDelta = yearEnd ? 1 : 0;
			int daysDelta = yearEnd ? -1 : 0;
			Date gsDate = FiscalCalendarUtil.toGregorianDate(gsCalendar, Integer.parseInt(yearNumber) + yearDelta, 
					daysDelta); 
			
			/*
			 * uncomment when filter picker support for other calendars will be available
			Date date = FiscalCalendarUtil.convertDate(gsCalendar, gsDate, currentCalendar);
			*/
			Date date = gsDate;
			settings.set(dateSettingsName, DateTimeUtil.formatDateForPicker2(date, Constants.CALENDAR_DATE_PICKER));
		}
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
	 * 		"funding-type" : [“Actual Commitments”, “Actual Disbursements”],
	 * 		"currency-code" : “USD”,
	 * 		"calendar-id" : “123”
	 *      "year-range" : {
	 *       		from : "all",
	 *       		to   : "2014"
	 *       }
	 * 	}
	 * }
	 * @param spec - report specification over which to apply the settings
	 * @param config - JSON request that includes the settings
	 * @param setDefaults if true, then not specified settings will be configured with defaults
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
	    // keep existing if no new settings are applied 
	    if (reportSettings.getYearRangeFilter() != null && settings == null)
	        return;
		
		// apply year range settings
		Integer start = null;
		Integer end = null;
		if (settings != null && settings.get(SettingsConstants.YEAR_RANGE_ID) != null) {
			Map<String, Object> yearRange = (Map<String, Object>) settings.get(SettingsConstants.YEAR_RANGE_ID);
            if (yearRange.get(SettingsConstants.YEAR_FROM)!=null)
                start = Integer.valueOf((String) yearRange.get(SettingsConstants.YEAR_FROM));
            if (yearRange.get(SettingsConstants.YEAR_TO)!=null)
                end = Integer.valueOf((String) yearRange.get(SettingsConstants.YEAR_TO));
		} else {
			start = AmpARFilter.getDefaultStartYear(reportSettings.getCalendar());
			end = AmpARFilter.getDefaultEndYear(reportSettings.getCalendar());
		}
		
		// clear previous year settings
		reportSettings.setYearRangeFilter(null);
		reportSettings.setOldCalendar(null);
		// TODO: update settings to store [ALL, ALL] range just to reflect
		// the previous selection
		if (!(start == -1 && end == -1)) {
			try {
				start = start == -1 ? null : start;
				end = end == -1 ? null : end;
				reportSettings.setYearsRangeFilterRule(start, end);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
}
