package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Set;

public class AmpRegionalObservationMeasure implements Serializable {
	private Long ampRegionalObservationMeasureId;
	private String name;
	private AmpRegionalObservation regionalObservation;
	private Set<AmpRegionalObservationActor> actors;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getActors() {
		return actors;
	}

	public void setActors(Set actors) {
		this.actors = actors;
	}

	public boolean equals(Object arg) {
		if (arg instanceof AmpRegionalObservationMeasure) {
			AmpRegionalObservationMeasure measure = (AmpRegionalObservationMeasure) arg;
			return measure.getAmpRegionalObservationMeasureId().equals(ampRegionalObservationMeasureId);
		}
		throw new ClassCastException();
	}

	public Long getAmpRegionalObservationMeasureId() {
		return ampRegionalObservationMeasureId;
	}

	public void setAmpRegionalObservationMeasureId(Long ampRegionalObservationMeasureId) {
		this.ampRegionalObservationMeasureId = ampRegionalObservationMeasureId;
	}

	public AmpRegionalObservation getRegionalObservation() {
		return regionalObservation;
	}

	public void setRegionalObservation(AmpRegionalObservation regionalObservation) {
		this.regionalObservation = regionalObservation;
	}
}