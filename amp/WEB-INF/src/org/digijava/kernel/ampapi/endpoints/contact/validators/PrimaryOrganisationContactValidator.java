package org.digijava.kernel.ampapi.endpoints.contact.validators;

import java.util.Collection;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidator;
import org.digijava.kernel.ampapi.endpoints.contact.ContactEPConstants;
import org.digijava.kernel.ampapi.endpoints.contact.ContactErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public class PrimaryOrganisationContactValidator extends InputValidator {

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
            Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {

        boolean found = false;
        if (fieldPath.equals(ContactEPConstants.ORGANISATION_CONTACTS)) {
            Collection orgContacts = (Collection) newFieldParent.get(ContactEPConstants.ORGANISATION_CONTACTS);
            if (orgContacts != null) {
                for (Object contact : orgContacts) {
                    Map contactMap = (Map) contact;
                    boolean primary = Boolean.TRUE.equals(contactMap.get(ContactEPConstants.PRIMARY_CONTACT));
                    if (primary) {
                        if (found) {
                            return false;
                        } else {
                            found = true;
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ContactErrors.UNIQUE_PRIMARY_ORG_CONTACT;
    }
}
