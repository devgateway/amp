package org.digijava.module.trubudget.model.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateProjectModel {
    private String apiVersion;

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public String toString() {
        return "CreateProjectModel{" +
                "apiVersion='" + apiVersion + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data{
        private Project project;

        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
        }


        @Override
        public String toString() {
            return "Data{" +
                    "project=" + project +
                    '}';
        }
    }
    public  static class Project {
        private String id;
        private String status;
        private String displayName;
        private String description;
        private String assignee;
        private String thumbnail;
        private List<ProjectedBudget> projectedBudgets= new ArrayList<>();
        private Map<String, Object> additionalData= new HashMap<>();
        private List<String> tags;

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

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public List<ProjectedBudget> getProjectedBudgets() {
            return projectedBudgets;
        }

        public void setProjectedBudgets(List<ProjectedBudget> projectedBudgets) {
            this.projectedBudgets = projectedBudgets;
        }

        public Map<String, Object> getAdditionalData() {
            return additionalData;
        }

        public void setAdditionalData(Map<String, Object> additionalData) {
            this.additionalData = additionalData;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "Project{" +
                    "id='" + id + '\'' +
                    ", status='" + status + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", description='" + description + '\'' +
                    ", assignee='" + assignee + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", projectedBudgets=" + projectedBudgets +
                    ", additionalData=" + additionalData +
                    ", tags=" + tags +
                    '}';
        }
    }
    public static class ProjectedBudget {
        private String organization;
        private String value;
        private String currencyCode;

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        @Override
        public String toString() {
            return "ProjectedBudget{" +
                    "organization='" + organization + '\'' +
                    ", value='" + value + '\'' +
                    ", currencyCode='" + currencyCode + '\'' +
                    '}';
        }
    }
}





