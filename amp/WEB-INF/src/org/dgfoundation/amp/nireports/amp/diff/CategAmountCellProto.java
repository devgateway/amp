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
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.digijava.module.aim.dbentity.AmpCurrency;

/**
 * a "proto" CategAmountCell which contains all the data necessary to translate a transaction to {@link CategAmountCell} once one has been gives a Calendar and a Currency
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
	}
	
	public CategAmountCell materialize(AmpCurrency usedCurrency, CachingCalendarConverter calendarConverter, CurrencyConvertor currencyConvertor, NiPrecisionSetting precisionSetting) {
		BigDecimal usedExchangeRate = BigDecimal.valueOf(currencyConvertor.getExchangeRate(origCurrency.getCurrencyCode(), usedCurrency.getCurrencyCode(), fixed_exchange_rate == null ? null : fixed_exchange_rate.doubleValue(), transactionDate));
		MonetaryAmount amount = new MonetaryAmount(origAmount.multiply(usedExchangeRate), origAmount, origCurrency, transactionDate, precisionSetting);
		CategAmountCell cell = new CategAmountCell(activityId, amount, metaInfo, coordinates, calendarConverter.translate(transactionMoment));
		return cell;
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
		return String.format("CategAmountCellProto, actId: %d, %d %s on %s", activityId, this.origAmount, origCurrency.getCurrencyCode(), this.transactionDate);
	}
	
	@Override
	public String toString() {
		return String.format("(actId: %d, amt: %s %s, coos: {%s}, meta: {%s}", this.activityId, origAmount, origCurrency.getCurrencyCode(), AmpCollections.sortedMap(coordinates, (a, b) -> a.toString().compareTo(b.toString())), metaInfo);
	}
}
