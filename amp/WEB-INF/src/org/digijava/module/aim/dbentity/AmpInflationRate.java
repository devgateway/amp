package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.currency.CurrencyEPConstants;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * https://jira.dgfoundation.org/browse/AMP-20534, https://jira.dgfoundation.org/browse/AMP-20923
 * @author Dolghier Constantin
 *
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_INFLATION_RATES")
public class AmpInflationRate implements Serializable, Comparable<AmpInflationRate>
{
    private static final long serialVersionUID = 1L;
    
    public final static int MIN_DEFLATION_YEAR = ArConstants.MIN_SUPPORTED_YEAR;
    public final static int MAX_DEFLATION_YEAR = ArConstants.MAX_SUPPORTED_YEAR;
    public final static String MIN_DATE_STR = MIN_DEFLATION_YEAR + "-01-01";
    public final static String MAX_DATE_STR = MAX_DEFLATION_YEAR + "-12-31";
    public final static Date MIN_DATE = DateTimeUtil.parseDate(MIN_DATE_STR, CurrencyEPConstants.DATE_FORMAT);
    public final static Date MAX_DATE = DateTimeUtil.parseDate(MAX_DATE_STR, CurrencyEPConstants.DATE_FORMAT);
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_INFLATION_RATE_seq")
    @SequenceGenerator(name = "AMP_INFLATION_RATE_seq", sequenceName = "AMP_INFLATION_RATE_seq", allocationSize = 1)
    @Column(name = "amp_inflation_rate_id")
    private Long id;

    @Column(name = "period_start", unique = true)
    private Date periodStart;

    @Column(name = "inflation_rate")
    private Double inflationRate;

    @ManyToOne
    @JoinColumn(name = "currency_id", unique = true, nullable = false)
    @NotNull

    private AmpCurrency currency;

    


    public AmpInflationRate() {}
    
    public AmpInflationRate(AmpCurrency currency, Date periodStart, double inflationRate) {
        this.currency = currency;
        this.periodStart = periodStart;
        this.inflationRate = inflationRate;
    }
    
    @Override public boolean equals(Object obj) {
        if (obj instanceof AmpInflationRate) {
            return this.compareTo((AmpInflationRate) obj) == 0;
        }
        return false;
    }
    
    @Override public int compareTo(AmpInflationRate other) {
        if (!this.currency.equals(other.currency))
            return this.currency.compareTo(other.currency);
        
        if (this.periodStart.equals(other.periodStart))
            return this.periodStart.compareTo(other.periodStart);
        
        return Double.compare(this.inflationRate, other.inflationRate);
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public AmpCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(AmpCurrency currency) {
        this.currency = currency;
    }
    
    public Date getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart;
    }

    public double getInflationRate() {
        return inflationRate;
    }

    public void setInflationRate(double inflationRate) {
        this.inflationRate = inflationRate;
    }

    /**
     * copies all the fields except id from an another instance
     * @param air
     */
    public void importDataFrom(AmpInflationRate air) {
        this.currency = air.currency;
        this.inflationRate = air.inflationRate;
        this.periodStart = air.periodStart;
    }

    @AssertTrue()
    private boolean isValid() {
        boolean res = (this.inflationRate > -100) && (this.currency != null) && (!this.currency.isVirtual()) && (!this.periodStart.before(MIN_DATE)) && (!this.periodStart.after(MAX_DATE));
        return res;
    }
    
    @Override public String toString() {
        return String.format("currency: %s, year: %d, rate: %.2f, constant: %s", currency.getCurrencyCode(), this.periodStart, this.inflationRate);
    }
}   
