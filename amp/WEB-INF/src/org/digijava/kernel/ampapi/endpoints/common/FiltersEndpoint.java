package org.digijava.kernel.ampapi.endpoints.common;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.AmpEndpoint;
import org.digijava.kernel.ampapi.endpoints.filters.FilterList;
import org.digijava.kernel.ampapi.endpoints.common.model.FilterDescriptor;
import org.digijava.kernel.ampapi.endpoints.common.model.Location;
import org.digijava.kernel.ampapi.endpoints.common.model.Org;
import org.digijava.kernel.ampapi.endpoints.common.model.OrgGroup;
import org.digijava.kernel.ampapi.endpoints.common.model.OrgType;
import org.digijava.kernel.ampapi.endpoints.common.model.YearRange;
import org.digijava.kernel.ampapi.endpoints.dto.FilterValue;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersBuilder;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersManager;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.settings.SettingField;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AmpThemeSkeleton;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.ObjectNotFoundException;

/**
 * Class that holds method related to filters (available options, available filters)
 * 
 * @author jdeanquin@developmentgateway.org
 * 
 */
@Path("filters")
@Api("filters")
public class FiltersEndpoint implements AmpEndpoint {
    private static final String DISPLAY_NAME_PROPERTY = "DisplayName";
    private static final String NAME_PROPERTY = "Name";
    private static final String SECTORS_SUFFIX = " Sectors";
    private static final Logger logger = Logger.getLogger(FiltersEndpoint.class);
    
    // todo
    // probably not the best place to keep, but definitely better than in the method
    private static final String PRIVATE_WS_CONDITION = "WHERE (isolated is false) OR (isolated is null)";
    private static final String PARENT_WS_CONDITION = "WHERE parent_team_id = ";

    private static final int START_YEAR = 1985;
    private static final int END_YEAR = 2025;


    //AmpARFilter filters;
    
    public FiltersEndpoint() {
        //filters = new AmpARFilter();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<AvailableMethod> getAvailableFilters() {
        return EndpointUtils.getAvailableMethods(FiltersEndpoint.class.getName(),true);
    }


