package org.digijava.module.aim.dbentity;

public class AmpFeature {
	
	private Integer ampFeatureId;
	private String name;
	private String description;
	private String code;
	private boolean active;
	
	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * @param active The active to set.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * @return Returns the ampFeatureId.
	 */
	public Integer getAmpFeatureId() {
		return ampFeatureId;
	}
	/**
	 * @param ampFeatureId The ampFeatureId to set.
	 */
	public void setAmpFeatureId(Integer ampFeatureId) {
		this.ampFeatureId = ampFeatureId;
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
	
	
}