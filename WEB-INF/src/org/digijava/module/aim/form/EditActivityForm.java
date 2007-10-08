/**
 * @author Priyajith
 * @version 0.1
 */

package org.digijava.module.aim.form;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.*;

public class EditActivityForm extends ActionForm implements Serializable{

private int isPreview=0;
	private Long activityId = null;
	private String ampId = null;
	private String title = null;
	private String description = null;
	private String lessonsLearned=null;
	private String purpose = null;
	private String results = null;
	private Double overallCost = null;
	private Double overallContribution=null;
	private String objectives = null;
    private String documentSpace = null;
	private String condition = null;
	private Long statusId = null;
	private String status=null;
	private String statusReason = null;
	private Collection financingBreakdown=null;
	//private Collection statusCollection = null;
	private Long levelId = null;
	private Long modality = null;
	private Long countryIso = null;
	private Long assistanceType = null;
	private String createdDate;
	private String updatedDate;
	private AmpTeamMember updatedBy;
        private AmpTeamMember createdBy;
	private Collection perspectives;
	private Boolean budget=true;
	private String budgetCheckbox;
	private double regionTotalDisb;
	private AmpTeam team;

    private Long selPrograms[];
    private Long selProgramId;
    private Long program;
    private List actPrograms;
	private Collection programCollection;
    private Long selectedPrograms[];
    private Double allCosts;
    private Long logframeCategory;

	private String originalAppDate;
	private String revisedAppDate;
	private String originalStartDate;
	private String revisedStartDate;
	private String currentCompDate;
	private String revisedCompDate;
	private String contractingDate;
	private String disbursementsDate;
	private String proposedCompDate;
	private Collection activityCloseDates;
	private String multiSectorSelecting;

	private String currentValDate;
	private String currentValComments;
	private String programDescription;

	private List costs;

	/**
	 * List of ReferenceDoc helper beans
	 */
	private List referenceDocs;
	
	/**
	 * Doc names come from CategoryValues.
	 * And this is list of IDs of those CategoryValues.
	 */
	private Long[] allReferenceDocNameIds;
//	private String[] refDocComments;
	
	// location selector pop-up
	private Long selLocs[] = null; // location selected from step 2 page to
	// remove from the locations list
	private Collection selectedLocs = null; // list of locations to be added to
	//the activity
	private Collection searchLocs = null; // list of searched locations.
	private Long searchedLocs[] = null; // locations selected by user to be added in activity after searching

	private Long selOrgs[] = null;
	private List selectedOrgs = null; // list of organisations to be added to the activity

	private Collection levelCollection = null;
	//private Collection modalityCollection = null;
	//private Collection assistanceTypes = null;

	private String step = null;

	private String visibleProgram=null;

	private Collection activitySectors; // sectors related to the activity
	private Long selActivitySectors[];

	private Collection cols = null;
	private Collection colsAlpha = null;
	private Collection orgTypes = null;
	private boolean reset;
	private boolean orgPopupReset;
	//private String implementationLevel = null;
	private Long implemLocationLevel;

	private Collection selectedPhysicalProgress;
	private Long selPhyProg[];
	private Collection documentList;
	private Collection documents;
    private Collection managedDocumentList;
	private long selDocs[];
	private Collection linksList;
	private long selLinks[];
    private String selManagedDocs[];
	private OrgProjectId selectedOrganizations[];
	//private Collection selectedOrganizationsList;
	private Collection executingAgencies;
	private Long selExAgencies[];
	private Collection impAgencies;
	private Long selImpAgencies[];
	//private Collection contractors;
	private Long selContractors[];
	private Collection reportingOrgs;
	private Long selReportingOrgs[];

	// Beneficiary Agency
	private Collection benAgencies;
	private Long selBenAgencies[];
	// /Beneficiary Agency

	// Contracting Agency
	private Collection conAgencies;
	private Long selConAgencies[];
	// /Contracting Agency

	private String contFirstName;
	private String contLastName;
	private String email;

	// contact information
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

	private String actAthFirstName;
	private String actAthLastName;
	private String actAthEmail;
	private String actAthAgencySource;

	private String conditions;
	private int item;

	//private Collection selectedComponents;
	//private Long selComp[];

	// FOR SELECT ORGANIZATION POPUP
	private Long ampOrgTypeId;
	private String orgType;
	private String keyword;
	private int tempNumResults;
	private Collection pagedCol;
	private Integer currentPage;
	private String currentAlpha;
	private boolean startAlphaFlag;
	private Long selOrganisations[]; // list of org selected from
	private TreeSet selectedOrganisationPaged;
	private Integer selectedOrganisationFromPages;

	// pop-up organisation selector window
	private Collection pages;
	private String[] alphaPages;
	private int numResults;

	//	For view comment popup
	private String actionFlag = null;
	private boolean serializeFlag;
	private boolean commentFlag;
	private String commentText = null;
	private Long ampCommentId;
	private String fieldName;
	private AmpField field = null;
	private ArrayList commentsCol = new ArrayList();
	private HashMap allComments=new HashMap();

	// For activity approval process
	private String approvalStatus;
	private String workingTeamLeadFlag;

	// For aid harmonization survey indicators
	private Collection survey = null;
	private List indicators = null; 	// holds collection of Indicator helper objects
	private Collection pageColl = null;	// total number of survey pages
	private Integer currPage = null;
	private Integer startIndex = null;	// starting record for iteartion over indicator collection on each page
	private String fundingOrg = null;	// acronym of funding organisation
	private Long ampSurveyId = null;
	private Boolean surveyFlag = null;	// if true then survey properties are cleared

	private String totalCommitted = "";
	private String totalDisbursed = "";
	private String totalUnDisbursed = "";
	private String totalExpenditure = "";
	private String totalUnExpended = "";
	private String totalExpended="";


	/* START FINANCIAL EDIT */
	private Collection fundingOrganizations; // Collection of FundingOrganization objects
	private String orgFundingId;
	private Long fundingId;
	private Long orgId;
	private String orgName;
	private String signatureDate;
	private String event;
	private String reportingDate;
	private String plannedStartDate;
	private String plannedCompletionDate;
	private String actualStartDate;
	private String actualCompletionDate;
	private String fundingConditions;
	private Collection currencies;
	private Collection organizations;
	private List fundingDetails; //Collection of FundingDetail objects
	private List<MTEFProjection> fundingMTEFProjections; //collection of funding mtefprojections objects
	private Collection projections; //the values in the category manager for the projections
	
	
	private boolean dupFunding;
	private boolean editFunding;
	private int offset;
	private long transIndexId;
	private Long selFundingOrgs[];

	private List programLevels;

    private int numComm;
	private int numDisb;
	private int numExp;
	/* Projections */
	private int numProjections;

	private String contractors;

	/* END FINANCIAL EDIT */

	private boolean orgSelReset;
	// FOR SELECT LOCATION POPUP
	private String fill; // which among countries,region,zone and woreda need to
						 // be loaded with data. ie if the value

	// of fill is 'region', load all region data beloning to a particluar
	// country selected
	private Integer impLevelValue; // Implementation Level value
	private String impCountry; // Implementation country

	private Long impRegion; // Implementation region
	private Long impMultiRegion[]; // Implementation region

	private Long impZone; // Implementation zone
	private Long impMultiZone[]; // Implementation zone

	private Long impWoreda; // Implementation woreda
	private Long impMultiWoreda[];

	private Collection countries;

	private Collection regions;

	private Collection zones;

	private Collection woredas;

	private boolean locationReset;

	private String country;

	// FOR SELECT SECTOR POPUP

	// FOR ADD PHYSICAL PROGRESS POPUP
	private boolean phyProgReset;

	private String phyProgTitle;

	private String phyProgDesc;

	private String phyProgRepDate;

	private Long phyProgId;

	// FOR ADD DOCUMENT POPUP
	private FormFile docFile;
	private String docWebResource;
	private String docTitle;
	private String docDescription;
	private String docDate;
	private String docFileOrLink;
	private boolean docReset;
	private boolean showInHomePage;
	private int pageId;
	private boolean editAct;
	private boolean firstSubmit;
	private Long docType;
	private Long docLang;
	private String docComment;

	// FOR ADD COMPONENTS
	private Collection allComps;
	private boolean componentReset;
	private String componentTitle;
	private String componentDesc;
	private String componentAmount;
	////////////////////
	private double compTotalDisb;

	private String currencyCode;
	private String componentRepDate;
	private Long componentId;
	private Collection<Components<FundingDetail>> selectedComponents;
	private Long[] selComp;

	private String author;
	private String context;

	private Collection regionalFundings;
	private Long[] selRegFundings;
	private Collection fundingRegions;
	private Long fundingRegionId;

	private ArrayList issues;
	private Long[] selIssues;
	private String issue;
	private Long[] selMeasures;
	private String measure;
	private Long issueId;
	private Long measureId;
	private Long[] selActors;
	private String actor;
	private Long actorId;

	private String editKey;

	private double totalCommitments;
	private double totalDisbursements;
	private double totalExpenditures;
	private String currCode;

	private boolean donorFlag;
	private Long fundDonor;

	private Collection indicatorsME;
	private ActivityIndicator indicatorValues;
	private Long indicatorId;
	private Long indicatorValId;
	private Long expIndicatorId;
	private float baseVal;
	private String baseValDate;
	private String baseValComments;
	private float targetVal;
	private String targetValDate;
	private String targetValComments;
	private float revTargetVal;
	private String revTargetValDate;
	private String revTargetValComments;
	private float currentVal;
	private String currValDate;
	private String currValComments;
	private String comments;
	private float revisedTargetVal;

	private Collection riskCollection;
	private Long indicatorRisk;
	private Collection indHistory;
	private Collection updateIndValues;
	private Collection indicatorPriorValues;
    private ProposedProjCost proProjCost;

    private String lineMinRank;
	private String planMinRank;
	private Collection actRankCollection;

	/*Categories*/
	private Long acChapter				= new Long(0);
	private Long accessionInstrument	= new Long(0);

	 private Collection searchedSectors = null; // list of searched Sectors.

	 private String equalOpportunity;
	 private String environment;
	 private String minorities;
	 

	 /*
	  * Tanzania ADDS
	  */
	 private String FY;
	 private String vote;
	 private String subVote;
	 private String subProgram;
	 private String projectCode;
	 private Long gbsSbs;

	 private Boolean governmentApprovalProcedures;
	 private Boolean jointCriteria;
  private boolean defaultCountryIsSet;
	 
	 
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

	public Collection getSearchedSectors() {
		return searchedSectors;
	}

	public void setSearchedSectors(Collection searchedSectors) {
		this.searchedSectors = searchedSectors;
	}

