/**
 * 
 */
package org.digijava.module.dataExchange.type;

/**
 * A pair of iatiCodeName and iatiCodeValue.
 * 
 * Note: we hack import mappings to get iatiCodeName, so we may not always be able to identify iatiCodeValue
 * @author nmandrescu
 */
public class IatiCode {
	private String codeName = null;
	private String codeValue = null;
	
	public IatiCode() {
		
	}
	
	public IatiCode(String iatiCodeName, String iatiCodeValue) {
		this.codeName = iatiCodeName;
		this.codeValue = iatiCodeValue;
	}
	/**
	 * @return the iati code name
	 */
	public String getCodeName() {
		return codeName;
	}
	/**
	 * @param iatiCodeName the iati code name to set
	 */
	public void setCodeName(String iatiCodeName) {
		this.codeName = iatiCodeName;
	}

	/**
	 * @return the iati code value
	 */
	public String getCodeValue() {
		return codeValue;
	}

	/**
	 * @param iatiCodeValue the iati code value to set
	 */
	public void setCodeValue(String iatiCodeValue) {
		this.codeValue = iatiCodeValue;
	}
}
