
package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.DbUtil;

public class AmpPerspective {
	private Long ampPerspectiveId;
	private String name;
	private String code;
	/**
	 * @return Returns the ampPerspectiveId.
	 */
	public Long getAmpPerspectiveId() {
		return ampPerspectiveId;
	}
	/**
	 * @param ampPerspectiveId The ampPerspectiveId to set.
	 */
	public void setAmpPerspectiveId(Long ampPerspectiveId) {
		this.ampPerspectiveId = ampPerspectiveId;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getNameTrimmedForTrn(){
		String s=DbUtil.getTrnMessage("aim:"+this.getName().replaceAll(" ",""));
		if(s!=null) return s;
		return this.getName();
	}
}