package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.*;
public class GlobalSettingsForm extends ActionForm {
	Collection gsfCol = null;
	Collection countryNameCol = null;
	String countryName = null;
	Long globalId;
	String globalSettingsName = null;
	String gsfValue = null;
	public Long getGlobalId() {
		return globalId;
	}
	public void setGlobalId(Long gsfId) {
		this.globalId = gsfId;
	}
	public String getGlobalSettingsName() {
		return globalSettingsName;
	}
	public void setGlobalSettingsName(String gsfName) {
		this.globalSettingsName = gsfName;
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