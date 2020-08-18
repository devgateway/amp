package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterListTreeNode {

    private Long id;
    private String name;
    private String acronym;
    private String code;
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
