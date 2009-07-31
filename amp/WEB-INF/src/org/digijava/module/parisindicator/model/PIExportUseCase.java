package org.digijava.module.parisindicator.model;

import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.struts.action.ActionServlet;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.parisindicator.helper.*;
import org.digijava.module.parisindicator.helper.export.PIAbstractExport;
import org.digijava.module.parisindicator.helper.export.PIReport3Export;
import org.digijava.module.parisindicator.util.*;

public class PIExportUseCase {

	public synchronized void createPDFReport(ActionServlet servlet, HttpServletResponse response,
			HttpServletRequest request, String reportCode, Collection<PIReportAbstractRow> mainTableRows,
			int[][] miniTable, int year) throws Exception {

		String reportsFolderPath = servlet.getServletContext().getRealPath(
				"/WEB-INF/classes/org/digijava/module/parisindicator/jasperreports");
		java.io.File reportsFolder = new java.io.File(reportsFolderPath);
		if (!reportsFolder.exists() || !reportsFolder.isDirectory()) {
			reportsFolder.mkdirs();
		}
		reportsFolderPath = reportsFolder.getAbsolutePath().replace("\\", "/");
		String reportName = "ParisIndicator" + reportCode;
		String reportPath = reportsFolderPath + "/" + reportName;
		String realPathJrxml = reportPath + ".jrxml";
		String jasperFile = reportPath + ".jasper";

		PIAbstractExport export = null;
		if (PIConstants.PARIS_INDICATOR_REPORT_3.equalsIgnoreCase(reportCode)) {
			export = new PIReport3Export(RequestUtils.getSite(request), RequestUtils.getNavigationLanguage(request)
					.getCode());
		}

		try {
			JasperCompileManager.compileReportToFile(realPathJrxml);
			JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(export.generateDataSource(mainTableRows)
					.toArray());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile, export.getParameters(year), dataSource);
			response.setHeader("Content-Disposition", "attachment; filename=ParisIndicator" + reportCode + ".pdf");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.exportReport();
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}