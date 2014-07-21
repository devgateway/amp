package org.digijava.module.dataExchange.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.dataExchange.action.ImportActionNew;
import org.digijava.module.dataExchange.dbentity.*;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.IatiActivity;

import javax.servlet.ServletRequest;
import java.io.File;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 3/12/14
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportFormNew extends ActionForm {
    private int page = ImportActionNew.IATI_IMPORT_PAGE_UPLOAD;

    AmpDEUploadSession upSess;
    private FormFile file;
    private List<DESourceSetting> configurations;
    private Long configurationId;
    private List<AbstractMap.SimpleEntry <String, String>> countryList;
    private String[] selCountries;
    private Set<Map.Entry <String, String>> languageList;
    private String[] selLanguages;
    private String defaultLanguage;
    private Map <IatiActivity, Set<DEMappingFields>> iatiImportedProjectMap;
    private Map <IatiActivity, Set<DEMappingFields>> iatiImportedProjectMapFiltered;
    private Map <String, Set<IatiActivity>> countryActMap;
    private TreeSet<String> ampClasses;
    private String selAmpClass;
    Map <String, Set<DEMappingFields>> groupFldsByPath;
    List<AmpDEUploadSession> uploadSessions;
    Set<DEMappingFields> selFlds;
    Long objId; //General.
    List<DELogPerItem> logItems;

    public void resetForm() {
        upSess = null;
        FormFile file = null;
        configurations = null;
        configurationId = null;
        countryList = null;
        selCountries = null;
        iatiImportedProjectMap = null;
        iatiImportedProjectMapFiltered = null;
        countryActMap = null;
        ampClasses = null;
        selAmpClass = null;
        groupFldsByPath = null;
        uploadSessions = null;
        selFlds = null;
        objId = null;
        logItems = null;
    }

    public List<DELogPerItem> getLogItems() {
        return logItems;
    }

    public void setLogItems(List<DELogPerItem> logItems) {
        this.logItems = logItems;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public List<AmpDEUploadSession> getUploadSessions() {
        return uploadSessions;
    }

    public void setUploadSessions(List<AmpDEUploadSession> uploadSessions) {
        this.uploadSessions = uploadSessions;
    }

    public AmpDEUploadSession getUpSess() {
        return upSess;
    }

    public void setUpSess(AmpDEUploadSession upSess) {
        this.upSess = upSess;
    }

    public Set<DEMappingFields> getSelFlds() {
        return selFlds;
    }

    public void setSelFlds(Set<DEMappingFields> selFlds) {
        this.selFlds = selFlds;
    }

    public Map<String, Set<DEMappingFields>> getGroupFldsByPath() {
        return groupFldsByPath;
    }

    public void setGroupFldsByPath(Map<String, Set<DEMappingFields>> groupFldsByPath) {
        this.groupFldsByPath = groupFldsByPath;
    }

    public String getSelAmpClass() {
        return selAmpClass;
    }

    public void setSelAmpClass(String selAmpClass) {
        this.selAmpClass = selAmpClass;
    }

    public TreeSet<String> getAmpClasses() {
        return ampClasses;
    }

    public void setAmpClasses(TreeSet<String> ampClasses) {
        this.ampClasses = ampClasses;
    }

    public Map<IatiActivity, Set<DEMappingFields>> getIatiImportedProjectMapFiltered() {
        return iatiImportedProjectMapFiltered;
    }

    public void setIatiImportedProjectMapFiltered(Map<IatiActivity, Set<DEMappingFields>> iatiImportedProjectMapFiltered) {
        this.iatiImportedProjectMapFiltered = iatiImportedProjectMapFiltered;
    }

    public Map<IatiActivity, Set<DEMappingFields>> getIatiImportedProjectMap() {
        return iatiImportedProjectMap;
    }

    public void setIatiImportedProjectMap(Map<IatiActivity, Set<DEMappingFields>> iatiImportedProjectMap) {
        this.iatiImportedProjectMap = iatiImportedProjectMap;
    }

    public Map<String, Set<IatiActivity>> getCountryActMap() {
        return countryActMap;
    }

    public void setCountryActMap(Map<String, Set<IatiActivity>> countryActMap) {
        this.countryActMap = countryActMap;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<AbstractMap.SimpleEntry<String, String>> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<AbstractMap.SimpleEntry<String, String>> countryList) {
        this.countryList = countryList;
    }

    public String[] getSelCountries() {
        return selCountries;
    }

    public void setSelCountries(String[] selCountries) {
        this.selCountries = selCountries;
    }

    /**
	 * @return full list of languages detected in imported XML
	 */
	public Set<Map.Entry <String, String>> getLanguageList() {
		return languageList;
	}

	/**
	 * @param languageList - full list of languages detected in imported XML
	 */
	public void setLanguageList(Set<Map.Entry <String, String>> languageList) {
		this.languageList = languageList;
	}

	/**
	 * @return the selected languages for import
	 */
	public String[] getSelLanguages() {
		return selLanguages;
	}

	/**
	 * @param selLanguages - selected languages for import
	 */
	public void setSelLanguages(String[] selLanguages) {
		this.selLanguages = selLanguages;
	}

	/**
	 * @return the default language to be used if no language is detected for an element
	 */
	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	/**
	 * @param defaultLanguge the default language to be used if no language is detected for an element
	 */
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    public List<DESourceSetting> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<DESourceSetting> configurations) {
        this.configurations = configurations;
    }

    public Long getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Long configurationId) {
        this.configurationId = configurationId;
    }
}
