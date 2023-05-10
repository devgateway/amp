package org.digijava.kernel.ampapi.endpoints.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DagRunsResponseDTO {
    @JsonProperty("dag_id")
    private String dagId;
    @JsonProperty("dag_run_id")
    private String dagRunId;
    @JsonProperty("data_interval_end")
    private String dataIntervalEnd;
    @JsonProperty("data_interval_start")
    private String dataIntervalStart;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("execution_date")
    private String executionDate;
    @JsonProperty("external_trigger")
    private boolean externalTrigger;
    @JsonProperty("last_scheduling_decision")
    private String lastSchedulingDecision;
    @JsonProperty("logical_date")
    private String logicalDate;
    private String note;
    @JsonProperty("run_type")
    private String runType;
    @JsonProperty("start_date")
    private String startDate;
    private String state;

    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    public String getDagRunId() {
        return dagRunId;
    }

    public void setDagRunId(String dagRunId) {
        this.dagRunId = dagRunId;
    }

    public String getDataIntervalEnd() {
        return dataIntervalEnd;
    }

    public void setDataIntervalEnd(String dataIntervalEnd) {
        this.dataIntervalEnd = dataIntervalEnd;
    }

    public String getDataIntervalStart() {
        return dataIntervalStart;
    }

    public void setDataIntervalStart(String dataIntervalStart) {
        this.dataIntervalStart = dataIntervalStart;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public boolean isExternalTrigger() {
        return externalTrigger;
    }

    public void setExternalTrigger(boolean externalTrigger) {
        this.externalTrigger = externalTrigger;
    }

    public String getLastSchedulingDecision() {
        return lastSchedulingDecision;
    }

    public void setLastSchedulingDecision(String lastSchedulingDecision) {
        this.lastSchedulingDecision = lastSchedulingDecision;
    }

    public String getLogicalDate() {
        return logicalDate;
    }

    public void setLogicalDate(String logicalDate) {
        this.logicalDate = logicalDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
