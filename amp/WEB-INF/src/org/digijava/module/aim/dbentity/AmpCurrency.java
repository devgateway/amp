package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Identifiable;
import org.hibernate.Query;

@TranslatableClass (displayName = "Currency")
public class AmpCurrency implements Serializable,Comparable, Identifiable
{
	@Interchangeable(fieldTitle="ID")
	private Long ampCurrencyId;
	@Interchangeable(fieldTitle="Currency Code")
	private String currencyCode;
	@Interchangeable(fieldTitle="Country Name")
    private String countryName;
	@Interchangeable(fieldTitle="Currency Name")
	@TranslatableField
    private String currencyName;
	@Interchangeable(fieldTitle="Country Location")
    private AmpCategoryValueLocations countryLocation;
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
	 * @return the countryLocation
	 */
	public AmpCategoryValueLocations getCountryLocation() {
		return countryLocation;
	}
	/**
	 * @param countryLocation the countryLocation to set
	 */
	public void setCountryLocation(AmpCategoryValueLocations countryLocation) {
		this.countryLocation = countryLocation;
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
	
	/**
	 * cache of result of calling _is_rate_cache
	 */
	Boolean _is_rate_cache = null;
	
	/**
	 * Returns true iff currency has at least one active exchange rate against the base currency
	 * @param currencyCode currency Code
	 * @return boolean
	 * @author Irakli Kobiashvili
	 */
	public boolean isRate()
	{
		if (_is_rate_cache != null)
			return _is_rate_cache;
		
		try {
			String baseCurrencyCode = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
			if (currencyCode.equalsIgnoreCase(baseCurrencyCode)){
				return true;
			}
		
			String queryString = "SELECT COUNT(*) FROM " + AmpCurrencyRate.class.getName() + " f " + 
								"WHERE (f.fromCurrencyCode = :currencyCode AND f.toCurrencyCode = :baseCurrencyCode) " + 
								"OR (f.fromCurrencyCode = :baseCurrencyCode AND f.toCurrencyCode = :currencyCode) AND f.exchangeRate IS NOT NULL";
			
			Query q = PersistenceManager.getRequestDBSession().createQuery(queryString);
			q.setString("currencyCode", currencyCode);
			q.setString("baseCurrencyCode", baseCurrencyCode);
			long cnt = PersistenceManager.getLong(q.uniqueResult());
			boolean result = (cnt > 0);
			_is_rate_cache = result;
			return result;
		} catch (Exception ex) {			
			throw new RuntimeException("Error retriving currency exchange rate for "+ currencyCode,ex);
		}
	}
}	
