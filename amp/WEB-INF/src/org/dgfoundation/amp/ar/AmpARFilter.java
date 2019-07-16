/**
 * AmpARFilter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.dgfoundation.amp.PropertyListable;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.annotations.reports.IgnorePersistence;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Constants.GlobalSettings;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.AmpARFilterHelper;
import org.digijava.module.aim.logic.Logic;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;


/**
 * Filtering bean. Holds info about filtering parameters and creates the
 * filtering query
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundatiTeam member rights follow
 *         the activity roles as we have defined them.on.org
 * @since Aug 5, 2006
 * 
 */

public class AmpARFilter extends PropertyListable {
    
    public final static int FILTER_SECTION_FILTERS = 1;
    public final static int FILTER_SECTION_SETTINGS = 2;
    public final static int FILTER_SECTION_ALL = FILTER_SECTION_FILTERS | FILTER_SECTION_SETTINGS;
    
    public final static String DYNAMIC_FILTER_YEAR = "year";
    public final static String DYNAMIC_FILTER_MONTH = "month";
    public final static String DYNAMIC_FILTER_DAY = "day";
    public final static String DYNAMIC_FILTER_ADD_OP = "+";
    public final static String DYNAMIC_FILTER_SUBTRACT_OP = "-";
    
    /**
     * see {@link #selectedActivityPledgesSettings}
     */
    public final static int SELECTED_ACTIVITY_PLEDGES_SETTINGS_WITH_PLEDGES_ONLY = 1;
    public final static int SELECTED_ACTIVITY_PLEDGES_SETTINGS_WITHOUT_PLEDGES_ONLY = 2;
    
    public final static String SDF_OUT_FORMAT_STRING = "yyyy-MM-dd";
    public final static String SDF_IN_FORMAT_STRING = "dd/MM/yyyy";
    
    public static final Set<String> DATE_PROPERTIES = new HashSet<>(Arrays.asList("fromDate", "toDate", 
            "fromActivityActualCompletionDate", "toActivityActualCompletionDate",
            "fromActivityFinalContractingDate", "toActivityFinalContractingDate",
            "fromActivityStartDate", "toActivityStartDate",
            "fromProposedStartDate", "toProposedStartDate",
            "fromEffectiveFundingDate", "toEffectiveFundingDate",
            "fromFundingClosingDate", "toFundingClosingDate",
            "fromProposedApprovalDate", "toProposedApprovalDate"));
    
    public final static Long TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES = -997L;
    
    /**
     * holding my nose while writing this. This id should behave like "a pledge report without any filters whatsoever"
     */
    public final static long DUMMY_SUPPLEMENTARY_PLEDGE_FETCHING_REPORT_ID = -996L;
    
    public static final Set<String> SETTINGS_PROPERTIES = new HashSet<>(Arrays.asList("amountinthousand", "calendarType", 
            "customusegroupings", "decimalseparator", "groupingsize", "maximumFractionDigits", "renderEndYear", 
            "renderStartYear", "sortByAsc", "sortBy"));
    
    public final static Map<String, Integer> activityApprovalStatus = Collections.unmodifiableMap(new HashMap<String, Integer>(){{
        this.put("Existing Unvalidated", 0);
        this.put("New Draft", 1);
        this.put("New Unvalidated", 2);
        this.put("Existing Draft", 3);
        this.put("Validated Activities", 4);
    }});
    
    
    // change this together with v_mondrian_activity_fixed_texts.xml
    public final static Map<String, Integer> activityStatusToNr = Collections.unmodifiableMap(new HashMap<String, Integer>(){{
        this.put(Constants.APPROVED_STATUS, 1);
        this.put(Constants.EDITED_STATUS, 2);
        this.put(Constants.STARTED_APPROVED_STATUS, 3);
        this.put(Constants.STARTED_STATUS, 4);
        this.put(Constants.NOT_APPRVED, 5);
        this.put(Constants.REJECTED_STATUS, 6);     
    }});
    
    /**
     * list of all legal values of AmpActivity::"approvalStatus". DO NOT CHANGE, make a different set with a subset of these if you need the subset only
     */
    public final static Set<String> activityStatus = activityStatusToNr.keySet();
    
    public final static Set<String> validatedActivityStatus = Collections.unmodifiableSet(new HashSet<String>() {{
                                                        this.add(Constants.APPROVED_STATUS);
                                                        this.add(Constants.STARTED_APPROVED_STATUS);
        }});

    public final static Set<String> unvalidatedActivityStatus = Collections.unmodifiableSet(new HashSet<String>() {{
        this.add(Constants.STARTED_STATUS);
        this.add(Constants.EDITED_STATUS);
        this.add(Constants.REJECTED_STATUS);
    }});


    /**
     * Date string formatted for SQL queries
     * field not static because SimpleDateFormat is not thread-safe
     */
    private final SimpleDateFormat sdfOut = new SimpleDateFormat(SDF_OUT_FORMAT_STRING);
    
    /**
     * Date string formatted for database serialization
     * field not static because SimpleDateFormat is not thread-safe
     */
    private final SimpleDateFormat sdfIn = new SimpleDateFormat(SDF_IN_FORMAT_STRING);
    
    /**
     * this is true iff this filter has been touched by a "change filters" functionality
     */
    private boolean settingsHaveBeenAppliedFlag = false;
    
    protected static Logger logger = Logger.getLogger(AmpARFilter.class);
    
    private Long id;
    private boolean justSearch;
    private boolean workspaceonly;

    private Long ampReportId;
    private Set<AmpCategoryValue> statuses = null;
    private Set<AmpTeam> workspaces = null;
    // private Set donors=null; //not used anymore
    @PropertyListableIgnore
    private Set<AmpSector> sectors = null;
    @PropertyListableIgnore
    private Set<AmpSector> sectorsAndAncestors  = null;
    private Set<AmpSector> selectedSectors = null;
    
    private Long teamMemberId;
    
    private String CRISNumber;
    private String budgetNumber;
    private Boolean showArchived;
    
    @PropertyListableIgnore
    private Integer computedYear;
    
    @PropertyListableIgnore
    private Integer actualAppYear;
    
    @PropertyListableIgnore
    private ArrayList<FilterParam> indexedParams=null;
    
    /**
     * whether this filter should queryAppend a WorkspaceFilter query in generateFilterQuery. Ignored when building a workspace filter (always ON)
     */
    private boolean needsTeamFilter;
    
    @PropertyListableIgnore
    private Set<AmpSector> secondarySectors = null;
    private Set<AmpSector> selectedSecondarySectors = null;
    @PropertyListableIgnore
    private Set<AmpSector> secondarySectorsAndAncestors = null;
    @PropertyListableIgnore
    private Set<AmpSector> tertiarySectors = null;
    private Set<AmpSector> selectedTertiarySectors = null;
    @PropertyListableIgnore
    private Set<AmpSector> tertiarySectorsAndAncestors = null;
    @PropertyListableIgnore
    private Set<AmpSector> quaternarySectors = null;
    private Set<AmpSector> selectedQuaternarySectors = null;
    @PropertyListableIgnore
    private Set<AmpSector> quaternarySectorsAndAncestors = null;
    @PropertyListableIgnore
    private Set<AmpSector> quinarySectors = null;
    private Set<AmpSector> selectedQuinarySectors = null;
    @PropertyListableIgnore
    private Set<AmpSector> quinarySectorsAndAncestors = null;
    
    @PropertyListableIgnore
    private Set<AmpSector> tagSectors = null;
    private Set<AmpSector> selectedTagSectors = null;
    @PropertyListableIgnore
    private Set<AmpSector> tagSectorsAndAncestors = null;


    @PropertyListableIgnore
    private Set<AmpTheme> relatedSecondaryProgs;

    @PropertyListableIgnore
    private List<AmpTheme> nationalPlanningObjectives;
    private Set<AmpTheme> selectedNatPlanObj;
    @PropertyListableIgnore
    private Set<AmpTheme> relatedNatPlanObjs;

    @PropertyListableIgnore
    private String teamAccessType;
    
    @PropertyListableIgnore
    private List<AmpTheme> primaryPrograms;
    private Set<AmpTheme> selectedPrimaryPrograms;
    @PropertyListableIgnore
    private Set<AmpTheme> relatedPrimaryProgs;

    @PropertyListableIgnore
    private List<AmpTheme> secondaryPrograms;
    private Set<AmpTheme> selectedSecondaryPrograms;
    
    /**
     * only valid after the query has been generated
     */
    @PropertyListableIgnore
    private boolean pledgeFilter;
    
    /**
     * see getter for description
     */
    @PropertyListableIgnore
    private boolean dateFilterUsed;
    
    /**
     * the toString() of the filter filled with default values (a poor man's "default value") - a hack to know when to display the "filters have been applied" warning on reports / tabs"
     */
    @PropertyListableIgnore
    private String defaultValues;
    
    private String multiDonor = null;

    public String getMultiDonor() {
        return multiDonor;
    }

    public void setMultiDonor(String multiDonor) {
        this.multiDonor = multiDonor;
    }

    @PropertyListableIgnore
    public List<AmpTheme> getNationalPlanningObjectives() {
        return nationalPlanningObjectives;
    }

    public void setNationalPlanningObjectives(List<AmpTheme> nationalPlanningObjectives) {
        this.nationalPlanningObjectives = nationalPlanningObjectives;
    }   


    @PropertyListableIgnore
    public Set<AmpTheme> getRelatedNatPlanObjs() {
        return relatedNatPlanObjs;
    }

    public void setRelatedNatPlanObjs(Set<AmpTheme> relatedNatPlanObjs) {
        this.relatedNatPlanObjs = relatedNatPlanObjs;
    }
    @PropertyListableIgnore
    public Set<AmpTheme> getRelatedSecondaryProgs() {
        return relatedSecondaryProgs;
    }

    public void setRelatedSecondaryProgs(Set<AmpTheme> relatedSecondaryProgs) {
        this.relatedSecondaryProgs = relatedSecondaryProgs;
    }

    @PropertyListableIgnore
    public Set<AmpTheme> getRelatedPrimaryProgs() {
        return relatedPrimaryProgs;
    }

    public void setRelatedPrimaryProgs(Set<AmpTheme> relatedPrimaryProgs) {
        this.relatedPrimaryProgs = relatedPrimaryProgs;
    }

    @PropertyListableIgnore
    public List<AmpTheme> getPrimaryPrograms() {
        return primaryPrograms;
    }

    public void setPrimaryPrograms(List<AmpTheme> primaryPrograms) {
        this.primaryPrograms = primaryPrograms;
    }

    @PropertyListableIgnore
    public List<AmpTheme> getSecondaryPrograms() {
        return secondaryPrograms;
    }

    public void setSecondaryPrograms(List<AmpTheme> secondaryPrograms) {
        this.secondaryPrograms = secondaryPrograms;
    }

    public Set<AmpTheme> getSelectedNatPlanObj() {
        return selectedNatPlanObj;
    }

    public void setSelectedNatPlanObj(Set<AmpTheme> selectedNatPlanObj) {
        this.selectedNatPlanObj = selectedNatPlanObj;
    }

    public Set<AmpTheme> getSelectedPrimaryPrograms() {
        return selectedPrimaryPrograms;
    }

    public void setSelectedPrimaryPrograms(Set<AmpTheme> selectedPrimaryPrograms) {
        this.selectedPrimaryPrograms = selectedPrimaryPrograms;
    }

    public Set<AmpTheme> getSelectedSecondaryPrograms() {
        return selectedSecondaryPrograms;
    }

    public void setSelectedSecondaryPrograms(Set<AmpTheme> selectedSecondaryPrograms) {
        this.selectedSecondaryPrograms = selectedSecondaryPrograms;
    }

    private Set regions = null;
    private Set<AmpIndicatorRiskRatings> risks = null;
    private Set<AmpOrgType> donorTypes = null;
    private Set<AmpOrgGroup> donorGroups = null;
    private Set<AmpOrgGroup> contractingAgencyGroups = null;
    
    private Set<AmpOrganisation> responsibleorg = null;
    private Set<AmpOrganisation> componentFunding = null;
    private Set<AmpOrganisation> componentSecondResponsible = null;
    private Set<AmpOrganisation> executingAgency;
    private Set<AmpOrganisation> contractingAgency;
    private Set<AmpOrganisation> implementingAgency;
    private Set<AmpOrganisation> beneficiaryAgency;
    private Set<AmpOrganisation> donnorgAgency;

    private Set<AmpCategoryValue> financingInstruments = null;
    private Set<AmpCategoryValue> aidModalities = null;
    private Set<AmpCategoryValue> projectCategory = null;

    private Set<AmpCategoryValue> fundingStatus = null;
    
    private Set<AmpCategoryValue> typeOfAssistance = null;
    private Set<AmpCategoryValue> modeOfPayment = null;
    private Set<AmpCategoryValue> activityPledgesTitle = null;
    private Set<AmpCategoryValue> concessionalityLevel = null;

    private Set<AmpCategoryValue> expenditureClass = null;

    private Set<AmpCategoryValue> performanceAlertLevel = null;
    
    private Set<String> performanceAlertType = null;

    // private Long ampModalityId=null;

    private AmpCurrency currency = null;
    
    /**
     *  FIELD NOT USED, but cannot delete it because of serialized instances
     */   
    private Set ampTeamsforpledges = null;
    
    private AmpFiscalCalendar calendarType = null;
    private boolean widget = false;
    private boolean publicView = false;
    private Set<AmpCategoryValue> budget = null;
    private Collection<Integer> lineMinRank;
    
    /**
     * if set is null - "all", else the elements in the set mean
     * 1 - yes, 2 - no
     */
    private Set<Integer> humanitarianAid;
    
    /**
     * if set is null - "all", else the elements in the set mean
     * 1 - yes, 2 - no
     */
    private Set<Integer> disasterResponse;
    
    /**
     * the date is stored in the {@link #sdfIn} hardcoded format
     */
    private String fromDate;
    
    /**
     * the date is stored in the {@link #sdfIn} hardcoded format
     */
    private String toDate;

    private String dynDateFilterCurrentPeriod;
    private Integer dynDateFilterAmount;
    private String dynDateFilterOperator;
    private String dynDateFilterXPeriod;
    
    private String fromActivityStartDate; // view: v_actual_start_date, column name: Actual Start Date
    private String toActivityStartDate;
    private String dynActivityStartFilterCurrentPeriod;
    private Integer dynActivityStartFilterAmount;
    private String dynActivityStartFilterOperator;
    private String dynActivityStartFilterXPeriod;

    private String fromIssueDate;
    private String toIssueDate;
    private String dynIssueFilterCurrentPeriod;
    private Integer dynIssueFilterAmount;
    private String dynIssueFilterOperator;
    private String dynIssueFilterXPeriod;

    private String fromActivityActualCompletionDate;
    private String toActivityActualCompletionDate;
    private String dynActivityActualCompletionFilterCurrentPeriod;
    private Integer dynActivityActualCompletionFilterAmount;
    private String dynActivityActualCompletionFilterOperator;
    private String dynActivityActualCompletionFilterXPeriod;
    
    private String fromActivityFinalContractingDate; // view: v_contracting_date, column name: Final Date for Contracting
    private String toActivityFinalContractingDate;  // view: v_contracting_date, column name: Final Date for Contracting
    private String dynActivityFinalContractingFilterCurrentPeriod;
    private Integer dynActivityFinalContractingFilterAmount;
    private String dynActivityFinalContractingFilterOperator;
    private String dynActivityFinalContractingFilterXPeriod;
    

    private String fromActualApprovalDate;
    private String toActualApprovalDate;
    private String dynActualApprovalFilterCurrentPeriod;
    private Integer dynActualApprovalFilterAmount;
    private String dynActualApprovalFilterOperator;
    private String dynActualApprovalFilterXPeriod;
    private String fromProposedApprovalDate;    // view: v_actual_proposed_date, column name: [Proposed Approval Date], translated in Nepal as [Date of Agreement]
    private String toProposedApprovalDate;      // view: v_actual_proposed_date, column name: [Proposed Approval Date], translated in Nepal as [Date of Agreement]
    private String fromProposedStartDate;
    private String toProposedStartDate;
    private String dynProposedApprovalFilterCurrentPeriod;
    private Integer dynProposedApprovalFilterAmount;
    private String dynProposedApprovalFilterOperator;
    private String dynProposedApprovalFilterXPeriod;

    private String fromEffectiveFundingDate;
    private String toEffectiveFundingDate;
    private String dynEffectiveFundingFilterCurrentPeriod;
    private Integer dynEffectiveFundingFilterAmount;
    private String dynEffectiveFundingFilterOperator;
    private String dynEffectiveFundingFilterXPeriod;

    private String fromFundingClosingDate;
    private String toFundingClosingDate;
    private String dynFundingClosingFilterCurrentPeriod;
    private Integer dynFundingClosingFilterAmount;
    private String dynFundingClosingFilterOperator;
    private String dynFundingClosingFilterXPeriod;

    /**
     * whether to only show activities linked/not linked to pledges
     */
    private Integer selectedActivityPledgesSettings = -1;

    private Integer fromMonth;
    private Integer yearFrom;
    private Integer toMonth;
    private Integer yearTo;
    private Collection<AmpCategoryValueLocations> locationSelected;
    
    @PropertyListableIgnore
    private Collection<AmpCategoryValueLocations> relatedLocations;
    
    private Collection<AmpCategoryValueLocations> pledgesLocations;
    private Boolean unallocatedLocation = null;
    //private AmpCategoryValueLocations regionSelected = null;
    private Collection<String> approvalStatusSelected;
    
    // these fields moved to WorkspaceFilter in AMP 2.3.7
    //private boolean approved = false;
    //private boolean draft = false;

    private Integer renderStartYear = null; // the range of dates columns that
                                            // has to be render, years not in
                                            // range will be computables for
                                            // totals but wont be renderable
    private Integer renderEndYear = null;

    private DecimalFormat currentFormat = null;
    
