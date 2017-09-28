package org.dgfoundation.amp.reports.saiku.export;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;

/**
 * @author Viorel Chihai
 *
 */
public class SaikuReportCsvExporter implements SaikuReportExporter {
    
    private final String separator = ";";
    private String lineSeparator = "";
    
    @Override
    public byte[] exportReport(GeneratedReport report, GeneratedReport dualReport) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        StringBuilder csvContent = renderCsvContent(report);

        os.write(csvContent.toString().getBytes("UTF-8"));
        os.flush();
        os.close();
        
        return os.toByteArray();
    }
    
    /**
     * @param report
     * @return
     */
    private StringBuilder renderCsvContent(GeneratedReport report) {
        StringBuilder csvContent = new StringBuilder();
        
        lineSeparator = System.getProperty("line.separator");
        
        renderHeader(csvContent, report);
        renderLines(csvContent, new StringBuilder(), report, report.reportContents, 0);
        
        return csvContent;
    }

    /**
     * @param csvContent
     * @param report
     */
    private void renderHeader(StringBuilder csvContent, GeneratedReport report) {
        IntWrapper intWrapper = new IntWrapper();
        report.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName)).forEach(roc -> { 
            if (intWrapper.value > 0) {
                csvContent.append(separator);
            }
            csvContent.append("\"").append(roc.getHierarchicalName().replaceAll("\\]\\[", " ").replaceAll("\\[", "").replaceAll("\\]", "")).append("\"");
            intWrapper.inc();
        });
        csvContent.append(lineSeparator);
    }
    
    /**
     * @param csvContent
     * @param currLine
     * @param report
     * @param reportContents
     * @param level
     */
    private void renderLines(StringBuilder csvContent, StringBuilder currLine, GeneratedReport report,  ReportArea reportContents, int level) {
        if (reportContents.getChildren() != null) {
            renderGroupLines(csvContent, currLine, report, reportContents, level);
        } else {
            // If it is summaryReport with one dummy column, we need to show the row with data
            if (level == 0 && !hasReportGeneratedDummyColumn(report)) {
                return;
            }
            
            int hierCnt = report.spec.getHierarchies().size();
            for (int i = hierCnt; i < report.leafHeaders.size(); i++) {
                ReportOutputColumn roc = report.leafHeaders.get(i);
                
                if (isHiddenColumn(roc.originalColumnName)) {
                    continue;
                }
                
                ReportCell rc = reportContents.getContents().get(roc) != null ? reportContents.getContents().get(roc) : roc.emptyCell;
                
                if (rc instanceof AmountCell) {
                    BigDecimal value = new BigDecimal(rc.value.toString());
                    currLine.append(value.stripTrailingZeros().toPlainString());
                } else {
                    currLine.append("\"").append(rc.displayedValue.replaceAll("\"", "'")).append("\"");
                }
                
                
                if (i < report.leafHeaders.size() - 1) {
                    currLine.append(separator);
                }
            }
            
            csvContent.append(currLine);
            csvContent.append(lineSeparator);
        }
    }
    
    /**
     * @param csvContent
     * @param currLine
     * @param report
     * @param reportContents
     * @param level
     */
    private void renderGroupLines(StringBuilder csvContent, StringBuilder currLine, GeneratedReport report,  ReportArea reportContents, int level) {
        for (ReportArea reportArea : reportContents.getChildren()) {
            StringBuilder tmpLine = new StringBuilder(currLine);
            if (reportArea.getNrEntities() > 0) {
                tmpLine.append("\"").append(reportArea.getOwner().debugString).append("\"");
                tmpLine.append(separator);
            } 
            
            renderLines(csvContent, tmpLine, report, reportArea, level+1);
        }
    }
    
    protected boolean isHiddenColumn(String columnName) {
        return columnName.equals("Draft") || columnName.equals("Approval Status");
    }
    
    protected boolean hasReportGeneratedDummyColumn(GeneratedReport report) {
         return report.spec.isSummaryReport() && report.spec.getHierarchies().isEmpty();
    }
}
