package org.digijava.kernel.ampapi.endpoints.indicator;

import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicatorColor;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpIndicatorWorkspace;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.ColorRampUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Creates a new indicator or updates an existing one
 * 
 * @author Nadejda Mandrescu
 */
public class IndicatorUpdater {
    
    private Indicator indicator;
    
    private ApiEMGroup errors = new ApiEMGroup(); 
    private TranslationUtil contentTranslator = new TranslationUtil();
    private Long indicatorId;
    private boolean indicatorIdDetected;
    
    public IndicatorUpdater(Indicator indicator) {
        this.indicator = indicator;
    }
    
    public ApiEMGroup getApiErrors() {
        return errors;
    }
    
    public TranslationUtil getContentTranslator() {
        return contentTranslator;
    }
    
    public Long getIndicatorId() {
        if (!indicatorIdDetected) {
            indicatorIdDetected = true;
            indicatorId = indicator.getId();
        }
        return indicatorId;
    }

    public AmpIndicatorLayer getIndicatorLayer() {
        AmpIndicatorLayer indicatorLayer;
        Long indicatorId = getIndicatorId();

        if (indicatorId != null) {
            indicatorLayer = DynLocationManagerUtil.getIndicatorLayerById(indicatorId);
        } else{
            indicatorLayer = new AmpIndicatorLayer();
            indicatorLayer.setId(null);
        }

        indicatorLayer.setName(contentTranslator.extractTranslationsOrSimpleValue(
                IndicatorEPConstants.NAME, indicatorLayer, indicator.getName()));
        indicatorLayer.setDescription(contentTranslator.extractTranslationsOrSimpleValue(
                IndicatorEPConstants.DESCRIPTION, indicatorLayer, indicator.getDescription()));
        indicatorLayer.setUnit(contentTranslator.extractTranslationsOrSimpleValue(
                IndicatorEPConstants.UNIT, indicatorLayer, indicator.getUnit()));
        indicatorLayer.setNumberOfClasses(indicator.getNumberOfClasses());
        addIndicatorType(indicatorLayer);

        if (indicatorLayer.getId() == null) {
            indicatorLayer.setCreatedOn(new Date());
            indicatorLayer.setCreatedBy(TeamUtil.getCurrentAmpTeamMember());
        }
        indicatorLayer.setUpdatedOn(new Date());
        indicatorLayer.setAccessType((indicator.getAccessTypeId() != null
                ? IndicatorAccessType.getValueFromLong(indicator.getAccessTypeId()) : IndicatorAccessType.TEMPORARY));
        setAdmLevel(indicatorLayer);  
        
        if (indicator.getZeroCategoryEnabled() != null) {
           indicatorLayer.setZeroCategoryEnabled(indicator.getZeroCategoryEnabled());
        }

        if (indicator.getColorRampId() != null) {
            Set<AmpIndicatorColor> colorRamp = new HashSet<>();
            String[] colorRampColors = ColorRampUtil.getColorRamp(indicator.getColorRampId().intValue(),
                    indicatorLayer.getNumberOfClasses());
            for (int i = 0; i < colorRampColors.length; i++) {
                AmpIndicatorColor color = new AmpIndicatorColor();
                long payload = i + 1;
                color.setPayload(payload);
                color.setColor(colorRampColors[i]);
                color.setIndicatorLayer(indicatorLayer);
                colorRamp.add(color);
            }
            
            if (indicatorLayer.getColorRamp() != null) {
                indicatorLayer.getColorRamp().clear();
                indicatorLayer.getColorRamp().addAll(colorRamp); 
            } else {
                indicatorLayer.setColorRamp(colorRamp);
            }
        }

        if (indicator.getSharedWorkspaces() != null) {
            Set<AmpIndicatorWorkspace> teams = new HashSet<>();
            for (Long wsId : indicator.getSharedWorkspaces()) {
                AmpTeam team = TeamUtil.getAmpTeam(wsId);
                AmpIndicatorWorkspace indicatorWs = new AmpIndicatorWorkspace();
                indicatorWs.setWorkspace(team);
                indicatorWs.setIndicatorLayer(indicatorLayer);
                teams.add(indicatorWs);
            }
            
            if (indicatorLayer.getSharedWorkspaces() != null) {
                indicatorLayer.getSharedWorkspaces().clear();
                indicatorLayer.getSharedWorkspaces().addAll(teams); 
            } else {
                indicatorLayer.setSharedWorkspaces(teams);
            }
        }

        if (indicator.getValues() != null) {

            Indicator.Option option = indicator.getOption();

            Set<AmpLocationIndicatorValue> locationIndicatorValues = new HashSet<>();
            for (IndicatorValue location : indicator.getValues()) {

                long locId = location.getId();
                AmpCategoryValue admLevel = indicatorLayer.getAdmLevel();
                AmpCategoryValueLocations locationObject = DynLocationManagerUtil.getLocationById(locId, admLevel);

                if (locationObject != null) {

                    AmpLocationIndicatorValue locationIndicatorValue = null;

                    if (indicatorLayer.getId() != null) {
                        locationIndicatorValue = DynLocationManagerUtil.getLocationIndicatorValue(indicatorLayer.getId(), locId);
                    }

                    if (locationIndicatorValue != null && option == Indicator.Option.NEW) {
                        continue;
                    } else {
                        if (locationIndicatorValue == null) {
                            locationIndicatorValue = new AmpLocationIndicatorValue();
                            locationIndicatorValue.setLocation(locationObject);
                            locationIndicatorValue.setIndicator(indicatorLayer);
                        }
                        locationIndicatorValue.setValue(location.getValue().doubleValue());
                    }
                    locationIndicatorValues.add(locationIndicatorValue);
                } else {
                    errors.addApiErrorMessage(IndicatorErrors.LOCATION_NOT_FOUND,
                            IndicatorEPConstants.FIELD_ID + " = " + locId);
                }
            }
            
            if (indicatorLayer.getIndicatorValues() != null) {
                indicatorLayer.getIndicatorValues().clear();
                indicatorLayer.getIndicatorValues().addAll(locationIndicatorValues); 
            } else {
                indicatorLayer.setIndicatorValues(locationIndicatorValues);
            }
        }

        return indicatorLayer;
    }
    
    private void setAdmLevel(AmpIndicatorLayer indicatorLayer) {
        AmpCategoryValue newAdmLevel = CategoryManagerUtil.getAmpCategoryValueFromDb(indicator.getAdmLevelId());
        // configure or update population flag: true if was already designated AND has & have the same valid admLevel
        boolean isPopulation = indicatorLayer.getId() != null && indicatorLayer.isPopulation()
                && newAdmLevel != null && newAdmLevel.equals(indicatorLayer.getAdmLevel());
        indicatorLayer.setPopulation(isPopulation);
        indicatorLayer.setAdmLevel(newAdmLevel);
    }
    
    private void addIndicatorType(AmpIndicatorLayer ampIndicatorLayer) {
        Long typeId = indicator.getIndicatorTypeId();
        AmpCategoryValue acv = typeId == null ? null : CategoryManagerUtil.getAmpCategoryValueFromDb(typeId);
        if (acv != null && CategoryConstants.INDICATOR_LAYER_TYPE_KEY.equals(acv.getAmpCategoryClass().getKeyName())) {
            ampIndicatorLayer.setIndicatorType(acv);
        } else {
            errors.addApiErrorMessage(IndicatorErrors.INVALID_INDICATOR_TYPE,
                    IndicatorEPConstants.INDICATOR_TYPE_ID + " = " + typeId);
        }
    }

}
