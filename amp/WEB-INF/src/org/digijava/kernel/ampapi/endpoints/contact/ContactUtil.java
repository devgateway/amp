package org.digijava.kernel.ampapi.endpoints.contact;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.errors.GenericErrors;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.util.ContactInfoUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Octavian Ciubotaru
 */
public final class ContactUtil {

    private ContactUtil() {
    }

    public static Collection<Map<String, Object>> getContacts(List<Long> ids) {
        Map<Long, Map<String, Object>> jsonContacts = new TreeMap<>();
        ids = new ArrayList<>(new TreeSet<>(ids));
        ContactExporter exporter = new ContactExporter();

        for (int fromIndex = 0; fromIndex < ids.size(); fromIndex += ActivityEPConstants.BATCH_DB_QUERY_SIZE) {
            // for simplicity using the same DB batch size as for activities
            int end = Math.min(ids.size(), fromIndex + ActivityEPConstants.BATCH_DB_QUERY_SIZE);
            List<Long> currentIds = ids.subList(fromIndex, end);
            List<AmpContact> contacts = ContactInfoUtil.getContacts(currentIds);
            contacts.forEach(contact -> {
                Map<String, Object> result = new LinkedHashMap<>();
                try {
                    result = exporter.export(contact);
                } catch (Exception e) {
                    result.put(EPConstants.ERROR, ApiError.toError(
                            GenericErrors.INTERNAL_ERROR.withDetails(e.getMessage())).getErrors());
                    result.put(ContactEPConstants.ID, contact.getId());
                } finally {
                    PersistenceManager.getSession().evict(contact);
                }
                jsonContacts.put(contact.getId(), result);
            });
            PersistenceManager.getSession().clear();
        }
        // Always succeed on normal exit, no matter if some activities export failed
        EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_OK);
        return jsonContacts.values();
    }

    public static Map<String, Object> getContact(Long id) {
        AmpContact contact = (AmpContact) PersistenceManager.getSession().get(AmpContact.class, id);

        if (contact == null) {
            ApiErrorResponseService.reportResourceNotFound(ContactErrors.CONTACT_NOT_FOUND);
        }

        return new ContactExporter().export(contact);
    }
}
