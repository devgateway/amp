package org.digijava.module.aim.form ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.FinancingBreakdown;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class ChannelOverviewForm extends MainProjectDetailsForm
{
	private Long id ;
	//private String ampId ;
	private String objective ;
	private String status ;
	private String projectImplUnit ;
	private String language ;
	private String version ;
	private Collection sectors ;
	private Collection subSectors ;
	private String country;
	private Collection locations;
	private String fundingagency ;
	private String reportingagency;
	private String implagency;
	private String relatedins;
	private String level ;
	private Collection internalIds ;
	private String modality ;
	private String theme;
	private String modalityCode;
	private int flag;
	private int flago;
	private String activityStartDate;
	private String activityCloseDate;
	private String condition;
	private String grandTotal;
	private boolean validLogin;
	private Collection assistance ;
	private Integer pageNo;
	private String currCode;
	private Collection modal;
	private boolean canView;
	private HashMap allComments;
	private AmpActivityVersion activity;
	private Long implemLocationLevel;
	private int numImplLocationLevels	= 0;
	private String buttonText;  // added by Akash for activity approval
    private List primaryPrograms;
    private List secondaryPrograms;
    private List nationalPlanObjectivePrograms;
	private Collection<ActivitySector> activitySectors;
	//contact Information
	private AmpActivityContact primaryDonorContact;
	private AmpActivityContact primaryMofedContact;
	private AmpActivityContact primaryprojCoordinatorContact;
	private AmpActivityContact primarySectorMinistryContact;
	private AmpActivityContact primaryImplExecutingAgencyContact;
	
	private Collection<FinancingBreakdown> financingBreakdown;
	
	private String equalOpportunity;
	private String environment;
	private String minorities;
        private SortedSet<Location> sortedLocations;


    public Collection<FinancingBreakdown> getFinancingBreakdown() {
		return financingBreakdown;
	}

	public void setFinancingBreakdown(Collection<FinancingBreakdown> financingBreakdown) {
		this.financingBreakdown = financingBreakdown;
	}

	private String debugFM;
   
    private OrgProjectId selectedOrganizations[]; //To Show organitations name in channel overview
	private    HashMap<String,String> errors = new HashMap<String, String>();
	private    HashMap<String,String> messages = new HashMap<String, String>();

	private List classificationConfigs;
	private Long impLocation;
	private String projectCategory;
	private String govAgreementNumber;
	private String financialInstrument;
	private String acChapter;
	private String procurementSystem;
	private String reportingSystem;
	private String auditSystem;
	private String institutions;
	private String accessionInstrument;
	private Collection closingDates;
	private Integer countryIndex;
	private List<Integer> numOfLocationsPerLayer;
	private String budgetsector;
	private String budgetorganization;
	private String budgetdepartment;
	private String budgetprogram;
	
	private AmpCategoryValue budgetCV;
	

	public AmpCategoryValue getBudgetCV() {
		return budgetCV;
	}

	public void setBudgetCV(AmpCategoryValue budgetCV) {
		this.budgetCV = budgetCV;
	}

	public String getBudgetsector() {
		return budgetsector;
	}

	public void setBudgetsector(String budgetsector) {
		this.budgetsector = budgetsector;
	}

	public String getBudgetorganization() {
		return budgetorganization;
	}

	public void setBudgetorganization(String budgetorganization) {
		this.budgetorganization = budgetorganization;
	}

	public String getBudgetdepartment() {
		return budgetdepartment;
	}

	public void setBudgetdepartment(String budgetdepartment) {
		this.budgetdepartment = budgetdepartment;
	}

	public String getBudgetprogram() {
		return budgetprogram;
	}

	public void setBudgetprogram(String budgetprogram) {
		this.budgetprogram = budgetprogram;
	}

	/**
	 * @return Returns the revCompDates.
	 */
	public Collection getClosingDates() {
		return closingDates;
	}

	/**
	 * @param revCompDates
	 *            The revCompDates to set.
	 */
	public void setClosingDates(Collection closingDates) {
		this.closingDates = closingDates;
	}
	  
	public void addMessage(String key, String value) {
	    this.messages.put(key, value) ;
	}

	public void addError(String key, String value) {
	    this.errors.put(key, value) ;
	}

	public void clearMessages(){
        this.errors.clear();
	    this.messages.clear();
	}
    /**
     * 
     * @return
     */
    public Long getImplemLocationLevel() {
		return implemLocationLevel;
	}
    
    public String getDebugFM() {
		return debugFM;
	}

	public void setDebugFM(String debugFM) {
		this.debugFM = debugFM;
	}

	/**
     * 
     * @param implemLocationLevel
     */

	public void setImplemLocationLevel(Long implemLocationLevel) {
		this.implemLocationLevel = implemLocationLevel;
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

	public Integer getPageNo() {
			  return pageNo;
	}

	public void setPageNo(Integer page) {
			  pageNo = page;
	}

	private boolean add;  // added by Priyajith
	private ArrayList<AmpCategoryValue> typesOfAssistance;
	private ArrayList<AmpCategoryValue> modalities;
	private ArrayList<AmpCategoryValue> fundingStatuses;
	private ArrayList<AmpCategoryValue> modesOfPayment;

	private Collection relOrgs;

	public boolean getAdd() {
			  return add;
	}

	public void setAdd(boolean flag) {
			  add = flag;
	}

	public Long getId()
	{
		return id ;
	}

	/*
	public String getAmpId()
	{
		return ampId;
	}*/

	public String getLanguage()
	{
		return language;
	}

	public String getObjective()
	{
		return objective;
	}

	public String getStatus()
	{
		return status;
	}

	public String getVersion()
	{
		return version;
	}

	public String getLevel()
	{
		return level;
	}


	public boolean getValidLogin()
	{
		return validLogin;
	}

	public int getFlag()
	{
		return flag;
	}

	public int getFlago()
	{
		return flago;
	}

	public String getActivityStartDate()
	{
		return activityStartDate;
	}

	public String getActivityCloseDate()
	{
		return activityCloseDate;
	}

	public String getCondition()
	{
		return condition;
	}

	public String getGrandTotal()
	{
		return grandTotal;
	}

	public Collection getAssistance()
	{
		return assistance;
	}

	public void setId(Long id)
	{
		this.id = id ;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public void setObjective(String objective)
	{
		this.objective = objective ;
	}

	public void setStatus(String status)
	{
		this.status = status ;
	}

	public void setVersion(String version)
	{
		this.version = version ;
	}

	public void setActivityStartDate(String string)
	{
		this.activityStartDate = string ;
	}

	public void setActivityCloseDate(String string)
	{
		this.activityCloseDate = string ;
	}

	public void setCondition(String string)
	{
		this.condition = string ;
	}

	public void setGrandTotal(String string)
	{
		this.grandTotal = string ;
	}


	public void setValidLogin(boolean bool)
	{
		this.validLogin = bool ;
	}

	public Collection getSectors()
	{
		return sectors;
	}


	public void setSectors(Collection sectors)
	{
		this.sectors = sectors ;
	}


	public Collection getSubSectors()
	{
		return subSectors;
	}


	public void setSubSectors(Collection collection)
	{
		subSectors = collection;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{

		this.country = country;
	}
	public Collection getLocations()
	{
		return locations;
	}

	public void setLocations(Collection locations)
	{

		this.locations = locations;
	}
	public String getFundingagency()
	{
		return fundingagency ;
	}
	public void setFundingagency(String fundingagency)
	{
		this.fundingagency = fundingagency ;
	}

	public String getReportingagency()
	{
		return reportingagency;
	}
	public void setReportingagency(String reportingagency)
	{
		this.reportingagency= reportingagency;
	}


	public String getImplagency()
	{
		return implagency;
	}
	public void setImplagency(String implagency)
	{
		this.implagency= implagency;
	}


	public String getRelatedins()
	{
		return relatedins;
	}

	/*
	public void setAmpId(String string) {
		ampId = string;
	}*/

	public void setRelatedins(String relatedins)
	{
		this.relatedins= relatedins;
	}

	public void setLevel(String level)
	{
		this.level = level;
	}

	public void setFlago(int flago)
	{
		this.flago = flago;
	}

	public void setFlag(int flag)
	{
		this.flag = flag;
	}

	/**
	 * @return
	 */
	public Collection getInternalIds() {
		return internalIds;
	}

	/**
	 * @param collection
	 */
	public void setInternalIds(Collection collection) {
		internalIds = collection;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String string) {
		modality = string;
	}

	public String getModalityCode() {
		return modalityCode;
	}

	public void setModalityCode(String string) {
		modalityCode = string;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String string) {
		theme = string;
	}

	public void setAssistance(Collection c)
	{
		assistance = c ;
	}

	/**
	 * @return Returns the activity.
	 */
	public AmpActivityVersion getActivity() {
		return activity;
	}
	/**
	 * @param activity The activity to set.
	 */
	public void setActivity(AmpActivityVersion activity) {
		this.activity = activity;
	}
	/**
	 * @return Returns the currCode.
	 */
	public String getCurrCode() {
		return currCode;
	}
	/**
	 * @param currCode The currCode to set.
	 */
	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	/**
	 * @return Returns the modal.
	 */
	public Collection getModal() {
		return modal;
	}
	/**
	 * @param modal The modal to set.
	 */
	public void setModal(Collection modal) {
		this.modal = modal;
	}
	/**
	 * @return Returns the buttonText.
	 */
	public String getButtonText() {
		return buttonText;
	}
	/**
	 * @param buttonText The buttonText to set.
	 */
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	/**
	 * @return Returns the canView.
	 */
	public boolean isCanView() {
		return canView;
	}

	/**
	 * @param canView The canView to set.
	 */
	public void setCanView(boolean canView) {
		this.canView = canView;
	}

	public HashMap getAllComments() {
		return allComments;
	}

        public List getSecondaryPrograms() {
                return secondaryPrograms;
        }

        public List getPrimaryPrograms() {
                return primaryPrograms;
        }

        public List getNationalPlanObjectivePrograms() {
                return nationalPlanObjectivePrograms;
        }

        public void setAllComments(HashMap allComments) {
		this.allComments = allComments;
	}

        public void setSecondaryPrograms(List secondaryPrograms) {
                this.secondaryPrograms = secondaryPrograms;
        }

        public void setPrimaryPrograms(List primaryPrograms) {
                this.primaryPrograms = primaryPrograms;
        }

        public void setNationalPlanObjectivePrograms(List
            nationalPlanObjectivePrograms) {
                this.nationalPlanObjectivePrograms =
                    nationalPlanObjectivePrograms;
        }

		public int getNumImplLocationLevels() {
			return numImplLocationLevels;
		}

		public void setNumImplLocationLevels(int numImplLocationLevels) {
			this.numImplLocationLevels = numImplLocationLevels;
		}
                
                public boolean getImplLocationCountry(){
                    boolean flag=ActivityUtil.isImplLocationCountry(activity.getAmpActivityId());
                    return flag;
                }

				public HashMap<String, String> getErrors() {
					return errors;
				}

				public void setErrors(HashMap<String, String> errors) {
					this.errors = errors;
				}

				public HashMap<String, String> getMessages() {
					return messages;
				}

				public void setMessages(HashMap<String, String> messages) {
					this.messages = messages;
				}


			    public List getClassificationConfigs() {
			        return classificationConfigs;
			    }

			    public void setClassificationConfigs(List classificationConfigs) {
			        this.classificationConfigs = classificationConfigs;
			    }
				/**
				 * @return Returns the activitySectors.
				 */
				public Collection<ActivitySector> getActivitySectors() {
					return activitySectors;
				}

				/**
				 * @param activitySectors
				 *            The activitySectors to set.
				 */
				public void setActivitySectors(Collection<ActivitySector> activitySectors) {
					this.activitySectors = activitySectors;
				}

				public void setTypesOfAssistance(
						ArrayList<AmpCategoryValue> typesOfAssistance) {
					this.typesOfAssistance = typesOfAssistance;
					
				}
				public ArrayList<AmpCategoryValue> getTypesOfAssistance(){
					return this.typesOfAssistance;
				}

				public void setUniqueModalities(
						ArrayList<AmpCategoryValue> modalities) {
					this.modalities = modalities;
					
				}
				public ArrayList<AmpCategoryValue> getUniqueModalities(){
					return this.modalities;
				}

				public void setImpLocation(Long impLocation) {
					this.impLocation = impLocation;
				}

				public Long getImpLocation() {
					return impLocation;
				}

				public String getProjectCategory() {
					return projectCategory;
				}

				public void setProjectCategory(String projectCategory) {
					this.projectCategory = projectCategory;
				}
				
				public String getGovAgreementNumber() {
					return govAgreementNumber;
				}

				public void setGovAgreementNumber(String govAgreementNumber) {
					this.govAgreementNumber = govAgreementNumber;
				}

				public void setRelOrgs(Collection relOrgs) {
					// TODO Auto-generated method stub
					this.relOrgs = relOrgs;
				}

				public Collection getRelOrgs() {
					return this.relOrgs;
				}

				public String getFinancialInstrument() {
					return financialInstrument;
				}
			
				public void setFinancialInstrument(String financialInformation) {
					this.financialInstrument = financialInformation;
				}
  public String getProcurementSystem() {
					return procurementSystem;
				}

				public void setProcurementSystem(String procurementSystem) {
					this.procurementSystem = procurementSystem;
				}

				public String getReportingSystem() {
					return reportingSystem;
				}

				public void setReportingSystem(String reportingSystem) {
					this.reportingSystem = reportingSystem;
				}

				public String getAuditSystem() {
					return auditSystem;
				}

				public void setAuditSystem(String auditSystem) {
					this.auditSystem = auditSystem;
				}

				public String getInstitutions() {
					return institutions;
				}

				public void setInstitutions(String institutions) {
					this.institutions = institutions;
				}

public String getAccessionInstrument() {
	    return accessionInstrument;
	  }

	  public void setAccessionInstrument(String accessionInstrument) {
	    this.accessionInstrument = accessionInstrument;
	  }

	  public String getAcChapter() {
		    return acChapter;
		  }

		  public void setAcChapter(String acChapter) {
		    this.acChapter = acChapter;
		  }

		/**
		 * @return the countryIndex
		 */
		public Integer getCountryIndex() {
			return countryIndex;
		}

		/**
		 * @param countryIndex the countryIndex to set
		 */
		public void setCountryIndex(Integer countryIndex) {
			this.countryIndex = countryIndex;
		}

		public ArrayList<AmpCategoryValue> getFundingStatuses() {
			return fundingStatuses;
		}

		public void setFundingStatuses(ArrayList<AmpCategoryValue> fundingStatuses) {
			this.fundingStatuses = fundingStatuses;
		}

		public List<Integer> getNumOfLocationsPerLayer() {
			return numOfLocationsPerLayer;
		}

		public void setNumOfLocationsPerLayer(List<Integer> numOfLocationsPerLayer) {
			this.numOfLocationsPerLayer = numOfLocationsPerLayer;
		}

		/**
		 * @return the modesOfPayment
		 */
		public ArrayList<AmpCategoryValue> getModesOfPayment() {
			return modesOfPayment;
		}

		/**
		 * @param modesOfPayment the modesOfPayment to set
		 */
		public void setModesOfPayment(ArrayList<AmpCategoryValue> modesOfPayment) {
			this.modesOfPayment = modesOfPayment;
		}

		public void setPrimaryDonorContact(AmpActivityContact primaryDonorContact) {
			this.primaryDonorContact = primaryDonorContact;
		}

		public AmpActivityContact getPrimaryDonorContact() {
			return primaryDonorContact;
		}

		public void setPrimaryMofedContact(AmpActivityContact primaryMofedContact) {
			this.primaryMofedContact = primaryMofedContact;
		}

		public AmpActivityContact getPrimaryMofedContact() {
			return primaryMofedContact;
		}

		public void setPrimaryprojCoordinatorContact(
				AmpActivityContact primaryprojCoordinatorContact) {
			this.primaryprojCoordinatorContact = primaryprojCoordinatorContact;
		}

		public AmpActivityContact getPrimaryprojCoordinatorContact() {
			return primaryprojCoordinatorContact;
		}

		public void setPrimarySectorMinistryContact(
				AmpActivityContact primarySectorMinistryContact) {
			this.primarySectorMinistryContact = primarySectorMinistryContact;
		}

		public AmpActivityContact getPrimarySectorMinistryContact() {
			return primarySectorMinistryContact;
		}

		public void setPrimaryImplExecutingAgencyContact(
				AmpActivityContact primaryImplExecutingAgencyContact) {
			this.primaryImplExecutingAgencyContact = primaryImplExecutingAgencyContact;
		}

		public AmpActivityContact getPrimaryImplExecutingAgencyContact() {
			return primaryImplExecutingAgencyContact;
		}

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

		public String getProjectImplUnit() {
			return projectImplUnit;
		}

		public void setProjectImplUnit(String projectImplUnit) {
			this.projectImplUnit = projectImplUnit;
		}
                public SortedSet<Location> getSortedLocations() {
                    return sortedLocations;
                }

                public void setSortedLocations(SortedSet<Location> sortedLocations) {
                    this.sortedLocations = sortedLocations;
                }

}
