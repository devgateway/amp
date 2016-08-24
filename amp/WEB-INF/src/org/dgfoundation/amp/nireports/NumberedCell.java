package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;

import org.dgfoundation.amp.newreports.ReportSettings;

/**
 * a Cell which has a number. The number can be a percentage or simple amount
 * @author Dolghier Constantin
 *
 */
public interface NumberedCell extends Comparable {
	
	public BigDecimal getAmount();
	public NiPrecisionSetting getPrecision();
	
	/**
	 * whether this cell should respond to {@link ReportSettings#getUnitsOption()}
	 * @return
	 */
	public boolean isScalableByUnits();
	
	@Override
	public default int compareTo(Object oth) {
		NumberedCell other = (NumberedCell) oth;
		return getAmount().compareTo(other.getAmount());
	}
}
