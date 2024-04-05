package org.digijava.module.content.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.content.dbentity.AmpContentItem;

import java.util.Collection;

public class ContentManagerForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Collection<AmpContentItem> contents;

    public void setContents(Collection<AmpContentItem> contents) {
        this.contents = contents;
    }

    public Collection<AmpContentItem> getContents() {
        return contents;
    }

}
