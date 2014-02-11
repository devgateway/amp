package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.annotations.reports.Identificator;

public class GPISetup implements Serializable, Comparable {

	@Identificator
	private Long gpiSetupId;
	private AmpMeasures indicator5aActualDisbursement;
	private AmpMeasures indicator5aPlannedDisbursement;
	private AmpMeasures indicator6ScheduledDisbursements;
	private AmpMeasures indicator9bDisbursements;

	public Long getGpiSetupId() {
		return gpiSetupId;
	}

	public void setGpiSetupId(Long gpiSetupId) {
		this.gpiSetupId = gpiSetupId;
	}

	public AmpMeasures getIndicator5aActualDisbursement() {
		return indicator5aActualDisbursement;
	}

	public void setIndicator5aActualDisbursement(AmpMeasures indicator5aActualDisbursement) {
		this.indicator5aActualDisbursement = indicator5aActualDisbursement;
	}

	public AmpMeasures getIndicator5aPlannedDisbursement() {
		return indicator5aPlannedDisbursement;
	}

	public void setIndicator5aPlannedDisbursement(AmpMeasures indicator5aPlannedDisbursement) {
		this.indicator5aPlannedDisbursement = indicator5aPlannedDisbursement;
	}

	public AmpMeasures getIndicator6ScheduledDisbursements() {
		return indicator6ScheduledDisbursements;
	}

	public void setIndicator6ScheduledDisbursements(AmpMeasures indicator6ScheduledDisbursements) {
		this.indicator6ScheduledDisbursements = indicator6ScheduledDisbursements;
	}

	public AmpMeasures getIndicator9bDisbursements() {
		return indicator9bDisbursements;
	}

	public void setIndicator9bDisbursements(AmpMeasures indicator9bDisbursements) {
		this.indicator9bDisbursements = indicator9bDisbursements;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
