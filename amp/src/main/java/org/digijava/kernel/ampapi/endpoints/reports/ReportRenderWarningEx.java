package org.digijava.kernel.ampapi.endpoints.reports;

import org.dgfoundation.amp.newreports.ReportRenderWarning;

/**
 * the external API faucet of a {@link ReportRenderWarning} coming off scanning the DB schema for inconsistencies
 * @author Dolghier Constantin
 *
 */
public class ReportRenderWarningEx {
    
    protected final long activityId;
    protected final String message;
    
    public ReportRenderWarningEx(ReportRenderWarning rrw) {
        this(rrw.subject.getEntityId(), rrw.warningType.toString());
    }
    
    public ReportRenderWarningEx(long activityId, String message) {
        this.activityId = activityId;
        this.message = message;
    }

    public long getActivityId() {
        return activityId;
    }

    public String getMessage() {
        return message;
    }
}
