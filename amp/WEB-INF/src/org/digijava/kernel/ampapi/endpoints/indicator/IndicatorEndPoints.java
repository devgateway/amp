package org.digijava.kernel.ampapi.endpoints.indicator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueService;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.gis.services.BoundariesService;
import org.digijava.kernel.ampapi.endpoints.gis.services.Boundary;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.ColorRampUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import com.sun.jersey.multipart.FormDataParam;

@Path("indicator")
@Api("indicator")
public class IndicatorEndPoints implements ErrorReportingEndpoint {

    @GET
    @Path("/indicator-layer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
        @ApiMethod(id = "getIndicators", ui = false)
    @ApiOperation(
            value = "Retrieve and provide a list of indicator layers.",
            notes = "</br>\n"
                    + "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>page</b><dd> - the information about the page<pre>\n"
                    + "        recordsPerPage: records per page\n"
                    + "        currentPageNumber: current page\n"
                    + "        totalPageCount: total of pages\n"
                    + "        totalRecords: total of records\n"
                    + "</pre>\n"
                    + "<dt><b>data</b><dd> - the list of the indicators layers\n"
                    + "for more details of indicators layer info go to /indicator-layer/{id}\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  \"page\": {\n"
                    + "    \"recordsPerPage\": 10,\n"
                    + "    \"currentPageNumber\": 1,\n"
                    + "    \"totalPageCount\": 2,\n"
                    + "    \"totalRecords\": 15\n"
                    + "  },\n"
                    + "  \"data\": [\n"
                    + "    {\n"
                    + "      \"id\": 24,\n"
                    + "      \"name\": {\n"
                    + "        \"pt\": \"name in pt\",\n"
                    + "        \"tm\": null,\n"
                    + "        \"en\": \"name in en\"\n"
                    + "      },\n"
                    + "      \"description\": {\n"
                    + "        \"pt\": \"descrip in pt\",\n"
                    + "        \"tm\": null,\n"
                    + "        \"en\": \"descrip in en\"\n"
                    + "      },\n"
                    + "      \"unit\": {\n"
                    + "        \"pt\": \"unit in pt\",\n"
                    + "        \"tm\": null,\n"
                    + "        \"en\": \"unit in en\"\n"
                    + "      },\n"
                    + "      \"numberOfClasses\": 2,\n"
                    + "      \"admLevelId\": 77,\n"
                    + "      \"admLevelName\": \"Region\",\n"
                    + "      \"adminLevel\": \"adm-1\",\n"
                    + "      \"isPopulation\": false,\n"
                    + "      \"indicatorTypeId\": 261,\n"
                    + "      \"accessTypeId\": 1,\n"
                    + "      \"createdOn\": \"22/09/2016\",\n"
                    + "      \"updatedOn\": \"22/09/2016\",\n"
                    + "      \"createdBy\": \"atl@amp.org\",\n"
                    + "      \"colorRampId\": 0,\n"
                    + "      \"colorRamp\": [\n"
                    + "        \"#e9ced2\",\n"
                    + "        \"#8d1874\"\n"
                    + "      ],\n"
                    + "      \"sharedWorkspaces\": [],\n"
                    + "      \"numberOfImportedRecords\": 0\n"
                    + "    },\n"
                    + " ....\n"
                    + "  ]\n"
                    + "}</pre>")
    public JsonBean getIndicators(
            @ApiParam("first element in the list") @QueryParam("offset") Integer offset,
            @ApiParam("size of the page") @QueryParam("count") Integer count,
            @ApiParam("field to order the list") @QueryParam("orderby") String orderBy,
            @ApiParam("asc / desc order") @QueryParam("sort") String sort) {
        return IndicatorService.getIndicators(offset, count, orderBy,sort );
    }

    @GET
    @Path("/indicator-layer/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getIndicatorById", ui = false)
    @ApiOperation(
            value = "Retrieve and provide indicator layer by Id.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>name</b><dd> - the name of indicator in the available languages\n"
                    + "<dt><b>description</b><dd> - the description of indicator in the available languages\n"
                    + "<dt><b>unit</b><dd> - the unit of indicator in the available languages\n"
                    + "<dt><b>numberOfClasses</b><dd> - number of classes to split the range\n"
                    + "<dt><b>admLevelId</b><dd> - the administrative level id\n"
                    + "<dt><b>admLevelName</b><dd> - the administrative level name\n"
                    + "<dt><b>adminLevel</b><dd> - the administrative level\n"
                    + "<dt><b>isPopulation</b><dd> - true or false if the indicator layer is Population\n"
                    + "<dt><b>indicatorTypeId</b><dd> - the indicator type\n"
                    + "<dt><b>accessTypeId</b><dd> - the access type\n"
                    + "<dt><b>createdOn</b><dd> - the date of creation\n"
                    + "<dt><b>updatedOn</b><dd> - the date of last updated\n"
                    + "<dt><b>createdBy</b><dd> - email of the user who created the indicator\n"
                    + "<dt><b>colorRampId</b><dd> - the color ramp id\n"
                    + "<dt><b>colorRamp</b><dd> -  array of color ramp\n"
                    + "<dt><b>sharedWorkspaces</b><dd> - array of workspaces where the indicator layer can be showed\n"
                    + "<dt><b>numberOfImportedRecords</b><dd> - number of records imported to this indicator\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  \"id\": 24,\n"
                    + "  \"name\": {\n"
                    + "    \"pt\": \"name in pt\",\n"
                    + "    \"tm\": null,\n"
                    + "    \"en\": \"name in en\"\n"
                    + "  },\n"
                    + "  \"description\": {\n"
                    + "    \"pt\": \"descrip in pt\",\n"
                    + "    \"tm\": null,\n"
                    + "    \"en\": \"descrip in en\"\n"
                    + "  },\n"
                    + "  \"unit\": {\n"
                    + "    \"pt\": \"unit in pt\",\n"
                    + "    \"tm\": null,\n"
                    + "    \"en\": \"unit in en\"\n"
                    + "  },\n"
                    + "  \"numberOfClasses\": 2,\n"
                    + "  \"admLevelId\": 77,\n"
                    + "  \"admLevelName\": \"Region\",\n"
                    + "  \"adminLevel\": \"adm-1\",\n"
                    + "  \"isPopulation\": false,\n"
                    + "  \"indicatorTypeId\": 261,\n"
                    + "  \"accessTypeId\": 1,\n"
                    + "  \"createdOn\": \"22/09/2016\",\n"
                    + "  \"updatedOn\": \"22/09/2016\",\n"
                    + "  \"createdBy\": \"atl@amp.org\",\n"
                    + "  \"colorRampId\": 0,\n"
                    + "  \"colorRamp\": [\n"
                    + "    \"#e9ced2\",\n"
                    + "    \"#8d1874\"\n"
                    + "  ],\n"
                    + "  \"sharedWorkspaces\": [],\n"
                    + "  \"numberOfImportedRecords\": 0\n"
                    + "}</pre>")
    public JsonBean getIndicatorById(
            @ApiParam("indicator ID to query for indicator layer") @PathParam("id") long indicatorId) {

        return IndicatorService.getIndicatorById(indicatorId);
    }

    @GET
    @Path("/indicator-layer/check-name")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "checkName", ui = false)
    @ApiOperation(
            value = "Retrieve true or false if an indicator layer with the param name exists.",
            notes = "<dl>\n"
                    + "The  JSON object holds information regarding:\n"
                    + "<dt><b>result</b><dd> - true / false if the name exists or not\n"
                    + "</dl></br></br>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  \"result\": false\n"
                    + "}</pre>")
    public JsonBean checkName(@ApiParam("indicator name to check the uniqueness") @QueryParam("name") String name) {
        return IndicatorService.checkName(name);
    }

