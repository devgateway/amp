package org.digijava.module.gpi.action;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.gpi.form.GPIForm;
import org.digijava.module.gpi.helper.GPIAbstractReport;
import org.digijava.module.gpi.model.GPIExportUseCase;
import org.digijava.module.gpi.model.GPIUseCase;

public class GPIAction extends Action {

	private static Logger logger = Logger.getLogger(GPIAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {

		logger.debug("GPIAction begin");
		GPIForm gpiForm = (GPIForm) form;
		GPIUseCase useCase = new GPIUseCase();
		
		// Reformat some data because how the arrays come from the page.
		gpiForm = formatFilters(gpiForm);

		// Setup filters.
		ServletContext ampContext = getServlet().getServletContext();
		useCase.setupFiltersData(gpiForm, request, ampContext);
		if (gpiForm.isReset()) {
			useCase.resetFilterSelections(gpiForm, ((TeamMember) request.getSession().getAttribute("currentMember")).getAppSettings());
		}

		// Setup common data.
		gpiForm.setAvailableGPIReports(useCase.setupAvailableGPIReports());
		String piReportCode = request.getParameter("reportId");
		gpiForm.setGPIReport(useCase.getGPIReport(piReportCode));

		// Create report.
		if (gpiForm.getGPIReport() == null) {
			return mapping.findForward("forward");
		}
		// Create report.
		GPIAbstractReport report = useCase.createReport(gpiForm, request);
		gpiForm.setMainTableRows(report.getReportRows());
		gpiForm.setMiniTable(report.getMiniTable());

		// Set output.
		if (gpiForm.isPrintPreview()) {
			gpiForm.setPrintPreview(false);
			return mapping.findForward("print");
		} else if (gpiForm.isExportPDF() || gpiForm.isExportXLS()) {
			GPIExportUseCase pdfUseCase = new GPIExportUseCase();
			pdfUseCase.exportReport(getServlet(), response, request, gpiForm.getGPIReport().getIndicatorCode(), gpiForm.getMainTableRows(), gpiForm.getMiniTable(), gpiForm.getStartYear(),
					gpiForm.getEndYear(), (gpiForm.isExportPDF()) ? "PDF" : "XLS", gpiForm.getSelectedCurrency());
			gpiForm.setExportPDF(false);
			gpiForm.setExportXLS(false);
			return null;
		} else {
			return mapping.findForward("forward");
		}
	}

	private GPIForm formatFilters(GPIForm piForm) {
		// TODO: Create an object that will have all filters.
		if (piForm.getSelectedDonors() != null && piForm.getSelectedDonors().length > 0 && !piForm.getSelectedDonors()[0].isEmpty() && !piForm.getSelectedDonors()[0].equals("0")) {
			piForm.setSelectedDonors(piForm.getSelectedDonors()[0].split(","));
		} else {
			piForm.setSelectedDonors(null);
		}
		if (piForm.getSelectedDonorGroups() != null && piForm.getSelectedDonorGroups().length > 0 && !piForm.getSelectedDonorGroups()[0].isEmpty() && !piForm.getSelectedDonorGroups()[0].equals("0")) {
			piForm.setSelectedDonorGroups(piForm.getSelectedDonorGroups()[0].split(","));
		} else {
			piForm.setSelectedDonorGroups(null);
		}
		if (piForm.getSelectedStatuses() != null && piForm.getSelectedStatuses().length > 0 && !piForm.getSelectedStatuses()[0].isEmpty() && !piForm.getSelectedStatuses()[0].equals("0")) {
			piForm.setSelectedStatuses(piForm.getSelectedStatuses()[0].split(","));
		} else {
			piForm.setSelectedStatuses(null);
		}
		if (piForm.getSelectedSectors() != null && piForm.getSelectedSectors().length > 0 && !piForm.getSelectedSectors()[0].isEmpty() && !piForm.getSelectedSectors()[0].equals("0")) {
			piForm.setSelectedSectors(piForm.getSelectedSectors()[0].split(","));
		} else {
			piForm.setSelectedSectors(null);
		}
		if (piForm.getSelectedFinancingIstruments() != null && piForm.getSelectedFinancingIstruments().length > 0 && !piForm.getSelectedFinancingIstruments()[0].isEmpty()
				&& !piForm.getSelectedFinancingIstruments()[0].equals("0")) {
			piForm.setSelectedFinancingIstruments(piForm.getSelectedFinancingIstruments()[0].split(","));
		} else {
			piForm.setSelectedFinancingIstruments(null);
		}
		return piForm;
	}
}
