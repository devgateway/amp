package org.digijava.module.aim.dbentity;

import java.util.Date;
import java.util.Set;

public class AmpMEIndicators {
	private Long ampMEIndId;
	private String code;
	private String name;
	private String type;
	private String description;
	private boolean defaultInd;
	private boolean ascendingInd;
	private int valueType;
	private Date creationDate;
	private int indicatorCategory;
	private boolean npIndicator;
	private Set themes;
	
	
	/**
	 * @return Returns the ampMEIndId.
	 */
	public Long getAmpMEIndId() {
		return ampMEIndId;
	}
	/**
	 * @param ampMEIndId The ampMEIndId to set.
	 */
	public void setAmpMEIndId(Long ampMEIndId) {
		this.ampMEIndId = ampMEIndId;
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
	 * @return Returns the defaultInd.
	 */
	public boolean isDefaultInd() {
		return defaultInd;
	}
	/**
	 * @param defaultInd The defaultInd to set.
	 */
	public void setDefaultInd(boolean defaultInd) {
		this.defaultInd = defaultInd;
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
	 * @return Returns the ascendingInd.
	 */
	public boolean isAscendingInd() {
		return ascendingInd;
	}
	/**
	 * @param ascendingInd The ascendingInd to set.
	 */
	public void setAscendingInd(boolean ascendingInd) {
		this.ascendingInd = ascendingInd;
	}
	/**
	 * @return Returns the creationDate.
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate The creationDate to set.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return Returns the indicatorCategory.
	 */
	public int getIndicatorCategory() {
		return indicatorCategory;
	}
	/**
	 * @param indicatorCategory The indicatorCategory to set.
	 */
	public void setIndicatorCategory(int indicatorCategory) {
		this.indicatorCategory = indicatorCategory;
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
	 * @return Returns the valueType.
	 */
	public int getValueType() {
		return valueType;
	}
	/**
	 * @param valueType The valueType to set.
	 */
	public void setValueType(int valueType) {
		this.valueType = valueType;
	}
	/**
	 * @return Returns the themes.
	 */
	public Set getThemes() {
		return themes;
	}
	/**
	 * @param themes The themes to set.
	 */
	public void setThemes(Set themes) {
		this.themes = themes;
	}
}