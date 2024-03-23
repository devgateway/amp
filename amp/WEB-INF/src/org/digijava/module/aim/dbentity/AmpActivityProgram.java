package org.digijava.module.aim.dbentity;

import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.ThemePossibleValuesProvider;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.*;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.Output;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TreeNodeAware;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AmpActivityProgram implements Versionable, Serializable, Cloneable, TreeNodeAware<AmpTheme> {

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampActivityProgramId;

        @Interchangeable(fieldTitle = "Program Percentage", importable = true, percentageConstraint = true,
                fmPath = FMVisibility.PARENT_FM + "/programPercentage",
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        private Float programPercentage;

        @PossibleValues(ThemePossibleValuesProvider.class)
        @Interchangeable(fieldTitle = "Program", importable = true, pickIdOnly = true, uniqueConstraint = true,
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        private AmpTheme program;

    @Interchangeable(fieldTitle = "Indirect Programs")
    private Set<AmpActivityIndirectProgram> indirectPrograms = new HashSet<>();

        @InterchangeableBackReference
        private AmpActivityVersion activity;
        private AmpActivityProgramSettings programSetting;

    public Set<AmpActivityIndirectProgram> getIndirectPrograms() {
        return indirectPrograms;
    }

    public void setIndirectPrograms(Set<AmpActivityIndirectProgram> indirectPrograms) {
        this.indirectPrograms = indirectPrograms;
    }

    public void addIndirectProgram(AmpActivityIndirectProgram indirectProgram) {
        indirectProgram.setActivityProgram(this);
        indirectPrograms.add(indirectProgram);
    }

    public Long getAmpActivityProgramId() {
                return ampActivityProgramId;
        }

        public Float getProgramPercentage() {
                return programPercentage;
        }

        public AmpTheme getProgram() {
                return program;
        }

        public AmpActivityVersion getActivity() {
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

        public void setActivity(AmpActivityVersion activity) {
                this.activity = activity;
        }

        public void setProgramSetting(AmpActivityProgramSettings programSetting) {
                this.programSetting = programSetting;
        }

        public String getHierarchyNames() {
            return getHierarchyNames(false);
        }
        
        public String getHierarchyNames(boolean insertNewLine) {
                String names = "";
                names = ProgramUtil.printHierarchyNames(this.program, insertNewLine);
                names += "[" + this.program.getName() + "]";
                return names;
        }
        
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpActivityProgram aux = (AmpActivityProgram) obj;
        if (this.program.getAmpThemeId().equals(aux.program.getAmpThemeId())) {
            return true;
        }
        return false;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(new Output(null, new String[] { "Name" }, new Object[] { this.program.getName() }));
        out.getOutputs()
                .add(new Output(null, new String[] { "Percentage" }, new Object[] { this.programPercentage }));
        return out;
    }

    @Override
    public Object getValue() {
        String ret = "";
        ret = "" + this.programPercentage;
        return ret;
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpActivityProgram aux = (AmpActivityProgram) clone();
        aux.activity = newActivity;

        aux.setIndirectPrograms(new HashSet<>());
        for (AmpActivityIndirectProgram indirectProgram : indirectPrograms) {
            AmpActivityIndirectProgram clonedIp = (AmpActivityIndirectProgram) indirectProgram.clone();
            clonedIp.setId(null);
            aux.addIndirectProgram(clonedIp);
        }

        aux.ampActivityProgramId = null;
        
        return aux;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
    return super.clone();
    }

    @Override
    public AmpAutoCompleteDisplayable<AmpTheme> getTreeNode() {
        return program;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmpActivityProgram that = (AmpActivityProgram) o;
        return Objects.equals(program, that.program) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity,  program);
    }
}
