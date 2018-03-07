package org.digijava.kernel.ampapi.endpoints.common;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.ampapi.endpoints.filters.ApprovalStatusFilterListManager;
import org.digijava.kernel.ampapi.endpoints.filters.FilterList;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersManager;
import org.digijava.kernel.ampapi.endpoints.filters.WorkspaceFilterListManager;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.FilterComponentType;
import org.digijava.kernel.ampapi.endpoints.util.FilterDefinition;
import org.digijava.kernel.ampapi.endpoints.util.FilterFieldType;
import org.digijava.kernel.ampapi.endpoints.util.FilterType;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;

/**
 * Retrieve available filters.
 * 
 * @author vchihai@developmentgateway.org
 * 
 */
@Path("nifilters")
public class NiFiltersEndpoint {

    private static final int DATE_START_YEAR = 1985;
    private static final int DATE_END_YEAR = 2025;

    public NiFiltersEndpoint() { }

    /**
     * Retrieve all filter definitions.
     * </br>
     * 
     * <dl>
     * Each filter definition holds information regarding:
     * <dt><b>id</b><dd> - the id of the filter
     * <dt><b>name</b><dd> - the name of the filter
     * <dt><b>endpoint</b><dd> - the relative URL where the values should be fetched
     * <dt><b>ui</b><dd> - whenever to display this filter in UI (widget)
     * <dt><b>method</b><dd> - the used method for fetching the values (see endpoint attribute)
     * <dt><b>columns</b><dd> - the visibility of the filters depends on the columns
     * <dt><b>tab</b><dd> - under which tab in Filter Widget should be visible the filter
     * <dt><b>fieldType</b><dd> - the type of the filter field. Used for building the Filter Widget
     * Possible Values (TREE|OPTIONS|DATE)
     * <dt><b>dataType</b><dd> - the type of the filter data. Used for building the Filter Widget
     * Possible Values (TEXT|DATE)
     * <dt><b>componentType</b><dd> - the AMP component where the filter can be visible
     * Used for building the Filter Widget. Possible Values (ALL|DASHBOARD|REPORTS|GIS|TAB|GPI_REPORT)
     * <dt><b>multiple</b><dd> - can be selected multiple values for the specific filter
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * [
     *   ...
     *   {
     *     "id": "type-of-assistance",
     *     "name": "Type Of Assistance",
     *     "endpoint": "/rest/nifilters/typeOfAssistance/",
     *     "ui": true,
     *     "method": "GET",
     *     "columns": ["Type Of Assistance"],
     *     "tab": "Financial",
     *     "fieldType": "TREE",
     *     "dataType": "TEXT",
     *     "componentType": ["ALL"],
     *     "multiple" : true
     *   }
     *   ....
     * ]</pre>
     *
     * @return list of filter definitions
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<AvailableMethod> getAvailableFilters() {
        return EndpointUtils.getAvailableFilterMethods(NiFiltersEndpoint.class.getName());
    }
    
    /**
     * List the organization types and items of 'Organizations' filter.
     * 
     * </br>
     * The response contains 2 objects - the list definitions and the values. 
     * Having this, the filter widget should create a tree for each list.
     * <dl>
     * Each filter definition holds information regarding:
     * <dt><b>id</b><dd> - the id of the filter. 
     * It is used during the fetching the children from the items object (see listDefinitionIds in items objects)
     * <dt><b>name</b><dd> - the name of the filter
     * <dt><b>displayName</b><dd> - the translated name of the filter. This will be shown in Filter Widget
     * <dt><b>filterIds</b><dd> - what filterId should be associated to the each tree item. 
     * If the filterId is an empty string, that level should removed form the tree.
     * <dt><b>tab</b><dd> - under which tab should be shown the filter tree
     * <dt><b>items</b><dd> - the name of the object from which the values should be fetched
     * <dt><b>filtered</b><dd> - if the tree should be build dynamically. 
     * If it is false, the list of items should be taken as it is. 
     * </dl></br></br>
     * 
     * The items object contains the values used to build the tree.
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * "listDefinitions" : 
     *  [
     *    ...
     *    {
     *     "id": 1,
     *     "name": "Donor",
     *     "displayName": "donor",
     *     "filterIds": ["donor-type", "donor-group", "donor-agency"],
     *     "tab": "Funding Organizations",
     *     "items": "organizations",
     *     "filtered": false
     *    },
     *    {
     *     "id": 8,
     *     "name": "Contracting Agency",
     *     "displayName": "Contracting Agency",
     *     "filterIds": ["", "contracting-agency-group", "contracting-agency"],
     *     "tab": "All Agencies",
     *     "items": "organizations",
     *     "filtered": false
     *    }
     *    ...
     *  ], 
     * "items" : {
     *   "organizations": [
     *     {
     *        "id": 4,
     *        "name": "Bilateral",
     *        "children": [
     *            {
     *               "id": 4,
     *               "name": "BILATERAL Group",
     *               "children": [
     *                  {
     *                     "id": 20,
     *                     "name": "Austria",
     *                     "acronym": "Austria",
     *                     "listDefinitionIds": [
     *                        1,
     *                        2,
     *                        4,
     *                        11
     *                     ]
     *                  },
     *                  {
     *                     "id": 21,
     *                     "name": "Belgium",
     *                     "acronym": "Belgium",
     *                     "listDefinitionIds": [
     *                        1
     *                     ]
     *                  },
     *                  ...
     *               ]
     *            },
     *            ...
     *         ]
     *      },
     *      ...
     *   ]
     *  }
     * </pre>
     * 
     * @return tree definitions (filter types) and the list of organizations
     */
    @GET
    @Path("/organizations")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = "organizations", name = "Organizations")
    @FilterDefinition(tab = EPConstants.TAB_ORGANIZATIONS)
    public FilterList getOrganizations() {
        return FiltersManager.getInstance().getOrganizationFilterList();
    }
    
    /**
     * List the program settings and items of 'Programs' filter.
     * 
     * The structure of the response is similar to /organizations endpoint.
     * 
     * @return tree definitions (filter types) and the list of programs
     */
    @GET
    @Path("/programs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = "programs", name = "Programs")
    @FilterDefinition(tab = EPConstants.TAB_PROGRAMS)
    public FilterList getPrograms() {
        return FiltersManager.getInstance().getProgramFilterList();
    }
    
    /**
     * List the sector schemas and items of 'Sectors' filter.
     * 
     * The structure of the response is similar to /organizations endpoint.
     * 
     * @return tree definitions (filter types) and the tree structure of the sectors
     */
    @GET
    @Path("/sectors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = "sectors", name = "Sectors")
    @FilterDefinition(tab = EPConstants.TAB_SECTORS)
    public FilterList getSectors() {
        return FiltersManager.getInstance().getSectorFilterList();
    }
    
    /**
     * List the locations of the 'Locations' filter.
     * 
     * The structure of the response is similar to /organizations endpoint.
     * 
     * @return tree definitions (filter types) and the tree structure of the locations
     */
    @GET
    @Path("/locations")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = "locations", name = "Locations")
    @FilterDefinition(tab = EPConstants.TAB_LOCATIONS)
    public FilterList getLocations() {
        return FiltersManager.getInstance().getLocationFilterList();
    }

    /**
     * List the possible values of 'Approval Status' filter.
     * 
     * @return filter definition and values of 'approval-status' filter
     */
    @GET
    @Path("/activityApprovalStatus")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.APPROVAL_STATUS, name = ColumnConstants.APPROVAL_STATUS)
    @FilterDefinition(tab = EPConstants.TAB_ACTIVITY, columns = ColumnConstants.APPROVAL_STATUS, 
                        visibilityCheck = "hasToShowActivityapprovalStatusFilter")
    public FilterList getActivityApprovalStatus() {
        return FiltersManager.getInstance().getApprovalStatusFilter();
    }

    /**
     * List the possible values of 'Type Of Assistance' filter.
     * 
     * @return filter definition and values of 'type-of-assistance' filter.
     */
    @GET
    @Path("/typeOfAssistance/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.TYPE_OF_ASSISTANCE, name = ColumnConstants.TYPE_OF_ASSISTANCE)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.TYPE_OF_ASSISTANCE)
    public FilterList getTypeOfAssistance() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.TYPE_OF_ASSISTANCE);
    }
    
    /**
     * List the possible values of 'Mode of Payment' filter.
     * 
     * @return filter definition and values of 'mode-of-payment' filter.
     */
    @GET
    @Path("/modeOfPayment/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.MODE_OF_PAYMENT, name = ColumnConstants.MODE_OF_PAYMENT)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.MODE_OF_PAYMENT)
    public FilterList getModeOfPayment() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.MODE_OF_PAYMENT);
    }
    
    /**
     * List the possible values of 'Activity Status' filter.
     * 
     * @return filter definition and values of 'status' filter.
     */
    @GET
    @Path("/activityStatus/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.STATUS, name = "Activity Status")
    @FilterDefinition(tab = EPConstants.TAB_ACTIVITY, columns = ColumnConstants.STATUS)
    public FilterList getActivityStatus() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.STATUS);
    }

    /**
     * List the possible values of 'Activity Budget' filter.
     * 
     * @return filter definition and values of 'on-off-treasure-budget' filter.
     */
    @GET
    @Path("/activityBudget/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.ON_OFF_TREASURY_BUDGET, name = "Activity Budget")
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.ON_OFF_TREASURY_BUDGET)
    public FilterList getActivityBudget() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.ON_OFF_TREASURY_BUDGET);
    }   
    
    /**
     * List the possible values of 'Funding Status' filter.
     * 
     * @return filter definition and values of 'funding-status' filter.
     */
    @GET
    @Path("/fundingStatus/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.FUNDING_STATUS)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.FUNDING_STATUS)
    public FilterList getFundingStatus() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.FUNDING_STATUS);
    }
    
    /**
     * List the possible values of 'Expenditure Class' filter.
     * 
     * @return filter definition and values of 'expenditure-class' filter.
     */
    @GET
    @Path("/expenditureClass/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.EXPENDITURE_CLASS, name = ColumnConstants.EXPENDITURE_CLASS)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.EXPENDITURE_CLASS)
    public FilterList getExpenditureClass() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.EXPENDITURE_CLASS);
    }
    
    /**
     * List the possible values of 'Concessionality Level' filter.
     * 
     * @return filter definition and values of 'concessionality-level' filter.
     */
    @GET
    @Path("/concessionalityLevel/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.CONCESSIONALITY_LEVEL, name = ColumnConstants.CONCESSIONALITY_LEVEL)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.CONCESSIONALITY_LEVEL)
    public FilterList getConcessionalityLevel() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.CONCESSIONALITY_LEVEL);
    }

    /**
     * List the possible values of 'Performance Alert Level' filter.
     * 
     * @return filter definition and values of 'performance-alert-level' filter.
     */
    @GET
    @Path("/performanceAlertLevel")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.PERFORMANCE_ALERT_LEVEL,
            columns = ColumnConstants.PERFORMANCE_ALERT_LEVEL, name = ColumnConstants.PERFORMANCE_ALERT_LEVEL)
    @FilterDefinition(tab = EPConstants.TAB_ACTIVITY, columns = ColumnConstants.PERFORMANCE_ALERT_LEVEL)
    public FilterList getPerformanceAlertLevel() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.PERFORMANCE_ALERT_LEVEL);
    }

    /**
     * List the possible values of 'Financing Instrument' filter.
     * 
     * @return filter definition and values of 'financing-instrument' filter.
     */
    @GET
    @Path("/financingInstruments/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.FINANCING_INSTRUMENT, name = ColumnConstants.FINANCING_INSTRUMENT)
    @FilterDefinition(tab = EPConstants.TAB_ACTIVITY, columns = ColumnConstants.FINANCING_INSTRUMENT)
    public FilterList getFinancingInstruments() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.FINANCING_INSTRUMENT);
    }
    
    /**
     * List the possible values of 'Humanitarian Aid' filter.
     * 
     * @return filter definition and values of 'humanitarian-aid' filter.
     */
    @GET
    @Path("/humanitarianAid/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.HUMANITARIAN_AID, name = ColumnConstants.HUMANITARIAN_AID)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.HUMANITARIAN_AID)
    public FilterList getHumanitarianAid() {
        return FiltersManager.getInstance().getBooleanFilter(FiltersConstants.HUMANITARIAN_AID);
    }
    
    /**
     * List the possible values of 'Disaster Response Marker' filter.
     * 
     * @return filter definition and values of 'disaster-response-marker' filter.
     */
    @GET
    @Path("/disasterResponse/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.DISASTER_RESPONSE_MARKER, 
                    name = ColumnConstants.DISASTER_RESPONSE_MARKER)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.DISASTER_RESPONSE_MARKER)
    public FilterList getDisasterResponse() {
        return FiltersManager.getInstance().getBooleanFilter(FiltersConstants.DISASTER_RESPONSE_MARKER);
    }
    
    /**
     * List the possible values of 'Workspaces' filter.
     * 
     * @return filter definition and values of 'team' filter.
     */
    @GET
    @Path("/workspaces")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.TEAM, name = "Workspaces")
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.TEAM, 
                        visibilityCheck = "hasToShowWorkspaceFilter")
    public FilterList getWorkspaces() {
        return FiltersManager.getInstance().getWorkspaceFilter();
    }

    /**
     * List the possible values of 'Computed Year' filter.
     * 
     * @return filter definition and values of 'computed-year' filter.
     */
    @GET
    @Path("/computed-year")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.COMPUTED_YEAR, name = ColumnConstants.COMPUTED_YEAR)
    @FilterDefinition(tab = EPConstants.TAB_OTHER,  columns = ColumnConstants.COMPUTED_YEAR,
                      fieldType = FilterFieldType.OPTIONS, multiple = false,
                      componentType = {FilterComponentType.REPORTS, FilterComponentType.TAB})
    public FilterList getComputedYear() {
        return FiltersManager.getInstance().getComputedYearFilter();
    }

    @GET
    @Path("/date/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Date", id = "date", tab = EPConstants.TAB_OTHER)
    public JsonBean getDates() {
        JsonBean date = new JsonBean();
        date.set("startYear", DATE_START_YEAR);
        date.set("endYear", DATE_END_YEAR);
        return date;
    }
    
    @GET
    @Path("/proposedStartDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.PROPOSED_START_DATE, columns = ColumnConstants.PROPOSED_START_DATE,
            id = FiltersConstants.PROPOSED_START_DATE, filterType = {
            FilterType.REPORTS, FilterType.TAB }, tab = EPConstants.TAB_OTHER)
    public JsonBean getProposedStartDate() {
        return new JsonBean();
    }
    
    @GET
    @Path("/actualStartDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ACTUAL_START_DATE, columns = ColumnConstants.ACTUAL_START_DATE,
            id = FiltersConstants.ACTUAL_START_DATE, filterType = {
            FilterType.REPORTS, FilterType.TAB }, tab = EPConstants.TAB_OTHER)
    public JsonBean getActualStartDate() {
        return new JsonBean();
    }
    
    @GET
    @Path("/actualApprovalDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ACTUAL_APPROVAL_DATE, columns = ColumnConstants.ACTUAL_APPROVAL_DATE,
            id = FiltersConstants.ACTUAL_APPROVAL_DATE, filterType = {
            FilterType.REPORTS, FilterType.TAB, FilterType.GPI_REPORTS }, tab = EPConstants.TAB_OTHER)
    public JsonBean getActualApprovalDate() {
        return new JsonBean();
    }
    
    @GET
    @Path("/actualCompletionDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ACTUAL_COMPLETION_DATE, 
            columns = ColumnConstants.ACTUAL_COMPLETION_DATE,
            id = FiltersConstants.ACTUAL_COMPLETION_DATE, filterType = {
            FilterType.REPORTS, FilterType.TAB }, tab = EPConstants.TAB_OTHER)
    public JsonBean getActualCompletionDate() {
        return new JsonBean();
    }
    
    @GET
    @Path("/effectiveFundingDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.EFFECTIVE_FUNDING_DATE, 
            columns = ColumnConstants.EFFECTIVE_FUNDING_DATE,
            id = FiltersConstants.EFFECTIVE_FUNDING_DATE, tab = EPConstants.TAB_FINANCIALS)
    public JsonBean getEffectiveFundingDate() {
        return new JsonBean();
    }
    
    @GET
    @Path("/finalDateContracting/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.FINAL_DATE_FOR_CONTRACTING, 
            columns = ColumnConstants.FINAL_DATE_FOR_CONTRACTING,
            id = FiltersConstants.FINAL_DATE_FOR_CONTRACTING, filterType = {
            FilterType.REPORTS, FilterType.TAB }, tab = EPConstants.TAB_OTHER)
    public JsonBean getDateForContracting() {
        return new JsonBean();
    }

    @GET
    @Path("/fundingClosingDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.FUNDING_CLOSING_DATE, columns = ColumnConstants.FUNDING_CLOSING_DATE,
            id = FiltersConstants.FUNDING_CLOSING_DATE, tab = EPConstants.TAB_FINANCIALS)
    public JsonBean getFundingClosingDate() {
        return new JsonBean();
    }

    @GET
    @Path("/issueDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ISSUE_DATE, columns = ColumnConstants.ISSUE_DATE,
            id = FiltersConstants.ISSUE_DATE, filterType = {
            FilterType.REPORTS, FilterType.TAB }, tab = EPConstants.TAB_OTHER)
    public JsonBean getIssueDate() {
        return new JsonBean();
    }
    
    @GET
    @Path("/proposedApprovalDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.PROPOSED_APPROVAL_DATE, 
            columns = ColumnConstants.PROPOSED_APPROVAL_DATE,
            id = FiltersConstants.PROPOSED_APPROVAL_DATE, filterType = {
            FilterType.REPORTS, FilterType.TAB }, tab = EPConstants.TAB_OTHER)
    public JsonBean getProposedApprovalDate() {
        return new JsonBean();
    }  
    
    @GET
    @Path("/proposedCompletionDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.PROPOSED_COMPLETION_DATE, 
            columns = ColumnConstants.PROPOSED_COMPLETION_DATE,
            id = FiltersConstants.PROPOSED_COMPLETION_DATE, filterType = {
            FilterType.REPORTS, FilterType.TAB }, tab = EPConstants.TAB_OTHER)
    public JsonBean getProposedCompletionDate() {
        return new JsonBean();
    }
    
    /**
     * List the possible values of 'Boundaries' filter.
     * 
     * @return filter definition and values of 'boundaries' filter.
     */
    @GET
    @Path("/boundaries")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Boundaries", tab = EPConstants.TAB_LOCATIONS)
    public List<String> getBoundaries() {
        return QueryUtil.getImplementationLocationsInUse();
    }
    
    public boolean hasToShowActivityApprovalStatusFilter() {
        return ApprovalStatusFilterListManager.getInstance().isVisible();
    }
    
    public boolean hasToShowWorkspaceFilter() {
        return WorkspaceFilterListManager.getInstance().isVisible();
     }
    
}