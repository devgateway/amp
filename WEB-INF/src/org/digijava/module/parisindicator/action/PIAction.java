package org.digijava.module.parisindicator.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

		// SETUP FAKE DATA FOR TESTING PURPOSES.
		Collection donors = new ArrayList();
		Iterator iter = piForm.getDonors().iterator();
		donors.add(iter.next());
		donors.add(iter.next());
		donors.add(iter.next());
		donors.add(iter.next());
		donors.add(iter.next());
		donors.add(iter.next());
		donors.add(iter.next());
		donors.add(iter.next());
		donors.add(iter.next());
		donors.add(iter.next());
		piForm.setSelectedDonors(donors);
		Collection dng = new ArrayList();
		Iterator iter2 = piForm.getDonorGroups().iterator();
		dng.add(iter2.next());
		dng.add(iter2.next());
		dng.add(iter2.next());
		dng.add(iter2.next());
		dng.add(iter2.next());
		dng.add(iter2.next());
		dng.add(iter2.next());
		dng.add(iter2.next());
		piForm.setSelectedDonorGroups(dng);
		piForm.setSelectedCalendar("1");

		// Create report.
		PIAbstractReport report = useCase.createReport(piForm);
		piForm.setMainTableRows(report.getReportRows());

		return mapping.findForward("forward");
	}
}
