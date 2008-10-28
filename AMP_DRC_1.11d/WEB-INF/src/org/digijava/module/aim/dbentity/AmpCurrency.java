package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.util.Identifiable;

public class AmpCurrency implements Serializable,Comparable, Identifiable
{
	private Long ampCurrencyId;
	private String currencyCode;
	private String countryName;
	private String currencyName;
	private Country countryId;
	private Integer activeFlag;

	/**
	 * @return Returns the activeFlag.
	 */
	public Integer getActiveFlag() {
		return activeFlag;
	}
	/**
	 * @param activeFlag The activeFlag to set.
	 */
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	/**
	 * @return Returns the ampCurrencyId.
	 */
	public Long getAmpCurrencyId() {
		return ampCurrencyId;
	}
	/**
	 * @param ampCurrencyId The ampCurrencyId to set.
	 */
	public void setAmpCurrencyId(Long ampCurrencyId) {
		this.ampCurrencyId = ampCurrencyId;
	}
	/**
	 * @return Returns the countryId.
	 */
	public Country getCountryId() {
		return countryId;
	}
	/**
	 * @param countryId The countryId to set.
	 */
	public void setCountryId(Country countryId) {
		this.countryId = countryId;
	}
	/**
	 * @return Returns the countryName.
	 */
	public String getCountryName() {
		return countryName;
	}
	/**
	 * @param countryName The countryName to set.
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	/**
	 * @return Returns the currencyCode.
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode The currencyCode to set.
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/**
	 * @return Returns the currencyName.
	 */
	public String getCurrencyName() {
		return currencyName;
	}
	/**
	 * @param currencyName The currencyName to set.
	 */
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		if (obj instanceof AmpCurrency) {
			AmpCurrency curr = (AmpCurrency) obj;
			return curr.getCurrencyCode().equals(this.currencyCode);
		}
		
		return false;
		
	}
	
	public int compareTo(Object obj) {
		if (obj == null) throw new NullPointerException();
		
		if (obj instanceof AmpCurrency) {
			AmpCurrency curr = (AmpCurrency) obj;
			return (this.currencyCode.compareTo(curr.getCurrencyCode()));
		} else {
			throw new ClassCastException();
		}
	}
	public Object getIdentifier() {
		return this.getAmpCurrencyId();
	}
	
	public String toString() {
		return currencyCode;
	}
}	
