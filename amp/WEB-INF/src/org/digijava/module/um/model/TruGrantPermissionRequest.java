package org.digijava.module.um.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TruGrantPermissionRequest {
    @JsonProperty("apiVersion")
    private String apiVersion;

    @JsonProperty("data")
    private Data data;

    // Getters and setters

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
        @JsonProperty("identity")
        private String identity;

        @JsonProperty("intent")
        private String intent;

        // Getters and setters

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getIntent() {
            return intent;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }
    }
}

