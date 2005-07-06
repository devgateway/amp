/*
 *   Image.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 7, 2003
 * 	 CVS-ID:
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
package org.digijava.kernel.entity;

import org.digijava.kernel.Entity;
import java.io.Serializable;
 
public class Image
    extends Entity implements Serializable{

    private String contentType;
    private byte[] image;
    /*
     * this class should be able to store bitstreams for images
     */

    public Image() {
    }

    public Image(String name) {
        super(name);
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}