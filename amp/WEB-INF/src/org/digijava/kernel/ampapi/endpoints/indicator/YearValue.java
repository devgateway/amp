package org.digijava.kernel.ampapi.endpoints.indicator;

import java.math.BigDecimal;

public class YearValue {

    private int year;

    private BigDecimal value;

    public YearValue(int year, BigDecimal value) {
        this.year = year;
        this.value = value;
    }

    public int getYear() {
        return year;
    }

    public BigDecimal getValue() {
        return value;
    }
}
