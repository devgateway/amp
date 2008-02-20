package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpSISINProyect implements Serializable{	
	private Long ampActivityId;
	private Long componentId;
	private String sisincode;
	private String localization;
	private String sisinsector;
	private String financingsource;
	private String agencysource;
	private String stage;
	private String programcode;

	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}

	public Long getComponentId() {
		return componentId;
	}

	public void setAmpActivityId(Long ampActivityId) {
		this.ampActivityId = ampActivityId;
	}

	public Long getAmpActivityId() {
		return ampActivityId;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AmpSISINProyect))
			throw new ClassCastException();
		AmpSISINProyect sisinProyect = (AmpSISINProyect) obj;
		if (ampActivityId == null || componentId == null
				|| sisinProyect.getAmpActivityId() == null
				|| sisinProyect.getComponentId() == null)
			return false;
		
		return ampActivityId.equals(sisinProyect.getAmpActivityId())
				&& componentId.equals(sisinProyect.getComponentId());
	}
	
	@Override
	public int hashCode() {
		if(ampActivityId != null )
			return ampActivityId.hashCode();
		if(componentId != null )
			return componentId.hashCode();
			
		return 0;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}

	public String getLocalization() {
		return localization;
	}

	public void setFinancingsource(String financingsource) {
		this.financingsource = financingsource;
	}

	public String getFinancingsource() {
		return financingsource;
	}

	public void setAgencysource(String agencysource) {
		this.agencysource = agencysource;
	}

	public String getAgencysource() {
		return agencysource;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getStage() {
		return stage;
	}

	public void setProgramcode(String programcode) {
		this.programcode = programcode;
	}

	public String getProgramcode() {
		return programcode;
	}

	public void setSisincode(String sisincode) {
		this.sisincode = sisincode;
	}

	public String getSisincode() {
		return sisincode;
	}

	public void setSisinsector(String sisinsector) {
		this.sisinsector = sisinsector;
	}

	public String getSisinsector() {
		return sisinsector;
	}
}
