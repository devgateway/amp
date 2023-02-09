package org.digijava.kernel.ampapi.endpoints.indicator.manager.validators;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author Viorel Chihai
 */
public class ProgramIdsValidator implements ConstraintValidator<ValidProgramIds, List<Long>> {

    @Override
    public void initialize(ValidProgramIds constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<Long> programIds, ConstraintValidatorContext context) {

        if (CollectionUtils.isEmpty(programIds)) {
            return true;
        }

        Session session = PersistenceManager.getSession();
        Integer validProgramIdsFromDb = (Integer) session.createQuery("SELECT count(p.ampThemeId) FROM AmpTheme p "
                        + "WHERE p.ampThemeId in (:programIds)")
                .setParameterList("programIds", programIds)
                .uniqueResult();


        return programIds.size() == validProgramIdsFromDb;
    }

}
