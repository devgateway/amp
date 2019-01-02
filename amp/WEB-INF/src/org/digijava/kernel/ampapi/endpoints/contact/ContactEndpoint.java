package org.digijava.kernel.ampapi.endpoints.contact;

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * @author Octavian Ciubotaru
 */
@Path("contact")
@Api("contact")
public class ContactEndpoint implements ErrorReportingEndpoint {

    @GET
    @Path("fields")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getFields", ui = false)
    @ApiOperation(
            value = "Provides full set of available fields and their settings/rules in a hierarchical structure.",
            notes = "See [Fields Enumeration Wiki](https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration).")
    public List<APIField> getAvailableFields() {
        return AmpFieldsEnumerator.getPublicContactEnumerator().getContactFields();
    }

    @POST
    @Path("field/values")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getMultiValues", ui = false)
    @ApiOperation(
            value = "Returns a list of possible values for each requested field.",
            notes = "If value can be translated then each possible value will contain value-translations element, "
                    + "a map where key is language code and value is translated value.\n"
                    + "\n"
                    + "### Sample request\n"
                    + "\n"
                    + "`[\"title\", \"organisation_contacts~organisation\", \"phone~type\"]`")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "list of possible values grouped by field"))
    public Map<String, List<PossibleValue>> getValues(
            @ApiParam("list of fully qualified contact fields") List<String> fields) {
        Map<String, List<PossibleValue>> response;
        if (fields == null) {
            response = emptyMap();
        } else {
            List<APIField> apiFields = AmpFieldsEnumerator.getPublicContactEnumerator().getContactFields();
            response = fields.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(toMap(identity(), fieldName -> possibleValuesFor(fieldName, apiFields)));
        }
        return response;
    }

    private List<PossibleValue> possibleValuesFor(String fieldName, List<APIField> apiFields) {
        return PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(fieldName, apiFields);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getContact", ui = false)
    @ApiOperation("Retrieve contact")
    public JsonBean getContact(@ApiParam("contact id") @PathParam("id") Long id) {
        return ContactUtil.getContact(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "createContact", ui = false)
    @ApiOperation("Create new contact")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "brief representation of contact"))
    public JsonBean createContact(JsonBean contact) {
        ContactImporter importer = new ContactImporter();
        List<ApiErrorMessage> errors = importer.createContact(contact);
        return ContactUtil.getImportResult(importer.getContact(), importer.getNewJson(), errors);
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "updateContact", ui = false)
    @ApiOperation("Update an existing contact")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "brief representation of contact"))
    public JsonBean updateContact(@ApiParam("id of the existing contact") @PathParam("id") Long id, JsonBean contact) {
        ContactImporter importer = new ContactImporter();
        List<ApiErrorMessage> errors = importer.updateContact(id, contact);
        return ContactUtil.getImportResult(importer.getContact(), importer.getNewJson(), errors);
    }

    @Override
    public Class getErrorsClass() {
        return ContactErrors.class;
    }
}
