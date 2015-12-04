package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.util.Date;

import org.dgfoundation.amp.nireports.CurrencyConvertor;

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
	public BigDecimal getRate(String fromCurrency, String toCurrency, Date date) {
		return BigDecimal.ONE;
	}

}
