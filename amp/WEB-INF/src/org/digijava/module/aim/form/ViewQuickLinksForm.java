package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class ViewQuickLinksForm
    extends ActionForm {
    private Collection memberLinks;

    public ViewQuickLinksForm() {
    }

    public Collection getMemberLinks() {
        return memberLinks;
    }

    public void setMemberLinks(Collection memberLinks) {
        this.memberLinks = memberLinks;
    }
}
