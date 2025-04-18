package org.digijava.kernel.translator.form;

import org.apache.struts.action.ActionForm;

public class AjaxTranslatorForm extends ActionForm {

    private String originalText;

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

}
