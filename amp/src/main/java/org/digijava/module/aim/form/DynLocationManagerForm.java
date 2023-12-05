package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DynLocationManagerForm extends ActionForm {
    public enum Option{
        OVERWRITE,NEW
    }
    private Collection<AmpCategoryValueLocations> firstLevelLocations;
    private Collection<AmpCategoryValueLocations> unorganizedLocations;
    private List<AmpCategoryValue> locationLevels = new ArrayList<AmpCategoryValue>();
    private int numOfLayers;
    private Long firstLayerId;
    private String treeStructure;
    private String unorgLocations;
    private Long deleteLocationId;
    private boolean hideEmptyCountries  = true;
    private boolean importantErrorAppeared = false;
    private FormFile fileUploaded;
    private int option;

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public FormFile getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(FormFile fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    

    public Collection<AmpCategoryValueLocations> getFirstLevelLocations() {
        return firstLevelLocations;
    }

    public void setFirstLevelLocations(
            Collection<AmpCategoryValueLocations> firstLevelLocations) {
        this.firstLevelLocations = firstLevelLocations;
    }
    
    public Collection<AmpCategoryValueLocations> getUnorganizedLocations() {
        return unorganizedLocations;
    }

    public void setUnorganizedLocations(
            Collection<AmpCategoryValueLocations> unorganizedLocations) {
        this.unorganizedLocations = unorganizedLocations;
    }
    
    public List<AmpCategoryValue> getLocationLevels() {
        return locationLevels;
    }
    
    public void setLocationLevels(List<AmpCategoryValue> locationLevels) {
        this.locationLevels = locationLevels;
    }
    
    public int getNumOfLayers() {
        return numOfLayers;
    }

    public void setNumOfLayers(int numOfLayers) {
        this.numOfLayers = numOfLayers;
    }

    public Long getFirstLayerId() {
        return firstLayerId;
    }

    public void setFirstLayerId(Long firstLayerId) {
        this.firstLayerId = firstLayerId;
    }

    public String getTreeStructure() {
        return treeStructure;
    }

    public void setTreeStructure(String treeStructure) {
        this.treeStructure = treeStructure;
    }

    public String getUnorgLocations() {
        return unorgLocations;
    }

    public void setUnorgLocations(String unorgLocations) {
        this.unorgLocations = unorgLocations;
    }

    public Long getDeleteLocationId() {
        return deleteLocationId;
    }

    public void setDeleteLocationId(Long deleteLocationId) {
        this.deleteLocationId = deleteLocationId;
    }

    /**
     * @return the hideEmptyCountries
     */
    public boolean getHideEmptyCountries() {
        return hideEmptyCountries;
    }

    /**
     * @param hideEmptyCountries the hideEmptyCountries to set
     */
    public void setHideEmptyCountries(boolean hideEmptyCountries) {
        this.hideEmptyCountries = hideEmptyCountries;
    }

    /**
     * @return the importantErrorAppeared
     */
    public boolean getImportantErrorAppeared() {
        return importantErrorAppeared;
    }

    /**
     * @param importantErrorAppeared the importantErrorAppeared to set
     */
    public void setImportantErrorAppeared(boolean importantErrorAppeared) {
        this.importantErrorAppeared = importantErrorAppeared;
    }
    
    
}