	public Long getAcChapter() {
		return acChapter;
	}

	public void setAcChapter(Long acChapter) {
		this.acChapter = acChapter;
	}

	public EditActivityForm() {
		step = "1";
		reset = false;
		editAct = false;
		orgPopupReset = true;
//		numResults = 0;
		tempNumResults = 0;
		pageId = 0;
		modality = null;
		docWebResource = "http://";
		editKey = "";
		fundingRegionId = new Long(-1);
		totalCommitments = 0;
		totalDisbursements = 0;
		totalExpenditures = 0;
		donorFlag = false;
		keyword=null;
		//selectedOrganizationsList=null;
		//selectedOrganisationFromPages= new Integer(-1);
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {

		if (reset) {
			donorFlag = false;
			fundDonor = null;
			createdDate = null;
			activityId = null;
			ampId = null;
			title = null;
			description = null;
			results = null;
			purpose=  null;
			objectives = null;
            documentSpace = null;
			condition = null;
			statusId = new Long(0);
			status=null;
			statusReason = null;
			//statusCollection = null;
			levelId = new Long(0);
			program = null;
			modality = null;
			countryIso = null;
			assistanceType = null;
			selLocs = null;
			selectedLocs = null;
			selOrgs = null;
			selectedOrgs = null;
			levelCollection = null;
			programCollection = null;
//			modalityCollection = null;
			//assistanceTypes = null;
			step = "1";
			activitySectors = null;
//			numResults = 0;
			cols = null;
			colsAlpha = null;
			orgTypes = null;
//			implementationLevel = null;
			implemLocationLevel		= null;

			selectedPhysicalProgress = null;
			componentDesc = null;
			selPhyProg = null;
			documentList = null;
            managedDocumentList = null;
			selDocs = null;
			linksList = null;
			selLinks = null;
            selManagedDocs = null;
			selectedOrganizations = null;
			pages = null;
			alphaPages = null;
			fundingOrganizations = null;
			fundingId = null;
			orgFundingId = null;
			orgId = null;
			orgName = null;
			signatureDate = null;
			reportingDate = null;
			plannedCompletionDate = null;
			plannedStartDate = null;
			actualStartDate = null;
			actualCompletionDate = null;
			fundingConditions = null;
			event = null;
			currencies = null;
			organizations = null;
			fundingDetails = null;
			editFunding = false;
			offset = 0;
			selFundingOrgs = null;
			reset = false;
			editAct = false;
			currentPage = new Integer(0);
			currentAlpha = null;
			executingAgencies = null;
			impAgencies = null;
			benAgencies = null;
			conAgencies = null;
			contractors = null;
			reportingOrgs = null;
			selReportingOrgs = null;
			selExAgencies = null;
			selImpAgencies = null;
			selBenAgencies = null;
			selConAgencies = null;
			selContractors = null;
			contFirstName = null;
			contLastName = null;
			email = null;
			conditions = null;
			selectedComponents = null;
			componentId = null;
			selComp = null;
			programDescription = null;
			originalAppDate = null;
			revisedAppDate = null;
			originalStartDate = null;
			revisedStartDate = null;
			currentCompDate = null;
			proposedCompDate = null;
			revisedCompDate = null;
			activityCloseDates = null;
			author = null;
			dnrCntEmail = null;
			dnrCntFirstName = null;
			dnrCntLastName = null;
			dnrCntTitle = null;
			dnrCntOrganization = null;
			dnrCntPhoneNumber = null;
			dnrCntFaxNumber = null;
			mfdCntEmail = null;
			mfdCntFirstName = null;
			mfdCntLastName = null;
			mfdCntTitle = null;
			mfdCntOrganization = null;
			mfdCntPhoneNumber = null;
			mfdCntFaxNumber = null;
			actAthEmail = null;
			actAthFirstName = null;
			actAthAgencySource=null;
			actAthLastName = null;
			regionalFundings = null;
			issues = null;
			editKey = "";
			regionalFundings = null;
			perspectives = null;
			totalCommitments = 0;
			totalDisbursements = 0;
			totalExpenditures = 0;
			lineMinRank = null;
			planMinRank = null;
			costs=null;
			logframeCategory=null;
			docType=null;
			docLang=null;
			documents=null;
			contractingDate = null;
			disbursementsDate = null;
			overallCost = null;
			overallContribution=null;
			allComments=null;
			financingBreakdown=null;
			visibleProgram=null;
			fundingRegions = null;
			
			allReferenceDocNameIds =null;
//			refDocComments = null;
			referenceDocs = null;
			
			budget		= true;
		}

		if (orgSelReset) {
			pagedCol = null;
			selOrganisations = null;
			keyword = null;
			setOrgType("");
			setAmpOrgTypeId(null);
			//setTempNumResults(10);
		}

		/*if (sectorReset) {
			sector = new Long(-1);
			subsectorLevel1 = new Long(-1);
			subsectorLevel2 = new Long(-1);
			sectorScheme = new Long(-1);
			parentSectors = null;
			childSectorsLevel1 = null;
			childSectorsLevel2 = null;
		}*/

		if (locationReset) {
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

		if (phyProgReset) {
			phyProgTitle = null;
			phyProgDesc = null;
			phyProgRepDate = null;
			phyProgId = null;
		}

		if (docReset) {
			docFile = null;
			docWebResource = null;
			docTitle = null;
			docDescription = null;
			showInHomePage = false;
			docComment		= null;
			docDate = null;
			docType = null;
			docLang = null;
		}
		if (componentReset) {
			componentTitle = null;
			componentDesc = null;
			componentAmount = null;
			currencyCode = null;
			componentRepDate = null;
		}
	}

	public OrgProjectId getSelectedOrganizations(int index) {
		return selectedOrganizations[index];
	}

	public void setSelectedOrganizations(int index, OrgProjectId orgProjectId) {
		selectedOrganizations[index] = orgProjectId;
	}

	public FundingDetail getFundingDetail(int index) {
		int currentSize = fundingDetails.size();
		if (index >= currentSize) {
			for (int i = 0; i <= index - currentSize; i++) {
				fundingDetails.add(new FundingDetail());
			}
		}
		return (FundingDetail) fundingDetails.get(index);
	}

	public OrgProjectId getSelectedOrgs(int index) {
		int currentSize = selectedOrgs.size();
		if (index >= currentSize) {
			for (int i = 0; i <= index - currentSize; i++) {
				selectedOrgs.add(new OrgProjectId());
			}
		}
		return (OrgProjectId) selectedOrgs.get(index);
	}


	/**
	 * @return Returns the activitySectors.
	 */
	public Collection getActivitySectors() {
		return activitySectors;
	}

	/**
	 * @param activitySectors
	 *            The activitySectors to set.
	 */
	public void setActivitySectors(Collection activitySectors) {
		this.activitySectors = activitySectors;
	}

	/**
	 * @return Returns the ampId.
	 */
	public String getAmpId() {
		return ampId;
	}

	/**
	 * @param ampId
	 *            The ampId to set.
	 */
	public void setAmpId(String ampId) {
		this.ampId = ampId;
	}

	/**
	 * @return Returns the assistanceType.
	 */
	public Long getAssistanceType() {
		return assistanceType;
	}

	/**
	 * @param assistanceType
	 *            The assistanceType to set.
	 */
	public void setAssistanceType(Long assistanceType) {
		this.assistanceType = assistanceType;
	}

	/**
	 * @return Returns the childSectorsLevel1.
	 */
	/**
	 * @param childSectorsLevel1
	 *            The childSectorsLevel1 to set.
	 */

	/**
	 * @return Returns the childSectorsLevel2.
	 */

	/**
	 * @param childSectorsLevel2
	 *            The childSectorsLevel2 to set.
	 */

	/**
	 * @return Returns the cols.
	 */
	public Collection getCols() {
		return cols;
	}

	/**
	 * @param cols
	 *            The cols to set.
	 */
	public void setCols(Collection cols) {
		this.cols = cols;
	}

	/**
	 * @return Returns the colsAlpha.
	 */
	public Collection getColsAlpha() {
		return colsAlpha;
	}

	/**
	 * @param colsAlpha
	 *            The colsAlpha to set.
	 */
	public void setColsAlpha(Collection colsAlpha) {
		this.colsAlpha = colsAlpha;
	}

	/**
	 * @return Returns the condition.
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            The condition to set.
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * @return Returns the countries.
	 */
	public Collection getCountries() {
		return countries;
	}

	/**
	 * @param countries
	 *            The countries to set.
	 */
	public void setCountries(Collection countries) {
		this.countries = countries;
	}

	/**
	 * @return Returns the countryIso.
	 */
	public Long getCountryIso() {
		return countryIso;
	}

	/**
	 * @param countryIso
	 *            The countryIso to set.
	 */
	public void setCountryIso(Long countryIso) {
		this.countryIso = countryIso;
	}

	/**
	 * @return Returns the currencies.
	 */
	public Collection getCurrencies() {
		return currencies;
	}

	/**
	 * @param currencies
	 *            The currencies to set.
	 */
	public void setCurrencies(Collection currencies) {
		this.currencies = currencies;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the event.
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            The event to set.
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return Returns the fill.
	 */
	public String getFill() {
		return fill;
	}

	/**
	 * @param fill
	 *            The fill to set.
	 */
	public void setFill(String fill) {
		this.fill = fill;
	}

	/**
	 * @return Returns the fundingDetails.
	 */
	public List getFundingDetails() {
		return fundingDetails;
	}

	/**
	 * @param fundingDetails
	 *            The fundingDetails to set.
	 */
	public void setFundingDetails(List fundingDetails) {
		this.fundingDetails = fundingDetails;
	}

	/**
	 * @return Returns the dupFunding.
	 */
	public boolean isDupFunding() {
		return dupFunding;
	}

	/**
	 * @param dupFunding The dupFunding to set.
	 */
	public void setDupFunding(boolean dupFunding) {
		this.dupFunding = dupFunding;
	}

	/**
	 * @return Returns the firstSubmit.
	 */
	public boolean isFirstSubmit() {
		return firstSubmit;
	}

	/**
	 * @param firstSubmit The firstSubmit to set.
	 */
	public void setFirstSubmit(boolean firstSubmit) {
		this.firstSubmit = firstSubmit;
	}

	/**
	 * @return Returns the fundingOrganizations.
	 */
	public FundingOrganization getFundingOrganization(int index) {
		int currentSize = fundingOrganizations.size();
		if (index >= currentSize) {
			for (int i = 0; i <= index - currentSize; i++) {
				fundingOrganizations.add(new FundingOrganization());
			}
		}
		return (FundingOrganization) ((ArrayList)fundingOrganizations).get(index);
	}

	/**
	 * @param fundingOrganizations
	 *            The fundingOrganizations to set.
	 */
	public void setFundingOrganizations(Collection fundingOrganizations) {
		this.fundingOrganizations = fundingOrganizations;
	}

	/**
	 * @return Returns the impCountry.
	 */
	public String getImpCountry() {
		return impCountry;
	}

	/**
	 * @param impCountry
	 *            The impCountry to set.
	 */
	public void setImpCountry(String impCountry) {
		this.impCountry = impCountry;
	}


	/**
	 * @return Returns the impLevelValue.
	 */
	public Integer getImpLevelValue() {
		return impLevelValue;
	}

	/**
	 * @param impLevelValue
	 *            The impLevelValue to set.
	 */
	public void setImpLevelValue(Integer impLevelValue) {
		this.impLevelValue = impLevelValue;
	}

	/**
	 * @return Returns the impRegion.
	 */
	public Long getImpRegion() {
		return impRegion;
	}

	/**
	 * @param impRegion
	 *            The impRegion to set.
	 */
	public void setImpRegion(Long impRegion) {
		this.impRegion = impRegion;
	}

	/**
	 * @return Returns the impWoreda.
	 */
	public Long getImpWoreda() {
		return impWoreda;
	}

	/**
	 * @param impWoreda
	 *            The impWoreda to set.
	 */
	public void setImpWoreda(Long impWoreda) {
		this.impWoreda = impWoreda;
	}

	/**
	 * @return Returns the impZone.
	 */
	public Long getImpZone() {
		return impZone;
	}

	/**
	 * @param impZone
	 *            The impZone to set.
	 */
	public void setImpZone(Long impZone) {
		this.impZone = impZone;
	}

	/**
	 * @return Returns the keyword.
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 *            The keyword to set.
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return Returns the level.
	 */
	public Long getLevelId() {
		return levelId;
	}

	/**
	 * @param level
	 *            The level to set.
	 */
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

	/**
	 * @return Returns the levelCollection.
	 */
	public Collection getLevelCollection() {
		return levelCollection;
	}

	/**
	 * @param levelCollection
	 *            The levelCollection to set.
	 */
	public void setLevelCollection(Collection levelCollection) {
		this.levelCollection = levelCollection;
	}

	/**
	 * @return Returns the locationReset.
	 */
	public boolean isLocationReset() {
		return locationReset;
	}

	/**
	 * @param locationReset
	 *            The locationReset to set.
	 */
	public void setLocationReset(boolean locationReset) {
		this.locationReset = locationReset;
	}

	/**
	 * @return Returns the modality.
	 */
	public Long getModality() {
		return modality;
	}

	/**
	 * @param modality
	 *            The modality to set.
	 */
	public void setModality(Long modality) {
		this.modality = modality;
	}


	/**
	 * @return Returns the objectives.
	 */
	public String getObjectives() {
		return objectives;
	}

	/**
	 * @param objectives
	 *            The objectives to set.
	 */
	public void setObjectives(String objectives) {
		this.objectives = objectives;
	}

	/**
	 * @return Returns the organizations.
	 */
	public Collection getOrganizations() {
		return organizations;
	}

	/**
	 * @param organizations
	 *            The organizations to set.
	 */
	public void setOrganizations(Collection organizations) {
		this.organizations = organizations;
	}

	/**
	 * @return Returns the orgFundingId.
	 */
	public String getOrgFundingId() {
		return orgFundingId;
	}

	/**
	 * @param orgFundingId
	 *            The orgFundingId to set.
	 */
	public void setOrgFundingId(String orgFundingId) {
		this.orgFundingId = orgFundingId;
	}

	/**
	 * @return Returns the orgId.
	 */
	public Long getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId
	 *            The orgId to set.
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return Returns the orgName.
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName
	 *            The orgName to set.
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @return Returns the orgPopupReset.
	 */
	public boolean isOrgPopupReset() {
		return orgPopupReset;
	}

	/**
	 * @param orgPopupReset
	 *            The orgPopupReset to set.
	 */
	public void setOrgPopupReset(boolean orgPopupReset) {
		this.orgPopupReset = orgPopupReset;
	}

	/**
	 * @return Returns the orgSelReset.
	 */
	public boolean isOrgSelReset() {
		return orgSelReset;
	}

	/**
	 * @param orgSelReset
	 *            The orgSelReset to set.
	 */
	public void setOrgSelReset(boolean orgSelReset) {
		this.orgSelReset = orgSelReset;
	}

	/**
	 * @return Returns the orgType.
	 */
	public String getOrgType() {
		return orgType;
	}

	/**
	 * @param orgType
	 *            The orgType to set.
	 */
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	/**
	 * @return Returns the pagedCol.
	 */
	public Collection getPagedCol() {
		return pagedCol;
	}

	/**
	 * @param pagedCol
	 *            The pagedCol to set.
	 */
	public void setPagedCol(Collection pagedCol) {
		this.pagedCol = pagedCol;
	}

	/**
	 * @return Returns the pages.
	 */
	public Collection getPages() {
		return pages;
	}

	/**
	 * @param alphaPages
	 *            The alphaPages to set.
	 */
	public void setAlphaPages(String[] pages) {
		this.alphaPages = pages;
	}

	/**
	 * @return Returns the alphaPages.
	 */
	public String[] getAlphaPages() {
		return alphaPages;
	}

	/**
	 * @param pages
	 *            The pages to set.
	 */
	public void setPages(Collection pages) {
		this.pages = pages;
	}

	/**
	 * @return Returns the parentSectors.
	 */
	/**
	 * @param parentSectors
	 *            The parentSectors to set.
	 */
	/**
	 * @return Returns the regions.
	 */
	public Collection getRegions() {
		return regions;
	}

	/**
	 * @param regions
	 *            The regions to set.
	 */
	public void setRegions(Collection regions) {
		this.regions = regions;
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
	 * @return Returns the selectedLocs.
	 */
	public Collection getSelectedLocs() {
		return selectedLocs;
	}

	/**
	 * @param selectedLocs
	 *            The selectedLocs to set.
	 */
	public void setSelectedLocs(Collection selectedLocs) {
		this.selectedLocs = selectedLocs;
	}

	/**
	 * @return Returns the selectedOrgs.
	 */
	public List getSelectedOrgs() {
		return selectedOrgs;
	}

	/**
	 * @param selectedOrgs
	 *            The selectedOrgs to set.
	 */
	public void setSelectedOrgs(List selectedOrgs) {
		this.selectedOrgs = selectedOrgs;
	}

	/**
	 * @return Returns the selLocs.
	 */
	public Long[] getSelLocs() {
		return selLocs;
	}

	/**
	 * @param selLocs
	 *            The selLocs to set.
	 */
	public void setSelLocs(Long[] selLocs) {
		this.selLocs = selLocs;
	}

	/**
	 * @return Returns the selOrganisations.
	 */
	public Long[] getSelOrganisations() {
		return selOrganisations;
	}

	/**
	 * @param selOrganisations
	 *            The selOrganisations to set.
	 */
	public void setSelOrganisations(Long[] selOrganisations) {
		this.selOrganisations = selOrganisations;
	}

	/**
	 * @return Returns the selOrgs.
	 */
	public Long[] getSelOrgs() {
		return selOrgs;
	}

	/**
	 * @param selOrgs
	 *            The selOrgs to set.
	 */
	public void setSelOrgs(Long[] selOrgs) {
		this.selOrgs = selOrgs;
	}

	/**
	 * @return Returns the signatureDate.
	 */
	public String getSignatureDate() {
		return signatureDate;
	}

	/**
	 * @param signatureDate
	 *            The signatureDate to set.
	 */
	public void setSignatureDate(String signatureDate) {
		this.signatureDate = signatureDate;
	}

	/**
	 * @return Returns the status.
	 */
	public Long getStatusId() {
		return statusId;
	}

	/**
	 * @param status
	 *            The status to set.
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}


	/**
	 * @return Returns the statusReason.
	 */
	public String getStatusReason() {
		return statusReason;
	}

	/**
	 * @param statusReason
	 *            The statusReason to set.
	 */
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
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
	 * @return Returns the tempNumResults.
	 */
	public int getTempNumResults() {
		return tempNumResults;
	}

	/**
	 * @param tempNumResults
	 *            The tempNumResults to set.
	 */
	public void setTempNumResults(int tempNumResults) {
		this.tempNumResults = tempNumResults;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the woredas.
	 */
	public Collection getWoredas() {
		return woredas;
	}

	/**
	 * @param woredas
	 *            The woredas to set.
	 */
	public void setWoredas(Collection woredas) {
		this.woredas = woredas;
	}

	/**
	 * @return Returns the zones.
	 */
	public Collection getZones() {
		return zones;
	}

	/**
	 * @param zones
	 *            The zones to set.
	 */
	public void setZones(Collection zones) {
		this.zones = zones;
	}

	public boolean isEditFunding() {
		return editFunding;
	}

	public void setEditFunding(boolean editFunding) {
		this.editFunding = editFunding;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Long[] getSelFundingOrgs() {
		return selFundingOrgs;
	}

	public void setSelFundingOrgs(Long[] selFundingOrgs) {
		this.selFundingOrgs = selFundingOrgs;
	}

	/**
	 * @return Returns the phyProgDesc.
	 */
	public String getPhyProgDesc() {
		return phyProgDesc;
	}

	/**
	 * @param phyProgDesc
	 *            The phyProgDesc to set.
	 */
	public void setPhyProgDesc(String phyProgDesc) {
		this.phyProgDesc = phyProgDesc;
	}

	/**
	 * @return Returns the phyProgRepDate.
	 */
	public String getPhyProgRepDate() {
		return phyProgRepDate;
	}

	/**
	 * @param phyProgRepDate
	 *            The phyProgRepDate to set.
	 */
	public void setPhyProgRepDate(String phyProgRepDate) {
		this.phyProgRepDate = phyProgRepDate;
	}

	/**
	 * @return Returns the phyProgReset.
	 */
	public boolean isPhyProgReset() {
		return phyProgReset;
	}

	/**
	 * @param phyProgReset
	 *            The phyProgReset to set.
	 */
	public void setPhyProgReset(boolean phyProgReset) {
		this.phyProgReset = phyProgReset;
	}

	/**
	 * @return Returns the phyProgTitle.
	 */
	public String getPhyProgTitle() {
		return phyProgTitle;
	}

	/**
	 * @param phyProgTitle
	 *            The phyProgTitle to set.
	 */
	public void setPhyProgTitle(String phyProgTitle) {
		this.phyProgTitle = phyProgTitle;
	}

	/**
	 * @return Returns the selectedPhysicalProgress.
	 */
	public Collection getSelectedPhysicalProgress() {
		return selectedPhysicalProgress;
	}

	/**
	 * @param selectedPhysicalProgress
	 *            The selectedPhysicalProgress to set.
	 */
	public void setSelectedPhysicalProgress(Collection selectedPhysicalProgress) {
		this.selectedPhysicalProgress = selectedPhysicalProgress;
	}

	/**
	 * @return Returns the selPhyProg.
	 */
	public Long[] getSelPhyProg() {
		return selPhyProg;
	}

	/**
	 * @param selPhyProg
	 *            The selPhyProg to set.
	 */
	public void setSelPhyProg(Long[] selPhyProg) {
		this.selPhyProg = selPhyProg;
	}

	/**
	 * @return Returns the docDescription.
	 */
	public String getDocDescription() {
		return docDescription;
	}

	/**
	 * @param docDescription
	 *            The docDescription to set.
	 */
	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
	}

	/**
	 * @return Returns the docFile.
	 */
	public FormFile getDocFile() {
		return docFile;
	}

	/**
	 * @param docFile
	 *            The docFile to set.
	 */
	public void setDocFile(FormFile docFile) {
		this.docFile = docFile;
	}

	/**
	 * @return Returns the docFileOrLink.
	 */
	public String getDocFileOrLink() {
		return docFileOrLink;
	}

	/**
	 * @param docFileOrLink
	 *            The docFileOrLink to set.
	 */
	public void setDocFileOrLink(String docFileOrLink) {
		this.docFileOrLink = docFileOrLink;
	}

	/**
	 * @return Returns the docReset.
	 */
	public boolean getDocReset() {
		return docReset;
	}

	/**
	 * @param docReset
	 *            The docReset to set.
	 */
	public void setDocReset(boolean docReset) {
		this.docReset = docReset;
	}

	/**
	 * @return Returns the docTitle.
	 */
	public String getDocTitle() {
		return docTitle;
	}

	/**
	 * @param docTitle
	 *            The docTitle to set.
	 */
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	/**
	 * @return Returns the docWebResource.
	 */
	public String getDocWebResource() {
		return docWebResource;
	}

	/**
	 * @param docWebResource
	 *            The docWebResource to set.
	 */
	public void setDocWebResource(String docWebResource) {
		this.docWebResource = docWebResource;
	}

	/**
	 * @return Returns the documentList.
	 */
	public Collection getDocumentList() {
		return documentList;
	}

	/**
	 * @param documentList
	 *            The documentList to set.
	 */
	public void setDocumentList(Collection documentList) {
		this.documentList = documentList;
	}

	/**
	 * @return Returns the linksList.
	 */
	public Collection getLinksList() {
		return linksList;
	}

	/**
	 * @param linksList
	 *            The linksList to set.
	 */
	public void setLinksList(Collection linksList) {
		this.linksList = linksList;
	}

	/**
	 * @return Returns the selDocs.
	 */
	public long[] getSelDocs() {
		return selDocs;
	}

	/**
	 * @param selDocs
	 *            The selDocs to set.
	 */
	public void setSelDocs(long[] selDocs) {
		this.selDocs = selDocs;
	}

	/**
	 * @return Returns the selLinks.
	 */
	public long[] getSelLinks() {
		return selLinks;
	}

	/**
	 * @param selLinks
	 *            The selLinks to set.
	 */
	public void setSelLinks(long[] selLinks) {
		this.selLinks = selLinks;
	}

	/**
	 * @return Returns the selectedOrganizations.
	 */
	public OrgProjectId[] getSelectedOrganizations() {
		return selectedOrganizations;
	}

	/**
	 * @param selectedOrganizations
	 *            The selectedOrganizations to set.
	 */
	public void setSelectedOrganizations(OrgProjectId[] selectedOrganizations) {
		this.selectedOrganizations = selectedOrganizations;
	}

	/**
	 * @return Returns the pageId.
	 */
	public int getPageId() {
		return pageId;
	}

	/**
	 * @param pageId
	 *            The pageId to set.
	 */
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	/**
	 * @return Returns the activityId.
	 */
	public Long getActivityId() {
		return activityId;
	}
	/**
	 * @param activityId The activityId to set.
	 */
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	/**
	 * @return Returns the editAct.
	 */
	public boolean isEditAct() {
		return editAct;
	}
	/**
	 * @param edit The editAct to set.
	 */
	public void setEditAct(boolean edit) {
		this.editAct = edit;
	}
	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return Returns the currentPage.
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}
	/**
	 * @param currentPage The currentPage to set.
	 */
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * @return Returns the currentAlpha.
	 */
	public String getCurrentAlpha() {
		return currentAlpha;
	}
	/**
	 * @param currentAlpha The currentAlpha to set.
	 */
	public void setCurrentAlpha(String currentAlpha) {
		this.currentAlpha = currentAlpha;
	}
	/**
	 * @return Returns the phyProgId.
	 */
	public Long getPhyProgId() {
		return phyProgId;
	}
	/**
	 * @param phyProgId The phyProgId to set.
	 */
	public void setPhyProgId(Long phyProgId) {
		this.phyProgId = phyProgId;
	}
	/**
	 * @return Returns the conditions.
	 */
	public String getConditions() {
		return conditions;
	}
	/**
	 * @param conditions The conditions to set.
	 */
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	/**
	 * @return Returns the contractors.
	 */
	public String getContractors() {
		return contractors;
	}
	/**
	 * @param contractors The contractors to set.
	 */
	public void setContractors(String contractors) {
		this.contractors = contractors;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the executingAgencies.
	 */
	public Collection getExecutingAgencies() {
		return executingAgencies;
	}
	/**
	 * @param executingAgencies The executingAgencies to set.
	 */
	public void setExecutingAgencies(Collection executingAgencies) {
		this.executingAgencies = executingAgencies;
	}
	/**
	 * @return Returns the impAgencies.
	 */
	public Collection getImpAgencies() {
		return impAgencies;
	}
	/**
	 * @param impAgencies The impAgencies to set.
	 */
	public void setImpAgencies(Collection impAgencies) {
		this.impAgencies = impAgencies;
	}
	/**
	 * @return Returns the selContractors.
	 */
	public Long[] getSelContractors() {
		return selContractors;
	}
	/**
	 * @param selContractors The selContractors to set.
	 */
	public void setSelContractors(Long[] selContractors) {
		this.selContractors = selContractors;
	}
	/**
	 * @return Returns the selExAgencies.
	 */
	public Long[] getSelExAgencies() {
		return selExAgencies;
	}
	/**
	 * @param selExAgencies The selExAgencies to set.
	 */
	public void setSelExAgencies(Long[] selExAgencies) {
		this.selExAgencies = selExAgencies;
	}
	/**
	 * @return Returns the selImpAgencies.
	 */
	public Long[] getSelImpAgencies() {
		return selImpAgencies;
	}
	/**
	 * @param selImpAgencies The selImpAgencies to set.
	 */
	public void setSelImpAgencies(Long[] selImpAgencies) {
		this.selImpAgencies = selImpAgencies;
	}
	/**
	 * @return Returns the item.
	 */
	public int getItem() {
		return item;
	}
	/**
	 * @param item The item to set.
	 */
	public void setItem(int item) {
		this.item = item;
	}

	/**
	 * @return Returns the reportingOrgs.
	 */
	public Collection getReportingOrgs() {
		return reportingOrgs;
	}
	/**
	 * @param reportingOrgs The reportingOrgs to set.
	 */
	public void setReportingOrgs(Collection reportingOrgs) {
		this.reportingOrgs = reportingOrgs;
	}
	/**
	 * @return Returns the selReportingOrgs.
	 */
	public Long[] getSelReportingOrgs() {
		return selReportingOrgs;
	}
	/**
	 * @param selReportingOrgs The selReportingOrgs to set.
	 */
	public void setSelReportingOrgs(Long[] selReportingOrgs) {
		this.selReportingOrgs = selReportingOrgs;
	}
	/**
	 * @return Returns the selComp.
	 */
	public Long[] getSelComp() {
		return selComp;
	}
	/**
	 * @param selComp The selComp to set.
	 */
	public void setSelComp(Long[] selComp) {
		this.selComp = selComp;
	}
	/**
	 * @return Returns the selectedComponents.
	 */
	public Collection<Components<FundingDetail>> getSelectedComponents() {
		return selectedComponents;
	}
	/**
	 * @param selectedComponents The selectedComponents to set.
	 */
	public void setSelectedComponents(Collection<Components<FundingDetail>> selectedComponents) {
		this.selectedComponents = selectedComponents;
	}
	/**
	 * @return Returns the componentAmount.
	 */
	public String getComponentAmount() {
		return componentAmount;
	}
	/**
	 * @param componentAmount The componentAmount to set.
	 */
	public void setComponentAmount(String componentAmount) {
		this.componentAmount = componentAmount;
	}
	/**
	 * @return Returns the componentDesc.
	 */
	public String getComponentDesc() {
		return componentDesc;
	}
	/**
	 * @param componentDesc The componentDesc to set.
	 */
	public void setComponentDesc(String componentDesc) {
		this.componentDesc = componentDesc;
	}
	/**
	 * @return Returns the componentRepDate.
	 */
	public String getComponentRepDate() {
		return componentRepDate;
	}
	/**
	 * @param componentRepDate The componentRepDate to set.
	 */
	public void setComponentRepDate(String componentRepDate) {
		this.componentRepDate = componentRepDate;
	}
	/**
	 * @return Returns the componentReset.
	 */
	public boolean isComponentReset() {
		return componentReset;
	}
	/**
	 * @param componentReset The componentReset to set.
	 */
	public void setComponentReset(boolean componentReset) {
		this.componentReset = componentReset;
	}
	/**
	 * @return Returns the componentTitle.
	 */
	public String getComponentTitle() {
		return componentTitle;
	}
	/**
	 * @param componentTitle The componentTitle to set.
	 */
	public void setComponentTitle(String componentTitle) {
		this.componentTitle = componentTitle;
	}
	/**
	 * @return Returns the currencyCode.
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode The currencyCode to set.
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/**
	 * @return Returns the componentId.
	 */
	public Long getComponentId() {
		return componentId;
	}
	/**
	 * @param componentId The componentId to set.
	 */
	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}

	/**
	 * @return Returns the impMultiRegion.
	 */
	public Long[] getImpMultiRegion() {
		return impMultiRegion;
	}
	/**
	 * @param impMultiRegion The impMultiRegion to set.
	 */
	public void setImpMultiRegion(Long[] impMultiRegion) {
		this.impMultiRegion = impMultiRegion;
	}
	/**
	 * @return Returns the impMultiZone.
	 */
	public Long[] getImpMultiZone() {
		return impMultiZone;
	}
	/**
	 * @param impMultiZone The impMultiZone to set.
	 */
	public void setImpMultiZone(Long[] impMultiZone) {
		this.impMultiZone = impMultiZone;
	}
	/**
	 * @return Returns the impMultiWoreda.
	 */
	public Long[] getImpMultiWoreda() {
		return impMultiWoreda;
	}
	/**
	 * @param impMultiWoreda The impMultiWoreda to set.
	 */
	public void setImpMultiWoreda(Long[] impMultiWoreda) {
		this.impMultiWoreda = impMultiWoreda;
	}
	/**
	 * @return Returns the transIndexId.
	 */
	public long getTransIndexId() {
		return transIndexId;
	}
	/**
	 * @param transIndexId The transIndexId to set.
	 */
	public void setTransIndexId(long transIndexId) {
		this.transIndexId = transIndexId;
	}
	/**
	 * @return Returns the numComm.
	 */
	public int getNumComm() {
		return numComm;
	}
	/**
	 * @param numComm The numComm to set.
	 */
	public void setNumComm(int numComm) {
		this.numComm = numComm;
	}
	/**
	 * @return Returns the numDisb.
	 */
	public int getNumDisb() {
		return numDisb;
	}
	/**
	 * @param numDisb The numDisb to set.
	 */
	public void setNumDisb(int numDisb) {
		this.numDisb = numDisb;
	}
	/**
	 * @return Returns the numExp.
	 */
	public int getNumExp() {
		return numExp;
	}
	/**
	 * @param numExp The numExp to set.
	 */
	public void setNumExp(int numExp) {
		this.numExp = numExp;
	}

	/**
	 * @return Returns the actualCompletionDate.
	 */
	public String getActualCompletionDate() {
		return actualCompletionDate;
	}
	/**
	 * @param actualCompletionDate The actualCompletionDate to set.
	 */
	public void setActualCompletionDate(String actualCompletionDate) {
		this.actualCompletionDate = actualCompletionDate;
	}
	/**
	 * @return Returns the actualStartDate.
	 */
	public String getActualStartDate() {
		return actualStartDate;
	}
	/**
	 * @param actualStartDate The actualStartDate to set.
	 */
	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	/**
	 * @return Returns the fundingConditions.
	 */
	public String getFundingConditions() {
		return fundingConditions;
	}
	/**
	 * @param fundingConditions The fundingConditions to set.
	 */
	public void setFundingConditions(String fundingConditions) {
		this.fundingConditions = fundingConditions;
	}
	/**
	 * @return Returns the plannedCompletionDate.
	 */
	public String getPlannedCompletionDate() {
		return plannedCompletionDate;
	}
	/**
	 * @param plannedCompletionDate The plannedCompletionDate to set.
	 */
	public void setPlannedCompletionDate(String plannedCompletionDate) {
		this.plannedCompletionDate = plannedCompletionDate;
	}
	/**
	 * @return Returns the plannedStartDate.
	 */
	public String getPlannedStartDate() {
		return plannedStartDate;
	}
	/**
	 * @param plannedStartDate The plannedStartDate to set.
	 */
	public void setPlannedStartDate(String plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	/**
	 * @return Returns the reportingDate.
	 */
	public String getReportingDate() {
		return reportingDate;
	}
	/**
	 * @param reportingDate The reportingDate to set.
	 */
	public void setReportingDate(String reportingDate) {
		this.reportingDate = reportingDate;
	}
	/**
	 * @return Returns the activityCloseDates.
	 */
	public Collection getActivityCloseDates() {
		return activityCloseDates;
	}
	/**
	 * @param activityCloseDates The activityCloseDates to set.
	 */
	public void setActivityCloseDates(Collection activityCloseDates) {
		this.activityCloseDates = activityCloseDates;
	}
	/**
	 * @return Returns the currentCompDate.
	 */
	public String getCurrentCompDate() {
		return currentCompDate;
	}
	/**
	 * @param currentCompDate The currentCompDate to set.
	 */
	public void setCurrentCompDate(String currentCompDate) {
		this.currentCompDate = currentCompDate;
	}
	/**
	 * @return Returns the originalAppDate.
	 */
	public String getOriginalAppDate() {
		return originalAppDate;
	}
	/**
	 * @param originalAppDate The originalAppDate to set.
	 */
	public void setOriginalAppDate(String originalAppDate) {
		this.originalAppDate = originalAppDate;
	}
	/**
	 * @return Returns the originalStartDate.
	 */
	public String getOriginalStartDate() {
		return originalStartDate;
	}
	/**
	 * @param originalStartDate The originalStartDate to set.
	 */
	public void setOriginalStartDate(String originalStartDate) {
		this.originalStartDate = originalStartDate;
	}
	/**
	 * @return Returns the revisedAppDate.
	 */
	public String getRevisedAppDate() {
		return revisedAppDate;
	}
	/**
	 * @param revisedAppDate The revisedAppDate to set.
	 */
	public void setRevisedAppDate(String revisedAppDate) {
		this.revisedAppDate = revisedAppDate;
	}
	/**
	 * @return Returns the revisedStartDate.
	 */
	public String getRevisedStartDate() {
		return revisedStartDate;
	}
	/**
	 * @param revisedStartDate The revisedStartDate to set.
	 */
	public void setRevisedStartDate(String revisedStartDate) {
		this.revisedStartDate = revisedStartDate;
	}
	/**
	 * @return Returns the program.
	 */
	public Long getProgram() {
		return program;
	}
	/**
	 * @param program The program to set.
	 */
	public void setProgram(Long program) {
		this.program = program;
	}
	/**
	 * @return Returns the programCollection.
	 */
	public Collection getProgramCollection() {
		return programCollection;
	}
	/**
	 * @param programCollection The programCollection to set.
	 */
	public void setProgramCollection(Collection programCollection) {
		this.programCollection = programCollection;
	}
	/**
	 * @return Returns the contFirstName.
	 */
	public String getContFirstName() {
		return contFirstName;
	}
	/**
	 * @param contFirstName The contFirstName to set.
	 */
	public void setContFirstName(String contFirstName) {
		this.contFirstName = contFirstName;
	}
	/**
	 * @return Returns the contLastName.
	 */
	public String getContLastName() {
		return contLastName;
	}
	/**
	 * @param contLastName The contLastName to set.
	 */
	public void setContLastName(String contLastName) {
		this.contLastName = contLastName;
	}
	/**
	 * @return Returns the proposedCompDate.
	 */
	public String getProposedCompDate() {
		return proposedCompDate;
	}
	/**
	 * @param proposedCompDate The proposedCompDate to set.
	 */
	public void setProposedCompDate(String proposedCompDate) {
		this.proposedCompDate = proposedCompDate;
	}
	/**
	 * @return Returns the revisedCompDate.
	 */
	public String getRevisedCompDate() {
		return revisedCompDate;
	}
	/**
	 * @param revisedCompDate The revisedCompDate to set.
	 */
	public void setRevisedCompDate(String revisedCompDate) {
		this.revisedCompDate = revisedCompDate;
	}


	/**
	 * @return Returns the searchLocs.
	 */
	public Collection getSearchLocs() {
		return searchLocs;
	}
	/**
	 * @param searchLocs The searchLocs to set.
	 */
	public void setSearchLocs(Collection searchLocs) {
		this.searchLocs = searchLocs;
	}
	/**
	 * @param ampOrgTypeId The ampOrgTypeId to set.
	 */
	public void setAmpOrgTypeId(Long ampOrgTypeId) {
		this.ampOrgTypeId = ampOrgTypeId;
	}
	/**
	 * @return Returns the ampOrgTypeId.
	 */
	public Long getAmpOrgTypeId() {
		return ampOrgTypeId;
	}
	/**
	 * @return Returns the startAlphaFlag.
	 */
	public boolean getStartAlphaFlag() {
		return startAlphaFlag;
	}
	/**
	 * @param startAlphaFlag The startAlphaFlag to set.
	 */
	public void setStartAlphaFlag(boolean startAlphaFlag) {
		this.startAlphaFlag = startAlphaFlag;
	}
	/**
	 * @return Returns the orgTypes.
	 */
	public Collection getOrgTypes() {
		return orgTypes;
	}
	/**
	 * @param orgTypes The orgTypes to set.
	 */
	public void setOrgTypes(Collection orgTypes) {
		this.orgTypes = orgTypes;
	}

	/**
	 * @return Returns the searchedLocs.
	 */
	public Long[] getSearchedLocs() {
		return searchedLocs;
	}
	/**
	 * @param searchedLocs The searchedLocs to set.
	 */
	public void setSearchedLocs(Long[] searchedLocs) {
		this.searchedLocs = searchedLocs;
	}

	/**
	 * @return Returns the currentValDate.
	 */
	public String getCurrentValDate() {
		return currentValDate;
	}
	/**
	 * @param currentValDate The currentValDate to set.
	 */
	public void setCurrentValDate(String currentValDate) {
		this.currentValDate = currentValDate;
	}

	/**
	 * @return Returns the programDescription.
	 */
	public String getProgramDescription() {
		return programDescription;
	}
	/**
	 * @param programDescription The programDescription to set.
	 */
	public void setProgramDescription(String programDescription) {
		this.programDescription = programDescription;
	}

	/**
	 * @return Returns the field.
	 */
	public AmpField getField() {
		return field;
	}
	/**
	 * @param field The field to set.
	 */
	public void setField(AmpField field) {
		this.field = field;
	}
	/**
	 * @return Returns the fieldName.
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName The fieldName to set.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return Returns the commentText.
	 */
	public String getCommentText() {
		return commentText;
	}
	/**
	 * @param commentText The commentText to set.
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	/**
	 * @return Returns the commentsCol.
	 */
	public ArrayList getCommentsCol() {
		return commentsCol;
	}
	/**
	 * @param commentsCol The commentsCol to set.
	 */
	public void setCommentsCol(ArrayList commentsCol) {
		this.commentsCol = commentsCol;
	}
	/**
	 * @return Returns the actionFlag.
	 */
	public String getActionFlag() {
		return actionFlag;
	}
	/**
	 * @param actionFlag The actionFlag to set.
	 */
	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}
	/**
	 * @return Returns the ampCommentId.
	 */
	public Long getAmpCommentId() {
		return ampCommentId;
	}
	/**
	 * @param ampCommentId The ampCommentId to set.
	 */
	public void setAmpCommentId(Long ampCommentId) {
		this.ampCommentId = ampCommentId;
	}

	/**
	 * @return Returns the serializeFlag.
	 */
	public boolean isSerializeFlag() {
		return serializeFlag;
	}
	/**
	 * @param serializeFlag The serializeFlag to set.
	 */
	public void setSerializeFlag(boolean serializeFlag) {
		this.serializeFlag = serializeFlag;
	}
	/**
	 * @return Returns the commentFlag.
	 */
	public boolean isCommentFlag() {
		return commentFlag;
	}
	/**
	 * @param commentFlag The commentFlag to set.
	 */
	public void setCommentFlag(boolean commentFlag) {
		this.commentFlag = commentFlag;
	}

	/**
	 * @return Returns the author.
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author The author to set.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return Returns the actAthEmail.
	 */
	public String getActAthEmail() {
		return actAthEmail;
	}
	/**
	 * @param actAthEmail The actAthEmail to set.
	 */
	public void setActAthEmail(String actAthEmail) {
		this.actAthEmail = actAthEmail;
	}
	/**
	 * @return Returns the actAthFirstName.
	 */
	public String getActAthFirstName() {
		return actAthFirstName;
	}
	/**
	 * @param actAthFirstName The actAthFirstName to set.
	 */
	public void setActAthFirstName(String actAthFirstName) {
		this.actAthFirstName = actAthFirstName;
	}
	/**
	 * @return Returns the actAthLastName.
	 */
	public String getActAthLastName() {
		return actAthLastName;
	}
	/**
	 * @param actAthLastName The actAthLastName to set.
	 */
	public void setActAthLastName(String actAthLastName) {
		this.actAthLastName = actAthLastName;
	}
	/**
	 * @return Returns the dnrCntEmail.
	 */
	public String getDnrCntEmail() {
		return dnrCntEmail;
	}
	/**
	 * @param dnrCntEmail The dnrCntEmail to set.
	 */
	public void setDnrCntEmail(String dnrCntEmail) {
		this.dnrCntEmail = dnrCntEmail;
	}
	/**
	 * @return Returns the dnrCntFirstName.
	 */
	public String getDnrCntFirstName() {
		return dnrCntFirstName;
	}
	/**
	 * @param dnrCntFirstName The dnrCntFirstName to set.
	 */
	public void setDnrCntFirstName(String dnrCntFirstName) {
		this.dnrCntFirstName = dnrCntFirstName;
	}
	/**
	 * @return Returns the dnrCntLastName.
	 */
	public String getDnrCntLastName() {
		return dnrCntLastName;
	}
	/**
	 * @param dnrCntLastName The dnrCntLastName to set.
	 */
	public void setDnrCntLastName(String dnrCntLastName) {
		this.dnrCntLastName = dnrCntLastName;
	}
	/**
	 * @return Returns the mfdCntEmail.
	 */
	public String getMfdCntEmail() {
		return mfdCntEmail;
	}
	/**
	 * @param mfdCntEmail The mfdCntEmail to set.
	 */
	public void setMfdCntEmail(String mfdCntEmail) {
		this.mfdCntEmail = mfdCntEmail;
	}
	/**
	 * @return Returns the mfdCntFirstName.
	 */
	public String getMfdCntFirstName() {
		return mfdCntFirstName;
	}
	/**
	 * @param mfdCntFirstName The mfdCntFirstName to set.
	 */
	public void setMfdCntFirstName(String mfdCntFirstName) {
		this.mfdCntFirstName = mfdCntFirstName;
	}
	/**
	 * @return Returns the mfdCntLastName.
	 */
	public String getMfdCntLastName() {
		return mfdCntLastName;
	}
	/**
	 * @param mfdCntLastName The mfdCntLastName to set.
	 */
	public void setMfdCntLastName(String mfdCntLastName) {
		this.mfdCntLastName = mfdCntLastName;
	}
	/**
	 * @return Returns the createdDate.
	 */
	public String getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate The createdDate to set.
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

    /**
     * @return Returns the context.
     */
    public String getContext() {
        return context;
    }
    /**
     * @param context The context to set.
     */
    public void setContext(String context) {
        this.context = context;
    }

	/**
	 * @return Returns the regionalFundings.
	 */
	public Collection getRegionalFundings() {
		return regionalFundings;
	}

	/**
	 * @param regionalFundings The regionalFundings to set.
	 */
	public void setRegionalFundings(Collection regionalFundings) {
		this.regionalFundings = regionalFundings;
	}

	/**
	 * @return Returns the issues.
	 */
	public ArrayList getIssues() {
		return issues;
	}

	/**
	 * @param issues The issues to set.
	 */
	public void setIssues(ArrayList issues) {
		this.issues = issues;
	}

	/**
	 * @return Returns the selIssues.
	 */
	public Long[] getSelIssues() {
		return selIssues;
	}

	/**
	 * @param selIssues The selIssues to set.
	 */
	public void setSelIssues(Long[] selIssues) {
		this.selIssues = selIssues;
	}

	/**
	 * @return Returns the issue.
	 */
	public String getIssue() {
		return issue;
	}

	/**
	 * @param issue The issue to set.
	 */
	public void setIssue(String issue) {
		this.issue = issue;
	}

	/**
	 * @return Returns the measure.
	 */
	public String getMeasure() {
		return measure;
	}

	/**
	 * @param measure The measure to set.
	 */
	public void setMeasure(String measure) {
		this.measure = measure;
	}

	/**
	 * @return Returns the selMeasures.
	 */
	public Long[] getSelMeasures() {
		return selMeasures;
	}

	/**
	 * @param selMeasures The selMeasures to set.
	 */
	public void setSelMeasures(Long[] selMeasures) {
		this.selMeasures = selMeasures;
	}

	/**
	 * @return Returns the issueId.
	 */
	public Long getIssueId() {
		return issueId;
	}

	/**
	 * @param issueId The issueId to set.
	 */
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	/**
	 * @return Returns the measureId.
	 */
	public Long getMeasureId() {
		return measureId;
	}

	/**
	 * @param measureId The measureId to set.
	 */
	public void setMeasureId(Long measureId) {
		this.measureId = measureId;
	}

	/**
	 * @return Returns the actor.
	 */
	public String getActor() {
		return actor;
	}

	/**
	 * @param actor The actor to set.
	 */
	public void setActor(String actor) {
		this.actor = actor;
	}

	/**
	 * @return Returns the actorId.
	 */
	public Long getActorId() {
		return actorId;
	}

	/**
	 * @param actorId The actorId to set.
	 */
	public void setActorId(Long actorId) {
		this.actorId = actorId;
	}

	/**
	 * @return Returns the selActors.
	 */
	public Long[] getSelActors() {
		return selActors;
	}

	/**
	 * @param selActors The selActors to set.
	 */
	public void setSelActors(Long[] selActors) {
		this.selActors = selActors;
	}

	/**
	 * @return Returns the editKey.
	 */
	public String getEditKey() {
		return editKey;
	}

	/**
	 * @param editKey The editKey to set.
	 */
	public void setEditKey(String editKey) {
		this.editKey = editKey;
	}

	/**
	 * @return Returns the selRegFundings.
	 */
	public Long[] getSelRegFundings() {
		return selRegFundings;
	}

	/**
	 * @param selRegFundings The selRegFundings to set.
	 */
	public void setSelRegFundings(Long[] selRegFundings) {
		this.selRegFundings = selRegFundings;
	}


	/**
	 * @return Returns the fundingRegionId.
	 */
	public Long getFundingRegionId() {
		return fundingRegionId;
	}

	/**
	 * @param fundingRegionId The fundingRegionId to set.
	 */
	public void setFundingRegionId(Long fundingRegionId) {
		this.fundingRegionId = fundingRegionId;
	}

	/**
	 * @return Returns the fundingRegions.
	 */
	public Collection getFundingRegions() {
		return fundingRegions;
	}

	/**
	 * @param fundingRegions The fundingRegions to set.
	 */
	public void setFundingRegions(Collection fundingRegions) {
		this.fundingRegions = fundingRegions;
	}

	/**
	 * @return Returns the perspectives.
	 */
	public Collection getPerspectives() {
		return perspectives;
	}

	/**
	 * @param perspectives The perspectives to set.
	 */
	public void setPerspectives(Collection perspectives) {
		this.perspectives = perspectives;
	}

	/**
	 * @return Returns the totalCommitments.
	 */
	public double getTotalCommitments() {
		return Math.round(totalCommitments * 100) / ((double)100);
	}

	/**
	 * @param totalCommitments The totalCommitments to set.
	 */
	public void setTotalCommitments(double totalCommitments) {
		this.totalCommitments = totalCommitments;
	}

	/**
	 * @return Returns the totalDisbursements.
	 */
	public double getTotalDisbursements() {
		return Math.round(totalDisbursements * 100) / ((double)100);
	}

	/**
	 * @param totalDisbursements The totalDisbursements to set.
	 */
	public void setTotalDisbursements(double totalDisbursements) {
		this.totalDisbursements = totalDisbursements;
	}

	/**
	 * @return Returns the totalExpenditures.
	 */
	public double getTotalExpenditures() {
		return Math.round(totalExpenditures* 100) / ((double)100);
	}

	/**
	 * @param totalExpenditures The totalExpenditures to set.
	 */
	public void setTotalExpenditures(double totalExpenditures) {
		this.totalExpenditures = totalExpenditures;
	}
	/**
	 * @return Returns the approvalStatus.
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}
	/**
	 * @param approvalStatus The approvalStatus to set.
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	/**
	 * @return Returns the workingTeamLeadFlag.
	 */
	public String getWorkingTeamLeadFlag() {
		return workingTeamLeadFlag;
	}
	/**
	 * @param workingTeamLeadFlag The workingTeamLeadFlag to set.
	 */
	public void setWorkingTeamLeadFlag(String workingTeamLeadFlag) {
		this.workingTeamLeadFlag = workingTeamLeadFlag;
	}

	/**
	 * @return Returns the showInHomePage.
	 */
	public boolean isShowInHomePage() {
		return showInHomePage;
	}

	/**
	 * @param showInHomePage The showInHomePage to set.
	 */
	public void setShowInHomePage(boolean showInHomePage) {
		this.showInHomePage = showInHomePage;
	}

	/**
	 * @return Returns the donorFlag.
	 */
	public boolean isDonorFlag() {
		return donorFlag;
	}

	/**
	 * @param donorFlag The donorFlag to set.
	 */
	public void setDonorFlag(boolean donorFlag) {
		this.donorFlag = donorFlag;
	}

	/**
	 * @return Returns the fundDonor.
	 */
	public Long getFundDonor() {
		return fundDonor;
	}

	/**
	 * @param fundDonor The fundDonor to set.
	 */
	public void setFundDonor(Long fundDonor) {
		this.fundDonor = fundDonor;
	}

	/**
	 * @return Returns the fundingId.
	 */
	public Long getFundingId() {
		return fundingId;
	}

	/**
	 * @param fundingId The fundingId to set.
	 */
	public void setFundingId(Long fundingId) {
		this.fundingId = fundingId;
	}

	/**
	 * @return Returns the indicatorsME.
	 */
	public Collection getIndicatorsME() {
		return indicatorsME;
	}

	/**
	 * @param indicatorsME The indicatorsME to set.
	 */
	public void setIndicatorsME(Collection indicatorsME) {
		this.indicatorsME = indicatorsME;
	}

	/**
	 * @return Returns the indicatorId.
	 */
	public Long getIndicatorId() {
		return indicatorId;
	}

	/**
	 * @param indicatorId The indicatorId to set.
	 */
	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}

	/**
	 * @return Returns the indicatorValId.
	 */
	public Long getIndicatorValId() {
		return indicatorValId;
	}

	/**
	 * @param indicatorValId The indicatorValId to set.
	 */
	public void setIndicatorValId(Long indicatorValId) {
		this.indicatorValId = indicatorValId;
	}

	/**
	 * @return Returns the expIndicatorId.
	 */
	public Long getExpIndicatorId() {
		return expIndicatorId;
	}

	/**
	 * @param expIndicatorId The expIndicatorId to set.
	 */
	public void setExpIndicatorId(Long expIndicatorId) {
		this.expIndicatorId = expIndicatorId;
	}

	/**
	 * @return Returns the indicatorValues.
	 */
	public ActivityIndicator getIndicatorValues() {
		return indicatorValues;
	}

	/**
	 * @param indicatorValues The indicatorValues to set.
	 */
	public void setIndicatorValues(ActivityIndicator indicatorValues) {
		this.indicatorValues = indicatorValues;
	}

	/**
	 * @return Returns the baseVal.
	 */
	public float getBaseVal() {
		return baseVal;
	}

	/**
	 * @param baseVal The baseVal to set.
	 */
	public void setBaseVal(float baseVal) {
		this.baseVal = baseVal;
	}

	/**
	 * @return Returns the baseValDate.
	 */
	public String getBaseValDate() {
		return baseValDate;
	}

	/**
	 * @param baseValDate The baseValDate to set.
	 */
	public void setBaseValDate(String baseValDate) {
		this.baseValDate = baseValDate;
	}

	/**
	 * @return Returns the targetVal.
	 */
	public float getTargetVal() {
		return targetVal;
	}

	/**
	 * @param targetVal The targetVal to set.
	 */
	public void setTargetVal(float targetVal) {
		this.targetVal = targetVal;
	}

	/**
	 * @return Returns the targetValDate.
	 */
	public String getTargetValDate() {
		return targetValDate;
	}

	/**
	 * @param targetValDate The targetValDate to set.
	 */
	public void setTargetValDate(String targetValDate) {
		this.targetValDate = targetValDate;
	}


	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return Returns the currentVal.
	 */
	public float getCurrentVal() {
		return currentVal;
	}

	/**
	 * @param currentVal The currentVal to set.
	 */
	public void setCurrentVal(float currentVal) {
		this.currentVal = currentVal;
	}

	/**
	 * @return Returns the riskCollection.
	 */
	public Collection getRiskCollection() {
		return riskCollection;
	}

	/**
	 * @param riskCollection The riskCollection to set.
	 */
	public void setRiskCollection(Collection riskCollection) {
		this.riskCollection = riskCollection;
	}

	/**
	 * @return Returns the indicatorRisk.
	 */
	public Long getIndicatorRisk() {
		return indicatorRisk;
	}

	/**
	 * @param indicatorRisk The indicatorRisk to set.
	 */
	public void setIndicatorRisk(Long indicatorRisk) {
		this.indicatorRisk = indicatorRisk;
	}

	/**
	 * @return Returns the indHistory.
	 */
	public Collection getIndHistory() {
		return indHistory;
	}

	/**
	 * @param indHistory The indHistory to set.
	 */
	public void setIndHistory(Collection indHistory) {
		this.indHistory = indHistory;
	}

	/**
	 * @return Returns the updateIndValues.
	 */
	public Collection getUpdateIndValues() {
		return updateIndValues;
	}

	/**
	 * @param updateIndValues The updateIndValues to set.
	 */
	public void setUpdateIndValues(Collection updateIndValues) {
		this.updateIndValues = updateIndValues;
	}

	/**
	 * @return Returns the indicatorPriorValues.
	 */
	public Collection getIndicatorPriorValues() {
		return indicatorPriorValues;
	}

	/**
	 * @param indicatorPriorValues The indicatorPriorValues to set.
	 */
	public void setIndicatorPriorValues(Collection indicatorPriorValues) {
		this.indicatorPriorValues = indicatorPriorValues;
	}
		/**
	 * @return Returns the ampSurveyId.
	 */
	public Long getAmpSurveyId() {
		return ampSurveyId;
	}
	/**
	 * @param ampSurveyId The ampSurveyId to set.
	 */
	public void setAmpSurveyId(Long ampSurveyId) {
		this.ampSurveyId = ampSurveyId;
	}
	/**
	 * @return Returns the currPage.
	 */
	public Integer getCurrPage() {
		return currPage;
	}
	/**
	 * @param currPage The currPage to set.
	 */
	public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}
	/**
	 * @return Returns the fundingOrg.
	 */
	public String getFundingOrg() {
		return fundingOrg;
	}
	/**
	 * @param fundingOrg The fundingOrg to set.
	 */
	public void setFundingOrg(String fundingOrg) {
		this.fundingOrg = fundingOrg;
	}
	/**
	 * @return Returns the indicators.
	 */
	public List getIndicators() {
		return indicators;
	}
	/**
	 * @param indicators The indicators to set.
	 */
	public void setIndicators(List indicators) {
		this.indicators = indicators;
	}
	/**
	 * @return Returns the pageColl.
	 */
	public Collection getPageColl() {
		return pageColl;
	}
	/**
	 * @param pageColl The pageColl to set.
	 */
	public void setPageColl(Collection pageColl) {
		this.pageColl = pageColl;
	}
	/**
	 * @return Returns the startIndex.
	 */
	public Integer getStartIndex() {
		return startIndex;
	}
	/**
	 * @param startIndex The startIndex to set.
	 */
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	/**
	 * @return Returns the survey.
	 */
	public Collection getSurvey() {
		return survey;
	}
	/**
	 * @param survey The survey to set.
	 */
	public void setSurvey(Collection survey) {
		this.survey = survey;
	}
	/**
	 * @return Returns the surveyFlag.
	 */
	public Boolean getSurveyFlag() {
		return surveyFlag;
	}
	/**
	 * @param surveyFlag The surveyFlag to set.
	 */
	public void setSurveyFlag(Boolean surveyFlag) {
		this.surveyFlag = surveyFlag;
	}

	/**
	 * @return Returns the revTargetVal.
	 */
	public float getRevTargetVal() {
		return revTargetVal;
	}

	/**
	 * @param revTargetVal The revTargetVal to set.
	 */
	public void setRevTargetVal(float revTargetVal) {
		this.revTargetVal = revTargetVal;
	}

	/**
	 * @return Returns the revTargetValDate.
	 */
	public String getRevTargetValDate() {
		return revTargetValDate;
	}

	/**
	 * @param revTargetValDate The revTargetValDate to set.
	 */
	public void setRevTargetValDate(String revTargetValDate) {
		this.revTargetValDate = revTargetValDate;
	}

	/**
	 * @return Returns the currValDate.
	 */
	public String getCurrValDate() {
		return currValDate;
	}

	/**
	 * @param currValDate The currValDate to set.
	 */
	public void setCurrValDate(String currValDate) {
		this.currValDate = currValDate;
	}

	/**
	 * @return Returns the currCode.
	 */
	public String getCurrCode() {
		return currCode;
	}

    public String getDocumentSpace() {
        return documentSpace;
    }

    public Collection getManagedDocumentList() {
        return managedDocumentList;
    }

    public String[] getSelManagedDocs() {
        return selManagedDocs;
    }

    /**
	 * @param currCode The currCode to set.
	 */
	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

    public void setDocumentSpace(String documentSpace) {
        this.documentSpace = documentSpace;
    }

    public void setManagedDocumentList(Collection managedDocumentList) {
        this.managedDocumentList = managedDocumentList;
    }

    public void setSelManagedDocs(String[] selManagedDocs) {
        this.selManagedDocs = selManagedDocs;
    }

	/**
	 * @return Returns the baseValComments.
	 */
	public String getBaseValComments() {
		return baseValComments;
	}

	/**
	 * @param baseValComments The baseValComments to set.
	 */
	public void setBaseValComments(String baseValComments) {
		this.baseValComments = baseValComments;
	}

	/**
	 * @return Returns the currValComments.
	 */
	public String getCurrValComments() {
		return currValComments;
	}

	/**
	 * @param currValComments The currValComments to set.
	 */
	public void setCurrValComments(String currValComments) {
		this.currValComments = currValComments;
	}

	/**
	 * @return Returns the revTargetValComments.
	 */
	public String getRevTargetValComments() {
		return revTargetValComments;
	}

	/**
	 * @param revTargetValComments The revTargetValComments to set.
	 */
	public void setRevTargetValComments(String revTargetValComments) {
		this.revTargetValComments = revTargetValComments;
	}

	/**
	 * @return Returns the targetValComments.
	 */
	public String getTargetValComments() {
		return targetValComments;
	}

	/**
	 * @param targetValComments The targetValComments to set.
	 */
	public void setTargetValComments(String targetValComments) {
		this.targetValComments = targetValComments;
	}

	/**
	 * @return Returns the currentValComments.
	 */
	public String getCurrentValComments() {
		return currentValComments;
	}

    public ProposedProjCost getProProjCost() {
        return proProjCost;
    }

    /**
	 * @param currentValComments The currentValComments to set.
	 */
	public void setCurrentValComments(String currentValComments) {
		this.currentValComments = currentValComments;
	}

    public void setProProjCost(ProposedProjCost proProjCost) {
        this.proProjCost = proProjCost;
    }

	public Collection getActRankCollection() {
		return actRankCollection;
	}

	public void setActRankCollection(Collection actRankCollection) {
		this.actRankCollection = actRankCollection;
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

	public Collection getAllComps() {
		return allComps;
	}

	public void setAllComps(Collection allComps) {
		this.allComps = allComps;
	}

	public double getCompTotalDisb() {
		return compTotalDisb;
	}

	public void setCompTotalDisb(double compTotalDisb) {
		this.compTotalDisb = compTotalDisb;
	}

	public double getRegionTotalDisb() {
		return regionTotalDisb;
	}

    public Long[] getSelectedPrograms() {
        return selectedPrograms;
    }


    public Long[] getSelPrograms() {
        return selPrograms;
    }

    public List getProgramLevels() {
        return programLevels;
    }

    public List getActPrograms() {
        return actPrograms;
    }

    public Long getSelProgramId() {
        return selProgramId;
    }

    public void setSelectedPrograms(Long[] selectedPrograms) {
		this.selectedPrograms = selectedPrograms;
	}

	public void setRegionTotalDisb(double regionTotalDisb) {
		this.regionTotalDisb = regionTotalDisb;
	}

    public void setSelPrograms(Long[] selPrograms) {
        this.selPrograms = selPrograms;
    }

    public void setProgramLevels(List programLevels) {
        this.programLevels = programLevels;
    }

    public void setActPrograms(List actPrograms) {
        this.actPrograms = actPrograms;
    }

    public void setSelProgramId(Long selProgramId) {
        this.selProgramId = selProgramId;
    }

	public Boolean getBudget() {
		return budget;
	}

	public void setBudget(Boolean budget) {
		this.budget = budget;
	}

	public AmpTeam getTeam() {
		return team;
	}

	public void setTeam(AmpTeam team) {
		this.team = team;
	}

	public AmpTeamMember getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(AmpTeamMember updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getActAthAgencySource() {
		return actAthAgencySource;
	}

	public void setActAthAgencySource(String actAthAgencySource) {
		this.actAthAgencySource = actAthAgencySource;
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


	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}


	public List getCosts() {
		return costs;
	}

	public void setCosts(List euActivities) {
		this.costs = euActivities;
	}

	public Long getLogframeCategory() {
		return logframeCategory;
	}

	public void setLogframeCategory(Long logframeCategory) {
		this.logframeCategory = logframeCategory;
	}

	public Long getAccessionInstrument() {
		return accessionInstrument;
	}

	public void setAccessionInstrument(Long accessionInstrument) {
		this.accessionInstrument = accessionInstrument;
	}

	public Double getOverallContribution() {
		return overallContribution;
	}

	public void setOverallContribution(Double overallContribution) {
		this.overallContribution = overallContribution;
	}

	public Double getOverallCost() {
		return overallCost;
	}

	public void setOverallCost(Double overallCost) {
		this.overallCost = overallCost;
	}


	public Collection getBenAgencies() {
		return benAgencies;
	}

	public void setBenAgencies(Collection benAgencies) {
		this.benAgencies = benAgencies;
	}

	public Long[] getSelBenAgencies() {
		return selBenAgencies;
	}

	public void setSelBenAgencies(Long[] selBenAgencies) {
		this.selBenAgencies = selBenAgencies;
	}

	public Collection getConAgencies() {
		return conAgencies;
	}

	public void setConAgencies(Collection conAgencies) {
		this.conAgencies = conAgencies;
	}

	public Long[] getSelConAgencies() {
		return selConAgencies;
	}

	public void setSelConAgencies(Long[] selConAgencies) {
		this.selConAgencies = selConAgencies;
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

	public Collection getDocuments() {
		return documents;
	}

	public void setDocuments(Collection documents) {
		this.documents = documents;
	}

	public HashMap getAllComments() {
		return allComments;
	}

	public void setAllComments(HashMap allComments) {
		this.allComments = allComments;
	}

	public float getRevisedTargetVal() {
		return revisedTargetVal;
	}

	public void setRevisedTargetVal(float revisedTargetVal) {
		this.revisedTargetVal = revisedTargetVal;
	}

	public Integer getSelectedOrganisationFromPages() {
		return selectedOrganisationFromPages;
	}

	public void setSelectedOrganisationFromPages(
			Integer selectedOrganisationFromPages) {
		this.selectedOrganisationFromPages = selectedOrganisationFromPages;
	}

	public TreeSet getSelectedOrganisationPaged() {
		return selectedOrganisationPaged;
	}

	public void setSelectedOrganisationPaged(TreeSet selectedOrganisationPaged) {
		this.selectedOrganisationPaged = selectedOrganisationPaged;
	}

	public String getDnrCntFaxNumber() {
		return dnrCntFaxNumber;
	}

	public void setDnrCntFaxNumber(String dnrCntFaxNumber) {
		this.dnrCntFaxNumber = dnrCntFaxNumber;
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

	public String getDnrCntTitle() {
		return dnrCntTitle;
	}

	public void setDnrCntTitle(String dnrCntTitle) {
		this.dnrCntTitle = dnrCntTitle;
	}

	public String getMfdCntFaxNumber() {
		return mfdCntFaxNumber;
	}

	public void setMfdCntFaxNumber(String mfdCntFaxNumber) {
		this.mfdCntFaxNumber = mfdCntFaxNumber;
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

	public String getMfdCntTitle() {
		return mfdCntTitle;
	}

	public void setMfdCntTitle(String mfdCntTitle) {
		this.mfdCntTitle = mfdCntTitle;
	}

	public int getNumResults() {
		return numResults;
	}

	public void setNumResults(int numResults) {
		this.numResults = numResults;
	}

	public Long[] getSelActivitySectors() {
		return selActivitySectors;
	}

	public void setSelActivitySectors(Long[] selActivitySectors) {
		this.selActivitySectors = selActivitySectors;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Collection getFinancingBreakdown() {
		return financingBreakdown;
	}

	public void setFinancingBreakdown(Collection financingBreakdown) {
		this.financingBreakdown = financingBreakdown;
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

	public String getTotalExpenditure() {
		return totalExpenditure;
	}

	public void setTotalExpenditure(String totalExpenditure) {
		this.totalExpenditure = totalExpenditure;
	}

	public String getTotalUnDisbursed() {
		return totalUnDisbursed;
	}

	public void setTotalUnDisbursed(String totalUnDisbursed) {
		this.totalUnDisbursed = totalUnDisbursed;
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

	public String getVisibleProgram() {
		return visibleProgram;
	}

	public void setVisibleProgram(String visibleProgram) {
		this.visibleProgram = visibleProgram;
	}

	public Long getImplemLocationLevel() {
		return implemLocationLevel;
	}

	public void setImplemLocationLevel(Long implemLocationLevel) {
		this.implemLocationLevel = implemLocationLevel;
	}

	public String getFY() {
		return FY;
	}

	public void setFY(String fy) {
		FY = fy;
	}

	public Long getGbsSbs() {
		return gbsSbs;
	}

	public void setGbsSbs(Long gbsSbs) {
		this.gbsSbs = gbsSbs;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getSubProgram() {
		return subProgram;
	}

	public void setSubProgram(String subProgram) {
		this.subProgram = subProgram;
	}

	public String getSubVote() {
		return subVote;
	}

	public void setSubVote(String subVote) {
		this.subVote = subVote;
	}

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public String getBudgetCheckbox() {
		return budgetCheckbox;
	}

  public AmpTeamMember getCreatedBy() {
    return createdBy;
  }

  public void setBudgetCheckbox(String budgetCheckbox) {
		this.budgetCheckbox = budgetCheckbox;
	}

	
	public void setCreatedBy(AmpTeamMember createdBy) {
    this.createdBy = createdBy;
  }

	public Collection getFundingOrganizations() {
		return fundingOrganizations;
	}
	
	public int getIsPreview() {
    return isPreview;
  }
  
   public void setIsPreview(int isPreview) {
    this.isPreview = isPreview;
  }
	
	
	public String getMultiSectorSelecting() {
	return multiSectorSelecting;
}

	public void setMultiSectorSelecting(String multiSectorSelecting) {
		this.multiSectorSelecting = multiSectorSelecting;
	}

	public String getLessonsLearned() {
		return lessonsLearned;
	}

	public void setLessonsLearned(String lessonsLearned) {
		this.lessonsLearned = lessonsLearned;
	}

	public List<MTEFProjection> getFundingMTEFProjections() {
		return fundingMTEFProjections;
	}
	
	public MTEFProjection getMtefProjection(int index) {
		while ( fundingMTEFProjections.size() <= index ) {
			fundingMTEFProjections.add( new MTEFProjection() );
		}
		return fundingMTEFProjections.get(index);
	}
	
	public Double getAllCosts() {
		return allCosts;
	}

	public void setAllCosts(Double allCosts) {
		this.allCosts = allCosts;
	}
	public void setFundingMTEFProjections(List<MTEFProjection> fundingMTEFProjections) {
		this.fundingMTEFProjections = fundingMTEFProjections;
	}

	public Collection getProjections() {
		return projections;
	}

	public void setProjections(Collection projections) {
		this.projections = projections;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getEqualOpportunity() {
		return equalOpportunity;
	}
	
	public void setEqualOpportunity(String equalOpportunity) {
		this.equalOpportunity = equalOpportunity;
	}

	public String getMinorities() {
		return minorities;
	}

	public void setMinorities(String minorities) {
		this.minorities = minorities;
	}

	public List getReferenceDocs() {
		return referenceDocs;
	}

	public ReferenceDoc getReferenceDoc(int index) {
		return (ReferenceDoc)referenceDocs.get(index);
	}

	public void setReferenceDocs(List referenceDocs) {
		this.referenceDocs = referenceDocs;
	}

	public void setReferenceDoc(int index,ReferenceDoc doc) {
		this.referenceDocs = referenceDocs;
	}

	public Long[] getAllReferenceDocNameIds() {
		return allReferenceDocNameIds;
	}

	public boolean getDefaultCountryIsSet() {
    	return defaultCountryIsSet;
    }

	public void setAllReferenceDocNameIds(Long[] selectedReferenceDocs) {
		this.allReferenceDocNameIds = selectedReferenceDocs;
	}
	
	public void setDefaultCountryIsSet(boolean defaultIsCountrySet) {
    	this.defaultCountryIsSet = defaultIsCountrySet;
  	}

//	public String[] getRefDocComments() {
//		return refDocComments;
//	}
//
//	public String getRefDocComment(int index) {
//		return (refDocComments==null || refDocComments.length<=index)?"":refDocComments[index];
//	}
//
//	public void setRefDocComments(String[] refDocComments) {
//		this.refDocComments = refDocComments;
//	}
//
//	public void setRefDocComment(int index, String comment) {
//		if (refDocComments==null){
//			refDocComments=new String[index+1];
//		}
//		if (refDocComments.length<=index){
//			String[] newArray=new String[index+1];
//			System.arraycopy(refDocComments, 0, newArray, 0, refDocComments.length);
//			refDocComments=newArray;
//		}
//		refDocComments[index]=comment;
//	}





	public int getNumProjections() {
		return numProjections;
	}

	public void setNumProjections(int numProjections) {
		this.numProjections = numProjections;
	}

	public String getDocComment() {
		return docComment;
	}

	public void setDocComment(String docComment) {
		this.docComment = docComment;
	}
	
	


}


