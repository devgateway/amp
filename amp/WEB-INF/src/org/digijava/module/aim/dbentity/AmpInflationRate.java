package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * https://jira.dgfoundation.org/browse/AMP-20534, https://jira.dgfoundation.org/browse/AMP-20923
 * @author Dolghier Constantin
 *
 */
public class AmpInflationRate implements Serializable, Comparable<AmpInflationRate>
{
	private static final long serialVersionUID = 1L;
	
	public final static int MIN_DEFLATION_YEAR = 1970;
	public final static int MAX_DEFLATION_YEAR = 2050;

	private Long id;
	
	/**
	 * the currency related to which the inflation rate is specified
	 */
	@NotNull
	private AmpCurrency baseCurrency;

	/**
	 * the Gregorian year for which we specify the inflation rate compared with the previous year on
	 */
	private int year;
	
	/**
	 * inflation rate expressed as a percentage of the prices of the previous year. <br />
	 * Thus, inflationRate = -50% means prices have halved while inflationRate = +100% means prices have doubled
	 */
	private double inflationRate;
	
	/**
	 * whether for this currency and this year one should create a constant currency
	 */
	private boolean constantCurrency;
	
	public AmpInflationRate() {}
	
	public AmpInflationRate(AmpCurrency baseCurrency, int year, double inflationRate, boolean constantCurrency) {
		this.baseCurrency = baseCurrency;
		this.year = year;
		this.inflationRate = inflationRate;
		this.constantCurrency = constantCurrency;
	}
	
	@Override public boolean equals(Object obj) {
		if (obj instanceof AmpInflationRate) {
			return this.compareTo((AmpInflationRate) obj) == 0;
		}
		return false;
	}
	
	@Override public int compareTo(AmpInflationRate other) {
		if (!this.baseCurrency.equals(other.baseCurrency))
			return this.baseCurrency.compareTo(other.baseCurrency);
		
		if (this.year != other.year)
			return this.year - other.year;
		
		return Double.compare(this.inflationRate, other.inflationRate);
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public AmpCurrency getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(AmpCurrency baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getInflationRate() {
		return inflationRate;
	}

	public void setInflationRate(double inflationRate) {
		this.inflationRate = inflationRate;
	}

	public boolean isConstantCurrency() {
		return constantCurrency;
	}

	public void setConstantCurrency(Boolean constantCurrency) {
		this.constantCurrency = constantCurrency != null && constantCurrency;
	}

	/**
	 * copies all the fields except id from an another instance
	 * @param air
	 */
	public void importDataFrom(AmpInflationRate air) {
		this.baseCurrency = air.baseCurrency;
		this.constantCurrency = air.constantCurrency;
		this.inflationRate = air.inflationRate;
		this.year = air.year;
	}

	@AssertTrue()
	private boolean isValid() {
		return (this.inflationRate > -100) && (this.baseCurrency != null) && (!this.baseCurrency.isVirtual()) && (this.year >= MIN_DEFLATION_YEAR) && (this.year <= MAX_DEFLATION_YEAR);
	}
	
	@Override public String toString() {
		return String.format("currency: %s, year: %d, rate: %.2f, constant: %s", baseCurrency.getCurrencyCode(), this.year, this.inflationRate, this.constantCurrency);
	}
}	
