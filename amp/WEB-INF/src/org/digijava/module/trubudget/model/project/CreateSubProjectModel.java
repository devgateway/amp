package org.digijava.module.trubudget.model.project;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSubProjectModel {
    private String apiVersion;
    private Data data;

    // Getters and setters for apiVersion and data

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CreateSubProjectModel{" +
                "apiVersion='" + apiVersion + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {
        private String projectId;
        private Subproject subproject;

        // Getters and setters for projectId and subproject

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public Subproject getSubproject() {
            return subproject;
        }

        public void setSubproject(Subproject subproject) {
            this.subproject = subproject;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "projectId='" + projectId + '\'' +
                    ", subproject=" + subproject +
                    '}';
        }
    }

    public static class Subproject {
        private String id;
        private String status="open";
        private String displayName;
        private String description;
        private String assignee;
        private String validator;
        @JsonProperty("workflowitemType")
        private String workflowitemType="general";
        private String currency;
        private List<CreateProjectModel.ProjectedBudget> projectedBudgets= new ArrayList<>();

        private Map<String, Object> additionalData= new HashMap<>();

        // Getters and setters for id, status, displayName, description, amount, assignee, currency, and additionalData

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }


        public String getAssignee() {
            return assignee;
        }

        public void setAssignee(String assignee) {
            this.assignee = assignee;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Map<String, Object> getAdditionalData() {
            return additionalData;
        }

        public void setAdditionalData(Map<String, Object> additionalData) {
            this.additionalData = additionalData;
        }

        public String getValidator() {
            return validator;
        }

        public void setValidator(String validator) {
            this.validator = validator;
        }

        public String getWorkflowitemType() {
            return workflowitemType;
        }

        public void setWorkflowitemType(String workflowitemType) {
            this.workflowitemType = workflowitemType;
        }

        public List<CreateProjectModel.ProjectedBudget> getProjectedBudgets() {
            return projectedBudgets;
        }

        public void setProjectedBudgets(List<CreateProjectModel.ProjectedBudget> projectedBudgets) {
            this.projectedBudgets = projectedBudgets;
        }

        @Override
        public String toString() {
            return "Subproject{" +
                    "id='" + id + '\'' +
                    ", status='" + status + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", description='" + description + '\'' +
                    ", assignee='" + assignee + '\'' +
                    ", validator='" + validator + '\'' +
                    ", workflowitemType='" + workflowitemType + '\'' +
                    ", currency='" + currency + '\'' +
                    ", projectedBudgets=" + projectedBudgets +
                    ", additionalData=" + additionalData +
                    '}';
        }
    }
}




