package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.List;

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
    
    private List<Long> listDefinitionIds;
    
    private List<FilterListTreeNode> children;
    
    public FilterListTreeNode() { }
    
    public FilterListTreeNode(Long id, String value) {
        this(id, value, value);
    }

    public FilterListTreeNode(Long id, String value, String name) {
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
    
}
