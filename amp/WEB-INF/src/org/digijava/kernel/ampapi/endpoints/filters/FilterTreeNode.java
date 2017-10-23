package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FilterTreeNode {

    private Long id;
    private String name;
    private String acronym;
    private String code;
    
    @JsonProperty(FiltersConstants.LIST_TREE_DEFINITION_IDS)
    private List<Long> treeIds;
    
    private List<FilterTreeNode> children;

    public void addChild(FilterTreeNode child) {
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

    public List<FilterTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<FilterTreeNode> children) {
        this.children = children;
    }
    
    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public List<Long> getTreeIds() {
        return treeIds;
    }

    public void setTreeIds(List<Long> treeIds) {
        this.treeIds = treeIds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
