package org.digijava.module.aim.dbentity ;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AMP_CURRENCY_RATE")
public class AmpCurrencyRate
{
    @Id
    @GeneratedValue(generator = "ampCurrencyRateSeq")
    @SequenceGenerator(name = "ampCurrencyRateSeq", sequenceName = "AMP_CURRENCY_RATE_seq", allocationSize = 1)
    @Column(name = "amp_currency_rate_id")
    private Long ampCurrencyRateId;

    @Column(name = "from_currency_code", nullable = false)
    private String fromCurrencyCode;

    @Column(name = "to_currency_code", nullable = false)
    private String toCurrencyCode;

    @Column(name = "exchange_rate", nullable = false)
    private Double exchangeRate;

    @Column(name = "exchange_rate_date", nullable = false)
    private Date exchangeRateDate;

    @Column(name = "data_source")
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
