
package org.digijava.module.aim.dbentity;

import java.io.Serializable;


public class AmpOrgGroup implements Serializable 
{
	private Long ampOrgGrpId;
	private String orgGrpName;
	private String orgGrpCode;
	private AmpLevel levelId;
	//private Set organizations; 
	/**
	 * @return Returns the ampOrgGrpId.
	 */
	public Long getAmpOrgGrpId() {
		return ampOrgGrpId;
	}
	/**
	 * @param ampOrgGrpId The ampOrgGrpId to set.
	 */
	public void setAmpOrgGrpId(Long ampOrgGrpId) {
		this.ampOrgGrpId = ampOrgGrpId;
	}
	/**
	 * @return Returns the levelId.
	 */
	public AmpLevel getLevelId() {
		return levelId;
	}
	/**
	 * @param levelId The levelId to set.
	 */
	public void setLevelId(AmpLevel levelId) {
		this.levelId = levelId;
	}
	/**
	 * @return Returns the orgGrpCode.
	 */
	public String getOrgGrpCode() {
		return orgGrpCode;
	}
	/**
	 * @param orgGrpCode The orgGrpCode to set.
	 */
	public void setOrgGrpCode(String orgGrpCode) {
		this.orgGrpCode = orgGrpCode;
	}
	/**
	 * @return Returns the orgGrpName.
	 */
	public String getOrgGrpName() {
		return orgGrpName;
	}
	/**
	 * @param orgGrpName The orgGrpName to set.
	 */
	public void setOrgGrpName(String orgGrpName) {
		this.orgGrpName = orgGrpName;
	}

}