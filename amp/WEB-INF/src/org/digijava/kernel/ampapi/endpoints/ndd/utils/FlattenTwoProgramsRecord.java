package org.digijava.kernel.ampapi.endpoints.ndd.utils;

import org.digijava.module.aim.dbentity.AmpTheme;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class FlattenTwoProgramsRecord {

    private AmpTheme outerProgram;
    private AmpTheme innerProgram;
    private Map<String, BigDecimal> amountsByYear;

    public FlattenTwoProgramsRecord(AmpTheme innerProgram, AmpTheme outerProgram,
                                    Map<String, BigDecimal> amountsByYear) {
        this.innerProgram = innerProgram;
        this.outerProgram = outerProgram;
        this.amountsByYear = amountsByYear;
    }

    public FlattenTwoProgramsRecord() {
    }

    public AmpTheme getOuterProgram() {
        return outerProgram;
    }

    public void setOuterProgram(AmpTheme outerProgram) {
        this.outerProgram = outerProgram;
    }

    public Map<String, BigDecimal> getAmountsByYear() {
        return amountsByYear;
    }

    public void setAmountsByYear(Map<String, BigDecimal> amountsByYear) {
        this.amountsByYear = amountsByYear;
    }

    public AmpTheme getInnerProgram() {
        return innerProgram;
    }

    public void setInnerProgram(AmpTheme innerProgram) {
        this.innerProgram = innerProgram;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlattenTwoProgramsRecord that = (FlattenTwoProgramsRecord) o;
        return outerProgram.equals(that.outerProgram)
                && innerProgram.equals(that.innerProgram)
                && amountsByYear.equals(that.amountsByYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outerProgram, innerProgram, amountsByYear);
    }
}
