package org.digijava.module.visualization.dbentity;

import java.io.Serializable;

public class AmpGraph implements Serializable {
	
	private Long id;
	private String name;
	private String containerId;
	private String extractMethod;
	private boolean barGraphEnabled;
	private boolean barProfileGraphEnabled;
	private boolean barGrowthGraphEnabled;
	private boolean lineGraphEnabled;
	private boolean pieGraphEnabled;
	private boolean dataListEnabled;
	
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
	public String getContainerId() {
		return containerId;
	}
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}
	public String getExtractMethod() {
		return extractMethod;
	}
	public void setExtractMethod(String extractMethod) {
		this.extractMethod = extractMethod;
	}
	public boolean isBarGraphEnabled() {
		return barGraphEnabled;
	}
	public void setBarGraphEnabled(boolean barGraphEnabled) {
		this.barGraphEnabled = barGraphEnabled;
	}
	public boolean isBarProfileGraphEnabled() {
		return barProfileGraphEnabled;
	}
	public void setBarProfileGraphEnabled(boolean barProfileGraphEnabled) {
		this.barProfileGraphEnabled = barProfileGraphEnabled;
	}
	public boolean isBarGrowthGraphEnabled() {
		return barGrowthGraphEnabled;
	}
	public void setBarGrowthGraphEnabled(boolean barGrowthGraphEnabled) {
		this.barGrowthGraphEnabled = barGrowthGraphEnabled;
	}
	public boolean isLineGraphEnabled() {
		return lineGraphEnabled;
	}
	public void setLineGraphEnabled(boolean lineGraphEnabled) {
		this.lineGraphEnabled = lineGraphEnabled;
	}
	public boolean isPieGraphEnabled() {
		return pieGraphEnabled;
	}
	public void setPieGraphEnabled(boolean pieGraphEnabled) {
		this.pieGraphEnabled = pieGraphEnabled;
	}
	public boolean isDataListEnabled() {
		return dataListEnabled;
	}
	public void setDataListEnabled(boolean dataListEnabled) {
		this.dataListEnabled = dataListEnabled;
	}
	
}
