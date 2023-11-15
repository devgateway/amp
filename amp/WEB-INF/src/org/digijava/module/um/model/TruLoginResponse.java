package org.digijava.module.um.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TruLoginResponse {
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

    @Override
    public String toString() {
        return "TruLoginResponse{" +
                "apiVersion='" + apiVersion + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {
        @JsonProperty("user")
        private User user;

        // Getters and setters

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "user=" + user +
                    '}';
        }
    }

    public static class User {
        @JsonProperty("id")
        private String id;

        @JsonProperty("displayName")
        private String displayName;

        @JsonProperty("organization")
        private String organization;

        @JsonProperty("allowedIntents")
        private List<String> allowedIntents;

        @JsonProperty("groups")
        private List<String> groups;

        @JsonProperty("token")
        private String token;

        // Getters and setters

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public List<String> getAllowedIntents() {
            return allowedIntents;
        }

        public void setAllowedIntents(List<String> allowedIntents) {
            this.allowedIntents = allowedIntents;
        }

        public List<String> getGroups() {
            return groups;
        }

        public void setGroups(List<String> groups) {
            this.groups = groups;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id='" + id + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", organization='" + organization + '\'' +
                    ", allowedIntents=" + allowedIntents +
                    ", groups=" + groups +
                    ", token='" + token + '\'' +
                    '}';
        }
    }
}
