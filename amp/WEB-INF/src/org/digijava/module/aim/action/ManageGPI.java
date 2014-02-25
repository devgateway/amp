package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.GPISetup;
import org.digijava.module.aim.form.ManageGPIForm;
import org.digijava.module.aim.form.ViewAhSurveisForm;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.GPISetupUtil;

public class ManageGPI extends DispatchAction {

	public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

		ManageGPIForm gpiForm = (ManageGPIForm) form;

		Map<String, String> measures = GPISetup.getListOfValues();
		gpiForm.setMeasures(measures);

		// If there is a previous setup then populate the values in the form.
		// Notice the table amp_gpi_setup should never have more than one
		// record!!! (at least for now).
		GPISetup setup = GPISetupUtil.getSetup();
		if (setup != null) {
			gpiForm.setIndicator5aActualDisbursement(setup.getIndicator5aActualDisbursement());
			gpiForm.setIndicator5aPlannedDisbursement(setup.getIndicator5aPlannedDisbursement());
			gpiForm.setIndicator6ScheduledDisbursements(setup.getIndicator6ScheduledDisbursements());
			gpiForm.setIndicator9bDisbursements(setup.getIndicator9bDisbursements());
		}

		return mapping.findForward("forward");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		ManageGPIForm gpiForm = (ManageGPIForm) form;
		GPISetup setup = GPISetupUtil.getSetup();
		if (setup == null) {
			setup = new GPISetup();
		}

		if (!"".equals(gpiForm.getIndicator5aActualDisbursement())) {
			setup.setIndicator5aActualDisbursement(gpiForm.getIndicator5aActualDisbursement());
		}
		if (!"".equals(gpiForm.getIndicator5aPlannedDisbursement())) {
			setup.setIndicator5aPlannedDisbursement(gpiForm.getIndicator5aPlannedDisbursement());
		}
		if (!"".equals(gpiForm.getIndicator6ScheduledDisbursements())) {
			setup.setIndicator6ScheduledDisbursements(gpiForm.getIndicator6ScheduledDisbursements());
		}
		if (!"".equals(gpiForm.getIndicator9bDisbursements())) {
			setup.setIndicator9bDisbursements(gpiForm.getIndicator9bDisbursements());
		}
		GPISetupUtil.saveGPISetup(setup);
		return mapping.findForward("save");
	}
}
