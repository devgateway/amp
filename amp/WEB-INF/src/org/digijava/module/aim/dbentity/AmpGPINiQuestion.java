package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

public class AmpGPINiQuestion implements Serializable{
	private static final long serialVersionUID = 6751072241262868712L;
	private Long ampGPINiQuestionId;
	private AmpGPINiIndicator ampGPINiIndicatorId;
	private String code;
	private String description;
	private String type;
	private Integer index;
	private Boolean allowMutiple;
	private Boolean requiresDataEntry;
	private Set<AmpGPINiQuestionOption> options;
	
	public Long getAmpGPINiQuestionId() {
		return ampGPINiQuestionId;
	}
	public void setAmpGPINiQuestionId(Long ampGPINiQuestionId) {
		this.ampGPINiQuestionId = ampGPINiQuestionId;
	}
	public AmpGPINiIndicator getAmpGPINiIndicatorId() {
		return ampGPINiIndicatorId;
	}
	public void setAmpGPINiIndicatorId(AmpGPINiIndicator ampGPINiIndicatorId) {
		this.ampGPINiIndicatorId = ampGPINiIndicatorId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Boolean getAllowMutiple() {
		return allowMutiple;
	}
	public void setAllowMutiple(Boolean allowMutiple) {
		this.allowMutiple = allowMutiple;
	}
	public Boolean getRequiresDataEntry() {
		return requiresDataEntry;
	}
	public void setRequiresDataEntry(Boolean requiresDataEntry) {
		this.requiresDataEntry = requiresDataEntry;
	}
	public Set<AmpGPINiQuestionOption> getOptions() {
		return options;
	}
	public void setOptions(Set<AmpGPINiQuestionOption> options) {
		this.options = options;
	}
	
}
