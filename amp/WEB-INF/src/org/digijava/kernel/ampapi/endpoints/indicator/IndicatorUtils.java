package org.digijava.kernel.ampapi.endpoints.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.gis.services.GapAnalysis;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.SecurityUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpIndicatorColor;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpIndicatorWorkspace;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ColorRampUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

public class IndicatorUtils {
    protected static final Logger logger = Logger.getLogger(IndicatorUtils.class);

    public static Map<String, String> validFieldList = null;

    static
    {
        validFieldList = new HashMap<String, String>();
        validFieldList.put(IndicatorEPConstants.FIELD_ID, "indicator");
        validFieldList.put(IndicatorEPConstants.FIELD_NAME, "indicator");
        validFieldList.put(IndicatorEPConstants.FIELD_DESCRIPTION, "indicator");
        validFieldList.put(IndicatorEPConstants.FIELD_NUMBER_OF_CLASSES, "indicator");
        validFieldList.put(IndicatorEPConstants.FIELD_UNIT, "indicator");
        validFieldList.put(IndicatorEPConstants.FIELD_ADM_LEVEL_ID, "indicator");
        validFieldList.put(IndicatorEPConstants.FIELD_ACCESS_TYPE_ID, "indicator");
        validFieldList.put(IndicatorEPConstants.FIELD_CREATED_ON, "indicator");
        validFieldList.put(IndicatorEPConstants.FIELD_UPDATED_ON, "indicator");
        validFieldList.put(IndicatorEPConstants.FIELD_CREATED_BY, "c");
    }

    public static String addAlias(String field) {
        String result = field;
        if (validFieldList.containsKey(field)) {
            result = validFieldList.get(field) + "." + field;
        }
            return result;

    }

    public static JsonBean buildIndicatorLayerJson(AmpIndicatorLayer indicator) {
        JsonBean indicatorJson = new JsonBean();

        indicatorJson.set(IndicatorEPConstants.ID, indicator.getId());
        indicatorJson.set(IndicatorEPConstants.NAME, TranslationUtil.getTranslatableFieldValue(IndicatorEPConstants.NAME, indicator.getName(), indicator.getId()));
        indicatorJson.set(IndicatorEPConstants.DESCRIPTION, TranslationUtil.getTranslatableFieldValue(IndicatorEPConstants.DESCRIPTION, indicator.getDescription(), indicator.getId()));
        indicatorJson.set(IndicatorEPConstants.UNIT, TranslationUtil.getTranslatableFieldValue(IndicatorEPConstants.UNIT, indicator.getUnit(), indicator.getId()));
        indicatorJson.set(IndicatorEPConstants.NUMBER_OF_CLASSES, indicator.getNumberOfClasses());
        indicatorJson.set(IndicatorEPConstants.ADM_LEVEL_ID, indicator.getAdmLevel().getId());
        indicatorJson.set(IndicatorEPConstants.IS_POPULATION, indicator.isPopulation());
        indicatorJson.set(IndicatorEPConstants.INDICATOR_TYPE_ID, indicator.getIndicatorType() == null ? null : 
            indicator.getIndicatorType().getId());
        indicatorJson.set(IndicatorEPConstants.ACCESS_TYPE_ID, indicator.getAccessType().getValue());

        indicatorJson.set(IndicatorEPConstants.CREATED_ON, FormatHelper.formatDate(indicator.getCreatedOn()));
        indicatorJson.set(IndicatorEPConstants.UPDATED_ON, FormatHelper.formatDate(indicator.getUpdatedOn()));

        if (indicator.getCreatedBy() != null) {
            indicatorJson.set(IndicatorEPConstants.CREATE_BY, indicator.getCreatedBy().getUser().getEmail());
        }

        if (indicator.getColorRamp() != null) {
            String[] colors = new String[indicator.getColorRamp().size()];
            int i=0;
            for (AmpIndicatorColor indicatorColor: indicator.getColorRamp()){
                if (indicatorColor.getPayload() == IndicatorEPConstants.PAYLOAD_INDEX) {
                    long colorId = ColorRampUtil.getColorId(indicatorColor.getColor());
                    indicatorJson.set(IndicatorEPConstants.COLOR_RAMP_ID, colorId);
                }
                colors[i++] = indicatorColor.getColor();
            }
            indicatorJson.set(IndicatorEPConstants.COLOR_RAMP, colors);
        }

        if (indicator.getSharedWorkspaces() != null) {
            Collection<JsonBean> sharedWorkspaces = new ArrayList<JsonBean>();
            for (AmpIndicatorWorkspace indicatorWS: indicator.getSharedWorkspaces()){
                sharedWorkspaces.add(SecurityUtil.getTeamJsonBean(indicatorWS.getWorkspace()));
            }
            indicatorJson.set(IndicatorEPConstants.SHARED_WORKSPACES, sharedWorkspaces);
        }

        indicatorJson.set(IndicatorEPConstants.NUMBER_OF_IMPORTED_RECORDS, (indicator.getIndicatorValues()!=null ? indicator.getIndicatorValues().size() : 0));

        return indicatorJson;
    }

