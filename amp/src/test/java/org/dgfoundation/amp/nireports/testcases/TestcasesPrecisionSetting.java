package org.dgfoundation.amp.nireports.testcases;

import org.dgfoundation.amp.nireports.NiPrecisionSetting;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestcasesPrecisionSetting implements NiPrecisionSetting {

    @Override
    public BigDecimal adjustPrecision(BigDecimal src) {
        return src.setScale(6, RoundingMode.HALF_EVEN);
    }
}
