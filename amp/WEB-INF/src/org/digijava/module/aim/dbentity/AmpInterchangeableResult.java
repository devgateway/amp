package org.digijava.module.aim.dbentity;

import java.sql.Date;

/**
 * Created by esoliani on 17/06/16.
 */

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "AMP_INTERCHANGEABLE_RESULT")
public class AmpInterchangeableResult {

    public enum AmpResultStatus {
        UPDATED, FAILED, FAILED_COMPLETE, INSERTED;

        public String getType() {
            return this.name().toLowerCase();
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_INTERCHANGEABLE_RESULT_seq")
    @SequenceGenerator(name = "AMP_INTERCHANGEABLE_RESULT_seq", sequenceName = "AMP_INTERCHANGEABLE_RESULT_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "activity_id")
    private String ampActivityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AmpResultStatus status;

    @Column(name = "errors")
    private String errorDetails;

    @Column(name = "operation")
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
