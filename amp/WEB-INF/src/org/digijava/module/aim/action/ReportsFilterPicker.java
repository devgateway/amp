/**
 * 
 */
package org.digijava.module.aim.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.InvalidReportContextException;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.OrgTypeSkeleton;
import org.digijava.module.aim.form.DynamicDateFilter;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.aim.util.AmpThemeSkeleton;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.HierarchyListableUtil;
import org.digijava.module.aim.util.LocationSkeleton;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.OrgGroupSkeleton;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorSkeleton;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.digijava.module.aim.util.filters.DateListableImplementation;
import org.digijava.module.aim.util.filters.GroupingElement;
import org.digijava.module.aim.util.filters.HierarchyListableImplementation;
import org.digijava.module.aim.util.time.StopWatch;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Session;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author mihai
 * 
 */
public class ReportsFilterPicker extends Action {
    private static Logger logger = Logger.getLogger(ReportsFilterPicker.class);
    
    final static String KEY_RISK_PREFIX = "aim:risk:";
    public final static String ONLY_JOINT_CRITERIA  = "0";
    public final static String ONLY_GOV_PROCEDURES  = "1";

    public final static String PLEDGE_REPORT_REQUEST_ATTRIBUTE = "is_pledge_report";

    
    public final static Long tryParseLong(String input)
    {
        try
        {
            return Long.parseLong(input);
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
//      AmpCaching.clearInstance();
        ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
        //filterForm.setAmpReportId(ReportContextData.getFromRequest().getAmp);
        try {
            boolean showWorkspaceFilterInTeamWorkspace = "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SHOW_WORKSPACE_FILTER_IN_TEAM_WORKSPACES));
            boolean showWorkspaceFilter = true;
            TeamMember teamMember = (TeamMember) request.getSession().getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
            AmpTeam ampTeam = null;
            if (teamMember != null) {
                ampTeam = TeamUtil.getAmpTeam(teamMember.getTeamId());
            }
        
        if(request.getSession().getAttribute(Constants.CURRENT_MEMBER) == null && !FeaturesUtil.isVisibleModule("Public Report Generator")){
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
    
    
            String ampReportId  = request.getParameter("ampReportId");
            if ( "".equals(ampReportId) )
                ampReportId     = null;
            
            Long longAmpReportId = ampReportId == null ? null : tryParseLong(ampReportId);
            
            if(ampReportId==null){
                ampReportId = request.getParameter("reportContextId");
                if ( "".equals(ampReportId) )
                    ampReportId     = null;
                longAmpReportId = ampReportId == null ? null : tryParseLong(ampReportId);
            }
            if(longAmpReportId==null){
                String pledged = request.getParameter("pledged");
                if(pledged!=null){
                    filterForm.setPledged(Boolean.valueOf(pledged));
                }
            }
            String sourceIsReportWizard         = request.getParameter("sourceIsReportWizard");
            if ("true".equals(sourceIsReportWizard) ) {
                filterForm.setSourceIsReportWizard(true);
                if ( request.getParameter("doreset") != null ) {
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
            }
            else
                filterForm.setSourceIsReportWizard(false);
            
            filterForm.setAmpReportId(ampReportId);
    //      this code makes no sense        
    //      AmpARFilter arf = (AmpARFilter) request.getSession().getAttribute(ArConstants.REPORTS_Z_FILTER);
    //      if (arf == null)
    //      {
    //          arf = new AmpARFilter();        
    //          arf.setPublicView(true);
    //          request.getSession().setAttribute(ArConstants.REPORTS_Z_FILTER,arf);
    //      }
            
            filterForm.setPledged(false);
            if (longAmpReportId != null)
            {
                Session session = PersistenceManager.getSession();
                AmpReports report = (AmpReports) session.get(AmpReports.class, longAmpReportId);
                if (report != null &&  report.getType() != null ){
                    filterForm.setReporttype(report.getType());
                    filterForm.setPledged(report.getType() == ArConstants.PLEDGES_TYPE);
                }
                if (ampReportId.length() > 0 && report != null && report.getDrilldownTab())
                    request.getSession().setAttribute(Constants.CURRENT_TAB_REPORT, report);
            }
            
            // init form
            if (request.getParameter("init") != null)
            {
                AmpARFilter reportFilter = FilterUtil.getOrCreateFilter(longAmpReportId, null);
                FilterUtil.populateForm(filterForm, reportFilter, longAmpReportId);
                modeRefreshDropdowns(filterForm, AmpARFilter.FILTER_SECTION_SETTINGS, reportFilter);
                return null;
            }
    
            String applyFormatValue = request.getParameter("applyFormat");
            if (applyFormatValue != null)
            {
                if (applyFormatValue.equals("Reset"))
                {
                    // TODO-CONSTANTIN: reset is now done client-side. If done server-side, should handle non-english translations of "Reset" here!
                    // reset tab/report settings
                    AmpARFilter arf = createOrResetFilter(filterForm, AmpARFilter.FILTER_SECTION_SETTINGS);
                    return decideNextForward(mapping, filterForm, request, arf);
                    //return modeReset(mapping, form, request, response);
                } else if (applyFormatValue.equals("true"))
                {
                    AmpARFilter arf = ReportContextData.getFromRequest().getFilter();
                    return decideNextForward(mapping, filterForm, request, arf); // an AMP-y-hacky way of saying "please redraw the report without changing anything"
                }
                else
                {
                    if (!applyFormatValue.equals("Apply Format"))
                        logger.warn("unknown applyformat setting, assuming it is 'Apply Format': " + applyFormatValue);
                    // apply tab/report settings
                    AmpARFilter arf = createOrFillFilter(filterForm, AmpARFilter.FILTER_SECTION_SETTINGS);
                    return decideNextForward(mapping, filterForm, request, arf);
                }               
            }
            
            // gone till here -> Apply or Reset Filters form 
            if (request.getParameter("reset") != null)
            {
                AmpARFilter arf = createOrResetFilter(filterForm, AmpARFilter.FILTER_SECTION_FILTERS);
                return decideNextForward(mapping, filterForm, request, arf);
            }
    
            if (request.getParameter("apply") != null)
            {
                // apply Filters form
                AmpARFilter arf = createOrFillFilter(filterForm, AmpARFilter.FILTER_SECTION_FILTERS);
                return decideNextForward(mapping, filterForm, request, arf);
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
        /*AmpARFilter arf = createOrFillFilter(filterForm, AmpARFilter.FILTER_SECTION_FILTERS);
        return decideNextForward(mapping, filterForm, request, arf);*/
    }
    
    /**
     * 
     * @param fmField the FM field that should be checked in order to see if this element should be shown in the page.
     * If null, no check will be done.
     * @param baseFormProperty the string that will be used to construct the HTML form properties
     * @param label the name that will appear next to this field
     * @param dynamicDateFilterObj the object from the ActionForm that defines this dynamic date filter 
     * @param htmlDivId
     * @param otherCriteriaElements
     */
    private static void addDateFilter(String fmField, String baseFormProperty, String label,
                                      DynamicDateFilter dynamicDateFilterObj, String htmlDivId,
                                      Collection<GroupingElement<HierarchyListableImplementation>> otherCriteriaElements) {
            if (fmField == null || FeaturesUtil.isVisibleField(fmField) ) {
            
                Collection<DateListableImplementation> children = new ArrayList<DateListableImplementation>();
                DateListableImplementation fromDate = new DateListableImplementation("From", "from" + baseFormProperty + "Date");
                fromDate.setActionFormProperty("from" + baseFormProperty + "Date");
                fromDate.setTranslateable(true);
                children.add(fromDate);
                
                DateListableImplementation toDate = new DateListableImplementation("To", "to" + baseFormProperty + "Date");
                toDate.setActionFormProperty("to" + baseFormProperty + "Date");
                toDate.setTranslateable(true);
                children.add(toDate);
                
                DateListableImplementation groupFromTo = new DateListableImplementation("", "0");
                groupFromTo.setTranslateable(false);
                groupFromTo.setChildren(children);
                
                children = new ArrayList<DateListableImplementation>();
                children.add(groupFromTo);
                
                // little ugly hack
                String dynamicFilterBaseFormProperty    = "".equals(baseFormProperty) ? "Date" : baseFormProperty;
                DateListableImplementation dynamicFilter = new DateListableImplementation("Dynamic Date Filter", "dynamic" + dynamicFilterBaseFormProperty + "Filter");
                dynamicFilter.setActionFormProperty("dynamic" + dynamicFilterBaseFormProperty + "Filter");
                dynamicFilter.setTranslateable(true);
                children.add(dynamicFilter);
                
                if (dynamicDateFilterObj.getCurrentPeriod() != null) {
                    dynamicFilter.setSelected(true);
                } else {
                    groupFromTo.setSelected(true);
                }
                
                DateListableImplementation rootDate = new DateListableImplementation(label, "root" + baseFormProperty);
                rootDate.setTranslateable(true);
                rootDate.setChildren(children);
                GroupingElement<HierarchyListableImplementation> filterByDate   = 
                        new GroupingElement<HierarchyListableImplementation>(label, htmlDivId, rootDate, "", GroupingElement.GROUPING_ELEMENT_FIELD_TYPE_DATE);

                otherCriteriaElements.add(filterByDate);
            }
    }
    
    /**
     * add to the Filter Form an element regarding filtering by a certain type of agencies, if feature is enabled. 
     * @param filterForm - the form where to add the filtering elem
     * @param featureName - the feature's basic name, like "Executing" or "Contracting". <b>Should not</b> contain the word "Agency" (it is added automatically) and should be Capitalized
     * @param roleCode - the role code, from Constants.ROLE_CODE_XXXXX_AGENCY
     * @param includeParent  - creates the parent-children id mapping
     */
    private static void addAgencyFilter(ReportsFilterPickerForm filterForm, String featureName, String roleCode, boolean includeParent)
    {
        if (!Character.isUpperCase(featureName.charAt(0)))
            throw new RuntimeException("invalid feature name: must be a single term beginning with an upper case" + featureName);
        if (featureName.contains("Agenc"))
            throw new RuntimeException("invalid feature name: should not contain the word 'Agency' or derivated' " + featureName);

        addAgencyFilterFaster(filterForm, featureName + " Agency", roleCode, featureName + " Agencies", "filter_" + featureName.toLowerCase() + "_agencies_div", "selected" + featureName + "Agency", includeParent);
    }

    /**
     * add to the Filter Form an element regarding filtering by a certain type of agencies, if feature is enabled.
     * @param filterForm - the form where to add the filtering elem
     * @param featureName - the feature name to be checked in the feature manager
     * @param roleCode - the role code
     * @param rootElementName - the root element name to be displayed on form
     * @param filterDivId - the id to contain the filter
     * @param selectId - the id of the generated select
     * @param includeParent -includes the parent ids of the objects
     */
    
    private static void addAgencyFilter(ReportsFilterPickerForm filterForm, String featureName, String roleCode, String rootElementName, String filterDivId, String selectId, boolean includeParent)
    {       
        if (FeaturesUtil.isVisibleModule("/Activity Form/Organizations/" + featureName) ) {
            Collection<AmpOrganisation> relevantAgencies = (ReportsUtil.getAllOrgByRoleOfPortfolio(roleCode));
            HierarchyListableUtil.changeTranslateable(relevantAgencies, false);
            HierarchyListableImplementation rootRelevantAgencies = new HierarchyListableImplementation("All " + rootElementName, "0", relevantAgencies);
            GroupingElement<HierarchyListableImplementation> relevantAgenciesElement = new GroupingElement<HierarchyListableImplementation>(rootElementName, filterDivId, rootRelevantAgencies, selectId);
            filterForm.getRelatedAgenciesElements().add(relevantAgenciesElement);
            if (includeParent) {
                for (AmpOrganisation donor:relevantAgencies) {
                    rootRelevantAgencies.getParentMapping().put(donor.getAmpOrgId(),donor.getOrgGrpId().getAmpOrgGrpId());
                }
            }
        }   
    }

    private static void addComponentOrganisations(ReportsFilterPickerForm filterForm, String name, String
            roleCode, String filterDivId, String selectId) {
        if (FeaturesUtil.isVisibleField(name)) {
            Collection<AmpOrganisation> relevantAgencies = (ReportsUtil.getComponentFundingOrgs(roleCode));
            HierarchyListableUtil.changeTranslateable(relevantAgencies, false);
            HierarchyListableImplementation rootRelevantAgencies = new HierarchyListableImplementation("All " + name,
                    "0", relevantAgencies);
            GroupingElement<HierarchyListableImplementation> relevantAgenciesElement = new
                    GroupingElement<HierarchyListableImplementation>(name, filterDivId, rootRelevantAgencies,
                    selectId);
            filterForm.getRelatedAgenciesElements().add(relevantAgenciesElement);
        }
    }
    
    private static void addAgencyFilterFaster(ReportsFilterPickerForm filterForm, String featureName, String roleCode, String rootElementName, String filterDivId, String selectId, boolean includeParent)
    {       
        
        if (FeaturesUtil.isVisibleModule("/Activity Form/Organizations/" + featureName)) {
            List<OrganizationSkeleton> relevantAgencies = ReportsUtil.getAllOrgByRoleOfPortfolioFaster(roleCode);
            HierarchyListableUtil.changeTranslateable(relevantAgencies, false);
            HierarchyListableImplementation rootRelevantAgencies = new HierarchyListableImplementation("All " + rootElementName, "0", relevantAgencies);
            GroupingElement<HierarchyListableImplementation> relevantAgenciesElement = new GroupingElement<HierarchyListableImplementation>(rootElementName, filterDivId, rootRelevantAgencies, selectId);
            filterForm.getRelatedAgenciesElements().add(relevantAgenciesElement);
            if (includeParent) {
                for (OrganizationSkeleton donor:relevantAgencies) {
                    rootRelevantAgencies.getParentMapping().put(donor.getAmpOrgId(),donor.getOrgGrpId());
                }
            }
        }   
    }
    
    /**
     * TODO: write description later
     * @param filterForm
     * @param featureName
     * @param sectorName
     * @param rootLabel
     * @param filterDiv
     * @param selectId
     * @param ampContext
     */
    private static void addSectorElement(ReportsFilterPickerForm filterForm, String fieldName, String sectorName, String rootLabel, String filterDiv, String selectId)
    {
        if (FeaturesUtil.isVisibleField(fieldName)){
            Collection<SectorSkeleton> ampSectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchyFaster(sectorName);
            HierarchyListableUtil.changeTranslateable(ampSectors, false);
            
            HierarchyListableImplementation rootAmpSectors  = new HierarchyListableImplementation(rootLabel, "0", ampSectors);
            GroupingElement<HierarchyListableImplementation> sectorsElement = new GroupingElement<HierarchyListableImplementation>(rootLabel, filterDiv, rootAmpSectors, selectId);
            filterForm.getSectorElements().add(sectorsElement);
        }
    }
    
    /**
     * add a financing location element to the FinancingLocationElements of a FilterForm
     * @param filterForm
     * @param fieldName  <i> if null, then always enabled</i>
     * @param rootLabel
     * @param financingModeKey
     * @param elementName
     * @param filterId
     * @param selectId
     * @param request
     * @param ampContext
     * @throws Exception
     */
    private static void addFinancingLocationElement(ReportsFilterPickerForm filterForm, String fieldName, String rootLabel, String financingModeKey, String elementName, String filterId, String selectId)
    {
        boolean enabled = (fieldName == null) ||
                ((fieldName != null) && (FeaturesUtil.isVisibleField(fieldName)));
        
        if (enabled) { 
            Collection<AmpCategoryValue> modeOfPaymentValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(financingModeKey, true);    
            HierarchyListableImplementation rootModeOfPayment = new HierarchyListableImplementation(rootLabel, "0", modeOfPaymentValues);
            GroupingElement<HierarchyListableImplementation> modeOfPaymentElement   =
                    new GroupingElement<HierarchyListableImplementation>(elementName, filterId, rootModeOfPayment, selectId);
            filterForm.getFinancingLocationElements().add(modeOfPaymentElement);
        }
    }
    
    /**
     * removes an element with the given name, if exists. Noop if it doesn't exist
     * @param hier
     * @param name
     */
    public static void removeElementByName(Collection<GroupingElement<HierarchyListableImplementation>> hier, String name)
    {
        Iterator<GroupingElement<HierarchyListableImplementation>> iter = hier.iterator();
        while (iter.hasNext())
            if (iter.next().getName().equals(name))
                iter.remove();
    }

    /**
     * fills the dropdowns part of ReportsFilterPickerForm pertaining to "settings"
     * @param filterForm
     */
    public static void fillSettingsFormDropdowns(ReportsFilterPickerForm filterForm, AmpARFilter reportFilter)
    {
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
        String selecteddecimalseparator  = String.valueOf((usedDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator()));
             
         if (!selecteddecimalseparator.equalsIgnoreCase(".") && !selecteddecimalseparator.equalsIgnoreCase(",") ){
             decimalseparators.add(selecteddecimalseparator);
         }
             
         decimalseparators.add(".");
         decimalseparators.add(",");
         //decimalseparators.add(TranslatorWorker.translateText("CUSTOM",request));
         filterForm.setAlldecimalSymbols(decimalseparators);
         
         ArrayList<String> groupseparators = new ArrayList<String>();
         String selectedgroupingseparator  = String.valueOf(usedDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator());
         
         if (!selectedgroupingseparator.equalsIgnoreCase(".") && !selectedgroupingseparator.equalsIgnoreCase(",") ){
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
    
    public static boolean isFalse(Boolean b)
    {
        if (b == null)
            return true;
        return (b.booleanValue() == false);
    }
    

    
    /**
     * selects all locations of a given type from entrySet
     * 
     * @param entrySet
     * @param typeCategoryValue an AmpCategoryValue object, defining the location type
     * @return
     */
    private static List<LocationSkeleton> filterLocationsByType(Collection<LocationSkeleton> entrySet, AmpCategoryValue typeCategoryValue) {
        List<LocationSkeleton> filteredLocations = new ArrayList<>();
        for (LocationSkeleton loc : entrySet) {
            if (loc.getCvId() == typeCategoryValue.getId()){
                filteredLocations.add(loc);
            }
        }
        return filteredLocations;
    }
    
    /**
     * gets default country based on settings
     * 
     * @param locations a map of LocationSkeleton, the location ID is the key
     * @return a LocationSkeleton of the default country
     */
    private static LocationSkeleton getDefaultCountry(Map<Long, LocationSkeleton> locations) {
        AmpCategoryValueLocations hibernatedLocation = DynLocationManagerUtil.getDefaultCountry();
        return locations.get(hibernatedLocation.getIdentifier());
    }
    

    /**
     * returns either the list of all the countries with sublocations in the system or the sole configured root country (see AMP-16857)
     * optimized for AMP-17807
     * @return
     */
    private static List<LocationSkeleton> getRootLocations(){       
        Map<Long, LocationSkeleton> allLocations = LocationSkeleton.populateSkeletonLocationsList();
        TeamMember currentMember = TeamUtil.getCurrentMember();
        AmpApplicationSettings ampAppSettings = null;
        boolean showAllCountries = false;
        
        if (currentMember != null && currentMember.getMemberId() != null) {
            AmpTeamMember ampCurrentMember = TeamMemberUtil.getAmpTeamMemberCached(currentMember.getMemberId());

            if (ampCurrentMember != null) {
                ampAppSettings = DbUtil.getTeamAppSettings(ampCurrentMember.getAmpTeam().getAmpTeamId());
                showAllCountries = ampAppSettings != null && ampAppSettings.getShowAllCountries();

            }
        } else {
            showAllCountries = FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.SHOW_ALL_COUNTRIES);
        }

        List<LocationSkeleton> filterCountries = new ArrayList<LocationSkeleton>();
        if (showAllCountries) {
            AmpCategoryValue layer = CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getAmpCategoryValueFromDB();
            if (layer == null) {
                logger.error("No Country value found in category Implementation Location. Please correct this.");
            } else {
                //Set<LocationSkeleton> countries = DynLocationManagerUtil.getLocationsByLayer(layer);
                List<LocationSkeleton> countries = filterLocationsByType(allLocations.values(), layer);
                //layer filtering already done above
                for (LocationSkeleton loc : countries) {
                    if (loc.getChildLocations() != null && !loc.getChildLocations().isEmpty()) {
                        //adding countries, so that our structure is hierarchic
                        filterCountries.add(loc);
                    }
                }
            }
        } else {
            
            filterCountries.add(getDefaultCountry(allLocations));
        }
        for (LocationSkeleton country: filterCountries)
            HierarchyListableUtil.changeTranslateable(country, false);
        return filterCountries;
    }

    
    
    /**
     * populate all the non-setting fields in a form, like drop-down lists, checkbox lists etc etc etc
     * <b>Always</b> call this function after populateForm, because it corrects some fields spoiled by it (fromYear, for example)
     * @param filterForm the form to populate
     * @throws Exception
     */
    public static void modeRefreshDropdowns(ReportsFilterPickerForm filterForm, int subsection, 
            AmpARFilter reportFilter) throws DgException {
                
        if ((subsection & AmpARFilter.FILTER_SECTION_SETTINGS) > 0)
        {
            fillSettingsFormDropdowns(filterForm, reportFilter);
        }
        
        if ((subsection & AmpARFilter.FILTER_SECTION_FILTERS) == 0)
            return; // the part below is too big to enclose in braces, so negating the condition and the action (return)
        // create filter dropdowns

        StopWatch.reset("Filters");
        StopWatch.next("Filters", true);

        TeamMember teamMember = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        /**
        * For filterPicker ver2
        */              
            
        StopWatch.next("Filters", true, "various fast stuff");
        StopWatch.next("Filters", true, "calendars end");       
                
        filterForm.setSectorElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
        filterForm.setProgramElements(new ArrayList<GroupingElement<AmpThemeSkeleton>>());      

        StopWatch.next("Filters", true, "before sectors");
//      private void addSectorElement(ReportsFilterPickerForm filterForm, String featureName, String sectorName, String rootLabel, String filterDiv, String selectId, ServletContext ampContext)
        addSectorElement(filterForm, "Sector", AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME, "Primary Sectors", "filter_sectors_div", "selectedSectors");
        addSectorElement(filterForm, "Secondary Sector", AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME, "Secondary Sectors", "filter_secondary_sectors_div", "selectedSecondarySectors");
        addSectorElement(filterForm, "Tertiary Sector",  AmpClassificationConfiguration.TERTIARY_CLASSIFICATION_CONFIGURATION_NAME,  "Tertiary Sectors",  "filter_tertiary_sectors_div",  "selectedTertiarySectors");
        addSectorElement(filterForm, "Quaternary Sector",
                AmpClassificationConfiguration.QUATERNARY_CLASSIFICATION_CONFIGURATION_NAME, "Quaternary Sectors",
                "filter_quaternary_sectors_div", "selectedQuaternarySectors");
        addSectorElement(filterForm, "Quinary Sector",
                AmpClassificationConfiguration.QUINARY_CLASSIFICATION_CONFIGURATION_NAME, "Quinary Sectors",
                "filter_quinary_sectors_div", "selectedQuinarySectors");
        addSectorElement(filterForm, "Sector Tag",      AmpClassificationConfiguration.TAG_CLASSIFICATION_CONFIGURATION_NAME,  "Tag Sector",              "filter_tag_sectors_div",       "selectedTagSectors");
                        
        
        StopWatch.next("Filters", true, "before programs");
        if ( FeaturesUtil.isVisibleModule("National Planning Dashboard") )
        {
            Map<Long, AmpThemeSkeleton> allPrograms = ProgramUtil.getAllThemesFaster();
            //this is now done automatically in the getallthemesfaster
            //HashMap<Long, AmpTheme> progMap       = ProgramUtil.prepareStructure(allPrograms);
            
            AmpActivityProgramSettings primaryPrgSetting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
            AmpThemeSkeleton primaryProg = null;
            //List<AmpTheme> primaryPrograms;       
            if (primaryPrgSetting!=null && primaryPrgSetting.getDefaultHierarchy() != null) {
                //primaryProg= ProgramUtil.getAmpThemesAndSubThemesHierarchy(primaryPrgSetting.getDefaultHierarchy());
                primaryProg = allPrograms.get(primaryPrgSetting.getDefaultHierarchyId() );
                HierarchyListableUtil.changeTranslateable(primaryProg, false);
                GroupingElement<AmpThemeSkeleton> primaryProgElement = new GroupingElement<AmpThemeSkeleton>("Primary Program", "filter_primary_prog_div", primaryProg, "selectedPrimaryPrograms");
                filterForm.getProgramElements().add(primaryProgElement);
            }
            StopWatch.next("Filters", true, "After Primary Programs");
            AmpThemeSkeleton secondaryProg = null;
            AmpActivityProgramSettings secondaryPrg = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM);
//          List<AmpTheme> secondaryPrograms;       
            if (secondaryPrg!=null && secondaryPrg.getDefaultHierarchy() != null) {
                //secondaryProg= ProgramUtil.getAmpThemesAndSubThemesHierarchy(secondaryPrg.getDefaultHierarchy());
                secondaryProg   = allPrograms.get(secondaryPrg.getDefaultHierarchyId() );
                HierarchyListableUtil.changeTranslateable(secondaryProg, false);
                GroupingElement<AmpThemeSkeleton> secondaryProgElement = new GroupingElement<AmpThemeSkeleton>("Secondary Program", "filter_secondary_prog_div", secondaryProg, "selectedSecondaryPrograms");
                filterForm.getProgramElements().add(secondaryProgElement);
            }       
            StopWatch.next("Filters", true, "After Secondary Programs");
            
            AmpActivityProgramSettings natPlanSetting       = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
            
            //List<AmpTheme> nationalPlanningObjectives;
            if (natPlanSetting != null && natPlanSetting.getDefaultHierarchy() != null) {
                //AmpTheme nationalPlanningProg = ProgramUtil.getAmpThemesAndSubThemesHierarchy(natPlanSetting.getDefaultHierarchy());
                AmpThemeSkeleton nationalPlanningProg   = allPrograms.get(natPlanSetting.getDefaultHierarchyId() );
                HierarchyListableUtil.changeTranslateable(nationalPlanningProg, false);
                GroupingElement<AmpThemeSkeleton> natPlanProgElement = new GroupingElement<AmpThemeSkeleton>("National Planning Objective", "filter_nat_plan_obj_div", nationalPlanningProg, "selectedNatPlanObj");         
                filterForm.getProgramElements().add(natPlanProgElement);
            }
            StopWatch.next("Filters", true, "After NPO");
        }
        StopWatch.next("Filters", true, "After Programs");
        
        
        StopWatch.next("Filters", true, "After Programs");
        //long a = System.currentTimeMillis();
        List<OrgTypeSkeleton> donorTypes = DbUtil.getAllOrgTypesFaster();
        List<OrgGroupSkeleton> donorGroups = /*ARUtil.filterDonorGroups(*/DbUtil.getAllOrgGroupsOfPortfolioFaster();

        Collections.sort(donorGroups);
        Collections.sort(donorTypes);
        
        HierarchyListableUtil.changeTranslateable(donorTypes, false);
        HierarchyListableUtil.changeTranslateable(donorGroups, false);
        
        filterForm.setDonorElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
        
        HierarchyListableImplementation rootOrgType = new HierarchyListableImplementation("All Donor Types", "0", donorTypes);
        GroupingElement<HierarchyListableImplementation> donorTypeElement = new GroupingElement<HierarchyListableImplementation>("Donor Types", "filter_donor_types_div", rootOrgType, "selectedDonorTypes");
        filterForm.getDonorElements().add(donorTypeElement);
        
        HierarchyListableImplementation rootOrgGroup = new HierarchyListableImplementation("All Donor Groups", "0", donorGroups);
        GroupingElement<HierarchyListableImplementation> donorGroupElement = new GroupingElement<HierarchyListableImplementation>("Donor Groups", "filter_donor_groups_div", rootOrgGroup, "selectedDonorGroups");
        for (OrgGroupSkeleton group:donorGroups) {
            rootOrgGroup.getParentMapping().put(group.getAmpOrgGrpId(),group.getOrgTypeId());
        }
        filterForm.getDonorElements().add(donorGroupElement);
        StopWatch.next("Filters", true, "After beta1");
        Collection<AmpOrganisation> donors = ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_DONOR);
        HierarchyListableUtil.changeTranslateable(donors, false);
        HierarchyListableImplementation rootDonors = new HierarchyListableImplementation("All Donors", "0", donors);
        
        GroupingElement<HierarchyListableImplementation> donorsElement  = new GroupingElement<HierarchyListableImplementation>("Donor Agencies", "filter_donor_agencies_div", rootDonors, "selectedDonnorAgency");
        for (AmpOrganisation donor:donors) {
            if (donor.getAmpOrgId() == null || donor.getOrgGrpId() == null || donor.getOrgGrpId().getAmpOrgGrpId() == null) {
                logger.error("oopsie for " + donor);
            } else {
                rootDonors.getParentMapping().put(donor.getAmpOrgId(),donor.getOrgGrpId().getAmpOrgGrpId());
            }
        }
        filterForm.getDonorElements().add(donorsElement);
        
        filterForm.setRelatedAgenciesElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
        
        
        
        
        StopWatch.next("Filters", true, "Donor stuff");
        //------------------begin here-------------------------------------------------
        //  private void addAgencyFilter(ReportsFilterPickerForm filterForm, String featureName, String roleCode, String rootElementName, String filderDivId, String selectId, ServletContext ampContext)
        if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Executing Agency")){
            addAgencyFilter(filterForm, "Executing", Constants.ROLE_CODE_EXECUTING_AGENCY, false);
        }
        if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Contracting Agency")){
            addAgencyFilter(filterForm, "Contracting", Constants.ROLE_CODE_CONTRACTING_AGENCY, true);
        }
        if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Implementing Agency")){
            addAgencyFilter(filterForm, "Implementing", Constants.ROLE_CODE_IMPLEMENTING_AGENCY, false);
        }
        if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Responsible Organization")){
            addAgencyFilter(filterForm, "Responsible Organization", Constants.ROLE_CODE_RESPONSIBLE_ORG, "Responsible Agencies", "filter_responsible_agencies_div", "selectedresponsibleorg", false);
        }
        if(FeaturesUtil.isVisibleModule("/Activity Form/Organizations/Beneficiary Agency")){
            addAgencyFilter(filterForm, "Beneficiary", Constants.ROLE_CODE_BENEFICIARY_AGENCY, false);
        }

        addComponentOrganisations(filterForm, ColumnConstants.COMPONENT_FUNDING_ORGANIZATION, Constants
                        .COMPONENT_FUNDING_ORGANIZATION,
                "filter_component_funding_div", "selectedComponentFundingOrg");
        addComponentOrganisations(filterForm, ColumnConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION, Constants
                        .COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION, "filter_component_second_responsible_div",
                "selectedComponentSecondResponsibleOrg");

        // Contracting Agency Groups, based off Donor Groups
        // stimate domnule GARTNER, ce face filterDonorGroups in afara de a exclude grupurile cu "guv" si "gouv" in nume din lista? E nevoie de ei aici? 
        if(FeaturesUtil.isVisibleField("Contracting Agency Groups")){
            
            Collection<AmpOrgGroup> contractingAgencyGroups = /*ARUtil.filterDonorGroups(*/DbUtil.getAllContractingAgencyGroupsOfPortfolio()/*)*/;
            HierarchyListableUtil.changeTranslateable(contractingAgencyGroups, false);
    
            HierarchyListableImplementation rootContractingAgenciesGroup = new HierarchyListableImplementation("All Contracting Agency Groups", "0", contractingAgencyGroups);
            GroupingElement<HierarchyListableImplementation> contractingAgencyGroupElement  = new GroupingElement<HierarchyListableImplementation>("Contracting Agency Groups", "filter_contracting_agency_groups_div", rootContractingAgenciesGroup, "selectedContractingAgencyGroups");
            filterForm.getRelatedAgenciesElements().add(contractingAgencyGroupElement);
        }
        
        filterForm.setFinancingLocationElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
        
        //-------------------end here--------------------------------------------------------------------------------------
        
        StopWatch.next("Filters", true, "Agency stuff");
        
        //private void addFinancingLocationElement(ReportsFilterPickerForm filterForm, String fieldName, String rootLabel, String financingModeKey, String elementName, String filterId, String selectId, HttpServletRequest request, ServletContext ampContext) throws Exception
        addFinancingLocationElement(filterForm, null, "All Financing Instrument Values", CategoryConstants.FINANCING_INSTRUMENT_KEY, "Financing Instrument", "filter_financing_instr_div", "selectedFinancingInstruments");
        
        addFinancingLocationElement(filterForm, null, "All "+ TranslatorWorker.translateText(CategoryConstants.FUNDING_STATUS_NAME) +" Values", CategoryConstants.FUNDING_STATUS_KEY, "Funding Status", "filter_funding_status_div", "selectedFundingStatus");
        
        if (filterForm.getPledged() != null && filterForm.getPledged() && FeaturesUtil.isVisibleField("Pledge Funding - Aid Modality"))
            addFinancingLocationElement(filterForm, null, "All Aid Modality Values", CategoryConstants.MODALITIES_KEY, "Aid Modality", "filter_aid_modality_div", "selectedAidModalities");
        
        addFinancingLocationElement(filterForm, null, "All Type of Assistance Values", CategoryConstants.TYPE_OF_ASSISTENCE_KEY, "Type of Assistance", "filter_type_of_assistance_div", "selectedTypeOfAssistance");
        
        addFinancingLocationElement(filterForm, null, "All Expenditure Class Values", CategoryConstants.EXPENDITURE_CLASS_KEY, "Expenditure Class", "filter_expenditure_class_div", "selectedExpenditureClasses");
        
        if (FeaturesUtil.isVisibleField("Mode of Payment") && isFalse(filterForm.getPledged())) {
            addFinancingLocationElement(filterForm, "Mode of Payment", "All Mode of Payment Values", CategoryConstants.MODE_OF_PAYMENT_KEY, "Mode of Payment", "filter_mode_of_payment_div", "selectedModeOfPayment");
        }else{
            removeElementByName(filterForm.getFinancingLocationElements(), "Mode of Payment");
        }
        if (FeaturesUtil.isVisibleField("Concessionality Level")) {
            addFinancingLocationElement(filterForm, "Concessionality Level", "All Concessionality Level Values", 
                    CategoryConstants.CONCESSIONALITY_LEVEL_KEY, "Concessionality Level", 
                    "filter_concessionality_level_div", "selectedConcensionalityLevel");
        } else{
            removeElementByName(filterForm.getFinancingLocationElements(), "Concessionality Level");
        }
        if (FeaturesUtil.isVisibleField("Project Category")) {
            addFinancingLocationElement(filterForm, "Project Category", "All Project Category Values", CategoryConstants.PROJECT_CATEGORY_KEY, "Project Category", "filter_project_category_div", "selectedProjectCategory");
        }
        
        addFinancingLocationElement(filterForm, "Activity Pledges Title", "All pledges", CategoryConstants.PLEDGES_NAMES_KEY, "Pledges titles", "filter_activity_peldges_title_div", "selectedActivityPledgesTitle");

        addDateFilter("Effective Funding Date", "EffectiveFunding",
                "Effective Funding Date", filterForm.getDynamicEffectiveFundingFilter(), "filter_efd_div", filterForm.getFinancingLocationElements());
        addDateFilter("Funding Closing Date", "FundingClosing",
                "Funding Closing Date", filterForm.getDynamicFundingClosingFilter(), "filter_fcd_div", filterForm.getFinancingLocationElements());
        filterForm.setOtherCriteriaElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>() );
        if (true) { //Here needs to be a check to see if the field/feature is enabled
            Collection<AmpCategoryValue> activityStatusValues   = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY, true);  
            HierarchyListableImplementation rootActivityStatus  = new HierarchyListableImplementation("All", "0", activityStatusValues);
            GroupingElement<HierarchyListableImplementation> activityStatusElement  =
                    new GroupingElement<HierarchyListableImplementation>("Status", "filter_activity_status_div", 
                            rootActivityStatus, "selectedStatuses");
            filterForm.getOtherCriteriaElements().add(activityStatusElement);
        }
        
        if (true) { //Here needs to be a check to see if the field/feature is enabled
            Collection<AmpTeam> creatorsList    = TeamUtil.getAllRelatedTeams();
            List<HierarchyListableImplementation> children  = new ArrayList<HierarchyListableImplementation>();
            HierarchyListableImplementation rootCreators = new HierarchyListableImplementation("All", "0", children);
            for(AmpTeam ampTeam:creatorsList) {
                // isolated workspaces shouldn't be added
                if (!ampTeam.getIsolated()) {
                    children.add(new HierarchyListableImplementation(ampTeam.getName(), ampTeam.getAmpTeamId().toString()));
                }
            }

            //sort workspace list
            Collections.sort(children, new Comparator<HierarchyListableImplementation>() {
                @Override
                public int compare(HierarchyListableImplementation o1, HierarchyListableImplementation o2) {
                    return o1.getLabel().compareTo(o2.getLabel());
                }
            });

            GroupingElement<HierarchyListableImplementation> activityStatusElement  =
                    new GroupingElement<HierarchyListableImplementation>("Workspace", "filter_workspace_div", 
                            rootCreators, "selectedWorkspaces");
            filterForm.getOtherCriteriaElements().add(activityStatusElement);
        }
        if (FeaturesUtil.isVisibleField("Project Implementing Unit")) {         
            Collection<AmpCategoryValue> projectImplementingUnits   = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY, true);
            HierarchyListableImplementation rootProjectImplementingUnit = new HierarchyListableImplementation("All Project Implementing Units", "0", projectImplementingUnits);
            GroupingElement<HierarchyListableImplementation> projectImplUnitElement =new GroupingElement<HierarchyListableImplementation>("Project Implementing Unit", "filter_project_impl_unit_div", 
                    rootProjectImplementingUnit, "selectedProjectImplUnit");
            filterForm.getOtherCriteriaElements().add(projectImplUnitElement);
            
        }
        StopWatch.next("Filters", true, "AFTER CATEGORY VALUES");
        
        if (FeaturesUtil.isVisibleFeature("Disbursement Orders")) { 
            Collection<HierarchyListableImplementation> children    = new ArrayList<HierarchyListableImplementation>();
            HierarchyListableImplementation rootDisbursementOrders  = new HierarchyListableImplementation("All", "-1", children);
            children.add(new HierarchyListableImplementation("Not Rejected", "0"));
            children.add(new HierarchyListableImplementation("Rejected", "1"));
            GroupingElement<HierarchyListableImplementation> disbOrdersElement  =
                    new GroupingElement<HierarchyListableImplementation>("Disbursement Orders", "filter_disb_orders_div", rootDisbursementOrders, "disbursementOrders");
            filterForm.getFinancingLocationElements().add(disbOrdersElement);
        }
        if (true && isFalse(filterForm.getPledged())) {//Here needs to be a check to see if the field/feature is enabled
            Collection<AmpCategoryValue> budgetCategoryValues   = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_BUDGET_KEY, true);  
            HierarchyListableImplementation rootBudgetCategory  = new HierarchyListableImplementation("All", "0", budgetCategoryValues);
            GroupingElement<HierarchyListableImplementation> disbOrdersElement  =
                new GroupingElement<HierarchyListableImplementation>("Activity Budget", "filter_on_budget_div", rootBudgetCategory, "selectedBudgets");
            filterForm.getFinancingLocationElements().add(disbOrdersElement);
        }
        else 
            removeElementByName(filterForm.getFinancingLocationElements(), "Activity Budget");
        
        if (true) {
            StopWatch.next("Filters", true, "start rendering regions");


            List<LocationSkeleton> filterCountries = getRootLocations();
            StopWatch.next("Filters", true, "after get root locations");
          
            HierarchyListableImplementation rootRegions;

            if (filterCountries.size() > 1) {
                rootRegions = new HierarchyListableImplementation("All Locations", "0", filterCountries);

                // this code makes no sense, added in http://tools.digijava.org/fisheye/changelog/amp?cs=22910 and then perpetuated via refactorings
                for (LocationSkeleton country: filterCountries) {
                    HierarchyListableImplementation countryRoot = new HierarchyListableImplementation(country.getLabel(), country.getUniqueId(), country.getChildren());
                }

            } else {
                rootRegions = new HierarchyListableImplementation("All Regions", "0");
                for (LocationSkeleton rootCandidate:filterCountries) {
                    rootRegions.setChildren(rootCandidate.getChildren());
                    break;
                }
                /*
                 * this looks potentially buggy
                rootRegions.setChildren( filterCountries.get(0).getChildren());
                */
            }


            GroupingElement<HierarchyListableImplementation> regionsElement =
                    new GroupingElement<HierarchyListableImplementation>("Regions", "filter_regions_div", 
                            rootRegions, "regionSelected");
            filterForm.getFinancingLocationElements().add(regionsElement);
            StopWatch.next("Filters", true, "end rendering regions");
        }
        if( FeaturesUtil.isVisibleField("Joint Criteria") && 
                FeaturesUtil.isVisibleField("Government Approval Procedures")) {
            Collection<HierarchyListableImplementation> children    = 
                new ArrayList<HierarchyListableImplementation>();
            HierarchyListableImplementation activitySettings    = new HierarchyListableImplementation("Both Settings", "-1", children);
            if ( FeaturesUtil.isVisibleField("Joint Criteria") ) {
                HierarchyListableImplementation jointCriteriaDO = new HierarchyListableImplementation("Only Projects Under Joint Criteria", ONLY_JOINT_CRITERIA);
                children.add(jointCriteriaDO);
            }
            if ( FeaturesUtil.isVisibleField("Government Approval Procedures")) {
                HierarchyListableImplementation govProceduresDO = new HierarchyListableImplementation("Only Projects Having Government Approval Procedures", ONLY_GOV_PROCEDURES);
                children.add(govProceduresDO);
            }
            
            if (!filterForm.isPledgeReport()){
                GroupingElement<HierarchyListableImplementation> activitySettingsElement    =
                    new GroupingElement<HierarchyListableImplementation>("Activity Display Settings", "filter_act_settings_div", activitySettings, "selectedActivitySettings");
                filterForm.getFinancingLocationElements().add(activitySettingsElement);
            }
        }
        
        if ((!filterForm.isPledgeReport()) && (FeaturesUtil.isVisibleField("Only show projects related to pledges"))) {
            List<HierarchyListableImplementation> children = new ArrayList<HierarchyListableImplementation>();
            HierarchyListableImplementation activitySettings = new HierarchyListableImplementation("All Activities", "-1", children);
            {
                HierarchyListableImplementation jointCriteriaDO = new HierarchyListableImplementation("Only show activities related to pledges", 
                        Integer.toString(AmpARFilter.SELECTED_ACTIVITY_PLEDGES_SETTINGS_WITH_PLEDGES_ONLY));
                children.add(jointCriteriaDO);
            }
//          {
//              HierarchyListableImplementation jointCriteriaDO = new HierarchyListableImplementation();
//              jointCriteriaDO.setLabel("Show activities not related to pledges");
//              jointCriteriaDO.setUniqueId(Integer.toString(AmpARFilter.SELECTED_ACTIVITY_PLEDGES_SETTINGS_WITHOUT_PLEDGES_ONLY));
//              children.add(jointCriteriaDO);
//          }
            
            GroupingElement<HierarchyListableImplementation> activitySettingsElement    =
                new GroupingElement<HierarchyListableImplementation>("Activities with pledges", "filter_act_pledge_settings_div", activitySettings, "selectedActivityPledgesSettings");
            filterForm.getFinancingLocationElements().add(activitySettingsElement);
        }
        
        if (true) { //Here needs to be a check to see if the field/feature is enabled
            if (teamMember != null){
                List<HierarchyListableImplementation> children = new ArrayList<HierarchyListableImplementation>();
                HierarchyListableImplementation rootApprovalStatus  = new HierarchyListableImplementation("All", "-1", children);
                children.add(new HierarchyListableImplementation(TranslatorWorker.translateText("New Draft"), "1"));
                children.add(new HierarchyListableImplementation(TranslatorWorker.translateText("New Unvalidated"), "2"));
                children.add(new HierarchyListableImplementation(TranslatorWorker.translateText("Validated Activities"), "4"));
                children.add(new HierarchyListableImplementation(TranslatorWorker.translateText("Existing Draft"), "3"));
                children.add(new HierarchyListableImplementation(TranslatorWorker.translateText("Existing Unvalidated"), "0"));
                
                GroupingElement<HierarchyListableImplementation> approvalStatusElement  =
                        new GroupingElement<HierarchyListableImplementation>("Approval Status", "filter_approval_status_div",  rootApprovalStatus, "approvalStatusSelected");
                filterForm.getOtherCriteriaElements().add(approvalStatusElement);
            }
        }
        
        if ( FeaturesUtil.isVisibleField("Line Ministry Rank")) {
            Collection<HierarchyListableImplementation> children = new ArrayList<HierarchyListableImplementation>();
            HierarchyListableImplementation rootLineMinRank = new HierarchyListableImplementation("All", "-1", children);
            for (int i = 1; i < 6 ; i ++) {
                children.add(new HierarchyListableImplementation(Integer.toString(i), Integer.toString(i)));
            }
            GroupingElement<HierarchyListableImplementation> lineMinRankElement =
                    new GroupingElement<HierarchyListableImplementation>("Line Ministry Rank", "filter_line_min_rank_div", rootLineMinRank, "lineMinRanks");
            filterForm.getOtherCriteriaElements().add(lineMinRankElement);
        }
        
        if (FeaturesUtil.isVisibleFeature("Archived") && teamMember != null)
            addBooleanElementToFilter(filterForm, "Archived", "filter_archived_div", "selectedArchivedStatus", "Non-archived Activities", "Archived Activities");
        
        if (FeaturesUtil.isVisibleField("Humanitarian Aid") && !filterForm.isPledgeReport())
            addBooleanElementToFilter(filterForm, "Humanitarian Aid", "filter_humanitarian_aid_div", "selectedHumanitarianAid", "Yes", "No");
        
        if (FeaturesUtil.isVisibleField("Disaster Response Marker") && !filterForm.isPledgeReport())
            addBooleanElementToFilter(filterForm, "Disaster Response Marker", "filter_disaster_response_div", "selectedDisasterResponse", "Yes", "No");
        
        if ( FeaturesUtil.isVisibleFeature("Multi Donor")) {
            Collection<HierarchyListableImplementation> children    = 
                new ArrayList<HierarchyListableImplementation>();
            HierarchyListableImplementation rootMultiDonor  = new HierarchyListableImplementation("All", "-1", children);
            children.add(new HierarchyListableImplementation("Yes", "yes"));
            children.add(new HierarchyListableImplementation("No", "no"));
            
            GroupingElement<HierarchyListableImplementation> lineMinRankElement =
                    new GroupingElement<HierarchyListableImplementation>("Multiple Donors", "filter_multi_donor_div", rootMultiDonor, "selectedMultiDonor");
            filterForm.getOtherCriteriaElements().add(lineMinRankElement);
        }
        
        addDateFilter("Actual Start Date", "ActivityStart", "Actual Start Date",
                filterForm.getDynamicActivityStartFilter(), "filter_activity_start_date_div", filterForm.getOtherCriteriaElements());

        addDateFilter("Issue Date", "Issue", "Issue Date",
                filterForm.getDynamicIssueFilter(), "filter_issue_date_div", filterForm
                        .getOtherCriteriaElements());

        addDateFilter("Actual Completion Date", "ActivityActualCompletion",
                "Actual Completion Date", filterForm.getDynamicActivityActualCompletionFilter(),
                "filter_activity_actual_completion_date_div", filterForm.getOtherCriteriaElements());
        
        addDateFilter("Final Date for Contracting", "ActivityFinalContracting",
                "Final Date for Contracting", filterForm.getDynamicActivityFinalContractingFilter(), "filter_activity_final_contracting_date_div", filterForm.getOtherCriteriaElements());
    
        addDateFilter("Actual Approval Date", "ActualApproval",
                "Actual Approval Date", filterForm.getDynamicActualApprovalFilter(), "filter_actual_approval_date_div",
                filterForm.getOtherCriteriaElements());
        
        addDateFilter("Proposed Approval Date", "ProposedApproval",
                "Proposed Approval Date", filterForm.getDynamicProposedApprovalFilter(), "filter_proposed_approval_date_div", filterForm.getOtherCriteriaElements());
        
        //Finance date filter
        addDateFilter(null, "", "Date Filter", filterForm.getDynamicDateFilter(), "filter_date_div", filterForm.getOtherCriteriaElements());

        if (FeaturesUtil.isVisibleField(ColumnConstants.PERFORMANCE_ALERT_LEVEL)) {
            Collection<AmpCategoryValue> categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(
                    CategoryConstants.PERFORMANCE_ALERT_LEVEL_KEY, true);
            HierarchyListableImplementation tree = new HierarchyListableImplementation("All", "0", categoryValues);
            GroupingElement<HierarchyListableImplementation> groupingElement =
                    new GroupingElement<>("Performance Alert Level", "filter_performance_alert_level_div",
                            tree, "selectedPerformanceAlertLevels");
            filterForm.getOtherCriteriaElements().add(groupingElement);
        }

        if (FeaturesUtil.isVisibleField(ColumnConstants.PERFORMANCE_ALERT_TYPE)) {
            List<HierarchyListableImplementation> children = new ArrayList<HierarchyListableImplementation>();
            HierarchyListableImplementation rootPerformanceRuleType =
                    new HierarchyListableImplementation("All", "-1", children);

            PerformanceRuleManager.PERF_ALERT_TYPE_TO_ID.entrySet().forEach(e -> {
                children.add(new HierarchyListableImplementation(TranslatorWorker.translateText(
                        PerformanceRuleManager.PERF_ALERT_TYPE_TO_DESCRIPTION.get(e.getKey())),
                        String.valueOf(e.getValue())));
            });

            GroupingElement<HierarchyListableImplementation> perfGroupingElement =
                    new GroupingElement<>("Performance Alert Type", "filter_performance_alert_type_div",
                            rootPerformanceRuleType, "selectedPerformanceAlertTypes");
            filterForm.getOtherCriteriaElements().add(perfGroupingElement);
        }

        Collection<AmpIndicatorRiskRatings> meRisks = MEIndicatorsUtil.getAllIndicatorRisks();
        for (AmpIndicatorRiskRatings element:meRisks) {
            String value = element.getRatingName();
            String key = KEY_RISK_PREFIX + value.toLowerCase();
            key = key.replaceAll(" ", "");
            String msg = CategoryManagerUtil.translate(key, value);
            element.setRatingName(msg);
        }
        
        Collection<AmpIndicatorRiskRatings> allIndicatorRisks = meRisks;
        // Collection regions=LocationUtil.getAllVRegions();
        //filterForm.setCurrencies(currency);
        // filterForm.setDonors(donors);
        filterForm.setRisks(allIndicatorRisks);
        

        filterForm.setFromMonths(new ArrayList<BeanWrapperImpl>());
        filterForm.setToMonths(new ArrayList<BeanWrapperImpl>());

        filterForm.setCountYears(new ArrayList<BeanWrapperImpl>());
        filterForm.setComputedYearsRange(new ArrayList<BeanWrapperImpl>());
        filterForm.setActualAppYearsRange(new ArrayList<BeanWrapperImpl>());
        filterForm.setApprovalStatusSelectedCollection(new ArrayList());            
        
        int curYear = new GregorianCalendar().get(Calendar.YEAR);
        
        for (long i = curYear-10; i < curYear; i ++) {
            filterForm.getComputedYearsRange().add(new BeanWrapperImpl(new Long(i)));
            filterForm.getActualAppYearsRange().add(new BeanWrapperImpl(new Long(i)));
        }
        
        for (long i = 10; i <= 100; i += 10) {
            filterForm.getCountYears().add(new BeanWrapperImpl(new Long(i)));
        }


        if (filterForm.getFromYear() == null) {
            // Long
            // fromYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE));
            filterForm.setFromYear(-1l);
        }

        if (filterForm.getToYear() == null) {
            // Long
            // toYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));
            filterForm.setToYear(-1l);
        }

        for (int i = 1; i <= 12; i++) {
            filterForm.getFromMonths().add(new BeanWrapperImpl(new Integer(i)));
            filterForm.getToMonths().add(new BeanWrapperImpl(new Integer(i)));
        }

        if (filterForm.getFromMonth() == null)
            filterForm.setFromMonth(-1);

        if (filterForm.getToMonth() == null)
            filterForm.setToMonth(-1);

        /*--------------------------*/
        // create the pageSizes Collection for the dropdown
        List<BeanWrapper> pageSizes = new ArrayList<BeanWrapper>();
        
        pageSizes.add(new BeanWrapperImpl(new String("A0")));
        pageSizes.add(new BeanWrapperImpl(new String("A1")));
        pageSizes.add(new BeanWrapperImpl(new String("A2")));
        pageSizes.add(new BeanWrapperImpl(new String("A3")));
        pageSizes.add(new BeanWrapperImpl(new String("A4")));
        
        filterForm.setPageSizes(pageSizes);
                
        StopWatch.next("Filters", true, "end refreshDropDowns");
    }

    protected static void addBooleanElementToFilter(ReportsFilterPickerForm filterForm, String name, String divId, String propertyName, String positiveLabel, String negativeLabel) {
        Collection<HierarchyListableImplementation> children = new ArrayList<HierarchyListableImplementation>();
        HierarchyListableImplementation rootArchivedStatus  = new HierarchyListableImplementation("All", "0", children);
        children.add(new HierarchyListableImplementation(TranslatorWorker.translateText(positiveLabel), "1"));
        children.add(new HierarchyListableImplementation(TranslatorWorker.translateText(negativeLabel), "2"));

        GroupingElement<HierarchyListableImplementation> archivedElement = new GroupingElement<HierarchyListableImplementation>(name, divId, rootArchivedStatus, propertyName);
        filterForm.getOtherCriteriaElements().add(archivedElement);
    }
    
    /**
     * generate a session based AmpARFilter object based on the form selections and forward to different actions (depending on the request source)
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
    
        if ( filterForm.getSourceIsReportWizard() != null && filterForm.getSourceIsReportWizard() ) {
            return mapping.findForward("reportWizard");
        }
        if ( request.getParameter("queryEngine") != null && "true".equals(request.getParameter("queryEngine")) ) {
            return mapping.findForward("queryView");
        }
            
        if (arf.isPublicView()){
            return mapping.findForward(arf.isWidget() ? "publicView" : "reportView");
        }
        
        if (arf.isWidget()){
            return mapping.findForward("mydesktop");
        }else{
            return mapping.findForward("reportView");
        }
    }

    /**
     * utility proxy for FilterUtil.getOrCreateFilter
     * @param filterForm
     * @return
     */
    public static AmpARFilter getOrCreateFilter(ReportsFilterPickerForm filterForm)
    {
        
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
     * @return reference to either the created or edited AmpARFilter instance. (it can be found in RCD.getFilter() anyway)
     * @throws DgException - exceptions from deep inside AMP's bowels
     */
    public static AmpARFilter createOrFillFilter(ReportsFilterPickerForm filterForm, int subsection) throws DgException
    {
        AmpARFilter arf = getOrCreateFilter(filterForm);

        if ((subsection & AmpARFilter.FILTER_SECTION_FILTERS) > 0)          
            fillFilterFromFilterForm(arf, filterForm);
        
        if ((subsection & AmpARFilter.FILTER_SECTION_SETTINGS) > 0)
            fillFilterFromSettingsForm(arf, filterForm);

        arf.signalSettingsHaveBeenApplied();
        return arf;
    }
    
    public static AmpARFilter createOrResetFilter(ReportsFilterPickerForm filterForm, int subsection) throws DgException
    {
        AmpARFilter arf = getOrCreateFilter(filterForm);
        
        if ((subsection & AmpARFilter.FILTER_SECTION_FILTERS) > 0)          
            arf.fillWithDefaultsFilter(arf.getAmpReportId());
        
        if ((subsection & AmpARFilter.FILTER_SECTION_SETTINGS) > 0)
            arf.fillWithDefaultsSettings();
        
        arf.postprocess();
        return arf;
    }
    /**
     * pumps the "Tab / Report Settings" part from the form to the filter
     * @param arf
     * @param filterForm
     * @see #fillFilterFromFilterForm(AmpARFilter, ReportsFilterPickerForm) for copying the settings part
     */
    public static void fillFilterFromSettingsForm(AmpARFilter arf, ReportsFilterPickerForm filterForm)
    {
        //DecimalFormat custom = new DecimalFormat();
        //DecimalFormatSymbols ds = new DecimalFormatSymbols();
        Character decimalSeparator  = !"CUSTOM".equalsIgnoreCase(filterForm.getCustomDecimalSymbol()) ? 
                filterForm.getCustomDecimalSymbol().charAt(0) : filterForm.getCustomDecimalSymbolTxt().charAt(0);       
        arf.setDecimalseparator(decimalSeparator.toString());

        boolean useGroupings = filterForm.getCustomUseGrouping() != null && filterForm.getCustomUseGrouping().booleanValue() == true;
        arf.setCustomusegroupings(useGroupings);
        
        if (useGroupings)
        {
            Character groupingSeparator = !"CUSTOM".equalsIgnoreCase(filterForm.getCustomGroupCharacter()) ? 
                filterForm.getCustomGroupCharacter().charAt(0) : filterForm.getCustomGroupCharacterTxt().charAt(0);     
            arf.setGroupingseparator( groupingSeparator.toString() );
            arf.setGroupingsize(filterForm.getCustomGroupSize());
        }
        
        //custom.setMaximumFractionDigits((filterForm.getCustomDecimalPlaces() != -1) ? filterForm.getCustomDecimalPlaces() : 99);

        arf.setAmountinthousand(filterForm.getAmountinthousands());     
        
        Integer maximumDecimalPlaces    = filterForm.getCustomDecimalPlaces();
        if ( maximumDecimalPlaces == -2 ) {//CUSTOM
            arf.setMaximumFractionDigits(filterForm.getCustomDecimalPlacesTxt());
        }
        else if (maximumDecimalPlaces > -1)
            arf.setMaximumFractionDigits(maximumDecimalPlaces );
        else{
            DecimalFormat defaultDecimalFormat = FormatHelper.getDecimalFormat();
            arf.setMaximumFractionDigits(defaultDecimalFormat.getMaximumFractionDigits());
        }
                
        AmpCurrency currency;
        if (filterForm.getCurrency() != null){
            currency = (AmpCurrency) Util.getSelectedObject(AmpCurrency.class, filterForm.getCurrency());
        }else{
            currency = (AmpCurrency) Util.getSelectedObject(AmpCurrency.class, filterForm.getDefaultCurrency());
        }
            
        arf.setCurrency(currency);
        
        //TODO-CONSTANTIN: these fields are absent from the form (not rendered in html), so they are always NULL here
        if (filterForm.getRenderStartYear() != null)
            arf.setRenderStartYear(filterForm.getRenderStartYear());
        
        if (filterForm.getRenderEndYear() != null)
            arf.setRenderEndYear(filterForm.getRenderEndYear());

        AmpFiscalCalendar selcal =  null;
        if (filterForm.getCalendar() != null) {
            selcal = (AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class, filterForm.getCalendar());
        } else {
            selcal = (AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class, filterForm.getDefaultCalendar());
        }

        arf.setCalendarType(selcal);
        arf.buildCustomFormat();
    }
    

    
    public static <T extends Object> HashSet<T> nullOrCopy(Set<T> in)
    {
        if (in == null)
            return null;
        
        if (in.isEmpty())
            return null;
        
        return new HashSet<T>(in);
    }
    
    public static Set<AmpCategoryValue> pumpCategoryValueSetFromForm(Object[] ids)
    {
        if (ids == null || ids.length == 0)
            return null;
        
        Set<AmpCategoryValue> result = new HashSet<AmpCategoryValue>();
        for (int i = 0; i < ids.length; i++) {
            Long id = (ids[i] instanceof Long) ? (Long) ids[i] : Long.parseLong(ids[i].toString());
            AmpCategoryValue value = CategoryManagerUtil.getAmpCategoryValueFromDb(id);
            if (value != null)
                result.add(value);              
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


    /**
     * fills an AmpARFilter instance with Filters data from a ReportsFilterPickerForm
     * @param arf
     * @param filterForm
     * @see #fillFilterFromSettingsForm(AmpARFilter, ReportsFilterPickerForm) for copying the settings part
     */
    public static void fillFilterFromFilterForm(AmpARFilter arf, ReportsFilterPickerForm filterForm) throws DgException
    {
        Session session = PersistenceManager.getSession();
        arf.readRequestData(TLSUtils.getRequest(), AmpARFilter.FILTER_SECTION_FILTERS, null);

        // for each sector we have also to add the subsectors

        Set<AmpSector> selectedSectors = Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedSectors());
        Set<AmpSector> selectedSecondarySectors = Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedSecondarySectors());
        Set<AmpSector> selectedTertiarySectors = Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedTertiarySectors());
        Set<AmpSector> selectedQuaternarySectors =
                Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedQuaternarySectors());
        Set<AmpSector> selectedQuinarySectors =
                Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedQuinarySectors());
        Set<AmpSector> selectedTagSectors = Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedTagSectors() );

        arf.setSelectedSectors(nullOrCopy(selectedSectors));
        arf.setSelectedSecondarySectors(nullOrCopy(selectedSecondarySectors));
        arf.setSelectedTertiarySectors(nullOrCopy(selectedTertiarySectors));
        arf.setSelectedQuaternarySectors(nullOrCopy(selectedQuaternarySectors));
        arf.setSelectedQuinarySectors(nullOrCopy(selectedQuinarySectors));
        arf.setSelectedTagSectors(nullOrCopy(selectedTagSectors));
        

        Set<AmpTheme> selectedNatPlanObj = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedNatPlanObj());
        Set<AmpTheme> selectedPrimaryPrograms = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedPrimaryPrograms());
        Set<AmpTheme> selectedSecondaryPrograms = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedSecondaryPrograms());

        arf.setSelectedNatPlanObj(nullOrCopy(selectedNatPlanObj));
        arf.setSelectedPrimaryPrograms(nullOrCopy(selectedPrimaryPrograms));
        arf.setSelectedSecondaryPrograms(nullOrCopy(selectedSecondaryPrograms));
        
