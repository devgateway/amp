package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.AmpReportGenerator;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReports;

public class MyAmpReportGenerator extends AmpReportGenerator{
    
    protected AmpReportModifier reportModifier;
    public MyAmpReportGenerator(AmpReports report, AmpARFilter filter, boolean regenerateFilterQuery, AmpReportModifier modifier){
        super(report, filter, regenerateFilterQuery);
        this.reportModifier = modifier;
    }
    
    @Override
    protected String buildColumnFilterSQLClause(AmpColumns col, String extractorView) {
        String res = super.buildColumnFilterSQLClause(col, extractorView);
        if (reportModifier == null){
            return res;
        }
        String addedQuery = reportModifier.getColumnFilter(res, col, extractorView);
        if (addedQuery != null)
            return res + addedQuery;
        return res;
    }

}
