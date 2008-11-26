/**
 * @author Priyajith
 * @version 0.1
 */

package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.CustomField;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.util.Step;
import org.digijava.module.contentrepository.helper.DocumentData;

public class EditActivityForm extends ActionForm implements Serializable {

	private HashMap<String, String> errors = new HashMap<String, String>();
	private HashMap<String, String> messages = new HashMap<String, String>();
	//used by old add org selector
	private int item;
	
	//identification
	private Long activityId = null;
	private String ampId = null;
	private Boolean budget = true;
	private AmpTeam team;
	
	//regional funding
	private double regionTotalDisb;

	// Staus
	private String status = null;
	private AmpTeamMember createdBy;
	private AmpTeamMember updatedBy;
	private AmpTeamMember approvedBy;
	private String createdDate;
	private String updatedDate;
	private Date approvalDate;
	private Boolean draft;
	
	//Flags
	private boolean govFlag;
	private boolean teamLead;
	private String svAction;
	private int isPreview = 0;
	
	//survey
	private Long surveyOrgId;
	private Long activityLevel;

	private Double overallCost = null;
	private Double overallContribution = null;
	
	//related documents
	private String documentSpace = null;
	private String condition = null;
	
	private Collection financingBreakdown = null;
	

	// montenegro mission:
	private Double ipaBudget = null; // EU total contribution
	private String contractDetails = null;
	
	//POPUP ADD PROGRAM
	private Long selPrograms[];
	private Long selProgramId;
	private Long program;
	private List actPrograms;
	private Collection programCollection;
	private Long selectedPrograms[];
	
	//LOG FRAME
	private Double allCosts;
	

	//PLANNING
	private String revisedCompDate;
	private Collection activityCloseDates;

	
	private String programDescription;
	private boolean sectorReset;

	//references
	private List referenceDocs;
	private Long[] allReferenceDocNameIds;

	//costing
	private List costs;
	// to check if the current memeber is Team lead

	public boolean isTeamLead() {
		return teamLead;
	}

	public void setTeamLead(boolean teamLead) {
		this.teamLead = teamLead;
	}

	// location selector pop-up
	private Long selLocs[] = null; // location selected from step 2 page to
	// remove from the locations list
	// list of locations to be added to
	// the activity
	private Collection searchLocs = null; // list of searched locations.
	private Long searchedLocs[] = null; // locations selected by user to be
	// added in activity after searching

	private Long selOrgs[] = null;
	private List selectedOrgs = null; // list of organisations to be added to
	// the activity
	private Collection levelCollection = null;

	private String step = null;
	private String visibleProgram = null;
	private Long[] selCompSectors;
	private Collection cols = null;
	private Collection colsAlpha = null;
	private Collection orgTypes = null;
	private boolean reset;
	private boolean orgPopupReset;
	// private String implementationLevel = null;
	private Collection selectedPhysicalProgress;
	private Long selPhyProg[];
	private Collection documentList;
	private Collection documents;
	private Collection<DocumentData> crDocuments;
	private Collection managedDocumentList;
	private long selDocs[];
	private Collection linksList;
	private long selLinks[];
	private String selManagedDocs[];
	
	
	private String contFirstName;
	private String contLastName;
	private String email;

	// contact information
	private String actAthFirstName;
	private String actAthLastName;
	private String actAthEmail;
	private String actAthAgencySource;
	private String conditions;

	// private Collection selectedComponents;
	// private Long selComp[];

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
	private Long prevOrg;
	private TreeSet selectedOrganisationPaged;
	private Integer selectedOrganisationFromPages;

	// pop-up organisation selector window
	private Collection pages;
	private String[] alphaPages;
	private int numResults;

	// For view comment popup
	private String actionFlag = null;
	private boolean serializeFlag;
	private boolean commentFlag;
	private String commentText = null;
	private Long ampCommentId;
	private String fieldName;
	private AmpField field = null;

	private int pagesToShow;
	private int pagesSize;
	private int startPage;

	private ArrayList commentsCol = new ArrayList();
	private HashMap allComments = new HashMap();

	// For activity approval process
	private String approvalStatus;
	private String workingTeamLeadFlag;


	private Long fundingId;
	private Long orgId;
	
	private Collection currencies;
	
	private Long selSectors[] = null; // sectors selected by user to be added
	// in activity after searching

	private int offset;
	private long transIndexId;
	private Long selFundingOrgs[];

	private List programLevels;
	