//      AmpApplicationSettings tempSettings = ReportFilterFormUtil.getAppSetting();

        if (filterForm.getText() != null) {
            arf.setText(filterForm.getText());
        }

        if (filterForm.getIndexString() != null) {
            arf.setIndexText(filterForm.getIndexString());
            arf.setSearchMode(filterForm.getSearchMode());
        }

        arf.setYearFrom(filterForm.getFromYear() == null || filterForm.getFromYear().longValue() == -1 ? null : filterForm.getFromYear().intValue());
        arf.setYearTo(filterForm.getToYear() == null || filterForm.getToYear().longValue() == -1 ? null : filterForm.getToYear().intValue());
        arf.setFromMonth(filterForm.getFromMonth() == null || filterForm.getFromMonth().intValue() == -1 ? null : filterForm.getFromMonth().intValue());
        arf.setToMonth(filterForm.getToMonth() == null || filterForm.getToMonth().intValue() == -1 ? null : filterForm.getToMonth().intValue());

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
    
        arf.setFromActualApprovalDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromActualApprovalDate()));
        arf.setToActualApprovalDate(FilterUtil.convertUiToArFilterDate(filterForm.getToActualApprovalDate()));
        arf.setDynActualApprovalFilterCurrentPeriod(filterForm.getDynamicActualApprovalFilter().getCurrentPeriod());
        arf.setDynActualApprovalFilterAmount(filterForm.getDynamicActualApprovalFilter().getAmount());
        arf.setDynActualApprovalFilterOperator(filterForm.getDynamicActualApprovalFilter().getOperator());
        arf.setDynActualApprovalFilterXPeriod(filterForm.getDynamicActualApprovalFilter().getxPeriod());
        arf.setFromProposedApprovalDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromProposedApprovalDate()));
        arf.setToProposedApprovalDate(FilterUtil.convertUiToArFilterDate(filterForm.getToProposedApprovalDate()));
        arf.setDynProposedApprovalFilterCurrentPeriod(filterForm.getDynamicProposedApprovalFilter().getCurrentPeriod());
        arf.setDynProposedApprovalFilterAmount(filterForm.getDynamicProposedApprovalFilter().getAmount());
        arf.setDynProposedApprovalFilterOperator(filterForm.getDynamicProposedApprovalFilter().getOperator());
        arf.setDynProposedApprovalFilterXPeriod(filterForm.getDynamicProposedApprovalFilter().getxPeriod());
        
        arf.setToActivityActualCompletionDate(FilterUtil.convertUiToArFilterDate(filterForm.getToActivityActualCompletionDate()));
        arf.setFromActivityActualCompletionDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromActivityActualCompletionDate()));
        arf.setDynActivityActualCompletionFilterCurrentPeriod(filterForm.getDynamicActivityActualCompletionFilter().getCurrentPeriod());
        arf.setDynActivityActualCompletionFilterAmount(filterForm.getDynamicActivityActualCompletionFilter().getAmount());
        arf.setDynActivityActualCompletionFilterOperator(filterForm.getDynamicActivityActualCompletionFilter().getOperator());
        arf.setDynActivityActualCompletionFilterXPeriod(filterForm.getDynamicActivityActualCompletionFilter().getxPeriod());
        
        arf.setToActivityFinalContractingDate(FilterUtil.convertUiToArFilterDate(filterForm.getToActivityFinalContractingDate()));
        arf.setFromActivityFinalContractingDate(FilterUtil.convertUiToArFilterDate(filterForm.getFromActivityFinalContractingDate()));
        arf.setDynActivityFinalContractingFilterCurrentPeriod(filterForm.getDynamicActivityFinalContractingFilter().getCurrentPeriod());
        arf.setDynActivityFinalContractingFilterAmount(filterForm.getDynamicActivityFinalContractingFilter().getAmount());
        arf.setDynActivityFinalContractingFilterOperator(filterForm.getDynamicActivityFinalContractingFilter().getOperator());
        arf.setDynActivityFinalContractingFilterXPeriod(filterForm.getDynamicActivityFinalContractingFilter().getxPeriod());

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

        arf.setSelectedActivityPledgesSettings(Integer.parseInt(filterForm.getSelectedActivityPledgesSettings()));

        int curYear = new GregorianCalendar().get(Calendar.YEAR);
        
        if (filterForm.getComputedYear()!=-1){
            arf.setComputedYear(filterForm.getComputedYear());
        }else{
            if (FeaturesUtil.isVisibleFeature("Computed Columns Filters"))
                arf.setComputedYear(curYear);
            else
                arf.setComputedYear(null);
                    
        }
        if (filterForm.getActualAppYear()!=-1){
            arf.setActualAppYear(filterForm.getActualAppYear());
        }
        else 
            arf.setActualAppYear(null);
        // arf.setDonors(Util.getSelectedObjects(AmpOrgGroup.class,filterForm.getSelectedDonors()));

        Integer all = new Integer(-1);
                
        if ( filterForm.getLineMinRanks() != null && filterForm.getLineMinRanks().length > 0 ) {
            ArrayList<Integer> ranks        = new ArrayList<Integer>();
            for (int i=0; i< filterForm.getLineMinRanks().length;  i++) {
                Integer val     = Integer.parseInt((String)filterForm.getLineMinRanks()[i]);
                if ( val == 0 ) {
                    ranks   = null;
                    break;
                } else
                    ranks.add(val);
            }
            arf.setLineMinRank(ranks);
         }
        //if (!all.equals(filterForm.getRegionSelected()))
        //  arf.setRegionSelected(filterForm.getRegionSelected() == null || filterForm.getRegionSelected() == -1 ? 
        //                  null : DynLocationManagerUtil.getLocation(filterForm.getRegionSelected(),false) );
        
        Set<AmpCategoryValueLocations> selectedRegions = null;
        if (filterForm.getRegionSelected() != null){
            if (!filterForm.getRegionSelected()[0].toString().equals("-1")) {
                selectedRegions = Util.getSelectedObjects(AmpCategoryValueLocations.class, filterForm.getRegionSelected());
            }
        }
        
        if (selectedRegions != null && selectedRegions.size() > 0) {
            arf.setLocationSelected(new HashSet<AmpCategoryValueLocations>());
            arf.getLocationSelected().addAll(selectedRegions);
        } else {
            arf.setLocationSelected(null);
            //arf.setRelatedLocations(null);
        }
        
        if (!all.equals(filterForm.getApprovalStatusSelected())){
            if(filterForm.getApprovalStatusSelected() != null){
                ArrayList<String> appvals = new ArrayList<String>();
                for (int i = 0; i < filterForm.getApprovalStatusSelected().length; i++) {
                    String id = filterForm.getApprovalStatusSelected()[i].toString();
                    appvals.add(id);
                }
                arf.setApprovalStatusSelected(appvals);
            }
            else{
                arf.setApprovalStatusSelected(null);
            }
        }
        else 
            arf.setApprovalStatusSelected(null);
        
        arf.setStatuses(pumpCategoryValueSetFromForm(filterForm.getSelectedStatuses()));
        
        arf.setExpenditureClass(pumpCategoryValueSetFromForm(filterForm.getSelectedExpenditureClasses()));

        arf.setPerformanceAlertLevel(pumpCategoryValueSetFromForm(filterForm.getSelectedPerformanceAlertLevels()));

        arf.setPerformanceAlertType(pumpPerformanceAlertTypeSetFromForm(filterForm.getSelectedPerformanceAlertTypes()));

        if (filterForm.getSelectedWorkspaces() != null && filterForm.getSelectedWorkspaces().length > 0)
            arf.setWorkspaces(new HashSet<AmpTeam>());
        else
            arf.setWorkspaces(null);

        for (int i = 0; filterForm.getSelectedWorkspaces() != null && i < filterForm.getSelectedWorkspaces().length; i++) {
            Long workspaceId = Long.parseLong( filterForm.getSelectedWorkspaces()[i].toString() );
            AmpTeam value   = (AmpTeam) session.load(AmpTeam.class, workspaceId);
            arf.getWorkspaces().add(value);
        }

        arf.setProjectCategory(pumpCategoryValueSetFromForm(filterForm.getSelectedProjectCategory()));
        arf.setFinancingInstruments(pumpCategoryValueSetFromForm(filterForm.getSelectedFinancingInstruments()));
        arf.setFundingStatus(pumpCategoryValueSetFromForm(filterForm.getSelectedFundingStatus()));
        arf.setAidModalities(pumpCategoryValueSetFromForm(filterForm.getSelectedAidModalities()));
        arf.setTypeOfAssistance(pumpCategoryValueSetFromForm(filterForm.getSelectedTypeOfAssistance()));
        arf.setModeOfPayment(pumpCategoryValueSetFromForm(filterForm.getSelectedModeOfPayment()));
        arf.setProjectImplementingUnits(pumpCategoryValueSetFromForm(filterForm.getSelectedProjectImplUnit()));
        arf.setActivityPledgesTitle( pumpCategoryValueSetFromForm(filterForm.getSelectedActivityPledgesTitle()) );
        
        if (filterForm.getPageSize() != null) {
            arf.setPageSize(filterForm.getPageSize()); // set page size in the ARF filter
        }

        arf.setRisks(Util.getSelectedObjects(AmpIndicatorRiskRatings.class, filterForm.getSelectedRisks()));

        if ( filterForm.getSelectedActivitySettings() != null && filterForm.getSelectedActivitySettings().length > 0 ) {
            boolean isJointCriteria = false;
            boolean isGovProcedures = false;
            for (int i = 0; i < filterForm.getSelectedActivitySettings().length; i++) {
                String element = filterForm.getSelectedActivitySettings()[i].toString();
                if ( ONLY_JOINT_CRITERIA.equals(element) ) {
                    arf.setJointCriteria(true);
                    isJointCriteria     = true;
                }
                if ( ONLY_GOV_PROCEDURES.equals(element) ) {
                    arf.setGovernmentApprovalProcedures(true);
                    isGovProcedures     = true;
                }
            }
            if (!isJointCriteria)
                arf.setJointCriteria(null);
            if (!isGovProcedures)
                arf.setGovernmentApprovalProcedures(null);
        }
