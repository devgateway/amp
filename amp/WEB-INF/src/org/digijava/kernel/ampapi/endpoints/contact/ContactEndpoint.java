package org.digijava.kernel.ampapi.endpoints.contact;

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpContact;

/**
 * @author Octavian Ciubotaru
 */
@Path("contact")
public class ContactEndpoint implements ErrorReportingEndpoint {

    /**
     * Provides full set of available fields and their settings/rules in a hierarchical structure.
     * @return JSON with fields information
     * @see <a href="https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration">Fields Enumeration Wiki<a/>
     */
    @GET
    @Path("fields")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getFields", ui = false)
    public List<APIField> getAvailableFields() {
        return AmpFieldsEnumerator.PUBLIC_CONTACT_ENUMERATOR.getContactFields();
    }

    /**
     * Returns a list of possible values for each requested field.
     * <p>If value can be translated then each possible value will contain value-translations element, a map where key
     * is language code and value is translated value.</p>
     * <h3>Sample request:</h3><pre>
     * ["title", "organisation_contacts~organisation", "phone~type"]
     * </pre>
     * <h3>Sample response:</h3><pre>
     * {
     *   "phone~type": [
     *     {
     *       "id": 194,
     *       "value": "Cell",
     *       "translated-value": {
     *         "fr": "Portable"
     *       }
     *     },
     *     {
     *       "id": 193,
     *       "value": "Home",
     *       "translated-value": {
     *         "fr": "Domicile"
     *       }
     *     }
     *   ],
     *   "organisation_contacts~organisation": [
     *     {
     *       "id": 105,
     *       "value": "Volet Trésor",
     *       "translated-value": {
     *         "fr": "Volet Trésor"
     *       },
     *       "extra_info": {
     *         "acronym": "TRESOR",
     *         "organization_group": "Etat"
     *       }
     *     }
     *   ],
     *   "title": [
     *     {
     *       "id": 241,
     *       "value": "Mr",
     *       "translated-value": {
     *         "fr": "M."
     *       }
     *     },
     *     {
     *       "id": 242,
     *       "value": "Ms",
     *       "translated-value": {
     *         "fr": "Melle"
     *       }
     *     }
     *   ]
     * }
     * </pre>
     *
     * @implicitParam translations|string|query|false|||||false|pipe separated list of language codes
     * @param fields list of fully qualified contact fields
     * @return list of possible values grouped by field
     */
    @POST
    @Path("field/values")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getMultiValues", ui = false)
    public Map<String, List<PossibleValue>> getValues(List<String> fields) {
        Map<String, List<PossibleValue>> response;
        if (fields == null) {
            response = emptyMap();
        } else {
            response = fields.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(toMap(identity(), this::possibleValuesFor));
        }
        return response;
    }

    private List<PossibleValue> possibleValuesFor(String fieldName) {
        return PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(fieldName, AmpContact.class, null);
    }

    /**
     * Retrieve contact.
     * @param id contact id
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getContact", ui = false)
    public JsonBean getContact(@PathParam("id") Long id) {
        return ContactUtil.getContact(id);
    }

    /**
     * Create new contact.
     * @param contact the contact to create
     * @return brief representation of contact
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "createContact", ui = false)
    public JsonBean createContact(JsonBean contact) {
        ContactImporter importer = new ContactImporter();
        List<ApiErrorMessage> errors = importer.createContact(contact);
        return ContactUtil.getImportResult(importer.getContact(), importer.getNewJson(), errors);
    }

    /**
     * Update an existing contact.
     * @param id id of the existing contact
     * @param contact updated contact
     * @return brief representation of contact
     */
    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "updateContact", ui = false)
    public JsonBean updateContact(@PathParam("id") Long id, JsonBean contact) {
        ContactImporter importer = new ContactImporter();
        List<ApiErrorMessage> errors = importer.updateContact(id, contact);
        return ContactUtil.getImportResult(importer.getContact(), importer.getNewJson(), errors);
    }

    @Override
    public Class getErrorsClass() {
        return ContactErrors.class;
    }
}
