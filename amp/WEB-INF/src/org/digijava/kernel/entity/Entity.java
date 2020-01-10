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

/**
 * This class serves two important purposes.
 * 1. It provides common prororties to many kernel classes such as User, Site, Group, etc.
 * 2. It serves as a base class for building hierarchies of objects
 * This is a hibernate persisted class. Extended classes should be mapped through joined-subclass tag.
 */

import java.security.Principal;
import java.util.Date;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;

public abstract class Entity
    implements Principal {

    @Interchangeable(fieldTitle = "Id")
    @PossibleValueId
    protected Long id;
    protected String name;
    private Entity creationEntity;
    private Date creationDate;
    private String creationIP;
    private Date lastModified;
    private Entity modifyingEntity;
    private String modifyingIP;

    /**
     * Default constructor, needed for Hibernate
     */
    protected Entity() {
        currentDate();

    }

    public Entity(String name) {
        currentDate();
        this.name = name;
    }

    private void currentDate() {
        this.creationDate = new Date();
        this.lastModified = new Date();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public Entity getCreationEntity() {
        return creationEntity;
    }

    public void setCreationEntity(Entity entity) {
        this.creationEntity = entity;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date CreationDate) {
        this.creationDate = CreationDate;
    }

    public String getCreationIP() {
        return creationIP;
    }

    public void setCreationIP(String CreationIP) {
        this.creationIP = CreationIP;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date LastModified) {
        this.lastModified = LastModified;
    }

    public Entity getModifyingEntity() {
        return modifyingEntity;
    }

    public void setModifyingObject(Entity entity) {
        this.modifyingEntity = entity;
    }

    public String getModifyingIP() {
        return modifyingIP;
    }

    public void setModifyingIP(String ModifyingIP) {
        this.modifyingIP = ModifyingIP;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setModifyingEntity(Entity modifyingEntity) {
        this.modifyingEntity = modifyingEntity;
    }

}
