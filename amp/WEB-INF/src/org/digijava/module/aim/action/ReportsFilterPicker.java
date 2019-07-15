/**
 *
 */
package org.digijava.module.aim.action;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.InvalidReportContextException;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.reports.converters.AmpReportFiltersConverter;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.digijava.module.aim.util.time.StopWatch;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mihai
 *
 */
public class ReportsFilterPicker extends Action {
    private static Logger logger = Logger.getLogger(ReportsFilterPicker.class);

    public static final String ONLY_JOINT_CRITERIA = "0";
    public static final String ONLY_GOV_PROCEDURES = "1";
    private static final int DEFAULT_MAX_DECIMAL_PLACES = -2;

    public static final String PLEDGE_REPORT_REQUEST_ATTRIBUTE = "is_pledge_report";

    public static final String FILTERS_WIDGET = "filtersWidget";
    public static final String FILTERS = "filters";
    public static final int FIRST_ELEMENT = 0;

    public static final Long tryParseLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
        try {
            boolean showWorkspaceFilterInTeamWorkspace = "true"
                    .equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants
                            .SHOW_WORKSPACE_FILTER_IN_TEAM_WORKSPACES));
            boolean showWorkspaceFilter = true;
            TeamMember teamMember = (TeamMember) request.getSession()
                    .getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
            AmpTeam ampTeam = null;
            if (teamMember != null) {
                ampTeam = TeamUtil.getAmpTeam(teamMember.getTeamId());
            }

            if (request.getSession().getAttribute(Constants.CURRENT_MEMBER) == null
                    && !FeaturesUtil.isVisibleModule("Public Report Generator")) {
                return mapping.findForward("mydesktop");
            }

            if (request.getSession().getAttribute(Constants.CURRENT_MEMBER) == null
                    && !FeaturesUtil.isVisibleModule("Public Report Generator")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }

            if (ampTeam != null && ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_TEAM)
                    && !ampTeam.getComputation() && !showWorkspaceFilterInTeamWorkspace) {
                showWorkspaceFilter = false;
            }
            filterForm.setShowWorkspaceFilter(showWorkspaceFilter);


            String ampReportId = request.getParameter("ampReportId");
            if ("".equals(ampReportId)) {
                ampReportId = null;
            }

            Long longAmpReportId = ampReportId == null ? null : tryParseLong(ampReportId);

            if (ampReportId == null) {
                ampReportId = request.getParameter("reportContextId");
                if ("".equals(ampReportId)) {
                    ampReportId = null;
                }
                longAmpReportId = ampReportId == null ? null : tryParseLong(ampReportId);
            }
            if (longAmpReportId == null) {
                String pledged = request.getParameter("pledged");
                if (pledged != null) {
                    filterForm.setPledged(Boolean.valueOf(pledged));
                }
            }
            String sourceIsReportWizard = request.getParameter("sourceIsReportWizard");
            if ("true".equals(sourceIsReportWizard)) {
                filterForm.setSourceIsReportWizard(true);
                if (request.getParameter("doreset") != null) {
                    try {
                        AmpARFilter reportFilter = FilterUtil.getOrCreateFilter(longAmpReportId, null);
                        FilterUtil.populateForm(filterForm, reportFilter, longAmpReportId);
                        modeRefreshDropdowns(filterForm, AmpARFilter.FILTER_SECTION_FILTERS, reportFilter);
                    } catch (InvalidReportContextException e) {
                        logger.error(e.getMessage(), e);
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return null;
                    }

                    return mapping.findForward("forward");
                }
            } else {
                filterForm.setSourceIsReportWizard(false);
            }

            filterForm.setAmpReportId(ampReportId);

            filterForm.setPledged(false);
            if (longAmpReportId != null) {
                Session session = PersistenceManager.getSession();
                AmpReports report = (AmpReports) session.get(AmpReports.class, longAmpReportId);
                if (report != null && report.getType() != null) {
                    filterForm.setReporttype(report.getType());
                    filterForm.setPledged(report.getType() == ArConstants.PLEDGES_TYPE);
                }
                if (ampReportId.length() > 0 && report != null && report.getDrilldownTab()) {
                    request.getSession().setAttribute(Constants.CURRENT_TAB_REPORT, report);
                }
            }

            // init form
            if (request.getParameter("init") != null) {
                AmpARFilter reportFilter = FilterUtil.getOrCreateFilter(longAmpReportId, null);
                FilterUtil.populateForm(filterForm, reportFilter, longAmpReportId);
                modeRefreshDropdowns(filterForm, AmpARFilter.FILTER_SECTION_SETTINGS, reportFilter);
                return null;
            }

            String applyFormatValue = request.getParameter("applyFormat");
            if (applyFormatValue != null) {
                if (applyFormatValue.equals("Reset")) {
                    // TODO-CONSTANTIN: reset is now done client-side. If done server-side, should handle non-english
                    // translations of "Reset" here!
                    // reset tab/report settings
                    AmpARFilter arf = createOrResetFilter(filterForm, AmpARFilter.FILTER_SECTION_SETTINGS);
                    return decideNextForward(mapping, filterForm, request, arf);
                    //return modeReset(mapping, form, request, response);
                } else if (applyFormatValue.equals("true")) {
                    AmpARFilter arf = ReportContextData.getFromRequest().getFilter();
                    // an AMP-y-hacky way of saying "please redraw the report without changing anything"
                    return decideNextForward(mapping, filterForm, request, arf);
                } else {
                    if (!applyFormatValue.equals("Apply Format")) {
                        logger.warn("unknown applyformat setting, assuming it is 'Apply Format': " + applyFormatValue);
                    }
                    // apply tab/report settings
                    AmpARFilter arf = createOrFillFilter(filterForm, AmpARFilter.FILTER_SECTION_SETTINGS);
                    return decideNextForward(mapping, filterForm, request, arf);
                }
            }

            if (request.getParameter("apply") != null) {
                LinkedHashMap<String, Object> filters = new LinkedHashMap<>();
                Map<String, String[]> parameters = request.getParameterMap();
                String widgetFilters = parameters.get(FILTERS_WIDGET)[FIRST_ELEMENT];
                JSONObject jsonObjParams = new JSONObject(widgetFilters);
                JSONObject jsonFilters = jsonObjParams.getJSONObject(FILTERS);
                jsonFilters.keySet().forEach((key -> {
                    String type = jsonFilters.get(key).getClass().getName();
                    if (org.json.JSONArray.class.getCanonicalName().equals(type)) {
                        List<Integer> aux = new ArrayList<>();
                        jsonFilters.getJSONArray(key).iterator().forEachRemaining(s -> {
                            aux.add(new Integer(s.toString()));
                        });
                        filters.put(key, aux);
                    } else if (org.json.JSONObject.class.getCanonicalName().equals(type)) {
                        LinkedHashMap<String, Object> dates = new LinkedHashMap<>();
                        jsonFilters.getJSONObject(key).keySet().forEach(d -> {
                            dates.put(d, jsonFilters.getJSONObject(key).get(d).toString());
                        });
                        filters.put(key, dates);
                    } else if (java.lang.Integer.class.getCanonicalName().equals(type)) {
                        filters.put(key, jsonFilters.get(key).toString());
                    } else if (java.lang.String.class.getCanonicalName().equals(type)) {
                        filters.put(key, jsonFilters.get(key).toString());
                    } else {
                        throw new RuntimeException("Unsupported type.");
                    }
                }));
                AmpReportFilters filterRules = FilterUtils.getFilters(filters, new AmpReportFilters());
                AmpReportFiltersConverter converter = new AmpReportFiltersConverter(filterRules);
                AmpARFilter ampARFilter = converter.buildFilters();
                ampARFilter.fillWithDefaultsSettings();
                if (jsonObjParams.get(EPConstants.INCLUDE_LOCATION_CHILDREN) != null) {
                    filterForm.setIncludeLocationChildren(
                            (Boolean) jsonObjParams.get(EPConstants.INCLUDE_LOCATION_CHILDREN));
                }
                FilterUtil.populateForm(filterForm, ampARFilter, longAmpReportId);
                // We need to "recreate" the arfilter or it wont be shown.
                ampARFilter = createOrFillFilter(filterForm, AmpARFilter.FILTER_SECTION_FILTERS);
                return decideNextForward(mapping, filterForm, request, ampARFilter);
            }

            AmpARFilter reportFilter = FilterUtil.getOrCreateFilter(longAmpReportId, null);
            FilterUtil.populateForm(filterForm, reportFilter, longAmpReportId);
            modeRefreshDropdowns(filterForm, AmpARFilter.FILTER_SECTION_ALL, reportFilter);
        } catch (InvalidReportContextException e) {
            logger.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        return mapping.findForward("forward");
    }

    /**
     * fills the dropdowns part of ReportsFilterPickerForm pertaining to "settings"
     * @param filterForm
     */
    public static void fillSettingsFormDropdowns(ReportsFilterPickerForm filterForm, AmpARFilter reportFilter) {
        StopWatch.reset("Filters-Settings");
        StopWatch.next("Filters-Settings", true, "Settings part dropdowns START");

        filterForm.setFromYears(new ArrayList<BeanWrapperImpl>());
        filterForm.setToYears(new ArrayList<BeanWrapperImpl>());


        Integer yearFrom = FeaturesUtil.getGlobalSettingValueInteger(
                org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START);
        yearFrom = FiscalCalendarUtil.getActualYear(FiscalCalendarUtil.getGSCalendar(), yearFrom, 0,
                reportFilter.getCalendarType());
        Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(
                org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));

        if (filterForm.getCountYear() == null) {
            filterForm.setCountYear(countYear);
        }

        if (filterForm.getCountYearFrom() == null) {
            filterForm.setCountYearFrom(yearFrom.longValue());
        }

        for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
            filterForm.getFromYears().add(new BeanWrapperImpl(i));
            filterForm.getToYears().add(new BeanWrapperImpl(i));
        }
        ArrayList<String> decimalseparators = new ArrayList<String>();
        DecimalFormat usedDecimalFormat = FormatHelper.getDecimalFormat();
        String selecteddecimalseparator = String.valueOf((usedDecimalFormat.getDecimalFormatSymbols()
                .getDecimalSeparator()));

        if (!selecteddecimalseparator.equalsIgnoreCase(".")
                && !selecteddecimalseparator.equalsIgnoreCase(",")) {
            decimalseparators.add(selecteddecimalseparator);
        }

        decimalseparators.add(".");
        decimalseparators.add(",");
        //decimalseparators.add(TranslatorWorker.translateText("CUSTOM",request));
        filterForm.setAlldecimalSymbols(decimalseparators);

        ArrayList<String> groupseparators = new ArrayList<String>();
        String selectedgroupingseparator = String.valueOf(usedDecimalFormat
                .getDecimalFormatSymbols().getGroupingSeparator());

        if (!selectedgroupingseparator.equalsIgnoreCase(".")
                && !selectedgroupingseparator.equalsIgnoreCase(",")) {
            groupseparators.add(selectedgroupingseparator);
        }

        groupseparators.add(".");
        groupseparators.add(",");
        //groupseparators.add(TranslatorWorker.translateText("CUSTOM",request));
        filterForm.setAllgroupingseparators(groupseparators);


        if (filterForm.getCustomDecimalSymbol() != null) {
            filterForm.setCustomDecimalSymbol(selecteddecimalseparator);
            filterForm.setCustomDecimalPlaces(usedDecimalFormat.getMaximumFractionDigits());
            filterForm.setCustomGroupCharacter(selectedgroupingseparator);
            filterForm.setCustomUseGrouping(usedDecimalFormat.isGroupingUsed());
            filterForm.setCustomGroupSize(usedDecimalFormat.getGroupingSize());
        }

        filterForm.setCurrencies(CurrencyUtil.getUsableCurrencies(true));
        AmpCurrency defaultCurrency = AmpARFilter.getDefaultCurrency();
        filterForm.setDefaultCurrency(defaultCurrency.getAmpCurrencyId());

        if (AmpCaching.getInstance().allFisCalendars == null)
            AmpCaching.getInstance().allFisCalendars = DbUtil.getAllFisCalenders();

        filterForm.setCalendars(AmpCaching.getInstance().allFisCalendars);
        AmpFiscalCalendar defaultCalendar = AmpARFilter.getDefaultCalendar();
        filterForm.setDefaultCalendar(defaultCalendar != null ? defaultCalendar.getAmpFiscalCalId() : null);
        StopWatch.next("Filters-Settings", true, "Settings part dropdowns END");
    }

    /**
     * populate all the non-setting fields in a form, like drop-down lists, checkbox lists etc etc etc
     * <b>Always</b> call this function after populateForm, because it corrects some fields spoiled by it
     * (fromYear, for example)
     * @param filterForm the form to populate
     * @throws Exception
     */
    public static void modeRefreshDropdowns(ReportsFilterPickerForm filterForm, int subsection,
                                            AmpARFilter reportFilter) throws DgException {

        if ((subsection & AmpARFilter.FILTER_SECTION_SETTINGS) > 0) {
            fillSettingsFormDropdowns(filterForm, reportFilter);
        }
    }

    /**
     * generate a session based AmpARFilter object based on the form selections and forward to different actions
     * (depending on the request source)
     *
     * @param mapping
     * @param form
     * @param requestTeamMember
     *            teamMember = (TeamMember) httpSession
     *            .getAttribute(Constants.CURRENT_MEMBER);
     *            AmpApplicationSettings tempSettings =
     *            DbUtil.getMemberAppSettings(teamMember.getMemberId());
     *
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward decideNextForward(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                           AmpARFilter arf) throws Exception {
        ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
        
        /* when applying settings / filters to a report, it goes into infinite recursion, because:
        - decideNextForward forwards to viewNewAdvancedReport
        - viewNewAdvancedReport has a jsp:include ReportsFilterPicker
        - we come here again        
         */
        if (request.getAttribute("NO_RECURSION") != null)
            return mapping.findForward("forward");

        request.setAttribute("NO_RECURSION", true);

        if (filterForm.getSourceIsReportWizard() != null && filterForm.getSourceIsReportWizard()) {
            return mapping.findForward("reportWizard");
        }
        if (request.getParameter("queryEngine") != null && "true".equals(request.getParameter("queryEngine"))) {
            return mapping.findForward("queryView");
        }

        if (arf.isPublicView()) {
            return mapping.findForward(arf.isWidget() ? "publicView" : "reportView");
        }

        if (arf.isWidget()) {
            return mapping.findForward("mydesktop");
        } else {
            return mapping.findForward("reportView");
        }
    }

    /**
     * utility proxy for FilterUtil.getOrCreateFilter
     * @param filterForm
     * @return
     */
    public static AmpARFilter getOrCreateFilter(ReportsFilterPickerForm filterForm) {

//      if ( filterForm.getSourceIsReportWizard() != null && filterForm.getSourceIsReportWizard() ) {
//      arf = new AmpARFilter();
//  }

        Long ampReportId = null;
        if (filterForm.getAmpReportId() != null && AmpMath.isLong(filterForm.getAmpReportId()))
            ampReportId = Long.parseLong(filterForm.getAmpReportId());
        if (ampReportId == null && ReportContextData.getFromRequest().getReportMeta() != null)
            ampReportId = ReportContextData.getFromRequest().getReportMeta().getAmpReportId();

        return FilterUtil.getOrCreateFilter(ampReportId, null);
    }

    /**
     * creates or updates the report's filter with a ReportFilterPickerForm's data, adds the result to session
     * @param filterForm the form
     * @return reference to either the created or edited AmpARFilter instance.
     * (it can be found in RCD.getFilter() anyway)
     * @throws DgException - exceptions from deep inside AMP's bowels
     */
    public static AmpARFilter createOrFillFilter(ReportsFilterPickerForm filterForm, int subsection)
            throws DgException {
        AmpARFilter arf = getOrCreateFilter(filterForm);

        if ((subsection & AmpARFilter.FILTER_SECTION_FILTERS) > 0) {
            fillFilterFromFilterForm(arf, filterForm);
        }

        if ((subsection & AmpARFilter.FILTER_SECTION_SETTINGS) > 0) {
            fillFilterFromSettingsForm(arf, filterForm);
        }

        arf.signalSettingsHaveBeenApplied();
        return arf;
    }

    public static AmpARFilter createOrResetFilter(ReportsFilterPickerForm filterForm, int subsection)
            throws DgException {
        AmpARFilter arf = getOrCreateFilter(filterForm);

        if ((subsection & AmpARFilter.FILTER_SECTION_FILTERS) > 0) {
            arf.fillWithDefaultsFilter(arf.getAmpReportId());
        }

        if ((subsection & AmpARFilter.FILTER_SECTION_SETTINGS) > 0) {
            arf.fillWithDefaultsSettings();
        }

        arf.postprocess();
        return arf;
    }

    /**
     * pumps the "Tab / Report Settings" part from the form to the filter
     * @param arf
     * @param filterForm
     * @see #fillFilterFromFilterForm(AmpARFilter, ReportsFilterPickerForm) for copying the settings part
     */
    public static void fillFilterFromSettingsForm(AmpARFilter arf, ReportsFilterPickerForm filterForm) {
        //DecimalFormat custom = new DecimalFormat();
        //DecimalFormatSymbols ds = new DecimalFormatSymbols();
        Character decimalSeparator = !"CUSTOM".equalsIgnoreCase(filterForm.getCustomDecimalSymbol())
                ? filterForm.getCustomDecimalSymbol().charAt(0) : filterForm.getCustomDecimalSymbolTxt().charAt(0);
        arf.setDecimalseparator(decimalSeparator.toString());

        boolean useGroupings = filterForm.getCustomUseGrouping() != null && filterForm.getCustomUseGrouping();
        arf.setCustomusegroupings(useGroupings);

        if (useGroupings) {
            Character groupingSeparator = !"CUSTOM".equalsIgnoreCase(filterForm.getCustomGroupCharacter())
                    ? filterForm.getCustomGroupCharacter().charAt(0)
                    : filterForm.getCustomGroupCharacterTxt().charAt(0);
            arf.setGroupingseparator(groupingSeparator.toString());
            arf.setGroupingsize(filterForm.getCustomGroupSize());
        }

        //custom.setMaximumFractionDigits((filterForm.getCustomDecimalPlaces() != -1)
        // ? filterForm.getCustomDecimalPlaces() : 99);

        arf.setAmountinthousand(filterForm.getAmountinthousands());

        Integer maximumDecimalPlaces = filterForm.getCustomDecimalPlaces();
        if (maximumDecimalPlaces == DEFAULT_MAX_DECIMAL_PLACES) { //CUSTOM
            arf.setMaximumFractionDigits(filterForm.getCustomDecimalPlacesTxt());
        } else if (maximumDecimalPlaces > -1) {
            arf.setMaximumFractionDigits(maximumDecimalPlaces);
        } else {
            DecimalFormat defaultDecimalFormat = FormatHelper.getDecimalFormat();
            arf.setMaximumFractionDigits(defaultDecimalFormat.getMaximumFractionDigits());
        }

        AmpCurrency currency;
        if (filterForm.getCurrency() != null) {
            currency = (AmpCurrency) Util.getSelectedObject(AmpCurrency.class, filterForm.getCurrency());
        } else {
            currency = (AmpCurrency) Util.getSelectedObject(AmpCurrency.class, filterForm.getDefaultCurrency());
        }

        arf.setCurrency(currency);

        //TODO-CONSTANTIN: these fields are absent from the form (not rendered in html), so they are always NULL here
        if (filterForm.getRenderStartYear() != null) {
            arf.setRenderStartYear(filterForm.getRenderStartYear());
        }

        if (filterForm.getRenderEndYear() != null) {
            arf.setRenderEndYear(filterForm.getRenderEndYear());
        }

        AmpFiscalCalendar selcal = null;
        if (filterForm.getCalendar() != null) {
            selcal = (AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class, filterForm.getCalendar());
        } else {
            selcal = (AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class,
                    filterForm.getDefaultCalendar());
        }

        arf.setCalendarType(selcal);
        arf.buildCustomFormat();
    }

    public static <T extends Object> HashSet<T> nullOrCopy(Set<T> in) {
        if (in == null) {
            return null;
        }

        if (in.isEmpty()) {
            return null;
        }

        return new HashSet<T>(in);
    }

    public static Set<AmpCategoryValue> pumpCategoryValueSetFromForm(Object[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }

        Set<AmpCategoryValue> result = new HashSet<AmpCategoryValue>();
        for (int i = 0; i < ids.length; i++) {
            Long id = (ids[i] instanceof Long) ? (Long) ids[i] : Long.parseLong(ids[i].toString());
            AmpCategoryValue value = CategoryManagerUtil.getAmpCategoryValueFromDb(id);
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

    public static Set<String> pumpPerformanceAlertTypeSetFromForm(Object[] ids) {
        if (ids != null) {
            Set<String> alertTypes = new HashSet<String>();
            for (int i = 0; i < ids.length; i++) {
                String id = ids[i].toString();
                alertTypes.add(id);
            }
            return alertTypes;
        }

        return null;
    }

    private static void fillFilterSectors(AmpARFilter arf, ReportsFilterPickerForm filterForm) {
        // for each sector we have also to add the subsectors
        Set<AmpSector> selectedSectors = Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedSectors());
        Set<AmpSector> selectedSecondarySectors = Util.getSelectedObjects(AmpSector.class,
                filterForm.getSelectedSecondarySectors());
        Set<AmpSector> selectedTertiarySectors = Util.getSelectedObjects(AmpSector.class,
                filterForm.getSelectedTertiarySectors());
        Set<AmpSector> selectedQuaternarySectors =
                Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedQuaternarySectors());
        Set<AmpSector> selectedQuinarySectors =
                Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedQuinarySectors());
        Set<AmpSector> selectedTagSectors = Util.getSelectedObjects(AmpSector.class,
                filterForm.getSelectedTagSectors());

        arf.setSelectedSectors(nullOrCopy(selectedSectors));
        arf.setSelectedSecondarySectors(nullOrCopy(selectedSecondarySectors));
        arf.setSelectedTertiarySectors(nullOrCopy(selectedTertiarySectors));
        arf.setSelectedQuaternarySectors(nullOrCopy(selectedQuaternarySectors));
        arf.setSelectedQuinarySectors(nullOrCopy(selectedQuinarySectors));
        arf.setSelectedTagSectors(nullOrCopy(selectedTagSectors));
    }

    private static void fillFilterPrograms(AmpARFilter arf, ReportsFilterPickerForm filterForm) {
        Set<AmpTheme> selectedNatPlanObj = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedNatPlanObj());
        Set<AmpTheme> selectedPrimaryPrograms = Util.getSelectedObjects(AmpTheme.class,
                filterForm.getSelectedPrimaryPrograms());
        Set<AmpTheme> selectedSecondaryPrograms = Util.getSelectedObjects(AmpTheme.class,
                filterForm.getSelectedSecondaryPrograms());
        Set<AmpTheme> selectedTertiaryPrograms = Util.getSelectedObjects(AmpTheme.class,
                filterForm.getSelectedTertiaryPrograms());

        arf.setSelectedNatPlanObj(nullOrCopy(selectedNatPlanObj));
        arf.setSelectedPrimaryPrograms(nullOrCopy(selectedPrimaryPrograms));
        arf.setSelectedSecondaryPrograms(nullOrCopy(selectedSecondaryPrograms));
        arf.setSelectedTertiaryPrograms(nullOrCopy(selectedTertiaryPrograms));
    }

    private static void fillFilterDates(AmpARFilter arf, ReportsFilterPickerForm filterForm) {
        arf.setYearFrom(filterForm.getFromYear() == null || filterForm.getFromYear() == -1
                ? null : filterForm.getFromYear().intValue());
        arf.setYearTo(filterForm.getToYear() == null || filterForm.getToYear() == -1
                ? null : filterForm.getToYear().intValue());
        arf.setFromMonth(filterForm.getFromMonth() == null || filterForm.getFromMonth() == -1
                ? null : filterForm.getFromMonth());
        arf.setToMonth(filterForm.getToMonth() == null || filterForm.getToMonth() == -1
                ? null : filterForm.getToMonth());

        arf.setFromDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromDate()));
        arf.setToDate(FilterUtil.convertUiToArFilterDate(filterForm.getToDate()));
        arf.setDynDateFilterCurrentPeriod(filterForm.getDynamicDateFilter().getCurrentPeriod());
        arf.setDynDateFilterAmount(filterForm.getDynamicDateFilter().getAmount());
        arf.setDynDateFilterOperator(filterForm.getDynamicDateFilter().getOperator());
        arf.setDynDateFilterXPeriod(filterForm.getDynamicDateFilter().getxPeriod());

        arf.setToActivityStartDate(FilterUtil.convertUiToArFilterDate(filterForm.getToActivityStartDate()));
        arf.setFromActivityStartDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromActivityStartDate()));
        arf.setDynActivityStartFilterCurrentPeriod(filterForm.getDynamicActivityStartFilter().getCurrentPeriod());
        arf.setDynActivityStartFilterAmount(filterForm.getDynamicActivityStartFilter().getAmount());
        arf.setDynActivityStartFilterOperator(filterForm.getDynamicActivityStartFilter().getOperator());
        arf.setDynActivityStartFilterXPeriod(filterForm.getDynamicActivityStartFilter().getxPeriod());

        arf.setToIssueDate(FilterUtil.convertUiToArFilterDate(filterForm.getToIssueDate()));
        arf.setFromIssueDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromIssueDate()));
        arf.setDynIssueFilterCurrentPeriod(filterForm.getDynamicIssueFilter().getCurrentPeriod());
        arf.setDynIssueFilterAmount(filterForm.getDynamicIssueFilter().getAmount());
        arf.setDynIssueFilterOperator(filterForm.getDynamicIssueFilter().getOperator());
        arf.setDynIssueFilterXPeriod(filterForm.getDynamicIssueFilter().getxPeriod());

        arf.setFromProposedApprovalDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromProposedApprovalDate()));
        arf.setToProposedApprovalDate(FilterUtil.convertUiToArFilterDate(filterForm.getToProposedApprovalDate()));
        arf.setDynProposedApprovalFilterCurrentPeriod(filterForm.getDynamicProposedApprovalFilter().getCurrentPeriod());
        arf.setDynProposedApprovalFilterAmount(filterForm.getDynamicProposedApprovalFilter().getAmount());
        arf.setDynProposedApprovalFilterOperator(filterForm.getDynamicProposedApprovalFilter().getOperator());
        arf.setDynProposedApprovalFilterXPeriod(filterForm.getDynamicProposedApprovalFilter().getxPeriod());

        arf.setToActivityActualCompletionDate(FilterUtil
                .convertUiToArFilterDate(filterForm.getToActivityActualCompletionDate()));
        arf.setFromActivityActualCompletionDate(FilterUtil
                .convertUiToArFilterDate(filterForm.getFromActivityActualCompletionDate()));
        arf.setDynActivityActualCompletionFilterCurrentPeriod(filterForm
                .getDynamicActivityActualCompletionFilter().getCurrentPeriod());
        arf.setDynActivityActualCompletionFilterAmount(filterForm
                .getDynamicActivityActualCompletionFilter().getAmount());
        arf.setDynActivityActualCompletionFilterOperator(filterForm
                .getDynamicActivityActualCompletionFilter().getOperator());
        arf.setDynActivityActualCompletionFilterXPeriod(filterForm
                .getDynamicActivityActualCompletionFilter().getxPeriod());

        arf.setToActivityFinalContractingDate(FilterUtil
                .convertUiToArFilterDate(filterForm.getToActivityFinalContractingDate()));
        arf.setFromActivityFinalContractingDate(FilterUtil
                .convertUiToArFilterDate(filterForm.getFromActivityFinalContractingDate()));
        arf.setDynActivityFinalContractingFilterCurrentPeriod(filterForm
                .getDynamicActivityFinalContractingFilter().getCurrentPeriod());
        arf.setDynActivityFinalContractingFilterAmount(filterForm
                .getDynamicActivityFinalContractingFilter().getAmount());
        arf.setDynActivityFinalContractingFilterOperator(filterForm
                .getDynamicActivityFinalContractingFilter().getOperator());
        arf.setDynActivityFinalContractingFilterXPeriod(filterForm
                .getDynamicActivityFinalContractingFilter().getxPeriod());

        arf.setToEffectiveFundingDate(FilterUtil.convertUiToArFilterDate(filterForm.getToEffectiveFundingDate()));
        arf.setFromEffectiveFundingDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromEffectiveFundingDate()));
        arf.setDynEffectiveFundingFilterCurrentPeriod(filterForm.getDynamicEffectiveFundingFilter().getCurrentPeriod());
        arf.setDynEffectiveFundingFilterAmount(filterForm.getDynamicEffectiveFundingFilter().getAmount());
        arf.setDynEffectiveFundingFilterOperator(filterForm.getDynamicEffectiveFundingFilter().getOperator());
        arf.setDynEffectiveFundingFilterXPeriod(filterForm.getDynamicEffectiveFundingFilter().getxPeriod());

        arf.setToFundingClosingDate(FilterUtil.convertUiToArFilterDate(filterForm.getToFundingClosingDate()));
        arf.setFromFundingClosingDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromFundingClosingDate()));
        arf.setDynFundingClosingFilterCurrentPeriod(filterForm.getDynamicFundingClosingFilter().getCurrentPeriod());
        arf.setDynFundingClosingFilterAmount(filterForm.getDynamicFundingClosingFilter().getAmount());
        arf.setDynFundingClosingFilterOperator(filterForm.getDynamicFundingClosingFilter().getOperator());
        arf.setDynFundingClosingFilterXPeriod(filterForm.getDynamicFundingClosingFilter().getxPeriod());

        arf.setFromPledgeDetailStartDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromPledgeDetailStartDate()));
        arf.setToPledgeDetailStartDate(FilterUtil.convertUiToArFilterDate(filterForm.getToPledgeDetailStartDate()));
        arf.setFromPledgeDetailEndDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromPledgeDetailEndDate()));
        arf.setToPledgeDetailEndDate(FilterUtil.convertUiToArFilterDate(filterForm.getToPledgeDetailEndDate()));

        arf.setSelectedActivityPledgesSettings(Integer.parseInt(filterForm.getSelectedActivityPledgesSettings()));

        int curYear = new GregorianCalendar().get(Calendar.YEAR);

        if (filterForm.getComputedYear() != -1) {
            arf.setComputedYear(filterForm.getComputedYear());
        } else {
            if (FeaturesUtil.isVisibleFeature("Computed Year")) {
                arf.setComputedYear(curYear);
            } else {
                arf.setComputedYear(null);
            }

        }
        arf.setToActualApprovalDate(FilterUtil.convertUiToArFilterDate(filterForm.getToActualApprovalDate()));
        arf.setFromActualApprovalDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromActualApprovalDate()));

        arf.setToProposedCompletionDate(
                FilterUtil.convertUiToArFilterDate(filterForm.getToProposedCompletionDate()));
        arf.setFromProposedCompletionDate(
                FilterUtil.convertUiToArFilterDate(filterForm.getFromProposedCompletionDate()));
    }

    private static void fillFilterOrganizations(AmpARFilter arf, ReportsFilterPickerForm filterForm) {
        arf.setBeneficiaryAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedBeneficiaryAgency()));
        arf.setBeneficiaryAgencyTypes(ReportsUtil.processSelectedFilters(filterForm.getSelectedBeneficiaryAgencyTypes(),
                AmpOrgType.class));
        arf.setBeneficiaryAgencyGroups(ReportsUtil.processSelectedFilters(
                filterForm.getSelectedBeneficiaryAgencyGroups(), AmpOrgGroup.class));

        arf.setDonnorgAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedDonnorAgency()));

        arf.setResponsibleorg(ReportsUtil.processSelectedFilters(filterForm.getSelectedresponsibleorg()));
        arf.setResponsibleOrgTypes(
                ReportsUtil.processSelectedFilters(filterForm.getSelectedResponsibleOrgTypes(), AmpOrgType.class));
        arf.setResponsibleOrgGroups(ReportsUtil.processSelectedFilters(
                filterForm.getSelectedResponsibleOrgGroups(), AmpOrgGroup.class));

        arf.setComponentFunding(ReportsUtil.processSelectedFilters(filterForm.getSelectedComponentFundingOrg()));
        arf.setComponentSecondResponsible(ReportsUtil
                .processSelectedFilters(filterForm.getSelectedComponentSecondResponsibleOrg()));

        arf.setImplementingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedImplementingAgency()));
        arf.setImplementingAgencyTypes(
                ReportsUtil.processSelectedFilters(filterForm.getSelectedImplementingAgencyTypes(), AmpOrgType.class));
        arf.setImplementingAgencyGroups(ReportsUtil.processSelectedFilters(
                filterForm.getSelectedImplementingAgencyGroups(), AmpOrgGroup.class));

        arf.setContractingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedContractingAgency()));
        arf.setContractingAgencyTypes(ReportsUtil.processSelectedFilters(filterForm.getSelectedContractingAgencyTypes(),
                AmpOrgType.class));
        arf.setContractingAgencyGroups(ReportsUtil.processSelectedFilters(
                filterForm.getSelectedContractingAgencyGroups(), AmpOrgGroup.class));

        arf.setExecutingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedExecutingAgency()));
        arf.setExecutingAgencyTypes(ReportsUtil.processSelectedFilters(filterForm.getSelectedExecutingAgencyTypes(),
                AmpOrgType.class));
        arf.setExecutingAgencyGroups(ReportsUtil.processSelectedFilters(
                filterForm.getSelectedExecutingAgencyGroups(), AmpOrgGroup.class));

        if (filterForm.getSelectedDonorTypes() != null && filterForm.getSelectedDonorTypes().length > 0) {
            arf.setDonorTypes(new HashSet<AmpOrgType>());
            for (int i = 0; i < filterForm.getSelectedDonorTypes().length; i++) {
                Long id = Long.parseLong(filterForm.getSelectedDonorTypes()[i].toString());
                AmpOrgType type = DbUtil.getAmpOrgType(id);
                if (type != null) {
                    arf.getDonorTypes().add(type);
                }
            }
        } else {
            arf.setDonorTypes(null);
        }
        if (filterForm.getSelectedDonorGroups() != null && filterForm.getSelectedDonorGroups().length > 0) {
            arf.setDonorGroups(new HashSet<AmpOrgGroup>());
            for (int i = 0; i < filterForm.getSelectedDonorGroups().length; i++) {
                Long id = Long.parseLong(filterForm.getSelectedDonorGroups()[i].toString());
                AmpOrgGroup grp = DbUtil.getAmpOrgGroup(id);
                if (grp != null) {
                    arf.getDonorGroups().add(grp);
                }
            }
        } else {
            arf.setDonorGroups(null);
        }
        if (filterForm.getSelectedMultiDonor() != null && filterForm.getSelectedMultiDonor().length == 1) {
            arf.setMultiDonor((String) filterForm.getSelectedMultiDonor()[0]);
        } else {
            arf.setMultiDonor(null);
        }
    }

    private static void fillFilterCategories(AmpARFilter arf, ReportsFilterPickerForm filterForm) {
        arf.setStatuses(pumpCategoryValueSetFromForm(filterForm.getSelectedStatuses()));
        arf.setExpenditureClass(pumpCategoryValueSetFromForm(filterForm.getSelectedExpenditureClasses()));
        arf.setPerformanceAlertLevel(pumpCategoryValueSetFromForm(filterForm.getSelectedPerformanceAlertLevels()));
        arf.setProjectCategory(pumpCategoryValueSetFromForm(filterForm.getSelectedProjectCategory()));
        arf.setFinancingInstruments(pumpCategoryValueSetFromForm(filterForm.getSelectedFinancingInstruments()));
        arf.setFundingStatus(pumpCategoryValueSetFromForm(filterForm.getSelectedFundingStatus()));
        arf.setAidModalities(pumpCategoryValueSetFromForm(filterForm.getSelectedAidModalities()));
        arf.setTypeOfAssistance(pumpCategoryValueSetFromForm(filterForm.getSelectedTypeOfAssistance()));
        arf.setModeOfPayment(pumpCategoryValueSetFromForm(filterForm.getSelectedModeOfPayment()));
        arf.setProjectImplementingUnits(pumpCategoryValueSetFromForm(filterForm.getSelectedProjectImplUnit()));
        arf.setActivityPledgesTitle(pumpCategoryValueSetFromForm(filterForm.getSelectedActivityPledgesTitle()));
        arf.setBudget(pumpCategoryValueSetFromForm(filterForm.getSelectedBudgets()));
    }

    /**
     * fills an AmpARFilter instance with Filters data from a ReportsFilterPickerForm
     * @param arf
     * @param filterForm
     * @see #fillFilterFromSettingsForm(AmpARFilter, ReportsFilterPickerForm) for copying the settings part
     */
    public static void fillFilterFromFilterForm(AmpARFilter arf, ReportsFilterPickerForm filterForm)
            throws DgException {
        Session session = PersistenceManager.getSession();
        arf.readRequestData(TLSUtils.getRequest(), AmpARFilter.FILTER_SECTION_FILTERS, null);
        fillFilterSectors(arf, filterForm);
        fillFilterPrograms(arf, filterForm);
        if (filterForm.getText() != null) {
            arf.setText(filterForm.getText());
        }
        if (filterForm.getIndexString() != null) {
            arf.setIndexText(filterForm.getIndexString());
            arf.setSearchMode(filterForm.getSearchMode());
        }
        fillFilterDates(arf, filterForm);
        arf.setSelectedActivityPledgesSettings(Integer.parseInt(filterForm.getSelectedActivityPledgesSettings()));
        Integer all = new Integer(-1);
        if (filterForm.getLineMinRanks() != null && filterForm.getLineMinRanks().length > 0) {
            ArrayList<Integer> ranks = new ArrayList<Integer>();
            for (int i = 0; i < filterForm.getLineMinRanks().length; i++) {
                Integer val = Integer.parseInt((String) filterForm.getLineMinRanks()[i]);
                if (val == 0) {
                    ranks = null;
                    break;
                } else {
                    ranks.add(val);
                }
            }
            arf.setLineMinRank(ranks);
        }
        Set<AmpCategoryValueLocations> selectedRegions = null;
        if (ArrayUtils.isNotEmpty(filterForm.getRegionSelected())) {
            if (!filterForm.getRegionSelected()[0].toString().equals("-1")) {
                selectedRegions = Util.getSelectedObjects(AmpCategoryValueLocations.class,
                        filterForm.getRegionSelected());
            }
        }
        if (selectedRegions != null && selectedRegions.size() > 0) {
            arf.setLocationSelected(new HashSet<AmpCategoryValueLocations>());
            arf.getLocationSelected().addAll(selectedRegions);
        } else {
            arf.setLocationSelected(null);
            //arf.setRelatedLocations(null);
        }
        if (!all.equals(filterForm.getApprovalStatusSelected())) {
            if (filterForm.getApprovalStatusSelected() != null) {
                ArrayList<String> appvals = new ArrayList<String>();
                for (int i = 0; i < filterForm.getApprovalStatusSelected().length; i++) {
                    String id = filterForm.getApprovalStatusSelected()[i].toString();
                    appvals.add(id);
                }
                arf.setApprovalStatusSelected(appvals);
            } else {
                arf.setApprovalStatusSelected(null);
            }
        } else {
            arf.setApprovalStatusSelected(null);
        }
        fillFilterCategories(arf, filterForm);
        arf.setPerformanceAlertType(pumpPerformanceAlertTypeSetFromForm(filterForm.getSelectedPerformanceAlertTypes()));
        if (filterForm.getSelectedWorkspaces() != null && filterForm.getSelectedWorkspaces().length > 0) {
            arf.setWorkspaces(new HashSet<AmpTeam>());
        } else {
            arf.setWorkspaces(null);
        }
        for (int i = 0;
             filterForm.getSelectedWorkspaces() != null && i < filterForm.getSelectedWorkspaces().length; i++) {
            Long workspaceId = Long.parseLong(filterForm.getSelectedWorkspaces()[i].toString());
            AmpTeam value = (AmpTeam) session.load(AmpTeam.class, workspaceId);
            arf.getWorkspaces().add(value);
        }
        if (filterForm.getPageSize() != null) {
            arf.setPageSize(filterForm.getPageSize()); // set page size in the ARF filter
        }
        arf.setRisks(Util.getSelectedObjects(AmpIndicatorRiskRatings.class, filterForm.getSelectedRisks()));
        if (filterForm.getSelectedActivitySettings() != null && filterForm.getSelectedActivitySettings().length > 0) {
            boolean isJointCriteria = false;
            boolean isGovProcedures = false;
            for (int i = 0; i < filterForm.getSelectedActivitySettings().length; i++) {
                String element = filterForm.getSelectedActivitySettings()[i].toString();
                if (ONLY_JOINT_CRITERIA.equals(element)) {
                    arf.setJointCriteria(true);
                    isJointCriteria = true;
                }
                if (ONLY_GOV_PROCEDURES.equals(element)) {
                    arf.setGovernmentApprovalProcedures(true);
                    isGovProcedures = true;
                }
            }
            if (!isJointCriteria) {
                arf.setJointCriteria(null);
            }
            if (!isGovProcedures) {
                arf.setGovernmentApprovalProcedures(null);
            }
        }
        arf.setJustSearch(filterForm.getJustSearch() == null ? false : filterForm.getJustSearch());
        arf.setWorkspaceonly(filterForm.getWorkspaceonly() == null ? false : filterForm.getWorkspaceonly());
        
        /*THIS IS USED FOR PLEDGES IN ORDER TO SHOW ONLY PLEDGES ASSOCIATED TO THE ACTIVITIES
        THAT BELONG TO THE WORKSPACE PLEASE DON'T DELETE IT AGAIN*/
        if (arf.isWorkspaceonly()) {
            arf.setAmpTeamsforpledges(WorkspaceFilter.getAmpTeamsSet(arf.getTeamMemberId()));
        } else {
            arf.setAmpTeamsforpledges(null);
        }
        fillFilterOrganizations(arf, filterForm);
        arf.setProjectCategory(ReportsUtil
                .processSelectedFilters(filterForm.getSelectedProjectCategory(), AmpCategoryValue.class));
        arf.setHumanitarianAid(buildBooleanField(filterForm.getSelectedHumanitarianAid()));
        arf.setDisasterResponse(buildBooleanField(filterForm.getSelectedDisasterResponse()));
        arf.getUndefinedOptions().clear(); 
        arf.getUndefinedOptions().addAll(filterForm.getUndefinedOptions());
        arf.setIncludeLocationChildren(filterForm.getIncludeLocationChildren());
        arf.postprocess();
    }

    protected static Set<Integer> buildBooleanField(Object[] values) {
        if (values == null) {
            return null;
        }

        Set<Integer> res = new HashSet<>();
        for (Object obj : values) {
            if (obj != null && Integer.parseInt(obj.toString()) > 0) {
                res.add(Integer.parseInt(obj.toString()));
            }
        }
        return res;
    }
}
