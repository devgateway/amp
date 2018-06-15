/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.indicator;

import org.apache.commons.lang.math.NumberUtils;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
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

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Creates a new indicator or updates an existing one
 * 
 * @author Nadejda Mandrescu
 */
public class IndicatorUpdater {
    
    private JsonBean indicator;
    
    private ApiEMGroup errors = new ApiEMGroup(); 
    private TranslationUtil contentTranslator = new TranslationUtil();
    private Long indicatorId;
    private boolean indicatorIdDetected;
    
    public IndicatorUpdater(JsonBean indicator) {
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
            String indIdStr = indicator.getString(IndicatorEPConstants.ID);
            if (indIdStr != null) {
                if (NumberUtils.isNumber(indIdStr)) {
                    indicatorId = Long.valueOf(indIdStr);
                } else {
                    errors.addApiErrorMessage(IndicatorErrors.FIELD_INVALID_VALUE, IndicatorEPConstants.ID + " = " + indIdStr);
                }
            }
        }
        return indicatorId;
    }

    public AmpIndicatorLayer getIndicatorLayer() {
        AmpIndicatorLayer indicatorLayer = null;
        Long indicatorId = getIndicatorId();

        if (indicatorId != null) {
            indicatorLayer = DynLocationManagerUtil.getIndicatorLayerById(indicatorId);
        } else{
            indicatorLayer = new AmpIndicatorLayer();
            indicatorLayer.setId(null);
        }

        indicatorLayer.setName(contentTranslator.extractTranslationsOrSimpleValue(IndicatorEPConstants.NAME, indicatorLayer, indicator.get(IndicatorEPConstants.NAME)));
        indicatorLayer.setDescription(contentTranslator.extractTranslationsOrSimpleValue(IndicatorEPConstants.DESCRIPTION, indicatorLayer, indicator.get(IndicatorEPConstants.DESCRIPTION)));
        indicatorLayer.setUnit(contentTranslator.extractTranslationsOrSimpleValue(IndicatorEPConstants.UNIT, indicatorLayer, indicator.get(IndicatorEPConstants.UNIT)));
        indicatorLayer.setNumberOfClasses(Long.valueOf(indicator.getString(IndicatorEPConstants.NUMBER_OF_CLASSES)));
        addIndicatorType(indicatorLayer);

        if (indicatorLayer.getId() == null) {
            indicatorLayer.setCreatedOn(new Date());
            indicatorLayer.setCreatedBy(TeamUtil.getCurrentAmpTeamMember());
        }
        indicatorLayer.setUpdatedOn(new Date());
        indicatorLayer.setAccessType( (indicator.getString(IndicatorEPConstants.ACCESS_TYPE_ID)!=null ? IndicatorAccessType.getValueFromLong(Long.valueOf(indicator.getString(IndicatorEPConstants.ACCESS_TYPE_ID))) :IndicatorAccessType.TEMPORARY));
        setAdmLevel(indicatorLayer);  
        
        String zeroCategoryEnabled = indicator.getString(IndicatorEPConstants.FIELD_ZERO_CATEGORY_ENABLED);
        if (zeroCategoryEnabled != null) {
           indicatorLayer.setZeroCategoryEnabled(Boolean.parseBoolean(zeroCategoryEnabled));
        }        
        
        if (indicator.get(IndicatorEPConstants.COLOR_RAMP_ID)!=null) {
            Set<AmpIndicatorColor> colorRamp = new HashSet<AmpIndicatorColor>();
            String[] colorRampColors = ColorRampUtil.getColorRamp(EndpointUtils.getSingleValue(indicator, IndicatorEPConstants.COLOR_RAMP_ID,null),
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

        if (indicator.get(IndicatorEPConstants.SHARED_WORKSPACES)!=null) {
            Set<AmpIndicatorWorkspace> teams = new HashSet<AmpIndicatorWorkspace>();
            List<String> indicatorTeams =  EndpointUtils.getSingleValue(indicator, IndicatorEPConstants.SHARED_WORKSPACES, Collections.emptyList());
            for (int i = 0; i < indicatorTeams.size(); i++) {
                AmpTeam team = TeamUtil.getAmpTeam(new Long(String.valueOf(indicatorTeams.get(i))));
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

        if (indicator.get(IndicatorEPConstants.VALUES)!=null) {

            IndicatorImporter.Option option = (indicator.getString(IndicatorEPConstants.OPTION_TO_SAVE_VALUES) == "new") ? IndicatorImporter.Option.NEW: IndicatorImporter.Option.OVERWRITE;

            Set<AmpLocationIndicatorValue> locationIndicatorValues = new HashSet<AmpLocationIndicatorValue>();
            List<LinkedHashMap<String, Object>> locationValues =  EndpointUtils.getSingleValue(indicator, IndicatorEPConstants.VALUES, Collections.emptyList());
            for (int i = 0; i < locationValues.size(); i++) {

                LinkedHashMap<String, Object> location = locationValues.get(i);

                long locId = new Long(String.valueOf(location.get(IndicatorEPConstants.ID)));
                AmpCategoryValueLocations locationObject = DynLocationManagerUtil.getLocationById(locId , indicatorLayer.getAdmLevel());

                if (locationObject != null ) {

                    AmpLocationIndicatorValue locationIndicatorValue = null;

                    if (indicatorLayer.getId() != null) {
                        locationIndicatorValue = DynLocationManagerUtil.getLocationIndicatorValue(indicatorLayer.getId(), locId);
                    }

                    if (locationIndicatorValue != null && option.equals(IndicatorImporter.Option.NEW)) {
                        continue;
                    } else {
                        if (locationIndicatorValue != null) {
                            locationIndicatorValue.setValue(Double.parseDouble(String.valueOf(location.get(IndicatorEPConstants.VALUE))));
                        } else {
                            locationIndicatorValue = new AmpLocationIndicatorValue();
                            locationIndicatorValue.setLocation(locationObject);
                            locationIndicatorValue.setIndicator(indicatorLayer);
                            locationIndicatorValue.setValue(Double.parseDouble(String.valueOf(location.get(IndicatorEPConstants.VALUE))));
                        }
                    }
                    locationIndicatorValues.add(locationIndicatorValue);
                } else {
                    errors.addApiErrorMessage(IndicatorErrors.LOCATION_NOT_FOUND, IndicatorEPConstants.FIELD_ID + " = " + locId);
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
        AmpCategoryValue newAdmLevel = CategoryManagerUtil.getAmpCategoryValueFromDb(Long.valueOf(indicator.getString(IndicatorEPConstants.ADM_LEVEL_ID)));
        // configure or update population flag: true if was already designated AND has & have the same valid admLevel
        boolean isPopulation = indicatorLayer.getId() != null && indicatorLayer.isPopulation()
                && newAdmLevel != null && newAdmLevel.equals(indicatorLayer.getAdmLevel());
        indicatorLayer.setPopulation(isPopulation);
        indicatorLayer.setAdmLevel(newAdmLevel);
    }
    
    private void addIndicatorType(AmpIndicatorLayer ampIndicatorLayer) {
        Object type = indicator.get(IndicatorEPConstants.INDICATOR_TYPE_ID);
        Long typeId = (type != null && type instanceof Integer) ? ((Integer) type).longValue() : null;
        AmpCategoryValue acv = typeId == null ? null : CategoryManagerUtil.getAmpCategoryValueFromDb(typeId);
        if (acv != null && CategoryConstants.INDICATOR_LAYER_TYPE_KEY.equals(acv.getAmpCategoryClass().getKeyName())) {
            ampIndicatorLayer.setIndicatorType(acv);
        } else {
            errors.addApiErrorMessage(IndicatorErrors.INVALID_INDICATOR_TYPE, IndicatorEPConstants.INDICATOR_TYPE_ID + " = " + type);
        }
    }

}
