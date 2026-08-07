package org.digijava.module.trubudget.model.project;

public class ProjectViewDetailsModel {
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

    public static class Data {
        private Project project;

        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
        }
    }

    public static class Project {
        private ProjectData data;

        public ProjectData getData() {
            return data;
        }

        public void setData(ProjectData data) {
            this.data = data;
        }
    }

    public static class ProjectData {
        private String id;
        private String status;

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
    }
}
