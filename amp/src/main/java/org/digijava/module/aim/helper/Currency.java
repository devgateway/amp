/*
 * Currency.java
 */

package org.digijava.module.aim.helper;


public class Currency {
    private Long currencyId;
    private Long currencyRateId;
    private String currencyCode;
    private String countryName;
    private Double exchangeRate;
    private String currencyName;
    
    public Currency() {}

    public Currency(Long currencyId, Long currencyRateId, String currencyCode,
            String countryName, Double exchangeRate,String currencyName) {

        setCurrencyId(currencyId);
        setCurrencyRateId(currencyRateId);
        setCurrencyCode(currencyCode);
        setCountryName(countryName);
        setExchangeRate(exchangeRate);
        setCurrencyName(currencyName);
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
     * @return Returns the currencyId.
     */
    public Long getCurrencyId() {
        return currencyId;
    }
    /**
     * @param currencyId The currencyId to set.
     */
    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
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
    /**
     * @return Returns the currencyRateId.
     */
    public Long getCurrencyRateId() {
        return currencyRateId;
    }
    /**
     * @param currencyRateId The currencyRateId to set.
     */
    public void setCurrencyRateId(Long currencyRateId) {
        this.currencyRateId = currencyRateId;
    }
    /**
     * @return Returns the exchangeRate.
     */
    public Double getExchangeRate() {
        return exchangeRate;
    }
    /**
     * @param exchangeRate The exchangeRate to set.
     */
    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
