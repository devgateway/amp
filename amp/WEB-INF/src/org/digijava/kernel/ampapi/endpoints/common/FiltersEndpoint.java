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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersBuilder;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRuleManager;
import org.digijava.kernel.ampapi.endpoints.settings.SettingField;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.FilterType;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
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
public class FiltersEndpoint {
    private static final String DISPLAY_NAME_PROPERTY = "DisplayName";
    private static final String NAME_PROPERTY = "Name";
    private static final String SECTORS_SUFFIX = " Sectors";
    private static final Logger logger = Logger.getLogger(FiltersEndpoint.class);
    
    /** the value to use as a filter value when filtering booleans for ANY DEFINED */
    public static final String ANY_BOOLEAN = "999888777";

    // todo
    // probably not the best place to keep, but definitely better than in the method
    private static final String PRIVATE_WS_CONDITION = "WHERE (isolated is false) OR (isolated is null)";
    private static final String PARENT_WS_CONDITION = "WHERE parent_team_id = ";



    //AmpARFilter filters;
    
    public FiltersEndpoint() {
        //filters = new AmpARFilter();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<AvailableMethod> getAvailableFilters() {
        return EndpointUtils.getAvailableMethods(FiltersEndpoint.class.getName(),true);
    }

    
    /**
     * Return activity status options
     * 
     * @return
     */
    @GET
    @Path("/activityapprovalStatus")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.APPROVAL_STATUS, columns = ColumnConstants.APPROVAL_STATUS,
                name = "Approval Status", visibilityCheck = "hasToShowActivityapprovalStatusFilter", tab=EPConstants.TAB_ACTIVITY)
    public JsonBean getActivityApprovalStatus() {
        JsonBean as=new JsonBean();
        AmpTeam ampTeam = getAmpTeam();
        //Hide in public view
        if (ampTeam!=null){
            List<SimpleJsonBean> activityStatus = new ArrayList<SimpleJsonBean>();
            for (String key : AmpARFilter.activityApprovalStatus.keySet()) {
                SimpleJsonBean sjb = new SimpleJsonBean();
                sjb.setId(AmpARFilter.activityApprovalStatus.get(key));
                sjb.setName(TranslatorWorker.translateText(key));
                activityStatus.add(sjb);
            }
            activityStatus = orderByProperty (activityStatus,NAME_PROPERTY);
            as.set("filterId", FiltersConstants.APPROVAL_STATUS);
            as.set("name", TranslatorWorker.translateText(ColumnConstants.APPROVAL_STATUS));
            as.set("values",activityStatus);
        }
        
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
     * @return
     */
    public boolean hasToShowActivityapprovalStatusFilter() {
        if(TLSUtils.getRequest().getSession().getAttribute(
                org.digijava.module.aim.helper.Constants.CURRENT_MEMBER)==null){
            return false;
        }else{
            return true;
        }

    }
    
    /**
     * Return the adminlevels for filtering
     * 
     * @return
     */
    @GET
    @Path("/boundaries")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false,  id = "Boundaries", tab=EPConstants.TAB_LOCATIONS)
    public List<String> getBoundaries() {
        return QueryUtil.getImplementationLocationsInUse();
    }

    /**
     * Returns the sector schema lists
     * 
     * @return
     */
    @GET
    @Path("/sectors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Sectors", id = "Sectors", tab = EPConstants.TAB_SECTORS)
    public List<SimpleJsonBean> getSectorsSchemas() throws AmpApiException{
        List<SimpleJsonBean> sectorList = new ArrayList<SimpleJsonBean>();
        List<AmpClassificationConfiguration> schems = SectorUtil.getAllClassificationConfigs();
        Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();
        for (AmpClassificationConfiguration ampClassificationConfiguration : schems) {
            final String columnName = AmpClassificationConfiguration.NAME_TO_COLUMN_MAP
                    .get(ampClassificationConfiguration.getName()); 
            if (visibleColumns.contains(columnName)) {
                Long sectorConfigId = ampClassificationConfiguration.getId();
                String sectorDisplayName = TranslatorWorker.translateText(ampClassificationConfiguration.getName() + SECTORS_SUFFIX);
                
                SimpleJsonBean sectorBean = new SimpleJsonBean(sectorConfigId, sectorDisplayName);
                sectorBean.setFilterId(FilterUtils.INSTANCE.idFromColumnName(columnName));
                
                sectorList.add(sectorBean);
            }
        }
        
        return sectorList;
    }

    /**
     * Return the sector filtered by the given sectorName
     * 
     * @return
     */
    @GET
    @Path("/sectors/{sectorId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "SectorsById", tab = EPConstants.TAB_SECTORS)
    public SimpleJsonBean getSectors(@PathParam("sectorId") Long sectorId) {

        SimpleJsonBean sector = new SimpleJsonBean();

        try {
            AmpClassificationConfiguration c = SectorUtil
                    .getClassificationConfigById(sectorId);

            String sectorConfigName = c.getName();
            List<SimpleJsonBean> ampSectorsList = new ArrayList<SimpleJsonBean>();
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
    /**
     * Return the year range configure for GIS
     * @return
     */
    @GET
    @Path("/date/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Date", id = "date", tab = EPConstants.TAB_OTHER)
    public JsonBean getDates(){
        JsonBean date = new JsonBean();
        date.set("startYear", 1985);
        date.set("endYear", 2025);
        return date;
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
    public JsonBean getProposedStartDate(){
        return new JsonBean();
    }
    
    @GET
    @Path("/actualStartDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ACTUAL_START_DATE, columns = ColumnConstants.ACTUAL_START_DATE,
            id = FiltersConstants.ACTUAL_START_DATE, tab = EPConstants.TAB_OTHER)
    public JsonBean getActualStartDate(){
        return new JsonBean();
    }
    
    @GET
    @Path("/proposedCompletionDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.PROPOSED_COMPLETION_DATE, columns = ColumnConstants.PROPOSED_COMPLETION_DATE,
            id = FiltersConstants.PROPOSED_COMPLETION_DATE, tab = EPConstants.TAB_OTHER)
    public JsonBean getProposedCompletionDate(){
        return new JsonBean();
    }
    
    @GET
    @Path("/actualCompletionDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ACTUAL_COMPLETION_DATE, columns = ColumnConstants.ACTUAL_COMPLETION_DATE,
            id = FiltersConstants.ACTUAL_COMPLETION_DATE, tab = EPConstants.TAB_OTHER)
    public JsonBean getActualCompletionDate(){
        return new JsonBean();
    }

    @GET
    @Path("/finalDateContracting/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.FINAL_DATE_FOR_CONTRACTING, columns = ColumnConstants.FINAL_DATE_FOR_CONTRACTING,
            id = FiltersConstants.FINAL_DATE_FOR_CONTRACTING, tab = EPConstants.TAB_OTHER)
    public JsonBean getDateForContracting() {
        return new JsonBean();
    }

    @GET
    @Path("/issueDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ISSUE_DATE, columns = ColumnConstants.ISSUE_DATE,
            id = FiltersConstants.ISSUE_DATE, tab = EPConstants.TAB_OTHER)
    public JsonBean getIssueDate() {
        return new JsonBean();
    }
    
    @GET
    @Path("/proposedApprovalDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.PROPOSED_APPROVAL_DATE, columns = ColumnConstants.PROPOSED_APPROVAL_DATE,
            id = FiltersConstants.PROPOSED_APPROVAL_DATE, tab = EPConstants.TAB_OTHER)
    public JsonBean getProposedApprovalDate() {
        return new JsonBean();
    }   

    @GET
    @Path("/actualApprovalDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.ACTUAL_APPROVAL_DATE, columns = ColumnConstants.ACTUAL_APPROVAL_DATE,
            id = FiltersConstants.ACTUAL_APPROVAL_DATE, tab = EPConstants.TAB_OTHER)
    public JsonBean getActualApprovalDate() {
        return new JsonBean();
    }   
        
    /**
     * Return the programs filtered by the given sectorName
     * 
     * @return
     */
    @GET
    @Path("/programs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Programs", id = "Programs", 
    columns={ColumnConstants.PRIMARY_PROGRAM, ColumnConstants.SECONDARY_PROGRAM, 
            ColumnConstants.NATIONAL_PLANNING_OBJECTIVES, ColumnConstants.TERTIARY_PROGRAM},
            tab = EPConstants.TAB_PROGRAMS)
    public List<SimpleJsonBean> getPrograms() {
        List<SimpleJsonBean> programs = new ArrayList<SimpleJsonBean>();
        try {
            Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();

            List<Object[]> progs = PersistenceManager.getSession().createSQLQuery("SELECT amp_program_settings_id, name FROM amp_program_settings").list();
            for (Object[] program : progs) {
                String programName = String.valueOf(program[1]);
                final String columnName = ProgramUtil.NAME_TO_COLUMN_MAP.get(String.valueOf(program[1]));
                // only add if its enabled
                if (visibleColumns.contains(columnName)) {
                    SimpleJsonBean bean = new SimpleJsonBean(PersistenceManager.getLong(program[0]), TranslatorWorker.translateText(programName));
                    bean.setFilterId(FilterUtils.INSTANCE.idFromColumnName(columnName));
                    programs.add(bean);
                }
            }
            return programs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Return org types with its orgs groups
     * 
     * @return
     *  [
     *      {
     *          "id": 39,
     *          "groupIds": [43,67,46,33,49,66,21,64,41,63],
     *          "name": "Multilateral"
     *      },
     *  ..
     *  ]
     */
    @GET
    @Path("/org-types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Types of Organizations", id = "organizationTypesList")
    
    public List<JsonBean> getOrgTypes() {
        List <JsonBean> orgTypes = QueryUtil.getOrgTypes();
        return orderByName(orgTypes);
    }

    /**
     * Return org groups with its orgs ids
     * 
     * @return
     *  [
     *      {
     *      id: 0,
     *      name: "some org group",
     *      typeId: :id,                  //id of parent type
     *      orgIds: [:id, :id, :id]    //ids of child orgs
     *      },
     *  ....
     *  ]
     */
    @GET
    @Path("/org-groups")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Organization Groups", id = "orgGroupsList")
    
    public List<JsonBean> getOrgGroups() {
        List <JsonBean> orgGroups = orderByName(QueryUtil.getOrgGroups());
        return orgGroups;
        
    }   

    /**
     *  List all available orgs

     * @return
     *  [
     *      {
     *      id: 0,
     *      name: "some org",
     *      groupId: :id,                  //id of parent group
     *      roleIds: [:id, :id, :id]    //ids of all roles this org appears in ie. [0,2]
     *      },
     *  ...
     *  ]
     */
    @GET
    @Path("/orgs")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = "Organizations", name = "orgsList", tab=EPConstants.TAB_ORGANIZATIONS)
    
    public List<JsonBean> getOrgs() { 
        List <JsonBean> orgs = QueryUtil.getOrgs();
        return orderByName(orgs);
    }   

    /**
     * List all available orgs roles
     * 
     * @return
     *  [
     *      {
     *      id: 0,
     *      name: "Donor"
     *      },
     *  ...
     *  ]
     */ 

    @GET
    @Path("/org-roles")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Organization Roles", id = "orgRolesList", tab=EPConstants.TAB_ALL_AGENCIES)
    
    public List<SimpleJsonBean> getorgRoles() {
        List <SimpleJsonBean> orgRoles = QueryUtil.getOrgRoles();
        return orderByProperty(orgRoles,DISPLAY_NAME_PROPERTY);
    }   
    

    /**
     * Return the programs filtered by the given programSettingsId
     * 
     * @return
     */
    @GET
    @Path("/programs/{programId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "ProgramsByProgramName", 
    columns={ColumnConstants.PRIMARY_PROGRAM, ColumnConstants.SECONDARY_PROGRAM, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES, ColumnConstants.TERTIARY_PROGRAM}, 
    tab=EPConstants.TAB_PROGRAMS)
    public SimpleJsonBean getPrograms(@PathParam("programId") Long programId) {
        try {
            Object[] idname = (Object[]) PersistenceManager.getSession().createSQLQuery("select default_hierarchy, name from amp_program_settings where amp_program_settings_id = " + programId).uniqueResult();
            Long rootAmpThemeId = idname == null ? null : PersistenceManager.getLong(idname[0]);
            if (rootAmpThemeId != null) {
                String schemeName = String.valueOf(idname[1]);
                Map<Long, AmpThemeSkeleton> themes = AmpThemeSkeleton.populateThemesTree(rootAmpThemeId);
                String programName = schemeName.equals(ProgramUtil.NATIONAL_PLAN_OBJECTIVE) ? ColumnConstants.NATIONAL_PLANNING_OBJECTIVES : schemeName;
                SimpleJsonBean bean = buildProgramsJsonBean(themes.get(rootAmpThemeId), programName, 0);
                bean.setFilterId(FilterUtils.INSTANCE.idFromColumnName(programName));
                return bean;
            } else {
                return new SimpleJsonBean();
            }

        } catch (ObjectNotFoundException e) {
            return new SimpleJsonBean();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Return type of assistance 
     * 
     * @return
     */
    @GET
    @Path("/typeOfAssistance/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.TYPE_OF_ASSISTANCE, columns = ColumnConstants.TYPE_OF_ASSISTANCE,
            name="Type of Assistance", tab=EPConstants.TAB_FINANCIALS)
    public JsonBean getTypeOfAssistance() {
        return getCategoryValue(CategoryConstants.TYPE_OF_ASSISTENCE_KEY,ColumnConstants.TYPE_OF_ASSISTANCE);
    }
    
    /**
     * Return mode of payment 
     * 
     * @return
     */
    @GET
    @Path("/modeOfPayment/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.MODE_OF_PAYMENT, columns = ColumnConstants.MODE_OF_PAYMENT,
            name="Mode of Payment", tab=EPConstants.TAB_FINANCIALS)
    public JsonBean getModeOfPayment() {
        return getCategoryValue(CategoryConstants.MODE_OF_PAYMENT_KEY,ColumnConstants.MODE_OF_PAYMENT);
    }
    /**
     * Return Activitystatus 
     * 
     * @return
     */
    @GET
    @Path("/activityStatus/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.STATUS, columns = ColumnConstants.STATUS,name="Activity Status",
            tab=EPConstants.TAB_ACTIVITY)
    public JsonBean getActivityStatus() {
        return getCategoryValue(CategoryConstants.ACTIVITY_STATUS_KEY,
                ColumnConstants.STATUS);
    }

    /**
     * Return Activity Budget
     * 
     * @return
     */
    @GET
    @Path("/activityBudget/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.ON_OFF_TREASURY_BUDGET, columns = ColumnConstants.ON_OFF_TREASURY_BUDGET,
            name="Activity Budget", tab=EPConstants.TAB_FINANCIALS)
    public JsonBean getActivityBudget() {
        return getCategoryValue(CategoryConstants.ACTIVITY_BUDGET_KEY, ColumnConstants.ON_OFF_TREASURY_BUDGET);
    }   
    
    /**
     * Funding status filter information 
     * 
     * @return
     */
    @GET
    @Path("/fundingStatus/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.FUNDING_STATUS, columns = ColumnConstants.FUNDING_STATUS,
            name="Funding Status",tab=EPConstants.TAB_FINANCIALS)
    public JsonBean getFundingStatus() {
        return getCategoryValue(CategoryConstants.FUNDING_STATUS_KEY,ColumnConstants.FUNDING_STATUS);
    }
    
    /**
     * Funding status filter information 
     * 
     * @return
     */
    @GET
    @Path("/expenditureClass/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.EXPENDITURE_CLASS, columns = ColumnConstants.EXPENDITURE_CLASS,
            name="Expenditure Class", tab=EPConstants.TAB_FINANCIALS)
    public JsonBean getExpenditureClass() {
        return getCategoryValue(CategoryConstants.EXPENDITURE_CLASS_KEY, ColumnConstants.EXPENDITURE_CLASS);
    }
    
    /**
     * Funding concessionality level information
     * 
     * @return
     */
    @GET
    @Path("/concessionalityLevel/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.CONCESSIONALITY_LEVEL, columns = ColumnConstants.CONCESSIONALITY_LEVEL,
            name="Concessionality Level", tab=EPConstants.TAB_FINANCIALS)
    public JsonBean getConcessionalityLevel() {
        return getCategoryValue(CategoryConstants.CONCESSIONALITY_LEVEL_KEY, ColumnConstants.CONCESSIONALITY_LEVEL);
    }

    /**
     * Performance Alert Level filter
     *
     * @return
     */
    @GET
    @Path("/performanceAlertLevel")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.PERFORMANCE_ALERT_LEVEL,
            columns = ColumnConstants.PERFORMANCE_ALERT_LEVEL, name = ColumnConstants.PERFORMANCE_ALERT_LEVEL,
            tab = EPConstants.TAB_ACTIVITY)
    public JsonBean getPerformanceAlertLevel() {
        return getCategoryValue(CategoryConstants.PERFORMANCE_ALERT_LEVEL_KEY, ColumnConstants.PERFORMANCE_ALERT_LEVEL);
    }
    
    /**
     * Performance Alert Type filter
     *
     * @return
     */
    @GET
    @Path("/performanceAlertType")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.PERFORMANCE_ALERT_TYPE,
            columns = ColumnConstants.PERFORMANCE_ALERT_TYPE, name = ColumnConstants.PERFORMANCE_ALERT_TYPE,
            tab = EPConstants.TAB_ACTIVITY)
    public JsonBean getPerformanceAlertType() {
        JsonBean pt = new JsonBean();
        
        List<SimpleJsonBean> performanceAlertTypes = new ArrayList<SimpleJsonBean>();
        
        for (Entry<String, Long> entry : PerformanceRuleManager.PERF_ALERT_TYPE_TO_ID.entrySet()) {
            SimpleJsonBean sjb = new SimpleJsonBean();
            sjb.setId(entry.getValue());
            sjb.setName(TranslatorWorker.translateText(
                    PerformanceRuleManager.PERF_ALERT_TYPE_TO_DESCRIPTION.get(entry.getKey())));
            performanceAlertTypes.add(sjb);
        }
        
        performanceAlertTypes = orderByProperty(performanceAlertTypes, NAME_PROPERTY);
        pt.set("filterId", FiltersConstants.PERFORMANCE_ALERT_TYPE);
        pt.set("name", TranslatorWorker.translateText(ColumnConstants.PERFORMANCE_ALERT_TYPE));
        pt.set("values", performanceAlertTypes);
        
        return pt;
    }

    /**
     * Return financing instruments 
     * 
     * @return
     */
    @GET
    @Path("/financingInstruments/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.FINANCING_INSTRUMENT, columns = ColumnConstants.FINANCING_INSTRUMENT,
            name="Financing Instruments", tab=EPConstants.TAB_FINANCIALS)
    public JsonBean getFinancingInstruments() {
        return getCategoryValue(CategoryConstants.FINANCING_INSTRUMENT_KEY, ColumnConstants.FINANCING_INSTRUMENT);
    }

    private List<SimpleJsonBean> getCategoryValue(String categoryKey) {
        List<SimpleJsonBean> fi = new ArrayList<SimpleJsonBean>();

        Collection<AmpCategoryValue> col = CategoryManagerUtil
                .getAmpCategoryValueCollectionByKey(categoryKey,true);
        for (AmpCategoryValue ampCategoryValue : col) {
            if (!Boolean.TRUE.equals(ampCategoryValue.getDeleted())) {
                String translatedValue = CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue);
                fi.add(new SimpleJsonBean(ampCategoryValue.getIdentifier(), translatedValue));
            }
        }
        //reorder because after we get the translated name we lose ordering
        fi = orderByProperty (fi,NAME_PROPERTY);
        return fi;
        
    }
    
    
    /**
     * Return locations
     * 
     * @return
     */
    @GET
    @Path("/locations/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.LOCATION, columns = ColumnConstants.LOCATION, name="Locations",
            tab=EPConstants.TAB_LOCATIONS)
    public JsonBean getLocations() {
        return QueryUtil.getLocationsForFilter();
    }
    
    @GET
    @Path("/humanitarianAid/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.HUMANITARIAN_AID, columns = ColumnConstants.HUMANITARIAN_AID,
            name=ColumnConstants.HUMANITARIAN_AID, tab=EPConstants.TAB_FINANCIALS)
    public JsonBean getHumanitarianAid() {
        return buildYesNoJsonBean(ColumnConstants.HUMANITARIAN_AID);
    }
    
    @GET
    @Path("/disasterResponse/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, id = FiltersConstants.DISASTER_RESPONSE_MARKER, columns = ColumnConstants.DISASTER_RESPONSE_MARKER,
            name=ColumnConstants.DISASTER_RESPONSE_MARKER, tab=EPConstants.TAB_FINANCIALS)
    public JsonBean getDisasterResponse() {
        return buildYesNoJsonBean(ColumnConstants.DISASTER_RESPONSE_MARKER);
    }
    
    protected JsonBean buildYesNoJsonBean(String columnName) {
        JsonBean res = new JsonBean();
        res.set("filterId", FilterUtils.INSTANCE.idFromColumnName(columnName));
        res.set("name", columnName);
        res.set("translatedName", columnName);
        res.set("id", ANY_BOOLEAN);
        res.set("values", 
                Arrays.asList(
                    new SimpleJsonBean(1, "Yes", null, TranslatorWorker.translateText("Yes")),
                    new SimpleJsonBean(2, "No", null, TranslatorWorker.translateText("No"))
                ));
        return res;
    }
    
/**
 * used to return AmpCategoryClass values wrapped to be provided to the filter widget
 * @param categoryKey
 * @param filterId
 * @return
 */
    private JsonBean getCategoryValue(String categoryKey, String columnName) {
        JsonBean js=new JsonBean();
        js.set("filterId", FilterUtils.INSTANCE.idFromColumnName(columnName));
        js.set("name", TranslatorWorker.translateText(columnName));
        js.set("values",getCategoryValue(categoryKey));
        return js;
        
    }
    
    public static SimpleJsonBean buildProgramsJsonBean(AmpThemeSkeleton loc, String programName, int level) {
        SimpleJsonBean res = new SimpleJsonBean();
        res.setId(loc.getId());
        res.setName(loc.getName());     
        res.setFilterId(FilterUtils.INSTANCE.idFromColumnName(programName + " Level " + level));
        ArrayList<SimpleJsonBean> children = new ArrayList<SimpleJsonBean>();
        for(AmpThemeSkeleton child:loc.getChildLocations())
            children.add(buildProgramsJsonBean(child, programName, level + 1));
        res.setChildren(children);
        return res;
    }
    
    /**
     * Get JsonEnable object for programs
     * 
     * @param t
     *            AmpThem to get the programFrom
     * @return
     */
    private SimpleJsonBean getPrograms(AmpTheme t,String programName,Integer level) {
        SimpleJsonBean p = new SimpleJsonBean();
        p.setId(t.getAmpThemeId());
        p.setName(t.getName());
        p.setChildren(new ArrayList<SimpleJsonBean>());
        String columnName=null;
        if(level>0){
            if(programName.equals(ProgramUtil.NATIONAL_PLAN_OBJECTIVE)){
                columnName=ColumnConstants.NATIONAL_PLANNING_OBJECTIVES +" Level " +level;
            }else{
                if(programName.equals(ProgramUtil.PRIMARY_PROGRAM)){
                    columnName=ColumnConstants.PRIMARY_PROGRAM +" Level " +level;
                }else{
                    if(programName.equals(ProgramUtil.SECONDARY_PROGRAM)){
                        columnName=ColumnConstants.SECONDARY_PROGRAM +" Level " +level;
                    }else{
                        if(programName.equals(ProgramUtil.TERTIARY_PROGRAM)){
                            columnName=ColumnConstants.TERTIARY_PROGRAM +" Level " +level;
                        }
                    }
                }
            }
            p.setFilterId(columnName);
        }
        level++;
        for (AmpTheme tt : t.getSiblings()) {
            p.getChildren().add(getPrograms(tt,programName,level));
        }
        orderByProperty(p.getChildren(),NAME_PROPERTY);
        return p;
    }

    /**
     * Get Sectors from AmpSector
     * 
     * @param as
     * @param sectorConfigName 
     * @return
     */

    private SimpleJsonBean getSectors(AmpSector as, String sectorConfigName, Integer level) {
        SimpleJsonBean s = new SimpleJsonBean();
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
    
    
    /**
     * Return all workspaces to be used for filtering
     * 
     * @return
     */
    @GET
    @Path("/workspaces")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = "Workspaces", id = FiltersConstants.TEAM, columns = ColumnConstants.TEAM,
                visibilityCheck = "hasToShowWorkspaceFilter", tab = EPConstants.TAB_OTHER)
    public JsonBean getWorkspaces() {
        List<SimpleJsonBean> teamsListJson = new ArrayList<SimpleJsonBean>();
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
                    SimpleJsonBean ampTeamJson = new SimpleJsonBean();
                    ampTeamJson.setId(ampTeamId);
                    ampTeamJson.setName(teamNames.get(ampTeamId));
                    teamsListJson.add(ampTeamJson);
                }
            }

            teamsListJson = orderByProperty(teamsListJson, NAME_PROPERTY);
        }
        JsonBean js = new JsonBean();
        js.set("filterId", FiltersConstants.TEAM);
        js.set("name", TranslatorWorker.translateText("Workspaces"));
        js.set("values", teamsListJson);
        
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
     * Orders a List <JsonBean> by name
     * 
     * @param fi, List <JsonBean> to be ordered
     * @return ordered List 
     */
    private static List <JsonBean> orderByName(List <JsonBean> fi) {
        Collections.sort(fi, new Comparator<JsonBean>() {
            @Override
            public int compare(JsonBean a, JsonBean b) {
                    String prop1 = (String) a.get("name");
                    String prop2 = (String) b.get("name");
                    prop1 = prop1.trim();
                    prop2 = prop2.trim();
                    return prop1.compareToIgnoreCase(prop2);
                }
        });
        return fi;

    }

    /**
     * Orders a List <SimpleJsonBean> based on the property desired.
     * It can order using any attributes of SimpleJsonBean like: id, code, name, displayName
     * 
     * @param list the list to be ordered
     * @param property, String with the attribute to be ordered
     * @return ordered List <SimpleJsonBean>
     */
    private static List<SimpleJsonBean> orderByProperty(List<SimpleJsonBean> list, final String property) {
        Collections.sort(list, new Comparator<SimpleJsonBean>() {
            @Override
            public int compare(SimpleJsonBean a, SimpleJsonBean b) {
                try {
                    String property1 = (String) SimpleJsonBean.class.getMethod("get" + property).invoke(a);
                    String property2 = (String) SimpleJsonBean.class.getMethod("get" + property).invoke(b);
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
    public JsonBean getEffectiveFundingDate(){
        return new JsonBean();
    }

    @GET
    @Path("/fundingClosingDate/")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = true, name = ColumnConstants.FUNDING_CLOSING_DATE, columns = ColumnConstants.FUNDING_CLOSING_DATE,
            id = FiltersConstants.FUNDING_CLOSING_DATE, tab = EPConstants.TAB_FINANCIALS)
    public JsonBean getFundingClosingDate(){
        return new JsonBean();
    }
}
