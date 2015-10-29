package org.dgfoundation.amp.nireports;

import java.math.BigInteger;
import org.joda.time.DateTime;

/**
 * <strong>immutable</strong> representation of a transaction
 * @author Dolghier Constantin
 *
 */
public class MonetaryAmount {
	public final BigInteger amount;
	
	/**
	 * will be null for summed-up transactions
	 */
	public final BigInteger origAmount;
	
	/**
	 * will be null for summed-up transactions
	 */
	public final String origCurrency;
	
	/**
	 * withTimeAtStartOfDay()
	 */
	public final DateTime date;
	
	public MonetaryAmount(BigInteger amount, BigInteger origAmount, String origCurrency, DateTime date) {
		this.amount = amount;
		this.origAmount = origAmount;
		this.origCurrency = origCurrency;
		this.date = date;
	}
	
	public MonetaryAmount(BigInteger amount) {
		this(amount, null, null, null);
	}
	
	public MonetaryAmount multiplyBy(BigInteger other) {
		return new MonetaryAmount(amount.multiply(other), origAmount, origCurrency, null);
	}
	
	public MonetaryAmount add(MonetaryAmount other) {
		return new MonetaryAmount(amount.add(other.amount));
	}
}
