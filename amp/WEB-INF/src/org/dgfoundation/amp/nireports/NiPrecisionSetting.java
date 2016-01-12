package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;

/**
 * an interface which governs the way precision is handled in a given NiReport
 * @author Dolghier Constantin
 *
 */
public interface NiPrecisionSetting {
	public BigDecimal adjustPrecision(BigDecimal src);
	
	public final static NiPrecisionSetting IDENTITY_PRECISION_SETTING = (z -> z); 
}
