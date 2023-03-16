package org.digijava.kernel.ampapi.endpoints.indicator.manager.validators;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProgramSchemeIdValidator implements ConstraintValidator<ValidProgramSchemeId, Long> {

    @Override
    public void initialize(ValidProgramSchemeId constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long programSchemeId, ConstraintValidatorContext context) {

        if (programSchemeId == null) {
            return true;
        }

        Session session = PersistenceManager.getSession();
        Long findProgramSchemeId = (Long) session.createQuery("SELECT p.ampProgramSettingsId FROM AmpProgramSettings p"
                        + "WHERE p.ampProgramSettingsId = :programSchemeId AND (p.deleted = false OR p.deleted is null)")
                .setParameter("programSchemeId", programSchemeId)
                .uniqueResult();

        return findProgramSchemeId != null;

    }
}
