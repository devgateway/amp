package org.digijava.kernel.ampapi.endpoints.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.newreports.pagination.PartialReportArea;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.reports.ActivityType;
import org.dgfoundation.amp.reports.CachedReportData;
import org.dgfoundation.amp.reports.ReportCacher;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.reports.converters.MtefConverter;
import org.dgfoundation.amp.utils.BoundedList;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.publicportal.PublicReportFormParameters;
import org.digijava.kernel.ampapi.endpoints.reports.saiku.QueryModel;
import org.digijava.kernel.ampapi.endpoints.reports.saiku.QuerySettings;
import org.digijava.kernel.ampapi.endpoints.reports.saiku.SaikuBasedQuery;
import org.digijava.kernel.ampapi.endpoints.reports.saiku.SortParam;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.ampapi.endpoints.util.MaxSizeLinkedHashMap;
import org.digijava.kernel.ampapi.endpoints.util.ReportConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.esrigis.dbentity.AmpApiState;

/**
 * Reports API utility classes
 * 
 * @author Nadejda Mandrescu
 */
public class ReportsUtil {
    // TODO: the class grow up too much, we need to split it
    
    protected static final Logger logger = Logger.getLogger(ReportsUtil.class);

    /**
     * Retrieves the page for the  result for the specified reportId and a given page number
     *  
     * @param reportId    report Id
     * @param formParams  form parameters, that must be in the following format:    <br/>
     * {                                                                            <br/>
     *  "page"            : 1,                                                      <br/>
     *  "recordsPerPage"  : 10,                                                     <br/>
     *  "regenerate"      : true,                                                   <br/>
     *  "filters"         : //see filters format defined in Gis,                    <br/>
     *  "settings"        : //see {@link SettingsUtils#applySettings}               <br/>
     *  "sorting"         : [                                                       <br/>
     *        {                                                                     <br/>
     *          "columns" : ["Donor Agency", "Actual Commitments"],                 <br/>
     *          "asc"     : true                                                    <br/>
     *        },                                                                    <br/>
     *        {                                                                     <br/>
     *          "columns" : ["Project Title"],                                      <br/>
     *          "asc"     : false                                                   <br/>
     *        }                                                                     <br/>
     *       ]                                                                      <br/>
     *                                                                              <br/>
     *  "add_columns"     : ["Activity Id", "Approval Status"],                     <br/>
     *  "add_hierarchies" : ["Approval Status"],                                    <br/>
     *  "add_measures"    : ["Custom Measure"],                                     <br/>
     *  "rowTotals"       : true                                                    <br/>
     *  "reportType"      : "C"                                                     <br/>
     *  "projectType"     : ["S"]                                                   <br/>
     * }                                                                            <br/>
     * 
     * where: <br>
     * <dl>
     *   <dt>name</dt>          <dd>mandatory to be provided for custom reports, otherwise is skipped</dd> 
     *   <dt>page</dt>          <dd>optional, page number, starting from 1. Use 0 to retrieve only
     *                              pagination information, without any records. Default to 0</dd>
     *   <dt>recordsPerPage</dt> <dd>optional, the number of records per page to return. Default
     *                              will be set to the number configured in AMP. Set it to -1
     *                              to get the unlimited records, that will provide all records.</dd>
     *   <dt>sorting</dt>       <dd>optional, a list of sorting maps. Each map has 'columns' list 
     *                              and 'asc' flag, that is true for sorting ascending. Hierarchical 
     *                              sorting will define a column list as a tuple to sort by.</dd>
     *   <dt>regenerate</dt>    <dd>optional, set to true for all first access and any changes and 
     *                              to false for consequent page navigation. Default to true</dd>
     *   <dt>add_columns</dt>   <dd>optional, a list of columns names to be added to the report configuration</dd>
     *   <dt>columns_with_ids</dt>  <dd>optional, a list of columns names that should also provide ids</dd>
     *   <dt>add_hierarchies</dt>  <dd>optional, a list of hierarchies to be added to the report configuration</dd>
     *   <dt>add_measures</dt>  <dd>optional, a list of measures to be added to the report configuration</dd>
     *   <dt>raw_values</dt>       <dd>optional, a list that configures which elements must provide raw (unformatted) 
     *                             values, with possible options (default []):
     *                             "M": measures </dd>
     *   <dt>show_empty_rows</dt> <dd>optional, default false, to show rows with empty measures amounts</dd>
     *   <dt>show_empty_cols</dt> <dd>optional, default false, to show full column groups (by quarter, year) 
     *                              with empty measures amounts</dd>
     *   <dt>forceHeaders</dt>  <dd>optional, flag, if the report query returns empty response
     *                              the list of column headers is populated from the request. Default is false</dd>
     *   <dt>settings</dt>      <dd>Report settings</dd>
     *   <dt>reportType</dt>    <dd>can be on of "D" (Donor), "C" (Component), "P" (Pledge) report type.
     *                          Default is "D" if not provided.</dd>
     *   <dt>projectType</dt>   <dd>an optional list of projects, mainly used to change usual default project
     *                          types included in the report. The list option per report type:
     *                          <br>"D" : ["A", "S", "P"], where default is ["A", "S"] if both are reachable
     *                          <br>"C" : ["A", "S"], where default is ["A", "S"]
     *                          <br>"P" : ["P"], where default is ["P"]
     *                          <br>where "A" - standard activity, "S" - SSC Activity, "P" - pledge</dd>
     *   <dt>info</dt>          <dd>optional, list of additional information to include 
     *                          <br>["stats", "warnings", "generatedHeaders"], default is [] (none) 
     * </dl>
     * @return JsonBean result for the requested page and pagination information
     */
    public static final PagedReportResult getReportResultByPage(Long reportId, ReportFormParameters formParams) {
        PagedReportResult result = new PagedReportResult();
        
        // read pagination data
        int page = EndpointUtils.getSingleValue(formParams.getPage(), 0);
        int recordsPerPage = EndpointUtils.getSingleValue(formParams.getRecordsPerPage(),
                ReportPaginationUtils.getRecordsNumberPerPage());
        int start = (page - 1) * recordsPerPage;
        
        // get the report (from cache if it was cached)
        CachedReportData cachedReportData = getCachedReportData(reportId, formParams);
        if (cachedReportData != null) {
            fillReportInfo(cachedReportData.report, result, formParams);
        }

        // extract data for the requested page
        ReportArea pageArea = null;
        if (recordsPerPage != -1) {
            pageArea = cachedReportData.paginationInfo.getPage(start, recordsPerPage);
        } else if (cachedReportData != null && cachedReportData.report !=null) {
            pageArea = cachedReportData.report.reportContents;
        }
        
        int totalPageCount = cachedReportData.paginationInfo.getPageCount(recordsPerPage);
        
        // configure the result
        result.setPage(new JSONReportPage(pageArea, recordsPerPage, page, totalPageCount,
                cachedReportData.paginationInfo.getRecordsCount()));
        result.setSettings(cachedReportData != null
                ? SettingsUtils.getReportSettings(cachedReportData.report.spec) : null);

        boolean showRowTotals = cachedReportData.report.reportContents.getContents().values().stream()
                .map(rc -> rc.displayedValue)
                .anyMatch(StringUtils::isNotEmpty);

        result.setRowTotals(showRowTotals);
        
        processRawValues(formParams);
        result.setReportWarnings(cachedReportData.report.reportWarnings);
        
        return result;
    }
    
