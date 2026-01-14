package org.digijava.module.trubudget.model.workflowitem;

import java.util.List;
import java.util.Map;

public class WorkflowItem {
    private String id;
    private String creationUnixTs;
    private String status;
    private String rejectReason;
    private String amountType;
    private String displayName;
    private String description;
    private String amount;
    private String assignee;
    private String currency;
    private String billingDate;
    private String dueDate;
    private String exchangeRate;
    private Map<String, Object> additionalData;
    private String workflowItemType;
    private List<Document> documents;
    private List<String> allowedIntents;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreationUnixTs() {
        return creationUnixTs;
    }

    public void setCreationUnixTs(String creationUnixTs) {
        this.creationUnixTs = creationUnixTs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
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

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }

    public String getWorkflowItemType() {
        return workflowItemType;
    }

    public void setWorkflowItemType(String workflowItemType) {
        this.workflowItemType = workflowItemType;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<String> getAllowedIntents() {
        return allowedIntents;
    }

    public void setAllowedIntents(List<String> allowedIntents) {
        this.allowedIntents = allowedIntents;
    }

    // Getters and setters for all fields
    public static class Document {
        private String hash;
        private String fileName;
        private String id;
        private boolean available;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }
    }
}


