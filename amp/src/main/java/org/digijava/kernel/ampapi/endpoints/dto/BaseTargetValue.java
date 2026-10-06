package org.digijava.kernel.ampapi.endpoints.dto;

import java.math.BigDecimal;

public class BaseTargetValue {
    private BigDecimal base;
    private BigDecimal target;

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }
}
