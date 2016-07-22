package org.digijava.kernel.ampapi.endpoints.indicator;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;


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
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(IndicatorErrors.UNAUTHORIZED);
        }

        AmpIndicatorLayer indicatorLayer = DynLocationManagerUtil.getIndicatorLayerById(id);
        JsonBean indicatorJson = null;
        if (indicatorLayer!=null) {
            indicatorJson = IndicatorUtils.buildIndicatorLayerJson(indicatorLayer);
        } else {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(IndicatorErrors.INVALID_ID);
        }
        return indicatorJson;
    }

    public static JsonBean getIndicatorByName(String name) {

        AmpIndicatorLayer indicatorLayer = DynLocationManagerUtil.getIndicatorLayerByName(name);
        JsonBean result = new JsonBean();
        if (indicatorLayer!=null) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(IndicatorErrors.EXISTING_NAME);
        } else {
            result.set(IndicatorEPConstants.RESULT, IndicatorEPConstants.AVAIBLE);
            return result;
        }

    }

    public static JsonBean deleteIndicatorById(long id) {
        JsonBean result = new JsonBean();

        if (!IndicatorUtils.hasRights(id)) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(IndicatorErrors.UNAUTHORIZED);
        }

        AmpIndicatorLayer indicatorLayer = DynLocationManagerUtil.getIndicatorLayerById(id);
        if (indicatorLayer==null) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(IndicatorErrors.INVALID_ID);
        }
        else {
            DbUtil.delete(indicatorLayer);
        }

        result.set(IndicatorEPConstants.RESULT,IndicatorEPConstants.DELETED);
        return result;
    }

    public static JsonBean saveIndicator(JsonBean indicator) {
        JsonBean result = new JsonBean();
        TranslationUtil indicatorTranslation = new TranslationUtil();
        AmpIndicatorLayer indLayer = IndicatorUtils.setIndicatorLayer(indicator, indicatorTranslation);

        if (indLayer.getId() != null && !IndicatorUtils.hasRights(indLayer.getId())) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(IndicatorErrors.UNAUTHORIZED);
        }

        try {
            if (indLayer.getId()==null) {
                result.set(IndicatorEPConstants.RESULT, IndicatorEPConstants.INSERTED);
            } else {
                result.set(IndicatorEPConstants.RESULT, IndicatorEPConstants.SAVED);
            }

            Session sess = PersistenceManager.getSession();
            sess.saveOrUpdate(indLayer);
            sess.flush();

            indicatorTranslation.serialize(indLayer, IndicatorEPConstants.NAME, indicatorTranslation.getTranslations());
            indicatorTranslation.serialize(indLayer, IndicatorEPConstants.DESCRIPTION, indicatorTranslation.getTranslations());
            indicatorTranslation.serialize(indLayer, IndicatorEPConstants.UNIT, indicatorTranslation.getTranslations());

            JsonBean indicatorJson = IndicatorUtils.buildIndicatorLayerJson(indLayer);

            result.set(IndicatorEPConstants.DATA, indicatorJson);
        } catch (Exception e) {
            logger.error("saveIndicator: ", e);
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(IndicatorErrors.UNKNOWN_ERROR);
        }

        return result;
    }
}
