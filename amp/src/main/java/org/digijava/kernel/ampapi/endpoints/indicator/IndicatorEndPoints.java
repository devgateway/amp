package org.digijava.kernel.ampapi.endpoints.indicator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.fasterxml.jackson.annotation.JsonView;
import com.sun.jersey.multipart.FormDataParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueLabel;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueService;
import org.digijava.kernel.ampapi.endpoints.gis.services.BoundariesService;
import org.digijava.kernel.ampapi.endpoints.gis.services.Boundary;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.ColorRampUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

@Path("indicator")
@Api("indicator")
public class IndicatorEndPoints {

    @GET
    @Path("/indicator-layer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
        @ApiMethod(id = "getIndicators", ui = false)
    @ApiOperation(value = "Retrieve and provide a list of indicator layers.")
    public IndicatorPageDataResult getIndicators(
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
    @ApiOperation("Retrieve and provide indicator layer by Id.")
    public Indicator getIndicatorById(
            @ApiParam("indicator ID to query for indicator layer") @PathParam("id") long indicatorId) {
        return IndicatorService.getIndicatorById(indicatorId);
    }

    @GET
    @Path("/indicator-layer/check-name")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "checkName", ui = false)
    @ApiOperation("Retrieve true or false if an indicator layer with the param name exists.")
    public CheckNameResult checkName(
            @ApiParam("indicator name to check the uniqueness") @QueryParam("name") String name) {
        return IndicatorService.checkName(name);
    }

    @DELETE
    @Path("/indicator-layer/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "deleteIndicatorById", ui = false)
    @ApiOperation(value = "Delete indicator layer by Id.")
    public IndicatorOperationResult deleteIndicatorById(
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
                    + "</dl>\n")
    @JsonView(Indicator.LayerView.class)
    public IndicatorOperationDataResult saveIndicator(Indicator indicator) {
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
    public Response download(
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
                    + "The file to be uploaded is generated on /indicator-layer/download\n")
    public IndicatorImporterResult importIndicator(
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
                    + "    ]\n"
                    + "   },\n"
                    + "    ....\n"
                    + " ]</pre>"
    )
    public List<Map<String, String[]>> getColors() {
        List<Map<String, String[]>> colorList = new ArrayList<>();
        String[][] colors = ColorRampUtil.getColors();
        int index = 0;
        for (String[] color : colors) {
            Map<String, String[]> colorMap = new HashMap<>();
            colorMap.put(String.valueOf(index++), color);
            colorList.add(colorMap);
        }

        return colorList;
    }

    @GET
    @Path("/adm-level")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Retrieve and provide administrative levels.")
    public List<CategoryValueLabel> getLevels() {
        Collection<AmpCategoryValue> admLevels = CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(
                "implementation_location", false);
        Map<String, Boundary> boundariesMap = BoundariesService.getBoundariesAsList();
        List<CategoryValueLabel> indicatorLayerList = new ArrayList<>();
        for (AmpCategoryValue level: admLevels) {
            if (boundariesMap.containsKey(IndicatorEPConstants.ADM_PREFIX + level.getIndex())) {
                indicatorLayerList.add(new CategoryValueLabel(level.getId(), level.getValue(), level.getLabel()));
            }            
        }
        
        return indicatorLayerList;
    }

    @GET
    @Path("/access-type")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Retrieve and provide access types.")
    public List<CategoryValueLabel> getAccessTypes() {
        List<CategoryValueLabel> accessTypeList = new ArrayList<>();
        for (IndicatorAccessType access: IndicatorAccessType.values()){
            accessTypeList.add(new CategoryValueLabel(access.getValue(), access.name(),
                    TranslatorWorker.translateText(access.name())));
        }

        return accessTypeList;
    }

    @GET
    @Path("/indicator-types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation("Retrieve and provide indicator layer types.")
    public List<CategoryValueLabel> getIndicatorLayerTypes() {
        return CategoryValueService.getCategoryValues(CategoryConstants.INDICATOR_LAYER_TYPE_KEY);
    }

    @POST
    @Path("/population-layers")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "designate-population-layers", ui = false, authTypes = AuthRule.IN_ADMIN)
    @ApiOperation(
            value = "Configures new list of indicator layers to be designated as population layers.",
            notes = "</br>\nOnly 'Count' type layers can be designated as population layers.\n"
    )
    public void setPopulationLayers(PopulationLayerRequest populationLayerRequest) {
        new PopulationLayerDesignator().designateAsPopulationLayers(populationLayerRequest);
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

}
