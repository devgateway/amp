package org.dgfoundation.amp.nireports.testcases;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.dgfoundation.amp.nireports.NiPrecisionSetting;

public class TestcasesPrecisionSetting implements NiPrecisionSetting {

    @Override
    public BigDecimal adjustPrecision(BigDecimal src) {
        return src.setScale(6, RoundingMode.HALF_EVEN);
    }
}
