package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpIndicatorColor;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;

public class AddIndicatorLayerForm extends ActionForm  {
	
	private Long indicatorLayerId;
	private String name;
	private String description;
	private Set <AmpIndicatorColor> colorRamp;
	private Long numberOfClasses;
	private Long admLevelId;
	private String event;
	private Collection<AmpCategoryValue> admLevelList;
	private Integer selectedColorRamp;
	private  List <AmpIndicatorLayer> indicatorLayers;
	private Long idOfIndicator = null;
	private Integer selectedColorRampIndex;
	private String unit;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getNumberOfClasses() {
		return numberOfClasses;
	}
	public void setNumberOfClasses(Long numberOfClasses) {
		this.numberOfClasses = numberOfClasses;
	}
	
	public Long getIndicatorLayerId() {
		return indicatorLayerId;
	}
	public void setIndicatorLayerId(Long indicatorLayerId) {
		this.indicatorLayerId = indicatorLayerId;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Collection<AmpCategoryValue> getAdmLevelList() {
		return admLevelList;
	}
	public void setAdmLevelList(Collection<AmpCategoryValue> admLevelList) {
		this.admLevelList = admLevelList;
	}
	public Long getAdmLevelId() {
		return admLevelId;
	}
	public void setAdmLevelId(Long admLevelId) {
		this.admLevelId = admLevelId;
	}
	public Set<AmpIndicatorColor> getColorRamp() {
		return colorRamp;
	}
	public void setColorRamp(Set<AmpIndicatorColor> colorRamp) {
		this.colorRamp = colorRamp;
	}
	public Integer getSelectedColorRamp() {
		return selectedColorRamp;
	}
	public void setSelectedColorRamp(Integer selectedColorRamp) {
		this.selectedColorRamp = selectedColorRamp;
	}
	public List <AmpIndicatorLayer> getIndicatorLayers() {
		return indicatorLayers;
	}
	public void setIndicatorLayers(List <AmpIndicatorLayer> indicatorLayers) {
		this.indicatorLayers = indicatorLayers;
	}
	public Long getIdOfIndicator() {
		return idOfIndicator;
	}
	public void setIdOfIndicator(Long idOfIndicator) {
		this.idOfIndicator = idOfIndicator;
	}
	public Integer getSelectedColorRampIndex() {
		return selectedColorRampIndex;
	}
	public void setSelectedColorRampIndex(Integer selectedColorRampIndex) {
		this.selectedColorRampIndex = selectedColorRampIndex;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	

}
