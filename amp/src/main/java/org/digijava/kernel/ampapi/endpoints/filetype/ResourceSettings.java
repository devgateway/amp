package org.digijava.kernel.ampapi.endpoints.filetype;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;

public class ResourceSettings {
    
    @JsonProperty(SettingsConstants.LIMIT_FILE_TO_UPLOAD)
    @ApiModelProperty(value = "Enable the file type validation")
    private Boolean limitFileToUpload;
    
    @JsonProperty(SettingsConstants.MAXIMUM_FILE_SIZE)
    @ApiModelProperty(value = "The maximum limit of the file size, in MB", example = "20")
    private Long maximumFileSize;
    
    @JsonProperty(SettingsConstants.SORT_COLUMN)
    @ApiModelProperty(value = "The column used to sort the items in the resource table",
            allowableValues = "resource_title_ASC, resource_title_DESC, type_ASC, type_DESC, file_name_ASC, "
                    + "file_name_DESC, date_ASC, date_DESC, yearOfPublication_ASC, yearOfPublication_DESC, size_ASC, "
                    + "size_DESC, cm_doc_type_ASC, cm_doc_type_DESC")
    private String sortColumn;
    
    public Boolean getLimitFileToUpload() {
        return limitFileToUpload;
    }
    
    public void setLimitFileToUpload(Boolean limitFileToUpload) {
        this.limitFileToUpload = limitFileToUpload;
    }
    
    public Long getMaximumFileSize() {
        return maximumFileSize;
    }
    
    public void setMaximumFileSize(Long maximumFileSize) {
        this.maximumFileSize = maximumFileSize;
    }
    
    public String getSortColumn() {
        return sortColumn;
    }
    
    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }
}
