package org.digijava.kernel.ampapi.endpoints.integration.dto;

import javax.validation.constraints.NotNull;

public class DagRunsRequestDTO {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String workspaceId;

    @NotNull
    private String fileName;

    @NotNull
    private String dagId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