    public static boolean isAdmin() {
        return "yes".equals(TLSUtils.getRequest().getSession().getAttribute("ampAdmin"));
    }

    public static boolean hasRights(long indicatorId) {
        if (isAdmin()) {
            return true;
        } else {
            TeamMember tm = TeamUtil.getCurrentMember();
            if (tm != null) {
                AmpTeamMember ampTeamMember = TeamUtil.getCurrentAmpTeamMember();
                if (ampTeamMember != null) {
                    AmpIndicatorLayer indicatorLayer =DynLocationManagerUtil.getIndicatorLayerById(indicatorId);
                    if (indicatorLayer.getCreatedBy() != null && indicatorLayer.getCreatedBy().getUser().getId() == ampTeamMember.getUser().getId()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static JsonBean getList(Collection<AmpIndicatorLayer> indicatorLayers, Integer offset, Integer count) {

        offset = Math.min(indicatorLayers.size(), offset == null ? 0 : offset);
        count = (count == null ? IndicatorEPConstants.DEFAULT_COUNT : count);
        int totalPageCount = (indicatorLayers.size() / count) + 1;

        JsonBean result = new JsonBean();
        JsonBean page = new JsonBean();

        Collection<JsonBean> indicatorLayerList = new ArrayList<JsonBean>();
        Collection<AmpIndicatorLayer> col = new ArrayList<>(indicatorLayers).subList(offset, offset + Math.min(indicatorLayers.size() - offset, count));

        for (AmpIndicatorLayer indicator: col){
            JsonBean indicatorJson = IndicatorUtils.buildIndicatorLayerJson(indicator);
            indicatorLayerList.add(indicatorJson);
        }

        page.set(IndicatorEPConstants.RECORDS_PER_PAGE,count);
        page.set(IndicatorEPConstants.CURRENT_PAGE_NUMBER, (offset / count) + 1);
        page.set(IndicatorEPConstants.TOTAL_PAGE_COUNT,totalPageCount);
        page.set(IndicatorEPConstants.TOTAL_RECORDS,indicatorLayers.size());

        result.set(IndicatorEPConstants.PAGE,page);
        result.set(IndicatorEPConstants.DATA,indicatorLayerList);

        return result;
    }

    public static final void validateOrderBy(String orderBy, String sort, ApiEMGroup errors) {

        ApiErrorMessage err = IndicatorUtils.validateField(orderBy);
        if (err != null) errors.addApiErrorMessage(err, "orderBy");

        err = IndicatorUtils.validateSort(sort);
        if (err != null) errors.addApiErrorMessage(err, "sort");
    }

    public static final ApiErrorMessage validateField(String field) {
        return (validFieldList.containsKey(field)? null : new ApiErrorMessage(IndicatorErrors.INVALID_FIELD.id, IndicatorErrors.INVALID_FIELD.description,field));
    }

    public static final ApiErrorMessage validateSort(String sort) {
        return ("desc".equalsIgnoreCase(sort) || "asc".equalsIgnoreCase(sort)) ? null : new ApiErrorMessage(IndicatorErrors.INVALID_SORT.id, IndicatorErrors.INVALID_SORT.description,sort);
    }
    
    /**
     * Get unique Population Layer designated for the given implementation location
     * @param hardcodedCatValue implLoc the implementation location (Region, etc)
     * @return the population layer or null if no unique layer found
     */
    public static AmpIndicatorLayer getPopulationLayer(HardCodedCategoryValue hardcodedCatValue) {
        return getPopulationLayer(hardcodedCatValue == null ? null : hardcodedCatValue.getAmpCategoryValueFromDB());
    }
    
    /**
     * Get unique Population Layer designated for the given implementation location
     * @param implLoc the implementation location (Region, etc)
     * @return the population layer or null if no unique layer found
     */
    public static AmpIndicatorLayer getPopulationLayer(AmpCategoryValue implementationLocation) {
        AmpIndicatorLayer ail = null;
        if (implementationLocation != null && implementationLocation.isVisible()) {
            List<AmpIndicatorLayer> ailList = PersistenceManager.getSession()
                    .createQuery("select o from " + AmpIndicatorLayer.class.getName() + " o "
                    + "where o.population is true and o.admLevel is not null and o.admLevel.id=:admLevelId")
                    .setLong("admLevelId", implementationLocation.getId()).list();
            if (ailList != null && ailList.size() == 1) {
                ail = ailList.iterator().next();
            }
        }
        if (ail == null) {
            logger.error("Could not uniquely locate population layer for admLevel = " + implementationLocation.getValue());
        }
        return ail;
    }
    
    /**
     * 
     * @param indicatorId
     * @param input
     * @param isGapAnalysis
     * @return
     */
    public static JsonBean getIndicatorsAndLocationValues(Long indicatorId, JsonBean input, boolean isGapAnalysis) {
        AmpIndicatorLayer indicator = (AmpIndicatorLayer) DbUtil.getObjectOrNull(AmpIndicatorLayer.class, indicatorId);
        if (indicator == null) {
            return ApiError.toError(new ApiErrorMessage(IndicatorErrors.INVALID_ID, String.valueOf(indicatorId)));
        }
        GapAnalysis gapAnalysis = isGapAnalysis ? new GapAnalysis(indicator, input) : null;
        boolean doingGapAnalysis = gapAnalysis != null && gapAnalysis.isReadyForGapAnalysis();
        if (doingGapAnalysis) {
            logger.info("Generating Gap Analysis");
        } else if (isGapAnalysis) {
            logger.error("Requested gap analysis, but it cannot be done => providing non-gap analysis data");
        }
        
        String unit = indicator.getUnit(); 
        if (doingGapAnalysis) {
            unit = String.format("%s / %s", gapAnalysis.getCurrencyCode(), unit); 
        }
        
        // build general indicator info
        JsonBean response = new JsonBean();
        response.set(EPConstants.NAME, indicator.getName());
        response.set("classes", indicator.getNumberOfClasses());
        response.set("id", indicator.getId());
        response.set("unit", unit);
        response.set("description", indicator.getDescription());
        response.set("admLevelId", indicator.getAdmLevel().getLabel());
        
        // build locations values
        List<JsonBean> values = new ArrayList<>();
        for (AmpLocationIndicatorValue locIndValue : indicator.getIndicatorValues()) {
            JsonBean object = new JsonBean();
            String geoCode = locIndValue.getLocation().getGeoCode();
            BigDecimal value = BigDecimal.valueOf(locIndValue.getValue());
            if (doingGapAnalysis) {
                value = gapAnalysis.getGapAnalysisAmount(value, geoCode);
            }
            object.set("value", value);
            object.set("geoId", geoCode);
            object.set("name", locIndValue.getLocation().getName());
            
            values.add(object);
        }
        response.set("values", values);
        return response;
    }
}