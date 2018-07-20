package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.newreports.ReportColumn;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * @author Octavian Ciubotaru
 */
public class SaikuPagedReportResult extends PagedReportResult {

    private JsonBean query;

    private List<String> cellset;

    private Set<ReportColumn> columns;

    private Set<ReportColumn> hierarchies;

    private Map<String, Object> colorSettings;

    private String reportTotalsString;

    public JsonBean getQuery() {
        return query;
    }

    public void setQuery(JsonBean query) {
        this.query = query;
    }

    public List<String> getCellset() {
        return cellset;
    }

    public void setCellset(List<String> cellset) {
        this.cellset = cellset;
    }

    public Set<ReportColumn> getColumns() {
        return columns;
    }

    public void setColumns(Set<ReportColumn> columns) {
        this.columns = columns;
    }

    public Set<ReportColumn> getHierarchies() {
        return hierarchies;
    }

    public void setHierarchies(Set<ReportColumn> hierarchies) {
        this.hierarchies = hierarchies;
    }

    public Map<String, Object> getColorSettings() {
        return colorSettings;
    }

    public void setColorSettings(Map<String, Object> colorSettings) {
        this.colorSettings = colorSettings;
    }

    public String getReportTotalsString() {
        return reportTotalsString;
    }

    public void setReportTotalsString(String reportTotalsString) {
        this.reportTotalsString = reportTotalsString;
    }
}