	/* END FINANCIAL EDIT */

	private boolean orgSelReset;
	// FOR SELECT LOCATION POPUP
	private String fill; // which among countries,region,zone and woreda need
	// to
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

	// FOR  SECTOR POPUP
	private Long sector;
	private Long subsectorLevel1;
	private Long subsectorLevel2;
	private Long sectorScheme;
	private Collection sectorSchemes;
	private Collection parentSectors;
	private Collection childSectorsLevel1;
	private Collection childSectorsLevel2;
	private Long selActivitySectors[];
	private Collection orderedFundingOrganizations;

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
	
	private Long docType;
	private Long docLang;
	private String docComment;

	// FOR ADD COMPONENTS
	private ArrayList<AmpComponentType> allCompsType;
	private Long selectedType;
	private String newCompoenentName;
	private Collection allComps;
	private boolean componentReset;
	private String componentTitle;
	private String componentDesc;
	private String componentAmount;
	// //////////////////

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
	private String issueDate;
	private Long[] selMeasures;
	private String measure;
	private Long issueId;
	private Long measureId;
	private Long[] selActors;
	private String actor;
	private Long actorId;

	private String editKey;

	private String currCode;

	private Long fundDonor;



	/* Categories */
	private Long acChapter = new Long(0);

	private Collection searchedSectors = null; // list of searched Sectors.

	/*
	 * Tanzania ADDS
	 */
	private String FY;
	private String vote;
	private String subVote;
	private String subProgram;
	private String projectCode;
	private Long gbsSbs;

	private boolean defaultCountryIsSet;
	// program settings

	private int programType;
	private Long[] selectedNPOPrograms;
	private Long[] selectedPPrograms;
	private Long[] selectedSPrograms;

	
	//*** Recovery Save
	private String stepText[];
	private Boolean stepFailure[];
	private String stepFailureText[];
	
	private List contracts;
	private Integer selContractId;
	private Long creditTypeId;
	private String convenioNumcont;
	private String clasiNPD;

	private List steps;

	private Boolean wasDraft;
	
	private List<CustomField> customFields;

	public Boolean getWasDraft() {
		return wasDraft;
	}

