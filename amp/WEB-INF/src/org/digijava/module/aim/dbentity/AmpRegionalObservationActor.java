package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpRegionalObservationActor implements Serializable, Cloneable {

	private Long ampRegionalObservationActorId;
	private String name;
	private String nameTrimmed;
	private AmpRegionalObservationMeasure measure;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameTrimmed() {
		return name.replace(" ", "");
	}

	public void setNameTrimmed(String nameTrimmed) {
		this.nameTrimmed = nameTrimmed;
	}

	public Long getAmpRegionalObservationActorId() {
		return ampRegionalObservationActorId;
	}

	public void setAmpRegionalObservationActorId(Long ampRegionalObservationActorId) {
		this.ampRegionalObservationActorId = ampRegionalObservationActorId;
	}

	public AmpRegionalObservationMeasure getMeasure() {
		return measure;
	}

	public void setMeasure(AmpRegionalObservationMeasure measure) {
		this.measure = measure;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}