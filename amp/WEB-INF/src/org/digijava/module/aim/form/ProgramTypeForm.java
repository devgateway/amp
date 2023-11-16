package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.io.Serializable;
import java.util.Collection;

public class ProgramTypeForm extends ActionForm implements Serializable{

    private Collection programNames;
    private Long id;
    private String name;
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Collection getProgramNames() {
        return programNames;
    }
    public void setProgramNames(Collection programNames) {
        this.programNames = programNames;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
}
