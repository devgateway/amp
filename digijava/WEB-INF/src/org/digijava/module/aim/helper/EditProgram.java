package org.digijava.module.aim.helper ;

import java.util.Set;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTheme;

public class EditProgram
{
	private AmpActivity activityId;
    private Long ampThemeId ;
	private AmpTheme parentThemeId ;
	private String themeCode ;
	private String name ;
	private String type ;
	private int indlevel;
	private String description ;
	private String language ;
	private String version ;
	private Set indicators;
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
	

}