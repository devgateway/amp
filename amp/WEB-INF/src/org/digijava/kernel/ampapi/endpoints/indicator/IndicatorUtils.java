package org.digijava.kernel.ampapi.endpoints.indicator;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.SecurityUtil;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpIndicatorColor;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpIndicatorWorkspace;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ColorRampUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        validFieldList.put(IndicatorEPConstants.FIELD_CREATED_BY, "indicator.createdBy.user");
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

    public static AmpIndicatorLayer setIndicatorLayer(JsonBean indicator, TranslationUtil indicatorTranslation) {
        AmpIndicatorLayer indicatorLayer = null;

        if (indicator.get("id")!=null) {
            indicatorLayer = DynLocationManagerUtil.getIndicatorLayerById(Long.valueOf(indicator.getString("id")));
        } else{
            indicatorLayer = new AmpIndicatorLayer();
            indicatorLayer.setId(null);
        }

        indicatorLayer.setName(indicatorTranslation.extractTranslationsOrSimpleValue(getField( indicatorLayer, IndicatorEPConstants.NAME), indicatorLayer, indicator.get(IndicatorEPConstants.NAME)));
        indicatorLayer.setDescription(indicatorTranslation.extractTranslationsOrSimpleValue(getField( indicatorLayer, IndicatorEPConstants.DESCRIPTION), indicatorLayer, indicator.get(IndicatorEPConstants.DESCRIPTION)));
        indicatorLayer.setUnit(indicatorTranslation.extractTranslationsOrSimpleValue(getField( indicatorLayer, IndicatorEPConstants.UNIT), indicatorLayer, indicator.get(IndicatorEPConstants.UNIT)));
        indicatorLayer.setNumberOfClasses(Long.valueOf(indicator.getString(IndicatorEPConstants.NUMBER_OF_CLASSES)));
        if (indicatorLayer.getId() == null) {
            indicatorLayer.setCreatedOn(new Date());
            indicatorLayer.setCreatedBy(TeamUtil.getCurrentAmpTeamMember());
        }
        indicatorLayer.setUpdatedOn(new Date());
        indicatorLayer.setAccessType( (indicator.getString(IndicatorEPConstants.ACCESS_TYPE_ID)!=null ? IndicatorAccessType.getValueFromLong(Long.valueOf(indicator.getString(IndicatorEPConstants.ACCESS_TYPE_ID))) :IndicatorAccessType.NO_TYPE));
        indicatorLayer.setAdmLevel(CategoryManagerUtil.getAmpCategoryValueFromDb(Long.valueOf(indicator.getString(IndicatorEPConstants.ADM_LEVEL_ID))));

        if (indicator.get(IndicatorEPConstants.COLOR_RAMP_ID)!=null) {
            Set<AmpIndicatorColor> colorRamp = new HashSet<AmpIndicatorColor>();
            String[] colorRampColors = ColorRampUtil.getColorRamp(EndpointUtils.getSingleValue(indicator, IndicatorEPConstants.COLOR_RAMP_ID,null),
                    indicatorLayer.getNumberOfClasses());
            for (int i = 0; i < colorRampColors.length; i++) {
                AmpIndicatorColor color = new AmpIndicatorColor();
                long payload = i + 1;
                color.setPayload(payload);
                color.setColor(colorRampColors[i]);
                colorRamp.add(color);
            }
            indicatorLayer.setColorRamp(colorRamp);
        }

        if (indicator.get(IndicatorEPConstants.SHARED_WORKSPACES)!=null) {
            Set<AmpIndicatorWorkspace> teams = new HashSet<AmpIndicatorWorkspace>();
            List<String> indicatorTeams =  EndpointUtils.getSingleValue(indicator, IndicatorEPConstants.SHARED_WORKSPACES, Collections.emptyList());
            for (int i = 0; i < indicatorTeams.size(); i++) {
                AmpTeam team = TeamUtil.getAmpTeam(new Long(String.valueOf(indicatorTeams.get(i))));
                AmpIndicatorWorkspace indicatorWs = new AmpIndicatorWorkspace();
                indicatorWs.setWorkspace(team);
                teams.add(indicatorWs);
            }
            indicatorLayer.setSharedWorkspaces(teams);
        }

        return indicatorLayer;
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
                    if (indicatorLayer.getCreatedBy() != null && indicatorLayer.getCreatedBy().getAmpTeamMemId()==ampTeamMember.getAmpTeamMemId()){
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

    protected static Field getField(Object parent, String actualFieldName) {
        if (parent == null) {
            return null;
        }
        Field field = null;
        try {
            Class<?> clazz = parent.getClass();
            while (field == null && !clazz.equals(Object.class)) {
                try {
                    field = clazz.getDeclaredField(actualFieldName);
                    field.setAccessible(true);
                } catch (NoSuchFieldException ex) {
                    clazz = clazz.getSuperclass();
                }
            }
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        return field;
    }
}