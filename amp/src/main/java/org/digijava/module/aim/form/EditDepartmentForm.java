package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

public class EditDepartmentForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String depname;
    private String depcode;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDepname() {
        return depname;
    }
    public void setDepname(String depname) {
        this.depname = depname;
    }
    public String getDepcode() {
        return depcode;
    }
    public void setDepcode(String depcode) {
        this.depcode = depcode;
    }
    
}
