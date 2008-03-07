/*
 *   HighlightItemForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 10, 2003
 * 	 CVS-ID: $Id$
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.module.highlights.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.common.action.PaginationForm;
import org.digijava.module.highlights.dbentity.Highlight;
import java.util.Comparator;
import java.util.Collections;

public class HighlightItemForm
    extends PaginationForm {

    public static class HighlightInfo {

        /**
         * Highlight identity
         */
        private Long id;

        /**
         * Highlight title
         */
        private String title;
        /**
         * Highlight description
         */
        private String description;

        /**
         * Highlight topic
         */
        private String topic;

        /**
         * Array of Highlight links
         */
        private ArrayList links;

        /**
         * Highlight layout:
         * 1 - for Layout_1,
         * 2 - for Layout_2,
         * 3 - for Layout_3
         */
        private int layout;

        /**
         * Highlight image height
         */
        private String imageHeight;

        /**
         * Highlight image width
         */
        private String imageWidth;

        /**
         * Highlight creation date
         */
        private String creationDate;

        /**
         * Highlight author First names
         */
        private String authorFirstName;

        /**
         * Highlight author Last name
         */
        private String authorLastName;

        /**
         * true if Highligh image should be visible,
         * false otherwise
         */
        private boolean showImage;

        /**
         * true if Highlight has an image(for sayouts Layout_1 and Layout_2),
         * false otherwise(for Layout_3)
         */
        private boolean haveImage;

        /**
         * true if image sizes were indicated when creating Highlight,
         * false otherwise - in this case, Highligh image is displayed in it's original size
         */
        private boolean haveImageSizes;

        /**
         * true if Highlight is active,
         * false if Highlight is archived
         */
        private boolean active;

        /**
         * true if Highlight is public,
         * false otherwise
         */
        private boolean isPublic;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public ArrayList getLinks() {
            return links;
        }

        public void setLinks(ArrayList links) {
            this.links = links;
        }

        public String getImageHeight() {
            return imageHeight;
        }

        public void setImageHeight(String imageHeight) {
            this.imageHeight = imageHeight;
        }

        public String getImageWidth() {
            return imageWidth;
        }

        public void setImageWidth(String imageWidth) {
            this.imageWidth = imageWidth;
        }

        public int getLayout() {
            return layout;
        }

        public void setLayout(int layout) {
            this.layout = layout;
        }

        public String getAuthorFirstName() {
            return authorFirstName;
        }

        public void setAuthorFirstName(String authorFirstName) {
            this.authorFirstName = authorFirstName;
        }

        public String getAuthorLastName() {
            return authorLastName;
        }

        public void setAuthorLastName(String authorLastName) {
            this.authorLastName = authorLastName;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public boolean isShowImage() {
            return showImage;
        }

        public void setShowImage(boolean showImage) {
            this.showImage = showImage;
        }

        public boolean isHaveImage() {
            return haveImage;
        }

        public void setHaveImage(boolean haveImage) {
            this.haveImage = haveImage;
        }

        public boolean isHaveImageSizes() {
            return haveImageSizes;
        }

        public void setHaveImageSizes(boolean haveImageSizes) {
            this.haveImageSizes = haveImageSizes;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isIsPublic() {
            return isPublic;
        }

        public void setIsPublic(boolean isPublic) {
            this.isPublic = isPublic;
        }
    }

    public static class LinkInfo {
        private Long id;
        private String url;
        private String name;
        private int index;

        public LinkInfo() {
            url = new String("");
            name = new String("");
        }

        public LinkInfo(Long id, String name, String url, int index) {
            this.id = id;
            this.name = name;
            this.url = url;
            this.index = index;
        }
        public LinkInfo(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    /**
     * active Highlight identity
     */
    private Long activeHighlightId;

    /**
     * Highlight title
     */
    private String title;

    /**
     * Highlight description
     */
    private String description;

    /**
     * Highlight topic
     */
    private String topic;

    /**
     * true if Highlight is active,
     * false if Highlight is archived
     */
    private boolean active;

    /**
     * Highlight creation date
     */
    private String creationDate;

    /**
     * Highlight last updation date
     */
    private String updationDate;

    /**
     * Array of Highlight links
     */
    private ArrayList links;

    /**
     * Highlight layout:
     * "1" - for Layout_1,
     * "2" - for Layout_2,
     * "3" - for Layout_3
     */
    private String layout;

    /**
     * true if Layout_1 was selected,
     * false otherwise
     */
    private boolean layout1;

    /**
     * true if Layout_2 was selected,
     * false otherwise
     */
    private boolean layout2;

    /**
     * true if Layout_3 was selected,
     * false otherwise
     */
    private boolean layout3;

    /**
     * Highlight image height for Layout_1
     */
    private String imageHeight1;

    /**
     * Highlight image width for Layout_1
     */
    private String imageWidth1;

    /**
     * Highlight image height for Layout_2
     */
    private String imageHeight2;

    /**
     * Highlight image width for Layout_2
     */
    private String imageWidth2;

    /**
     * Highlight image height
     */
    private String imageHeight;

    /**
     * Highlight image width
     */
    private String imageWidth;

    /**
     * Shortened topic length
     */
    private int shortTopicLength;

    /**
     * Highlight author identity
     */
    private Long authorUserId;

    /**
     * Highlight author First names
     */
    private String authorFirstName;

    /**
     * Highlight author Last name
     */
    private String authorLastName;

    /**
     * Highlight last updater identity
     */
    private Long updaterUserId;

    /**
     * Highlight last updater First names
     */
    private String updaterFirstName;

    /**
     * Highlight last updater Last name
     */
    private String updaterLastName;

    /**
     * Shortened topic text
     */
    private String topicSmall;

    /**
     * HighlightInfo instance for Highlight preveiw
     */
    private HighlightInfo previewItem;

    /**
     * list of Highlights
     */
    private List highlightsList;

    /**
     * FormFile instance for Highlight image upload
     */
    protected FormFile photoFile;

    /**
     * Highlight image
     */
    private byte[] image;

    /**
     * content type of image
     */
    private String contentType;

    /**
     * true if Highlight has image(for sayouts Layout_1 and Layout_2),
     * false otherwise(for Layout_3)
     */
    protected boolean haveImage;

    /**
     * true if image sizes were indicated when creating Highlight,
     * false otherwise - in this case, Highligh image is displayed in it's original size
     */
    private boolean haveImageSizes;

    /**
     * true if Highlight item is being previewed,
     * false otherwise
     */
    private boolean preview;

    /**
     * true if Highlights list should be ordered by title property,
     * false otherwise
     */
    private boolean orderByTitle;

    /**
     * true if Highlights list should be ordered by authorFirstName property,
     * false otherwise
     */
    private boolean orderByAuthor;

    /**
     * true if Highlights list should be ordered by creationDate property,
     * false otherwise
     */
    private boolean orderByCreationDate;

    /**
         * Contains information about how Highlights list should be ordered, presicely,
     * by which property(e.g. title,authorFirstName,creationDate) and direction(i.e. ascending or descending)
     */
    private String orderBy;

    /**
     * true if HighlightItemForm should be reseted,
     * false otherwise
     */
    private boolean formReset;

    private int imageSize;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getUpdaterFirstName() {
        return updaterFirstName;
    }

    public void setUpdaterFirstName(String updaterFirstName) {
        this.updaterFirstName = updaterFirstName;
    }

    public String getUpdaterLastName() {
        return updaterLastName;
    }

    public void setUpdaterLastName(String updaterLastName) {
        this.updaterLastName = updaterLastName;
    }

    public int getShortTopicLength() {
        return shortTopicLength;
    }

    public void setShortTopicLength(int shortTopicLength) {
        this.shortTopicLength = shortTopicLength;
    }

    public String getTopicSmall() {
        return topicSmall;
    }

    public void setTopicSmall(String topicSmall) {
        this.topicSmall = topicSmall;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        preview = false;
        previewItem = null;
        highlightsList = null;

        if (formReset) {

            activeHighlightId = null;

            links = new ArrayList();

            layout = null;

            layout1 = false;
            layout2 = false;
            layout3 = false;

            title = null;
            description = null;
            topic = null;
            topicSmall = null;

            creationDate = null;
            updationDate = null;

            imageHeight1 = null;
            imageWidth1 = null;
            imageHeight2 = null;
            imageWidth2 = null;

            shortTopicLength = 0;

            contentType = null;
        }
        super.reset(mapping, request);
    }

    public LinkInfo getLink(int index) {
        LinkInfo linkInfo = null;
        int currentSize = links.size();
        if (index >= currentSize) {
            for (int i = 0; i <= index - currentSize; i++) {
                links.add(new LinkInfo());
            }
        }

        return (LinkInfo) links.get(index);
    }

    public ArrayList getLinks() {
        return links;
    }

    public void setLinks(ArrayList links) {
        this.links = links;
    }

    public Long getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(Long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public Long getUpdaterUserId() {
        return updaterUserId;
    }

    public void setUpdaterUserId(Long updaterUserId) {
        this.updaterUserId = updaterUserId;
    }

    public boolean isHaveImage() {
        return haveImage;
    }

    public void setHaveImage(boolean havePhoto) {
        this.haveImage = havePhoto;
    }

    public FormFile getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(FormFile photoFile) {
        this.photoFile = photoFile;
    }

    public String getImageHeight1() {
        return imageHeight1;
    }

    public void setImageHeight1(String imageHeight1) {
        this.imageHeight1 = imageHeight1;
    }

    public String getImageWidth1() {
        return imageWidth1;
    }

    public void setImageWidth1(String imageWidth1) {
        this.imageWidth1 = imageWidth1;
    }

    public String getImageHeight2() {
        return imageHeight2;
    }

    public void setImageHeight2(String imageHeight2) {
        this.imageHeight2 = imageHeight2;
    }

    public String getImageWidth2() {
        return imageWidth2;
    }

    public void setImageWidth2(String imageWidth2) {
        this.imageWidth2 = imageWidth2;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public boolean isLayout1() {
        return layout1;
    }

    public void setLayout1(boolean layout1) {
        this.layout1 = layout1;
    }

    public boolean isLayout2() {
        return layout2;
    }

    public void setLayout2(boolean layout2) {
        this.layout2 = layout2;
    }

    public boolean isLayout3() {
        return layout3;
    }

    public void setLayout3(boolean layout3) {
        this.layout3 = layout3;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdationDate() {
        return updationDate;
    }

    public void setUpdationDate(String updationDate) {
        this.updationDate = updationDate;
    }

    public Long getActiveHighlightId() {
        return activeHighlightId;
    }

    public void setActiveHighlightId(Long activeHighlightId) {
        this.activeHighlightId = activeHighlightId;
    }

    public HighlightInfo getPreviewItem() {
        return previewItem;
    }

    public void setPreviewItem(HighlightInfo previewItem) {
        this.previewItem = previewItem;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public List getHighlightsList() {
        return highlightsList;
    }

    public void setHighlightsList(List highlightsList) {
        this.highlightsList = highlightsList;
    }

    public boolean isHaveImageSizes() {
        return haveImageSizes;
    }

    public void setHaveImageSizes(boolean haveImageSizes) {
        this.haveImageSizes = haveImageSizes;
    }

    public boolean isOrderByAuthor() {
        return orderByAuthor;
    }

    public void setOrderByAuthor(boolean orderByAuthor) {
        this.orderByAuthor = orderByAuthor;
    }

    public boolean isOrderByCreationDate() {
        return orderByCreationDate;
    }

    public void setOrderByCreationDate(boolean orderByCreationDate) {
        this.orderByCreationDate = orderByCreationDate;
    }

    public boolean isOrderByTitle() {
        return orderByTitle;
    }

    public void setOrderByTitle(boolean orderByTitle) {
        this.orderByTitle = orderByTitle;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isFormReset() {
        return formReset;
    }

    public void setFormReset(boolean formReset) {
        this.formReset = formReset;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        ActionErrors errors = super.validate(actionMapping, httpServletRequest);

        if (errors == null) {
            errors = new ActionErrors();
        }
        if (shortTopicLength == 0) {
            errors.add(null,
                       new ActionError("error.highlights.shortLengthNull"));
        }

        if (links != null && links.size() != 0) {
            Iterator iter = links.iterator();
            while (iter.hasNext()) {
                LinkInfo item = (LinkInfo) iter.next();

                if ( (item.getName() != null &&
                      item.getName().trim().length() != 0) &&
                    ( (item.getUrl() == null) ||
                     (item.getUrl().trim().length() == 0) ||
                     item.getUrl().equals( (String) (httpServletRequest.
                    getScheme() +
                    "://")))) {

                    errors.add(null,
                               new ActionError("error:highlights:urlEmpty"));
                }

                if ( ( (item.getUrl() != null) &&
                      (item.getUrl().trim().length() != 0) &&
                      !item.getUrl().equals( (String) (httpServletRequest.
                    getScheme() +
                    "://")) &&
                      (item.getName() == null ||
                       item.getName().trim().length() == 0))) {

                    errors.add(null,
                               new ActionError("error:highlights:nameEmpty"));
                }
            }
        }

        if (layout == null) {
            errors.add(null,
                       new ActionError("error:highlights:layoutEmpty"));
        }
        else {
            if ( (layout.equals("1") || layout.equals("2"))) {
                if ( (photoFile == null ||
                      (photoFile != null && photoFile.getFileSize() == 0)) &&
                    image == null) {

                    errors.add(null,
                               new ActionError("error:highlights:imgEmpty"));
                }
            }
        }
        //
        if ( (photoFile != null &&
              photoFile.getFileSize() > Highlight.MAX_IMG_SIZE) ||
            imageSize > Highlight.MAX_IMG_SIZE) {
            errors.add(null,
                       new ActionError("error:highlights:imgSizeExceed"));
        }
        return errors.isEmpty() ? null : errors;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }
}