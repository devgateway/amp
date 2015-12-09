package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.dgfoundation.amp.nireports.CurrencyConvertor;
import org.dgfoundation.amp.nireports.NiCurrency;

/**
 * TODO: this will be the currency convertor
 * @author Dolghier Constantin
 *
 */
public class AmpCurrencyConvertor implements CurrencyConvertor {

	public static AmpCurrencyConvertor getInstance() {
		return new AmpCurrencyConvertor();
	}
	
	@Override
	public BigDecimal getRate(NiCurrency fromCurrency, NiCurrency toCurrency, LocalDate date) {
		return BigDecimal.ONE;
	}

}
