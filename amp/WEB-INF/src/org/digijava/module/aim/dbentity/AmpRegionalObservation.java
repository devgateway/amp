package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class AmpRegionalObservation implements Serializable {

	private Long ampRegionalObservationId;
	private String name;
	private AmpActivity activity;
	private Set<AmpRegionalObservationMeasure> regionalObservationMeasures;
	private Date observationDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AmpActivity getActivity() {
		return activity;
	}

	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}

	public boolean equals(Object arg) {
		if (arg instanceof AmpRegionalObservation) {
			AmpRegionalObservation issue = (AmpRegionalObservation) arg;
			return issue.getAmpRegionalObservationId().equals(ampRegionalObservationId);
		}
		throw new ClassCastException();
	}

	public Long getAmpRegionalObservationId() {
		return ampRegionalObservationId;
	}

	public void setAmpRegionalObservationId(Long ampRegionalObservationId) {
		this.ampRegionalObservationId = ampRegionalObservationId;
	}

	public Set getRegionalObservationMeasures() {
		return regionalObservationMeasures;
	}

	public void setRegionalObservationMeasures(Set regionalObservationMeasures) {
		this.regionalObservationMeasures = regionalObservationMeasures;
	}

	public Date getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(Date observationDate) {
		this.observationDate = observationDate;
	}
}