package org.digijava.kernel.ampapi.endpoints.resource;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class AmpResource {
    
    @Interchangeable(fieldTitle = "UUID")
    private String uuid;
    
    @Interchangeable(fieldTitle = "Title", importable = true, required = ActivityEPConstants.REQUIRED_ALWAYS)
    private String title;
    
    @Interchangeable(fieldTitle = "Description", importable = true)
    private String description;
    
    @Interchangeable(fieldTitle = "Note", importable = true)
    private String note;
    
    @PossibleValues(ResourceTypePossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Type", importable = true, pickIdOnly = true)
    private AmpCategoryValue type;
    
    @Interchangeable(fieldTitle = "URL", importable = true)
    private String url;
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AmpCategoryValue getType() {
        return type;
    }

    public void setType(AmpCategoryValue type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
