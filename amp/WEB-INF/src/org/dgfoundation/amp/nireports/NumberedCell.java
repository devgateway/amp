package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;

/**
 * a Cell which has a number. The number can be a percentage or simple amount
 * @author Dolghier Constantin
 *
 */
public interface NumberedCell extends Comparable {
	public BigDecimal getAmount();
	public NiPrecisionSetting getPrecision();
	
	@Override
	public default int compareTo(Object oth) {
		NumberedCell other = (NumberedCell) oth;
		return getAmount().compareTo(other.getAmount());
	}
}
