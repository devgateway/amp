/**
 * 
 */
package org.digijava.module.budgetexport.util;

import java.util.Map;

/**
 * @author Alex Gartner
 *
 */
public abstract class MappingRetriever {
    private Long projectId;
    private String viewName;
    /**
     * @return the projectId
     */
    public Long getProjectId() {
        return projectId;
    }
    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    /**
     * @return the viewName
     */
    public String getViewName() {
        return viewName;
    }
    /**
     * @param viewName the viewName to set
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    public MappingRetriever(Long projectId, String viewName) {
        this.projectId = projectId;
        this.viewName = viewName;
    }
    
    public abstract Map<String, String> retrieveMapping();
    
    
}
