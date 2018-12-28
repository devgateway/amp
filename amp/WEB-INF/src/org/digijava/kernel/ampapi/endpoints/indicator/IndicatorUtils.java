package org.digijava.kernel.ampapi.endpoints.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.exception.AmpWebApplicationException;
import org.digijava.kernel.ampapi.endpoints.gis.services.AdmLevel;
import org.digijava.kernel.ampapi.endpoints.gis.services.GapAnalysis;
import org.digijava.kernel.ampapi.endpoints.gis.PerformanceFilterParameters;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
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
        indicatorJson.set(IndicatorEPConstants.ADM_LEVEL_NAME, indicator.getAdmLevel().getLabel());
        indicatorJson.set(IndicatorEPConstants.ADMIN_LEVEL, IndicatorEPConstants.ADM_PREFIX + indicator.getAdmLevel().getIndex());
        indicatorJson.set(IndicatorEPConstants.IS_POPULATION, indicator.isPopulation());
        indicatorJson.set(IndicatorEPConstants.INDICATOR_TYPE_ID, indicator.getIndicatorType() == null ? null : 
            indicator.getIndicatorType().getId());
        indicatorJson.set(IndicatorEPConstants.ACCESS_TYPE_ID, indicator.getAccessType().getValue());
        indicatorJson.set(IndicatorEPConstants.FIELD_ZERO_CATEGORY_ENABLED, indicator.getZeroCategoryEnabled());
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


    public static Indicator buildSerializedIndicatorLayerJson(AmpIndicatorLayer indicator,
            SaveIndicatorRequest indicatorJson) {

        indicatorJson.setId((long) System.identityHashCode(indicator));
        indicatorJson.setAdmLevelId(indicator.getAdmLevel().getId());
        indicatorJson.setAdmLevelName(indicator.getAdmLevel().getValue());
        String admLevelLabel = IndicatorEPConstants.ADM_PREFIX + indicator.getAdmLevel().getIndex();
        indicatorJson.setAdminLevel(AdmLevel.fromString(admLevelLabel));
        indicatorJson.setCreatedOn(indicator.getCreatedOn());

        if (indicator.getColorRamp() != null) {
            indicatorJson.setColorRamp(new ArrayList<>(indicator.getColorRamp()));
        }

        if (indicator.getIndicatorValues() != null) {
            List<IndicatorValue> indicatorValues = new ArrayList<>();
            for (AmpLocationIndicatorValue indicatorValue: indicator.getIndicatorValues()){
                indicatorValues.add(getLocationIndicatorValueBean(indicatorValue));
            }
            indicatorJson.setValues(indicatorValues);
        }

        indicatorJson.setNumberOfImportedRecords(
                indicator.getIndicatorValues() != null ? indicator.getIndicatorValues().size() : 0);

        return indicatorJson;
    }

    private static IndicatorValue getLocationIndicatorValueBean(AmpLocationIndicatorValue indicatorValue) {
        return new IndicatorValue(
                indicatorValue.getLocation().getId(),
                new BigDecimal(indicatorValue.getValue()),
                indicatorValue.getLocation().getGeoCode(),
                indicatorValue.getLocation().getName());
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

        offset = Math.min(indicatorLayers.size(), (offset == null || offset >= indicatorLayers.size()) ? 0 : offset);
        count = (count == null ? IndicatorEPConstants.DEFAULT_COUNT : count);
        int totalPageCount = (indicatorLayers.size() % count == 0 ? (indicatorLayers.size() / count) : (indicatorLayers.size() / count) + 1);

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
     * @param implementationLocation the implementation location (Region, etc)
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
    
    public static Indicator getIndicatorsAndLocationValues(Long indicatorId) {
        AmpIndicatorLayer indicator = getAmpIndicatorLayer(indicatorId);
        return getIndicatorsAndLocationValues(indicator);
    }

    public static Indicator doGapAnalysis(Long indicatorId, PerformanceFilterParameters input) {
        AmpIndicatorLayer indicator = getAmpIndicatorLayer(indicatorId);
        return doGapAnalysis(indicator, input);
    }

    private static AmpIndicatorLayer getAmpIndicatorLayer(Long indicatorId) {
        AmpIndicatorLayer indicator = DbUtil.getObjectOrNull(AmpIndicatorLayer.class, indicatorId);
        if (indicator == null) {
            JsonBean error = ApiError.toError(IndicatorErrors.INVALID_ID.withDetails(String.valueOf(indicatorId)));
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST, error);
        }
        return indicator;
    }

    private static Indicator getIndicatorsAndLocationValues(AmpIndicatorLayer indicator) {
        String unit = indicator.getUnit();

        // build general indicator info
        Indicator response = new Indicator();
        response.setName(TranslationUtil.getTranslatableFieldValue(
                IndicatorEPConstants.NAME, indicator.getName(), indicator.getId()));
        response.setNumberOfClasses(indicator.getNumberOfClasses());
        response.setId(indicator.getId());
        response.setUnit(TranslationUtil.getTranslatableFieldValue(
                IndicatorEPConstants.UNIT, unit, indicator.getId()));
        response.setDescription(TranslationUtil.getTranslatableFieldValue(
                IndicatorEPConstants.DESCRIPTION, indicator.getDescription(), indicator.getId()));
        response.setAdmLevelId(indicator.getAdmLevel().getId());
        response.setAdmLevelName(indicator.getAdmLevel().getLabel());
        response.setGapAnalysis(false);
        response.setIndicatorTypeId(indicator.getIndicatorType() == null ? null : indicator.getIndicatorType().getId());
        response.setZeroCategoryEnabled(indicator.getZeroCategoryEnabled());

        // build locations values
        List<IndicatorValue> values = new ArrayList<>();
        for (AmpLocationIndicatorValue locIndValue : indicator.getIndicatorValues()) {
            String geoCode = locIndValue.getLocation().getGeoCode();
            BigDecimal value = BigDecimal.valueOf(locIndValue.getValue());

            values.add(new IndicatorValue(
                    locIndValue.getLocation().getId(),
                    value,
                    geoCode,
                    locIndValue.getLocation().getName()));
        }
        response.setValues(values);
        return response;
    }

    public static Indicator doGapAnalysis(AmpIndicatorLayer indicator, PerformanceFilterParameters input) {

        GapAnalysis gapAnalysis = new GapAnalysis(indicator, input);
        boolean doingGapAnalysis = gapAnalysis.isReadyForGapAnalysis();

        Indicator response = getIndicatorsAndLocationValues(indicator);

        if (doingGapAnalysis) {
            logger.info("Generating Gap Analysis");

            String unit = String.format("%s / %s", gapAnalysis.getCurrencyCode(), response.getUnit());
            response.setUnit(
                    TranslationUtil.getTranslatableFieldValue(IndicatorEPConstants.UNIT, unit, response.getId()));

            response.setGapAnalysis(true);

            for (IndicatorValue value : response.getValues()) {
                value.setValue(gapAnalysis.getGapAnalysisAmount(value.getValue(), value.getGeoId()));
            }
        } else {
            logger.error("Requested gap analysis, but it cannot be done => providing non-gap analysis data");
        }

        return response;
    }
    
    /**
     * Find the corresponding adm-0, adm-1, etc for the selected implementation location
     * @param indicator the indicator
     * @return the corresponding adm-x 
     */
    public static String getAdmX(AmpIndicatorLayer indicator) {
        String implementationLocation = indicator.getAdmLevel() == null ? null : indicator.getAdmLevel().getValue();
        return GisConstants.IMPL_CATEGORY_VALUE_TO_ADM.get(implementationLocation);
    }
}
