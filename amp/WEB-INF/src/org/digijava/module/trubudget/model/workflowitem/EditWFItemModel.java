package org.digijava.module.trubudget.model.workflowitem;


import org.digijava.module.trubudget.model.subproject.CreateWorkFlowItemModel;

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
    public static class Data {
        private String projectId;
        private String subprojectId;
        private String workflowitemId;
        private String displayName;
        private String description;
        private Map<String , Object> additionalData;
        private List<CreateWorkFlowItemModel.Document> documents;

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
    }


}
