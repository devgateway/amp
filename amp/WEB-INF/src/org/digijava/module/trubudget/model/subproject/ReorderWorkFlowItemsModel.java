package org.digijava.module.trubudget.model.subproject;

import java.util.Set;

public class ReorderWorkFlowItemsModel {
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
        private Set<String> ordering;

        // Getters and setters for projectId, subprojectId, and ordering

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

        public Set<String> getOrdering() {
            return ordering;
        }

        public void setOrdering(Set<String> ordering) {
            this.ordering = ordering;
        }
    }
}


