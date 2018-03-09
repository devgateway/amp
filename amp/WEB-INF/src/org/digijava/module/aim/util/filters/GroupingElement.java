package org.digijava.module.aim.util.filters;

import org.digijava.module.aim.util.HierarchyListable;



public class GroupingElement <T extends HierarchyListable> {

    /**
     * Consider using enum here
     */
    public static final String GROUPING_ELEMENT_FIELD_TYPE_DATE = "Date";


    private String name;
    private String htmlDivId;
    private T rootHierarchyListable;
    private String actionFormProperty;
    private String fieldType;
    
    public GroupingElement() {

    }
    
    public GroupingElement(String name, String htmlDivId, T rootHierarchyLIstable, String actionFormProperty) {
        this(name, htmlDivId, rootHierarchyLIstable, actionFormProperty, null);
    }

    public GroupingElement(String name, String htmlDivId, T rootHierarchyLIstable, String actionFormProperty, String fieldType) {
        this.name                   = name;
        this.htmlDivId              = htmlDivId;
        this.rootHierarchyListable  = rootHierarchyLIstable;
        this.actionFormProperty     = actionFormProperty;
        this.fieldType = fieldType;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the htmlDivId
     */
    public String getHtmlDivId() {
        return htmlDivId;
    }
    /**
     * @param htmlDivId the htmlDivId to set
     */
    public void setHtmlDivId(String htmlDivId) {
        this.htmlDivId = htmlDivId;
    }
    /**
     * @return the rootHierarchyListable
     */
    public T getRootHierarchyListable() {
        return rootHierarchyListable;
    }
    /**
     * @param rootHierarchyListable the rootHierarchyListable to set
     */
    public void setRootHierarchyListable(T rootHierarchyListable) {
        this.rootHierarchyListable = rootHierarchyListable;
    }
    /**
     * @return the actionFormProperty
     */
    public String getActionFormProperty() {
        return actionFormProperty;
    }
    /**
     * @param actionFormProperty the actionFormProperty to set
     */
    public void setActionFormProperty(String actionFormProperty) {
        this.actionFormProperty = actionFormProperty;
    }

    /**
     *
     * @return
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     *
     * @param fieldType
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
