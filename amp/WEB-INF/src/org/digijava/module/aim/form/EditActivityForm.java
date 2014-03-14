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

import lombok.Data;

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
import org.digijava.module.aim.form.helpers.ActivityFundingDigest;
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

@Data
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
	
//	private String stepText[];
//	private Boolean stepFailure[];
//	private String stepFailureText[];
//	private String step = null;
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
	
	private Issues lineMinistryObservations = new Issues();
	
	/**
	 * Map Api url for locations map
	 */
	private String esriapiurl;
	    
     /**
     * 0 displayAddDefaultProgram = "true" & displayAddProgram = "false";
     * 1 displayAddDefaultProgram = "false" & displayAddProgram = "true";
     * 2 displayAddDefaultProgram = "true" & displayAddProgram = "true";   
     */
	private Integer displayProgram = new Integer(0);
	
	/**
     * This collection represents the list of surveys available in the Paris Indicator page.
     */
    private Collection<SurveyFunding> surveyFundings = null;
    private int draftRedirectedPage;
    private List<String> warningMessges;

    @Data
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
    }
	
    @Data
	public class Contracts {
		private List contracts;
		private Double ipaBudget = null;
		private Integer selContractId;
		private String contractDetails = null;
	}

    @Data
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
		
		private String ssc_typeOfCooperation;
		private String ssc_typeOfImplementation;
		private String ssc_modalities;
        private Integer fundingSourcesNumber;

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

		public void setStatusReason(String statusReason) {
			if (statusReason != null)
				statusReason = statusReason.trim();
			this.statusReason = statusReason;
		}
	}

    @Data
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
	}

    @Data
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
		private String fill;
		
		/**
		 * which among countries,region,zone and woreda need to be loaded with data. ie if the value of fill is 'region', load all region data beloning to a particluar
		 * country selected
		 */
		private Integer impLevelValue; // Implementation Level value
		private String impCountry; // Implementation country
		
		private TreeMap<Integer, Collection<KeyValue>> locationByLayers;
		
		private TreeMap<Integer, Long> selectedLayers ;
		
		private Long parentLocId;
		
		private Long [] userSelectedLocs;
		
		private String country;
		private Long levelId = null;
        private int levelIdx;
		private Long implemLocationLevel = null;
		private Collection<org.digijava.module.aim.helper.Location> selectedLocs = null;
		private boolean defaultCountryIsSet;
		private int pagesSize;
		
		private boolean noMoreRecords=false;

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

		public void reset(ActionMapping mapping, HttpServletRequest request) {
			fill = "country";
			impLevelValue = new Integer(0);
			impCountry = "";
		}
	}

    @Data
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

		public void setPages(Collection pages) {
			this.pages = pages;
			if (pages != null) {
				this.pagesSize = pages.size();
			}
		}

	}

    @Data
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
	}

    @Data
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
		
		public ArrayList<Long> getDeletedObservations() {
			if (this.deletedObservations == null) {
				this.deletedObservations = new ArrayList();
			}
			return deletedObservations;
		}

		public ArrayList<Long> getDeletedMeasures() {
			if (this.deletedMeasures == null) {
				this.deletedMeasures = new ArrayList();
			}
			return deletedMeasures;
		}

		public ArrayList<Long> getDeletedActors() {
			if (this.deletedActors == null) {
				this.deletedActors = new ArrayList();
			}
			return deletedActors;
		}

	}

    @Data
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
	}

    @Data
	public class CrossCuttingIssues {
		private String equalOpportunity;
		private String environment;
		private String minorities;
	}



	public FundingOrganization getFundingOrganization(int index) {
		if(getFunding().getFundingOrganizations() == null)
			return null;
		int currentSize = getFunding().getFundingOrganizations().size();
		if (index >= currentSize) {
			for (int i = 0; i <= index - currentSize; i++) {
				getFunding().getFundingOrganizations().add(new FundingOrganization());
			}
		}
		return (FundingOrganization) ((ArrayList) getFunding().getFundingOrganizations()).get(index);
	}
	
	@Data
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
	}

	@Data
	public class Comments {
		private String actionFlag;
		private String fieldName;
		private AmpField field = null;

		private ArrayList commentsCol = new ArrayList();
		private HashMap allComments = new HashMap();
		private boolean commentFlag;
		private String commentText = null;
		private Long ampCommentId;
	}

	@Data
	public class PhisycalProgress {
		private Collection selectedPhysicalProgress;
		private Long selPhyProg[];
		private boolean phyProgReset;
		private String phyProgTitle;
		private String phyProgDesc;
		private String phyProgRepDate;
		private Long phyProgId;
	}

	@Data
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
		//private Collection documentList;
		private List<org.digijava.module.aim.helper.Documents> documents = new ArrayList<org.digijava.module.aim.helper.Documents>();
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
	}

	@Data
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

	@Data
	public class Costing {
		private Double allCosts;
		private List costs;
		private Double overallCost = null;
		private Double overallContribution = null;
	}

	@Data
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
	}

	@Data
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
	}

	private Identification identification;
	private Planning planning;
	private Location location;
	private Sector sectors;
	private Component components;
	private Programs programs;
	private CrossCuttingIssues crossIssues;
	private ActivityFundingDigest funding;
	//private ActivityFundingDigest oldFunding;
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

	public EditActivityForm() {
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
			//this.oldFunding = null;
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
            //this.funding=null;
            this.issues=null;
            this.observations=null;
			reset = false;
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
	
	public ActivityFundingDigest getFunding() {
		if (this.funding == null) {
			this.funding = new ActivityFundingDigest();
		}
		return this.funding;
	}
	
//	public ActivityFundingDigest getOldFunding() {
//		if (this.oldFunding == null) {
//			this.oldFunding = new ActivityFundingDigest();
//		}
//		return this.oldFunding;
//	}

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
		int currentSize = funding.getFundingDetails().size();
		if (index >= currentSize) {
			for (int i = 0; i <= index - currentSize; i++) {
				funding.getFundingDetails().add(new FundingDetail());
			}
		}
		return funding.getFundingDetails().get(index);
	}

//	public MTEFProjection getMtefProjection(int index) {
//		while (funding.fundingMTEFProjections.size() <= index) {
//			funding.fundingMTEFProjections.add(new MTEFProjection());
//		}
//		return funding.fundingMTEFProjections.get(index);
//	}

	public IndicatorME getIndicator() {
		if (this.indicatorME == null) {
			this.indicatorME = new IndicatorME();
		}
		return this.indicatorME;
	}

	public org.digijava.module.aim.helper.Location getSelectedLocs(int index) {
		return (org.digijava.module.aim.helper.Location) (this.location.selectedLocs.toArray()[index]);
	}

	public ActivityContactInfo getContactInformation() {
		if(this.contactInformation==null){
			this.contactInformation=new ActivityContactInfo ();
		}
		return this.contactInformation;
	}	
}



