/*
 *   PickupItem.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 24, 2003
 * 	 CVS-ID: $Id: PickupItem.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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
package org.digijava.module.admin.helper.pickup;

import java.util.Collection;

public class PickupItem {

    private Long id;
    private String name;
    private Collection children;
    private boolean expand;
    private boolean expandable;
    private int level;
    private String key;
    private String type;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection getChildren() {
        return children;
    }

    public void setChildren(Collection children) {
        this.children = children;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public boolean isExpand() {
        return expand;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}