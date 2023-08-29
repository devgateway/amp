package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.util.Collection;

public class ViewAhSurveisForm extends ActionForm {

    private Collection surveis;

    public ViewAhSurveisForm() {
    }

    public Collection getSurveis() {
        return surveis;
    }

    public void setSurveis(Collection surveis) {
        this.surveis = surveis;
    }
}
