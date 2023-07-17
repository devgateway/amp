package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_AHSURVEY_CALC_FORMULA")
public class AmpAhsurveyIndicatorCalcFormula implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_ahsurvey_calc_formula_seq_generator")
    @SequenceGenerator(name = "amp_ahsurvey_calc_formula_seq_generator", sequenceName = "AMP_AHSURVEY_CALC_FORMULA_seq", allocationSize = 1)
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

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "amp_indicator_id")
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
