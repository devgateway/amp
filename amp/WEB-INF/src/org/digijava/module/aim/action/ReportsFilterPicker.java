package org.digijava.module.aim.action;

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
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.reports.converters.AmpReportFiltersConverter;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
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
import org.hibernate.Session;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mihai
 *
 */
public class ReportsFilterPicker extends Action {
    private static Logger logger = Logger.getLogger(ReportsFilterPicker.class);

    public final static String PLEDGE_REPORT_REQUEST_ATTRIBUTE = "is_pledge_report";

    public final static Long tryParseLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
        try {
            boolean showWorkspaceFilterInTeamWorkspace = "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SHOW_WORKSPACE_FILTER_IN_TEAM_WORKSPACES));
            boolean showWorkspaceFilter = true;
            TeamMember teamMember = (TeamMember) request.getSession().getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
            AmpTeam ampTeam = null;
            if (teamMember != null) {
                ampTeam = TeamUtil.getAmpTeam(teamMember.getTeamId());
            }

            if (request.getSession().getAttribute(Constants.CURRENT_MEMBER) == null && !FeaturesUtil.isVisibleModule("Public Report Generator")) {
                return mapping.findForward("mydesktop");
            }

            if (request.getSession().getAttribute(Constants.CURRENT_MEMBER) == null && !FeaturesUtil.isVisibleModule("Public Report Generator")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }

            if (ampTeam != null && ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_TEAM) &&
                    ampTeam.getComputation() == false && !showWorkspaceFilterInTeamWorkspace) {
                showWorkspaceFilter = false;
            }
            filterForm.setShowWorkspaceFilter(showWorkspaceFilter);


            String ampReportId = request.getParameter("ampReportId");
            if ("".equals(ampReportId))
                ampReportId = null;

            Long longAmpReportId = ampReportId == null ? null : tryParseLong(ampReportId);

