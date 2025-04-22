package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

public class DonorDataImport extends ActionForm {

    private String type;
    public String getType() {
        return (this.type);
    }

    public void setType(String type) {
        this.type = type;
    }
}
