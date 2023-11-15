package org.digijava.module.parisindicator.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.parisindicator.form.PIForm;
import org.digijava.module.parisindicator.helper.PIAbstractReport;
import org.digijava.module.parisindicator.model.PIExportUseCase;
import org.digijava.module.parisindicator.model.PIUseCase;

import javax.servlet.ServletContext;

public class PIAction extends Action {

    private static Logger logger = Logger.getLogger(PIAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {

        logger.debug("PIAction begin");
        PIForm piForm = (PIForm) form;
        PIUseCase useCase = new PIUseCase();
        

        // Reformat some data because how the arrays come from the page.
        if (piForm.getSelectedDonors() != null && piForm.getSelectedDonors().length > 0
                && !piForm.getSelectedDonors()[0].isEmpty() && !piForm.getSelectedDonors()[0].equals("0")) {
            piForm.setSelectedDonors(piForm.getSelectedDonors()[0].split(","));
        } else {
            piForm.setSelectedDonors(null);
        }
        if (piForm.getSelectedDonorGroups() != null && piForm.getSelectedDonorGroups().length > 0
                && !piForm.getSelectedDonorGroups()[0].isEmpty() && !piForm.getSelectedDonorGroups()[0].equals("0")) {
            piForm.setSelectedDonorGroups(piForm.getSelectedDonorGroups()[0].split(","));
        } else {
            piForm.setSelectedDonorGroups(null);
        }
        if (piForm.getSelectedStatuses() != null && piForm.getSelectedStatuses().length > 0
                && !piForm.getSelectedStatuses()[0].isEmpty() && !piForm.getSelectedStatuses()[0].equals("0")) {
            piForm.setSelectedStatuses(piForm.getSelectedStatuses()[0].split(","));
        } else {
            piForm.setSelectedStatuses(null);
        }
        if (piForm.getSelectedSectors() != null && piForm.getSelectedSectors().length > 0
                && !piForm.getSelectedSectors()[0].isEmpty() && !piForm.getSelectedSectors()[0].equals("0")) {
            piForm.setSelectedSectors(piForm.getSelectedSectors()[0].split(","));
        } else {
            piForm.setSelectedSectors(null);
        }
        if (piForm.getSelectedFinancingIstruments() != null && piForm.getSelectedFinancingIstruments().length > 0
                && !piForm.getSelectedFinancingIstruments()[0].isEmpty()
                && !piForm.getSelectedFinancingIstruments()[0].equals("0")) {
            piForm.setSelectedFinancingIstruments(piForm.getSelectedFinancingIstruments()[0].split(","));
        } else {
            piForm.setSelectedFinancingIstruments(null);
        }

        // Setup filters.
        ServletContext ampContext = getServlet().getServletContext();
        useCase.setupFiltersData(piForm, request,ampContext);
        if (piForm.isReset()) {
            useCase.resetFilterSelections(piForm, ((TeamMember) request.getSession().getAttribute("currentMember"))
                    .getAppSettings());
        }

        // Setup common data.
        piForm.setAvailablePIReports(useCase.setupAvailablePIReports());
        String piReportCode = request.getParameter("reportId");
        piForm.setPiReport(useCase.getPIReport(piReportCode));

        // Create report.
        if(piForm.getPiReport()==null){
            return mapping.findForward("forward");
        }
        // Create report.
        PIAbstractReport report = useCase.createReport(piForm, request);
        piForm.setMainTableRows(report.getReportRows());
        piForm.setMiniTable(report.getMiniTable());

        // Set output.
        if (piForm.isPrintPreview()) {
            piForm.setPrintPreview(false);
            return mapping.findForward("print");
        } else if (piForm.isExportPDF() || piForm.isExportXLS()) {
            PIExportUseCase pdfUseCase = new PIExportUseCase();
            pdfUseCase.exportReport(getServlet(), response, request, piForm.getPiReport().getIndicatorCode(), piForm
                    .getMainTableRows(), piForm.getMiniTable(), piForm.getStartYear(), piForm.getEndYear(), (piForm
                    .isExportPDF()) ? "PDF" : "XLS", piForm.getSelectedCurrency());
            piForm.setExportPDF(false);
            piForm.setExportXLS(false);
            return null;
        } else {
            return mapping.findForward("forward");
        }
    }
}
