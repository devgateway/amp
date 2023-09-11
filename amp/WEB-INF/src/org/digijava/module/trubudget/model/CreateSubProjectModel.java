package org.digijava.module.trubudget.model;


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
    }

    public static class Subproject {
        private String id;
        private String status;
        private String displayName;
        private String description;
        private String amount;
        private String assignee;
        private String currency;
        private Map<String, Object> additionalData;

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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
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
    }
}




