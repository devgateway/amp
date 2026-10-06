package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

public class AmpReportLog implements Serializable{

    private Long ampReportMemberLogId;
    
    private AmpReports report;
    
    private AmpTeamMember member;   
    
    private Date lastView;

    public Long getAmpReportMemberLogId() {
        return ampReportMemberLogId;
    }

    public void setAmpReportMemberLogId(Long ampReportsMemberLogId) {
        this.ampReportMemberLogId = ampReportsMemberLogId;
    }

    public AmpReports getReport() {
        return report;
    }

    public void setReport(AmpReports report) {
        this.report = report;
    }

    public AmpTeamMember getMember() {
        return member;
    }

    public void setMember(AmpTeamMember member) {
        this.member = member;
    }

    public Date getLastView() {
        return lastView;
    }

    public void setLastView(Date lastView) {
        this.lastView = lastView;
    }
}
