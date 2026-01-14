package org.digijava.kernel.ampapi.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class DateConversionResult {

    @JsonProperty("converted-date")
    @ApiModelProperty(value = "converted date in the format yyyy-MM-dd", example = "2006-11-20")
    private String convertedDate;

    public DateConversionResult(String convertedDate) {
        this.convertedDate = convertedDate;
    }

    public String getConvertedDate() {
        return convertedDate;
    }
}
