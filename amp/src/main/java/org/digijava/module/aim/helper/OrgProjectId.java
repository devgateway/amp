package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpOrganisation;

import java.io.Serializable;

public class OrgProjectId implements Serializable, Comparable
{

    
    private Long id;
    private String projectId;
    private AmpOrganisation organisation;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    public AmpOrganisation getOrganisation() {
        return organisation;
    }
    public void setOrganisation(AmpOrganisation organisation) {
        this.organisation = organisation;
    }
    
    public int compareTo(Object o) {
        return this.organisation.getName().compareTo(((OrgProjectId) o).organisation.getName());
    }

    
}
