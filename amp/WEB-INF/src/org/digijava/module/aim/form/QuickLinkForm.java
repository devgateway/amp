package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

public class QuickLinkForm
    extends ActionForm {
    private String tempId;
    private String linkName;
    private String link;
    private String action;
    private Long id = null;

    public QuickLinkForm() {
    }

    public String getAction() {
        return action;
    }

    public Long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getLinkName() {
        return linkName;
    }

    public String getTempId() {
        return tempId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }
}
