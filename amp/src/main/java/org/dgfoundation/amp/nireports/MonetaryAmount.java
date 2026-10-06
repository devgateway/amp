package org.dgfoundation.amp.nireports;

import org.digijava.module.common.util.DateTimeUtil;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * <strong>immutable</strong> representation of a transaction. Contains an amount, a date, a precision setting + some fields kept mainly for debugging or "what if some schema will need it" reasons. 
 * @author Dolghier Constantin
 *
 */
public class MonetaryAmount implements Comparable<MonetaryAmount> {
    
    public final BigDecimal amount;
    
    /**
     * will be null for summed-up transactions
     */
    public final BigDecimal origAmount;
    
    /**
     * will be null for summed-up transactions
     */
    public final NiCurrency origCurrency;
    
    /**
     * withTimeAtStartOfDay()
     */
    public final LocalDate date;
    
    public final NiPrecisionSetting precisionSetting;
        
    public MonetaryAmount(BigDecimal amount, BigDecimal origAmount, NiCurrency origCurrency, LocalDate date, NiPrecisionSetting precisionSetting) {
        NiUtils.failIf(origAmount == null ^ origCurrency == null, "orgAmount and origCurrency must either be both null or both nonnull");
        this.amount = precisionSetting.adjustPrecision(amount);
        this.origAmount = origAmount;
        this.origCurrency = origCurrency;
        this.date = date;
        this.precisionSetting = precisionSetting;       
    }
    
    public MonetaryAmount(BigDecimal amount, NiPrecisionSetting precisionSetting) {
        this(amount, null, null, null, precisionSetting);
    }
    
    public MonetaryAmount multiplyBy(BigDecimal other) {
        return new MonetaryAmount(amount.multiply(other), origAmount, origCurrency, this.date, precisionSetting);
    }
    
    public MonetaryAmount add(MonetaryAmount other) {
        return new MonetaryAmount(amount.add(other.amount), precisionSetting);
    }

    @Override
    public int compareTo(MonetaryAmount o) {
        int amountsDelta = amount.compareTo(o.amount);
        if (amountsDelta != 0)
            return amountsDelta;
        
        if (date != null || o.date != null) {
            // at least one of the dates is not null
            if (date == null)
                return 1;
            if (o.date == null)
                return -1;
            int datesDelta = date.compareTo(o.date);
            if (datesDelta != 0)
                return datesDelta;
        }
        return 0;
    }
    
    public long getJulianDayCode() {
        return DateTimeUtil.toJulianDayNumber(date);
    }
    
    /**
     * amount as should be displayed in the report
     * @return
     */
    public String getDisplayable() {
        return amount.stripTrailingZeros().toPlainString();
    }
    
    @Override
    public String toString() {
        return String.format("%s %s", amount.stripTrailingZeros().toPlainString(), date == null ? "(no date)" : "on " + date.toString());
    }
}
