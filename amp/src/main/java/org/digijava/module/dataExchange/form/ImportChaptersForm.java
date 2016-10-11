/**
 * ImportChaptersForm.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.dataExchange.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *
 */
public class ImportChaptersForm extends ActionForm {
	private FormFile uploadedFile;
	private Integer chaptersInserted;
	private Integer chaptersUpdated;
	private Integer imputationsInserted;
	private Integer imputationsUpdated;
	private Integer errorNumber;
	private boolean importPerform=false;
	
	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		super.reset(arg0, arg1);
		uploadedFile=null;
		chaptersInserted=null;
		chaptersUpdated=null;
		imputationsInserted=null;
		imputationsUpdated=null;
		errorNumber=null;
		importPerform=false;
	}

	/**
	 * @return the uploadedFile
	 */
	public FormFile getUploadedFile() {
		return uploadedFile;
	}

	/**
	 * @param uploadedFile the uploadedFile to set
	 */
	public void setUploadedFile(FormFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	/**
	 * @return the chaptersInserted
	 */
	public Integer getChaptersInserted() {
		return chaptersInserted;
	}

	/**
	 * @param chaptersInserted the chaptersInserted to set
	 */
	public void setChaptersInserted(Integer chaptersInserted) {
		this.chaptersInserted = chaptersInserted;
	}

	/**
	 * @return the chaptersUpdated
	 */
	public Integer getChaptersUpdated() {
		return chaptersUpdated;
	}

	/**
	 * @param chaptersUpdated the chaptersUpdated to set
	 */
	public void setChaptersUpdated(Integer chaptersUpdated) {
		this.chaptersUpdated = chaptersUpdated;
	}

	/**
	 * @return the imputationsInserted
	 */
	public Integer getImputationsInserted() {
		return imputationsInserted;
	}

	/**
	 * @param imputationsInserted the imputationsInserted to set
	 */
	public void setImputationsInserted(Integer imputationsInserted) {
		this.imputationsInserted = imputationsInserted;
	}

	/**
	 * @return the imputationsUpdated
	 */
	public Integer getImputationsUpdated() {
		return imputationsUpdated;
	}

	/**
	 * @param imputationsUpdated the imputationsUpdated to set
	 */
	public void setImputationsUpdated(Integer imputationsUpdated) {
		this.imputationsUpdated = imputationsUpdated;
	}

	/**
	 * @return the errorNumber
	 */
	public Integer getErrorNumber() {
		return errorNumber;
	}

	/**
	 * @param errorNumber the errorNumber to set
	 */
	public void setErrorNumber(Integer errorNumber) {
		this.errorNumber = errorNumber;
	}

	/**
	 * @return the importPerform
	 */
	public boolean isImportPerform() {
		return importPerform;
	}

	/**
	 * @param importPerform the importPerform to set
	 */
	public void setImportPerform(boolean importPerform) {
		this.importPerform = importPerform;
	}

}
