package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.ProgramUtil;

public class AmpActivityProgram {

        private Long ampActivityProgramId;
        private Float programPercentage;
        private AmpTheme program;
        private AmpActivity activity;
        private AmpActivityProgramSettings programSetting;
        public Long getAmpActivityProgramId() {
                return ampActivityProgramId;
        }

        public Float getProgramPercentage() {
                return programPercentage;
        }

        public AmpTheme getProgram() {
                return program;
        }

        public AmpActivity getActivity() {
                return activity;
        }

        public AmpActivityProgramSettings getProgramSetting() {
                return programSetting;
        }

        public void setAmpActivityProgramId(Long ampActivityProgramId) {
                this.ampActivityProgramId = ampActivityProgramId;
        }

        public void setProgramPercentage(Float programPercentage) {
                this.programPercentage = programPercentage;
        }

        public void setProgram(AmpTheme program) {
                this.program = program;
        }

        public void setActivity(AmpActivity activity) {
                this.activity = activity;
        }

        public void setProgramSetting(AmpActivityProgramSettings programSetting) {
                this.programSetting = programSetting;
        }

        public String getHierarchyNames() {
                String names = "";
                names = ProgramUtil.printHierarchyNames(this.program);
                names += "[" + this.program.getName() + "]";
                return names;
        }
}
