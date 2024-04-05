package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * 
 * @author dan
 *
 */
@Deprecated
public class TranslatorManagerForm extends ActionForm {
    
    private static final long serialVersionUID = 2L;

    private List<String> languages;
    private List<String> importedLanguages;
    private String [] selectedLanguages;
    private String [] selectedImportedLanguages;
    private String [] overwriteTrn;
    private FormFile fileUploaded;
    private String[] keywords;
    private String skipOrUpdateTrnsWithKeywords;
    

    public String[] getSelectedLanguages() {
        return selectedLanguages;
    }

    public void setSelectedLanguages(String[] selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public FormFile getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(FormFile fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
        languages=null;
        importedLanguages=null;
        selectedLanguages=null;
        fileUploaded=null;
        selectedImportedLanguages=null;
        overwriteTrn=null;
        keywords=null;
        skipOrUpdateTrnsWithKeywords=null;
    }

    public String[] getSelectedImportedLanguages() {
        return selectedImportedLanguages;
    }

    public void setSelectedImportedLanguages(String[] selectedImportedLanguages) {
        this.selectedImportedLanguages = selectedImportedLanguages;
    }

    public List<String> getImportedLanguages() {
        return importedLanguages;
    }

    public void setImportedLanguages(List<String> importedLanguages) {
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

    public String getSkipOrUpdateTrnsWithKeywords() {
        return skipOrUpdateTrnsWithKeywords;
    }

    public void setSkipOrUpdateTrnsWithKeywords(String skipOrUpdateTrnsWithKeywords) {
        this.skipOrUpdateTrnsWithKeywords = skipOrUpdateTrnsWithKeywords;
    }
    
}