    public final static int AMOUNT_OPTION_IN_UNITS = 0;
    public final static int AMOUNT_OPTION_IN_THOUSANDS = 1;
    public final static int AMOUNT_OPTION_IN_MILLIONS = 2;
    public final static int AMOUNT_OPTION_IN_BILLIONS = 3;
    
    private Integer amountinthousand;
    
    /**
     * DEPRECATED, TO BE REMOVED IN NEXT BRANCH
     * @deprecated
     */
    private Boolean amountinmillion;
    
    private String decimalseparator; //always of length 1: cannot switch to Character now, because it would be incompatible with all the serialized filters around the world
    private String groupingseparator; //always of length 1: cannot switch to Character now, because it would be incompatible with all the serialized filters around the world
    private Integer groupingsize;
    private Boolean customusegroupings;
    private Integer maximumFractionDigits;

    private TeamMember teamMember;
    
    
    /**
     * @return the maximumFractionDigits
     */
    public Integer getMaximumFractionDigits() {
        return maximumFractionDigits;
    }

    /**
     * @param maximumFractionDigits the maximumFractionDigits to set
     */
    public void setMaximumFractionDigits(Integer maximumFractionDigits) {
        this.maximumFractionDigits = maximumFractionDigits;
    }

    /**
     * returns null or 1-long String
     * @return
     */
    public String getDecimalseparator() {
        return decimalseparator;
    }

    /**
     * only push 1-long Strings here!
     * @param decimalseparator
     */
    public void setDecimalseparator(String decimalseparator) {
        if (decimalseparator != null && decimalseparator.length() != 1)
        {
            new RuntimeException("invalid decimalseparator value: " + decimalseparator).printStackTrace();          
        }
        this.decimalseparator = decimalseparator;
    }

    /**
     * returns null or 1-long String
     * @return
     */
    public String getGroupingseparator() {
        return groupingseparator;
    }

    /**
     * only push 1-long Strings here!
     * @param decimalseparator
     */
    public void setGroupingseparator(String groupingseparator) {
        if (groupingseparator != null && groupingseparator.length() != 1)
        {
            new RuntimeException("invalid groupingseparator value: " + groupingseparator).printStackTrace();            
        }
        this.groupingseparator = groupingseparator;
    }
    
    /**
     * DO NOT USE - TO BE PHYSICALLY REMOVED IN 2.4
     * @deprecated
     * @return
     */
    public final Boolean getAmountinmillion() {
        return amountinmillion;
    }

    /**
     * DO NOT USE - TO BE PHYSICALLY REMOVED IN 2.4
     * @deprecated
     * @return
     */
    public final void setAmountinmillion(Boolean amountinmillion) {
        this.amountinmillion = amountinmillion;
    }

    private Boolean governmentApprovalProcedures;
    private Boolean jointCriteria;
    private String accessType=null;
    
    
    private String pageSize; // to be used for exporting reports

    private String text;
    private String indexText;
    private String searchMode;
    private static final String initialPledgeFilterQueryWithUnrelatedFunding = "SELECT -1 AS pledge_id UNION SELECT distinct(id) as pledge_id FROM amp_funding_pledges WHERE 1=1 ";
    private static final String initialPledgeFilterQueryWithoutUnrelatedFunding = "SELECT distinct(id) as pledge_id FROM amp_funding_pledges WHERE 1=1 ";
    
    /*Maybe not the best place to add this condition but we need to ensure we 
      Never ever show activities which are not assigned to a workspace that is why this is added  "amp_team_id IS NOT NULL" */
    
    private static final String initialFilterQuery = "SELECT distinct(amp_activity_id) FROM amp_activity WHERE 1=1 AND amp_team_id IS NOT NULL";
    private String generatedFilterQuery;
    private int initialQueryLength = initialFilterQuery.length();
    
    private String sortBy;
    private Boolean sortByAsc;
    private List<String> hierarchySorters;
    private List<Long> ampActivityIdOrder;

    private Set<AmpCategoryValue> projectImplementingUnits; 
    
    /**
     * why doesn't it have a setter like all normal fields do?
     */
    private boolean budgetExport;
    
    private void queryAppend(String filter) {
        if (filter != null && !filter.isEmpty())
            generatedFilterQuery += " AND amp_activity_id IN (" + filter + ")";
    }

    private void queryNotAppend(String filter) {
        if (filter != null && !filter.isEmpty())
            generatedFilterQuery += " AND amp_activity_id NOT IN (" + filter + ")";
    }

    private void pledgeQueryAppend(String filter) {
        if (filter != null && !filter.isEmpty())
            generatedFilterQuery += " AND id IN (" + filter + ")";
    }
    
    /**
     * fills the "grouping" subpart of the settings part of filters with defaults
     * @return
     */
    private void fillWithDefaultGroupingSettings()
    {
        DecimalFormat usedDecimalFormat = FormatHelper.getDecimalFormat();
        
        Character customDecimalSymbol   = usedDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
        Integer customDecimalPlaces = usedDecimalFormat.getMaximumFractionDigits();
        Character customGroupSeparator  = usedDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator();
        Boolean customUseGrouping   = usedDecimalFormat.isGroupingUsed();
        Integer customGroupSize     = usedDecimalFormat.getGroupingSize();
        this.setGroupingseparator(customGroupSeparator.toString());
        this.setGroupingsize(customGroupSize);
        this.setCustomusegroupings(customUseGrouping);
        this.setMaximumFractionDigits(customDecimalPlaces);
        this.setDecimalseparator(customDecimalSymbol.toString());
        
        buildCustomFormat();
    }
    
    /**
     * gets the calendar to be used when a running a report which has no per-report calendar settings
     * @return
     */
    public static AmpFiscalCalendar getWorkspaceCalendar() {
        
        AmpApplicationSettings settings = getEffectiveSettings();
        AmpFiscalCalendar res = null;
        
        if (settings != null)
            res = settings.getFiscalCalendar();
        
        if (res == null) { // still no calendar source -> use Global Setting
            String gvalue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
            if (gvalue != null) {
                Long fiscalCalId = Long.parseLong(gvalue);
                res = DbUtil.getAmpFiscalCalendar(fiscalCalId);
            }
        }
        return res;
    }

    /**
     * fills the "Report / Tab Settings" part of the instance with the default values
     */
    public void fillWithDefaultsSettings()
    {
        fillWithDefaultGroupingSettings();
        this.setAmountinthousand(Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)));
        
        setCalendarType(getWorkspaceCalendar());
        AmpApplicationSettings settings = getEffectiveSettings();
        if (settings != null) {
            this.setCurrency(settings.getCurrency());
        } else {
            this.setCurrency(CurrencyUtil.getBaseCurrency());
        }       
        initRenderStartEndYears(settings);
    }
    
    /**
     * computes the set of all the selected ACVL ids, including children and ascendants
     * @return
     */
    public Set<Long> buildAllRelatedLocationsIds() {
        if (locationSelected == null)
            return null;
        
        Set<Long> allDescendantsIds = DynLocationManagerUtil.populateWithDescendantsIds(locationSelected, false);
        List<AmpCategoryValueLocations> allAscendingLocations   = new ArrayList<AmpCategoryValueLocations>();
        DynLocationManagerUtil.populateWithAscendants(allAscendingLocations, locationSelected);
        Set<Long> allSelectedLocations = new HashSet<Long>(allDescendantsIds);
        
        for(AmpCategoryValueLocations ascendant:allAscendingLocations)
            allSelectedLocations.add(ascendant.getId());
        
        return allSelectedLocations;
    }
    
    public List<AmpCategoryValueLocations> buildAllRelatedLocations() {
        return DynLocationManagerUtil.loadLocations(buildAllRelatedLocationsIds());
    }
    
    /**
     * computes the current user's effective AmpApplicationSettings, searching through the hierarchy
     * returns null if there is no current user
     * @return
     */
    public static AmpApplicationSettings getEffectiveSettings()
    {
        HttpServletRequest request = TLSUtils.getRequest();
        if (request == null)
            return null;
        
        TeamMember tm = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
        if (tm == null)
            return null;
        
        if (AmpCaching.getInstance().applicationSettingsRetrieved)
            return AmpCaching.getInstance().applicationSettings;
        
        return getEffectiveSettings(tm);
    }
    
    /**
     * computes a TeamMember's effective AmpApplicationSettings, searching through the hierarchy
     * returns null of nothing could be found OR if the teammember is null
     * @param tm
     * @return
     */
    public static AmpApplicationSettings getEffectiveSettings(TeamMember tm)
    {
        if (tm == null)
            return null;

        AmpApplicationSettings settings = null;

        if (tm.getTeamId() != null)
            settings = DbUtil.getTeamAppSettings(tm.getTeamId()); // use workspace settings
        
        try
        {
            AmpCaching.getInstance().applicationSettingsRetrieved = true;
            AmpCaching.getInstance().applicationSettings = settings;
        }
        catch(Exception e)
        {
            // AmpCaching does not work out of the Struts request cycle
        }
        return settings;
    }
    
    public int getAmountDivider() {
        return AmountsUnits.getAmountDivider(computeEffectiveAmountInThousand());
    }
    
    /**
     * returns an AmountsUnits instance corresponding to the instance's {@link #amountinthousand} value OR null
     * @return
     */
    public AmountsUnits getUnitsOptions() {
        if (amountinthousand == null) return null;
        return AmountsUnits.getForValue(amountinthousand);
    }
    
    public void initFilterQueryPledge() {
        String showUnlinkedFunging  = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.UNLINKED_FUNDING_IN_PLEDGES_REPORTS);
        if ( "true".equals(showUnlinkedFunging ) )
            this.generatedFilterQuery = initialPledgeFilterQueryWithUnrelatedFunding;
        else
            this.generatedFilterQuery = initialPledgeFilterQueryWithoutUnrelatedFunding;
    }
    
    public void initFilterQuery()
    {
        if (this.pledgeFilter)
            initFilterQueryPledge();
        else
            this.generatedFilterQuery = initialFilterQuery;
    }
    
    /**
     * fills the AmpARFilter instance with defaults taken from all the appropiate sources: hardcoded defaults, AmpGlobalSettings, Workspace settings
     * only "filter" settings altered, the "settings" one are left untouched
     */
    public void fillWithDefaultsFilter(Long ampReportId)
    {
        this.setShowArchived(false);
        this.setYearFrom(null);
        this.setYearTo(null);
        this.setFromMonth(null);
        this.setToMonth(null);
        this.setFromDate(null);
        this.setToDate(null);
        this.setLineMinRank(null);
        this.setText(null);
        this.setPageSize(null);
        this.setGovernmentApprovalProcedures(null);
        this.setJointCriteria(null);
        this.setJustSearch(false);
        this.setWorkspaceonly(false);
        this.setLocationSelected(null);
        this.setRelatedLocations(null);
        this.setApprovalStatusSelected(null);
        this.setProjectImplementingUnits(null);
        this.setSortByAsc(true);
        this.setHierarchySorters(new ArrayList<String>());
        this.setAmpActivityIdOrder(new ArrayList<Long>());
        this.budgetExport = false;
        
        HttpServletRequest request = TLSUtils.getRequest();
        this.setAmpReportId(ampReportId);
        
        AmpReports ampReport = ampReportId == null ? null : DbUtil.getAmpReport(ampReportId);
        if (ampReport != null)
        {
            this.budgetExport   = ampReport.getBudgetExporter()==null ? false:ampReport.getBudgetExporter();                
            if (ampReport.getType() == ArConstants.PLEDGES_TYPE){
                    this.pledgeFilter = true;
            }
        }
        this.pledgeFilter |= (this.ampReportId != null && this.ampReportId.longValue() == DUMMY_SUPPLEMENTARY_PLEDGE_FETCHING_REPORT_ID);
        
        this.initFilterQuery();

        getEffectiveSettings(); // do not remove call - also writes into the caches

        if (teamMember == null && request != null) {
            teamMember = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
        }

        if (teamMember != null && teamMember.getTeamId() == null) {
            teamMember = null;
        }

        if (teamMember != null) {
            this.setNeedsTeamFilter(false);
            this.setAccessType(teamMember.getTeamAccessType());
            teamMemberId = teamMember.getMemberId();
        }
        else {
            // public view
            this.setNeedsTeamFilter(true);
            this.setAccessType("Management"); // should always be Management, as a report can be made public only from management workspace

            //Check if the reportid is not nut for public mondrian reports
            if (ampReport != null)
            {
                if (ampReport != null && ampReport.getWorkspaceLinked() && ampReport.getOwnerId() != null)
                {                   
                    teamMemberId = ampReport.getOwnerId().getAmpTeamMemId();
                } else
                {
                    // not workspace linked or no report (??)
                    teamMemberId = TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES;
                }
            }
        }
        
        //FormatHelper.tlocal.set(null); // somewhat ugly hack, as this shouldn't belong in here
    }
    
    private int getCalendarYear(AmpApplicationSettings settings, Integer settingsYear, String globalSettingsKey)
    {
        Long defaultCalendarId;
        
        if (settings != null){
            if (settings.getFiscalCalendar() != null){
                defaultCalendarId = settings.getFiscalCalendar().getAmpFiscalCalId();
            }else{
                defaultCalendarId = Long.parseLong(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));   
            }
        }   

        Integer result = null;
        // Check if there is value on workspace or member setting
        if (settingsYear != null && settingsYear.intValue() != 0) {
            result = settingsYear;
        } else 
        {
            // global setting
            String gvalue = FeaturesUtil.getGlobalSettingValue(globalSettingsKey);
            if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
                result = Integer.parseInt(gvalue);
            }
        }
        
        //TODO-CONSTANTIN: strange conversion between calendars
        if (result == null)
            return -1;
        
        result = FiscalCalendarUtil.getYearOnCalendar(calendarType.getAmpFiscalCalId(), result, settings);
        //TODO:Constantin: used the ReportsFilterPickerForm-ported between-calendars conversion utilities (migrated by me to FiscalCalendarUtils) as the code looks more maintained. In case it is faulty, we could try our luck with the commented code below
        
