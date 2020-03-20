package org.digijava.kernel.ampapi.endpoints.resource.dto;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.ALWAYS;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.kernel.validators.resource.ResourceRequiredValidator;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceEPConstants;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceType;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceTypePossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601TimeStampSerializer;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.annotations.interchange.ResourceFieldsConstants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

/**
 *
 * @author Viorel Chihai
 *
 */
@InterchangeableValidator(ResourceRequiredValidator.class)
@JsonPropertyOrder({ ResourceEPConstants.UUID, ResourceEPConstants.TITLE, ResourceEPConstants.FILE_NAME,
    ResourceEPConstants.WEB_LINK, ResourceEPConstants.DESCRIPTION, ResourceEPConstants.NOTE,
    ResourceEPConstants.TYPE, ResourceEPConstants.LINK, ResourceEPConstants.YEAR_OF_PUBLICATION,
    ResourceEPConstants.ADDING_DATE, ResourceEPConstants.FILE_SIZE, ResourceEPConstants.PUBLIC,
    ResourceEPConstants.PRIVATE, ResourceEPConstants.CREATOR_EMAIL, ResourceEPConstants.TEAM,
    ResourceEPConstants.RESOURCE_TYPE
})
public class AmpResource {

    @Interchangeable(fieldTitle = "UUID")
    @JsonProperty(ResourceEPConstants.UUID)
    @ApiModelProperty(example = "05a2f2d4-58f5-4198-8a05-cf42a758ce85")
    private String uuid;

    @Interchangeable(fieldTitle = "Title", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @JsonProperty(ResourceEPConstants.TITLE)
    private MultilingualContent title;

    @Interchangeable(fieldTitle = ResourceFieldsConstants.FILE_NAME, importable = true,
            requiredDependencies = ResourceRequiredValidator.RESOURCE_TYPE_FILE_VALID_KEY,
            dependencyRequired = ALWAYS)
    @JsonProperty(ResourceEPConstants.FILE_NAME)
    @JsonView(ResourceView.File.class)
    private String fileName;

    @Interchangeable(fieldTitle = ResourceFieldsConstants.WEB_LINK, importable = true,
            requiredDependencies = ResourceRequiredValidator.RESOURCE_TYPE_LINK_VALID_KEY,
            dependencyRequired = ALWAYS)
    @JsonProperty(ResourceEPConstants.WEB_LINK)
    @ApiModelProperty(example = "https://www.postgresql.org/docs/9.2/static/sql-createcast.html")
    @JsonView(ResourceView.Link.class)
    private String webLink;

    @Interchangeable(fieldTitle = "Description", importable = true)
    @JsonProperty(ResourceEPConstants.DESCRIPTION)
    private MultilingualContent description;

    @Interchangeable(fieldTitle = "Note", importable = true)
    @JsonProperty(ResourceEPConstants.NOTE)
    private MultilingualContent note;

    @Interchangeable(fieldTitle = "Type", importable = true, pickIdOnly = true,
            discriminatorOption = CategoryConstants.DOCUMENT_TYPE_KEY)
    @JsonProperty(ResourceEPConstants.TYPE)
    @JsonView(ResourceView.Full.class)
    @ApiModelProperty(example = "50", dataType = "java.lang.Long")
    private AmpCategoryValue type;

    @Interchangeable(fieldTitle = "URL")
    @ApiModelProperty(example = "/contentrepository/downloadFile.do?uuid=05a2f2d4-58f5-4198-8a05-cf42a758ce85")
    @JsonView(ResourceView.Full.class)
    private String url;

    @Interchangeable(fieldTitle = "Year Of Publication", importable = true)
    @JsonProperty(ResourceEPConstants.YEAR_OF_PUBLICATION)
    @ApiModelProperty(example = "2002")
    @JsonView(ResourceView.Full.class)
    private String yearOfPublication;

    @Interchangeable(fieldTitle = "Adding Date")
    @ApiModelProperty(example = "2019-04-12T09:32:38.922+0000")
    @JsonSerialize(using = ISO8601TimeStampSerializer.class)
    @JsonProperty(ResourceEPConstants.ADDING_DATE)
    private Date addingDate;

    @Interchangeable(fieldTitle = "File Size")
    @JsonProperty(ResourceEPConstants.FILE_SIZE)
    @JsonView(ResourceView.Full.class)
    private Double fileSize;

    @Interchangeable(fieldTitle = "Public")
    private Boolean isPublic;

    @Interchangeable(fieldTitle = "Private", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private Boolean isPrivate;

    @Interchangeable(fieldTitle = "Creator Email", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @ApiModelProperty(example = "john.smith@amp.org")
    @JsonProperty(ResourceEPConstants.CREATOR_EMAIL)
    @JsonView(ResourceView.Full.class)
    private String creatorEmail;

    @Interchangeable(fieldTitle = "Team", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @JsonProperty(ResourceEPConstants.TEAM)
    private Long team;

    @PossibleValues(ResourceTypePossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Resource Type", importable = true, pickIdOnly = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @JsonIgnore
    private ResourceType resourceType;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public MultilingualContent getTitle() {
        return title;
    }

    public void setTitle(MultilingualContent title) {
        this.title = title;
    }

    public MultilingualContent getDescription() {
        return description;
    }

    public void setDescription(MultilingualContent description) {
        this.description = description;
    }

    public MultilingualContent getNote() {
        return note;
    }

    public void setNote(MultilingualContent note) {
        this.note = note;
    }

    @JsonProperty(ResourceEPConstants.TYPE)
    public Long getTypeId() {
        return type == null ? null : type.getId();
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonProperty(ResourceEPConstants.RESOURCE_TYPE)
    @ApiModelProperty(accessMode = AccessMode.READ_WRITE)
    public Integer getResourceTypeId() {
        return resourceType.getId();
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

}