    protected static void fillReportInfo(GeneratedReport report, PagedReportResult result,
            ReportFormParameters formParams) {
        if (report == null) return;
        result.setEmpty(report.isEmpty);
        result.setHeaders(report.leafHeaders);
        List<String> addInfo = EndpointUtils.getSingleValue(formParams.getAdditionalInfo(), Collections.emptyList());
        for (String infoToAdd : addInfo) {
            if (EPConstants.GENERATED_HEADERS.equalsIgnoreCase(infoToAdd)) {
                result.setGeneratedHeaders(report.generatedHeaders);
            } else if (EPConstants.WARNINGS.equalsIgnoreCase(infoToAdd)) {
                result.setWarnings(report.reportWarnings);
            } else if (EPConstants.STATS.equalsIgnoreCase(infoToAdd)) {
                result.setStats(report.getMapTimings());
            }
        }
    }
    
    protected static void processRawValues(ReportFormParameters formParams) {
        List<String> rawValuesElements =
                EndpointUtils.getSingleValue(formParams.getRawValues(), Collections.emptyList());
        boolean keepRawValuesForMeasures = false;
        for (String elemType : rawValuesElements) {
            // the only supported for now
            if ("M".equalsIgnoreCase(elemType)) {
                keepRawValuesForMeasures = true;
            }
        }
        if (!keepRawValuesForMeasures) {
            EndpointUtils.applyJsonFilter(EPConstants.JSON_FILTER_AMOUNT_CELL, "value");
        }
    }
    
