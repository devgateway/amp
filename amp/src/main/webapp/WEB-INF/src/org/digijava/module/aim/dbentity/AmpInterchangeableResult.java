package org.digijava.module.aim.dbentity;

import java.sql.Date;

/**
 * Created by esoliani on 17/06/16.
 */
public class AmpInterchangeableResult {

    public enum AmpResultStatus {
        UPDATED, FAILED, FAILED_COMPLETE, INSERTED;

        public String getType() {
            return this.name().toLowerCase();
        }
    }

    private Long id;
    private Date date;
    private String projectId;
    private String ampActivityId;
    private AmpResultStatus status;
    private String errorDetails;
    private String operation;

    public AmpInterchangeableResult() {
    }

    public AmpInterchangeableResult(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(String ampActivityId) {
        this.ampActivityId = ampActivityId;
    }

    public AmpResultStatus getStatus() {
        return status;
    }

    public void setStatus(AmpResultStatus status) {
        this.status = status;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AmpInterchangeableResult that = (AmpInterchangeableResult) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AmpInterchangeableResult{" +
                "id=" + id +
                ", projectId='" + projectId + '\'' +
                ", status=" + status +
                '}';
    }
}
