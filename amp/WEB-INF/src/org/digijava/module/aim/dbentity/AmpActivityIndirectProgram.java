package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Octavian Ciubotaru
 */
public class AmpActivityIndirectProgram implements Serializable, Cloneable {

    public static final int PERCENTAGE_PRECISION = 2;

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    private AmpActivityProgram activityProgram;

    @Interchangeable(fieldTitle = "Program", pickIdOnly = true)
    private AmpTheme program;

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
