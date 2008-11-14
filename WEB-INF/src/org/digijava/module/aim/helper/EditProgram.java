package org.digijava.module.aim.helper ;

import java.util.Set;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class EditProgram
{
	private AmpActivity activityId;
    private Long ampThemeId ;
	private AmpTheme parentThemeId ;
	private String themeCode ;
	private String name ;
	//private String type ;
	private AmpCategoryValue typeCategVal;
	
	private int indlevel;
	private String description ;
	private String language ;
	private String version ;
	private Set indicators;
	
	private String leadAgency;
	private String targetGroups;
	private String background;
	private String objectives;
	private String outputs;
	private String beneficiaries;
	private String environmentConsiderations;
	
	private Double externalFinancing;
	private Double internalFinancing;
	private Double totalFinancing;

	/**
	 * @return Returns the activityId.
	 */
	public AmpActivity getActivityId() {
		return activityId;
	}
	/**
	 * @param activityId The activityId to set.
	 */
	public void setActivityId(AmpActivity activityId) {
		this.activityId = activityId;
	}
	/**
	 * @return Returns the ampThemeId.
	 */
	public Long getAmpThemeId() {
		return ampThemeId;
	}
	/**
	 * @param ampThemeId The ampThemeId to set.
	 */
	public void setAmpThemeId(Long ampThemeId) {
		this.ampThemeId = ampThemeId;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the indicators.
	 */
	public Set getIndicators() {
		return indicators;
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
	public int getIndlevel() {
		return indlevel;
	}
	/**
	 * @param indlevel The indlevel to set.
	 */
	public void setIndlevel(int indlevel) {
		this.indlevel = indlevel;
	}
	/**
	 * @return Returns the language.
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language The language to set.
	 */
	public void setLanguage(String language) {
		this.language = language;
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
	 * @return Returns the parentThemeId.
	 */
	public AmpTheme getParentThemeId() {
		return parentThemeId;
	}
	/**
	 * @param parentThemeId The parentThemeId to set.
	 */
	public void setParentThemeId(AmpTheme parentThemeId) {
		this.parentThemeId = parentThemeId;
	}
	/**
	 * @return Returns the themeCode.
	 */
	public String getThemeCode() {
		return themeCode;
	}
	/**
	 * @param themeCode The themeCode to set.
	 */
	public void setThemeCode(String themeCode) {
		this.themeCode = themeCode;
	}
	
	
	
	public AmpCategoryValue getTypeCategVal() {
		return typeCategVal;
	}
	public void setTypeCategVal(AmpCategoryValue typeCategVal) {
		this.typeCategVal = typeCategVal;
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
	/**
	 * @return the externalFinancing
	 */
	public Double getExternalFinancing() {
		return externalFinancing;
	}
	/**
	 * @param externalFinancing the externalFinancing to set
	 */
	public void setExternalFinancing(Double externalFinancing) {
		this.externalFinancing = externalFinancing;
	}
	/**
	 * @return the internalFinancing
	 */
	public Double getInternalFinancing() {
		return internalFinancing;
	}
	/**
	 * @param internalFinancing the internalFinancing to set
	 */
	public void setInternalFinancing(Double internalFinancing) {
		this.internalFinancing = internalFinancing;
	}
	/**
	 * @return the totalFinancing
	 */
	public Double getTotalFinancing() {
		return totalFinancing;
	}
	/**
	 * @param totalFinancing the totalFinancing to set
	 */
	public void setTotalFinancing(Double totalFinancing) {
		this.totalFinancing = totalFinancing;
	}
	
}