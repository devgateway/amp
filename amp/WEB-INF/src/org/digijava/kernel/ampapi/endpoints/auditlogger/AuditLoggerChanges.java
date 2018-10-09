package org.digijava.kernel.ampapi.endpoints.auditlogger;

import org.digijava.kernel.ampapi.endpoints.auditlogger.dto.AuditLoggerRecord;

import java.util.List;

public class AuditLoggerChanges {

    private Long currentActivityId;
    private Long previousActivityId;
    private List<AuditLoggerRecord> auditLoggerRecords;

    public void setAuditLoggerRecords(List<AuditLoggerRecord> auditLoggerRecords) {
        this.auditLoggerRecords = auditLoggerRecords;
    }

    public List<AuditLoggerRecord> getAuditLoggerRecords() {
        return auditLoggerRecords;
    }

    public Long getCurrentActivityId() {
        return currentActivityId;
    }

    public void setCurrentActivityId(Long currentActivityId) {
        this.currentActivityId = currentActivityId;
    }

    public Long getPreviousActivityId() {
        return previousActivityId;
    }

    public void setPreviousActivityId(Long previousActivityId) {
        this.previousActivityId = previousActivityId;
    }
}
