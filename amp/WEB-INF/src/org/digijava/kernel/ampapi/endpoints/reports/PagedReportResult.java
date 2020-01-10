package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dgfoundation.amp.newreports.HeaderCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportWarning;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.settings.Settings;

/**
 * @author Octavian Ciubotaru
 */
public class PagedReportResult {

    private JSONReportPage page;

    private Settings settings;

    private SortedMap<Long, SortedSet<ReportWarning>> reportWarnings;

    @JsonProperty("isEmpty")
    private boolean empty;

    private List<ReportOutputColumn> headers;

    @JsonProperty(EPConstants.GENERATED_HEADERS)
    private List<List<HeaderCell>> generatedHeaders;

    @JsonProperty(EPConstants.WARNINGS)
    private SortedMap<Long, SortedSet<ReportWarning>> warnings;

    @JsonProperty(EPConstants.STATS)
    private Map<String, Object> stats;

    private boolean rowTotals;

    public JSONReportPage getPage() {
        return page;
    }

    public void setPage(JSONReportPage page) {
        this.page = page;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public SortedMap<Long, SortedSet<ReportWarning>> getReportWarnings() {
        return reportWarnings;
    }

    public void setReportWarnings(SortedMap<Long, SortedSet<ReportWarning>> reportWarnings) {
        this.reportWarnings = reportWarnings;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public List<ReportOutputColumn> getHeaders() {
        return headers;
    }

    public void setHeaders(List<ReportOutputColumn> headers) {
        this.headers = headers;
    }

    public List<List<HeaderCell>> getGeneratedHeaders() {
        return generatedHeaders;
    }

    public void setGeneratedHeaders(List<List<HeaderCell>> generatedHeaders) {
        this.generatedHeaders = generatedHeaders;
    }

    public SortedMap<Long, SortedSet<ReportWarning>> getWarnings() {
        return warnings;
    }

    public void setWarnings(SortedMap<Long, SortedSet<ReportWarning>> warnings) {
        this.warnings = warnings;
    }

    public Map<String, Object> getStats() {
        return stats;
    }

    public void setStats(Map<String, Object> stats) {
        this.stats = stats;
    }

    public boolean isRowTotals() {
        return rowTotals;
    }

    public void setRowTotals(boolean rowTotals) {
        this.rowTotals = rowTotals;
    }
}
