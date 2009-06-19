package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

/**
 * NPD settings in admin menu.
 * @author Dare
 *
 */
public class NpdSettingsForm extends ActionForm  {
	private static final long serialVersionUID = 1L;
	private Integer width;
	private Integer height;
	private String angle;
	private Long ampTeamId;
	private Integer pageSize;
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
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
