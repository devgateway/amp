package org.digijava.kernel.ampapi.endpoints.indicator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.menu.AmpView;
import org.dgfoundation.amp.menu.MenuUtils;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.hibernate.Session;


/**
 * Indicator Endpoint utility methods
 * 
 * @author apicca
 */
public class IndicatorService {

    protected static final Logger logger = Logger.getLogger(IndicatorService.class);

    public static JsonBean getIndicators(Integer offset, Integer count, String orderBy, String sort ) {
        ApiEMGroup errors = new ApiEMGroup();

        orderBy = ((orderBy==null || "".equals(orderBy)) ? IndicatorEPConstants.DEFAULT_INDICATOR_ORDER_FIELD : orderBy);

        sort = (sort!=null ? sort : "asc");

        IndicatorUtils.validateOrderBy(orderBy, sort, errors);
        if (errors.size() > 0) {
            return ApiError.toError(errors.getAllErrors());
        }
        orderBy = IndicatorUtils.addAlias(orderBy);
        Collection<AmpIndicatorLayer> indicatorLayers = null;
        if (IndicatorUtils.isAdmin()) {
            indicatorLayers = DynLocationManagerUtil.getIndicatorLayers(orderBy, sort);
        } else if (TeamUtil.getCurrentMember() == null) {
            indicatorLayers = DynLocationManagerUtil.getIndicatorLayerByAccessType(IndicatorEPConstants.ACCESS_TYPE_PUBLIC, orderBy, sort);
        }
        else {
            indicatorLayers = DynLocationManagerUtil.getIndicatorLayerByCreatedBy(TeamUtil.getCurrentAmpTeamMember(), orderBy, sort);
        }

        if (indicatorLayers==null)
            indicatorLayers = new ArrayList<AmpIndicatorLayer>();

        return IndicatorUtils.getList(indicatorLayers, offset, count);
    }

    public static JsonBean getIndicatorById(long id) {

        if (!IndicatorUtils.hasRights(id)) {
            ApiErrorResponse.reportForbiddenAccess(IndicatorErrors.UNAUTHORIZED);
        }

        AmpIndicatorLayer indicatorLayer = DynLocationManagerUtil.getIndicatorLayerById(id);
        JsonBean indicatorJson = null;
        if (indicatorLayer!=null) {
            indicatorJson = IndicatorUtils.buildIndicatorLayerJson(indicatorLayer);
        } else {
            return ApiError.toError(IndicatorErrors.INVALID_ID);
        }
        return indicatorJson;
    }

    public static JsonBean checkName(String name) {

        AmpIndicatorLayer indicatorLayer = DynLocationManagerUtil.getIndicatorLayerByName(name);
        JsonBean result = new JsonBean();
        if (indicatorLayer!=null) {
            result.set(IndicatorEPConstants.RESULT, true);
            return result;
        } else {
            result.set(IndicatorEPConstants.RESULT, false);
            return result;
        }

    }

    public static JsonBean deleteIndicatorById(long id) {
        JsonBean result = new JsonBean();

        if (!IndicatorUtils.hasRights(id)) {
            ApiErrorResponse.reportForbiddenAccess(IndicatorErrors.UNAUTHORIZED);
        }

        AmpIndicatorLayer indicatorLayer = DynLocationManagerUtil.getIndicatorLayerById(id);
        if (indicatorLayer == null) {
            return ApiError.toError(IndicatorErrors.INVALID_ID);
        } else {
            DbUtil.delete(indicatorLayer);
        }

        result.set(IndicatorEPConstants.RESULT,IndicatorEPConstants.DELETED);
        return result;
    }

    public static JsonBean saveIndicator(JsonBean indicator) {
        JsonBean result = new JsonBean();
        ApiEMGroup errors = new ApiEMGroup();
        List<AmpContentTranslation> translations = new ArrayList<>();
        
        AmpIndicatorLayer indLayer = getIndicatorLayer(indicator, errors, translations);
        if (!errors.isEmpty()) {
            return ApiError.toError(errors);
        }
                
        try {
            if (indLayer.getId() == null) {
                result.set(IndicatorEPConstants.RESULT, IndicatorEPConstants.INSERTED);
            } else {
                result.set(IndicatorEPConstants.RESULT, IndicatorEPConstants.SAVED);
            }

            if (indLayer.getAccessType() != IndicatorAccessType.TEMPORARY) {
                Session sess = PersistenceManager.getSession();
                sess.saveOrUpdate(indLayer);

                TranslationUtil.serialize(indLayer, IndicatorEPConstants.NAME, translations);
                TranslationUtil.serialize(indLayer, IndicatorEPConstants.DESCRIPTION, translations);
                TranslationUtil.serialize(indLayer, IndicatorEPConstants.UNIT, translations);

                result.set(IndicatorEPConstants.DATA, IndicatorUtils.buildIndicatorLayerJson(indLayer));
            } else {
                result.set(IndicatorEPConstants.DATA, IndicatorUtils.buildSerializedIndicatorLayerJson(indLayer, indicator));
            }
        } catch (Exception e) {
            logger.error("saveIndicator: ", e);
            throw new RuntimeException(e);
        }

        return result;
    }
    
    public static AmpIndicatorLayer getIndicatorLayer(JsonBean indicator, ApiEMGroup errors, 
            List<AmpContentTranslation> translations) {
        IndicatorUpdater updater = new IndicatorUpdater(indicator);
        Long indicatorId = updater.getIndicatorId();
        
        if (indicatorId != null && !IndicatorUtils.hasRights(indicatorId)) {
            ApiErrorResponse.reportForbiddenAccess(IndicatorErrors.UNAUTHORIZED);
        }
        
        AmpIndicatorLayer indLayer = updater.getIndicatorLayer();
                
        if (!updater.getApiErrors().isEmpty()) {
            errors.add(updater.getApiErrors());
        } else {
            AmpIndicatorLayer existingIndicator = DynLocationManagerUtil.getIndicatorLayerByName(indLayer.getName());
            if (existingIndicator != null && existingIndicator.getId() != indLayer.getId()) {
                errors.addApiErrorMessage(IndicatorErrors.EXISTING_NAME, indLayer.getName());
            }
            boolean isAuthenticated = MenuUtils.getCurrentView() != AmpView.PUBLIC;
            if (isAuthenticated && indLayer.getAccessType() == IndicatorAccessType.TEMPORARY) {
                errors.addApiErrorMessage(IndicatorErrors.FIELD_INVALID_VALUE, 
                        IndicatorEPConstants.ACCESS_TYPE_ID + " = " + indicator.get(IndicatorEPConstants.ACCESS_TYPE_ID));
            }
            if (translations != null) {
                translations.addAll(updater.getContentTranslator().getTranslations());
            }
        }
        return indLayer;
    }
    
}
