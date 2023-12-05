package org.digijava.module.translation.form;

import org.apache.struts.action.ActionForm;


public class TranslationCleanupForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private int deleteBeforeDate;
    public int getDeleteBeforeDate() {
        return deleteBeforeDate;
    }
    public void setDeleteBeforeDate(int deleteBeforeDate) {
        this.deleteBeforeDate = deleteBeforeDate;
    }   
}
