package org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard;

import java.math.BigDecimal;

public class DashboardCoreIndicatorValue {
    private String indicator;
    private Long indicator_id;
    private BigDecimal targetValue;
    private BigDecimal actualValue;
    private DashboardCoreIndicatorType coreIndicatorType;

    public BigDecimal getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(BigDecimal targetValue) {
        this.targetValue = targetValue;
    }

    public BigDecimal getActualValue() {
        return actualValue;
    }

    public void setActualValue(BigDecimal actualValue) {
        this.actualValue = actualValue;
    }

    public DashboardCoreIndicatorType getCoreIndicatorType() {
        return coreIndicatorType;
    }

    public void setCoreIndicatorType(DashboardCoreIndicatorType coreIndicatorType) {
        this.coreIndicatorType = coreIndicatorType;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public Long getIndicator_id() {
        return indicator_id;
    }

    public void setIndicator_id(Long indicator_id) {
        this.indicator_id = indicator_id;
    }
}
