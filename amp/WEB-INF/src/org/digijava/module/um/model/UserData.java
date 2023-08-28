package org.digijava.module.um.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserData {
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
        return "UserData{" +
                "apiVersion='" + apiVersion + '\'' +
                ", data=" + data +
                '}';
    }

    // Nested class for "data" field
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

    // Nested class for "user" field
    public static class User {
        @JsonProperty("id")
        private String id;

        @JsonProperty("displayName")
        private String displayName;

        @JsonProperty("organization")
        private String organization;

        @JsonProperty("password")
        private String password;

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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id='" + id + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", organization='" + organization + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
