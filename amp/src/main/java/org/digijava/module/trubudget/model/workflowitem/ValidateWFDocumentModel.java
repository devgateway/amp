package org.digijava.module.trubudget.model.workflowitem;

public class ValidateWFDocumentModel {
    private String apiVersion;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public static class Data
    {
        private String base64String;
        private String hash;

        public String getBase64String() {
            return base64String;
        }

        public void setBase64String(String base64String) {
            this.base64String = base64String;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }
}
