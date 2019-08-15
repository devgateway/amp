package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.kernel.ampapi.endpoints.common.values.providers.ThemePossibleValuesProvider;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.Output;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TreeNodeAware;

public class AmpActivityProgram implements Versionable, Serializable, Cloneable, TreeNodeAware<AmpTheme> {

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampActivityProgramId;

        @Interchangeable(fieldTitle = "Program Percentage", importable = true, percentageConstraint = true,
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        private Float programPercentage;

        @PossibleValues(ThemePossibleValuesProvider.class)
        @Interchangeable(fieldTitle = "Program", importable = true, pickIdOnly = true, uniqueConstraint = true,
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        private AmpTheme program;
        @InterchangeableBackReference
        private AmpActivityVersion activity;
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
}