    protected static ReportFormParameters convertSaikuParamsToReports(SaikuBasedQuery original) {
        ReportFormParameters newParams = new ReportFormParameters();
        QueryModel queryModel = original.getQueryModel();
        if (queryModel != null) {
            if (queryModel.getPage() != null) {
                newParams.setPage(queryModel.getPage());
            }
            if (queryModel.getRecordsPerPage() != null) {
                newParams.setRecordsPerPage(queryModel.getRecordsPerPage());
            }
            if (queryModel.getFilters() != null) {
                newParams.setFilters(queryModel.getFilters());
                //newParams.set("page", new Integer("1"));
            }
            if (queryModel.getSettings() != null) {
                newParams.setSettings(queryModel.getSettings());
            }
            if (queryModel.getSorting() != null) {
                newParams.setSorting(queryModel.getSorting());
            }
            if (original.getDinamic() != null) {
                newParams.setDynamic(true);
            }
            if (queryModel.getIncludeLocationChildren() != null) {
                newParams.setIncludeLocationChildren(queryModel.getIncludeLocationChildren());
            }
        }
        
        if (original.getAdditionalColumns() != null) {
            newParams.setAdditionalColumns(original.getAdditionalColumns());
        }
        
        if (original.getMd5() != null) {
            newParams.setMd5(original.getMd5());
        }
        
        QuerySettings querySettings = original.getQuerySettings();
        if (querySettings != null) {
            newParams.setAdditionalInfo(querySettings.getInfo());
        }
        
        return newParams;
    }
    
    public static GeneratedReport getGeneratedReport(Long reportId, ReportFormParameters formParams) {
        CachedReportData cachedReportData = getCachedReportData(reportId, formParams);
        
        if (cachedReportData != null && cachedReportData.report != null) {
            return cachedReportData.report;
        }
        
        return null;
    }
    
    private static CachedReportData getCachedReportData(Long reportId, ReportFormParameters formParams) {

        // use null in case the frontend has not generated an md5 token (e.g. Tabs as of 15/aug/2016).
        // Using the timestamp is a VERY bad idea since it would pollute the cache at each page cache
        String reportToken = formParams.getMd5();
        
        boolean regenerate = ReportCacher.getReportData(reportToken) == null;
        CachedReportData cachedReportData = null;
        
        // generate the report
        if (regenerate) {
            ReportSpecificationImpl spec = null;
            if (Boolean.TRUE.equals(formParams.getCustom())) {
                String reportName = formParams.getReportName();
                spec = EndpointUtils.getReportSpecification(formParams.getReportType(), reportName);
            } else {
                if (Boolean.TRUE.equals(formParams.getDynamic())) {
                    try {
                        spec = AmpReportsToReportSpecification.convert(ReportsUtil.getAmpReportFromSession(reportId.intValue()));
                    } catch (Exception e) {
                        logger.error("Cannot get report from session",e);
                        throw new RuntimeException("Cannot restore report from session: " + reportId);
                    }
                }else{
                    spec = ReportsUtil.getReport(reportId);
                }
            }
            // add additional requests
            update(spec, formParams);
            // regenerate
            GeneratedReport generatedReport = EndpointUtils.runReport(spec, PartialReportArea.class, 
                    buildOutputSettings(spec, formParams));
            cachedReportData = ReportPaginationUtils.cacheReportData(reportToken, generatedReport);
        } else {
            cachedReportData = ReportCacher.getReportData(reportToken);
        }
        return cachedReportData;
    }
    
