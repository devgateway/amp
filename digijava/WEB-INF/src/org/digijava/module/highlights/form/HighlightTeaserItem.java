/*
 *   TeaserItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Jun 17,2004
 *   CVS-ID: $Id: HighlightTeaserItem.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
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
import java.util.Set;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class HighlightTeaserItem {
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
     * Set of Highlight links
     */
    private Set links;

    private ArrayList linksArray;
    /**
     * Highlight layout:
     * 1 - for Leyout_1,
     * 2 - for Leyout_2,
     * 3 - for Leyout_3
     */
    private int layout;

    /**
     * Shortened topic length
     */
    private int shortTopicLength;

    /**
     * Highlight image height
     */
    private int imageHeight;

    /**
     * Highlight image width
     */
    private int imageWidth;

    /**
     * type of image
     */
    private String contentType;

    /**
     * true if Highligh image should be visible,
     * false otherwise
     */
    private boolean showImage;
    /**
     * true if Highligh should be visible for users,(Highlight is Public)
     * false, if
     */

    public HighlightTeaserItem() {

    }

    public HighlightTeaserItem(Long id,
			       String title,
			       String description,
			       String topic,
int shortTopicLength,
			       int layout,
			       int imageWidth,
			       int imageHeight,
			       String contentType,
			       boolean showImage) {

//        links = new HashSet();
	linksArray = new ArrayList();

	this.id = id;
	this.title = title;
	this.description = description;
	this.topic = topic;
	this.shortTopicLength = shortTopicLength;
	this.layout = layout;
	this.imageWidth = imageWidth;
	this.imageHeight = imageHeight;
	this.contentType = contentType;
	this.showImage = showImage;
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

    public String getShortenedTopic() {
	return org.digijava.module.common.util.ModuleUtil.
	     truncateWords(getTopicText(),
			   shortTopicLength);

    }

    public String getTopicText() {
	if (getTopic() != null)
	    return getTopic();

	return null;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Set getLinks() {
	return links;
    }

    public void setLinks(Set links) {
	this.links = links;
    }

    public int getLayout() {
	return layout;
    }

    public void setLayout(int layout) {
	this.layout = layout;
    }

    public int getShortTopicLength() {
	return shortTopicLength;
    }

    public void setShortTopicLength(int shortTopicLength) {
	this.shortTopicLength = shortTopicLength;
    }

    public int getImageHeight() {
	return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
	this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
	return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
	this.imageWidth = imageWidth;
    }

    public boolean isShowImage() {
	return showImage;
    }

    public void setShowImage(boolean showImage) {
	this.showImage = showImage;
    }

    public String getContentType() {
	return contentType;
    }

    public void setContentType(String contentType) {
	this.contentType = contentType;
    }

    public ArrayList getLinksArray() {
	return linksArray;
    }

    public void setLinksArray(ArrayList linksArray) {
	this.linksArray = linksArray;
    }

}