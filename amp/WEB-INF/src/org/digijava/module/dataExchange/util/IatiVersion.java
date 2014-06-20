/**
 * 
 */
package org.digijava.module.dataExchange.util;


/**
 * Keeps the list of IatiVersions supported in AMP
 * @author nmandrescu
 *
 */
public enum IatiVersion {
	V_1_03("1.03"),
	V_1_04("1.04");
	
	private String value;
	
	IatiVersion(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() { 
		return this.value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public static IatiVersion getValueOf(String value) {
		switch(value) {
		case "1.03": return V_1_03;
		case "1.04": return V_1_04;
		default: return null;
		}
	}
}
