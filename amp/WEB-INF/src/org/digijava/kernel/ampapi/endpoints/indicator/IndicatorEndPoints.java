package org.digijava.kernel.ampapi.endpoints.indicator;

import com.sun.jersey.multipart.FormDataParam;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.CategoryValueService;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.ColorRampUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Path("indicator")
public class IndicatorEndPoints implements ErrorReportingEndpoint {

    private static final Logger logger = Logger.getLogger(IndicatorEndPoints.class);

    /**
     * Retrieve and provide a list of indicator layers
     * @param count page size
     * @param offset
     * @return <pre>
     * [
     *  {
     *      "id": 70,
     *      "name": "New layer",
     *      "description": "layer description",
     *      "numberOfClasses": 5,
     *      "unit": "",
     *      "admLevelId": 77,
     *      "accessTypeId": 2,
     *      "population": true,
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
     *  },
     *      ....
     * ]
     *  </pre>
     */
    @GET
    @Path("/indicator-layer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
        @ApiMethod(id = "getIndicators", ui = false)
    public JsonBean getIndicators(@QueryParam("offset") Integer offset, @QueryParam("count") Integer count, @QueryParam("orderby") String orderBy, @QueryParam("sort") String sort) {
        return IndicatorService.getIndicators(offset, count, orderBy,sort );
    }

    /**
     * Retrieve and provide indicator layer by Id
     * @param indicatorId indicator ID to query for indicator layer
     * @return <pre>
     *
     *  {
     *      "id": 70,
     *      "name": "New layer",
     *      "description": "layer description",
     *      "numberOfClasses": 5,
     *      "unit": "",
     *      "admLevelId": 77,
     *      "population": true,
     *      "indicatorTypeId": 262,
     *      "accessTypeId": 2,
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
     *  </pre>
     */
    @GET
    @Path("/indicator-layer/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getIndicatorById", ui = false)
    public JsonBean getIndicatorById(@PathParam("id") long indicatorId) {

        return IndicatorService.getIndicatorById(indicatorId);
    }

    @GET
    @Path("/indicator-layer/check-name")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "checkName", ui = false)
    public JsonBean checkName(@QueryParam("name") String name) {
        return IndicatorService.checkName(name);
    }
    /**
     * Delete indicator layer by Id
     * @param indicatorId indicator ID to query for indicator layer
     * @return <pre>
     *
     *  {
     *      "result": "DELETED"
     *  }
     * </pre>
     */
    @DELETE
    @Path("/indicator-layer/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "deleteIndicatorById", ui = false)
    public JsonBean deleteIndicatorById(@PathParam("id") long indicatorId) {
        return IndicatorService.deleteIndicatorById(indicatorId);
    }

    /**
     * Create or updated indicator layer
     * @return <pre>
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
     * }
     * </pre>
     */
    @POST
    @Path("/indicator-layer")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "saveIndicator", ui = false)
    public JsonBean saveIndicator(final JsonBean indicator) {
        return IndicatorService.saveIndicator(indicator);
    }

    /**
     * Export indicator layers values
     * @param admLevelId adm Level ID to query for category value
     */
    @GET
    @Path("/indicator-layer/download")
    @Produces("application/vnd.ms-excel")
    public StreamingOutput download(@QueryParam("admLevelId") long admLevelId, @QueryParam("name") String indicatorName) {

        return IndicatorExporter.download(admLevelId,indicatorName);
    }

    /**
     * Import indicator layers values
     * @param option to indicate mode OVERWRITE or NEW
     */
    @POST
    @Path("/indicator-layer/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public JsonBean importIndicator(
            @FormDataParam("option") long saveOption,
            @FormDataParam("name") String name,
            @FormDataParam("file") InputStream uploadedInputStream
    ) {
        return IndicatorImporter.importIndicator(uploadedInputStream);
    }

    /**
     * Retrieve and provide adm levels
     * @return <pre>
     *
     *  [
     *  {
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
     *   },
     *      ....
     *  ]
     * </pre>
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
     * Retrieve and provide adm levels
     * @return <pre>
     *
     *  [
     *      {
     *      "id": 1,
     *      "label": "Country",
     *      "value": "Country"
     *      },
     *      ....
     *  ]
     * </pre>
     */
    @GET
    @Path("/adm-level")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Collection<JsonBean> getLevels() {

        Collection<AmpCategoryValue> admLevels = CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(
                "implementation_location", true);

        Collection<JsonBean> indicatorLayerList = new ArrayList<JsonBean>();
        for (AmpCategoryValue admLevel: admLevels){
            JsonBean categoryValue = new JsonBean();
            categoryValue.set(IndicatorEPConstants.ID, admLevel.getId());
            categoryValue.set(IndicatorEPConstants.LABEL, admLevel.getLabel());
            categoryValue.set(IndicatorEPConstants.VALUE, admLevel.getValue());
            indicatorLayerList.add(categoryValue);
        }

        return indicatorLayerList;
    }

    /**
     * Retrieve and provide access types
     * @return <pre>
     *
     *  [
     *      {
     *      "id": 1,
     *      "value": "Private",
     *      "label": "Private"
     *      },
     *      ....
     *  ]
     * </pre>
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
     * 
     * Provide Indicator Layer Types:
     * <pre>
     * @return 
     * [
     *  { 
     *    "id" : 123,
     *    "orig-name" : "Ration (% of Total Population)", // not translated
     *    "name" : “Ration (% of Total Population)” // translated
     *  }, 
     *  ...
     * ]
     * </pre>
     * 
     */
    @GET
    @Path("/indicator-types")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<JsonBean> getIndicatorLayerTypes() {
        return CategoryValueService.getCategoryValues(CategoryConstants.INDICATOR_LAYER_TYPE_KEY, true);
    }
    
    /**
     * <pre>
     * Configures new list of indicator layers to be designated as population layers
     * {
     *   “layersIds” : [5,10,11 23, ...]
     * }
     * </pre>
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
     * @return a list (e.g. [2, 3, 4, ...]) of all possible indicator layers to be designated as population layers.
     * E.g. at this moment we agreed that only "count" population layers and "non-country" population layers are allowed
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
