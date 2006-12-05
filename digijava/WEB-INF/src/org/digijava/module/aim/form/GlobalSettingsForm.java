package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.*;
public class GlobalSettingsForm extends ActionForm {
	Collection gsfCol = null;
	Collection countryNameCol = null;
	String countryName = null;
	Long gsfId;
	String gsfName = null;
	String gsfValue = null;
	public Long getGsfId() {
		return gsfId;
	}
	public void setGsfId(Long gsfId) {
		this.gsfId = gsfId;
	}
	public String getGsfName() {
		return gsfName;
	}
	public void setGsfName(String gsfName) {
		this.gsfName = gsfName;
	}
	public String getGsfValue() {
		return gsfValue;
	}
	public void setGsfValue(String gsfValue) {
		this.gsfValue = gsfValue;
	}
	public Collection getGsfCol() {
		return gsfCol;
	}
	public void setGsfCol(Collection gsfCol) {
		this.gsfCol = gsfCol;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Collection getCountryNameCol() {
		return countryNameCol;
	}
	public void setCountryNameCol(Collection countryNameCol) {
		this.countryNameCol = countryNameCol;
	}
}