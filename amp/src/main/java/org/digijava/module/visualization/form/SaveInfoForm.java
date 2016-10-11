package org.digijava.module.visualization.form;

import org.apache.struts.action.ActionForm;

public class SaveInfoForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String background;
	private String description;
	private String keyAreas;
	private String type;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeyAreas() {
		return keyAreas;
	}
	public void setKeyAreas(String keyAreas) {
		this.keyAreas = keyAreas;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
