/*
 *   Entity.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created: Jul 2, 2003
 *	 CVS-ID: $Id: Entity.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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
package org.digijava.kernel;

/**
 * This class serves two important purposes.
 * 1. It provides common prororties to many kernet classes such as DgUser, DgSite, DgPermission
 * and DgGroup
 * 2. It serves as a base class for building hierarchies of objects
 *
 * This is a hibernate persisted class. Extended classes should be mapped through joined-subclass tag.
 */

import java.util.Collection;
import java.util.HashSet;
import java.util.Date;
import java.security.Permission;
import java.security.Principal;

public abstract class Entity
    implements Principal {

    private Long id;
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