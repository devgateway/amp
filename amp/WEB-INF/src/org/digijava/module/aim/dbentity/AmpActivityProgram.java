package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
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
import javax.persistence.*;

@Entity
@Table(name = "AMP_ACTIVITY_PROGRAM")
public class AmpActivityProgram implements Versionable, Serializable, Cloneable, TreeNodeAware<AmpTheme> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_activity_program_seq_generator")
    @SequenceGenerator(name = "amp_activity_program_seq_generator", sequenceName = "AMP_ACTIVITY_PROGRAM_seq", allocationSize = 1)
    @Column(name = "amp_activity_program_id")
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampActivityProgramId;

    @ManyToOne
    @JoinColumn(name = "amp_activity_id", nullable = false)
    @InterchangeableBackReference
    private AmpActivityVersion activity;

    @ManyToOne
    @JoinColumn(name = "amp_program_id", nullable = false)
    @PossibleValues(ThemePossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Program", importable = true, pickIdOnly = true, uniqueConstraint = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private AmpTheme program;

    @Column(name = "program_percentage")
    @Interchangeable(fieldTitle = "Program Percentage", importable = true, percentageConstraint = true,
            fmPath = FMVisibility.PARENT_FM + "/programPercentage",
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private Float programPercentage;

    @ManyToOne
    @JoinColumn(name = "program_setting", nullable = false)
    private AmpActivityProgramSettings programSetting;

    @OneToMany(mappedBy = "activityProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    @Interchangeable(fieldTitle = "Indirect Programs")
    private Set<AmpActivityIndirectProgram> indirectPrograms = new HashSet<>();



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
}
