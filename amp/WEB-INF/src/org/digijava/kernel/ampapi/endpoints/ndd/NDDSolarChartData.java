package org.digijava.kernel.ampapi.endpoints.ndd;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.module.aim.dbentity.AmpTheme;

import java.math.BigDecimal;
import java.util.List;

public class NDDSolarChartData {

    public static class ProgramData {
        @JsonProperty("program")
        private final AmpTheme program;

        @JsonProperty("amount")
        private final BigDecimal amount;

        public ProgramData(AmpTheme program, BigDecimal amount) {
            this.program = program;
            this.amount = amount;
        }

        public AmpTheme getProgram() {
            return program;
        }

        public BigDecimal getAmount() {
            return amount;
        }
    }

    @JsonProperty("directProgram")
    private ProgramData directProgram;

    @JsonProperty("indirectPrograms")
    private List<ProgramData> indirectPrograms;

    public NDDSolarChartData(ProgramData directProgram, List<ProgramData> indirectPrograms) {
        this.directProgram = directProgram;
        this.indirectPrograms = indirectPrograms;
    }

    public NDDSolarChartData() {
    }

    public ProgramData getDirectProgram() {
        return directProgram;
    }

    public void setDirectProgram(ProgramData directProgram) {
        this.directProgram = directProgram;
    }

    public List<ProgramData> getIndirectPrograms() {
        return indirectPrograms;
    }

    public void setIndirectPrograms(List<ProgramData> indirectPrograms) {
        this.indirectPrograms = indirectPrograms;
    }
}
