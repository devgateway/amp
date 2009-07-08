package org.digijava.module.parisindicator.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.parisindicator.form.PIForm;
import org.digijava.module.parisindicator.helper.PIAbstractReport;
import org.digijava.module.parisindicator.model.PIUseCase;

public class PIAction extends Action {

	private static Logger logger = Logger.getLogger(PIAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {

		logger.debug("PIAction begin");
		PIForm piForm = (PIForm) form;
		PIUseCase useCase = new PIUseCase();

		// Reformat some data.
		if (piForm.getSelectedDonors() != null && piForm.getSelectedDonors().length > 0
				&& !piForm.getSelectedDonors()[0].isEmpty()) {
			piForm.setSelectedDonors(piForm.getSelectedDonors()[0].split(","));
		} else {
			piForm.setSelectedDonors(null);
		}
		if (piForm.getSelectedDonorGroups() != null && piForm.getSelectedDonorGroups().length > 0
				&& !piForm.getSelectedDonorGroups()[0].isEmpty()) {
			piForm.setSelectedDonorGroups(piForm.getSelectedDonorGroups()[0].split(","));
		} else {
			piForm.setSelectedDonorGroups(null);
		}
		if (piForm.getSelectedStatuses() != null && piForm.getSelectedStatuses().length > 0
				&& !piForm.getSelectedStatuses()[0].isEmpty()) {
			piForm.setSelectedStatuses(piForm.getSelectedStatuses()[0].split(","));
		} else {
			piForm.setSelectedStatuses(null);
		}
		if (piForm.getSelectedSectors() != null && piForm.getSelectedSectors().length > 0
				&& !piForm.getSelectedSectors()[0].isEmpty()) {
			piForm.setSelectedSectors(piForm.getSelectedSectors()[0].split(","));
		} else {
			piForm.setSelectedSectors(null);
		}

		// Setup filters.
		useCase.setupFiltersData(piForm, request);
		if (piForm.isReset()) {
			useCase.resetFilterSelections(piForm, ((TeamMember) request.getSession().getAttribute("currentMember"))
					.getAppSettings());
		}

		// Setup common data.
		piForm.setAvailablePIReports(useCase.setupAvailablePIReports());
		String piReportCode = request.getParameter("reportId");
		piForm.setPiReport(useCase.getPIReport(piReportCode));

		// Create report.
		PIAbstractReport report = useCase.createReport(piForm);
		piForm.setMainTableRows(report.getReportRows());
		piForm.setMiniTable(report.getMiniTable());

		return mapping.findForward("forward");
	}
}
