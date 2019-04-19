package org.digijava.module.aim.validator.percentage;

import javax.validation.ConstraintValidatorContext;

import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;

/**
 * @author Octavian Ciubotaru
 */
public class OrgRoleTotalPercentageValidator
        extends AbstractTotalPercentageValidator<OrgRoleTotalPercentage, AmpActivityFields, AmpRole> {

    @Override
    public Iterable getCollection(AmpActivityFields value) {
        return value.getOrgrole();
    }

    @Override
    public AmpRole getItemGroup(Object item) {
        return ((AmpOrgRole) item).getRole();
    }

    @Override
    public Float getPercentage(Object item) {
        return ((AmpOrgRole) item).getPercentage();
    }

    @Override
    public boolean skipValidationForGroup(AmpRole key) {
        return key != null && Constants.ROLE_CODE_DONOR.equals(key.getRoleCode());
    }

    /**
     * <p>In UI organizations are grouped by roles. Violations are reported for each group separately.</p>
     *
     * <p>Problem 1. There is mismatch between the violation and the model because there is no single object to which a
     * violation can be attributed to. One way to deal with this is to assign violations at activity level but this
     * creates more problems because UI is left to distinguish the violations by parsing the message. Another way is
     * to assign violations to the first object from the group which leads to problem 2.</p>
     *
     * <p>Problem 2. Validated collections are represented by a Set in the model. There is no such property expression
     * that can lead to one item in the set. To deal with this problem we could:
     *  a) use atKey() which was designed for maps
     *  b) change model to use List instead of Set and use atIndex() to reference the problematic item</p>
     *
     * <p>Implemented solution is atKey() hack applied for sets.</p>
     *
     * @param context context in which the constraint is evaluated
     * @param group group
     */
    @Override
    public void addConstraintViolationForGroup(ConstraintValidatorContext context, AmpRole group) {
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addNode("orgrole")
                .addNode("percentage")
                        .inIterable()
                        .atKey(group)
                .addConstraintViolation();
    }
}
