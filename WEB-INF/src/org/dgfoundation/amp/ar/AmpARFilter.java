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
import java.util.Collection;
import java.util.Iterator;
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
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.AmpARFilterHelper;
import org.digijava.module.aim.logic.Logic;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * Filtering bean. Holds info about filtering parameters and creates the filtering query
 * @author Mihai Postelnicu - mpostelnicu@dgfoundatiTeam member rights follow the activity roles as we have defined them.on.org
 * @since Aug 5, 2006
 *
 */
public class AmpARFilter extends PropertyListable implements Filter {
	
	protected static Logger logger = Logger.getLogger(AmpARFilter.class);
	private Long id;
	private Long ampReportId;
	private Set statuses=null;
	//private Set donors=null; //not used anymore
	@PropertyListableIgnore
	private Set sectors=null;
	private Set selectedSectors	= null;
	
	@PropertyListableIgnore
	private Set secondarySectors=null;
	private Set selectedSecondarySectors=null;
	
	private Set regions=null;
	private Set risks=null;
	private Set donorTypes=null;
	private Set donorGroups=null;

	private Set executingAgency;
	private Set implementingAgency;
	private Set beneficiaryAgency;
	
	private Set teamAssignedOrgs=null;

	private Set financingInstruments=null;
	private Set<AmpCategoryValue> typeOfAssistance=null;
	//private Long ampModalityId=null;
	
	private AmpCurrency currency=null;
	private Set ampTeams=null;
	private AmpFiscalCalendar calendarType=null;
	private AmpPerspective perspective;
	private boolean widget=false;
	private boolean publicView=false;
	private Boolean budget=null;
	private Integer lineMinRank;
	private Integer planMinRank;
	private Integer fromMonth;
	private Integer yearFrom;
	private Integer toMonth;
	private Integer yearTo;	
	private Long regionSelected=null;
	private boolean approved=false;
	private boolean draft=false;
	
	private Boolean governmentApprovalProcedures;
	private Boolean jointCriteria;
	
	private String pageSize; // to be used for exporting reports
	
	private String text;
	private String indexText;
	
	private static final String initialFilterQuery="SELECT distinct(amp_activity_id) FROM amp_activity WHERE 1";
	private String generatedFilterQuery;
	private int initialQueryLength=initialFilterQuery.length();

	private void queryAppend(String filter) {
		//generatedFilterQuery+= (initialQueryLength==generatedFilterQuery.length()?"":" AND ") + " amp_activity_id IN ("+filter+")";
		generatedFilterQuery+= " AND amp_activity_id IN ("+filter+")";
	}
	
	public void readRequestData(HttpServletRequest request) {
		this.generatedFilterQuery = initialFilterQuery;
		TeamMember tm = (TeamMember) request.getSession().getAttribute(
				Constants.CURRENT_MEMBER);

		selectPerspective(tm);
		
				
		this.setAmpTeams(new TreeSet());
	
		
		if(tm!=null){
			this.setAmpTeams(TeamUtil.getRelatedTeamsForMember(tm));			
			//set the computed workspace orgs
			Set teamAO		= TeamUtil.getComputedOrgs(this.getAmpTeams());
			
			if ( teamAO!=null && teamAO.size()>0 )
					this.setTeamAssignedOrgs(teamAO);

			AmpApplicationSettings tempSettings = DbUtil.getMemberAppSettings(tm.getMemberId());
		    
			if(tempSettings==null)
				if(tm!=null)
				    tempSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
			
		    if (this.getCurrency() == null)
		    	this.setCurrency(tempSettings.getCurrency());	
		    
			
			if (this.getYearFrom()==null && tempSettings.getReportStartYear()!=null && tempSettings.getReportStartYear().intValue()!=0)
			    this.setYearFrom(tempSettings.getReportStartYear());
			
			if (this.getYearTo()==null && tempSettings.getReportEndYear()!=null && tempSettings.getReportEndYear().intValue()!=0)
			    this.setYearTo(tempSettings.getReportEndYear());
			
		}
		

		
		String widget=(String) request.getAttribute("widget");
		if(widget!=null) this.setWidget(new Boolean(widget).booleanValue());
		
		String ampReportId = request.getParameter("ampReportId");
		if(ampReportId==null) {
			AmpReports ar=(AmpReports) request.getSession().getAttribute("reportMeta");
			ampReportId=ar.getAmpReportId().toString();
		}			
			
		this.setAmpReportId(new Long(ampReportId));
		
	}

