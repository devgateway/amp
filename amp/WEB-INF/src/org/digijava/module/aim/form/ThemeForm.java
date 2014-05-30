package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.helper.IndicatorThemeBean;
import org.digijava.module.aim.helper.IndicatorsBean;


public class ThemeForm extends ActionForm {

		  private Collection themes;
		  private Collection subPrograms;
		  private Collection<IndicatorTheme> prgIndicators;
		  private Collection<IndicatorThemeBean> programIndicators; 
		  private Long themeId;
		  private String programName;
		  private String sectorName;
		  private String programCode;
		  private String budgetProgramCode;
		  private String programDescription;
		  //private String programType; replaced by programTypeCategValId which uses Category Manager
		  private Long programTypeCategValId;
		  private int prgLevel;
		  private Long prgParentThemeId;
		  private Long rootId;
		  private String prgLanguage;
		  private String parentProgram;
		  private String version; 
		  private String event;
		  private Long indicatorId;
		  private String code;
		  private String indicatorName; 
		  private String name;
		  private String themeName;
		  private String encodeName;
		  private String type;
		  private String indicatorDescription;
		  private int category;
		  private boolean npIndicator;
		  private List prgIndValues;
		  private int valueType[];
		  private String creationDate[];
		  private String flag; 
		  //private Collection programTypeNames; No longer needed, program type is in category manager
		  private Long selectTheme;
  		  private String indType;
  		  private Long indicatorsId[];
  		  private AmpLocation location;
  		  private Collection<AmpRegion> locationsCol;
  		  private Collection<LabelValueBean> selectedlocations;
  		  private String keyword;
  		  private Long selectedLocationId;
  		  private String indame;
		  
		  private String programLeadAgency;
		  private String programTargetGroups;
		  private String programBackground;
		  private String programObjectives;
		  private String programOutputs;
		  private String programBeneficiaries;
		  private String programEnvironmentConsiderations;
		  private Double programExternalFinancing;
		  private Double programInernalFinancing;
		  private Double programTotalFinancing;
		  
		 
		  private boolean reset;
		  private boolean indPopupReset;
          private int tempNumResults;
          private Collection<IndicatorsBean> allIndicators;
          private int numResults;
          private Integer currentPage;
      	  private String currentAlpha;
      	  private boolean startAlphaFlag;
      	  private Collection cols = null;
      	  private Collection colsAlpha = null;
       	  private int item;
       	  private Integer selectedindicatorFromPages;
      	  private String step = null;
      	  private Collection  allSectors;
      	  private Collection pagedCol;
      	  private Long parentId;
      	  private Double valAmount[];
      	  private Long indid[]; // list of ind selected from
      	  private Long parentIndex;
      	  private String action;
      	  
      	  private String fill; // National, region, district 	
      	  private boolean defaultCountryIsSet;
      	  private Collection<AmpCategoryValueLocations> regions;
      	  private Long impRegion;
      	  
      	  private Collection<AmpCategoryValueLocations> zones;
      	  private Long impZone;
          
      	  private Collection<AmpCategoryValueLocations> woredas;
      	  private Long impWoreda;
      	  
      	  private String country;
      	  private String impCountry;
      	  
      	  private Integer locationLevelIndex;
          
          
       // pop-up organisation selector window
      	private Collection pages;
      	private String[] alphaPages;
        private String alpha;

        private Boolean showInRMFilters;

    public Boolean getShowInRMFilters() {
        return showInRMFilters;
    }

    public void setShowInRMFilters(Boolean showInRMFilters) {
        this.showInRMFilters = showInRMFilters;
    }

    public String getAlpha() {
            return alpha;
        }

        public void setAlpha(String alpha) {
            this.alpha = alpha;
        }
        
      	
      	private String activitiesUsingTheme	= null;
      	private String settingsUsedByTheme	= null;
      	
      	
				
		public String getFill() {
			return fill;
		}

		public void setFill(String fill) {
			this.fill = fill;
		}
		
		

		public Collection<IndicatorThemeBean> getProgramIndicators() {
			return programIndicators;
		}

		public void setProgramIndicators(
				Collection<IndicatorThemeBean> programIndicators) {
			this.programIndicators = programIndicators;
		}

		public String getIndicatorName() {
			return indicatorName;
		}

		public void setIndicatorName(String indicatorName) {
			this.indicatorName = indicatorName;
		}

		public Integer getLocationLevelIndex() {
			return locationLevelIndex;
		}

		public void setLocationLevelIndex(Integer locationLevelIndex) {
			this.locationLevelIndex = locationLevelIndex;
		}

		public boolean isDefaultCountryIsSet() {
			return defaultCountryIsSet;
		}