//      arf.setGovernmentApprovalProcedures(filterForm.getGovernmentApprovalProcedures());
//      arf.setJointCriteria(filterForm.getJointCriteria());

        if (filterForm.getSelectedDonorTypes() != null && filterForm.getSelectedDonorTypes().length > 0) {
            arf.setDonorTypes(new HashSet<AmpOrgType>());
            for (int i = 0; i < filterForm.getSelectedDonorTypes().length; i++) {
                Long id = Long.parseLong(filterForm.getSelectedDonorTypes()[i].toString());
                AmpOrgType type = DbUtil.getAmpOrgType(id);
                if (type != null)
                    arf.getDonorTypes().add(type);
            }
        } else
            arf.setDonorTypes(null);

        if (filterForm.getSelectedDonorGroups() != null && filterForm.getSelectedDonorGroups().length > 0) {
            arf.setDonorGroups(new HashSet<AmpOrgGroup>());
            for (int i = 0; i < filterForm.getSelectedDonorGroups().length; i++) {
                Long id = Long.parseLong(filterForm.getSelectedDonorGroups()[i].toString());
                AmpOrgGroup grp = DbUtil.getAmpOrgGroup(id);
                if (grp != null)
                    arf.getDonorGroups().add(grp);
            }
        } else
            arf.setDonorGroups(null);

        if (filterForm.getSelectedContractingAgencyGroups() != null && filterForm.getSelectedContractingAgencyGroups().length > 0) {
            arf.setContractingAgencyGroups(new HashSet<AmpOrgGroup>());
            for (int i = 0; i < filterForm.getSelectedContractingAgencyGroups().length; i++) {
                Long id = Long.parseLong(filterForm.getSelectedContractingAgencyGroups()[i].toString());
                AmpOrgGroup grp = DbUtil.getAmpOrgGroup(id);
                if (grp != null)
                    arf.getContractingAgencyGroups().add(grp);
            }
        } else
            arf.setContractingAgencyGroups(null);

        arf.setBudget(pumpCategoryValueSetFromForm(filterForm.getSelectedBudgets()));
        
        if ( filterForm.getSelectedMultiDonor() != null && filterForm.getSelectedMultiDonor().length == 1 ) {
            arf.setMultiDonor( (String) filterForm.getSelectedMultiDonor()[0] );
        }
        else
            arf.setMultiDonor(null);

        arf.setJustSearch(filterForm.getJustSearch()==null?false:filterForm.getJustSearch());
        
        
        arf.setWorkspaceonly(filterForm.getWorkspaceonly()==null?false:filterForm.getWorkspaceonly());
        
        /*THIS IS USED FOR PLEDGES IN ORDER TO SHOW ONLY PLEDGES ASSOCIATED TO THE ACTIVITIES THAT BELONG TO THE WORKSPACE
         PLEASE DON'T DELETE IT AGAIN*/
        if(arf.isWorkspaceonly()){
            arf.setAmpTeamsforpledges(WorkspaceFilter.getAmpTeamsSet(arf.getTeamMemberId()));
        }else{
            arf.setAmpTeamsforpledges(null);
        }
        
        arf.setBeneficiaryAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedBeneficiaryAgency()));
        arf.setDonnorgAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedDonnorAgency()));
        arf.setResponsibleorg(ReportsUtil.processSelectedFilters(filterForm.getSelectedresponsibleorg()));
        arf.setComponentFunding(ReportsUtil.processSelectedFilters(filterForm.getSelectedComponentFundingOrg()));
        arf.setComponentSecondResponsible(ReportsUtil.processSelectedFilters(filterForm.getSelectedComponentSecondResponsibleOrg()));

        arf.setImplementingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedImplementingAgency()));
        arf.setExecutingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedExecutingAgency()));
        arf.setContractingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedContractingAgency()));
        arf.setProjectCategory(ReportsUtil.processSelectedFilters(filterForm.getSelectedProjectCategory(), AmpCategoryValue.class));
        
        if ( filterForm.getSelectedArchivedStatus() == null || filterForm.getSelectedArchivedStatus().length != 1 ) {
            arf.setShowArchived(null);
        }
        else {
            String selection    = (String) filterForm.getSelectedArchivedStatus()[0];
            if ("1".equals(selection) )
                arf.setShowArchived(false);
            else
                arf.setShowArchived(true);
        }
        
        arf.setHumanitarianAid(buildBooleanField(filterForm.getSelectedHumanitarianAid()));
        arf.setDisasterResponse(buildBooleanField(filterForm.getSelectedDisasterResponse()));
        arf.postprocess();
    }

    protected static Set<Integer> buildBooleanField(Object[] values) {
        if (values == null)
            return null;
        
        Set<Integer> res = new HashSet<>();
        for(Object obj:values) {
            if (obj != null && Integer.parseInt(obj.toString()) > 0)
                res.add(Integer.parseInt(obj.toString()));
            }
        return res;
    }
}