	public void selectPerspective(TeamMember tm) {
		String perspectiveCode = null;
		if (tm != null) {
			String perspective = tm.getAppSettings().getPerspective();
			if (perspective != null) {
				if (perspective.equals("Donor"))
					perspectiveCode = "DN";
				if (perspective.equals("MOFED"))
					perspectiveCode = "MA";
			} else {
				perspectiveCode = "MA";
			}
		} else {
			perspectiveCode = "MA";
		}

		this.setPerspective(DbUtil.getPerspective(perspectiveCode));
	}
	

	public AmpARFilter() {
		super();	
		this.generatedFilterQuery=initialFilterQuery;
	}
	
	public void generateFilterQuery(HttpServletRequest request) {
		String BUDGET_FILTER="SELECT amp_activity_id FROM amp_activity WHERE budget="+(budget!=null?budget.toString():"null")+(budget!=null && budget.booleanValue()==false?" OR budget is null":"");
		String TEAM_FILTER="SELECT amp_activity_id FROM amp_activity WHERE amp_team_id IN ("+Util.toCSString(ampTeams,true)+") " +
				"OR amp_activity_id IN (SELECT ata.amp_activity_id FROM amp_team_activities ata WHERE ata.amp_team_id IN ("+Util.toCSString(ampTeams,true)+") )";
		
		//computed workspace filter -- append it to the team filter so normal team activities are also possible 
		if(teamAssignedOrgs!=null && teamAssignedOrgs.size() > 0) {
			TEAM_FILTER+=" OR amp_activity_id IN (SELECT DISTINCT(activity) FROM amp_org_role WHERE organisation IN ("+Util.toCSString(teamAssignedOrgs, true)+") )";
			TEAM_FILTER+= "OR amp_activity_id IN (SELECT distinct(amp_activity_id) FROM amp_funding WHERE amp_donor_org_id IN ("+Util.toCSString(teamAssignedOrgs, true)+"))";
			
		}
		
		String STATUS_FILTER="SELECT amp_activity_id FROM v_status WHERE amp_status_id IN ("+Util.toCSString(statuses,true)+")";
	
		//String ORG_FILTER = "SELECT amp_activity_id FROM v_donor_groups WHERE amp_org_grp_id IN ("+Util.toCSString(donors,true)+")";
//		String PARENT_SECTOR_FILTER="SELECT amp_activity_id FROM v_sectors WHERE amp_sector_id IN ("+Util.toCSString(sectors,true)+")";
//		String SUB_SECTOR_FILTER="SELECT amp_activity_id FROM v_sub_sectors WHERE amp_sector_id IN ("+Util.toCSString(sectors,true)+")";
//		String SECTOR_FILTER="(("+PARENT_SECTOR_FILTER+") UNION ("+SUB_SECTOR_FILTER+"))";
		
		String SECTOR_FILTER=
			"SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c " +
			"WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id " +
			"AND c.name='Primary' AND aas.amp_sector_id in (" +Util.toCSString(sectors,true)+ ")";
		
//		String SECONDARY_PARENT_SECTOR_FILTER=
//			"SELECT amp_activity_id FROM v_secondary_sectors WHERE amp_sector_id IN ("+Util.toCSString(secondarySectors,true)+")";
//		String SECONDARY_SUB_SECTOR_FILTER=
//			"SELECT amp_activity_id FROM v_secondary_sub_sectors WHERE amp_sector_id IN ("+Util.toCSString(secondarySectors,true)+")";
//		String SECONDARY_SECTOR_FILTER="(("+SECONDARY_PARENT_SECTOR_FILTER+") UNION ("+SECONDARY_SUB_SECTOR_FILTER+"))";
		String SECONDARY_SECTOR_FILTER=
			"SELECT aas.amp_activity_id FROM amp_activity_sector aas, amp_sector s, amp_classification_config c " +
			"WHERE aas.amp_sector_id=s.amp_sector_id AND s.amp_sec_scheme_id=c.classification_id " +
			"AND c.name='Secondary' AND aas.amp_sector_id in (" +Util.toCSString(secondarySectors,true)+ ")";
		
		
		String REGION_FILTER="SELECT amp_activity_id FROM v_regions WHERE name IN ("+Util.toCSString(regions,true)+")";
		String FINANCING_INSTR_FILTER="SELECT amp_activity_id FROM v_financing_instrument WHERE amp_modality_id IN ("+Util.toCSString(financingInstruments, true)+")";
		String LINE_MIN_RANK_FILTER="SELECT amp_activity_id FROM amp_activity WHERE line_min_rank="+lineMinRank;
		String PLAN_MIN_RANK_FILTER="SELECT amp_activity_id FROM amp_activity WHERE plan_min_rank="+planMinRank;
		String REGION_SELECTED_FILTER="SELECT amp_activity_id FROM v_regions WHERE region_id ="+regionSelected;
		String APPROVED_FILTER="SELECT amp_activity_id FROM amp_activity WHERE approval_status like '" + Constants.APPROVED_STATUS+"'";
		String DRAFT_FILTER="SELECT amp_activity_id FROM amp_activity WHERE (draft is null) OR (draft = false)";
		String TYPE_OF_ASSISTANCE_FILTER	= "SELECT amp_activity_id FROM v_terms_assist WHERE terms_assist_code IN ("+Util.toCSString(typeOfAssistance, true)+")";
		
		String DONOR_TYPE_FILTER	= 
			"SELECT aa.amp_activity_id " +
			"FROM amp_activity aa, amp_org_role aor, amp_role rol, amp_org_type typ, amp_organisation og  " +
			"WHERE aa.amp_activity_id = aor.activity AND aor.role = rol.amp_role_id AND rol.role_code='DN' " +
			"AND typ.amp_org_type_id =  og.org_type_id AND og.amp_org_id = aor.organisation " +
			"AND typ.amp_org_type_id IN ("+ Util.toCSString(donorTypes, true) +")";
		
		String DONOR_GROUP_FILTER	= 
			"SELECT aa.amp_activity_id " +
			"FROM amp_activity aa, amp_org_role aor, amp_role rol, amp_org_group grp, amp_organisation og  " +
			"WHERE aa.amp_activity_id = aor.activity AND aor.role = rol.amp_role_id AND rol.role_code='DN' " +
			"AND grp.amp_org_grp_id =  og.org_grp_id AND og.amp_org_id = aor.organisation " +
			"AND grp.amp_org_grp_id IN ("+ Util.toCSString(donorGroups, true) +")";
		
		String EXECUTING_AGENCY_FILTER	= 
			"SELECT v.amp_activity_id FROM v_executing_agency v  " +
			"WHERE v.amp_org_id IN ("+ Util.toCSString(executingAgency, true) +")";
		String BENEFICIARY_AGENCY_FILTER	= 
			"SELECT v.amp_activity_id FROM v_beneficiary_agency v  " +
			"WHERE v.amp_org_id IN ("+ Util.toCSString(beneficiaryAgency, true) +")";
		String IMPLEMENTING_AGENCY_FILTER	= 
			"SELECT v.amp_activity_id FROM v_implementing_agency v  " +
			"WHERE v.amp_org_id IN ("+ Util.toCSString(implementingAgency, true) +")";
		
		/*if(fromYear!=null) {
			AmpARFilterHelper filterHelper = Logic.getInstance().getAmpARFilterHelper();
			String FROM_FUNDING_YEAR_FILTER = filterHelper.createFromYearQuery(fromYear);
		    queryAppend(FROM_FUNDING_YEAR_FILTER);
		}
		
		if(toYear!=null) {
			AmpARFilterHelper filterHelper = Logic.getInstance().getAmpARFilterHelper();
			String TO_FUNDING_YEAR_FILTER = filterHelper.createToYearQuery(toYear);
		    queryAppend(TO_FUNDING_YEAR_FILTER);
		}
		
		if (fromMonth!=null) {
			AmpARFilterHelper filterHelper = Logic.getInstance().getAmpARFilterHelper();
			String FROM_MONTH_FILTER = filterHelper.createFromMonthQuery(fromMonth);
			queryAppend(FROM_MONTH_FILTER);
		}
		
		if (toMonth!=null) {
			AmpARFilterHelper filterHelper = Logic.getInstance().getAmpARFilterHelper();
			String TO_MONTH_FILTER = filterHelper.createToMonthQuery(toMonth);
			queryAppend(TO_MONTH_FILTER);
		}*/
		
		if (fromMonth == null){
			if (yearFrom != null){
				AmpARFilterHelper filterHelper = Logic.getInstance().getAmpARFilterHelper();
				String FROM_FUNDING_YEAR_FILTER = filterHelper.createFromYearQuery(yearFrom);
			    queryAppend(FROM_FUNDING_YEAR_FILTER);
			}
		}
		else{
			AmpARFilterHelper filterHelper = Logic.getInstance().getAmpARFilterHelper();
			String FROM_FUNDING_YEARMONTH_FILTER = filterHelper.createFromMonthQuery(fromMonth, yearFrom);
		    queryAppend(FROM_FUNDING_YEARMONTH_FILTER);
		}
		
		
		if (toMonth == null){
			if (yearTo != null){
				AmpARFilterHelper filterHelper = Logic.getInstance().getAmpARFilterHelper();
				String TO_FUNDING_YEAR_FILTER = filterHelper.createToYearQuery(yearTo);
			    queryAppend(TO_FUNDING_YEAR_FILTER);
			}
		}
		else{
			AmpARFilterHelper filterHelper = Logic.getInstance().getAmpARFilterHelper();
			String TO_FUNDING_YEARMONTH_FILTER = filterHelper.createToMonthQuery(toMonth, yearTo);
		    queryAppend(TO_FUNDING_YEARMONTH_FILTER);
		}
		
		/*
		if (fromYear==null)
			fromYear = 0;
		
		if (fromMonth==null)
			fromMonth = 1;
		
		if (toYear==null)
			toYear = 9999;
		
		if (toMonth==null)
			toMonth = 12;
		
		AmpARFilterHelper filterHelper = Logic.getInstance().getAmpARFilterHelper();
		String MONTH_YEAR_FILTER = filterHelper.createMonthYearQuery(fromMonth, fromYear, toMonth, toYear);
		queryAppend(MONTH_YEAR_FILTER);
		*/
		if(text!=null)
		{
			if("".equals(text.trim())==false)
			{
			String TEXT_FILTER="SELECT a.amp_activity_id from amp_activity a WHERE a.amp_id="+text;
			queryAppend(TEXT_FILTER);
			}
		}
		
		if (indexText != null)
			if ("".equals(indexText.trim()) == false){
				String LUCENE_ID_LIST = "";
				HttpSession session = request.getSession();
				ServletContext ampContext = session.getServletContext();
				Directory idx = (Directory) ampContext.getAttribute(Constants.LUCENE_INDEX);
	
				Hits hits = LuceneUtil.search(idx, "all", indexText);
				logger.info("New lucene search !");
				for(int i = 0; i < hits.length(); i++) {
					Document doc;
					try {
						doc = hits.doc(i);
						if (LUCENE_ID_LIST == "")
							LUCENE_ID_LIST = doc.get("id");
						else
							LUCENE_ID_LIST = LUCENE_ID_LIST + "," + doc.get("id");
						//AmpActivity act = ActivityUtil.getAmpActivity(Long.parseLong(doc.get("id")));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				logger.info("Lucene ID List:" + LUCENE_ID_LIST);
				if (LUCENE_ID_LIST.length() < 1){
					logger.info("Not found!");
					LUCENE_ID_LIST = "-1";
				}
				queryAppend(LUCENE_ID_LIST);
			}
		
		String RISK_FILTER="SELECT v.activity_id from AMP_ME_INDICATOR_VALUE v, AMP_INDICATOR_RISK_RATINGS r where v.risk=r.amp_ind_risk_ratings_id and r.amp_ind_risk_ratings_id in ("+Util.toCSString(risks,true)+")";
		
		if(budget!=null) queryAppend(BUDGET_FILTER);
		if(ampTeams!=null && ampTeams.size()>0) queryAppend(TEAM_FILTER);
		if(statuses!=null && statuses.size()>0) queryAppend(STATUS_FILTER);
		//if(donors!=null && donors.size()>0) queryAppend(ORG_FILTER);
		if(sectors!=null && sectors.size()!=0) {
			queryAppend(SECTOR_FILTER);
		}
		if (secondarySectors!=null && secondarySectors.size()!=0 ) {
			queryAppend(SECONDARY_SECTOR_FILTER);
		}
		if(regions!=null && regions.size()>0) queryAppend(REGION_FILTER);
		if(financingInstruments!=null && financingInstruments.size()>0) queryAppend(FINANCING_INSTR_FILTER);
		if(risks!=null && risks.size()>0) queryAppend(RISK_FILTER);
		if(lineMinRank!=null) queryAppend(LINE_MIN_RANK_FILTER);
		if(planMinRank!=null) queryAppend(PLAN_MIN_RANK_FILTER);
		if(regionSelected!=null) queryAppend(REGION_SELECTED_FILTER);
		if(approved==true) queryAppend(APPROVED_FILTER);
		if (draft == true) queryAppend(DRAFT_FILTER);
		if( typeOfAssistance!=null && typeOfAssistance.size()>0 ) queryAppend(TYPE_OF_ASSISTANCE_FILTER);
		
		if(donorGroups!=null && donorGroups.size()>0) queryAppend(DONOR_GROUP_FILTER);
		if(donorTypes!=null && donorTypes.size()>0) queryAppend(DONOR_TYPE_FILTER);
		
		if ( executingAgency!=null && executingAgency.size()>0 ) queryAppend(EXECUTING_AGENCY_FILTER);
		if ( beneficiaryAgency!=null && beneficiaryAgency.size()>0 ) queryAppend(BENEFICIARY_AGENCY_FILTER);
		if ( implementingAgency!=null && implementingAgency.size()>0 ) queryAppend(IMPLEMENTING_AGENCY_FILTER);
		
		if(governmentApprovalProcedures!=null)
		{
			String GOVERNMENT_APPROVAL_FILTER="SELECT a.amp_activity_id from amp_activity a where governmentApprovalProcedures="+governmentApprovalProcedures.toString();
			queryAppend(GOVERNMENT_APPROVAL_FILTER);
		}
		if(jointCriteria!=null)
		{
			String JOINT_CRITERIA_FILTER="SELECT a.amp_activity_id from amp_activity a where jointCriteria="+jointCriteria.toString();
			queryAppend(JOINT_CRITERIA_FILTER);
		}
	}

	
	/**
	 * @return Returns the ampCurrencyCode.
	 */
	public AmpCurrency getCurrency() {
		return currency;
	}


	/**
	 * @param ampCurrencyCode The ampCurrencyCode to set.
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

	/**
	 * @return Returns the sectors.
	 */
	@PropertyListableIgnore
	public Set getSectors() {
		return sectors;
	}

	/**
	 * @param sectors The sectors to set.
	 */
	public void setSectors(Set sectors) {
		this.sectors = sectors;
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
	 * @param ampTeams The ampTeams to set.
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
	 * @param id The id to set.
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
	 * @param calendarType The calendarType to set.
	 */
	public void setCalendarType(AmpFiscalCalendar calendarType) {
		this.calendarType = calendarType;
	}

	/**
	 * @return Returns the donors.
	 */
//	public Set getDonors() {
//		return donors;
//	}

	/**
	 * @param donors The donors to set.
	 */
//	public void setDonors(Set donors) {
//		this.donors = donors;
//	}

	/**
	 * @return Returns the regions.
	 */
	public Set getRegions() {
		return regions;
	}

	/**
	 * @param regions The regions to set.
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
	 * @param statuses The statuses to set.
	 */
	public void setStatuses(Set statuses) {
		this.statuses = statuses;
	}

	/**
	 * @return Returns the perspectiveCode.
	 */
	@PropertyListableIgnore
	public AmpPerspective getPerspective() {
		return perspective;
	}

	/**
	 * @param perspectiveCode The perspectiveCode to set.
	 */
	public void setPerspective(AmpPerspective perspective) {
		this.perspective = perspective;
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

	public Boolean getBudget() {
		return budget;
	}

	public void setBudget(Boolean budget) {
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
	 * provides a way to display this bean in HTML. Properties are automatically shown along with their values. CollectionS are unfolded and
	 * excluded properties (internally used) are not shown.
	 * @see AmpARFilter.IGNORED_PROPERTIES
	 */
	public String toString() {
		StringBuffer ret=new StringBuffer();
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(AmpARFilter.class);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			Method m=propertyDescriptors[i].getReadMethod();
			Object object = m.invoke(this,new Object[]{});
			if(object==null || IGNORED_PROPERTIES.contains(propertyDescriptors[i].getName())) continue;			
			ret.append("<b>").append(propertyDescriptors[i].getName()).append(": ").append("</b>");
			if(object instanceof Collection) 
				ret.append(Util.toCSString((Collection) object,false));
			
			else ret.append(object);
			if(i<propertyDescriptors.length) ret.append("; ");
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

	private static final String IGNORED_PROPERTIES="class#generatedFilterQuery#initialQueryLength#widget#publicView#ampReportId#perspective";

	public Integer getLineMinRank() {
		return lineMinRank;
	}

	public void setLineMinRank(Integer lineMinRank) {
		this.lineMinRank = lineMinRank;
	}

	public Integer getPlanMinRank() {
		return planMinRank;
	}

	public void setPlanMinRank(Integer planMinRank) {
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
		if(text.trim().length()==0) this.text=null;
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

	public void setGovernmentApprovalProcedures(Boolean governmentApprovalProcedures) {
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

	public Long getRegionSelected() {
		return regionSelected;
	}

	public void setRegionSelected(Long regionSelected) {
		this.regionSelected = regionSelected;
	}

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



	public boolean isDraft() {
		return draft;
	}

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

	public Set getSelectedSecondarySectors() {
		return selectedSecondarySectors;
	}

	public void setSelectedSecondarySectors(Set selectedSecondarySectors) {
		this.selectedSecondarySectors = selectedSecondarySectors;
	}

	public Set<AmpCategoryValue> getTypeOfAssistance() {
		return typeOfAssistance;
	}

	public void setTypeOfAssistance(Set<AmpCategoryValue> typeOfAssistance) {
		this.typeOfAssistance = typeOfAssistance;
	}

	public Set getTeamAssignedOrgs() {
		return teamAssignedOrgs;
	}

	public void setTeamAssignedOrgs(Set teamAssignedOrgs) {
		this.teamAssignedOrgs = teamAssignedOrgs;
	}


}
