/*
*   ItemStatus.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id$
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

package org.digijava.module.common.dbentity;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import javax.persistence.*;

@Entity
@Table(name = "DG_ITEM_STATUS")
public class ItemStatus {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "NAME")
    private String name;
    // news status
    @Transient
    public final static String PUBLISHED = "pu";
    @Transient
    public final static String PENDING = "pe";
    @Transient
    public final static String REJECTED = "re";
    @Transient
    public final static String ARCHIVED = "ar";
    @Transient
    public final static String DELETED = "dl";

    // -----------
    // same as publish not used in database
    @Transient
    public final static String REVOKE = "rev";


    public ItemStatus() {
    }

    public ItemStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
