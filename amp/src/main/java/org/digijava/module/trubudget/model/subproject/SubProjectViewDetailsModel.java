package org.digijava.module.trubudget.model.subproject;

public class SubProjectViewDetailsModel {
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
        private Subproject subproject;

        public Subproject getSubproject() {
            return subproject;
        }

        public void setSubproject(Subproject subproject) {
            this.subproject = subproject;
        }
    }

    public static class Subproject {
        private SubprojectData data;

        public SubprojectData getData() {
            return data;
        }

        public void setData(SubprojectData data) {
            this.data = data;
        }
    }

    public static class SubprojectData {
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
