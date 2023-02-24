package org.digijava.kernel.ampapi.endpoints.common.print;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by esoliani on 22/07/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class HtmlContent {

    @ApiModelProperty(value = "the html content", example = "<html>...</html>")
    private String content;

    @ApiModelProperty(value = "the width of the image (in px)", example = "100")
    private Integer width;

    @ApiModelProperty(value = "the height of the image (in px)", example = "100")
    private Integer height;

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(final Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(final Integer height) {
        this.height = height;
    }
}
