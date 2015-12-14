package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public interface CurrencyConvertor {
	public BigDecimal getExchangeRate(NiCurrency fromCurrency, NiCurrency toCurrency, LocalDate date);
}
