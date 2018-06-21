package org.digijava.kernel.ampapi.endpoints.contact;

import java.util.List;

import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpContact;

/**
 * @author Octavian Ciubotaru
 */
public final class ContactUtil {

    private ContactUtil() {
    }

    public static JsonBean getImportResult(AmpContact contact, JsonBean json, List<ApiErrorMessage> errors) {
        JsonBean result;
        if (errors.size() == 0 && contact == null) {
            result = ApiError.toError(ApiError.UNKOWN_ERROR);
        } else if (errors.size() > 0) {
            result = ApiError.toError(errors);
            result.set(ContactEPConstants.CONTACT, json);
        } else {
            result = new JsonBean();
            result.set(ContactEPConstants.ID, contact.getId());
            result.set(ContactEPConstants.NAME, contact.getName());
            result.set(ContactEPConstants.LAST_NAME, contact.getLastname());
        }
        return result;
    }

    public static JsonBean getContact(Long id) {
        AmpContact contact = (AmpContact) PersistenceManager.getSession().get(AmpContact.class, id);
        
        if (contact == null) {
            ApiErrorResponse.reportResourceNotFound(ContactErrors.CONTACT_NOT_FOUND);
        }
        
        return new ContactExporter().export(contact);
    }
}
