package org.digijava.kernel.ampapi.endpoints.indicator.manager.validators;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Timothy Mugo
 */
public class ProgramIdValidator implements ConstraintValidator<ValidProgramId, Long> {

    @Override
    public void initialize(ValidProgramId constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long programId, ConstraintValidatorContext context) {

        if (programId == null) {
            return true;
        }

        Session session = PersistenceManager.getSession();
        Long findProgramId = (Long) session.createQuery("SELECT p.ampThemeId FROM AmpTheme p "
                        + "WHERE p.ampThemeId = :programId AND (p.deleted = false OR p.deleted is null)")
                .setParameter("programId", programId)
                .uniqueResult();

        return findProgramId != null;

    }
}
