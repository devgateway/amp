package org.digijava.module.aim.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class NpdSettingsForm extends ActionForm  {
	private Integer width;
	private Integer height;
	private String angle;
	private Long ampTeamId;
	
	public void setAngle(String angle) {
		this.angle = angle;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public String getAngle() {
		return angle;
	}
	public Integer getHeight() {
		return height;
	}
	public Integer getWidth() {
		return width;
	}
	public Long getAmpTeamId() {
		return ampTeamId;
	}
	public void setAmpTeamId(Long ampTeamId) {
		this.ampTeamId = ampTeamId;
	}
}
