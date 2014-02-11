package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpMeasures;

public class ManageGPIForm extends ActionForm {

	private Collection<AmpMeasures> measures;
	private Long indicator5aActualDisbursement;
	private Long indicator5aPlannedDisbursement;
	private Long indicator6ScheduledDisbursements;
	private Long indicator9bDisbursements;

	public ManageGPIForm() {
	}

	public Collection<AmpMeasures> getMeasures() {
		return measures;
	}

	public Long getIndicator5aActualDisbursement() {
		return indicator5aActualDisbursement;
	}

	public void setIndicator5aActualDisbursement(Long indicator5aActualDisbursement) {
		this.indicator5aActualDisbursement = indicator5aActualDisbursement;
	}

	public Long getIndicator5aPlannedDisbursement() {
		return indicator5aPlannedDisbursement;
	}

	public void setIndicator5aPlannedDisbursement(Long indicator5aPlannedDisbursement) {
		this.indicator5aPlannedDisbursement = indicator5aPlannedDisbursement;
	}

	public Long getIndicator6ScheduledDisbursements() {
		return indicator6ScheduledDisbursements;
	}

	public void setIndicator6ScheduledDisbursements(Long indicator6ScheduledDisbursements) {
		this.indicator6ScheduledDisbursements = indicator6ScheduledDisbursements;
	}

	public Long getIndicator9bDisbursements() {
		return indicator9bDisbursements;
	}

	public void setIndicator9bDisbursements(Long indicator9bDisbursements) {
		this.indicator9bDisbursements = indicator9bDisbursements;
	}

	public void setMeasures(Collection<AmpMeasures> measures) {
		this.measures = measures;
	}

}