    @GET
    @Path("/activityapprovalStatus")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.APPROVAL_STATUS, columns = ColumnConstants.APPROVAL_STATUS,
                name = "Approval Status", visibilityCheck = "hasToShowActivityapprovalStatusFilter", tab=EPConstants.TAB_ACTIVITY)
    @ApiOperation("Return activity status options")
    public FilterDescriptor getActivityApprovalStatus() {
        FilterDescriptor as = new FilterDescriptor();

        List<FilterValue> activityStatus = new ArrayList<FilterValue>();
        for (String key : AmpARFilter.VALIDATION_STATUS.keySet()) {
            FilterValue sjb = new FilterValue();
            sjb.setId(AmpARFilter.VALIDATION_STATUS.get(key));
            sjb.setName(TranslatorWorker.translateText(key));
            activityStatus.add(sjb);
        }
        activityStatus = orderByProperty(activityStatus, NAME_PROPERTY);
        as.setFilterId(FiltersConstants.APPROVAL_STATUS);
        as.setName(TranslatorWorker.translateText(ColumnConstants.APPROVAL_STATUS));
        as.setValues(activityStatus);

        return as;
    }

    private AmpTeam getAmpTeam() {
        TeamMember teamMember = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(
                Constants.CURRENT_MEMBER);
        AmpTeam ampTeam = null;
        if (teamMember != null) {
            ampTeam = TeamUtil.getAmpTeam(teamMember.getTeamId());
        }
        return ampTeam;
    }

    /**
     * Returns fi the approval status filter should be shown
     *
     * @return
     */
    public boolean hasToShowActivityapprovalStatusFilter() {
        return (TLSUtils.getRequest().getSession().getAttribute(
                org.digijava.module.aim.helper.Constants.CURRENT_MEMBER) != null);
    }

    @GET
    @Path("/boundaries")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false,  id = "Boundaries", tab=EPConstants.TAB_LOCATIONS)
    @ApiOperation("Return the admin levels for filtering")
    public List<String> getBoundaries() {
        return QueryUtil.getImplementationLocationsInUse();
    }

    @GET
    @Path("/sectors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Sectors", id = "Sectors", tab = EPConstants.TAB_SECTORS)
    @ApiOperation("Returns the sector schema lists")
    public List<FilterValue> getSectorsSchemas() throws AmpApiException {
        List<FilterValue> sectorList = new ArrayList<FilterValue>();
        List<AmpClassificationConfiguration> schems = SectorUtil.getAllClassificationConfigs();
        Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();
        for (AmpClassificationConfiguration ampClassificationConfiguration : schems) {
            final String columnName = AmpClassificationConfiguration.NAME_TO_COLUMN_MAP
                    .get(ampClassificationConfiguration.getName()); 
            if (visibleColumns.contains(columnName)) {
                Long sectorConfigId = ampClassificationConfiguration.getId();
                String sectorDisplayName = TranslatorWorker.translateText(ampClassificationConfiguration.getName() + SECTORS_SUFFIX);
                
                FilterValue sectorBean = new FilterValue(sectorConfigId, sectorDisplayName);
                sectorBean.setFilterId(FilterUtils.INSTANCE.idFromColumnName(columnName));
                
                sectorList.add(sectorBean);
            }
        }
        
        return sectorList;
    }

    @GET
    @Path("/sectors/{sectorId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "SectorsById", tab = EPConstants.TAB_SECTORS)
    @ApiOperation("Return the sector filtered by the given sectorName")
    public FilterValue getSectors(@PathParam("sectorId") Long sectorId) {

        FilterValue sector = new FilterValue();

        try {
            AmpClassificationConfiguration c = SectorUtil
                    .getClassificationConfigById(sectorId);

            String sectorConfigName = c.getName();
            List<FilterValue> ampSectorsList = new ArrayList<FilterValue>();
            sector.setId(sectorId);
            sector.setName(TranslatorWorker.translateText(sectorConfigName + SECTORS_SUFFIX));
            List<AmpSector> s = SectorUtil
                    .getAmpSectorsAndSubSectorsHierarchy(sectorConfigName);
            for (AmpSector ampSector : s) {
                ampSectorsList.add(getSectors(ampSector,sectorConfigName,1));
            }
            ampSectorsList = orderByProperty(ampSectorsList,NAME_PROPERTY);
            sector.setChildren(ampSectorsList);
        } catch (DgException e) {
            logger.error("Cannot get sector by id",e);
        }
        return sector;
    }

    @GET
    @Path("/date/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Date", id = "date", tab = EPConstants.TAB_OTHER)
    @ApiOperation("Return the year range configure for GIS")
    public YearRange getDates() {
        YearRange range = new YearRange();
        range.setStartYear(START_YEAR);
        range.setEndYear(END_YEAR);
        return range;
        //return getDefaultDate(); // tabs/saiku should have this empty by default; gis/dashboards fill it client side
        // the API does not offer server-side the possibility of knowing the kind of filter widget being filtered, so instead
        // the settings API ships them all client side and they are sorted out there
        //return new JsonBean();
    }
    
    @GET
    @Path("/proposedStartDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.PROPOSED_START_DATE, columns = ColumnConstants.PROPOSED_START_DATE,
            id = FiltersConstants.PROPOSED_START_DATE, tab = EPConstants.TAB_OTHER)
    public void getProposedStartDate() {
    }
    
    @GET
    @Path("/actualStartDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ACTUAL_START_DATE, columns = ColumnConstants.ACTUAL_START_DATE,
            id = FiltersConstants.ACTUAL_START_DATE, tab = EPConstants.TAB_OTHER)
    public void getActualStartDate() {
    }
    
    @GET
    @Path("/proposedCompletionDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.PROPOSED_COMPLETION_DATE, columns = ColumnConstants.PROPOSED_COMPLETION_DATE,
            id = FiltersConstants.PROPOSED_COMPLETION_DATE, tab = EPConstants.TAB_OTHER)
    public void getProposedCompletionDate() {
    }
    
    @GET
    @Path("/actualCompletionDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ACTUAL_COMPLETION_DATE, columns = ColumnConstants.ACTUAL_COMPLETION_DATE,
            id = FiltersConstants.ACTUAL_COMPLETION_DATE, tab = EPConstants.TAB_OTHER)
    public void getActualCompletionDate() {
    }

    @GET
    @Path("/finalDateContracting/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.FINAL_DATE_FOR_CONTRACTING, columns = ColumnConstants.FINAL_DATE_FOR_CONTRACTING,
            id = FiltersConstants.FINAL_DATE_FOR_CONTRACTING, tab = EPConstants.TAB_OTHER)
    public void getDateForContracting() {
    }

    @GET
    @Path("/issueDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ISSUE_DATE, columns = ColumnConstants.ISSUE_DATE,
            id = FiltersConstants.ISSUE_DATE, tab = EPConstants.TAB_OTHER)
    public void getIssueDate() {
    }
    
    @GET
    @Path("/proposedApprovalDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.PROPOSED_APPROVAL_DATE, columns = ColumnConstants.PROPOSED_APPROVAL_DATE,
            id = FiltersConstants.PROPOSED_APPROVAL_DATE, tab = EPConstants.TAB_OTHER)
    public void getProposedApprovalDate() {
    }

    @GET
    @Path("/actualApprovalDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ACTUAL_APPROVAL_DATE, columns = ColumnConstants.ACTUAL_APPROVAL_DATE,
            id = FiltersConstants.ACTUAL_APPROVAL_DATE, tab = EPConstants.TAB_OTHER)
    public void getActualApprovalDate() {
    }

    @GET
    @Path("/programs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Programs", id = "Programs", tab = EPConstants.TAB_PROGRAMS)
    @ApiOperation("Return the programs filtered by the given sectorName")
    public List<FilterValue> getPrograms() {
        List<FilterValue> programs = new ArrayList<FilterValue>();
        try {
            Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();

            List<Object[]> progs = PersistenceManager.getSession().createSQLQuery("SELECT amp_program_settings_id, name FROM amp_program_settings").list();
            for (Object[] program : progs) {
                String programName = String.valueOf(program[1]);
                final String columnName = ProgramUtil.NAME_TO_COLUMN_MAP.get(String.valueOf(program[1]));
                // only add if its enabled
                if (visibleColumns.contains(columnName)) {
                    Long programSettingId = PersistenceManager.getLong(program[0]);
                    String translatedProgramName = TranslatorWorker.translateText(programName);
                    FilterValue bean = new FilterValue(programSettingId, translatedProgramName);
                    bean.setFilterId(FilterUtils.INSTANCE.idFromColumnName(columnName));
                    programs.add(bean);
                }
            }
            return programs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/org-types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Types of Organizations", id = "organizationTypesList")
    @ApiOperation("Return org types with its orgs groups")
    public List<OrgType> getOrgTypes() {
        List<OrgType> orgTypes = QueryUtil.getOrgTypes();
        orderByName(orgTypes, OrgType::getName);
        return orgTypes;
    }

    @GET
    @Path("/org-groups")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Organization Groups", id = "orgGroupsList")
    @ApiOperation("Return org groups with its orgs ids")
    public List<OrgGroup> getOrgGroups() {
        List<OrgGroup> orgGroups = QueryUtil.getOrgGroups();
        orderByName(orgGroups, OrgGroup::getName);
        return orgGroups;
    }

    @GET
    @Path("/orgs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = "Organizations", name = "orgsList", tab=EPConstants.TAB_ORGANIZATIONS)
    @ApiOperation("List all available orgs")
    public List<Org> getOrgs() {
        List<Org> orgs = QueryUtil.getOrgs();
        orderByName(orgs, Org::getName);
        return orgs;
    }



    @GET
    @Path("/org-roles")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Organization Roles", id = "orgRolesList", tab=EPConstants.TAB_ALL_AGENCIES)
    @ApiOperation("List all available orgs roles")
    public List<FilterValue> getorgRoles() {
        List<FilterValue> orgRoles = QueryUtil.getOrgRoles();
        return orderByProperty(orgRoles,DISPLAY_NAME_PROPERTY);
    }

    @GET
    @Path("/programs/{programId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ProgramsByProgramName", tab = EPConstants.TAB_PROGRAMS)
    @ApiOperation("Return the programs filtered by the given programSettingsId")
    public FilterValue getPrograms(@PathParam("programId") Long programId) {
        try {
            Object[] idname = (Object[]) PersistenceManager.getSession().createSQLQuery("select default_hierarchy, name from amp_program_settings where amp_program_settings_id = " + programId).uniqueResult();
            Long rootAmpThemeId = idname == null ? null : PersistenceManager.getLong(idname[0]);
            if (rootAmpThemeId != null) {
                String schemeName = String.valueOf(idname[1]);
                Map<Long, AmpThemeSkeleton> themes = AmpThemeSkeleton.populateThemesTree(rootAmpThemeId);
                String programName = schemeName.equals(ProgramUtil.NATIONAL_PLAN_OBJECTIVE)
                        ? ProgramUtil.NATIONAL_PLANNING_OBJECTIVES : schemeName;
                FilterValue bean = buildProgramsJsonBean(themes.get(rootAmpThemeId), programName, 0);
                bean.setFilterId(FilterUtils.INSTANCE.idFromColumnName(programName + " Level 1"));
                return bean;
            } else {
                return new FilterValue();
            }

        } catch (ObjectNotFoundException e) {
            return new FilterValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/typeOfAssistance/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.TYPE_OF_ASSISTANCE, columns = ColumnConstants.TYPE_OF_ASSISTANCE,
            name="Type of Assistance", tab=EPConstants.TAB_FINANCIALS)
    @ApiOperation("Return type of assistance")
    public FilterDescriptor getTypeOfAssistance() {
        return getCategoryValue(CategoryConstants.TYPE_OF_ASSISTENCE_KEY, ColumnConstants.TYPE_OF_ASSISTANCE);
    }

    @GET
    @Path("/modeOfPayment/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.MODE_OF_PAYMENT, columns = ColumnConstants.MODE_OF_PAYMENT,
            name="Mode of Payment", tab=EPConstants.TAB_FINANCIALS)
    @ApiOperation("Return mode of payment")
    public FilterDescriptor getModeOfPayment() {
        return getCategoryValue(CategoryConstants.MODE_OF_PAYMENT_KEY, ColumnConstants.MODE_OF_PAYMENT);
    }

    @GET
    @Path("/activityStatus/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.STATUS, columns = ColumnConstants.STATUS,name="Activity Status",
            tab=EPConstants.TAB_ACTIVITY)
    @ApiOperation("Return Activitystatus")
    public FilterDescriptor getActivityStatus() {
        return getCategoryValue(CategoryConstants.ACTIVITY_STATUS_KEY, ColumnConstants.STATUS);
    }

    @GET
    @Path("/activityBudget/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.ACTIVITY_BUDGET, columns = ColumnConstants.ACTIVITY_BUDGET,
            name="Activity Budget", tab=EPConstants.TAB_FINANCIALS)
    @ApiOperation("Return Activity Budget")
    public FilterDescriptor getActivityBudget() {
        return getCategoryValue(CategoryConstants.ACTIVITY_BUDGET_KEY, ColumnConstants.ACTIVITY_BUDGET);
    }   

    @GET
    @Path("/fundingStatus/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.FUNDING_STATUS, columns = ColumnConstants.FUNDING_STATUS,
            name="Funding Status",tab=EPConstants.TAB_FINANCIALS)
    @ApiOperation("Funding status filter information")
    public FilterDescriptor getFundingStatus() {
        return getCategoryValue(CategoryConstants.FUNDING_STATUS_KEY, ColumnConstants.FUNDING_STATUS);
    }

    @GET
    @Path("/expenditureClass/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.EXPENDITURE_CLASS, columns = ColumnConstants.EXPENDITURE_CLASS,
            name="Expenditure Class", tab=EPConstants.TAB_FINANCIALS)
    @ApiOperation("Funding status filter information")
    public FilterDescriptor getExpenditureClass() {
        return getCategoryValue(CategoryConstants.EXPENDITURE_CLASS_KEY, ColumnConstants.EXPENDITURE_CLASS);
    }

    @GET
    @Path("/concessionalityLevel/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.CONCESSIONALITY_LEVEL, columns = ColumnConstants.CONCESSIONALITY_LEVEL,
            name="Concessionality Level", tab=EPConstants.TAB_FINANCIALS)
    @ApiOperation("Funding concessionality level information")
    public FilterDescriptor getConcessionalityLevel() {
        return getCategoryValue(CategoryConstants.CONCESSIONALITY_LEVEL_KEY, ColumnConstants.CONCESSIONALITY_LEVEL);
    }

    @GET
    @Path("/performanceAlertLevel")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.PERFORMANCE_ALERT_LEVEL,
            columns = ColumnConstants.PERFORMANCE_ALERT_LEVEL, name = ColumnConstants.PERFORMANCE_ALERT_LEVEL,
            tab = EPConstants.TAB_ACTIVITY)
    @ApiOperation("Performance Alert Level filter")
    public FilterDescriptor getPerformanceAlertLevel() {
        return getCategoryValue(CategoryConstants.PERFORMANCE_ALERT_LEVEL_KEY, ColumnConstants.PERFORMANCE_ALERT_LEVEL);
    }

    @GET
    @Path("/performanceAlertType")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.PERFORMANCE_ALERT_TYPE,
            columns = ColumnConstants.PERFORMANCE_ALERT_TYPE, name = ColumnConstants.PERFORMANCE_ALERT_TYPE,
            tab = EPConstants.TAB_ACTIVITY)
    @ApiOperation("Performance Alert Type filter")
    public FilterDescriptor getPerformanceAlertType() {
        FilterDescriptor pt = new FilterDescriptor();

        List<FilterValue> performanceAlertTypes = new ArrayList<FilterValue>();

        for (Entry<String, Long> entry : PerformanceRuleManager.PERF_ALERT_TYPE_TO_ID.entrySet()) {
            FilterValue sjb = new FilterValue();
            sjb.setId(entry.getValue());
            sjb.setName(TranslatorWorker.translateText(
                    PerformanceRuleManager.PERF_ALERT_TYPE_TO_DESCRIPTION.get(entry.getKey())));
            performanceAlertTypes.add(sjb);
        }

        performanceAlertTypes = orderByProperty(performanceAlertTypes, NAME_PROPERTY);
        pt.setFilterId(FiltersConstants.PERFORMANCE_ALERT_TYPE);
        pt.setName(TranslatorWorker.translateText(ColumnConstants.PERFORMANCE_ALERT_TYPE));
        pt.setValues(performanceAlertTypes);

        return pt;
    }

    @GET
    @Path("/financingInstruments/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.FINANCING_INSTRUMENT, columns = ColumnConstants.FINANCING_INSTRUMENT,
            name = ColumnConstants.FINANCING_INSTRUMENT, tab = EPConstants.TAB_FINANCIALS)
    @ApiOperation("Return financing instruments")
    public FilterDescriptor getFinancingInstruments() {
        return getCategoryValue(CategoryConstants.FINANCING_INSTRUMENT_KEY, ColumnConstants.FINANCING_INSTRUMENT);
    }

    private List<FilterValue> getCategoryValue(String categoryKey) {
        List<FilterValue> fi = new ArrayList<FilterValue>();

        Collection<AmpCategoryValue> col = CategoryManagerUtil
                .getAmpCategoryValueCollectionByKey(categoryKey,true);
        for (AmpCategoryValue ampCategoryValue : col) {
            if (!Boolean.TRUE.equals(ampCategoryValue.getDeleted())) {
                String translatedValue = CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue);
                fi.add(new FilterValue(ampCategoryValue.getIdentifier(), translatedValue));
            }
        }
        //reorder because after we get the translated name we lose ordering
        fi = orderByProperty (fi,NAME_PROPERTY);
        fi.add(new FilterValue(ColumnReportData.UNALLOCATED_ID, FiltersConstants.UNDEFINED_NAME));
        return fi;
        
    }

    @GET
    @Path("/locations/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.LOCATION, columns = ColumnConstants.LOCATION, name="Locations",
            tab=EPConstants.TAB_LOCATIONS)
    @ApiOperation("Return locations")
    public Location getLocations() {
        return QueryUtil.getLocationsForFilter();
    }
    
    /**
     * List the locations tree
     *
     * @return tree definitions (filter types) and the tree structure of the locations
     */
    @GET
    @Path("/locationlist")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = "LocationList", name = "LocationList", tab = EPConstants.TAB_LOCATIONS)
    public FilterList getLocationsList() {
        return FiltersManager.getInstance().getLocationFilterList();
    }
    
    @GET
    @Path("/humanitarianAid/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.HUMANITARIAN_AID, columns = ColumnConstants.HUMANITARIAN_AID,
            name=ColumnConstants.HUMANITARIAN_AID, tab=EPConstants.TAB_FINANCIALS)
    public FilterDescriptor getHumanitarianAid() {
        return buildYesNoJsonBean(ColumnConstants.HUMANITARIAN_AID);
    }
    
    @GET
    @Path("/disasterResponse/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.DISASTER_RESPONSE_MARKER, columns = ColumnConstants.DISASTER_RESPONSE_MARKER,
            name=ColumnConstants.DISASTER_RESPONSE_MARKER, tab=EPConstants.TAB_FINANCIALS)
    public FilterDescriptor getDisasterResponse() {
        return buildYesNoJsonBean(ColumnConstants.DISASTER_RESPONSE_MARKER);
    }
    
    protected FilterDescriptor buildYesNoJsonBean(String columnName) {
        FilterDescriptor res = new FilterDescriptor();
        res.setFilterId(FilterUtils.INSTANCE.idFromColumnName(columnName));
        res.setName(columnName);
        res.setValues(
                Arrays.asList(
                    new FilterValue(1, "Yes", null, TranslatorWorker.translateText("Yes")),
                    new FilterValue(2, "No", null, TranslatorWorker.translateText("No"))
                ));
        return res;
    }

    /**
     * used to return AmpCategoryClass values wrapped to be provided to the filter widget
     *
     * @param categoryKey
     * @param filterId
     * @return
     */
    private FilterDescriptor getCategoryValue(String categoryKey, String columnName) {
        FilterDescriptor js = new FilterDescriptor();
        js.setFilterId(FilterUtils.INSTANCE.idFromColumnName(columnName));
        js.setName(TranslatorWorker.translateText(columnName));
        js.setValues(getCategoryValue(categoryKey));
        return js;
    }
    
    public static FilterValue buildProgramsJsonBean(AmpThemeSkeleton loc, String programName, int level) {
        FilterValue res = new FilterValue();
        res.setId(loc.getId());
        res.setName(loc.getName());     
        res.setFilterId(FilterUtils.INSTANCE.idFromColumnName(programName + " Level " + level));
        ArrayList<FilterValue> children = new ArrayList<FilterValue>();
        for(AmpThemeSkeleton child:loc.getChildLocations())
            children.add(buildProgramsJsonBean(child, programName, level + 1));
        res.setChildren(children);
        return res;
    }
    
    /**
     * Get Sectors from AmpSector
     * 
     * @param as
     * @param sectorConfigName 
     * @return
     */

    private FilterValue getSectors(AmpSector as, String sectorConfigName, Integer level) {
        FilterValue s = new FilterValue();
        s.setId(as.getAmpSectorId());
        s.setCode(as.getSectorCodeOfficial());
        s.setName(as.getName());
        s.setChildren(new ArrayList<>());
        String columnName = AmpClassificationConfiguration.NAME_TO_COLUMN_AND_LEVEL.get(sectorConfigName).get(level);
        s.setFilterId(FilterUtils.INSTANCE.idFromColumnName(columnName));
        level++;
        for (AmpSector ampSectorChild : as.getSectors()) {
            s.getChildren().add(getSectors(ampSectorChild, sectorConfigName, level));
        }
        orderByProperty(s.getChildren(), NAME_PROPERTY);
        return s;
    }
    

    @GET
    @Path("/workspaces")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Workspaces", id = FiltersConstants.TEAM, columns = ColumnConstants.TEAM,
                visibilityCheck = "hasToShowWorkspaceFilter", tab = EPConstants.TAB_OTHER)
    @ApiOperation("Return all workspaces to be used for filtering")
    public FilterDescriptor getWorkspaces() {
        List<FilterValue> teamsListJson = new ArrayList<FilterValue>();
        if (hasToShowWorkspaceFilter()) {

            AmpTeam ws = getAmpTeam();

            Map<Long, String> teamNames = null;

            if (ws != null && ws.getComputation() != null && ws.getComputation()) {
                Set<AmpTeam> workspaces = WorkspaceFilter.getComputedRelatedWorkspaces();
                if (workspaces != null) {
                    teamNames = new HashMap<Long, String>();
                    for (AmpTeam team : workspaces) {
                        teamNames.put(team.getAmpTeamId(), team.getName());
                    }
                }
            } else {
                // display only child workspaces in case of computed workspaces
                if (ws != null && Constants.ACCESS_TYPE_MNGMT.equals(ws.getAccessType())) {
                    teamNames = DatabaseViewFetcher
                            .fetchInternationalizedView("amp_team", PARENT_WS_CONDITION + ws.getAmpTeamId(), "amp_team_id", "name");
                } else {
                    teamNames = DatabaseViewFetcher
                            .fetchInternationalizedView("amp_team", PRIVATE_WS_CONDITION, "amp_team_id", "name");
                }
            }

            if (teamNames != null) {
                for (long ampTeamId : teamNames.keySet()) {
                    FilterValue ampTeamJson = new FilterValue();
                    ampTeamJson.setId(ampTeamId);
                    ampTeamJson.setName(teamNames.get(ampTeamId));
                    teamsListJson.add(ampTeamJson);
                }
            }

            teamsListJson = orderByProperty(teamsListJson, NAME_PROPERTY);
        }
        FilterDescriptor js = new FilterDescriptor();
        js.setFilterId(FiltersConstants.TEAM);
        js.setName(TranslatorWorker.translateText("Workspaces"));
        js.setValues(teamsListJson);
        
        return js;
    }

    public boolean hasToShowWorkspaceFilter () {
        boolean showWorkspaceFilter = true;
        boolean showWorkspaceFilterInTeamWorkspace = "true".equalsIgnoreCase(
                FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SHOW_WORKSPACE_FILTER_IN_TEAM_WORKSPACES));
        AmpTeam ampTeam = getAmpTeam();

        //Hide Workspace in public view
        if (ampTeam == null) {
            showWorkspaceFilter = false;
        } else {
            boolean isComputation = ampTeam.getComputation() != null && ampTeam.getComputation();

            // showWorkspaceFilterInTeamWorkspace matters for computation workspace
            if (ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_TEAM) && isComputation && !showWorkspaceFilterInTeamWorkspace) {
                showWorkspaceFilter = false;
            }

            // showWorkspaceFilterInTeamWorkspace matters for management workspace
            if (ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_MNGMT) && !showWorkspaceFilterInTeamWorkspace) {
                showWorkspaceFilter = false;
            }

            // if it's regular team, non computation workspace
            if (ampTeam.getAccessType().equals(Constants.ACCESS_TYPE_TEAM) && !isComputation) {
                showWorkspaceFilter = false;
            }
        }
        return showWorkspaceFilter;

    }

    /**
     * Orders a List<T> by name.
     *
     * @param list, List<T> to be ordered
     * @param nameFn function to retrieve the name of an item
     */
    private static <T> void orderByName(List<T> list, Function<T, String> nameFn) {
        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T a, T b) {
                return nameFn.apply(a).trim().compareToIgnoreCase(nameFn.apply(b).trim());
            }
        });
    }

    /**
     * Orders a List <FilterValue> based on the property desired.
     * It can order using any attributes of FilterValue like: id, code, name, displayName
     * 
     * @param list the list to be ordered
     * @param property, String with the attribute to be ordered
     * @return ordered List <FilterValue>
     */
    private static List<FilterValue> orderByProperty(List<FilterValue> list, final String property) {
        Collections.sort(list, new Comparator<FilterValue>() {
            @Override
            public int compare(FilterValue a, FilterValue b) {
                try {
                    String property1 = (String) FilterValue.class.getMethod("get" + property).invoke(a);
                    String property2 = (String) FilterValue.class.getMethod("get" + property).invoke(b);
                    property1 = property1.trim();
                    property2 = property2.trim();
                    return property1.compareToIgnoreCase(property2);

                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                    logger.warn("Couldn't order the JSON objects based on property " + property);
                    return 0;
                }

            }
        });
        return list;
        
    }
    
    @GET
    @Path("/computed-year")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.COMPUTED_YEAR, name = "Computed Year",
        columns = ColumnConstants.COMPUTED_YEAR, tab = EPConstants.TAB_OTHER)
    public SettingField getComputedYear() {
        return FiltersBuilder.buildComputedYears();
    }

    @GET
    @Path("/effectiveFundingDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.EFFECTIVE_FUNDING_DATE, columns = ColumnConstants.EFFECTIVE_FUNDING_DATE,
            id = FiltersConstants.EFFECTIVE_FUNDING_DATE, tab = EPConstants.TAB_FINANCIALS)
    public void getEffectiveFundingDate() {
    }

    @GET
    @Path("/fundingClosingDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.FUNDING_CLOSING_DATE, columns = ColumnConstants.FUNDING_CLOSING_DATE,
            id = FiltersConstants.FUNDING_CLOSING_DATE, tab = EPConstants.TAB_FINANCIALS)
    public void getFundingClosingDate() {
    }
}
