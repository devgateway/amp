package org.digijava.module.trubudget.model.workflowitem;

import java.util.List;

public class CreateWFResponseModel {
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

    @Override
    public String toString() {
        return "CreateWFResponseModel{" +
                "apiVersion='" + apiVersion + '\'' +
                ", data=" + data +
                '}';
    }

    // Constructors, getters, and setters

    public static class Data {
        private Project project;
        private Subproject subproject;
        private Workflowitem workflowitem;

        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
        }

        public Subproject getSubproject() {
            return subproject;
        }

        public void setSubproject(Subproject subproject) {
            this.subproject = subproject;
        }

        public Workflowitem getWorkflowitem() {
            return workflowitem;
        }

        public void setWorkflowitem(Workflowitem workflowitem) {
            this.workflowitem = workflowitem;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "project=" + project +
                    ", subproject=" + subproject +
                    ", workflowitem=" + workflowitem +
                    '}';
        }

        // Constructors, getters, and setters

        public static class Project {
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            // Constructors, getters, and setters
        }

        public static class Subproject {
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            // Constructors, getters, and setters
        }

        public static class Workflowitem {
            private String id;
            private List<String> documents;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<String> getDocuments() {
                return documents;
            }

            public void setDocuments(List<String> documents) {
                this.documents = documents;
            }

            @Override
            public String toString() {
                return "Workflowitem{" +
                        "id='" + id + '\'' +
                        ", documents=" + documents +
                        '}';
            }

            // Constructors, getters, and setters
        }
    }
}
