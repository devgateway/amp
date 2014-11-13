package org.digijava.module.aim.dbentity;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;


public class AmpIndicatorLayer implements Serializable, Comparable <AmpIndicatorLayer> {
	
	private Long id;
	private String name;
	private String description;
	private Set <AmpIndicatorColor> colorRamp;
	private Long numberOfClasses;
	private AmpCategoryValue admLevel;
	private Set <AmpLocationIndicatorValue> indicatorValues;
	
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


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
	
	@Override
	public int compareTo(AmpIndicatorLayer o) {
		return id.compareTo(o.getId());
	}


	public AmpCategoryValue getAdmLevel() {
		return admLevel;
	}


	public void setAdmLevel(AmpCategoryValue admLevel) {
		this.admLevel = admLevel;
	}


	public Set<AmpIndicatorColor> getColorRamp() {
		return colorRamp;
	}


	public void setColorRamp(Set<AmpIndicatorColor> colorRamp) {
		this.colorRamp = colorRamp;
	}


	public Set<AmpLocationIndicatorValue> getIndicatorValues() {
		return indicatorValues;
	}


	public void setIndicatorValues(Set<AmpLocationIndicatorValue> indicatorValues) {
		this.indicatorValues = indicatorValues;
	}

	
	
	

}
