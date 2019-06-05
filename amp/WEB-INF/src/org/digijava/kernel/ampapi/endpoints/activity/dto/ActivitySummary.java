package org.digijava.kernel.ampapi.endpoints.activity.dto;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601TimeStampSerializer;
import org.digijava.module.aim.dbentity.AmpActivityGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Nadejda Mandrescu
 */
@JsonPropertyOrder({ "internal_id", "amp_id", "project_title", "iati_identifier", "creation_date", "update_date",
    "activity_group", "workspaces_edit", ActivityEPConstants.EDIT, ActivityEPConstants.VIEW })
@ApiModel(description =
        "Provides a brief summary of the activity and clarifies if the user from session can view or edit it.")
public class ActivitySummary {

    @JsonProperty("internal_id")
    @ApiModelProperty(dataType = "java.lang.Long")
    private Object ampActivityId;

    @JsonProperty("amp_id")
    @ApiModelProperty(example = "4532580543")
    private String ampId;

    @JsonProperty("project_title")
    private MultilingualContent name;

    @JsonProperty("iati_identifier")
    @ApiModelProperty(example = "AA-AAA-123456789")
    private String iatiIdentifier;

    @JsonSerialize(using = ISO8601TimeStampSerializer.class)
    @JsonProperty("creation_date")
    @ApiModelProperty(example = "2019-04-12T09:32:38.922+0000")
    private Date createdDate;

    @JsonSerialize(using = ISO8601TimeStampSerializer.class)
    @JsonProperty("update_date")
    @ApiModelProperty(example = "2019-04-12T09:32:38.922+0000")
    private Date updatedDate;

    @JsonProperty("activity_group")
    @JsonView(ActivityView.Import.class)
    private AmpActivityGroup ampActivityGroup;

    @JsonProperty(ActivityEPConstants.EDIT)
    private boolean editable;

    @JsonProperty(ActivityEPConstants.VIEW)
    private boolean viewable;

    @JsonProperty("workspaces_edit")
    @JsonView(ActivityView.List.class)
    private Set<Long> workspaces;

    public Object getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(Object ampActivityId) {
        this.ampActivityId = ampActivityId;
    }

    public String getAmpId() {
        return ampId;
    }

    public void setAmpId(String ampId) {
        this.ampId = ampId;
    }

    public MultilingualContent getName() {
        return name;
    }

    public void setName(MultilingualContent name) {
        this.name = name;
    }

    public String getIatiIdentifier() {
        return iatiIdentifier;
    }

    public void setIatiIdentifier(String iatiIdentifier) {
        this.iatiIdentifier = iatiIdentifier;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public AmpActivityGroup getAmpActivityGroup() {
        return ampActivityGroup;
    }

    public void setAmpActivityGroup(AmpActivityGroup ampActivityGroup) {
        this.ampActivityGroup = ampActivityGroup;
    }

    public Set<Long> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(Set<Long> workspaces) {
        this.workspaces = workspaces;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isViewable() {
        return viewable;
    }

    public void setViewable(boolean viewable) {
        this.viewable = viewable;
    }

}