		public void setDefaultCountryIsSet(boolean defaultCountryIsSet) {
			this.defaultCountryIsSet = defaultCountryIsSet;
		}

		public Collection<AmpCategoryValueLocations> getRegions() {
			return regions;
		}

		public void setRegions(Collection<AmpCategoryValueLocations> regions) {
			this.regions = regions;
		}

		public Long getImpRegion() {
			return impRegion;
		}

		public void setImpRegion(Long impRegion) {
			this.impRegion = impRegion;
		}

		public Collection<AmpCategoryValueLocations> getZones() {
			return zones;
		}

		public void setZones(Collection<AmpCategoryValueLocations> zones) {
			this.zones = zones;
		}

		public Long getImpZone() {
			return impZone;
		}

		public void setImpZone(Long impZone) {
			this.impZone = impZone;
		}

		public Collection<AmpCategoryValueLocations> getWoredas() {
			return woredas;
		}

		public void setWoredas(Collection<AmpCategoryValueLocations> woredas) {
			this.woredas = woredas;
		}

		public Long getImpWoreda() {
			return impWoreda;
		}

		public void setImpWoreda(Long impWoreda) {
			this.impWoreda = impWoreda;
		}

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getImpCountry() {
			return impCountry;
		}

		public void setImpCountry(String impCountry) {
			this.impCountry = impCountry;
		}

		/**
		 * @return Returns the themes.
		 */
		public Collection getThemes() {
			return themes;
		}

		/**
		 * @param themes The themes to set.
		 */
		public void setThemes(Collection themes) {
			this.themes = themes;
		}

		/**
		 * @return Returns the subPrograms.
		 */
		public Collection getSubPrograms() {
			return subPrograms;
		}

		/**
		 * @param subPrograms The subPrograms to set.
		 */
		public void setSubPrograms(Collection subPrograms) {
			this.subPrograms = subPrograms;
		}

		/**
		 * @return Returns the prgIndicators.
		 */
		public Collection<IndicatorTheme> getPrgIndicators() {
			return prgIndicators;
		}

		/**
		 * @param prgIndicators The prgIndicators to set.
		 */
		public void setPrgIndicators(Collection<IndicatorTheme> prgIndicators) {
			this.prgIndicators = prgIndicators;
		}

		/**
		 * @return Returns the themeId.
		 */
		public Long getThemeId() {
			return themeId;
		}

		/**
		 * @param themeId The themeId to set.
		 */
		public void setThemeId(Long themeId) {
			this.themeId = themeId;
		}

		/**
		 * @return Returns the programCode.
		 */
		public String getProgramCode() {
			return programCode;
		}

