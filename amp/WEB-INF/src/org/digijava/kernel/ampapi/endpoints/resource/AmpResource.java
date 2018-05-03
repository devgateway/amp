package org.digijava.kernel.ampapi.endpoints.resource;

import java.util.Date;

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
    
    @Interchangeable(fieldTitle = "File Name")
    private String fileName;
    
    @Interchangeable(fieldTitle = "Web Link", importable = true)
    private String webLink;
    
    @Interchangeable(fieldTitle = "Description", importable = true)
    private String description;
    
    @Interchangeable(fieldTitle = "Note", importable = true)
    private String note;
    
    @PossibleValues(ResourceTypePossibleValuesProvider.class)
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
    
    @Interchangeable(fieldTitle = "Private")
    private Boolean isPrivate;
    
    @Interchangeable(fieldTitle = "Creator Email")
    private String creatorEmail;
    
    @Interchangeable(fieldTitle = "Team")
    private Long team;
    
    @Interchangeable(fieldTitle = "Team Member")
    private Long teamMember;
    
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

    public Long getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(Long teamMember) {
        this.teamMember = teamMember;
    }
    
}
