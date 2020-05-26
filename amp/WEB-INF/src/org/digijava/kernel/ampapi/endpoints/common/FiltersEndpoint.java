package org.digijava.kernel.ampapi.endpoints.common;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.ampapi.endpoints.filters.ApprovalStatusFilterListManager;
import org.digijava.kernel.ampapi.endpoints.filters.FilterList;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersManager;
import org.digijava.kernel.ampapi.endpoints.filters.WorkspaceFilterListManager;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.FilterComponentType;
import org.digijava.kernel.ampapi.endpoints.util.FilterDataType;
import org.digijava.kernel.ampapi.endpoints.util.FilterDefinition;
import org.digijava.kernel.ampapi.endpoints.util.FilterFieldType;
import org.digijava.kernel.ampapi.endpoints.util.FilterReportType;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.request.TLSUtils;

/**
 * Filters Endpoint
 *
 * @author Viorel Chihai
 * 
 */
@Path("filters")
@Api("filters")
public class FiltersEndpoint {

    public FiltersEndpoint() { }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(value = "Retrieve available filters.")
    public List<AvailableMethod> getAvailableFilters(
            @ApiParam(value = "Report Type, D=donor, P=pledges", allowableValues = "D,P")
            @DefaultValue("D") @QueryParam("report-type") String reportType) {
        return EndpointUtils.getAvailableFilterMethods(FiltersEndpoint.class.getName(), reportType);
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
     * <dt><b>filtered</b><dd> - if the tree should be built dynamically.
     * If it is false, the list of items should be taken as it is.
     * </dl></br></br>
     * <p>
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
    @ApiMethod(id = "organizations", name = "Organizations")
    @ApiOperation(value = "Retrieve the data needed for building the 'Organizations' filters.",
            notes = "The response contains 2 objects - the list definitions and the values. \n"
                    + "The filter widget should create a tree for each organization type.")
    @FilterDefinition(tab = EPConstants.TAB_ORGANIZATIONS)
    public FilterList getOrganizations() {
        return FiltersManager.getInstance().getOrganizationFilterList();
    }

    /**
     * List the program settings and items of 'Programs' filter.
     * <p>
     * The structure of the response is similar to /organizations endpoint.
     *
     * @return tree definitions (filter types) and the list of programs
     */
    @GET
    @Path("/programs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "programs", name = "Programs")
    @ApiOperation(value = "Retrieve the data needed for building the 'Programs' filters.",
            notes = "The response contains 2 objects - the list definitions and the values. \n"
                    + "The filter widget should create a tree for each program settings.")
    @FilterDefinition(tab = EPConstants.TAB_PROGRAMS)
    public FilterList getPrograms() {
        return FiltersManager.getInstance().getProgramFilterList();
    }

    /**
     * List the sector schemas and items of 'Sectors' filter.
     * <p>
     * The structure of the response is similar to /organizations endpoint.
     *
     * @return tree definitions (filter types) and the tree structure of the sectors
     */
    @GET
    @Path("/sectors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "sectors", name = "Sectors")
    @FilterDefinition(tab = EPConstants.TAB_SECTORS)
    @ApiOperation(value = "Retrieve the data needed for building the 'Sectors' filters.",
            notes = "The response contains 2 objects - the list definitions and the values. \n"
                    + "The filter widget should create a tree for each sector scheme.")
    public FilterList getSectors() {
        return FiltersManager.getInstance().getSectorFilterList();
    }

    /**
     * List the locations of the 'Locations' filter.
     * <p>
     * The structure of the response is similar to /organizations endpoint.
     *
     * @return tree definitions (filter types) and the tree structure of the locations
     */
    @GET
    @Path("/locations")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "locations", name = "Locations")
    @FilterDefinition(tab = EPConstants.TAB_LOCATIONS)
    @ApiOperation(value = "Retrieve the data needed for building the 'Locations' filters.",
            notes = "The response contains 2 objects - the list definitions and the values. \n"
                    + "The filter widget should create a tree for each country.")
    public FilterList getLocations(@ApiParam(value = "Retrieve all countries, default false")
                                   @QueryParam("showAllCountries") @DefaultValue("false") boolean showAllCountries,
                                   @ApiParam(value = "Retrieve first level location, default false")
                                   @QueryParam("firstLevelOnly") @DefaultValue("false") boolean firstLevelOnly) {
        return FiltersManager.getInstance().getLocationFilterList(showAllCountries, firstLevelOnly);
    }

    /**
     * List the possible values of 'Approval Status' filter.
     *
     * @return filter definition and values of 'approval-status' filter
     */
    @GET
    @Path("/activityApprovalStatus")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.APPROVAL_STATUS, name = ColumnConstants.APPROVAL_STATUS)
    @FilterDefinition(tab = EPConstants.TAB_ACTIVITY, columns = ColumnConstants.APPROVAL_STATUS,
            visibilityCheck = "hasToShowActivityApprovalStatusFilter")
    @ApiOperation(value = "Retrieve the data needed for building the 'Approval Status' filter.",
            notes = "The response contains 2 objects - the list definition and the values. \n"
                    + "The filter widget should create a tree for 'Approval Status' values.")
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
    @ApiMethod(id = FiltersConstants.TYPE_OF_ASSISTANCE, name = ColumnConstants.TYPE_OF_ASSISTANCE)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.TYPE_OF_ASSISTANCE)
    @ApiOperation(value = "Retrieve the data needed for building the 'Type           of Assistance' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Type of Assistance' values.")
    public FilterList getTypeOfAssistance() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.TYPE_OF_ASSISTANCE);
    }

    /**
     * List the possible values of 'modalities' filter.
     *
     * @return filter definition and values of 'modalities' filter.
     */
    @GET
    @Path("/modalities/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.MODALITIES, name = ColumnConstants.MODALITIES)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.MODALITIES)
    @ApiOperation(value = "Retrieve the data needed for building the 'Type           of Assistance' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Type of Assistance' values.")
    public FilterList getModalities(@ApiParam(value = "Force SSC workspace, default false")
                                    @QueryParam("sscWorkspace") @DefaultValue("false") boolean forceSscWorkspace) {
        TLSUtils.getThreadLocalInstance().setForcedSSCWorkspace(forceSscWorkspace);
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.MODALITIES);
    }

    /**
     * Li∂st the possible values of 'Mode of Payment' filter.
     *
     * @return filter definition and values of 'mode-of-payment' filter.
     */
    @GET
    @Path("/modeOfPayment/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.MODE_OF_PAYMENT, name = ColumnConstants.MODE_OF_PAYMENT)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.MODE_OF_PAYMENT)
    @ApiOperation(value = "Retrieve the data needed for building the 'Mode of Payment' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Mode of Payment' values.")
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
    @ApiMethod(id = FiltersConstants.STATUS, name = FiltersConstants.ACTIVITY_STATUS_NAME)
    @ApiOperation(value = "Retrieve the data needed for building the 'Activity Status' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Activity Status' values.")
    @FilterDefinition(tab = EPConstants.TAB_ACTIVITY, columns = ColumnConstants.STATUS)
    public FilterList getActivityStatus() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.STATUS);
    }

    /**
     * List the possible values of 'Activity Budget' filter.
     *
     * @return filter definition and values of 'activity-budget' filter.
     */
    @GET
    @Path("/activityBudget/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.ACTIVITY_BUDGET, name = FiltersConstants.ACTIVITY_BUDGET_NAME)
    @ApiOperation(value = "Retrieve the data needed for building the 'Activity Budget' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Activity Budget' values.")
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.ACTIVITY_BUDGET)
    public FilterList getActivityBudget() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.ACTIVITY_BUDGET);
    }

    /**
     * List the possible values of 'Funding Status' filter.
     *
     * @return filter definition and values of 'funding-status' filter.
     */
    @GET
    @Path("/fundingStatus/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.FUNDING_STATUS)
    @ApiOperation(value = "Retrieve the data needed for building the 'Funding Status' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Funding Status' values.")
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
    @ApiMethod(id = FiltersConstants.EXPENDITURE_CLASS, name = ColumnConstants.EXPENDITURE_CLASS)
    @ApiOperation(value = "Retrieve the data needed for building the 'Expenditure Class' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Expenditure Class' values.")
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
    @ApiMethod(id = FiltersConstants.CONCESSIONALITY_LEVEL, name = ColumnConstants.CONCESSIONALITY_LEVEL)
    @ApiOperation(value = "Retrieve the data needed for building the 'Concessionality Level' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Concessionality Level' values.")
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
    @ApiMethod(id = FiltersConstants.PERFORMANCE_ALERT_LEVEL,
            columns = ColumnConstants.PERFORMANCE_ALERT_LEVEL, name = ColumnConstants.PERFORMANCE_ALERT_LEVEL)
    @ApiOperation(value = "Retrieve the data needed for building the 'Performance Alert Level' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Performance Alert Level' values.")
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
    @ApiMethod(id = FiltersConstants.FINANCING_INSTRUMENT, name = ColumnConstants.FINANCING_INSTRUMENT)
    @ApiOperation(value = "Retrieve the data needed for building the 'Financing Instrument' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Financing Instrument' values.")
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.FINANCING_INSTRUMENT)
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
    @ApiMethod(id = FiltersConstants.HUMANITARIAN_AID, name = ColumnConstants.HUMANITARIAN_AID)
    @ApiOperation(value = "Retrieve the data needed for building the 'Humanitarian Aid' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Humanitarian Aid' values.")
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
    @ApiMethod(id = FiltersConstants.DISASTER_RESPONSE_MARKER,
            name = ColumnConstants.DISASTER_RESPONSE_MARKER)
    @ApiOperation(value = "Retrieve the data needed for building the 'Disaster Response Marker' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Disaster Response Marker' values.")
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.DISASTER_RESPONSE_MARKER)
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
    @ApiMethod(id = FiltersConstants.TEAM, name = "Workspaces")
    @ApiOperation(value = "Retrieve the data needed for building the 'Workspaces' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Workspaces' values.")
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
    @Path("/computedYear")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.COMPUTED_YEAR, name = ColumnConstants.COMPUTED_YEAR)
    @ApiOperation(value = "Retrieve the data needed for building the 'Computed Year' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Computed Year' values.")
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.COMPUTED_YEAR,
            fieldType = FilterFieldType.OPTIONS, multiple = false)
    public FilterList getComputedYear() {
        return FiltersManager.getInstance().getComputedYearFilter();
    }

    /**
     * List the values of startYear and endYear of 'Date' filter.
     * <p>
     * The startYear and endYear values are taken from the items.values object.
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * "listDefinitions" :
     *  [
     *    {
     *     "name": "Date",
     *     "displayName": "Date",
     *     "filterIds": ["date"],
     *     "items": "values",
     *     "filtered": true
     *    }
     *  ],
     * "items" : {
     *   "values": [
     *      {
     *         "id" : 1985,
     *         "name" : "startYear",
     *         "value" : "1985"
     *      },
     *      {
     *         "id" : 2025,
     *         "name" : "endYear",
     *         "value" : "2025"
     *      }
     *   ]
     *  }
     * </pre>
     *
     * @return filter definition and year values (star and end) of 'date' filter.
     */
    @GET
    @Path("/date/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "date", name = "Date")
    @ApiOperation(value = "Retrieve the data needed for building the 'Date' filter.",
            notes = "This endpoint is used for fetching information about 'Date' filter. "
                    + "The items attribute contains information about the startYear and the endYear.")
    @FilterDefinition(tab = EPConstants.TAB_OTHER, dataType = FilterDataType.DATE)
    public FilterList getDates() {
        return FiltersManager.getInstance().getDateFilter();
    }

    @GET
    @Path("/proposedStartDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.PROPOSED_START_DATE, name = ColumnConstants.PROPOSED_START_DATE)
    @ApiOperation(value = "Generic endpoint for 'Proposed Start Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.PROPOSED_START_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE)
    public FilterList getProposedStartDate() {
        return new FilterList();
    }

    @GET
    @Path("/actualStartDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.ACTUAL_START_DATE, name = ColumnConstants.ACTUAL_START_DATE)
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.ACTUAL_START_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE)
    @ApiOperation(value = "Generic endpoint for 'Actual Start Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getActualStartDate() {
        return new FilterList();
    }

    @GET
    @Path("/actualApprovalDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.ACTUAL_APPROVAL_DATE, name = ColumnConstants.ACTUAL_APPROVAL_DATE)
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.ACTUAL_APPROVAL_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE)
    @ApiOperation(value = "Generic endpoint for 'Actual Approval Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getActualApprovalDate() {
        return new FilterList();
    }

    @GET
    @Path("/actualCompletionDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.ACTUAL_COMPLETION_DATE, name = ColumnConstants.ACTUAL_COMPLETION_DATE)
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.ACTUAL_COMPLETION_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE)
    @ApiOperation(value = "Generic endpoint for 'Actual Completion Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getActualCompletionDate() {
        return new FilterList();
    }

    @GET
    @Path("/effectiveFundingDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.EFFECTIVE_FUNDING_DATE, name = ColumnConstants.EFFECTIVE_FUNDING_DATE)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.EFFECTIVE_FUNDING_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE)
    @ApiOperation(value = "Generic endpoint for 'Effective Funding Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getEffectiveFundingDate() {
        return new FilterList();
    }

    @GET
    @Path("/finalDateContracting/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.FINAL_DATE_FOR_CONTRACTING, name = ColumnConstants.FINAL_DATE_FOR_CONTRACTING)
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.FINAL_DATE_FOR_CONTRACTING,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE)
    @ApiOperation(value = "Generic endpoint for 'Final Date for Contracting' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getFinalDateForContracting() {
        return new FilterList();
    }

    @GET
    @Path("/fundingClosingDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.FUNDING_CLOSING_DATE, name = ColumnConstants.FUNDING_CLOSING_DATE)
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.FUNDING_CLOSING_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE)
    @ApiOperation(value = "Generic endpoint for 'Funding Closing Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getFundingClosingDate() {
        return new FilterList();
    }

    @GET
    @Path("/issueDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.ISSUE_DATE, name = ColumnConstants.ISSUE_DATE)
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.ISSUE_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE)
    @ApiOperation(value = "Generic endpoint for 'Issue Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getIssueDate() {
        return new FilterList();
    }

    @GET
    @Path("/proposedApprovalDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.PROPOSED_APPROVAL_DATE, name = ColumnConstants.PROPOSED_APPROVAL_DATE)
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.PROPOSED_APPROVAL_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE)
    @ApiOperation(value = "Generic endpoint for 'Proposed Approval Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getProposedApprovalDate() {
        return new FilterList();
    }

    @GET
    @Path("/proposedCompletionDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.PROPOSED_COMPLETION_DATE, name = ColumnConstants.PROPOSED_COMPLETION_DATE)
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.PROPOSED_COMPLETION_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE)
    @ApiOperation(value = "Generic endpoint for 'Proposed Completion Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getProposedCompletionDate() {
        return new FilterList();
    }

    /**
     * List the donor types and groups.
     * <p>
     * The items object contains the values used to build the tree.
     *
     * @return tree definitions (filter types) and the list of pledges donor types and groups
     */
    @GET
    @Path("/pledgesDonors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "pledges-donors", name = "Pledges Donors")
    @ApiOperation(value = "Retrieve the data needed for building the 'Pledges Donors' filter.",
            notes = "The response contains 2 objects - the list definitions and the values. \n"
                    + "The filter widget should create a tree for 'Pledges Donors' values.")
    @FilterDefinition(tab = EPConstants.TAB_ORGANIZATIONS, reportType = FilterReportType.PLEDGE)
    public FilterList getPledgesDonros() {
        return FiltersManager.getInstance().getPledgesDonorFilterList();
    }

    /**
     * List the program settings and items of 'Pledges Programs' filter.
     * <p>
     * The structure of the response is similar to /organizations endpoint.
     *
     * @return tree definitions (filter types) and the list of pledges programs
     */
    @GET
    @Path("/pledgesPrograms")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "pledges-programs", name = "Pledges Programs")
    @ApiOperation(value = "Retrieve the data needed for building the 'Pledges Programs' filter.",
            notes = "The response contains 2 objects - the list definitions and the values. \n"
                    + "The filter widget should create a tree for each pledge program settings.")
    @FilterDefinition(tab = EPConstants.TAB_PROGRAMS, reportType = FilterReportType.PLEDGE)
    public FilterList getPledgesPrograms() {
        return FiltersManager.getInstance().getPledgesProgramFilterList();
    }

    /**
     * List the sector schemas and items of 'Pledges Sectors' filter.
     * <p>
     * The structure of the response is similar to /organizations endpoint.
     *
     * @return tree definitions (filter types) and the tree structure of the pledges sectors
     */
    @GET
    @Path("/pledgesSectors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "pledges-sectors", name = "Pledges Sectors")
    @ApiOperation(value = "Retrieve the data needed for building the 'Pledges Sectors' filter.",
            notes = "The response contains 2 objects - the list definitions and the values. \n"
                    + "The filter widget should create a tree for each pledge sector scheme.")
    @FilterDefinition(tab = EPConstants.TAB_SECTORS, reportType = FilterReportType.PLEDGE)
    public FilterList getPledgesSectors() {
        return FiltersManager.getInstance().getPledgesSectorFilterList();

    }

    /**
     * List the locations of the 'Pledges Locations' filter.
     * <p>
     * The structure of the response is similar to /organizations endpoint.
     *
     * @return tree definitions (filter types) and the tree structure of the pledges locations
     */
    @GET
    @Path("/pledgesLocations")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "pledges-locations", name = "Pledges Locations")
    @ApiOperation(value = "Retrieve the data needed for building the 'Pledges Locations' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for each country.")
    @FilterDefinition(tab = EPConstants.TAB_LOCATIONS, reportType = FilterReportType.PLEDGE)
    public FilterList getPledgesLocations() {
        return FiltersManager.getInstance().getPledgesLocationFilterList();
    }

    /**
     * List the possible values of 'Pledges Status' filter.
     *
     * @return filter definition and values of 'pledge-status' filter.
     */
    @GET
    @Path("/pledgesStatus/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.STATUS, name = ColumnConstants.PLEDGE_STATUS)
    @ApiOperation(value = "Retrieve the data needed for building the 'Pledges Status' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Pledges Status' values.")
    @FilterDefinition(tab = EPConstants.TAB_PLEDGE, columns = ColumnConstants.PLEDGE_STATUS,
            componentType = {FilterComponentType.REPORTS}, reportType = FilterReportType.PLEDGE)
    public FilterList getPledgesStatus() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.PLEDGES_STATUS);
    }

    /**
     * List the possible values of 'Pledges Aid of Modality' filter.
     *
     * @return filter definition and values of 'pledge-aid-of-modality' filter.
     */
    @GET
    @Path("/pledgesAidOfModality/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.PLEDGES_AID_MODALITY, name = ColumnConstants.PLEDGES_AID_MODALITY)
    @ApiOperation(value = "Retrieve the data needed for building the 'Pledges Aid of Modality' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Pledges Aid of Modality' values.")
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.PLEDGES_AID_MODALITY,
            componentType = {FilterComponentType.REPORTS}, reportType = FilterReportType.PLEDGE)
    public FilterList getPledgesAidOfModality() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.PLEDGES_AID_MODALITY);
    }

    /**
     * List the possible values of 'Pledges Type of Assistance' filter.
     *
     * @return filter definition and values of 'pledge-type-of-assistance' filter.
     */
    @GET
    @Path("/pledgesTypeOfAssistance/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.PLEDGES_TYPE_OF_ASSISTANCE, name = ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE)
    @ApiOperation(value = "Retrieve the data needed for building the 'Pledges Type of Assistance' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Pledges Type of Assistance' values.")
    @FilterDefinition(tab = EPConstants.TAB_FINANCIALS, columns = ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE,
            componentType = {FilterComponentType.REPORTS}, reportType = FilterReportType.PLEDGE)
    public FilterList getPledgesTypeOfAssistance() {
        return FiltersManager.getInstance().getCategoryValueFilter(FiltersConstants.PLEDGES_TYPE_OF_ASSISTANCE);
    }

    @GET
    @Path("/pledgesDetailStartDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.PLEDGES_DETAIL_START_DATE, name = ColumnConstants.PLEDGES_DETAIL_START_DATE)
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.PLEDGES_DETAIL_START_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE,
            componentType = {FilterComponentType.REPORTS}, reportType = FilterReportType.PLEDGE)
    @ApiOperation(value = "Generic endpoint for 'Pledges Detail Start Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getPledgesDetailStartDate() {
        return new FilterList();
    }

    @GET
    @Path("/pledgesDetailEndDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = FiltersConstants.PLEDGES_DETAIL_END_DATE, name = ColumnConstants.PLEDGES_DETAIL_END_DATE)
    @FilterDefinition(tab = EPConstants.TAB_OTHER, columns = ColumnConstants.PLEDGES_DETAIL_END_DATE,
            fieldType = FilterFieldType.DATE_RANGE, dataType = FilterDataType.DATE,
            componentType = {FilterComponentType.REPORTS}, reportType = FilterReportType.PLEDGE)
    @ApiOperation(value = "Generic endpoint for 'Pledges Detail End Date' filter.", hidden = true,
            notes = "Since the date filters doesn't have possible values, this endpoint returns an empty list.")
    public FilterList getPledgesDetailEndDate() {
        return new FilterList();
    }

    /**
     * List the possible values of 'Boundaries' filter.
     *
     * @return filter definition and values of 'boundaries' filter.
     */
    @GET
    @Path("/boundaries")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "boundaries", name = "Boundaries")
    @ApiOperation(value = "Retrieve the data needed for building the 'Boundaries' filter.",
            notes = "The response contains 2 objects - the filter definition and the values. \n"
                    + "The filter widget should create a tree for 'Boundaries' values.")
    @FilterDefinition(ui = false, tab = EPConstants.TAB_LOCATIONS)
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