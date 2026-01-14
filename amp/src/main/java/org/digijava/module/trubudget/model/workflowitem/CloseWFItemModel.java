package org.digijava.module.trubudget.model.workflowitem;

public class CloseWFItemModel {
    private String apiVersion;
    private Data data;

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

    public static class Data{
        private String projectId;
        private String subprojectId;
        private String workflowitemId;
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

        public String getWorkflowitemId() {
            return workflowitemId;
        }

        public void setWorkflowitemId(String workflowitemId) {
            this.workflowitemId = workflowitemId;
        }
    }


    public static class RejectData extends Data
    {
        private String rejectReason;


        public String getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(String rejectReason) {
            this.rejectReason = rejectReason;
        }



    }
}
