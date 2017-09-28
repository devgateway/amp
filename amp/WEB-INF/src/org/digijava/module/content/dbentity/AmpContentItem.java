package org.digijava.module.content.dbentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AmpContentItem {

    private Long ampContentItemId;
    private String pageCode;
    private String layout;
    private String title;
    private String description;
    private String htmlblock_1;
    private String htmlblock_2;
    private Boolean isHomepage;

    private Set<AmpContentItemThumbnail> contentThumbnails;

    public void setAmpContentItemId(Long ampContentItemId) {
        this.ampContentItemId = ampContentItemId;
    }

    public Long getAmpContentItemId() {
        return ampContentItemId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setContentThumbnails(
            Set<AmpContentItemThumbnail> contentThumbnails) {
        this.contentThumbnails = contentThumbnails;
    }

    public Set<AmpContentItemThumbnail> getContentThumbnails() {
        return contentThumbnails;
    }

    public void setHtmlblock_1(String htmlblock_1) {
        this.htmlblock_1 = htmlblock_1;
    }

    public String getHtmlblock_1() {
        return htmlblock_1;
    }

    public void setHtmlblock_2(String htmlblock_2) {
        this.htmlblock_2 = htmlblock_2;
    }

    public String getHtmlblock_2() {
        return htmlblock_2;
    }

    public void setIsHomepage(Boolean isHomepage) {
        this.isHomepage = isHomepage;
    }

    public Boolean getIsHomepage() {
        return isHomepage;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getLayout() {
        return layout;
    }
}
