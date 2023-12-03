package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;

/**
 * an interface which governs the way precision is handled in a given NiReport. It is an operation applied to intermediate results from time to time in order 
 * to keep BigDecimal instances from growing too large with irrelevant precision
 * @author Dolghier Constantin
 *
 */
public interface NiPrecisionSetting {
    public BigDecimal adjustPrecision(BigDecimal src);
    
    /**
     * the NOP precision setting - not recommended for production schemas unless speed is not an issue
     */
    public final static NiPrecisionSetting IDENTITY_PRECISION_SETTING = (z -> z); 
}
