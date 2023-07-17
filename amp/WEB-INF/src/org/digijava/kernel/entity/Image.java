/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.entity;

import javax.persistence.*;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "DG_IMAGE")
public class Image
    extends Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dg_image_seq")
    @SequenceGenerator(name = "dg_image_seq", sequenceName = "dg_image_seq", allocationSize = 1)    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "CREATION_IP")
    private String creationIP;

    @Column(name = "LAST_MODIFIED")
    private Date lastModified;

    @Column(name = "MODIFYING_IP")
    private String modifyingIP;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    @Lob
    @Column(name = "DATA")
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
