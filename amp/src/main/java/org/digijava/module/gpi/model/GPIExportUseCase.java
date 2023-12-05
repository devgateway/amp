package org.digijava.module.gpi.model;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.apache.struts.action.ActionServlet;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.gpi.helper.export.*;
import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;
import org.digijava.module.gpi.util.GPIConstants;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

public class GPIExportUseCase {

    public synchronized void exportReport(ActionServlet servlet, HttpServletResponse response,
            HttpServletRequest request, String reportCode, Collection<GPIReportAbstractRow> mainTableRows,
            int[][] miniTable, int startYear, int endYear, String type, String currency) throws Exception {

        String reportsFolderPath = servlet.getServletContext().getRealPath(
                "/src/main/webapp/WEB-INF/classes/org/digijava/module/gpi/jasperreports");
        java.io.File reportsFolder = new java.io.File(reportsFolderPath);
        if (!reportsFolder.exists() || !reportsFolder.isDirectory()) {
            reportsFolder.mkdirs();
        }
        reportsFolderPath = reportsFolder.getAbsolutePath().replace("\\", "/");
        String reportName = "GPI" + reportCode;
        String reportPath = reportsFolderPath + "/" + reportName;
        String realPathJrxml = reportPath + ".jrxml";
        String jasperFile = reportPath + ".jasper";

        GPIAbstractExport export = null;
        JRDataSource dataSource = null;
        JasperPrint jasperPrint = null;

        Site site = RequestUtils.getSite(request);
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
        if (GPIConstants.GPI_REPORT_1.equalsIgnoreCase(reportCode)) {
            export = new GPIReport1Export(site, langCode, currency);
        } else if (GPIConstants.GPI_REPORT_5a.equalsIgnoreCase(reportCode)) {
            export = new GPIReport5aExport(site, langCode, currency);
        } else if (GPIConstants.GPI_REPORT_6.equalsIgnoreCase(reportCode)) {
            export = new GPIReport6Export(site, langCode, currency);
        } else if (GPIConstants.GPI_REPORT_9b.equalsIgnoreCase(reportCode)) {
            export = new GPIReport9bExport(site, langCode, currency);
        }

        try {
            JasperCompileManager.compileReportToFile(realPathJrxml);
            dataSource = new JRBeanArrayDataSource(export.generateDataSource(mainTableRows).toArray());
            jasperPrint = JasperFillManager.fillReport(jasperFile, export.getParameters(endYear), dataSource);
            JRAbstractExporter exporter = null;
            if (type.equalsIgnoreCase("PDF")) {
                response.setHeader("Content-Disposition", ResponseUtil.encodeContentDispositionForDownload(request, "GPI" + reportCode + ".pdf"));
                response.setContentType("application/pdf");
                exporter = new JRPdfExporter();
            }
            if (type.equalsIgnoreCase("XLS")) {
                response.setHeader("Content-Disposition", ResponseUtil.encodeContentDispositionForDownload(request, "GPI" + reportCode + ".xls"));
                response.setContentType("application/vnd.ms-excel");
                exporter = new JRXlsExporter();
                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, new Integer("-1"));
                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporter.getParameters().put(JRDesignParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
            }
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);          
            exporter.exportReport();
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
