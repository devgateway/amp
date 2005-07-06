/*
 *   Sdm.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: Sdm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

package org.digijava.module.sdm.dbentity;

import java.util.Date;
import java.util.Collection;
import java.util.Set;
import java.util.*;
import org.digijava.module.common.dbentity.ItemStatus;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Sdm {

    private Long id;
    private String siteId;
    private String instanceId;
    private String name;
    private Set items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set getItems() {
        return items;
    }

    public void setItems(Set items) {
        this.items = items;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public SdmItem getItemByIndex(Long index) {

        Set sdmItems = this.getItems();
        SdmItem  result = null;

        Iterator iter = sdmItems.iterator();
        while(iter.hasNext()) {
            SdmItem item = (SdmItem) iter.next();

            if (item.getParagraphOrder().equals(index)) {
                result = item;
                break;
            }
        }
        return result;
    }
}