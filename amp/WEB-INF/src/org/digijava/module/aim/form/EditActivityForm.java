/**
 * @author Priyajith
 * @version 0.1
 */

package org.digijava.module.aim.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.exprlogic.MathExpression;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpChapter;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpWoreda;
import org.digijava.module.aim.dbentity.AmpZone;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CustomField;
import org.digijava.module.aim.helper.CustomFieldStep;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.SurveyFunding;
import org.digijava.module.aim.util.CustomFieldsUtil;
import org.digijava.module.aim.util.Step;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.springframework.beans.BeanWrapperImpl;

public class EditActivityForm extends ActionForm implements Serializable {

	private static final long serialVersionUID = -2405474513633165920L;
	
	private HashMap<String, String> errors = new HashMap<String, String>();
	private HashMap<String, String> messages = new HashMap<String, String>();
	
	private Long activityId = null;
	private String context;
	private boolean editAct;
	private String editKey;
	private boolean reset;
	private String svAction;
	private int isPreview = 0;
	private String buttonText;
	private String activityExists = "yes";
	
	private String workingTeamLeadFlag;
	private boolean teamLead;
	
	private List steps;
	private String stepText[];
	private Boolean stepFailure[];
	private String stepFailureText[];
	private String step = null;
	private int pageId;
	private FormFile fileImport = null;
	private boolean popupView = false;

	private String currCode;
	private Collection currencies;
	private boolean serializeFlag;
	private List<CustomField<?>> customFields;
	private List<CustomFieldStep> customFieldsSteps;
    /** The currency code in which we want to  see total funding
     * on the components and regional funding page...
     */
    private String fundingCurrCode;
    private String regFundingPageCurrCode;
    private ActivityContactInfo contactInformation;
    
	private List<AmpActivityContact> activityContacts; //holds all activity contacts
	private List<AmpActivityContact> mofedContacts;
	private List<AmpActivityContact> donorContacts;
	private List<AmpActivityContact> sectorMinistryContacts;
	private List<AmpActivityContact> projCoordinatorContacts;
	private List<AmpActivityContact> implExecutingAgencyContacts;
	
	private List<AmpStructure> structures;
	
	private Issues lineMinistryObservations;
	
	/**
	 * Map Api url for locations map
	 */
	private String esriapiurl;
	
	public List<AmpStructure> getStructures() {
		return structures;
	}

	public void setStructures(List<AmpStructure> structures) {
		this.structures = structures;
	}

	public String getEsriapiurl() {
		return esriapiurl;
	}

	public void setEsriapiurl(String esriapiurl) {
		this.esriapiurl = esriapiurl;
	}
    
    //0 displayAddDefaultProgram = "true" & displayAddProgram = "false";
    //1 displayAddDefaultProgram = "false" & displayAddProgram = "true";
    //2 displayAddDefaultProgram = "true" & displayAddProgram = "true";
	private Integer displayProgram = new Integer(0);
	
	
    public Integer getDisplayProgram() {
		return displayProgram;
	}

	public void setDisplayProgram(Integer displayProgram) {
		this.displayProgram = displayProgram;
	}

	/**
     * This collection represents the list of surveys available in the Paris Indicator page.
     */
    private Collection<SurveyFunding> surveyFundings = null;
    private int draftRedirectedPage;
    private List<String> warningMessges;

    public List<String> getWarningMessges() {
        return warningMessges;
    }

    public void setWarningMessges(List<String> warningMessges) {
        this.warningMessges = warningMessges;
    }

    public int getDraftRedirectedPage() {
        return draftRedirectedPage;
    }

    public void setDraftRedirectedPage(int draftRedirectedPage) {
        this.draftRedirectedPage = draftRedirectedPage;
    }

	public Collection<SurveyFunding> getSurveyFundings() {
		return surveyFundings;
	}

	public void setSurveyFundings(Collection<SurveyFunding> surveyFundings) {
		this.surveyFundings = surveyFundings;
	}

	public Issues getLineMinistryObservations() {
		if (this.lineMinistryObservations == null) {
			this.lineMinistryObservations = new Issues();
		}
		return lineMinistryObservations;
	}

	public void setLineMinistryObservations(Issues lineMinistryObservations) {
		this.lineMinistryObservations = lineMinistryObservations;
	}


	public class ActivityContactInfo{
		
		private List<AmpActivityContact> activityContacts; //holds all activity contacts
		private List<AmpActivityContact> mofedContacts;
		private List<AmpActivityContact> donorContacts;
		private List<AmpActivityContact> sectorMinistryContacts;
		private List<AmpActivityContact> projCoordinatorContacts;
		private List<AmpActivityContact> implExecutingAgencyContacts;
		
		private String[] primaryDonorContIds;
		private String[] primaryMofedContIds;
		private String[] primaryProjCoordContIds;
		private String[] primarySecMinContIds;
		private String[] primaryImplExecutingContIds;
		
		private String primaryDonorContId;
	 	private String primaryMofedContId;
	 	private String primaryProjCoordContId;
	 	private String primarySecMinContId;
	 	private String primaryImplExecutingContId;
		
		private Boolean resetDonorIds;
		private Boolean resetMofedIds;
		private Boolean resetProjCoordIds;
		private Boolean resetSecMinIds;
		private Boolean resetImplExecutingIds;
		
		
		private List<AmpContact> contacts; //holds all existing contacts
		private String primaryContact;
		private Boolean primaryAllowed; //defines whether activity contact can be primary or not. primary contact must be one for each type(mofed,donor,e.t.c.)
		private String contactType;		
		
		private String temporaryId; //contact's temporary id		

		
		public String[] getPrimaryDonorContIds() {
			return primaryDonorContIds;
		}
		public void setPrimaryDonorContIds(String[] primaryDonorContIds) {
			this.primaryDonorContIds = primaryDonorContIds;
		}
		public String[] getPrimaryMofedContIds() {
			return primaryMofedContIds;
		}
		public void setPrimaryMofedContIds(String[] primaryMofedContIds) {
			this.primaryMofedContIds = primaryMofedContIds;
		}
		public String[] getPrimaryProjCoordContIds() {
			return primaryProjCoordContIds;
		}
		public void setPrimaryProjCoordContIds(String[] primaryProjCoordContIds) {
			this.primaryProjCoordContIds = primaryProjCoordContIds;
		}
		public String[] getPrimarySecMinContIds() {
			return primarySecMinContIds;
		}
		public void setPrimarySecMinContIds(String[] primarySecMinContIds) {
			this.primarySecMinContIds = primarySecMinContIds;
		}
		
		public List<AmpActivityContact> getMofedContacts() {
			return mofedContacts;
		}
		public void setMofedContacts(List<AmpActivityContact> mofedContacts) {
			this.mofedContacts = mofedContacts;
		}
		public List<AmpActivityContact> getDonorContacts() {
			return donorContacts;
		}
		public void setDonorContacts(List<AmpActivityContact> donorContacts) {
			this.donorContacts = donorContacts;
		}
		public List<AmpActivityContact> getSectorMinistryContacts() {
			return sectorMinistryContacts;
		}
		public void setSectorMinistryContacts(
				List<AmpActivityContact> sectorMinistryContacts) {
			this.sectorMinistryContacts = sectorMinistryContacts;
		}
		public List<AmpActivityContact> getProjCoordinatorContacts() {
			return projCoordinatorContacts;
		}
		public void setProjCoordinatorContacts(
				List<AmpActivityContact> projCoordinatorContacts) {
			this.projCoordinatorContacts = projCoordinatorContacts;
		}
		public List<AmpActivityContact> getActivityContacts() {
			return activityContacts;
		}
		public void setActivityContacts(List<AmpActivityContact> activityContacts) {
			this.activityContacts = activityContacts;
		}
		public List<AmpContact> getContacts() {
			return contacts;
		}
		public void setContacts(List<AmpContact> contacts) {
			this.contacts = contacts;
		}
		
		public String getPrimaryContact() {
			return primaryContact;
		}
		public void setPrimaryContact(String primaryContact) {
			this.primaryContact = primaryContact;
		}
		public String getContactType() {
			return contactType;
		}
		public void setContactType(String contactType) {
			this.contactType = contactType;
		}
		public Boolean getPrimaryAllowed() {
			return primaryAllowed;
		}
		public void setPrimaryAllowed(Boolean primaryAllowed) {
			this.primaryAllowed = primaryAllowed;
		}
		public Boolean getResetDonorIds() {
			return resetDonorIds;
		}
		public void setResetDonorIds(Boolean resetDonorIds) {
			this.resetDonorIds = resetDonorIds;
		}
		public Boolean getResetMofedIds() {
			return resetMofedIds;
		}
		public void setResetMofedIds(Boolean resetMofedIds) {
			this.resetMofedIds = resetMofedIds;
		}
		public Boolean getResetProjCoordIds() {
			return resetProjCoordIds;
		}
		public void setResetProjCoordIds(Boolean resetProjCoordIds) {
			this.resetProjCoordIds = resetProjCoordIds;
		}
		public Boolean getResetSecMinIds() {
			return resetSecMinIds;
		}
		public void setResetSecMinIds(Boolean resetSecMinIds) {
			this.resetSecMinIds = resetSecMinIds;
		}
		public List<AmpActivityContact> getImplExecutingAgencyContacts() {
			return implExecutingAgencyContacts;
		}
		public void setImplExecutingAgencyContacts(
				List<AmpActivityContact> implExecutingAgencyContacts) {
			this.implExecutingAgencyContacts = implExecutingAgencyContacts;
		}
		public String[] getPrimaryImplExecutingContIds() {
			return primaryImplExecutingContIds;
		}
		public void setPrimaryImplExecutingContIds(String[] primaryImplExecutingContIds) {
			this.primaryImplExecutingContIds = primaryImplExecutingContIds;
		}
		public Boolean getResetImplExecutingIds() {
			return resetImplExecutingIds;
		}
		public void setResetImplExecutingIds(Boolean resetImplExecutingIds) {
			this.resetImplExecutingIds = resetImplExecutingIds;
		}
		public String getTemporaryId() {
			return temporaryId;
		}
		public void setTemporaryId(String temporaryId) {
			this.temporaryId = temporaryId;
		}

        public String getPrimaryDonorContId() {
            return primaryDonorContId;
        }

        public void setPrimaryDonorContId(String primaryDonorContId) {
            this.primaryDonorContId = primaryDonorContId;
        }

        public String getPrimaryMofedContId() {
            return primaryMofedContId;
        }

        public void setPrimaryMofedContId(String primaryMofedContId) {
            this.primaryMofedContId = primaryMofedContId;
        }

        public String getPrimaryProjCoordContId() {
            return primaryProjCoordContId;
        }

        public void setPrimaryProjCoordContId(String primaryProjCoordContId) {
            this.primaryProjCoordContId = primaryProjCoordContId;
        }

        public String getPrimarySecMinContId() {
            return primarySecMinContId;
        }

        public void setPrimarySecMinContId(String primarySecMinContId) {
            this.primarySecMinContId = primarySecMinContId;
        }

        public String getPrimaryImplExecutingContId() {
            return primaryImplExecutingContId;
        }

        public void setPrimaryImplExecutingContId(String primaryImplExecutingContId) {
            this.primaryImplExecutingContId = primaryImplExecutingContId;
        }
    }
	
	public class Contracts {
		private List contracts;
		private Double ipaBudget = null;
		private Integer selContractId;
		private String contractDetails = null;
		
		public String getContractDetails() {
			return contractDetails;
		}

		public void setContractDetails(String contractDetails) {
			this.contractDetails = contractDetails;
		}

		public List getContracts() {
			return contracts;
		}

		public Integer getSelContractId() {
			return selContractId;
		}

		public void setSelContractId(Integer selContractId) {
			this.selContractId = selContractId;
		}

		public void setContracts(List contracts) {
			this.contracts = contracts;
		}

		public Double getIpaBudget() {
			return ipaBudget;
		}

		public void setIpaBudget(Double ipaBudget) {
			this.ipaBudget = ipaBudget;
		}
	}

	public class Identification {
		
		private String ampId = null;
		//private Integer budget;
		private Long budgetCV;
		private Long budgetCVOff;
		private Long budgetCVOn;
		
		
		private AmpTeam team;
		
		private String title = null;
		private String projectComments = null;
		private String objectives = null;
		private String description = null;
		private String purpose = null;
		private String results = null;
		private String lessonsLearned = null;
		private String projectImpact = null;
		private String activitySummary = null;
		private String contractingArrangements = null;
		private String condSeq = null;
		private String linkedActivities = null;
		private String conditionality = null;
		private String projectManagement = null;
		private Long procurementSystem = new Long(0);
		private Long reportingSystem = new Long(0);
		private Long auditSystem = new Long(0);
		private Long institutions = new Long(0);
		private Long accessionInstrument = new Long(0);
		private Long projectCategory = new Long(0);
		private String govAgreementNumber;
		private String budgetCodeProjectID;
		private String budgetCheckbox;
		private Boolean governmentApprovalProcedures;
		private Boolean jointCriteria;
		private Boolean humanitarianAid;
		private String crisNumber;
		private Long selOrgs[];
		private OrgProjectId selectedOrganizations[];
		private Long acChapter = new Long(0);
		private String status = null;
		private AmpTeamMember createdBy;
		private AmpTeamMember modifiedBy;
		private AmpTeamMember approvedBy;
		private String createdDate;
		private String updatedDate;
		private Date approvalDate;
		private Boolean draft;
		private String actAthEmail;
		private String actAthAgencySource;
		private String actAthFirstName;
		private String actAthLastName;
		private String conditions;
		
		private String ssc_component_title;
		private String ssc_component_description;
		
		private String ssc_typeOfCooperation;
		private String ssc_typeOfImplementation;
		private String ssc_modalities;
		
		private String FY;
		
		private List<LabelValueBean> yearsRange;
		private String[] selectedFYs;
		private Boolean resetselectedFYs;
		
		private String vote;
		private String subVote;
		private String subProgram;
		private String projectCode;
		private String ministryCode;
		private Long gbsSbs;
		private String approvalStatus;
		private String previousApprovalStatus;
		private Boolean wasDraft;
		private String convenioNumcont;
		private Collection levelCollection = null;
		private Long activityLevel;
		private String author;
		private String clasiNPD;
		private String[] budgetCodes;
		
		//code chapitre
		private Collection<BeanWrapperImpl> chapterCodes;
		private Collection<BeanWrapperImpl> chapterYears;
		private String chapterCode;
		private AmpChapter chapterForPreview;
		private Integer chapterYear;
		private ArrayList<AmpBudgetSector>budgetsectors;
		private ArrayList<AmpOrganisation>budgetorgs;
		private ArrayList<AmpDepartments> budgetdepartments;
		private ArrayList<AmpTheme> budgetprograms;
		
		private Long selectedbudgedsector;
		private Long selectedorg;
		private Long selecteddepartment;
		private Long selectedprogram;
		
		private Long statusId = null;
		private String statusReason = null;
		
		private Long projectImplUnitId;
		public String getBudgetCheckbox() {
			return budgetCheckbox;
		}

		public void setBudgetCheckbox(String budgetCheckbox) {
			this.budgetCheckbox = budgetCheckbox;
		}
	
		public List<LabelValueBean> getYearsRange() {
			return yearsRange;
		}

		public void setYearsRange(List<LabelValueBean> yearsRange) {
			this.yearsRange = yearsRange;
		}

		public String[] getSelectedFYs() {
			return selectedFYs;
		}

		public Boolean getResetselectedFYs() {
			return resetselectedFYs;
		}

		public AmpTeamMember getModifiedBy() {
			return modifiedBy;
		}

		public void setModifiedBy(AmpTeamMember modifiedBy) {
			this.modifiedBy = modifiedBy;
		}

		public void setResetselectedFYs(Boolean resetselectedFYs) {
			this.resetselectedFYs = resetselectedFYs;
		}

