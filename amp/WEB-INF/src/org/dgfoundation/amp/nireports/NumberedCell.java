package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;

/**
 * a Cell which has a number. The number can be a percentage or simple amount
 * @author Dolghier Constantin
 *
 */
public interface NumberedCell {
	public BigDecimal getAmount();
	public NiPrecisionSetting getPrecision();
}
