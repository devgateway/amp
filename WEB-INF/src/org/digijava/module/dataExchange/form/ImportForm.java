/**
 * 
 */
package org.digijava.module.dataExchange.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.dataExchange.dbentity.AmpDEImportLog;
import org.digijava.module.dataExchange.type.AmpColumnEntry;

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
	
	private List allActivitiesFromDB;
	
	public List getAllActivitiesFromDB() {
		return allActivitiesFromDB;
	}

	public void setAllActivitiesFromDB(List allActivitiesFromDB) {
		this.allActivitiesFromDB = allActivitiesFromDB;
	}

	private AmpDEImportLog activityTree = null;
	
	private AmpColumnEntry activityStructure = null;
	
	private String[] primaryKeys = null;

	public String[] getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(String[] primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public AmpColumnEntry getActivityStructure() {
		return activityStructure;
	}

	public void setActivityStructure(AmpColumnEntry activityStructure) {
		this.activityStructure = activityStructure;
	}

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
