package org.digijava.kernel.ampapi.endpoints.dto.gis.ssc;

import java.util.ArrayList;
import java.util.List;

public class SscDashboardXlsResult {
    public SscDashboardXlsResult() {
        sscDashboardXlsResultRows = new ArrayList<>();
    }

    public List<SscDashboardXlsResultRow> getSscDashboardXlsResultRows() {
        return sscDashboardXlsResultRows;
    }

    public void setSscDashboardXlsResultRows(List<SscDashboardXlsResultRow> sscDashboardXlsResultRows) {
        this.sscDashboardXlsResultRows = sscDashboardXlsResultRows;
    }

    private List<SscDashboardXlsResultRow> sscDashboardXlsResultRows;

}
