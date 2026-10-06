package org.digijava.module.aim.dbentity ;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.currency.CurrencyEPConstants;
import org.digijava.module.common.util.DateTimeUtil;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * https://jira.dgfoundation.org/browse/AMP-20534, https://jira.dgfoundation.org/browse/AMP-20923
 * @author Dolghier Constantin
 *
 */
public class AmpInflationRate implements Serializable, Comparable<AmpInflationRate>
{
    private static final long serialVersionUID = 1L;
    
    public final static int MIN_DEFLATION_YEAR = ArConstants.MIN_SUPPORTED_YEAR;
    public final static int MAX_DEFLATION_YEAR = ArConstants.MAX_SUPPORTED_YEAR;
    public final static String MIN_DATE_STR = MIN_DEFLATION_YEAR + "-01-01";
    public final static String MAX_DATE_STR = MAX_DEFLATION_YEAR + "-12-31";
    public final static Date MIN_DATE = DateTimeUtil.parseDate(MIN_DATE_STR, CurrencyEPConstants.DATE_FORMAT);
    public final static Date MAX_DATE = DateTimeUtil.parseDate(MAX_DATE_STR, CurrencyEPConstants.DATE_FORMAT);

    private Long id;
    
    /**
     * the currency related to which the inflation rate is specified
     */
    @NotNull
    private AmpCurrency currency;

    /**
     * the Gregorian period start date for which we specify the inflation rate compared with the previous period
     */
    private Date periodStart;
    
    /**
     * inflation rate expressed as a percentage change of the prices from previous period. <br />
     * Thus, inflationRate = -50% means prices have halved while inflationRate = +100% means prices have doubled
     */
    private double inflationRate;
    
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
