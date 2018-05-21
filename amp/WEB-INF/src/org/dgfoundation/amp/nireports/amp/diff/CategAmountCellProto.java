package org.dgfoundation.amp.nireports.amp.diff;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.currencyconvertor.CurrencyConvertor;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.digijava.module.aim.dbentity.AmpCurrency;

/**
 * a "proto cell": a class which contains all the data necessary to translate a transaction to {@link CategAmountCell} once one has been given a Calendar, Locale and a Currency.
 * The process which transforms a {@link CategAmountCellProto} into a {@link CategAmountCell} is called <i>materialization</i> and is implemented in {@link #materialize(AmpCurrency, CachingCalendarConverter, CurrencyConvertor, NiPrecisionSetting)}
 * @author Dolghier Constantin
 *
 */
public class CategAmountCellProto extends Cell {
    public final MetaInfoSet metaInfo;
    public final BigDecimal origAmount;
    public final AmpCurrency origCurrency;
    public final BigDecimal fixed_exchange_rate;
    public final java.sql.Date transactionMoment;
    public final LocalDate transactionDate;
    
    public CategAmountCellProto(long activityId, BigDecimal origAmount, AmpCurrency origCurrency, java.sql.Date transactionMoment, MetaInfoSet metaInfo, Map<NiDimensionUsage, Coordinate> coos, BigDecimal fixed_exchange_rate) {
        super(activityId, -1, coos, Optional.empty());
        this.origAmount = origAmount;
        this.origCurrency = origCurrency;
        this.transactionMoment = transactionMoment;
        this.metaInfo = metaInfo;
        this.fixed_exchange_rate = fixed_exchange_rate;
        this.transactionDate = transactionMoment.toLocalDate();
        
        NiUtils.failIf(origAmount == null, String.format("Amount cannot be null for %s", this));
        NiUtils.failIf(origCurrency == null, String.format("Currency cannot be null for %s", this));
    }
    
    /**
     * materializes this instance into a full transaction. The operation is O(1) cheap because the heavyweight components of a cell are deeply immutable structures
     * which are shared between with the prototype (namely, {@link #metaInfo} and {@link #getCoordinates()})
     * @param usedCurrency the {@link AmpCurrency} to use for converting the natural transaction to
     * @param calendarConverter the O(1) {@link CalendarConverter} to use for translating the natural transaction's date
     * @param currencyConvertor the O(1) {@link CurrencyConvertor} to use for for converting amounts between currencies
     * @param precisionSetting the precision settings to use while doing the amount conversions and to store in the generated {@link MonetaryAmount}
     * @return
     */
    public CategAmountCell materialize(AmpCurrency usedCurrency, CachingCalendarConverter calendarConverter, CurrencyConvertor currencyConvertor, NiPrecisionSetting precisionSetting) {
        return materialize(usedCurrency, calendarConverter, currencyConvertor, precisionSetting, true);
    }
    
    /**
     * AMP-27571
     * Materializes this instance into a full transaction. 
     * The original currency will be set to the current used one, in order to group the cells by currency.
     * This method is called only when report.spec.showOriginalCurrency is set to true
     * 
     * @param usedCurr
     * @param calendarConverter
     * @param currencyConvertor
     * @param precisionSetting
     * @return
     */
    public CategAmountCell materialize(AmpCurrency usedCurr, CachingCalendarConverter calendarConverter, 
            CurrencyConvertor currencyConvertor, NiPrecisionSetting precisionSetting, boolean isAmountConverted) {
        
        BigDecimal convertedAmount = origAmount;
        
        if (isAmountConverted) {
            BigDecimal usedExchangeRate = BigDecimal.valueOf(currencyConvertor.getExchangeRate(
                    origCurrency.getCurrencyCode(), usedCurr.getCurrencyCode(), 
                    fixed_exchange_rate == null ? null : fixed_exchange_rate.doubleValue(), transactionDate));
            convertedAmount = origAmount.multiply(usedExchangeRate);
        }
        
        MonetaryAmount amount = new MonetaryAmount(convertedAmount, origAmount, origCurrency, transactionDate, 
                precisionSetting);
        
        CategAmountCell cell = new CategAmountCell(activityId, amount, metaInfo, coordinates, 
                calendarConverter.translate(transactionMoment), isAmountConverted);
        
        return cell;
    }

    @Override
    public CategAmountCellProto changeOwnerId(long newActivityId) {
        return new CategAmountCellProto(newActivityId, origAmount, origCurrency, transactionMoment, metaInfo, this.coordinates, fixed_exchange_rate);
    }

    @Override
    public int compareTo(Object o) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public MetaInfoSet getMetaInfo() {
        return this.metaInfo;
    }

    @Override
    public String getDisplayedValue() {
        return String.format("CategAmountCellProto, actId: %d, %d %s on %s", activityId, this.origAmount, origCurrency, this.transactionDate);
    }
    
    @Override
    public String toString() {
        return String.format("(actId: %d, amt: %s %s, coos: {%s}, meta: {%s}", this.activityId, origAmount, origCurrency, AmpCollections.sortedMap(coordinates, (a, b) -> a.toString().compareTo(b.toString())), metaInfo);
    }
}
