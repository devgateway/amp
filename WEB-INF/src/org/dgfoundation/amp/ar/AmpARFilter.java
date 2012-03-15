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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.store.Directory;
import org.dgfoundation.amp.PropertyListable;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.PropertyListable.PropertyListableIgnore;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.annotations.reports.IgnorePersistence;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
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
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.TeamUtil;
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
	private static SimpleDateFormat sdfOut=new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfIn=new SimpleDateFormat("dd/MM/yyyy");
	protected static Logger logger = Logger.getLogger(AmpARFilter.class);
	private Long id;
	private boolean justSearch=false;
	private Long ampReportId;
	private Set statuses = null;
	// private Set donors=null; //not used anymore
	@PropertyListableIgnore
	private Set sectors = null;
	@PropertyListableIgnore
	private Set sectorsAndAncestors	= null;
	private Set selectedSectors = null;
	
	private String CRISNumber;
	private String budgetNumber;
	
	@PropertyListableIgnore
	private Integer computedYear;
	
	@PropertyListableIgnore
	private Integer actualAppYear;
	
	@PropertyListableIgnore
	private ArrayList<FilterParam> indexedParams=null;

	
	@PropertyListableIgnore
	private Long activitiesRejectedByFilter;
	
	@PropertyListableIgnore
	private Set secondarySectors = null;
	private Set selectedSecondarySectors = null;
	@PropertyListableIgnore
	private Set secondarySectorsAndAncestors = null;
    @PropertyListableIgnore
	private Set tertiarySectors = null;
	private Set selectedTertiarySectors = null;
    @PropertyListableIgnore
    private Set tertiarySectorsAndAncestors = null;


	@PropertyListableIgnore
	private Set relatedSecondaryProgs;

	@PropertyListableIgnore
	private List nationalPlanningObjectives;
	private Set selectedNatPlanObj;
	@PropertyListableIgnore
	private Set relatedNatPlanObjs;

	@PropertyListableIgnore
	private String teamAccessType;
	
	@PropertyListableIgnore
	private List primaryPrograms;
	private Set selectedPrimaryPrograms;
	@PropertyListableIgnore
	private Set relatedPrimaryProgs;

	@PropertyListableIgnore
	private List secondaryPrograms;
	private Set selectedSecondaryPrograms;

	@PropertyListableIgnore
	public List getNationalPlanningObjectives() {
		return nationalPlanningObjectives;
	}

	public void setNationalPlanningObjectives(List nationalPlanningObjectives) {
		this.nationalPlanningObjectives = nationalPlanningObjectives;
	}
	
	

	@PropertyListableIgnore
	public Set getRelatedNatPlanObjs() {
		return relatedNatPlanObjs;
	}

	public void setRelatedNatPlanObjs(Set relatedNatPlanObjs) {
		this.relatedNatPlanObjs = relatedNatPlanObjs;
	}
	@PropertyListableIgnore
	public Set getRelatedSecondaryProgs() {
		return relatedSecondaryProgs;
	}

	public void setRelatedSecondaryProgs(Set relatedSecondaryProgs) {
		this.relatedSecondaryProgs = relatedSecondaryProgs;
	}

	@PropertyListableIgnore
	public Set getRelatedPrimaryProgs() {
		return relatedPrimaryProgs;
	}

	public void setRelatedPrimaryProgs(Set relatedPrimaryProgs) {
		this.relatedPrimaryProgs = relatedPrimaryProgs;
	}

	@PropertyListableIgnore
	public List getPrimaryPrograms() {
		return primaryPrograms;
	}

	public void setPrimaryPrograms(List primaryPrograms) {
		this.primaryPrograms = primaryPrograms;
	}

	@PropertyListableIgnore
	public List getSecondaryPrograms() {
		return secondaryPrograms;
	}

	public void setSecondaryPrograms(List secondaryPrograms) {
		this.secondaryPrograms = secondaryPrograms;
	}

	public Set getSelectedNatPlanObj() {
		return selectedNatPlanObj;
	}

	public void setSelectedNatPlanObj(Set selectedNatPlanObj) {
		this.selectedNatPlanObj = selectedNatPlanObj;
	}

	public Set getSelectedPrimaryPrograms() {
		return selectedPrimaryPrograms;
	}

	public void setSelectedPrimaryPrograms(Set selectedPrimaryPrograms) {
		this.selectedPrimaryPrograms = selectedPrimaryPrograms;
	}

	public Set getSelectedSecondaryPrograms() {
		return selectedSecondaryPrograms;
	}

	public void setSelectedSecondaryPrograms(Set selectedSecondaryPrograms) {
		this.selectedSecondaryPrograms = selectedSecondaryPrograms;
	}

	private Set regions = null;
	private Set risks = null;
	private Set donorTypes = null;
	private Set donorGroups = null;
	private Set responsibleorg = null;
	private Set executingAgency;
	private Set implementingAgency;
	private Set beneficiaryAgency;
	private Set<AmpOrganisation> donnorgAgency;

	private Set teamAssignedOrgs = null;

	private Set financingInstruments = null;
	private Set<AmpCategoryValue> projectCategory = null;

	
	private Set<AmpCategoryValue> typeOfAssistance = null;
	private Set<AmpCategoryValue> modeOfPayment = null;
	// private Long ampModalityId=null;

	private AmpCurrency currency = null;
	private Set ampTeams = null;
	private AmpFiscalCalendar calendarType = null;
	private boolean widget = false;
	private boolean publicView = false;
	private Set<AmpCategoryValue> budget = null;
	private Collection<Integer> lineMinRank;
	private Collection<Integer> planMinRank;
	private String fromDate;
	private String toDate;
	private Integer fromMonth;
	private Integer yearFrom;
	private Integer toMonth;
	private Integer yearTo;
	private Collection<AmpCategoryValueLocations> locationSelected = null;
	@PropertyListableIgnore
	private Collection<AmpCategoryValueLocations> relatedLocations;
	private Boolean unallocatedLocation = null;
	//private AmpCategoryValueLocations regionSelected = null;
	private Collection<String> approvalStatusSelected=null;
	private boolean approved = false;
	private boolean draft = false;

	private Integer renderStartYear = null; // the range of dates columns that
											// has to be render, years not in
											// range will be computables for
											// totals but wont be rederisables
	private Integer renderEndYear = null;

	private DecimalFormat currentFormat = null;
	private Boolean amountinthousand;
	private Boolean amountinmillion;
	public final Boolean getAmountinmillion() {
		return amountinmillion;
	}

	public final void setAmountinmillion(Boolean amountinmillion) {
		this.amountinmillion = amountinmillion;
	}

	private Boolean governmentApprovalProcedures;
	private Boolean jointCriteria;
	private String accessType=null;
	
	
	private String pageSize; // to be used for exporting reports

	private String text;
	private String indexText;
	private static final String initialPledgeFilterQuery = "SELECT distinct(id) FROM amp_funding_pledges WHERE 1=1 ";
	private static final String initialFilterQuery = "SELECT distinct(amp_activity_id) FROM amp_activity WHERE 1=1 ";
	private String generatedFilterQuery;
	private int initialQueryLength = initialFilterQuery.length();
	
	private String sortBy;
	private Boolean sortByAsc						= true;
	private Collection<String> hierarchySorters		= new ArrayList<String>();
	
	private Set<AmpCategoryValue> projectImplementingUnits = null; 
	
	private boolean budgetExport		= false;
	
	private void queryAppend(String filter) {
		generatedFilterQuery += " AND amp_activity_id IN (" + filter + ")";
	}
	
	private void PledgequeryAppend(String filter) {
		generatedFilterQuery += " AND id IN (" + filter + ")";
	}
	
	public void readRequestData(HttpServletRequest request) {
		this.generatedFilterQuery = initialFilterQuery;
		TeamMember tm = (TeamMember) request.getSession().getAttribute(
				Constants.CURRENT_MEMBER);
		this.setAmpTeams(new TreeSet());
		
		String ampReportId = null ;
		//Check if the reportid is not nut for public mondrian reports
		if (request.getParameter("ampReportId")!=null && !request.getParameter("ampReportId").equals("")){
			ampReportId = request.getParameter("ampReportId");
			AmpReports ampReport=DbUtil.getAmpReport(Long.parseLong(ampReportId));
			
			this.budgetExport	= ampReport.getBudgetExporter()==null ? false:ampReport.getBudgetExporter();
			
			Site site = RequestUtils.getSite(request);
			Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
			String siteId = site.getId().toString();
			String locale = navigationLanguage.getCode();
			ampReport.setSiteId(siteId);
			ampReport.setLocale(locale);
			if (ampReport.getType() == ArConstants.PLEDGES_TYPE){
					this.generatedFilterQuery = initialPledgeFilterQuery;
			}
		}
		if (ampReportId == null) {
			AmpReports ar = (AmpReports) request.getSession().getAttribute(
			"reportMeta");
			if (ar!=null){
				ampReportId = ar.getAmpReportId().toString();
			}
		}
		
		AmpApplicationSettings tempSettings = null;
		
		if (tm == null || tm.getTeamId() == null ) {
			tm	= null;
		}
		if (tm != null) {
			this.setAccessType(tm.getTeamAccessType());
			this.setAmpTeams(TeamUtil.getRelatedTeamsForMember(tm));
			// set the computed workspace orgs
			//Set teamAO = TeamUtil.getComputedOrgs(this.getAmpTeams());
			Set teamAO = TeamUtil.getComputedOrgs(this.getAmpTeams());

			if (teamAO != null && teamAO.size() > 0)
				this.setTeamAssignedOrgs(teamAO);

			tempSettings = DbUtil.getMemberAppSettings(tm.getMemberId());

			if (tempSettings == null)
				if (tm != null)
					tempSettings = DbUtil.getTeamAppSettings(tm.getTeamId());

			if (this.getCurrency() == null)
				this.setCurrency(tempSettings.getCurrency());

		}
		else {
			//Check if the reportid is not nut for public mondrian reports
			if (ampReportId !=null){
				AmpReports ampReport=DbUtil.getAmpReport(Long.parseLong(ampReportId));
			
				//TreeSet allManagementTeams=(TreeSet) TeamUtil.getAllRelatedTeamsByAccessType("Management");
				TreeSet teams=new TreeSet();
				this.setAccessType("team");
				if (ampReport.getOwnerId()!=null){
					teams.add(ampReport.getOwnerId().getAmpTeam());
					teams.addAll(TeamUtil.getAmpLevel0Teams(ampReport.getOwnerId().getAmpTeam().getAmpTeamId()));
					this.setAmpTeams(teams);
				}else{
					teams.add(-1);
					this.setAmpTeams(teams);
					logger.error("Error getOwnerId() is null setting team to -1");
				}
			}
		}
		
		 if (calendarType==null){
			if (tempSettings!=null){
				calendarType=tempSettings.getFiscalCalendar();
			}
		}
		String gvalue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (calendarType==null){
			if (gvalue!=null){
				Long fiscalCalId=Long.parseLong(gvalue);
				calendarType=DbUtil.getFiscalCalendar(fiscalCalId);
			}
		}
		
		
		Long defaultCalendarId=null;
		
		if (tempSettings!=null){
			if (tempSettings.getFiscalCalendar()!=null){
				defaultCalendarId=tempSettings.getFiscalCalendar().getAmpFiscalCalId();
			}else{
				defaultCalendarId=Long.parseLong(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));	
			}
		}	
		///Set the range depending of workspase setup / global setting and selected calendar
		ICalendarWorker worker = null;
		Date checkDate = null;
		if (renderStartYear == null || (request.getParameter("view") != null && "reset".compareTo(request.getParameter("view")) == 0)) {
			// Check if there is value on workspace setting
			if (tempSettings != null
					&& tempSettings.getReportStartYear() != null
					&& tempSettings.getReportStartYear().intValue() != 0) {
				this.setRenderStartYear(tempSettings.getReportStartYear());
			} else { // if not check if the value exist on
				// global setting
				 gvalue = FeaturesUtil
						.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
				if (gvalue != null && !"".equalsIgnoreCase(gvalue)
						&& Integer.parseInt(gvalue) > 0) {
					renderStartYear = Integer.parseInt(gvalue);
				}

			}
			
			
			
if (renderStartYear!=null && renderStartYear>0 && calendarType != null && calendarType.getAmpFiscalCalId().equals(defaultCalendarId) ){
				worker = calendarType.getworker();
				try {
					checkDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + renderStartYear);
					worker.setTime(checkDate);
					renderStartYear=worker.getYear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			}
		
		renderStartYear=(renderStartYear==null)?-1:renderStartYear;
		
		if (renderEndYear == null || (request.getParameter("view") != null && "reset".compareTo(request.getParameter("view")) == 0)) {
			// Check if there is value on workspace setting
			if (tempSettings != null && tempSettings.getReportEndYear() != null
					&& tempSettings.getReportEndYear().intValue() != 0) {
				this.setRenderEndYear(tempSettings.getReportEndYear());
			} else {
				 gvalue=null;
				 gvalue = FeaturesUtil
						.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
				if (gvalue != null && !"".equalsIgnoreCase(gvalue)
						&& Integer.parseInt(gvalue) > 0) {
					renderEndYear = Integer.parseInt(gvalue);
				}
			}
			
			 if (renderEndYear!=null && renderEndYear>0 && calendarType != null && calendarType.getAmpFiscalCalId().equals(defaultCalendarId) ){
				worker = calendarType.getworker();
				try {
					checkDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + renderEndYear);
					worker.setTime(checkDate);
					renderEndYear=worker.getYear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		renderEndYear=(renderEndYear==null)?-1:renderEndYear;
		

		if (currentFormat == null) {
			currentFormat = FormatHelper.getDefaultFormat();
			FormatHelper.tlocal.set(null);
		} else {
			FormatHelper.tlocal.set(currentFormat);
		}

		String widget = (String) request.getAttribute("widget");
		if (widget != null)
			this.setWidget(new Boolean(widget).booleanValue());

		try {
			this.setAmpReportId(new Long(ampReportId));
		}
		catch (NumberFormatException e) {
			logger.info("NumberFormatException:" + e.getMessage());
		}

	}

	public AmpARFilter() {
		super();
		this.generatedFilterQuery = initialFilterQuery;
	}

	public void generateFilterQuery(HttpServletRequest request) {
		if (request.getSession().getAttribute(ArConstants.PLEDGES_REPORT) != null && 
				request.getSession().getAttribute(ArConstants.PLEDGES_REPORT).toString().equalsIgnoreCase("true")){
			indexedParams=new ArrayList<FilterParam>();
		
			String DONNOR_AGENCY_FILTER = " SELECT v.pledge_id FROM v_pledges_donor v  WHERE v.amp_donor_org_id IN ("
				+ Util.toCSString(donnorgAgency) + ")";
			
//			String REGION_SELECTED_FILTER = "SELECT v.pledge_id FROM v_pledges_regions v WHERE region_id ="
//				+ (regionSelected==null?null:regionSelected.getIdentifier());
			
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
//			if (regionSelected != null){
//				PledgequeryAppend(REGION_SELECTED_FILTER);
//			}
			if (financingInstruments != null && financingInstruments.size() > 0){
				PledgequeryAppend(FINANCING_INSTR_FILTER);
			}
			if (typeOfAssistance != null && typeOfAssistance.size() > 0){
				PledgequeryAppend(TYPE_OF_ASSISTANCE_FILTER);
			}
			/*
			if (sectors != null && sectors.size() != 0) {
				PledgequeryAppend(SECTOR_FILTER);
			}
			*/
			return;
		}
		
		indexedParams=new ArrayList<FilterParam>();
		
		String BUDGET_FILTER = "SELECT amp_activity_id FROM v_activity_budget WHERE budget_id IN ("
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
				+ ") " + " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
				+ Util.toCSString(ampTeams) + ") ) AND draft<>true ";
		else{
			
			TEAM_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IS NOT NULL AND amp_team_id IN ("
				+ Util.toCSString(ampTeams)
				+ ") "
				+ " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
				+ Util.toCSString(ampTeams) + ") )" ;
		}
		NO_MANAGEMENT_ACTIVITIES +="SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IS NOT NULL AND amp_team_id IN ("
			+ Util.toCSString(ampTeams)
			+ ") "
			+ " OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("
			+ Util.toCSString(ampTeams) + ") )" ;
			

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
		int c;
		if(draft){
			c= Math.abs( DbUtil.countActivitiesByQuery(TEAM_FILTER + " AND amp_activity_id IN (SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft = 0) )",null )-DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,null));
		}
		else c= Math.abs( DbUtil.countActivitiesByQuery(TEAM_FILTER,null)-DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,null) );
		this.setActivitiesRejectedByFilter(new Long(c));
		request.getSession().setAttribute("activitiesRejected",this.getActivitiesRejectedByFilter());

		String STATUS_FILTER = "SELECT amp_activity_id FROM v_status WHERE amp_status_id IN ("
				+ Util.toCSString(statuses) + ")";

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
	 	
	 	String REGION_SELECTED_FILTER = "";
		if (unallocatedLocation != null) {
			if (unallocatedLocation == true) {
				REGION_SELECTED_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE amp_activity_id NOT IN(SELECT amp_activity_id FROM amp_activity_location)";
			}
		}
		
		String ACTUAL_APPROVAL_YEAR_FILTER = "";
		if (actualAppYear!=null && actualAppYear!=-1) {
			ACTUAL_APPROVAL_YEAR_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE YEAR(actual_approval_date) = " + actualAppYear + " ";
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
					actStatusValue.append(" (approval_status='edited' and draft <>1)");break;
				case 1://New Draft - This will show all the activities that have never been approved and are saved as drafts.
					actStatusValue.append(" (approval_status in ('started', 'startedapproved') and draft=1) ");break;
				case 2://New Un-validated - This will show all activities that are new and have never been approved by the workspace manager.
					actStatusValue.append(" (approval_status='started' and draft<>1)");break;
				case 3://existing draft. This is because when you filter by Existing Unvalidated you get draft activites that were edited and saved as draft
					actStatusValue.append(" ( (approval_status='edited' or approval_status='approved') and draft=1) ");break;
				case 4://Validated Activities 
					actStatusValue.append("(approval_status='approved' and draft<>1)");break;
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
		
		String DRAFT_FILTER = "SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft = 0)";
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

		String EXECUTING_AGENCY_FILTER = "SELECT v.amp_activity_id FROM v_executing_agency v  "
				+ "WHERE v.amp_org_id IN ("
				+ Util.toCSString(executingAgency) + ")";
		
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
			queryAppend(TO_DATE_FILTER);
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
				Directory idx = (Directory) ampContext
						.getAttribute(Constants.LUCENE_INDEX);

				Hits hits = LuceneUtil.search(ampContext.getRealPath("/") + LuceneUtil.ACTVITY_INDEX_DIRECTORY, "all", indexText);
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
		if (  !this.budgetExport && ampTeams != null && ampTeams.size() > 0)
			queryAppend(TEAM_FILTER);
		if (statuses != null && statuses.size() > 0)
			queryAppend(STATUS_FILTER);
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
		if (donorTypes != null && donorTypes.size() > 0)
			queryAppend(DONOR_TYPE_FILTER);

		if (executingAgency != null && executingAgency.size() > 0)
			queryAppend(EXECUTING_AGENCY_FILTER);
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
					+ ((governmentApprovalProcedures)?"1":"0");
			queryAppend(GOVERNMENT_APPROVAL_FILTER);
		}
		if (jointCriteria != null) {
			String JOINT_CRITERIA_FILTER = "SELECT a.amp_activity_id from amp_activity a where jointCriteria="
					+ ((jointCriteria)?"1":"0");;
			queryAppend(JOINT_CRITERIA_FILTER);
		}
		DbUtil.countActivitiesByQuery(this.generatedFilterQuery,indexedParams);
		logger.info(this.generatedFilterQuery);
		
		if(draft){
			c= Math.abs( DbUtil.countActivitiesByQuery(this.generatedFilterQuery + " AND amp_activity_id IN (SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft = 0) )",indexedParams )-DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,indexedParams) );
		}
		else c= Math.abs( DbUtil.countActivitiesByQuery(this.generatedFilterQuery,indexedParams)-DbUtil.countActivitiesByQuery(NO_MANAGEMENT_ACTIVITIES,null) );
		this.setActivitiesRejectedByFilter(new Long(c));
		request.getSession().setAttribute("activitiesRejected",this.getActivitiesRejectedByFilter());
		
		
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

	public Set getFinancingInstruments() {
		return financingInstruments;
	}

	public void setFinancingInstruments(Set financingInstruments) {
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
	public Set getSectors() {
		return sectors;
	}

	/**
	 * @param sectors
	 *            The sectors to set.
	 */
	public void setSectors(Set sectors )  {
		this.sectors = sectors;
	}
	
	

	/**
	 * @return the sectorsAndAncestors
	 */
	@PropertyListableIgnore
	public Set getSectorsAndAncestors() {
		return sectorsAndAncestors;
	}

	/**
	 * @param sectorsAndAncestors the sectorsAndAncestors to set
	 */
	public void setSectorsAndAncestors(Set sectorsAndAncestors) {
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
	public Set getAmpTeams() {
		return ampTeams;
	}

	/**
	 * @param ampTeams
	 *            The ampTeams to set.
	 */
	public void setAmpTeams(Set ampTeams) {
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
	public Set getStatuses() {
		return statuses;
	}

	/**
	 * @param statuses
	 *            The statuses to set.
	 */
	public void setStatuses(Set statuses) {
		this.statuses = statuses;
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

	public void setWidget(boolean widget) {
		this.widget = widget;
	}

	public Set getBudget() {
		return budget;
	}

	public void setBudget(Set budget) {
		this.budget = budget;
	}

	public Set getRisks() {
		return risks;
	}

	public void setRisks(Set risks) {
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
		if (text.trim().length() == 0)
			this.text = null;
		this.text = text;

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

	public Set getDonorTypes() {
		return donorTypes;
	}

	public void setDonorTypes(Set donorTypes) {
		this.donorTypes = donorTypes;
	}

	public Set getDonorGroups() {
		return donorGroups;
	}

	public void setDonorGroups(Set donorGroups) {
		this.donorGroups = donorGroups;
	}

	public Set getBeneficiaryAgency() {
		return beneficiaryAgency;
	}

	public void setBeneficiaryAgency(Set beneficiaryAgency) {
		this.beneficiaryAgency = beneficiaryAgency;
	}

	public Set getExecutingAgency() {
		return executingAgency;
	}

	public void setExecutingAgency(Set executingAgency) {
		this.executingAgency = executingAgency;
	}

	public Set getImplementingAgency() {
		return implementingAgency;
	}

	public void setImplementingAgency(Set implementingAgency) {
		this.implementingAgency = implementingAgency;
	}

	public Set getSelectedSectors() {
		return selectedSectors;
	}

	public void setSelectedSectors(Set selectedSectors) {
		this.selectedSectors = selectedSectors;
	}

	@PropertyListableIgnore
	public Set getSecondarySectors() {
		return secondarySectors;
	}

	public void setSecondarySectors(Set secondarySectors) {
		this.secondarySectors = secondarySectors;
	}
	
	/**
	 * @return the secondarySectorsAndAncestors
	 */
	@PropertyListableIgnore
	public Set getSecondarySectorsAndAncestors() {
		return secondarySectorsAndAncestors;
	}

	/**
	 * @param secondarySectorsAndAncestors the secondarySectorsAndAncestors to set
	 */
	public void setSecondarySectorsAndAncestors(Set secondarySectorsAndAncestors) {
		this.secondarySectorsAndAncestors = secondarySectorsAndAncestors;
	}

	public Set getSelectedSecondarySectors() {
		return selectedSecondarySectors;
	}

	public void setSelectedSecondarySectors(Set selectedSecondarySectors) {
		this.selectedSecondarySectors = selectedSecondarySectors;
	}

       public Set getSelectedTertiarySectors() {
        return selectedTertiarySectors;
    }

    public void setSelectedTertiarySectors(Set selectedTertiarySectors) {
        this.selectedTertiarySectors = selectedTertiarySectors;
    }
    
    @PropertyListableIgnore
    public Set getTertiarySectors() {
        return tertiarySectors;
    }

    public void setTertiarySectors(Set tertiarySectors) {
        this.tertiarySectors = tertiarySectors;
    }
    @PropertyListableIgnore
    public Set getTertiarySectorsAndAncestors() {
        return tertiarySectorsAndAncestors;
    }

    public void setTertiarySectorsAndAncestors(Set tertiarySectorsAndAncestors) {
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
		this.renderStartYear = renderStartYear;
	}

	public Integer getRenderEndYear() {
		return renderEndYear;
	}

	public void setRenderEndYear(Integer renderEndYear) {
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

	@IgnorePersistence
	public Long getActivitiesRejectedByFilter() {
		return activitiesRejectedByFilter;
	}

	public void setActivitiesRejectedByFilter(Long activitiesRejectedByFilter) {
		this.activitiesRejectedByFilter = activitiesRejectedByFilter;
	}

	public String getTeamAccessType() {
		return teamAccessType;
	}

	public void setTeamAccessType(String teamAccessType) {
		this.teamAccessType = teamAccessType;
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

	public Set getResponsibleorg() {
		return responsibleorg;
	}

	public void setResponsibleorg(Set responsibleorg) {
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
	
	public Boolean getAmountinthousand() {
		return amountinthousand;
	}

	public void setAmountinthousand(Boolean amountinthousand) {
		this.amountinthousand = amountinthousand;
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


}
