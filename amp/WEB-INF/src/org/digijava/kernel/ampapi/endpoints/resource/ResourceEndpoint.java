package org.digijava.kernel.ampapi.endpoints.resource;

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

/**
 * @author Viorel Chihai
 */
@Path("resource")
public class ResourceEndpoint implements ErrorReportingEndpoint {
    
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
        return AmpFieldsEnumerator.PUBLIC_ENUMERATOR.getResourceFields();
    }
    
    /**
     * Returns a list of possible values for each requested field.
     * <p>If value can be translated then each possible value will contain value-translations element, a map where key
     * is language code and value is translated value.</p>
     * <h3>Sample request:</h3><pre>
     * ["type"]
     * </pre>
     * <h3>Sample response:</h3><pre>
     * {
     *   "type": [
     *     {
     *       "id": 54,
     *       "value": "Contract",
     *       "translated-value": {
     *         "en": "Contract"
     *       }
     *     },
     *     {
     *       "id": 46,
     *       "value": "Project Document",
     *       "translated-value": {
     *         "en": "Project Document"
     *       }
     *     }
     *   ]
     * }
     * </pre>
     *
     * @implicitParam translations|string|query|false|||||false|pipe separated list of language codes
     * @param fields list of fully qualified resource fields
     * @return list of possible values grouped by field
     */
    @POST
    @Path("field/values")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getResourceMultiValues", ui = false)
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
        return PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(fieldName, AmpResource.class, null);
    }
    
    /**
     * Retrieve resource by uuid.
     * 
     * <h3>Sample response:</h3><pre>
     *  {
     *     "uuid": "05a2f2d4-58f5-4198-8a05-cf42a758ce85",
     *     "title": "fda",
     *     "file_name": null,
     *     "web_link": "https://www.postgresql.org/docs/9.2/static/sql-createcast.html",
     *     "description": "fdas",
     *     "note": "fda",
     *     "type": 50,
     *     "url": "/contentrepository/downloadFile.do?uuid=05a2f2d4-58f5-4198-8a05-cf42a758ce85",
     *     "year_of_publication": "2002",
     *     "adding_date": "2018-05-03T15:03:40.607+0300",
     *     "file_size": 0,
     *     "public": false,
     *     "private": true,
     *     "creator_email": "atl@amp.org",
     *     "team": null,
     *     "team_member": 14
     *  }
     *  </pre>
     * @param uuid resource uuid
     */
    @GET
    @Path("{uuid}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getResource", ui = false)
    public JsonBean getResource(@PathParam("uuid") String uuid) {
        return ResourceUtil.getResource(uuid);
    }
    
    /**
     * Retrieve all resources from AMP.
     * 
     * @return list of resources
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getAllResources", ui = false)
    public List<JsonBean> getAllResources() {
        return ResourceUtil.getAllResources();
    }
    
    /**
     * Retrieve resources from AMP.
     * 
     * <h3>Sample request:</h3><pre>
     *  ["02af826a-d89e-4f7b-a30b-0a79630d2151", 
     *  "66434e33-d8db-4787-93e6-be09ae828de4", 
     *  "bb5cfc4a-9399-4afa-bef2-4a0e74c4a728"
     *  ]
     * </pre>
     * 
     * @param uuids the list of uuids
     * @return list of resources
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "getAllResourcesByIds", ui = false)
    public List<JsonBean> getAllResources(List<String> uuids) {
        return ResourceUtil.getAllResources(uuids);
    }

    /**
     * Create new resource.
     * @param resource the resource to create
     * @return brief representation of resource
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "createResource", ui = false)
    public JsonBean createResource(JsonBean resource) {
        ResourceImporter importer = new ResourceImporter();
        List<ApiErrorMessage> errors = importer.createResource(resource);
        return ResourceUtil.getImportResult(importer.getResource(), importer.getNewJson(), errors);
    }

    @Override
    public Class<?> getErrorsClass() {
        return ResourceErrors.class;
    }
    
}