	public void setWasDraft(Boolean wasDraft) {
		this.wasDraft = wasDraft;
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

	public Integer getSelContractId() {
		return selContractId;
	}

	public void setSelContractId(Integer selContractId) {
		this.selContractId = selContractId;
	}

	public List getContracts() {
		return contracts;
	}

	public void setContracts(List contracts) {
		this.contracts = contracts;
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
		tempNumResults = 0;
		pageId = 0;
		docWebResource = "http://";
		editKey = "";
		fundingRegionId = new Long(-1);
		keyword = null;
		
    	List<CustomField> customFields = new ArrayList<CustomField>(); 
    	CustomField cf = new CustomField();
    	cf.setStep(1);
    	cf.setName("Field1");
    	cf.setDescription("Description");
    	cf.setAmpActivityPropertyName("customField1");
    	customFields.add(cf);
    	this.setCustomFields(customFields);	
	}


	public void reset(ActionMapping mapping, HttpServletRequest request) {
	if (reset) {
			this.identification=null;
			this.planning=null;
			this.location=null;
			this.sectors=null;
			this.components=null;
			this.program=null;
			this.getPrograms().setNationalPlanObjectivePrograms(null);
			this.getPrograms().setPrimaryPrograms(null);
			this.getPrograms().setSecondaryPrograms(null);
			this.crossIssues=null;
			this.funding=null;
			this.survey=null;
			this.contactInfo=null;
			this.agencies=null;
			this.indicatorME=null;
			
			contractDetails = null;
			fundDonor = null;
			createdDate = null;
			activityId = null;
			ampId = null;
			documentSpace = null;
			condition = null;
			status = null;
			program = null;
			selLocs = null;
			selOrgs = null;
			selectedOrgs = null;
			levelCollection = null;
			programCollection = null;
			step = "1";
			selSectors = null;
			cols = null;
			colsAlpha = null;
			orgTypes = null;
			selectedPhysicalProgress = null;
			componentDesc = null;
			selPhyProg = null;
			documentList = null;
			managedDocumentList = null;
			selDocs = null;
			linksList = null;
			selLinks = null;
			selManagedDocs = null;
			pages = null;
			alphaPages = null;
			fundingId = null;
			orgId = null;
			currencies = null;
			offset = 0;
			selFundingOrgs = null;
			reset = false;
			editAct = false;
			currentPage = new Integer(0);
			currentAlpha = null;
			contFirstName = null;
			contLastName = null;
			email = null;
			conditions = null;
			selectedComponents = null;
			componentId = null;
			selComp = null;
			programDescription = null;
			revisedCompDate = null;
			activityCloseDates = null;
			author = null;
			actAthEmail = null;
			actAthFirstName = null;
			actAthAgencySource = null;
			actAthLastName = null;
			regionalFundings = null;
			issues = null;
			editKey = "";
			regionalFundings = null;
			costs = null;
			docType = null;
			docLang = null;
			documents = null;
			overallCost = null;
			overallContribution = null;
			allComments = null;
			financingBreakdown = null;
			visibleProgram = null;
			fundingRegions = null;
			allReferenceDocNameIds = null;
			referenceDocs = null;
			budget = false;
			acChapter = new Long(0);
			selectedSPrograms = null;
			selectedPPrograms = null;
			selectedNPOPrograms = null;
			contracts = null;
			selContractId = null;
			steps = null;
			FY = null;
			vote = null;
			subVote = null;
			subProgram = null;
			projectCode = null;
			gbsSbs = null;	
			
	        Iterator<CustomField> itcf = this.customFields.iterator();
	        while(itcf.hasNext()){
	        	CustomField cf = itcf.next();
        		cf.setValue(null);
	        }
	        
		}

		if (orgSelReset) {
			pagedCol = null;
			selOrganisations = null;
			keyword = null;
			setOrgType("");
			setAmpOrgTypeId(null);
		}

		
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
			docComment = null;
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
		if (request.getParameter("budgetCheckbox") != null) {
			budget = false;
		}
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
		if (pages != null) {
			this.pagesSize = pages.size();
		}
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
	 * @param activityId
	 *            The activityId to set.
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
	 * @param edit
	 *            The editAct to set.
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
	 * @param country
	 *            The country to set.
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
	 * @param currentPage
	 *            The currentPage to set.
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
	 * @param currentAlpha
	 *            The currentAlpha to set.
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
	 * @param phyProgId
	 *            The phyProgId to set.
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
	 * @param conditions
	 *            The conditions to set.
	 */
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Returns the selComp.
	 */
	public Long[] getSelComp() {
		return selComp;
	}

	/**
	 * @param selComp
	 *            The selComp to set.
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
	 * @param selectedComponents
	 *            The selectedComponents to set.
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
	 * @param componentAmount
	 *            The componentAmount to set.
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
	 * @param componentDesc
	 *            The componentDesc to set.
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
	 * @param componentRepDate
	 *            The componentRepDate to set.
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
	 * @param componentReset
	 *            The componentReset to set.
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
	 * @param componentTitle
	 *            The componentTitle to set.
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
	 * @param currencyCode
	 *            The currencyCode to set.
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
	 * @param componentId
	 *            The componentId to set.
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
	 * @param impMultiRegion
	 *            The impMultiRegion to set.
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
	 * @param impMultiZone
	 *            The impMultiZone to set.
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
	 * @param impMultiWoreda
	 *            The impMultiWoreda to set.
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
	 * @param transIndexId
	 *            The transIndexId to set.
	 */
	public void setTransIndexId(long transIndexId) {
		this.transIndexId = transIndexId;
	}

	
	/**
	 * @return Returns the activityCloseDates.
	 */
	public Collection getActivityCloseDates() {
		return activityCloseDates;
	}

	/**
	 * @param activityCloseDates
	 *            The activityCloseDates to set.
	 */
	public void setActivityCloseDates(Collection activityCloseDates) {
		this.activityCloseDates = activityCloseDates;
	}

	/**
	 * @return Returns the program.
	 */
	public Long getProgram() {
		return program;
	}

	/**
	 * @param program
	 *            The program to set.
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
	 * @param programCollection
	 *            The programCollection to set.
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
	 * @param contFirstName
	 *            The contFirstName to set.
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
	 * @param contLastName
	 *            The contLastName to set.
	 */
	public void setContLastName(String contLastName) {
		this.contLastName = contLastName;
	}

	/**
	 * @return Returns the revisedCompDate.
	 */
	public String getRevisedCompDate() {
		return revisedCompDate;
	}

	/**
	 * @param revisedCompDate
	 *            The revisedCompDate to set.
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
	 * @param searchLocs
	 *            The searchLocs to set.
	 */
	public void setSearchLocs(Collection searchLocs) {
		this.searchLocs = searchLocs;
	}

	/**
	 * @param ampOrgTypeId
	 *            The ampOrgTypeId to set.
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
	 * @param startAlphaFlag
	 *            The startAlphaFlag to set.
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
	 * @param orgTypes
	 *            The orgTypes to set.
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
	 * @param searchedLocs
	 *            The searchedLocs to set.
	 */
	public void setSearchedLocs(Long[] searchedLocs) {
		this.searchedLocs = searchedLocs;
	}

	/**
	 * @return Returns the programDescription.
	 */
	public String getProgramDescription() {
		return programDescription;
	}

	/**
	 * @param programDescription
	 *            The programDescription to set.
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
	 * @param field
	 *            The field to set.
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
	 * @param fieldName
	 *            The fieldName to set.
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
	 * @param commentText
	 *            The commentText to set.
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
	 * @param commentsCol
	 *            The commentsCol to set.
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
	 * @param actionFlag
	 *            The actionFlag to set.
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
	 * @param ampCommentId
	 *            The ampCommentId to set.
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
	 * @param serializeFlag
	 *            The serializeFlag to set.
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
	 * @param commentFlag
	 *            The commentFlag to set.
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
	 * @param author
	 *            The author to set.
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
	 * @param actAthEmail
	 *            The actAthEmail to set.
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
	 * @param actAthFirstName
	 *            The actAthFirstName to set.
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
	 * @param actAthLastName
	 *            The actAthLastName to set.
	 */
	public void setActAthLastName(String actAthLastName) {
		this.actAthLastName = actAthLastName;
	}

	
	/**
	 * @return Returns the createdDate.
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            The createdDate to set.
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
	 * @param context
	 *            The context to set.
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
	 * @param regionalFundings
	 *            The regionalFundings to set.
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
	 * @param issues
	 *            The issues to set.
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
	 * @param selIssues
	 *            The selIssues to set.
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
	 * @param issue
	 *            The issue to set.
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
	 * @param measure
	 *            The measure to set.
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
	 * @param selMeasures
	 *            The selMeasures to set.
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
	 * @param issueId
	 *            The issueId to set.
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
	 * @param measureId
	 *            The measureId to set.
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
	 * @param actor
	 *            The actor to set.
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
	 * @param actorId
	 *            The actorId to set.
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
	 * @param selActors
	 *            The selActors to set.
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
	 * @param editKey
	 *            The editKey to set.
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
	 * @param selRegFundings
	 *            The selRegFundings to set.
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
	 * @param fundingRegionId
	 *            The fundingRegionId to set.
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
	 * @param fundingRegions
	 *            The fundingRegions to set.
	 */
	public void setFundingRegions(Collection fundingRegions) {
		this.fundingRegions = fundingRegions;
	}

	/**
	 * @return Returns the approvalStatus.
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}

	/**
	 * @param approvalStatus
	 *            The approvalStatus to set.
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
	 * @param workingTeamLeadFlag
	 *            The workingTeamLeadFlag to set.
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
	 * @param showInHomePage
	 *            The showInHomePage to set.
	 */
	public void setShowInHomePage(boolean showInHomePage) {
		this.showInHomePage = showInHomePage;
	}

	/**
	 * @return Returns the fundDonor.
	 */
	public Long getFundDonor() {
		return fundDonor;
	}

	/**
	 * @param fundDonor
	 *            The fundDonor to set.
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
	 * @param fundingId
	 *            The fundingId to set.
	 */
	public void setFundingId(Long fundingId) {
		this.fundingId = fundingId;
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
	 * @param currCode
	 *            The currCode to set.
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

	//
	public void setSelectedNPOPrograms(Long[] selectedNPOPrograms) {
		this.selectedNPOPrograms = selectedNPOPrograms;
	}

	public void setSelectedPPrograms(Long[] selectedPPrograms) {
		this.selectedPPrograms = selectedPPrograms;
	}

	public void setSelectedSPrograms(Long[] selectedSPrograms) {
		this.selectedSPrograms = selectedSPrograms;
	}

	public void setProgramType(int programType) {
		this.programType = programType;
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

	public Integer getSelectedOrganisationFromPages() {
		return selectedOrganisationFromPages;
	}

	public void setSelectedOrganisationFromPages(Integer selectedOrganisationFromPages) {
		this.selectedOrganisationFromPages = selectedOrganisationFromPages;
	}

	public TreeSet getSelectedOrganisationPaged() {
		return selectedOrganisationPaged;
	}

	public void setSelectedOrganisationPaged(TreeSet selectedOrganisationPaged) {
		this.selectedOrganisationPaged = selectedOrganisationPaged;
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

	public String getVisibleProgram() {
		return visibleProgram;
	}

	public void setVisibleProgram(String visibleProgram) {
		this.visibleProgram = visibleProgram;
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



	public AmpTeamMember getCreatedBy() {
		return createdBy;
	}

	

	public void setCreatedBy(AmpTeamMember createdBy) {
		this.createdBy = createdBy;
	}

	public int getIsPreview() {
		return isPreview;
	}

	public void setIsPreview(int isPreview) {
		this.isPreview = isPreview;
	}



	public Double getAllCosts() {
		return allCosts;
	}

	public void setAllCosts(Double allCosts) {
		this.allCosts = allCosts;
	}



	public List getReferenceDocs() {
		return referenceDocs;
	}

	public ReferenceDoc getReferenceDoc(int index) {
		return (ReferenceDoc) referenceDocs.get(index);
	}

	public void setReferenceDocs(List referenceDocs) {
		this.referenceDocs = referenceDocs;
	}

	public void setReferenceDoc(int index, ReferenceDoc doc) {
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

	public String getDocComment() {
		return docComment;
	}

	public void setDocComment(String docComment) {
		this.docComment = docComment;
	}

	public void setDraft(Boolean draft) {
		this.draft = draft;
	}

	public Long getActivityLevel() {
		return activityLevel;
	}

	public void setActivityLevel(Long activityLevel) {
		this.activityLevel = activityLevel;
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

	public Collection getOrderedFundingOrganizations() {
		return orderedFundingOrganizations;
	}

	public void setOrderedFundingOrganizations(Collection orderedFundingOrganizations) {
		this.orderedFundingOrganizations = orderedFundingOrganizations;
	}

	public Collection getParentSectors() {
		return parentSectors;
	}

	public void setParentSectors(Collection parentSectors) {
		this.parentSectors = parentSectors;
	}


	public Long getSector() {
		return sector;
	}

	public void setSector(Long sector) {
		this.sector = sector;
	}

	public boolean isSectorReset() {
		return sectorReset;
	}

	public void setSectorReset(boolean sectorReset) {
		this.sectorReset = sectorReset;
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

	public Long[] getSelCompSectors() {
		return selCompSectors;
	}

	public void setSelCompSectors(Long[] selCompSectors) {
		this.selCompSectors = selCompSectors;
	}

	
	public Long[] getSelSectors() {
		return selSectors;
	}

	public void setSelSectors(Long[] selSectors) {
		this.selSectors = selSectors;
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



	public Boolean getDraft() {
		return draft;
	}

	public int getProgramType() {
		return programType;
	}

	public Long[] getSelectedNPOPrograms() {
		return selectedNPOPrograms;
	}

	public Long[] getSelectedPPrograms() {
		return selectedPPrograms;
	}

	public Long[] getSelectedSPrograms() {
		return selectedSPrograms;
	}

	
	

	public String getSvAction() {
		return svAction;
	}

	public Long getSurveyOrgId() {
		return surveyOrgId;
	}

	

	public void setSvAction(String svAction) {
		this.svAction = svAction;
	}

	public void setSurveyOrgId(Long surveyOrgId) {
		this.surveyOrgId = surveyOrgId;
	}

	public int getPagesToShow() {
		return pagesToShow;
	}

	public void setPagesToShow(int pagesToShow) {
		this.pagesToShow = pagesToShow;
	}

	public int getStartPage() {
		int value;
		if (getCurrentPage() > (this.getPagesToShow() / 2)) {
			value = (this.getCurrentPage() - (this.getPagesToShow() / 2)) - 1;
		} else {
			value = 0;
		}
		this.startPage = value;
		return startPage;
	}

	public void setstartPage(int offset) {
		this.offset = offset;
	}

	public int getPagesSize() {
		return pagesSize;
	}

	public boolean isGovFlag() {
		return govFlag;
	}

	public void setPagesSize(int pagesSize) {
		this.pagesSize = pagesSize;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public void setGovFlag(boolean govFlag) {
		this.govFlag = govFlag;
	}

	public Collection<DocumentData> getCrDocuments() {
		return crDocuments;
	}

	public void setCrDocuments(Collection<DocumentData> crDocuments) {
		this.crDocuments = crDocuments;
	}



	public void setCreditTypeId(Long creditType) {
		this.creditTypeId = creditType;
	}

	public Long getCreditTypeId() {
		return creditTypeId;
	}


	
	public void setContractDetails(String contractDetails) {
		this.contractDetails = contractDetails;
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

	public void setConvenioNumcont(String convenioNumcont) {
		this.convenioNumcont = convenioNumcont;
	}

	public String getConvenioNumcont() {
		return convenioNumcont;
	}

	public String getClasiNPD() {
		return clasiNPD;
	}

	public void setClasiNPD(String clasiNPD) {
		this.clasiNPD = clasiNPD;
	}

	public void setPrevOrg(Long prevOrg) {
		this.prevOrg = prevOrg;
	}

	public Long getPrevOrg() {
		return prevOrg;
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

	// Dumies
	public String getPerspectives() {
		return null;
	}

	public Double getIpaBudget() {
		return ipaBudget;
	}

	public void setIpaBudget(Double ipaBudget) {
		this.ipaBudget = ipaBudget;
	}

	public AmpTeamMember getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(AmpTeamMember approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getContractDetails() {
		return contractDetails;
	}
	
	
	
	public class Identification {
		private String title = null;
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
		private Long accessionInstrument = new Long(0);
		private Long projectCategory = new Long(0);
		private String govAgreementNumber;
		private Long acChapter = new Long(0);
		private String budgetCheckbox;
		private Boolean governmentApprovalProcedures;
		private Boolean jointCriteria;
		private Boolean humanitarianAid;
		private String crisNumber;
		private OrgProjectId selectedOrganizations[];

		

		public void setSelectedOrganizations(int index, OrgProjectId orgProjectId) {
			selectedOrganizations[index] = orgProjectId;
		}

		public String getBudgetCheckbox() {
			return budgetCheckbox;
		}

		public void setBudgetCheckbox(String budgetCheckbox) {
			this.budgetCheckbox = budgetCheckbox;
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
		private String currentCompDate;
		private Long statusId = null;
		private String statusReason = null;

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

		public Collection getActRankCollection() {
			return actRankCollection;
		}

		public void setActRankCollection(Collection actRankCollection) {
			this.actRankCollection = actRankCollection;
		}

	}

	public class Location {

		private Long levelId = null;
		private Long implemLocationLevel;
		private Collection selectedLocs = null;

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

		public Collection getSelectedLocs() {
			return selectedLocs;
		}

		public void setSelectedLocs(Collection selectedLocs) {
			this.selectedLocs = selectedLocs;
		}
	}
	
	public org.digijava.module.aim.helper.Location getSelectedLocs(int index) {
		return (org.digijava.module.aim.helper.Location)(this.location.selectedLocs.toArray()[index]);
	}
	
	public class Sector {
		private List classificationConfigs;
		private String primarySectorVisible = null;
		private String secondarySectorVisible = null;
		private Collection<ActivitySector> activitySectors;

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

		public Collection<ActivitySector> getActivitySectors() {
			return activitySectors;
		}

		public void setActivitySectors(Collection<ActivitySector> activitySectors) {
			this.activitySectors = activitySectors;
		}

	}

	public class Component {
		private Collection activityComponentes;
		private String multiSectorSelecting;
		private Long selActivityComponentes[];

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

	}

	public class Programs {
		private AmpActivityProgramSettings nationalSetting;
		private List nationalPlanObjectivePrograms;
		private AmpActivityProgramSettings primarySetting;
		private List primaryPrograms;
		private List secondaryPrograms;
		private AmpActivityProgramSettings secondarySetting;

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
		private double totalCommitmentsDouble;
		private String totalDisbursements;
		private String totalExpenditures;
		private String totalPlannedExpenditures;
		private String totalPlannedDisbursements;
		private String totalPlannedDisbursementsOrders;
		private String totalActualDisbursementsOrders;
		private String unDisbursementsBalance;
		private boolean fixerate;

		// Add Funding fields

		private Collection organizations;
		private Collection<AmpCurrency> validcurrencies;
		private boolean dupFunding;
		private String orgName;
		private Long assistanceType = null;
		private Long modality = null;
		private List<MTEFProjection> fundingMTEFProjections;
		private Collection projections;
		private String orgFundingId;
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

		// end add funding field

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

		public FundingOrganization getFundingOrganization(int index) {
			int currentSize = fundingOrganizations.size();
			if (index >= currentSize) {
				for (int i = 0; i <= index - currentSize; i++) {
					fundingOrganizations.add(new FundingOrganization());
				}
			}
			return (FundingOrganization) ((ArrayList) fundingOrganizations).get(index);
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

		public List<MTEFProjection> getFundingMTEFProjections() {
			return fundingMTEFProjections;
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

	private Identification identification;

	public Identification getIdentification() {
		if (this.identification == null) {
			this.identification = new Identification();
		}
		return identification;
	}

	private Planning planning;

	public Planning getPlanning() {
		if (this.planning == null) {
			this.planning = new Planning();
		}
		return this.planning;
	}

	private Location location;

	public Location getLocation() {
		if (this.location == null) {
			this.location = new Location();
		}
		return this.location;
	}

	private Sector sectors;

	public Sector getSectors() {
		if (this.sectors == null) {
			this.sectors = new Sector();
		}
		return this.sectors;

	}

	private Component components;

	public Component getComponents() {
		if (this.components == null) {
			this.components = new Component();
		}
		return this.components;

	}

	private Programs programs;

	public Programs getPrograms() {
		if (this.programs == null) {
			this.programs = new Programs();
		}
		return this.programs;

	}

	private CrossCuttingIssues crossIssues;

	public CrossCuttingIssues getCrossIssues() {
		if (this.crossIssues == null) {
			this.crossIssues = new CrossCuttingIssues();
		}
		return this.crossIssues;

	}

	private Funding funding;

	public Funding getFunding() {
		if (this.funding == null) {
			this.funding = new Funding();
		}
		return this.funding;

	}

	public class Survey {

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

	}

	public class Agencies {

		private Long selConAgencies[];
		private Collection<AmpOrganisation> conAgencies;

		private Collection<AmpOrganisation> executingAgencies;
		private Long selExAgencies[];

		private Collection<AmpOrganisation> impAgencies;
		private Long selImpAgencies[];

		private Collection<AmpOrganisation> regGroups;
		private Long selRegGroups[];

		private Collection<AmpOrganisation> reportingOrgs;
		private Long selReportingOrgs[];

		private Collection<AmpOrganisation> sectGroups;
		private Long selSectGroups[];

		private Collection<AmpOrganisation> benAgencies;
		private Long selBenAgencies[];

		private Collection<AmpOrganisation> respOrganisations;
		private Long selRespOrganisations[];

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

	}

	private Agencies agencies;

	public Agencies getAgencies() {
		if (this.agencies == null) {
			this.agencies = new Agencies();
		}
		return this.agencies;

	}

	private Survey survey = null;

	public Survey getSurvey() {
		if (this.survey == null) {
			this.survey = new Survey();
		}
		return this.survey;
	}

	private ContactInformation contactInfo;

	public ContactInformation getContactInfo() {
		if (this.contactInfo == null) {
			this.contactInfo = new ContactInformation();
		}
		return this.contactInfo;

	}

	
	// indexed properties getter should be on the root form object
	public OrgProjectId getSelectedOrganizations(int index) {
		return identification.selectedOrganizations[index];
	}

	public ActivitySector getActivitySectors(int index) {
		return (ActivitySector)(sectors.activitySectors.toArray()[index]);
	}

	public AmpActivityProgram getNationalPlanObjectivePrograms(int index) {
		return (AmpActivityProgram)(programs.nationalPlanObjectivePrograms.toArray()[index]);
	}
	public AmpActivityProgram getPrimaryPrograms(int index) {
		return (AmpActivityProgram)(programs.primaryPrograms.toArray()[index]);
	}
	public AmpActivityProgram getSecondaryPrograms(int index) {
		return (AmpActivityProgram)(programs.secondaryPrograms.toArray()[index]);
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

	public class IndicatorME {
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
		private String currentVal;
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

		public String getCurrentVal() {
			return currentVal;
		}

		public void setCurrentVal(String currentVal) {
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

	private IndicatorME indicatorME = null;

	public IndicatorME getIndicator() {
		if (this.indicatorME == null) {
			this.indicatorME = new IndicatorME();
		}
		return this.indicatorME;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
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

	public void setCustomFields(List<CustomField> customFields) {
		this.customFields = customFields;
	}

	public List<CustomField> getCustomFields() {
		return customFields;
	}
}