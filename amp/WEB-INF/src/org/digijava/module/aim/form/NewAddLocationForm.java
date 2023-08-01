/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.digijava.module.aim.form;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author medea
 */
public class NewAddLocationForm extends ActivityForm {

    private List<AmpCategoryValue> categoryValues;
    private List<AmpCategoryValueLocations> locations;
    private String ids;
    private String name = null;
    private String iso;
    private String iso3;
    private String code;
    private String description;
    private String gsLat;
    private String gsLong;
    private String event;
    private Long editedId;
    private Long categoryIndex;
    private Long parentLocationId;
    private Long parentCatValId;
    private Long locationId;
    private Collection <AmpLocationIndicatorValue> locationIndicatorValues;
    

    public String getParentCatValName() {
        return CategoryManagerUtil.getAmpCategoryValueFromDb(parentCatValId).getValue();
    }

   

    public Long getParentCatValId() {
        return parentCatValId;
    }

    public void setParentCatValId(Long parentCatValId) {
        this.parentCatValId = parentCatValId;
    }

    public Long getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(Long parentLocationId) {
        this.parentLocationId = parentLocationId;
    }

    public boolean getCategoryLevelCountry() {
        return CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.getValueKey().equals(
                 CategoryManagerUtil.getAmpCategoryValueFromDb(parentCatValId).getValue() );
    }


    public Long getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(Long categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    public Long getEditedId() {
        return editedId;
    }

    public void setEditedId(Long editedId) {
        this.editedId = editedId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getGsLat() {
        return gsLat;
    }

    public void setGsLat(String gsLat) {
        this.gsLat = gsLat;
    }

    public String getGsLong() {
        return gsLong;
    }

    public void setGsLong(String gsLong) {
        this.gsLong = gsLong;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private String geoCode;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<AmpCategoryValue> getCategoryValues() {
        return categoryValues;
    }

    public void setCategoryValues(List<AmpCategoryValue> categoryValues) {
        this.categoryValues = categoryValues;
    }

    public List<AmpCategoryValueLocations> getLocations() {
        return locations;
    }

    public void setLocations(List<AmpCategoryValueLocations> locations) {
        this.locations = locations;
    }



    public Collection<AmpLocationIndicatorValue> getLocationIndicatorValues() {
        return locationIndicatorValues;
    }



    public void setLocationIndicatorValues(Collection<AmpLocationIndicatorValue> locationIndicatorValues) {
        this.locationIndicatorValues = locationIndicatorValues;
    }



    public Long getLocationId() {
        return locationId;
    }



    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    
   
}
