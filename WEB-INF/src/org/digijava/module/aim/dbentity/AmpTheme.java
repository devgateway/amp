package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpTheme implements Serializable, Identifiable
{
	private static final long serialVersionUID = 1L;
	private AmpActivity activityId;
    private Long ampThemeId ;
	private AmpTheme parentThemeId ;
	private String themeCode ;
	private String budgetProgramCode ;
	private String name ;
	private String encodeName;
	//private String type ;
	private AmpCategoryValue typeCategoryValue;
	private Integer indlevel;
	private String description ;
	private String language ;
	private String version ;

	/**
	 * Connections to Indicators.
	 * This field contains {@link IndicatorTheme} beans.
	 * Please refer to AmpTheme.hbm.xml and IndicatorConnection.hbm.xml for details.
	 */
	private Set<IndicatorTheme> indicators;
	private Set activities;
	private String leadAgency;
	private String targetGroups;
	private String background;
	private String objectives;
	private String outputs;
	private String beneficiaries;
	private String environmentConsiderations;
	private BigDecimal externalFinancing;
	private BigDecimal internalFinancing;
	private BigDecimal totalFinancing;
	
	private String programviewname;
        private Set programSettings;

	public Set getActivities() {
		return activities;
	}

	public void setActivities(Set activities) {
		this.activities = activities;
	}

	public AmpTheme()
	{
		activities=new HashSet();
	}

	/**
	 * @return
	 */
	public Long getAmpThemeId() {
		return ampThemeId;
	}

	/**
	 * @return Returns the parentThemeId.
	 */
	public AmpTheme getParentThemeId() {
		return parentThemeId;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return
	 */
	public String getThemeCode() {
		return themeCode;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param long1
	 */
	public void setAmpThemeId(Long long1) {
		ampThemeId = long1;
	}

	/**
	 * @param parentThemeId The parentThemeId to set.
	 */
	public void setParentThemeId(AmpTheme parentThemeId) {
		this.parentThemeId = parentThemeId;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param string
	 */
	public void setLanguage(String string) {
		language = string;
	}

	/**
	 * @param string
	 */
	public void setThemeCode(String string) {
		themeCode = string;
	}

	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}

	/**
	 * @return Returns the indicators.
	 */
	public Set<IndicatorTheme> getIndicators() {
		return indicators;
	}

    public AmpActivity getActivityId() {
        return activityId;
    }

    /**
	 * @param indicators The indicators to set.
	 */
	public void setIndicators(Set<IndicatorTheme> indicators) {
		this.indicators = indicators;
	}

    /**
	 * @return Returns the indlevel.
	 */
	public Integer getIndlevel() {
		if(indlevel == null)
			indlevel = new Integer(0);
		return indlevel;
	}

	/**
	 * @param indlevel The indlevel to set.
	 */
	public void setIndlevel(Integer indlevel) {
		if(indlevel == null)
			indlevel = new Integer(0);
		this.indlevel = indlevel;
	}

	public void setActivityId(AmpActivity activityId) {
        this.activityId = activityId;
    }

    public boolean equals(Object obj) {
        if (obj == null)
            throw new NullPointerException();

        if (!(obj instanceof AmpTheme))
            throw new ClassCastException();

        AmpTheme theme = (AmpTheme) obj;
        return (theme.getAmpThemeId().equals(ampThemeId));
	}


    public void setLeadAgency (String leadAgency) {
		this.leadAgency	= leadAgency;
	}
	public String getLeadAgency () {
		return this.leadAgency;
	}

	public void setTargetGroups (String targetGroups) {
		this.targetGroups	= targetGroups;
	}
	public String getTargetGroups () {
		return this.targetGroups;
	}


	public void setBackground (String background) {
		this.background	= background;
	}
	public String getBackground () {
		return this.background;
	}

	public void setObjectives (String objectives) {
		this.objectives	= objectives;
	}
	public String getObjectives() {
		return this.objectives;
	}

	public void setOutputs (String outputs) {
		this.outputs	= outputs;
	}
	public String getOutputs () {
		return this.outputs;
	}

	public void setBeneficiaries (String beneficiaries) {
		this.beneficiaries	= beneficiaries;
	}
	public String getBeneficiaries () {
		return this.beneficiaries;
	}

	public void setEnvironmentConsiderations (String environmentConsiderations) {
		this.environmentConsiderations	= environmentConsiderations;
	}
	public String getEnvironmentConsiderations () {
		return this.environmentConsiderations;
	}

	public String getProgramviewname() {
		return programviewname;
	}

	public void setProgramviewname(String programviewname) {
		this.programviewname = programviewname;
	}

	public AmpCategoryValue getTypeCategoryValue() {
		return typeCategoryValue;
	}

	public void setTypeCategoryValue(AmpCategoryValue typeCategoryValue) {
		this.typeCategoryValue = typeCategoryValue;
	}

	public String getEncodeName() {
		if(name != null) {
				encodeName = name.replace("'", " ");
				//encodeName = encodeName.replaceAll("\"", "\\\'");
				//System.out.println(encodeName);

		}
		return encodeName;
	}

        public Set getProgramSettings() {
                return programSettings;
        }

        public void setEncodeName(String encodeName) {
		this.encodeName = encodeName;
	}

        public void setProgramSettings(Set programSettings) {
                this.programSettings = programSettings;
        }

        public Object getIdentifier() {
           return this.getAmpThemeId();
        }
        
        @Override
        public String toString() {
		return name;
	}

		/**
		 * @return the totalFinancing
		 */
		public BigDecimal getTotalFinancing() {
			return FeaturesUtil.applyThousandsForVisibility(totalFinancing);
		}

		/**
		 * @param totalFinancing the totalFinancing to set
		 */
		public void setTotalFinancing(BigDecimal totalFinancing) {
			this.totalFinancing = FeaturesUtil.applyThousandsForEntry(totalFinancing);
		}

		/**
		 * @return the externalFinancing
		 */
		public BigDecimal getExternalFinancing() {
			return FeaturesUtil.applyThousandsForVisibility(externalFinancing);
		}

		/**
		 * @param externalFinancing the externalFinancing to set
		 */
		public void setExternalFinancing(BigDecimal externalFinancing) {
			this.externalFinancing = FeaturesUtil.applyThousandsForEntry(externalFinancing);
		}

		/**
		 * @return the internalFinancing
		 */
		public BigDecimal getInternalFinancing() {
			return FeaturesUtil.applyThousandsForVisibility(internalFinancing);
		}

		/**
		 * @param internalFinancing the internalFinancing to set
		 */
		public void setInternalFinancing(BigDecimal internalFinancing) {
			this.internalFinancing = FeaturesUtil.applyThousandsForEntry(internalFinancing);
		}

		public String getBudgetProgramCode() {
			return budgetProgramCode;
		}

		public void setBudgetProgramCode(String budgetProgramCode) {
			this.budgetProgramCode = budgetProgramCode;
		}

}