		public void setSelectedFYs(String[] selectedFYs) {
			this.selectedFYs = selectedFYs;
		}

		public ArrayList<AmpOrganisation> getBudgetorgs() {
			return budgetorgs;
		}

		public void setBudgetorgs(ArrayList<AmpOrganisation> budgetorgs) {
			this.budgetorgs = budgetorgs;
		}

		public ArrayList<AmpDepartments> getBudgetdepartments() {
			return budgetdepartments;
		}

		public void setBudgetdepartments(ArrayList<AmpDepartments> budgetdepartments) {
			this.budgetdepartments = budgetdepartments;
		}

		public ArrayList<AmpTheme> getBudgetprograms() {
			return budgetprograms;
		}

		public void setBudgetprograms(ArrayList<AmpTheme> budgetprograms) {
			this.budgetprograms = budgetprograms;
		}

		public Long getSelectedorg() {
			return selectedorg;
		}

		public void setSelectedorg(Long selectedorg) {
			this.selectedorg = selectedorg;
		}

		public Long getSelecteddepartment() {
			return selecteddepartment;
		}

		public void setSelecteddepartment(Long selecteddepartment) {
			this.selecteddepartment = selecteddepartment;
		}

		public Long getSelectedprogram() {
			return selectedprogram;
		}

		public void setSelectedprogram(Long selectedprogram) {
			this.selectedprogram = selectedprogram;
		}

		public Long getSelectedbudgedsector() {
			return selectedbudgedsector;
		}

		public void setSelectedbudgedsector(Long selectedbudgedsector) {
			this.selectedbudgedsector = selectedbudgedsector;
		}

		public ArrayList<AmpBudgetSector> getBudgetsectors() {
			return budgetsectors;
		}

		public void setBudgetsectors(ArrayList<AmpBudgetSector> budgetsectors) {
			this.budgetsectors = budgetsectors;
		}

		public String[] getBudgetCodes() {
			return budgetCodes;
		}

		public void setBudgetCodes(String[] budgetCodes) {
			this.budgetCodes = budgetCodes;
		}

		public String getApprovalStatus() {
			return approvalStatus;
		}

		public void setApprovalStatus(String approvalStatus) {
			this.approvalStatus = approvalStatus;
		}

		public String getFY() {
			return FY;
		}

		public void setFY(String fy) {
			FY = fy;
		}

		public String getVote() {
			return vote;
		}

		public void setVote(String vote) {
			this.vote = vote;
		}

		public String getSubVote() {
			return subVote;
		}

		public void setSubVote(String subVote) {
			this.subVote = subVote;
		}

		public String getSubProgram() {
			return subProgram;
		}

		public void setSubProgram(String subProgram) {
			this.subProgram = subProgram;
		}
	
		
		public String getProjectCode() {
			return projectCode;
		}

		public void setProjectCode(String projectCode) {
			this.projectCode = projectCode;
		}
		
		public String getMinistryCode() {
			return ministryCode;
		}

		public void setMinistryCode(String ministryCode) {
			this.ministryCode = ministryCode;
		}

		public Long getGbsSbs() {
			return gbsSbs;
		}

		public void setGbsSbs(Long gbsSbs) {
			this.gbsSbs = gbsSbs;
		}

		public String getConditions() {
			return conditions;
		}

		public void setConditions(String conditions) {
			this.conditions = conditions;
		}

		public String getActAthFirstName() {
			return actAthFirstName;
		}

		public void setActAthFirstName(String actAthFirstName) {
			this.actAthFirstName = actAthFirstName;
		}

		public String getActAthLastName() {
			return actAthLastName;
		}

		public void setActAthLastName(String actAthLastName) {
			this.actAthLastName = actAthLastName;
		}

		public String getActAthEmail() {
			return actAthEmail;
		}

		public void setActAthEmail(String actAthEmail) {
			this.actAthEmail = actAthEmail;
		}


		public String getActAthAgencySource() {
			return actAthAgencySource;
		}

