package org.digijava.kernel.ampapi.endpoints.ndd;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NDDSolarChartData {

    public static class Program {

        private final String code;
        private final String name;
        private final String filterColumnName;
        private final Long objectId;

        public Program(String code, String name, String filterColumnName, Long objectId) {
            this.code = code;
            this.name = name;
            this.filterColumnName = filterColumnName;
            this.objectId = objectId;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getFilterColumnName() {
            return filterColumnName;
        }

        public Long getObjectId() {
            return objectId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Program program = (Program) o;
            return Objects.equals(code, program.code)
                    && Objects.equals(name, program.name)
                    && Objects.equals(filterColumnName, program.filterColumnName)
                    && Objects.equals(objectId, program.objectId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, name, filterColumnName, objectId);
        }
    }

    public static class ProgramData {

        private final Program programLvl1;

        private final Program programLvl2;

        private final Program programLvl3;

        private BigDecimal amount;

        private Map<String, BigDecimal> amountsByYear;

        /**
         * Convert an AmpTheme tree into a plain structure.
         *
         * @param program
         * @param amount
         */
        public ProgramData(AmpTheme program, BigDecimal amount, Map<String, BigDecimal> amountsByYear) {
            String configurationName = null;
            AmpActivityProgramSettings activityProgramSettings = null;
            switch (program.getIndlevel()) {
                case 3:
                    activityProgramSettings = program.getParentThemeId()
                            .getParentThemeId().getParentThemeId().getProgramSettings()
                            .stream().findAny().orElse(null);
                    if (activityProgramSettings != null) {
                        configurationName = activityProgramSettings.getName();
                        this.programLvl1 = new Program(program.getParentThemeId().getParentThemeId().getThemeCode(),
                                program.getParentThemeId().getParentThemeId().getName(), FilterUtils.INSTANCE.
                                idFromColumnName(configurationName + " Level 1"),
                                program.getParentThemeId().getParentThemeId().getAmpThemeId());
                        this.programLvl2 = new Program(program.getParentThemeId().getThemeCode(),
                                program.getParentThemeId().getName(), FilterUtils.
                                INSTANCE.idFromColumnName(configurationName + " Level 2"),
                                program.getParentThemeId().getAmpThemeId());
                        this.programLvl3 = new Program(program.getThemeCode(), program.getName(),
                                FilterUtils.INSTANCE.idFromColumnName(configurationName + " Level 3"),
                                program.getAmpThemeId());
                        this.amount = amount;
                        this.amountsByYear = amountsByYear;
                    } else {
                        this.programLvl1 = null;
                        this.programLvl2 = null;
                        this.programLvl3 = null;
                        this.amount = null;
                        this.amountsByYear = null;
                    }
                    break;
                case 2:
                    activityProgramSettings = program
                            .getParentThemeId().getParentThemeId().getProgramSettings()
                            .stream().findAny().orElse(null);
                    if (activityProgramSettings != null) {
                        configurationName = activityProgramSettings.getName();
                        this.programLvl1 = new Program(program.getParentThemeId().getThemeCode(),
                                program.getParentThemeId().getName(), FilterUtils.INSTANCE.
                                idFromColumnName(configurationName + " Level 1"),
                                program.getParentThemeId().getAmpThemeId());
                        this.programLvl2 = new Program(program.getThemeCode(),
                                program.getName(), FilterUtils.
                                INSTANCE.idFromColumnName(configurationName + " Level 2"),
                                program.getAmpThemeId());
                        this.programLvl3 = null;
                        this.amount = amount;
                        this.amountsByYear = amountsByYear;
                    } else {
                        this.programLvl1 = null;
                        this.programLvl2 = null;
                        this.programLvl3 = null;
                        this.amount = null;
                        this.amountsByYear = null;
                    }
                    break;
                case 1:
                    activityProgramSettings = program.getParentThemeId()
                            .getProgramSettings()
                            .stream().findAny().orElse(null);
                    if (activityProgramSettings != null) {
                        configurationName = activityProgramSettings.getName();
                        this.programLvl1 = new Program(program.getThemeCode(),
                                program.getName(), FilterUtils.INSTANCE.
                                idFromColumnName(configurationName + " Level 1"),
                                program.getAmpThemeId());
                        this.programLvl2 = null;
                        this.programLvl3 = null;
                        this.amount = amount;
                        this.amountsByYear = amountsByYear;
                    } else {
                        this.programLvl1 = null;
                        this.programLvl2 = null;
                        this.programLvl3 = null;
                        this.amount = null;
                        this.amountsByYear = null;
                    }
                    break;
                default:
                    this.programLvl1 = null;
                    this.programLvl2 = null;
                    this.programLvl3 = null;
                    this.amount = null;
                    this.amountsByYear = null;
                    break;
            }
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

        public Map<String, BigDecimal> getAmountsByYear() {
            return amountsByYear;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public void setAmountsByYear(Map<String, BigDecimal> amountsByYear) {
            this.amountsByYear = amountsByYear;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProgramData p2 = (ProgramData) o;
            return ((programLvl1 == null && p2.programLvl1 == null) || Objects.equals(programLvl1, p2.programLvl1))
                    && ((programLvl2 == null && p2.programLvl2 == null) || Objects.equals(programLvl2, p2.programLvl2))
                    && ((programLvl3 == null && p2.programLvl3 == null) || Objects.equals(programLvl3, p2.programLvl3));
        }

        @Override
        public int hashCode() {
            return Objects.hash(programLvl1, programLvl2, programLvl3, amount, amountsByYear);
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
