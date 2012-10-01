package org.digijava.module.esrigis.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class StructuresImporterForm extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FormFile uploadedFile;
	private boolean importPerform=false;
	
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
	
	
	
}
