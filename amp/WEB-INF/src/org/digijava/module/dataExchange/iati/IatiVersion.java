/**
 * 
 */
package org.digijava.module.dataExchange.iati;


import java.math.BigDecimal;

/**
 * Keeps the list of IatiVersions supported in AMP 
 * @author Nadejda Mandrescu
 */ 
public enum IatiVersion {
	V_1_01("1.01"),
	V_1_02("1.02"),
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
		case "1.01": return V_1_01;
		case "1.02": return V_1_02;
		case "1.03": return V_1_03;
		case "1.04": return V_1_04;
		default: return null;
		}
	}

    /**
     * Jaxb parses version as BigDecimal
     * @param value
     * @return
     */
    public static IatiVersion getValueOf(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return getValueOf(value.toString());
    }
}
