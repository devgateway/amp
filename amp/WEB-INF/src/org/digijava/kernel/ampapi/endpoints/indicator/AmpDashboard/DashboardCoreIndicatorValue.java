package org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard;

public class DashboardCoreIndicatorValue {
    private Double targetValue;
    private Double actualValue;
    private DashboardCoreIndicatorType coreIndicatorType;

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public Double getActualValue() {
        return actualValue;
    }

    public void setActualValue(Double actualValue) {
        this.actualValue = actualValue;
    }

    public DashboardCoreIndicatorType getCoreIndicatorType() {
        return coreIndicatorType;
    }

    public void setCoreIndicatorType(DashboardCoreIndicatorType coreIndicatorType) {
        this.coreIndicatorType = coreIndicatorType;
    }

}
