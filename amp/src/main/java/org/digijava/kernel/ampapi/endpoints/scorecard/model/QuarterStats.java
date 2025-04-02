package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class QuarterStats {

    @ApiModelProperty(value = "the count of active organisations for the past quarter", example = "52")
    private final int organizations;

    @ApiModelProperty(value = "the count of projects with action in the past quarter", example = "181")
    private final int projects;

    @ApiModelProperty(value = "the count of users logged into the System in the past quarter", example = "23")
    private final int users;

    public QuarterStats(int organizations, int projects, int users) {
        this.organizations = organizations;
        this.projects = projects;
        this.users = users;
    }

    public int getOrganizations() {
        return organizations;
    }

    public int getProjects() {
        return projects;
    }

    public int getUsers() {
        return users;
    }
}
