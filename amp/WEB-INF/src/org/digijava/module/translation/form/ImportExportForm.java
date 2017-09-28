package org.digijava.module.translation.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * Form for translations import export action in admin menu of AMP.
 * Replaces form in AIM module with name TranslatorManagerForm.java
 * uses same field names to have minimal changes in JSP's.
 * AMP-9085 
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class ImportExportForm extends ActionForm {
    

    private static final long serialVersionUID = 1L;

    private List<String> languages;
    private List<String> importedLanguages;
    private String [] selectedLanguages;
    private String [] selectedImportedLanguages;
    private String [] overwriteTrn;
    private FormFile fileUploaded;
    private String[] keywords;
    private String skipOrUpdateTrnsWithKeywords;
    private int exportFormat;
    private String[] errors;
    private boolean exportAmpOfflineTranslationsOnly;

    
    public int getExportFormat() {
        return exportFormat;
    }
    public void setExportFormat(int exportFormat) {
        this.exportFormat = exportFormat;
    }
    public List<String> getLanguages() {
        return languages;
    }
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
    public List<String> getImportedLanguages() {
        return importedLanguages;
    }
    public void setImportedLanguages(List<String> importedLanguages) {
        this.importedLanguages = importedLanguages;
    }
    public String[] getSelectedLanguages() {
        return selectedLanguages;
    }
    public void setSelectedLanguages(String[] selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }
    public String[] getSelectedImportedLanguages() {
        return selectedImportedLanguages;
    }
    public void setSelectedImportedLanguages(String[] selectedImportedLanguages) {
        this.selectedImportedLanguages = selectedImportedLanguages;
    }
    public String[] getOverwriteTrn() {
        return overwriteTrn;
    }
    public void setOverwriteTrn(String[] overwriteTrn) {
        this.overwriteTrn = overwriteTrn;
    }
    public FormFile getFileUploaded() {
        return fileUploaded;
    }
    public void setFileUploaded(FormFile fileUploaded) {
        this.fileUploaded = fileUploaded;
    }
    public String[] getKeywords() {
        return keywords;
    }
    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }
    public String getSkipOrUpdateTrnsWithKeywords() {
        return skipOrUpdateTrnsWithKeywords;
    }
    public void setSkipOrUpdateTrnsWithKeywords(String skipOrUpdateTrnsWithKeywords) {
        this.skipOrUpdateTrnsWithKeywords = skipOrUpdateTrnsWithKeywords;
    }

    public boolean isExportAmpOfflineTranslationsOnly() {
        return exportAmpOfflineTranslationsOnly;
    }

    public void setExportAmpOfflineTranslationsOnly(boolean exportAmpOfflineTranslationsOnly) {
        this.exportAmpOfflineTranslationsOnly = exportAmpOfflineTranslationsOnly;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        languages=null;
        importedLanguages=null;
        selectedLanguages=null;
        fileUploaded=null;
        selectedImportedLanguages=null;
        overwriteTrn=null;
        keywords=null;
        skipOrUpdateTrnsWithKeywords=null;
        exportAmpOfflineTranslationsOnly = false;
    }
    public String[] getErrors() {
        return errors;
    }
    public void setErrors(String[] errors) {
        this.errors = errors;
    }


}
