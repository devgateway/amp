package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;

/**
 * @author Viorel Chihai
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"ws-member-ids", "fields"})
public class APIWorkspaceMemberFieldList {

    @JsonProperty(ActivityEPConstants.API_WS_MEMBER_IDS)
    private List<Long> wsMemberIds;

    @JsonProperty(ActivityEPConstants.API_FIELDS)
    private List<APIField> fields;

    public APIWorkspaceMemberFieldList() {
        super();
    }

    public APIWorkspaceMemberFieldList(List<Long> wsMemberIds, List<APIField> fields) {
        super();
        this.wsMemberIds = wsMemberIds;
        this.fields = fields;
    }

    public List<Long> getWsMemberIds() {
        return wsMemberIds;
    }

    public void setWsMemberIds(List<Long> wsMemberIds) {
        this.wsMemberIds = wsMemberIds;
    }

    public List<APIField> getFields() {
        return fields;
    }

    public void setFields(List<APIField> fields) {
        this.fields = fields;
    }

}
