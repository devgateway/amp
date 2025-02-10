package org.digijava.kernel.ampapi.endpoints.indicator.manager.validators;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import org.digijava.module.aim.dbentity.*;

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
        long validSectorIdsFromDb = (Long) session.createQuery("SELECT count(s.ampSectorId) FROM " + AmpSector.class.getName()  + " " +
                        "AS s WHERE s.ampSectorId in (:sectorIds) AND (s.deleted = false OR s.deleted is null)")
                .setParameterList("sectorIds", sectorIds)
                .uniqueResult();

        int sectorIdsSize = Math.toIntExact(validSectorIdsFromDb);


        return sectorIds.size() == sectorIdsSize;
    }

}
