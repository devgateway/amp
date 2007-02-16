package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public class AmpTheme implements Serializable
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
	private Collection activityPrograms;
			
	public Collection getActivityPrograms() {
		return activityPrograms;
	}

	public void setActivityPrograms(Collection activityPrograms) {
		this.activityPrograms = activityPrograms;
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
	public int getIndlevel() {
		return indlevel;
	}

	/**
	 * @param indlevel The indlevel to set.
	 */
	public void setIndlevel(int indlevel) {
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
}
