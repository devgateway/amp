package org.digijava.module.aim.validator.percentage;

import javax.validation.ConstraintValidatorContext;

import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpRole;

/**
 * @author Octavian Ciubotaru
 */
public class ProgramTotalPercentageValidator extends AbstractTotalPercentageValidator
        <ProgramTotalPercentage, AmpActivityFields, AmpActivityProgramSettings> {

    @Override
    public Iterable getCollection(AmpActivityFields value) {
        return value.getActPrograms();
    }

    @Override
    public AmpActivityProgramSettings getItemGroup(Object item) {
        return ((AmpActivityProgram) item).getProgramSetting();
    }

    @Override
    public Float getPercentage(Object item) {
        return ((AmpActivityProgram) item).getProgramPercentage();
    }

    /**
     * <p>In UI programs are grouped by program settings. Violations are reported for each program setting
     * separately.</p>
     *
     * <p>For more details about violation see
     * {@link OrgRoleTotalPercentageValidator#addConstraintViolationForGroup(ConstraintValidatorContext, AmpRole)}
     * </p>
     *
     * @param context context in which the constraint is evaluated
     * @param group group
     */
    @Override
    public void addConstraintViolationForGroup(ConstraintValidatorContext context, AmpActivityProgramSettings group) {
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addNode("actPrograms")
                .addNode("programPercentage")
                        .inIterable()
                        .atKey(group)
                .addConstraintViolation();
    }
}
