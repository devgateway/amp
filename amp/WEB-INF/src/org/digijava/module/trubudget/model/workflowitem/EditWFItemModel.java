package org.digijava.module.trubudget.model.workflowitem;


import org.digijava.module.trubudget.model.subproject.CreateWorkFlowItemModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditWFItemModel {
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
        return "EditWFItemModel{" +
                "apiVersion='" + apiVersion + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {
        private String projectId;
        private String subprojectId;
        private String workflowitemId;
        private String displayName;
        private String amountType;
        private String amount;
        private String currency;
        private String exchangeRate="1.0";
        private String billingDate;
        private String dueDate;
        private String description;
        private Map<String , Object> additionalData= new HashMap<>();
        private List<CreateWorkFlowItemModel.Document> documents=new ArrayList<>();

        // Getters and setters for projectId, displayName, and description

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
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

        public String getSubprojectId() {
            return subprojectId;
        }

        public void setSubprojectId(String subprojectId) {
            this.subprojectId = subprojectId;
        }

        public Map<String, Object> getAdditionalData() {
            return additionalData;
        }

        public void setAdditionalData(Map<String, Object> additionalData) {
            this.additionalData = additionalData;
        }

        public String getWorkflowitemId() {
            return workflowitemId;
        }

        public void setWorkflowitemId(String workflowitemId) {
            this.workflowitemId = workflowitemId;
        }

        public List<CreateWorkFlowItemModel.Document> getDocuments() {
            return documents;
        }

        public void setDocuments(List<CreateWorkFlowItemModel.Document> documents) {
            this.documents = documents;
        }

        public String getAmountType() {
            return amountType;
        }

        public void setAmountType(String amountType) {
            this.amountType = amountType;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getExchangeRate() {
            return exchangeRate;
        }

        public void setExchangeRate(String exchangeRate) {
            this.exchangeRate = exchangeRate;
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

        @Override
        public String toString() {
            return "Data{" +
                    "projectId='" + projectId + '\'' +
                    ", subprojectId='" + subprojectId + '\'' +
                    ", workflowitemId='" + workflowitemId + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", amountType='" + amountType + '\'' +
                    ", amount='" + amount + '\'' +
                    ", currency='" + currency + '\'' +
                    ", exchangeRate='" + exchangeRate + '\'' +
                    ", billingDate='" + billingDate + '\'' +
                    ", dueDate='" + dueDate + '\'' +
                    ", description='" + description + '\'' +
                    ", additionalData=" + additionalData +
                    ", documents=" + documents +
                    '}';
        }
    }


}
