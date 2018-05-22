package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Viorel Chihai
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "ws-member-ids", "fields" })
public class APIWorkspaceMemberFieldList {

    @JsonProperty(ActivityEPConstants.API_WS_MEMBER_IDS)
    private List<Long> wsMemberIds;

    @JsonProperty(ActivityEPConstants.API_FIELDS)
    private List<APIField> fields;
    
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