//      if (result != null && result > 0 && calendarType != null && calendarType.getAmpFiscalCalId().equals(defaultCalendarId) ){
//          ICalendarWorker worker = calendarType.getworker();
//          try {
//              Date checkDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + result);
//              worker.setTime(checkDate);
//              result = worker.getYear();
//          } catch (Exception e) {
//              e.printStackTrace();
//          }
//      }
        
        return result == null ? -1 : result;
    }
    
    /**
     * sets renderStartYear and renderEndYear according to Global, Workspace and User settings, translated by calendar
     * @param settings
     */
    private void initRenderStartEndYears(AmpApplicationSettings settings)
    {
        ///Set the range depending of workspace setup / global setting and selected calendar
        Integer renderStartSettingsYear = getDefaultYear(settings, calendarType, true);
        Integer renderEndSettingsYear = getDefaultYear(settings, calendarType, false);
        
        setRenderStartYear(renderStartSettingsYear);
        setRenderEndYear(renderEndSettingsYear);    
    }
    
    public static Integer getDefaultStartYear() {
        return getDefaultStartYear(getDefaultCalendar());
    }
    
    public static Integer getDefaultEndYear() {
        return getDefaultEndYear(getDefaultCalendar());
    }
    
    public static Integer getDefaultStartYear(AmpFiscalCalendar current) {
        return getDefaultYear(getEffectiveSettings(), current, true);
    }
    
    public static Integer getDefaultEndYear(AmpFiscalCalendar current) {
        return getDefaultYear(getEffectiveSettings(), current, false);
    }
    
    public static Integer getDefaultYear(AmpApplicationSettings settings, AmpFiscalCalendar current,
            boolean startYear) {
        
        // 1st default priority are Workspace Settings
        Integer renderSettingsYear = (settings == null) ? null : getValidYear(
                startYear ? settings.getReportStartYear() : settings.getReportEndYear());
        
        // this is the calendar the settings are taken from, 
        // that we need to keep track for any year conversions to the actually used calendar
        AmpFiscalCalendar sourceCalendar = renderSettingsYear == null ? null : settings.getFiscalCalendar();
        
        // 2nd default priority are Global Settings
        if (renderSettingsYear == null) {
            renderSettingsYear = FeaturesUtil.getGlobalSettingValueInteger(startYear ?
                    GlobalSettings.START_YEAR_DEFAULT_VALUE : GlobalSettings.END_YEAR_DEFAULT_VALUE);
            Long gsCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
            sourceCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(gsCalendarId);
        }
        
        // now convert the years to the actual calendar if needed
        if (current != null && !current.getAmpFiscalCalId().equals(sourceCalendar.getAmpFiscalCalId())) {
            int yearDelta = startYear ? 0 : 1;
            int daysDelta = startYear ? 0 : -1;
            renderSettingsYear = FiscalCalendarUtil
                    .getActualYear(sourceCalendar, renderSettingsYear + yearDelta, daysDelta, current);
        }
        
        return renderSettingsYear;
    }
    
    private static Integer getValidYear(Integer year) {
        // TODO: we may also have to check if it is in the allowed year range (is there any?)
        if (year == null || year == 0)
            return null;
        return year;
    }
    
    /**
     * tries to guess an ampReportId from the request or session data
     */
    private Long getAttachedAmpReportId(HttpServletRequest request)
    {
        String ampReportId = null ;
        //Check if the reportid is not nut for public mondrian reports
        if (request.getParameter("ampReportId") != null && request.getParameter("ampReportId").length() > 0)
            ampReportId = request.getParameter("ampReportId");

        if (ampReportId == null && ReportContextData.contextIdExists())
        {
            AmpReports ar = ReportContextData.getFromRequest().getReportMeta();
            if (ar != null){
                ampReportId = ar.getAmpReportId().toString();
            }
        }       
        Long ampReportIdLong = ampReportId == null ? null : Long.parseLong(ampReportId);
        return ampReportIdLong;
    }
    
    /**
     * initializes the given part(s) of the filter
     * @param request
     * @param subsection: one of the FILTER_SECTION_ZZZZ constants
     * @param ampReportId: a forced ampReportId or null if you want one to be deducted automatically (usually works)
     */
    public void readRequestData(HttpServletRequest request, int subsection, Long forcedAmpReportId) 
    {
        if ((subsection & FILTER_SECTION_SETTINGS) > 0)
            fillWithDefaultsSettings();

        if ((subsection & FILTER_SECTION_FILTERS) > 0)
        {
            Long ampReportIdLong = forcedAmpReportId == null ? getAttachedAmpReportId(request) : forcedAmpReportId;
            fillWithDefaultsFilter(ampReportIdLong);
        }
        initWidget();
    }

    public void initWidget()
    {
        HttpServletRequest request = TLSUtils.getRequest();
        if (request == null)
            return;
        String widget = (String) request.getAttribute("widget");
        if (widget != null)
            this.setWidget(new Boolean(widget));
    }
    
    /**
     * returns true iff the filter has been changed compared to its "default" value. The default value is the one which the filter had when {@link #rememberDefaultValues()} has been called last 
     * @return
     */
    @PropertyListableIgnore
    public boolean getChanged()
    {
        if (defaultValues == null)
            return true;
        return !defaultValues.equals(this.getStringRepresentation());
    }
    
    /**
     * remember, somewhere, the current values in the filter as being "default". See {@link #getChanged()} for uses
     */
    public void rememberDefaultValues()
    {
        this.defaultValues = this.getStringRepresentation();
    }

    /**
     * builds the customFormat field based on the other fields in the instance
     * contains some copy-paste from {@link #fillWithDefaultGroupingSettings()}, but too tired to abstractize somehow away
     */
    public void buildCustomFormat() {
        DecimalFormat custom = buildCustomFormat(this.getDecimalseparator(), this.getGroupingseparator(), 
                this.getMaximumFractionDigits(), this.getCustomusegroupings(), this.getGroupingsize());
        this.setCurrentFormat(custom);
    }
    
    public static DecimalFormat buildCustomFormat(String decimalSeparator, String groupingSeparator, 
            Integer maximumFractionDigits, Boolean customUseGroupings, Integer groupingSize) {
        DecimalFormat usedDecimalFormat = FormatHelper.getDecimalFormat();
        
        Character defaultDecimalSymbol  = usedDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
        Integer defaultDecimalPlaces    = usedDecimalFormat.getMaximumFractionDigits();
        Character defaultGroupSeparator = usedDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator();
        Boolean defaultUseGrouping  = usedDecimalFormat.isGroupingUsed();
        Integer defaultGroupSize        = usedDecimalFormat.getGroupingSize();
        
        DecimalFormat custom = new DecimalFormat();
        DecimalFormatSymbols ds = new DecimalFormatSymbols();
        if (decimalSeparator != null){
            ds.setDecimalSeparator(decimalSeparator.charAt(0));
        }else{
            ds.setDecimalSeparator(defaultDecimalSymbol);
        }
        
        if (groupingSeparator != null){
            ds.setGroupingSeparator(groupingSeparator.charAt(0));
        }else{
            ds.setGroupingSeparator(defaultGroupSeparator);
        }
        
        if (maximumFractionDigits != null && maximumFractionDigits > -1)
            custom.setMaximumFractionDigits(maximumFractionDigits);
        else
            custom.setMaximumFractionDigits((defaultDecimalPlaces != -1) ? defaultDecimalPlaces : 99);
        
        custom.setGroupingUsed(customUseGroupings == null ? defaultUseGrouping : customUseGroupings);
        custom.setGroupingSize(groupingSize == null ? defaultGroupSize : groupingSize);
        custom.setDecimalFormatSymbols(ds);
        return custom;
    }
    
    /**
     * postprocesses list after being populated from form
     */
    public void postprocess()
    {
        FilterUtil.postprocessFilterSectors(this);
        FilterUtil.postprocessFilterPrograms(this);
        
        buildCustomFormat();
    }

    public AmpARFilter(TeamMember teamMember) {
        this();
        this.teamMember = teamMember;
    }
    
    public AmpARFilter() {
        super();
        this.generatedFilterQuery = initialFilterQuery;
    }
    
    private String createDateCriteria(String to, String from, String sqlColumn) {
        String dateCriteria     = "";
        try {
            if ( (to != null && to.length() > 0)  ) {
                dateCriteria    = sqlColumn + " <= '" + sdfOut.format(sdfIn.parse(to)) + "'";
            }
            if ( (from != null && from.length() > 0)  ) {
                if ( dateCriteria.length() > 0 ) {
                    dateCriteria += " AND ";
                }
                else
                    dateCriteria    = "";
                
                dateCriteria    += sqlColumn + " >= '" + sdfOut.format(sdfIn.parse(from)) + "'";
            }
        }
        catch (ParseException pe) {
            pe.printStackTrace();
        }
        
        return dateCriteria;
    }

    public boolean wasDateFilterUsed()
    {
        return (
                ((fromDate != null) && (fromDate.length() > 0)) ||
                ((toDate != null) && (toDate.length() > 0))
                );
    }
    
    public void generateFilterQuery(HttpServletRequest request, boolean workspaceFilter) {
        generateFilterQuery(request, workspaceFilter, false);
    }
    
    /**
     * generates SQL subquery which selects activity ids of activities which use one of the sectors of a given sector scheme
     * @param s the set of sectors
     * @param classificationName one of "Primary" / "Secondary" / "Tertiary" / "Tag"
     * @return
     */
    protected static String generateSectorFilterSubquery(Set<AmpSector> s, String classificationName){
        if (s == null || s.isEmpty())
            return null;
        String subquery = "SELECT DISTINCT(aas.amp_activity_id) FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
                + "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
                + "AND c.name='" + classificationName +"' AND aas.amp_sector_id in ("
                + Util.toCSStringForIN(s) + ")";
        return subquery;
    }
    
    /**
     * generates SQL subquery which selects activity ids of activities which use one of the sectors of a given sector scheme
     * @param s the set of sectors
     * @param classificationName one of "Primary" / "Secondary" / "Tertiary" / "Tag"
     * @return
     */
    protected static String generateProgramFilterSubquery(Collection<AmpTheme> s, String classificationName){
        if (s == null || s.isEmpty())
            return null;
        String subquery = "SELECT aap.amp_activity_id FROM amp_activity_program aap inner join  amp_theme p on aap.amp_program_id=p.amp_theme_id "
                + "inner join AMP_PROGRAM_SETTINGS ps on ps.amp_program_settings_id=aap.program_setting where ps.name='" + classificationName + "' AND "
                + " aap.amp_program_id in ("
                + Util.toCSStringForIN(s) + ")";
        return subquery;
    }

    /**
     * generates SQL subquery which selects pledge ids of pledges which use one of the sectors of a given sector scheme
     * @param s the set of sectors
     * @param classificationName one of "Primary" / "Secondary" / "Tertiary" / "Tag"
     * @return
     */
    protected static String generatePledgesSectorFilterSubquery(Set<AmpSector> s, String classificationName){
        if (s == null || s.isEmpty())
            return null;
        String subquery = "SELECT fps.pledge_id FROM amp_funding_pledges_sector fps, amp_sector s, amp_classification_config c "
                + "WHERE fps.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
                + "AND c.name='" + classificationName +"' AND fps.amp_sector_id in ("
                + Util.toCSStringForIN(s) + ")";
        return subquery;
    }

    /**
     * generates SQL subquery which selects activity ids of activities which use one of the sectors of a given sector scheme
     * @param s the set of sectors
     * @param classificationName one of "Primary" / "Secondary" / "Tertiary" / "Tag"
     * @return
     */
    protected static String generatePledgesProgramFilterSubquery(Collection<AmpTheme> s, String classificationName){
        if (s == null || s.isEmpty())
            return null;
        String subquery = "SELECT fpp.pledge_id FROM amp_funding_pledges_program fpp inner join amp_theme p on fpp.amp_program_id=p.amp_theme_id "
                + "inner join AMP_PROGRAM_SETTINGS ps on ps.amp_program_settings_id=getprogramsettingid(fpp.amp_program_id) where ps.name='" + classificationName + "' AND "
                + " fpp.amp_program_id in ("
                + Util.toCSStringForIN(s) + ")";
        return subquery;
    }

    public void generatePledgeFilterQuery()
    {
        this.pledgeFilter = true;
        indexedParams=new ArrayList<FilterParam>();
        
//      String WORKSPACE_ONLY = "";
//      if (this.workspaceonly && "Management".equals(this.getAccessType())){
//          WORKSPACE_ONLY = "SELECT v.pledge_id FROM v_pledges_projects v WHERE v.approval_status IN ("+Util.toCSString(activityStatus)+")";
//          pledgeQueryAppend(WORKSPACE_ONLY);
//      }
        
        String DONNOR_AGENCY_FILTER = " SELECT v.pledge_id FROM v_pledges_donor v  WHERE v.amp_donor_org_id IN ("
            + Util.toCSString(donnorgAgency) + ")";
        
        String DONOR_TYPE_FILTER    = "SELECT v.id FROM v_pledges_donor_type v WHERE org_type_id IN ("
            + Util.toCSString(donorTypes) + ")";

        String DONOR_GROUP_FILTER = "SELECT v.pledge_id FROM v_pledges_donor_group v WHERE org_grp_id IN ("
                + Util.toCSString(donorGroups) + ")";

        String AID_MODALITIES_FILTER = "SELECT v.pledge_id FROM v_pledges_aid_modality v WHERE amp_modality_id IN ("
            + Util.toCSString(aidModalities) + ")";
        
        String TYPE_OF_ASSISTANCE_FILTER = "SELECT v.pledge_id FROM v_pledges_type_of_assistance v WHERE id IN ("
            + Util.toCSString(typeOfAssistance) + ")";
        
        String REGION_SELECTED_FILTER = "";
        if (locationSelected != null) {
            Set<AmpCategoryValueLocations> allSelectedLocations = new HashSet<AmpCategoryValueLocations>();
            allSelectedLocations.addAll(locationSelected);
            
            DynLocationManagerUtil.populateWithDescendants(allSelectedLocations, locationSelected, false);
            this.pledgesLocations = new ArrayList<AmpCategoryValueLocations>();
            this.pledgesLocations.addAll(allSelectedLocations);
            DynLocationManagerUtil.populateWithAscendants(this.pledgesLocations, locationSelected);
            
            this.relatedLocations = allSelectedLocations;
            
            String allSelectedLocationString = Util.toCSString(allSelectedLocations);
            String subSelect = "SELECT aal.pledge_id FROM amp_funding_pledges_location aal, amp_location al " +
                    "WHERE ( aal.location_id=al.location_id AND " +
                    "al.location_id IN (" + allSelectedLocationString + ") )";
            
            if (REGION_SELECTED_FILTER.equals("")) {
                REGION_SELECTED_FILTER  = subSelect;
            } else {
                REGION_SELECTED_FILTER += " OR amp_activity_id IN (" + subSelect + ")"; 
            }           
        }
        
        if (donnorgAgency != null && donnorgAgency.size() > 0){
            pledgeQueryAppend(DONNOR_AGENCY_FILTER);
        }
        
        if (donorGroups != null && donorGroups.size() > 0)
            pledgeQueryAppend(DONOR_GROUP_FILTER);
        
        if (donorTypes != null && donorTypes.size() > 0)
            pledgeQueryAppend(DONOR_TYPE_FILTER);
        
        if (aidModalities != null && aidModalities.size() > 0){
            pledgeQueryAppend(AID_MODALITIES_FILTER);
        }
        if (typeOfAssistance != null && typeOfAssistance.size() > 0){
            pledgeQueryAppend(TYPE_OF_ASSISTANCE_FILTER);
        }
        pledgeQueryAppend(generatePledgesSectorFilterSubquery(sectors, "Primary"));
        pledgeQueryAppend(generatePledgesSectorFilterSubquery(secondarySectors, "Secondary"));
        pledgeQueryAppend(generatePledgesSectorFilterSubquery(tertiarySectors, "Tertiary"));
        pledgeQueryAppend(generatePledgesSectorFilterSubquery(quaternarySectors, "Quaternary"));
        pledgeQueryAppend(generatePledgesSectorFilterSubquery(quinarySectors, "Quinary"));
        pledgeQueryAppend(generatePledgesSectorFilterSubquery(tagSectors, "Tag"));
        
        pledgeQueryAppend(generatePledgesProgramFilterSubquery(nationalPlanningObjectives, "National Plan Objective"));
        pledgeQueryAppend(generatePledgesProgramFilterSubquery(primaryPrograms, "Primary Program"));
        pledgeQueryAppend(generatePledgesProgramFilterSubquery(secondaryPrograms, "Secondary Program"));

        if (!REGION_SELECTED_FILTER.equals("")) {
            pledgeQueryAppend(REGION_SELECTED_FILTER);
        }
    }

    public void generateFilterQuery(HttpServletRequest request, boolean workspaceFilter, boolean skipPledgeCheck) {
        AmpARFilterParams params =  org.dgfoundation.amp.ar.AmpARFilterParams.getParamsFromRequest(request, workspaceFilter, skipPledgeCheck);
        generateFilterQuery(params);
    }
    
    public void generateFilterQuery(AmpARFilterParams params) {
        initFilterQuery(); //reinit filters or else they will grow indefinitely
        if ( !params.getSkipPledgeCheck() &&  !params.getWorkspaceFilter() && ReportContextData.getFromRequest().isPledgeReport()){
            generatePledgeFilterQuery();        
            return;
        }
        
        this.pledgeFilter = false;
        
        TeamMember loggedInTeamMember = params.getMember();     
                        
        indexedParams=new ArrayList<FilterParam>();
        
        String BUDGET_FILTER = "SELECT amp_activity_id FROM v_on_off_budget WHERE budget_id IN ("
            + Util.toCSString(budget) + ")";            

        String STATUS_FILTER = "SELECT amp_activity_id FROM v_status WHERE amp_status_id IN ("
                + Util.toCSString(statuses) + ")";



        Set<AmpTeam> checkedWorkspaces;
        boolean showWorkspaceFilterInTeamWorkspace = "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SHOW_WORKSPACE_FILTER_IN_TEAM_WORKSPACES));
        // https://jira.dgfoundation.org/browse/AMP-15117: ignore the "workspace" filter settings only in case of "team workspace without computation"
        if (loggedInTeamMember != null && Constants.ACCESS_TYPE_TEAM.equals(loggedInTeamMember.getTeamAccessType()) &&
            !isTrue(loggedInTeamMember.getComputation()) && !showWorkspaceFilterInTeamWorkspace) {
            AmpTeam selTeam = TeamUtil.getTeamByName(loggedInTeamMember.getTeamName());
            checkedWorkspaces = new HashSet<AmpTeam>();
            checkedWorkspaces.add(selTeam);
        } else {
            checkedWorkspaces = workspaces;
        }

        String WORKSPACE_FILTER = "select amp_activity_id from amp_activity where amp_team_id IN ("
            + Util.toCSStringForIN(checkedWorkspaces) + ")";

        // String ORG_FILTER = "SELECT amp_activity_id FROM v_donor_groups WHERE
        // amp_org_grp_id IN ("+Util.toCSString(donors,true)+")";
        // String PARENT_SECTOR_FILTER="SELECT amp_activity_id FROM v_sectors
        // WHERE amp_sector_id IN ("+Util.toCSString(sectors,true)+")";
        // String SUB_SECTOR_FILTER="SELECT amp_activity_id FROM v_sub_sectors
        // WHERE amp_sector_id IN ("+Util.toCSString(sectors,true)+")";
        // String SECTOR_FILTER="(("+PARENT_SECTOR_FILTER+") UNION
        // ("+SUB_SECTOR_FILTER+"))";

        String SECTOR_FILTER = generateSectorFilterSubquery(sectors, "Primary");

        // String SECONDARY_PARENT_SECTOR_FILTER=
        // "SELECT amp_activity_id FROM v_secondary_sectors WHERE amp_sector_id
        // IN ("+Util.toCSString(secondarySectors,true)+")";
        // String SECONDARY_SUB_SECTOR_FILTER=
        // "SELECT amp_activity_id FROM v_secondary_sub_sectors WHERE
        // amp_sector_id IN ("+Util.toCSString(secondarySectors,true)+")";
        // String SECONDARY_SECTOR_FILTER="(("+SECONDARY_PARENT_SECTOR_FILTER+")
        // UNION ("+SECONDARY_SUB_SECTOR_FILTER+"))";
        String SECONDARY_SECTOR_FILTER = generateSectorFilterSubquery(secondarySectors, "Secondary");

       String TERTIARY_SECTOR_FILTER = generateSectorFilterSubquery(tertiarySectors, "Tertiary");

       String quinarySectorFilter = generateSectorFilterSubquery(quinarySectors, "Quinary");

       String quaternarySectorFilter = generateSectorFilterSubquery(quaternarySectors, "Quaternary");

       String TAG_SECTOR_FILTER = generateSectorFilterSubquery(tagSectors, "Tag");

        String REGION_FILTER = "SELECT amp_activity_id FROM v_regions WHERE name IN ("
                + Util.toCSStringForIN(regions) + ")";
        String FINANCING_INSTR_FILTER = "SELECT amp_activity_id FROM v_financing_instrument WHERE id IN ("
                + Util.toCSStringForIN(financingInstruments) + ")";
        String AID_MODALITIES_FILTER = "SELECT amp_activity_id FROM v_modalities WHERE level_code IN (" + Util.toCSStringForIN(aidModalities) + ")";
        String LINE_MIN_RANK_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE line_min_rank IN ("
                + Util.toCSStringForIN(lineMinRank) + ")";

        String FUNDING_STATUS_FILTER = "SELECT amp_activity_id FROM v_funding_status WHERE funding_status_code IN ("
                + Util.toCSStringForIN(fundingStatus) + ")";
        
        
        
        String MULTI_DONOR      = "SELECT amp_activity_id FROM v_multi_donor WHERE value = '" + multiDonor + "'";
        
        String REGION_SELECTED_FILTER = "";
        if (unallocatedLocation != null) {
            if (unallocatedLocation == true) {
                REGION_SELECTED_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE amp_activity_id NOT IN(SELECT amp_activity_id FROM amp_activity_location)";
            }
        }
        
        String ACTUAL_APPROVAL_YEAR_FILTER = "";
        if (actualAppYear!=null && actualAppYear!=-1) {
            ACTUAL_APPROVAL_YEAR_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE EXTRACT (YEAR FROM actual_approval_date) = " + actualAppYear + " ";
        }
        
        if (locationSelected != null) {
            long a = System.currentTimeMillis();
            Set<Long> allDescendantsIds = DynLocationManagerUtil.populateWithDescendantsIds(locationSelected, false);
            long b = System.currentTimeMillis();
            logger.error("generating " + allDescendantsIds.size() + " ids took " + (b - a) + " millies");
            
            List<AmpCategoryValueLocations> allAscendingLocations   = new ArrayList<AmpCategoryValueLocations>();
            DynLocationManagerUtil.populateWithAscendants(allAscendingLocations, locationSelected);
            
            Set<Long> allSelectedLocations = new HashSet<Long>(allDescendantsIds);
            for(AmpCategoryValueLocations ascendant:allAscendingLocations)
                allSelectedLocations.add(ascendant.getId());
            
            String allDescendantsIdsString          = Util.toCSStringForIN(allDescendantsIds);
            String subSelect            = "SELECT aal.amp_activity_id FROM amp_activity_location aal, amp_location al " +
                    "WHERE ( aal.amp_location_id=al.amp_location_id AND " +
                    "al.location_id IN (" + allDescendantsIdsString + ") )";
            
            this.relatedLocations = DynLocationManagerUtil.loadLocations(allSelectedLocations);
            
            if (REGION_SELECTED_FILTER.equals("")) {
                REGION_SELECTED_FILTER  = subSelect;
            } else {
                REGION_SELECTED_FILTER += " OR amp_activity_id IN (" + subSelect + ")"; 
            }           
        }
        
        String approvalStatusQuery = "";
        if (approvalStatusSelected != null) { 
            approvalStatusQuery = "select amp_activity_id from amp_activity_version where 1=1 " + buildApprovalStatusQuery(approvalStatusSelected, false); 
        }    
        
        String TYPE_OF_ASSISTANCE_FILTER = "SELECT amp_activity_id FROM v_terms_assist WHERE terms_assist_code IN ("
            + Util.toCSString(typeOfAssistance) + ")";
        
        String EXPENDITURE_CLASS_FILTER = "SELECT amp_activity_id FROM v_expenditure_class WHERE id IN (" + Util.toCSStringForIN(getExpenditureClassForFilters()) + ")";

        String performanceAlertLevelFilter = "SELECT amp_activity_id FROM v_performance_alert_level "
                + "WHERE level_code IN (" + Util.toCSStringForIN(getPerformanceAlertLevelForFilters()) + ")";
        
        String MODE_OF_PAYMENT_FILTER = "SELECT amp_activity_id FROM v_mode_of_payment WHERE mode_of_payment_code IN ("
            + Util.toCSString(modeOfPayment) + ")";
        
        String CONCESSIONALITY_LEVEL_FILTER = "SELECT amp_activity_id FROM v_concessionality_level WHERE id IN ("
                + Util.toCSString(concessionalityLevel) + ")";

        String ACTIVITY_PLEDGES_TITLE = "SELECT amp_activity_id FROM v_activity_pledges_title WHERE title_id IN ("
            + Util.toCSString(activityPledgesTitle) + ")";

        String PROJECT_CATEGORY_FILTER = "SELECT amp_activity_id FROM v_project_category WHERE amp_category_id IN ("
            + Util.toCSString(projectCategory) + ")";

        String PROJECT_IMPL_UNIT_FILTER = "SELECT amp_activity_id FROM v_project_impl_unit WHERE proj_impl_unit_id IN ("
            + Util.toCSString(projectImplementingUnits) + ")";

//      String DONOR_TYPE_FILTER = "SELECT aa.amp_activity_id "
//              + "FROM amp_activity aa, amp_org_role aor, amp_role rol, amp_org_type typ, amp_organisation og  "
//              + "WHERE aa.amp_activity_id = aor.activity AND aor.role = rol.amp_role_id AND rol.role_code='DN' "
//              + "AND typ.amp_org_type_id =  og.org_type_id AND og.amp_org_id = aor.organisation "
//              + "AND typ.amp_org_type_id IN ("
//              + Util.toCSString(donorTypes) + ")";
        
        String DONOR_TYPE_FILTER    = "SELECT amp_activity_id FROM v_donor_type WHERE org_type_id IN ("
            + Util.toCSStringForIN(donorTypes) + ")";

        String DONOR_GROUP_FILTER = "SELECT amp_activity_id FROM v_donor_groups WHERE org_grp_id IN ("
                + Util.toCSStringForIN(donorGroups) + ")";

        String CONTRACTING_AGENCY_GROUP_FILTER = "SELECT amp_activity_id FROM v_contracting_agency_groups WHERE org_grp_id IN ("
                + Util.toCSStringForIN(contractingAgencyGroups) + ")";
                
        String EXECUTING_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_executing_agency v  "
                + "WHERE v.org_id IN ("
                + Util.toCSStringForIN(executingAgency) + ")";
        
        String CONTRACTING_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_contracting_agency v  "
                + "WHERE v.org_id IN ("
                + Util.toCSStringForIN(contractingAgency) + ")";

        
        String BENEFICIARY_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_beneficiary_agency v  "
                + "WHERE v.org_id IN ("
                + Util.toCSStringForIN(beneficiaryAgency) + ")";
        
        String IMPLEMENTING_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_implementing_agency v  "
                + "WHERE v.org_id IN ("
                + Util.toCSStringForIN(implementingAgency) + ")";
        
        String RESPONSIBLE_ORGANIZATION_FILTER = " SELECT v.amp_activity_id FROM v_responsible_organisation v  WHERE v.org_id IN ("
            + Util.toCSStringForIN(responsibleorg) + ")";

        String COMPONENT_FUNDING_ORGANIZATION_FILTER = " SELECT v.amp_activity_id FROM v_component_funding_organization_name v  WHERE v.org_id IN ("
            + Util.toCSStringForIN(componentFunding) + ")";

        String COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION_FILTER = " SELECT v.amp_activity_id FROM v_component_second_responsible_organization_name v  WHERE v.org_id IN ("
            + Util.toCSStringForIN(componentSecondResponsible) + ")";

        String DONNOR_AGENCY_FILTER = " SELECT v.amp_activity_id FROM v_donors v  WHERE v.amp_donor_org_id IN ("
            + Util.toCSStringForIN(donnorgAgency) + ")";
        String ARCHIVED_FILTER          = "";
        if (this.showArchived != null)
            ARCHIVED_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE "
                    + ((this.showArchived) ? "archived=true"
                            : "(archived=false or archived is null)");

        String ACTIVITY_ID_FILTER = ""; 
        if (params.getActivityIdFilter() != null) {
            ACTIVITY_ID_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE amp_activity_id = " + params.getActivityIdFilter();
        }
        
        buildDatesFilterStatements();

        if (fromMonth == null) {
            if (yearFrom != null) {
                AmpARFilterHelper filterHelper = Logic.getInstance()
                        .getAmpARFilterHelper();
                String FROM_FUNDING_YEAR_FILTER = filterHelper
                        .createFromYearQuery(yearFrom);
                queryAppend(FROM_FUNDING_YEAR_FILTER);
            }
        } else {
            AmpARFilterHelper filterHelper = Logic.getInstance()
                    .getAmpARFilterHelper();
            String FROM_FUNDING_YEARMONTH_FILTER = filterHelper
                    .createFromMonthQuery(fromMonth, yearFrom);
            queryAppend(FROM_FUNDING_YEARMONTH_FILTER);
        }

        if (toMonth == null) {
            if (yearTo != null) {
                AmpARFilterHelper filterHelper = Logic.getInstance()
                        .getAmpARFilterHelper();
                String TO_FUNDING_YEAR_FILTER = filterHelper
                        .createToYearQuery(yearTo);
                queryAppend(TO_FUNDING_YEAR_FILTER);
            }
        } else {
            AmpARFilterHelper filterHelper = Logic.getInstance()
                    .getAmpARFilterHelper();
            String TO_FUNDING_YEARMONTH_FILTER = filterHelper
                    .createToMonthQuery(toMonth, yearTo);
            queryAppend(TO_FUNDING_YEARMONTH_FILTER);
        }
        
        if (text != null) {
            if (! "".equals(text.trim())) {
                String TEXT_FILTER = "SELECT a.amp_activity_id from amp_activity a WHERE a.amp_id="
                        + text;
                queryAppend(TEXT_FILTER);
            }
        }

        if (indexText != null && ! "".equals(indexText.trim())) {
            //shouldn't enter here if not using an httprequest!

            String LUCENE_ID_LIST = "";
            searchMode = params.getLuceneSearchModeParam();

            String index = AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH + LuceneUtil.ACTIVITY_INDEX_DIRECTORY;
            Document[] docs = LuceneUtil.search(index, "all", indexText, searchMode);
            logger.info("New lucene search !");

            for (Document doc : docs) {
                if (LUCENE_ID_LIST.equals("")) {
                    LUCENE_ID_LIST = doc.get("id");
                } else {
                    LUCENE_ID_LIST = LUCENE_ID_LIST + "," + doc.get("id");
                }
                ampActivityIdOrder.add(Long.parseLong(doc.get("id")));
            }

            logger.info("Lucene ID List:" + LUCENE_ID_LIST);
            if (LUCENE_ID_LIST.length() < 1) {
                logger.info("Not found!");
                LUCENE_ID_LIST = "-1";
            }
            queryAppend(LUCENE_ID_LIST);
        }

        String RISK_FILTER = "SELECT v.activity_id from AMP_ME_INDICATOR_VALUE v, AMP_INDICATOR_RISK_RATINGS r where v.risk=r.amp_ind_risk_ratings_id and r.amp_ind_risk_ratings_id in ("
                + Util.toCSString(risks) + ")";

        if (budget != null)
            queryAppend(BUDGET_FILTER);

        if (!params.getWorkspaceFilter())
        {
            // not workspace, e.g. normal report/tab filter
            // Merge Filter with the Workspace Filter
//          AmpARFilter teamFilter = (AmpARFilter) request.getSession().getAttribute(ArConstants.TEAM_FILTER);

            if (!this.budgetExport && (params.getTeamFilter()!= null))
            {
                queryAppend(params.getTeamFilter().getGeneratedFilterQuery());
            }
        }
        
        if (statuses != null && statuses.size() > 0)
            queryAppend(STATUS_FILTER);
        
        if (humanitarianAid != null)
            queryAppend(String.format("SELECT v.amp_activity_id FROM v_humanitarian_aid v WHERE val_id IN (%s)", Util.toCSStringForIN(humanitarianAid)));
            
        if (disasterResponse != null)
            queryAppend(String.format("SELECT v.amp_activity_id FROM v_disaster_response_marker v WHERE val_id IN (%s)", Util.toCSStringForIN(disasterResponse)));
            
        if (workspaces != null && workspaces.size() > 0)
            queryAppend(WORKSPACE_FILTER);
        // if(donors!=null && donors.size()>0) queryAppend(ORG_FILTER);
        
        queryAppend(SECTOR_FILTER);
        queryAppend(SECONDARY_SECTOR_FILTER);
        queryAppend(TERTIARY_SECTOR_FILTER);
        queryAppend(quaternarySectorFilter);
        queryAppend(quinarySectorFilter);
        queryAppend(TAG_SECTOR_FILTER);

        queryAppend(generateProgramFilterSubquery(nationalPlanningObjectives, "National Plan Objective"));
        queryAppend(generateProgramFilterSubquery(primaryPrograms, "Primary Program"));
        queryAppend(generateProgramFilterSubquery(secondaryPrograms, "Secondary Program"));
        
        if (this.getSelectedActivityPledgesSettings() != null && this.getSelectedActivityPledgesSettings() > 0) {
            String hasPledgeFilter = "SELECT DISTINCT(amp_activity_id) FROM v_related_pledges";
            if (this.getSelectedActivityPledgesSettings().equals(SELECTED_ACTIVITY_PLEDGES_SETTINGS_WITH_PLEDGES_ONLY)) {
                queryAppend(hasPledgeFilter);
            }
            else {
                queryNotAppend(hasPledgeFilter);
            }
        }
        
        if (regions != null && regions.size() > 0)
            queryAppend(REGION_FILTER);
        if (financingInstruments != null && financingInstruments.size() > 0)
            queryAppend(FINANCING_INSTR_FILTER);
        if (fundingStatus != null && fundingStatus.size() > 0)
            queryAppend(FUNDING_STATUS_FILTER);
        if (aidModalities != null && !aidModalities.isEmpty())
            queryAppend(AID_MODALITIES_FILTER);
        if (risks != null && risks.size() > 0)
            queryAppend(RISK_FILTER);
        if ((lineMinRank != null) && (lineMinRank.size() > 0))
            queryAppend(LINE_MIN_RANK_FILTER);
        //if (regionSelected != null)
        //  queryAppend(REGION_SELECTED_FILTER);
        if (!REGION_SELECTED_FILTER.equals("")) {
            queryAppend(REGION_SELECTED_FILTER);
        }
        
        queryAppend(approvalStatusQuery);

        if (typeOfAssistance != null && typeOfAssistance.size() > 0)
            queryAppend(TYPE_OF_ASSISTANCE_FILTER);
        
        if (expenditureClass != null && expenditureClass.size() > 0)
            queryAppend(EXPENDITURE_CLASS_FILTER);

        if (performanceAlertLevel != null && performanceAlertLevel.size() > 0) {
            queryAppend(performanceAlertLevelFilter);
        }
        
        if (modeOfPayment != null && modeOfPayment.size() > 0)
            queryAppend(MODE_OF_PAYMENT_FILTER);
        
        if (concessionalityLevel != null && concessionalityLevel.size() > 0) {
            queryAppend(CONCESSIONALITY_LEVEL_FILTER);
        }

        if (projectCategory != null && projectCategory.size() > 0)
            queryAppend(PROJECT_CATEGORY_FILTER);
        
        if ( activityPledgesTitle != null && activityPledgesTitle.size() > 0 ) {
            queryAppend(ACTIVITY_PLEDGES_TITLE);
        }
        
        if(projectImplementingUnits!=null && projectImplementingUnits.size() > 0){
            queryAppend(PROJECT_IMPL_UNIT_FILTER);
        }
        
        if (donorGroups != null && donorGroups.size() > 0)
            queryAppend(DONOR_GROUP_FILTER);
            
        if ((contractingAgencyGroups != null) && (contractingAgencyGroups.size() > 0))
            queryAppend(CONTRACTING_AGENCY_GROUP_FILTER);
            
        if (donorTypes != null && donorTypes.size() > 0)
            queryAppend(DONOR_TYPE_FILTER);

        if (executingAgency != null && executingAgency.size() > 0)
            queryAppend(EXECUTING_AGENCY_FILTER);

        if (contractingAgency != null && contractingAgency.size() > 0)
            queryAppend(CONTRACTING_AGENCY_FILTER);

        if (beneficiaryAgency != null && beneficiaryAgency.size() > 0)
            queryAppend(BENEFICIARY_AGENCY_FILTER);
        
        if (implementingAgency != null && implementingAgency.size() > 0)
            queryAppend(IMPLEMENTING_AGENCY_FILTER);

        if (donnorgAgency != null && donnorgAgency.size() > 0)
            queryAppend(DONNOR_AGENCY_FILTER);
        
        if (responsibleorg!=null && responsibleorg.size() >0){
            queryAppend(RESPONSIBLE_ORGANIZATION_FILTER);
        }

        if (componentFunding != null && componentFunding.size() > 0) {
            queryAppend(COMPONENT_FUNDING_ORGANIZATION_FILTER);
        }

        if (componentSecondResponsible != null && componentSecondResponsible.size() > 0) {
            queryAppend(COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION_FILTER);
        }

        if (actualAppYear!=null && actualAppYear!=-1) {
            queryAppend(ACTUAL_APPROVAL_YEAR_FILTER);
        }
        
        if (governmentApprovalProcedures != null) {
            String GOVERNMENT_APPROVAL_FILTER = "SELECT a.amp_activity_id from amp_activity a where governmentApprovalProcedures="
                    + ((governmentApprovalProcedures)?"true":"false");
            queryAppend(GOVERNMENT_APPROVAL_FILTER);
        }
        if (jointCriteria != null) {
            String JOINT_CRITERIA_FILTER = "SELECT a.amp_activity_id from amp_activity a where jointCriteria="
                    + ((jointCriteria)?"true":"false");;
            queryAppend(JOINT_CRITERIA_FILTER);
        }
        if (showArchived != null) {
            queryAppend(ARCHIVED_FILTER);
        }
        if ( multiDonor != null ) {
            queryAppend( MULTI_DONOR );
        }
        if (params.getActivityIdFilter() != null) {
            queryAppend(ACTIVITY_ID_FILTER);
        }
        
        
        String ISOLATED_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE amp_activity_id IN (select amp_activity_id FROM amp_activity_version aav WHERE " 
                + "aav.amp_team_id IN (select amp_team_id from amp_team WHERE isolated = true)) ";
        
        /* TEAM FILTER HACK ZONE
         * because in certain situations this zone can add an OR, any queryAppend calls MUST be done BEFORE THIS AREA
         */
        if ((loggedInTeamMember == null) || !loggedInTeamMember.getTeamIsolated()) {
            queryNotAppend(ISOLATED_FILTER);
        }
        
        
        processTeamFilter(loggedInTeamMember, params.getWorkspaceFilter());
        
        /**
         * NO queryAppend CALLS AFTER THIS POINT !
         */
        
        if ( this.isPublicView() && ! this.budgetExport  ){
            generatedFilterQuery = getOffLineQuery(generatedFilterQuery);
        }

        //DbUtil.countActivitiesByQuery(this.generatedFilterQuery,indexedParams);
//      logger.error(this.generatedFilterQuery);
    }
    
    
    @PropertyListableIgnore
    protected StringGenerator overridingTeamFilter = null;
    
    
    @PropertyListableIgnore
    public void setOverridingTeamFilter(StringGenerator overridingTeamFilter)
    {
        this.overridingTeamFilter = overridingTeamFilter;
    }
    
    @PropertyListableIgnore
    public StringGenerator getOverridingTeamFilter()
    {
        return overridingTeamFilter;
    }
    
    /**
     * appends to the query statements filtering by various date fields, most importantly - transaction date
     */
    protected void buildDatesFilterStatements() {
        // build transaction date filtering statements
        String removeEmptyRows = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.REPORTS_REMOVE_EMPTY_ROWS);
        boolean dateFilterHidesProjects = "true".equalsIgnoreCase(removeEmptyRows);
        
        String[] dates = this.calculateDateFilters(fromDate, toDate, dynDateFilterCurrentPeriod, dynDateFilterAmount, dynDateFilterOperator, dynDateFilterXPeriod);
        String fromDate = dates[0];
        String toDate = dates[1];
        
        if (dateFilterHidesProjects && fromDate != null && fromDate.length() > 0) {
            String FROM_DATE_FILTER = null;
            try {
                FROM_DATE_FILTER = " SELECT distinct(f.amp_activity_id) FROM amp_funding_detail fd, amp_funding f WHERE f.amp_funding_id=fd.amp_funding_id AND fd.transaction_date>='"
                    + sdfOut.format(sdfIn.parse(fromDate)) + "'";

            } catch (ParseException e) {
                logger.error(e, e);
            }
            dateFilterUsed = true;
            queryAppend(FROM_DATE_FILTER);
        }
        if (dateFilterHidesProjects && toDate != null && toDate.length()>0) {
            String TO_DATE_FILTER=null;
            try {
                TO_DATE_FILTER = " SELECT distinct(f.amp_activity_id) FROM amp_funding_detail fd, amp_funding f WHERE f.amp_funding_id=fd.amp_funding_id AND fd.transaction_date<='"
                    +  sdfOut.format(sdfIn.parse(toDate)) + "'";
            } catch (ParseException e) {
                logger.error(e, e);
            }
            dateFilterUsed = true;
            queryAppend(TO_DATE_FILTER);
        }
        
        // build activity start date filtering statements
        dates = this.calculateDateFilters(fromActivityStartDate, toActivityStartDate, dynActivityStartFilterCurrentPeriod, dynActivityStartFilterAmount, dynActivityStartFilterOperator, dynActivityStartFilterXPeriod);
        fromDate = dates[0];
        toDate = dates[1];

        String ACTIVITY_START_DATE_FILTER       = this.createDateCriteria(toDate, fromDate, "asd.actual_start_date");
        if ( ACTIVITY_START_DATE_FILTER.length() > 0 ) {
            ACTIVITY_START_DATE_FILTER = "SELECT asd.amp_activity_id from v_actual_start_date asd WHERE " + ACTIVITY_START_DATE_FILTER;
            queryAppend(ACTIVITY_START_DATE_FILTER);
        }

        // build issue date filtering statements
        dates = this.calculateDateFilters(fromIssueDate, toIssueDate, dynIssueFilterCurrentPeriod,
                dynIssueFilterAmount, dynIssueFilterOperator, dynIssueFilterXPeriod);
        fromDate = dates[0];
        toDate = dates[1];

        String ISSUE_DATE_FILTER = this.createDateCriteria(toDate, fromDate, "asd.issuedate");
        if ( ISSUE_DATE_FILTER.length() > 0 ) {
            ISSUE_DATE_FILTER = "SELECT asd.amp_activity_id from v_issue_date asd WHERE " + ISSUE_DATE_FILTER;
            queryAppend(ISSUE_DATE_FILTER);
        }
        
        dates = this.calculateDateFilters(fromActivityActualCompletionDate, toActivityActualCompletionDate, dynActivityActualCompletionFilterCurrentPeriod, dynActivityActualCompletionFilterAmount, dynActivityActualCompletionFilterOperator, dynActivityActualCompletionFilterXPeriod);
        fromDate = dates[0];
        toDate = dates[1];

        String ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER       = this.createDateCriteria(toDate, fromDate, "acd.actual_completion_date");
        if ( ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER.length() > 0 ) {
            ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER = "SELECT acd.amp_activity_id from v_actual_completion_date acd WHERE " + ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER;
            queryAppend(ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER);
        }

        dates = this.calculateDateFilters(fromActivityFinalContractingDate, toActivityFinalContractingDate, dynActivityFinalContractingFilterCurrentPeriod, dynActivityFinalContractingFilterAmount, dynActivityFinalContractingFilterOperator, dynActivityFinalContractingFilterXPeriod);
        fromDate = dates[0];
        toDate = dates[1];

        String ACTIVITY_FINAL_CONTRACTING_DATE_FILTER       = this.createDateCriteria(toDate, fromDate, "ctrd.contracting_date");
        if ( ACTIVITY_FINAL_CONTRACTING_DATE_FILTER.length() > 0 ) {
            ACTIVITY_FINAL_CONTRACTING_DATE_FILTER = "SELECT ctrd.amp_activity_id from v_contracting_date ctrd WHERE " + ACTIVITY_FINAL_CONTRACTING_DATE_FILTER;
            queryAppend(ACTIVITY_FINAL_CONTRACTING_DATE_FILTER);
        }
        
        dates = this.calculateDateFilters(fromProposedApprovalDate, toProposedApprovalDate, dynProposedApprovalFilterCurrentPeriod, dynProposedApprovalFilterAmount, dynProposedApprovalFilterOperator, dynProposedApprovalFilterXPeriod);
        fromDate = dates[0];
        toDate = dates[1];
        
        String ACTIVITY_PROPOSED_APPROVAL_DATE_FILTER       = this.createDateCriteria(toDate, fromDate, "apsd.proposed_approval_date");
        if ( ACTIVITY_PROPOSED_APPROVAL_DATE_FILTER.length() > 0 ) {
            ACTIVITY_PROPOSED_APPROVAL_DATE_FILTER = "SELECT apsd.amp_activity_id from v_actual_proposed_date apsd WHERE " + ACTIVITY_PROPOSED_APPROVAL_DATE_FILTER;
            queryAppend(ACTIVITY_PROPOSED_APPROVAL_DATE_FILTER);
        }

        dates = this.calculateDateFilters(fromEffectiveFundingDate, toEffectiveFundingDate, dynEffectiveFundingFilterCurrentPeriod, dynEffectiveFundingFilterAmount, dynEffectiveFundingFilterOperator, dynEffectiveFundingFilterXPeriod);
        fromDate = dates[0];
        toDate = dates[1];

        String ACTIVITY_EFFECTIVE_FUNDING_DATE_FILTER       = this.createDateCriteria(toDate, fromDate, "apsd.effective_funding_date");
        if (ACTIVITY_EFFECTIVE_FUNDING_DATE_FILTER.length() > 0) {
            ACTIVITY_EFFECTIVE_FUNDING_DATE_FILTER = "SELECT apsd.amp_activity_id from v_actual_proposed_date apsd WHERE " + ACTIVITY_EFFECTIVE_FUNDING_DATE_FILTER;
            queryAppend(ACTIVITY_EFFECTIVE_FUNDING_DATE_FILTER);
        }

        dates = this.calculateDateFilters(fromFundingClosingDate, toFundingClosingDate, dynFundingClosingFilterCurrentPeriod, dynFundingClosingFilterAmount, dynFundingClosingFilterOperator, dynFundingClosingFilterXPeriod);
        fromDate = dates[0];
        toDate = dates[1];

        String ACTIVITY_FUNDING_CLOSING_DATE_FILTER     = this.createDateCriteria(toDate, fromDate, "apsd.funding_closing_date");
        if (ACTIVITY_FUNDING_CLOSING_DATE_FILTER.length() > 0) {
            ACTIVITY_FUNDING_CLOSING_DATE_FILTER = "SELECT apsd.amp_activity_id from v_funding_closing_date apsd WHERE " + ACTIVITY_FUNDING_CLOSING_DATE_FILTER;
            queryAppend(ACTIVITY_FUNDING_CLOSING_DATE_FILTER);
        }

    }
    

    protected void processTeamFilter(TeamMember member, boolean workspaceFilter)
    {
        if (overridingTeamFilter != null)
        {
            if (overridingTeamFilter.getString() != null)
                queryAppend(overridingTeamFilter.getString());
            return;
        }
        
        boolean thisIsComputedWorkspaceWithFilters = workspaceFilter && (member != null) && 
                isTrue(member.getComputation()) && isTrue(member.getUseFilters());

        String TEAM_FILTER = WorkspaceFilter.generateWorkspaceFilterQuery(member);

        if (needsTeamFilter)
        {
            /* needsTeamFilter can only be true in public view
             * public views cannot be shared from within a computed Workspace (THIS IS NOT SUPPORTED NOW)
             */
             queryAppend(TEAM_FILTER);
        }
        else
        if (workspaceFilter)
        {
            if (thisIsComputedWorkspaceWithFilters)
            {
                /* do a somewhat ugly hack: the TEAM_FILTER will only contain the activities from within the workspace
                 * here we run the filter part of the workspace and OR with the own activities returned in TEAM_FILTER
                 */
                String activitiesInTheWorkspace = "SELECT amp_activity_id from amp_activity WHERE amp_activity_id IN (" + TEAM_FILTER + ")";
                String otherActivities = this.generatedFilterQuery + (TeamUtil.hideDraft(member) ? " AND draft <> true" : "");
                
                this.generatedFilterQuery = String.format("(%s) UNION (%s)", otherActivities, activitiesInTheWorkspace);
                //this.generatedFilterQuery = String.format("(%s) OR (%s)", this.generatedFilterQuery, queryToAppend);
        }
            else
            {
                // normal workspace filter, works hackless
                queryAppend(TEAM_FILTER);
            }
        }
    }
    
    private String[] calculateDateFilters(String startDate, String lastDate, String currentPeriod, Integer amount, String op, String xPeriod){
        
        String fromDate = startDate;
        String toDate = lastDate;
        
        Date[] ddates = calculateDateFiltersAsDate(currentPeriod, amount, op, xPeriod);
        
        Date dfromDate = ddates[0];
        Date dtoDate = ddates[1];
        
        if (dfromDate != null){
            fromDate = FormatHelper.formatDate(dfromDate);
        }
        if (dtoDate != null){
            toDate = FormatHelper.formatDate(dtoDate);
        }

        return new String[]{fromDate, toDate};
    }
    
    private Date[] calculateDateFiltersAsDate(String currentPeriod, Integer amount, String op, String xPeriod){

        Date dfromDate = null;
        Date dtoDate = null;

        if(currentPeriod != null){

            Integer calendarPeriod = null;
            if(DYNAMIC_FILTER_YEAR.equals(xPeriod)){
                calendarPeriod = Calendar.YEAR;
            }else if(DYNAMIC_FILTER_MONTH.equals(xPeriod)){
                calendarPeriod = Calendar.MONTH;
            }else{
                calendarPeriod = Calendar.DATE;
            }

            if(DYNAMIC_FILTER_ADD_OP.equals(op)){/* + */
                //start date is always the first date of the selected period
                if(DYNAMIC_FILTER_YEAR.equals(currentPeriod)){/*years*/
                    dfromDate = FiscalCalendarUtil.getCalendarStartDateForCurrentYear(calendarType.getAmpFiscalCalId());//first date of current fiscal year                 
                }else if(DYNAMIC_FILTER_MONTH.equals(currentPeriod)){/*months*/
                    dfromDate = FiscalCalendarUtil.getFirstDateOfCurrentMonth();//first date of current month
                }else{ /*days*/
                    dfromDate = FiscalCalendarUtil.getCurrentDate();
                }
                
                dtoDate = FiscalCalendarUtil.addToDate(dfromDate, amount, calendarPeriod);
                
            }else{/* - */
                //end date is always the last date of the selected period
                if(DYNAMIC_FILTER_YEAR.equals(currentPeriod)){/*years*/
                    dtoDate = FiscalCalendarUtil.getCalendarEndDateForCurrentYear(calendarType.getAmpFiscalCalId());//first date of current fiscal year                 
                }else if(DYNAMIC_FILTER_MONTH.equals(currentPeriod)){/*months*/
                    dtoDate = FiscalCalendarUtil.getLastDateOfCurrentMonth();//last date of current month
                }else{ /*days*/
                    dtoDate = FiscalCalendarUtil.getCurrentDate();
                }
                
                dfromDate = FiscalCalendarUtil.addToDate(dtoDate, -amount, calendarPeriod);
            }
        }
        
        return new Date[]{dfromDate, dtoDate};
    }

    /**
     * returns the default currency name
     * default is taken from either user settings, workspace settings or hardcoded global setting, whichever has the value first
     */
    public static AmpCurrency getDefaultCurrency()
    {
        AmpApplicationSettings tempSettings = AmpARFilter.getEffectiveSettings();
        String currCode = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        AmpCurrency result = CurrencyUtil.getAmpcurrency(currCode);
        if (tempSettings != null && tempSettings.getCurrency()!=null)
            result = tempSettings.getCurrency();
        return result; 
    }

    /**
     * returns the default calendar
     * default is taken from either user settings, workspace settings or hardcoded global setting, whichever has the value first
     */
    public static AmpFiscalCalendar getDefaultCalendar() {
        AmpApplicationSettings tempSettings = AmpARFilter.getEffectiveSettings();
        String calendarCode = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
        try {
            AmpFiscalCalendar result = FiscalCalendarUtil.getAmpFiscalCalendar(Long.valueOf(calendarCode));
            if (tempSettings != null && tempSettings.getFiscalCalendar()!=null)
                result = tempSettings.getFiscalCalendar();
            return result;
        } catch (NumberFormatException ignore) {
            return null;
        }

    }

    /**
     * computes the effectively-used currency name: if one is set, then its name is returned, else the user/workspace/system default
     * @return
     */
    public AmpCurrency getUsedCurrency()
    {
        if (getCurrency() != null)
            return getCurrency();
        else
            return getDefaultCurrency();
    }
    
    /**
     * computes the name of the effectively-used currency name: if one is set, then its name is returned, else the user/workspace/system default
     * @return
     */
    public String getUsedCurrencyName()
    {
        return getUsedCurrency().getCurrencyName();
    }
    
    /**
     * @return Returns the ampCurrencyCode.
     */
    public AmpCurrency getCurrency() {
        return currency;
    }

    /**
     * @param ampCurrencyCode
     *            The ampCurrencyCode to set.
     */
    public void setCurrency(AmpCurrency ampCurrencyCode) {
        this.currency = ampCurrencyCode;
    }

    public Set<AmpCategoryValue> getFinancingInstruments() {
        return financingInstruments;
    }

    public void setFinancingInstruments(Set<AmpCategoryValue> financingInstruments) {
        this.financingInstruments = financingInstruments;
    }

    public Set<AmpCategoryValue> getFundingStatus() {
        return fundingStatus;
    }

    public void setFundingStatus(Set<AmpCategoryValue> fundingStatus) {
        this.fundingStatus = fundingStatus;
    }

    public Set<AmpCategoryValue> getAidModalities(){
        return this.aidModalities;
    }
    
    public void setAidModalities(Set<AmpCategoryValue> aidModalities){
        this.aidModalities = aidModalities;
    }
    
    public void setProjectCategory(Set<AmpCategoryValue> projectCategory) {
        this.projectCategory = projectCategory;
    }

    public Set<AmpCategoryValue> getProjectCategory() {
        return projectCategory;
    }

    /**
     * @return Returns the sectors.
     */
    @PropertyListableIgnore
    public Set<AmpSector> getSectors() {
        return sectors;
    }

    /**
     * @param sectors
     *            The sectors to set.
     */
    public void setSectors(Set<AmpSector> sectors )  {
        this.sectors = sectors;
    }   

    /**
     * @return the sectorsAndAncestors
     */
    @PropertyListableIgnore
    public Set<AmpSector> getSectorsAndAncestors() {
        return sectorsAndAncestors;
    }

    /**
     * @param sectorsAndAncestors the sectorsAndAncestors to set
     */
    public void setSectorsAndAncestors(Set<AmpSector> sectorsAndAncestors) {
        this.sectorsAndAncestors = sectorsAndAncestors;
    }

    /**
     * @return Returns the generatedFilterQuery.
     */
    @PropertyListableIgnore
    public String getGeneratedFilterQuery() {
        return generatedFilterQuery;
    }

    /**
     * @return Returns the initialQueryLength.
     */
    @PropertyListableIgnore
    public int getInitialQueryLength() {
        return initialQueryLength;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Returns the calendarType.
     */
    public AmpFiscalCalendar getCalendarType() {
        return calendarType;
    }

    /**
     * @param calendarType
     *            The calendarType to set.
     */
    public void setCalendarType(AmpFiscalCalendar calendarType) {
        this.calendarType = calendarType;
    }

    /**
     * @return Returns the donors.
     */
    // public Set getDonors() {
    // return donors;
    // }
    /**
     * @param donors
     *            The donors to set.
     */
    // public void setDonors(Set donors) {
    // this.donors = donors;
    // }
    /**
     * @return Returns the regions.
     */
    
    public Set getRegions() {
        return regions;
    }

    /**
     * @param regions
     *            The regions to set.
     */
    public void setRegions(Set regions) {
        this.regions = regions;
    }

    /**
     * @return Returns the statuses.
     */
    public Set<AmpCategoryValue> getStatuses() {
        return statuses;
    }

    /**
     * @param statuses
     *            The statuses to set.
     */
    public void setStatuses(Set<AmpCategoryValue> statuses) {
        this.statuses = statuses;
    }

    /**
     * @return Returns the workspaces.
     */
    public Set<AmpTeam> getWorkspaces() {
        return workspaces;
    }
    /**
     * @return Returns all non-isolated (== private / siloed) workspaces
     */ 
    public Set<AmpTeam> getNonPrivateWorkspaces() {
        Set<AmpTeam> newset = new HashSet<>();
        if (workspaces != null) {
            for (AmpTeam team : workspaces) {
                if (!team.getIsolated()) {
                    newset.add(team);
                }
            }
        }
        return newset;
    }
    
    /**
     * @param workspaces
     *            The workspaces to set.
     */
    public void setWorkspaces(Set<AmpTeam> workspaces) {
        this.workspaces = workspaces;
    }

    @PropertyListableIgnore
    public boolean isPublicView() {
        return publicView;
    }

    public void setPublicView(boolean publicView) {
        this.publicView = publicView;
    }

    @PropertyListableIgnore
    public boolean isWidget() {
        return widget;
    }

    /**
     * returns true IFF this filter has a date filter
     * only valid after the filter has been constructed (e.g. generateFilterQuery called) 
     * @return
     */
    @PropertyListableIgnore
    public boolean hasDateFilter()
    {
        return this.dateFilterUsed;
    }
    
    public void setWidget(boolean widget) {
        this.widget = widget;
    }

    public Set<AmpCategoryValue> getBudget() {
        return budget;
    }

    public void setBudget(Set<AmpCategoryValue> budget) {
        this.budget = budget;
    }

    public Set<AmpIndicatorRiskRatings> getRisks() {
        return risks;
    }

    public void setRisks(Set<AmpIndicatorRiskRatings> risks) {
        this.risks = risks;
    }

    /**
     * @return Returns the approvalStatus.
     */

    /**
     * provides a way to display this bean in HTML. Properties are automatically
     * shown along with their values. CollectionS are unfolded and excluded
     * properties (internally used) are not shown.
     * 
     * @see AmpARFilter.IGNORED_PROPERTIES
     */
    public String toString() {
        StringBuffer ret = new StringBuffer();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(AmpARFilter.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                if (IGNORED_PROPERTIES.contains(propertyDescriptors[i].getName()))
                    continue;
                Method m = propertyDescriptors[i].getReadMethod();
                Object object = m.invoke(this, new Object[] {});
                if (object == null || IGNORED_PROPERTIES.contains(propertyDescriptors[i]
                        .getName()))
                    continue;
                ret.append("<b>").append(propertyDescriptors[i].getName())
                        .append(": ").append("</b>");
                if (object instanceof Collection)
                {
                    ret.append(Util.toCSString((Collection) object));
                }
                else
                {
                    ret.append(object);
                }
                if (i < propertyDescriptors.length)
                    ret.append("; ");
            }
        } catch (IntrospectionException e) {
            logger.error(e);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            logger.error(e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error(e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return ret.toString();
    }

    private static final String IGNORED_PROPERTIES = "class#changed#generatedFilterQuery#initialQueryLength#widget#publicView#ampReportId";

    public Collection<Integer> getLineMinRank() {
        return lineMinRank;
    }

    public void setLineMinRank(Collection<Integer> lineMinRank) {
        this.lineMinRank = lineMinRank;
    }



    @PropertyListableIgnore
    public Long getAmpReportId() {
        return ampReportId;
    }

    public void setAmpReportId(Long ampReportId) {
        this.ampReportId = ampReportId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null)
            this.text = text;
        else
            if (text.trim().length() == 0)
                this.text = null;
            else
                this.text = text.trim();

    }

    public void setFromMonth(Integer fromMonth) {
        this.fromMonth = fromMonth;
    }

    public void setToMonth(Integer toMonth) {
        this.toMonth = toMonth;
    }

    public Integer getFromMonth() {
        return fromMonth;
    }

    public Integer getToMonth() {
        return toMonth;
    }

    public Integer getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(Integer fromYear) {
        this.yearFrom = fromYear;
    }

    public Integer getYearTo() {
        return yearTo;
    }

    public void setYearTo(Integer toYear) {
        this.yearTo = toYear;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getGovernmentApprovalProcedures() {
        return governmentApprovalProcedures;
    }

    public void setGovernmentApprovalProcedures(
            Boolean governmentApprovalProcedures) {
        this.governmentApprovalProcedures = governmentApprovalProcedures;
    }

    public Boolean getJointCriteria() {
        return jointCriteria;
    }

    public void setJointCriteria(Boolean jointCriteria) {
        this.jointCriteria = jointCriteria;
    }

    @Override
    public String getBeanName() {
        return null;
    }

//  /**
//   * @return the regionSelected
//   */
//  public AmpCategoryValueLocations getRegionSelected() {
//      return regionSelected;
//  }
//
//  /**
//   * @param regionSelected the regionSelected to set
//   */
//  public void setRegionSelected(AmpCategoryValueLocations regionSelected) {
//      this.regionSelected = regionSelected;
//  }

    public String getIndexText() {
        return indexText;
    }


    public void setIndexText(String indexText) {
        this.indexText = indexText;
    }

    public Set<AmpOrgType> getDonorTypes() {
        return donorTypes;
    }

    public void setDonorTypes(Set<AmpOrgType> donorTypes) {
        this.donorTypes = donorTypes;
    }

    public Set<AmpOrgGroup> getDonorGroups() {
        return donorGroups;
    }

    public void setDonorGroups(Set<AmpOrgGroup> donorGroups) {
        this.donorGroups = donorGroups;
    }

    public void setContractingAgencyGroups(Set<AmpOrgGroup> contractingAgencyGroups) {
        this.contractingAgencyGroups = contractingAgencyGroups;
    }
    
    public Set<AmpOrgGroup> getContractingAgencyGroups(){
        return this.contractingAgencyGroups;
    }
    
    public Set<AmpOrganisation> getBeneficiaryAgency() {
        return beneficiaryAgency;
    }

    public void setBeneficiaryAgency(Set<AmpOrganisation> beneficiaryAgency) {
        this.beneficiaryAgency = beneficiaryAgency;
    }

    public Set<AmpOrganisation> getExecutingAgency() {
        return executingAgency;
    }

    public void setExecutingAgency(Set<AmpOrganisation> executingAgency) {
        this.executingAgency = executingAgency;
    }

    public Set<AmpOrganisation> getContractingAgency() {
        return contractingAgency;
    }

    public void setContractingAgency(Set<AmpOrganisation> contractingAgency) {
        this.contractingAgency = contractingAgency;
    }

    public Set<AmpOrganisation> getImplementingAgency() {
        return implementingAgency;
    }

    public void setImplementingAgency(Set<AmpOrganisation> implementingAgency) {
        this.implementingAgency = implementingAgency;
    }

    public Set<AmpSector> getSelectedSectors() {
        return selectedSectors;
    }

    public void setSelectedSectors(Set<AmpSector> selectedSectors) {
        this.selectedSectors = selectedSectors;
    }

    @PropertyListableIgnore
    public Set<AmpSector> getSecondarySectors() {
        return secondarySectors;
    }

    public void setSecondarySectors(Set<AmpSector> secondarySectors) {
        this.secondarySectors = secondarySectors;
    }
    
    /**
     * @return the secondarySectorsAndAncestors
     */
    @PropertyListableIgnore
    public Set<AmpSector> getSecondarySectorsAndAncestors() {
        return secondarySectorsAndAncestors;
    }

    /**
     * @param secondarySectorsAndAncestors the secondarySectorsAndAncestors to set
     */
    public void setSecondarySectorsAndAncestors(Set<AmpSector> secondarySectorsAndAncestors) {
        this.secondarySectorsAndAncestors = secondarySectorsAndAncestors;
    }

    public Set<AmpSector> getSelectedSecondarySectors() {
        return selectedSecondarySectors;
    }

    public void setSelectedSecondarySectors(Set<AmpSector> selectedSecondarySectors) {
        this.selectedSecondarySectors = selectedSecondarySectors;
    }

    public Set<AmpSector> getSelectedTertiarySectors() {
        return selectedTertiarySectors;
    }

    public void setSelectedTertiarySectors(Set<AmpSector> selectedTertiarySectors) {
        this.selectedTertiarySectors = selectedTertiarySectors;
    }
    
    @PropertyListableIgnore
    public Set<AmpSector> getTertiarySectors() {
        return tertiarySectors;
    }

    public void setTertiarySectors(Set<AmpSector> tertiarySectors) {
        this.tertiarySectors = tertiarySectors;
    }
    @PropertyListableIgnore
    public Set<AmpSector> getTertiarySectorsAndAncestors() {
        return tertiarySectorsAndAncestors;
    }

    public void setTertiarySectorsAndAncestors(Set<AmpSector> tertiarySectorsAndAncestors) {
        this.tertiarySectorsAndAncestors = tertiarySectorsAndAncestors;
    }

    public Set<AmpSector> getSelectedQuaternarySectors() {
        return selectedQuaternarySectors;
    }

    public void setSelectedQuaternarySectors(final Set<AmpSector> selectedQuaternarySectors) {
        this.selectedQuaternarySectors = selectedQuaternarySectors;
    }

    @PropertyListableIgnore
    public Set<AmpSector> getQuaternarySectors() {
        return quaternarySectors;
    }

    public void setQuaternarySectors(final Set<AmpSector> quaternarySectors) {
        this.quaternarySectors = quaternarySectors;
    }

    @PropertyListableIgnore
    public Set<AmpSector> getQuaternarySectorsAndAncestors() {
        return quaternarySectorsAndAncestors;
    }

    public void setQuaternarySectorsAndAncestors(final Set<AmpSector> quaternarySectorsAndAncestors) {
        this.quaternarySectorsAndAncestors = quaternarySectorsAndAncestors;
    }

    public Set<AmpSector> getSelectedQuinarySectors() {
        return selectedQuinarySectors;
    }

    public void setSelectedQuinarySectors(final Set<AmpSector> selectedQuinarySectors) {
        this.selectedQuinarySectors = selectedQuinarySectors;
    }

    @PropertyListableIgnore
    public Set<AmpSector> getQuinarySectors() {
        return quinarySectors;
    }

    public void setQuinarySectors(final Set<AmpSector> quinarySectors) {
        this.quinarySectors = quinarySectors;
    }

    @PropertyListableIgnore
    public Set<AmpSector> getQuinarySectorsAndAncestors() {
        return quinarySectorsAndAncestors;
    }

    public void setQuinarySectorsAndAncestors(final Set<AmpSector> quinarySectorsAndAncestors) {
        this.quinarySectorsAndAncestors = quinarySectorsAndAncestors;
    }

    public Set<AmpCategoryValue> getTypeOfAssistance() {
        return typeOfAssistance;
    }

    public void setTypeOfAssistance(Set<AmpCategoryValue> typeOfAssistance) {
        this.typeOfAssistance = typeOfAssistance;
    }

    public Set<AmpCategoryValue> getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(Set<AmpCategoryValue> modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public Set<AmpCategoryValue> getConcessionalityLevel() {
        return concessionalityLevel;
    }

    public void setConcessionalityLevel(Set<AmpCategoryValue> concessionalityLevel) {
        this.concessionalityLevel = concessionalityLevel;
    }

    public Set<AmpCategoryValue> getActivityPledgesTitle() {
        return activityPledgesTitle;
    }

    public void setActivityPledgesTitle(Set<AmpCategoryValue> activityPledgesTitle) {
        this.activityPledgesTitle = activityPledgesTitle;
    }

    public Integer getRenderStartYear() {
        return renderStartYear;
    }

    public void setRenderStartYear(Integer renderStartYear) {
        if (renderStartYear == null)
            new RuntimeException("null not allowed here!").printStackTrace();
        this.renderStartYear = renderStartYear;     
    }

    public Integer getRenderEndYear() {
        return renderEndYear;
    }

    public void setRenderEndYear(Integer renderEndYear) {
        if (renderEndYear == null)
            new RuntimeException("null not allowed here!").printStackTrace();
        this.renderEndYear = renderEndYear;
    }

    @PropertyListableIgnore
    public DecimalFormat getCurrentFormat() {
        return currentFormat;
    }

    public void setCurrentFormat(DecimalFormat currentFormat) {
        this.currentFormat = currentFormat;
    }

    /**
     * returns the date in the {@link #sdfIn} format
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * returns the fromDate as a Date object
     * @return
     */
    public Date buildFromDateAsDate()
    {
        if (fromDate == null || (fromDate.length() == 0)){//if fromDate is not set, maybe a dynamic filter was applied
            Date[] dates = this.calculateDateFiltersAsDate(this.dynDateFilterCurrentPeriod, this.dynDateFilterAmount, this.dynDateFilterOperator, this.dynDateFilterXPeriod);
            return dates[0];
        }
        try
        {
            return sdfIn.parse(fromDate);
        }
        catch(ParseException e)
        {
            logger.error("invalid date trickled into AmpARFilter::fromDate!", e); // SHOULD NOT HAPPEN!
            return null;
        }
    }
    
    /**
     * returns the toDate as a Date object
     * @return
     */
    public Date buildToDateAsDate()
    {
        if (toDate == null || (toDate.length() == 0)){//if toDate is not set, maybe a dynamic filter was applied
            Date[] dates = this.calculateDateFiltersAsDate(this.dynDateFilterCurrentPeriod, this.dynDateFilterAmount, this.dynDateFilterOperator, this.dynDateFilterXPeriod);
            return dates[1];
        }
        try
        {
            return sdfIn.parse(toDate);
        }
        catch(ParseException e)
        {
            logger.error("invalid date trickled into AmpARFilter::toDate!", e); // SHOULD NOT HAPPEN!
            return null;
        }
    }
    
    /**
     * sets the date in the {@link #sdfIn} format. Will ignore call if fed incorrect data
     */
    public void setFromDate(String fromDate) {
        if (!FormatHelper.isValidDateString(fromDate, sdfIn)) {
            logger.error("tried to push invalidly-formatted date into AmpARFilter: " + fromDate, new RuntimeException());
            return;
        }
        this.fromDate = fromDate;
    }

    /**
     * returns the date in the {@link #sdfIn} format
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * sets the date in the {@link #sdfIn} format. Will ignore call if fed incorrect data
     */
    public void setToDate(String toDate) {
        if (!FormatHelper.isValidDateString(toDate, sdfIn)) {
            logger.error("tried to push invalidly-formatted date into AmpARFilter: " + toDate, new RuntimeException());
            return;
        }
        this.toDate = toDate;
    }
        

    /**
     * @return the fromActivityStartDate
     */
    public String getFromActivityStartDate() {
        return fromActivityStartDate;
    }
    
    /**
     * @return a ['from', 'to'] pair for ActivityStartDate range or [null, null] if none is configured
     */
    public Date[] buildFromAndToActivityStartDateAsDate() {
        Date[] dateRange = buildFromAndTo(fromActivityStartDate, toActivityStartDate);
        if (dateRange != null) {
            return dateRange;
        } else {
            return calculateDateFiltersAsDate(this.dynActivityStartFilterCurrentPeriod, this.dynActivityStartFilterAmount, this.dynActivityStartFilterOperator, this.dynActivityStartFilterXPeriod);
        }
    }

    /**
     * @param fromActivityStartDate the fromActivityStartDate to set
     */
    public void setFromActivityStartDate(String fromActivityStartDate) {
        this.fromActivityStartDate = fromActivityStartDate;
    }

    /**
     * @return the fromIssueDate
     */
    public String getFromIssueDate() {
        return fromIssueDate;
    }

    /**
     * @return a ['from', 'to'] pair for IssueDate range or [null, null] if none is configured
     */
    public Date[] buildFromAndToIssueDateAsDate() {
        Date[] dateRange = buildFromAndTo(fromIssueDate, toIssueDate);
        if (dateRange != null) {
            return dateRange;
        } else {
            return calculateDateFiltersAsDate(this.dynIssueFilterCurrentPeriod, this.dynIssueFilterAmount, this.dynIssueFilterOperator, this.dynIssueFilterXPeriod);
        }
    }

    /**
     * @param fromIssueDate the fromIssueDate to set
     */
    public void setFromIssueDate(String fromIssueDate) {
        this.fromIssueDate = fromIssueDate;
    }

    public String getFromProposedApprovalDate() {
        return fromProposedApprovalDate;
    }
    
    /**
     * @return a ['from', 'to'] pair for ProposedApprovalDate range or [null, null] if none is configured
     */
    public Date[] buildFromAndToProposedApprovalDateAsDate() {
        Date[] dateRange = buildFromAndTo(fromProposedApprovalDate, toProposedApprovalDate);
        if (dateRange != null) {
            return dateRange;
        } else {
            return calculateDateFiltersAsDate(this.dynProposedApprovalFilterCurrentPeriod, this.dynProposedApprovalFilterAmount, this.dynProposedApprovalFilterOperator, this.dynProposedApprovalFilterXPeriod);
        }
    }
    
    public String getFromActualApprovalDate() {
        return fromActualApprovalDate;
    }
    
    public void setFromActualApprovalDate(String fromActualApprovalDate) {
        this.fromActualApprovalDate = fromActualApprovalDate;
    }
    
    public String getToActualApprovalDate() {
        return toActualApprovalDate;
    }
    
    public void setToActualApprovalDate(String toActualApprovalDate) {
        this.toActualApprovalDate = toActualApprovalDate;
    }
    
    public void setFromProposedApprovalDate(String fromProposedApprovalDate) {
        this.fromProposedApprovalDate = fromProposedApprovalDate;
    }
    
    public String getToProposedApprovalDate() {
        return toProposedApprovalDate;
    }

    public void setToProposedApprovalDate(String toProposedApprovalDate) {
        this.toProposedApprovalDate = toProposedApprovalDate;
    }

    public String getFromProposedStartDate() {
        return fromProposedStartDate;
    }

    public void setFromProposedStartDate(String fromProposedStartDate) {
        this.fromProposedStartDate = fromProposedStartDate;
    }

    public String getToProposedStartDate() {
        return toProposedStartDate;
    }

    public void setToProposedStartDate(String toProposedStartDate) {
        this.toProposedStartDate = toProposedStartDate;
    }
    
    public Date[] buildFromAndToActualApprovalDateAsDate() {
        Date[] dateRange = buildFromAndTo(fromActualApprovalDate, toActualApprovalDate);
        if (dateRange != null) {
            return dateRange;
        } else {
            return calculateDateFiltersAsDate(this.dynActualApprovalFilterCurrentPeriod,
                    this.dynActualApprovalFilterAmount,
                    this.dynActualApprovalFilterOperator,
                    this.dynActualApprovalFilterXPeriod);
        }
    }

    public Date[] buildFromAndToProposedStartDateAsDate() {
        return buildFromAndTo(fromProposedStartDate, toProposedStartDate);
    }

    /**
     * @return the toActivityStartDate
     */
    public String getToActivityStartDate() {
        return toActivityStartDate;
    }

    /**
     * @param toActivityStartDate the toActivityStartDate to set
     */
    public void setToActivityStartDate(String toActivityStartDate) {
        this.toActivityStartDate = toActivityStartDate;
    }
    
    

    /**
     * @return the fromActivityActualCompletionDate
     */
    public String getFromActivityActualCompletionDate() {
        return fromActivityActualCompletionDate;
    }
    
    /**
     * @return a ['from', 'to'] pair for ActivityActualCompletionDate range or [null, null] if none is configured
     */
    public Date[] buildFromAndToActivityActualCompletionDateAsDate() {
        Date[] dateRange = buildFromAndTo(fromActivityActualCompletionDate, toActivityActualCompletionDate);
        if (dateRange != null) {
            return dateRange;
        } else {
            return calculateDateFiltersAsDate(this.dynActivityActualCompletionFilterCurrentPeriod, this.dynActivityActualCompletionFilterAmount, this.dynActivityActualCompletionFilterOperator, this.dynActivityActualCompletionFilterXPeriod);
        }
    }

    /**
     * @param fromActivityActualCompletionDate the fromActivityActualCompletionDate to set
     */
    public void setFromActivityActualCompletionDate(
            String fromActivityActualCompletionDate) {
        this.fromActivityActualCompletionDate = fromActivityActualCompletionDate;
    }

    /**
     * @return the toActivityActualCompletionDate
     */
    public String getToActivityActualCompletionDate() {
        return toActivityActualCompletionDate;
    }

    /**
     * @param toActivityActualCompletionDate the toActivityActualCompletionDate to set
     */
    public void setToActivityActualCompletionDate(
            String toActivityActualCompletionDate) {
        this.toActivityActualCompletionDate = toActivityActualCompletionDate;
    }

    public String getDynDateFilterCurrentPeriod() {
        return dynDateFilterCurrentPeriod;
    }

    public void setDynDateFilterCurrentPeriod(String dynDateFilterCurrentPeriod) {
        this.dynDateFilterCurrentPeriod = dynDateFilterCurrentPeriod;
    }

    public Integer getDynDateFilterAmount() {
        return dynDateFilterAmount;
    }

    public void setDynDateFilterAmount(Integer dynDateFilterAmount) {
        this.dynDateFilterAmount = dynDateFilterAmount;
    }

    public String getDynDateFilterOperator() {
        return dynDateFilterOperator;
    }

    public void setDynDateFilterOperator(String dynDateFilterOperator) {
        this.dynDateFilterOperator = dynDateFilterOperator;
    }

    public String getDynDateFilterXPeriod() {
        return dynDateFilterXPeriod;
    }

    public void setDynDateFilterXPeriod(String dynDateFilterXPeriod) {
        this.dynDateFilterXPeriod = dynDateFilterXPeriod;
    }

    public String getDynActivityStartFilterCurrentPeriod() {
        return dynActivityStartFilterCurrentPeriod;
    }

    public void setDynActivityStartFilterCurrentPeriod(
            String dynActivityStartFilterCurrentPeriod) {
        this.dynActivityStartFilterCurrentPeriod = dynActivityStartFilterCurrentPeriod;
    }

    public Integer getDynActivityStartFilterAmount() {
        return dynActivityStartFilterAmount;
    }

    public void setDynActivityStartFilterAmount(Integer dynActivityStartFilterAmount) {
        this.dynActivityStartFilterAmount = dynActivityStartFilterAmount;
    }

    public String getDynActivityStartFilterOperator() {
        return dynActivityStartFilterOperator;
    }

    public void setDynActivityStartFilterOperator(
            String dynActivityStartFilterOperator) {
        this.dynActivityStartFilterOperator = dynActivityStartFilterOperator;
    }

    public String getDynActivityStartFilterXPeriod() {
        return dynActivityStartFilterXPeriod;
    }

    public void setDynActivityStartFilterXPeriod(
            String dynActivityStartFilterXPeriod) {
        this.dynActivityStartFilterXPeriod = dynActivityStartFilterXPeriod;
    }

    public String getDynActivityActualCompletionFilterCurrentPeriod() {
        return dynActivityActualCompletionFilterCurrentPeriod;
    }

    public void setDynActivityActualCompletionFilterCurrentPeriod(
            String dynActivityActualCompletionFilterCurrentPeriod) {
        this.dynActivityActualCompletionFilterCurrentPeriod = dynActivityActualCompletionFilterCurrentPeriod;
    }

    public Integer getDynActivityActualCompletionFilterAmount() {
        return dynActivityActualCompletionFilterAmount;
    }

    public void setDynActivityActualCompletionFilterAmount(
            Integer dynActivityActualCompletionFilterAmount) {
        this.dynActivityActualCompletionFilterAmount = dynActivityActualCompletionFilterAmount;
    }

    public String getDynActivityActualCompletionFilterOperator() {
        return dynActivityActualCompletionFilterOperator;
    }

    public void setDynActivityActualCompletionFilterOperator(
            String dynActivityActualCompletionFilterOperator) {
        this.dynActivityActualCompletionFilterOperator = dynActivityActualCompletionFilterOperator;
    }

    public String getDynActivityActualCompletionFilterXPeriod() {
        return dynActivityActualCompletionFilterXPeriod;
    }

    public void setDynActivityActualCompletionFilterXPeriod(
            String dynActivityActualCompletionFilterXPeriod) {
        this.dynActivityActualCompletionFilterXPeriod = dynActivityActualCompletionFilterXPeriod;
    }

    public String getDynActivityFinalContractingFilterCurrentPeriod() {
        return dynActivityFinalContractingFilterCurrentPeriod;
    }

    public void setDynActivityFinalContractingFilterCurrentPeriod(
            String dynActivityFinalContractingFilterCurrentPeriod) {
        this.dynActivityFinalContractingFilterCurrentPeriod = dynActivityFinalContractingFilterCurrentPeriod;
    }

    public Integer getDynActivityFinalContractingFilterAmount() {
        return dynActivityFinalContractingFilterAmount;
    }

    public void setDynActivityFinalContractingFilterAmount(
            Integer dynActivityFinalContractingFilterAmount) {
        this.dynActivityFinalContractingFilterAmount = dynActivityFinalContractingFilterAmount;
    }

    public String getDynActivityFinalContractingFilterOperator() {
        return dynActivityFinalContractingFilterOperator;
    }

    public void setDynActivityFinalContractingFilterOperator(
            String dynActivityFinalContractingFilterOperator) {
        this.dynActivityFinalContractingFilterOperator = dynActivityFinalContractingFilterOperator;
    }

    public String getDynActivityFinalContractingFilterXPeriod() {
        return dynActivityFinalContractingFilterXPeriod;
    }

    public void setDynActivityFinalContractingFilterXPeriod(
            String dynActivityFinalContractingFilterXPeriod) {
        this.dynActivityFinalContractingFilterXPeriod = dynActivityFinalContractingFilterXPeriod;
    }
    
    public String getDynActualApprovalFilterCurrentPeriod() {
        return dynActualApprovalFilterCurrentPeriod;
    }
    
    public void setDynActualApprovalFilterCurrentPeriod(String dynActualApprovalFilterCurrentPeriod) {
        this.dynActualApprovalFilterCurrentPeriod = dynActualApprovalFilterCurrentPeriod;
    }
    
    public Integer getDynActualApprovalFilterAmount() {
        return dynActualApprovalFilterAmount;
    }
    
    public void setDynActualApprovalFilterAmount(Integer dynActualApprovalFilterAmount) {
        this.dynActualApprovalFilterAmount = dynActualApprovalFilterAmount;
    }
    
    public String getDynActualApprovalFilterOperator() {
        return dynActualApprovalFilterOperator;
    }
    
    public void setDynActualApprovalFilterOperator(String dynActualApprovalFilterOperator) {
        this.dynActualApprovalFilterOperator = dynActualApprovalFilterOperator;
    }
    
    public String getDynActualApprovalFilterXPeriod() {
        return dynActualApprovalFilterXPeriod;
    }
    
    public void setDynActualApprovalFilterXPeriod(String dynActualApprovalFilterXPeriod) {
        this.dynActualApprovalFilterXPeriod = dynActualApprovalFilterXPeriod;
    }
    
    public String getDynProposedApprovalFilterCurrentPeriod() {
        return dynProposedApprovalFilterCurrentPeriod;
    }

    public void setDynProposedApprovalFilterCurrentPeriod(
            String dynProposedApprovalFilterCurrentPeriod) {
        this.dynProposedApprovalFilterCurrentPeriod = dynProposedApprovalFilterCurrentPeriod;
    }

    public Integer getDynProposedApprovalFilterAmount() {
        return dynProposedApprovalFilterAmount;
    }

    public void setDynProposedApprovalFilterAmount(
            Integer dynProposedApprovalFilterAmount) {
        this.dynProposedApprovalFilterAmount = dynProposedApprovalFilterAmount;
    }

    public String getDynProposedApprovalFilterOperator() {
        return dynProposedApprovalFilterOperator;
    }

    public void setDynProposedApprovalFilterOperator(
            String dynProposedApprovalFilterOperator) {
        this.dynProposedApprovalFilterOperator = dynProposedApprovalFilterOperator;
    }

    public String getDynProposedApprovalFilterXPeriod() {
        return dynProposedApprovalFilterXPeriod;
    }

    public void setDynProposedApprovalFilterXPeriod(
            String dynProposedApprovalFilterXPeriod) {
        this.dynProposedApprovalFilterXPeriod = dynProposedApprovalFilterXPeriod;
    }

    /**
     * @return the fromActivityFinalContractingDate
     */
    public String getFromActivityFinalContractingDate() {
        return fromActivityFinalContractingDate;
    }
    
    /**
     * @return a ['from', 'to'] pair for ActivityFinalContractingDate range or [null, null] if none is configured
     */
    public Date[] buildFromAndToActivityFinalContractingDateAsDate() {
        Date[] dateRange = buildFromAndTo(fromActivityFinalContractingDate, toActivityFinalContractingDate);
        if (dateRange != null) {
            return dateRange;
        } else {
            return calculateDateFiltersAsDate(this.dynActivityFinalContractingFilterCurrentPeriod, this.dynActivityFinalContractingFilterAmount, this.dynActivityFinalContractingFilterOperator, this.dynActivityFinalContractingFilterXPeriod);
        }
    }

    /**
     * @return a ['from', 'to'] pair for EffectiveFundingDate range or [null, null] if none is configured
     */
    public Date[] buildFromAndToEffectiveFundingDateAsDate() {
        Date[] dateRange = buildFromAndTo(fromEffectiveFundingDate, toEffectiveFundingDate);
        if (dateRange != null) {
            return dateRange;
        } else {
            return calculateDateFiltersAsDate(this.dynEffectiveFundingFilterCurrentPeriod, this.dynEffectiveFundingFilterAmount, this.dynEffectiveFundingFilterOperator, this.dynEffectiveFundingFilterXPeriod);
        }
    }

    /**
     * @return a ['from', 'to'] pair for FundingClosingDate range or [null, null] if none is configured
     */
    public Date[] buildFromAndToFundingClosingDateAsDate() {
        Date[] dateRange = buildFromAndTo(fromFundingClosingDate, toFundingClosingDate);
        if (dateRange != null) {
            return dateRange;
        } else {
            return calculateDateFiltersAsDate(this.dynFundingClosingFilterCurrentPeriod, this.dynFundingClosingFilterAmount, this.dynFundingClosingFilterOperator, this.dynFundingClosingFilterXPeriod);
        }
    }

    private Date[] buildFromAndTo(String from, String to) {
        boolean noFrom = StringUtils.isEmpty(from);
        boolean noTo = StringUtils.isEmpty(to);
        if (noFrom && noTo) {
            return null;
        } else {
            try {
                return new Date[]{(noFrom ? null : sdfIn.parse(from)), (noTo ? null : sdfIn.parse(to))};
            } catch (ParseException e) {
                logger.error("invalid date trickled into AmpARFilter!", e); // SHOULD NOT HAPPEN!
                return null;
            }
        }
    }

    /**
     * @param fromActivityFinalContractingDate the fromActivityFinalContractingDate to set
     */
    public void setFromActivityFinalContractingDate(
            String fromActivityFinalContractingDate) {
        this.fromActivityFinalContractingDate = fromActivityFinalContractingDate;
    }

    /**
     * @return the toActivityFinalContractingDate
     */
    public String getToActivityFinalContractingDate() {
        return toActivityFinalContractingDate;
    }

    /**
     * @param toActivityFinalContractingDate the toActivityFinalContractingDate to set
     */
    public void setToActivityFinalContractingDate(
            String toActivityFinalContractingDate) {
        this.toActivityFinalContractingDate = toActivityFinalContractingDate;
    }

    public Collection<String> getApprovalStatusSelected() {
        return approvalStatusSelected;
    }

    public void setApprovalStatusSelected(Collection<String> approvalStatusSelected) {
        this.approvalStatusSelected = approvalStatusSelected;
    }

    @PropertyListableIgnore
    public List<String> getApprovalStatusSelectedStrings() {
        ArrayList<String> approvalStatuses = new ArrayList<String>();
        if (approvalStatusSelected == null)
            return approvalStatuses;
        
        for (String status:approvalStatusSelected) {
            switch (Integer.parseInt(status)) {
            
            case 1:
                approvalStatuses.add(TranslatorWorker.translateText("New Draft"));
                break;
                
            case 2:
                approvalStatuses.add(TranslatorWorker.translateText("New Unvalidated"));
                break;
                
            case 3:
                approvalStatuses.add(TranslatorWorker.translateText("Existing Draft"));
                break;
                
            case 4:
                approvalStatuses.add(TranslatorWorker.translateText("Validated Activities"));
                break;
                
            case 0:
                approvalStatuses.add(TranslatorWorker.translateText("Existing Unvalidated"));
                break;
            }
        }
        return approvalStatuses;
    }

    public Set<AmpOrganisation> getDonnorgAgency() {
        return donnorgAgency;
    }

    public void setDonnorgAgency(Set<AmpOrganisation> donnorgAgency) {
        this.donnorgAgency = donnorgAgency;
    }
    @IgnorePersistence
    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getTeamAccessType() {
        return teamAccessType;
    }

    public void setTeamAccessType(String teamAccessType) {
        this.teamAccessType = teamAccessType;
    }
    
    public boolean isWorkspaceonly() {
        return workspaceonly;
    }

    public void setWorkspaceonly(boolean workspaceonly) {
        this.workspaceonly = workspaceonly;
    }
    
    public boolean isJustSearch() {
        return justSearch;
    }

    public void setJustSearch(boolean justSearch) {
        this.justSearch = justSearch;
    }
    @PropertyListableIgnore
    public ArrayList<FilterParam> getIndexedParams() {
        return indexedParams;
    }

    public Set<AmpOrganisation> getResponsibleorg() {
        return responsibleorg;
    }

    public void setResponsibleorg(Set<AmpOrganisation> responsibleorg) {
        this.responsibleorg = responsibleorg;
    }

    public Set<AmpOrganisation> getComponentFunding() {
        return componentFunding;
    }

    public void setComponentFunding(Set<AmpOrganisation> componentFunding) {
        this.componentFunding = componentFunding;
    }

    public Set<AmpOrganisation> getComponentSecondResponsible() {
        return componentSecondResponsible;
    }

    public void setComponentSecondResponsible(Set<AmpOrganisation> componentSecondResponsible) {
        this.componentSecondResponsible = componentSecondResponsible;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getSortByAsc() {
        return sortByAsc;
    }

    public void setSortByAsc(Boolean sortByAsc) {
        this.sortByAsc = sortByAsc;
    }

    public List<String> getHierarchySorters() {
        return cleanupHierarchySorters(hierarchySorters);
    }

    public void setHierarchySorters(List<String> hierarchySorters) {
        this.hierarchySorters = cleanupHierarchySorters(hierarchySorters);
    }

    public List<Long> getAmpActivityIdOrder() {
        return ampActivityIdOrder;
    }

    public void setAmpActivityIdOrder(List<Long> ampActivityIdOrder) {
        this.ampActivityIdOrder = ampActivityIdOrder;
    }

    /** for each given sorting key only keeps the last entry */
    protected List<String> cleanupHierarchySorters(List<String> in) {
        if (in == null || in.isEmpty())
            return in;
        LinkedHashMap<String, String> entriesByHier = new LinkedHashMap<>();
        
        for(String entry:in) {
            String key = entry.substring(0, entry.lastIndexOf('_'));
            entriesByHier.put(key, entry);
        }
        in.clear();
        in.addAll(entriesByHier.values());
        return in;
    }

    public String getCRISNumber() {
        return CRISNumber;
    }

    public void setCRISNumber(String number) {
        CRISNumber = number;
    }

    public String getBudgetNumber() {
        return budgetNumber;
    }

    public void setBudgetNumber(String budgetNumber) {
        this.budgetNumber = budgetNumber;
    }

    public Integer getComputedYear() {
        return computedYear;
    }

    public void setComputedYear(Integer computedYear) {
        this.computedYear = computedYear;
    }

    public Integer getActualAppYear() {
        return actualAppYear;
    }

    public void setActualAppYear(Integer actualAppYear) {
        this.actualAppYear = actualAppYear;
    }

    /**
     * @return the locationSelected
     */
    public Collection<AmpCategoryValueLocations> getLocationSelected() {
        return locationSelected;
    }

    /**
     * @param locationSelected the locationSelected to set
     */
    public void setLocationSelected(
            Collection<AmpCategoryValueLocations> locationSelected) {
        this.locationSelected = locationSelected;
    }

    /**
     * @return the unallocatedLocation
     */
    public Boolean getUnallocatedLocation() {
        return unallocatedLocation;
    }

    /**
     * @param unallocatedLocation the unallocatedLocation to set
     */
    public void setUnallocatedLocation(Boolean unallocatedLocation) {
        this.unallocatedLocation = unallocatedLocation;
    }
    
    /**
     * only call this function directly if you NEED to know that the underlying value is null. In case you just want to know the value of the option, call computeEffectiveAmountInThousand 
     * @return
     */
    public Integer getAmountinthousand() {
        return amountinthousand;
    }

    public void setAmountinthousand(Integer amountinthousand) {
        this.amountinthousand = amountinthousand;
    }

    /**
     * returns AMOUNT_OPTION_IN_UNITS if amountInThousand is NULL, else returns amountInThousand
     * @return
     */
    public int computeEffectiveAmountInThousand()
    {
        if (getAmountinthousand() == null)
            return AMOUNT_OPTION_IN_UNITS;
        return getAmountinthousand();
    }
    
    /**
     * @return the relatedLocations
     */
    @PropertyListableIgnore
    public Collection<AmpCategoryValueLocations> getRelatedLocations() {
        return relatedLocations;
    }

    /**
     * @param relatedLocations the relatedLocations to set
     */
    public void setRelatedLocations(
            Collection<AmpCategoryValueLocations> relatedLocations) {
        this.relatedLocations = relatedLocations;
    }
    
    public Collection<AmpCategoryValueLocations> getPledgesLocations() {
        return pledgesLocations;
    }

    public void setPledgesLocations(
            Collection<AmpCategoryValueLocations> pledgesLocations) {
        this.pledgesLocations = pledgesLocations;
    }

    public Set<AmpCategoryValue> getProjectImplementingUnits() {
        return projectImplementingUnits;
    }
    
    
    
    public void setProjectImplementingUnits(
            Set<AmpCategoryValue> projectImplementingUnits) {
        this.projectImplementingUnits = projectImplementingUnits;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public String getSearchMode() {
        return searchMode;
    }

    /**
     * @return the showArchived
     */
    public Boolean getShowArchived() {
        return showArchived;
    }

    /**
     * @param showArchived
     *            the showArchived to set
     */
    public void setShowArchived(Boolean showArchived) {
        this.showArchived = showArchived;
    }
    
    @PropertyListableIgnore
    public String getFilterConditionOnly() {
        String genFilter = this.getGeneratedFilterQuery();
        int pos = genFilter.indexOf(initialFilterQuery);
        genFilter = genFilter.substring(pos + initialFilterQuery.length());
        pos = genFilter.indexOf("AND");
        if (pos > 0)
            genFilter = genFilter.substring(pos + 3); //don't crash on empty filters
        return genFilter;
    }
    
    @PropertyListableIgnore
    public static String getOffLineQuery(String query) {
        String result = query;
        Pattern p = Pattern.compile(ArConstants.AMP_ACTIVITY_TABLE);
        Matcher m = p.matcher(result);
        result = m.replaceAll(ArConstants.CACHED_ACTIVITY_TABLE);
        return result;
    }

    /**
     * modified copy-paste from (now-deleted) CategAmountColWorker::isRenderizable()
     * @param year
     * @return
     */
    public boolean passesYearRangeFilter(int year)
    {
        boolean renderizable=true;
        
        //we now check if the year filtering is used - we do not want items from other years to be shown
        if((this.getRenderStartYear()!=null && this.getRenderStartYear()> 0) || (this.getRenderEndYear()!=null && this.getRenderEndYear() > 0  )) {
            Integer itemYear = year;
            
            if (this.getRenderStartYear() != null && this.getRenderStartYear() > 0 &&
                itemYear.intValue() < this.getRenderStartYear().intValue()) renderizable=false;
            
            if (this.getRenderEndYear() != null && this.getRenderEndYear() > 0 &&
                itemYear.intValue() > this.getRenderEndYear().intValue()) renderizable=false;
        }
        return renderizable;
    }
    
    /**
     * @return the groupingsize
     */
    public Integer getGroupingsize() {
        return groupingsize;
    }

    /**
     * @param groupingsize the groupingsize to set
     */
    public void setGroupingsize(Integer groupingsize) {
        this.groupingsize = groupingsize;
    }
    
    public Integer getSelectedActivityPledgesSettings() {
        return selectedActivityPledgesSettings;
    }

    public void setSelectedActivityPledgesSettings(Integer selectedActivityPledgesSettings) {
        if (selectedActivityPledgesSettings != null)
            this.selectedActivityPledgesSettings = selectedActivityPledgesSettings;
    }
    
    /**
     * @return the customusegroupings
     */
    public Boolean getCustomusegroupings() {
        return customusegroupings;
    }

    /**
     * @param customusegroupings the customusegroupings to set
     */
    public void setCustomusegroupings(Boolean customusegroupings) {
        this.customusegroupings = customusegroupings;
    }
    
    @PropertyListableIgnore
    public Set<AmpSector> getTagSectors() {
        return tagSectors;
    }

    public void setTagSectors(Set<AmpSector> tagSectors) {
        this.tagSectors = tagSectors;
    }

    public Set<AmpSector> getSelectedTagSectors() {
        return selectedTagSectors;
    }

    public void setSelectedTagSectors(Set<AmpSector> selectedTagSectors) {
        this.selectedTagSectors = selectedTagSectors;
    }
    @PropertyListableIgnore
    public Set<AmpSector> getTagSectorsAndAncestors() {
        return tagSectorsAndAncestors;
    }

    public void setTagSectorsAndAncestors(Set<AmpSector> tagSectorsAndAncestors) {
        this.tagSectorsAndAncestors = tagSectorsAndAncestors;
    }

    /*THIS IS USED FOR PLEDGES IN ORDER TO SHOW ONLY PLEDGES ASSOCIATED TO THE ACTIVITIES THAT BELONG TO THE WORKSPACE
     PLEASE DON'T DELETE IT AGAIN*/
    public Set getAmpTeamsforpledges() {
        return ampTeamsforpledges;
    }

    /*THIS IS USED FOR PLEDGES IN ORDER TO SHOW ONLY PLEDGES ASSOCIATED TO THE ACTIVITIES THAT BELONG TO THE WORKSPACE
     PLEASE DON'T DELETE IT AGAIN*/
    public void setAmpTeamsforpledges(Set ampTeamsforpledges) {
        this.ampTeamsforpledges = ampTeamsforpledges;
    }
     
    
    /**
     * effective team member - used for generating the TeamFilter
     * equals currently logged-in user or, if missing, the AmpReport owner
     * take care for special values (always negative) like TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES!
     * @return
     */
    public Long getTeamMemberId()
    {
        return this.teamMemberId;
    }

    /**
     * only valid after the query has been generated!
     * @return
     */
    @PropertyListableIgnore
    public boolean isPledgeFilter()
    {
        return pledgeFilter;
    }

    public void setTeamMemberId(Long teamMemberId)
    {
        this.teamMemberId =teamMemberId; 
    }
    
    public boolean getNeedsTeamFilter()
    {
        return needsTeamFilter;
    }
    
    private void setNeedsTeamFilter(boolean needs)
    {
        this.needsTeamFilter = needs;
    }
    
    public void signalSettingsHaveBeenApplied()
    {
        this.settingsHaveBeenAppliedFlag = true;
    }
    
    public boolean haveSettingsBeenApplied()
    {
        return settingsHaveBeenAppliedFlag;
    }

    /**
     * makes a detached copy of this filter in something which will be runnable as a pledge filter
     * @return
     */
    public AmpARFilter asPledgeFilter() {
        try {
            AmpARFilter res = (AmpARFilter) this.clone();
            res.pledgeFilter = true;
            res.ampReportId = DUMMY_SUPPLEMENTARY_PLEDGE_FETCHING_REPORT_ID;
            res.initFilterQuery();
            res.generatePledgeFilterQuery();
            return res;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Generates a fragment of the query to filter for a given approval status 
     * 
     * @param id, the approval status to query
     * @param inclusive, whether the element should be included or if it is a negative filter 
     * @return the fragment of the query
     */
    public static String buildApprovalStatusQuery(int id, boolean negative) {
        String verb = negative ? "NOT " : "";
        
        switch (id) {
            case -1:
                return "1=1";
                
            case 0:// Existing Un-validated - This will show all the activities that
                // have been approved at least once and have since been edited
                // and not validated.           
                return String.format("%s(approval_status IN ('edited', 'not_approved', 'rejected') and draft <> true)", verb);
        
            case 1:// New Draft - This will show all the activities that have never
                // been approved and are saved as drafts.
                return String.format("%s(approval_status IN ('started', 'startedapproved') and draft is true) ", verb);
                
            case 2:// New Un-validated - This will show all activities that are new
                // and have never been approved by the workspace manager.
                return String.format("%s(approval_status = 'started' and draft <> true)", verb);
            
            case 3:// existing draft. This is because when you filter by Existing
                // Unvalidated you get draft activites that were edited and
                // saved as draft
                return String.format("%s(approval_status IN ('edited', 'approved') AND (draft is true))", verb);
            
            case 4:// Validated Activities
                return String.format("%s(approval_status IN ('approved', 'startedapproved') and (draft<>true))", verb);
            
            default:
                throw new RuntimeException("unrecognized approval status value: " + id);
        }
    }

    /**
     * Generates a fragment of the query to filter for a given approval status options (e.g., OR between options)
     * @param ids - a series of IDs which could be recognized by {@link #buildApprovalStatusQuery(int, boolean)}
     * @param negative
     * @return
     */
    public static String buildApprovalStatusQuery(Collection<String> ids, boolean negative) {
        List<String> queries = new ArrayList<>();
        for(String id:ids)
            queries.add(buildApprovalStatusQuery(Integer.parseInt(id), negative));
        
        return mergeStatements(queries, "OR");
    }
    
    /**
     * returns:
     *      "", if statements.empty
     *      AND (statements[0]), if statements.len = 1
     *      AND ((statements[0]) OR (statements[1]) OR (statements[2])), if statements.len > 1
     * @param statements
     * @return
     */
    public static String mergeStatements(List<String> statements) {
        return mergeStatements(statements, "OR");
    }   

    /**
     * returns:
     *      "", if statements.empty <br />
     *      AND (statements[0]), if statements.len = 1 <br />
     *      AND ((statements[0]) [separator] (statements[1]) [separator] (statements[2])), if statements.len > 1 <br />
     * @param statements
     * @return
     */
    public static String mergeStatements(List<String> statements, String separator) {
        if (statements.isEmpty())
            return "";
        if (statements.size() == 1)
            return String.format(" AND (%s)", statements.get(0));
                    
        StringBuilder ret = new StringBuilder(" AND (");
        
        for(int i = 0; i < statements.size(); i++) {
            if (i > 0) {
                ret.append(" " + separator + " ");
            }
            ret.append("(").append(statements.get(i)).append(")");
        }
        ret.append(" )");
        return ret.toString();
    }

    public Set<Integer> getHumanitarianAid() {
        return humanitarianAid;
    }

    public void setHumanitarianAid(Set<Integer> humanitarianAid) {
        this.humanitarianAid = humanitarianAid;
    }
    
    public Set<Integer> getDisasterResponse() {
        return disasterResponse;
    }

    public Set<Integer> getDisasterResponseCodes() {
        Set<Integer> res = new HashSet<>();
        
        if (disasterResponse == null)
            return null;
        
        for(int v:disasterResponse) {
            if (v == 1 || v == 2) res.add(v);
            else res.add(999999999);
        }
        return res;
    }
    
    public void setDisasterResponse(Set<Integer> disasterResponse) {
        this.disasterResponse = disasterResponse;
    }
    
    public Set<AmpCategoryValue> getExpenditureClass() {
        return expenditureClass;
    }

    public Set<Long> getExpenditureClassForFilters() {
        if (expenditureClass == null) return null;
        if (expenditureClass.isEmpty()) return new HashSet<>();
        Set<Long> res = new HashSet<>();
        for(AmpCategoryValue acv:expenditureClass)
            res.add(acv.getId());
        res.add(0l);
        return res;
    }
    
    public void setExpenditureClass(Set<AmpCategoryValue> expenditureClass) {
        this.expenditureClass = expenditureClass;
    }

    public Set<AmpCategoryValue> getPerformanceAlertLevel() {
        return performanceAlertLevel;
    }

    public void setPerformanceAlertLevel(final Set<AmpCategoryValue> performanceAlertLevel) {
        this.performanceAlertLevel = performanceAlertLevel;
    }

    private Set<Long> getPerformanceAlertLevelForFilters() {
        return Optional.ofNullable(performanceAlertLevel)
                .orElse(emptySet())
                .stream()
                .map(AmpCategoryValue::getId)
                .collect(toSet());
    }
    
    public Set<String> getPerformanceAlertType() {
        return performanceAlertType;
    }

    public void setPerformanceAlertType(final Set<String> performanceAlertLevel) {
        this.performanceAlertType = performanceAlertLevel;
    }

    public static boolean isTrue(Boolean b) {
        return b != null && b;
    }

    public String getFromEffectiveFundingDate() {
        return fromEffectiveFundingDate;
    }

    public void setFromEffectiveFundingDate(String fromEffectiveFundingDate) {
        this.fromEffectiveFundingDate = fromEffectiveFundingDate;
    }

    public String getToEffectiveFundingDate() {
        return toEffectiveFundingDate;
    }

    public void setToEffectiveFundingDate(String toEffectiveFundingDate) {
        this.toEffectiveFundingDate = toEffectiveFundingDate;
    }

    public String getDynEffectiveFundingFilterCurrentPeriod() {
        return dynEffectiveFundingFilterCurrentPeriod;
    }

    public void setDynEffectiveFundingFilterCurrentPeriod(String dynEffectiveFundingFilterCurrentPeriod) {
        this.dynEffectiveFundingFilterCurrentPeriod = dynEffectiveFundingFilterCurrentPeriod;
    }

    public Integer getDynEffectiveFundingFilterAmount() {
        return dynEffectiveFundingFilterAmount;
    }

    public void setDynEffectiveFundingFilterAmount(Integer dynEffectiveFundingFilterAmount) {
        this.dynEffectiveFundingFilterAmount = dynEffectiveFundingFilterAmount;
    }

    public String getDynEffectiveFundingFilterOperator() {
        return dynEffectiveFundingFilterOperator;
    }

    public void setDynEffectiveFundingFilterOperator(String dynEffectiveFundingFilterOperator) {
        this.dynEffectiveFundingFilterOperator = dynEffectiveFundingFilterOperator;
    }

    public String getDynEffectiveFundingFilterXPeriod() {
        return dynEffectiveFundingFilterXPeriod;
    }

    public void setDynEffectiveFundingFilterXPeriod(String dynEffectiveFundingFilterXPeriod) {
        this.dynEffectiveFundingFilterXPeriod = dynEffectiveFundingFilterXPeriod;
    }

    public String getFromFundingClosingDate() {
        return fromFundingClosingDate;
    }

    public void setFromFundingClosingDate(String fromFundingClosingDate) {
        this.fromFundingClosingDate = fromFundingClosingDate;
    }

    public String getToFundingClosingDate() {
        return toFundingClosingDate;
    }

    public void setToFundingClosingDate(String toFundingClosingDate) {
        this.toFundingClosingDate = toFundingClosingDate;
    }

    public String getDynFundingClosingFilterCurrentPeriod() {
        return dynFundingClosingFilterCurrentPeriod;
    }

    public void setDynFundingClosingFilterCurrentPeriod(String dynFundingClosingFilterCurrentPeriod) {
        this.dynFundingClosingFilterCurrentPeriod = dynFundingClosingFilterCurrentPeriod;
    }

    public Integer getDynFundingClosingFilterAmount() {
        return dynFundingClosingFilterAmount;
    }

    public void setDynFundingClosingFilterAmount(Integer dynFundingClosingFilterAmount) {
        this.dynFundingClosingFilterAmount = dynFundingClosingFilterAmount;
    }

    public String getDynFundingClosingFilterOperator() {
        return dynFundingClosingFilterOperator;
    }

    public void setDynFundingClosingFilterOperator(String dynFundingClosingFilterOperator) {
        this.dynFundingClosingFilterOperator = dynFundingClosingFilterOperator;
    }

    public String getDynFundingClosingFilterXPeriod() {
        return dynFundingClosingFilterXPeriod;
    }

    public void setDynFundingClosingFilterXPeriod(String dynFundingClosingFilterXPeriod) {
        this.dynFundingClosingFilterXPeriod = dynFundingClosingFilterXPeriod;
    }

    public String getToIssueDate() {
        return toIssueDate;
    }

    public void setToIssueDate(String toIssueDate) {
        this.toIssueDate = toIssueDate;
    }

    public String getDynIssueFilterCurrentPeriod() {
        return dynIssueFilterCurrentPeriod;
    }

    public void setDynIssueFilterCurrentPeriod(String dynIssueFilterCurrentPeriod) {
        this.dynIssueFilterCurrentPeriod = dynIssueFilterCurrentPeriod;
    }

    public Integer getDynIssueFilterAmount() {
        return dynIssueFilterAmount;
    }

    public void setDynIssueFilterAmount(Integer dynIssueFilterAmount) {
        this.dynIssueFilterAmount = dynIssueFilterAmount;
    }

    public String getDynIssueFilterOperator() {
        return dynIssueFilterOperator;
    }

    public void setDynIssueFilterOperator(String dynIssueFilterOperator) {
        this.dynIssueFilterOperator = dynIssueFilterOperator;
    }

    public String getDynIssueFilterXPeriod() {
        return dynIssueFilterXPeriod;
    }

    public void setDynIssueFilterXPeriod(String dynIssueFilterXPeriod) {
        this.dynIssueFilterXPeriod = dynIssueFilterXPeriod;
    }
}
