package org.digijava.kernel.ampapi.endpoints.resource;

import java.util.Date;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeDependencyResolver;
import org.digijava.module.aim.annotations.activityversioning.ResourceTextField;
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
    @ResourceTextField(fieldTitle = "Title", translationsField = "translatedTitles")
    private String title;
    
    @Interchangeable(fieldTitle = "File Name", importable = true, required = ActivityEPConstants.REQUIRED_ALWAYS,
            dependencies = InterchangeDependencyResolver.RESOURCE_TYPE_FILE_VALID_KEY)
    private String fileName;
    
    @Interchangeable(fieldTitle = "Web Link", importable = true, required = ActivityEPConstants.REQUIRED_ALWAYS,
            dependencies = InterchangeDependencyResolver.RESOURCE_TYPE_LINK_VALID_KEY)
    private String webLink;
    
    @Interchangeable(fieldTitle = "Description", importable = true)
    @ResourceTextField(fieldTitle = "Description", translationsField = "translatedDescriptions")
    private String description;
    
    @Interchangeable(fieldTitle = "Note", importable = true)
    @ResourceTextField(fieldTitle = "Note", translationsField = "translatedNotes")
    private String note;
    
    @PossibleValues(DocumentTypePossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Type", importable = true, pickIdOnly = true)
    private AmpCategoryValue type;
    
    @Interchangeable(fieldTitle = "URL")
    private String url;
    
    @Interchangeable(fieldTitle = "Year Of Publication")
    private String yearOfPublication;
    
    @Interchangeable(fieldTitle = "Adding Date")
    private Date addingDate;
    
    @Interchangeable(fieldTitle = "File Size")
    private Double fileSize;
    
    @Interchangeable(fieldTitle = "Public")
    private Boolean isPublic;
    
    @Interchangeable(fieldTitle = "Private", importable = true, required = ActivityEPConstants.REQUIRED_ALWAYS)
    private Boolean isPrivate;
    
    @Interchangeable(fieldTitle = "Creator Email", importable = true, required = ActivityEPConstants.REQUIRED_ALWAYS)
    private String creatorEmail;
    
    @Interchangeable(fieldTitle = "Team", importable = true, required = ActivityEPConstants.REQUIRED_ALWAYS)
    private Long team;
    
    @PossibleValues(ResourceTypePossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Resource Type", importable = true, required = ActivityEPConstants.REQUIRED_ALWAYS)
    private String resourceType;
    
    private Map<String, String> translatedTitles;
    private Map<String, String> translatedDescriptions;
    private Map<String, String> translatedNotes;
    
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(Date addingDate) {
        this.addingDate = addingDate;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(String yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public Boolean isPublic() {
        return isPublic;
    }

    public void setPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public Boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Long getTeam() {
        return team;
    }

    public void setTeam(Long team) {
        this.team = team;
    }
    
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Map<String, String> getTranslatedTitles() {
        return translatedTitles;
    }

    public void setTranslatedTitles(Map<String, String> translatedTitles) {
        this.translatedTitles = translatedTitles;
    }

    public Map<String, String> getTranslatedDescriptions() {
        return translatedDescriptions;
    }

    public void setTranslatedDescriptions(Map<String, String> translatedDescriptions) {
        this.translatedDescriptions = translatedDescriptions;
    }

    public Map<String, String> getTranslatedNotes() {
        return translatedNotes;
    }

    public void setTranslatedNotes(Map<String, String> translatedNotes) {
        this.translatedNotes = translatedNotes;
    }
    
}
