package org.digijava.kernel.ampapi.endpoints.contact;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.*;
import org.digijava.kernel.ampapi.endpoints.activity.*;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.contact.dto.ContactView;
import org.digijava.kernel.ampapi.endpoints.contact.dto.SwaggerContact;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.util.ActivityUtil;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

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
            value = "Retrieve all available contact fields with their settings and rules",
            notes = "This endpoint returns a comprehensive list of all fields that can be used when creating or " +
                    "updating contacts in the AMP system.\n\n" +
                    "The response is structured hierarchically and includes detailed information about each field, " +
                    "such as its data type, validation rules, required status, dependencies, and other metadata " +
                    "that defines how the field should be handled in forms and data processing.\n\n" +
                    "Client applications can use this information to dynamically build contact forms, validate " +
                    "user input, and ensure that contact data meets the system's requirements.\n\n" +
                    "For more details on the field enumeration format, see the " +
                    "[Fields Enumeration Wiki](https://wiki.dgfoundation.org/display/AMPDOC/Fields+enumeration).\n\n" +
                    "This endpoint requires authentication.")
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
        ActivityUtil.loadWorkspacePrefixesIntoRequest();
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
    @ApiOperation(
            value = "Retrieve field values by their IDs",
            notes = "This endpoint allows you to fetch detailed information about specific field values " +
                    "by providing their IDs.\n\n" +
                    "For each field specified in the request, you can provide a list of IDs, and the endpoint " +
                    "will return the corresponding values and metadata for those IDs.\n\n" +
                    "This is particularly useful when you need to display human-readable labels for ID values " +
                    "stored in contacts, or when you need additional metadata about specific field values.\n\n" +
                    "The request format is a JSON object where keys are fully qualified field names and values " +
                    "are arrays of IDs. For example: `{\"organisation_contacts~organisation\": [123, 456]}`")    public Map<String, List<FieldIdValue>> getFieldValuesById(
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
    @ApiOperation(
            value = "Retrieve a specific contact by ID",
            notes = "This endpoint returns detailed information about a single contact identified by its ID.\n\n" +
                    "The response includes all available fields for the contact, such as name, title, " +
                    "organization, email, phone numbers, and any other information stored in the system.\n\n" +
                    "This endpoint is useful for displaying contact details or for retrieving a contact's " +
                    "information before updating it. Authentication is required to access this endpoint.")
    public SwaggerContact getContact(@ApiParam("contact id") @PathParam("id") Long id) {
        Map<String, Object> contact = ContactUtil.getContact(id);
        return new SwaggerContact(contact);
    }

    @POST
    @Path("/batch")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getContact", ui = false)
    @ApiOperation(
            value = "Retrieve multiple contacts by their IDs",
            notes = "This endpoint allows you to fetch information for multiple contacts in a single request.\n\n" +
                    "The request body should contain an array of contact IDs for which you want to retrieve information.\n\n" +
                    "The response includes complete information for each requested contact, with the same structure " +
                    "as the single contact endpoint. If a contact ID is not found or not accessible, it will be omitted " +
                    "from the response.\n\n" +
                    "This endpoint requires authentication to access contact information.")
    public Collection<Map<String, Object>> getContact(List<Long> ids) {
        return ContactUtil.getContacts(ids);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = {AuthRule.AUTHENTICATED, AuthRule.AMP_OFFLINE_OPTIONAL}, id = "createContact", ui = false)
    @ApiOperation(
            value = "Create new contact",
            notes = "This endpoint allows you to create a new contact in the system.\n\n" +
                    "The request body should contain all required contact information according to the field " +
                    "specifications returned by the fields endpoint. This includes personal information, " +
                    "organization details, contact methods, and any other required fields.\n\n" +
                    "On success, the endpoint returns a summary representation of the newly created contact " +
                    "including its system-generated ID. If validation fails, detailed error messages will be " +
                    "returned indicating which fields have issues.\n\n" +
                    "This endpoint requires authentication to create contacts in the system.")
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
    @ApiOperation(
            value = "Update an existing contact",
            notes = "This endpoint allows you to update an existing contact identified by its ID.\n\n" +
                    "The path parameter specifies the ID of the contact to update, and the request body should " +
                    "contain the updated contact information. All required fields must be included in the request, " +
                    "not just the fields being changed.\n\n" +
                    "On success, the endpoint returns a summary representation of the updated contact. " +
                    "If validation fails, detailed error messages will be returned indicating which fields have issues.\n\n" +
                    "This endpoint requires authentication to update contacts in the system.")
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
