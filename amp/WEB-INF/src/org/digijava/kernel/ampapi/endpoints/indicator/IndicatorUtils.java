package org.digijava.kernel.ampapi.endpoints.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.exception.AmpWebApplicationException;
import org.digijava.kernel.ampapi.endpoints.gis.services.AdmLevel;
import org.digijava.kernel.ampapi.endpoints.gis.services.GapAnalysis;
import org.digijava.kernel.ampapi.endpoints.gis.PerformanceFilterParameters;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpIndicatorColor;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.dbentity.AmpTeamMember;
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

    public static Indicator buildJsonIndicatorFromIndicatorLayer(AmpIndicatorLayer indLayer, boolean includeAdmLevel) {
        Indicator apiIndicator = new Indicator();

        apiIndicator.setId(indLayer.getId());
        apiIndicator.setName(TranslationUtil.getTranslatableFieldValue(
                IndicatorEPConstants.NAME, indLayer.getName(), indLayer.getId()));
        apiIndicator.setDescription(TranslationUtil.getTranslatableFieldValue(
                IndicatorEPConstants.DESCRIPTION, indLayer.getDescription(), indLayer.getId()));
        apiIndicator.setUnit(TranslationUtil.getTranslatableFieldValue(
                IndicatorEPConstants.UNIT, indLayer.getUnit(), indLayer.getId()));
        apiIndicator.setNumberOfClasses(indLayer.getNumberOfClasses());

        if (includeAdmLevel) {
            apiIndicator.setAdmLevelId(indLayer.getAdmLevel().getId());
            apiIndicator.setAdmLevelName(indLayer.getAdmLevel().getLabel());
            String admLevelLabel = IndicatorEPConstants.ADM_PREFIX + indLayer.getAdmLevel().getIndex();
            apiIndicator.setAdminLevel(AdmLevel.fromString(admLevelLabel));
        }

        apiIndicator.setIndicatorTypeId(indLayer.getIndicatorType() == null ? null
                : indLayer.getIndicatorType().getId());
        apiIndicator.setAccessTypeId(indLayer.getAccessType().getValue());
        apiIndicator.setZeroCategoryEnabled(indLayer.getZeroCategoryEnabled());
        apiIndicator.setCreatedOn(indLayer.getCreatedOn());
        apiIndicator.setUpdatedOn(indLayer.getUpdatedOn());

        if (indLayer.getCreatedBy() != null) {
            apiIndicator.setCreatedBy(indLayer.getCreatedBy().getUser().getEmail());
        }

        List<AmpIndicatorColor> colorList = new ArrayList<>(indLayer.getColorRamp());
        colorList.sort(Comparator.comparing(AmpIndicatorColor::getPayload));
        for (AmpIndicatorColor color : colorList) {
            if (color.getPayload() == IndicatorEPConstants.PAYLOAD_INDEX) {
                long colorId = ColorRampUtil.getColorId(color.getColor());
                apiIndicator.setMultiColor(IndicatorEPConstants.MULTI_COLOR_PALETTES.contains(colorId));
                apiIndicator.setColorRampId(colorId);
            }
        }
        apiIndicator.setColorRamp(colorList);

        return apiIndicator;
    }

    public static Indicator buildIndicatorLayerJson(AmpIndicatorLayer indLayer) {
        Indicator apiIndicator = buildJsonIndicatorFromIndicatorLayer(indLayer, true);
        apiIndicator.setPopulation(indLayer.isPopulation());

        if (indLayer.getSharedWorkspaces() != null) {
            List<Long> sharedWorkspaces = indLayer.getSharedWorkspaces().stream()
                    .map(ind -> ind.getWorkspace().getAmpTeamId())
                    .collect(Collectors.toList());

            apiIndicator.setSharedWorkspaces(sharedWorkspaces);
        }

        int importedRecords = (indLayer.getIndicatorValues() != null ? indLayer.getIndicatorValues().size() : 0);
        apiIndicator.setNumberOfImportedRecords(importedRecords);

        return apiIndicator;
    }

    public static List<Indicator> getApiIndicatorsForGis(List<AmpIndicatorLayer> indicators,
                                                                   boolean includeAdmLevel) {
        List<Indicator> apiIndicators = new ArrayList<>();
        GapAnalysis gapAnalysis = new GapAnalysis();

        for (AmpIndicatorLayer indicator : indicators) {
            Indicator apiIndicator = buildJsonIndicatorFromIndicatorLayer(indicator, includeAdmLevel);

            apiIndicator.setCanDoGapAnalysis(gapAnalysis.canDoGapAnalysis(indicator));

            apiIndicators.add(apiIndicator);
        }

        return apiIndicators;
    }


    public static Indicator buildSerializedIndicatorLayerJson(AmpIndicatorLayer indicator, Indicator indicatorJson) {

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

    public static boolean isAdmin() {
        return "yes".equals(TLSUtils.getRequest().getSession().getAttribute("ampAdmin"));
    }

    private static IndicatorValue getLocationIndicatorValueBean(AmpLocationIndicatorValue indicatorValue) {
        return new IndicatorValue(
                indicatorValue.getLocation().getId(),
                new BigDecimal(indicatorValue.getValue()),
                indicatorValue.getLocation().getGeoCode(),
                indicatorValue.getLocation().getName());
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

    public static IndicatorPageDataResult getList(Collection<AmpIndicatorLayer> indicatorLayers,
                                                  Integer offset, Integer count) {

        offset = Math.min(indicatorLayers.size(), (offset == null || offset >= indicatorLayers.size()) ? 0 : offset);
        count = (count == null ? IndicatorEPConstants.DEFAULT_COUNT : count);
        int totalPageCount = (indicatorLayers.size() % count == 0 ? (indicatorLayers.size() / count) : (indicatorLayers.size() / count) + 1);

        Collection<AmpIndicatorLayer> col = new ArrayList<>(indicatorLayers)
                .subList(offset, offset + Math.min(indicatorLayers.size() - offset, count));


        List<Indicator> indicators = col.stream()
                .map(ind -> IndicatorUtils.buildIndicatorLayerJson(ind))
                .collect(Collectors.toList());

        PageInformation page = new PageInformation();
        page.setRecordsPerPage(count);
        page.setCurrentPageNumber((offset / count) + 1);
        page.setTotalPageCount(totalPageCount);
        page.setTotalRecords(indicatorLayers.size());

        return new IndicatorPageDataResult(page, indicators);
    }

    public static final void validateOrderBy(String orderBy, String sort, ApiEMGroup errors) {

        ApiErrorMessage err = IndicatorUtils.validateField(orderBy);
        if (err != null) errors.addApiErrorMessage(err, "orderBy");

        err = IndicatorUtils.validateSort(sort);
        if (err != null) errors.addApiErrorMessage(err, "sort");
    }

    public static final ApiErrorMessage validateField(String field) {
        return (validFieldList.containsKey(field) ? null : new ApiErrorMessage(ValidationErrors.FIELD_INVALID.id,
                ValidationErrors.FIELD_INVALID.description, field));
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
            ApiErrorResponse error = ApiError.toError(
                    ValidationErrors.INVALID_ID.withDetails(String.valueOf(indicatorId)));
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST, error);
        }
        return indicator;
    }

    private static Indicator getIndicatorsAndLocationValues(AmpIndicatorLayer indicator) {
        // build general indicator info
        Indicator apiIndicator = buildJsonIndicatorFromIndicatorLayer(indicator, false);
        apiIndicator.setGapAnalysis(false);

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
        apiIndicator.setValues(values);

        return apiIndicator;
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
