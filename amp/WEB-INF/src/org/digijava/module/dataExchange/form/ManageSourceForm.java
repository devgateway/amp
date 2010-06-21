/**
 * 
 */
package org.digijava.module.dataExchange.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author Alex Gartner
 *
 */
public class ManageSourceForm extends ActionForm {
	
	private Long selectedSourceId;
	
	
	//Source details properties come here
	private String name;
	private String source;
	private String strategy;
	private String importWorkspaceName;
	private List<String> fields;
	private String language;
	private String approvalStatus;
	private Long dbId;
	private String uniqueIdentifier;
	//END - Source details properties come here

	private Long executingSourceId			= null;
	private FormFile xmlFile				= null;
	
	private String action					= null;
	

	/**
	 * @return the selectedSourceId
	 */
	public Long getSelectedSourceId() {
		return selectedSourceId;
	}

	/**
	 * @param selectedSourceId the selectedSourceId to set
	 */
	public void setSelectedSourceId(Long selectedSourceId) {
		this.selectedSourceId = selectedSourceId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the strategy
	 */
	public String getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	/**
	 * @return the importWorkspaceName
	 */
	public String getImportWorkspaceName() {
		return importWorkspaceName;
	}

	/**
	 * @param importWorkspaceName the importWorkspaceName to set
	 */
	public void setImportWorkspaceName(String importWorkspaceName) {
		this.importWorkspaceName = importWorkspaceName;
	}

	/**
	 * @return the fields
	 */
	public List<String> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
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
	 * @return the xmlFile
	 */
	public FormFile getXmlFile() {
		return xmlFile;
	}

	/**
	 * @param xmlFile the xmlFile to set
	 */
	public void setXmlFile(FormFile xmlFile) {
		this.xmlFile = xmlFile;
	}

	/**
	 * @return the executingSourceId
	 */
	public Long getExecutingSourceId() {
		return executingSourceId;
	}

	/**
	 * @param executingSourceId the executingSourceId to set
	 */
	public void setExecutingSourceId(Long executingSourceId) {
		this.executingSourceId = executingSourceId;
	}

	/**
	 * @return the dbId
	 */
	public Long getDbId() {
		return dbId;
	}

	/**
	 * @param dbId the dbId to set
	 */
	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
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
	
	
	
}