            if (ampReportId == null) {
                ampReportId = request.getParameter("reportContextId");
                if ("".equals(ampReportId))
                    ampReportId = null;
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
                if (ampReportId.length() > 0 && report != null && report.getDrilldownTab())
                    request.getSession().setAttribute(Constants.CURRENT_TAB_REPORT, report);
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

            if (request.getParameter("applyWithNewWidget") != null) {
                LinkedHashMap<String, Object> filters = new LinkedHashMap<String, Object>();
                Map<String, String[]> parameters = request.getParameterMap();
                parameters.keySet().stream().filter(x -> x.startsWith("filter")).forEach((s -> {
                    String key = s.toString().substring(s.indexOf("[") + 1, s.indexOf("]"));
                    String subKey = s.replace("filters[" + key + "]", "");
                    if (subKey.contains("[]")) {
                        filters.put(key, Arrays.asList(parameters.get(s)));
                    } else {
                        if (!subKey.equals("")) {
                            String subKey2 = subKey.substring(subKey.indexOf("[") + 1, subKey.indexOf("]"));
                            LinkedHashMap<String, Object> sub = new LinkedHashMap<String, Object>();
                            sub.put(subKey2, parameters.get(s)[0]);
                            if (!filters.containsKey(key)) {
                                filters.put(key, sub);
                            } else {
                                ((LinkedHashMap<String, Object>) filters.get(key)).put(subKey2, parameters.get(s)[0]);
                            }
                        } else {
                            filters.put(key, parameters.get(s)[0]);
                        }
                    }
                }));
                // AmpReportFilters filterRules = FilterUtils.getFilterRules(filters, null);
                AmpReportFilters filterRules = FilterUtils.getFilters(filters, new AmpReportFilters());
                AmpReportFiltersConverter converter = new AmpReportFiltersConverter(filterRules);
                AmpARFilter ampARFilter = converter.buildFilters();
                ampARFilter.fillWithDefaultsSettings();
                FilterUtil.populateForm(filterForm, ampARFilter, longAmpReportId);
                AmpARFilter ampARFilter2 = createOrFillFilter(filterForm, AmpARFilter.FILTER_SECTION_FILTERS);
                return decideNextForward(mapping, filterForm, request, ampARFilter2);
            }

            AmpARFilter reportFilter = FilterUtil.getOrCreateFilter(longAmpReportId, null);
            FilterUtil.populateForm(filterForm, reportFilter, longAmpReportId);
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
        Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));

        if (filterForm.getCountYear() == null) {
            filterForm.setCountYear(countYear);
        }

        if (filterForm.getCountYearFrom() == null) {
            filterForm.setCountYearFrom(yearFrom.longValue());
        }

        for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
            filterForm.getFromYears().add(new BeanWrapperImpl(new Long(i)));
            filterForm.getToYears().add(new BeanWrapperImpl(new Long(i)));
        }
        ArrayList<String> decimalseparators = new ArrayList<String>();
        DecimalFormat usedDecimalFormat = FormatHelper.getDecimalFormat();
        String selecteddecimalseparator = String.valueOf((usedDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator()));

        if (!selecteddecimalseparator.equalsIgnoreCase(".") && !selecteddecimalseparator.equalsIgnoreCase(",")) {
            decimalseparators.add(selecteddecimalseparator);
        }

        decimalseparators.add(".");
        decimalseparators.add(",");
        //decimalseparators.add(TranslatorWorker.translateText("CUSTOM",request));
        filterForm.setAlldecimalSymbols(decimalseparators);

        ArrayList<String> groupseparators = new ArrayList<String>();
        String selectedgroupingseparator = String.valueOf(usedDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator());

        if (!selectedgroupingseparator.equalsIgnoreCase(".") && !selectedgroupingseparator.equalsIgnoreCase(",")) {
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

    public static void modeRefreshDropdowns(ReportsFilterPickerForm filterForm, int subsection,
                                            AmpARFilter reportFilter) throws DgException {
        if ((subsection & AmpARFilter.FILTER_SECTION_SETTINGS) > 0) {
            fillSettingsFormDropdowns(filterForm, reportFilter);
        }
    }

    /**
     * generate a session based AmpARFilter object based on the form selections and forward to different actions (depending on the request source)
     *
     * @param mapping
     * @param form
     * @return
     * @throws Exception
     */
    public ActionForward decideNextForward(ActionMapping mapping, ActionForm form, HttpServletRequest request, AmpARFilter arf) throws Exception {
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
     * @return reference to either the created or edited AmpARFilter instance. (it can be found in RCD.getFilter() anyway)
     * @throws DgException - exceptions from deep inside AMP's bowels
     */
    public static AmpARFilter createOrFillFilter(ReportsFilterPickerForm filterForm, int subsection) throws DgException {
        AmpARFilter arf = getOrCreateFilter(filterForm);

        if ((subsection & AmpARFilter.FILTER_SECTION_SETTINGS) > 0)
            fillFilterFromSettingsForm(arf, filterForm);

        arf.signalSettingsHaveBeenApplied();
        return arf;
    }

    public static AmpARFilter createOrResetFilter(ReportsFilterPickerForm filterForm, int subsection) throws DgException {
        AmpARFilter arf = getOrCreateFilter(filterForm);

        if ((subsection & AmpARFilter.FILTER_SECTION_SETTINGS) > 0)
            arf.fillWithDefaultsSettings();

        arf.postprocess();
        return arf;
    }

    /**
     * pumps the "Tab / Report Settings" part from the form to the filter
     * @param arf
     * @param filterForm
     */
    public static void fillFilterFromSettingsForm(AmpARFilter arf, ReportsFilterPickerForm filterForm) {
        //DecimalFormat custom = new DecimalFormat();
        //DecimalFormatSymbols ds = new DecimalFormatSymbols();
        Character decimalSeparator = !"CUSTOM".equalsIgnoreCase(filterForm.getCustomDecimalSymbol()) ?
                filterForm.getCustomDecimalSymbol().charAt(0) : filterForm.getCustomDecimalSymbolTxt().charAt(0);
        arf.setDecimalseparator(decimalSeparator.toString());

        boolean useGroupings = filterForm.getCustomUseGrouping() != null && filterForm.getCustomUseGrouping().booleanValue() == true;
        arf.setCustomusegroupings(useGroupings);

        if (useGroupings) {
            Character groupingSeparator = !"CUSTOM".equalsIgnoreCase(filterForm.getCustomGroupCharacter()) ?
                    filterForm.getCustomGroupCharacter().charAt(0) : filterForm.getCustomGroupCharacterTxt().charAt(0);
            arf.setGroupingseparator(groupingSeparator.toString());
            arf.setGroupingsize(filterForm.getCustomGroupSize());
        }

        arf.setAmountinthousand(filterForm.getAmountinthousands());

        Integer maximumDecimalPlaces = filterForm.getCustomDecimalPlaces();
        if (maximumDecimalPlaces == -2) {//CUSTOM
            arf.setMaximumFractionDigits(filterForm.getCustomDecimalPlacesTxt());
        } else if (maximumDecimalPlaces > -1)
            arf.setMaximumFractionDigits(maximumDecimalPlaces);
        else {
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
        if (filterForm.getRenderStartYear() != null)
            arf.setRenderStartYear(filterForm.getRenderStartYear());

        if (filterForm.getRenderEndYear() != null)
            arf.setRenderEndYear(filterForm.getRenderEndYear());

        AmpFiscalCalendar selcal = null;
        if (filterForm.getCalendar() != null) {
            selcal = (AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class, filterForm.getCalendar());
        } else {
            selcal = (AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class, filterForm.getDefaultCalendar());
        }

        arf.setCalendarType(selcal);
        arf.buildCustomFormat();
    }
}
