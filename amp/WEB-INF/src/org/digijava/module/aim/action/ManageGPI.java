package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.GPISetup;
import org.digijava.module.aim.form.ManageGPIForm;
import org.digijava.module.aim.form.ViewAhSurveisForm;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.GPISetupUtil;

public class ManageGPI extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

		// Look for the list of available measures.
		Collection<AmpMeasures> list = AdvancedReportUtil.getMeasureList();
		ManageGPIForm gpiForm = (ManageGPIForm) form;
		gpiForm.setMeasures(new ArrayList<AmpMeasures>());
		for (AmpMeasures am : list) {
			if ("A".equals(am.getType())) {
				gpiForm.getMeasures().add(am);
			}
		}

		// If there is a previous setup then populate the values in the form.
		// Notice the table amp_gpi_setup should never have more than one
		// record!!! (at least for now).
		GPISetup setup = GPISetupUtil.getSetup();
		if (setup != null) {
			gpiForm.setIndicator5aActualDisbursement(setup.getIndicator5aActualDisbursement().getMeasureId());
			gpiForm.setIndicator5aPlannedDisbursement(setup.getIndicator5aPlannedDisbursement().getMeasureId());
			gpiForm.setIndicator6ScheduledDisbursements(setup.getIndicator6ScheduledDisbursements().getMeasureId());
			gpiForm.setIndicator9bDisbursements(setup.getIndicator9bDisbursements().getMeasureId());
		}

		return mapping.findForward("forward");
	}
}
