package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpResourceManagerSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -653148030798254713L;
	/**
	 * 
	 */

	private Long ampResourceManagerSettingsId;
	
	private String sortColumn;
	private Integer maximunFileSize;
	private boolean limitFileToUpload;
	public Long getAmpResourceManagerSettingsId() {
		return ampResourceManagerSettingsId;
	}
	public void setAmpResourceManagerSettingsId(Long ampResourceManagerSettingsId) {
		this.ampResourceManagerSettingsId = ampResourceManagerSettingsId;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public Integer getMaximunFileSize() {
		return maximunFileSize;
	}
	public void setMaximunFileSize(Integer maximunFileSize) {
		this.maximunFileSize = maximunFileSize;
	}
	public boolean isLimitFileToUpload() {
		return limitFileToUpload;
	}
	public void setLimitFileToUpload(boolean limitFileToUpload) {
		this.limitFileToUpload = limitFileToUpload;
	}
	
	

}