		public void setActAthAgencySource(String actAthAgencySource) {
			this.actAthAgencySource = actAthAgencySource;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public AmpTeamMember getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(AmpTeamMember createdBy) {
			this.createdBy = createdBy;
		}	

		public AmpTeamMember getApprovedBy() {
			return approvedBy;
		}

		public void setApprovedBy(AmpTeamMember approvedBy) {
			this.approvedBy = approvedBy;
		}

		public String getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(String createdDate) {
			this.createdDate = createdDate;
		}

		public String getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(String updatedDate) {
			this.updatedDate = updatedDate;
		}
		
		public Date getApprovalDate() {
			return approvalDate;
		}

		public void setApprovalDate(Date approvalDate) {
			this.approvalDate = approvalDate;
		}

		public Boolean getDraft() {
			return draft;
		}

		public String getSsc_component_title()
		{
			return ssc_component_title;
		}

		public void setSsc_component_title(String value)
		{
			this.ssc_component_title = value;
		}
	
		public String getSsc_component_description()
		{
			return ssc_component_description;
		}

		public void setSsc_component_description(String value)
		{
			this.ssc_component_description = value;
		}

		public void setDraft(Boolean draft) {
			this.draft = draft;
		}

		public void setSelectedOrganizations(int index, OrgProjectId orgProjectId) {
			selectedOrganizations[index] = orgProjectId;
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

		public Boolean getHumanitarianAid() {
			return humanitarianAid;
		}

		public void setHumanitarianAid(Boolean humanitarianAid) {
			this.humanitarianAid = humanitarianAid;
		}

		public String getCrisNumber() {
			return crisNumber;
		}

		public void setCrisNumber(String crisNumber) {
			this.crisNumber = crisNumber;
		}

		public OrgProjectId[] getSelectedOrganizations() {
			return selectedOrganizations;
		}

		public void setSelectedOrganizations(OrgProjectId[] selectedOrganizations) {
			this.selectedOrganizations = selectedOrganizations;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getObjectives() {
			return objectives;
		}

		public void setObjectives(String objectives) {
			this.objectives = objectives;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getPurpose() {
			return purpose;
		}

		public void setPurpose(String purpose) {
			this.purpose = purpose;
		}

		public String getResults() {
			return results;
		}

		public void setResults(String results) {
			this.results = results;
		}

		public String getLessonsLearned() {
			return lessonsLearned;
		}

		public void setLessonsLearned(String lessonsLearned) {
			this.lessonsLearned = lessonsLearned;
		}

		public String getProjectImpact() {
			return projectImpact;
		}

		public void setProjectImpact(String projectImpact) {
			this.projectImpact = projectImpact;
		}

		public String getActivitySummary() {
			return activitySummary;
		}

		public void setActivitySummary(String activitySummary) {
			this.activitySummary = activitySummary;
		}

		public String getContractingArrangements() {
			return contractingArrangements;
		}

		public void setContractingArrangements(String contractingArrangements) {
			this.contractingArrangements = contractingArrangements;
		}

		public String getCondSeq() {
			return condSeq;
		}

		public void setCondSeq(String condSeq) {
			this.condSeq = condSeq;
		}

		public String getLinkedActivities() {
			return linkedActivities;
		}

		public void setLinkedActivities(String linkedActivities) {
			this.linkedActivities = linkedActivities;
		}

		public String getConditionality() {
			return conditionality;
		}

		public void setConditionality(String conditionality) {
			this.conditionality = conditionality;
		}

		public String getProjectManagement() {
			return projectManagement;
		}

		public void setProjectManagement(String projectManagement) {
			this.projectManagement = projectManagement;
		}

		public Long getProcurementSystem() {
			return procurementSystem;
		}

		public void setProcurementSystem(Long procurementSystem) {
			this.procurementSystem = procurementSystem;
		}

		public Long getReportingSystem() {
			return reportingSystem;
		}

		public void setReportingSystem(Long reportingSystem) {
			this.reportingSystem = reportingSystem;
		}

		public Long getAuditSystem() {
			return auditSystem;
		}

		public void setAuditSystem(Long auditSystem) {
			this.auditSystem = auditSystem;
		}

		public Long getInstitutions() {
			return institutions;
		}

		public void setInstitutions(Long institutions) {
			this.institutions = institutions;
		}

		public Long getAccessionInstrument() {
			return accessionInstrument;
		}

		public void setAccessionInstrument(Long accessionInstrument) {
			this.accessionInstrument = accessionInstrument;
		}

		public Long getProjectCategory() {
			return projectCategory;
		}

		public void setProjectCategory(Long projectCategory) {
			this.projectCategory = projectCategory;
		}

		public String getGovAgreementNumber() {
			return govAgreementNumber;
		}

		public void setGovAgreementNumber(String govAgreementNumber) {
			this.govAgreementNumber = govAgreementNumber;
		}

		public Long getAcChapter() {
			return acChapter;
		}

		public void setAcChapter(Long acChapter) {
			this.acChapter = acChapter;
		}

		public String getBudgetCodeProjectID() {
			return budgetCodeProjectID;
		}

		public void setBudgetCodeProjectID(String budgetCodeProjectID) {
			this.budgetCodeProjectID = budgetCodeProjectID;
		}

	

		public String getAmpId() {
			return ampId;
		}

		public void setAmpId(String ampId) {
			this.ampId = ampId;
		}

//		public Integer getBudget() {
//			return budget;
//		}
//
//		public void setBudget(Integer budget) {
//			this.budget = budget;
//		}
		
		
		public Long getBudgetCV() {
			return budgetCV;
		}
		
		public void setBudgetCV(Long budgetCV) {
			this.budgetCV = budgetCV;
		}
		

		public Long getBudgetCVOff() {
			return budgetCVOff;
		}

		public void setBudgetCVOff(Long budgetCVOff) {
			this.budgetCVOff = budgetCVOff;
		}

		public Long getBudgetCVOn() {
			return budgetCVOn;
		}

		public void setBudgetCVOn(Long budgetCVOn) {
			this.budgetCVOn = budgetCVOn;
		}

		public AmpTeam getTeam() {
			return team;
		}

		public void setTeam(AmpTeam team) {
			this.team = team;
		}

		public Boolean getWasDraft() {
			return wasDraft;
		}

		public void setWasDraft(Boolean wasDraft) {
			this.wasDraft = wasDraft;
		}

		public String getConvenioNumcont() {
			return convenioNumcont;
		}

		public void setConvenioNumcont(String convenioNumcont) {
			this.convenioNumcont = convenioNumcont;
		}

		public Collection getLevelCollection() {
			return levelCollection;
		}

		public void setLevelCollection(Collection levelCollection) {
			this.levelCollection = levelCollection;
		}

		public Long getActivityLevel() {
			return activityLevel;
		}

		public void setActivityLevel(Long activityLevel) {
			this.activityLevel = activityLevel;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getClasiNPD() {
			return clasiNPD;
		}

		public void setClasiNPD(String clasiNPD) {
			this.clasiNPD = clasiNPD;
		}

		public Long[] getSelOrgs() {
			return selOrgs;
		}

		public void setSelOrgs(Long[] selOrgs) {
			this.selOrgs = selOrgs;
		}

		public String getProjectComments() {
			return this.projectComments;
		}

		public void setProjectComments(String projectComments) {
			this.projectComments = projectComments;
		}

		/**
		 * @return the previousApprovalStatus
		 */
		public String getPreviousApprovalStatus() {
			return previousApprovalStatus;
		}

		/**
		 * @param previousApprovalStatus the previousApprovalStatus to set
		 */
		public void setPreviousApprovalStatus(String previousApprovalStatus) {
			this.previousApprovalStatus = previousApprovalStatus;
		}
		
	
		/**
		 * @return the chapterCode
		 */
		public String getChapterCode() {
			return chapterCode;
		}

		/**
		 * @param chapterCode the chapterCode to set
		 */
		public void setChapterCode(String chapterCode) {
			this.chapterCode = chapterCode;
		}

		/**
		 * @return the chapterYear
		 */
		public Integer getChapterYear() {
			return chapterYear;
		}

		/**
		 * @param chapterYear the chapterYear to set
		 */
		public void setChapterYear(Integer chapterYear) {
			this.chapterYear = chapterYear;
		}

		/**
		 * @return the chapterCodes
		 */
		public Collection<BeanWrapperImpl> getChapterCodes() {
			return chapterCodes;
		}

		/**
		 * @param chapterCodes the chapterCodes to set
		 */
		public void setChapterCodes(Collection<BeanWrapperImpl> chapterCodes) {
			this.chapterCodes = chapterCodes;
		}

		/**
		 * @return the chapterYears
		 */
		public Collection<BeanWrapperImpl> getChapterYears() {
			return chapterYears;
		}

		/**
		 * @param chapterYears the chapterYears to set
		 */
		public void setChapterYears(Collection<BeanWrapperImpl> chapterYears) {
			this.chapterYears = chapterYears;
		}

		/**
		 * @return the chapterForPreview
		 */
		public AmpChapter getChapterForPreview() {
			return chapterForPreview;
		}

		/**
		 * @param chapterForPreview the chapterForPreview to set
		 */
		public void setChapterForPreview(AmpChapter chapterForPreview) {
			this.chapterForPreview = chapterForPreview;
		}

		public Long getStatusId() {
			return statusId;
		}

		public void setStatusId(Long statusId) {
			this.statusId = statusId;
		}

		public String getStatusReason() {
			return statusReason;
		}

		public void setStatusReason(String statusReason) {
			this.statusReason = statusReason;
		}

		public Long getProjectImplUnitId() {
			return projectImplUnitId;
		}

		public void setProjectImplUnitId(Long projectImplUnitId) {
			this.projectImplUnitId = projectImplUnitId;
		}

		public String getSsc_typeOfCooperation() {
			return ssc_typeOfCooperation;
		}

		public void setSsc_typeOfCooperation(String ssc_typeOfCooperation) {
			this.ssc_typeOfCooperation = ssc_typeOfCooperation;
		}

		public String getSsc_typeOfImplementation() {
			return ssc_typeOfImplementation;
		}

		public void setSsc_typeOfImplementation(String ssc_typeOfImplementation) {
			this.ssc_typeOfImplementation = ssc_typeOfImplementation;
		}

		public String getSsc_modalities() {
			return ssc_modalities;
		}

		public void setSsc_modalities(String ssc_modalities) {
			this.ssc_modalities = ssc_modalities;
		}

	}

	public class Planning {

		private Collection actRankCollection;
		private String lineMinRank;
		private String planMinRank;
		private String originalAppDate;
		private String revisedAppDate;
		private String originalStartDate;
		private String revisedStartDate;
		private String contractingDate;
		private String disbursementsDate;
		private String proposedCompDate;
		private String originalCompDate;
		private String currentCompDate;
		private String revisedCompDate;
		private Collection activityCloseDates;
		private Long creditTypeId;
		
		public BigDecimal getProjectPeriod(){
			BigDecimal projectPeriod=null;
			Date actualStartDate=DateConversion.getDate(this.revisedStartDate);
			Date proposedCompletionDate=DateConversion.getDate(this.proposedCompDate);
			Date currentCompletionDate=DateConversion.getDate(this.currentCompDate);
			try {
				if(actualStartDate!=null && currentCompletionDate!=null){
					projectPeriod=MathExpression.getMonthDifference(new BigDecimal(currentCompletionDate.getTime()),new BigDecimal(actualStartDate.getTime()));					
				}else
				if(actualStartDate!=null&&proposedCompletionDate!=null){
					projectPeriod=MathExpression.getMonthDifference(new BigDecimal(proposedCompletionDate.getTime()),new BigDecimal(actualStartDate.getTime()));		
				}
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return projectPeriod;
		}

		public String getRevisedCompDate() {
			return revisedCompDate;
		}

		public void setRevisedCompDate(String revisedCompDate) {
			this.revisedCompDate = revisedCompDate;
		}

		public Collection getActivityCloseDates() {
			return activityCloseDates;
		}

		public void setActivityCloseDates(Collection activityCloseDates) {
			this.activityCloseDates = activityCloseDates;
		}

		public String getLineMinRank() {
			return lineMinRank;
		}

		public void setLineMinRank(String lineMinRank) {
			this.lineMinRank = lineMinRank;
		}

		public String getPlanMinRank() {
			return planMinRank;
		}

		public void setPlanMinRank(String planMinRank) {
			this.planMinRank = planMinRank;
		}

		public String getOriginalAppDate() {
			return originalAppDate;
		}

		public void setOriginalAppDate(String originalAppDate) {
			this.originalAppDate = originalAppDate;
		}

		public String getRevisedAppDate() {
			return revisedAppDate;
		}

		public void setRevisedAppDate(String revisedAppDate) {
			this.revisedAppDate = revisedAppDate;
		}
		
		public String getOriginalCompDate() {
			return originalCompDate;
		}

		public void setOriginalCompDate(String originalCompDate) {
			this.originalCompDate = originalCompDate;
		}

		public String getOriginalStartDate() {
			return originalStartDate;
		}

		public void setOriginalStartDate(String originalStartDate) {
			this.originalStartDate = originalStartDate;
		}

		public String getRevisedStartDate() {
			return revisedStartDate;
		}

		public void setRevisedStartDate(String revisedStartDate) {
			this.revisedStartDate = revisedStartDate;
		}

		public String getContractingDate() {
			return contractingDate;
		}

		public void setContractingDate(String contractingDate) {
			this.contractingDate = contractingDate;
		}

		public String getDisbursementsDate() {
			return disbursementsDate;
		}

		public void setDisbursementsDate(String disbursementsDate) {
			this.disbursementsDate = disbursementsDate;
		}

		public String getProposedCompDate() {
			return proposedCompDate;
		}

		public void setProposedCompDate(String proposedCompDate) {
			this.proposedCompDate = proposedCompDate;
		}

		public String getCurrentCompDate() {
			return currentCompDate;
		}

		public void setCurrentCompDate(String currentCompDate) {
			this.currentCompDate = currentCompDate;
		}

		public Collection getActRankCollection() {
			return actRankCollection;
		}

		public void setActRankCollection(Collection actRankCollection) {
			this.actRankCollection = actRankCollection;
		}

		public Long getCreditTypeId() {
			return creditTypeId;
		}

		public void setCreditTypeId(Long creditTypeId) {
			this.creditTypeId = creditTypeId;
		}

	}

	public class Location {
		private String keyword;
		private int tempNumResults;
		private int numResults;
		private int currentPage;
		private Collection<Integer> pages;
		private Collection cols;
		private Collection pagedCol;
		
		private boolean locationReset;
		private Long selLocs[] = null; // location selected from step 2 page to
		private Collection searchLocs = null; // list of searched locations.
		private Long searchedLocs[] = null; // locations selected by user to be
		private String fill; // which among countries,region,zone and woreda
		// need
		// to
		// be loaded with data. ie if the value

		// of fill is 'region', load all region data beloning to a particluar
		// country selected
		private Integer impLevelValue; // Implementation Level value
		private String impCountry; // Implementation country
		
		private TreeMap<Integer, Collection<KeyValue>> locationByLayers;
		
		private TreeMap<Integer, Long> selectedLayers ;
		
		private Long parentLocId;
		
		private Long [] userSelectedLocs;
		
		@Deprecated
		private Long impRegion; // Implementation region
		@Deprecated
		private Long impMultiRegion[]; // Implementation region

		@Deprecated
		private Long impZone; // Implementation zone
		@Deprecated
		private Long impMultiZone[]; // Implementation zone

		@Deprecated
		private Long impWoreda; // Implementation woreda
		@Deprecated
		private Long impMultiWoreda[];
		
		@Deprecated
		private Collection<Country> countries;
		@Deprecated
		private Collection<AmpRegion> regions;
		@Deprecated
		private Collection<AmpZone> zones;
		@Deprecated
		private Collection<AmpWoreda> woredas;

		private String country;
		private Long levelId = null;
        private int levelIdx;
		private Long implemLocationLevel = null;
		private Collection<org.digijava.module.aim.helper.Location> selectedLocs = null;
		private boolean defaultCountryIsSet;
		private int pagesSize;
		
		private boolean noMoreRecords=false;

        public int getLevelIdx() {
            return levelIdx;
        }

        public void setLevelIdx(int levelIdx) {
            this.levelIdx = levelIdx;
        }

        public boolean isNoMoreRecords() {
			return noMoreRecords;
		}

		public void setNoMoreRecords(boolean noMoreRecords) {
			this.noMoreRecords = noMoreRecords;
		}

		public Long getLevelId() {
			return levelId;
		}

		public void setLevelId(Long levelId) {
			this.levelId = levelId;
		}

		public Long getImplemLocationLevel() {
			return implemLocationLevel;
		}

		public void setImplemLocationLevel(Long implemLocationLevel) {
			this.implemLocationLevel = implemLocationLevel;
		}


		public Collection<org.digijava.module.aim.helper.Location> getSelectedLocs() {
			return selectedLocs;
		}

		public void setSelectedLocs(Collection<org.digijava.module.aim.helper.Location> selectedLocs) {
			this.selectedLocs = selectedLocs;
		}

		public boolean isLocationReset() {
			return locationReset;
		}

		public void setLocationReset(boolean locationReset) {
			this.locationReset = locationReset;
		}

		public Long[] getSelLocs() {
			return selLocs;
		}

		public void setSelLocs(Long[] selLocs) {
			this.selLocs = selLocs;
		}

		public Collection getSearchLocs() {
			return searchLocs;
		}

		public void setSearchLocs(Collection searchLocs) {
			this.searchLocs = searchLocs;
		}

		public Long[] getSearchedLocs() {
			return searchedLocs;
		}

		public void setSearchedLocs(Long[] searchedLocs) {
			this.searchedLocs = searchedLocs;
		}

		public String getFill() {
			return fill;
		}

		public void setFill(String fill) {
			this.fill = fill;
		}

		public Integer getImpLevelValue() {
			return impLevelValue;
		}

		public void setImpLevelValue(Integer impLevelValue) {
			this.impLevelValue = impLevelValue;
		}

		public String getImpCountry() {
			return impCountry;
		}

		public void setImpCountry(String impCountry) {
			this.impCountry = impCountry;
		}

		public Long getImpRegion() {
			return impRegion;
		}

		public void setImpRegion(Long impRegion) {
			this.impRegion = impRegion;
		}

		public Long[] getImpMultiRegion() {
			return impMultiRegion;
		}

		public void setImpMultiRegion(Long[] impMultiRegion) {
			this.impMultiRegion = impMultiRegion;
		}

		public Long getImpZone() {
			return impZone;
		}

		public void setImpZone(Long impZone) {
			this.impZone = impZone;
		}

		public Long[] getImpMultiZone() {
			return impMultiZone;
		}

		public void setImpMultiZone(Long[] impMultiZone) {
			this.impMultiZone = impMultiZone;
		}

		public Long getImpWoreda() {
			return impWoreda;
		}

		public void setImpWoreda(Long impWoreda) {
			this.impWoreda = impWoreda;
		}

		public Long[] getImpMultiWoreda() {
			return impMultiWoreda;
		}

		public void setImpMultiWoreda(Long[] impMultiWoreda) {
			this.impMultiWoreda = impMultiWoreda;
		}

		

		public Collection<Country> getCountries() {
			return countries;
		}

		public void setCountries(Collection<Country> countries) {
			this.countries = countries;
		}

		public Collection<AmpRegion> getRegions() {
			return regions;
		}

		public void setRegions(Collection<AmpRegion> regions) {
			this.regions = regions;
		}

		public Collection<AmpZone> getZones() {
			return zones;
		}

		public void setZones(Collection<AmpZone> zones) {
			this.zones = zones;
		}

		public Collection<AmpWoreda> getWoredas() {
			return woredas;
		}

		public void setWoredas(Collection<AmpWoreda> woredas) {
			this.woredas = woredas;
		}

		
		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public boolean isDefaultCountryIsSet() {
			return defaultCountryIsSet;
		}

		public void setDefaultCountryIsSet(boolean defaultCountryIsSet) {
			this.defaultCountryIsSet = defaultCountryIsSet;
		}

		public int getTempNumResults() {
			return tempNumResults;
		}

		public void setTempNumResults(int tempNumResults) {
			this.tempNumResults = tempNumResults;
		}

		public int getCurrentPage() {
			return currentPage;
		}

		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}

		public String getKeyword() {
			return keyword;
		}

		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

		public int getNumResults() {
			return numResults;
		}

		public void setNumResults(int numResults) {
			this.numResults = numResults;
		}

		public Collection getCols() {
			return cols;
		}

		public void setCols(Collection cols) {
			this.cols = cols;
		}

		public Collection getPagedCol() {
			return pagedCol;
		}

		public void setPagedCol(Collection pagedCol) {
			this.pagedCol = pagedCol;
		}

		public Collection getPages() {
			return pages;
		}

		public void setPages(Collection pages) {
			this.pages = pages;
			if (pages != null) {
				this.pagesSize = pages.size();
			}
		}

		
		public TreeMap<Integer, Collection<KeyValue>> getLocationByLayers() {
			if ( locationByLayers == null )
				locationByLayers	= new TreeMap<Integer, Collection<KeyValue>>();
			return locationByLayers;
		}
		

		public TreeMap<Integer, Long> getSelectedLayers() {
			if (selectedLayers == null)
				selectedLayers		= new TreeMap<Integer, Long>();
			return selectedLayers;
		}
		
		public void setSelectedLayer(String key, Object value) {
			selectedLayers.put(Integer.parseInt(key), (Long)value);
		}
		public Long getSelectedLayer(String key) {
			return selectedLayers.get( Integer.parseInt(key) );
		}

		public Long getParentLocId() {
			return parentLocId;
		}

		public void setParentLocId(Long parentLocId) {
			this.parentLocId = parentLocId;
		}

		public Long[] getUserSelectedLocs() {
			return userSelectedLocs;
		}

		public void setUserSelectedLocs(Long[] userSelectedLocs) {
			this.userSelectedLocs = userSelectedLocs;
		}

		public void reset(ActionMapping mapping, HttpServletRequest request) {
			fill = "country";
			impLevelValue = new Integer(0);
			impCountry = "";
			impRegion = new Long(-1);
			impZone = new Long(-1);
			impWoreda = new Long(-1);
			regions = null;
			zones = null;
			woredas = null;
		}
	}

	public class Sector {
		
		private String keyword;
		private int tempNumResults;
		private int numResults;
		private int currentPage;
		private Collection<Integer> pages;
		private Collection cols;
		private Collection pagedCol;
		
		
		private Long sector;
		private Long subsectorLevel1;
		private Long subsectorLevel2;
		private Long sectorScheme;
		private Collection sectorSchemes;
		private Collection parentSectors;
		private Collection childSectorsLevel1;
		private Collection childSectorsLevel2;
		private Long selActivitySectors[];
		private List classificationConfigs;
		private String primarySectorVisible = null;
		private String secondarySectorVisible = null;
        private String tertiarySectorVisible=null;
		private Collection<ActivitySector> activitySectors;
		private Collection searchedSectors = null; // list of searched Sectors.
		private Long selSectors[] = null; // sectors selected by user to be
		// added
		private boolean sectorReset;
		private int pagesSize;

		public List getClassificationConfigs() {
			return classificationConfigs;
		}

		public void setClassificationConfigs(List classificationConfigs) {
			this.classificationConfigs = classificationConfigs;
		}

		public String getPrimarySectorVisible() {
			return primarySectorVisible;
		}

		public void setPrimarySectorVisible(String primarySectorVisible) {
			this.primarySectorVisible = primarySectorVisible;
		}

		public String getSecondarySectorVisible() {
			return secondarySectorVisible;
		}

		public void setSecondarySectorVisible(String secondarySectorVisible) {
			this.secondarySectorVisible = secondarySectorVisible;
		}
        
        public String getTertiarySectorVisible() {
            return tertiarySectorVisible;
        }

        public void setTertiarySectorVisible(String tertiarySectorVisible) {
            this.tertiarySectorVisible = tertiarySectorVisible;
        }

		public Collection<ActivitySector> getActivitySectors() {
			return activitySectors;
		}

		public void setActivitySectors(Collection<ActivitySector> activitySectors) {
			this.activitySectors = activitySectors;
		}

		public Long getSector() {
			return sector;
		}

		public void setSector(Long sector) {
			this.sector = sector;
		}

		public Long getSubsectorLevel1() {
			return subsectorLevel1;
		}

		public void setSubsectorLevel1(Long subsectorLevel1) {
			this.subsectorLevel1 = subsectorLevel1;
		}

		public Long getSubsectorLevel2() {
			return subsectorLevel2;
		}

		public void setSubsectorLevel2(Long subsectorLevel2) {
			this.subsectorLevel2 = subsectorLevel2;
		}

		public Long getSectorScheme() {
			return sectorScheme;
		}

		public void setSectorScheme(Long sectorScheme) {
			this.sectorScheme = sectorScheme;
		}

		public Collection getSectorSchemes() {
			return sectorSchemes;
		}

		public void setSectorSchemes(Collection sectorSchemes) {
			this.sectorSchemes = sectorSchemes;
		}

		public Collection getParentSectors() {
			return parentSectors;
		}

		public void setParentSectors(Collection parentSectors) {
			this.parentSectors = parentSectors;
		}

		public Collection getChildSectorsLevel1() {
			return childSectorsLevel1;
		}

		public void setChildSectorsLevel1(Collection childSectorsLevel1) {
			this.childSectorsLevel1 = childSectorsLevel1;
		}

		public Collection getChildSectorsLevel2() {
			return childSectorsLevel2;
		}

		public void setChildSectorsLevel2(Collection childSectorsLevel2) {
			this.childSectorsLevel2 = childSectorsLevel2;
		}

		public Long[] getSelActivitySectors() {
			return selActivitySectors;
		}

		public void setSelActivitySectors(Long[] selActivitySectors) {
			this.selActivitySectors = selActivitySectors;
		}

		public Collection getSearchedSectors() {
			return searchedSectors;
		}

		public void setSearchedSectors(Collection searchedSectors) {
			this.searchedSectors = searchedSectors;
		}

		public Long[] getSelSectors() {
			return selSectors;
		}

		public void setSelSectors(Long[] selSectors) {
			this.selSectors = selSectors;
		}

		public boolean isSectorReset() {
			return sectorReset;
		}

		public void setSectorReset(boolean sectorReset) {
			this.sectorReset = sectorReset;
		}

		public String getKeyword() {
			return keyword;
		}

		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

		public int getTempNumResults() {
			return tempNumResults;
		}

		public void setTempNumResults(int tempNumResults) {
			this.tempNumResults = tempNumResults;
		}

		public int getNumResults() {
			return numResults;
		}

		public void setNumResults(int numResults) {
			this.numResults = numResults;
		}

		public int getCurrentPage() {
			return currentPage;
		}

		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}

		public Collection<Integer> getPages() {
			return pages;
		}

		public void setPages(Collection pages) {
			this.pages = pages;
			if (pages != null) {
				this.pagesSize = pages.size();
			}
		}

		public Collection getCols() {
			return cols;
		}

		public void setCols(Collection cols) {
			this.cols = cols;
		}

		public Collection getPagedCol() {
			return pagedCol;
		}

		public void setPagedCol(Collection pagedCol) {
			this.pagedCol = pagedCol;
		}

	}

	public class Component {
		private Collection activityComponentes;
		private String multiSectorSelecting;
		private Long selActivityComponentes[];
		private ArrayList<AmpComponentType> allCompsType;
		private Long selectedType;
		private String newCompoenentName;
		private Collection allComps;
		private boolean componentReset;
		private String componentTitle;
		private String componentDesc;
		private String componentAmount;
		private Long componentId;
		private double compTotalDisb;
		private Collection<Components<FundingDetail>> selectedComponents;
		private Long[] selComp;
		private Long[] selCompSectors;
		private String currencyCode;
		private String componentRepDate;
        private String fundingCurrCode;

        public String getFundingCurrCode() {
            return fundingCurrCode;
        }

        public void setFundingCurrCode(String fundingCurrCode) {
            this.fundingCurrCode = fundingCurrCode;
        }

		public Long getComponentId() {
			return componentId;
		}

		public void setComponentId(Long componentId) {
			this.componentId = componentId;
		}

		public double getCompTotalDisb() {
			return compTotalDisb;
		}

		public void setCompTotalDisb(double compTotalDisb) {
			this.compTotalDisb = compTotalDisb;
		}

		public Collection<Components<FundingDetail>> getSelectedComponents() {
			return selectedComponents;
		}

		public void setSelectedComponents(Collection<Components<FundingDetail>> selectedComponents) {
			this.selectedComponents = selectedComponents;
		}

		public Long[] getSelComp() {
			return selComp;
		}

		public void setSelComp(Long[] selComp) {
			this.selComp = selComp;
		}

		public ArrayList<AmpComponentType> getAllCompsType() {
			return allCompsType;
		}

		public void setAllCompsType(ArrayList<AmpComponentType> allCompsType) {
			this.allCompsType = allCompsType;
		}

		public Long getSelectedType() {
			return selectedType;
		}

		public void setSelectedType(Long selectedType) {
			this.selectedType = selectedType;
		}

		public String getNewCompoenentName() {
			return newCompoenentName;
		}

		public void setNewCompoenentName(String newCompoenentName) {
			this.newCompoenentName = newCompoenentName;
		}

		public Collection getAllComps() {
			return allComps;
		}

		public void setAllComps(Collection allComps) {
			this.allComps = allComps;
		}

		public boolean isComponentReset() {
			return componentReset;
		}

		public void setComponentReset(boolean componentReset) {
			this.componentReset = componentReset;
		}

		public String getComponentTitle() {
			return componentTitle;
		}

		public void setComponentTitle(String componentTitle) {
			this.componentTitle = componentTitle;
		}

		public String getComponentDesc() {
			return componentDesc;
		}

		public void setComponentDesc(String componentDesc) {
			this.componentDesc = componentDesc;
		}

		public String getComponentAmount() {
			return componentAmount;
		}

		public void setComponentAmount(String componentAmount) {
			this.componentAmount = componentAmount;
		}

		public Long[] getSelActivityComponentes() {
			return selActivityComponentes;
		}

		public void setSelActivityComponentes(Long[] selActivityComponentes) {
			this.selActivityComponentes = selActivityComponentes;
		}

		public Collection getActivityComponentes() {
			return activityComponentes;
		}

		public void setActivityComponentes(Collection activityComponentes) {
			this.activityComponentes = activityComponentes;
		}

		public String getMultiSectorSelecting() {
			return multiSectorSelecting;
		}

		public void setMultiSectorSelecting(String multiSectorSelecting) {
			this.multiSectorSelecting = multiSectorSelecting;
		}

		public Long[] getSelCompSectors() {
			return selCompSectors;
		}

		public void setSelCompSectors(Long[] selCompSectors) {
			this.selCompSectors = selCompSectors;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public String getComponentRepDate() {
			return componentRepDate;
		}

		public void setComponentRepDate(String componentRepDate) {
			this.componentRepDate = componentRepDate;
		}

	}

	public class Issues {

		private ArrayList<org.digijava.module.aim.helper.Issues> issues;
		
		private String issue;
		private String measure;
		private String actor;		
		
		private Long issueId;
		private Long measureId;
		private Long actorId;
		
		private String issueDate;
		
		private ArrayList<Long> deletedObservations;
		private ArrayList<Long> deletedMeasures;
		private ArrayList<Long> deletedActors;
		

		public String getActor() {
			return actor;
		}

		public void setActor(String actor) {
			this.actor = actor;
		}

		public Long getActorId() {
			return actorId;
		}

		public void setActorId(Long actorId) {
			this.actorId = actorId;
		}

		public String getIssue() {
			return issue;
		}

		public void setIssue(String issue) {
			this.issue = issue;
		}

		public ArrayList<org.digijava.module.aim.helper.Issues>  getIssues() {
			return issues;
		}

		public void setIssues(ArrayList<org.digijava.module.aim.helper.Issues>  issues) {
			this.issues = issues;
		}

		public String getIssueDate() {
			return issueDate;
		}

		public void setIssueDate(String issueDate) {
			this.issueDate = issueDate;
		}


		public String getMeasure() {
			return measure;
		}

		public void setMeasure(String measure) {
			this.measure = measure;
		}

		public Long getIssueId() {
			return issueId;
		}

		public void setIssueId(Long issueId) {
			this.issueId = issueId;
		}

		public Long getMeasureId() {
			return measureId;
		}

		public void setMeasureId(Long measureId) {
			this.measureId = measureId;
		}

		public ArrayList<Long> getDeletedObservations() {
			if (this.deletedObservations == null) {
				this.deletedObservations = new ArrayList();
			}
			return deletedObservations;
		}

		public void setDeletedObservations(ArrayList<Long> deletedObservations) {
			this.deletedObservations = deletedObservations;
		}

		public ArrayList<Long> getDeletedMeasures() {
			if (this.deletedMeasures == null) {
				this.deletedMeasures = new ArrayList();
			}
			return deletedMeasures;
		}

		public void setDeletedMeasures(ArrayList<Long> deletedMeasures) {
			this.deletedMeasures = deletedMeasures;
		}

		public ArrayList<Long> getDeletedActors() {
			if (this.deletedActors == null) {
				this.deletedActors = new ArrayList();
			}
			return deletedActors;
		}

		public void setDeletedActors(ArrayList<Long> deletedActors) {
			this.deletedActors = deletedActors;
		}

	}

	public class Programs { 
		private int programType;
		private Long[] selectedNPOPrograms;
		private Long[] selectedPPrograms;
		private Long[] selectedSPrograms;
		private List programLevels;
		private Long selPrograms[];
		private Long selProgramId;

		private List actPrograms;
		private Collection programCollection;
		private Long selectedPrograms[];
		private Long program;
		private String programDescription;
		private AmpActivityProgramSettings nationalSetting;
		private List nationalPlanObjectivePrograms;
		private AmpActivityProgramSettings primarySetting;
		private List primaryPrograms;
		private List secondaryPrograms;
		private List tertiaryPrograms;
		private AmpActivityProgramSettings secondarySetting;
		private AmpActivityProgramSettings tertiarySetting;

		private String visibleProgram = null;

		public AmpActivityProgramSettings getNationalSetting() {
			return nationalSetting;
		}

		public void setNationalSetting(AmpActivityProgramSettings nationalSetting) {
			this.nationalSetting = nationalSetting;
		}

		public List getNationalPlanObjectivePrograms() {
			return nationalPlanObjectivePrograms;
		}

		public void setNationalPlanObjectivePrograms(List nationalPlanObjectivePrograms) {
			this.nationalPlanObjectivePrograms = nationalPlanObjectivePrograms;
		}

		public AmpActivityProgramSettings getPrimarySetting() {
			return primarySetting;
		}

		public void setPrimarySetting(AmpActivityProgramSettings primarySetting) {
			this.primarySetting = primarySetting;
		}

		public List getPrimaryPrograms() {
			return primaryPrograms;
		}

		public void setPrimaryPrograms(List primaryPrograms) {
			this.primaryPrograms = primaryPrograms;
		}

		public AmpActivityProgramSettings getSecondarySetting() {
			return secondarySetting;
		}

		public void setSecondarySetting(AmpActivityProgramSettings secondarySetting) {
			this.secondarySetting = secondarySetting;
		}

		public List getSecondaryPrograms() {
			return secondaryPrograms;
		}

		public void setSecondaryPrograms(List secondaryPrograms) {
			this.secondaryPrograms = secondaryPrograms;
		}
        
		public List getTertiaryPrograms() {
			return tertiaryPrograms;
		}

		public void setTertiaryPrograms(List tertiaryPrograms) {
			this.tertiaryPrograms = tertiaryPrograms;
		}
		
		public AmpActivityProgramSettings getTertiarySetting() {
			return tertiarySetting;
		}

		public void setTertiarySetting(AmpActivityProgramSettings tertiarySetting) {
			this.tertiarySetting = tertiarySetting;
		}
		
		public Long getProgram() {
			return program;
		}

		public void setProgram(Long program) {
			this.program = program;
		}

		public String getProgramDescription() {
			return programDescription;
		}

		public void setProgramDescription(String programDescription) {
			this.programDescription = programDescription;
		}

		public List getProgramLevels() {
			return programLevels;
		}

		public void setProgramLevels(List programLevels) {
			this.programLevels = programLevels;
		}

		public Long[] getSelPrograms() {
			return selPrograms;
		}

		public void setSelPrograms(Long[] selPrograms) {
			this.selPrograms = selPrograms;
		}

		public Long getSelProgramId() {
			return selProgramId;
		}

		public void setSelProgramId(Long selProgramId) {
			this.selProgramId = selProgramId;
		}

		public List getActPrograms() {
			return actPrograms;
		}

		public void setActPrograms(List actPrograms) {
			this.actPrograms = actPrograms;
		}

		public Collection getProgramCollection() {
			return programCollection;
		}

		public void setProgramCollection(Collection programCollection) {
			this.programCollection = programCollection;
		}

		public Long[] getSelectedPrograms() {
			return selectedPrograms;
		}

		public void setSelectedPrograms(Long[] selectedPrograms) {
			this.selectedPrograms = selectedPrograms;
		}

		public int getProgramType() {
			return programType;
		}

		public void setProgramType(int programType) {
			this.programType = programType;
		}

		public Long[] getSelectedNPOPrograms() {
			return selectedNPOPrograms;
		}

		public void setSelectedNPOPrograms(Long[] selectedNPOPrograms) {
			this.selectedNPOPrograms = selectedNPOPrograms;
		}

		public Long[] getSelectedPPrograms() {
			return selectedPPrograms;
		}

		public void setSelectedPPrograms(Long[] selectedPPrograms) {
			this.selectedPPrograms = selectedPPrograms;
		}

		public Long[] getSelectedSPrograms() {
			return selectedSPrograms;
		}

		public void setSelectedSPrograms(Long[] selectedSPrograms) {
			this.selectedSPrograms = selectedSPrograms;
		}

		

	}

	public class CrossCuttingIssues {
		private String equalOpportunity;
		private String environment;
		private String minorities;

		public String getEqualOpportunity() {
			return equalOpportunity;
		}

		public void setEqualOpportunity(String equalOpportunity) {
			this.equalOpportunity = equalOpportunity;
		}

		public String getEnvironment() {
			return environment;
		}

		public void setEnvironment(String environment) {
			this.environment = environment;
		}

		public String getMinorities() {
			return minorities;
		}

		public void setMinorities(String minorities) {
			this.minorities = minorities;
		}

	}

	public class Funding {
		private ProposedProjCost proProjCost;
		private Collection<FundingOrganization> fundingOrganizations;
		private String donorObjective;
		private List<FundingDetail> fundingDetails;

		private String totalCommitted = "";
		private String totalDisbursed = "";
		private String totalUnDisbursed = "";
		private String totalExpenditure = "";
		private String totalUnExpended = "";
		private String totalExpended = "";
		private String totalDisbOrder = "";

		private String totalCommitments;
		private String totalPlannedCommitments;
		private String totalPlannedRoF;
		private String totalPlannedEDD;		
		private String totalActualRoF;
		private String totalActualEDD;
		
		private String totalPipelineCommitments;
		private double totalCommitmentsDouble;
		private String totalDisbursements;
		private String totalExpenditures;
		private String totalPlannedExpenditures;
		private String totalPlannedDisbursements;
		private String totalPlannedDisbursementsOrders;
		private String totalActualDisbursementsOrders;
		private String unDisbursementsBalance;
		private boolean fixerate;
		private double regionTotalDisb;
		private Collection orderedFundingOrganizations;
		private Collection financingBreakdown = null;
		
		private String consumptionRate;
		private String deliveryRate;

		// Add Funding fields

		private Collection organizations;
		private Collection<AmpCurrency> validcurrencies;
		private Collection<FundingPledges> pledgeslist;
		private boolean dupFunding;
		private String orgName;
		
		private Long assistanceType = null;  // this is id of a category value from category Type Of Assistance
		private Long modality = null; // this is id of a category value from category Financing Instrument
		private Long fundingStatus = null; // this is id of a category value from category Funding Status
		private Long modeOfPayment = null; // this is id of a category value from category Mode Of Payment
		
		private List<MTEFProjection> fundingMTEFProjections;
		private List<KeyValue> availableMTEFProjectionYears;
		private Collection projections;
		private String orgFundingId;
		private String sourceRole;
		private int numComm;
		private int numDisb;
		private int numExp;
		private int numDisbOrder;
		private int numProjections;
		private String disbOrderId;
		private String signatureDate;
		private String reportingDate;
		private String plannedStartDate;
		private String plannedCompletionDate;
		private String actualStartDate;
		private String actualCompletionDate;
		private String fundingConditions;

		// flags field
		private boolean firstSubmit;
		private String event;
		private boolean editFunding;
		private Long fundDonor;
		private Long fundingId;
		private Long fundingRegionId;
		private Collection<AmpCategoryValueLocations> fundingRegions;
		private Long[] selRegFundings;

		private Collection regionalFundings;
		private Long selFundingOrgs[];
		private long orgId;
		private int offset;
		private int indexId;
		private long transIndexId;
        private String fundingCurrCode;
        private int selectedMTEFProjectionYear;
        
        private float capitalSpendingPercentage;
        private boolean showActual;
        private boolean showPlanned;
        private boolean showPipeline;
        
        
        public boolean isShowPipeline() {
			return showPipeline;
		}

		public void setShowPipeline(boolean showPipeline) {
			this.showPipeline = showPipeline;
		}

		public boolean isShowActual() {
			return showActual;
		}

		public void setShowActual(boolean showActual) {
			this.showActual = showActual;
		}

		public boolean isShowPlanned() {
			return showPlanned;
		}

		public void setShowPlanned(boolean showPlanned) {
			this.showPlanned = showPlanned;
		}

		public float getCapitalSpendingPercentage() {
            return capitalSpendingPercentage;
        }

        public void setCapitalSpendingPercentage(float capitalSpendingPercentage) {
            this.capitalSpendingPercentage = capitalSpendingPercentage;
        }

        public void setConsumptionRate(String consumptionRate) {
            this.consumptionRate = consumptionRate;
        }   
        
		public void setDeliveryRate(String deliveryRate) {
			this.deliveryRate = deliveryRate;
		}             
        
		public String getConsumptionRate() {
			return consumptionRate;
		}
		
		public String getDeliveryRate() {
			return deliveryRate;
		}
        
        public List<FundingDetail> getCommitmentsDetails() {
			if(fundingDetails != null){
				List<FundingDetail> commitments = new ArrayList<FundingDetail>();
				for (FundingDetail detail : fundingDetails){
					if(detail.getTransactionType() == Constants.COMMITMENT) commitments.add(detail);
				}
				return commitments;
			}
			return fundingDetails;
		}
        
     
		
		public List<FundingDetail> getDisbursementsDetails() {
			if(fundingDetails != null){
				List<FundingDetail> disbursements = new ArrayList<FundingDetail>();
				for (FundingDetail detail : fundingDetails){
					if(detail.getTransactionType() == Constants.DISBURSEMENT) disbursements.add(detail);
				}
				return disbursements;
			}
			return fundingDetails;
		}
		
		public List<FundingDetail> getDisbursementOrdersDetails() {
			
			if(fundingDetails != null){
				List<FundingDetail> disbursementOrder = new ArrayList<FundingDetail>();
				for (FundingDetail detail : fundingDetails){
					if(detail.getTransactionType() == Constants.DISBURSEMENT_ORDER) disbursementOrder.add(detail);
				}
				return disbursementOrder;
			}
			return fundingDetails;
		}

		public List<FundingDetail> getExpendituresDetails() {
			if(fundingDetails != null){
				List<FundingDetail> expenditures = new ArrayList<FundingDetail>();
				for (FundingDetail detail : fundingDetails){
					if(detail.getTransactionType() == Constants.EXPENDITURE) expenditures.add(detail);
				}
				return expenditures;
			}
			return fundingDetails;
		}
        
        public Collection<FundingPledges> getPledgeslist() {
			return pledgeslist;
		}

		public void setPledgeslist(Collection<FundingPledges> pledgeslist) {
			this.pledgeslist = pledgeslist;
		}
        
        public String getFundingCurrCode() {
            return fundingCurrCode;
        }

        public void setFundingCurrCode(String fundingCurrCode) {
            this.fundingCurrCode = fundingCurrCode;
        }
		
		private boolean totDisbIsBiggerThanTotCom;
		
		public int getIndexId() {
			return indexId;
		}

		public void setIndexId(int indexId) {
			this.indexId = indexId;
		}

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public long getOrgId() {
			return orgId;
		}

		public void setOrgId(long orgId) {
			this.orgId = orgId;
		}

		public Collection<AmpCategoryValueLocations> getFundingRegions() {
			return fundingRegions;
		}
		
		public Collection<AmpCategoryValueLocations> getFundingRegionsUnique() {
			Collection<AmpCategoryValueLocations> unique = new ArrayList<AmpCategoryValueLocations>();
			Iterator<AmpCategoryValueLocations> it = fundingRegions.iterator();
			while (it.hasNext()) {
				AmpCategoryValueLocations val = (AmpCategoryValueLocations) it.next();
				if (!unique.contains(val))
					unique.add(val);
			}
			return unique;
		}
				
				
		public void setFundingRegions(Collection<AmpCategoryValueLocations> fundingRegions) {
			this.fundingRegions = fundingRegions;
		}

		public int getNumComm() {
			return numComm;
		}

		public void setNumComm(int numComm) {
			this.numComm = numComm;
		}

		public int getNumDisb() {
			return numDisb;
		}

		public void setNumDisb(int numDisb) {
			this.numDisb = numDisb;
		}

		public int getNumExp() {
			return numExp;
		}

		public void setNumExp(int numExp) {
			this.numExp = numExp;
		}

		public int getNumProjections() {
			return numProjections;
		}

		public void setNumProjections(int numProjections) {
			this.numProjections = numProjections;
		}

		public ProposedProjCost getProProjCost() {
			return proProjCost;
		}

		public void setProProjCost(ProposedProjCost proProjCost) {
			this.proProjCost = proProjCost;
		}

		public Collection<FundingOrganization> getFundingOrganizations() {
			return fundingOrganizations;
		}

		public void setFundingOrganizations(Collection<FundingOrganization> fundingOrganizations) {
			this.fundingOrganizations = fundingOrganizations;
		}

	

		public boolean isDisbursementOrders() {
			boolean disbOrdersExist = false;
			if (fundingDetails != null && fundingDetails.size() > 0) {
				Iterator<FundingDetail> detailIter = fundingDetails.iterator();
				while (detailIter.hasNext()) {
					FundingDetail det = detailIter.next();
					if (det.getTransactionType() == 4) {
						disbOrdersExist = true;
						break;
					}
				}
			}
			return disbOrdersExist;

		}

		public boolean isFixerate() {
			this.fixerate = false;
			if (fundingDetails != null) {
				Iterator iter = fundingDetails.iterator();
				while (iter.hasNext()) {
					FundingDetail element = (FundingDetail) iter.next();
					if (element.getFixedExchangeRate() != null) {
						this.fixerate = true;
						break;
					}
				}
			}
			return fixerate;
		}

		public List<FundingDetail> getFundingDetails() {
			return fundingDetails;
		}

		public void setFundingDetails(List<FundingDetail> fundingDetails) {
			this.fundingDetails = fundingDetails;
		}

		public String getTotalCommitted() {
			return totalCommitted;
		}

		public void setTotalCommitted(String totalCommitted) {
			this.totalCommitted = totalCommitted;
		}

		public String getTotalDisbursed() {
			return totalDisbursed;
		}

		public void setTotalDisbursed(String totalDisbursed) {
			this.totalDisbursed = totalDisbursed;
		}

		public String getTotalUnDisbursed() {
			return totalUnDisbursed;
		}
		
		public void setTotalActualRoF(String totalActualRoF)
		{
			this.totalActualRoF = totalActualRoF;
		}

		public void setTotalActualEDD(String totalActualEDD)
		{
			this.totalActualEDD = totalActualEDD;
		}
		
		public String getTotalActualRoF()
		{
			return this.totalActualRoF;
		}
		
		public String getTotalActualEDD()
		{
			return this.totalActualEDD;
		}
		
		public void setTotalUnDisbursed(String totalUnDisbursed) {
			this.totalUnDisbursed = totalUnDisbursed;
		}

		public String getTotalExpenditure() {
			return totalExpenditure;
		}

		public void setTotalExpenditure(String totalExpenditure) {
			this.totalExpenditure = totalExpenditure;
		}

		public String getTotalUnExpended() {
			return totalUnExpended;
		}

		public void setTotalUnExpended(String totalUnExpended) {
			this.totalUnExpended = totalUnExpended;
		}

		public String getTotalExpended() {
			return totalExpended;
		}

		public void setTotalExpended(String totalExpended) {
			this.totalExpended = totalExpended;
		}

		public String getTotalDisbOrder() {
			return totalDisbOrder;
		}

		public void setTotalDisbOrder(String totalDisbOrder) {
			this.totalDisbOrder = totalDisbOrder;
		}

		public String getTotalCommitments() {
			return totalCommitments;
		}

		public void setTotalCommitments(String totalCommitments) {
			this.totalCommitments = totalCommitments;
		}

		public String getTotalPlannedCommitments() {
			return totalPlannedCommitments;
		}

		public void setTotalPlannedCommitments(String totalPlannedCommitments) {
			this.totalPlannedCommitments = totalPlannedCommitments;
		}

		public String getTotalPlannedReleaseOfFunds() {
			return totalPlannedRoF;
		}

		public void setTotalPlannedReleaseOfFunds(String totalPlannedRoF) {
			this.totalPlannedRoF = totalPlannedRoF;
		}
		public String getTotalPlannedEDD() {
			return totalPlannedEDD;
		}

		public void setTotalPlannedEDD(String totalPlannedEDD) {
			this.totalPlannedEDD = totalPlannedEDD;
		}
		
		
		public double getTotalCommitmentsDouble() {
			return totalCommitmentsDouble;
		}

		public void setTotalCommitmentsDouble(double totalCommitmentsDouble) {
			this.totalCommitmentsDouble = totalCommitmentsDouble;
		}

		public String getTotalDisbursements() {
			return totalDisbursements;
		}

		public void setTotalDisbursements(String totalDisbursements) {
			this.totalDisbursements = totalDisbursements;
		}

		public String getTotalExpenditures() {
			return totalExpenditures;
		}

		public void setTotalExpenditures(String totalExpenditures) {
			this.totalExpenditures = totalExpenditures;
		}

		public String getTotalPlannedExpenditures() {
			return totalPlannedExpenditures;
		}

		public void setTotalPlannedExpenditures(String totalPlannedExpenditures) {
			this.totalPlannedExpenditures = totalPlannedExpenditures;
		}

		public String getTotalPlannedDisbursements() {
			return totalPlannedDisbursements;
		}

		public void setTotalPlannedDisbursements(String totalPlannedDisbursements) {
			this.totalPlannedDisbursements = totalPlannedDisbursements;
		}

		public String getTotalPlannedDisbursementsOrders() {
			return totalPlannedDisbursementsOrders;
		}

		public void setTotalPlannedDisbursementsOrders(String totalPlannedDisbursementsOrders) {
			this.totalPlannedDisbursementsOrders = totalPlannedDisbursementsOrders;
		}

		public String getTotalActualDisbursementsOrders() {
			return totalActualDisbursementsOrders;
		}

		public void setTotalActualDisbursementsOrders(String totalActualDisbursementsOrders) {
			this.totalActualDisbursementsOrders = totalActualDisbursementsOrders;
		}

		public String getUnDisbursementsBalance() {
			return unDisbursementsBalance;
		}

		public void setUnDisbursementsBalance(String unDisbursementsBalance) {
			this.unDisbursementsBalance = unDisbursementsBalance;
		}

		public void setFixerate(boolean fixerate) {
			this.fixerate = fixerate;
		}

		public Collection<AmpCurrency> getValidcurrencies() {
			return validcurrencies;
		}

		public void setValidcurrencies(Collection<AmpCurrency> validcurrencies) {
			this.validcurrencies = validcurrencies;
		}

		public boolean isDupFunding() {
			return dupFunding;
		}

		public void setDupFunding(boolean dupFunding) {
			this.dupFunding = dupFunding;
		}

		public String getOrgName() {
			return orgName;
		}

		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}

		public Long getAssistanceType() {
			return assistanceType;
		}

		public void setAssistanceType(Long assistanceType) {
			this.assistanceType = assistanceType;
		}

		public Long getModality() {
			return modality;
		}

		public void setModality(Long modality) {
			this.modality = modality;
		}

		/**
		 * @return the fundingStatus
		 */
		public Long getFundingStatus() {
			return fundingStatus;
		}

		/**
		 * @param fundingStatus the fundingStatus to set
		 */
		public void setFundingStatus(Long fundingStatus) {
			this.fundingStatus = fundingStatus;
		}

		public List<MTEFProjection> getFundingMTEFProjections() {
			return fundingMTEFProjections;
		}

		public void setFundingMTEFProjections(List<MTEFProjection> fundingMTEFProjections) {
			this.fundingMTEFProjections = fundingMTEFProjections;
		}
		

		/**
		 * @return the availableMTEFProjectionYears
		 */
		public List<KeyValue> getAvailableMTEFProjectionYears() {
			return availableMTEFProjectionYears;
		}

		/**
		 * @param availableMTEFProjectionYears the availableMTEFProjectionYears to set
		 */
		public void setAvailableMTEFProjectionYears(
				List<KeyValue> availableMTEFProjectionYears) {
			this.availableMTEFProjectionYears = availableMTEFProjectionYears;
		}

		public Collection getProjections() {
			return projections;
		}

		public void setProjections(Collection projections) {
			this.projections = projections;
		}

		public String getOrgFundingId() {
			return orgFundingId;
		}

		public void setOrgFundingId(String orgFundingId) {
			this.orgFundingId = orgFundingId;
		}

		public Collection getOrganizations() {
			return organizations;
		}

		public void setOrganizations(Collection organizations) {
			this.organizations = organizations;
		}

		public int getNumDisbOrder() {
			return numDisbOrder;
		}

		public void setNumDisbOrder(int numDisbOrder) {
			this.numDisbOrder = numDisbOrder;
		}

		public String getDisbOrderId() {
			return disbOrderId;
		}

		public void setDisbOrderId(String disbOrderId) {
			this.disbOrderId = disbOrderId;
		}

		public String getSignatureDate() {
			return signatureDate;
		}

		public void setSignatureDate(String signatureDate) {
			this.signatureDate = signatureDate;
		}

		public String getPlannedStartDate() {
			return plannedStartDate;
		}

		public void setPlannedStartDate(String plannedStartDate) {
			this.plannedStartDate = plannedStartDate;
		}

		public String getPlannedCompletionDate() {
			return plannedCompletionDate;
		}

		public void setPlannedCompletionDate(String plannedCompletionDate) {
			this.plannedCompletionDate = plannedCompletionDate;
		}

		public String getActualStartDate() {
			return actualStartDate;
		}

		public void setActualStartDate(String actualStartDate) {
			this.actualStartDate = actualStartDate;
		}

		public String getActualCompletionDate() {
			return actualCompletionDate;
		}

		public void setActualCompletionDate(String actualCompletionDate) {
			this.actualCompletionDate = actualCompletionDate;
		}

		public String getFundingConditions() {
			return fundingConditions;
		}

		public void setFundingConditions(String fundingConditions) {
			this.fundingConditions = fundingConditions;
		}

		public boolean isFirstSubmit() {
			return firstSubmit;
		}

		public void setFirstSubmit(boolean firstSubmit) {
			this.firstSubmit = firstSubmit;
		}

		public String getEvent() {
			return event;
		}

		public void setEvent(String event) {
			this.event = event;
		}

		public boolean isEditFunding() {
			return editFunding;
		}

		public void setEditFunding(boolean editFunding) {
			this.editFunding = editFunding;
		}

		public String getReportingDate() {
			return reportingDate;
		}

		public void setReportingDate(String reportingDate) {
			this.reportingDate = reportingDate;
		}

		public String getDonorObjective() {
			return donorObjective;
		}

		public void setDonorObjective(String donorObjective) {
			this.donorObjective = donorObjective;
		}

		public Long getFundDonor() {
			return fundDonor;
		}

		public void setFundDonor(Long fundDonor) {
			this.fundDonor = fundDonor;
		}

		public Long getFundingId() {
			return fundingId;
		}

		public void setFundingId(Long fundingId) {
			this.fundingId = fundingId;
		}

		public Long getFundingRegionId() {
			return fundingRegionId;
		}

		public void setFundingRegionId(Long fundingRegionId) {
			this.fundingRegionId = fundingRegionId;
		}

		public double getRegionTotalDisb() {
			return regionTotalDisb;
		}

		public void setRegionTotalDisb(double regionTotalDisb) {
			this.regionTotalDisb = regionTotalDisb;
		}

		public Collection getOrderedFundingOrganizations() {
			return orderedFundingOrganizations;
		}

		public void setOrderedFundingOrganizations(Collection orderedFundingOrganizations) {
			this.orderedFundingOrganizations = orderedFundingOrganizations;
		}


		public Collection getFinancingBreakdown() {
			return financingBreakdown;
		}

		public void setFinancingBreakdown(Collection financingBreakdown) {
			this.financingBreakdown = financingBreakdown;
		}

		public Collection getRegionalFundings() {
			return regionalFundings;
		}

		public void setRegionalFundings(Collection regionalFundings) {
			this.regionalFundings = regionalFundings;
		}

		public Long[] getSelFundingOrgs() {
			return selFundingOrgs;
		}

		public void setSelFundingOrgs(Long[] selFundingOrgs) {
			this.selFundingOrgs = selFundingOrgs;
		}

	

		public Long[] getSelRegFundings() {
			return selRegFundings;
		}

		public void setSelRegFundings(Long[] selRegFundings) {
			this.selRegFundings = selRegFundings;
		}

		public long getTransIndexId() {
			return transIndexId;
		}

		public void setTransIndexId(long transIndexId) {
			this.transIndexId = transIndexId;
		}

		/**
		 * @return the selectedMTEFProjectionYear
		 */
		public int getSelectedMTEFProjectionYear() {
			return selectedMTEFProjectionYear;
		}

		/**
		 * @param selectedMTEFProjectionYear the selectedMTEFProjectionYear to set
		 */
		public void setSelectedMTEFProjectionYear(int selectedMTEFProjectionYear) {
			this.selectedMTEFProjectionYear = selectedMTEFProjectionYear;
		}

		public String getTotalPipelineCommitments() {
			return totalPipelineCommitments;
		}

		public void setTotalPipelineCommitments(String totalPipelineCommitments) {
			this.totalPipelineCommitments = totalPipelineCommitments;
		}

		/**
		 * @return the modeOfPayment
		 */
		public Long getModeOfPayment() {
			return modeOfPayment;
		}

		/**
		 * @param modeOfPayment the modeOfPayment to set
		 */
		public void setModeOfPayment(Long modeOfPayment) {
			this.modeOfPayment = modeOfPayment;
		}

		/**
		 * @return the sourceRole
		 */
		public String getSourceRole() {
			return sourceRole;
		}

		/**
		 * @param sourceRole the sourceRole to set
		 */
		public void setSourceRole(String sourceRole) {
			this.sourceRole = sourceRole;
		}	
		
	}

	public FundingOrganization getFundingOrganization(int index) {
		if(getFunding().fundingOrganizations==null) return null;
		int currentSize = getFunding().fundingOrganizations.size();
		if (index >= currentSize) {
			for (int i = 0; i <= index - currentSize; i++) {
				getFunding().fundingOrganizations.add(new FundingOrganization());
			}
		}
		return (FundingOrganization) ((ArrayList) getFunding().fundingOrganizations).get(index);
	}
	
	public class ContactInformation {
		private String dnrCntFirstName;
		private String dnrCntLastName;
		private String dnrCntEmail;
		private String dnrCntTitle;
		private String dnrCntOrganization;
		private String dnrCntPhoneNumber;
		private String dnrCntFaxNumber;
		private String mfdCntFirstName;
		private String mfdCntLastName;
		private String mfdCntEmail;
		private String mfdCntTitle;
		private String mfdCntOrganization;
		private String mfdCntPhoneNumber;
		private String mfdCntFaxNumber;
		private String prjCoFirstName;
		private String prjCoLastName;
		private String prjCoEmail;
		private String prjCoTitle;
		private String prjCoOrganization;
		private String prjCoPhoneNumber;
		private String prjCoFaxNumber;
		private String secMiCntFirstName;
		private String secMiCntLastName;
		private String secMiCntEmail;
		private String secMiCntTitle;
		private String secMiCntOrganization;
		private String secMiCntPhoneNumber;
		private String secMiCntFaxNumber;

		private String contFirstName;
		private String contLastName;
		private String email;

		public String getContFirstName() {
			return contFirstName;
		}

		public void setContFirstName(String contFirstName) {
			this.contFirstName = contFirstName;
		}

		public String getContLastName() {
			return contLastName;
		}

		public void setContLastName(String contLastName) {
			this.contLastName = contLastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getDnrCntFirstName() {
			return dnrCntFirstName;
		}

		public void setDnrCntFirstName(String dnrCntFirstName) {
			this.dnrCntFirstName = dnrCntFirstName;
		}

		public String getDnrCntLastName() {
			return dnrCntLastName;
		}

		public void setDnrCntLastName(String dnrCntLastName) {
			this.dnrCntLastName = dnrCntLastName;
		}

		public String getDnrCntEmail() {
			return dnrCntEmail;
		}

		public void setDnrCntEmail(String dnrCntEmail) {
			this.dnrCntEmail = dnrCntEmail;
		}

		public String getDnrCntTitle() {
			return dnrCntTitle;
		}

		public void setDnrCntTitle(String dnrCntTitle) {
			this.dnrCntTitle = dnrCntTitle;
		}

		public String getDnrCntOrganization() {
			return dnrCntOrganization;
		}

		public void setDnrCntOrganization(String dnrCntOrganization) {
			this.dnrCntOrganization = dnrCntOrganization;
		}

		public String getDnrCntPhoneNumber() {
			return dnrCntPhoneNumber;
		}

		public void setDnrCntPhoneNumber(String dnrCntPhoneNumber) {
			this.dnrCntPhoneNumber = dnrCntPhoneNumber;
		}

		public String getDnrCntFaxNumber() {
			return dnrCntFaxNumber;
		}

		public void setDnrCntFaxNumber(String dnrCntFaxNumber) {
			this.dnrCntFaxNumber = dnrCntFaxNumber;
		}

		public String getMfdCntFirstName() {
			return mfdCntFirstName;
		}

		public void setMfdCntFirstName(String mfdCntFirstName) {
			this.mfdCntFirstName = mfdCntFirstName;
		}

		public String getMfdCntLastName() {
			return mfdCntLastName;
		}

		public void setMfdCntLastName(String mfdCntLastName) {
			this.mfdCntLastName = mfdCntLastName;
		}

		public String getMfdCntEmail() {
			return mfdCntEmail;
		}

		public void setMfdCntEmail(String mfdCntEmail) {
			this.mfdCntEmail = mfdCntEmail;
		}

		public String getMfdCntTitle() {
			return mfdCntTitle;
		}

		public void setMfdCntTitle(String mfdCntTitle) {
			this.mfdCntTitle = mfdCntTitle;
		}

		public String getMfdCntOrganization() {
			return mfdCntOrganization;
		}

		public void setMfdCntOrganization(String mfdCntOrganization) {
			this.mfdCntOrganization = mfdCntOrganization;
		}

		public String getMfdCntPhoneNumber() {
			return mfdCntPhoneNumber;
		}

		public void setMfdCntPhoneNumber(String mfdCntPhoneNumber) {
			this.mfdCntPhoneNumber = mfdCntPhoneNumber;
		}

		public String getMfdCntFaxNumber() {
			return mfdCntFaxNumber;
		}

		public void setMfdCntFaxNumber(String mfdCntFaxNumber) {
			this.mfdCntFaxNumber = mfdCntFaxNumber;
		}

		public String getPrjCoFirstName() {
			return prjCoFirstName;
		}

		public void setPrjCoFirstName(String prjCoFirstName) {
			this.prjCoFirstName = prjCoFirstName;
		}

		public String getPrjCoLastName() {
			return prjCoLastName;
		}

		public void setPrjCoLastName(String prjCoLastName) {
			this.prjCoLastName = prjCoLastName;
		}

		public String getPrjCoEmail() {
			return prjCoEmail;
		}

		public void setPrjCoEmail(String prjCoEmail) {
			this.prjCoEmail = prjCoEmail;
		}

		public String getPrjCoTitle() {
			return prjCoTitle;
		}

		public void setPrjCoTitle(String prjCoTitle) {
			this.prjCoTitle = prjCoTitle;
		}

		public String getPrjCoOrganization() {
			return prjCoOrganization;
		}

		public void setPrjCoOrganization(String prjCoOrganization) {
			this.prjCoOrganization = prjCoOrganization;
		}

		public String getPrjCoPhoneNumber() {
			return prjCoPhoneNumber;
		}

		public void setPrjCoPhoneNumber(String prjCoPhoneNumber) {
			this.prjCoPhoneNumber = prjCoPhoneNumber;
		}

		public String getPrjCoFaxNumber() {
			return prjCoFaxNumber;
		}

		public void setPrjCoFaxNumber(String prjCoFaxNumber) {
			this.prjCoFaxNumber = prjCoFaxNumber;
		}

		public String getSecMiCntFirstName() {
			return secMiCntFirstName;
		}

		public void setSecMiCntFirstName(String secMiCntFirstName) {
			this.secMiCntFirstName = secMiCntFirstName;
		}

		public String getSecMiCntLastName() {
			return secMiCntLastName;
		}

		public void setSecMiCntLastName(String secMiCntLastName) {
			this.secMiCntLastName = secMiCntLastName;
		}

		public String getSecMiCntEmail() {
			return secMiCntEmail;
		}

		public void setSecMiCntEmail(String secMiCntEmail) {
			this.secMiCntEmail = secMiCntEmail;
		}

		public String getSecMiCntTitle() {
			return secMiCntTitle;
		}

		public void setSecMiCntTitle(String secMiCntTitle) {
			this.secMiCntTitle = secMiCntTitle;
		}

		public String getSecMiCntOrganization() {
			return secMiCntOrganization;
		}

		public void setSecMiCntOrganization(String secMiCntOrganization) {
			this.secMiCntOrganization = secMiCntOrganization;
		}

		public String getSecMiCntPhoneNumber() {
			return secMiCntPhoneNumber;
		}

		public void setSecMiCntPhoneNumber(String secMiCntPhoneNumber) {
			this.secMiCntPhoneNumber = secMiCntPhoneNumber;
		}

		public String getSecMiCntFaxNumber() {
			return secMiCntFaxNumber;
		}

		public void setSecMiCntFaxNumber(String secMiCntFaxNumber) {
			this.secMiCntFaxNumber = secMiCntFaxNumber;
		}

	}

	public class Comments {
		private String actionFlag;
		private String fieldName;
		private AmpField field = null;

		private ArrayList commentsCol = new ArrayList();
		private HashMap allComments = new HashMap();
		private boolean commentFlag;
		private String commentText = null;
		private Long ampCommentId;
		

		public ArrayList getCommentsCol() {
			return commentsCol;
		}

		public void setCommentsCol(ArrayList commentsCol) {
			this.commentsCol = commentsCol;
		}

		public HashMap getAllComments() {
			return allComments;
		}

		public void setAllComments(HashMap allComments) {
			this.allComments = allComments;
		}

		public boolean isCommentFlag() {
			return commentFlag;
		}

		public void setCommentFlag(boolean commentFlag) {
			this.commentFlag = commentFlag;
		}

		public String getCommentText() {
			return commentText;
		}

		public void setCommentText(String commentText) {
			this.commentText = commentText;
		}

		public Long getAmpCommentId() {
			return ampCommentId;
		}

		public void setAmpCommentId(Long ampCommentId) {
			this.ampCommentId = ampCommentId;
		}
	


		public String getActionFlag() {
			return actionFlag;
		}

		public void setActionFlag(String actionFlag) {
			this.actionFlag = actionFlag;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public AmpField getField() {
			return field;
		}

		public void setField(AmpField field) {
			this.field = field;
		}

	}

	public class PhisycalProgress {
		private Collection selectedPhysicalProgress;
		private Long selPhyProg[];
		private boolean phyProgReset;
		private String phyProgTitle;
		private String phyProgDesc;
		private String phyProgRepDate;
		private Long phyProgId;

		public boolean isPhyProgReset() {
			return phyProgReset;
		}

		public void setPhyProgReset(boolean phyProgReset) {
			this.phyProgReset = phyProgReset;
		}

		public String getPhyProgTitle() {
			return phyProgTitle;
		}

		public void setPhyProgTitle(String phyProgTitle) {
			this.phyProgTitle = phyProgTitle;
		}

		public String getPhyProgDesc() {
			return phyProgDesc;
		}

		public void setPhyProgDesc(String phyProgDesc) {
			this.phyProgDesc = phyProgDesc;
		}

		public String getPhyProgRepDate() {
			return phyProgRepDate;
		}

		public void setPhyProgRepDate(String phyProgRepDate) {
			this.phyProgRepDate = phyProgRepDate;
		}

		public Long getPhyProgId() {
			return phyProgId;
		}

		public void setPhyProgId(Long phyProgId) {
			this.phyProgId = phyProgId;
		}

		public Collection getSelectedPhysicalProgress() {
			return selectedPhysicalProgress;
		}

		public void setSelectedPhysicalProgress(Collection selectedPhysicalProgress) {
			this.selectedPhysicalProgress = selectedPhysicalProgress;
		}

		public Long[] getSelPhyProg() {
			return selPhyProg;
		}

		public void setSelPhyProg(Long[] selPhyProg) {
			this.selPhyProg = selPhyProg;
		}
	}

	public class Documents {
		private String documentSpace = null;
		private FormFile docFile;
		private String docWebResource= "http://";
		private String docTitle;
		private String docDescription;
		private String docDate;
		private String docFileOrLink;
		private boolean docReset;
		private boolean showInHomePage;
		private int pageId;
		private Collection documentList;
		private Collection documents;
		private Collection<DocumentData> crDocuments;
		private Collection managedDocumentList;
		private long selDocs[];
		private Collection linksList;
		private long selLinks[];
		private String selManagedDocs[];
		private String actionFlag = null;

		
		private int pagesToShow;
		private int pagesSize;
		private int startPage;
		private ReferenceDoc[] referenceDocs;

		private Long docType;
		private Long docLang;
		private String docComment;
		private Long[] allReferenceDocNameIds;

		public FormFile getDocFile() {
			return docFile;
		}

		public void setDocFile(FormFile docFile) {
			this.docFile = docFile;
		}

		public String getDocWebResource() {
			return docWebResource;
		}

		public void setDocWebResource(String docWebResource) {
			this.docWebResource = docWebResource;
		}

		public String getDocTitle() {
			return docTitle;
		}

		public void setDocTitle(String docTitle) {
			this.docTitle = docTitle;
		}

		public String getDocDescription() {
			return docDescription;
		}

		public void setDocDescription(String docDescription) {
			this.docDescription = docDescription;
		}

		public String getDocDate() {
			return docDate;
		}

		public void setDocDate(String docDate) {
			this.docDate = docDate;
		}

		public String getDocFileOrLink() {
			return docFileOrLink;
		}

		public void setDocFileOrLink(String docFileOrLink) {
			this.docFileOrLink = docFileOrLink;
		}

		public boolean isDocReset() {
			return docReset;
		}

		public void setDocReset(boolean docReset) {
			this.docReset = docReset;
		}

		public boolean isShowInHomePage() {
			return showInHomePage;
		}

		public void setShowInHomePage(boolean showInHomePage) {
			this.showInHomePage = showInHomePage;
		}

		public int getPageId() {
			return pageId;
		}

		public void setPageId(int pageId) {
			this.pageId = pageId;
		}

		public boolean isEditAct() {
			return editAct;
		}

		public Long getDocType() {
			return docType;
		}

		public void setDocType(Long docType) {
			this.docType = docType;
		}

		public Long getDocLang() {
			return docLang;
		}

		public void setDocLang(Long docLang) {
			this.docLang = docLang;
		}

		public String getDocComment() {
			return docComment;
		}

		public void setDocComment(String docComment) {
			this.docComment = docComment;
		}

		public String getDocumentSpace() {
			return documentSpace;
		}

		public void setDocumentSpace(String documentSpace) {
			this.documentSpace = documentSpace;
		}

		public Collection getDocumentList() {
			return documentList;
		}

		public void setDocumentList(Collection documentList) {
			this.documentList = documentList;
		}

		public Collection getDocuments() {
			return documents;
		}

		public void setDocuments(Collection documents) {
			this.documents = documents;
		}

		public Collection<DocumentData> getCrDocuments() {
			return crDocuments;
		}

		public void setCrDocuments(Collection<DocumentData> crDocuments) {
			this.crDocuments = crDocuments;
		}

		public Collection getManagedDocumentList() {
			return managedDocumentList;
		}

		public void setManagedDocumentList(Collection managedDocumentList) {
			this.managedDocumentList = managedDocumentList;
		}

		public long[] getSelDocs() {
			return selDocs;
		}

		public void setSelDocs(long[] selDocs) {
			this.selDocs = selDocs;
		}

		public Collection getLinksList() {
			return linksList;
		}

		public void setLinksList(Collection linksList) {
			this.linksList = linksList;
		}

		public long[] getSelLinks() {
			return selLinks;
		}

		public void setSelLinks(long[] selLinks) {
			this.selLinks = selLinks;
		}

		public String[] getSelManagedDocs() {
			return selManagedDocs;
		}

		public void setSelManagedDocs(String[] selManagedDocs) {
			this.selManagedDocs = selManagedDocs;
		}

		public String getActionFlag() {
			return actionFlag;
		}

		public void setActionFlag(String actionFlag) {
			this.actionFlag = actionFlag;
		}

		

		public int getPagesToShow() {
			return pagesToShow;
		}

		public void setPagesToShow(int pagesToShow) {
			this.pagesToShow = pagesToShow;
		}

		public int getPagesSize() {
			return pagesSize;
		}

		public void setPagesSize(int pagesSize) {
			this.pagesSize = pagesSize;
		}

		public int getStartPage() {
			return startPage;
		}

		public void setStartPage(int startPage) {
			this.startPage = startPage;
		}

		public ReferenceDoc[] getReferenceDocs() {
			return referenceDocs;
		}

		public void setReferenceDocs(ReferenceDoc[] referenceDocs) {
			this.referenceDocs = referenceDocs;
		}

		public Long[] getAllReferenceDocNameIds() {
			return allReferenceDocNameIds;
		}

		public void setAllReferenceDocNameIds(Long[] allReferenceDocNameIds) {
			this.allReferenceDocNameIds = allReferenceDocNameIds;
		}
	}

	public class Survey {
		private Long surveyOrgId;
		private AmpAhsurvey ahsurvey;
		private Collection survey = null;
		private List indicators = null; // holds collection of Indicator helper
		private Collection pageColl = null; // total number of survey pages
		private Integer currPage = null;
		private Integer startIndex = null; // starting record for iteartion
		// over
		private String fundingOrg = null; // acronym of funding organisation
		private Long ampSurveyId = null;
		private Boolean surveyFlag = null; // if true then survey properties

		// are

		public AmpAhsurvey getAhsurvey() {
			return ahsurvey;
		}

		public void setAhsurvey(AmpAhsurvey ahsurvey) {
			this.ahsurvey = ahsurvey;
		}

		public Collection getSurvey() {
			return survey;
		}

		public void setSurvey(Collection survey) {
			this.survey = survey;
		}

		public List getIndicators() {
			return indicators;
		}

		public void setIndicators(List indicators) {
			this.indicators = indicators;
		}

		public Collection getPageColl() {
			return pageColl;
		}

		public void setPageColl(Collection pageColl) {
			this.pageColl = pageColl;
		}

		public Integer getCurrPage() {
			return currPage;
		}

		public void setCurrPage(Integer currPage) {
			this.currPage = currPage;
		}

		public Integer getStartIndex() {
			return startIndex;
		}

		public void setStartIndex(Integer startIndex) {
			this.startIndex = startIndex;
		}

		public String getFundingOrg() {
			return fundingOrg;
		}

		public void setFundingOrg(String fundingOrg) {
			this.fundingOrg = fundingOrg;
		}

		public Long getAmpSurveyId() {
			return ampSurveyId;
		}

		public void setAmpSurveyId(Long ampSurveyId) {
			this.ampSurveyId = ampSurveyId;
		}

		public Boolean getSurveyFlag() {
			return surveyFlag;
		}

		public void setSurveyFlag(Boolean surveyFlag) {
			this.surveyFlag = surveyFlag;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Survey) {
				Survey aux = (Survey) obj;
				if(aux.getAmpSurveyId().equals(this.getAmpSurveyId())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return (this.ampSurveyId == null ? 0 : this.ampSurveyId.intValue());
		}
	}

	public class Costing {
		private Double allCosts;
		private List costs;
		private Double overallCost = null;
		private Double overallContribution = null;

		public List getCosts() {
			return costs;
		}

		public void setCosts(List costs) {
			this.costs = costs;
		}

		public Double getOverallCost() {
			return overallCost;
		}

		public void setOverallCost(Double overallCost) {
			this.overallCost = overallCost;
		}

		public Double getOverallContribution() {
			return overallContribution;
		}

		public void setOverallContribution(Double overallContribution) {
			this.overallContribution = overallContribution;
		}

		public Double getAllCosts() {
			return allCosts;
		}

		public void setAllCosts(Double allCosts) {
			this.allCosts = allCosts;
		}

	}

	public class Agencies {

		private Long selConAgencies[];
		private Collection<AmpOrganisation> conAgencies;
		private HashMap<String, String> conOrgToInfo;
        private HashMap<String, String> conOrgPercentage;


		private Collection<AmpOrganisation> executingAgencies;
		private Long selExAgencies[];
		private HashMap<String, String> executingOrgToInfo;
        private HashMap<String, String> executingOrgPercentage;


		private Collection<AmpOrganisation> impAgencies;
		private Long selImpAgencies[];
		private HashMap<String, String> impOrgToInfo;
        private HashMap<String, String> impOrgPercentage;

		private Collection<AmpOrganisation> regGroups;
		private Long selRegGroups[];
		private HashMap<String, String> regOrgToInfo;
        private HashMap<String, String> regOrgPercentage;


		private Collection<AmpOrganisation> reportingOrgs;
		private Long selReportingOrgs[];
		private HashMap<String, String> repOrgToInfo;
        private HashMap<String, String> repOrgPercentage;

		private Collection<AmpOrganisation> sectGroups;
		private Long selSectGroups[];
		private HashMap<String, String> sectOrgToInfo;
        private HashMap<String, String> sectOrgPercentage;

		private Collection<AmpOrganisation> benAgencies;
		private Long selBenAgencies[];
		private HashMap<String, String> benOrgToInfo;
        private HashMap<String, String> benOrgPercentage;

		private Collection<AmpOrganisation> respOrganisations;
		private Long selRespOrganisations[];
		private HashMap<String, String> respOrgToInfo;
        private HashMap<String, String> respOrgPercentage;

		private int item;

		public int getItem() {
			return item;
		}

		public void setItem(int item) {
			this.item = item;
		}

		public Long[] getSelConAgencies() {
			return selConAgencies;
		}

		public void setSelConAgencies(Long[] selConAgencies) {
			this.selConAgencies = selConAgencies;
		}

		public Collection<AmpOrganisation> getConAgencies() {
			return conAgencies;
		}

		public void setConAgencies(Collection<AmpOrganisation> conAgencies) {
			this.conAgencies = conAgencies;
		}

		public Collection<AmpOrganisation> getExecutingAgencies() {
			return executingAgencies;
		}

		public void setExecutingAgencies(Collection<AmpOrganisation> executingAgencies) {
			this.executingAgencies = executingAgencies;
		}

		public Long[] getSelExAgencies() {
			return selExAgencies;
		}

		public void setSelExAgencies(Long[] selExAgencies) {
			this.selExAgencies = selExAgencies;
		}

		public Collection<AmpOrganisation> getImpAgencies() {
			return impAgencies;
		}

		public void setImpAgencies(Collection<AmpOrganisation> impAgencies) {
			this.impAgencies = impAgencies;
		}

		public Long[] getSelImpAgencies() {
			return selImpAgencies;
		}

		public void setSelImpAgencies(Long[] selImpAgencies) {
			this.selImpAgencies = selImpAgencies;
		}

		public Collection<AmpOrganisation> getRegGroups() {
			return regGroups;
		}

		public void setRegGroups(Collection<AmpOrganisation> regGroups) {
			this.regGroups = regGroups;
		}

		public Long[] getSelRegGroups() {
			return selRegGroups;
		}

		public void setSelRegGroups(Long[] selRegGroups) {
			this.selRegGroups = selRegGroups;
		}

		public Collection<AmpOrganisation> getReportingOrgs() {
			return reportingOrgs;
		}

		public void setReportingOrgs(Collection<AmpOrganisation> reportingOrgs) {
			this.reportingOrgs = reportingOrgs;
		}

		public Long[] getSelReportingOrgs() {
			return selReportingOrgs;
		}

		public void setSelReportingOrgs(Long[] selReportingOrgs) {
			this.selReportingOrgs = selReportingOrgs;
		}

		public Collection<AmpOrganisation> getSectGroups() {
			return sectGroups;
		}

		public void setSectGroups(Collection<AmpOrganisation> sectGroups) {
			this.sectGroups = sectGroups;
		}

		public Long[] getSelSectGroups() {
			return selSectGroups;
		}

		public void setSelSectGroups(Long[] selSectGroups) {
			this.selSectGroups = selSectGroups;
		}

		public Collection<AmpOrganisation> getBenAgencies() {
			return benAgencies;
		}

		public void setBenAgencies(Collection<AmpOrganisation> benAgencies) {
			this.benAgencies = benAgencies;
		}

		public Long[] getSelBenAgencies() {
			return selBenAgencies;
		}

		public void setSelBenAgencies(Long[] selBenAgencies) {
			this.selBenAgencies = selBenAgencies;
		}

		public Collection<AmpOrganisation> getRespOrganisations() {
			return respOrganisations;
		}

		public void setRespOrganisations(Collection<AmpOrganisation> respOrganisations) {
			this.respOrganisations = respOrganisations;
		}

		public Long[] getSelRespOrganisations() {
			return selRespOrganisations;
		}

		public void setSelRespOrganisations(Long[] selRespOrganisations) {
			this.selRespOrganisations = selRespOrganisations;
		}

		public HashMap<String, String> getConOrgToInfo() {
			return conOrgToInfo;
		}

		public void setConOrgToInfo(HashMap<String, String> conOrgToInfo) {
			this.conOrgToInfo = conOrgToInfo;
		}

		public HashMap<String, String> getExecutingOrgToInfo() {
			return executingOrgToInfo;
		}

		public void setExecutingOrgToInfo(HashMap<String, String> executingOrgToInfo) {
			this.executingOrgToInfo = executingOrgToInfo;
		}

		public HashMap<String, String> getImpOrgToInfo() {
			return impOrgToInfo;
		}

		public void setImpOrgToInfo(HashMap<String, String> impOrgToInfo) {
			this.impOrgToInfo = impOrgToInfo;
		}

		public HashMap<String, String> getRegOrgToInfo() {
			return regOrgToInfo;
		}

		public void setRegOrgToInfo(HashMap<String, String> regOrgToInfo) {
			this.regOrgToInfo = regOrgToInfo;
		}

		public HashMap<String, String> getRepOrgToInfo() {
			return repOrgToInfo;
		}

		public void setRepOrgToInfo(HashMap<String, String> repOrgToInfo) {
			this.repOrgToInfo = repOrgToInfo;
		}

		public HashMap<String, String> getSectOrgToInfo() {
			return sectOrgToInfo;
		}

		public void setSectOrgToInfo(HashMap<String, String> sectOrgToInfo) {
			this.sectOrgToInfo = sectOrgToInfo;
		}

		public HashMap<String, String> getBenOrgToInfo() {
			return benOrgToInfo;
		}

		public void setBenOrgToInfo(HashMap<String, String> benOrgToInfo) {
			this.benOrgToInfo = benOrgToInfo;
		}

		public HashMap<String, String> getRespOrgToInfo() {
			return respOrgToInfo;
		}

		public void setRespOrgToInfo(HashMap<String, String> respOrgToInfo) {
			this.respOrgToInfo = respOrgToInfo;
		}
		
		public HashMap<String, String> getConOrgPercentage() {
			return conOrgPercentage;
		}

		public void setConOrgPercentage(HashMap<String, String> conOrgPercentage) {
			this.conOrgPercentage = conOrgPercentage;
		}

		public HashMap<String, String> getExecutingOrgPercentage() {
			return executingOrgPercentage;
		}

		public void setExecutingOrgPercentage(
				HashMap<String, String> executingOrgPercentage) {
			this.executingOrgPercentage = executingOrgPercentage;
		}

		public HashMap<String, String> getImpOrgPercentage() {
			return impOrgPercentage;
		}

		public void setImpOrgPercentage(HashMap<String, String> impOrgPercentage) {
			this.impOrgPercentage = impOrgPercentage;
		}

		public HashMap<String, String> getRegOrgPercentage() {
			return regOrgPercentage;
		}

		public void setRegOrgPercentage(HashMap<String, String> regOrgPercentage) {
			this.regOrgPercentage = regOrgPercentage;
		}

		public HashMap<String, String> getRepOrgPercentage() {
			return repOrgPercentage;
		}

		public void setRepOrgPercentage(HashMap<String, String> repOrgPercentage) {
			this.repOrgPercentage = repOrgPercentage;
		}

		public HashMap<String, String> getSectOrgPercentage() {
			return sectOrgPercentage;
		}

		public void setSectOrgPercentage(HashMap<String, String> sectOrgPercentage) {
			this.sectOrgPercentage = sectOrgPercentage;
		}

		public HashMap<String, String> getBenOrgPercentage() {
			return benOrgPercentage;
		}

		public void setBenOrgPercentage(HashMap<String, String> benOrgPercentage) {
			this.benOrgPercentage = benOrgPercentage;
		}

		public HashMap<String, String> getRespOrgPercentage() {
			return respOrgPercentage;
		}

		public void setRespOrgPercentage(HashMap<String, String> respOrgPercentage) {
			this.respOrgPercentage = respOrgPercentage;
		}
		
	}

	public class IndicatorME implements Serializable {
		private Long logframeCategory;
		private Collection indicatorsME;
		private ActivityIndicator indicatorValues;
		private Long indicatorId;
		private Long indicatorValId;
		private Long expIndicatorId;
		private Float baseVal;
		private String baseValDate;
		private String baseValComments;
		private Float targetVal;
		private String targetValDate;
		private String targetValComments;
		private Float revTargetVal;
		private String revTargetValDate;
		private String revTargetValComments;
		private Float currentVal;
		private String currValDate;
		private String currValComments;
		private String comments;
		private Float revisedTargetVal;
		private Collection riskCollection;
		private Long indicatorRisk;
		private String currentValDate;
		private String currentValComments;
		private String indicatorPriorValues;

		public String getIndicatorPriorValues() {
			return indicatorPriorValues;
		}

		public void setIndicatorPriorValues(String indicatorPriorValues) {
			this.indicatorPriorValues = indicatorPriorValues;
		}

		public String getCurrentValDate() {
			return currentValDate;
		}

		public void setCurrentValDate(String currentValDate) {
			this.currentValDate = currentValDate;
		}

		public String getCurrentValComments() {
			return currentValComments;
		}

		public void setCurrentValComments(String currentValComments) {
			this.currentValComments = currentValComments;
		}

		public Long getLogframeCategory() {
			return logframeCategory;
		}

		public void setLogframeCategory(Long logframeCategory) {
			this.logframeCategory = logframeCategory;
		}

		public Collection getIndicatorsME() {
			return indicatorsME;
		}

		public void setIndicatorsME(Collection indicatorsME) {
			this.indicatorsME = indicatorsME;
		}

		public ActivityIndicator getIndicatorValues() {
			return indicatorValues;
		}

		public void setIndicatorValues(ActivityIndicator indicatorValues) {
			this.indicatorValues = indicatorValues;
		}

		public Long getIndicatorId() {
			return indicatorId;
		}

		public void setIndicatorId(Long indicatorId) {
			this.indicatorId = indicatorId;
		}

		public Long getIndicatorValId() {
			return indicatorValId;
		}

		public void setIndicatorValId(Long indicatorValId) {
			this.indicatorValId = indicatorValId;
		}

		public Long getExpIndicatorId() {
			return expIndicatorId;
		}

		public void setExpIndicatorId(Long expIndicatorId) {
			this.expIndicatorId = expIndicatorId;
		}

		public Float getBaseVal() {
			return baseVal;
		}

		public void setBaseVal(Float baseVal) {
			this.baseVal = baseVal;
		}

		public String getBaseValDate() {
			return baseValDate;
		}

		public void setBaseValDate(String baseValDate) {
			this.baseValDate = baseValDate;
		}

		public String getBaseValComments() {
			return baseValComments;
		}

		public void setBaseValComments(String baseValComments) {
			this.baseValComments = baseValComments;
		}

		public Float getTargetVal() {
			return targetVal;
		}

		public void setTargetVal(Float targetVal) {
			this.targetVal = targetVal;
		}

		public String getTargetValDate() {
			return targetValDate;
		}

		public void setTargetValDate(String targetValDate) {
			this.targetValDate = targetValDate;
		}

		public String getTargetValComments() {
			return targetValComments;
		}

		public void setTargetValComments(String targetValComments) {
			this.targetValComments = targetValComments;
		}

		public Float getRevTargetVal() {
			return revTargetVal;
		}

		public void setRevTargetVal(Float revTargetVal) {
			this.revTargetVal = revTargetVal;
		}

		public String getRevTargetValDate() {
			return revTargetValDate;
		}

		public void setRevTargetValDate(String revTargetValDate) {
			this.revTargetValDate = revTargetValDate;
		}

		public String getRevTargetValComments() {
			return revTargetValComments;
		}

		public void setRevTargetValComments(String revTargetValComments) {
			this.revTargetValComments = revTargetValComments;
		}

		public Float getCurrentVal() {
			return currentVal;
		}

		public void setCurrentVal(Float currentVal) {
			this.currentVal = currentVal;
		}

		public String getCurrValDate() {
			return currValDate;
		}

		public void setCurrValDate(String currValDate) {
			this.currValDate = currValDate;
		}

		public String getCurrValComments() {
			return currValComments;
		}

		public void setCurrValComments(String currValComments) {
			this.currValComments = currValComments;
		}

		public String getComments() {
			return comments;
		}

		public void setComments(String comments) {
			this.comments = comments;
		}

		public Float getRevisedTargetVal() {
			return revisedTargetVal;
		}

		public void setRevisedTargetVal(Float revisedTargetVal) {
			this.revisedTargetVal = revisedTargetVal;
		}

		public Collection getRiskCollection() {
			return riskCollection;
		}

		public void setRiskCollection(Collection riskCollection) {
			this.riskCollection = riskCollection;
		}

		public Long getIndicatorRisk() {
			return indicatorRisk;
		}

		public void setIndicatorRisk(Long indicatorRisk) {
			this.indicatorRisk = indicatorRisk;
		}

	}

	private Identification identification;
	private Planning planning;
	private Location location;
	private Sector sectors;
	private Component components;
	private Programs programs;
	private CrossCuttingIssues crossIssues;
	private Funding funding;
	private Funding oldFunding;
	private Documents documents = null;
	private Agencies agencies;
	
	/**
	 * This is the survey helper object for the actual or last edited survey.
	 */
	private Survey survey = null;
	
	/**
	 * This collection holds Survey objects (helpers) that have been edited.
	 */
	private Set<Survey> surveys;
	
	/**
	 * This collection holds AmpAHsurvey objects (surveys) that have been edited.
	 */
	private Set<AmpAhsurvey> ampAhsurveys;
	
	private ContactInformation contactInfo;
	private Comments comments = null;
	private PhisycalProgress phisycalProgress;
	private IndicatorME indicatorME = null;
	private Contracts contracts = null;
	private Costing costing = null;
	private Issues issues = null;
	private Issues observations = null;
	private boolean totDisbIsBiggerThanTotCom;
	
	public Set<Survey> getSurveys() {
		return surveys;
	}

	public void setSurveys(Set<Survey> surveys) {
		this.surveys = surveys;
	}
	
	public Set<AmpAhsurvey> getAmpAhsurveys() {
		return ampAhsurveys;
	}

	public void setAmpAhsurveys(Set<AmpAhsurvey> surveys) {
		this.ampAhsurveys = surveys;
	}
	
	public String getWorkingTeamLeadFlag() {
		return workingTeamLeadFlag;
	}

	public void setWorkingTeamLeadFlag(String workingTeamLeadFlag) {
		this.workingTeamLeadFlag = workingTeamLeadFlag;
	}

	public List getSteps() {
		return steps;
	}

	public void setSteps(List steps) {
		this.steps = steps;
	}

	public int getStepNumberOnPage() {
		int stepNumberOnPage = 0;
		if (steps != null) {
			Iterator<Step> iter = steps.iterator();
			while (iter.hasNext()) {
				Step stp = iter.next();
				if (stp.getStepNumber().equals(step)) {
					return stp.getStepActualNumber();
				}
			}
		}
		return stepNumberOnPage;

	}

	public EditActivityForm() {
		step = "1";
		reset = false;
		editAct = false;
		pageId = 0;
		editKey = "";
		customFields = CustomFieldsUtil.getCustomFields();
    	customFieldsSteps = CustomFieldsUtil.getCustomFieldsSteps();
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		if (reset) {
			this.activityContacts = null;
			this.identification = null;
			this.planning = null;
			this.location = null;
			this.sectors = null;
			this.components = null;
			this.getPrograms().setNationalPlanObjectivePrograms(null);
			this.getPrograms().setPrimaryPrograms(null);
			this.getPrograms().setSecondaryPrograms(null);
			this.getPrograms().setTertiaryPrograms(null);
			this.crossIssues = null;
			this.funding = null;
			this.oldFunding = null;
			this.survey = null;
			this.surveys = null;
			this.ampAhsurveys = null;
			this.contactInfo = null;
			this.contracts = null;
			this.contactInformation=null;
			this.agencies = null;
			this.indicatorME = null;
            this.fundingCurrCode=null;
            this.regFundingPageCurrCode=null;
            this.funding=null;
            this.issues=null;
            this.observations=null;
			step = "1";
			reset = false;
			steps = null;
			if (this.customFields != null && this.customFields.size() > 0) {
				Iterator<CustomField<?>> itcf = this.customFields.iterator();
				while (itcf.hasNext()) {
					CustomField<?> cf = itcf.next();
					cf.setValue(null);
				}
			}
			if (this.costing !=null){
				this.costing.allCosts = null;
				this.costing.costs = null;
				this.costing.overallContribution=null;
				this.costing.overallCost = null;
			}

            clearMessages();
            this.draftRedirectedPage=1; // Desktop
		}
		if (this.getLocation().isLocationReset()) {
			this.getLocation().reset(mapping, request);
		}
		
		 if (this.getPhisycalProgress().isPhyProgReset()) {
			 this.phisycalProgress=new PhisycalProgress();
			}

		if (this.getDocuments().isDocReset()) {
			this.documents=new Documents();
		 }
		
		if (this.getComponents().isComponentReset()) {
			this.components=new Component();
		}

	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	/**
	 * @return Returns the reset.
	 */
	public boolean isReset() {
		return reset;
	}

	/**
	 * @param reset
	 *            The reset to set.
	 */
	public void setReset(boolean reset) {
		this.reset = reset;
	}

	/**
	 * @return Returns the step.
	 */
	public String getStep() {
		return step;
	}

	/**
	 * @param step
	 *            The step to set.
	 */
	public void setStep(String step) {
		this.step = step;
	}

	/**
	 * @return Returns the context.
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context
	 *            The context to set.
	 */
	public void setContext(String context) {
		this.context = context;
	}

	public int getIsPreview() {
		return isPreview;
	}

	public void setIsPreview(int isPreview) {
		this.isPreview = isPreview;
	}

	public String getSvAction() {
		return svAction;
	}

	public void setSvAction(String svAction) {
		this.svAction = svAction;
	}

	public void addMessage(String key, String value) {
		this.messages.put(key, value);
	}

	public void addError(String key, String value) {
		this.errors.put(key, value);
	}

	public void clearMessages() {
		this.errors.clear();
		this.messages.clear();
	}

	/**
	 * @return the errors
	 */
	public HashMap<String, String> getErrors() {
		return errors;
	}

	/**
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(HashMap<String, String> errors) {
		this.errors = errors;
	}

	/**
	 * @return the messages
	 */
	public HashMap<String, String> getMessages() {
		return messages;
	}

	/**
	 * @param messages
	 *            the messages to set
	 */
	public void setMessages(HashMap<String, String> messages) {
		this.messages = messages;
	}

	public Identification getIdentification() {
		if (this.identification == null) {
			this.identification = new Identification();
		}
		return identification;
	}

	public Planning getPlanning() {
		if (this.planning == null) {
			this.planning = new Planning();
		}
		return this.planning;
	}

	public Location getLocation() {
		if (this.location == null) {
			this.location = new Location();
		}
		return this.location;
	}

	public Sector getSectors() {
		if (this.sectors == null) {
			this.sectors = new Sector();
		}
		return this.sectors;

	}

	public Component getComponents() {
		if (this.components == null) {
			this.components = new Component();
		}
		return this.components;

	}

	public Programs getPrograms() {
		if (this.programs == null) {
			this.programs = new Programs();
		}
		return this.programs;

	}

	public CrossCuttingIssues getCrossIssues() {
		if (this.crossIssues == null) {
			this.crossIssues = new CrossCuttingIssues();
		}
		return this.crossIssues;

	}
	
	public Funding getFunding() {
		if (this.funding == null) {
			this.funding = new Funding();
		}
		return this.funding;
	}
	
	public Funding getOldFunding() {
		if (this.oldFunding == null) {
			this.oldFunding = new Funding();
		}
		return this.oldFunding;
	}

	public Documents getDocuments() {
		if (this.documents == null) {
			this.documents = new Documents();
		}
		return documents;
	}

	public Agencies getAgencies() {
		if (this.agencies == null) {
			this.agencies = new Agencies();
		}
		return this.agencies;

	}

	public Survey getSurvey() {
		/*if (this.survey == null) {
			this.survey = new Survey();
		}*/
		return this.survey;
	}

	public ContactInformation getContactInfo() {
		if (this.contactInfo == null) {
			this.contactInfo = new ContactInformation();
		}
		return this.contactInfo;

	}

	public Comments getComments() {
		if (this.comments == null) {
			this.comments = new Comments();
		}
		return comments;
	}

	public PhisycalProgress getPhisycalProgress() {
		if (this.phisycalProgress == null) {
			this.phisycalProgress = new PhisycalProgress();
		}
		return phisycalProgress;
	}

	public Contracts getContracts() {
		if (this.contracts == null) {
			this.contracts = new Contracts();
		}
		return contracts;
	}

	public Costing getCosting() {
		if (this.costing == null) {
			this.costing = new Costing();
		}
		return costing;
	}

	public Issues getIssues() {
		if (this.issues == null) {
			this.issues = new Issues();
		}
		return issues;
	}
	
	public Issues getObservations() {
		if (this.observations == null) {
			this.observations = new Issues();
		}
		return observations;
	}

	// indexed properties getter should be on the root form object
	public OrgProjectId getSelectedOrganizations(int index) {
		return identification.selectedOrganizations[index];
	}

	public ActivitySector getActivitySectors(int index) {
		return (ActivitySector) (sectors.activitySectors.toArray()[index]);
	}

	public AmpActivityProgram getNationalPlanObjectivePrograms(int index) {
		return (AmpActivityProgram) (programs.nationalPlanObjectivePrograms.toArray()[index]);
	}

	public AmpActivityProgram getPrimaryPrograms(int index) {
		return (AmpActivityProgram) (programs.primaryPrograms.toArray()[index]);
	}

	public AmpActivityProgram getSecondaryPrograms(int index) {
		return (AmpActivityProgram) (programs.secondaryPrograms.toArray()[index]);
	}
	
	public AmpActivityProgram getTertiaryPrograms(int index) {
		return (AmpActivityProgram) (programs.tertiaryPrograms.toArray()[index]);
	}

	public FundingDetail getFundingDetail(int index) {
		int currentSize = funding.fundingDetails.size();
		if (index >= currentSize) {
			for (int i = 0; i <= index - currentSize; i++) {
				funding.fundingDetails.add(new FundingDetail());
			}
		}
		return (FundingDetail) funding.fundingDetails.get(index);
	}

	public MTEFProjection getMtefProjection(int index) {
		while (funding.fundingMTEFProjections.size() <= index) {
			funding.fundingMTEFProjections.add(new MTEFProjection());
		}
		return funding.fundingMTEFProjections.get(index);
	}

	public IndicatorME getIndicator() {
		if (this.indicatorME == null) {
			this.indicatorME = new IndicatorME();
		}
		return this.indicatorME;
	}

	public String[] getStepText() {
		return stepText;
	}

	public void setStepText(String[] stepText) {
		this.stepText = stepText;
	}

	public Boolean[] getStepFailure() {
		return stepFailure;
	}

	public void setStepFailure(Boolean[] stepFailure) {
		this.stepFailure = stepFailure;
	}

	public String[] getStepFailureText() {
		return stepFailureText;
	}

	public void setStepFailureText(String[] stepFailureText) {
		this.stepFailureText = stepFailureText;
	}

	public void setCustomFields(List<CustomField<?>> customFields) {
		this.customFields = customFields;
	}

	public List<CustomField<?>> getCustomFields() {
		return customFields;
	}

	public void setCustomFieldsSteps(List<CustomFieldStep> customFieldsSteps) {
		this.customFieldsSteps = customFieldsSteps;
	}

	public List<CustomFieldStep> getCustomFieldsSteps() {
		return customFieldsSteps;
	}

	public boolean isEditAct() {
		return editAct;
	}

	public void setEditAct(boolean editAct) {
		this.editAct = editAct;
	}

	public org.digijava.module.aim.helper.Location getSelectedLocs(int index) {
		return (org.digijava.module.aim.helper.Location) (this.location.selectedLocs.toArray()[index]);
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public Collection getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Collection currencies) {
		this.currencies = currencies;
	}

	public boolean isSerializeFlag() {
		return serializeFlag;
	}

	public void setSerializeFlag(boolean serializeFlag) {
		this.serializeFlag = serializeFlag;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}



	public boolean isTeamLead() {
		return teamLead;
	}

	public void setTeamLead(boolean teamLead) {
		this.teamLead = teamLead;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getEditKey() {
		return editKey;
	}

	public void setEditKey(String editKey) {
		this.editKey = editKey;
	}
	
	public boolean isTotDisbIsBiggerThanTotCom() {
		return this.totDisbIsBiggerThanTotCom;
	}

	public void setTotDisbIsBiggerThanTotCom(boolean totDisbIsBiggerThanTotCom) {
		this.totDisbIsBiggerThanTotCom = totDisbIsBiggerThanTotCom;
	}
      public String getFundingCurrCode() {
        return fundingCurrCode;
    }

    public void setFundingCurrCode(String fundingCurrCode) {
        this.fundingCurrCode = fundingCurrCode;
    }
    public String getRegFundingPageCurrCode() {
        return regFundingPageCurrCode;
    }

    public void setRegFundingPageCurrCode(String regfundingPageCurrCode) {
        this.regFundingPageCurrCode = regfundingPageCurrCode;
    }

    /*
	public void setContactInformation(ActivityContactInfo contactInformation) {
		this.contactInformation = contactInformation;
	}
	*/

	public ActivityContactInfo getContactInformation() {
		if(this.contactInformation==null){
			this.contactInformation=new ActivityContactInfo ();
		}
		return this.contactInformation;
	}

	public void setActivityContacts(List<AmpActivityContact> activityContacts) {
		this.activityContacts = activityContacts;
	}

	public List<AmpActivityContact> getActivityContacts() {
		return activityContacts;
	}

	public void setMofedContacts(List<AmpActivityContact> mofedContacts) {
		this.mofedContacts = mofedContacts;
	}

	public List<AmpActivityContact> getMofedContacts() {
		return mofedContacts;
	}

	public void setDonorContacts(List<AmpActivityContact> donorContacts) {
		this.donorContacts = donorContacts;
	}

	public List<AmpActivityContact> getDonorContacts() {
		return donorContacts;
	}

	public void setSectorMinistryContacts(List<AmpActivityContact> sectorMinistryContacts) {
		this.sectorMinistryContacts = sectorMinistryContacts;
	}

	public List<AmpActivityContact> getSectorMinistryContacts() {
		return sectorMinistryContacts;
	}

	public void setProjCoordinatorContacts(List<AmpActivityContact> projCoordinatorContacts) {
		this.projCoordinatorContacts = projCoordinatorContacts;
	}

	public List<AmpActivityContact> getProjCoordinatorContacts() {
		return projCoordinatorContacts;
	}

	public void setImplExecutingAgencyContacts(
			List<AmpActivityContact> implExecutingAgencyContacts) {
		this.implExecutingAgencyContacts = implExecutingAgencyContacts;
	}

	public List<AmpActivityContact> getImplExecutingAgencyContacts() {
		return implExecutingAgencyContacts;
	}

	public FormFile getFileImport() {
		return fileImport;
	}

	public void setFileImport(FormFile fileImport) {
		this.fileImport = fileImport;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public String getButtonText() {
		return buttonText;
	}

	public String getActivityExists() {
		return activityExists;
	}

	public void setActivityExists(String activityExists) {
		this.activityExists = activityExists;
	}

	public boolean isPopupView() {
		return popupView;
	}

	public void setPopupView(boolean popupView) {
		this.popupView = popupView;
	}	
	
}



