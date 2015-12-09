package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import org.joda.time.DateTime;

/**
 * <strong>immutable</strong> representation of a transaction
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
	
	public MonetaryAmount(BigDecimal amount, BigDecimal origAmount, NiCurrency origCurrency, LocalDate date) {
		this.amount = amount;
		this.origAmount = origAmount;
		this.origCurrency = origCurrency;
		this.date = date;
	}
	
	public MonetaryAmount(BigDecimal amount) {
		this(amount, null, null, null);
	}
	
	public MonetaryAmount multiplyBy(BigDecimal other) {
		return new MonetaryAmount(amount.multiply(other), origAmount, origCurrency, null);
	}
	
	public MonetaryAmount add(MonetaryAmount other) {
		return new MonetaryAmount(amount.add(other.amount));
	}

	@Override
	public int compareTo(MonetaryAmount o) {
		return amount.compareTo(o.amount);
	}
	
	@Override
	public String toString() {
		return amount.toString();
	}
}
