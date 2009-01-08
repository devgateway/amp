/**
 * 
 */
package org.digijava.module.dataExchange.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author dan
 *
 */
public class ImportForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1898016793971504413L;
	
	private FormFile uploadedFile;

	public FormFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(FormFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

}
