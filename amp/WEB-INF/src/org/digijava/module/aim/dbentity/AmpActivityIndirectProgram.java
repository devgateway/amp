package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;

/**
 * @author Octavian Ciubotaru
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_ACTIVITY_INDIRECT_PROGRAM")
public class AmpActivityIndirectProgram implements Serializable, Cloneable {

    public static final int PERCENTAGE_PRECISION = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ACTIVITY_INDIRECT_PROGRAM_SEQ")
    @SequenceGenerator(name = "AMP_ACTIVITY_INDIRECT_PROGRAM_SEQ", sequenceName = "AMP_ACTIVITY_INDIRECT_PROGRAM_SEQ", allocationSize = 1)    @Column(name = "id")
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_activity_program_id", nullable = false)
    private AmpActivityProgram activityProgram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_program_id", nullable = false)
    @Interchangeable(fieldTitle = "Program", pickIdOnly = true)
    private AmpTheme program;

    @Column(name = "program_percentage", precision = 5, scale = 2)
    @Interchangeable(fieldTitle = "Percentage")
    private BigDecimal percentage;


    public AmpActivityIndirectProgram() {
    }

    public AmpActivityIndirectProgram(AmpTheme program, BigDecimal percentage) {
        this.program = program;
        this.percentage = percentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpActivityProgram getActivityProgram() {
        return activityProgram;
    }

    public void setActivityProgram(AmpActivityProgram activityProgram) {
        this.activityProgram = activityProgram;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public AmpTheme getProgram() {
        return program;
    }

    public void setProgram(AmpTheme program) {
        this.program = program;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
