package org.digijava.module.aim.dbentity ;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AmpCurrencyRate
{
    private Long ampCurrencyRateId;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private Double exchangeRate;
    private Date exchangeRateDate;
    private Integer dataSource;
    
    public AmpCurrencyRate() {};
    
    public AmpCurrencyRate(String fromCurrencyCode, String toCurrencyCode, double exchangeRate, Date exchangeRateDate, int dataSource) {
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
        this.exchangeRate = exchangeRate;
        this.exchangeRateDate = exchangeRateDate;
        this.dataSource = dataSource;
    }

    @Override public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("from %s to %s on %s equals %.4f", fromCurrencyCode, toCurrencyCode, sdf.format(this.exchangeRateDate), exchangeRate);
    }
    
    /**
     * @return
     */
    public Long getAmpCurrencyRateId() {
        return ampCurrencyRateId;
    }

    /**
     * @param long1
     */
    public void setAmpCurrencyRateId(Long long1) {
        ampCurrencyRateId = long1;
    }
        
    public String getfromCurrencyCode(){
        return fromCurrencyCode;
    }
    public String getToCurrencyCode(){
        return toCurrencyCode;
    }
    
    public void setFromCurrencyCode(String s1) {
        fromCurrencyCode = s1;
    }
    
    public void setToCurrencyCode(String s1) {
        toCurrencyCode = s1;
    }
    
    /**
     * @return
     */
    public Double getExchangeRate() {
        return exchangeRate;
    }

    public Date getExchangeRateDate() {
        return exchangeRateDate;
    }

    /**
     * @return
     */
    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    /**
     * @param double1
     */
    public void setExchangeRate(Double double1) {
        exchangeRate = double1;
    }

    public void setExchangeRateDate(Date d) {
        exchangeRateDate = d;
    }

    public Integer getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(Integer dataSource) {
        this.dataSource = dataSource;
    }

}   
