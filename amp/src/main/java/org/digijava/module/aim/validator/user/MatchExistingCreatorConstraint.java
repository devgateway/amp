package org.digijava.module.aim.validator.user;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.validator.ActivityValidationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Nadejda Mandrescu
 */
public class MatchExistingCreatorConstraint implements ConstraintValidator<MatchExistingCreator, AmpTeamMember> {

    @Override
    public void initialize(MatchExistingCreator constraintAnnotation) {
    }

    @Override
    public boolean isValid(AmpTeamMember value, ConstraintValidatorContext context) {
        ActivityValidationContext avc = ActivityValidationContext.getOrThrow();
        if (avc.getOldActivity() == null) {
            return value != null;
        }
        // e.g. in Haiti there are 322 activities without creator created between 2010 and 2017
        // hence it must be a controlled approach of updating the creator if ever needed and not accidental
        if (avc.getOldActivity().getActivityCreator() == null) {
            return value == null;
        }
        return avc.getOldActivity().getActivityCreator().equals(value);
    }

}
