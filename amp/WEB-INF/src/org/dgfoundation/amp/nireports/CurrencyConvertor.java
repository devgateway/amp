package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public interface CurrencyConvertor {
	public BigDecimal getRate(Date date);
}
