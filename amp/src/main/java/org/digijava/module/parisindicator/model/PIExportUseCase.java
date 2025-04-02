package org.digijava.module.parisindicator.model;

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
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.parisindicator.helper.*;
import org.digijava.module.parisindicator.helper.export.*;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.*;

public class PIExportUseCase {

    public synchronized void exportReport(ActionServlet servlet, HttpServletResponse response,
            HttpServletRequest request, String reportCode, Collection<PIReportAbstractRow> mainTableRows,
            int[][] miniTable, int startYear, int endYear, String type, String currency) throws Exception {

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
        JRDataSource dataSource = null;
        JasperPrint jasperPrint = null;

        Site site = RequestUtils.getSite(request);
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        if (PIConstants.PARIS_INDICATOR_REPORT_3.equalsIgnoreCase(reportCode)) {
            export = new PIReport3Export(site, langCode, currency);
        } else if (PIConstants.PARIS_INDICATOR_REPORT_4.equalsIgnoreCase(reportCode)) {
            export = new PIReport4Export(site, langCode, currency);
        } else if (PIConstants.PARIS_INDICATOR_REPORT_5a.equalsIgnoreCase(reportCode)) {
            export = new PIReport5aExport(site, langCode, currency);
            // Dynamically generate the .jrxml file.
            ((PIReport5aExport) export).createJrxmlFromClass(reportPath + "_sub.jrxml", startYear, endYear);
            JasperCompileManager.compileReportToFile(reportPath + "_sub.jrxml");
            ((PIReport5aExport) export).setSubReportDirectory(reportPath + "_sub.jasper");
            ((PIReport5aExport) export).setMiniReportData(((PIReport5aExport) export).generateDataSource(miniTable,
                    startYear, endYear));
        } else if (PIConstants.PARIS_INDICATOR_REPORT_5b.equalsIgnoreCase(reportCode)) {
            export = new PIReport5bExport(site, langCode, currency);
            // Dynamically generate the .jrxml file.
            ((PIReport5bExport) export).createJrxmlFromClass(reportPath + "_sub.jrxml", startYear, endYear);
            JasperCompileManager.compileReportToFile(reportPath + "_sub.jrxml");
            ((PIReport5bExport) export).setSubReportDirectory(reportPath + "_sub.jasper");
            ((PIReport5bExport) export).setMiniReportData(((PIReport5bExport) export).generateDataSource(miniTable,
                    startYear, endYear));
        } else if (PIConstants.PARIS_INDICATOR_REPORT_6.equalsIgnoreCase(reportCode)) {
            export = new PIReport6Export(site, langCode);
            // Dynamically generate the .jrxml file.
            ((PIReport6Export) export).createJrxmlFromClass(realPathJrxml, startYear, endYear);
        } else if (PIConstants.PARIS_INDICATOR_REPORT_7.equalsIgnoreCase(reportCode)) {
            export = new PIReport7Export(site, langCode, currency);
        } else if (PIConstants.PARIS_INDICATOR_REPORT_9.equalsIgnoreCase(reportCode)) {
            export = new PIReport9Export(site, langCode, currency);
        } else if (PIConstants.PARIS_INDICATOR_REPORT_10a.equalsIgnoreCase(reportCode)) {
            export = new PIReport10aExport(site, langCode);
        } else if (PIConstants.PARIS_INDICATOR_REPORT_10b.equalsIgnoreCase(reportCode)) {
            export = new PIReport10bExport(site, langCode);
        }

        try {
            JasperCompileManager.compileReportToFile(realPathJrxml);
            if (PIConstants.PARIS_INDICATOR_REPORT_6.equalsIgnoreCase(reportCode)) {
                dataSource = new PIJasperDataSource(((PIReport6Export) export).generateDataSource(mainTableRows,
                        startYear, endYear));
            } else {
                dataSource = new JRBeanArrayDataSource(export.generateDataSource(mainTableRows).toArray());
            }
            jasperPrint = JasperFillManager.fillReport(jasperFile, export.getParameters(endYear), dataSource);
            JRAbstractExporter exporter = null;
            if (type.equalsIgnoreCase("PDF")) {
                response.setHeader("Content-Disposition", ResponseUtil.encodeContentDispositionForDownload(request, "ParisIndicator" + reportCode + ".pdf"));
                response.setContentType("application/pdf");
                exporter = new JRPdfExporter();
            }
            if (type.equalsIgnoreCase("XLS")) {
                response.setHeader("Content-Disposition", ResponseUtil.encodeContentDispositionForDownload(request, "ParisIndicator" + reportCode + ".xls"));
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
        }
    }
}
