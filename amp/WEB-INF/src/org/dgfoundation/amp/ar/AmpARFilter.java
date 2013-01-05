/**
 * AmpARFilter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.store.Directory;
import org.dgfoundation.amp.PropertyListable;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.RequestUtils;
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
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.logic.AmpARFilterHelper;
import org.digijava.module.aim.logic.Logic;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.exceptions.UsedCategoryException;
import org.digijava.module.mondrian.query.MoConstants;


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
	
	private static SimpleDateFormat sdfOut=new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfIn=new SimpleDateFormat("dd/MM/yyyy");
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
	private Set<AmpSector> sectorsAndAncestors	= null;
	private Set<AmpSector> selectedSectors = null;
	
	private String CRISNumber;
	private String budgetNumber;
	private Boolean showArchived;
	
	@PropertyListableIgnore
	private Integer computedYear;
	
	@PropertyListableIgnore
	private Integer actualAppYear;
	
	@PropertyListableIgnore
	private ArrayList<FilterParam> indexedParams=null;

	
//	@PropertyListableIgnore
//	private Long activitiesRejectedByFilter;
	
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
	 * see getter for description
	 */
	@PropertyListableIgnore
	private boolean dateFilterUsed;
	
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
	private Set<AmpOrganisation> executingAgency;
	private Set<AmpOrganisation> contractingAgency;
	private Set<AmpOrganisation> implementingAgency;
	private Set<AmpOrganisation> beneficiaryAgency;
	private Set<AmpOrganisation> donnorgAgency;

	private Set teamAssignedOrgs = null;

	private Set<AmpCategoryValue> financingInstruments = null;
	private Set<AmpCategoryValue> projectCategory = null;

	
	private Set<AmpCategoryValue> typeOfAssistance = null;
	private Set<AmpCategoryValue> modeOfPayment = null;
	// private Long ampModalityId=null;

	private AmpCurrency currency = null;
	private Set<AmpTeam> ampTeams = null;
	private Set<AmpTeam> ampTeamsforpledges = null;
	private AmpFiscalCalendar calendarType = null;
	private boolean widget = false;
	private boolean publicView = false;
	private Set<AmpCategoryValue> budget = null;
	private Collection<Integer> lineMinRank;
	private Collection<Integer> planMinRank;
	private String fromDate;
	private String toDate;
	
	private String fromActivityStartDate; // view: v_actual_start_date, column name: Actual Start Date
	private String toActivityStartDate;
	
	private String fromActivityActualCompletionDate; // view: v_actual_completion_date, column name: Current Completion Date
	private String toActivityActualCompletionDate;  // view: v_actual_completion_date, column name: Current Completion Date
	
	private String fromActivityFinalContractingDate; // view: v_contracting_date, column name: Final Date for Contracting
	private String toActivityFinalContractingDate;  // view: v_contracting_date, column name: Final Date for Contracting
	
	private Integer fromMonth;
	private Integer yearFrom;
	private Integer toMonth;
	private Integer yearTo;
	private Collection<AmpCategoryValueLocations> locationSelected;
	@PropertyListableIgnore
	private Collection<AmpCategoryValueLocations> relatedLocations;
	private Boolean unallocatedLocation = null;
	//private AmpCategoryValueLocations regionSelected = null;
	private Collection<String> approvalStatusSelected;
	private boolean approved = false;
	private boolean draft = false;

	private Integer renderStartYear = null; // the range of dates columns that
											// has to be render, years not in
											// range will be computables for
											// totals but wont be renderable
	private Integer renderEndYear = null;

	private DecimalFormat currentFormat = null;
	
	public final static int AMOUNT_OPTION_IN_UNITS = 0; // not sure
	public final static int AMOUNT_OPTION_IN_THOUSANDS = 1;
	public final static int AMOUNT_OPTION_IN_MILLIONS = 2;
	
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
	private static final String initialPledgeFilterQuery = "SELECT distinct(id) FROM amp_funding_pledges WHERE 1=1 ";
	private static final String initialFilterQuery = "SELECT distinct(amp_activity_id) FROM amp_activity WHERE 1=1 ";
	private String generatedFilterQuery;
	private int initialQueryLength = initialFilterQuery.length();
	
	private String sortBy;
	private Boolean sortByAsc;
	private Collection<String> hierarchySorters;
	
	private Set<AmpCategoryValue> projectImplementingUnits; 
	
	/**
	 * why doesn't it have a setter like all normal fields do?
	 */
	private boolean budgetExport;
	
	private void queryAppend(String filter) {
		generatedFilterQuery += " AND amp_activity_id IN (" + filter + ")";
	}
	
	private void PledgequeryAppend(String filter) {
		generatedFilterQuery += " AND id IN (" + filter + ")";
	}
	
	/**
	 * fills the "grouping" subpart of the settings part of filters with defaults
	 * @return
	 */
	private void fillWithDefaultGroupingSettings()
	{
		DecimalFormat usedDecimalFormat = FormatHelper.getDecimalFormat();
		
		Character customDecimalSymbol	= usedDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
		Integer customDecimalPlaces	= usedDecimalFormat.getMaximumFractionDigits();
		Character customGroupSeparator	= usedDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator();
		Boolean customUseGrouping	= usedDecimalFormat.isGroupingUsed();
		Integer customGroupSize		= usedDecimalFormat.getGroupingSize();
		
		this.setGroupingseparator(customGroupSeparator.toString());
		this.setGroupingsize(customGroupSize);
		this.setCustomusegroupings(customUseGrouping);
		this.setMaximumFractionDigits(customDecimalPlaces);
		this.setDecimalseparator(customDecimalSymbol.toString());
		
		buildCustomFormat();
	}
	
	/**
	 * fills the "Report / Tab Settings" part of the instance with the default values
	 */
	public void fillWithDefaultsSettings()
	{
		fillWithDefaultGroupingSettings();
		this.setAmountinthousand(Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)));
		
		calendarType = null;
		AmpApplicationSettings settings = getEffectiveSettings();
		if (settings != null)
		{
			this.setCalendarType(settings.getFiscalCalendar());
			this.setCurrency(settings.getCurrency());
		}
		
		if (calendarType == null) // still no calendar source -> use Global Setting
		{
			String gvalue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
			if (gvalue != null){
				Long fiscalCalId = Long.parseLong(gvalue);
				setCalendarType(DbUtil.getFiscalCalendar(fiscalCalId));
			}
		}
		initRenderStartEndYears(settings);
	}
	
	/**
	 * computes the current user's effective AmpApplicationSettings, searching through the hierarchy
	 * returns null if there is no current user
	 * @return
	 */
	public static AmpApplicationSettings getEffectiveSettings()
	{
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		if (tm == null)
			return null;
		
		AmpApplicationSettings settings = null;
		if (tm.getMemberId() != null)
			settings = DbUtil.getMemberAppSettings(tm.getMemberId()); // member settings take precedence

		if (settings == null && tm.getTeamId() != null)
			settings = DbUtil.getTeamAppSettings(tm.getTeamId()); // use workspace settings if no member settings present
		
		return settings;
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
		this.setPlanMinRank(null);
		this.setText(null);
		this.setPageSize(null);
		this.setGovernmentApprovalProcedures(null);
		this.setJointCriteria(null);
		this.setJustSearch(false);
		this.setWorkspaceonly(false);
		this.setLocationSelected(null);
		this.setApprovalStatusSelected(null);
		this.setProjectImplementingUnits(null);
		this.setSortByAsc(true);
		this.setHierarchySorters(new ArrayList<String>());
		this.budgetExport = false;
		
		HttpServletRequest request = TLSUtils.getRequest();
		this.generatedFilterQuery = initialFilterQuery;
		this.setAmpReportId(ampReportId);
		
		AmpReports ampReport = ampReportId == null ? null : DbUtil.getAmpReport(ampReportId);
		if (ampReport != null)
		{			
			this.budgetExport	= ampReport.getBudgetExporter()==null ? false:ampReport.getBudgetExporter();				
			if (ampReport.getType() == ArConstants.PLEDGES_TYPE){
					this.generatedFilterQuery = initialPledgeFilterQuery;
			}
		}

		AmpApplicationSettings settings = getEffectiveSettings();
		
		TeamMember tm = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
		this.setAmpTeams(new TreeSet<AmpTeam>());		
		if (tm == null || tm.getTeamId() == null )
			tm	= null;

		if (tm != null) {
			this.setAccessType(tm.getTeamAccessType());
			this.setAmpTeams(TeamUtil.getRelatedTeamsForMember(tm));
			// set the computed workspace orgs
			//Set teamAO = TeamUtil.getComputedOrgs(this.getAmpTeams());
			Set teamAO = TeamUtil.getComputedOrgs(this.getAmpTeams());

			if (teamAO != null && teamAO.size() > 0)
				this.setTeamAssignedOrgs(teamAO);
		}
		else {
			//Check if the reportid is not nut for public mondrian reports
			if (ampReport != null)
			{
				//TreeSet allManagementTeams=(TreeSet) TeamUtil.getAllRelatedTeamsByAccessType("Management");
				TreeSet<AmpTeam> teams=new TreeSet<AmpTeam>();
				this.setAccessType("team");
				if (ampReport.getOwnerId()!=null){
					teams.add(ampReport.getOwnerId().getAmpTeam());
					teams.addAll(TeamUtil.getAmpLevel0Teams(ampReport.getOwnerId().getAmpTeam().getAmpTeamId()));
					this.setAmpTeams(teams);
					Set teamAO = TeamUtil.getComputedOrgs(this.getAmpTeams());
					if (teamAO != null && teamAO.size() > 0){
						this.setTeamAssignedOrgs(teamAO);
					}
				}else{
					((Set) teams).add(-1); //TODO:Constantin - waddafa?
					this.setAmpTeams(teams);
					logger.error("Error getOwnerId() is null setting team to -1");
				}
			}
		}
		
		//FormatHelper.tlocal.set(null); // somewhat ugly hack, as this shouldn't belong in here
	}
	
	private int getCalendarYear(AmpApplicationSettings settings, Integer settingsYear, String globalSettingsKey)
	{
		Long defaultCalendarId = null;
		
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
		
		//BOZO: strange conversion between calendars
		if (result == null)
			return -1;
		
		result = FiscalCalendarUtil.getYearOnCalendar(calendarType.getAmpFiscalCalId(), result, settings);
		//TODO:Constantin: used the ReportsFilterPickerForm-ported between-calendars conversion utilities (migrated by me to FiscalCalendarUtils) as the code looks more maintained. In case it is faulty, we could try our luck with the commented code below
		
//		if (result != null && result > 0 && calendarType != null && calendarType.getAmpFiscalCalId().equals(defaultCalendarId) ){
//			ICalendarWorker worker = calendarType.getworker();
//			try {
//				Date checkDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + result);
//				worker.setTime(checkDate);
//				result = worker.getYear();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
		return result == null ? -1 : result;
	}
	
	/**
	 * sets renderStartYear and renderEndYear according to Global, Workspace and User settings, translated by calendar
	 * @param settings
	 */
	private void initRenderStartEndYears(AmpApplicationSettings settings)
	{
		///Set the range depending of workspace setup / global setting and selected calendar
		Integer renderStartSettingsYear = (settings == null) ? null : settings.getReportStartYear();
		Integer renderendSettingsYear = (settings == null) ? null : settings.getReportEndYear();
		setRenderStartYear(getCalendarYear(settings, renderStartSettingsYear, org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE));
		setRenderEndYear(getCalendarYear(settings, renderendSettingsYear, org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));	
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

		if (ampReportId == null) {
			AmpReports ar = ReportContextData.getFromRequest().getReportMeta();
			if (ar != null){
				ampReportId = ar.getAmpReportId().toString();
			}
		}		
		Long ampReportIdLong = ampReportId == null ? null : Long.parseLong(ampReportId);
		return ampReportIdLong;
	}
	
	/**
	 * TODO-Constantin: non-trivially-slow function called at least 3 times per report render
	 * loads some default values on the filter based on request parameters, app defaults, workspace defaults etc
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

		String widget = (String) request.getAttribute("widget");
		if (widget != null)
			this.setWidget(new Boolean(widget));
	}

	/**
	 * builds the customFormat field based on the other fields in the instance
	 * contains some copy-paste from {@link #fillWithDefaultGroupingSettings()}, but too tired to abstractize somehow away
	 */
	public void buildCustomFormat()
	{
		DecimalFormat usedDecimalFormat = FormatHelper.getDecimalFormat();
		
		Character defaultDecimalSymbol	= usedDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
		Integer defaultDecimalPlaces	= usedDecimalFormat.getMaximumFractionDigits();
		Character defaultGroupSeparator	= usedDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator();
		Boolean defaultUseGrouping	= usedDecimalFormat.isGroupingUsed();
		Integer defaultGroupSize		= usedDecimalFormat.getGroupingSize();
		
		DecimalFormat custom = new DecimalFormat();
		DecimalFormatSymbols ds = new DecimalFormatSymbols();
		if (this.getDecimalseparator() != null){
			ds.setDecimalSeparator(getDecimalseparator().charAt(0));
		}else{
			ds.setDecimalSeparator(defaultDecimalSymbol);
		}
		
		if (this.getGroupingseparator() != null){
			ds.setGroupingSeparator(this.getGroupingseparator().charAt(0));
		}else{
			ds.setGroupingSeparator(defaultGroupSeparator);
		}
		
		if (this.getMaximumFractionDigits() != null && this.getMaximumFractionDigits() > -1)
			custom.setMaximumFractionDigits(this.getMaximumFractionDigits());
		else
			custom.setMaximumFractionDigits((defaultDecimalPlaces != -1) ? defaultDecimalPlaces : 99);
		
		custom.setGroupingUsed(this.getCustomusegroupings() == null ? defaultUseGrouping : this.getCustomusegroupings());
		custom.setGroupingSize(this.getGroupingsize() == null ? defaultGroupSize : this.getGroupingsize());
		custom.setDecimalFormatSymbols(ds);
		this.setCurrentFormat(custom);
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
	
	public AmpARFilter() {
		super();
		this.generatedFilterQuery = initialFilterQuery;
	}
	
	private String createDateCriteria(String to, String from, String sqlColumn) {
		String dateCriteria	 	= "";
		try {
			if ( (to != null && to.length() > 0)  ) {
				dateCriteria	= sqlColumn + " <= '" + sdfOut.format(sdfIn.parse(to)) + "'";
			}
			if ( (from != null && from.length() > 0)  ) {
				if ( dateCriteria.length() > 0 ) {
					dateCriteria += " AND ";
				}
				else
					dateCriteria	= "";
				
				dateCriteria	+= sqlColumn + " >= '" + sdfOut.format(sdfIn.parse(from)) + "'";
			}
		}
		catch (ParseException pe) {
			pe.printStackTrace();
		}
		
		return dateCriteria;
	}

	public void generateFilterQuery(HttpServletRequest request, boolean workspaceFilter) {
		if (request.getSession().getAttribute(ArConstants.PLEDGES_REPORT) != null && 
				request.getSession().getAttribute(ArConstants.PLEDGES_REPORT).toString().equalsIgnoreCase("true")){
			indexedParams=new ArrayList<FilterParam>();
			
			/*String WORKSPACE_ONLY="";
			if (this.workspaceonly){
					WORKSPACE_ONLY = "SELECT v.pledge_id FROM v_pledges_projects v WHERE amp_team_id IN ("
					+ Util.toCSString(ampTeams) + ")";
					PledgequeryAppend(WORKSPACE_ONLY);
			}*/
			
			String DONNOR_AGENCY_FILTER = " SELECT v.pledge_id FROM v_pledges_donor v  WHERE v.amp_donor_org_id IN ("
				+ Util.toCSString(donnorgAgency) + ")";
			
			String DONOR_TYPE_FILTER	= "SELECT v.id FROM v_pledges_donor_type v WHERE org_type_id IN ("
				+ Util.toCSString(donorTypes) + ")";

			String DONOR_GROUP_FILTER = "SELECT v.pledge_id FROM v_pledges_donor_group v WHERE amp_org_grp_id IN ("
					+ Util.toCSString(donorGroups) + ")";

			String FINANCING_INSTR_FILTER = "SELECT v.pledge_id FROM v_pledges_aid_modality v WHERE amp_modality_id IN ("
				+ Util.toCSString(financingInstruments) + ")";
			
			String TYPE_OF_ASSISTANCE_FILTER = "SELECT v.pledge_id FROM v_pledges_type_of_assistance v WHERE terms_assist_code IN ("
				+ Util.toCSString(typeOfAssistance) + ")";
			
			String SECTOR_FILTER = "SELECT pledge_id FROM amp_funding_pledges_sector ps, amp_sector s, amp_classification_config c "
				+ "WHERE ps.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
				+ "AND c.name='Primary' AND ps.amp_sector_id in ("
				+ Util.toCSString(sectors) + ")";
			
			if (donnorgAgency != null && donnorgAgency.size() > 0){
				PledgequeryAppend(DONNOR_AGENCY_FILTER);
			}
			
			if (donorGroups != null && donorGroups.size() > 0)
				PledgequeryAppend(DONOR_GROUP_FILTER);
			
			if (donorTypes != null && donorTypes.size() > 0)
				PledgequeryAppend(DONOR_TYPE_FILTER);
			
			if (financingInstruments != null && financingInstruments.size() > 0){
				PledgequeryAppend(FINANCING_INSTR_FILTER);
			}
			if (typeOfAssistance != null && typeOfAssistance.size() > 0){
				PledgequeryAppend(TYPE_OF_ASSISTANCE_FILTER);
			}
		
		
			return;
		}
		
		indexedParams=new ArrayList<FilterParam>();
		
		String BUDGET_FILTER = "SELECT amp_activity_id FROM v_on_off_budget WHERE budget_id IN ("
			+ Util.toCSString(budget) + ")";
		String TEAM_FILTER = "";

		//new computed filter - after permissions #3167
		//AMP-3726
		Set<String> activityStatus = new HashSet<String>();
		activityStatus.add(Constants.APPROVED_STATUS);
		activityStatus.add(Constants.EDITED_STATUS);
		activityStatus.add(Constants.STARTED_APPROVED_STATUS);
//		activityStatus.add(Constants.STARTED_STATUS);
		String NO_MANAGEMENT_ACTIVITIES="";
		if("Management".equals(this.getAccessType()))
			TEAM_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE approval_status IN ("+Util.toCSString(activityStatus)+") AND draft<>true AND " +
					"amp_team_id IS NOT NULL AND amp_team_id IN ("
				+ Util.toCSString(ampTeams)
				+ ") ";
				// + " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
				//+ Util.toCSString(ampTeams) + ") ) AND draft<>true "; 
		else{
			
			TEAM_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IS NOT NULL AND amp_team_id IN ("
				+ Util.toCSString(ampTeams)
				+ ") ";
				//+ " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
				//+ Util.toCSString(ampTeams) + ") )" ;
		}
		NO_MANAGEMENT_ACTIVITIES +="SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IS NOT NULL AND amp_team_id IN ("
			+ Util.toCSString(ampTeams)
			+ ") ";
			//+ " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
			//+ Util.toCSString(ampTeams) + ") )" ;
			

	// computed workspace filter -- append it to the team filter so normal
	// team activities are also possible
		//AMP-4495 - in computed workspace, the unapproved or draft activities from other
		//worskpaces should not be displayed
			if (teamAssignedOrgs != null && teamAssignedOrgs.size() > 0) {
				
				TEAM_FILTER += " OR amp_activity_id IN (SELECT DISTINCT(aor.activity) FROM amp_org_role aor, amp_activity a WHERE aor.organisation IN ("
						+ Util.toCSString(teamAssignedOrgs) + ") AND aor.activity=a.amp_activity_id AND a.amp_team_id IS NOT NULL AND a.approval_status IN (" +
						Util.toCSString(activityStatus)	+")  ) " + (draft?"AND draft<>true ":"");
				TEAM_FILTER += " OR amp_activity_id IN (SELECT distinct(af.amp_activity_id) FROM amp_funding af, amp_activity b WHERE af.amp_donor_org_id IN ("
						+ Util.toCSString(teamAssignedOrgs) + ") AND af.amp_activity_id=b.amp_activity_id AND b.amp_team_id IS NOT NULL AND b.approval_status IN (" +
						Util.toCSString(activityStatus)	+")  ) " + (draft?"AND draft<>true ":"");
//				TEAM_FILTER += " OR amp_activity_id IN (SELECT DISTINCT(aor.activity) FROM amp_org_role aor, amp_activity a WHERE aor.organisation IN ("
//					+ Util.toCSString(teamAssignedOrgs) + ") AND aor.activity=a.amp_activity_id AND a.amp_team_id IS NOT NULL )";
//				TEAM_FILTER += " OR amp_activity_id IN (SELECT distinct(af.amp_activity_id) FROM amp_funding af, amp_activity b WHERE af.amp_donor_org_id IN ("
//					+ Util.toCSString(teamAssignedOrgs) + ") AND af.amp_activity_id=b.amp_activity_id AND b.amp_team_id IS NOT NULL )";

				
				NO_MANAGEMENT_ACTIVITIES += " OR amp_activity_id IN (SELECT DISTINCT(aor.activity) FROM amp_org_role aor, amp_activity a WHERE aor.organisation IN ("
					+ Util.toCSString(teamAssignedOrgs) + ") AND aor.activity=a.amp_activity_id AND a.amp_team_id IS NOT NULL )";
				NO_MANAGEMENT_ACTIVITIES +=" OR amp_activity_id IN (SELECT distinct(af.amp_activity_id) FROM amp_funding af, amp_activity b WHERE af.amp_donor_org_id IN ("
					+ Util.toCSString(teamAssignedOrgs) + ") AND af.amp_activity_id=b.amp_activity_id AND b.amp_team_id IS NOT NULL )";
		
			}
			
			if (!workspaceFilter){
				//Merge Filter with the Workspace Filter
				AmpARFilter teamFilter = (AmpARFilter) request.getSession().getAttribute(ArConstants.TEAM_FILTER);
				if (teamFilter != null){
					TEAM_FILTER += " OR " + teamFilter.getFilterConditionOnly();
				}
			}
//		int c;
//		if (!workspaceFilter){
//			if(draft){
//				c= Math.abs( DbUtil.countActivitiesByQuery(TEAM_FILTER + " AND amp_activity_id IN (SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft is false ) )",null )-DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,null));
//			}
//			else c= Math.abs( DbUtil.countActivitiesByQuery(TEAM_FILTER,null) - DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,null) );
//			this.setActivitiesRejectedByFilter(new Long(c));
//			request.getSession().setAttribute("activitiesRejected",this.getActivitiesRejectedByFilter());
//		}

		String STATUS_FILTER = "SELECT amp_activity_id FROM v_status WHERE amp_status_id IN ("
				+ Util.toCSString(statuses) + ")";

		String WORKSPACE_FILTER = "select amp_activity_id from amp_activity where amp_team_id IN ("
			+ Util.toCSString(workspaces) + ")";

		// String ORG_FILTER = "SELECT amp_activity_id FROM v_donor_groups WHERE
		// amp_org_grp_id IN ("+Util.toCSString(donors,true)+")";
		// String PARENT_SECTOR_FILTER="SELECT amp_activity_id FROM v_sectors
		// WHERE amp_sector_id IN ("+Util.toCSString(sectors,true)+")";
		// String SUB_SECTOR_FILTER="SELECT amp_activity_id FROM v_sub_sectors
		// WHERE amp_sector_id IN ("+Util.toCSString(sectors,true)+")";
		// String SECTOR_FILTER="(("+PARENT_SECTOR_FILTER+") UNION
		// ("+SUB_SECTOR_FILTER+"))";

		String SECTOR_FILTER = "SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
				+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
				+ "AND c.name='Primary' AND aas.amp_sector_id in ("
				+ Util.toCSString(sectors) + ")";

		String NATIONAL_PLAN_FILTER = "SELECT aap.amp_activity_id FROM amp_activity_program aap inner join  amp_theme p on aap.amp_program_id=p.amp_theme_id "
				+ "inner join  AMP_PROGRAM_SETTINGS ps on ps.amp_program_settings_id=aap.program_setting where ps.name='National Plan Objective' AND "
				+ " aap.amp_program_id in ("
				+ Util.toCSString(nationalPlanningObjectives) + ")";

		String PRIMARY_PROGRAM_FILTER = "SELECT aap.amp_activity_id FROM amp_activity_program aap inner join  amp_theme p on aap.amp_program_id=p.amp_theme_id "
				+ "inner join  AMP_PROGRAM_SETTINGS ps on ps.amp_program_settings_id=aap.program_setting where ps.name='Primary Program' AND "
				+ " aap.amp_program_id in ("
				+ Util.toCSString(primaryPrograms) + ")";

		String SECONDARY_PROGRAM_FILTER = "SELECT aap.amp_activity_id FROM amp_activity_program aap inner join  amp_theme p on aap.amp_program_id=p.amp_theme_id "
				+ "inner join  AMP_PROGRAM_SETTINGS ps on ps.amp_program_settings_id=aap.program_setting where ps.name='Secondary Program' AND "
				+ " aap.amp_program_id in ("
				+ Util.toCSString(secondaryPrograms) + ")";

		// String SECONDARY_PARENT_SECTOR_FILTER=
		// "SELECT amp_activity_id FROM v_secondary_sectors WHERE amp_sector_id
		// IN ("+Util.toCSString(secondarySectors,true)+")";
		// String SECONDARY_SUB_SECTOR_FILTER=
		// "SELECT amp_activity_id FROM v_secondary_sub_sectors WHERE
		// amp_sector_id IN ("+Util.toCSString(secondarySectors,true)+")";
		// String SECONDARY_SECTOR_FILTER="(("+SECONDARY_PARENT_SECTOR_FILTER+")
		// UNION ("+SECONDARY_SUB_SECTOR_FILTER+"))";
		String SECONDARY_SECTOR_FILTER = "SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
				+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
				+ "AND c.name='Secondary' AND aas.amp_sector_id in ("
				+ Util.toCSString(secondarySectors) + ")";

       String TERTIARY_SECTOR_FILTER = "SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
				+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
				+ "AND c.name='Tertiary' AND aas.amp_sector_id in ("
				+ Util.toCSString(tertiarySectors) + ")";
       
       String TAG_SECTOR_FILTER = "SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c "
				+ "WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id "
				+ "AND c.name='Tag' AND aas.amp_sector_id in ("
				+ Util.toCSString(tagSectors) + ")";


		String REGION_FILTER = "SELECT amp_activity_id FROM v_regions WHERE name IN ("
				+ Util.toCSString(regions) + ")";
		String FINANCING_INSTR_FILTER = "SELECT amp_activity_id FROM v_financing_instrument WHERE amp_modality_id IN ("
				+ Util.toCSString(financingInstruments) + ")";
		String LINE_MIN_RANK_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE line_min_rank IN ("
	 	 		+ Util.toCSString(lineMinRank) + ")";
	 	String PLAN_MIN_RANK_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE plan_min_rank IN ("
	 	 		+ Util.toCSString( planMinRank ) + ")";
		//String REGION_SELECTED_FILTER = "SELECT amp_activity_id FROM v_regions WHERE region_id ="
		//		+ (regionSelected==null?null:regionSelected.getIdentifier());
	 	
	 	String MULTI_DONOR		= "SELECT amp_activity_id FROM v_multi_donor WHERE value = '" + multiDonor + "'";
	 	
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
		
		if (locationSelected!=null) {
			Set<AmpCategoryValueLocations> allSelectedLocations = new HashSet<AmpCategoryValueLocations>();
			allSelectedLocations.addAll(locationSelected);
			
			DynLocationManagerUtil.populateWithDescendants(allSelectedLocations, locationSelected);
			this.relatedLocations						= new ArrayList<AmpCategoryValueLocations>();
			this.relatedLocations.addAll(allSelectedLocations);
			DynLocationManagerUtil.populateWithAscendants(this.relatedLocations, locationSelected);
			
			String allSelectedLocationString			= Util.toCSString(allSelectedLocations);
			String subSelect			= "SELECT aal.amp_activity_id FROM amp_activity_location aal, amp_location al " +
					"WHERE ( aal.amp_location_id=al.amp_location_id AND " +
					"al.location_id IN (" + allSelectedLocationString + ") )";
			
			if (REGION_SELECTED_FILTER.equals("")) {
				REGION_SELECTED_FILTER	= subSelect;
			} else {
				REGION_SELECTED_FILTER += " OR amp_activity_id IN (" + subSelect + ")"; 
			}			
		}
		
		StringBuffer actStatusValue = new StringBuffer("");
		if(approvalStatusSelected!=null){
			for(String valOption:approvalStatusSelected){
				switch (Integer.parseInt(valOption)) {
				case -1:
					actStatusValue.append("1=1 ");					
					break;
				case 0://Existing Un-validated - This will show all the activities that have been approved at least once and have since been edited and not validated.
					actStatusValue.append(" (approval_status='edited' and draft <> true)");break;
				case 1://New Draft - This will show all the activities that have never been approved and are saved as drafts.
					actStatusValue.append(" (approval_status in ('started', 'startedapproved') and draft is true) ");break;
				case 2://New Un-validated - This will show all activities that are new and have never been approved by the workspace manager.
					actStatusValue.append(" (approval_status='started' and draft <> true)");break;
				case 3://existing draft. This is because when you filter by Existing Unvalidated you get draft activites that were edited and saved as draft
					actStatusValue.append(" ( (approval_status='edited' or approval_status='approved') and draft is true ) ");break;
				case 4://Validated Activities 
					actStatusValue.append("(approval_status='approved' and draft<>true)");break;
				default:actStatusValue.append("1=1 ");	break;
				}
				actStatusValue.append(" OR ");
			}
		    int posi = actStatusValue.lastIndexOf("OR");
		    if(posi>0)
		    	actStatusValue.delete(posi, posi+2);
		}    
		String ACTIVITY_STATUS="select amp_activity_id from amp_activity where "+actStatusValue.toString();
		String APPROVED_FILTER = "";
			if("Management".equals(this.getAccessType()))
				APPROVED_FILTER="SELECT amp_activity_id FROM amp_activity WHERE approval_status IN ("
					+ Util.toCSString(activityStatus) + ")";
			else APPROVED_FILTER="SELECT amp_activity_id FROM amp_activity WHERE approval_status IN ('"
				+ Constants.APPROVED_STATUS + "','"+ Constants.STARTED_APPROVED_STATUS +"')";
		
		String DRAFT_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft is false )";
		String TYPE_OF_ASSISTANCE_FILTER = "SELECT amp_activity_id FROM v_terms_assist WHERE terms_assist_code IN ("
			+ Util.toCSString(typeOfAssistance) + ")";

		String MODE_OF_PAYMENT_FILTER = "SELECT amp_activity_id FROM v_mode_of_payment WHERE mode_of_payment_code IN ("
			+ Util.toCSString(modeOfPayment) + ")";

		String PROJECT_CATEGORY_FILTER = "SELECT amp_activity_id FROM v_project_category WHERE amp_category_id IN ("
			+ Util.toCSString(projectCategory) + ")";

		String PROJECT_IMPL_UNIT_FILTER = "SELECT amp_activity_id FROM v_project_impl_unit WHERE proj_impl_unit_id IN ("
			+ Util.toCSString(projectImplementingUnits) + ")";

//		String DONOR_TYPE_FILTER = "SELECT aa.amp_activity_id "
//				+ "FROM amp_activity aa, amp_org_role aor, amp_role rol, amp_org_type typ, amp_organisation og  "
//				+ "WHERE aa.amp_activity_id = aor.activity AND aor.role = rol.amp_role_id AND rol.role_code='DN' "
//				+ "AND typ.amp_org_type_id =  og.org_type_id AND og.amp_org_id = aor.organisation "
//				+ "AND typ.amp_org_type_id IN ("
//				+ Util.toCSString(donorTypes) + ")";
		
		String DONOR_TYPE_FILTER	= "SELECT amp_activity_id FROM v_donor_type WHERE org_type_id IN ("
			+ Util.toCSString(donorTypes) + ")";

		String DONOR_GROUP_FILTER = "SELECT amp_activity_id FROM v_donor_groups WHERE amp_org_grp_id IN ("
				+ Util.toCSString(donorGroups) + ")";

		String CONTRACTING_AGENCY_GROUP_FILTER = "SELECT amp_activity_id FROM v_contracting_agency_groups WHERE amp_org_grp_id IN ("
				+ Util.toCSString(contractingAgencyGroups) + ")";
				
		String EXECUTING_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_executing_agency v  "
				+ "WHERE v.amp_org_id IN ("
				+ Util.toCSString(executingAgency) + ")";
		
		String CONTRACTING_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_contracting_agency v  "
				+ "WHERE v.amp_org_id IN ("
				+ Util.toCSString(contractingAgency) + ")";

		
		String BENEFICIARY_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_beneficiary_agency v  "
				+ "WHERE v.amp_org_id IN ("
				+ Util.toCSString(beneficiaryAgency) + ")";
		
		String IMPLEMENTING_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_implementing_agency v  "
				+ "WHERE v.amp_org_id IN ("
				+ Util.toCSString(implementingAgency) + ")";
		
		String RESPONSIBLE_ORGANIZATION_FILTER = " SELECT v.amp_activity_id FROM v_responsible_organisation v  WHERE v.amp_org_id IN ("
			+ Util.toCSString(responsibleorg) + ")";

		String DONNOR_AGENCY_FILTER = " SELECT v.amp_activity_id FROM v_donors v  WHERE v.amp_donor_org_id IN ("
			+ Util.toCSString(donnorgAgency) + ")";
		String ARCHIVED_FILTER          = "";
		if (this.showArchived != null)
			ARCHIVED_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE "
					+ ((this.showArchived) ? "archived=true"
							: "(archived=false or archived is null)");

		boolean dateFilterHidesProjects = "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DATE_FILTER_HIDES_PROJECTS));

		if(dateFilterHidesProjects && fromDate!=null && fromDate.length()>0) {
			String FROM_DATE_FILTER=null;
			try {
				FROM_DATE_FILTER = " SELECT distinct(f.amp_activity_id) FROM amp_funding_detail fd, amp_funding f WHERE f.amp_funding_id=fd.amp_funding_id AND fd.transaction_date>='"
					+ sdfOut.format(sdfIn.parse(fromDate)) + "'";

			} catch (ParseException e) {
				logger.error(e);
				e.printStackTrace();
			}
			dateFilterUsed = true;
			queryAppend(FROM_DATE_FILTER);
		}
		if(dateFilterHidesProjects && toDate!=null && toDate.length()>0) {
			String TO_DATE_FILTER=null;
			try {
				TO_DATE_FILTER = " SELECT distinct(f.amp_activity_id) FROM amp_funding_detail fd, amp_funding f WHERE f.amp_funding_id=fd.amp_funding_id AND fd.transaction_date<='"
					+  sdfOut.format(sdfIn.parse(toDate)) + "'";
			} catch (ParseException e) {
				logger.error(e);
				e.printStackTrace();
			}
			dateFilterUsed = true;
			queryAppend(TO_DATE_FILTER);
		}
		
		String ACTIVITY_START_DATE_FILTER	 	= this.createDateCriteria(toActivityStartDate, fromActivityStartDate, "asd.actual_start_date");
		if ( ACTIVITY_START_DATE_FILTER.length() > 0 ) {
			ACTIVITY_START_DATE_FILTER = "SELECT asd.amp_activity_id from v_actual_start_date asd WHERE " + ACTIVITY_START_DATE_FILTER;
			queryAppend(ACTIVITY_START_DATE_FILTER);
		}
		
		String ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER	 	= this.createDateCriteria(toActivityActualCompletionDate, fromActivityActualCompletionDate, "acd.actual_completion_date");
		if ( ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER.length() > 0 ) {
			ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER = "SELECT acd.amp_activity_id from v_actual_completion_date acd WHERE " + ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER;
			queryAppend(ACTIVITY_ACTUAL_COMPLETION_DATE_FILTER);
		}
		
		String ACTIVITY_FINAL_CONTRACTING_DATE_FILTER	 	= this.createDateCriteria(toActivityFinalContractingDate, fromActivityFinalContractingDate, "ctrd.contracting_date");
		if ( ACTIVITY_FINAL_CONTRACTING_DATE_FILTER.length() > 0 ) {
			ACTIVITY_FINAL_CONTRACTING_DATE_FILTER = "SELECT ctrd.amp_activity_id from v_contracting_date ctrd WHERE " + ACTIVITY_FINAL_CONTRACTING_DATE_FILTER;
			queryAppend(ACTIVITY_FINAL_CONTRACTING_DATE_FILTER);
		}
		
		/*
		 * if(fromYear!=null) { AmpARFilterHelper filterHelper =
		 * Logic.getInstance().getAmpARFilterHelper(); String
		 * FROM_FUNDING_YEAR_FILTER =
		 * filterHelper.createFromYearQuery(fromYear);
		 * queryAppend(FROM_FUNDING_YEAR_FILTER); }
		 * 
		 * if(toYear!=null) { AmpARFilterHelper filterHelper =
		 * Logic.getInstance().getAmpARFilterHelper(); String
		 * TO_FUNDING_YEAR_FILTER = filterHelper.createToYearQuery(toYear);
		 * queryAppend(TO_FUNDING_YEAR_FILTER); }
		 * 
		 * if (fromMonth!=null) { AmpARFilterHelper filterHelper =
		 * Logic.getInstance().getAmpARFilterHelper(); String FROM_MONTH_FILTER =
		 * filterHelper.createFromMonthQuery(fromMonth);
		 * queryAppend(FROM_MONTH_FILTER); }
		 * 
		 * if (toMonth!=null) { AmpARFilterHelper filterHelper =
		 * Logic.getInstance().getAmpARFilterHelper(); String TO_MONTH_FILTER =
		 * filterHelper.createToMonthQuery(toMonth);
		 * queryAppend(TO_MONTH_FILTER); }
		 */

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

		
		/*
		if (fromDate != null) 
			if (fromDate.trim().length() > 0){
				String FROM_FUNDING_DATE_FILTER = "SELECT DISTINCT(f.amp_activity_id) FROM amp_funding f, amp_funding_detail fd "
					+ "WHERE f.amp_funding_id=fd.amp_funding_id AND DATEDIFF(fd.transaction_date,?) >= 0";
				queryAppend(FROM_FUNDING_DATE_FILTER);
				//add to the params list that will be used on the prepared statment
				indexedParams.add(new FilterParam(new java.sql.Date(FormatHelper.parseDate2(this.getFromDate()).getTime()),java.sql.Types.DATE));
			}
		if (toDate != null)
			if (toDate.trim().length() > 0){
				String TO_FUNDING_DATE_FILTER = "SELECT DISTINCT(f.amp_activity_id) FROM amp_funding f, amp_funding_detail fd "
					+ "WHERE f.amp_funding_id=fd.amp_funding_id AND DATEDIFF(?, fd.transaction_date) >= 0";
				queryAppend(TO_FUNDING_DATE_FILTER);
				//add to the params list that will be used on the prepared statment
				indexedParams.add(new FilterParam(new java.sql.Date(FormatHelper.parseDate2(this.getToDate()).getTime()),java.sql.Types.DATE));
				
			}
			*/
		
		/*
		 * if (fromYear==null) fromYear = 0;
		 * 
		 * if (fromMonth==null) fromMonth = 1;
		 * 
		 * if (toYear==null) toYear = 9999;
		 * 
		 * if (toMonth==null) toMonth = 12;
		 * 
		 * AmpARFilterHelper filterHelper =
		 * Logic.getInstance().getAmpARFilterHelper(); String MONTH_YEAR_FILTER =
		 * filterHelper.createMonthYearQuery(fromMonth, fromYear, toMonth,
		 * toYear); queryAppend(MONTH_YEAR_FILTER);
		 */
		if (text != null) {
			if ("".equals(text.trim()) == false) {
				String TEXT_FILTER = "SELECT a.amp_activity_id from amp_activity a WHERE a.amp_id="
						+ text;
				queryAppend(TEXT_FILTER);
			}
		}

		if (indexText != null)
			if ("".equals(indexText.trim()) == false) {
				String LUCENE_ID_LIST = "";
				HttpSession session = request.getSession();
				ServletContext ampContext = session.getServletContext();
//				Directory idx = (Directory) ampContext
//						.getAttribute(Constants.LUCENE_INDEX);
				if(request.getParameter("searchMode") != null)
					searchMode = request.getParameter("searchMode");
				Hits hits = LuceneUtil.search(ampContext.getRealPath("/") + LuceneUtil.ACTVITY_INDEX_DIRECTORY, "all", indexText, searchMode);
				logger.info("New lucene search !");
				if(hits!=null)
				for (int i = 0; i < hits.length(); i++) {
					Document doc;
					try {
						doc = hits.doc(i);
						if (LUCENE_ID_LIST == "")
							LUCENE_ID_LIST = doc.get("id");
						else
							LUCENE_ID_LIST = LUCENE_ID_LIST + ","
									+ doc.get("id");
						// AmpActivity act =
						// ActivityUtil.getAmpActivity(Long.parseLong(doc.get("id")));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
		if (!workspaceFilter &&  !this.budgetExport && ampTeams != null && ampTeams.size() > 0)
			queryAppend(TEAM_FILTER);
		if (statuses != null && statuses.size() > 0)
			queryAppend(STATUS_FILTER);
		if (workspaces != null && workspaces.size() > 0)
			queryAppend(WORKSPACE_FILTER);
		// if(donors!=null && donors.size()>0) queryAppend(ORG_FILTER);
		if (sectors != null && sectors.size() != 0) {
			queryAppend(SECTOR_FILTER);
		}
		if (secondarySectors != null && secondarySectors.size() != 0) {
			queryAppend(SECONDARY_SECTOR_FILTER);
		}
        if (tertiarySectors != null && !tertiarySectors.isEmpty()) {
			queryAppend(TERTIARY_SECTOR_FILTER);
		}
        if (tagSectors != null && tagSectors.size() > 0 ) {
        	queryAppend(TAG_SECTOR_FILTER);
        }
		if (nationalPlanningObjectives != null
				&& nationalPlanningObjectives.size() != 0) {
			queryAppend(NATIONAL_PLAN_FILTER);
		}
		if (primaryPrograms != null && primaryPrograms.size() != 0) {
			queryAppend(PRIMARY_PROGRAM_FILTER);
		}
		if (secondaryPrograms != null && secondaryPrograms.size() != 0) {
			queryAppend(SECONDARY_PROGRAM_FILTER);
		}
		if (regions != null && regions.size() > 0)
			queryAppend(REGION_FILTER);
		if (financingInstruments != null && financingInstruments.size() > 0)
			queryAppend(FINANCING_INSTR_FILTER);
		if (risks != null && risks.size() > 0)
			queryAppend(RISK_FILTER);
		if ((lineMinRank != null) && (lineMinRank.size() > 0))
			queryAppend(LINE_MIN_RANK_FILTER);
		if ((planMinRank != null)&&(planMinRank.size() > 0))
			queryAppend(PLAN_MIN_RANK_FILTER);
		//if (regionSelected != null)
		//	queryAppend(REGION_SELECTED_FILTER);
		if (!REGION_SELECTED_FILTER.equals("")) {
			queryAppend(REGION_SELECTED_FILTER);
		}
		if(approvalStatusSelected!=null)
			queryAppend(ACTIVITY_STATUS);
		if (approved == true)
			queryAppend(APPROVED_FILTER);
		if (draft == true)
			queryAppend(DRAFT_FILTER);
		if (typeOfAssistance != null && typeOfAssistance.size() > 0)
			queryAppend(TYPE_OF_ASSISTANCE_FILTER);
		if (modeOfPayment != null && modeOfPayment.size() > 0)
			queryAppend(MODE_OF_PAYMENT_FILTER);
		if (projectCategory != null && projectCategory.size() > 0)
			queryAppend(PROJECT_CATEGORY_FILTER);
		
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

		if ( this.isPublicView() && ! this.budgetExport  ){
			generatedFilterQuery = getOffLineQuery(generatedFilterQuery);
		}

//		DbUtil.countActivitiesByQuery(this.generatedFilterQuery,indexedParams); //BOZO: why is this query run, if its output is ignored?
		logger.info(this.generatedFilterQuery);
		
//		if (!workspaceFilter){
//			if(draft){
//				c= Math.abs( DbUtil.countActivitiesByQuery(this.generatedFilterQuery + " AND amp_activity_id IN (SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft is false) )",indexedParams )-DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,indexedParams) );
//			}
//			else c= Math.abs( DbUtil.countActivitiesByQuery(this.generatedFilterQuery,indexedParams)-DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,null) );
//			this.setActivitiesRejectedByFilter(new Long(c));
//			request.getSession().setAttribute("activitiesRejected",this.getActivitiesRejectedByFilter());
//		}
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
	 * @return Returns the ampTeams.
	 */
	@PropertyListableIgnore
	public Set<AmpTeam> getAmpTeams() {
		return ampTeams;
	}

	/**
	 * @param ampTeams
	 *            The ampTeams to set.
	 */
	public void setAmpTeams(Set<AmpTeam> ampTeams) {
		this.ampTeams = ampTeams;
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
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				Method m = propertyDescriptors[i].getReadMethod();
				Object object = m.invoke(this, new Object[] {});
				if (object == null
						|| IGNORED_PROPERTIES.contains(propertyDescriptors[i]
								.getName()))
					continue;
				ret.append("<b>").append(propertyDescriptors[i].getName())
						.append(": ").append("</b>");
				if (object instanceof Collection)
					ret.append(Util.toCSString((Collection) object));

				else
					ret.append(object);
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

	private static final String IGNORED_PROPERTIES = "class#generatedFilterQuery#initialQueryLength#widget#publicView#ampReportId";

	public Collection<Integer> getLineMinRank() {
		return lineMinRank;
	}

	public void setLineMinRank(Collection<Integer> lineMinRank) {
		this.lineMinRank = lineMinRank;
	}

	public Collection<Integer> getPlanMinRank() {
		return planMinRank;
	}

	public void setPlanMinRank(Collection<Integer> planMinRank) {
		this.planMinRank = planMinRank;
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

//	/**
//	 * @return the regionSelected
//	 */
//	public AmpCategoryValueLocations getRegionSelected() {
//		return regionSelected;
//	}
//
//	/**
//	 * @param regionSelected the regionSelected to set
//	 */
//	public void setRegionSelected(AmpCategoryValueLocations regionSelected) {
//		this.regionSelected = regionSelected;
//	}

	public String getIndexText() {
		return indexText;
	}

	@PropertyListableIgnore
	public boolean isApproved() {
		return approved;
	}

	public void setIndexText(String indexText) {
		this.indexText = indexText;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	@IgnorePersistence
	public boolean isDraft() {
		return draft;
	}
	
	/**
	 * TODO draft parameter needs to be renamed to hideDraft 
	 * @param draft
	 */
	public void setDraft(boolean draft) {
		this.draft = draft;
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

	@IgnorePersistence
	public Set getTeamAssignedOrgs() {
		return teamAssignedOrgs;
	}

	public void setTeamAssignedOrgs(Set teamAssignedOrgs) {
		this.teamAssignedOrgs = teamAssignedOrgs;
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

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	

	/**
	 * @return the fromActivityStartDate
	 */
	public String getFromActivityStartDate() {
		return fromActivityStartDate;
	}

	/**
	 * @param fromActivityStartDate the fromActivityStartDate to set
	 */
	public void setFromActivityStartDate(String fromActivityStartDate) {
		this.fromActivityStartDate = fromActivityStartDate;
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
	

	/**
	 * @return the fromActivityFinalContractingDate
	 */
	public String getFromActivityFinalContractingDate() {
		return fromActivityFinalContractingDate;
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

//	@IgnorePersistence
//	public Long getActivitiesRejectedByFilter() {
//		return activitiesRejectedByFilter;
//	}
//
//	public void setActivitiesRejectedByFilter(Long activitiesRejectedByFilter) {
//		this.activitiesRejectedByFilter = activitiesRejectedByFilter;
//	}

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

	public Collection<String> getHierarchySorters() {
		return hierarchySorters;
	}

	public void setHierarchySorters(Collection<String> hierarchySorters) {
		this.hierarchySorters = hierarchySorters;
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
	public String getOffLineQuery(String query) {
		String result = query;
		Pattern p = Pattern.compile(MoConstants.AMP_ACTIVITY_TABLE);
		Matcher m = p.matcher(result);
		result = m.replaceAll(MoConstants.CACHED_ACTIVITY_TABLE);
		return result;
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

	public Set<AmpTeam> getAmpTeamsforpledges() {
		return ampTeamsforpledges;
	}

	public void setAmpTeamsforpledges(Set<AmpTeam> ampTeamsforpledges) {
		this.ampTeamsforpledges = ampTeamsforpledges;
	}

	

}
