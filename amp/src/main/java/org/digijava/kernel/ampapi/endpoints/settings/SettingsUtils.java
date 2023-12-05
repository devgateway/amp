package org.digijava.kernel.ampapi.endpoints.settings;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.currency.ConstantCurrency;
import org.dgfoundation.amp.menu.AmpView;
import org.dgfoundation.amp.menu.MenuUtils;
import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.reports.ReportUtils;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.ampapi.endpoints.common.AmpGeneralSettings;
import org.digijava.kernel.ampapi.endpoints.common.CurrencySettings;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.util.*;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

import java.text.DecimalFormat;
import java.util.*;

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
    private static SettingOptions getCurrencySettings(boolean includeVirtual) {
        // build currency options
        List<SettingOptions.Option> options = new ArrayList<>();
        for (AmpCurrency ampCurrency : CurrencyUtil.getActiveAmpCurrencyByName(includeVirtual)) {
            String ccValue = ampCurrency.isVirtual()
                    ? ConstantCurrency.retrieveCCCurrencyCodeWithoutCalendar(ampCurrency.getCurrencyCode())
                    : ampCurrency.getCurrencyCode();
            SettingOptions.Option currencyOption = new SettingOptions.Option(ampCurrency.getCurrencyCode(),
                    ampCurrency.getCurrencyName(), ccValue);
            options.add(currencyOption);
        }
        // identifies the base currency
        String defaultId = EndpointUtils.getDefaultCurrencyCode();

        return new SettingOptions(defaultId, options);
    }

    /**
     * @return general calendar settings
     */
    private static SettingOptions getCalendarSettings() {
        // build calendar options
        List<SettingOptions.Option> options = new ArrayList<>();
        for (AmpFiscalCalendar ampCalendar : DbUtil.getAllFisCalenders()) {
            SettingOptions.Option calendarOption = new SettingOptions.Option(
                    String.valueOf(ampCalendar.getAmpFiscalCalId()), ampCalendar.getName(), true);
            options.add(calendarOption);
        }
        // identifies the default calendar
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
                    String.valueOf(ampCalendar.getAmpFiscalCalId()), ampCalendar.getName(), calendarCurrencies, true);
            options.add(calendarOption);
        }
        // identifies the default calendar
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
    static SettingOptions getFundingTypeSettings(Set<String> measures) {

        measures.retainAll(MeasuresVisibility.getConfigurableMeasures());

        // identifies the default funding type
        String defaultId = SettingsConstants.DEFAULT_FUNDING_TYPE_ID;
        // AMP-20157: We need to check if the default funding type (usually
        // Actual Commitments) is in the list of available active options.
        boolean found = false;

        // build funding type options
        List<SettingOptions.Option> options = new ArrayList<>();
        for (String measure : measures) {
            SettingOptions.Option fundingTypeOption = new SettingOptions.Option(measure, measure, true);
            options.add(fundingTypeOption);
            if (measure.equalsIgnoreCase(defaultId)) {
                found = true;
            }
        }
        if (!found) {
            if (options.size() > 0) {
                defaultId = options.get(0).id;
            }
        }

        return new SettingOptions(true, defaultId, options);
    }

    /**
     * Provides current report settings
     *
     * @param spec
     *            report specification
     * @return settings in a structure to be used in UI, with all options
     */
    public static Settings getReportSettings(ReportSpecification spec) {
        if (spec == null || spec.getSettings() == null) {
            return null;
        }

        return getReportSettings(spec.getSettings());
    }

    public static Settings getReportSettings(final ReportSettings reportSettings) {
        Settings settings = new Settings();

        settings.setCurrencyCode(getReportCurrencyCode(reportSettings));
        settings.setCalendarId(getReportCalendarId(reportSettings));
        settings.setYearRange(getReportYearRange(reportSettings));
        settings.setAmountFormat(getReportAmountFormat(reportSettings));

        return settings;
    }

    private static Settings.AmountFormat getReportAmountFormat(final ReportSettings settings) {
        DecimalFormat decimalFormat = settings.getCurrencyFormat();
        if (decimalFormat == null) {
            return null;
        }
        Settings.AmountFormat amountFormat = new Settings.AmountFormat();
        AmountsUnits unitsOption = settings.getUnitsOption();
        if (unitsOption != null) {
            amountFormat.setNumberDivider(unitsOption.divider);
        }
        amountFormat.setMaxFractionDigits(decimalFormat.getMaximumFractionDigits());
        amountFormat.setDecimalSymbol(decimalFormat.getDecimalFormatSymbols().getDecimalSeparator());
        amountFormat.setUseGrouping(decimalFormat.isGroupingUsed());
        amountFormat.setGroupSize(decimalFormat.getGroupingSize());
        amountFormat.setGroupSeparator(decimalFormat.getDecimalFormatSymbols().getGroupingSeparator());

        return amountFormat;
    }

    private static String getReportCurrencyCode(final ReportSettings settings) {
        if (settings.getCurrencyCode() != null) {
            return settings.getCurrencyCode();
        }

        return null;
    }

    private static String getReportCalendarId(final ReportSettings settings) {
        if (settings.getCurrencyCode() != null) {
            return settings.getCalendar().getIdentifier().toString();
        }

        return null;
    }

    private static SettingRange getReportYearRange(final ReportSettings settings) {
        if (settings.getYearRangeFilter() != null) {
            SettingRange yearRange = new SettingRange(SettingRange.Type.INT_VALUE);
            yearRange.setFrom(getReportYear(settings.getYearRangeFilter().min));
            yearRange.setTo(getReportYear(settings.getYearRangeFilter().max));
            yearRange.setRangeFrom(EndpointUtils.getRangeStartYear());
            yearRange.setRangeTo(EndpointUtils.getRangeEndYear());
            return yearRange;
        }

        return null;
    }

    static SettingField getCalendarCurrenciesField() {
        return getSettingFieldForOptions(SettingsConstants.CALENDAR_CURRENCIES_ID, getCalendarCurrencySettings());
    }

    static SettingField getCalendarField() {
        return getSettingFieldForOptions(SettingsConstants.CALENDAR_TYPE_ID, getCalendarSettings());
    }

    static SettingField getCurrencyField(boolean includeVirtual) {
        return getSettingFieldForOptions(SettingsConstants.CURRENCY_ID, getCurrencySettings(includeVirtual));
    }

    static SettingField getFundingTypeField(Set<String> measures) {
        return getSettingFieldForOptions(SettingsConstants.FUNDING_TYPE_ID, getFundingTypeSettings(measures));
    }

    static SettingField getReportAmountFormatField() {
        DecimalFormat format = FormatHelper.getDefaultFormat();
        final List<SettingField> formatFields = new ArrayList<>();

        // decimal separators
        final String selectedDecimalSeparator = String.valueOf(format.getDecimalFormatSymbols().getDecimalSeparator());

        formatFields.add(getOptionValueSetting(SettingsConstants.DECIMAL_SYMBOL, null, selectedDecimalSeparator,
                SettingsConstants.DECIMAL_SEPARATOR_MAP));

        // maximum fraction digits
        final String selectedMaxFarctDigits = String.valueOf(format.getMaximumFractionDigits());
        formatFields.add(getOptionValueSetting(SettingsConstants.MAX_FRACT_DIGITS, null, selectedMaxFarctDigits,
                SettingsConstants.MAX_FRACT_DIGITS_MAP));

        // is grouping used
        formatFields.add(SettingField.create(SettingsConstants.USE_GROUPING, null,
                SettingsConstants.ID_NAME_MAP.get(SettingsConstants.USE_GROUPING), format.isGroupingUsed()));

        // grouping separator
        final String selectedGroupSeparator = String.valueOf(format.getDecimalFormatSymbols().getGroupingSeparator());
        formatFields.add(getOptionValueSetting(SettingsConstants.GROUP_SEPARATOR, SettingsConstants.USE_GROUPING,
                selectedGroupSeparator, SettingsConstants.GROUP_SEPARATOR_MAP));

        // group size
        formatFields.add(SettingField.create(SettingsConstants.GROUP_SIZE, SettingsConstants.USE_GROUPING,
                SettingsConstants.ID_NAME_MAP.get(SettingsConstants.GROUP_SIZE), format.getGroupingSize()));

        // amount units
        final String selectedAmountUnits = String.valueOf(AmountsUnits.getDefaultValue().divider);
        formatFields.add(getOptionValueSetting(SettingsConstants.AMOUNT_UNITS, SettingsConstants.USE_GROUPING,
                selectedAmountUnits, SettingsConstants.AMOUNT_UNITS_MAP));

        return SettingField.create(SettingsConstants.AMOUNT_FORMAT_ID, null,
                SettingsConstants.ID_NAME_MAP.get(SettingsConstants.AMOUNT_FORMAT_ID), formatFields);
    }

    private static SettingField getOptionValueSetting(String settingId, String groupId, String selectedValue,
            Map<String, String> idValueUnmodifiable) {

        List<SettingOptions.Option> options = new ArrayList<>();
        Map<String, String> idValue = new LinkedHashMap<>(idValueUnmodifiable);
        String selectedId = null;

        if (idValue.containsKey(SettingsConstants.CUSTOM) && !idValue.values().contains(selectedValue)) {
            idValue.put(SettingsConstants.CUSTOM, selectedValue);
        }

        for (Map.Entry<String, String> entry : idValue.entrySet()) {
            if (entry.getValue().equals(selectedValue)) {
                selectedId = entry.getKey();
            }
            String name = SettingsConstants.ID_NAME_MAP.get(entry.getKey());
            String optionName = (name == null) ? entry.getValue() : name;
            options.add(new SettingOptions.Option(entry.getKey(), optionName, entry.getValue(), name != null));
        }

        if (selectedId == null) {
            selectedId = idValue.entrySet().iterator().next().getKey();
        }

        String settingName = SettingsConstants.ID_NAME_MAP.get(settingId);
        return SettingField.create(settingId, groupId, settingName, new SettingOptions(selectedId, options));
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
     * Return amount units field using defaults.
     *
     * @return field that defines the amount units in reports
     */
    static SettingField getReportAmountUnits() {
        final String defaultAmountUnit = String.valueOf(AmountsUnits.getDefaultValue().divider);

        return getOptionValueSetting(SettingsConstants.AMOUNT_UNITS, SettingsConstants.USE_GROUPING,
                defaultAmountUnit, SettingsConstants.AMOUNT_UNITS_MAP_REPORTS);
    }

    /**
     * Return year range field taking in consideration report settings. If
     * report settings are not specified then defaults are used.
     *
     * @param spec
     *            report specification used to select default values
     * @return field that defines the year range in reports
     */
    static SettingField getReportYearRangeField(ReportSpecification spec) {

        SettingRange range = spec != null && spec.getSettings() != null
                ? getReportYearRange(spec.getSettings()) : null;

        if (range == null) {
            range = new SettingRange(SettingRange.Type.INT_VALUE);
            range.setFrom(EndpointUtils.getDefaultReportStartYear());
            range.setTo(EndpointUtils.getDefaultReportEndYear());
            range.setRangeFrom(EndpointUtils.getRangeStartYear());
            range.setRangeTo(EndpointUtils.getRangeEndYear());
        }

        return SettingField.create(SettingsConstants.YEAR_RANGE_ID, null,
                SettingsConstants.ID_NAME_MAP.get(SettingsConstants.YEAR_RANGE_ID), range);
    }

    private static String getReportYear(String year) {
        if (year == null || FiltersConstants.FILTER_UNDEFINED_MAX.equals(year)) {
            return SettingsConstants.YEAR_ALL;
        }
        return year;
    }

    private static SettingField getSelectedOptions(String selectedId, SettingOptions defaults, String id) {
        /*
         * configuring id & name to null, because they must be removed later on,
         * when agreed with GIS to switch to a bit different structure provided
         * by SettingFilter as a root
         */
        String defaultId = selectedId == null ? defaults.defaultId : selectedId;
        SettingOptions actualOptions = new SettingOptions(defaults.multi, defaultId, defaults.options);
        return getSettingFieldForOptions(id, actualOptions);
    }

    private static SettingField getSettingFieldForOptions(String id, SettingOptions options) {
        String name = SettingsConstants.ID_NAME_MAP.get(id);
        return SettingField.create(id, null, name, options);
    }

    /**
     * Returns general settings.
     *
     * @return general settings object
     */
    public static AmpGeneralSettings getGeneralSettings() {
        AmpGeneralSettings settings = new AmpGeneralSettings();

        settings.setUseIconsForSectorsInProjectList(
                FeaturesUtil.isVisibleFeature(GisConstants.USE_ICONS_FOR_SECTORS_IN_PROJECT_LIST));

        settings.setProjectSites(FeaturesUtil.isVisibleFeature(GisConstants.PROJECT_SITES));

        settings.setMaxLocationsIcons(
                FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.MAX_LOCATIONS_ICONS));

        settings.setNumberFormat(ReportUtils.getCurrentUserDefaultSettings().getCurrencyFormat().toPattern());

        settings.setGsNumberFormat(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT));

        settings.setNumberGroupSeparator(
                FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR));

        settings.setNumberDecimalSeparator(
                FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR));

        settings.setNumberDivider(AmountsUnits.getDefaultValue().divider);
        settings.setAmountInThousands(AmountsUnits.getDefaultValue().code);

        settings.setLanguage(TLSUtils.getEffectiveLangCode());

        settings.setDefaultLanguage(TLSUtils.getSite().getDefaultLanguage().getCode());

        settings.setMultilingual(ContentTranslationUtil.multilingualIsEnabled());

        settings.setRtlDirection(SiteUtils.isEffectiveLangRTL());

        settings.setDefaultDateFormat(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT));

        settings.setHideEditableExportFormatsPublicView(!FeaturesUtil.showEditableExportFormats());

        settings.setDownloadMapSelector(FeaturesUtil.isVisibleFeature(GisConstants.DOWNLOAD_MAP_SELECTOR));

        settings.setGapAnalysisMap(FeaturesUtil.isVisibleFeature("Gap Analysis Map"));

        settings.setHasSscWorkspaces(!TeamUtil.getAllSSCWorkspaces().isEmpty());

        settings.setReorderFundingItemId(
                FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.REORDER_FUNDING_ITEMS));

        settings.setPublicVersionHistory(FeaturesUtil.isVisibleFeature("Version History"));

        settings.setPublicChangeSummary(FeaturesUtil.isVisibleField("Show Change Summary"));

        settings.setHideContactsPublicView(!FeaturesUtil.isVisibleFeature("Contacts"));
        AmpCurrency effCurrency = CurrencyUtil.getEffectiveCurrency();
        settings.setEffectiveCurrency(new CurrencySettings(effCurrency.getId(), effCurrency.getCurrencyCode()));

        settings.setNddMappingIndirectLevel(FeaturesUtil
                .getGlobalSettingValueInteger(GlobalSettingsConstants.MAPPING_INDIRECT_LEVEL));

        settings.setNddMappingProgramLevel(FeaturesUtil
                .getGlobalSettingValueInteger(GlobalSettingsConstants.MAPPING_PROGRAM_LEVEL));

        if (MenuUtils.getCurrentView() == AmpView.TEAM) {
            addWorkspaceSettings(settings);
        }
        addCalendarSettings(settings);

        if (TeamUtil.getCurrentUser() != null) {
            settings.setShowActivityWorkspaces(true);
        }
        addDateRangeSettingsForDashboardsAndGis(settings);

        return settings;
    }

    private static void addCalendarSettings(AmpGeneralSettings settings) {
        AmpFiscalCalendar ampFiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(FeaturesUtil.
                getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR));
        settings.setCalendarId(ampFiscalCalendar.getAmpFiscalCalId());
        settings.setCalendarIsFiscal(ampFiscalCalendar.getIsFiscal());
    }

    private static void addWorkspaceSettings(AmpGeneralSettings settings) {
        TeamMember teamMember = TeamUtil.getCurrentMember();
        AmpTeam ampTeam = EndpointUtils.getAppSettings().getTeam();

        settings.setTeamId(ampTeam.getAmpTeamId().toString());
        settings.setTeamLead(teamMember.getTeamHead());
        settings.setTeamValidator(teamMember.isApprover());
        settings.setCrossTeamValidation(ampTeam.getCrossteamvalidation());
        settings.setWorkspaceType(ampTeam.getAccessType());

        if (ampTeam.getWorkspacePrefix() != null) {
            settings.setWorkspacePrefix(ampTeam.getWorkspacePrefix().getValue());
        }

        AmpApplicationSettings appSettings = EndpointUtils.getAppSettings();
        if (appSettings != null) {
            settings.setWorkspaceDefaultRecordsPerPage(appSettings.getDefaultRecordsPerPage());
        }
    }

    private static void addDateRangeSettingsForDashboardsAndGis(AmpGeneralSettings settings) {
        long defaultCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
        AmpFiscalCalendar gsFiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(defaultCalendarId);
        AmpFiscalCalendar currentCalendar = AmpARFilter.getDefaultCalendar();

        addDateSetting(settings, GlobalSettingsConstants.DASHBOARD_DEFAULT_MAX_YEAR_RANGE,
                SettingsConstants.DASHBOARD_DEFAULT_MAX_DATE, SettingsConstants.DASHBOARD_DEFAULT_MAX_YEAR_RANGE,
                gsFiscalCalendar, currentCalendar, true);
        addDateSetting(settings, GlobalSettingsConstants.DASHBOARD_DEFAULT_MIN_YEAR_RANGE,
                SettingsConstants.DASHBOARD_DEFAULT_MIN_DATE, SettingsConstants.DASHBOARD_DEFAULT_MIN_YEAR_RANGE,
                gsFiscalCalendar, currentCalendar, false);
        addDateSetting(settings, GlobalSettingsConstants.GIS_DEFAUL_MAX_YEAR_RANGE,
                SettingsConstants.GIS_DEFAULT_MAX_DATE, SettingsConstants.GIS_DEFAULT_MAX_YEAR_RANGE,
                gsFiscalCalendar, currentCalendar, true);
        addDateSetting(settings, GlobalSettingsConstants.GIS_DEFAUL_MIN_YEAR_RANGE,
                SettingsConstants.GIS_DEFAULT_MIN_DATE, SettingsConstants.GIS_DEFAULT_MIN_YEAR_RANGE,
                gsFiscalCalendar, currentCalendar, false);
        addDateSetting(settings, Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE,
                SettingsConstants.REPORT_DEFAULT_MAX_DATE, SettingsConstants.REPORT_DEFAULT_MAX_DATE,
                gsFiscalCalendar, currentCalendar, true);
        addDateSetting(settings, Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE,
                SettingsConstants.REPORT_DEFAULT_MIN_DATE, SettingsConstants.REPORT_DEFAULT_MIN_DATE,
                gsFiscalCalendar, currentCalendar, false);

    }

    private static void addDateSetting(AmpGeneralSettings settings, String globalSettingsName, String dateSettingsName,
            String yearSettingsName, AmpFiscalCalendar gsCalendar, AmpFiscalCalendar currentCalendar, boolean yearEnd) {

        String yearNumber = FeaturesUtil.getGlobalSettingValue(globalSettingsName);

        if (yearSettingsName.equals(SettingsConstants.DASHBOARD_DEFAULT_MAX_YEAR_RANGE)) {
            settings.setDashboardDefaultMaxYearRange(yearNumber);
        } else if (yearSettingsName.equals(SettingsConstants.DASHBOARD_DEFAULT_MIN_YEAR_RANGE)) {
            settings.setDashboardDefaultMinYearRange(yearNumber);
        } else if (yearSettingsName.equals(SettingsConstants.GIS_DEFAULT_MAX_YEAR_RANGE)) {
            settings.setGisDefaultMaxYearRange(yearNumber);
        } else if (yearSettingsName.equals(SettingsConstants.GIS_DEFAULT_MIN_YEAR_RANGE)) {
            settings.setGisDefaultMinYearRange(yearNumber);
        } else if (yearSettingsName.equals(SettingsConstants.REPORT_DEFAULT_MAX_YEAR_RANGE)) {
            settings.setReportDefaultMaxYearRange(yearNumber);
        } else if (yearSettingsName.equals(SettingsConstants.REPORT_DEFAULT_MIN_YEAR_RANGE)) {
            settings.setReportDefaultMinYearRange(yearNumber);
        }

        if (!StringUtils.equals(yearNumber, "-1")) {
            int yearDelta = yearEnd ? 1 : 0;
            int daysDelta = yearEnd ? -1 : 0;
            Date gsDate = FiscalCalendarUtil.toGregorianDate(gsCalendar, Integer.parseInt(yearNumber) + yearDelta,
                    daysDelta);

            /*
             * uncomment when filter picker support for other calendars will be
             * available Date date = FiscalCalendarUtil.convertDate(gsCalendar,
             * gsDate, currentCalendar);
             */
            String formattedDate = DateTimeUtil.formatDateForPicker2(gsDate, Constants.CALENDAR_DATE_PICKER);

            if (dateSettingsName.equals(SettingsConstants.DASHBOARD_DEFAULT_MAX_DATE)) {
                settings.setDashboardDefaultMaxDate(formattedDate);
            } else if (dateSettingsName.equals(SettingsConstants.DASHBOARD_DEFAULT_MIN_DATE)) {
                settings.setDashboardDefaultMinDate(formattedDate);
            } else if (dateSettingsName.equals(SettingsConstants.GIS_DEFAULT_MAX_DATE)) {
                settings.setGisDefaultMaxDate(formattedDate);
            } else if (dateSettingsName.equals(SettingsConstants.GIS_DEFAULT_MIN_DATE)) {
                settings.setGisDefaultMinDate(formattedDate);
            } else if (dateSettingsName.equals(SettingsConstants.REPORT_DEFAULT_MAX_DATE)) {
                settings.setReportDefaultMaxDate(formattedDate);
            } else if (dateSettingsName.equals(SettingsConstants.REPORT_DEFAULT_MIN_DATE)) {
                settings.setReportDefaultMinDate(formattedDate);
            }
        }
    }

    /**
     * Adds measures to the report specification based on AMP-18874:
     * a) needs to have 'planned commitments' and  'planned disbursements'  if any planned setting is selected.
     * b) needs to have 'actual commitments' and  'actual disbursements'  if any actual setting is selected.
     * c) needs to have 'Bilateral SSC Commitments' and  'Triangular SSC Commitments' if any SSC setting is selected.
     *
     * @param spec
     * @param settings
     */
    public static void configureMeasures(final ReportSpecificationImpl spec, final Map<String, Object> settings) {
        if (spec != null) {
            String fundingType = (String) (settings == null ? null : settings.get(SettingsConstants.FUNDING_TYPE_ID));
            if (fundingType == null) {
                fundingType = SettingsUtils.getDefaultFundingType();
            }
            if (fundingType.startsWith("Actual")) {
                spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
                spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
            }
            if (fundingType.startsWith("Planned")) {
                spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_COMMITMENTS));
                spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
            }
            if (fundingType.contains("SSC")) {
                spec.addMeasure(new ReportMeasure(MeasureConstants.BILATERAL_SSC_COMMITMENTS));
                spec.addMeasure(new ReportMeasure(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS));
            }
        }
    }

    /**
     * Applies common settings and other custom settings (e.g. funding type)
     *
     * @param spec
     *            report specification
     * @param settings
     *            the settings
     */
    public static void applyExtendedSettings(ReportSpecificationImpl spec, Map<String, Object> settings) {
        // apply first common settings, i.e. calendar and currency
        applySettings(spec, settings, true);

        // now apply custom settings, i.e. selected measures
        List<String> measureOptions = new ArrayList<String>();
        if (settings != null) {
            Object fundingTypes = settings.get(SettingsConstants.FUNDING_TYPE_ID);
            if (fundingTypes != null) {
                if (fundingTypes instanceof String)
                    measureOptions.add((String) fundingTypes);
                // initial requirements was to use multiple funding type options
                // => keeping it just in case it will be needed
                // remove if it will be confirmed over time that is not required
                else if (fundingTypes instanceof List)
                    measureOptions.addAll((List<String>) fundingTypes);
            }
        }
        if (measureOptions.size() > 0) {
            for (String measure : measureOptions)
                spec.addMeasure(new ReportMeasure(measure));
        } else {
            spec.addMeasure(new ReportMeasure(SettingsUtils.getDefaultFundingType()));
        }
    }

    /**
     * Return the default Funding Type
     *
     * @return default Funding Type
     */
    public static String getDefaultFundingType() {
        String fundingType = SettingsConstants.DEFAULT_FUNDING_TYPE_ID;
        Set<String> measures = new LinkedHashSet<>(GisConstants.FUNDING_TYPES);
        measures.retainAll(MeasuresVisibility.getConfigurableMeasures());
        if (measures.size() > 0) {
            fundingType = measures.iterator().next();
        }
        return fundingType;
    }

    /**
     * Configures report specification with settings provided via Json
     *
     * Applies request settings, that are expected in the following format:
     * config = { ..., “settings” : { // fields selected options or specified
     * values "funding-type" : [“Actual Commitments”, “Actual Disbursements”],
     * "currency-code" : “USD”, "calendar-id" : “123” "year-range" : { from :
     * "all", to : "2014" } } }
     *
     * @param spec
     *            - report specification over which to apply the settings
     * @param settings
     *            - the settings
     * @param setDefaults
     *            if true, then not specified settings will be configured with
     *            defaults
     */
    public static void applySettings(ReportSpecificationImpl spec, Map<String, Object> settings, boolean setDefaults) {
        if (spec.getSettings() != null && !ReportSettingsImpl.class.isAssignableFrom(spec.getSettings().getClass())) {
            logger.error("Unsupported conversion for: " + spec.getSettings().getClass());
            return;
        }

        ReportSettingsImpl reportSettings = (ReportSettingsImpl) spec.getSettings();
        if (reportSettings == null) {
            reportSettings = new ReportSettingsImpl();
            spec.setSettings(reportSettings);
        }

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
        Map<String, Object> amountFormat = settings == null ? null
                : (Map<String, Object>) settings.get(SettingsConstants.AMOUNT_FORMAT_ID);
        if (amountFormat != null) {
            String decimalSymbol = (String) amountFormat.get(SettingsConstants.DECIMAL_SYMBOL);
            Integer maxFractDigits = (Integer) amountFormat.get(SettingsConstants.MAX_FRACT_DIGITS);
            Boolean useGrouping = (Boolean) amountFormat.get(SettingsConstants.USE_GROUPING);
            String groupingSeparator = (String) amountFormat.get(SettingsConstants.GROUP_SEPARATOR);
            Integer groupingSize = (Integer) amountFormat.get(SettingsConstants.GROUP_SIZE);

            DecimalFormat format = AmpARFilter.buildCustomFormat(decimalSymbol, groupingSeparator, maxFractDigits,
                    useGrouping, groupingSize);
            reportSettings.setCurrencyFormat(format);

            Integer multiplier = PersistenceManager.getInteger(amountFormat.get(SettingsConstants.AMOUNT_UNITS));
            if (multiplier != null) {
                reportSettings.setUnitsOption(AmountsUnits.getForDivider(multiplier));
            }
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
        if (settings != null && NumberUtils.isNumber(calendarId)) {
            reportSettings.setOldCalendar(reportSettings.getCalendar());
            reportSettings.setCalendar(DbUtil.getAmpFiscalCalendar(Long.valueOf(calendarId)));
        }

        if (setDefaults && reportSettings.getCalendar() == null)
            reportSettings.setCalendar(DbUtil.getAmpFiscalCalendar(Long.valueOf(EndpointUtils.getDefaultCalendarId())));
    }

    /**
     * Configures year range setting
     *
     * @param reportSettings
     * @param settings
     * @param setDefaults:
     *            if true AND there is no range setting in @reportSettings, then
     *            reportSettings will be populated with the workspace/system's
     *            default
     */
    public static void configureYearRange(ReportSettingsImpl reportSettings, Map<String, Object> settings,
            boolean setDefaults) {
        // keep existing if no new settings are applied
        if (reportSettings.getYearRangeFilter() != null && settings == null)
            return;

        // apply year range settings
        Integer start = null;
        Integer end = null;
        if (settings != null && settings.get(SettingsConstants.YEAR_RANGE_ID) != null) {
            Map<String, Object> yearRange = (Map<String, Object>) settings.get(SettingsConstants.YEAR_RANGE_ID);
            if (yearRange.get(SettingsConstants.YEAR_FROM) != null)
                start = Integer.valueOf(yearRange.get(SettingsConstants.YEAR_FROM).toString());
            if (yearRange.get(SettingsConstants.YEAR_TO) != null)
                end = Integer.valueOf(yearRange.get(SettingsConstants.YEAR_TO).toString());
        } else if (setDefaults) {
            start = AmpARFilter.getDefaultStartYear(reportSettings.getCalendar());
            end = AmpARFilter.getDefaultEndYear(reportSettings.getCalendar());
        }

        // clear previous year settings
        reportSettings.setYearRangeFilter(null);
        reportSettings.setOldCalendar(null);
        // TODO: update settings to store [ALL, ALL] range just to reflect the
        // previous selection
        start = (start == null || start == -1) ? null : start;
        end = (end == null || end == -1) ? null : end;
        if (start != null || end != null) {
            try {
                reportSettings.setYearsRangeFilterRule(start, end);
            } catch (Exception e) {

                logger.error(e.getMessage(),e);
            }
        }
    }

    /**
     * @param selectedOption
     * @return general currency settings
     */
    private static SettingOptions getSettingOptionsFromGlobalSettings(String selectedOption, String view) {

        List<KeyValue> settingsOptions = org.digijava.module.admin.util.DbUtil
                .getPossibleValues(view);

        List<SettingOptions.Option> options = new ArrayList<>();

        for (KeyValue sortOption : settingsOptions) {
            SettingOptions.Option settingsOption = new SettingOptions.Option(sortOption.getKey(),
                    sortOption.getValue());
            options.add(settingsOption);
        }

        String defaultId = "";
        if (selectedOption != null) {
            defaultId = selectedOption;
        }

        return new SettingOptions(defaultId, options);
    }

    private static SettingField getIntSetting(String id, int value) {
        String name = SettingsConstants.ID_NAME_MAP.get(id);

        return SettingField.create(id, null, name, value);
    }

    private static SettingField getStringSetting(String id, String value) {
        String name = SettingsConstants.ID_NAME_MAP.get(id);

        return SettingField.create(id, null, name, value);
    }

    private static SettingField getBooleanSetting(String id, boolean value) {
        String name = SettingsConstants.ID_NAME_MAP.get(id);

        return SettingField.create(id, null, name, value);
    }

    static List<SettingField> getResourceManagerSettings() {
        List<SettingField> settingFieldList = new ArrayList<>();
        // refactor to look at global settings

        settingFieldList.add(
                getIntSetting(SettingsConstants.MAXIMUM_FILE_SIZE, ResourceManagerSettingsUtil.getMaximunFileSize()));
        settingFieldList.add(getStringSetting(SettingsConstants.LIMIT_FILE_TO_UPLOAD,
                ResourceManagerSettingsUtil.isLimitFileToUpload() + ""));
        settingFieldList.add(getSettingFieldForOptions(SettingsConstants.SORT_COLUMN,
                getSettingOptionsFromGlobalSettings(ResourceManagerSettingsUtil.getSortColumn(),
                        SettingsConstants.SORT_COLUMN_VIEW)));

        return settingFieldList;
    }

}
