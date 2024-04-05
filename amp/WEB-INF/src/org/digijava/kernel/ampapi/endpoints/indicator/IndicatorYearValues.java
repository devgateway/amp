package org.digijava.kernel.ampapi.endpoints.indicator;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.digijava.module.aim.dbentity.AmpIndicator;

import java.math.BigDecimal;
import java.util.List;

public class IndicatorYearValues {

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "indicatorId")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("indicatorId")
    private AmpIndicator indicator;

    private BigDecimal baseValue;

    private List<YearValue> actualValues;

    private BigDecimal targetValue;

    public IndicatorYearValues(AmpIndicator indicator, BigDecimal baseValue, List<YearValue> actualValues,
                               BigDecimal targetValue) {
        this.indicator = indicator;
        this.baseValue = baseValue;
        this.actualValues = actualValues;
        this.targetValue = targetValue;
    }

    public AmpIndicator getIndicator() {
        return indicator;
    }

    public BigDecimal getBaseValue() {
        return baseValue;
    }

    public List<YearValue> getActualValues() {
        return actualValues;
    }

    public BigDecimal getTargetValue() {
        return targetValue;
    }
}
