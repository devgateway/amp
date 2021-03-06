package org.digijava.kernel.ampapi.endpoints.ndd.utils;

import org.digijava.module.aim.dbentity.AmpTheme;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class FlattenProgramRecord {

    private AmpTheme program;
    private Map<String, BigDecimal> amountsByYear;
    private BigDecimal amount;

    public FlattenProgramRecord(AmpTheme program, BigDecimal amount, Map<String, BigDecimal> amountsByYear) {
        this.program = program;
        this.amountsByYear = amountsByYear;
        this.amount = amount;
    }

    public FlattenProgramRecord() {
    }

    public AmpTheme getProgram() {
        return program;
    }

    public void setProgram(AmpTheme program) {
        this.program = program;
    }

    public Map<String, BigDecimal> getAmountsByYear() {
        return amountsByYear;
    }

    public void setAmountsByYear(Map<String, BigDecimal> amountsByYear) {
        this.amountsByYear = amountsByYear;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlattenProgramRecord that = (FlattenProgramRecord) o;
        return Objects.equals(program, that.program)
                && Objects.equals(amountsByYear, that.amountsByYear)
                && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(program, amountsByYear, amount);
    }
}
