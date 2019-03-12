package org.digijava.module.aim.validator.percentage;

import javax.validation.ConstraintValidatorContext;

import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpRole;

/**
 * @author Octavian Ciubotaru
 */
public class SectorsTotalPercentageValidator extends AbstractTotalPercentageValidator
        <SectorsTotalPercentage, AmpActivityFields, AmpClassificationConfiguration> {

    @Override
    public Iterable getCollection(AmpActivityFields value) {
        return value.getSectors();
    }

    @Override
    public AmpClassificationConfiguration getItemGroup(Object item) {
        return ((AmpActivitySector) item).getClassificationConfig();
    }

    @Override
    public Float getPercentage(Object item) {
        return ((AmpActivitySector) item).getSectorPercentage();
    }

    /**
     * <p>In UI activity sectors are grouped by classification configuration. Violations are reported for each
     * classification configuration separately.</p>
     *
     * <p>For more details about violation see
     * {@link OrgRoleTotalPercentageValidator#addConstraintViolationForGroup(ConstraintValidatorContext, AmpRole)}
     * </p>
     *
     * @param context context in which the constraint is evaluated
     * @param group group
     */
    @Override
    public void addConstraintViolationForGroup(ConstraintValidatorContext context,
            AmpClassificationConfiguration group) {

        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addNode("sectors")
                .addNode("sectorPercentage")
                        .inIterable()
                        .atKey(group)
                .addConstraintViolation();
    }
}
