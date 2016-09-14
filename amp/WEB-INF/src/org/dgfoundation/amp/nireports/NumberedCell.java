package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;

import org.dgfoundation.amp.newreports.ReportSettings;

/**
 * a Cell which contains a number. The number can be of any type (percentage, natural amount, etc)
 * @author Dolghier Constantin
 *
 */
public interface NumberedCell extends Comparable {
	
	public BigDecimal getAmount();
	public NiPrecisionSetting getPrecision();
	
	/**
	 * whether this cell should respond be scaled according to {@link ReportSettings#getUnitsOption()}
	 */
	public boolean isScalableByUnits();
	
	@Override
	public default int compareTo(Object oth) {
		NumberedCell other = (NumberedCell) oth;
		return getAmount().compareTo(other.getAmount());
	}
}
