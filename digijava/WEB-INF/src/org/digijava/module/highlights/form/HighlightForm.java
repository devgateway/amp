/*
 *   HighlightForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Oct 12, 2003
 * 	 CVS-ID: $Id: HighlightForm.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
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
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


public class HighlightForm
    extends ActionForm {

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
         * Highlight image
         */
        private byte[] image;

        /**
         * Highlight creation date
         */
        private String creationDate;

        /**
         * Highlight last updation date
         */
        private String updationDate;

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
         * true if full topic text is shown for active Highlight,
         * false if shortened topic text is shown for active Highlight
         */
        private boolean more;

        /**
         * true if Highligh image should be visible,
         * false otherwise
         */
        private boolean showImage;

        /**
         * true if image sizes were indicated when creating Highlight,
         * false otherwise - in this case, Highligh image is displayed in it's original size
         */
        private boolean haveImageSizes;

        public HighlightInfo() {

        }

        public void setMore(boolean more) {
            this.more = more;
        }

        public boolean getMore() {
            return more;
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

        public void setUpdaterUserId(Long updaterUserId) {
            this.updaterUserId = updaterUserId;
        }

        public Long getUpdaterUserId() {
            return updaterUserId;
        }

        public boolean isShowImage() {
            return showImage;
        }

        public void setShowImage(boolean showImage) {
            this.showImage = showImage;
        }

        public boolean isHaveImageSizes() {
            return haveImageSizes;
        }

        public void setHaveImageSizes(boolean haveImageSizes) {
            this.haveImageSizes = haveImageSizes;
        }
    }

    /**
     * active Highlight identity
     */
    private Long activeHighlightId;

    /**
     * Highlight layout:
     * "1" - for Leyout_1,
     * "2" - for Leyout_2,
     * "3" - for Leyout_3
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
     * HighlightInfo instance for active Highlight
     */
    private HighlightInfo activeHighlight;

    /**
     * true if full topic text is shown for active Highlight,
     * false if shortened topic text is shown for active Highlight
     */
    private boolean more;

    /**
     * true if Highlights archive exists,
     * false otherwise
     */
    private boolean archive;

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public HighlightInfo getActiveHighlight() {
        return activeHighlight;
    }

    public void setActiveHighlight(HighlightInfo activeHighlight) {
        this.activeHighlight = activeHighlight;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        layout = null;

        layout1 = false;
        layout2 = false;
        layout3 = false;

        //     more = false;
        activeHighlight = null;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
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

    public Long getActiveHighlightId() {
        return activeHighlightId;
    }

    public void setActiveHighlightId(Long activeHighlightId) {
        this.activeHighlightId = activeHighlightId;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

}