package org.digijava.kernel.ampapi.endpoints.indicator;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.TeamUtil;

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
    public static final String DEFAULT_INDICATOR_ORDER_FIELD = "created_on";

    public static JsonBean getIndicators(Integer offset, Integer count, String orderBy, String sort ) {
        orderBy = (orderBy!=null ? orderBy : DEFAULT_INDICATOR_ORDER_FIELD);
        sort = (sort!=null ? sort : "");

        Collection<AmpIndicatorLayer> indicatorLayers = null;
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        if (tm == null) {
            indicatorLayers = DynLocationManagerUtil.getIndicatorLayerByAccessType(IndicatorEPConstants.ACCESS_TYPE_PUBLIC, orderBy, sort);
        } else if (IndicatorUtils.isAdmin()) {
            indicatorLayers = DynLocationManagerUtil.getIndicatorLayers(orderBy, sort);
        }
        else {
            AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
            indicatorLayers = DynLocationManagerUtil.getIndicatorLayerByCreatedBy(ampTeamMember, orderBy, sort);
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
        AmpIndicatorLayer indLayer = IndicatorUtils.setIndicatorLayer(indicator);

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
            DbUtil.saveOrUpdateObject(indLayer);

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
