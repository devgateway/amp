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
public class SectorIdsValidator implements ConstraintValidator<ValidSectorIds, List<Long>> {

    @Override
    public void initialize(ValidSectorIds constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<Long> sectorIds, ConstraintValidatorContext context) {

        if (CollectionUtils.isEmpty(sectorIds)) {
            return true;
        }

        Session session = PersistenceManager.getSession();
        Integer validSectorIdsFromDb = (Integer) session.createQuery("SELECT count(s.ampSectorId) FROM AmpSector s "
                        + "WHERE s.ampSectorId in (:sectorIds)")
                .setParameterList("sectorIds", sectorIds)
                .uniqueResult();


        return sectorIds.size() == validSectorIdsFromDb;
    }

}
