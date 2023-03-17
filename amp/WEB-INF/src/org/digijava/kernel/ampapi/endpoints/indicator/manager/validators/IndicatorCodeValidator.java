package org.digijava.kernel.ampapi.endpoints.indicator.manager.validators;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IndicatorCodeValidator implements ConstraintValidator<ValidIndicatorCode, String> {

    @Override
    public void initialize(ValidIndicatorCode constraintAnnotation) {
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {

        if (code == null) {
            return false;
        }

        Session session = PersistenceManager.getSession();

        String findIndicatorCode = (String) session.createQuery("SELECT i.code FROM AmpIndicator i "
                        + "WHERE i.code = :code")
                .setParameter("code", code)
                .setMaxResults(1)
                .uniqueResult();

        return findIndicatorCode == null;
    }
}
