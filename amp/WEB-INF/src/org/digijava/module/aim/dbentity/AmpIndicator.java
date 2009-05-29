package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpIndicator implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long indicatorId;
	private boolean defaultInd;
	private String name;
	private String code;
	private String type;
	private Date creationDate;
	private int category;
	private String description;
	private Set sectors;
	private String comments;
        private String unit;
	/**
	 * Indicator connections with activities.
	 * Elements in this set contains activity and values assigned to this activity-indicator connections.
	 * Please look carefully in IndicatorConnection.hbm.xml before changing anything, because {@link IndicatorActivity} is subclass of {@link IndicatorConnection}
	 * @see IndicatorConnection
	 */
	private Set<IndicatorActivity> valuesActivity;

	/**
	 * Indicator connections with themes.
	 * Elements in this set contains theme and values assigned to this theme-indicator connections.
	 * Please look carefully in IndicatorConnection.hbm.xml before changing anything, because {@link IndicatorTheme} is subclass of {@link IndicatorConnection}
	 * @see IndicatorConnection
	 */
	private Set<IndicatorTheme> valuesTheme;


	private AmpCategoryValue indicatorsCategory;

	private AmpIndicatorRiskRatings risk;


	public Long getIndicatorId() {
		return indicatorId;
	}
	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set getSectors() {
		return sectors;
	}
	public void setSectors(Set sectors) {
		this.sectors = sectors;
	}
	public boolean isDefaultInd() {
		return defaultInd;
	}
	public void setDefaultInd(boolean defaultInd) {
		this.defaultInd = defaultInd;
	}

	public AmpCategoryValue getIndicatorsCategory() {
		return indicatorsCategory;
	}
	public void setIndicatorsCategory(AmpCategoryValue indicatorsCategory) {
		this.indicatorsCategory = indicatorsCategory;
	}
	public AmpIndicatorRiskRatings getRisk() {
		return risk;
	}
	public void setRisk(AmpIndicatorRiskRatings risk) {
		this.risk = risk;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Set<IndicatorActivity> getValuesActivity() {
		return valuesActivity;
	}
	public void setValuesActivity(Set<IndicatorActivity> valuesActivity) {
		this.valuesActivity = valuesActivity;
	}
	public Set<IndicatorTheme> getValuesTheme() {
		return valuesTheme;
	}

    public String getUnit() {
        return unit;
    }

    public void setValuesTheme(Set<IndicatorTheme> valuesTheme) {
		this.valuesTheme = valuesTheme;
	}

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
