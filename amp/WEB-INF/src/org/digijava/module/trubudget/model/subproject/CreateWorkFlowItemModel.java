package org.digijava.module.trubudget.model.subproject;


import java.util.List;
import java.util.Map;

public class CreateWorkFlowItemModel {
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
        private String subprojectId;
        private String displayName;
        private String description;
        private String amount;
        private String assignee;
        private String currency;
        private String amountType;
        private String billingDate;
        private String dueDate;
        private String exchangeRate;
        private List<Document> documents;
        private Map<String, Object> additionalData;
        private String workflowitemType;

        // Getters and setters for projectId, subprojectId, displayName, description,
        // amount, assignee, currency, amountType, billingDate, dueDate, exchangeRate,
        // documents, additionalData, and workflowitemType

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getSubprojectId() {
            return subprojectId;
        }

        public void setSubprojectId(String subprojectId) {
            this.subprojectId = subprojectId;
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

        public String getAmountType() {
            return amountType;
        }

        public void setAmountType(String amountType) {
            this.amountType = amountType;
        }

        public String getBillingDate() {
            return billingDate;
        }

        public void setBillingDate(String billingDate) {
            this.billingDate = billingDate;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public String getExchangeRate() {
            return exchangeRate;
        }

        public void setExchangeRate(String exchangeRate) {
            this.exchangeRate = exchangeRate;
        }

        public List<Document> getDocuments() {
            return documents;
        }

        public void setDocuments(List<Document> documents) {
            this.documents = documents;
        }

        public Map<String, Object> getAdditionalData() {
            return additionalData;
        }

        public void setAdditionalData(Map<String, Object> additionalData) {
            this.additionalData = additionalData;
        }

        public String getWorkflowitemType() {
            return workflowitemType;
        }

        public void setWorkflowitemType(String workflowitemType) {
            this.workflowitemType = workflowitemType;
        }
    }

    public static class Document {
        private String id;
        private String base64;
        private String fileName;

        // Getters and setters for id, base64, and fileName

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBase64() {
            return base64;
        }

        public void setBase64(String base64) {
            this.base64 = base64;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

}

