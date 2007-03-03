package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.Collection;
import java.util.List;

public class ThemeForm extends ActionForm {

		  private Collection themes;
		  private Collection subPrograms;
		  private Collection prgIndicators;
		  private Long themeId;
		  private String programName;
		  private String programCode;
		  private String programDescription;
		  private String programType;
		  private int prgLevel;
		  private Long prgParentThemeId;
		  private Long rootId;
		  private String prgLanguage;
		  private String parentProgram;
		  private String version; 
		  private String event;
		  private Long indicatorId;
		  private String code;
		  private String name;
		  private String type;
		  private String indicatorDescription;
		  private int category;
		  private boolean npIndicator;
		  private List prgIndValues;
		  private int valueType;
		  private String creationDate;
		  private String flag; 
		  private Collection programTypeNames;
		  
		  private String programLeadAgency;
		  private String programTargetGroups;
		  private String programBackground;
		  private String programObjectives;
		  private String programOutputs;
		  private String programBeneficiaries;
		  private String programEnvironmentConsiderations;
		
		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
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
		public Collection getPrgIndicators() {
			return prgIndicators;
		}

		/**
		 * @param prgIndicators The prgIndicators to set.
		 */
		public void setPrgIndicators(Collection prgIndicators) {
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

		/**
		 * @return Returns the programType.
		 */
		public String getProgramType() {
			return programType;
		}

		/**
		 * @param programType The programType to set.
		 */
		public void setProgramType(String programType) {
			this.programType = programType;
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

    public int getValueType() {
        return valueType;
    }

    public String getCreationDate() {
        return creationDate;
    }

		public void setPrgIndValues(List prgIndValues) {
			this.prgIndValues = prgIndValues;
		}

    public void setValueType(int valueType) {
        this.valueType = valueType;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

	public Collection getProgramTypeNames() {
		return programTypeNames;
	}

	public void setProgramTypeNames(Collection programTypeNames) {
		this.programTypeNames = programTypeNames;
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
}
