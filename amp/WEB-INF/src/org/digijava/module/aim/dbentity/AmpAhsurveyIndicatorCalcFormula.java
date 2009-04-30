package org.digijava.module.aim.dbentity;

public class AmpAhsurveyIndicatorCalcFormula {

    private Long id;
    private String calcFormula;
    private Long columnIndex;
    private String constantName;
    private String baseLineValue;
    private String targetValue;
    private Boolean enabled;
    private AmpAhsurveyIndicator parentIndicator;


    public AmpAhsurveyIndicatorCalcFormula() {
    }

    public String getCalcFormula() {
        return calcFormula;
    }

    public String getConstantName() {
        return constantName;
    }

    public Long getId() {
        return id;
    }

    public Long getColumnIndex() {
        return columnIndex;
    }

    public String getBaseLineValue() {
        return baseLineValue;
    }

    public AmpAhsurveyIndicator getParentIndicator() {
        return parentIndicator;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setCalcFormula(String calcFormula) {
        this.calcFormula = calcFormula;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setColumnIndex(Long columnIndex) {
        this.columnIndex = columnIndex;
    }

    public void setBaseLineValue(String baseLineValue) {
        this.baseLineValue = baseLineValue;
    }

    public void setParentIndicator(AmpAhsurveyIndicator parentIndicator) {
        this.parentIndicator = parentIndicator;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
