package org.digijava.module.aim.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;

public class AddCurrencyForm extends ActionForm {
    
    private Long currencyId;
    private Long currencyRateId;
    private String currencyCode;
    private String countryName;
    private Double exchangeRate;
    private Date exchangeRateDate; 
    private String flag;

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
    /**
     * @return Returns the flag.
     */
    public String getFlag() {
        return flag;
    }
    /**
     * @param flag The flag to set.
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }
}
