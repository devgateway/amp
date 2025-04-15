package org.digijava.module.esrigis.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class StructuresImporterForm extends ActionForm{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FormFile uploadedFile;
    private boolean importPerform=false;
    private ArrayList<String> errors;
    
    public ArrayList<String> getErrors() {
        return errors;
    }
    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }
    public FormFile getUploadedFile() {
        return uploadedFile;
    }
    public void setUploadedFile(FormFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    public boolean isImportPerform() {
        return importPerform;
    }
    public void setImportPerform(boolean importPerform) {
        this.importPerform = importPerform;
    }
    
     public void reset(ActionMapping mapping, HttpServletRequest request) {
         this.errors = null;
     }
    
    
}
