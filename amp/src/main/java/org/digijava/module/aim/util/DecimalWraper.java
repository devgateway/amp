package org.digijava.module.aim.util;

import java.math.BigDecimal;

import org.digijava.module.aim.helper.FormatHelper;

public class DecimalWraper {

    /**
     * 
     */
    
    public DecimalWraper() {
        this.value = new BigDecimal(0d);
    }

    private static final long serialVersionUID = 7549949655765988774L;

    private String calculations;

    private BigDecimal value;

    private String fromerate;

    private String torate;

    private String fromcurrency;

    private String tocurrency;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void add(BigDecimal value)
    {
        this.setValue(this.getValue().add(value));
    }
    
    public void add(DecimalWraper nr)
    {
        add(nr.getValue());
    }
    
    public String getCalculations() {
        return calculations;
    }

    public void setCalculations(String calculations) {
        this.calculations = calculations;
    }

    public Double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public String toString() {
        if (this.value != null) {
            return FormatHelper.formatNumber(this.value.doubleValue());
        }
        return "";
    }

    public String getFromcurrency() {
        return fromcurrency;
    }

    public void setFromcurrency(String fromcurrency) {
        this.fromcurrency = fromcurrency;
    }

    public String getFromerate() {
        return fromerate;
    }

    public void setFromerate(String fromerate) {
        this.fromerate = fromerate;
    }

    public String getTocurrency() {
        return tocurrency;
    }

    public void setTocurrency(String tocurrency) {
        this.tocurrency = tocurrency;
    }

    public String getTorate() {
        return torate;
    }

    public void setTorate(String torate) {
        this.torate = torate;
    }
}
