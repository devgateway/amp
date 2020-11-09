package org.digijava.kernel.ampapi.endpoints.ndd;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.module.aim.dbentity.AmpTheme;

import java.math.BigDecimal;
import java.util.List;

public class NDDSolarChartData {

    public static class Program {

        private final String code;
        private final String name;
        private final int level;

        public Program(String code, String name, int level) {
            this.code = code;
            this.name = name;
            this.level = level;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }
    }

    public static class ProgramData {

        private final Program programLvl1;

        private final Program programLvl2;

        private final Program programLvl3;

        private final BigDecimal amount;

        /**
         * Convert an AmpTheme tree into a plain structure.
         *
         * @param program
         * @param amount
         */
        public ProgramData(AmpTheme program, BigDecimal amount) {
            this.programLvl3 = new Program(program.getThemeCode(), program.getName(), 3);
            this.programLvl2 = new Program(program.getParentThemeId().getThemeCode(),
                    program.getParentThemeId().getName(), 2);
            this.programLvl1 = new Program(program.getParentThemeId().getParentThemeId().getThemeCode(),
                    program.getParentThemeId().getParentThemeId().getName(), 1);
            this.amount = amount;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public Program getProgramLvl1() {
            return programLvl1;
        }

        public Program getProgramLvl2() {
            return programLvl2;
        }

        public Program getProgramLvl3() {
            return programLvl3;
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
