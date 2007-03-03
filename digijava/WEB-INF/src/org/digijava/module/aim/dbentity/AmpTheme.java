package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AmpTheme implements Serializable
{
	private AmpActivity activityId;
    private Long ampThemeId ;
	private AmpTheme parentThemeId ;
	private String themeCode ;
	private String name ;
	private String type ;
	private Integer indlevel;
	private String description ;
	private String language ;
	private String version ;
	private Set indicators;
	private Set activities=new HashSet();
	
	private String leadAgency;
	private String targetGroups;
	private String background;
	private String objectives;
	private String outputs;
	private String beneficiaries;
	private String environmentConsiderations;
			
	public Set getActivities() {
		return activities;
	}

	public void setActivities(Set activities) {
		this.activities = activities;
	}

	public AmpTheme()
	{
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
	public String getType() {
		return type;
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
	public void setType(String string) {
		type = string;
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
	public Set getIndicators() {
		return indicators;
	}

    public AmpActivity getActivityId() {
        return activityId;
    }

    /**
	 * @param indicators The indicators to set.
	 */
	public void setIndicators(Set indicators) {
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
    
    
}
