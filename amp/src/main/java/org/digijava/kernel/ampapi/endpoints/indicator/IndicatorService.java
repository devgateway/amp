package org.digijava.kernel.ampapi.endpoints.indicator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.menu.AmpView;
import org.dgfoundation.amp.menu.MenuUtils;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.errors.GenericErrors;
import org.digijava.kernel.ampapi.endpoints.exception.AmpWebApplicationException;
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

    public static IndicatorPageDataResult getIndicators(Integer offset, Integer count, String orderBy, String sort) {
        ApiEMGroup errors = new ApiEMGroup();

        orderBy = ((orderBy==null || "".equals(orderBy)) ? IndicatorEPConstants.DEFAULT_INDICATOR_ORDER_FIELD : orderBy);

        sort = (sort!=null ? sort : "asc");

        IndicatorUtils.validateOrderBy(orderBy, sort, errors);
        if (errors.size() > 0) {
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST, ApiError.toError(errors.getAllErrors()));
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

    public static Indicator getIndicatorById(long id) {

        if (!IndicatorUtils.hasRights(id)) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        AmpIndicatorLayer indicatorLayer = DynLocationManagerUtil.getIndicatorLayerById(id);
        if (indicatorLayer!=null) {
            return IndicatorUtils.buildIndicatorLayerJson(indicatorLayer);
        }
    
        throw new AmpWebApplicationException(Response.Status.BAD_REQUEST,
                ApiError.toError(ValidationErrors.INVALID_ID));
    }

    public static CheckNameResult checkName(String name) {
        return new CheckNameResult(DynLocationManagerUtil.getIndicatorLayerByName(name) != null);

    }

    public static IndicatorOperationResult deleteIndicatorById(long id) {
        if (!IndicatorUtils.hasRights(id)) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        AmpIndicatorLayer indicatorLayer = DynLocationManagerUtil.getIndicatorLayerById(id);
        if (indicatorLayer != null) {
            DbUtil.delete(indicatorLayer);
        } else {
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST,
                    ApiError.toError(ValidationErrors.INVALID_ID));
        }

        return new IndicatorOperationResult(IndicatorOperationResult.ResultOptions.DELETED);
    }

    public static IndicatorOperationDataResult saveIndicator(Indicator indicatorRequest) {
    
        ApiEMGroup errors = new ApiEMGroup();
        List<AmpContentTranslation> translations = new ArrayList<>();
        AmpIndicatorLayer indLayer = getIndicatorLayer(indicatorRequest, errors, translations);
    
        if (!errors.isEmpty()) {
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST, errors);
        }
    
        IndicatorOperationDataResult result = new IndicatorOperationDataResult();
        try {
            if (indLayer.getId() == null) {
                result.setResult(IndicatorOperationResult.ResultOptions.INSERTED);
            } else {
                result.setResult(IndicatorOperationResult.ResultOptions.SAVED);
            }
    
            if (indLayer.getAccessType() != IndicatorAccessType.TEMPORARY) {
                Session sess = PersistenceManager.getSession();
                sess.saveOrUpdate(indLayer);
    
                TranslationUtil.serialize(indLayer, IndicatorEPConstants.NAME, translations);
                TranslationUtil.serialize(indLayer, IndicatorEPConstants.DESCRIPTION, translations);
                TranslationUtil.serialize(indLayer, IndicatorEPConstants.UNIT, translations);
    
                result.setData(IndicatorUtils.buildIndicatorLayerJson(indLayer));
            } else {
                result.setData(IndicatorUtils.buildSerializedIndicatorLayerJson(indLayer, indicatorRequest));
            }
        } catch (Exception e) {
            logger.error("saveIndicator: ", e);
            throw new RuntimeException(e);
        }

        return result;
    }
    
    public static AmpIndicatorLayer getIndicatorLayer(Indicator indicator, ApiEMGroup errors,
                                                      List<AmpContentTranslation> translations) {
        return getIndicatorLayer(indicator, errors, translations, true);
    }
    
    public static AmpIndicatorLayer getIndicatorLayer(Indicator indicator, ApiEMGroup errors,
            List<AmpContentTranslation> translations, boolean saveMode) {
        IndicatorUpdater updater = new IndicatorUpdater(indicator);
        Long indicatorId = updater.getIndicatorId();
        
        if (indicatorId != null && !IndicatorUtils.hasRights(indicatorId)) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }
        
        AmpIndicatorLayer indLayer = updater.getIndicatorLayer();
                
        if (!updater.getApiErrors().isEmpty()) {
            errors.add(updater.getApiErrors());
        } else {
            AmpIndicatorLayer existingIndicator = DynLocationManagerUtil.getIndicatorLayerByName(indLayer.getName());
            if (existingIndicator != null && !existingIndicator.getId().equals(indLayer.getId())) {
                errors.addApiErrorMessage(IndicatorErrors.EXISTING_NAME, indLayer.getName());
            }
            boolean isAuthenticated = MenuUtils.getCurrentView() != AmpView.PUBLIC;
            if (isAuthenticated && indLayer.getAccessType() == IndicatorAccessType.TEMPORARY && saveMode) {
                errors.addApiErrorMessage(ValidationErrors.FIELD_INVALID_VALUE,
                        IndicatorEPConstants.ACCESS_TYPE_ID + " = " + indicator.getAccessTypeId());
            }
            if (translations != null) {
                translations.addAll(updater.getContentTranslator().getTranslations());
            }
        }
        return indLayer;
    }
    
}
