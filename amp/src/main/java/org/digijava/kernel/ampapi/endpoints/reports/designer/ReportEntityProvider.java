package org.digijava.kernel.ampapi.endpoints.reports.designer;

import org.dgfoundation.amp.ar.MeasureConstants;

public abstract class ReportEntityProvider {

    protected boolean isMTEFName(String name) {
        String regex = "^(" + MeasureConstants.MTEF
                + "|" + MeasureConstants.REAL_MTEF
                + "|" + MeasureConstants.MTEF_PROJECTIONS
                + "|" + MeasureConstants.PIPELINE_MTEF_PROJECTIONS
                + "|" + MeasureConstants.PROJECTION_MTEF_PROJECTIONS
                + ").*$";

        return name.matches(regex);
    }

}
