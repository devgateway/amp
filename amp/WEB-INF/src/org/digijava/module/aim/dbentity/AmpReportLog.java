package org.digijava.module.aim.dbentity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AMP_REPORT_LOG")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpReportLog implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_report_log_seq")
    @SequenceGenerator(name = "amp_report_log_seq", sequenceName = "amp_report_log_seq", allocationSize = 1)
    @Column(name = "amp_reports_member_id")
    private Long ampReportMemberLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_report_id", referencedColumnName = "amp_report_id")
    private AmpReports report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_member_id", referencedColumnName = "amp_member_id")
    private AmpTeamMember member;

    @Column(name = "last_time_shown")
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
