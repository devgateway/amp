package org.digijava.module.aim.form;

import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
/**
 * 
 * @author dan
 *
 */
public class TranslatorManagerForm extends ActionForm {
	
	TreeSet languages;
	TreeSet importedLanguages;
	String [] selectedLanguages;
	String [] selectedImportedLanguages;
	String [] overwriteTrn;
	FormFile fileUploaded;
	String[] keywords;

	

	public String[] getSelectedLanguages() {
		return selectedLanguages;
	}

	public void setSelectedLanguages(String[] selectedLanguages) {
		this.selectedLanguages = selectedLanguages;
	}

	public TreeSet getLanguages() {
		return languages;
	}

	public void setLanguages(TreeSet languages) {
		this.languages = languages;
	}

	public FormFile getFileUploaded() {
		return fileUploaded;
	}

	public void setFileUploaded(FormFile fileUploaded) {
		this.fileUploaded = fileUploaded;
	}

	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		super.reset(arg0, arg1);
		languages=null;
		importedLanguages=null;
		selectedLanguages=null;
		fileUploaded=null;
		selectedImportedLanguages=null;
		overwriteTrn=null;
		keywords=null;
	}

	public String[] getSelectedImportedLanguages() {
		return selectedImportedLanguages;
	}

	public void setSelectedImportedLanguages(String[] selectedImportedLanguages) {
		this.selectedImportedLanguages = selectedImportedLanguages;
	}

	public TreeSet getImportedLanguages() {
		return importedLanguages;
	}

	public void setImportedLanguages(TreeSet importedLanguages) {
		this.importedLanguages = importedLanguages;
	}

	public String[] getOverwriteTrn() {
		return overwriteTrn;
	}

	public void setOverwriteTrn(String[] overwriteTrn) {
		this.overwriteTrn = overwriteTrn;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	
}