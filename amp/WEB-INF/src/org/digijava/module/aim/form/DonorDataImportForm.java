package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

public class DonorDataImportForm extends ActionForm {

    private String type;
    private String fileName;
    public String getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
