package org.digijava.module.um.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TruRevokePermissionRequest {
    @JsonProperty("apiVersion")
    private String apiVersion;

    @JsonProperty("data")
    private TruRevokePermissionRequest.Data data;

    // Getters and setters

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }



    public TruRevokePermissionRequest.Data getData() {
        return data;
    }

    public void setData(TruRevokePermissionRequest.Data data) {
        this.data = data;
    }

    public static class Data {
        @JsonProperty("identity")
        private String identity;

        @JsonProperty("intent")
        private String intent;

        @JsonProperty("userId")
        private String userId;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
