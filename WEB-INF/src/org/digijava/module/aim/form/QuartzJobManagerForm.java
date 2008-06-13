package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

public class QuartzJobManagerForm extends ActionForm {
    private String msText;

    public QuartzJobManagerForm() {
    }

    public String getMsText() {
        return msText;
    }

    public void setMsText(String msText) {
        this.msText = msText;
    }
}
