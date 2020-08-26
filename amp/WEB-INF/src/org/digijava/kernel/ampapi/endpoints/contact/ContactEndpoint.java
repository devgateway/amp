package org.digijava.kernel.ampapi.endpoints.contact;

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.digijava.kernel.translator.util.TrnUtil.PREFIXES;

import java.util.Collection;
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

import com.fasterxml.jackson.annotation.JsonView;

import org.digijava.kernel.ampapi.endpoints.activity.APIWorkspaceMemberFieldList;
import org.digijava.kernel.ampapi.endpoints.activity.FieldIdValue;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.contact.dto.ContactView;
import org.digijava.kernel.ampapi.endpoints.contact.dto.SwaggerContact;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpContact;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Octavian Ciubotaru
 */
@Path("contact")
@Api("contact")
public class ContactEndpoint {

    /**
     * Provides full set of available fields and their settings/rules in a hierarchical structure
     * grouped by workspace member id
     *
     * @param wsMemberIds
     * @return JSON with fields information grouped by ws-member-ids
     * @see <a href="https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration">Fields Enumeration Wiki<a/>
     */
    @POST
    @Path("ws-member-fields")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getAvailableFieldsBasedOnWs", ui = false)
    public List<APIWorkspaceMemberFieldList>
    getAvailableFieldsBasedOnWs(@ApiParam(value = "List of WS ids", required = true) List<Long> ids) {
        return AmpFieldsEnumerator.getAvailableFieldsBasedOnWs(ids, AmpFieldsEnumerator.TYPE_CONTACT);
    }

    @GET
    @Path("fields")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getFields", ui = false)
    @ApiOperation(
            value = "Provides full set of available fields and their settings/rules in a hierarchical structure.",
            notes = "See [Fields Enumeration Wiki](https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration).")
    public List<APIField> getAvailableFields() {
        return AmpFieldsEnumerator.getEnumerator().getContactFields();
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
        List<String> prefixes = TranslatorWorker.getAllPrefixes();
        TLSUtils.getRequest().setAttribute(PREFIXES, prefixes);
        if (fields == null) {
            response = emptyMap();
        } else {
            List<APIField> apiFields = AmpFieldsEnumerator.getEnumerator().getContactFields();
            response = fields.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(toMap(identity(), fieldName -> possibleValuesFor(fieldName, apiFields)));
        }
        return response;
    }


    @POST
    @Path("field/id-values")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getIdValues", ui = false)
    @ApiOperation(value = "Returns a list of values for all id of requested fields.")
    public Map<String, List<FieldIdValue>> getFieldValuesById(
            @ApiParam("List of fully qualified activity fields with list of ids.") Map<String, List<Long>> fieldIds) {
        List<APIField> apiFields = AmpFieldsEnumerator.getEnumerator().getContactFields();
        Map<String, List<FieldIdValue>> response = InterchangeUtils.getIdValues(fieldIds, apiFields);

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
    public SwaggerContact getContact(@ApiParam("contact id") @PathParam("id") Long id) {
        Map<String, Object> contact = ContactUtil.getContact(id);
        return new SwaggerContact(contact);
    }

    @POST
    @Path("/batch")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getContact", ui = false)
    @ApiOperation("Retrieve contacts")
    public Collection<Map<String, Object>> getContact(List<Long> ids) {
        return ContactUtil.getContacts(ids);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "createContact", ui = false)
    @ApiOperation("Create new contact")
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, reference = "AmpContact_Summary",
                    message = "brief representation of contact"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse_Summary",
            message = "error if invalid contact received")
    })
    @JsonView(ContactView.Summary.class)
    public JsonApiResponse<AmpContact> createContact(SwaggerContact contact) {
        return new ContactImporter().createContact(contact.getMap()).getResult();
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "updateContact", ui = false)
    @ApiOperation("Update an existing contact")
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, reference = "AmpContact_Summary",
                    message = "brief representation of contact"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, reference = "JsonApiResponse_Summary",
            message = "error if invalid contact received")
    })
    @JsonView(ContactView.Summary.class)
    public JsonApiResponse<AmpContact> updateContact(@ApiParam("id of the existing contact") @PathParam("id") Long id,
            SwaggerContact contact) {
        return new ContactImporter().updateContact(id, contact.getMap()).getResult();
    }

}
