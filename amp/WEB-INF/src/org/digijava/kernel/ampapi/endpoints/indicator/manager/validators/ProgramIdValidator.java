package org.digijava.kernel.ampapi.endpoints.indicator.manager.validators;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;

/**
 * @author Viorel Chihai
 * @author Timothy Mugo
 */
public class ProgramIdValidator implements ConstraintValidator<ValidProgramIds, Long> {

    @Override
    public void initialize(ValidProgramIds constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long programId, ConstraintValidatorContext context) {

        if (programId == null) {
            return true;
        }

        Session session = PersistenceManager.getSession();
        Long validProgramIdFromDb = (Long) session.createQuery("SELECT count(p.ampThemeId) FROM AmpTheme p "
                        + "WHERE p.ampThemeId=:programId AND (p.deleted = false OR p.deleted is null)")
                .setParameterList("programId", Collections.singleton(programId))
                .uniqueResult();


        return programId == validProgramIdFromDb;
    }

}
