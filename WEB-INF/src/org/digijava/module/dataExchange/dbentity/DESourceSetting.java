/**
 * 
 */
package org.digijava.module.dataExchange.dbentity;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpTeam;
/**
 * 
 *
 */
public class DESourceSetting {
	public final static String SOURCE_URL			= "URL";
	public final static String SOURCE_FILE			= "FILE";
	public final static String SOURCE_WEB_SERVICE	= "WEB_SERVICE";
	
	
	private Long id;
	
	/**
	 * Decides if it is an import or export source
	 */
	private Boolean isImport;
	
	/**
	 * 
	 */
	private String source;
	
	private AmpTeam importWorkspace;
	private String approvalStatus;
	private List  fields;
	private String uniqueIdentifier;
	private String uniqueIdentifierSeparator;
	
	private List exportFilters;
	private List<DELogPerExecution> logs;
	
	private String languageId;
	/**import option (dropdown):    
	                        1. add only new projects
	                        2. update only existing projects
	                        3. add new projects and update existing projects*/
	private String importStrategy;
	
	/**
	 * In case the source would be a URL
	 */
	private String url;

	/**
	 * @return the isImport
	 */
	public Boolean getIsImport() {
		return isImport;
	}

	/**
	 * @param isImport the isImport to set
	 */
	public void setIsImport(Boolean isImport) {
		this.isImport = isImport;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the importWorkspace
	 */
	public AmpTeam getImportWorkspace() {
		return importWorkspace;
	}

	/**
	 * @param importWorkspace the importWorkspace to set
	 */
	public void setImportWorkspace(AmpTeam importWorkspace) {
		this.importWorkspace = importWorkspace;
	}

	/**
	 * @return the approvalStatus
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}

	/**
	 * @param approvalStatus the approvalStatus to set
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	/**
	 * @return the fields
	 */
	public List getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List fields) {
		this.fields = fields;
	}

	/**
	 * @return the uniqueIdentifier
	 */
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	/**
	 * @param uniqueIdentifier the uniqueIdentifier to set
	 */
	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	/**
	 * @return the uniqueIdentifierSeparator
	 */
	public String getUniqueIdentifierSeparator() {
		return uniqueIdentifierSeparator;
	}

	/**
	 * @param uniqueIdentifierSeparator the uniqueIdentifierSeparator to set
	 */
	public void setUniqueIdentifierSeparator(String uniqueIdentifierSeparator) {
		this.uniqueIdentifierSeparator = uniqueIdentifierSeparator;
	}

	/**
	 * @return the exportFilters
	 */
	public List getExportFilters() {
		return exportFilters;
	}

	/**
	 * @param exportFilters the exportFilters to set
	 */
	public void setExportFilters(List exportFilters) {
		this.exportFilters = exportFilters;
	}

	/**
	 * @return the languageId
	 */
	public String getLanguageId() {
		return languageId;
	}

	/**
	 * @param languageId the languageId to set
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	/**
	 * @return the importStrategy
	 */
	public String getImportStrategy() {
		return importStrategy;
	}

	/**
	 * @param importStrategy the importStrategy to set
	 */
	public void setImportStrategy(String importStrategy) {
		this.importStrategy = importStrategy;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the logs
	 */
	public List<DELogPerExecution> getLogs() {
		return logs;
	}

	/**
	 * @param logs the logs to set
	 */
	public void setLogs(List<DELogPerExecution> logs) {
		this.logs = logs;
	}
	
	
	
}
