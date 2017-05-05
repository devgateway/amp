package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;

import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.TranslatedDate;

/**
 * <strong>This class is deeply immutable</strong>
 * 
 * @author Viorel Chihai
 *
 */
public final class GPIAmount  {
		
	/**
	 * the amount stored in this cell, plus some accessory information like the date of the transaction.
	 */
	public final MonetaryAmount amount;
	
	/**
	 * the effective date 
	 */
	public final TranslatedDate translatedDate;
	
	public final String donorName;
	public final String donorGroup;
	
	public GPIAmount(String donorName, String donorGroup, MonetaryAmount amount, TranslatedDate translatedDate) {
		this.donorName = donorName;
		this.donorGroup = donorGroup;
		this.amount = amount;
		this.translatedDate = translatedDate;
	}

	public TranslatedDate getTranslatedDate() {
		return translatedDate;
	}

	public int compareTo(Object o) {
		GPIAmount gac = (GPIAmount) o;
		return amount.compareTo(gac.amount);
	}

	public String getDisplayedValue() {
		return amount.getDisplayable();
	}

	public BigDecimal getAmount() {
		return amount.amount;
	}
	
	public String getDonorName() {
		return donorName;
	}
	
	public String getDonorGroup() {
		return donorGroup;
	}
	
	public NiPrecisionSetting getPrecision() {
		return amount.precisionSetting;
	}

	public boolean isScalableByUnits() {
		return true;
	}
	
	public String getYear() {
		return translatedDate.year.getValue();
	}
}
