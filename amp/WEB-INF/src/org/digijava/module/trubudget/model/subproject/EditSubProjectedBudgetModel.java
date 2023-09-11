package org.digijava.module.trubudget.model.subproject;


public class EditSubProjectedBudgetModel {
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
        private String organization;
        private String currencyCode;
        private String value;

        // Getters and setters for projectId, organization, currencyCode, and value

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getSubprojectId() {
            return subprojectId;
        }

        public void setSubprojectId(String subprojectId) {
            this.subprojectId = subprojectId;
        }
    }


}
