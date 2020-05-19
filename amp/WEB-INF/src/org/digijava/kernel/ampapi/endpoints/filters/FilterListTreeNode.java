package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FilterListTreeNode {
    
    @ApiModelProperty(value = "The id of the filter item", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "The name of the filter item", example = "Belgium")
    private String name;
    
    @ApiModelProperty(value = "The acronym of the filter item. Used for organisation names.", example = "Belgium")
    private String acronym;
    
    @ApiModelProperty(value = "The code of the filter item. Used for organisation groups.", example = "AUG")
    private String code;
    
    @ApiModelProperty(value = "The translated name", example = "Belgium")
    private String value;

    @ApiModelProperty(value = "The translated description ( use in case we want to add tooltips to a filter item)",
            example = "Eastern Europe and Central Asia")
    private String description;

    private List<Long> listDefinitionIds;
    
    private List<FilterListTreeNode> children;

    @JsonProperty("extra_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object extraInfo;

    public FilterListTreeNode() {

    }

    public FilterListTreeNode(Long id, String value) {
        this(id, value, null);
    }

    public FilterListTreeNode(Long id, String value, Object extraInfo) {
        this(id, value, value, extraInfo);
    }

    public FilterListTreeNode(Long id, String value, String name) {
        this(id, value, name, null);
    }

    public FilterListTreeNode(Long id, String value, String name, Object extraInfo) {
        super();
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public void addChild(FilterListTreeNode child) {
        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(child);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FilterListTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<FilterListTreeNode> children) {
        this.children = children;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public List<Long> getListDefinitionIds() {
        return listDefinitionIds;
    }

    public void setListDefinitionIds(List<Long> listDefinitionIds) {
        this.listDefinitionIds = listDefinitionIds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Object extraInfo) {
        this.extraInfo = extraInfo;
    }
}
