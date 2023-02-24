package org.digijava.module.translation.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.translation.util.ContentTrnObjectType;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 9/23/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContentTrnImportExportForm extends ActionForm {
    Collection<ContentTrnObjectType> existingTrnTypes;
    String[] selectedContentTypes;
    FormFile upFile;

    public Collection<ContentTrnObjectType> getExistingTrnTypes() {
        return existingTrnTypes;
    }

    public void setExistingTrnTypes(Collection<ContentTrnObjectType> existingTrnTypes) {
        this.existingTrnTypes = existingTrnTypes;
    }

    public String[] getSelectedContentTypes() {
        return selectedContentTypes;
    }

    public void setSelectedContentTypes(String[] selectedContentTypes) {
        this.selectedContentTypes = selectedContentTypes;
    }

    public FormFile getUpFile() {
        return upFile;
    }

    public void setUpFile(FormFile upFile) {
        this.upFile = upFile;
    }
}
