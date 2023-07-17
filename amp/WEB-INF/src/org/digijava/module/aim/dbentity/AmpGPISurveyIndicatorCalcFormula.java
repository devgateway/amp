package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_GPI_SURVEY_CALC_FORMULA")
public class AmpGPISurveyIndicatorCalcFormula implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_GPI_SURVEY_CALC_FORMULA_seq")
    @SequenceGenerator(name = "AMP_GPI_SURVEY_CALC_FORMULA_seq", sequenceName = "AMP_GPI_SURVEY_CALC_FORMULA_seq", allocationSize = 1)
    @Column(name = "formula_id")
    private Long id;

    @Column(name = "formula_text")
    private String calcFormula;

    @Column(name = "column_index")
    private Long columnIndex;

    @Column(name = "constant_name")
    private String constantName;

    @Column(name = "base_line_value")
    private String baseLineValue;

    @Column(name = "target_value")
    private String targetValue;

    @Column(name = "formula_status")
    private Boolean enabled;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "amp_indicator_id", nullable = false)
    private AmpGPISurveyIndicator parentIndicator;



    public AmpGPISurveyIndicatorCalcFormula() {
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

    public AmpGPISurveyIndicator getParentIndicator() {
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

    public void setParentIndicator(AmpGPISurveyIndicator parentIndicator) {
        this.parentIndicator = parentIndicator;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
