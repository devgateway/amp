package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

public class SaveFmDescriptionForm extends ActionForm {
    private Long id;
    private String description;
    private String objectVisibility;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectVisibility() {
        return objectVisibility;
    }

    public void setObjectVisibility(String objectVisibility) {
        this.objectVisibility = objectVisibility;
    }

}
