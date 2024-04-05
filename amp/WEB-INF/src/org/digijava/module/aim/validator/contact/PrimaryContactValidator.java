package org.digijava.module.aim.validator.contact;

import org.apache.commons.lang3.mutable.MutableLong;
import org.digijava.module.aim.dbentity.AmpActivityContact;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Viorel Chihai
 */
public class PrimaryContactValidator implements ConstraintValidator<PrimaryContact, Set<AmpActivityContact>> {
    
    @Override
    public void initialize(PrimaryContact constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(Set<AmpActivityContact> contacts, ConstraintValidatorContext context) {
        
        if (contacts == null) {
            return true;
        }
        
        context.disableDefaultConstraintViolation();
        
        Map<String, MutableLong> primaryContacts = new HashMap<>();
        
        for (AmpActivityContact contact : contacts) {
            String type = contact.getContactType();
            MutableLong cnt = primaryContacts.computeIfAbsent(type, r -> new MutableLong());
            if (Boolean.TRUE.equals(contact.getPrimaryContact())) {
                cnt.increment();
            }
        }
        
        boolean valid = true;
        for (Map.Entry<String, MutableLong> entry : primaryContacts.entrySet()) {
            if (entry.getValue().longValue() > 1) {
                addConstraintViolationForContactType(context, entry.getKey());
                valid = false;
            }
        }
        
        return valid;
    }
    
    /**
     * Build and add the constraint violation.
     *
     * @param context     context in which the constraint is evaluated
     * @param contactType contact type
     */
    public void addConstraintViolationForContactType(ConstraintValidatorContext context, String contactType) {
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("activityContacts")
                .inIterable()
                .atKey(contactType)
                .addConstraintViolation();
    }
}
