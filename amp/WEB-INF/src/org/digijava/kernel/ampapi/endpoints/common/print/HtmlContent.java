package org.digijava.kernel.ampapi.endpoints.common.print;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by esoliani on 22/07/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class HtmlContent {
    private String content;
    private Integer width;
    private Integer height;
    private String javascript;

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

    public String getJavascript() {
        return javascript;
    }

    public void setJavascript(final String javascript) {
        this.javascript = javascript;
    }
}
