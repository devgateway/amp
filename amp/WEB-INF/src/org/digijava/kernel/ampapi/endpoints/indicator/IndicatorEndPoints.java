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

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueService;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.gis.services.BoundariesService;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.ColorRampUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import com.sun.jersey.multipart.FormDataParam;

import net.sf.json.JSONObject;

@Path("indicator")
public class IndicatorEndPoints implements ErrorReportingEndpoint {

    private static final Logger logger = Logger.getLogger(IndicatorEndPoints.class);

    /**
     * Retrieve and provide a list of indicator layers.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>page</b><dd> - the information about the page<pre>
     *         recordsPerPage: records per page
     *         currentPageNumber: current page
     *         totalPageCount: total of pages
     *         totalRecords: total of records
     *
     * </pre>
     * <dt><b>data</b><dd> - the list of the indicators layers
     * for more details of indicators layer info go to /indicator-layer/{id}
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     * {
     *   "page": {
     *     "recordsPerPage": 10,
     *     "currentPageNumber": 1,
     *     "totalPageCount": 2,
     *     "totalRecords": 15
     *   },
     *   "data": [
     *     {
     *       "id": 24,
     *       "name": {
     *         "pt": "name in pt",
     *         "tm": null,
     *         "en": "name in en"
     *       },
     *       "description": {
     *         "pt": "descrip in pt",
     *         "tm": null,
     *         "en": "descrip in en"
     *       },
     *       "unit": {
     *         "pt": "unit in pt",
     *         "tm": null,
     *         "en": "unit in en"
     *       },
     *       "numberOfClasses": 2,
     *       "admLevelId": 77,
     *       "admLevelName": "Region",
     *       "adminLevel": "adm-1",
     *       "isPopulation": false,
     *       "indicatorTypeId": 261,
     *       "accessTypeId": 1,
     *       "createdOn": "22/09/2016",
     *       "updatedOn": "22/09/2016",
     *       "createdBy": "atl@amp.org",
     *       "colorRampId": 0,
     *       "colorRamp": [
     *         "#e9ced2",
     *         "#8d1874"
     *       ],
     *       "sharedWorkspaces": [],
     *       "numberOfImportedRecords": 0
     *     },
     *  ....
     *   ]
     * }</pre>
     *
     * @param offset first element in the list
     * @param count size of the page
     * @param orderBy field to order the list
     * @param sort asc / desc order
     *
     * @return a JSON object with a list of indicators and the information of the page.
     */
    @GET
    @Path("/indicator-layer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
        @ApiMethod(id = "getIndicators", ui = false)
    public JsonBean getIndicators(@QueryParam("offset") Integer offset, @QueryParam("count") Integer count, @QueryParam("orderby") String orderBy, @QueryParam("sort") String sort) {
        return IndicatorService.getIndicators(offset, count, orderBy,sort );
    }

    /**
     * Retrieve and provide indicator layer by Id.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - the name of indicator in the available languages
     * <dt><b>description</b><dd> - the description of indicator in the available languages
     * <dt><b>unit</b><dd> - the unit of indicator in the available languages
     * <dt><b>numberOfClasses</b><dd> - number of classes to split the range
     * <dt><b>admLevelId</b><dd> - the administrative level id
     * <dt><b>admLevelName</b><dd> - the administrative level name
     * <dt><b>adminLevel</b><dd> - the administrative level
     * <dt><b>isPopulation</b><dd> - true or false if the indicator layer is Population
     * <dt><b>indicatorTypeId</b><dd> - the indicator type
     * <dt><b>accessTypeId</b><dd> - the access type
     * <dt><b>createdOn</b><dd> - the date of creation
     * <dt><b>updatedOn</b><dd> - the date of last updated
     * <dt><b>createdBy</b><dd> - email of the user who created the indicator
     * <dt><b>colorRampId</b><dd> - the color ramp id
     * <dt><b>colorRamp</b><dd> -  array of color ramp
     * <dt><b>sharedWorkspaces</b><dd> - array of workspaces where the indicator layer can be showed
     * <dt><b>numberOfImportedRecords</b><dd> - number of records imported to this indicator
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     * {
     *   "id": 24,
     *   "name": {
     *     "pt": "name in pt",
     *     "tm": null,
     *     "en": "name in en"
     *   },
     *   "description": {
     *     "pt": "descrip in pt",
     *     "tm": null,
     *     "en": "descrip in en"
     *   },
     *   "unit": {
     *     "pt": "unit in pt",
     *     "tm": null,
     *     "en": "unit in en"
     *   },
     *   "numberOfClasses": 2,
     *   "admLevelId": 77,
     *   "admLevelName": "Region",
     *   "adminLevel": "adm-1",
     *   "isPopulation": false,
     *   "indicatorTypeId": 261,
     *   "accessTypeId": 1,
     *   "createdOn": "22/09/2016",
     *   "updatedOn": "22/09/2016",
     *   "createdBy": "atl@amp.org",
     *   "colorRampId": 0,
     *   "colorRamp": [
     *     "#e9ced2",
     *     "#8d1874"
     *   ],
     *   "sharedWorkspaces": [],
     *   "numberOfImportedRecords": 0
     * }</pre>
     *
     * @param indicatorId indicator ID to query for indicator layer
     *
     * @return a JSON object with the indicator layer
     */
    @GET
    @Path("/indicator-layer/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getIndicatorById", ui = false)
    public JsonBean getIndicatorById(@PathParam("id") long indicatorId) {

        return IndicatorService.getIndicatorById(indicatorId);
    }

    /**
     * Retrieve true or false if an indicator layer with the param name exists.
     * </br>
     * <dl>
     * The  JSON object holds information regarding:
     * <dt><b>result</b><dd> - true / false if the name exists or not
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *   "result": false
     * }</pre>
     *
     * @param name indicator name to check the uniqueness
     *
     * @return a JSON object indicating whether the name exists or not
     */
    @GET
    @Path("/indicator-layer/check-name")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "checkName", ui = false)
    public JsonBean checkName(@QueryParam("name") String name) {
        return IndicatorService.checkName(name);
    }
    /**
     * Delete indicator layer by Id.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>result</b><dd> - "DELETED" if the indicator layer was deleted correctly
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     *  {
     *    "result": "DELETED"
     *  }</pre>
     *
     * @param indicatorId indicator ID to query for indicator layer to be deleted
     *
     * @return a JSON object indicating if the layer was deleted or errors
     */
    @DELETE
    @Path("/indicator-layer/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "deleteIndicatorById", ui = false)
    public JsonBean deleteIndicatorById(@PathParam("id") long indicatorId) {
        return IndicatorService.deleteIndicatorById(indicatorId);
    }

    /**
     * Create or updated indicator layer.
     * </br>
     * <dl>
     * The JSON object holds information regarding:
     * <dt><b>result</b><dd> - "INSERTED" if the indicator layer was inserted correctly
     * <dt><b>data</b><dd> - the indicator layer inserted
     * for more details of indicators layer info go to /indicator-layer/{id}
     * </dl></br></br>
     *
     * <h3>Sample Imput:</h3><pre>
     * {
     *     "name": "New layer",
     *     "description": "layer description",
     *     "numberOfClasses": 5,
     *     "unit": "",
     *     "admLevelId": 77,
     *     "accessTypeId": 2,
     *     "indicatorTypeId": 262,
     *     "createdOn": "2016-06-24",
     *     "updatedOn": "2016-06-24",
     *     "createdBy": "atl@amp.org",
     *     "colorRampId": 5,
     *     "numberOfImportedRecords": 0
     * }</pre>
     *
     * <h3>Sample Output:</h3><pre>
     * {
     *  "result": "INSERTED",
     *  "data":
     *  {
     *      "id": 70,
     *      "name": "New layer",
     *      "description": "layer description",
     *      "numberOfClasses": 5,
     *      "unit": "",
     *      "admLevelId": 77,
     *      "accessTypeId": 2,
     *      "indicatorTypeId": 262,
     *      "createdOn": "2016-06-24",
     *      "updatedOn": "2016-06-24",
     *      "createdBy": "atl@amp.org",
     *      "colorRampId": 5,
     *      "colorRamp": [
     *          "#32786c",
     *          "#155848",
     *          "#7abcbb",
     *          "#549a93",
     *          "#a4dfe4"
     *      ],
     *      "numberOfImportedRecords": 0
     *  }
     * }</pre>
     *
     * @param indicator a JSON with the indicator layer information
     *
     * @return a JSON object indicating if the layer was inserted or errors
     */
    @POST
    @Path("/indicator-layer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "saveIndicator", ui = false)
    public JsonBean saveIndicator(final JsonBean indicator) {
        return IndicatorService.saveIndicator(indicator);
    }

    /**
     * Export indicator layers values by name.
     * </br>
     * <dl>
     * The file exported will have 2 columns, the first one with the category values for the administrative level indicated
     * and the second one with the values uploaded for this indicator layer if they exist.
     * </dl></br></br>
     *
     * @param admLevelId adm Level ID to query for category value
     * @param indicatorName indicator name to query for indicator name
     *
     * @return StreamingOutput with the file generated
     */
    @GET
    @Path("/indicator-layer/download")
    @Produces("application/vnd.ms-excel")
    public StreamingOutput download(@QueryParam("admLevelId") long admLevelId, @QueryParam("name") String indicatorName) {

        return IndicatorExporter.download(admLevelId,indicatorName);
    }

    /**
     * Import indicator layers values in the file.
     * </br>
     * <dl>
     * The file to be uploaded is generated on /indicator-layer/download
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     * {
     *   "values": [
     *     {
     *       "value": 214,
     *       "id": 214,
     *       "geoId": null,
     *       "name": "Timor-Leste"
     *     }
     *   ]
     * }</pre>
     *
     * @param uploadedInputStream file with values to be uploaded
     * @param admLevelId to check if the file has same adm level than the indicator
     *
     * @return a JSON object with the list of the values inserted or errors
     *
     */
    @POST
    @Path("/indicator-layer/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public JsonBean importIndicator(
            @FormDataParam("admLevelId") long admLevelId,
            @FormDataParam("file") InputStream uploadedInputStream
    ) {
        return IndicatorImporter.importIndicator(uploadedInputStream, admLevelId);
    }

    /**
     * Retrieve and provide a color list.
     * </br>
     * <dl>
     * The list of JSON object holds information regarding:
     * <dt><b>name</b><dd> - the index of the color
     * <dt><b>value</b><dd> - an array of colors
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     *  [
     *    {
     *     "0": [
     *      "#e9ced2",
     *      "#e4b9c3",
     *      "#e1a4b6",
     *      "#e08cab",
     *      "#e071a4",
     *      "#e050a0",
     *      "#cb4196",
     *      "#b6338c",
     *      "#a12580",
     *      "#8d1874"
     *     ]
     *    },
     *     ....
     *  ]</pre>
     *
     * @return a collection of JSON objects with a list of colors
     */
    @GET
    @Path("/amp-color")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
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

    /**
     * Retrieve and provide administrative levels.
     * </br>
     * <dl>
     * The administrative levels JSON object holds information regarding:
     * <dt><b>id</b><dd> - the id of the administrative levels
     * <dt><b>label</b><dd> - the label of the administrative levels
     * <dt><b>value</b><dd> - the value of the administrative levels
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     *  [
     *      {
     *      "id": 1,
     *      "label": "Country",
     *      "value": "Country"
     *      },
     *      ....
     *  ]</pre>
     *
     * @return a collection of JSON object with the available administrative levels
     */
    @GET
    @Path("/adm-level")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Collection<JsonBean> getLevels() {
        Collection<AmpCategoryValue> admLevels = CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(
                "implementation_location", false);
        Map<String, JSONObject> jsonFilesMap = BoundariesService.getBoundariesAsList();        
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

    /**
     * Retrieve and provide access types.
     * </br>
     * <dl>
     * The access types JSON object holds information regarding:
     * <dt><b>id</b><dd> - the id of the access types
     * <dt><b>value</b><dd> - the value of the access types
     * <dt><b>label</b><dd> - the label of the access types
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     *  [
     *      {
     *      "id": 1,
     *      "value": "Private",
     *      "label": "Private"
     *      },
     *      ....
     *  ]</pre>
     *
     * @return a collection of JSON object with the available access types
     */
    @GET
    @Path("/access-type")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
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

    /**
     * Retrieve and provide indicator layer types.
     * </br>
     * <dl>
     * The access types JSON object holds information regarding:
     * <dt><b>id</b><dd> - the id of the indicator layer type
     * <dt><b>orig-name</b><dd> - the original name, not translated of the indicator layer type
     * <dt><b>name</b><dd> - the name, translated of the indicator layer type
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     *  [
     *     {
     *      "id" : 123,
     *      "orig-name" : "Ration (% of Total Population)",
     *      "name" : “Ration (% of Total Population)”
     *     },
     *     ....
     *  ]</pre>
     *
     * @return a list of JSON objects with the available indicator layer types
     */
    @GET
    @Path("/indicator-types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<JsonBean> getIndicatorLayerTypes() {
        return CategoryValueService.getCategoryValues(CategoryConstants.INDICATOR_LAYER_TYPE_KEY, true);
    }

    /**
     * Configures new list of indicator layers to be designated as population layers.
     * </br>
     * Only 'Count' type layers can be designated as population layers.
     * <dl>
     * The access types JSON object holds information regarding:
     * <dt><b>layersIds</b><dd> - the array of indicators ids to be designated as population layers
     * </dl></br></br>
     *
     * <h3>Sample Imput:</h3><pre>
     * {
     *   "layersIds": [5,10,11,23]
     * }</pre>
     * </br>
     * <h3>Sample Output:</h3><pre>
     * {
     *   “layersIds” : [5,10,11 23, ...]
     * }</pre>
     *
     * @implicitParam X-Auth-Token|string|header
     * @param input a JSON object with a list of layers Ids
     * @return no content or errors
     */
    @POST
    @Path("/population-layers")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "designate-population-layers", ui = false, authTypes = AuthRule.IN_ADMIN)
    public JsonBean setPopulationLayers(JsonBean input) {
        return new PopulationLayerDesignator().designateAsPopulationLayers(input);
    }

    /**
     * Provide a list of all possible indicator layers to be designated as population layers.
     * </br>
     * <dl>
     * E.g. at this moment we agreed that only "count" population layers and "non-country" population layers are allowed
     * </dl></br></br>
     *
     * <h3>Sample Output:</h3><pre>
     * [2, 3, 4, ...]</pre>
     *
     * @return a list of all possible indicator layers to be designated as population layers.
     */
    @GET
    @Path("/population-layers-options")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
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
