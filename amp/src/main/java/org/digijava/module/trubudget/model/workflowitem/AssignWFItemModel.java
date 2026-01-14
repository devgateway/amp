package org.digijava.module.trubudget.model.workflowitem;



public class AssignWFItemModel {
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
        private String workflowitemId;
        private String identity;

        // Getters and setters for projectId, subprojectId, and identity

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

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getWorkflowitemId() {
            return workflowitemId;
        }

        public void setWorkflowitemId(String workflowitemId) {
            this.workflowitemId = workflowitemId;
        }
    }
}


