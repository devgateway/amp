package org.digijava.module.esrigis.dbentity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author jdeanquin
 *
 */
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AMP_API_STATE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmpApiState implements Serializable {

    public static class BriefView {
    }

    public static class DetailView extends BriefView {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_api_state_seq")
    @SequenceGenerator(name = "amp_api_state_seq", sequenceName = "amp_api_state_seq", allocationSize = 1)
    @Column(name = "id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(BriefView.class)
    private Long id;

    @Column(name = "title")
    @ApiModelProperty(example = "Dashboard")
    @JsonView(BriefView.class)
    private String title;

    @Column(name = "description")
    @ApiModelProperty(example = "Saved dashboard")
    @JsonView(BriefView.class)
    private String description;

    @Column(name = "stateBlob", columnDefinition = "text")
    @ApiModelProperty(example = "{\"chart:/rest/dashboard/tops/do\":{\"limit\":5,\"adjtype\":\"Actual Commitments\","
            + "\"view\":\"bar\",\"big\":false},\"chart:/rest/dashboard/tops/dg\":{\"limit\":5,"
            + "\"adjtype\":\"Actual Commitments\",\"view\":\"bar\",\"big\":false}}")
    @JsonView(DetailView.class)
    private String stateBlob;

    @Column(name = "date_created")
    @JsonView(BriefView.class)
    @JsonProperty(value = "created", access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = ApiStateDateTimeSerializer.class)
    private Date createdDate;

    @Column(name = "date_updated")
    @JsonIgnore

    private Date updatedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @JsonIgnore

    private ApiStateType type;


    public AmpApiState(){
        
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getStateBlob() {
        return stateBlob;
    }
    public void setStateBlob(String stateBlob) {
        this.stateBlob = stateBlob;
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((createdDate == null) ? 0 : createdDate.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((stateBlob == null) ? 0 : stateBlob.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result
                + ((updatedDate == null) ? 0 : updatedDate.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AmpApiState other = (AmpApiState) obj;
        if (createdDate == null) {
            if (other.createdDate != null)
                return false;
        } else if (!createdDate.equals(other.createdDate))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (stateBlob == null) {
            if (other.stateBlob != null)
                return false;
        } else if (!stateBlob.equals(other.stateBlob))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (updatedDate == null) {
            if (other.updatedDate != null)
                return false;
        } else if (!updatedDate.equals(other.updatedDate))
            return false;
        return true;
    }

    public ApiStateType getType() {
        return type;
    }
    
    public void setType(ApiStateType type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "AmpApiState [id=" + id + ", title=" + title + ", description="
                + description + ", type=" + type + ", stateBlob=" + stateBlob
                + ", createdDate=" + createdDate + ", updatedDate="
                + updatedDate + "]";
    }
    
}
