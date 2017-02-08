package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpGPINiQuestionOption implements Serializable {
	private static final long serialVersionUID = 3456453082099936257L;
	private Long ampGPINiQuestionOptionId;
	private AmpGPINiQuestion ampGPINiQuestionId;
	private String code;
	private String description;
	
	public Long getAmpGPINiQuestionOptionId() {
		return ampGPINiQuestionOptionId;
	}
	public void setAmpGPINiQuestionOptionId(Long ampGPINiQuestionOptionId) {
		this.ampGPINiQuestionOptionId = ampGPINiQuestionOptionId;
	}
	public AmpGPINiQuestion getAmpGPINiQuestionId() {
		return ampGPINiQuestionId;
	}
	public void setAmpGPINiQuestionId(AmpGPINiQuestion ampGPINiQuestionId) {
		this.ampGPINiQuestionId = ampGPINiQuestionId;
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
	
}
