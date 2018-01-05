/*
 * CurrencyRates.java
 * Created: 30-Apr-2005
 */

package org.digijava.module.aim.helper;


import java.util.Date;

public class CurrencyRates {
    private Long id;
    private String currencyCode;
    private String currencyName;
    private String fromCurrencyCode;
    private String fromCurrencyName;
    private Date exchangeRateDateAsDate;
    private String exchangeRateDate;
    private Double exchangeRate;
    
    public CurrencyRates() {}
    
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
    /**
     * @return Returns the exchangeRateDate.
     */
    public String getExchangeRateDate() {
        return exchangeRateDate;
    }
    /**
     * @param exchangeRateDate The exchangeRateDate to set.
     */
    public void setExchangeRateDate(String exchangeRateDate) {
        this.exchangeRateDate = exchangeRateDate;
    }

    public Date getExchangeRateDateAsDate() {
        return exchangeRateDateAsDate;
    }

    public void setExchangeRateDateAsDate(Date exchangeRateDateAsDate) {
        this.exchangeRateDateAsDate = exchangeRateDateAsDate;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    
    
    /**
     * @return the fromCurrencyCode
     */
    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    /**
     * @param fromCurrencyCode the fromCurrencyCode to set
     */
    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }

    /**
     * @return the fromCurrencyName
     */
    public String getFromCurrencyName() {
        return fromCurrencyName;
    }

    /**
     * @param fromCurrencyName the fromCurrencyName to set
     */
    public void setFromCurrencyName(String fromCurrencyName) {
        this.fromCurrencyName = fromCurrencyName;
    }

    public String getformattedRate(){
        
        if (this.exchangeRate!=null){
        if (this.exchangeRate!=0){
           return FormatHelper.formatNumber(this.exchangeRate);
        }    
        }
        
        return null;
    }
    public String getRateNotRounded(){
       if (this.exchangeRate!=null){
           if (this.exchangeRate!=0){
               return FormatHelper.formatNumberNotRounded(this.exchangeRate);
            }    
        }
        return null;        
    }
}
