package org.digijava.kernel.ampapi.endpoints.indicator.manager.validators;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IndicatorNameValidator implements ConstraintValidator<ValidIndicatorName, String> {

        @Override
        public void initialize(ValidIndicatorName constraintAnnotation) {
        }

        @Override
        public boolean isValid(String name, ConstraintValidatorContext context) {

                if (name == null) {
                    return false;
                }

                Session session = PersistenceManager.getSession();
                String findIndicatorName = (String) session.createQuery("SELECT i.name FROM AmpIndicator i "
                                + "WHERE i.name = :name")
                        .setParameter("name", name)
                        .setMaxResults(1)
                        .uniqueResult();

                return findIndicatorName == null;
        }
}