    /**
     * Retrieves report configuration for the specified report id 
     * @param reportId - report id to provide the specification for 
     * @return ReportSpecification object
     * @throws AMPException
     */
    public static ReportSpecificationImpl getReport(Long reportId) {
        AmpReports ampReport = DbUtil.getAmpReport(reportId);
        try {
            if (ampReport != null)
                return AmpReportsToReportSpecification.convert(ampReport);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Updates report specification with the configuration coming from   
     * @param spec - the specification that will be updated
     * @param formParams
     * @return the updated spec
     */
    public static ReportSpecification update(ReportSpecification spec, ReportFormParameters formParams) {
        if (spec == null || formParams == null) return spec;
        if (!(spec instanceof ReportSpecificationImpl)) {
            logger.error("Unsupported request for "  + spec.getClass());
            return spec;
        }
        ReportSpecificationImpl specImpl = (ReportSpecificationImpl) spec;
        
        // update report structure
        addColumns(specImpl, formParams);
        addHierarchies(specImpl, formParams);
        addMeasures(specImpl, formParams);
        
        // update report data presentation
        AmpFiscalCalendar oldCalendar = specImpl.getSettings() == null ? null : (AmpFiscalCalendar) specImpl.getSettings().getCalendar(); 
        SettingsUtils.applySettings(specImpl, formParams.getSettings(), false);
        configureFilters(specImpl, formParams.getFilters(), oldCalendar);
        configureSorting(specImpl, formParams);
        
        // update report data
        configureProjectTypes(specImpl, formParams.getProjectType());
        
        // configure include location children
        configureIncludeLocationChildrenFilters(specImpl, formParams.getIncludeLocationChildren());
        
        // update other settings
        setOtherOptions(specImpl, formParams);
        
        return spec;
    }

    private static void addColumns(ReportSpecification spec, ReportFormParameters formParams) {
        //adding new columns if not present
        if (formParams.getAdditionalColumns() != null) {
            addColumns(spec, formParams.getAdditionalColumns());
        }
    }
    /**
         * Updates report specification with the grouping criteria
         * @param spec - the specification that will be updated
         * @param groupingOption
         * @return the updated spec
         */
    public static void setGroupingCriteria(ReportSpecificationImpl spec, String groupingOption) {
        switch (groupingOption) {
        case ReportConstants.GROUPING_YEARLY:
            spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
            break;
        case ReportConstants.GROUPING_QUARTERLY:
            spec.setGroupingCriteria(GroupingCriteria.GROUPING_QUARTERLY);
            break;
        case ReportConstants.GROUPING_MONTHLY:
            spec.setGroupingCriteria(GroupingCriteria.GROUPING_MONTHLY);
            break;
        default:
            spec.setGroupingCriteria(GroupingCriteria.GROUPING_TOTALS_ONLY);
            break;
        }
    }
    
    private static void addColumns(ReportSpecification spec, Collection<String> columns) {
        for (String columnName : columns) {
            ReportColumn column = new ReportColumn(columnName);
            if (!spec.getColumns().contains(column)) {
                spec.getColumns().add(column);
            }
        }
    }
    
    /**
     * Builds special output settings as requested and updates spec if needed
     * @param spec
     * @param formParams
     * @return
     */
    public static OutputSettings buildOutputSettings(ReportSpecification spec, ReportFormParameters formParams) {
        Set<String> idsValuesColumns = null;
        
        if (formParams.getColumnsWithIds() != null) {
            idsValuesColumns = new HashSet<>(formParams.getColumnsWithIds());
            // fixing the spec if some columns were not configured
            addColumns(spec, idsValuesColumns);
        }
        
        return new OutputSettings(idsValuesColumns);
    }
    
    private static void addHierarchies(ReportSpecification spec, ReportFormParameters formParams) {
        //adding new hierarchies if not present
        if (formParams.getAdditionalHierarchies() != null) {
            List<String> hierarchies = formParams.getAdditionalHierarchies();
            List<ReportColumn> existingColumns = new ArrayList<ReportColumn>();
            existingColumns.addAll(spec.getColumns());
            for (String columnName : hierarchies) {
                ReportColumn column = new ReportColumn(columnName);
                if (!spec.getHierarchies().contains(column)) {
                    //add as a column if not present 
                    if (!existingColumns.contains(column)) {
                        existingColumns.add(spec.getHierarchies().size(), column);
                    }
                    spec.getHierarchies().add(column);
                }
            }
            spec.getColumns().clear();
            spec.getColumns().addAll(existingColumns);
        }
    }
    
    private static void addMeasures(ReportSpecification spec, ReportFormParameters formParams) {
        //add new measures if not present
        if (formParams.getAdditionalMeasures() != null) {
            List<String> measures = formParams.getAdditionalMeasures();
            for (String measureName : measures) {
                ReportMeasure measure = new ReportMeasure(measureName);
                if (!spec.getMeasures().contains(measure)) {
                    spec.getMeasures().add(measure);        }
            }
        }
    }
    
    // TODO: refactor once date filters per calendar are  handled
    public static void configureFilters(ReportSpecificationImpl spec, Map<String, Object> filterMap) {
        configureFilters(spec, filterMap, null);
    }
    
    public static void configureFilters(ReportSpecificationImpl spec, Map<String, Object> filterMap,
            AmpFiscalCalendar oldCalendar) {
        if (filterMap != null) {
            AmpReportFilters newFilters = new AmpReportFilters((AmpFiscalCalendar) spec.getSettings().getCalendar());
            if (spec.getFilters() != null) {
                // TODO: we need calendar + date to be linked in UI as well OR make same form for filters and settings
                // for now, if this is a calendar setting, let's check if any filters still exist and needs to be converted 
                if (spec.getSettings().getCalendar() != oldCalendar
                        && newFilters.getFilterRules().get(new ReportElement(ElementType.DATE)) != null) {
                    // for now we always use Gregorian, we'll update workflow when we'll add "calendar per date" support 
                    //newFilters.setOldCalendar(oldCalendar);
                }
            }

            AmpReportFilters formFilters = FilterUtils.getFilters(filterMap, newFilters);
            AmpReportFilters stickyFilters = copyStickyMtefEntries((AmpReportFilters) spec.getFilters(), formFilters);
            spec.setFilters(stickyFilters);
        }
    }
    
    /**
     * copies entries which are not enclosed in the Filter widget but should be kept post-filter-application from oldFilters to newFilters<br />
     * Now this only means MTEF filter entries
     * @param oldFilters
     * @param newFilters
     * @return
     */
    protected static AmpReportFilters copyStickyMtefEntries(AmpReportFilters oldFilters, AmpReportFilters newFilters) {
        if (oldFilters == null || oldFilters.getFilterRules() == null)
            return newFilters; // no chance of stickies
        
        AmpReportFilters result = newFilters == null ? new AmpReportFilters() : newFilters;
        
        boolean somethingAdded = false;
        if (result.getComputedYear() == null && oldFilters.getComputedYear() != null) {
            result.setComputedYear(oldFilters.getComputedYear());
            somethingAdded = true;
        }

        // set filters even if they are empty, that means filters are cleared up
        // copy MTEF-hacky entries from old widget to new widget, since these are supposed to be sticky (not present in the filter form)
        for(Entry<ReportElement, FilterRule> elem: oldFilters.getFilterRules().entrySet()) {
            if (MtefConverter.MTEF_DATE_ELEMENT_TYPES.contains(elem.getKey().type)) {
                result.getFilterRules().put(elem.getKey(), elem.getValue());
                somethingAdded = true;
            }
        }
        
        if (newFilters == null && !somethingAdded)
            return newFilters; // do not alter filters if we did nothing
        
        return result;
    }
    
    /**
     * Configure to include or not the location children (AMP-27559)
     * 
     * @param specImpl
     * @param includeLocationChildren
     */
    public static void configureIncludeLocationChildrenFilters(ReportSpecificationImpl specImpl,
                                                               Boolean includeLocationChildren) {
        if (includeLocationChildren != null) {
            specImpl.setIncludeLocationChildren(includeLocationChildren);
        }
    
        if (!specImpl.isIncludeLocationChildren()) {
            List<String> locationIds = new ArrayList<>();
            AmpReportFilters filterRules = (AmpReportFilters) specImpl.getFilters();
            for (Entry<ReportElement, FilterRule> filterRule : filterRules.getFilterRules().entrySet()) {
                if (isFilterLocationType(filterRule)) {
                    locationIds.addAll(filterRule.getValue().values);
                }
            }
        
            if (!locationIds.isEmpty()) {
                filterRules.addFilterRule(new ReportColumn(ColumnConstants.RAW_LOCATION),
                        new FilterRule(locationIds, true));
            }
        }
    }
    /**
 
     * Determine if the filter rule is of location type
     * 
     * @param filterRule
     * @return
     */
    private static boolean isFilterLocationType(Entry<ReportElement, FilterRule> filterRule) {
        if (filterRule.getKey() != null && filterRule.getKey().entity != null) {
            String columnName = filterRule.getKey().entity.getEntityName();
            
            return LocationUtil.LOCATIONS_COLUMNS_NAMES.contains(columnName);
        }
        
        return false;
    }

    /**
     * Explicitly configures projects that has to be included into the report. 
     * If nothing specified, then the default project types are used for the given report type.
     * 
     * @param spec 
     * @param projectTypeOptions
     */
    public static void configureProjectTypes(ReportSpecificationImpl spec, List<String> projectTypeOptions) {
        if (projectTypeOptions == null || projectTypeOptions.size() == 0) {
            return;
        }
        // no validation is done here, use "validateReportConfig" before creating report specification
        boolean hasPledge = projectTypeOptions.contains(ActivityType.PLEDGE.toString());
        boolean hasSSCActivity = projectTypeOptions.contains(ActivityType.SSC_ACTIVITY.toString());
        
        switch(spec.getReportType()) {
        case ArConstants.DONOR_TYPE:
            spec.setAlsoShowPledges(hasPledge);
            // no break, following same rule for SSC filters in both Donor & Component reports
        case ArConstants.COMPONENT_TYPE:
            if (hasSSCActivity && projectTypeOptions.size() == 1) {
                configureSSCWorkspaceFilter(spec, true);
            } else if (!hasSSCActivity) {
                configureSSCWorkspaceFilter(spec, false);
            }
            break;
        case ArConstants.PLEDGES_TYPE:
            // only pledges are allowed so far, no special rule
            break;
        }   
    }
    
    /**
     * Configures SSC workspaces to be the only one selected or excluded from the report
     * 
     * @param spec  report specification
     * @param add   has to be configured to true if only SSC workspaces are selected, 
     *              and to false if they are excluded  
     */
    public static void configureSSCWorkspaceFilter(ReportSpecificationImpl spec, boolean add) {
        List<AmpTeam> sscWorkspaces = TeamUtil.getAllSSCWorkspaces();
        if (sscWorkspaces.size() == 0) {
            logger.error("Cannot configure SSC Workspace filter, no SSC Workspace found");
            return;
        }
        List<String> sscWorkspacesIds = new ArrayList<String>(sscWorkspaces.size());
        for (AmpTeam sscWs : sscWorkspaces) {
            sscWorkspacesIds.add(sscWs.getIdentifier().toString());
        }
        AmpReportFilters filters = (AmpReportFilters) spec.getFilters();
        if (filters == null) {
            AmpFiscalCalendar calendar = spec.getSettings() == null ? null : (AmpFiscalCalendar) spec.getSettings().getCalendar();
            filters = new AmpReportFilters(calendar);
            spec.setFilters(filters);
        }
        filters.addFilterRule(new ReportColumn(ColumnConstants.TEAM), new FilterRule(sscWorkspacesIds, add));
    }
    
    /**
     * Retrieves sorting request
     * @param spec
     * @param formParams
     * @return true if sorting configuration changed
     */
    public static boolean configureSorting(ReportSpecificationImpl spec, ReportFormParameters formParams) {
        List<SortingInfo> newSorters = new ArrayList<SortingInfo>();
        
        if (formParams.getSorting() != null) {
            List<SortParam> sortingConfig = formParams.getSorting();
            logger.info("sortingConfig is: " + sortingConfig);
            
            for (SortParam sort : sortingConfig) {
                List<String> columns = sort.getColumns();
                Boolean asc = sort.isAsc();
                
                boolean isTotals;
                boolean isFunding;
                if (sort.getId() != null) {
                    // SAIKU request
                    String id = sort.getId();
                    isTotals = id.startsWith(String.format("[%s]", NiReportsEngine.TOTALS_COLUMN_NAME));
                    isFunding = id.startsWith(String.format("[%s]", NiReportsEngine.FUNDING_COLUMN_NAME));
                } else {
                    // tabs request - should check for general API
                    isFunding = columns.size() > 1;
                    isTotals = AmpReportsSchema.getInstance().getMeasures().containsKey(columns.get(0));
                }
                
                List<String> errors = new ArrayList<String>();
                if (columns == null)
                    errors.add("columns = null");
                
                if (asc == null)
                    errors.add("sorting order is not specified, asc = null");
                    
                if (errors.size() > 0)
                    logger.error("Ignoring invalid sorting request: " + errors);
                else {
                    int rootType = isTotals? SortingInfo.ROOT_PATH_TOTALS : (isFunding ? SortingInfo.ROOT_PATH_FUNDING : SortingInfo.ROOT_PATH_NONE);
                    newSorters.add(new SortingInfo(columns, rootType, asc));
                }
            }
        }
        
        // check if sorting config indeed changed
        boolean sortingChanged = true;
        if (sortingChanged)
            spec.setSorters(newSorters);
        return sortingChanged;
    }
    
    /**
     * Validates the report config data provide via formParams
     * 
     * @param formParams input parameters used 
     * @return JsonBean with errors or null if no error
     */
    public static final ApiErrorResponse validateReportConfig(ReportFormParameters formParams, boolean isCustom) {
        List<ApiErrorMessage> errors = new ArrayList<ApiErrorMessage>();
        // validate the name
        if (isCustom && StringUtils.isBlank(formParams.getReportName())) {
            errors.add(ReportErrors.REPORT_NAME_REQUIRED);
        }
        
        // validate the columns
        ApiErrorMessage err = validateList("columns", formParams.getAdditionalColumns(),
                ColumnsVisibility.getConfigurableColumns(), isCustom);
        if (err != null) errors.add(err);
        
        // validate the measures due to measureless reports a measure is not mandatory anymore
        err = validateList("measures", formParams.getAdditionalMeasures(),
                MeasuresVisibility.getConfigurableMeasures(), false);
        if (err != null) errors.add(err);
        
        // validate the hierarchies
        err = validateList("hierarchies", formParams.getAdditionalHierarchies(),
                formParams.getAdditionalColumns(), false);
        if (err != null) errors.add(err);
        
        // validate the project types
        err = validateList("project types", formParams.getProjectType(),
                ActivityType.STR_VALUES, false);
        if (err != null) errors.add(err);
        
        // validate the report type
        err = validateReportType(formParams.getReportType(), formParams.getProjectType());
        if (err != null) errors.add(err);
        if(errors.size()>0){
            return ApiError.toError(errors);
        }else{
            return null;
        }
    }

    public static ApiErrorResponse validateReportConfig(PublicReportFormParameters formParams) {
        List<ApiErrorMessage> errors = new ArrayList<ApiErrorMessage>();

        // validate the project types
        ApiErrorMessage err = validateList("project types", formParams.getProjectType(),
                ActivityType.STR_VALUES, false);
        if (err != null) {
            errors.add(err);
        }

        // validate the report type
        err = validateReportType(formParams.getReportType(), formParams.getProjectType());
        if (err != null) {
            errors.add(err);
        }
        return errors.size() > 0 ? ApiError.toError(errors) : null;
    }

    private static ApiErrorMessage validateList(String listName, List<String> values,
            Collection<String> allowedValues, boolean isMandatory) {
        if (values == null || values.size() == 0) {
            if (isMandatory) {
                return ReportErrors.LIST_NAME_REQUIRED.withPrefix(listName);
            }
        } else {
            List<String> copy = new ArrayList<String>(values);
            copy.removeAll(allowedValues);
            if (copy.size() > 0) {
                return ReportErrors.LIST_INVALID
                        .withPrefix(listName + ": [" + StringUtils.join(copy, ", ") + "]");
            }
        }
        return null;
    }
    
    private static ApiErrorMessage validateReportType(String reportType, List<String> activityTypes) {
        reportType = EndpointUtils.getSingleValue(reportType, EPConstants.DEFAULT_REPORT_TYPE);
        Integer reportTypeId = EPConstants.REPORT_TYPE_ID_MAP.get(reportType);
        if (reportTypeId == null) {
            return ReportErrors.REPORT_TYPE_INVALID.withDetails(reportType);
        }
        if (activityTypes != null) {
            if (activityTypes.size() == 0) {
                return ReportErrors.REPORT_TYPE_REQUIRED;
            }
            if (!EPConstants.REPORT_TYPE_ACTIVITY_MAP.get(reportType).containsAll(activityTypes)) {
                return ReportErrors.ACTIVITY_TYPE_LIST_INVALID.withDetails(activityTypes.toString());
            }
        }
        return null;
    }
    
    private static void setOtherOptions(ReportSpecificationImpl spec, ReportFormParameters formParams) {
        
        Boolean showEmptyRows = formParams.getShowEmptyRows();
        if (showEmptyRows != null) {
            spec.setDisplayEmptyFundingRows(showEmptyRows);
        }
        
        Boolean showEmptyColumnGroups = formParams.getShowEmptyColumnGroups();
        if (showEmptyColumnGroups != null) {
            spec.setDisplayEmptyFundingColumns(showEmptyColumnGroups);
        }
        
        Boolean forceHeaders = formParams.getForceHeaders();
        if (forceHeaders != null) {
            spec.setPopulateReportHeadersIfEmpty(forceHeaders);
        }
        String groupingOption = formParams.getGroupingOption();
        if (groupingOption != null) {
            ReportsUtil.setGroupingCriteria(spec, groupingOption);
        }
        
        Boolean summary = formParams.getSummary();
        if (summary != null) {
            spec.setSummaryReport(summary);
        }
        
        Boolean showOriginalCurrency = formParams.getShowOriginalCurrency();
        if (showOriginalCurrency != null) {
            spec.setShowOriginalCurrency(showOriginalCurrency);
        }
        
    }

    /**
     * Exports current report configuration to the map
     * 
     * @param config
     * @param reportId
     * @return
     */
    public static String exportToMap(final ReportConfig config, final Long reportId) {
        ReportSpecificationImpl spec = getReport(reportId);
        if (spec == null)
            return null;
        
        // detect layers view as a highest hierarchy from location columns
        String layersView = null;
        Set<String> orderedLocations = new LinkedHashSet<>(
                Arrays.asList(ColumnConstants.LOCATION_ADM_LEVEL_0, ColumnConstants.LOCATION_ADM_LEVEL_1,
                        ColumnConstants.LOCATION_ADM_LEVEL_2, ColumnConstants.LOCATION_ADM_LEVEL_3,
                        ColumnConstants.LOCATION));
        for (ReportColumn column : spec.getHierarchies()) {
            if (orderedLocations.contains(column.getColumnName())) {
                layersView = column.getColumnName();
                break;
            }
        }
        
        if (layersView == null) {
            // configure the default, that is the 1st sub-national level, e.g. Region if it is visible
            orderedLocations.remove(ColumnConstants.LOCATION_ADM_LEVEL_0);
            Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();
            for (String defaultOption : orderedLocations) {
                if (visibleColumns.contains(defaultOption)) {
                    layersView = defaultOption;
                    break;
                }
            }
        }
        config.setLayersView(layersView);
        
        // update the settings based on Measures
        Map<String, Object> settings = config.getSettings();
        // must be not null! but just in case something gets broken
        if (settings == null) {
            logger.error("No settings are provided - please fix!");
            settings = new HashMap<>();
            config.setSettings(settings);
        }
        
        // set default funding type
        String fundingType = SettingsConstants.DEFAULT_FUNDING_TYPE_ID;
        
        /*
         * NOTE: Requirement is still undefined clearly if any priority and which priority to be used,
         * so configuring meanwhile the definition list priority from FUNDING_TYPES,
         * because it seems some export mapping is still desired, though not clarified yet which one
         */
        // get first measure that is defined in FUNDING_TYPES
        // => first found has highest priority to consider as the default option
        MEASURE_TEST: for (String measureName : GisConstants.FUNDING_TYPES) {
            for (ReportMeasure measure : spec.getMeasures()) {
                if (measureName.equals(measure.getMeasureName())) {
                    fundingType = measureName;
                    break MEASURE_TEST;
                }
            }
        }
        
        settings.put(SettingsConstants.FUNDING_TYPE_ID, fundingType);
        
        // we need to stringify the final config
        String configJson;
        try {
            configJson = new ObjectMapper().writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to json representation.", e);
        }

        // configure api state json
        AmpApiState apiState = new AmpApiState();
        apiState.setTitle(spec.getReportName());
        apiState.setDescription(EPConstants.API_STATE_REPORT_EXPORT_DESCRIPTION);
        apiState.setStateBlob(configJson);
        
        // Saving the export to the user session.
        // Will there be any need to keep multiple states for the same report export?
        TLSUtils.getRequest().getSession().setAttribute(EPConstants.API_STATE_REPORT_EXPORT + reportId, apiState);
        
        return reportId.toString();
    }
    
    /**
     * Provides the saved api state for the given reportConfigId (at the moment = reportId)
     * 
     * @param reportConfigId
     * @return JsonBean with saved Api state
     */
    public static AmpApiState getApiState(String reportConfigId) {
        // TODO: can we safely remove it from session afterwards? 
        return (AmpApiState) TLSUtils.getRequest().getSession()
                .getAttribute(EPConstants.API_STATE_REPORT_EXPORT + reportConfigId);
    }
    
    /**
     * Last viewed reports are kept track for the user. This method allows to add a report id into
     * the 'last viewed' report list.
     * 
     * @param session, the session where the last viewed reports are kept
     * @param ampReportId, the id of the report to add as 'last viewed'
     */
    public static void addLastViewedReport(HttpSession session, Long ampReportId) {
        if (session.getAttribute(Constants.LAST_VIEWED_REPORTS) == null) {
            Comparator<AmpReports> ampReportsComparator = new Comparator<AmpReports>() {
                public int compare(AmpReports a, AmpReports b) {
                    return a.getAmpReportId().compareTo(b.getAmpReportId());
                }
            };
            session.setAttribute(Constants.LAST_VIEWED_REPORTS, new BoundedList<AmpReports>(5, ampReportsComparator));
        }

        BoundedList<AmpReports> bList = (BoundedList<AmpReports>) session.getAttribute(Constants.LAST_VIEWED_REPORTS);
        AmpReports report = (AmpReports) PersistenceManager.getSession().get(AmpReports.class, ampReportId);
        if ((report != null) && (!report.getDrilldownTab()))
            bList.add(report);

    }

    public static AmpReports getAmpReportFromSession(Integer reportToken) {
        MaxSizeLinkedHashMap<Integer, AmpReports> reportsList = (MaxSizeLinkedHashMap<Integer, AmpReports>) TLSUtils.getRequest().getSession().getAttribute("reportStack");
        if (reportsList == null) {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
        
        AmpReports ampReport = reportsList.get(reportToken);
        if (ampReport == null) {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
        return ampReport;
    }
    
    /**
     * The decimal format to be used for this report (the configured or the default one)
     * @param spec report specification
     * @return decimal format for report amounts
     */
    public static DecimalFormat getDecimalFormatOrDefault(ReportSpecification spec) {
        if (spec != null && spec.getSettings() != null && spec.getSettings().getCurrencyFormat() != null)
            return spec.getSettings().getCurrencyFormat();
        return FormatHelper.getDefaultFormat();
    }
    
    /**
     * The amounts unit configured or the default one to be used for the specified report
     * @param spec report specification
     * @return amount units
     */
    public static AmountsUnits getAmountsUnitsOrDefault(ReportSpecification spec) {
        if (spec != null && spec.getSettings() != null && spec.getSettings().getUnitsOption() != null)
            return spec.getSettings().getUnitsOption();
        return AmountsUnits.getDefaultValue();
    }
    

    public static String getUrl(AmpReports report) {
        return "/TEMPLATE/ampTemplate/saikuui_reports/index_reports.html#report/open/" + report.getAmpReportId();
    }
}
