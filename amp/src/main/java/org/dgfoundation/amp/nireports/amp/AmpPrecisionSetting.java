package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.NiPrecisionSetting;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * the precision setting used by AMP.
 * NiReports does all the calculations in units; the results are translated in the requested multiplicity units at the output stages.
 * Hence, keeping 6 significant digits as a scale is a more-than-ok 
 * 
 * @author Dolghier Constantin
 *
 */
public class AmpPrecisionSetting implements NiPrecisionSetting {

    @Override
    public BigDecimal adjustPrecision(BigDecimal src) {
        return src.setScale(6, RoundingMode.HALF_EVEN);
    }
}