    @DELETE
    @Path("/indicator-layer/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "deleteIndicatorById", ui = false)
    @ApiOperation(
            value = "Delete indicator layer by Id.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>result</b><dd> - \"DELETED\" if the indicator layer was deleted correctly\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + " {\n"
                    + "   \"result\": \"DELETED\"\n"
                    + " }</pre>")
    public JsonBean deleteIndicatorById(
            @ApiParam("indicator ID to query for indicator layer to be deleted") @PathParam("id") long indicatorId) {
        return IndicatorService.deleteIndicatorById(indicatorId);
    }

    @POST
    @Path("/indicator-layer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "saveIndicator", ui = false)
    @ApiOperation(
            value = "Create or updated indicator layer.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>result</b><dd> - \"INSERTED\" if the indicator layer was inserted correctly\n"
                    + "<dt><b>data</b><dd> - the indicator layer inserted\n"
                    + "for more details of indicators layer info go to /indicator-layer/{id}\n"
                    + "</dl>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + " \"result\": \"INSERTED\",\n"
                    + " \"data\":\n"
                    + " {\n"
                    + "     \"id\": 70,\n"
                    + "     \"name\": \"New layer\",\n"
                    + "     \"description\": \"layer description\",\n"
                    + "     \"numberOfClasses\": 5,\n"
                    + "     \"unit\": \"\",\n"
                    + "     \"admLevelId\": 77,\n"
                    + "     \"accessTypeId\": 2,\n"
                    + "     \"indicatorTypeId\": 262,\n"
                    + "     \"createdOn\": \"2016-06-24\",\n"
                    + "     \"updatedOn\": \"2016-06-24\",\n"
                    + "     \"createdBy\": \"atl@amp.org\",\n"
                    + "     \"colorRampId\": 5,\n"
                    + "     \"colorRamp\": [\n"
                    + "         \"#32786c\",\n"
                    + "         \"#155848\",\n"
                    + "         \"#7abcbb\",\n"
                    + "         \"#549a93\",\n"
                    + "         \"#a4dfe4\"\n"
                    + "     ],\n"
                    + "     \"numberOfImportedRecords\": 0\n"
                    + " }\n"
                    + "}</pre>")
    public JsonBean saveIndicator(SaveIndicatorRequest indicator) {
        return IndicatorService.saveIndicator(indicator);
    }

    @GET
    @Path("/indicator-layer/download")
    @Produces("application/vnd.ms-excel")
    @ApiOperation(
            value = "Export indicator layers values by name.",
            notes = "The file exported will have 2 columns, the first one with the category values for the "
                    + "administrative level indicated and the second one with the values uploaded for this "
                    + "indicator layer if they exist.")
    public StreamingOutput download(
            @ApiParam("indicator name to query for indicator name") @QueryParam("admLevelId") long admLevelId,
            @ApiParam("adm Level ID to query for category value") @QueryParam("name") String indicatorName) {

        return IndicatorExporter.download(admLevelId,indicatorName);
    }

    @POST
    @Path("/indicator-layer/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Import indicator layers values in the file.",
            notes = "Parameters:\n"
                    + "* admLevelId - to check if the file has same adm level than the indicator\n"
                    + "* file - file with values to be uploaded\n\n"
                    + "<dl>\n"
                    + "The file to be uploaded is generated on /indicator-layer/download\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  \"values\": [\n"
                    + "    {\n"
                    + "      \"value\": 214,\n"
                    + "      \"id\": 214,\n"
                    + "      \"geoId\": null,\n"
                    + "      \"name\": \"Timor-Leste\"\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}</pre>")
    public JsonBean importIndicator(
            @FormDataParam("admLevelId") long admLevelId,
            @FormDataParam("file") InputStream uploadedInputStream) {
        return IndicatorImporter.importIndicator(uploadedInputStream, admLevelId);
    }

    @GET
    @Path("/amp-color")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "List color ramps",
            notes = "<dl>\n"
                    + "The list of JSON object holds information regarding:\n"
                    + "<dt><b>name</b><dd> - the index of the color\n"
                    + "<dt><b>value</b><dd> - an array of colors\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + " [\n"
                    + "   {\n"
                    + "    \"0\": [\n"
                    + "     \"#e9ced2\",\n"
                    + "     \"#e4b9c3\",\n"
                    + "     \"#e1a4b6\",\n"
                    + "     \"#e08cab\",\n"
                    + "     \"#e071a4\",\n"
                    + "     \"#e050a0\",\n"
                    + "     \"#cb4196\",\n"
                    + "     \"#b6338c\",\n"
                    + "     \"#a12580\",\n"
                    + "     \"#8d1874\"\n"
                    + "    ]\n"
                    + "   },\n"
                    + "    ....\n"
                    + " ]</pre>"
    )
    public Collection<JsonBean> getColors() {
        Collection<JsonBean> colorList = new ArrayList<JsonBean>();
        String[][] colors = ColorRampUtil.getColors();
        int index = 0;
        for(String[] color :colors){
            JsonBean c = new JsonBean();
            c.set(String.valueOf(index++),color);
            colorList.add(c);
        }

        return colorList;
    }

    @GET
    @Path("/adm-level")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Retrieve and provide administrative levels.",
            notes = "<dl>\n"
                    + "The administrative levels JSON object holds information regarding:\n"
                    + "<dt><b>id</b><dd> - the id of the administrative levels\n"
                    + "<dt><b>label</b><dd> - the label of the administrative levels\n"
                    + "<dt><b>value</b><dd> - the value of the administrative levels\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + " [\n"
                    + "     {\n"
                    + "     \"id\": 1,\n"
                    + "     \"label\": \"Country\",\n"
                    + "     \"value\": \"Country\"\n"
                    + "     },\n"
                    + "     ....\n"
                    + " ]</pre>")
    public Collection<JsonBean> getLevels() {
        Collection<AmpCategoryValue> admLevels = CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(
                "implementation_location", false);
        Map<String, Boundary> jsonFilesMap = BoundariesService.getBoundariesAsList();
        Collection<JsonBean> indicatorLayerList = new ArrayList<JsonBean>();
        for (AmpCategoryValue admLevel: admLevels){         
            if (jsonFilesMap.containsKey(IndicatorEPConstants.ADM_PREFIX + admLevel.getIndex())) {
                JsonBean categoryValue = new JsonBean();
                categoryValue.set(IndicatorEPConstants.ID, admLevel.getId());
                categoryValue.set(IndicatorEPConstants.LABEL, admLevel.getLabel());
                categoryValue.set(IndicatorEPConstants.VALUE, admLevel.getValue());
                indicatorLayerList.add(categoryValue);
            }            
        }
        return indicatorLayerList;
    }

    @GET
    @Path("/access-type")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Retrieve and provide access types.",
            notes = "<dl>\n"
                    + "The access types JSON object holds information regarding:\n"
                    + "<dt><b>id</b><dd> - the id of the access types\n"
                    + "<dt><b>value</b><dd> - the value of the access types\n"
                    + "<dt><b>label</b><dd> - the label of the access types\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + " [\n"
                    + "     {\n"
                    + "     \"id\": 1,\n"
                    + "     \"value\": \"Private\",\n"
                    + "     \"label\": \"Private\"\n"
                    + "     },\n"
                    + "     ....\n"
                    + " ]</pre>")
    public Collection<JsonBean> getAccessTypes() {

        Collection<JsonBean> accessTypeList = new ArrayList<JsonBean>();

        for (IndicatorAccessType access: IndicatorAccessType.values()){
            JsonBean accessJson = new JsonBean();
            accessJson.set(IndicatorEPConstants.ID,access.getValue());
            accessJson.set(IndicatorEPConstants.VALUE,access.name());
            accessJson.set(IndicatorEPConstants.LABEL,TranslatorWorker.translateText(access.name()));
            accessTypeList.add(accessJson);
        }

        return accessTypeList;
    }

    @GET
    @Path("/indicator-types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Retrieve and provide indicator layer types.",
            notes = "<dl>\n"
                    + "The access types JSON object holds information regarding:\n"
                    + "<dt><b>id</b><dd> - the id of the indicator layer type\n"
                    + "<dt><b>orig-name</b><dd> - the original name, not translated of the indicator layer type\n"
                    + "<dt><b>name</b><dd> - the name, translated of the indicator layer type\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + " [\n"
                    + "    {\n"
                    + "     \"id\" : 123,\n"
                    + "     \"orig-name\" : \"Ration (% of Total Population)\",\n"
                    + "     \"name\" : “Ration (% of Total Population)”\n"
                    + "    },\n"
                    + "    ....\n"
                    + " ]</pre>")
    public List<JsonBean> getIndicatorLayerTypes() {
        return CategoryValueService.getCategoryValues(CategoryConstants.INDICATOR_LAYER_TYPE_KEY, true);
    }

    @POST
    @Path("/population-layers")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "designate-population-layers", ui = false, authTypes = AuthRule.IN_ADMIN)
    @ApiOperation(
            value = "Configures new list of indicator layers to be designated as population layers.",
            notes = "</br>\n"
                    + "Only 'Count' type layers can be designated as population layers.\n"
                    + "<dl>\n"
                    + "The access types JSON object holds information regarding:\n"
                    + "<dt><b>layersIds</b><dd> - the array of indicators ids to be designated as population layers\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Imput:</h3><pre>\n"
                    + "{\n"
                    + "  \"layersIds\": [5,10,11,23]\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  “layersIds” : [5,10,11 23, ...]\n"
                    + "}</pre>"
    )
    public JsonBean setPopulationLayers(@ApiParam("a JSON object with a list of layers Ids") JsonBean input) {
        return new PopulationLayerDesignator().designateAsPopulationLayers(input);
    }

    @GET
    @Path("/population-layers-options")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "Provide a list of all possible indicator layers to be designated as population layers.",
            notes = "<dl>\n"
                    + "E.g. at this moment we agreed that only \"count\" population layers and \"non-country\" "
                    + "population layers are allowed\n"
                    + "</dl></br></br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "[2, 3, 4, ...]</pre>")
    public List<Long> getAllowedPopulationLayersOptions() {
        return new PopulationLayerDesignator().getAllowedPopulationLayersOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getErrorsClass() {
        return IndicatorErrors.class;
    }
}
