package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.lang.Comparable;
import java.util.Date;
import java.util.Set;


public class AmpIndicator implements Serializable
{
	private Long indicatorId;
	private boolean defaultInd;
	private String name;
	private String code;
	private String type;
	private Date creationDate;
	private int category;
	private String description;
	private Set themes;
	private Set sectors;
	private Set activity;
	
	
	private Float baseVal;  // BASE
	private Date baseValDate;
	private String baseValComments;
	private Float actualVal;  // ACTUAL
	private Date actualValDate;
	private String actualValComments;
	private Float targetVal; // TARGET
	private Date targetValDate;
	private String targetValComments;
	private Float revisedTargetVal; // Revised TARGET
	private String revisedTargetValComments;
	private Date revisedTargetValDate;
	private String comments;
	
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
	public Set getThemes() {
		return themes;
	}
	public void setThemes(Set themes) {
		this.themes = themes;
	}
	public Set getSectors() {
		return sectors;
	}
	public void setSectors(Set sectors) {
		this.sectors = sectors;
	}
	public Set getActivity() {
		return activity;
	}
	public void setActivity(Set activity) {
		this.activity = activity;
	}
	public boolean isDefaultInd() {
		return defaultInd;
	}
	public void setDefaultInd(boolean defaultInd) {
		this.defaultInd = defaultInd;
	}
	public Float getBaseVal() {
		return baseVal;
	}
	public void setBaseVal(Float baseVal) {
		this.baseVal = baseVal;
	}
	public Date getBaseValDate() {
		return baseValDate;
	}
	public void setBaseValDate(Date baseValDate) {
		this.baseValDate = baseValDate;
	}
	public String getBaseValComments() {
		return baseValComments;
	}
	public void setBaseValComments(String baseValComments) {
		this.baseValComments = baseValComments;
	}
	public Float getActualVal() {
		return actualVal;
	}
	public void setActualVal(Float actualVal) {
		this.actualVal = actualVal;
	}
	public Date getActualValDate() {
		return actualValDate;
	}
	public void setActualValDate(Date actualValDate) {
		this.actualValDate = actualValDate;
	}
	public String getActualValComments() {
		return actualValComments;
	}
	public void setActualValComments(String actualValComments) {
		this.actualValComments = actualValComments;
	}
	public Float getTargetVal() {
		return targetVal;
	}
	public void setTargetVal(Float targetVal) {
		this.targetVal = targetVal;
	}
	public Date getTargetValDate() {
		return targetValDate;
	}
	public void setTargetValDate(Date targetValDate) {
		this.targetValDate = targetValDate;
	}
	public String getTargetValComments() {
		return targetValComments;
	}
	public void setTargetValComments(String targetValComments) {
		this.targetValComments = targetValComments;
	}
	public String getRevisedTargetValComments() {
		return revisedTargetValComments;
	}
	public void setRevisedTargetValComments(String revisedTargetValComments) {
		this.revisedTargetValComments = revisedTargetValComments;
	}
	public Date getRevisedTargetValDate() {
		return revisedTargetValDate;
	}
	public void setRevisedTargetValDate(Date revisedTargetValDate) {
		this.revisedTargetValDate = revisedTargetValDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Float getRevisedTargetVal() {
		return revisedTargetVal;
	}
	public void setRevisedTargetVal(Float revisedTargetVal) {
		this.revisedTargetVal = revisedTargetVal;
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
	
	
	
	
}