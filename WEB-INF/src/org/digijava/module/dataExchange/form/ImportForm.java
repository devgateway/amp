/**
 * 
 */
package org.digijava.module.dataExchange.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.dataExchange.dbentity.AmpDEImportLog;

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
	private String[] selectedLanguages;
	private String[] languages;
	
	private String[] options;
	private String[] selectedOptions;
	
	private AmpDEImportLog activityTree = null;

	public String[] getSelectedLanguages() {
		return selectedLanguages;
	}

	public void setSelectedLanguages(String[] selectedLanguages) {
		this.selectedLanguages = selectedLanguages;
	}

	public FormFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(FormFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String[] getLanguages() {
		return languages;
	}

	public void setLanguages(String[] languages) {
		this.languages = languages;
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public String[] getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(String[] selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

	public AmpDEImportLog getActivityTree() {
		return activityTree;
	}

	public void setActivityTree(AmpDEImportLog activityTree) {
		this.activityTree = activityTree;
	}



}
