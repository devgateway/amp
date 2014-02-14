package org.digijava.module.gpi.model;

import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.struts.action.ActionServlet;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.gpi.helper.*;
import org.digijava.module.gpi.helper.export.*;
import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;
import org.digijava.module.gpi.util.*;

public class GPIExportUseCase {

	public synchronized void exportReport(ActionServlet servlet, HttpServletResponse response,
			HttpServletRequest request, String reportCode, Collection<GPIReportAbstractRow> mainTableRows,
			int[][] miniTable, int startYear, int endYear, String type, String currency) throws Exception {

		/*String reportsFolderPath = servlet.getServletContext().getRealPath(
				"/WEB-INF/classes/org/digijava/module/gpi/jasperreports");
		java.io.File reportsFolder = new java.io.File(reportsFolderPath);
		if (!reportsFolder.exists() || !reportsFolder.isDirectory()) {
			reportsFolder.mkdirs();
		}
		reportsFolderPath = reportsFolder.getAbsolutePath().replace("\\", "/");
		String reportName = "ParisIndicator" + reportCode;
		String reportPath = reportsFolderPath + "/" + reportName;
		String realPathJrxml = reportPath + ".jrxml";
		String jasperFile = reportPath + ".jasper";

		GPIAbstractExport export = null;
		JRDataSource dataSource = null;
		JasperPrint jasperPrint = null;

		Site site = RequestUtils.getSite(request);
		String langCode = RequestUtils.getNavigationLanguage(request).getCode();
		if (GPIConstants.PARIS_INDICATOR_REPORT_1.equalsIgnoreCase(reportCode)) {
			export = new GPIReport1Export(site, langCode, currency);
		} else if (GPIConstants.PARIS_INDICATOR_REPORT_4.equalsIgnoreCase(reportCode)) {
			export = new GPIReport4Export(site, langCode, currency);
		} else if (GPIConstants.PARIS_INDICATOR_REPORT_5a.equalsIgnoreCase(reportCode)) {
			export = new GPIReport5aExport(site, langCode, currency);
			// Dynamically generate the .jrxml file.
			((GPIReport5aExport) export).createJrxmlFromClass(reportPath + "_sub.jrxml", startYear, endYear);
			JasperCompileManager.compileReportToFile(reportPath + "_sub.jrxml");
			((GPIReport5aExport) export).setSubReportDirectory(reportPath + "_sub.jasper");
			((GPIReport5aExport) export).setMiniReportData(((GPIReport5aExport) export).generateDataSource(miniTable,
					startYear, endYear));
		} else if (GPIConstants.PARIS_INDICATOR_REPORT_5b.equalsIgnoreCase(reportCode)) {
			export = new GPIReport5bExport(site, langCode, currency);
			// Dynamically generate the .jrxml file.
			((GPIReport5bExport) export).createJrxmlFromClass(reportPath + "_sub.jrxml", startYear, endYear);
			JasperCompileManager.compileReportToFile(reportPath + "_sub.jrxml");
			((GPIReport5bExport) export).setSubReportDirectory(reportPath + "_sub.jasper");
			((GPIReport5bExport) export).setMiniReportData(((GPIReport5bExport) export).generateDataSource(miniTable,
					startYear, endYear));
		} else if (GPIConstants.PARIS_INDICATOR_REPORT_6.equalsIgnoreCase(reportCode)) {
			export = new GPIReport6Export(site, langCode);
			// Dynamically generate the .jrxml file.
			((GPIReport6Export) export).createJrxmlFromClass(realPathJrxml, startYear, endYear);
		} else if (GPIConstants.PARIS_INDICATOR_REPORT_7.equalsIgnoreCase(reportCode)) {
			export = new GPIReport7Export(site, langCode, currency);
		} else if (GPIConstants.PARIS_INDICATOR_REPORT_9.equalsIgnoreCase(reportCode)) {
			export = new GPIReport9Export(site, langCode, currency);
		} else if (GPIConstants.PARIS_INDICATOR_REPORT_10a.equalsIgnoreCase(reportCode)) {
			export = new GPIReport10aExport(site, langCode);
		} else if (GPIConstants.PARIS_INDICATOR_REPORT_10b.equalsIgnoreCase(reportCode)) {
			export = new GPIReport10bExport(site, langCode);
		}

		try {
			JasperCompileManager.compileReportToFile(realPathJrxml);
			if (GPIConstants.PARIS_INDICATOR_REPORT_6.equalsIgnoreCase(reportCode)) {
				dataSource = new GPIJasperDataSource(((GPIReport6Export) export).generateDataSource(mainTableRows,
						startYear, endYear));
			} else {
				dataSource = new JRBeanArrayDataSource(export.generateDataSource(mainTableRows).toArray());
			}
			jasperPrint = JasperFillManager.fillReport(jasperFile, export.getParameters(endYear), dataSource);
			JRAbstractExporter exporter = null;
			if (type.equalsIgnoreCase("PDF")) {
				response.setHeader("Content-Disposition", "attachment; filename=ParisIndicator" + reportCode + ".pdf");
				response.setContentType("application/pdf");
				exporter = new JRPdfExporter();
			}
			if (type.equalsIgnoreCase("XLS")) {
				response.setHeader("Content-Disposition", "attachment; filename=ParisIndicator" + reportCode + ".xls");
				response.setContentType("application/vnd.ms-excel");
				exporter = new JRXlsExporter();
			}
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.exportReport();
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}