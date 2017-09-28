package org.digijava.module.aim.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
public class ListAppliedPatchesForm extends ActionForm {
    List patch = null;
    String content = null;

    public List getPatch() {
        return patch;
    }

    public void setPatch(List patch) {
        this.patch = patch;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