		/**
		 * @param programCode The programCode to set.
		 */
		public void setProgramCode(String programCode) {
			this.programCode = programCode;
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
		 * @return Returns the programName.
		 */
		public String getProgramName() {
			return programName;
		}

		/**
		 * @param programName The programName to set.
		 */
		public void setProgramName(String programName) {
			this.programName = programName;
		}

		

		public Long getProgramTypeCategValId() {
			return programTypeCategValId;
		}

		public void setProgramTypeCategValId(Long programTypeCategValId) {
			this.programTypeCategValId = programTypeCategValId;
		}

		/**
		 * @return Returns the prgLevel.
		 */
		public int getPrgLevel() {
			return prgLevel;
		}

		/**
		 * @param prgLevel The prgLevel to set.
		 */
		public void setPrgLevel(int prgLevel) {
			this.prgLevel = prgLevel;
		}

		/**
		 * @return Returns the indicatorDescription.
		 */
		public String getIndicatorDescription() {
			return indicatorDescription;
		}

		/**
		 * @param indicatorDescription The indicatorDescription to set.
		 */
		public void setIndicatorDescription(String indicatorDescription) {
			this.indicatorDescription = indicatorDescription;
		}

		/**
		 * @return Returns the prgLanguage.
		 */
		public String getPrgLanguage() {
			return prgLanguage;
		}

		/**
		 * @param prgLanguage The prgLanguage to set.
		 */
		public void setPrgLanguage(String prgLanguage) {
			this.prgLanguage = prgLanguage;
		}

		/**
		 * @return Returns the parentProgram.
		 */
		public String getParentProgram() {
			return parentProgram;
		}

		/**
		 * @param parentProgram The parentProgram to set.
		 */
		public void setParentProgram(String parentProgram) {
			this.parentProgram = parentProgram;
		}

		/**
		 * @return Returns the prgParentThemeId.
		 */
		public Long getPrgParentThemeId() {
			return prgParentThemeId;
		}

		/**
		 * @param prgParentThemeId The prgParentThemeId to set.
		 */
		public void setPrgParentThemeId(Long prgParentThemeId) {
			this.prgParentThemeId = prgParentThemeId;
		}

		/**
		 * @return Returns the rootId.
		 */
		public Long getRootId() {
			return rootId;
		}

		/**
		 * @param rootId The rootId to set.
		 */
		public void setRootId(Long rootId) {
			this.rootId = rootId;
		}

		/**
		 * @return Returns the version.
		 */
		public String getVersion() {
			return version;
		}

		/**
		 * @param version The version to set.
		 */
		public void setVersion(String version) {
			this.version = version;
		}

		/**
		 * @return Returns the event.
		 */
		public String getEvent() {
			return event;
		}

		/**
		 * @param event The event to set.
		 */
		public void setEvent(String event) {
			this.event = event;
		}

		/**
		 * @return Returns the category.
		 */
		public int getCategory() {
			return category;
		}

		/**
		 * @param category The category to set.
		 */
		public void setCategory(int category) {
			this.category = category;
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
		 * @return Returns the code.
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code The code to set.
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name The name to set.
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return Returns the npIndicator.
		 */
		public boolean isNpIndicator() {
			return npIndicator;
		}

		/**
		 * @param npIndicator The npIndicator to set.
		 */
		public void setNpIndicator(boolean npIndicator) {
			this.npIndicator = npIndicator;
		}

		/**
		 * @return Returns the type.
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type The type to set.
		 */
		public void setType(String type) {
			this.type = type;
		}

		public List getPrgIndValues() {
			return prgIndValues;
		}

    public int[] getValueType() {
        return valueType;
    }

    public String[] getCreationDate() {
        return creationDate;
    }

		public void setPrgIndValues(List prgIndValues) {
			this.prgIndValues = prgIndValues;
		}

    public void setValueType(int valueType[]) {
        this.valueType = valueType;
    }

    public void setCreationDate(String creationDate[]) {
        this.creationDate = creationDate;
    }
	
	public void setProgramLeadAgency (String programLeadAgency) {
		this.programLeadAgency	= programLeadAgency;
	}
	public String getProgramLeadAgency () {
		return this.programLeadAgency;
	}
	
	public void setProgramTargetGroups (String programTargetGroups) {
		this.programTargetGroups	= programTargetGroups;
	}
	public String getProgramTargetGroups () {
		return this.programTargetGroups;
	}

	
	public void setProgramBackground (String programBackground) {
		this.programBackground	= programBackground;
	}
	public String getProgramBackground () {
		return this.programBackground;
	}
	
	public void setProgramObjectives (String programObjectives) {
		this.programObjectives	= programObjectives;
	}
	public String getProgramObjectives() {
		return this.programObjectives;
	}
	
	public void setProgramOutputs (String programOutputs) {
		this.programOutputs	= programOutputs;
	}
	public String getProgramOutputs () {
		return this.programOutputs;
	}
	
	public void setProgramBeneficiaries (String programBeneficiaries) {
		this.programBeneficiaries	= programBeneficiaries;
	}
	public String getProgramBeneficiaries () {
		return this.programBeneficiaries;
	}
	
	public void setProgramEnvironmentConsiderations (String programEnvironmentConsiderations) {
		this.programEnvironmentConsiderations	= programEnvironmentConsiderations;
	}
	public String getProgramEnvironmentConsiderations () {
		return this.programEnvironmentConsiderations;
	}
	
	/**
	 * @return Returns the selectTheme.
	 */
	public Long getSelectTheme() {
		return selectTheme;
	}

	/**
	 * @param selectTheme The selectTheme to set.
	 */
	public void setSelectTheme(Long selectTheme) {
		this.selectTheme = selectTheme;
	}
	
	/**
	 * @return Returns the indType.
	 */
	public String getIndType() {
		return indType;
	}

	/**
	 * @param indType The indType to set.
	 */
	public void setIndType(String indType) {
		this.indType = indType;
	}

	public String getEncodeName() {		
		if(name != null) {
				encodeName = name.replace("'", "\\'");
				encodeName = encodeName.replace("\"", "\\'");
				////System.out.println(encodeName);
			
		}
		return encodeName;
	}

	public void setEncodeName(String encodeName) {
		this.encodeName = encodeName;
	}

	public Double getProgramExternalFinancing() {
		return programExternalFinancing;
	}

	public void setProgramExternalFinancing(Double programExternalFinancing) {
		this.programExternalFinancing = programExternalFinancing;
	}

	public Double getProgramInernalFinancing() {
		return programInernalFinancing;
	}

	public void setProgramInernalFinancing(Double programInernalFinancing) {
		this.programInernalFinancing = programInernalFinancing;
	}

	public Double getProgramTotalFinancing() {
		return programTotalFinancing;
	}

	public void setProgramTotalFinancing(Double programTotalFinancing) {
		this.programTotalFinancing = programTotalFinancing;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public Long[] getIndicatorsId() {
		return indicatorsId;
	}

	public void setIndicatorsId(Long[] indicatorsId) {
		this.indicatorsId = indicatorsId;
	}

	public AmpLocation getLocation() {
		return location;
	}

	public void setLocation(AmpLocation location) {
		this.location = location;
	}

	public Collection<AmpRegion> getLocationsCol() {
		return locationsCol;
	}

	public void setLocationsCol(Collection<AmpRegion> locationsCol) {
		this.locationsCol = locationsCol;
	}

	public Collection<LabelValueBean> getSelectedlocations() {
		return selectedlocations;
	}

	public void setSelectedlocations(Collection<LabelValueBean> selectedlocations) {
		this.selectedlocations = selectedlocations;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getSelectedLocationId() {
		return selectedLocationId;
	}

	public void setSelectedLocationId(Long selectedLocationId) {
		this.selectedLocationId = selectedLocationId;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public boolean isIndPopupReset() {
		return indPopupReset;
	}

	public void setIndPopupReset(boolean indPopupReset) {
		this.indPopupReset = indPopupReset;
	}

	public int getTempNumResults() {
		return tempNumResults;
	}

	public void setTempNumResults(int tempNumResults) {
		this.tempNumResults = tempNumResults;
	}

	public Collection<IndicatorsBean> getAllIndicators() {
		return allIndicators;
	}

	public void setAllIndicators(Collection<IndicatorsBean> allIndicators) {
		this.allIndicators = allIndicators;
	}

	public int getNumResults() {
		return numResults;
	}

	public void setNumResults(int numResults) {
		this.numResults = numResults;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getCurrentAlpha() {
		return currentAlpha;
	}

	public void setCurrentAlpha(String currentAlpha) {
		this.currentAlpha = currentAlpha;
	}

	public boolean isStartAlphaFlag() {
		return startAlphaFlag;
	}

	public void setStartAlphaFlag(boolean startAlphaFlag) {
		this.startAlphaFlag = startAlphaFlag;
	}

	public Collection getCols() {
		return cols;
	}

	public void setCols(Collection cols) {
		this.cols = cols;
	}

	public Collection getColsAlpha() {
		return colsAlpha;
	}

	public void setColsAlpha(Collection colsAlpha) {
		this.colsAlpha = colsAlpha;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public Integer getSelectedindicatorFromPages() {
		return selectedindicatorFromPages;
	}

	public void setSelectedindicatorFromPages(Integer selectedindicatorFromPages) {
		this.selectedindicatorFromPages = selectedindicatorFromPages;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public Collection getAllSectors() {
		return allSectors;
	}

	public void setAllSectors(Collection allSectors) {
		this.allSectors = allSectors;
	}

	public Collection getPages() {
		return pages;
	}

	public void setPages(Collection pages) {
		this.pages = pages;
	}

	public String[] getAlphaPages() {
		return alphaPages;
	}

	public void setAlphaPages(String[] alphaPages) {
		this.alphaPages = alphaPages;
	}

	public Collection getPagedCol() {
		return pagedCol;
	}

	public void setPagedCol(Collection pagedCol) {
		this.pagedCol = pagedCol;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Double[] getValAmount() {
		return valAmount;
	}

	public void setValAmount(Double[] valAmount) {
		this.valAmount = valAmount;
	}

	public Long[] getIndid() {
		return indid;
	}

	public void setIndid(Long[] indid) {
		this.indid = indid;
	}

	public String getIndame() {
		return indame;
	}

	public void setIndame(String indame) {
		this.indame = indame;
	}

	public Long getParentIndex() {
		return parentIndex;
	}

	public void setParentIndex(Long parentIndex) {
		this.parentIndex = parentIndex;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActivitiesUsingTheme() {
		return activitiesUsingTheme;
	}

	public void setActivitiesUsingTheme(String activitiesUsingTheme) {
		this.activitiesUsingTheme = activitiesUsingTheme;
	}

	public String getSettingsUsedByTheme() {
		return settingsUsedByTheme;
	}

	public void setSettingsUsedByTheme(String settingsUsedByTheme) {
		this.settingsUsedByTheme = settingsUsedByTheme;
	}

	public String getBudgetProgramCode() {
		return budgetProgramCode;
	}

	public void setBudgetProgramCode(String budgetProgramCode) {
		this.budgetProgramCode = budgetProgramCode;
	}
      
	
}
