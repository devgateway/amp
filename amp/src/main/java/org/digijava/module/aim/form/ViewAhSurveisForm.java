package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

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
